package com.example.mustafa.patrolguard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Handler handler;
    public static String clickedId = "";
    public static String clickedName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);


        handler = new Handler();
        try {


            Runnable run;
            run = new Runnable() {

                public void run() {
                    Download dw = new Download();

                    String url = "http://yags.xyz/admin/personnel/show";
                    dw.execute(url);

                    handler.postDelayed(this, 2000);
                }
            };
            handler.post(run);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap<String, String> axax = (HashMap<String, String>) listView.getItemAtPosition(position);

                clickedId = axax.get("First").toString();
                clickedName = axax.get("Second").toString();
                Toast.makeText(MainActivity.this, axax.get("First").toString(), Toast.LENGTH_LONG).show();

                Intent a = new Intent(getBaseContext(), assign.class);
                startActivityForResult(a, 1);

            }
        });
    }

    SimpleAdapter simpleAdapter;

    public class Download extends AsyncTask<String, Void, String> {
        private String readInputStreamToString(HttpURLConnection connection) {
            String result = null;
            StringBuffer sb = new StringBuffer();
            InputStream is = null;
            String TAG = "MyAwesomeApp";

            try {
                is = new BufferedInputStream(connection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                result = sb.toString();
            } catch (Exception e) {
                Log.i(TAG, "Error reading InputStream");
                result = null;
            } finally {

                Log.i("SORUNVAAAAAAAAAR", result);

                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Log.i(TAG, "Error closing InputStream");
                    }
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {


                HashMap<String, String> hashMap = new HashMap<>();

                JSONArray dataArray = new JSONArray(s);

                String parsed = "";
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject c = dataArray.getJSONObject(i);

                    String id = c.getString("id");
                    System.out.println("soad = [" + id + "]");
                    String nm = c.getString("name");
                    String lastlog = c.getString("lastLogin");
                    parsed += "ID : " + id + " - " + nm + " - " + lastlog + "\n";


                    hashMap.put(id, nm + " - " + lastlog);
                }
                //textViewx.setText(parsed);


                List<HashMap<String, String>> list = new ArrayList<>();
                simpleAdapter = new SimpleAdapter(getApplicationContext(), list, R.layout.activity_listitem,
                        new String[]{"First", "Second"},
                        new int[]{R.id.textView, R.id.textView2});

                Iterator it = hashMap.entrySet().iterator();
                while (it.hasNext()) {
                    HashMap<String, String> result = new HashMap<>();
                    Map.Entry pair = (Map.Entry) it.next();
                    result.put("First", pair.getKey().toString());
                    result.put("Second", pair.getValue().toString());
                    list.add(result);
                }
                listView.setAdapter(simpleAdapter);
                listView.notifyAll();
                /*jsonObject = new JSONObject(dataKismi.trim());
                Iterator<?> keys = jsonObject.keys();

                while( keys.hasNext() ) {
                    String key = (String)keys.next();
                    if ( jsonObject.get(key) instanceof JSONObject ) {
                        System.out.println("s = [" + key + "]");

                    }
                }*/
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
//            trustEveryone();
            HttpURLConnection httpURLConnection;

            try {
                url = new URL("http://evirgenmert.com/admin/personnel/show");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                System.out.println("stringset = [" + String.valueOf(httpURLConnection.getResponseCode()) + "]");
                result = readInputStreamToString(httpURLConnection);


                //result = reader.readLine();
                System.out.println("ress = [" + result + "]");
               /* InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader read = new InputStreamReader(inputStream);


               int data = read.read();
                while(data>0)
                {
                     char ch = (char) data;
                     result+= ch;
                     data=read.read();
                }*/

            } catch (Exception e) {
                System.out.println("ress = [" + e.toString() + "]");
                return null;
            }
            return result;
        }

        private void trustEveryone() {
            try {
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, new X509TrustManager[]{new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }}, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(
                        context.getSocketFactory());
            } catch (Exception e) { // should never happen
                e.printStackTrace();
            }
        }
    }

}

