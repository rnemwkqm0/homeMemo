package com.example.rnemw.homememo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen);

        releaseBtn = (Button) findViewById(R.id.lockReleaseBtn);
        releaseBtn.setOnLongClickListener(new Button.OnLongClickListener() {
            public boolean onLongClick(View v) {
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
        String[] from = {"_id", "memo"};
        int[] to = {R.id.item_counter, R.id.item_title};

        adapter = new SimpleCursorAdapter(this, R.layout.listview_layout, c, from, to);
        list = (ListView) findViewById(R.id.listview);
        // 리스트뷰 객체에 어댑터 설정
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View vi, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LockScreenActivity.this);
                c = db.rawQuery("SELECT * FROM contacts", null);
                c.moveToFirst();
                for(int i = 0; i < position; i++) {
                    c.moveToNext();
                }
                final int pos = c.getInt(0);;
                dialog.setTitle("메모글 변경");
                final EditText edittext;
                edittext = new EditText(LockScreenActivity.this);
                dialog.setView(edittext);

                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputValue = edittext.getText().toString();
                        db.execSQL("UPDATE contacts SET memo = '" + inputValue + "'" +
                                " WHERE _id = " + pos + ";");

                        c = db.rawQuery("SELECT * FROM contacts", null);
                        String[] from = {"_id", "memo"};
                        int[] to = {R.id.item_counter, R.id.item_title};

                        adapter = new SimpleCursorAdapter(LockScreenActivity.this, R.layout.listview_layout, c, from, to);
                        list = (ListView) findViewById(R.id.listview);
                        list.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });

                dialog.setNeutralButton("삭제", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        db.execSQL("DELETE FROM contacts WHERE _id = " + pos + ";");

                        c = db.rawQuery("SELECT * FROM contacts", null);
                        String[] from = {"_id", "memo"};
                        int[] to = {R.id.item_counter, R.id.item_title};

                        adapter = new SimpleCursorAdapter(LockScreenActivity.this, R.layout.listview_layout, c, from, to);
                        list = (ListView) findViewById(R.id.listview);
                        list.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });

                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    public void memoClick(View v) {
        switch(v.getId()){
            case R.id.addMemo:
            db.execSQL("INSERT INTO contacts VALUES (null, '클릭해서 메모를 입력해주세요.');");
            c = db.rawQuery("SELECT * FROM contacts", null);
            String[] from = {"_id", "memo"};
            int[] to = {R.id.item_counter, R.id.item_title};

            adapter = new SimpleCursorAdapter(this, R.layout.listview_layout, c, from, to);
            list = (ListView) findViewById(R.id.listview);
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