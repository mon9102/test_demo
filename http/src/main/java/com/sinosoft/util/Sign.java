package com.sinosoft.util;

import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.PublicKeyAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.jcajce.*;
import org.bouncycastle.util.io.Streams;

import java.io.*;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;

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

    public static PGPPublicKey readPublicKey(InputStream in) throws IOException, PGPException {
        in = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(in);
        PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(in);

        //
        // we just loop through the collection till we find a key suitable for encryption, in the real
        // world you would probably want to be a bit smarter about this.
        //
        PGPPublicKey key = null;

        //
        // iterate through the key rings.
        //
        Iterator<PGPPublicKeyRing> rIt = pgpPub.getKeyRings();

        while (key == null && rIt.hasNext()) {
            PGPPublicKeyRing kRing = rIt.next();
            Iterator<PGPPublicKey> kIt = kRing.getPublicKeys();
            while (key == null && kIt.hasNext()) {
                PGPPublicKey k = kIt.next();

                if (k.isEncryptionKey()) {
                    key = k;
                }
            }
        }

        if (key == null) {
            throw new IllegalArgumentException("Can't find encryption key in key ring.");
        }

        return key;
    }

    private static PGPPrivateKey findSecretKey(InputStream keyIn, long keyID, char[] pass) throws IOException, PGPException, NoSuchProviderException {
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(org.bouncycastle.openpgp.PGPUtil.getDecoderStream(keyIn));

        PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);

        if (pgpSecKey == null) {
            return null;
        }

        PBESecretKeyDecryptor a = new JcePBESecretKeyDecryptorBuilder(new JcaPGPDigestCalculatorProviderBuilder().setProvider("BC").build()).setProvider("BC").build(pass);

        return pgpSecKey.extractPrivateKey(a);
    }
    private static PGPSecretKey readSecretKey(InputStream input) throws IOException, PGPException {
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(input));

        //
        // we just loop through the collection till we find a key suitable for encryption, in the real
        // world you would probably want to be a bit smarter about this.
        //

        Iterator keyRingIter = pgpSec.getKeyRings();
        while (keyRingIter.hasNext()) {
            PGPSecretKeyRing keyRing = (PGPSecretKeyRing) keyRingIter.next();

            Iterator keyIter = keyRing.getSecretKeys();
            while (keyIter.hasNext()) {
                PGPSecretKey key = (PGPSecretKey) keyIter.next();

                if (key.isSigningKey()) {
                    return key;
                }
            }
        }

        throw new IllegalArgumentException("Can't find signing key in key ring.");
    }
    public static void main(String[] args) throws IOException, PGPException {
//        PgpUtils bl = new PgpUtils();
        String publicKeyPath = "C:\\Users\\wangh\\Desktop\\public.asc";
        String privateKeyPath = "C:\\Users\\wangh\\Desktop\\private.gpg";
        char[] pass = "123456".toCharArray();
        File keyInFile = new File(publicKeyPath);
        FileInputStream keyIn = new FileInputStream(keyInFile);
        File privatekey = new File(privateKeyPath);
        FileInputStream privatekeyIn = new FileInputStream(privatekey);
        PGPSecretKey pgpSecKey = readSecretKey(privatekeyIn);
        PGPPrivateKey pgpPrivKey = pgpSecKey.extractPrivateKey(new JcePBESecretKeyDecryptorBuilder().setProvider(new BouncyCastleProvider()).build(pass));
        byte[] a = "aaa".getBytes();
        byte[] signedEncryptedObject = createSignedEncryptedObject(readPublicKey(keyIn), pgpPrivKey, a);
        System.out.println(signedEncryptedObject.toString());
    }
}
