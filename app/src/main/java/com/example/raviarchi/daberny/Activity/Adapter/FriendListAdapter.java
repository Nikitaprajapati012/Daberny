package com.example.raviarchi.daberny.Activity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;

import java.util.ArrayList;

/**
 * Created by Archirayan on 14-Jun-17.
 * Archirayan Infotech pvt Ltd
 * dilip.bakotiya@gmail.com || info@archirayan.com
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {
    public String Id, interest;
    private ArrayList<String> arrayUserList;
    private Utils utils;
    private Context context;

    public FriendListAdapter(Context context, ArrayList<String> arraylist) {
        this.context = context;
        this.utils = new Utils(context);
        this.arrayUserList = arraylist;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_blockuser_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_adapter_blockuser_list_txtusername.setText(arrayUserList.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return arrayUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_adapter_blockuser_list_txtusername;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_adapter_blockuser_list_txtusername = (TextView) itemView.findViewById(R.id.adapter_blockuser_list_txtusername);
        }
    }
}
