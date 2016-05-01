package com.qingcong.slidedelete;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by chenpengfei on 2016/5/1.
 */
public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        String str = getIntent().getStringExtra("content");
        ((TextView)findViewById(R.id.tid)).setText(str);
    }
}
