package com.example.raviarchi.daberny.Activity.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Fragment.Home;
import com.example.raviarchi.daberny.Activity.Fragment.Tag;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ravi archi on 2/21/2017.
 */

public class SearchTagAdapter extends RecyclerView.Adapter<SearchTagAdapter.MyViewHolder> {
    public String Id, interest;
    public Utils utils;
    private List<UserProfileDetails> arrayUserList;
    private Context context;

    public SearchTagAdapter(Context context, ArrayList<UserProfileDetails> arraylist) {
        this.context = context;
        this.arrayUserList = arraylist;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tag_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final UserProfileDetails userdetails = arrayUserList.get(position);
        Id = userdetails.getUserId();

        holder.txtTags.setText(userdetails.getQueTag());


        holder.linearLayoutTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Tag();
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                bundle.putString("userprofiledetails", gson.toJson(userdetails));

                if (fragment != null) {
                    fragment.setArguments(bundle);
                    utils.replaceFragment(fragment);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayUserList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_tag_list_txttags)
        TextView txtTags;
        @BindView(R.id.adapter_tag_list_imgprofilepic)
        ImageView imgPic;
        @BindView(R.id.adapter_tag_list_layout)
        LinearLayout linearLayoutTag;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
