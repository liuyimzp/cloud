package com.yinhai.cloud.core.api.util;

import com.yinhai.cloud.core.api.exception.DataEncryptRuntimeException;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by pengwei on 2018/8/9.
 */
public class DataEncrypt {

    private final static String AES = "AES";
    private final static String AES_PADDING = "AES/ECB/PKCS5Padding";
    private final static String CHARSET = "utf-8";
    private final static String KEY = "2yh1108232851286";


    /**
     * AES 加密
     *
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc) {

        byte[] encrypted;
        try {
            byte[] raw = KEY.getBytes(CHARSET);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
            Cipher cipher = Cipher.getInstance(AES_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encrypted = cipher.doFinal(sSrc.getBytes(CHARSET));
        } catch (Exception e) {
            throw new DataEncryptRuntimeException(e.getMessage());
        }
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return new BASE64Encoder().encode(encrypted);
    }

    public static String decryptBasedDes(String sSrc) {

        String originalString ;
        try {
            byte[] raw = KEY.getBytes(CHARSET);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
            Cipher cipher = Cipher.getInstance(AES_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //先用base64转码
            byte[] encrypted1 = new sun.misc.BASE64Decoder().decodeBuffer(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            originalString = new String(original, CHARSET);
        } catch (Exception e) {
            return sSrc;
        }
        return originalString;

    }


}

