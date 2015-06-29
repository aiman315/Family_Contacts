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

    private final String TAG = "VerificationActivity";
    private String verificationKey;
    private AsyncTask myVerificationTask;

    public void onClickButtonPhoneVerify (View view) {
        Log.d(TAG, "onClickButtonPhoneVerify");
        if (validPhoneNumber()) {
            myVerificationTask.execute();
        }
    }

    public void onClickButtonPhoneConfirm (View view) {
        Log.d(TAG, "onClickButtonPhoneConfirm");

        EditText editTextVerificationCode = (EditText) findViewById(R.id.editTextVerificationCode);
        String code = editTextVerificationCode.getText().toString();
        Log.d(TAG, "Code value = "+code);
        if (code.equals(verificationKey)) {
            Log.d(TAG, "Success");
            Intent intentContactAdd = new Intent(this, ContactAddActivity.class);
            startActivity(intentContactAdd);
        } else {
            Log.d(TAG, "Fail");
        }
    }

    public void onClickImageButtonContactImage() {
        Log.d(TAG, "onClickImageButtonContactImage");
    }

    /**
     * Validate the user input phone number
     * Validation criteria: //TODO: fill phone validation criteria
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
        verificationKey = null;
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyVerificationTask extends AsyncTask {
        private int count = 30;

        @Override
        protected Object doInBackground(Object[] params) {
            Log.d(TAG,"in background");

            while(count > 0) {
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
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            verificationKey = "";

            for (int i = 0 ; i < 4 ; i++) {
                int rand = (int)(Math.random()*10);
                verificationKey += rand;
            }

            Log.d(TAG, "the value is "+verificationKey);

            EditText editTextPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
            String phoneNumber = editTextPhoneNumber.getText().toString();

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, verificationKey, null, null);

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
