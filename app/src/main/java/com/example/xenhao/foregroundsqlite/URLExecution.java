package com.example.xenhao.foregroundsqlite;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by XenHao on 18/12/2014.
 */
public class URLExecution extends IntentService {

    public URLExecution() {
        super("URLCallingThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {  Log.i("IntentService", "IntentService started to call URL");

        //  testing call to external webpage
        try{
            URL url = new URL("https://www.google.com");
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            readStream(con.getInputStream());
        } catch (Exception e){      Log.i("Foreground URL Call", "readStream error");
            e.printStackTrace();
        }
    }

    private void readStream(InputStream inputStream) {
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null){
                System.out.println(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try{
                    reader.close();     Log.i("Foreground URL Call", "Process done and closed");
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
