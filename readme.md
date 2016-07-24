# KeePassAndroid2

Android compatible library for reading/writing databases compatible with the renowned [KeePass](http://keepass.info) password
safe for Windows. 

Forked from @jorabin's [KeePassJava2](https://github.com/jorabin/KeePassJava2) repo

## changes to original KeePassJava2
- BouncyCastle -> SpongyCastle
- XML's `javax.xml.bind.DatatypeConverter` references to `utils/DatatypeConverter.java`
- Sample app which parses database
- Android instrumentation unit test
- Utilities to check database integrity and/or password correctness

Features to date:

- Read and write KeePass 2.x format
- Keepass 2.x Password and Keyfile Credentials
- Read KeePass 1.x format (Rijndael/AES only)
- *No* requirement for JCE Policy Files
- Interfaces for Database, Group and Entry allow compatible addition of other formats

## Quick Start

The class Javadoc on Interface classes Database, Group and Entry describe
how to use the methods of those classes to create and modifty entries.

### Check database password and/or integrity
        // get an input stream
        InputStream inputStream = new FileInputStream("test123.kdbx");
        // password credentials
        Credentials credentials = new KdbxCredentials.Password("123".getBytes());
        // check credentials for KDBX database
        boolean isCorrect = DomDatabaseWrapper.checkCredentials(credentials, inputStream);
        //in case of KDB database you should call
        KdbDatabase.checkCredentials(credentials, inputStream);

Before further usage `InputStream` need to be `reset()` or if stream doesn't support resetting
it has to be opened again


### Load KDBX Database

        Database database = DomDatabaseWrapper.load(credentials, inputStream);

### Load KDB Database

        Database database = KdbDatabase.load(credentials, inputStream);

### Save KDBX Database
        // create an empty database
        DomDatabaseWrapper database = new DomDatabaseWrapper();

        // add some groups and entries
        for (Integer g = 0; g < 5; g++){
            Group group = database.getRootGroup().addGroup(database.newGroup(g.toString()));
            for (int e = 0; e <= g; e++) {
                // entry factory is a local helper to populate an entry
                group.addEntry(entryFactory(database, g.toString(), e));
            }
        }

        // save to a file with password "123"
        FileOutputStream outputStream = new FileOutputStream("test.kdbx");
        database.save(new KdbxCredentials.Password("123".getBytes()), outputStream);


## To be done
- reading and handling of binary data

## Dependencies

- Bouncy Castle's fork for Android [SpongyCastle](https://github.com/rtyley/spongycastle)
- Google Guava [license](https://github.com/google/guava/blob/master/COPYING)

## Build

##  <a name="license">License</a>

Copyright (c) 2015, 2016 Jo Rabin, Pavel Ivanov (ivanovpv@gmail.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
