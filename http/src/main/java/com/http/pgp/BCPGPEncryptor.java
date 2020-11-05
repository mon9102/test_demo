package com.http.pgp;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

public class BCPGPEncryptor {

    private static Provider getProvider() {
        Provider provider = Security.getProvider("BC");
        if (provider == null) {
            provider = new BouncyCastleProvider();
            Security.addProvider(provider);
        }
        return provider;
    }

    Encrypt encrypt = new Encrypt();

    public BCPGPEncryptor(Encrypt encrypt) throws IOException, PGPException, NoSuchProviderException {
        encrypt.setPublicKey(BCPGPUtils.readPublicKey(encrypt.getPublicKeyFilePath()));

        PGPSecretKey secretKey = BCPGPUtils.readSecretKey(encrypt.getPrivateKeyFilePath());
        encrypt.setSecretKey(secretKey);

        PGPPrivateKey privateKey = secretKey
                .extractPrivateKey(new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider())
                        .build(encrypt.getPrivateKeyPassword().toCharArray()));
        encrypt.setPrivateKey(privateKey);
        this.encrypt = encrypt;
    }

    public void encryptFile(String inputFileNamePath, String outputFileNamePath) throws Exception {
        if (encrypt.isSigning()) {
            encryptAndSignFile(inputFileNamePath, outputFileNamePath, encrypt.getPublicKeyFilePath(),
                    encrypt.getPrivateKeyFilePath(), encrypt.getPrivateKeyPassword());
        } else {
            encryptFile(inputFileNamePath, outputFileNamePath, encrypt.getPublicKeyFilePath(), encrypt.isArmored(),
                    encrypt.isCheckIntegrity());
        }
        System.err.println(IOUtils.toString(new FileInputStream(outputFileNamePath)));
    }

    public void encryptFile(String inputFile, String outputFile, String publicKeyFile, boolean armor,
                            boolean withIntegrityCheck) throws IOException, NoSuchProviderException, PGPException {
        getProvider();

        OutputStream out = null;
        InputStream keyStream = null;
        OutputStream cOut = null;
        try {
            keyStream = new FileInputStream(publicKeyFile);
            PGPPublicKey pubKey = BCPGPUtils.readPublicKey(keyStream);
            out = new FileOutputStream(outputFile);
            if (armor) {
                out = new ArmoredOutputStream(out);
            }

            System.out.println(
                    "<<----------Encrypting the input file using public key---------->>" + encrypt.getPublicKeyFilePath());

            System.out.println("<<----------Compressing the encrypted data---------->>");

            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(CompressionAlgorithmTags.ZIP);
            PGPUtil.writeFileToLiteralData(comData.open(bOut), PGPLiteralData.BINARY, new File(inputFile));
            comData.close();

            byte[] compressedData = bOut.toByteArray();
            JcePGPDataEncryptorBuilder dataEncryptor = new JcePGPDataEncryptorBuilder(PGPEncryptedData.AES_256);
            dataEncryptor.setWithIntegrityPacket(withIntegrityCheck);
            dataEncryptor.setSecureRandom(new SecureRandom());

            PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(dataEncryptor);
            encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(pubKey));
            cOut = encGen.open(out, compressedData.length);
            cOut.write(compressedData);
            cOut.close();

            if (armor) {
                out.close();
            }

        } finally {
            try {
                cOut.close();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
            try {
                keyStream.close();
            } catch (Exception e) {
            }
        }
    }

    public void encryptAndSignFile(String inputFile, String outputFile, String publicKeyFile, String privateKeyFile,
                                   String passPhrase) {
        getProvider();
        InputStream input = null;
        OutputStream output = null;
        InputStream encryptKeyInput = null;
        File fileOutput = null;
        try {
            input = new FileInputStream(inputFile);
            fileOutput = new File(outputFile);
            output = new FileOutputStream(fileOutput);
            encryptKeyInput = new FileInputStream(publicKeyFile);

            int DEFAULT_BUFFER_SIZE = 16 * 1024;

            System.out.println(
                    "<<----------Encrypting the input file using public key---------->>" + encrypt.getPublicKeyFilePath());

            PGPSecretKey pgpSec = BCPGPUtils.readSecretKey(privateKeyFile);
            PGPPrivateKey signingKey = encrypt.getPrivateKey();

            String userid = (String) pgpSec.getPublicKey().getUserIDs().next();

            BcPGPDataEncryptorBuilder dataEncryptor = new BcPGPDataEncryptorBuilder(PGPEncryptedData.AES_256);
            dataEncryptor.setWithIntegrityPacket(encrypt.isCheckIntegrity());
            dataEncryptor.setSecureRandom(new SecureRandom());

            PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(dataEncryptor);
            encryptedDataGenerator.addMethod((new BcPublicKeyKeyEncryptionMethodGenerator(encrypt.getPublicKey())));

            OutputStream finalOut = new BufferedOutputStream(new ArmoredOutputStream(output), DEFAULT_BUFFER_SIZE);
            OutputStream encOut = encryptedDataGenerator.open(finalOut, new byte[DEFAULT_BUFFER_SIZE]);

            PGPCompressedDataGenerator compressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
            OutputStream compressedOut = new BufferedOutputStream(compressedDataGenerator.open(encOut));

            System.out.println("<<----------Generating OnePass Signature for signing using private Key---------->>"
                    + encrypt.getPrivateKeyFilePath());

            PGPSignatureGenerator signatureGenerator = new PGPSignatureGenerator(new BcPGPContentSignerBuilder(
                    encrypt.getSecretKey().getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA256));
            signatureGenerator.init(PGPSignature.BINARY_DOCUMENT, signingKey);

            PGPSignatureSubpacketGenerator subpacketGenerator = new PGPSignatureSubpacketGenerator();
            subpacketGenerator.setSignerUserID(false, userid);
            signatureGenerator.setHashedSubpackets(subpacketGenerator.generate());

            PGPOnePassSignature onePassSignature = signatureGenerator.generateOnePassVersion(false);
            onePassSignature.encode(compressedOut);

            System.out.println("<<----------Compressing the encrypted data---------->>");

            PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator(true);
            OutputStream literalOut = literalDataGenerator.open(compressedOut, PGPLiteralData.BINARY,
                    fileOutput.getName(), new Date(), new byte[1 << 16]);

            System.out.println("<<----------Signing the message after encryption---------->>");
            byte[] buffer = new byte[1 << 16];

            int bytesRead = 0;
            while ((bytesRead = input.read(buffer)) != -1) {
                literalOut.write(buffer, 0, bytesRead);
                signatureGenerator.update(buffer, 0, bytesRead);
                literalOut.flush();
            }
            // Close Literal data stream and add signature
            literalOut.close();
            literalDataGenerator.close();
            signatureGenerator.generate().encode(compressedOut);
            // Close all other streams
            compressedOut.close();
            compressedDataGenerator.close();
            encOut.close();
            encryptedDataGenerator.close();
            finalOut.close();

            input.close();
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
                encryptKeyInput.close();
            } catch (Exception e) {
            }
            System.out.println("<<----------Generated Encrypted and Signed Message successfully---------->>");
        }
    }
}
