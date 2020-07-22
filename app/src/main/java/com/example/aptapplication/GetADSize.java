package com.example.aptapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetADSize extends AsyncTask<Integer,Integer,Integer> {
    URL url = null;
    String json = null;
    HttpURLConnection conn = null;
    OutputStream os = null;
    @Override
    protected void onPreExecute() {
        try{
        url =new URL("http://121.139.55.207:8000/adRest/getAdSize");
            JSONObject jo = new JSONObject();
            jo.put("aptId", "2b2b76a7-491e-4579-a6c5-79b94eca55a8");
            json = jo.toString();
            conn =
                    (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
            conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정
            conn.setDoOutput(true);
            conn.setDoInput(true);


        }catch (Exception e){
            e.printStackTrace();
        }
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        while(isCancelled()==false){


        try {

            os = conn.getOutputStream();
            os.write(json.getBytes("euc-kr"));
            os.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuffer sb = new StringBuffer();
            String jsonData;
            String returnText;
            while ((jsonData = br.readLine()) != null) {
                sb.append(jsonData);
            }

            returnText = sb.toString();

            os.close();
            br.close();

            Thread.sleep(10000);


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ERROR", e.toString());
        }

        }
        return null;
    }

    protected void onPostExecute(Integer result) {
        conn.disconnect();

        Log.i("GET AD SIZE","SUCCESS GET AD SIZE");
    }

}
