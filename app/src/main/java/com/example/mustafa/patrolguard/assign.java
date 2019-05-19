package com.example.mustafa.patrolguard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class assign extends AppCompatActivity {

    TextView tx;
    Spinner task;
    Spinner area;
    HashMap<String,String> taskId;
    HashMap<String,String> areaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);
        tx = (TextView) findViewById(R.id.textView3);
        tx.setText(MainActivity.clickedName);
        task = (Spinner) findViewById(R.id.spinner3);

        area = (Spinner) findViewById(R.id.spinner2);

        taskId = new HashMap<>();
        areaId = new HashMap<>();

        Downloadx dw = new Downloadx();
        dw.execute("http://evirgenmert.com/admin/task/show");
        //tx.setText(res);
    }
    public <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    public void assigntsk(View view)
    {
       // Toast.makeText(this,getKeyByValue(taskId,String.valueOf(task.getSelectedItem())),Toast.LENGTH_LONG).show();
        String task_id = getKeyByValue(taskId,(String)(task.getSelectedItem()));
        String area_id = getKeyByValue(areaId,(String)(area.getSelectedItem()));

        try{
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url ="http://evirgenmert.com/admin/personnel/assignTask";
        // id_task, id_personnel, id_checkpoint

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id_task", task_id);
            jsonBody.put("id_personnel", MainActivity.clickedId);
            jsonBody.put("id_checkpoint", area_id);
        final String requestBody = jsonBody.toString();

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("responsef = [" + response + "]");
                    if(response.equals("200"))
                    {
                        Sc.msg = "TASK ASSIGNED";
                        Intent a = new Intent(getBaseContext(),Sc.class);
                        startActivity(a);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }}protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers

                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
// Add the request to the RequestQueue.
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    SimpleAdapter simpleAdapter;
    public class Downloadx extends AsyncTask<String,Void,String>
    {
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
            }
            catch (Exception e) {
                Log.i(TAG, "Error reading InputStream");
                result = null;
            }
            finally {

                Log.i("SORUNVAAAAAAAAAR",result);

                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (IOException e) {
                        Log.i(TAG, "Error closing InputStream");
                    }
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{


                HashMap<String, String> hashMap = new HashMap<>();

                JSONArray dataArray = new JSONArray(s);

                String parsed="";



                List<String> tlist = new ArrayList<String>();
                List<String> alist = new ArrayList<String>();

                for(int i=0;i< dataArray.length();i++)
                {
                    JSONObject c = dataArray.getJSONObject(i);

                    String tname = c.getString("task_name");

                    taskId.put(c.getString("id"),tname);
                    tlist.add(tname);

                    String areanm = c.getString("area_name");
                    areaId.put(c.getString("area_id"),areanm);
                    alist.add(areanm);

                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_spinner_item, tlist);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                task.setAdapter(dataAdapter);

                ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_spinner_item, alist);
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                area.setAdapter(dataAdapter2);
                //textViewx.setText(parsed);




               /* List<HashMap<String,String>> list = new ArrayList<>();
                simpleAdapter= new SimpleAdapter(getApplicationContext(),list,R.layout.activity_listitem,
                        new String[] {"First","Second"},
                        new int[]{R.id.textView,R.id.textView2});

                Iterator it = hashMap.entrySet().iterator();
                while(it.hasNext())
                {
                    HashMap<String,String> result = new HashMap<>();
                    Map.Entry pair = (Map.Entry) it.next();
                    result.put("First",pair.getKey().toString());
                    result.put("Second",pair.getValue().toString());
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
            }
            catch (Exception ex){
                ex.printStackTrace();}
        }

        @Override
        protected String doInBackground(String... strings) {
            // id_task, id_personnel, id_checkpoint
            String result="";
            URL url;
//            trustEveryone();
            HttpURLConnection httpURLConnection;

            try
            {
                url = new URL("http://evirgenmert.com/admin/task/show");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");

                httpURLConnection.connect();
                System.out.println("stringset = [" + String.valueOf(httpURLConnection.getResponseCode()) + "]");
                result = readInputStreamToString(httpURLConnection);



                //result = reader.readLine();
                System.out.println("ressf = [" + result + "]");
               /* InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader read = new InputStreamReader(inputStream);


               int data = read.read();
                while(data>0)
                {
                     char ch = (char) data;
                     result+= ch;
                     data=read.read();
                }*/

            }
            catch (Exception e){System.out.println("ress = [" + e.toString()+ "]");return null;}
            return result;
        }
        private void trustEveryone() {
            try {
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }});
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, new X509TrustManager[]{new X509TrustManager(){
                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {}
                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {}
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }}}, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(
                        context.getSocketFactory());
            } catch (Exception e) { // should never happen
                e.printStackTrace();
            }
        }
    }
}