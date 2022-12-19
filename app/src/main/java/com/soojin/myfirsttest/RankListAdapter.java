package com.soojin.myfirsttest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

//course 어뎁터
public class RankListAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;
    private Fragment parent;


    public RankListAdapter(Context context, List<Course> courseList, Fragment parent) {
        this.context = context;
        this.courseList = courseList;
        this.parent=parent;

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
        View v=View.inflate(context,R.layout.rank,null); //하나의 뷰를 만들고 course의 디자인 사용
        TextView rankTextView=(TextView)v.findViewById(R.id.rankTextView);
        TextView courseGrade =(TextView) v.findViewById(R.id.courseGrade);
        TextView courseTitle =(TextView) v.findViewById(R.id.courseTitle);
        TextView courseCredit=(TextView)v.findViewById(R.id.courseCredit);
        TextView courseDivide =(TextView) v.findViewById(R.id.courseDivide);
        TextView coursePersonnel =(TextView) v.findViewById(R.id.coursePersonnel);
        TextView courseProfessor =(TextView)v.findViewById(R.id.courseProfessor);
        TextView courseTime=(TextView) v.findViewById(R.id.courseTime);

        rankTextView.setText((i+1)+"위");
        if(i!=0){ //1위가 아니라면 칼라프라이머리 색상을 갖게 된다.
            rankTextView.setBackgroundColor(parent.getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
        }

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
            courseGrade.setText("인원제한없음");
        }
        else {
            coursePersonnel.setText("제한인원: " + courseList.get(i).getCoursePersonnel()+" 명");
        }

        //만약에 교수 이름이 공백이라면 개인연구를 출력하고
        if(courseList.get(i).getCourseProfessor().equals("")){
            courseProfessor.setText("개인 연구");
        }
       else{ //교수이름이 적혀져있으면 그 해당 교수님이름을 불러온다.
           courseProfessor.setText(courseList.get(i).getCourseProfessor()+"교수님");
        }
       courseTime.setText(courseList.get(i).getCourseTime()+"");

        v.setTag(courseList.get(i).getCourseID());

        return v;
    }
}


