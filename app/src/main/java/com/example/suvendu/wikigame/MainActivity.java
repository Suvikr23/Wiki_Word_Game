package com.example.suvendu.wikigame;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random ran = new Random();
    int random, pageid;
    TextView wikiText;
    private ProgressBar progressBar;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    Context context = getBaseContext();
    private ListPopupWindow listPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wikiText = (TextView) findViewById(R.id.wikitext);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.lv);
        listPopupWindow = new ListPopupWindow(MainActivity.this);
        Log.v("On click","After Context");
        listPopupWindow.setModal(true);//If the user touches outside the popup window's content area the popup window will be dismissed.
        Log.v("On click","After Modal");
        listPopupWindow.setWidth(300);

        Log.v("On click","After Width");
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        Log.v("On click","After Height");
        //random = ran.nextInt(500000);
        String url = "https://en.wikipedia.org/w/api.php?action=query&&format=json&prop=extracts&exintro=true&explaintext=&exsentences=10&titles=Code";
        new ReadJsonFeed().execute(url);
    }

    public class ReadJsonFeed extends AsyncTask<String, String, String> {

        //@TargetApi(19)
        @Override
        protected void onPostExecute(String s) {

            progressBar.setVisibility(View.GONE);

            try {
                JSONObject object = new JSONObject(s);

                /*JSONArray error = new JSONArray(object.getJSONArray("5225"));
                JSONObject extract = error.getJSONObject(3);*/

                String query = object.getString("query");
                JSONObject pages = new JSONObject(query);
                String page = pages.getString("pages");
                JSONObject num5225 = new JSONObject(page);
                String number = num5225.getString("5225");
                JSONObject extract = new JSONObject(number);
                String extra = extract.getString("extract");
                Log.v("Extract", extra);

                //Getting processed data from the class

                GetProcessedData gpd = new GetProcessedData();
                wikiText.setText(gpd.processData(extra, context,listPopupWindow));
                /*arrayList = new ArrayList<>(Arrays.asList(gpd.getAdapterWords()));
                adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);*/
                listPopupWindow.setAdapter(new ArrayAdapter(MainActivity.this,R.layout.list_item,gpd.getAdapterWords()));
                Log.v("On click","After Array Adapter");
                //listPopupWindow.setAnchorView(wikiText);
                wikiText.setMovementMethod(LinkMovementMethod.getInstance());

                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG);
                        Log.v("OnItemClick","inside on item click");
                        listPopupWindow.dismiss();
                    }
                });

            } catch (JSONException e) {
                wikiText.setText(e.getMessage());
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... urls) {

            //HttpURLConnection con ;
            int code;
            HttpClient client = new DefaultHttpClient();
            StringBuilder sb = new StringBuilder();
            HttpPost post = new HttpPost(urls[0]);
            try {
                HttpResponse response = client.execute(post);
                StatusLine line = response.getStatusLine();
                code = line.getStatusCode();
                if (code == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String str;

                    while ((str = br.readLine()) != null) {

                        sb.append(str);
                        Log.v("Readline", str);

                    }
                } else

                    return " " + code + response.getStatusLine().toString();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            Log.v("return", sb.toString());
            return sb.toString();
        }
    }
}
