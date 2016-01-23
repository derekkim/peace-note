package com.haranghaon.peacenote;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

public class ProgressAsync extends AsyncTask<Void, Void, Void> {

    private Context mContext;

    public ProgressAsync(Context context) {
        mContext = context;
    }

    Button bt;
    ProgressDialog progressDialog;

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
    }
}
