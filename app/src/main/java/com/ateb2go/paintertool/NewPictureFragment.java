package com.ateb2go.paintertool;

import android.annotation.SuppressLint;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewPictureFragment extends Fragment {

    RecyclerView recyclerView;
    FragAdapter adapter;
    ArrayList<PictureVO> pictures=new ArrayList<>();
    Context context;
    TextView tv;

    SwipeRefreshLayout srl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_newpicture, container, false);
        recyclerView=view.findViewById(R.id.recycler);
        adapter=new FragAdapter(pictures, context);
        recyclerView.setAdapter(adapter);
        tv=view.findViewById(R.id.tv_frag);

        srl=view.findViewById(R.id.srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                srl.setRefreshing(false);
            }
        });

        String serverUrl="http://ahpla.dothome.co.kr/Paintndots/newpicture.php";

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST, serverUrl,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pictures.clear();
                adapter.notifyDataSetChanged();

                try {

                    for(int i=0;i<response.length();i++){
                        JSONObject jsonObject=response.getJSONObject(i);
                        String nickname=jsonObject.getString("nickname");
                        String pass=jsonObject.getString("pass");
                        String title=jsonObject.getString("title");
                        String comment=jsonObject.getString("comment");
                        String imgPath="http://ahpla.dothome.co.kr/Paintndots/"+jsonObject.getString("img");
                        String date=jsonObject.getString("date");

                        pictures.add(0, new PictureVO(nickname, pass, title, comment, imgPath, date));
                        adapter.notifyItemInserted(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString()+" 에러", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);

        tv.setVisibility(View.GONE);

        return view;
    }
}
