package com.bigteam.kosta.bigteamclnt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, Splash.class));
    }

    public void onClickFileExplorer(View view) {
        //intent 생성
        Intent intent = new Intent(this, FileExplorerActivity.class);
        startActivity(intent);
    }
}
