package com.msidimi.t24.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.msidimi.t24.R;
import com.msidimi.t24.common.Constants;

/**
 * Created by mucahit on 18/09/16.
 */
public class SplashAct extends BaseAct {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashAct.this, MainAct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, Constants.SPLASH_TIME);
    }
}
