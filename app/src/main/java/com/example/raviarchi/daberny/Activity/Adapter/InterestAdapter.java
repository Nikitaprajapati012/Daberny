package com.example.raviarchi.daberny.Activity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.example.raviarchi.daberny.Activity.Model.UserProfileDetails;
import com.example.raviarchi.daberny.Activity.Utils.Utils;
import com.example.raviarchi.daberny.R;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Ravi archi on 2/21/2017.
 */

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.MyViewHolder> {
    public String Id;
    public Utils utils;
    private List<UserProfileDetails> arrayList;
    private Context context;

    public InterestAdapter(Context context, ArrayList<UserProfileDetails> arraylist) {
        this.context = context;
        this.arrayList = arraylist;
        Log.d("Length", "" + arrayList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_interest_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // holder.bind(arrayList.get(position), listener);
        final UserProfileDetails details = arrayList.get(position);

        Id = details.getUserInterestId();
        holder.title.setText(details.getUserInterestName());
        holder.ckTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.getId() != 0) {
                    holder.title.getText().toString();
                    Log.d("checked", "" + holder.title.getText());
                }
            }
        });
    }

    /* public void add(int position,String item) {
         arrayList.add(position, item);
         notifyItemInserted(position);
     }
 */
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CheckBox ckTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.adapter_interest_list_txttitle);
            ckTitle = (CheckBox) itemView.findViewById(R.id.adapter_interest_list_ckInterest);
        }
    }
}
