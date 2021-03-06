package com.example.aiman.familycontacts;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class ContactsListActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_CONTACT = 900;
    public static final int REQUEST_CODE_EDIT_CONTACT = 901;

    private static final String TAG = "ContactsListActivity";
    private static final int USER_ADMIN = 0;
    private static final int USER_REGULAR = 1;

    private int userType;

    private boolean addContact(Intent data) {
        Log.d(TAG, "addContact");

        ContentValues contact = new ContentValues();
        contact.put(MyContactsConnector.CONTACT_NAME, data.getStringExtra(MyContactsConnector.CONTACT_NAME));
        contact.put(MyContactsConnector.CONTACT_PHONE, data.getStringExtra(MyContactsConnector.CONTACT_PHONE));
        contact.put(MyContactsConnector.CONTACT_EMAIL, data.getStringExtra(MyContactsConnector.CONTACT_EMAIL));

        if (userType == USER_ADMIN) {
            //TODO: SQL query to create in Contacts table
            contact.put(MyContactsConnector.CONTACT_VERIFIED, MyContactsConnector.VERIFIED);
            Toast.makeText(getApplicationContext(), "Contact Add Confirmed", Toast.LENGTH_SHORT).show();
        } else {
            //TODO: SQL query to create in Requests table
            contact.put(MyContactsConnector.CONTACT_VERIFIED, MyContactsConnector.NOT_VERIFIED);
            Toast.makeText(getApplicationContext(), "Contact Add Requested", Toast.LENGTH_SHORT).show();
        }
        getContentResolver().insert(MyContactsConnector.CONTENT_URI, contact);
        return false;
    }

    private boolean editContact(Intent data) {
        Log.d(TAG, "editContact");
        if (userType == USER_ADMIN) {
            //TODO: SQL query to modify in Contacts table
            Toast.makeText(getApplicationContext(), "Contact Edit Confirmed", Toast.LENGTH_SHORT).show();
        } else {
            //TODO: SQL query to modify in Requests table
            Toast.makeText(getApplicationContext(), "Contact Edit Requested", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean deleteContact() {
        Log.d(TAG, "deleteContact");
        if (userType == USER_ADMIN) {
            //TODO: SQL query to delete in Contacts table
            Toast.makeText(getApplicationContext(), "Contact Delete Confirmed", Toast.LENGTH_SHORT).show();
        } else {
            //TODO: SQL query to delete in Requests table
            Toast.makeText(getApplicationContext(), "Contact Delete Requested", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        userType = USER_REGULAR;
    }


    // -------------------- Activity Life Cycle ------------------------

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        new LoadContactsTask().execute();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (id) {
            case R.id.action_add_contact: {
                Intent intentAddContactActivity = new Intent(getApplicationContext(), ContactAddActivity.class);
                startActivityForResult(intentAddContactActivity, REQUEST_CODE_ADD_CONTACT);
                return true;
            }
            case R.id.action_settings: {
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_CONTACT && resultCode == RESULT_OK) {
            addContact(data);
        } else if (requestCode == REQUEST_CODE_EDIT_CONTACT && resultCode == RESULT_OK) {
            //TODO:
            editContact(data);
        } else if ((requestCode == REQUEST_CODE_ADD_CONTACT || requestCode == REQUEST_CODE_EDIT_CONTACT) && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Internal class to load contacts from database into the contacts list
     *
     * @author Aiman
     */
    private class LoadContactsTask extends AsyncTask<Object, Object, Cursor> {

        @Override
        protected Cursor doInBackground(Object... arg0) {
            Log.d(TAG, "doInBackground");

            //get contacts
            final Cursor cursorResults = getContentResolver().query(MyContactsConnector.CONTENT_URI, null, null, null, MyContactsConnector.CONTACT_NAME);

            //add contacts
            final int indexContactId = cursorResults.getColumnIndex(MyContactsConnector.CONTACT_ID);
            int indexContactName = cursorResults.getColumnIndex(MyContactsConnector.CONTACT_NAME);
            int indexContactPhone = cursorResults.getColumnIndex(MyContactsConnector.CONTACT_PHONE);
            int indexContactEmail = cursorResults.getColumnIndex(MyContactsConnector.CONTACT_EMAIL);

            for (cursorResults.moveToFirst(); !cursorResults.isAfterLast(); cursorResults.moveToNext()) {
                if (isCancelled()) {
                    return null;
                }
                Button button = new Button(getApplicationContext());
                button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button.setId(Integer.parseInt(cursorResults.getString(indexContactId)));
                button.setText(cursorResults.getString(indexContactName));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick");

                        Intent intentViewContactActivity = new Intent(getApplicationContext(), ContactViewActivity.class);
                        intentViewContactActivity.putExtra(MyContactsConnector.CONTACT_ID, v.getId());
                        //startActivityForResult(intentViewContactActivity, REQUEST_CODE_EDIT_CONTACT);
                        startActivity(intentViewContactActivity);
                    }
                });
                publishProgress(button);
            }
            return null;

        }

        protected void onPostExecute(Cursor result) {
            Log.d(TAG, "onPostExecute");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //clear previous contacts from layout
            LinearLayout linearLayoutContactsList = (LinearLayout) findViewById(R.id.linearLayoutContactsList);
            linearLayoutContactsList.removeAllViews();
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            LinearLayout linearLayoutContactsList = (LinearLayout) findViewById(R.id.linearLayoutContactsList);
            linearLayoutContactsList.addView((View) values[0]);
        }
    }
}
