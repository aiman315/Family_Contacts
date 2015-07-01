package com.example.aiman.familycontacts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class VerificationActivity extends AppCompatActivity {

    private static final int VERIFICATION_CODE_LENGTH = 4;
    private static final int VERIFICATION_CODE_WAIT = 30;
    private final String TAG = "VerificationActivity";
    private String verificationCode;
    private AsyncTask myVerificationTask;

    public void onClickButtonPhoneVerify(View view) {
        Log.d(TAG, "onClickButtonPhoneVerify");
        if (validPhoneNumber()) {
            myVerificationTask.execute();
        }
    }

    public void onClickButtonPhoneConfirm(View view) {
        Log.d(TAG, "onClickButtonPhoneConfirm");

        EditText editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        EditText editTextVerificationCode = (EditText) findViewById(R.id.editTextVerificationCode);

        String selection =
                MyContactsConnector.CONTACT_PHONE + " = " + editTextPhoneNumber.getText().toString() +
                        " and " + MyContactsConnector.CONTACT_VERIFIED + " = " + MyContactsConnector.NOT_VERIFIED;

        String verificationCodeInput = editTextVerificationCode.getText().toString();
        Log.d(TAG, "Code value = " + verificationCodeInput);
        if (verificationCodeInput.equals(verificationCode) && getContentResolver().query(MyContactsConnector.CONTENT_URI, null, selection, null, null).getCount() != 0) {
            Log.d(TAG, "Success");
            Intent intentContactAdd = new Intent(this, ContactsListActivity.class);
            startActivity(intentContactAdd);
        } else {
            Log.d(TAG, "Fail");
        }
    }

    /**
     * Validate the user input phone number
     * Validation criteria: //TODO: fill phone validation criteria
     *
     * @return true: if phone number is valid, false: otherwise
     */
    private boolean validPhoneNumber() {
        Log.d(TAG, "validPhoneNumber");
        //FIXME: what are validation criteria?
        EditText editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        if (editTextPhoneNumber.getText() != null && editTextPhoneNumber.getText().toString().length() == 10) {
            return true;
        }
        editTextPhoneNumber.setError("Invalid Phone Number");
        return false;
    }


    // ---------------------- Activity Life Cycle -------------------------- //


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

        myVerificationTask.cancel(true);
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        // Initialisation
        verificationCode = null;
        myVerificationTask = new MyVerificationTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_verification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private class MyVerificationTask extends AsyncTask {
        private int count = VERIFICATION_CODE_WAIT;

        @Override
        protected Object doInBackground(Object[] params) {
            Log.d(TAG, "in background");

            while (count > 0) {
                try {
                    Thread.sleep(1000);
                    if (isCancelled()) {
                        return false;
                    }
                    publishProgress();
                    count--;
                } catch (InterruptedException exception) {
                    Log.e(TAG, "Verification countdown stopped");
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            verificationCode = "";

            for (int i = 0; i < VERIFICATION_CODE_LENGTH; i++) {
                int rand = (int) (Math.random() * 10);
                verificationCode += rand;
            }

            Log.d(TAG, "the value is " + verificationCode);

            EditText editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
            String phoneNumber = editTextPhoneNumber.getText().toString();

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, verificationCode, null, null);

            Button buttonVerify = (Button) findViewById(R.id.buttonPhoneVerify);
            buttonVerify.setEnabled(false);

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Button buttonVerify = (Button) findViewById(R.id.buttonPhoneVerify);
            buttonVerify.setEnabled(true);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);

            TextView textViewCount = (TextView) findViewById(R.id.textViewPhoneCount);
            //Log.d(TAG, "Count is = "+count);
            if (count > 0) {
                textViewCount.setText("next attempt available in (" + count + ") seconds");
            } else {
                textViewCount.setText(getString(R.string.empty));
            }
        }
    }
}
