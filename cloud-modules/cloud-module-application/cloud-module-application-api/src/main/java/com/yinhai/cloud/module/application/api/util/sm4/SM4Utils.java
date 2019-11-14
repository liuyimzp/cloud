package com.yinhai.cloud.module.application.api.util.sm4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SM4Utils {

    private  String secretKey = "AB78yuu8OP9023OP";

    private String iv = "UISwD9fW6cFh9SNS";

    private boolean hexString = false;

    private static final Logger logger = LoggerFactory.getLogger(SM4Utils.class);

    public SM4Utils() {
        super();
    }

    public String encryptData_ECB(String plainText) {
        try {
            SM4Context ctx = new SM4Context();
            ctx.setPadding(true);
            ctx.setMode(SM4.SM4_ENCRYPT);

            byte[] keyBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes("UTF-8");
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes("UTF-8"));
            String cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            logger.error(logger.getName() + "context",e);
            return null;
        }
    }

    public String decryptData_ECB(String cipherText) {
        try {
            SM4Context ctx = new SM4Context();
            ctx.setPadding(true);
            ctx.setMode(SM4.SM4_DECRYPT);

            byte[] keyBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes("UTF-8");
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, new BASE64Decoder().decodeBuffer(cipherText));
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            logger.error(logger.getName() + "context",e);
            return null;
        }
    }

    public String encryptData_CBC(String plainText) {
        try {
            SM4Context ctx = new SM4Context();
            ctx.setPadding(true);
            ctx.setMode(SM4.SM4_ENCRYPT);

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
                ivBytes = Util.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes("UTF-8");
                ivBytes = iv.getBytes("UTF-8");
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes("UTF-8"));
            String cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            logger.error(logger.getName() + "context",e);
            return null;
        }
    }

    public String decryptData_CBC(String cipherText) {
        try {
            SM4Context ctx = new SM4Context();
            ctx.setPadding(true);
            ctx.setMode(SM4.SM4_DECRYPT);

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
                ivBytes = Util.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes("UTF-8");
                ivBytes = iv.getBytes("UTF-8");
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, new BASE64Decoder().decodeBuffer(cipherText));
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            logger.error(logger.getName() + "context",e);
            return null;
        }
    }

    public static String encrypt(String s) {
        SM4Utils sm4 = new SM4Utils();
        return sm4.encryptData_CBC(s);
    }

    public static String decrypt(String s) {
        SM4Utils sm4 = new SM4Utils();
        return sm4.decryptData_CBC(s);
    }

}
