package com.ksesoftware.htpig.sosapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksesoftware.htpig.sosapp.R;
import com.ksesoftware.htpig.sosapp.model.Reason;

import java.util.ArrayList;

/**
 * Created by atbic on 7/1/2017.
 */

public class ReasonAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Reason> reasons;
    private LayoutInflater inflater;

    public ReasonAdapter(Context context, ArrayList<Reason> reasons) {
        this.context = context;
        this.reasons = reasons;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return reasons.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_reason, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtReason = (TextView) convertView.findViewById(R.id.txtReason);
            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        Reason reason = reasons.get(position);

        String txtStringReason = reason.getReason();

        if (txtStringReason != null && !txtStringReason.equals("")) {
            viewHolder.txtReason.setText(txtStringReason);
        }


        return convertView;
    }

    private class ViewHolder {

        TextView txtReason;

        public ViewHolder() {

        }
    }

}
