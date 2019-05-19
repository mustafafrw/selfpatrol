package com.example.mustafa.patrolguard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class tasks extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    public static String userId="1";
    public static HashMap<String,CheckPoint> checkwithIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tasks);

        checkwithIds = new HashMap<>();
        listView = (ExpandableListView)findViewById(R.id.lvExp);


        getTasks();
       // initData();
        listAdapter = new ExpandableListAdapter(getBaseContext(),listDataHeader,listHash);
        System.out.println("savedInstanceStatex = [" + listDataHeader.size() + "]");

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                System.out.println("tiklandi");
                System.out.println("parent = [" + parent + "], v = [" + v + "], groupPosition = [" + groupPosition + "], childPosition = [" + childPosition + "], id = [" + id + "]");

              /*  Object childName = listAdapter.getChild(groupPosition,childPosition);
                Object parentName = listAdapter.getGroup(groupPosition);*/
                CheckPoint cp = checkwithIds.get(String.valueOf(groupPosition)+"-"+String.valueOf(childPosition));

                Main2Activity.currentpoint =cp;

                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(intent);

                return false;
            }

        });
    }

    private void getTasks()
    {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest reqget = new JsonArrayRequest (Request.Method.GET,
                "http://evirgenmert.com/personnel/taskList/1",
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i=0;i<response.length();i++) {

                    try {
                        JSONObject checkPointList = response.getJSONObject(i);



                        JSONArray taskList = checkPointList.getJSONArray("tasks");

                        List<String> taskNames = new ArrayList<>();
                        for(int j=0;j< taskList.length();j++)
                        {
                            JSONObject taskObj = taskList.getJSONObject(j);
                            String checkhed="";
                            if(taskObj.getString("status").equals("0"))
                                checkhed="^";
                            taskNames.add(taskObj.getString("task_name")+checkhed);

                            CheckPoint cp = new CheckPoint();
                            cp.setId(checkPointList.getString("id"));
                            cp.setId_task(taskObj.getString("id_task"));
                            cp.setTask_name(taskObj.getString("task_name"));
                            cp.setName(checkPointList.getString("checkpoint_name"));
                            cp.setArea_name(checkPointList.getString("area_name"));
                            checkwithIds.put(String.valueOf(i)+"-"+String.valueOf(j),cp);
                        }

                        listDataHeader.add(checkPointList.getString("checkpoint_name")+" - "+checkPointList.getString("area_name"));
                        listHash.put(listDataHeader.get(i),taskNames);

                        listView.setAdapter(listAdapter);

                    } catch (JSONException e) {
                        System.out.println("responsex = [" + e.getMessage() + "]");
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("errorx = [" + error.getMessage() + "]");
                //Toast.makeText(getApplicationContext(),"hata -> " +error.getMessage(),Toast.LENGTH_LONG).show();
            }

        }

        );

        requestQueue.add(reqget);

        // Toast.makeText(this,getKeyByValue(taskId,String.valueOf(task.getSelectedItem())),Toast.LENGTH_LONG).show();
        /*String task_id = getKeyByValue(taskId,(String)(task.getSelectedItem()));
        String area_id = getKeyByValue(areaId,(String)(area.getSelectedItem()));*/
/*
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url ="http://evirgenmert.com/personnel/taskList/"+userId;
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
        }*/

    }
}

