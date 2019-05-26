package org.orion.common.miscutil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encrtption {

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

    public String base64Encode(String value) {
        return Base64.encodeBase64String(value.getBytes());
    }

    public String base64Decode(String value) {
        return new String(Base64.decodeBase64(value));
    }

    public String md5EncodeString(String value) {
        return DigestUtils.md5Hex(value);
    }

    public String md5EncodeData(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    public boolean checkMD5Matching(byte[] src, byte[] copy) {
        if (src == null)
            return false;
        String srcCode = md5EncodeData(src);
        String copyCode = md5EncodeData(copy);
        if (srcCode.equals(copyCode))
            return true;
        return false;
    }

}
