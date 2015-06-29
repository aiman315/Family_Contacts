package com.example.aiman.familycontacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class ContactViewActivity extends Activity {
    private final String TAG = "ContactViewActivity";

    private long contactId;
    private TextView textViewContactFullNameValue;
    private TextView textViewContactPhoneNumberValue;
    private TextView textViewContactEmailValue;
    private int isVerified;

    private View.OnClickListener copyContent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickCopyViewContent(v);
        }
    };

    /**
     * Starts NewContactActivity with the purpose to edit a contact
     * passes existing details to fill fields in the started activity, so they are edited
     * Invoked by clicking on <strong>Edit Contact</strong> in action bar
     */
    private void editContact() {
        Intent editContactIntent = new Intent(this, ContactAddActivity.class);
        editContactIntent.putExtra(MyContactsConnector.CONTACT_ID, contactId);
        editContactIntent.putExtra(MyContactsConnector.CONTACT_NAME, textViewContactFullNameValue.getText());
        editContactIntent.putExtra(MyContactsConnector.CONTACT_PHONE, textViewContactPhoneNumberValue.getText());
        editContactIntent.putExtra(MyContactsConnector.CONTACT_EMAIL, textViewContactEmailValue.getText());
        editContactIntent.putExtra(MyContactsConnector.CONTACT_VERIFIED, isVerified);
        startActivity(editContactIntent);
    }

    /**
     * Creates a dialog for user to choose to delete contact from database
     * Invoked by clicking on <strong>Delete Contact</strong> in action bar
     */
    private void deleteContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactViewActivity.this);
        builder.setTitle("Delete Dialog");
        builder.setMessage("Do you want to delete?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // a task to delete contact without affecting worker thread
                AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>(){
                    @Override
                    protected Object doInBackground(Long... params) {
                        getContentResolver().delete(MyContactsConnector.CONTENT_URI, MyContactsConnector.CONTACT_ID + "=" + contactId, null);
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object result) {
                        finish();		// close activity upon delete
                    }
                };
                deleteTask.execute(new Long[]{contactId});
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    /**
     * Displays a toast
     * Invoked by <strong>onActivityResult</strong> to indicate an issue loading image
     */
    private void loadingPhotoError() {
        Log.d(TAG, "loadingPhotoError");
        Toast.makeText(getApplicationContext(), "Problem loading photo", Toast.LENGTH_SHORT).show();
    }

    /**
     * Copies clicked view text to clip board
     * Displays a toast to indicate the content of clicked view has been copied
     * @param v clicked view
     */
    public void onClickCopyViewContent(View v) {
        Log.d(TAG, "onClickCopyViewContent");
        TextView textView = (TextView) v;
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, textView.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        textViewContactFullNameValue = (TextView) findViewById(R.id.textViewContactFullNameValue);
        textViewContactPhoneNumberValue = (TextView) findViewById(R.id.textViewContactPhoneNumberValue);
        textViewContactPhoneNumberValue.setOnClickListener(copyContent);
        textViewContactEmailValue = (TextView) findViewById(R.id.textViewContactEmailValue);
        textViewContactEmailValue.setOnClickListener(copyContent);

        contactId = getIntent().getIntExtra(MyContactsConnector.CONTACT_ID, -1);
    }

    //------------------------ ViewContactActivity LifeCycle methods -------------------------

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
        new LoadContactTask().execute(contactId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
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
            default:
                Log.d(TAG, "problem in selection");
                break;
        }
        return true;
    }

    //inner class to load the selected contact details without interrupting the worker thread
    private class LoadContactTask extends AsyncTask<Long, Object, Cursor> {

        //query for data from database
        @Override
        protected Cursor doInBackground(Long... params) {
            String whereClause = MyContactsConnector.CONTACT_ID + "=" + params[0];
            return getContentResolver().query(MyContactsConnector.CONTENT_URI, null, whereClause, null, null);
        }

        // fill activity views with data from database
        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);
            result.moveToFirst();

            textViewContactFullNameValue.setText(result.getString(MyContactsConnector.NAME_COLUMN));
            textViewContactPhoneNumberValue.setText(result.getString(MyContactsConnector.PHONE_COLUMN));
            textViewContactEmailValue.setText(result.getString(MyContactsConnector.EMAIL_COLUMN));

            setTitle(result.getString(MyContactsConnector.NAME_COLUMN)); //set activity title to contact name
            result.close();
        }
    }
}