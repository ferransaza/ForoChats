package com.example.forochats.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forochats.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {

    public String a = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String name = getIntent().getExtras().getString("name", "");
        ListView list = (ListView) findViewById(R.id.list);

        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            public void run() {
                Log.i("Debug :" ,"Debug");

                try {
                    InputStream stream = null;
                    //"http://192.168.1.144:9000/Application/ComprarProducte"
                    //http://localhost:9000/Application/entrar?n=Alvaro&password=1234
                    String query = "http://192.168.1.139:9000/Application/getchats?";
                    //String query = String.format("http://10.192.171.29:9000/Application/hello");
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 );
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();

                    //send parameters in message body
                    // n.setText("Esperant resposta  thread");
                    //receive response from server
                    stream = conn.getInputStream();
                    BufferedReader reader;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    // n.setText("Resposta rebuda  thread" + sb);
                    TextView n = findViewById(R.id.debugText);
                    n.post(new Runnable() {
                        public void run() {
                            a = sb.toString();
                            mostrar_lista(sb.toString(), list);
                        }
                    });
                } catch (Exception e) {
                    TextView n = findViewById(R.id.debugText);
                    n.post(new Runnable() {
                        public void run() {
                            n.setText("Catch: " + e);
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = list.getItemAtPosition(i).toString();
                openChatActivity(s, name);

            }
        });
    }

    public void mostrar_chat(String s, ListView l){
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            public void run() {
                try {
                    InputStream stream = null;
                    //"http://192.168.1.144:9000/Application/ComprarProducte"
                    //http://localhost:9000/Application/entrar?n=Alvaro&password=1234
                    String query = "http://192.168.1.139:9000/Application/getchat?theme=" + s;
                    //String query = String.format("http://10.192.171.29:9000/Application/hello");
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 );
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();

                    //send parameters in message body
                    // n.setText("Esperant resposta  thread");
                    //receive response from server
                    stream = conn.getInputStream();
                    BufferedReader reader;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    // n.setText("Resposta rebuda  thread" + sb);
                    TextView n = findViewById(R.id.debugText);
                    n.post(new Runnable() {
                        public void run() {
                            a = sb.toString();
                            n.setText(sb.toString());
                        }
                    });
                } catch (Exception e) {
                    TextView n = findViewById(R.id.debugText);
                    n.post(new Runnable() {
                        public void run() {
                            n.setText("Catch: " + e);
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void mostrar_lista(String str, ListView l){
        String[] strings2 = str.split("/");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, strings2);
        l.setAdapter(adapter);
    }

    private void openChatActivity(String s, String name) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("theme", s);
        startActivity(intent);
    }
}
