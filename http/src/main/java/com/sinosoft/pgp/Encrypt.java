package com.sinosoft.pgp;

import java.io.IOException;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;

public class Encrypt {
    private boolean isArmored;
    private boolean isSigning;
    private boolean checkIntegrity;
    private String publicKeyFilePath;
    private PGPPublicKey publicKey;
    private PGPPrivateKey privateKey;
    private PGPSecretKey secretKey;

    private String privateKeyFilePath;
    private String privateKeyPassword;

    public String getPrivateKeyFilePath() {
        return privateKeyFilePath;
    }

    public void setPrivateKeyFilePath(String privateKeyFilePath) {
        this.privateKeyFilePath = privateKeyFilePath;
    }

    public String getPrivateKeyPassword() {
        return privateKeyPassword;
    }

    public void setPrivateKeyPassword(String privateKeyPassword) {
        this.privateKeyPassword = privateKeyPassword;
    }

    public boolean isSigning() {
        return isSigning;
    }

    public void setSigning(boolean isSigning) {
        this.isSigning = isSigning;
    }

    public String getPublicKeyFilePath() {
        return publicKeyFilePath;
    }

    public void setPublicKeyFilePath(String publicKeyFilePath) throws IOException, PGPException {
        this.publicKeyFilePath = publicKeyFilePath;
        publicKey = BCPGPUtils.readPublicKey(publicKeyFilePath);
    }

    public boolean isArmored() {
        return isArmored;
    }

    public void setArmored(boolean isArmored) {
        this.isArmored = isArmored;
    }

    public boolean isCheckIntegrity() {
        return checkIntegrity;
    }

    public void setCheckIntegrity(boolean checkIntegrity) {
        this.checkIntegrity = checkIntegrity;
    }

    public PGPPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PGPPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PGPPrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PGPPrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PGPSecretKey getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(PGPSecretKey secretKey) {
        this.secretKey = secretKey;
    }

}
