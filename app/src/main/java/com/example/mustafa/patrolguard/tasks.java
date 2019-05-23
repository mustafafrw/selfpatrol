package com.example.mustafa.patrolguard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.example.mustafa.patrolguard.models.Checkpoint;
import com.example.mustafa.patrolguard.models.Task;
import com.example.mustafa.patrolguard.adapters.ExpandListViewAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class tasks extends AppCompatActivity {
    SwipeRefreshLayout pullToRefreshTasks;
    private ExpandableListView listView;
    private ExpandListViewAdapter listAdapter;

    private List<Checkpoint> listDataHeader;

    public static String userId = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tasks);

        pullToRefreshTasks = findViewById(R.id.pullToRefreshTasks);
        listDataHeader = new ArrayList<>();
        getTasks();

        listAdapter = new ExpandListViewAdapter(getApplicationContext(), listDataHeader);

        listView = findViewById(R.id.lvExp);
        listView.setClickable(true);
        listView.setAdapter(listAdapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                System.out.println("tiklandi");

                Checkpoint checkpoint = listDataHeader.get(groupPosition);
                Task task = checkpoint.getTasks().get(childPosition);

                Main2Activity.currentPoint = checkpoint;
                Main2Activity.currentTask = task;

                if (task.getStatus() != 1) {
                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                    startActivity(intent);
                }

                return false;
            }
        });

        pullToRefreshTasks.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listDataHeader.clear();
                listAdapter.notifyDataSetChanged();
                System.out.println("refreshx");
                System.out.println("task count1=" + listDataHeader.size());
                getTasks();
                System.out.println("task count2=" + listDataHeader.size());
                //pullToRefreshTasks.setRefreshing(false);
            }
        });
    }

    private void getTasks()
    {
        pullToRefreshTasks.setRefreshing(true);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        JsonArrayRequest reqget = new JsonArrayRequest(Request.Method.GET,
                "http://evirgenmert.com/personnel/taskList/1",
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Gson gson = new Gson();
                Checkpoint[] checkpoints = gson.fromJson(response.toString(), Checkpoint[].class);

                for (Checkpoint c: checkpoints) {
                    listDataHeader.add(c);
                    listAdapter.notifyDataSetChanged();

                }

                for (Checkpoint l: listDataHeader){
                    System.out.println("updatex: " + l.getCheckpoint_name());
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
        pullToRefreshTasks.setRefreshing(false);



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

