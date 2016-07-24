package org.linguafranca.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * Created by pivanov on 20.07.2016.
 */

public class DatatypeConverter {

    public static byte[] parseHexBinary(String hexString) {
        if(hexString==null || hexString.trim().length()==0)
            return new byte[0];
        try {
            return Hex.decodeHex(hexString.toCharArray());
        }
        catch(Exception ex) {
            return new byte[0];
        }
    }

    public static byte[] parseBase64Binary(String base64String) {
        return Base64.decodeBase64(base64String.getBytes());
        //org.apache.commons.codec.binary.Hex.decodeHex(key.toCharArray());
        //byte[] encrypted = DatatypeConverter.parseBase64Binary(base64);
    }

    public static String printBase64Binary(byte[] binary) {
        //String base64 = DatatypeConverter.printBase64Binary(encrypted);
        byte[] encoded=Base64.encodeBase64(binary);
        return  new String(encoded);
    }

    public static String printHexBinary(byte[] buffer) {
        //DatatypeConverter.printHexBinary(buffer);
        char[] chars=Hex.encodeHex(buffer);
        return String.valueOf(chars);
    }
}
