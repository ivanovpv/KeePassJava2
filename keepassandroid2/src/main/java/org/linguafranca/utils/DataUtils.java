package org.linguafranca.utils;

import android.util.Log;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.linguafranca.pwdb.Credentials;
import org.linguafranca.pwdb.kdb.KdbHeader;
import org.linguafranca.pwdb.kdb.KdbSerializer;
import org.linguafranca.pwdb.kdbx.stream_3_1.KdbxHeader;
import org.linguafranca.pwdb.kdbx.stream_3_1.KdbxSerializer;
import org.linguafranca.pwdb.kdbx.stream_3_1.KdbxStreamFormat;

import java.io.InputStream;

/**
 * Created by pivanov on 20.07.2016.
 */

public class DataUtils {

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

    public static boolean checkCredentialsKdbx (Credentials credentials, InputStream encryptedInputStream) {
        KdbxHeader kdbxHeader = new KdbxHeader();
        try {
            InputStream decryptedInputStream = KdbxSerializer.createUnencryptedInputStream(credentials,
                    kdbxHeader, encryptedInputStream);
            decryptedInputStream.close();
        }
        catch(Exception ex) {
            return false;
        }
        return true;
    }

    public static boolean checkCredentialsKdb(Credentials credentials, InputStream inputStream) {
        try {
            KdbSerializer.createKdbDatabase(credentials, new KdbHeader(), inputStream);
        } catch(Throwable th) {
            return false;
        }
        return true;
    }

}
