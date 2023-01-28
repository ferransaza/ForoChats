package com.example.forochats.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forochats.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView usernameEditText = (TextView) findViewById(R.id.username);
        //final EditText usernameEditText = binding.username;
        TextView passwordEditText = (TextView) findViewById(R.id.email);
        //final EditText passwordEditText = binding.password;
        Button loginButton = (Button) findViewById(R.id.login);
        Button registerButton = (Button) findViewById(R.id.register);
        //final Button loginButton = binding.login;
        ProgressBar loadingProgressBar = (ProgressBar) findViewById(R.id.loading);
        //final ProgressBar loadingProgressBar = binding.loading;
        String ip = getIpAddress();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                //startActivity(intent);
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("ip", ip);
                startActivity(intent);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
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
                            String query = "http://192.168.1.39:9000/Application/entrar?email=" + usernameEditText.getText().toString() + "&password=" + passwordEditText.getText().toString();
                            name = usernameEditText.getText().toString();
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
                                    int i = Integer.valueOf(sb.toString());
                                    if (i == 200){
                                        Toast.makeText(getApplicationContext(),"Logged", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        intent.putExtra("name", name);
                                        intent.putExtra("ip", ip);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Error logging", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } catch (Exception e) {
                            TextView n = findViewById(R.id.debugText);
                            n.post(new Runnable() {
                                public void run() {
                                    n.setText("Resposta rebuda thread " + e);
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress();
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }
}