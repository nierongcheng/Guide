package com.codi.cpclient;

import android.content.ContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private Intent mRequestFileIntent;
    private ParcelFileDescriptor mInputPFD;

    private Button mSelectBtn;
    private TextView mNameTv, mSizeTv, mContentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNameTv = (TextView) findViewById(R.id.nameTv);
        mSizeTv = (TextView) findViewById(R.id.sizeTv);
        mContentTv = (TextView) findViewById(R.id.contentTv);
        mSelectBtn = (Button) findViewById(R.id.selectBtn);

        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("text/plain");

        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestFile();
            }
        });
    }


    protected void requestFile() {
        startActivityForResult(mRequestFileIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }

        Uri returnUri = data.getData();
        try {
            mInputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("MainActivity", "File not found.");
            return;
        }

        Cursor cursor = getContentResolver().query(returnUri, null, null, null, null);
        if(cursor == null) {
            return;
        }

        if(cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            mNameTv.setText(cursor.getString(nameIndex));
            mSizeTv.setText(cursor.getString(sizeIndex));
        }

        cursor.close();

        String text = readFromPFD(mInputPFD);
        if (text != null) {
            mContentTv.setText(text);
        }

    }

    private String readFromPFD(ParcelFileDescriptor parcelFileDescriptor) {

        if (parcelFileDescriptor == null) {
            return null;
        }

        //读取文件内容
        FileReader fr = null;
        char[] buffer = new char[1024];

        try {
            StringBuilder strBuilder = new StringBuilder();
            fr = new FileReader(parcelFileDescriptor.getFileDescriptor());
            while (fr.read(buffer) != -1) {
                strBuilder.append(buffer);
            }
            fr.close();
            return strBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                parcelFileDescriptor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
