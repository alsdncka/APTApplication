package com.example.aptapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;


public class MainView extends AppCompatActivity {

    Handler handler = new Handler();

    Thread imageThread;
    boolean imageThreadStopFlag =false;
    Thread scrollThread;

    boolean scrollThreadStopFlag =false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideBottomSoftKey();
        hideUpperStateExpression();
        setContentView(R.layout.view);


        getAd();




    }



    public void getAd(){


        DisplayMetrics dm =getApplicationContext().getResources().getDisplayMetrics();
        final int width=dm.widthPixels;
        final int height = dm.heightPixels;

        imageThread = new Thread(){
            public void run() {

                while (!imageThreadStopFlag) {


                    try{
                        scrollThreadStopFlag=true;

                        if(scrollThread!=null){

                            while(scrollThread.isAlive()){
                                imageThread.sleep(3000);

                            }

                        }
                        Set<Thread> threads = Thread.getAllStackTraces().keySet();

                        for (Thread t : threads) {
                            String name = t.getName();
                            String id = String.valueOf(t.getId());
                            String state = String.valueOf(t.getState());
                            int priority = t.getPriority();
                           Log.w("THREAD",name +"\t"+id+"\t"+state+"\t"+priority);


                        }
                        int size = getAdSize();


                        final String notiText = getAptNoti();
                        for (int i = 0; i < size; i++) {
                            final int n = i;

                            final ImageView view = new ImageView(MainView.this);
                            final String url = "http://121.139.55.207:8000/adRest/getAdImage?aptId=2b2b76a7-491e-4579-a6c5-79b94eca55a8&adNum="+i;

                            view.setAdjustViewBounds(true);
                            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                            final FrameLayout imageBox = new FrameLayout(MainView.this);
                            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
                            imageBox.setLayoutParams(lp);
                            imageBox.addView(view);




                            handler.post(new Runnable() {

                                @Override
                                public void run() {

                                    LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
                                    if(n==0){
                                        ll.removeAllViews();
                                        TextView tv = (TextView)findViewById(R.id.textView2);
                                        tv.setSelected(true);
                                        tv.setText(notiText);
                                    }
                                    RequestOptions op = new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);

                                    Glide.with(MainView.this).load(url).apply(op).into(view);

                                    ll.addView(imageBox);

                                }
                            });
                        }

                        autoScroll(size);
                        try{
                            imageThread.sleep(180000);
                            Log.i("IMAGE THREAD","SUCCESS RELOAD IMAGE");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }
        };
        imageThread.start();
        //.start();

    }




    public void autoScroll(final int size){
        DisplayMetrics dm =getApplicationContext().getResources().getDisplayMetrics();
        final int width=dm.widthPixels;
        scrollThread = new Thread(new Runnable() {
            @Override
            public void run() {
                scrollThreadStopFlag=false;
                while(!scrollThreadStopFlag){
                    HorizontalScrollView sc = (HorizontalScrollView) findViewById(R.id.sc);
                    sc.pageScroll(HorizontalScrollView.FOCUS_RIGHT);
                    if(((size-1)*width)==sc.getScrollX()){
                        sc.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    }

                    try{
                        scrollThread.sleep(7000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }
        });


        scrollThread.start();

    }

    public int getAdSize(){
        try {
            URL url = new URL("http://121.139.55.207:8000/adRest/getAdSize");
            JSONObject jo = new JSONObject();
            jo.put("aptId", "2b2b76a7-491e-4579-a6c5-79b94eca55a8");
            String json = jo.toString();
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
            conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream os = conn.getOutputStream();
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
            conn.disconnect();
            os.close();
            br.close();


            return Integer.parseInt(returnText);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ERROR", e.toString());
        }
        return -1;
    }

    public String getAptNoti(){
        try {
            URL url = new URL("http://121.139.55.207:8000/adRest/getAptNoti");
            JSONObject jo = new JSONObject();
            jo.put("aptId", "2b2b76a7-491e-4579-a6c5-79b94eca55a8");
            String json = jo.toString();
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
            conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream os = conn.getOutputStream();
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
            conn.disconnect();
            os.close();
            br.close();

            Log.i("NOTI",returnText);
            return returnText;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ERROR", e.toString());
        }
        return null;
    }

    public void hideUpperStateExpression(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }



    public void hideBottomSoftKey(){
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }
        // 몰입 모드를 꼭 적용해야 한다면 아래의 3가지 속성을 모두 적용시켜야 합니다
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

    }



}
