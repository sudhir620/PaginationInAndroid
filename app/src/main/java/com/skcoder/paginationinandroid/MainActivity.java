package com.skcoder.paginationinandroid;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;
    static Apis apis;
    public static Integer page = 1;

    RecyclerView dataRecyclerView;
    RecyclerView page_rec_view;
    static DataAdapter adapter;
    static PageAdapter pageAdapter;
    static ArrayList<DataModel1> list = new ArrayList<>();
    static ArrayList<PageModel> pageList = new ArrayList<>();
    static Integer totalPages;

//    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder().baseUrl("https://reqres.in/").addConverterFactory(GsonConverterFactory.create()).build();
        apis = retrofit.create(Apis.class);
//        tv = findViewById(R.id.tv);


        dataRecyclerView = findViewById(R.id.data_rec_view);
        page_rec_view = findViewById(R.id.page_rec_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        dataRecyclerView.setLayoutManager(layoutManager);
        page_rec_view.setLayoutManager(layoutManager1);
        dataRecyclerView.setHasFixedSize(true);
        page_rec_view.setHasFixedSize(true);
        adapter = new DataAdapter(list, this);
        pageAdapter = new PageAdapter(pageList, this);
        dataRecyclerView.setAdapter(adapter);
        page_rec_view.setAdapter(pageAdapter);
        adapter.notifyDataSetChanged();
        pageAdapter.notifyDataSetChanged();


        loadData(page);
    }

    public static void loadData(Integer page) {
        Call<ProfileModel> getData = apis.getData(page);
        getData.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                if (response.isSuccessful()){
                    Log.d("testing2", response.body().getData().toString());

                    ArrayList<ProfileModel.DataModel> data = response.body().getData();
                    totalPages = response.body().getTotal_pages();
                    for (int i=0; i<totalPages;i++){
                        pageList.add(new PageModel(i+1));
                    }
                    pageAdapter.notifyDataSetChanged();

                    for (ProfileModel.DataModel item : data){
                        list.add(new DataModel1(item.getEmail(), item.getFirst_name(), item.getLast_name(), item.getAvatar()));
                    }
                    adapter.notifyDataSetChanged();

//                    tv.setText(response.body().getTotal_pages().toString());
                }else{
                    Log.d("testing1", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {
                Log.d("testing3", t.toString());
            }
        });
    }
}