package com.example.testingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testingapp.action.Delete;
import com.example.testingapp.utils.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://192.168.43.246/testhttp/index.php";
//    private static String url = "https://api.spoonacular.com/recipes/random?apiKey=a1f5ad16ddb444a69313d35fb006f706&includeNutrition=true.";

    ArrayList<HashMap<String, String>> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        lv.setOnItemLongClickListener(this);
        lv.setOnItemClickListener(this);

        new GetContacts().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new GetContacts().execute();
    }

    public void add(View view) {
        startActivity(new Intent(MainActivity.this, PostActivity.class));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView id = view.findViewById(R.id.id);
        Toast.makeText(getApplicationContext(),id.getText().toString(), Toast.LENGTH_LONG).show();

//        menyimpan id data yg dipilih ke variabel ID di Class SendDelete
        Delete.ID = id.getText().toString();

        new Delete().execute();
//        Bersihkan ListView
        contactList.clear();
//        Load ulang data ke listview
        new GetContacts().execute();
        return  true;
    }


    public void Refresh(View view) {
        contactList.clear();
        new GetContacts().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView id = view.findViewById(R.id.id);
        TextView nama = view.findViewById(R.id.name);
        TextView email = view.findViewById(R.id.email);
        TextView notelp = view.findViewById(R.id.mobile);

        Intent intent = new Intent(this, PutActivity.class);
        intent.putExtra("id", id.getText().toString());
        intent.putExtra("nama", nama.getText().toString());
        intent.putExtra("email", email.getText().toString());
        intent.putExtra("notelp", notelp.getText().toString());
        startActivity(intent);
    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    JSONObject contacts = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("nama");
                        String email = c.getString("email");
                        String mobile = c.getString("notelp");
                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[]{"id","name", "email",
                    "mobile"}, new int[]{R.id.id,R.id.name,
                    R.id.email, R.id.mobile});

            lv.setAdapter(adapter);
        }

    }


}