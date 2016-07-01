package com.smsfilter.congybk.smsfilter;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by congybk on 25/06/2016.
 */
public class ServiceSMSReceive extends Service {

    private BroadcastReceiver mReceive = null;
    private BroadcastReceiver mReceiveWifi;
    private BroadcastReceiver mReceiveConnect;
    private IntentFilter mIntentFilter;
    private IntentFilter mIntentFilterWifi;
    private IntentFilter mIntentFilterConnet;
    private ContentValues mValues = new ContentValues();

    private List<Message> mListMessage = new ArrayList<>();

    public ServiceSMSReceive(){

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("CREATE","onCreate");
        Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_SHORT).show();
        mIntentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        mIntentFilterWifi = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
        mIntentFilterConnet = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        mReceive = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                processReceive(context,intent);
            }
        };

        mReceiveWifi = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(isNextWork()) {
                    Toast.makeText(context, "có Wifi", Toast.LENGTH_LONG).show();
                }
            }
        };

        mReceiveConnect = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(isNextWork()) {
                    Toast.makeText(context, "có 3g", Toast.LENGTH_LONG).show();
                    int size = mListMessage.size();
                    Date today=new Date(System.currentTimeMillis());
                    SimpleDateFormat timeFormat= new SimpleDateFormat("yyyy/MM/dd");
                    String date =timeFormat.format(today.getTime());
                    for(int i=0;i<size;i++){
                        Toast.makeText(context, "Gửi tin", Toast.LENGTH_LONG).show();
                        mValues.put("number",mListMessage.get(i).getNumber());
                        mValues.put("content",mListMessage.get(i).getContent());
                        mValues.put("date",date);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new ThreadTask().execute("http://congybk.96.lt");
                            }
                        });
                    }
                }
            }
        };
    }
    private void processReceive(Context context, Intent intent) {
        Toast.makeText(context,"Có tin nhắn",Toast.LENGTH_LONG).show();
        Bundle bundle = intent.getExtras();

        if(bundle!=null){
            Object []smsExtras = (Object[]) bundle.get("pdus");
            for(int i=0;i<smsExtras.length;i++){
                SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtras[i]);
                String bodySMS = sms.getMessageBody();
                String number = sms.getOriginatingAddress();
                Message message = new Message(number,bodySMS);
                mListMessage.add(message);
            }
      //      deleteSMS(context,message);
      //      Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_SHORT).show();
        registerReceiver(mReceive,mIntentFilter);
        registerReceiver(mReceiveConnect,mIntentFilterConnet);
        registerReceiver(mReceiveWifi,mIntentFilterWifi);
        return START_STICKY;
    }
    public void deleteSMS(Context context,String message){
        if(message.contains("abc")){
            MediaPlayer player = MediaPlayer.create(context,R.raw.mymusic);
            player.start();
        }
    }
    public boolean isNextWork(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private String makePostRequest(String duongdan) {
        String queryResult = null;
        try {

            URL url = new URL(duongdan);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //add parameters
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream dout = new DataOutputStream(connection.getOutputStream());
            //write OutputStream
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(mValues));
            writer.flush();
            writer.close();
            os.close();

            InputStream in = new BufferedInputStream(connection.getInputStream());
            queryResult = readStream( in );
            connection.disconnect();

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return queryResult;
    }
    private String getQuery(ContentValues values) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : values.valueSet()){
            if (first)
                first = false;
            else
                result.append("&");

            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode((String) entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    @SuppressLint("NewApi")
    private String readStream(InputStream iStream) throws IOException {

        //Buffered reader allows us to read lne by line
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bReader.readLine()) != null) { //Read till end
                builder.append(line);
            }
            return builder.toString();
        }
    }

    public class ThreadTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            return makePostRequest((String)objects[0]);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
}



