package com.manoideveloppers.ilovezappos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter <RecycleAdapter.RecycleViewHolder> {
    private ArrayList <RecyleELements> recyleELements;

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_layout,parent,false);
        RecycleViewHolder recycleViewHolder = new RecycleViewHolder(view);
        return recycleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        RecyleELements currentItem = recyleELements.get(position);
        holder.textView1.setText(currentItem.getText1());
        holder.textView2.setText(currentItem.getText2());
        holder.textView3.setText(currentItem.getText3());
        holder.textView4.setText(currentItem.getText4());

    }

    @Override
    public int getItemCount() {
        return recyleELements.size();
    }

    public RecycleAdapter (ArrayList<RecyleELements> recyleELementsArrayList){
        recyleELements = recyleELementsArrayList;
    }

    public static class RecycleViewHolder extends RecyclerView.ViewHolder{
        TextView textView1,textView2,textView3,textView4 ;
        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);

           textView1 = itemView.findViewById(R.id.bidAmount);
           textView2 = itemView.findViewById(R.id.bidValue);
           textView3 = itemView.findViewById(R.id.askValue);
           textView4 = itemView.findViewById(R.id.askAmount);
        }
    }
}
