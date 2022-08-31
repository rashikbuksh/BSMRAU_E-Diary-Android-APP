package com.tostechllc.bsmrau_e_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class notices extends AppCompatActivity {

    public static final String FETCH_NOTICES = "https://tostechllc.com/android/getNotices.php";
    ArrayList<listedNotice> arrayListNotice;
    ListView noticeListView;
    customNoticeAdapter customNoticeAdapter;
    ImageButton back, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        noticeListView = findViewById(R.id.notice_listView);

        arrayListNotice = new ArrayList<>();

        if(checkNetworkConnection()){
            fetchNotice();
        }
        else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        home = findViewById(R.id.btn_home);

        home.setOnClickListener(view -> {
            Intent intent = new Intent(this, home.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);;
            startActivity(intent);
            finish();
        });
        back = findViewById(R.id.btn_back);
        back.setOnClickListener(view -> onBackPressed());

    }

    protected void onRestart() {

        super.onRestart();
    }

    public void fetchNotice(){
        @SuppressLint("StaticFieldLeak")
        class dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data){
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    for(int i =0;i<ja.length();i++){
                        jo=ja.getJSONObject(i);
                        int id = jo.getInt("id");
                        String noticeimage = jo.getString("noticeimage");
                        String noticeheading = jo.getString("noticeheading");
                        String details = jo.getString("details");
                        String created_on = jo.getString("created_on");
                        int status = jo.getInt("status");

                        System.out.println(id+" "+noticeimage+" "+noticeheading);

                        listedNotice listedNotice = new listedNotice(noticeimage,noticeheading,details,created_on,id,status);

                        //System.out.println("directory: "+postedDirectorysingle.id);

                        arrayListNotice.add(listedNotice);

                        loadDatainList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer data = new StringBuffer();
                    String line;

                    while((line=br.readLine())!=null){
                        data.append(line+"\n");
                    }
                    br.close();
                    return data.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        dbManager obj =new dbManager();
        obj.execute(FETCH_NOTICES);
    }
    public void loadDatainList(){
        customNoticeAdapter = new customNoticeAdapter(this,arrayListNotice);
        noticeListView.setAdapter(customNoticeAdapter);
        customNoticeAdapter.notifyDataSetChanged();
    }
    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}