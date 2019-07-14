package org.orion.common.miscutil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encrtption {

    private static final Logger logger = LoggerFactory.getLogger(Encrtption.class);

    public static String encryptPassword(String password) {
        String sha256pwd = DigestUtils.sha256Hex(password);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(15);
        return encoder.encode(sha256pwd);
    }

    public static boolean verify(String rawPassword, String encodedPassword) {
        String sha256pwd = DigestUtils.sha256Hex(rawPassword);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(15);
        return encoder.matches(sha256pwd, encodedPassword);
    }

    public static String base64Encode(String value) {
        return Base64.encodeBase64String(value.getBytes());
    }

    public static String base64Decode(String value) {
        return new String(Base64.decodeBase64(value));
    }

    public static String md5EncodeString(String value) {
        return DigestUtils.md5Hex(value);
    }

    public static String md5EncodeData(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    public static boolean checkMD5Matching(byte[] src, byte[] copy) {
        if (src == null)
            return false;
        String srcCode = md5EncodeData(src);
        String copyCode = md5EncodeData(copy);
        if (srcCode.equals(copyCode))
            return true;
        return false;
    }

    public static String encryptDES(String src, String key) {
        try
        {
            Cipher ecipher = Cipher.getInstance("DES");
            byte[] keyByte = new byte[8];
            System.arraycopy(("orion" + key).getBytes("UTF-8"), 0, keyByte, 0, keyByte.length);
            SecretKey secretKey = new SecretKeySpec(keyByte, "DES");
            ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] enc = ecipher.doFinal(src.getBytes("UTF-8"));
            byte[] encodedBytes = Base64.encodeBase64(enc);
            return new String(encodedBytes);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    public static String decryptDES(String encrytped, String key) {
        try
        {
            Cipher dcipher = Cipher.getInstance("DES");
            byte[] keyByte = new byte[8];
            System.arraycopy(("orion" + key).getBytes("UTF-8"), 0, keyByte, 0, keyByte.length);
            SecretKey secretKey = new SecretKeySpec(keyByte, "DES");
            dcipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] utf8 = dcipher.doFinal(Base64.decodeBase64(encrytped.getBytes("UTF-8")));
            return new String(utf8, "UTF8");
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    public static String encryptAES(String src, String key) {
        if (!StringUtil.isEmpty(key) && key.length() >= 16) {
            try {
                byte[] keyByte = ArrayUtil.get(key.getBytes("UTF-8"), 0, 15);
                SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                byte[] encrypted = cipher.doFinal(src.getBytes("UTF-8"));
                return new String(Base64.encodeBase64(encrypted));
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        return null;
    }

    public static String decryptAES(String encrytped, String key) {
        if (!StringUtil.isEmpty(key) && key.length() >= 16) {
            try {
                byte[] keyByte = ArrayUtil.get(key.getBytes("UTF-8"), 0, 15);
                SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                byte[] decrypted = cipher.doFinal(Base64.decodeBase64(encrytped.getBytes("UTF-8")));
                return new String(decrypted, "UTF-8");
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        return null;
    }

}
