package com.soojin.myfirsttest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
//노티스 어뎁터
public class NoticeListAdapter extends BaseAdapter {
    private Context context;
    private List<Notice> noticeList;

    public NoticeListAdapter(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public int getCount() {
        return noticeList.size(); //갯수
    }

    @Override
    public Object getItem(int i) {
        return noticeList.get(i); //해당위치에 있는 노티스들을 반환하도록함.
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=View.inflate(context,R.layout.notice,null); //하나의 뷰를 만들고 노티스의 디자인 사용
        TextView noticeText=(TextView)v.findViewById(R.id.noticeText);
        TextView nameText=(TextView)v.findViewById(R.id.nameText);
        TextView dateText=(TextView)v.findViewById(R.id.dateText);

        noticeText.setText(noticeList.get(i).getNotice()); //현재 리스트에 있는 값으로 넣어주기
        nameText.setText(noticeList.get(i).getName());
        dateText.setText(noticeList.get(i).getDate());

        v.setTag(noticeList.get(i).getNotice());//태그 붙여쥬기
        return v; //해당 뷰 반환
    }
}
