package com.example.aptapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdTask extends AsyncTask<Integer,Integer,Integer> {


    @Override
    protected void onPreExecute() {



    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        int ms = integers[0];
        int width = integers[1];
        int height = integers[2];
         GetADSize size = new GetADSize();
         size.execute();


        return null;
    }



    public Bitmap getImage(int num){
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://121.139.55.207:8000/adRest/getAdImage");
            JSONObject jo = new JSONObject();
            jo.put("aptId", "2b2b76a7-491e-4579-a6c5-79b94eca55a8");
            jo.put("adNum", String.valueOf(num));
            String json = jo.toString();
            conn =
                    (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
            conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("euc-kr"));
            os.flush();
            os.close();
            InputStream is =conn.getInputStream();
            Bitmap bit = BitmapFactory.decodeStream(is);

            is.close();

            return bit;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ERROR", e.toString());
        }finally {
            if(conn!=null){
                conn.disconnect();
            }
        }

        return null;
    }

}
