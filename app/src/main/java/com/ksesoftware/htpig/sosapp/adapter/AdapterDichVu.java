package com.ksesoftware.htpig.sosapp.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ksesoftware.htpig.sosapp.R;
import com.ksesoftware.htpig.sosapp.model.DichVu;

import java.util.ArrayList;

/**
 * Created by thuqu on 8/17/2017.
 */

public class AdapterDichVu extends ArrayAdapter<DichVu>{
    Context context;
    ArrayList<DichVu> arrayList;
    int id;
    public static String chuoi = "";
    public boolean sellecAll = false;
    public ViewGroup group;

    public AdapterDichVu(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<DichVu> objects) {
        super(context, resource, objects);
        this.arrayList = new ArrayList<DichVu>();
        this.arrayList.addAll(objects);
        this.context = context;
        this.id = resource;
        this.arrayList = objects;
    }

    private class ViewHolder {
        TextView txtTenDichVu;
        TextView txtIdDichVu;
        CheckBox checkDichVu;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_dialog_dichvu, null);
            viewHolder = new ViewHolder();
            viewHolder.txtTenDichVu = (TextView) convertView.findViewById(R.id.txtDichVu);
            viewHolder.checkDichVu = (CheckBox) convertView.findViewById(R.id.checkDichVu);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.checkDichVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CheckBox cb = (CheckBox) view;
                DichVu country = (DichVu) cb.getTag();


                if (position == 0) {
                    sellecAll = finalViewHolder.checkDichVu.isChecked();
                    for (int i = 0; i < arrayList.size(); i++) {
                        arrayList.get(i).setCheckBox(sellecAll);
                    }
                    notifyDataSetChanged();
                } else {
                    boolean key = true;
                    arrayList.get(position).setCheckBox(finalViewHolder.checkDichVu.isChecked());
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (!arrayList.get(i).isCheckBox()) {
                            key = false;
                            break;
                        }
                    }
                    arrayList.get(0).setCheckBox(key);
                    notifyDataSetChanged();
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 1; i < arrayList.size(); i++) {
                            if (!arrayList.get(i).isCheckBox()) {
                                arrayList.get(0).setCheckBox(false);
                                break;
                            }
                            if (arrayList.size() - 1 == i) {
                                arrayList.get(0).setCheckBox(true);
                            }
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        });
        DichVu dichVu = arrayList.get(position);
        viewHolder.txtTenDichVu.setText(dichVu.getTenDichVu());
        viewHolder.checkDichVu.setChecked(dichVu.isCheckBox());
        viewHolder.checkDichVu.setTag(dichVu);
        this.group = parent;
        return convertView;
    }
}
