package ru.ivanovpv.android.keepassreader;

import org.junit.Assert;
import org.junit.Test;
import org.linguafranca.pwdb.Credentials;
import org.linguafranca.pwdb.kdbx.KdbxCredentials;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.dom.DomDatabaseWrapper;
import org.linguafranca.pwdb.kdbx.stream_3_1.KdbxStreamFormat;
import org.linguafranca.utils.DataUtils;

import java.io.IOException;
import java.io.InputStream;

public class CheckKdbxPasswordTest {

    @Test
    public void checkKdbxPassword1() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test123.kdbx");
        Credentials credentials= new KdbxCredentials.Password("123".getBytes());
        boolean check = DataUtils.checkCredentialsKdbx(credentials, inputStream);
        Assert.assertEquals(true, check);
    }

    @Test
    public void checkKdbxPassword2() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test123.kdbx");
        Credentials credentials= new KdbxCredentials.Password("0123".getBytes());
        boolean check = DataUtils.checkCredentialsKdbx(credentials, inputStream);
        Assert.assertEquals(false, check);
    }

    @Test
    public void checkKdbxPasswordNew1() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test123.kdbx");
        Credentials credentials= new KdbxCreds("123".getBytes());
        boolean check = DataUtils.checkCredentialsKdbx(credentials, inputStream);
        Assert.assertEquals(true, check);
    }

    @Test
    public void checkKdbxPasswordNew2() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test123.kdbx");
        Credentials credentials= new KdbxCreds("0123".getBytes());
        boolean check = DataUtils.checkCredentialsKdbx(credentials, inputStream);
        Assert.assertEquals(false, check);
    }

    @Test
    public void inspectKeyfileDatabase() throws IOException {
        InputStream keyFileInputStream = getClass().getClassLoader().getResourceAsStream("KeyFileDatabase.key");
        Credentials credentials = new KdbxCreds("123".getBytes(), keyFileInputStream);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("KeyFileDatabase.kdbx");
        DomDatabaseWrapper database = new DomDatabaseWrapper(new KdbxStreamFormat(), credentials, inputStream);
        //database.save(new StreamFormat.None(), new Credentials.None(), System.out);
        Assert.assertNotNull(database);
    }

    @Test
    public void inspectPasswordDatabase() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test123.kdbx");
        DomDatabaseWrapper database = new DomDatabaseWrapper(new KdbxStreamFormat(), new KdbxCreds("123".getBytes()), inputStream);
        //database.save(new StreamFormat.None(), new Credentials.None(), System.out);
        Assert.assertNotNull(database);
    }
}
