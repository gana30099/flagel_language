package com.example.foraddingtoserverio;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
Use recycler view to refresh the data in it
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    //
    private LayoutInflater mInflater;
    private String[] localDataSet;
    private String[] localDataSet2;

    public CustomAdapter (Context context, String[] data, String[] data2) {
        this.mInflater = LayoutInflater.from(context);
        localDataSet = data;
        localDataSet2 = data2;    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mother;
        private final TextView hidden;
        private final EditText empty;
        public MyCustomEditTextListener myCustomEditTextListener;

        public ViewHolder(View view, MyCustomEditTextListener myCustomEditTextListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            this.myCustomEditTextListener = myCustomEditTextListener;
            mother = (TextView) view.findViewById(R.id.mother2);
            hidden = (TextView) view.findViewById(R.id.invisible);
            empty = (EditText) view.findViewById(R.id.other);
            empty.addTextChangedListener(myCustomEditTextListener);

        }

        public TextView getTextViewMother() {
            return mother;
        }

        public TextView getTextViewHidden() {
            return hidden;
        }

        /*
        Todo refresh?
         */
        public EditText getEditTextEmpty() {
            return empty;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(String[] dataSet, String[] dataSet2) {
        localDataSet = dataSet;
        localDataSet2 = dataSet2;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_view_items, viewGroup, false);

        return new ViewHolder(view, new MyCustomEditTextListener());
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextViewMother().setText(localDataSet[position]);
        viewHolder.getTextViewHidden().setText(localDataSet2[position]);
        viewHolder.getEditTextEmpty().setText("");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            localDataSet2[position] = charSequence.toString();
            System.out.println("--------localDataSet2[position] : " + localDataSet2[position] );
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
