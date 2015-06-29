package com.example.aiman.familycontacts;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class ContactsContentProvider extends ContentProvider {

    private static final String TAG = "ContactsContentProvider";

    //To manage URI user requests
    private static final int MULTIPLE_CONTACTS = 1;
    private static final int SINGLE_CONTACT = 2;
    private static final int ID_PATH_SEGMENT = 1;

    //URI matcher for two possible requests :
    // 1: retrieving a single contact details
    // 2: retrieving multiple contacts details
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MyContactsConnector.AUTHORITY, "contacts", MULTIPLE_CONTACTS);
        uriMatcher.addURI(MyContactsConnector.AUTHORITY, "contacts/#", SINGLE_CONTACT);
    }

    public ContactsContentProvider() {
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "getType");

        switch (uriMatcher.match(uri)) {
            case SINGLE_CONTACT:
                //return one authority
                return "vnd.android.cursor.item/contact/#";
            case MULTIPLE_CONTACTS:
                //return multiple authority
                return "vnd.android.cursor.dir/contacts";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(getContext(), MyContactsConnector.DATABASE_NAME, null, MyContactsConnector.DATABASE_VERSION);
        return dbHelper.getWritableDatabase() != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query");
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert");
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "update");
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete");
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }





    private class DatabaseOpenHelper extends SQLiteOpenHelper {
        private final String TAG = "DatabaseOpenHelper";
        public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(this.TAG, "onCreate");
            String createTable =
                    "create table "+ MyContactsConnector.TABLE_NAME + "(" +
                            MyContactsConnector.CONTACT_ID + " integer primary key autoincrement, " +
                            MyContactsConnector.CONTACT_NAME + " text, " +
                            MyContactsConnector.CONTACT_PHONE + " text, " +
                            MyContactsConnector.CONTACT_EMAIL + " text, " +
                            MyContactsConnector.CONTACT_IMAGE + " text); " +
                            MyContactsConnector.CONTACT_VERIFIED + "integer";
            db.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(this.TAG, "onUpgrade");
            db.execSQL("drop table if exists " + MyContactsConnector.TABLE_NAME);
            onCreate(db);
        }
    }
}
