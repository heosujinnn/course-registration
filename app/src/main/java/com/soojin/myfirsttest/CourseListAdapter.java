package com.soojin.myfirsttest;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.AlreadyBoundException;
import java.util.ArrayList;
import java.util.List;

//course 어뎁터
public class CourseListAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;
    private Fragment parent;
    //시간표 중복
    private String userID=MainActivity.userID; //메인 액티비티에 있는 (public)형태의 유저 아이디를 가져와서 스트링형태로 넣어줌
    private Schedule schedule=new Schedule();
    private List<Integer>courseIDList;

    //학점 제한
    public static int totalCredit=0;


    public CourseListAdapter(Context context, List<Course> courseList,Fragment parent) {
        this.context = context;
        this.courseList = courseList;
        this.parent=parent;

        schedule=new Schedule(); //변수 초기화
        courseIDList=new ArrayList<Integer>();//아이디리스트로 어레이리스트로 초기화
        new BackgroundTask().execute();
        totalCredit=0;
    }

    @Override
    public int getCount() {
        return courseList.size(); //갯수
    }

    @Override
    public Object getItem(int i) {
        return courseList.get(i); //해당위치에  반환하도록함.
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v=View.inflate(context,R.layout.course,null); //하나의 뷰를 만들고 course의 디자인 사용
        TextView courseGrade=(TextView)v.findViewById(R.id.courseGrade);
        TextView courseTitle=(TextView)v.findViewById(R.id.courseTitle);
        TextView courseCredit=(TextView)v.findViewById(R.id.courseCredit);
        TextView courseDivide=(TextView)v.findViewById(R.id.courseDivide);
        TextView coursePersonnel=(TextView)v.findViewById(R.id.coursePersonnel);
        TextView courseProfessor=(TextView)v.findViewById(R.id.courseProfessor);
        TextView courseTime=(TextView)v.findViewById(R.id.courseTime);

        if(courseList.get(i).getCourseGrade().equals("제한없음")||courseList.get(i).getCourseGrade().equals("")){
            courseGrade.setText("모든학년");
        }
        else{
            courseGrade.setText(courseList.get(i).getCourseGrade()+"학년");

        }

        courseTitle.setText(courseList.get(i).getCourseTitle());
        courseCredit.setText(courseList.get(i).getCourseCredit()+"학점");
        courseDivide.setText(courseList.get(i).getCourseDivide()+"분반");


        if(courseList.get(i).getCoursePersonnel()==0){
            courseGrade.setText("제한없음");

        }
        else{
            coursePersonnel.setText("제한인원:"+courseList.get(i).getCoursePersonnel()+"명");

        }

        if(courseList.get(i).getCourseProfessor().equals("")){
            //교수님 이름이 공백이면
            courseProfessor.setText("개인 공부");
        }
        else{
            courseProfessor.setText(courseList.get(i).getCourseProfessor()+"교수님");
        }
        //courseProfessor.setText(courseList.get(i).getCourseProfessor()+"교수님");
        courseTime.setText(courseList.get(i).getCourseTime()+"");



        v.setTag(courseList.get(i).getCourseID());//태그 붙여쥬기


        Button addButton=(Button)v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                boolean validate =false; //현재 강의를 추가할수잇는지없는지
                validate=schedule.validate(courseList.get(i).getCourseTime());
                //현재 추가하려는 강의에 시간표를 넣음으로서 타당성을 검증함.

                //String userID = MainActivity.userID; //회원아이디 가져옴
                if(!alreadyln(courseIDList,courseList.get(i).getCourseID())){
                    //내가 신청했던 강의 아이디속에서 현재 신청하려는 강의 아이디가 포함되어있다면?
                    AlertDialog.Builder builder=new AlertDialog.Builder(parent.getActivity());
                    AlertDialog dialog=builder.setMessage("이미 추가한 강의입니다.")
                            .setPositiveButton("다시시도",null)
                            .create();
                    dialog.show();
                }
                //학점 제한
                else if(totalCredit+courseList.get(i).getCourseCredit()>24){
                    //학점이 24학점 이상이라면
                    AlertDialog.Builder builder=new AlertDialog.Builder(parent.getActivity());
                    AlertDialog dialog=builder.setMessage("24학점을 초과할 수 없습니다.")
                            .setNegativeButton("다시시도",null)
                            .create();
                    dialog.show();

                }

                else if(validate==false){
                    AlertDialog.Builder builder=new AlertDialog.Builder(parent.getActivity());
                    AlertDialog dialog=builder.setMessage("시간표가 중복됩니다.")
                            .setNegativeButton("다시시도",null)
                            .create();
                    dialog.show();
                }
                else{
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                    AlertDialog dialog = builder.setMessage("강의가 추가되었습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();
                                    //강의 직접 추가하기 해당 강의 번호 등록.
                                    courseIDList.add(courseList.get(i).getCourseID());
                                    schedule.addSchedule(courseList.get(i).getCourseTime());

                                    totalCredit+=courseList.get(i).getCourseCredit();
                                    //토탈 크리딕에 현재 추가한 학점을 추가한다.

                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                    AlertDialog dialog = builder.setMessage("강의추가에 실패했습니다.")
                                            .setNegativeButton("확인", null)
                                            .create();
                                    dialog.show();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    AddRequest addRequest= new AddRequest(userID, courseList.get(i).getCourseID()+"", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                    queue.add(addRequest);
                }
                //이후에 AddRequest 클래스를 만들어줘여함
            }
        });

        return v; //해당 뷰 반환
    }
    class BackgroundTask extends AsyncTask<Void,Void,String> {
        String target;

        @Override
        protected void onPreExecute() {
            try{
                target="http://sujin0204.ivyro.net/ScheduleList.php?userID="+ URLEncoder.encode(userID,"UTF-8");
            }catch (Exception e){
                e.printStackTrace();
            }
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

                String courseProfessor;
                String courseTime;
                int courseID;
                String noticeContent, noticeName,noticeDate;

                totalCredit=0;
                while(count<jsonArray.length()){
                    JSONObject object=jsonArray.getJSONObject(count);
                    courseID=object.getInt("courseID");
                    courseProfessor=object.getString("courseProfessor");
                    courseTime=object.getString("courseTime");
                    totalCredit+=object.getInt("courseCredit"); //학점을 하나씩 더해나갈 수 있도록 함.
                    courseIDList.add(courseID);  //현재 사용자가 가지고잇는 시간표데이터에 있는 아이디가 courseIDList에 담기게 된다.
                    schedule.addSchedule(courseTime); //스케줄 또한 들어가게 됨.
                    count++;


                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    public boolean alreadyln(List<Integer>courseIDList,int item){
        //현재 콜스 아이디에ㅔ 해당하는 강의데이터가 이미 들어가있는 상태인지 아닌지 체크하는 메소드
        for(int i=0; i<courseIDList.size(); i++){
            if(courseIDList.get(i) == item){ //현재 추가하려는 아이디값과 일치하는게 하나라도잇으면 false
                return false;
            }
        }
        return true; //그렇지 않다면 트루
    }

}


