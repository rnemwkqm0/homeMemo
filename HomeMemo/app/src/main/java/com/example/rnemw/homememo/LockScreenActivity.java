package com.example.rnemw.homememo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by rnemw on 2016-12-19.
 */

public class LockScreenActivity extends AppCompatActivity {
    private Button releaseBtn;
    private DBHelper helper;
    private SQLiteDatabase db;
    private Cursor c;
    SimpleCursorAdapter adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen);

        releaseBtn = (Button)findViewById(R.id.lockReleaseBtn);
        releaseBtn.setOnLongClickListener(new Button.OnLongClickListener(){
            public boolean onLongClick(View v){
                finish();
                return true;
            }
        });

        helper = new DBHelper(this);
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }
        c = db.rawQuery("SELECT * FROM contacts", null);
        String[] from = {"memo"};
        int[] to = {android.R.id.text1, android.R.id.text2};

        adapter = new SimpleCursorAdapter(this, R.layout.listview_layout, c, from, to);
        list = (ListView)findViewById(R.id.listview);
        // 리스트뷰 객체에 어댑터 설정
        list.setAdapter(adapter);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    public void makeMemo(View v){
        if(v.getId() == R.id.addMemo){
            db.execSQL("INSERT INTO contacts VALUES (null, '새 메모');");
            c = db.rawQuery("SELECT * FROM contacts", null);
            String[] from = {"memo"};
            int[] to = {android.R.id.text1, android.R.id.text2};

            adapter = new SimpleCursorAdapter(this, R.layout.listview_layout, c, from, to);
            list = (ListView)findViewById(R.id.listview);
            // 리스트뷰 객체에 어댑터 설정
            list.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        helper.close();
        c.close();
    }
}