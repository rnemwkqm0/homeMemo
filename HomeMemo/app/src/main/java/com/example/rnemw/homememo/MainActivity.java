package com.example.rnemw.homememo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button onBtn, offBtn;
    Intent onIntent, offIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onBtn= (Button)findViewById(R.id.OnBtn);
        offBtn= (Button)findViewById(R.id.OffBtn);

        BtnOnClickListener onClickListener = new BtnOnClickListener();
        onBtn.setOnClickListener(onClickListener);
        offBtn.setOnClickListener(onClickListener);

    }

    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.OnBtn:
                    Toast.makeText(MainActivity.this, "홈 메모의 작동을 ON합니다.", Toast.LENGTH_LONG).show();
                    onIntent = new Intent(MainActivity.this, ScreenService.class);
                    startService(onIntent);
                    break;
                case R.id.OffBtn:
                    Toast.makeText(MainActivity.this, "홈 메모의 작동을 OFF합니다.", Toast.LENGTH_LONG).show();
                    offIntent = new Intent(MainActivity.this, ScreenService.class);
                    stopService(offIntent);
                    break;
            }
        }
    }
}
