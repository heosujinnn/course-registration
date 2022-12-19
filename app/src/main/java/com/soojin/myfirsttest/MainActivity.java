package com.soojin.myfirsttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    //어뎁터 연결을 위한 변수
    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList;

    //new 모든 클래스에서 접근 가능하게 함
    public  static String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //smartphone 방향 설정->세로로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //new
        userID=getIntent().getStringExtra("userID");




        //실제 DB와 연동하기 전에 직접 넣어 봄 .
        noticeListView = (ListView) findViewById(R.id.noticeListView);
        noticeList = new ArrayList<Notice>();
//        noticeList.add(new Notice("공지사항입니다.", "허수진", "2022-11-21"));
//        noticeList.add(new Notice("공지사항입니다.", "허수진", "2022-11-21"));
//        noticeList.add(new Notice("공지사항입니다.", "허수진", "2022-11-21"));
        adapter = new NoticeListAdapter(getApplicationContext(), noticeList); //어뎁터에 해당 리스트를 매칭!되어서 차례대로 들어감.
        noticeListView.setAdapter(adapter); //뷰형태로 보여지게 됨.


        final Button courseButton = (Button) findViewById(R.id.courseButton);
        final Button statisticsButton = (Button) findViewById(R.id.statisticsButton);
        final Button scheduleButton = (Button) findViewById(R.id.scheduleButton);
        final LinearLayout notice = (LinearLayout) findViewById(R.id.notice);


        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notice.setVisibility(View.GONE); //공지사항 notice가 보이지 않도록하는 (화면 바꾸는)
                courseButton.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.change));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.change));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new CourseFragment());
                fragmentTransaction.commit();

            }
        });

        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notice.setVisibility(View.GONE); //공지사항 notice가 보이지 않도록하는 (화면 바꾸는)
                courseButton.setBackgroundColor(getResources().getColor(R.color.change));
                statisticsButton.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.change));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new StatisticsFragment());
                fragmentTransaction.commit();

            }
        });

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notice.setVisibility(View.GONE); //공지사항 notice가 보이지 않도록하는 (화면 바꾸는)
                courseButton.setBackgroundColor(getResources().getColor(R.color.change));
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.change));
                scheduleButton.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new ScheduleFragment());
                fragmentTransaction.commit();

            }
        });

        new BackgroundTask().execute();
    }

        //DB연동:
        class BackgroundTask extends AsyncTask<Void,Void,String>{
            String target;

            @Override
            protected void onPreExecute() {
                target="http://sujin0204.ivyro.net/NoticeList.php"; //해당웹서버에 접속
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url=new URL(target);
                    HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                    InputStream inputStream=httpURLConnection.getInputStream(); //url에서 가져와서
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream)); //버퍼에 담아서 읽을 수 있도록
                    String temp;
                    StringBuilder stringBuilder=new StringBuilder();
                    while((temp=bufferedReader.readLine())!=null){
                        stringBuilder.append(temp+"\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();


                }catch (Exception e){
                    e.printStackTrace();
                }

                return null;

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String result) {
                try{
                    JSONObject jsonObject=new JSONObject(result);
                    JSONArray jsonArray=jsonObject.getJSONArray("response");
                    int count=0;
                    String noticeContent, noticeName,noticeDate;
                    while(count<jsonArray.length()){
                        JSONObject object=jsonArray.getJSONObject(count);
                        noticeContent=object.getString("noticeContent");
                        noticeName=object.getString("noticeName");
                        noticeDate=object.getString("noticeDate");
                        Notice notice=new Notice(noticeContent,noticeName,noticeDate);
                        noticeList.add(notice);
                        count++;
                        adapter.notifyDataSetChanged();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        private long lastTimeBackPressed;

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()-lastTimeBackPressed<1500){
            finish();
            return;
        }
        Toast.makeText(this,"한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        lastTimeBackPressed=System.currentTimeMillis();
    }
}
