package com.check.corona_prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.check.corona_prototype.Request.ScanRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class ScanQR extends AppCompatActivity {
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("id");
        IntentIntegrator qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경
        qrScan.setPrompt("QR코드를 스캔해주세요.");
        qrScan.setBeepEnabled(false); // 소리여부
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                finish();
            }
            else {
                // 시간과 날짜 가져오기
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String time = sdfNow.format(date);

                // QR코드 값 변수대입
                String[] temp = result.getContents().split("/");
                Double la = Double.valueOf(temp[0]);
                Double lo = Double.valueOf(temp[1]);
                String store = String.valueOf(temp[2]);

                Log.d("시간", time);
                Log.d("QR값1", la+"");
                Log.d("QR값2", lo+"");
                Log.d("QR값3", store);

                saveRoute(time, la, lo, store);

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
        }
    }
    private void saveRoute(String time, Double la, Double lo, String store){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) { // 스캔 성공
//                                Toast.makeText(getApplicationContext(), "스캔 성공", Toast.LENGTH_SHORT).show();

                    } else { // 스캔 실패
                        Toast.makeText(getApplicationContext(), "스캔 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ScanRequest scanRequest = new ScanRequest(id, time, la, lo, store,responseListener);
        RequestQueue queue = Volley.newRequestQueue(ScanQR.this);
        queue.add(scanRequest);

//                Toast.makeText(getApplicationContext(), "스캔 성공 \n" + time + "\n" +
//                        "위도: " + la + "\n" + "경도: " + lo + "\n" + "Store: " + store,  Toast.LENGTH_SHORT).show();


        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {}
        }, 2000);
//                Toast.makeText(getApplicationContext(), "스캔 성공", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("store", store);
        setResult(RESULT_OK, intent);
        finish();
    }
}