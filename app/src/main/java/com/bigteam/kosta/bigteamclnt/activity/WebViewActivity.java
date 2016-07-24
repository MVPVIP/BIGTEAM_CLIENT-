package com.bigteam.kosta.bigteamclnt.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bigteam.kosta.bigteamclnt.R;

/**
 * Created by kosta on 2016-07-21.
 */
public class WebViewActivity extends AppCompatActivity {
    private WebView analysisResultWebView;
    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview); // 리스트뷰 초기화

        Intent intent = getIntent();
        String apk_md5_name = (String)intent.getSerializableExtra("apk_md5_name");
        String analysisResultHtmlUrl = "http://192.168.0.146/result/"+apk_md5_name +".html";
        Log.i("Test", "analysisResultHtmlUrl:"   + analysisResultHtmlUrl);

        analysisResultWebView = (WebView) findViewById(R.id.webview);
        analysisResultWebView.getSettings().setJavaScriptEnabled(true);
        analysisResultWebView.getSettings().setSupportZoom(true);

        analysisResultWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String pageTitle = analysisResultWebView.getTitle();
                Log.i("TEST", "pageTitle : " + pageTitle);
                String[] separated = pageTitle.split("-");
                if(separated[0].equals("404 Not Found")) {
                    Toast.makeText(getApplicationContext(), "정밀분석이 아직 진행 중입니다. 잠시 후에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.i("TEST", "ELSE detect page not found error 404");
                }
            }
        });

        analysisResultWebView.loadUrl(analysisResultHtmlUrl);
        // WebViewClient 지정
//        analysisResultWebView.setWebViewClient(new WebViewClientClass());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && analysisResultWebView.canGoBack()) {
            analysisResultWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
