package com.example.aiman.familycontacts;

import android.net.Uri;

/**
 * Connector Class, holds constants by database and Activities
 * Created by Aiman on 29/06/15.
 */
public class MyContactsConnector {

    public static final String AUTHORITY = "com.example.aiman.familycontacts.ContactsContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/contacts");

    //Database attributes
    public static final String DATABASE_NAME = "AddressBook";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "contacts_table";

    public static final int NOT_VERIFIED = 0;
    public static final int VERIFIED = 1;

    //Table attributes
    public static final String CONTACT_ID = "_id";
    public static final String CONTACT_NAME = "name";
    public static final String CONTACT_PHONE = "phone";
    public static final String CONTACT_EMAIL = "email";
    public static final String CONTACT_IMAGE = "image";
    public static final String CONTACT_VERIFIED = "isVerified";

    //Table columns indexes
    public static final int NAME_COLUMN = 1;
    public static final int PHONE_COLUMN = 2;
    public static final int EMAIL_COLUMN = 3;
    public static final int VERIFICATION_COLUMN = 4;
}
