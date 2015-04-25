package com.codi.loader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private File mPrivateRootDir;
    private File mImagesDir;
    File[] mImageFiles;
    String[] mImageFilenames;

    private Intent mResultIntent;

    private ListView mFileListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultIntent = new Intent("com.codi.");
        mPrivateRootDir = getFilesDir();
        mImagesDir = new File(mPrivateRootDir, "images");
        mImageFiles = mImagesDir.listFiles();
        if(mImageFiles != null) {
            mImageFilenames = new String[mImageFiles.length];
            for(int i = 0, length = mImageFiles.length; i < length; i++) {
                mImageFilenames[i] = mImageFiles[i].getAbsolutePath();
            }
        }

        setResult(Activity.RESULT_CANCELED, null);

        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File requestFile = new File(mImageFilenames[position]);
                Uri fileUri = FileProvider.getUriForFile(MainActivity.this, "com.codi.cp.fileprovider", requestFile);
                if (fileUri != null) {
                    mResultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mResultIntent.setDataAndType(fileUri, getContentResolver().getType(fileUri));
                    MainActivity.this.setResult(Activity.RESULT_OK, mResultIntent);
                } else {
                    mResultIntent.setDataAndType(null, "");
                    MainActivity.this.setResult(Activity.RESULT_CANCELED, mResultIntent);
                }
            }
        });
    }
}
