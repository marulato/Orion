package org.orion.common.miscutil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.orion.common.validation.Token;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class Encrtption {

    private static GCMParameterSpec parameterSpec;

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

    public static String sha256(byte[] data) {
        if (ArrayUtil.isEmpty(data)) {
            return null;
        }
        return DigestUtils.sha256Hex(data);
    }

    public static String sha1(byte[] data) {
        if (ArrayUtil.isEmpty(data)) {
            return null;
        }
        return DigestUtils.sha1Hex(data);
    }

    public static String encryptDES(String src, String key) throws Exception {
            Cipher ecipher = Cipher.getInstance("DES");
            byte[] keyByte = new byte[8];
            System.arraycopy(("orion" + key).getBytes(StandardCharsets.UTF_8), 0, keyByte, 0, keyByte.length);
            SecretKey secretKey = new SecretKeySpec(keyByte, "DES");
            ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] enc = ecipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
            byte[] encodedBytes = Base64.encodeBase64(enc);
            return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    public static String decryptDES(String encrytped, String key) throws Exception {
            Cipher dcipher = Cipher.getInstance("DES");
            byte[] keyByte = new byte[8];
            System.arraycopy(("orion" + key).getBytes(StandardCharsets.UTF_8), 0, keyByte, 0, keyByte.length);
            SecretKey secretKey = new SecretKeySpec(keyByte, "DES");
            dcipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] utf8 = dcipher.doFinal(Base64.decodeBase64(encrytped.getBytes(StandardCharsets.UTF_8)));
            return new String(utf8, StandardCharsets.UTF_8);
    }

    public static String encryptAES(String src, String key, boolean padding) throws Exception {
        if (!StringUtil.isEmpty(key) && key.length() >= 16) {
            if (parameterSpec == null) {
                SecureRandom random = new SecureRandom();
                byte[] iv = new byte[12];
                random.nextBytes(iv);
                parameterSpec = new GCMParameterSpec(128, iv);
            }
            byte[] keyByte = ArrayUtil.get(key.getBytes(StandardCharsets.UTF_8), 0, 15);
            SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
            Cipher cipher = null;
            if (padding) {
                cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            } else {
                cipher = Cipher.getInstance("AES/GCM/NoPadding");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, parameterSpec);
            }
            byte[] encrypted = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.encodeBase64(encrypted), StandardCharsets.UTF_8);
        }
        return null;
    }

    public static String decryptAES(String encrytped, String key, boolean padding) throws Exception {
        if (!StringUtil.isEmpty(key) && key.length() >= 16) {
                byte[] keyByte = ArrayUtil.get(key.getBytes(StandardCharsets.UTF_8), 0, 15);
                SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
                Cipher cipher = null;
                if (padding) {
                    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                } else {
                    cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    cipher.init(Cipher.DECRYPT_MODE, skeySpec, parameterSpec);
                }
                byte[] decrypted = cipher.doFinal(Base64.decodeBase64(encrytped.getBytes(StandardCharsets.UTF_8)));
                return new String(decrypted, StandardCharsets.UTF_8);
        }
        return null;
    }

    public static String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        return new String(Base64.encodeBase64(key), StandardCharsets.UTF_8);
    }

    public static boolean validateToken(String token) {
        if (!StringUtil.isEmpty(token) && token.contains("%")) {
            token = URLDecoder.decode(token, StandardCharsets.UTF_8);
        }
        if (Token.getToken().equals(token)) {
            return DateUtil.isBetween(DateUtil.now(), Token.getGenerateDate(), Token.getInvalidDate());
        }
        return false;
    }

}
