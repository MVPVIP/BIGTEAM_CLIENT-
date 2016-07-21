package com.bigteam.kosta.bigteamclnt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bigteam.kosta.bigteamclnt.R;
import com.bigteam.kosta.bigteamclnt.Splash;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, Splash.class));
    }

    public void onClickApkListView(View view) { // 설치된 앱 리스트 클릭
        //intent 생성
        Intent intent = new Intent(this, ApkListActivity.class);
        startActivity(intent);
    }

    public void onClickFileExplorer(View view) { // 보유 APK파일 리스트 클릭
        //intent 생성
        Intent intent = new Intent(this, FileExplorerActivity.class);
        startActivity(intent);
    }

    public void onClickApkAnalysisHistory(View view) { // 보유 APK파일 리스트 클릭
        //intent 생성
        Intent intent = new Intent(this, ApkAnalysisHistoryListActivity.class);
        startActivity(intent);
    }
}
