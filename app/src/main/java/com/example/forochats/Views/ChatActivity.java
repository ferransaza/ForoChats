package com.example.forochats.Views;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forochats.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public String a = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String name = getIntent().getExtras().getString("name");
        String theme = getIntent().getExtras().getString("theme");
        ListView list = (ListView) findViewById(R.id.list);
        TextView MessageEditText = (TextView) findViewById(R.id.NewMessage);
        Button SenMsgButton = (Button) findViewById(R.id.Sendmsg);
        SenMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    InputStream stream = null;
                    String str = "";
                    String result = null;

                    public void run() {
                        try {
                            InputStream stream = null;
                            //"http://192.168.1.144:9000/Application/ComprarProducte"
                            //http://localhost:9000/Application/entrar?n=Alvaro&password=1234
                            String query = "http://192.168.1.39:9000/Application/crearmensaje?mensaje=" + MessageEditText.getText().toString() + "&theme=" + theme + "&email=" + name;
                            //String query = String.format("http://10.192.171.29:9000/Application/hello");
                            URL url = new URL(query);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(10000);
                            conn.setConnectTimeout(15000 /* milliseconds */);
                            conn.setRequestMethod("POST");
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
                            int i = Integer.valueOf(sb.toString());
                            Looper.prepare();
                            if (i == 200){
                                MessageEditText.setText("");
                                mostrar_chat(theme, list);
                            }
                        } catch (Exception e) {
                            Looper.prepare();
                                Toast.makeText(getApplicationContext(),"Error, please try again later" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
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
                    String query = "http://192.168.1.39:9000/Application/getchat?theme=" + s;
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
                    a = sb.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(sb.toString().equals("") == false) {
                                mostrar_lista2(sb.toString(), l);
                            }
                        }
                    });
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),"22222222222222222" + e, Toast.LENGTH_SHORT).show();
                    TextView n = findViewById(R.id.debugText12);
                    n.post(new Runnable() {
                        public void run() {
                            n.setText("ERROR: " + e);
                        }
                    });
                }
            }
        }).start();
    }

    public void mostrar_lista2(String str, ListView l) {
        List<String> string3 = new ArrayList<String>();
        List<String> strings4 = new ArrayList<String>();
        if (str.toString() != "[]") {
            String[] strings2 = str.toString().split("/");
            for (int k = 0; k < strings2.length; k = k + 2) {
                string3.add(strings2[k]);
                strings4.add(strings2[k + 1]);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, string3);
            l.setAdapter(adapter);
        }

    }
}