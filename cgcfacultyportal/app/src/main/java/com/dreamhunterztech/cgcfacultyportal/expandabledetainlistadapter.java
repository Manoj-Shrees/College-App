package com.dreamhunterztech.cgcfacultyportal;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class expandabledetainlistadapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> menu;
    private HashMap<String,List<String>> listsubmenu;
    public expandabledetainlistadapter(FragmentActivity datesheet, ArrayList<String> menu, HashMap<String, List<String>> submenu) {
        this.context=datesheet;
        this.menu=menu;
        this.listsubmenu=submenu;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return listsubmenu.get(menu.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {

        String picres = (String) getChild(groupPosition, childPosition);
        Toast.makeText(context,picres,Toast.LENGTH_SHORT);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandlistsubmenulayout2, null);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listsubmenu.get(menu.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return menu.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return menu.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandlistmenutextlayout, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.expandablelisttext);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private void getimgurl()
    {

    }
}
