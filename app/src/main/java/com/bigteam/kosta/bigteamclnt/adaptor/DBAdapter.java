package com.bigteam.kosta.bigteamclnt.adaptor;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigteam.kosta.bigteamclnt.R;

public class DBAdapter extends CursorAdapter {


    public DBAdapter(Context context, Cursor c) {
        super(context, c);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final TextView apk_name = (TextView)view.findViewById(R.id.apk_name);
        final TextView apk_md5_name = (TextView)view.findViewById(R.id.apk_md5_name);
        final TextView create_at = (TextView)view.findViewById(R.id.create_at);

        apk_name.setText("파일명\t\t\t:\t"+cursor.getString(cursor.getColumnIndex("apk_name"))+ ".apk");
        apk_md5_name.setText("해시값\t\t\t:\t"+cursor.getString(cursor.getColumnIndex("apk_md5_name")));
        create_at.setText("분석날짜\t\t\t\t:\t"+cursor.getString(cursor.getColumnIndex("create_at")));
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_layout, parent, false);
        return v;
    }
}