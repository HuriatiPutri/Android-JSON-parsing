package com.example.testingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testingapp.utils.HttpHandler;

import org.json.JSONObject;

public class PutActivity extends AppCompatActivity {

    EditText edtNama,edtEmail, edtNotelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        String id = getIntent().getStringExtra("id");
        String nama = getIntent().getStringExtra("nama");
        String email = getIntent().getStringExtra("email");
        String notelp = getIntent().getStringExtra("notelp");

        edtNama = findViewById(R.id.nama);
        edtEmail = findViewById(R.id.email);
        edtNotelp = findViewById(R.id.notelp);

        edtNama.setText(nama);
        edtEmail.setText(email);
        edtNotelp.setText(notelp);

    }

    public void save(View view) {
        new RequestAsync().execute();
    }

    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                // POST Request
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("nama", edtNama.getText().toString());
                postDataParams.put("email", edtEmail.getText().toString());
                postDataParams.put("noTelp", edtNotelp.getText().toString());

                return HttpHandler.sendPost("http://192.168.43.246/testhttp/put.php",postDataParams,"PUT");
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