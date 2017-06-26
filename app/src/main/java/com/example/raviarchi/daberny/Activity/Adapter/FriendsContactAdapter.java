package com.example.raviarchi.daberny.Activity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.raviarchi.daberny.Activity.Model.dataPojo;
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

public class FriendsContactAdapter extends RecyclerView.Adapter<FriendsContactAdapter.MyViewHolder> {
    public String Id, interest;
    public Utils utils;
    private List<dataPojo> arrayUserList;
    private Context context;

    public FriendsContactAdapter(Context context, ArrayList<dataPojo> arraylist) {
        this.context = context;
        this.arrayUserList = arraylist;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_people_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final dataPojo userdetails = arrayUserList.get(position);
        Id = userdetails.getId();
        holder.txtUsername.setText(userdetails.getUsername());
        holder.txtFullname.setText(userdetails.getFullname());
        if (userdetails.getImage().length() > 0) {
            Picasso.with(context).load(userdetails.getImage()).
                    transform(new RoundedTransformation(120,2))
                    .onlyScaleDown().
                    placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
        } else {
            Picasso.with(context).load(R.drawable.ic_placeholder).
                    transform(new RoundedTransformation(120,2)).
                    placeholder(R.drawable.ic_placeholder).into(holder.imgProfile);
        }

        /*holder.linearLayoutPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OtherUserProfile();
                Bundle bundle = new Bundle();
                bundle.putString("id", userdetails.getUserId());

                if (fragment != null) {
                    fragment.setArguments(bundle);
                    FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.frame_contain_layout, fragment);
                    transaction.commit();
                }
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return arrayUserList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.adapter_people_list_txtusername)
        TextView txtUsername;
        @BindView(R.id.adapter_people_list_txtfullname)
        TextView txtFullname;
        @BindView(R.id.adapter_people_list_layout)
        LinearLayout linearLayoutPeople;
        @BindView(R.id.adapter_people_list_imgprofilepic)
        ImageView imgProfile;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
