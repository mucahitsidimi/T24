package com.msidimi.t24.view;

import android.support.v4.app.Fragment;
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
public class BaseFrag extends Fragment {

    public void handleVolleyError(VolleyError error) {
        if (error instanceof NoConnectionError) {
            Toast.makeText(getActivity(), getString(R.string.m003), Toast.LENGTH_LONG).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(getActivity(), getString(R.string.m002), Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(getActivity(), getString(R.string.m004), Toast.LENGTH_LONG).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(getActivity(), getString(R.string.m001), Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(getActivity(), getString(R.string.m001), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.m001), Toast.LENGTH_LONG).show();
        }
    }
}
