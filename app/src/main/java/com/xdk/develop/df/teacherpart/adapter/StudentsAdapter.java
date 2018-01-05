package com.xdk.develop.df.teacherpart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.xdk.develop.df.teacherpart.R;
import com.xdk.develop.df.teacherpart.data.CurrentUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class StudentsAdapter extends BaseAdapter {
    private Context context;
    private List<CurrentUser> users = new ArrayList<>();
    private static List<CurrentUser> checkedUsers = new ArrayList<>();

    public StudentsAdapter(List<CurrentUser> users, Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_students, null);
            viewHolder.nameTx = (TextView) view.findViewById(R.id.item_students_name);
            viewHolder.classTx = (TextView) view.findViewById(R.id.item_students_class);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.item_students_check);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.nameTx.setText(users.get(i).getName());
        viewHolder.classTx.setText(users.get(i).getSchool()+users.get(i).getGrade()+"级"+users.get(i).getSclass()+"班");
        viewHolder.checkBox.setChecked(users.get(i).isChecked());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkedUsers.add(users.get(i));
                    users.get(i).setChecked(true);
                }else{
                    users.get(i).setChecked(false);
                    checkedUsers.remove(users.get(i));
                }
            }
        });
        return view;
    }
    public static List<CurrentUser> getCheckedUser(){
        return checkedUsers;
    }
    class ViewHolder {
        CheckBox checkBox;
        TextView nameTx;
        TextView classTx;
    }
}
