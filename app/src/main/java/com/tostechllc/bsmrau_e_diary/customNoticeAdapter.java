package com.tostechllc.bsmrau_e_diary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class customNoticeAdapter extends BaseAdapter {
    Context context;
    ArrayList<listedNotice> arrayList;
    public customNoticeAdapter(Context context, ArrayList<listedNotice> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.customview_notice_list, parent, false);


        TextView noticeheading = rowView.findViewById(R.id.tv_notice_title);
        TextView noticeDate = rowView.findViewById(R.id.tv_notice_date);

        listedNotice listedNotice = arrayList.get(position);


        noticeheading.setText(listedNotice.getHeading());
        noticeDate.setText(listedNotice.getDate());


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, noticeDetails.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("noticeheading", listedNotice.getHeading());
                intent.putExtra("noticedetails", listedNotice.getDetails());
                intent.putExtra("noticeimage", listedNotice.getImage());
                context.startActivity(intent);
            }

        });
        return rowView;
    }
}
