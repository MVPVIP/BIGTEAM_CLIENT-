package com.bigteam.kosta.bigteamclnt;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kosta on 2016-07-20.
 */

public class SqlLiteDBManger extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public SqlLiteDBManger(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */

        /* 테이블의 이름은 ApkAnalysisHistory, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        apk_name 문자열 컬럼, apk_md5_name 문자열 컬럼, create_at 문자열 컬럼, flag 문자열 컬럼 으로 구성된 테이블을 생성. */
//        db.execSQL("CREATE TABLE MONEYBOOK (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, price INTEGER, create_at TEXT);");
        db.execSQL("CREATE TABLE ApkAnalysisHistory (_id INTEGER KEY AUTOINCREMENT, apk_name TEXT PRIMARY KEY, apk_md5_name TEXT PRIMARY KEY, create_at TEXT, flag TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String create_at, String apk_name, String apk_md5_name, String flag) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO ApkAnalysisHistory VALUES(null, '" + apk_name + "', '" + apk_md5_name + "', '" + create_at + "','" + flag +"');" );
        db.close();
        Log.i("Test", "insertDB FINISH !!!!!!!!!!!!" );
    }

    public void update(String create_at, String apk_name, String apk_md5_name) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정

        // 정밀검사를 선택하였을 때 (분석서버로 파일을 전송할 때) 파일의 정보가 기저장된 정보와 일치할 경우 DB의 정보를 일부 업데이트 한다.
        db.execSQL("UPDATE ApkAnalysisHistory SET create_at=" + create_at + " WHERE apk_name='" + apk_name + "' AND apk_md5_name='" + apk_md5_name + "';");
        db.close();
    }

    public void delete(String apk_name, String apk_md5_name) {
        SQLiteDatabase db = getWritableDatabase();
        // 선택한 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM ApkAnalysisHistory WHERE apk_name='" + apk_name + "' AND apk_md5_name='" + apk_md5_name + "';");
        db.close();
    }

    public Cursor getApkAnalysisHistory() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList resultList = null;

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM ApkAnalysisHistory ORDER BY create_at desc ", null);
//        while (cursor.moveToNext()) {
////            result += cursor.getString(0)
////                    + " : "
////                    + cursor.getString(1)
////                    + " | "
////                    + cursor.getInt(2)
////                    + "원 "
////                    + cursor.getString(3)
////                    + "\n";
//            resultList.add()
//        }
//        while (cursor.moveToNext()) {
//            // 아마도 ArrayList로 보여줘야 될 것으로 생각됨.
//            // 추후 개발 할 것.
//        }

        return cursor;
    }

    public Boolean getAnalysisHistoryByMD5Name(String apk_name, String apk_md5_name ) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM ApkAnalysisHistory WHERE apk_name='" + apk_name + "' AND apk_md5_name='" + apk_md5_name + "';", null);
        Log.i("Test", "getAnalysisHistoryByMD5Name RESULT :   " + cursor );
        int countResult = cursor.getCount();
        Log.i("Test", "getAnalysisHistoryByMD5Name RESULT countResult :   " + countResult );
        if ( countResult < 1) {
            return false;   // false 일 때는 신규 분석 대상 데이터로 간주
        } else return true; // true 일 때 기저장된 데이터가 있는 경우
    }

    public void updateAnalysisHistory(String create_at, String apk_name, String apk_md5_name) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정

        // 정밀검사를 선택하였을 때 (분석서버로 파일을 전송할 때) 파일의 정보가 기저장된 정보와 일치할 경우 DB의 정보를 일부 업데이트 한다.
        db.execSQL("UPDATE ApkAnalysisHistory SET create_at='" + create_at + "' WHERE apk_name='" + apk_name + "' AND apk_md5_name='" + apk_md5_name + "';");
        db.close();
    }
}