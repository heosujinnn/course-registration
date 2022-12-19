package com.soojin.myfirsttest;

import android.content.Context;
import android.widget.TextView;

public class Schedule {
    private String monday[] = new String[10]; //0교시부터 10교시
    private String tuesday[] = new String[10];
    private String wednesday[] = new String[10];
    private String thursday[] = new String[10];
    private String friday[] = new String[10];

    public Schedule() { //생성자 만즐기!
        for (int i = 0; i < 10; i++) {
            monday[i] = "";
            tuesday[i] = "";
            wednesday[i] = "";
            thursday[i] = "";
            friday[i] = "";
        }
    }


    public void addSchedule(String scheduleText) { //addscheldule을 만들고 특정한 문자가 있을때 파싱해서 배열안으로 들어가게함
        int temp;
        // 만약에 월 :[3][4][5] 화:[4][5] 이면 월 :[3][4][5]/(파싱) 화:[4][5]
        if ((temp = scheduleText.indexOf("월")) > -1) {
            //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;

                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    monday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = "수업";
                    //즉, [와 ] 사이에 존재하는 숫자를 파싱해서 그 위치에 들어가는 교시에 수업이라는 데이터를 넣어준것이다.
                }
            }
        }
        if ((temp = scheduleText.indexOf("화")) > -1) {
            //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;

                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    tuesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = "수업";
                    //즉, [와 ] 사이에 존재하는 숫자를 파싱해서 그 위치에 들어가는 교시에 수업이라는 데이터를 넣어준것이다.
                }
            }
        }
        if ((temp = scheduleText.indexOf("수")) > -1) {
            //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;

                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    wednesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = "수업";
                    //즉, [와 ] 사이에 존재하는 숫자를 파싱해서 그 위치에 들어가는 교시에 수업이라는 데이터를 넣어준것이다.
                }
            }
        }
        if ((temp = scheduleText.indexOf("목")) > -1) {
            //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;

                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    thursday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = "수업";
                    //즉, [와 ] 사이에 존재하는 숫자를 파싱해서 그 위치에 들어가는 교시에 수업이라는 데이터를 넣어준것이다.
                }
            }
        }
        if ((temp = scheduleText.indexOf("금")) > -1) {
            //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;

                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    friday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = "수업";
                    //즉, [와 ] 사이에 존재하는 숫자를 파싱해서 그 위치에 들어가는 교시에 수업이라는 데이터를 넣어준것이다.
                }
            }
        }
    }


    public  boolean validate(String scheduleText) {
        //현재 스케줄데이터에 중복이 없는지 체쿠
        if (scheduleText.equals("")) {
            return true;
        } else {
            int temp;
            if ((temp = scheduleText.indexOf("월")) > -1) {
                //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
                temp += 2;
                int startPoint = temp;
                int endPoint = temp;
                //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
                for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                    if (scheduleText.charAt(i) == '[') {
                        startPoint = i;

                    }
                    if (scheduleText.charAt(i) == ']') {
                        endPoint = i;
                        if (!monday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals("")) {
                            return false; //공백이 아니라면 리턴 false;
                        }
                    }
                }
            }
            if ((temp = scheduleText.indexOf("화")) > -1) {
                //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
                temp += 2;
                int startPoint = temp;
                int endPoint = temp;
                //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
                for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                    if (scheduleText.charAt(i) == '[') {
                        startPoint = i;

                    }
                    if (scheduleText.charAt(i) == ']') {
                        endPoint = i;
                        if (!tuesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals("")) {
                            return false; //공백이 아니라면 리턴 false;
                        }
                    }
                }
            }

            if ((temp = scheduleText.indexOf("수")) > -1) {
                //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
                temp += 2;
                int startPoint = temp;
                int endPoint = temp;
                //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
                for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                    if (scheduleText.charAt(i) == '[') {
                        startPoint = i;

                    }
                    if (scheduleText.charAt(i) == ']') {
                        endPoint = i;
                        if (!wednesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals("")) {
                            return false; //공백이 아니라면 리턴 false;
                        }
                    }
                }
            }

            if ((temp = scheduleText.indexOf("목")) > -1) {
                //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
                temp += 2;
                int startPoint = temp;
                int endPoint = temp;
                //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
                for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                    if (scheduleText.charAt(i) == '[') {
                        startPoint = i;

                    }
                    if (scheduleText.charAt(i) == ']') {
                        endPoint = i;
                        if (!thursday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals("")) {
                            return false; //공백이 아니라면 리턴 false;
                        }
                    }
                }
            }

            if ((temp = scheduleText.indexOf("금")) > -1) {
                //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
                temp += 2;
                int startPoint = temp;
                int endPoint = temp;
                //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
                for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                    if (scheduleText.charAt(i) == '[') {
                        startPoint = i;

                    }
                    if (scheduleText.charAt(i) == ']') {
                        endPoint = i;
                        if (!friday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals("")) {
                            return false; //공백이 아니라면 리턴 false;
                        }//월~금까지 중복잇냐 업냐
                    }
                }
            }
        }
        return true;

    }

    public void addSchedule(String scheduleText,String courseTitle, String courseProfessor) { //addscheldule을 만들고 특정한 문자가 있을때 파싱해서 배열안으로
        String professor;
        if(courseProfessor.equals("")){
            professor="";
        }
        else{
            professor="";
        }

        int temp;
        // 만약에 월 :[3][4][5] 화:[4][5] 이면 월 :[3][4][5]/(파싱) 화:[4][5]
        if ((temp = scheduleText.indexOf("월")) > -1) {
            //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;

                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    monday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = courseTitle+professor;
                    //즉, [와 ] 사이에 존재하는 숫자를 파싱해서 그 위치에 들어가는 교시에 수업이라는 데이터를 넣어준것이다.
                }
            }
        }
        if ((temp = scheduleText.indexOf("화")) > -1) {
            //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;

                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    tuesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = courseTitle+professor;
                    //즉, [와 ] 사이에 존재하는 숫자를 파싱해서 그 위치에 들어가는 교시에 수업이라는 데이터를 넣어준것이다.
                }
            }
        }
        if ((temp = scheduleText.indexOf("수")) > -1) {
            //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;

                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    wednesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = courseTitle+professor;
                    //즉, [와 ] 사이에 존재하는 숫자를 파싱해서 그 위치에 들어가는 교시에 수업이라는 데이터를 넣어준것이다.
                }
            }
        }
        if ((temp = scheduleText.indexOf("목")) > -1) {
            //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;

                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    thursday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = courseTitle+professor;
                    //즉, [와 ] 사이에 존재하는 숫자를 파싱해서 그 위치에 들어가는 교시에 수업이라는 데이터를 넣어준것이다.
                }
            }
        }
        if ((temp = scheduleText.indexOf("금")) > -1) {
            //만약에 월이라는 단어가 포함되어있으면 그 위치를 반환해서 temp에 저장
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            //i가 temp 부터 스케줄 텍스트읠 길이보다 작을때이면서 ':'이 아닐때까지 1씩 더해준다.
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;

                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    friday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = courseTitle+professor;
                    //즉, [와 ] 사이에 존재하는 숫자를 파싱해서 그 위치에 들어가는 교시에 수업이라는 데이터를 넣어준것이다.
                }
            }
        }
    }
    //수업강의 목록을 보여줄 수 있도록 셋팅하는 함수
    public void setting(AutoResizeTextView[] monday, AutoResizeTextView[] tuesday, AutoResizeTextView[] wednesday, AutoResizeTextView[] thursday, AutoResizeTextView[] friday, Context context){
    int maxLength=0;
    String maxString="";
    for(int i=0; i<10; i++){
        if(this.monday[i].length()>maxLength){
            maxLength=this.monday[i].length();
            maxString=this.monday[i];
        }
        if(this.tuesday[i].length()>maxLength){
            maxLength=this.tuesday[i].length();
            maxString=this.tuesday[i];
        }
        if(this.wednesday[i].length()>maxLength){
            maxLength=this.wednesday[i].length();
            maxString=this.wednesday[i];
        }
        if(this.thursday[i].length()>maxLength){
            maxLength=this.thursday[i].length();
            maxString=this.thursday[i];
        }
        if(this.friday[i].length()>maxLength){
            maxLength=this.friday[i].length();
            maxString=this.friday[i];
        }
    }
        for(int i=0; i<10; i++){
            if(!this.monday[i].equals("")){
                //현재 배열의 값이 비어있지 않다면
                monday[i].setText(this.monday[i]);
                monday[i].setTextColor(context.getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));

            }
            else { //비어잇다면
                monday[i].setText(maxString);//가장긴거 선택
            }

            if(!this.tuesday[i].equals("")){
                //현재 배열의 값이 비워있지 않다며??
                tuesday[i].setText(this.tuesday[i]);
                tuesday[i].setTextColor(context.getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));

            }
            else {
                tuesday[i].setText(maxString);//10개의 공간 만듦
            }
            if(!this.wednesday[i].equals("")){
                //현재 배열의 값이 비워있지 않다며?
                wednesday[i].setText(this.wednesday[i]);
                wednesday[i].setTextColor(context.getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));

            }
            else {
                wednesday[i].setText(maxString);//10개의 공간 만듦
            }
            if(!this.thursday[i].equals("")){
                //현재 배열의 값이 비어있다면?
                thursday[i].setText(this.thursday[i]);
                thursday[i].setTextColor(context.getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));

            }
            else {
                thursday[i].setText(maxString);//10개의 공간 만듦
            }
            if(!this.friday[i].equals("")){
                //현재 배열의 값이 비어있다면?
                friday[i].setText(this.friday[i]);
                friday[i].setTextColor(context.getResources().getColor(com.google.android.material.R.color.design_default_color_primary_dark));

            }
            else {
                friday[i].setText(maxString);//10개의 공간 만듦
            }

            monday[i].resizeText(); //현재 사이즈 크기 조절
            tuesday[i].resizeText();
            wednesday[i].resizeText();
            thursday[i].resizeText();
            friday[i].resizeText();
        }
    }
}



