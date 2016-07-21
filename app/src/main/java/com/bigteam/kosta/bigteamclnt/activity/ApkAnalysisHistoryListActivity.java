package com.bigteam.kosta.bigteamclnt.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by kosta on 2016-07-20.
 * 액티비티 기능 설명 : 정밀 분석기능을 선택하여 <보유 APK파일 목록 -> 분석 대상 APK파일 선택 -> 정밀 분석 선택 -> 분석서버로 파일전송>  < > 안의 의 과정을 거치면  분석 대상 파일의 분석 이력을 DB에 저장하게 된다.
 *                      이 액티비티는 이 이력을 DB(안드로이드 내부 DB) 에서 불러와서 리스트 형식으로 보여주고 각 리스트 항목을 선택할 수 있게 한다, ( 선택 시 ApkAnalysisResultWebViewActivity로 이동하여 결과를 보여줌)
 */
public class ApkAnalysisHistoryListActivity extends AppCompatActivity {
}
