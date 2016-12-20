package com.example.rnemw.homememo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by rnemw on 2016-12-19.
 */

public class LockScreenActivity extends AppCompatActivity {
    private Button releaseBtn;

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


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }
}
