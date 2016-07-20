package com.bigteam.kosta.bigteamclnt.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import virustotalapi.ReportScan;

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

    TextView messageText;
    String upLoadServerUri = null;
    int serverResponseCode = 0;

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
                    Log.i("Test", mFileURI);

                    if (getExtension(mFileName).toLowerCase().equals("apk")) {  // 확장자가 apk인 경우를 식별하여 토스트로 사용자에게 출력
//                        if (!getExtension(mFileName).toLowerCase().equals("ap")) {  // 테스트 용 -> apk가 아닌 파일도 검색 허용
                        Toast.makeText(getApplicationContext(), mFileName +"을 선택하였습니다.", Toast.LENGTH_SHORT).show();

                        final CharSequence[] items = {"빠른 분석", "정밀 분석", "취소"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(FileExplorerActivity.this);     // 여기서 this는 Activity의 this

                        // 여기서 부터는 알림창의 속성 설정
                        builder.setTitle("분석방법을 선택하세요")        // 제목 설정
                                .setItems(items, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정
                                    public void onClick(DialogInterface dialog, final int index){
                                        if (items[index].equals("취소")) {
                                            Toast.makeText(getApplicationContext(), items[index]+"가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                                        } else if (items[index].equals("빠른 분석")){
                                            Toast.makeText(getApplicationContext(), items[index] + "이 선택되었습니다.", Toast.LENGTH_SHORT).show();

                                            new Thread () {
                                                ProgressDialog pDialog = ProgressDialog.show(FileExplorerActivity.this, "빠른 분석",
                                                    "분석 중입니다. 잠시 기다려주세요", true);
                                                public void run() {
                                                    try  {
                                                        Log.i("Test", "Get Hash value Start!!!" );
                                                        sha256Hash = HashUtil.getSHA256Code(mFileURI);
                                                        Log.i("Test", "sha256Hash_VALUE:"   + sha256Hash);

                                                        Set<ReportScan> Report = new HashSet<ReportScan>();
                                                        Report = virusTotalApi.detectBySha256Code(sha256Hash);

                                                        ArrayList resultListData = new ArrayList();
                                                        HashMap<String, String> resultHashmap = new HashMap<String, String>();
//                                                        Log.i("Test", "Report Result:"   + Report);
                                                        for (ReportScan report : Report) {
                                                            resultHashmap = new HashMap<String, String>();
                                                            resultHashmap.put("AV",report.getVendor());
                                                            resultHashmap.put("Detected",report.getMalwarename());
                                                            resultHashmap.put("update",report.getUpdate());
//                                                            resultListData.add(resultHashmap);
                                                            resultListData.add(index, resultHashmap);
//                                                            Log.i("Test", "for - resultListData:"   + resultListData);
                                                        }
                                                        Intent intent = new Intent(getApplicationContext(), VTResultPopActivity.class);
                                                        intent.putExtra("resultListData", resultListData);

                                                        startActivity(intent);
                                                        pDialog.dismiss();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }.start();
                                        } else { // 정밀분석
                                            Toast.makeText(getApplicationContext(), items[index] + "이 선택되었습니다.", Toast.LENGTH_SHORT).show();

                                            new Thread () {
                                                ProgressDialog pDialog = ProgressDialog.show(FileExplorerActivity.this, "정밀 분석",
                                                        "APK파일을 분석서버로 전송 중입니다. 잠시 기다려주세요", true);
                                                public void run() {
                                                    try  {
                                                        String tempMD5APKName = HashUtil.getMD5Code(mFileURI) + ".apk";
//                                                        Log.i("Test", "tempMD5APKName:"   + tempMD5APKName);
//                                                        Log.i("Test", "tempMD5APKName:"   + mRoot + mFileName);

                                                        File filePre = new File(getFileDirPath(mFileURI), mFileName );
                                                        File fileNow = new File(getFileDirPath(mFileURI), tempMD5APKName);
                                                        Log.i("Test", "filePre:"   + filePre);
                                                        Log.i("Test", "fileNow:"   + fileNow);


                                                        byte[] buf = new byte[1024];
                                                        FileInputStream fin = null;
                                                        FileOutputStream fout = null;

//                                                        if(!filePre.renameTo(fileNow)){ // 파일의 이름을 해시코드(MD5)로 변경하는 것을 실패했을 경우의 대비코드.
                                                            Log.i("Test", "renameTo");
                                                            buf = new byte[1024];
                                                            fin = new FileInputStream(filePre);
                                                            fout = new FileOutputStream(fileNow);
                                                            int read = 0;
                                                            while((read=fin.read(buf,0,buf.length))!=-1){
                                                                fout.write(buf, 0, read);
                                                            }
                                                            fin.close();
                                                            fout.close();
//                                                            filePre.delete();
//                                                        }




//                                                        if(filePre.renameTo(fileNow)){
//                                                            Toast.makeText(getApplicationContext(), "변경 성공", Toast.LENGTH_SHORT).show();
//                                                        }else{
//                                                            Toast.makeText(getApplicationContext(), "변경 실패", Toast.LENGTH_SHORT).show();
//                                                        }
//
//                                                        final String uploadFilePath = mFileURI; ///**********  File Path *************/
                                                        final String uploadFilePath = getFileDirPath(mFileURI)+ tempMD5APKName; ///**********  File Path *************/
//                                                        Log.i("Test", "uploadFilePath(MD5):"   + uploadFilePath);


//                                                        int serverResponseCode = 0;
//                                                        String upLoadServerUri = null;
//                                                        final TextView messageText;
                                                        messageText  = (TextView)findViewById(R.id.messageText);

                                                        /************* Php script path ****************/
                                                        upLoadServerUri = "http://192.168.0.146/upload.php";//서버컴퓨터의 ip주소
                                                        new Thread(new Runnable() {
                                                            public void run() {
                                                                runOnUiThread(new Runnable() {
                                                                    public void run() {
                                                                        messageText.setText("uploading started.....");
                                                                    }
                                                                });

                                                                uploadFile(uploadFilePath);
                                                            }
                                                        }).start();



                                                        pDialog.dismiss();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }.start();
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

    public int uploadFile(final String sourceFileUri) {
        Log.i("Test", "uploadFile START ...........FileURI:"   + sourceFileUri);
       String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

//            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    +sourceFileUri );

            runOnUiThread(new Runnable() {
                public void run() {
                    messageText.setText("Source File not exist :"
                            +sourceFileUri);
                }
            });
            return 0;
        }
        else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +sourceFileUri;

                            messageText.setText(msg);
                            Toast.makeText(FileExplorerActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

//                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(FileExplorerActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

//                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(FileExplorerActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file Exception", "Exception : "
                        + e.getMessage(), e);
            }
//            dialog.dismiss();
            File fileNow = new File(fileName);
            fileNow.delete();


            return serverResponseCode;

        } // End else block
    }


    public static String getFileName(String fullPath) {
        int S = fullPath.lastIndexOf("\\");
        int M = fullPath.lastIndexOf(".");
        int E = fullPath.length();

        String filename = fullPath.substring(S+1, M);
        String extname = fullPath.substring(M+1, E);

        String extractFileName = filename + "." + extname;
        return extractFileName;
    }

    public static String getFileDirPath(String fullPath) {
        Log.i("Test2", "fullPath: " + fullPath);
        int S = fullPath.lastIndexOf("/");
        int M = fullPath.lastIndexOf(".");
        int E = fullPath.length();

        String fileDirPath = fullPath.substring(0, S+1);
        Log.i("Test2", "fileDirPath: " + fileDirPath);
        return fileDirPath;
    }


}