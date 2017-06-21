package com.jb.smartsetting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jb.smartsetting.Common_Utility.LocationListViewHolder;
import com.jb.smartsetting.Common_Utility.ObjectReaderWriter;
import com.jb.smartsetting.GPS_Utility.ProximityLocationService;
import com.jb.smartsetting.GPS_Utility.Stub_Location_Object;

import java.util.ArrayList;

public class Main_LocationList_Activity extends AppCompatActivity implements View.OnClickListener {
    private final int RECYCLER_VIEW_NORMAL_MODE = 1;
    private final int RECYCLER_VIEW_DELETE_MODE = 2;

    private final String TAG = getClass().getName();
    private boolean isDebug = true;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private LocationItemAdapter locationItemAdapter;
    private Toolbar toolbar;
    private FloatingActionButton fab_newLocation;

    private ArrayList<Stub_Location_Object> arrLocationList;
    private ObjectReaderWriter objectReaderWriter;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__location_list_);


        init_View();
        init_Listener();

        objectReaderWriter = new ObjectReaderWriter(getApplicationContext());
        arrLocationList = objectReaderWriter.readObject();
        Toast.makeText(getApplicationContext(),""+arrLocationList.size(), Toast.LENGTH_SHORT).show();



        locationItemAdapter = new LocationItemAdapter();
        locationItemAdapter.setRECYCLER_VIEW_MODE(RECYCLER_VIEW_NORMAL_MODE);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(locationItemAdapter);


    }

    private void init_View(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.location_listview);
        fab_newLocation = (FloatingActionButton) findViewById(R.id.fab_add_location);
        setSupportActionBar(toolbar);
    }
    private void init_Listener(){
        fab_newLocation.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main__location_list_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void action_Add_Location(){
        Intent intent = new Intent(Main_LocationList_Activity.this, Sub_MapView_Activity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            // FloatingActionButton
            case R.id.fab_add_location :
                Intent intent = new Intent(Main_LocationList_Activity.this, Sub_MapView_Activity.class);
                startActivity(intent);
                break;
        }
    }

    public class LocationItemAdapter extends Adapter<LocationListViewHolder> implements View.OnClickListener {
        private int RECYCLER_MODE = -1;
        private ArrayList<Stub_Location_Object> items;

        public LocationItemAdapter(){
            items = new ArrayList<Stub_Location_Object>();
        }

        public void testAdd(String locationName){
            Stub_Location_Object test = new Stub_Location_Object();
            test.locationName = locationName;
            items.add(test);
        }

        public void setRECYCLER_VIEW_MODE(int i){
            RECYCLER_MODE = i;
        }

        @Override
        public LocationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_location_list_item, parent, false);
            return new LocationListViewHolder(v);
        }

        @Override
        public void onBindViewHolder(LocationListViewHolder holder, final int position) {
            // Item 삭제 및 일반 선택모드의 구분
            if(RECYCLER_MODE == RECYCLER_VIEW_NORMAL_MODE){
                holder.checkBox.setVisibility(View.GONE);
            }else if(RECYCLER_MODE == RECYCLER_VIEW_DELETE_MODE){
                holder.toggleButton.setVisibility(View.GONE);
                holder.checkBox.setVisibility(View.VISIBLE);
            }

            holder.locationName.setText(arrLocationList.get(position).getLocationName());
            holder.toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 활성화 -> 비활성화
                    if(arrLocationList.get(position).isEnabled){
                        arrLocationList.get(position).setEnabled(false);
                        objectReaderWriter.saveObject(arrLocationList.get(position));
                        Toast.makeText(Main_LocationList_Activity.this, "isEnabled : "+arrLocationList.get(position).isEnabled, Toast.LENGTH_SHORT).show();
                        stopService(new Intent(Main_LocationList_Activity.this, ProximityLocationService.class));
                    }else{
                        arrLocationList.get(position).setEnabled(true);
                        objectReaderWriter.saveObject(arrLocationList.get(position));
                        Toast.makeText(Main_LocationList_Activity.this, "isEnabled : "+arrLocationList.get(position).isEnabled, Toast.LENGTH_SHORT).show();
                        startService(new Intent(Main_LocationList_Activity.this, ProximityLocationService.class));
                    }

                }
            });
            holder.itemLayout.setOnClickListener(this);

        }

        @Override
        public int getItemCount() {
            return arrLocationList.size();
        }

        // RecyclerView내에서의 클릭리스너
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_layout :
                    Snackbar.make(v,"Selected ItemLayout", Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
