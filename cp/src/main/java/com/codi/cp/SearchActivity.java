package com.codi.cp;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class SearchActivity extends Activity {

    public static final String TAG = "SearchActivity";

    String[] mProjection = {
            UserDictionary.Words._ID,
            UserDictionary.Words.WORD,
            UserDictionary.Words.LOCALE
    };

    String mSelectionClause = null;
    String[] mSelectionArgs = null;
    String mOrderBy = UserDictionary.Words.WORD + " ASC";

    private EditText mSearchEt;

    private Cursor mCursor;
    private SimpleCursorAdapter mAdapter;

    String[] mWordListColumns = {
            UserDictionary.Words.WORD,
            UserDictionary.Words.LOCALE
    };

    int[] mWordListItems = { android.R.id.text1, android.R.id.text2 };

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list);
        mSearchEt = (EditText) findViewById(R.id.searchEt);

        TextView textView = new TextView(this);
        textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText("No Data");
        ((ViewGroup) mListView.getParent()).addView(textView);
        mListView.setEmptyView(textView);
    }

    public void search() {
        String searchStr = mSearchEt.getText().toString();


        if(TextUtils.isEmpty(searchStr)) {
            mSelectionClause = null;
            mSelectionArgs = null;
        } else {
            mSelectionClause = UserDictionary.Words.WORD + " = ?";
            mSelectionArgs = new String[] { searchStr };
        }

        mCursor = getContentResolver().query(UserDictionary.Words.CONTENT_URI, mProjection, mSelectionClause, mSelectionArgs, mOrderBy);
        if (mCursor == null) {

        } else {
            Log.d(TAG, "cursor counts: " + mCursor.getCount());
            mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, mCursor, mWordListColumns, mWordListItems, 0);
            mListView.setAdapter(mAdapter);
        }
    }

    public void startSearch(View view) {
        search();
    }
}
