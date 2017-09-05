package com.ksesoftware.htpig.sosapp.action;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.ksesoftware.htpig.sosapp.R;


/**
 * Created by atbic on 7/3/2017.
 */

public class Activity_Share extends Activity {
    Button btnShare;
    private ImageView imgShareBack;
    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_share);

        btnShare= (Button) findViewById(R.id.btnShare);
        imgShareBack= (ImageView) findViewById(R.id.imgShareBack);
        init();
        shareDialog=new ShareDialog(Activity_Share.this);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.canShow(ShareLinkContent.class)){
                    shareLinkContent =new ShareLinkContent.Builder()
                            .setContentTitle("App SOS")
                            .setContentDescription("App cứu hộ")
                            .setContentUrl(Uri.parse("https://www.youtube.com/watch?v=eOW9lYRpeoA"))
                            .build();
                }
                shareDialog.show(shareLinkContent);
            }
        });

    }

    private void init() {

        imgShareBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

    }
}
