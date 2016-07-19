package com.bigteam.kosta.bigteamclnt;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import virustotalapi.ReportFileScan;
import virustotalapi.ReportScan;
import virustotalapi.VirusTotal;

/**
 * Created by kosta on 2016-07-15.
 */
public class virusTotalApi {

    public static void detectBySha256(String sha256Value) throws Exception {
        Log.i("Test", "--------------------------------");
        Log.i("Test", "virusTotalApi START");
            VirusTotal VT = new VirusTotal("4cd67fa90000d3b29e25251e2267426db5da7529f58c8116708184b7db2216d6"); // 내가 발급 받은 Virus Total API Key (중요하므로 잘 보관 하도록 하자.) - 광훈 -

            try {
                Set<ReportScan> Report = VT.ReportScan(sha256Value); // sha256Value : 대상 apk 파일로부터 sha256 해시 값을 추출하여 사용하도록 한다.

                for (ReportScan report : Report) {

                System.out.println("AV: " + report.getVendor() + " Detected: " + report.getDetected() + " Update: " + report.getUpdate() + " Malware Name: " + report.getMalwarename());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        Log.i("Test", "virusTotalApi Finished");
        Log.i("Test", "--------------------------------");
    }

    public static Set detectBySha256Code(String sha256Value) throws Exception {
        Log.i("Test", "--------------------------------");
        Log.i("Test", "virusTotalApi START");
        VirusTotal VT = new VirusTotal("4cd67fa90000d3b29e25251e2267426db5da7529f58c8116708184b7db2216d6"); // 내가 발급 받은 Virus Total API Key (중요하므로 잘 보관 하도록 하자.) - 광훈 -
        Set<ReportScan> Report = new HashSet<ReportScan>();
        try {
            Report = VT.ReportScan(sha256Value); // sha256Value : 대상 apk 파일로부터 sha256 해시 값을 추출하여 사용하도록 한다.
//            for (ReportScan report : Report) {
//
//                System.out.println("AV: " + report.getVendor() + " Detected: " + report.getDetected() + " Update: " + report.getUpdate() + " Malware Name: " + report.getMalwarename());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Test", "virusTotalApi Finished");
        Log.i("Test", "--------------------------------");
        return Report;
    }
}
