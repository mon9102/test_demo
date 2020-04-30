package com.http.pgp;

public class AlgorithmUtils {
    public AlgorithmUtils() {
    }

    public static String getHashAlgorithm(int alg) {
        switch(alg) {
            case 1:
                return "MD5";
            case 2:
                return "SHA1";
            case 3:
                return "RIPEMD160";
            case 4:
                return "DOUBLE_SHA";
            case 5:
                return "MD2";
            case 6:
                return "TIGER_192";
            case 7:
                return "HAVAL_5_160";
            case 8:
                return "SHA256";
            case 9:
                return "SHA384";
            case 10:
                return "SHA512";
            case 11:
                return "SHA224";
            default:
                return "Unknown Hash Algorithm";
        }
    }

    public static String getSymmetricKeyAlgorithm(int alg) {
        switch(alg) {
            case 1:
                return "IDEA";
            case 2:
                return "TRIPLE_DES";
            case 3:
                return "CAST5";
            case 4:
                return "BLOWFISH";
            case 5:
                return "SAFER";
            case 6:
                return "DES";
            case 7:
                return "AES_128";
            case 8:
                return "AES_192";
            case 9:
                return "AES_256";
            case 10:
                return "TWOFISH";
            case 11:
                return "CAMELLIA_128";
            case 12:
                return "CAMELLIA_192";
            case 13:
                return "CAMELLIA_256";
            default:
                return "Unknown Symmetric Key Algorithm";
        }
    }

    public static String getCompressionAlgorithm(int alg) {
        switch(alg) {
            case 0:
                return "UNCOMPRESSED";
            case 1:
                return "ZIP";
            case 2:
                return "ZLIB";
            case 3:
                return "BZIP2";
            default:
                return "Unknown Compression Algorithm";
        }
    }
}
