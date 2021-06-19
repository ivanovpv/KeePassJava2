package ru.ivanovpv.android.keepassreader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.linguafranca.pwdb.Credentials;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.kdb.KdbCredentials;
import org.linguafranca.utils.DataUtils;

import java.io.InputStream;

public class CheckKdbPasswordTest {

    @Test
    public void checkPasswordTest1() throws Exception {
        // get an input stream from KDB file
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test123.kdb");
        // file has password credentials
        Credentials credentials = new KdbCredentials.Password("123".getBytes());
        boolean check=DataUtils.checkCredentialsKdb(credentials, inputStream);
        Assert.assertEquals(true, check);
    }

    @Test
    public void checkPasswordTest2() throws Exception {
        // get an input stream from KDB file
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test123.kdb");
        // file has password credentials
        Credentials credentials = new KdbCredentials.Password("1234".getBytes());
        boolean check=DataUtils.checkCredentialsKdb(credentials, inputStream);
        Assert.assertEquals(false, check);
    }
}
