package com.example.testingapp.action;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.testingapp.utils.HttpHandler;

import org.json.JSONObject;

public class Delete extends AsyncTask<String,String,String> {
    public static String ID="";
    @Override
    protected String doInBackground(String... strings) {
        try {
            // POST Request
            JSONObject postDataParams = new JSONObject();
            return HttpHandler.sendPost("http://192.168.43.246/testhttp/put.php?id="+ID,postDataParams, "DELETE");
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("TAG", s);
    }
}
