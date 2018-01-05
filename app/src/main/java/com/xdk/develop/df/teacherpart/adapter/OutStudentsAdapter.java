package com.xdk.develop.df.teacherpart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.data.CurrentUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */
public class OutStudentsAdapter extends BaseAdapter{
    private Context context;
    private List<CurrentUser> users = new ArrayList<>();

    public OutStudentsAdapter(List<CurrentUser> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_out_students, null);
            viewHolder.nameTx = (TextView) view.findViewById(R.id.item_out_name);
            viewHolder.classTx = (TextView) view.findViewById(R.id.item_out_state);
            viewHolder.checkBox = (TextView) view.findViewById(R.id.item_out_time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.nameTx.setText(users.get(i).getName());
        viewHolder.classTx.setText(users.get(i).getProfessional()+users.get(i).getSchool()+users.get(i).getGrade()+"级"+users.get(i).getSclass()+"班");
        return view;
    }
    class ViewHolder {
        TextView checkBox;
        TextView nameTx;
        TextView classTx;
    }
}
