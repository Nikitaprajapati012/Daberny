package com.example.raviarchi.daberny.Activity.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

/** * Created by Ravi archi on 2/21/2017.
 */

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.MyViewHolder> {
    public String Id;
    public Utils utils;
    public ArrayList<String> interestList, arrayInterestIdList;
    private List<UserProfileDetails> arrayUserList;
    private Context context;

    public FollowerAdapter(Context context, ArrayList<UserProfileDetails> arraylist) {
        this.context = context;
        this.arrayUserList = arraylist;
        this.utils =new Utils(context);
        notifyDataSetChanged();
        Log.d("Length", "" + arrayUserList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_follower_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final UserProfileDetails userdetails = arrayUserList.get(position);
        Id = userdetails.getUserId();
        holder.txtUsername.setText(userdetails.getUserUserName());

        if (userdetails.getUserImage().length() > 0) {
            Picasso.with(context).load(userdetails.getUserImage())
                    .transform(new RoundedTransformation(120, 2)).placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
        } else {
            Picasso.with(context).load(R.mipmap.ic_launcher)
                    .transform(new RoundedTransformation(120, 2))
                    .placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
        }

        holder.layoutFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/3/2017 redirect to particular follwer details
                Fragment fragment = null;
                fragment = new OtherUserProfile();
                Bundle bundle = new Bundle();
                bundle.putString("id", userdetails.getUserId());
                if (fragment != null) {
                    fragment.setArguments(bundle);
                    utils.replaceFragment(fragment);
                }
            }
        });

        if (userdetails.getIntName().size() > 0) {
            holder.txtInterest1.setText(userdetails.getIntName().get(0));
            holder.txtLevel1.setText(userdetails.getStartRankName().get(0));
        } else {
            holder.layoutInterest1.setVisibility(View.INVISIBLE);
        }
        if (userdetails.getIntName().size() > 1) {
            holder.txtInterest2.setText(userdetails.getIntName().get(1));
            holder.txtLevel2.setText(userdetails.getStartRankName().get(1));
        } else {
            holder.layoutInterest2.setVisibility(View.INVISIBLE);
        }
        if (userdetails.getIntName().size() > 2) {
            holder.txtInterest3.setText(userdetails.getIntName().get(2));
            holder.txtLevel3.setText(userdetails.getStartRankName().get(2));
        } else {
            holder.layoutInterest3.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayUserList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_follower_list_layout)
        LinearLayout layoutFollower;
        @BindView(R.id.adapter_follower_list_imgprofilepic)
        ImageView imgProfile;
        @BindView(R.id.adapter_follower_list_txtusername)
        TextView txtUsername;
        @BindView(R.id.adapter_follower_list_txtinterest1)
        TextView txtInterest1;
        @BindView(R.id.adapter_follower_list_txtinterest2)
        TextView txtInterest2;
        @BindView(R.id.adapter_follower_list_txtinterest3)
        TextView txtInterest3;
        @BindView(R.id.adapter_follower_list_txtlevel1)
        TextView txtLevel1;
        @BindView(R.id.adapter_follower_list_txtlevel2)
        TextView txtLevel2;
        @BindView(R.id.adapter_follower_list_txtlevel3)
        TextView txtLevel3;
        @BindView(R.id.adapter_follower_list_txtinterest1_layout)
        LinearLayout layoutInterest1;
        @BindView(R.id.adapter_follower_list_txtinterest2_layout)
        LinearLayout layoutInterest2;
        @BindView(R.id.adapter_follower_list_txtinterest3_layout)
        LinearLayout layoutInterest3;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
