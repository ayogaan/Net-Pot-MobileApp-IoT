package com.example.netplant;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{
    int id,cahaya,ph,air;
    ImageView barcahaya, barph, barair;
    OkHttpClient Client = new OkHttpClient();
    Gson gson = new Gson();
    Timer t1 = new Timer();

    String url = "http://192.168.43.241/netpot/generator.php?id=1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barcahaya = findViewById(R.id.cahaya);
        barph = findViewById(R.id.phmeter);
        barair = findViewById(R.id.air);
        t1.schedule(new update(),0,1000);
    }
    public void httpUpdate(){
        Request request = new Request.Builder()
                .url(url)
                .build();
        Client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("failed");
            }

            @Override
            public void onResponse(Response response) throws IOException {

                System.out.println("can make a http request");
                String responseBody = response.body().string();
                data data = gson.fromJson(responseBody, data.class);
                id = Integer.parseInt(data.id);
                cahaya = Integer.parseInt(data.cahaya);
                ph = Integer.parseInt(data.phmeter);
                air = Integer.parseInt(data.air);
                System.out.println(id);
                System.out.println(cahaya);
                System.out.println(ph);
                System.out.println(air);
                onChangeBar(barcahaya,cahaya);
                onChangeBar(barph,ph);
                onChangeBar(barair,air);



            }
        });
    }
    public void onChangeBar(final ImageView bar, final int value){
        runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                bar.setLayoutParams(new LinearLayout.LayoutParams(value,15));
            }
        }));
    }
    private class update extends TimerTask {
    public void run(){
        httpUpdate();
    }
    }
}
