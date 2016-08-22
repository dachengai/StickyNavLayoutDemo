package com.dacheng.coordinatorlayout_recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        public TextViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.item_title);
        }
    }
}
