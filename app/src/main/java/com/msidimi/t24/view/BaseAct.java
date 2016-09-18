package com.msidimi.t24.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.msidimi.t24.R;

/**
 * Created by mucahit on 18/09/16.
 */
public class BaseAct extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getString(R.string.progress_please_wait));
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (!isFinishing() && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void handleVolleyError(VolleyError error) {
        if (error instanceof NoConnectionError) {
            Toast.makeText(this, getString(R.string.m003), Toast.LENGTH_LONG).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(this, getString(R.string.m002), Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(this, getString(R.string.m004), Toast.LENGTH_LONG).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(this, getString(R.string.m001), Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(this, getString(R.string.m001), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.m001), Toast.LENGTH_LONG).show();
        }
    }
}
