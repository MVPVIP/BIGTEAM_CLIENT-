package com.bigteam.kosta.bigteamclnt.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigteam.kosta.bigteamclnt.HashUtil;
import com.bigteam.kosta.bigteamclnt.R;
import com.bigteam.kosta.bigteamclnt.virusTotalApi;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import virustotalapi.ReportScan;

//import com.bigteam.kosta.bigteamclnt.HashUtil;
//import java.security.MessageDigest;

/**
 * Created on 2016-07-12.
 * Author : CHAE KWANG HOON
 * FileName : FileExplorerActivity.java
 */
public class FileExplorerActivity extends AppCompatActivity {

    private Handler mHandler;
    private String mFileName;
    private ListView lvFileControl;
    private Context mContext = this;

    private List<String> lItem = null;
    private List<String> lPath = null;
    private String mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
    private TextView mPath;
    private String mFileURI;
    private String sha256Hash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();    //  상단타이블바 가리기
        actionBar.hide();                               //  상단타이블바 가리기
        setContentView(R.layout.activity_fileexplorer); // 리스트뷰 초기화

        mPath = (TextView) findViewById(R.id.tvPath);
        mHandler = new Handler();
        lvFileControl = (ListView) findViewById(R.id.lvFileControl); // 어댑터 설정
        getDir(mRoot);
        lvFileControl.setOnItemClickListener(new OnItemClickListener() { //list를 클릭 했을경우 실행되는 코드
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                File file = new File(lPath.get(position));

                if (file.isDirectory()) {
                    if (file.canRead())
                        getDir(lPath.get(position));
                    else {
                        Log.i("Test2", "test2");
                        Toast.makeText(mContext, "No files in this folder.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mFileName = file.getName();
                    mFileURI = lPath.get(position);
                    Log.i("Test", "ext:" + mFileName.substring(mFileName.lastIndexOf('.') + 1, mFileName.length()));
                    Log.i("Test", mFileURI + "777");
                    int toastTime = 1000;

                    if (getExtension(mFileName).toLowerCase().equals("apk")) {  // 확장자가 apk인 경우를 식별하여 토스트로 사용자에게 출력
//                        if (!getExtension(mFileName).toLowerCase().equals("ap")) {  // 테스트 용 -> apk가 아닌 파일도 검색 허용
                        Toast.makeText(getApplicationContext(), mFileName +"을 선택하였습니다.", Toast.LENGTH_SHORT).show();

                        final CharSequence[] items = {"빠른 분석", "정밀 분석", "취소"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(FileExplorerActivity.this);     // 여기서 this는 Activity의 this

                        // 여기서 부터는 알림창의 속성 설정
                        builder.setTitle("분석방법을 선택하세요")        // 제목 설정
                                .setItems(items, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정
                                    public void onClick(DialogInterface dialog, int index){
                                        if (items[index].equals("취소")) {
                                            Toast.makeText(getApplicationContext(), items[index]+"가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                                        } else if (items[index].equals("빠른 분석")){
                                            Toast.makeText(getApplicationContext(), items[index] + "이 선택되었습니다.", Toast.LENGTH_SHORT).show();

//                                            ProgressDialog pDialog = ProgressDialog.show(FileExplorerActivity.this, "빠른 분석",
//                                                    "분석 중입니다. 잠시 기다려주세요", true);


                                            new Thread () {
                                                ProgressDialog pDialog = ProgressDialog.show(FileExplorerActivity.this, "빠른 분석",
                                                    "분석 중입니다. 잠시 기다려주세요", true);
                                                public void run() {
                                                    try  {
                                                        Log.i("Test", "Get Hash value Start!!!" );
                                                        sha256Hash = HashUtil.getSHA256Code(mFileURI);
                                                        Log.i("Test", "sha256Hash_VALUE:"   + sha256Hash);

//                                                        virusTotalApi.detectBySha256(sha256Hash);
                                                        Set<ReportScan> Report = new HashSet<ReportScan>();
                                                        Report = virusTotalApi.detectBySha256Code(sha256Hash);
//                                                        for (ReportScan report : Report) {
//                                                            System.out.println("AV: " + report.getVendor() + " Detected: " + report.getDetected() + " Update: " + report.getUpdate() + " Malware Name: " + report.getMalwarename());
//                                                        }


                                                        pDialog.dismiss();







                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }.start();

                                            // 테스트 앱 해쉬값(sha-256 정보), 테스트 APK파일 명
                                            //  14711411fb3ba975aa7651a8a21605a6c9f45a1704461c9ab9af846f3cf9b4c3
                                            //   aaaaa_VirusShare_00d931896158edaa43552f8c10d1a888.apk

                                        } else {
                                            Toast.makeText(getApplicationContext(), items[index] + "이 선택되었습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        AlertDialog dialog = builder.create();    // 알림창 객체 생성
                        dialog.show();    // 알림창 띄우기

                    } else {
                        Toast.makeText(getApplicationContext(), "apk 파일을 선택해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getDir(String dirPath) {
        mPath.setText("Location: " + dirPath);
//        Log.i("Test", "Location: " + dirPath);

        lItem = new ArrayList<String>();
        lPath = new ArrayList<String>();

        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (!dirPath.equals(mRoot)) {
            //item.add(root); //to root.
            //path.add(root);
            lItem.add("../"); //to parent folder
            lPath.add(f.getParent());
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            lPath.add(file.getAbsolutePath());

            if (file.isDirectory())
                lItem.add(file.getName() + "/");
            else
                lItem.add(file.getName());
        }

        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lItem);
        lvFileControl.setAdapter(fileList);
    }

    public static String getExtension(String fileStr){ // 파일의 확장자를 가져오는 메소드
        return fileStr.substring(fileStr.lastIndexOf(".")+1,fileStr.length());
    }

}