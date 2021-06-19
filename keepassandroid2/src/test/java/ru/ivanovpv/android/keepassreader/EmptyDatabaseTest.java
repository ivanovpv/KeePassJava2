package ru.ivanovpv.android.keepassreader;

import org.junit.Before;
import org.junit.Test;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.kdb.KdbDatabase;

import static org.junit.Assert.assertTrue;

public class EmptyDatabaseTest {
    private Database database;

    @Before
    public void init() {
        database = new KdbDatabase();
    }

    @Test
    public void testEmptyDatabase() {
        assertTrue (database.getRootGroup().getName().equals("Root"));
        assertTrue (database.getRootGroup().getEntries().size() == 0);
        assertTrue (database.getRootGroup().getGroups().size() == 0);
    }
}
