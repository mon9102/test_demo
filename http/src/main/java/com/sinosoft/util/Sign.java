package com.sinosoft.util;

import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.PublicKeyAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.jcajce.*;
import org.bouncycastle.util.io.Streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Date;

public class Sign {
    public static byte[] createSignedObject(int signingAlg, PGPPrivateKey signingKey, byte[] data)
            throws PGPException, IOException
    {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        BCPGOutputStream bcOut = new BCPGOutputStream(bOut);
        PGPSignatureGenerator sGen = new PGPSignatureGenerator(
                new JcaPGPContentSignerBuilder(signingAlg, PGPUtil.SHA384).setProvider("BCFIPS"));
        sGen.init(PGPSignature.BINARY_DOCUMENT, signingKey);

        sGen.generateOnePassVersion(false).encode(bcOut);
        PGPLiteralDataGenerator lGen = new PGPLiteralDataGenerator();
        OutputStream lOut = lGen.open(
                bcOut,
                PGPLiteralData.BINARY,
                "_CONSOLE",
                data.length,
                new Date());
        for (int i = 0; i != data.length; i++)
        {
            lOut.write(data[i]);
            sGen.update(data[i]);
        }
        lGen.close();
        sGen.generate().encode(bcOut);
        return bOut.toByteArray();
    }

    public static boolean verifySignedObject(PGPPublicKey verifyingKey, byte[] pgpSignedData)
            throws PGPException, IOException
    {
        JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpSignedData);
        PGPOnePassSignatureList onePassList = (PGPOnePassSignatureList)pgpFact.nextObject();
        PGPOnePassSignature ops = onePassList.get(0);
        PGPLiteralData literalData = (PGPLiteralData)pgpFact.nextObject();
        InputStream dIn = literalData.getInputStream();
        ops.init(new JcaPGPContentVerifierBuilderProvider().setProvider("BCFIPS"), verifyingKey);
        int ch;
        while ((ch = dIn.read()) >= 0)
        {
            ops.update((byte)ch);
        }
        PGPSignatureList sigList = (PGPSignatureList)pgpFact.nextObject();
        PGPSignature sig = sigList.get(0);
        return ops.verify(sig);
    }

    public static byte[] createSignedEncryptedObject(
            PGPPublicKey encryptionKey, PGPPrivateKey signingKey, byte[] data)
            throws PGPException, IOException
    {
        byte[] plainText = Sign.createSignedObject(PublicKeyAlgorithmTags.ECDSA, signingKey, data);
        ByteArrayOutputStream encOut = new ByteArrayOutputStream();
        PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256)
                        .setWithIntegrityPacket(true)
                        .setSecureRandom(new SecureRandom())
                        .setProvider("BCFIPS"));
        encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encryptionKey)
                .setProvider("BCFIPS"));
        OutputStream cOut = encGen.open(encOut, plainText.length);
        cOut.write(plainText);
        cOut.close();
        return encOut.toByteArray();
    }
    public static boolean verifySignedEncryptedObject(
            PGPPrivateKey decryptionKey, PGPPublicKey verificationKey, byte[] pgpEncryptedData)
            throws PGPException, IOException
    {
        PGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpEncryptedData);
        PGPEncryptedDataList encList = (PGPEncryptedDataList)pgpFact.nextObject();
        PGPPublicKeyEncryptedData encData = (PGPPublicKeyEncryptedData)encList.get(0);
        PublicKeyDataDecryptorFactory dataDecryptorFactory =
                new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BCFIPS").build(decryptionKey);

        InputStream clear = encData.getDataStream(dataDecryptorFactory);
        byte[] signedData = Streams.readAll(clear);
        if (encData.verify())
        {
            return Sign.verifySignedObject(verificationKey, signedData);
        }
        throw new IllegalStateException("modification check failed");
    }


}
