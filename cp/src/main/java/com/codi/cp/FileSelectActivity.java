package com.codi.cp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

/**
 * Created by Codi on 2015/4/25 0025.
 */
public class FileSelectActivity extends Activity {

    private ListView mListView;
    private Intent mSelectIntent;

    String[] mFilenames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);

        mFilenames = readFilenames(getFilesDir().getAbsolutePath() + "/files");

        mSelectIntent = new Intent("com.codi.cp.ACTION_RETURN_FILE");

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mFilenames));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(mFilenames[position]);
                Uri fileUri;

                try {
                    fileUri = FileProvider.getUriForFile(FileSelectActivity.this, getString(R.string.fileprovider_authority), file);
                    if (fileUri != null) {
                        mSelectIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        mSelectIntent.setDataAndType(fileUri, getContentResolver().getType(fileUri));
                        setResult(RESULT_OK, mSelectIntent);
                    } else {
                        mSelectIntent.setDataAndType(null, "");
                        setResult(RESULT_CANCELED, mSelectIntent);
                    }

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String[] readFilenames(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if(files != null) {
            String[] filenames = new String[files.length];
            for(int i = 0, length = files.length; i < length; i++) {
                filenames[i] = files[i].getAbsolutePath();
            }
            return filenames;
        }
        return null;
    }
}
