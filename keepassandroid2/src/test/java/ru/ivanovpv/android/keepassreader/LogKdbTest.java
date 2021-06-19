package ru.ivanovpv.android.keepassreader;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.linguafranca.pwdb.Credentials;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;
import org.linguafranca.pwdb.Icon;
import org.linguafranca.pwdb.kdb.KdbCredentials;
import org.linguafranca.pwdb.kdb.KdbDatabase;
import org.linguafranca.utils.DataUtils;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

public class LogKdbTest {
    Logger logger = Logger.getAnonymousLogger();
    private Database database;

    @Before
    public void init() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test123.kdb");
        // file has password credentials
        Credentials credentials = new KdbCredentials.Password("123".getBytes());
        database = KdbDatabase.load(credentials, inputStream);
    }

    @Test
    public void logDatabaseTest() {
        logDatabase(database);
    }

    private void logDatabase(Database database) {
        logger.info("Database description="+database.getDescription());
        Group group=database.getRootGroup();
        logGroup(group);
    }

    private void logGroup(Group group) {
        logger.info("=========GROUP============");
        logger.info("Group="+group.getName());
        List<Entry> entries = group.getEntries();
        for(Entry entry:entries) {
            logEntry(entry);
        }
        List<Group> groups = group.getGroups();
        for(Group childGroup:groups) {
            logGroup(childGroup);
        }
    }

    private void logEntry(Entry entry) {
        logger.info("---------entry---------\n");
        logger.info("Entry uuid="+entry.getUuid());
        List<String> properties = entry.getPropertyNames();
        for(String propName:properties) {
            logger.info( "Property name="+propName+", value="+entry.getProperty(propName));
        }
        List<String> binProps = entry.getBinaryPropertyNames();
        for(String propName:binProps) {
            String base64= DataUtils.printBase64Binary(entry.getBinaryProperty(propName));
            logger.info("Binary data="+base64);
        }
        Icon icon=entry.getIcon();
        logger.info("Icon index="+icon.getIndex());
        logger.info("Created="+entry.getCreationTime());
        logger.info("Modified="+entry.getLastModificationTime());
        logger.info("Accessed="+entry.getLastAccessTime());
    }


}
