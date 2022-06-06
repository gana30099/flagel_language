package com.example.foraddingtoserverio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapterDel extends RecyclerView.Adapter<CustomAdapterDel.ViewHolder> {

    //
    private LayoutInflater mInflater;
    private String[] localDataSet;
    private String[] localDataSet2;

    public CustomAdapterDel(Context context, String[] data, String[] data2) {
        this.mInflater = LayoutInflater.from(context);
        this.localDataSet = data;
        this.localDataSet2 = data2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mother;
        private final TextView other;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

                // Define click listener for the ViewHolder's View
                mother = (TextView) itemView.findViewById(R.id.motherDel);
                other = (TextView) itemView.findViewById(R.id.otherDel);


        }

        public TextView getTextViewMother() {
            return mother;
        }

        public TextView getTextViewHidden() {
            return other;
        }
    }

    @NonNull
    @Override
    public CustomAdapterDel.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_items_2, parent, false);

        return new CustomAdapterDel.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextViewMother().setText(localDataSet[position]);
        holder.getTextViewHidden().setText(localDataSet2[position]);
    }


    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
