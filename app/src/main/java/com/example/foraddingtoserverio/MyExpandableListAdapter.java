package com.example.foraddingtoserverio;

import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter implements ExpandableListAdapter {

    private List<String> groupList = null;
    private Map<String, List<String>> childListMap = null;
    private MainActivity ipPlusPortMain;

    public MyExpandableListAdapter(List<String> groupList, Map<String, List<String>> childListMap, MainActivity ip ) {
        this.groupList = groupList;
        this.childListMap = childListMap;
        this.ipPlusPortMain = ip;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupIndex) {
        String group = groupList.get(groupIndex);
        List<String> childInfoList = childListMap.get(group);
        return childInfoList.size();
    }

    @Override
    public Object getGroup(int groupIndex) {
        return groupList.get(groupIndex);
    }

    @Override
    public Object getChild(int groupIndex, int childIndex) {
        String group = groupList.get(groupIndex);
        List<String> childInfoList = childListMap.get(group);
        return childInfoList.get(childIndex);
    }

    @Override
    public long getGroupId(int groupIndex) {
        return groupIndex;
    }

    @Override
    public long getChildId(int groupIndex, int childIndex) {
        return childIndex;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    // This method will return a View object displayed in group list item.
    @Override
    public View getGroupView(int groupIndex, boolean isExpanded, View view, ViewGroup viewGroup) {
        // Create the group view object.
        LinearLayout groupLayoutView = new LinearLayout(this.ipPlusPortMain);
        groupLayoutView.setOrientation(LinearLayout.HORIZONTAL);

        // Create and add an imageview in returned group view.
        //ImageView groupImageView = new ImageView(ExpandableListViewActivity.this);
                /*if(isExpanded) {
                    groupImageView.setImageResource(R.mipmap.ic_launcher_round);
                }else
                {
                    groupImageView.setImageResource(R.mipmap.ic_launcher);
                }
                groupLayoutView.addView(groupImageView);*/

        // Create and add a textview in returned group view.
        String groupText = groupList.get(groupIndex);
        TextView groupTextView = new TextView(this.ipPlusPortMain);
        groupTextView.setText(groupText);
        groupTextView.setTextSize(30);
        groupLayoutView.addView(groupTextView);

        return groupLayoutView;
    }

    // This method will return a View object displayed in child list item.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getChildView(int groupIndex, int childIndex, boolean isLastChild, View view, ViewGroup viewGroup) {
        // First get child text/
        Object childObj = this.getChild(groupIndex, childIndex);
        String childText = (String)childObj;

        // Create a TextView to display child text.
        TextView childTextView = new TextView(this.ipPlusPortMain);
        childTextView.setText(childText);
        childTextView.setTextSize(20);
        //childTextView.setBackgroundColor(Color.GREEN);

        // Get group image width.
        Drawable groupImage = this.ipPlusPortMain.getDrawable(R.mipmap.ic_launcher);
        int groupImageWidth = groupImage.getIntrinsicWidth();

        // Set child textview offset left. Then it will align to the right of the group image.
        childTextView.setPadding(groupImageWidth,0,0,0);

        return childTextView;
    }

    @Override
    public boolean isChildSelectable(int groupIndex, int childIndex) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupIndex) {

    }

    @Override
    public void onGroupCollapsed(int groupIndex) {


    }

    @Override
    public long getCombinedChildId(long groupIndex, long childIndex) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupIndex) {
        return 0;
    }
}
