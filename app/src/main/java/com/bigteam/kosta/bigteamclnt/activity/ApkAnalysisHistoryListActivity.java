package com.bigteam.kosta.bigteamclnt.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.bigteam.kosta.bigteamclnt.R;
import com.bigteam.kosta.bigteamclnt.SqlLiteDBManger;
import com.bigteam.kosta.bigteamclnt.adaptor.DBAdapter;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kosta on 2016-07-20.
 * 액티비티 기능 설명 : 정밀 분석기능을 선택하여 <보유 APK파일 목록 -> 분석 대상 APK파일 선택 -> 정밀 분석 선택 -> 분석서버로 파일전송>  < > 안의 의 과정을 거치면  분석 대상 파일의 분석 이력을 DB에 저장하게 된다.
 *                      이 액티비티는 이 이력을 DB(안드로이드 내부 DB) 에서 불러와서 리스트 형식으로 보여주고 각 리스트 항목을 선택할 수 있게 한다, ( 선택 시 ApkAnalysisResultWebViewActivity로 이동하여 결과를 보여줌)
 */
public class ApkAnalysisHistoryListActivity extends AppCompatActivity {

//    final SqlLiteDBManger dbHelper = new SqlLiteDBManger(getApplicationContext(), "ApkAnalysisHistory.db", null, 1);
    private ListView lView;

    private ArrayList resultDBList = new ArrayList();
    private HashMap<String, String> resultDBHashmap = new HashMap<String, String>();
    private Cursor rCursor = null;
    private Adapter adapter;
    static int responseCode;
    static String apk_md5_name;
    static Intent intent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        final SqlLiteDBManger dbHelper = new SqlLiteDBManger(getApplicationContext(), "ApkAnalysisHistory.db", null, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apkanalysishistory); // 리스트뷰 초기화

        // 테이블의 모든 레코드를 커서로 가져온다.
        rCursor = dbHelper.getApkAnalysisHistory();

        lView = (ListView)findViewById(R.id.list);
        DBAdapter dbAdapter = new DBAdapter(this, rCursor);
        lView.setAdapter(dbAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                rCursor.moveToPosition(position);
//                String apk_md5_name = rCursor.getString(rCursor.getColumnIndex("apk_md5_name"));
                apk_md5_name = rCursor.getString(rCursor.getColumnIndex("apk_md5_name"));
                Toast.makeText(getApplicationContext(), apk_md5_name, Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("apk_md5_name", apk_md5_name);
                ////////////////////////////////////////////////////////////


                new Thread () {
                    String analysisResultHtmlUrl = "http://192.168.0.146/result/"+apk_md5_name +".html";
                    public void run() {
                        try
                        {
                            URL url = new URL(analysisResultHtmlUrl);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            responseCode = conn.getResponseCode();
                            Log.i("TEST", "responseCode: " + responseCode);
                            conn.disconnect();
                            if (responseCode != 200) {
                                Handler mHandler = new Handler(Looper.getMainLooper());
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 내용
                                        Toast.makeText(getApplicationContext(), "정밀분석이 진행 중입니다. 잠시 후에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                }, 0);
                            } else {
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            responseCode = 0;
                        }
                    }
                }.start();
//                if (responseCode != 200) {
//                    Toast.makeText(getApplicationContext(), "정밀분석이 아직 진행 중입니다. 잠시 후에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
//                } else {
//                    startActivity(intent);
//                }
                ////////////////////////////////////////////////////////////
//                startActivity(intent);
            }
        });

//        while (rCursor.moveToNext()) {
//            String name = rCursor.getString(rCursor.getColumnIndex("apk_name"));
//            String md5Name = rCursor.getString(rCursor.getColumnIndex("apk_md5_name"));
//            String createDate = rCursor.getString(rCursor.getColumnIndex("create_at"));
//            String flag = rCursor.getString(rCursor.getColumnIndex("flag"));
//
//            resultDBHashmap = new HashMap<String, String>();
//            resultDBHashmap.put("apk_name", name);
//            resultDBHashmap.put("apk_md5_name", md5Name);
//            resultDBHashmap.put("create_at", createDate);
//            resultDBHashmap.put("flag", flag);
//            resultDBList.add(resultDBHashmap);
//
////            Log.i("DBTEST", "apk_name: " + name + "  md5Name: " + md5Name + "   createDate: " + createDate + "   flag: " + flag);
//
//            Log.i("Test", "resultDBList:" + resultDBList);
//        }
    }
}
