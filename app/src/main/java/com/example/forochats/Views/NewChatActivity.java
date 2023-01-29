package com.example.forochats.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forochats.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        String ip = getIntent().getExtras().getString("ip");
        String name = getIntent().getExtras().getString("name");
        TextView theme = (TextView) findViewById(R.id.theme);
        Button create_chat = (Button) findViewById(R.id.create_chat);
        create_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (theme.getText().toString().isEmpty() == false) {
                    new Thread(new Runnable() {
                        InputStream stream = null;
                        String str = "";
                        String result = null;

                        public void run() {
                            try {
                                InputStream stream = null;
                                //"http://192.168.1.144:9000/Application/ComprarProducte"
                                //http://localhost:9000/Application/entrar?n=Alvaro&password=1234
                                String query = "http://192.168.1.39:9000/Application/crearchat?theme=" + theme.getText().toString() + "&name=" + name;
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
                                Toast.makeText(getApplicationContext(), i, Toast.LENGTH_SHORT).show();
                                if (i == 200) {
                                    //Toast.makeText(getApplicationContext(), "Chat Created succesfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("ip", ip);
                                    startActivity(intent);
                                } else {
                                    //Toast.makeText(getApplicationContext(), "Error creating the chat", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"Resposta rebuda thread" + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
            }
            else{
                Toast.makeText(getApplicationContext(), "Please fill the chat's name first", Toast.LENGTH_SHORT).show();
            }
            };
        });
    }
}
