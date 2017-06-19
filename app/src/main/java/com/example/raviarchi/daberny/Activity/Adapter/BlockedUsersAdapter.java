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

import com.example.raviarchi.daberny.Activity.Fragment.OtherUserProfile;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.RoundedTransformation;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ravi archi on 2/21/2017.
 */

public class BlockedUsersAdapter extends RecyclerView.Adapter<BlockedUsersAdapter.MyViewHolder> {
    public String Id, interest;
    public Utils utils;
    private List<UserProfileDetails> arrayUserList;
    private Context context;

    public BlockedUsersAdapter(Context context, ArrayList<UserProfileDetails> arraylist) {
        this.context = context;
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
        final UserProfileDetails userdetails = arrayUserList.get(position);
        Id = userdetails.getUserId();
        holder.txtUsername.setText(userdetails.getUserUserName());
        holder.txtFullname.setText(userdetails.getUserFullName());
        if (userdetails.getUserImage().length() > 0) {
            Picasso.with(context).load(userdetails.getUserImage()).
                    transform(new RoundedTransformation(120,2)).
                    placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
        } else {
            Picasso.with(context).load(R.drawable.ic_placeholder).
            transform(new RoundedTransformation(120,2)).
            placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
        }

        holder.linearLayoutblockuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OtherUserProfile();
                Bundle bundle = new Bundle();
                bundle.putString("id", userdetails.getUserId());
                fragment.setArguments(bundle);
                utils.replaceFragment(fragment);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_blockuser_list_txtusername)
        TextView txtUsername;
        @BindView(R.id.adapter_blockuser_list_txtfullname)
        TextView txtFullname;
        @BindView(R.id.adapter_blockuser_list_layout)
        LinearLayout linearLayoutblockuser;
        @BindView(R.id.adapter_blockuser_list_imgprofilepic)
        ImageView imgProfile;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
