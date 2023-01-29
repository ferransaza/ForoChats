package com.example.forochats.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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

public class RegisterActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView username2 = (TextView) findViewById(R.id.username);
        TextView email = (TextView) findViewById(R.id.email);
        TextView age = (TextView) findViewById(R.id.age);
        TextView dni = (TextView) findViewById(R.id.dni);
        TextView password = (TextView) findViewById(R.id.password);
        TextView password2 = (TextView) findViewById(R.id.password2);
        Button registerButton1 = (Button) findViewById(R.id.register2);

        //final ProgressBar loadingProgressBar = binding.loading;
        registerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(password2.getText().toString())){
                    new Thread(new Runnable() {
                        InputStream stream = null;
                        String str = "";
                        String name;
                        String result = null;
                        public void run() {
                            Log.i("Debug :" ,"Debug");

                            try {
                                InputStream stream = null;
                                //"http://192.168.1.144:9000/Application/ComprarProducte"
                                //http://localhost:9000/Application/entrar?n=Alvaro&password=1234
                                String query = "http://192.168.1.39:9000/Application/registrar?name=" + username2.getText().toString() + "&age=" + age.getText().toString() + "&dni=" + dni.getText().toString() + "&email=" + email.getText().toString() + "&password=" + password.getText().toString();
                                name = email.getText().toString();
                                //String query = String.format("http://10.192.171.29:9000/Application/hello");
                                URL url = new URL(query);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setReadTimeout(10000 );
                                conn.setConnectTimeout(15000 /* milliseconds */);
                                conn.setRequestMethod("POST");
                                conn.setDoInput(true);
                                conn.setDoOutput(true);
                                conn.connect();
//
//                                String params = "name=" + username.getText() + "&age=" + age.getText() + "&dni=" + dni.getText() + "&email=" + email.getText() + "&password" + password.getText();
//                                OutputStream os = conn.getOutputStream();
//                                BufferedWriter writer = new BufferedWriter(
//                                        new OutputStreamWriter(os, "UTF-8"));
//                                writer.write(params);
//                                writer.flush();
//                                writer.close();
//                                os.close();

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
                                    Toast.makeText(getApplicationContext(),"Register succesfull", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    intent.putExtra("name", name);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Error, please try again", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(),"Resposta rebuda thread" + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error, las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}