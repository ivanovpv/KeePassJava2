package ru.ivanovpv.keepassreader;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;
import org.linguafranca.pwdb.Icon;
import org.linguafranca.pwdb.kdb.KdbCredentials;
import org.linguafranca.pwdb.kdb.KdbDatabase;
import org.linguafranca.pwdb.kdbx.KdbxCredentials;
import org.linguafranca.pwdb.kdbx.dom.DomDatabaseWrapper;
import org.linguafranca.security.Credentials;
import org.linguafranca.utils.DatatypeConverter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pivanov on 20.07.2016.
 */

public class ApplicationTest extends ApplicationTestCase<Application> {

    private final static String TAG="Test: "+ApplicationTest.class.getSimpleName();
    Application application;

    public ApplicationTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();
    }

    public void testKbdx() throws IOException {
        InputStream inputStream = application.getAssets().open("test123.kdbx");
        Credentials credentials = new KdbxCredentials.Password("123".getBytes());
        Database database = DomDatabaseWrapper.load(credentials, inputStream);
        assertNotNull(database);
        parseDatabase(database);
        inputStream.close();
    }

    public void testKbd() throws IOException {
        InputStream inputStream = application.getAssets().open("test123.kdb");
        Credentials credentials = new KdbCredentials.Password("123".getBytes());
        Database database = KdbDatabase.load(credentials, inputStream);
        assertNotNull(database);
        parseDatabase(database);
        inputStream.close();
    }

    private void parseDatabase(Database database) {
        Log.i(TAG, "======================");
        Log.i(TAG, "Database description="+database.getDescription());
        Group group=database.getRootGroup();
        parseGroup(group);
    }

    private void parseGroup(Group group) {
        Log.i(TAG, "--------------------");
        Log.i(TAG, "Group="+group.getName());
        Log.i(TAG, "Path="+group.getPath()+", UUID="+group.getUuid());
        for(Entry entry:group.getEntries()) {
            parseEntry(entry);
        }
        for(Group childGroup:group.getGroups()) {
            parseGroup(childGroup);
        }
    }

    private void parseEntry(Entry entry) {
        Log.i(TAG, "-->");
        Log.i(TAG, "Entry uuid="+entry.getUuid());
        for(String propName:entry.getPropertyNames()) {
            Log.i(TAG, "-->property name="+propName+", value="+entry.getProperty(propName));
        }
        Icon icon=entry.getIcon();
        Log.i(TAG, "-->icon index="+icon.getIndex());
        String base64= DatatypeConverter.printBase64Binary(entry.getBinaryData());
        Log.i(TAG, "-->binary data="+base64);
        Log.i(TAG, "-->created="+entry.getCreationTime().toString());
        Log.i(TAG, "-->modified="+entry.getLastModificationTime().toString());
        Log.i(TAG, "-->accessed="+entry.getLastAccessTime().toString());
    }




}