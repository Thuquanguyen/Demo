package com.ksesoftware.htpig.sosapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ksesoftware.htpig.sosapp.R;
import com.ksesoftware.htpig.sosapp.model.Xe;

import java.util.ArrayList;

/**
 * Created by thuqu on 8/16/2017.
 */

public class XeAdapter extends BaseExpandableListAdapter {

    private Context context;
    public static String res = "";
    private ArrayList<String> groups;
    private ArrayList<ArrayList<Xe>> colors;
    private LayoutInflater inflater;
    int count = 0;
    int dem = 0;

    public XeAdapter(Context context,
                     ArrayList<String> groups,
                     ArrayList<ArrayList<Xe>> colors) {
        this.context = context;
        this.groups = groups;
        this.colors = colors;
        inflater = LayoutInflater.from(context);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return colors.get(groupPosition).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) (groupPosition * 1024 + childPosition);  // Max 1024 children per group
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = null;
        count = count + 1;
        final int i = colors.size();
        Log.e("DKM", i + "");
        if (convertView != null)
            v = convertView;
        else
            v = inflater.inflate(R.layout.child_row, parent, false);
        final Xe c = (Xe) getChild(groupPosition, childPosition);

        final TextView color = (TextView) v.findViewById(R.id.childname);
        if (color != null)
            color.setText(c.getTenXe());

        final CheckBox cb = (CheckBox) v.findViewById(R.id.check1);
        cb.setChecked(c.isState());

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb.isChecked() == true) {
                    if (dem < 10) {
                        c.setState(true);
                        res = res + "," + c.getIdXe();
                        Toast.makeText(context, res, Toast.LENGTH_LONG).show();
                    } else if (dem >= 10) {
                        c.setState(false);
                        Toast.makeText(context, "bạn đã chọn quá 10 dịch vụ", Toast.LENGTH_LONG).show();
                        cb.setChecked(false);
                    }
                    dem++;
                } else {
                    if (res.length() > 4) {
                        res = res.substring(0,res.length() - 4);
                        dem--;
                    } else {
                        res = "";
                        dem=0;
                    }
                    Toast.makeText(context, "" + res, Toast.LENGTH_SHORT).show();
                    c.setState(false);
                }

            }
        });

        return v;
    }

    public int getChildrenCount(int groupPosition) {
        return colors.get(groupPosition).size();
    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return (long) (groupPosition * 1024);  // To be consistent with getChildId
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = null;
        if (convertView != null)
            v = convertView;
        else
            v = inflater.inflate(R.layout.group_row, parent, false);
        String gt = (String) getGroup(groupPosition);
        TextView colorGroup = (TextView) v.findViewById(R.id.childname);
        if (gt != null)
            colorGroup.setText(gt);
        return v;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void onGroupCollapsed(int groupPosition) {
    }

    public void onGroupExpanded(int groupPosition) {
    }


}
