package com.example.aiman.familycontacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;


public class ContactAddActivity extends Activity {

    private static final String TAG = "ContactAddActivity";
    private static final int REQUEST_CODE_CONTACT_IMAGE_GALLERY = 100;


    public void onClickButtonContactCancel (View view) {
        Log.d(TAG, "onClickButtonContactCancel");
        setResult(RESULT_CANCELED);
        finish();
    }


    public void onClickButtonContactConfirm (View view) {
        Log.d(TAG, "onClickButtonContactConfirm");
        setResult(RESULT_OK);
        finish();
    }


    public void onClickImageButtonContactImage (View view) {
        Log.d(TAG, "onClickImageButtonContactImage");
        //TODO:
        Intent intentContactImgae = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentContactImgae, REQUEST_CODE_CONTACT_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                ImageButton contactImage = (ImageButton) findViewById(R.id.imageButtonContactImage);
                contactImage.setImageBitmap(bitmap);
                contactImage.setTag(imageUri.toString());
                Log.d(TAG, imageUri.toString());
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(),"Error!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "FileNotFoundException");
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(),"Error!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "NullPointerException");
            }
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
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_add, menu);
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
}
