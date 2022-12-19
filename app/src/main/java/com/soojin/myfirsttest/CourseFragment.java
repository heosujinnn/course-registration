package com.soojin.myfirsttest;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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


    private ArrayAdapter yearAdapter;
    private Spinner yearSpinner;
    private ArrayAdapter termAdapter;
    private Spinner termSpinner;
    private ArrayAdapter areaAdapter;
    private Spinner areaSpinner;

    private ArrayAdapter majorAdapter; //학과 정보
    private Spinner majorSpinner;


    private String courseUniversity = ""; //학부인지 대학원인지
    private String courseYear = "";
    private String courseTerm = "";
    private String courseArea = "";

    //어뎁터연결 , 리스트 연결을 위한
    private ListView courseListView;
    private CourseListAdapter adapter;
    private List<Course> courseList;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RadioGroup courseUniversityGroup = (RadioGroup) getView().findViewById(R.id.courseUniversityGroup);
        yearSpinner = (Spinner) getView().findViewById(R.id.yearSpinner);
        termSpinner = (Spinner) getView().findViewById(R.id.termSpinner);
        areaSpinner = (Spinner) getView().findViewById(R.id.areaSpinner);
        majorSpinner = (Spinner) getView().findViewById(R.id.majorSpinner);


        courseUniversityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton courseButton = (RadioButton) getView().findViewById(i); //현재 선택된 버튼이 담긴다.
                courseUniversity = courseButton.getText().toString(); //현재선택된버튼을 콜스유니벌스로 가져온다.

                yearAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.year, android.R.layout.simple_spinner_dropdown_item);
                yearSpinner.setAdapter(yearAdapter);

                termAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.term, android.R.layout.simple_spinner_dropdown_item);
                termSpinner.setAdapter(termAdapter);

                if(courseUniversity.equals("학부")){
                    areaAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityArea, android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areaAdapter);
                    //학부선택시 교양 및 기타 항목의 모든 원소가 나오게함
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityRefinementMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }else{
                    areaAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.graduateArea, android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areaAdapter);
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.graduateMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }
            }
        });
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //스피너가 선택 됬을때 해야될 액션을 정의함
                if(areaSpinner.getSelectedItem().equals("교양및기타")){
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityRefinementMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }
                if(areaSpinner.getSelectedItem().equals("전공")){
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }
                if(areaSpinner.getSelectedItem().equals("일반대학원")){
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.graduateMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        //어뎁터 연결
        courseListView=(ListView)getView().findViewById(R.id.courseListView);
        courseList=new ArrayList<Course>();
        adapter=new CourseListAdapter(getContext().getApplicationContext(),courseList,this);
        courseListView.setAdapter(adapter);

        //서치버튼을 누르면 모든 강의 정보를 가져옴
        Button searchButton = (Button)getView().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false);
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                target = "http://sujin0204.ivyro.net/CourseList.php?courseUniversity="+ URLEncoder.encode(courseUniversity, "UTF-8")+ "&courseYear=" + URLEncoder.encode(yearSpinner.getSelectedItem().toString().substring(0,4), "UTF-8")+ "&courseTerm=" + URLEncoder.encode(termSpinner.getSelectedItem().toString(), "UTF-8") + "&courseArea=" + URLEncoder.encode(areaSpinner.getSelectedItem().toString(), "UTF-8") + "&courseMajor=" + URLEncoder.encode(majorSpinner.getSelectedItem().toString(), "UTF-8");              //  Toast.makeText(getActivity().getApplicationContext(), "ok",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;//결과 값을 여기에 저장함
                StringBuilder stringBuilder = new StringBuilder();
                //버퍼생성후 한줄씩 가져옴
                while((temp = bufferedReader.readLine()) != null){
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//결과값이 여기에 리턴되면 이 값이 onPostExcute의 파라미터로 넘어감
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        //10강 변경된 부분 PHP서버에서 보낸 스트링 데이터를 다이얼로그로 보여주는 부분임.
        @Override
        protected void onPostExecute(String result) {
           // super.onPostExecute(result);
            try{
//                AlertDialog dialog;
//                AlertDialog.Builder builder = new AlertDialog.Builder(CourseFragment.this.getContext());
//                dialog = builder.setMessage(result)
//                        .setPositiveButton("확인", null)
//                        .create();
//                dialog.show();

                courseList.clear(); //강의 목록 없애기
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response"); //response배열의 이름으로 받아줌
                int count = 0;
                int courseID;
                String courseUniversity; //학부 또는 대학원
                int courseYear; //강의 년도
                String courseTerm; //학기
                String courseArea; //교양 전공
                String courseMajor; //학부명
                String courseGrade; //학년
                String courseTitle; //강의명
                int courseCredit; //학점
                int courseDivide; //분반
                int coursePersonnel; //강의 인원 수
                String courseProfessor; //강의 교수명
                String courseTime; //시간대
                String courseRoom; //강의실위치
                while (count < jsonArray.length()) { //모든 배열의 원소를 돌면서
                    JSONObject object = jsonArray.getJSONObject(count); //현재의 배열을 가져올 수 있게함.
                    courseID = object.getInt("courseID");
                    courseUniversity = object.getString("courseUniversity");
                    courseYear = object.getInt("courseYear");
                    courseTerm=object.getString("courseTerm");
                    courseArea = object.getString("courseArea");
                    courseMajor = object.getString("courseMajor");
                    courseGrade = object.getString("courseGrade");
                    courseTitle = object.getString("courseTitle");
                    courseCredit = object.getInt("courseCredit");
                    courseDivide = object.getInt("courseDivide");
                    coursePersonnel = object.getInt("coursePersonnel");
                    courseProfessor = object.getString("courseProfessor");
                    courseTime = object.getString("courseTime");
                    courseRoom = object.getString("courseRoom");

                    //생성자에서 복사 붙여넣기함 , 강의에 대한 객체를 처리
                    Course course = new Course(courseID, courseUniversity, courseYear, courseTerm, courseArea, courseMajor, courseGrade, courseTitle, courseCredit, courseDivide, coursePersonnel, courseProfessor, courseTime, courseRoom);
                    courseList.add(course);
                    count++;
                }
                if(count==0){
                    //어떠한 강의도 조회되지 않았다면
                    AlertDialog dialog;
                   AlertDialog.Builder builder=new AlertDialog.Builder(CourseFragment.this.getActivity());
                    dialog=builder.setMessage("조회된 강의가 없습니다.\n 날짜를 확인하세요.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();

                }
                adapter.notifyDataSetChanged();


            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        }

}