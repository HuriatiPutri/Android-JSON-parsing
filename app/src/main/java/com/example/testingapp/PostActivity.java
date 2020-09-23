package com.example.testingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testingapp.utils.HttpHandler;

import org.json.JSONObject;

public class PostActivity extends AppCompatActivity {

    EditText edtNama,edtEmail, edtNotelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        edtNama = findViewById(R.id.nama);
        edtEmail = findViewById(R.id.email);
        edtNotelp = findViewById(R.id.notelp);

    }

    public void save(View view) {
        new RequestAsync().execute();
    }

    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                //GET Request
                //return RequestHandler.sendGet("https://prodevsblog.com/android_get.php");

                // POST Request
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("nama", edtNama.getText().toString());
                postDataParams.put("email", edtEmail.getText().toString());
                postDataParams.put("noTelp", edtNotelp.getText().toString());

                return HttpHandler.sendPost("http://192.168.43.246/testhttp/koneksi.php",postDataParams);
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                finish();
            }
        }


    }
}