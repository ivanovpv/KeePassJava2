package ru.ivanovpv.android.keepassreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.linguafranca.pwdb.Credentials;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;
import org.linguafranca.pwdb.Icon;
import org.linguafranca.pwdb.kdb.KdbCredentials;
import org.linguafranca.pwdb.kdb.KdbDatabase;
import org.linguafranca.pwdb.kdbx.KdbxCredentials;
import org.linguafranca.pwdb.kdbx.dom.DomDatabaseWrapper;
import org.linguafranca.utils.DataUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final static String TAG=MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String databaseName="test123.kdbx";
        String password="123";
        DatabaseLoader databaseLoader=new DatabaseLoader(this, databaseName, password);
        databaseLoader.execute();
    }

    private Database getKDBDatabase(InputStream is, byte[] passwordBytes) throws IOException {
        Credentials credentials = new KdbCredentials.Password(passwordBytes);
        Database database = KdbDatabase.load(credentials, is);
        return database;
    }

    private Database getKDBXDatabase(InputStream is, byte[] passwordBytes) throws IOException {
        Credentials credentials = new KdbxCredentials.Password(passwordBytes);
        return DomDatabaseWrapper.load(credentials, is);
    }

    private boolean checkKDBXDatabase(InputStream is, byte[] passwordBytes) {
        Credentials credentials = new KdbxCredentials.Password(passwordBytes);
        return DataUtils.checkCredentialsKdbx(credentials, is);
    }

    private boolean checkKDBDatabase(InputStream is, byte[] passwordBytes) {
        Credentials credentials = new KdbCredentials.Password(passwordBytes);
        return DataUtils.checkCredentialsKdb(credentials, is);
    }


    private StringBuffer printDatabase(Database database) {
        StringBuffer sb=new StringBuffer();
        sb.append("Database description="+database.getDescription()).append("\n");
        Group group=database.getRootGroup();
        return printGroup(group, sb);
    }

    private StringBuffer printGroup(Group group, StringBuffer sb) {
        sb.append("=========GROUP============\n");
        sb.append("Group="+group.getName()).append("\n");
        List<Entry> entries = group.getEntries();
        for(Entry entry:entries) {
            printEntry(entry, sb);
        }
        List<Group> groups = group.getGroups();
        for(Group childGroup:groups) {
            printGroup(childGroup, sb);
        }
        return sb;
    }

    private StringBuffer printEntry(Entry entry, StringBuffer sb) {
        sb.append("---------entry---------\n");
        sb.append("Entry uuid="+entry.getUuid()).append("\n");
        List<String> properties = entry.getPropertyNames();
        for(String propName:properties) {
            sb.append("Property name="+propName+", value="+entry.getProperty(propName)).append("\n");
        }
        List<String> binProps = entry.getBinaryPropertyNames();
        for(String propName:binProps) {
            String base64= DataUtils.printBase64Binary(entry.getBinaryProperty(propName));
            Log.i(TAG, "Binary data="+base64);
        }
        Icon icon=entry.getIcon();
        sb.append("Icon index="+icon.getIndex()).append("\n");
        sb.append("Created="+entry.getCreationTime()).append("\n");
        sb.append("Modified="+entry.getLastModificationTime()).append("\n");
        sb.append("Accessed="+entry.getLastAccessTime()).append("\n");
        return sb;
    }

    private void logDatabase(Database database) {
        Log.i(TAG, "Database description="+database.getDescription());
        Group group=database.getRootGroup();
        logGroup(group);
    }

    private void logGroup(Group group) {
        Log.i(TAG, "=========GROUP============");
        Log.i(TAG, "Group="+group.getName());
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
        Log.i(TAG, "---------entry---------\n");
        Log.i(TAG, "Entry uuid="+entry.getUuid());
        List<String> properties = entry.getPropertyNames();
        for(String propName:properties) {
            Log.i(TAG, "Property name="+propName+", value="+entry.getProperty(propName));
        }
        List<String> binProps = entry.getBinaryPropertyNames();
        for(String propName:binProps) {
            String base64= DataUtils.printBase64Binary(entry.getBinaryProperty(propName));
            Log.i(TAG, "Binary data="+base64);
        }
        Icon icon=entry.getIcon();
        Log.i(TAG, "Icon index="+icon.getIndex());
        Log.i(TAG, "Created="+entry.getCreationTime());
        Log.i(TAG, "Modified="+entry.getLastModificationTime());
        Log.i(TAG, "Accessed="+entry.getLastAccessTime());
    }


    private void showDatabase(Database database) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Keepass database content");
        builder.setCancelable(true);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        StringBuffer sb=printDatabase(database);
        builder.setMessage(sb.toString());
        AlertDialog dialog=builder.show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(10);
    }

    class DatabaseLoader extends AsyncTask<Void, Void, Database> {
        private String databaseName;
        private String password;
        private ProgressDialog progressDialog;
        private Activity activity;
        Boolean kdbx;

        DatabaseLoader(Activity activity, String databaseName, String password) {
            this.activity=activity;
            this.databaseName=databaseName;
            this.password=password;
            this.kdbx=null; //unknown database type
            progressDialog = new ProgressDialog(activity);
            progressDialog.setOwnerActivity(activity);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Wait while parsing database");
        }

        protected void onPreExecute() {
            super.onPreExecute();
            int index=-1;
            if(databaseName==null || databaseName.trim().length()==0)
                kdbx=null;
            else {
                String extension;
                index=databaseName.lastIndexOf('.');
                if(index >= 0)
                    extension=databaseName.trim().toLowerCase().substring(index);
                else
                    extension=".";
                if(extension.equalsIgnoreCase(".kdbx"))
                    kdbx=true;
                else if(extension.equalsIgnoreCase(".kdb"))
                    kdbx=false;
                else
                    kdbx=null;
            }

            progressDialog.show();
        }


        @Override
        protected Database doInBackground(Void... params) {
            Database database=null;
            InputStream inputStream=null;
            if(kdbx==null) {
                Log.i(TAG, "Database="+databaseName+", is in unknown format");
                return null;
            }
            try {
                inputStream=activity.getAssets().open(databaseName);
                boolean check;
                if(kdbx)
                    check = checkKDBXDatabase(inputStream, password.getBytes());
                else
                    check = checkKDBDatabase(inputStream, password.getBytes());
                if(check) {
                    Log.i(TAG, "Password is ok");
                    //reopen it again
                    inputStream = activity.getAssets().open(databaseName);
                    if(kdbx)
                        database = getKDBXDatabase(inputStream, password.getBytes());
                    else
                        database = getKDBDatabase(inputStream, password.getBytes());
                    logDatabase(database);
                }
                else {
                    Log.i(TAG, "Invalid password or database is corrupt");
                    return null;
                }
            }
            catch(Exception ex) {
                Log.e(TAG, "Error reading database", ex);
            }
            finally {
                if(inputStream!=null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {}
            }
            return database;
        }

        @Override
        protected void onPostExecute(final Database database) {
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
            super.onPostExecute(database);
            final AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            if(database!=null)
                builder.setTitle("Keepass database parsed, results are in Logcat");
            else
                builder.setTitle("Keepass database parsing failed, results are in Logcat");
            builder.setCancelable(true);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.show();
        }
    }
}
