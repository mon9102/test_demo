package com.sinosoft.pgp;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.bc.BcPublicKeyDataDecryptorFactory;
import org.bouncycastle.util.io.Streams;

public class BCPGPDecryptor {

    int compressionAlgorithm;
    int hashAlgorithm;
    int symmetricKeyAlgorithm;

    Decrypt decrypt = new Decrypt();

    private static Provider getProvider() {
        Provider provider = Security.getProvider("BC");
        if (provider == null) {
            provider = new BouncyCastleProvider();
            Security.addProvider(provider);
        }
        return provider;
    }

    public BCPGPDecryptor(Decrypt decrypt) throws IOException, PGPException, NoSuchProviderException {
        this.decrypt = decrypt;
    }

    public void decryptFile(String inputFileNamePath, String outputFileNamePath) throws Exception {
        if (decrypt.isVerify()) {
            decryptandVerifyFile(inputFileNamePath, outputFileNamePath, decrypt.getPublicKeyFilePath(),
                    decrypt.getPrivateKeyFilePath(), decrypt.getPrivateKeyPassword());
        } else {
            decryptFile(inputFileNamePath, outputFileNamePath, decrypt.getPrivateKeyFilePath(),
                    decrypt.getPrivateKeyPassword());
        }
        System.err.println(IOUtils.toString(new FileInputStream(outputFileNamePath)));
    }

    public void decryptFile(String inputFile, String outputFile, String privateKeyFile, String passphrase)
            throws Exception {
        getProvider();
        InputStream fIn = null;
        InputStream in = null;
        InputStream keyIn = null;
        OutputStream out = null;
        try {
            char[] passPhrase = passphrase.toCharArray();
            keyIn = new FileInputStream(privateKeyFile);
            fIn = new FileInputStream(inputFile);
            out = new FileOutputStream(outputFile);
            in = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(fIn);

            PGPObjectFactory pgpF = new PGPObjectFactory(in, new BcKeyFingerprintCalculator());
            PGPEncryptedDataList enc = null;
            Object o = pgpF.nextObject();

            if (o instanceof PGPEncryptedDataList) {
                enc = (PGPEncryptedDataList) o;
            } else {
                enc = (PGPEncryptedDataList) pgpF.nextObject();
            }

            Iterator<PGPPublicKeyEncryptedData> it = enc.getEncryptedDataObjects();
            PGPPrivateKey sKey = null;
            PGPPublicKeyEncryptedData pbe = null;

            while (sKey == null && it.hasNext()) {
                pbe = it.next();
                sKey = BCPGPUtils.findPrivateKey(keyIn, pbe.getKeyID(), passPhrase);
            }

            System.out
                    .println("<<----------Decrypting the input payload using private key---------->>" + privateKeyFile);

            InputStream clear = pbe.getDataStream(new BcPublicKeyDataDecryptorFactory(sKey));
            this.symmetricKeyAlgorithm = pbe.getSymmetricAlgorithm(new BcPublicKeyDataDecryptorFactory(sKey));

            PGPObjectFactory pgpFact = new PGPObjectFactory(clear, new BcKeyFingerprintCalculator());

            o = pgpFact.nextObject();
            if (o instanceof PGPCompressedData) {
                PGPCompressedData cData = (PGPCompressedData) o;
                pgpFact = new PGPObjectFactory(cData.getDataStream(), new BcKeyFingerprintCalculator());
                o = pgpFact.nextObject();
                this.compressionAlgorithm = cData.getAlgorithm();
            }

            if (o instanceof PGPLiteralData) {
                PGPLiteralData ld = (PGPLiteralData) o;
                InputStream unc = ld.getInputStream();
                OutputStream fOut = new BufferedOutputStream(new FileOutputStream(outputFile));
                Streams.pipeAll(unc, fOut);
                fOut.close();
            }

            if (pbe.isIntegrityProtected()) {
                if (!pbe.verify()) {
                    System.err.println("<-----message failed integrity check------->");
                } else {
                    System.out.println("<-----message integrity check passed------->");
                }
            } else {
                System.out.println("<--------no message integrity check------>");
            }


        } finally {
            try {
                fIn.close();
            } catch (Exception e) {
            }
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
            try {
                keyIn.close();
            } catch (Exception e) {
            }
        }
    }

    public void decryptandVerifyFile(String inputFile, String outFile, String publicKeyFile, String privateKeyFile,
                                     String passPhrase) {
        getProvider();
        InputStream input = null;
        InputStream verifyKeyInput = null;
        OutputStream output = null;
        InputStream decryptKeyInput = null;
        char[] passwd = null;
        try {
            passwd = passPhrase.toCharArray();
            input = PGPUtil.getDecoderStream(new FileInputStream(inputFile));
            verifyKeyInput = new FileInputStream(privateKeyFile);
            output = new FileOutputStream(outFile);
            decryptKeyInput = new FileInputStream(publicKeyFile);

            PGPObjectFactory pgpF = new PGPObjectFactory(input, new BcKeyFingerprintCalculator());
            PGPEncryptedDataList enc;

            Object o = pgpF.nextObject();

            if (o instanceof PGPEncryptedDataList) {
                enc = (PGPEncryptedDataList) o;
            } else {
                enc = (PGPEncryptedDataList) pgpF.nextObject();
            }

            Iterator<?> it = enc.getEncryptedDataObjects();
            PGPPrivateKey sKey = null;
            PGPPublicKeyEncryptedData pbe = null;
            PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(verifyKeyInput),
                    new BcKeyFingerprintCalculator());

            while (sKey == null && it.hasNext()) {
                pbe = (PGPPublicKeyEncryptedData) it.next();
                sKey = BCPGPUtils.findSecretKey(pgpSec, pbe.getKeyID(), passwd);
            }

            if (sKey == null) {
                throw new IllegalArgumentException("secret key for message not found.");
            }

            System.out
                    .println("<<----------Decrypting the input payload using private key---------->>" + privateKeyFile);

            InputStream clear = pbe.getDataStream(new BcPublicKeyDataDecryptorFactory(sKey));
            this.symmetricKeyAlgorithm = pbe.getSymmetricAlgorithm(new BcPublicKeyDataDecryptorFactory(sKey));

            PGPObjectFactory plainFact = new PGPObjectFactory(clear, new BcKeyFingerprintCalculator());

            Object message = null;

            PGPOnePassSignatureList onePassSignatureList = null;
            PGPSignatureList signatureList = null;
            PGPCompressedData compressedData = null;

            message = plainFact.nextObject();
            ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();

            while (message != null) {
                if (message instanceof PGPCompressedData) {
                    System.out.println("<<----------Decrypting the compressed payload---------->>");
                    compressedData = (PGPCompressedData) message;
                    plainFact = new PGPObjectFactory(compressedData.getDataStream(), new BcKeyFingerprintCalculator());
                    message = plainFact.nextObject();
                    this.compressionAlgorithm = compressedData.getAlgorithm();
                }

                if (message instanceof PGPLiteralData) {
                    Streams.pipeAll(((PGPLiteralData) message).getInputStream(), actualOutput);
                } else if (message instanceof PGPOnePassSignatureList) {
                    onePassSignatureList = (PGPOnePassSignatureList) message;
                } else if (message instanceof PGPSignatureList) {
                    signatureList = (PGPSignatureList) message;
                } else {
                    System.err.println("<----message unknown message type---->");
                }
                message = plainFact.nextObject();
            }
            actualOutput.close();
            PGPPublicKey publicKey = null;
            byte[] outputBytes = actualOutput.toByteArray();
            if (onePassSignatureList == null || signatureList == null) {
                throw new PGPException("Poor PGP. Signatures not found.");
            } else {
                for (int i = 0; i < onePassSignatureList.size(); i++) {
                    PGPOnePassSignature ops = onePassSignatureList.get(0);
                    PGPPublicKeyRingCollection pgpRing = new PGPPublicKeyRingCollection(
                            PGPUtil.getDecoderStream(decryptKeyInput), new BcKeyFingerprintCalculator());
                    publicKey = pgpRing.getPublicKey(ops.getKeyID());
                    if (publicKey != null) {
                        ops.init(new BcPGPContentVerifierBuilderProvider(), publicKey);
                        ops.update(outputBytes);
                        PGPSignature signature = signatureList.get(i);
                        if (ops.verify(signature)) {
                            System.out
                                    .println("<<----------Verifying the onepass signature using public key---------->>"
                                            + decrypt.getPublicKeyFilePath());
                            this.hashAlgorithm = ops != null ? ops.getHashAlgorithm() : 0;
                            Iterator<?> userIds = publicKey.getUserIDs();
                            while (userIds.hasNext()) {
                                String userId = (String) userIds.next();
                            }
                            System.err.println("<------Signature verified------>");
                        } else {
                            System.err.println("<-------Signature verification failed------>");
                        }
                    }
                }

            }

            if (pbe.isIntegrityProtected() && !pbe.verify()) {
                System.err.println("Data is integrity protected but integrity is lost.");
            } else if (publicKey == null) {
                System.err.println("Signature not found");
            }

            output.write(outputBytes);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
            try {
                output.close();
            } catch (Exception e) {
            }
            try {
                verifyKeyInput.close();
            } catch (Exception e) {
            }
            try {
                decryptKeyInput.close();
            } catch (Exception e) {
            }
        }
    }
}