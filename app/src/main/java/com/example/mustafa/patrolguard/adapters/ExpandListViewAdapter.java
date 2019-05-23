package com.example.mustafa.patrolguard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.mustafa.patrolguard.R;
import com.example.mustafa.patrolguard.models.Checkpoint;
import com.example.mustafa.patrolguard.models.Task;

import java.util.List;

public class ExpandListViewAdapter extends BaseExpandableListAdapter{

    public List<Checkpoint> list_parent;
    public Context context;
    public TextView txt;
    public TextView txt_child;
    public CheckBox checkBox;
    public LayoutInflater inflater;

    @Override
    public int getGroupCount() {

        return list_parent.size();
    }

    public ExpandListViewAdapter(Context context, List<Checkpoint> list_parent) {
        this.context = context;
        this.list_parent = list_parent;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list_parent.get(groupPosition).getTasks().size();
    }

    @Override
    public Checkpoint getGroup(int groupPosition) {

        return list_parent.get(groupPosition);
    }

    @Override
    public Task getChild(int groupPosition, int childPosition) {

        return list_parent.get(groupPosition).getTasks().get(childPosition);

    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {

        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View view, ViewGroup parent) {
        String title_name = getGroup(groupPosition).getCheckpoint_name();

        if(view == null)
        {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group,null);
        }

        txt = (TextView)view.findViewById(R.id.checkpointName);
        txt.setText(title_name);

        boolean expand = false;
        for (Task t: getGroup(groupPosition).getTasks()) {
            if (t.getStatus() == 0) {
                expand = true;
            } else {
                expand = false;
            }
        }

        if (expand) {
            ExpandableListView expandableListView =  (ExpandableListView) parent;
            expandableListView.expandGroup(groupPosition);
        } else {
            checkBox = view.findViewById(R.id.checkpointCheckbox);
            checkBox.setChecked(true);
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View view, ViewGroup parent) {
        String txt_child_name = getChild(groupPosition, childPosition).getTask_name();
        int status = getChild(groupPosition, childPosition).getStatus();
        if(view==null)
        {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }
        txt_child = view.findViewById(R.id.taskName);
        txt_child.setText(txt_child_name);

        checkBox = view.findViewById(R.id.taskCheckbox);
        checkBox.setChecked(status == 1);

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



}