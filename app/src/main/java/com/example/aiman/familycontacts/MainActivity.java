package com.example.aiman.familycontacts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private final String TAG = "MainActivity";
    private String verificationKey;

    public void onClickButtonPhoneVerify (View view) {
        Log.d(TAG, "onClickButtonPhoneVerify");
        new MyVerificationTask().execute();
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verificationKey = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

            if (editTextPhoneNumber.length() == 10) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, verificationKey, null, null);


                Button buttonVerify = (Button) findViewById(R.id.buttonPhoneVerify);
                buttonVerify.setEnabled(false);
            } else {
                Toast.makeText(MainActivity.this, "Invalid Phone Number",Toast.LENGTH_SHORT).show();
            }
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
