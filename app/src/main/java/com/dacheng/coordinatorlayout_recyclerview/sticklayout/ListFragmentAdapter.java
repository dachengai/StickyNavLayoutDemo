package com.dacheng.coordinatorlayout_recyclerview.sticklayout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dacheng.coordinatorlayout_recyclerview.R;
import com.dacheng.coordinatorlayout_recyclerview.sticklayout.like.FavorView;

/**
 * Created by dacheng on 16/8/22.
 */
public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentAdapter.TextViewHolder>{


    public ListFragmentAdapter() {
    }
    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        try{
            holder.mTextView.setText("test");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView mVisitors;
        public FavorView mPeriscoper;
        public TextViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.item_title);
            mVisitors = (TextView) view.findViewById(R.id.item_users);
            mPeriscoper = (FavorView) view.findViewById(R.id.periscope);
            mVisitors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPeriscoper.addHeart();
                }
            });
        }
    }
}
