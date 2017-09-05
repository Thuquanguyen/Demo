package com.ksesoftware.htpig.sosapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ksesoftware.htpig.sosapp.R;


/**
 * Created by atbic on 7/6/2017.
 */

public class RadiusDialog extends Dialog {
    private ListenDialog listenDialog;
    Button btnCaiDat;
    private Context context;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    public RadiusDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.radius_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initUI();

    }

    private void initUI() {
        radioGroup = (RadioGroup) findViewById(R.id.rgCaiDat);

        btnCaiDat= (Button) findViewById(R.id.btnCaiDat);
        btnCaiDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

               String radiusString =  radioButton.getText().toString();

                listenDialog.getStringDialog(radiusString);
                dismiss();
            }
        });
    }
    public void setListenDialog(ListenDialog listenDialog){
        this.listenDialog=listenDialog;
    }
    public interface ListenDialog{
        void getStringDialog(String content);
    }
}
