package com.soojin.myfirsttest;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ListView courseListView;
    private StatisticsCourseListAdapter adapter;
    private List<Course> courseList;

   // int totalCredit=0; //?????? ?????? ???

    public static int totalCredit=0; //?????? ????????? ??????????????? ???????????? ?????????
    public static TextView credit;

    private ArrayAdapter rankAdapter;
    private Spinner rankSpinner;

    //??????????????????
    private ListView rankListView;
    private RankListAdapter rankListAdapter;
    private List<Course> rankList;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        courseListView=(ListView) getView().findViewById(R.id.courseListView);
        courseList=new ArrayList<Course>();
        adapter=new StatisticsCourseListAdapter(getContext().getApplicationContext(),courseList,this);
        courseListView.setAdapter(adapter);
        new BackgroundTask().execute(); //????????? ??????????????????

        totalCredit=0;
        credit=(TextView) getView().findViewById(R.id.totalCredit);
        rankSpinner=(Spinner) getView().findViewById(R.id.rankSpinner);

        rankAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.rank, R.layout.spinner_item); //????????? ?????? xml layout

        rankSpinner.setPopupBackgroundResource(R.color.teal_700);
        //??????????????????
         rankListView=(ListView)getView().findViewById(R.id.rankListView);
         rankList=new ArrayList<Course>();
         rankListAdapter=new RankListAdapter(getContext().getApplicationContext(),rankList,this);
         rankListView.setAdapter(rankListAdapter);
        // new ByEntire().execute(); //?????? ?????? ??????



        rankSpinner.setAdapter(rankAdapter);
        rankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(rankSpinner.getSelectedItem().equals("????????????")){
                    rankList.clear();
                    new ByEntire().execute();

                }
                else if(rankSpinner.getSelectedItem().equals("???????????????")){
                    rankList.clear();
                    new ByMyMajor().execute();
                }
               else if(rankSpinner.getSelectedItem().equals("?????? ?????????")){
                    rankList.clear();
                    new ByMale().execute();
                }
                else if(rankSpinner.getSelectedItem().equals("?????? ?????????")){
                    rankList.clear();
                    new ByFemale().execute();
                }
                else if(rankSpinner.getSelectedItem().equals("?????? ?????????")){
                    rankList.clear();
                    new ByMajor().execute();
                }
                else if(rankSpinner.getSelectedItem().equals("?????? ?????????")){
                    rankList.clear();
                    new ByRefinement().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class ByMyMajor extends AsyncTask<Void,Void,String>{
        String target;

        @Override
        protected void onPreExecute() {
            try{
                target="http://sujin0204.ivyro.net/ByMyMajor.php?userID="+URLEncoder.encode(MainActivity.userID,"UTF-8");

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url=new URL(target);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream(); //url?????? ????????????
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream)); //????????? ????????? ?????? ??? ?????????
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
                int courseID;
                String courseGrade;
                String courseTitle;
                int courseDivide;

                String courseProfessor;
                int courseCredit;
                String courseTime;

                int coursePersonnel;
                // int courseRival;

                // String noticeContent, noticeName,noticeDate;
                while(count<jsonArray.length()){
                    JSONObject object=jsonArray.getJSONObject(count);
                    courseID=object.getInt("courseID");
                    courseGrade=object.getString("courseGrade");
                    courseTitle=object.getString("courseTitle");
                    courseDivide=object.getInt("courseDivide");
                    courseProfessor=object.getString("courseProfessor");
                    courseCredit=object.getInt("courseCredit");
                    courseTime=object.getString("courseTime");

                    coursePersonnel=object.getInt("coursePersonnel");
                    //   courseRival=object.getInt("COUNT(SCHEDULE.courseID)");
                    //  int courseCredit=object.getInt("courseCredit"); //courseCredit??? ????????????
                    //totalCredit+=courseCredit; //?????? ???????????? ????????? ????????????????????? ????????????.

                    // courseList.add(new Course(courseID,courseGrade,courseTitle,courseDivide,coursePersonnel,courseRival));
                    //  courseList.add(new Course(courseID,courseTitle,courseDivide,courseGrade,coursePersonnel,courseRival,courseCredit));

                    rankList.add(new Course( courseID,  courseGrade,  courseTitle,  courseCredit,  courseDivide,  coursePersonnel,  courseTime,courseProfessor));


                    count++;


                }
                rankListAdapter.notifyDataSetChanged(); //?????????????????? ??????

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    class ByMajor extends AsyncTask<Void,Void,String>{
        String target;

        @Override
        protected void onPreExecute() {
            try{
                target="http://sujin0204.ivyro.net/ByMajor.php";

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url=new URL(target);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream(); //url?????? ????????????
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream)); //????????? ????????? ?????? ??? ?????????
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
                int courseID;
                String courseGrade;
                String courseTitle;
                int courseDivide;

                String courseProfessor;
                int courseCredit;
                String courseTime;

                int coursePersonnel;
                // int courseRival;

                // String noticeContent, noticeName,noticeDate;
                while(count<jsonArray.length()){
                    JSONObject object=jsonArray.getJSONObject(count);
                    courseID=object.getInt("courseID");
                    courseGrade=object.getString("courseGrade");
                    courseTitle=object.getString("courseTitle");
                    courseDivide=object.getInt("courseDivide");
                    courseProfessor=object.getString("courseProfessor");
                    courseCredit=object.getInt("courseCredit");
                    courseTime=object.getString("courseTime");

                    coursePersonnel=object.getInt("coursePersonnel");
                    //   courseRival=object.getInt("COUNT(SCHEDULE.courseID)");
                    //  int courseCredit=object.getInt("courseCredit"); //courseCredit??? ????????????
                    //totalCredit+=courseCredit; //?????? ???????????? ????????? ????????????????????? ????????????.

                    // courseList.add(new Course(courseID,courseGrade,courseTitle,courseDivide,coursePersonnel,courseRival));
                    //  courseList.add(new Course(courseID,courseTitle,courseDivide,courseGrade,coursePersonnel,courseRival,courseCredit));

                    rankList.add(new Course( courseID,  courseGrade,  courseTitle,  courseCredit,  courseDivide,  coursePersonnel,  courseTime,courseProfessor));


                    count++;


                }
                rankListAdapter.notifyDataSetChanged(); //?????????????????? ??????

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    class ByRefinement extends AsyncTask<Void,Void,String>{
        String target;

        @Override
        protected void onPreExecute() {
            try{
                target="http://sujin0204.ivyro.net/ByRefinement.php";

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url=new URL(target);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream(); //url?????? ????????????
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream)); //????????? ????????? ?????? ??? ?????????
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
                int courseID;
                String courseGrade;
                String courseTitle;
                int courseDivide;

                String courseProfessor;
                int courseCredit;
                String courseTime;

                int coursePersonnel;
                // int courseRival;

                // String noticeContent, noticeName,noticeDate;
                while(count<jsonArray.length()){
                    JSONObject object=jsonArray.getJSONObject(count);
                    courseID=object.getInt("courseID");
                    courseGrade=object.getString("courseGrade");
                    courseTitle=object.getString("courseTitle");
                    courseDivide=object.getInt("courseDivide");
                    courseProfessor=object.getString("courseProfessor");
                    courseCredit=object.getInt("courseCredit");
                    courseTime=object.getString("courseTime");

                    coursePersonnel=object.getInt("coursePersonnel");
                    //   courseRival=object.getInt("COUNT(SCHEDULE.courseID)");
                    //  int courseCredit=object.getInt("courseCredit"); //courseCredit??? ????????????
                    //totalCredit+=courseCredit; //?????? ???????????? ????????? ????????????????????? ????????????.

                    // courseList.add(new Course(courseID,courseGrade,courseTitle,courseDivide,coursePersonnel,courseRival));
                    //  courseList.add(new Course(courseID,courseTitle,courseDivide,courseGrade,coursePersonnel,courseRival,courseCredit));

                    rankList.add(new Course( courseID,  courseGrade,  courseTitle,  courseCredit,  courseDivide,  coursePersonnel,  courseTime,courseProfessor));


                    count++;


                }
                rankListAdapter.notifyDataSetChanged(); //?????????????????? ??????

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }










    class ByFemale extends AsyncTask<Void,Void,String>{
        String target;

        @Override
        protected void onPreExecute() {
            try{
                target="http://sujin0204.ivyro.net/ByFemale.php";

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url=new URL(target);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream(); //url?????? ????????????
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream)); //????????? ????????? ?????? ??? ?????????
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
                int courseID;
                String courseGrade;
                String courseTitle;
                int courseDivide;

                String courseProfessor;
                int courseCredit;
                String courseTime;

                int coursePersonnel;
                // int courseRival;

                // String noticeContent, noticeName,noticeDate;
                while(count<jsonArray.length()){
                    JSONObject object=jsonArray.getJSONObject(count);
                    courseID=object.getInt("courseID");
                    courseGrade=object.getString("courseGrade");
                    courseTitle=object.getString("courseTitle");
                    courseDivide=object.getInt("courseDivide");
                    courseProfessor=object.getString("courseProfessor");
                    courseCredit=object.getInt("courseCredit");
                    courseTime=object.getString("courseTime");

                    coursePersonnel=object.getInt("coursePersonnel");
                    //   courseRival=object.getInt("COUNT(SCHEDULE.courseID)");
                    //  int courseCredit=object.getInt("courseCredit"); //courseCredit??? ????????????
                    //totalCredit+=courseCredit; //?????? ???????????? ????????? ????????????????????? ????????????.

                    // courseList.add(new Course(courseID,courseGrade,courseTitle,courseDivide,coursePersonnel,courseRival));
                    //  courseList.add(new Course(courseID,courseTitle,courseDivide,courseGrade,coursePersonnel,courseRival,courseCredit));

                    rankList.add(new Course( courseID,  courseGrade,  courseTitle,  courseCredit,  courseDivide,  coursePersonnel,  courseTime,courseProfessor));


                    count++;


                }
                rankListAdapter.notifyDataSetChanged(); //?????????????????? ??????

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }




    class ByMale extends AsyncTask<Void,Void,String>{
        String target;

        @Override
        protected void onPreExecute() {
            try{
                target="http://sujin0204.ivyro.net/ByMale.php";

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url=new URL(target);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream(); //url?????? ????????????
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream)); //????????? ????????? ?????? ??? ?????????
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
                int courseID;
                String courseGrade;
                String courseTitle;
                int courseDivide;

                String courseProfessor;
                int courseCredit;
                String courseTime;

                int coursePersonnel;
                // int courseRival;

                // String noticeContent, noticeName,noticeDate;
                while(count<jsonArray.length()){
                    JSONObject object=jsonArray.getJSONObject(count);
                    courseID=object.getInt("courseID");
                    courseGrade=object.getString("courseGrade");
                    courseTitle=object.getString("courseTitle");
                    courseDivide=object.getInt("courseDivide");
                    courseProfessor=object.getString("courseProfessor");
                    courseCredit=object.getInt("courseCredit");
                    courseTime=object.getString("courseTime");

                    coursePersonnel=object.getInt("coursePersonnel");
                    //   courseRival=object.getInt("COUNT(SCHEDULE.courseID)");
                    //  int courseCredit=object.getInt("courseCredit"); //courseCredit??? ????????????
                    //totalCredit+=courseCredit; //?????? ???????????? ????????? ????????????????????? ????????????.

                    // courseList.add(new Course(courseID,courseGrade,courseTitle,courseDivide,coursePersonnel,courseRival));
                    //  courseList.add(new Course(courseID,courseTitle,courseDivide,courseGrade,coursePersonnel,courseRival,courseCredit));

                    rankList.add(new Course( courseID,  courseGrade,  courseTitle,  courseCredit,  courseDivide,  coursePersonnel,  courseTime,courseProfessor));


                    count++;


                }
                rankListAdapter.notifyDataSetChanged(); //?????????????????? ??????

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }




    class ByEntire extends AsyncTask<Void,Void,String>{
        String target;

        @Override
        protected void onPreExecute() {
            try{
                target="http://sujin0204.ivyro.net/ByEntire.php?userID="+ URLEncoder.encode(MainActivity.userID,"UTF-8");

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url=new URL(target);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream(); //url?????? ????????????
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream)); //????????? ????????? ?????? ??? ?????????
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
                int courseID;
                String courseGrade;
                String courseTitle;
                int courseDivide;

                String courseProfessor;
                int courseCredit;
                String courseTime;

                int coursePersonnel;
               // int courseRival;

                // String noticeContent, noticeName,noticeDate;
                while(count<jsonArray.length()){
                    JSONObject object=jsonArray.getJSONObject(count);
                    courseID=object.getInt("courseID");
                    courseGrade=object.getString("courseGrade");
                    courseTitle=object.getString("courseTitle");
                    courseDivide=object.getInt("courseDivide");
                    courseProfessor=object.getString("courseProfessor");
                    courseCredit=object.getInt("courseCredit");
                    courseTime=object.getString("courseTime");

                    coursePersonnel=object.getInt("coursePersonnel");
                 //   courseRival=object.getInt("COUNT(SCHEDULE.courseID)");
                  //  int courseCredit=object.getInt("courseCredit"); //courseCredit??? ????????????
                    //totalCredit+=courseCredit; //?????? ???????????? ????????? ????????????????????? ????????????.

                    // courseList.add(new Course(courseID,courseGrade,courseTitle,courseDivide,coursePersonnel,courseRival));
                  //  courseList.add(new Course(courseID,courseTitle,courseDivide,courseGrade,coursePersonnel,courseRival,courseCredit));

                    rankList.add(new Course( courseID,  courseGrade,  courseTitle,  courseCredit,  courseDivide,  coursePersonnel,  courseTime,courseProfessor));


                    count++;


                }
                rankListAdapter.notifyDataSetChanged(); //?????????????????? ??????

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }



    class BackgroundTask extends AsyncTask<Void,Void,String>{
        String target;

        @Override
        protected void onPreExecute() {
            try{
                target="http://sujin0204.ivyro.net/StatisticsCourseList.php?userID="+ URLEncoder.encode(MainActivity.userID,"UTF-8");
                //????????????????????? ?????? ?????? ????????? ?????????;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url=new URL(target);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream(); //url?????? ????????????
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream)); //????????? ????????? ?????? ??? ?????????
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
                int courseID;
                String courseGrade;
                String courseTitle;
                int courseDivide;
                int coursePersonnel;
                int courseRival;

               // String noticeContent, noticeName,noticeDate;
                while(count<jsonArray.length()){
                    JSONObject object=jsonArray.getJSONObject(count);
                    courseID=object.getInt("courseID");
                    courseGrade=object.getString("courseGrade");
                    courseTitle=object.getString("courseTitle");
                    courseDivide=object.getInt("courseDivide");
                    coursePersonnel=object.getInt("coursePersonnel");
                    courseRival=object.getInt("COUNT(SCHEDULE.courseID)");
                    int courseCredit=object.getInt("courseCredit"); //courseCredit??? ????????????
                    totalCredit+=courseCredit; //?????? ???????????? ????????? ????????????????????? ????????????.

                   // courseList.add(new Course(courseID,courseGrade,courseTitle,courseDivide,coursePersonnel,courseRival));
                    courseList.add(new Course(courseID,courseTitle,courseDivide,courseGrade,coursePersonnel,courseRival,courseCredit));


                    count++;


                }
                adapter.notifyDataSetChanged(); //?????????????????? ??????

                credit.setText(totalCredit+"??????");

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }


}