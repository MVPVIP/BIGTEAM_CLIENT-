package com.bigteam.kosta.bigteamclnt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.ScrollingTabContainerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bigteam.kosta.bigteamclnt.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kosta on 2016-07-18.
 */
public class VTResultPopActivity extends Activity {
    private int resultAllNum;
    private int resultMalwareNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_vtresultpop); // 동적으로 생성하여 쓸모가 없을 수도 있다.

        // VirusTotal 결과를 받을 Intent를 생성
        Intent intent = getIntent();
//        ArrayList resultString = (ArrayList)intent.getSerializableExtra("result");
        ArrayList resultListData = (ArrayList) intent.getSerializableExtra("resultListData");

        Log.i("Test", "resultListData_size:" + resultListData.size());    //->>>>>>>>>>>>>>>주로 쓰는 로그

        if (resultListData.size() == 0) {
            //동적으로 테이블 생성
            LinearLayout linear = new LinearLayout(this);
            linear.setOrientation(LinearLayout.VERTICAL);

            final TableLayout layout = new TableLayout(this);
            layout.setStretchAllColumns(true);

            TableRow row = new TableRow(this);
            TextView column1 = new TextView(this);
            column1.setText("파일을 찾을 수 없습니다. \n 사용자가 찾고 있는 파일이 virustotal의 데이터베이스에 없습니다.");
            column1.setTextColor(Color.RED);
            column1.setTextSize(20);
            column1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            row.addView(column1);

            layout.addView(row);
            linear.addView(layout);
            setContentView(linear);

        } else {
            resultAllNum = resultListData.size();
            resultMalwareNum = 0;
//  resultListData 를 정렬한다.-------------------------------------------------------------------------------------------------------------
            ArrayList tmp1ResultListData = new ArrayList();  // 결과(악성코드값)가 있는 데이터를 1차 소팅하여 담을 리스트.
            ArrayList tmp2ResultListData = new ArrayList();  // 결과(악성코드값)가 없는(정상코드로 판정) 데이터를 1차 소팅하여 담을 리스트.

            for (int i = 0; i < resultListData.size(); i++) {
                HashMap getMap = new HashMap();
                getMap = (HashMap) resultListData.get(i);
                HashMap<String, String> tempResultHashmap = new HashMap<String, String>();
                if (!getMap.get("Detected").toString().equals("null")) {
                    tempResultHashmap = new HashMap<String, String>();
                    tempResultHashmap.put("AV", getMap.get("AV").toString());
                    tempResultHashmap.put("Detected", getMap.get("Detected").toString());
                    tempResultHashmap.put("update", getMap.get("update").toString());
                    tmp1ResultListData.add(tempResultHashmap);
                    resultMalwareNum++;
                } else {
                    tempResultHashmap = new HashMap<String, String>();
                    tempResultHashmap.put("AV", getMap.get("AV").toString());
                    tempResultHashmap.put("Detected", getMap.get("Detected").toString());
                    tempResultHashmap.put("update", getMap.get("update").toString());
                    tmp2ResultListData.add(tempResultHashmap);
                }
            }
            MapComparator comp = new MapComparator("AV");
//            comp = new MapComparator("AV");
            Collections.sort(tmp1ResultListData, comp);
            Collections.sort(tmp2ResultListData, comp);

            resultListData = new ArrayList();
            for (int i = 0; i < tmp1ResultListData.size(); i++) {
                HashMap getMap = new HashMap();
                getMap = (HashMap) tmp1ResultListData.get(i);
                HashMap<String, String> tempResultHashmap = new HashMap<String, String>();
                tempResultHashmap = new HashMap<String, String>();
                tempResultHashmap.put("AV", getMap.get("AV").toString());
                tempResultHashmap.put("Detected", getMap.get("Detected").toString());
                tempResultHashmap.put("update", getMap.get("update").toString());
                resultListData.add(tempResultHashmap);
            }
            for (int i = 0; i < tmp2ResultListData.size(); i++) {
                HashMap getMap = new HashMap();
                getMap = (HashMap) tmp2ResultListData.get(i);
                HashMap<String, String> tempResultHashmap = new HashMap<String, String>();
                tempResultHashmap = new HashMap<String, String>();
                tempResultHashmap.put("AV", getMap.get("AV").toString());
                tempResultHashmap.put("Detected", getMap.get("Detected").toString());
                tempResultHashmap.put("update", getMap.get("update").toString());
                resultListData.add(tempResultHashmap);
            }

//        Log.i("Test", "resultListData + null:"   + resultListData);    //->>>>>>>>>>>>>>>주로 쓰는 로그
//  resultListData 를 정렬종료.-------------------------------------------------------------------------------------------------------------

            ScrollView sv = new ScrollView(this);
            sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            //동적으로 테이블 생성
            LinearLayout linear = new LinearLayout(this);
            linear.setOrientation(LinearLayout.VERTICAL);

            final TableLayout layout = new TableLayout(this);
            layout.setStretchAllColumns(true);

            TableRow row = new TableRow(this);
            TableRow row0 = new TableRow(this);

            // 탐지 비율 을 나타내는 부분
            TextView rateTitle = new TextView(this);
            rateTitle.setTextSize(20);
            rateTitle.setText("탐지 비율:   ");
            rateTitle.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            row0.addView(rateTitle);

            TextView rateContent = new TextView(this);
            rateContent.setTextSize(20);
            rateContent.setText(resultMalwareNum + "/" + resultAllNum);
            rateContent.setTextColor(Color.RED);
            row0.addView(rateContent);
            layout.addView(row0);
            // 탐지 비율 을 나타내는 부분

            TextView column1 = new TextView(this);
            column1.setText("안티바이러스");
            row.addView(column1);

            TextView column2 = new TextView(this);
            column2.setText("결과");
            row.addView(column2);

            TextView column3 = new TextView(this);
            column3.setText("업데이트");
            row.addView(column3);
            layout.addView(row);

            sv.addView(linear);
            for (int i = 0; i < resultListData.size(); i++) {
                TableRow row2 = new TableRow(this);

                TextView textViewColumn1 = new TextView(this);
                HashMap getMap = new HashMap();
                getMap = (HashMap) resultListData.get(i);
                textViewColumn1.setText(getMap.get("AV").toString());

                TextView textViewColumn2 = new TextView(this);
                String Detected = getMap.get("Detected").toString();
                if (!Detected.equals("null")) { // 검사결과 악성코드로 분석된 경우 텍스트 색을 붉은색으로 처리
                    textViewColumn2.setTextColor(Color.RED);
                    textViewColumn2.setText(Detected);
                } else {  // 검사결과 비악성코드로 분석된 경우 텍스트 색을 녹색으로 처리, 결과값이 null일 경우 '-'로 바꾸어서 출력
                    textViewColumn2.setTextColor(Color.GREEN);
                    textViewColumn2.setText("-");
                }

                TextView textViewColumn3 = new TextView(this);
                textViewColumn3.setText(getMap.get("update").toString());

                row2.addView(textViewColumn1);
                row2.addView(textViewColumn2);
                row2.addView(textViewColumn3);

                layout.addView(row2);
//            Log.e("TAG", "add Row2");
            }

            linear.addView(layout);
//        setContentView(linear);
            this.setContentView(sv);
        }
    }
    public void onClickResultClose(View view) { // 팝업창 닫기
       this.finish();
    }
}

class MapComparator implements Comparator<HashMap<String, String>> {

    private final String key;

    public MapComparator(String key) {
        this.key = key;
    }

    @Override
    public int compare(HashMap<String, String> first, HashMap<String, String> second) {
        int result = first.get(key).compareTo(second.get(key));
        return result;
    }
}