package com.example.aiman.familycontacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactViewActivity extends AppCompatActivity {
    private static final String TAG = "ContactViewActivity";
    private static final int CONTACT_ID_INVALID = -999;

    private int contactId;
    private TextView textViewContactFullNameValue;
    private TextView textViewContactPhoneNumberValue;
    private TextView textViewContactEmailValue;
    private ImageView imageViewContactVerification;

    private void editContact() {
        Intent intentEditContact = new Intent(this, ContactAddActivity.class);
        intentEditContact.putExtra(MyContactsConnector.CONTACT_ID, contactId);
        intentEditContact.putExtra(MyContactsConnector.CONTACT_NAME, textViewContactFullNameValue.getText());
        intentEditContact.putExtra(MyContactsConnector.CONTACT_PHONE, textViewContactPhoneNumberValue.getText());
        intentEditContact.putExtra(MyContactsConnector.CONTACT_EMAIL, textViewContactEmailValue.getText());
        intentEditContact.putExtra(MyContactsConnector.CONTACT_VERIFIED, MyContactsConnector.NOT_VERIFIED);
        startActivity(intentEditContact);
    }

    private void deleteContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactViewActivity.this);
        builder.setTitle("Delete Dialog");
        builder.setMessage("Do you want to delete?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // a task to delete contact without affecting worker thread
                AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>() {
                    @Override
                    protected Object doInBackground(Long... params) {
                        getContentResolver().delete(MyContactsConnector.CONTENT_URI, MyContactsConnector.CONTACT_ID + "=" + contactId, null);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result) {
                        finish();        // close activity upon delete
                    }
                };
                deleteTask.execute();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    private void fillContactDetails() {
        String selection = MyContactsConnector.CONTACT_ID + "=" + contactId;
        Cursor cursorResult = getContentResolver().query(MyContactsConnector.CONTENT_URI, null, selection, null, null);

        cursorResult.moveToFirst();

        textViewContactFullNameValue.setText(cursorResult.getString(MyContactsConnector.NAME_COLUMN));
        textViewContactPhoneNumberValue.setText(cursorResult.getString(MyContactsConnector.PHONE_COLUMN));
        textViewContactEmailValue.setText(cursorResult.getString(MyContactsConnector.EMAIL_COLUMN));


        imageViewContactVerification.setImageResource(cursorResult.getInt(MyContactsConnector.VERIFICATION_COLUMN) == 1 ? android.R.drawable.checkbox_on_background : android.R.drawable.ic_delete);

        setTitle(cursorResult.getString(MyContactsConnector.NAME_COLUMN)); //set activity title to contact name
        cursorResult.close();
    }

    //------------------------ ViewContactActivity LifeCycle methods -------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        textViewContactFullNameValue = (TextView) findViewById(R.id.textViewContactFullNameValue);
        textViewContactPhoneNumberValue = (TextView) findViewById(R.id.textViewContactPhoneNumberValue);
        textViewContactEmailValue = (TextView) findViewById(R.id.textViewContactEmailValue);
        imageViewContactVerification = (ImageView) findViewById(R.id.imageViewContactVerification);

        contactId = getIntent().getIntExtra(MyContactsConnector.CONTACT_ID, CONTACT_ID_INVALID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_view, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        fillContactDetails();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d(TAG, "action_settings");
                break;
            case R.id.action_edit_contact: {
                Log.d(TAG, "action_edit_contact");
                editContact();
                break;
            }
            case R.id.action_delete_contact: {
                Log.d(TAG, "action_delete_contact");
                deleteContact();
                break;
            }
            default:
                Log.d(TAG, "problem in selection");
                break;
        }
        return true;
    }
}