package com.example.aiman.familycontacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class ContactsListActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_CONTACT = 900;
    private static final int REQUEST_CODE_EDIT_CONTACT = 901;

    private static final int RESULT_CODE_CANCEL = 800;
    private static final int RESULT_CODE_ADD_SUCCESS = 801;
    private static final int RESULT_CODE_ADD_FAIL = 802;

    private static final String TAG = "ContactsListActivity";
    private static final int USER_ADMIN = 0;
    private static final int USER_REGULAR = 1;

    private int userType;

    private boolean addContact() {
        Log.d(TAG, "addContact");
        if (userType == USER_ADMIN) {
            //TODO: SQL query to create in Contacts table
            Toast.makeText(getApplicationContext(), "Contact Add Confirmed", Toast.LENGTH_SHORT).show();
        } else {
            //TODO: SQL query to create in Requests table
            Toast.makeText(getApplicationContext(), "Contact Add Requested", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean editContact() {
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

    // -------------------- Activity Life Cycle ------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        userType = USER_REGULAR;
    }

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
                Intent intentAddActivity = new Intent(getApplicationContext(), ContactAddActivity.class);
                startActivityForResult(intentAddActivity, REQUEST_CODE_ADD_CONTACT);
                return true;
            }
            case R.id.action_edit_contact: {
                Intent intentAddActivity = new Intent(getApplicationContext(), ContactAddActivity.class);
                startActivityForResult(intentAddActivity, REQUEST_CODE_EDIT_CONTACT);
                return true;
            }
            case R.id.action_delete_contact: {
                //TODO
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
            //TODO:
            addContact();
        } else if (requestCode == REQUEST_CODE_EDIT_CONTACT && resultCode == RESULT_OK) {
            //TODO:
            editContact();
        } else if ((requestCode == REQUEST_CODE_ADD_CONTACT || requestCode == REQUEST_CODE_EDIT_CONTACT) && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
        }
    }
}
