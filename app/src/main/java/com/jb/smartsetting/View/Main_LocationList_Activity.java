package com.jb.smartsetting.View;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jb.smartsetting.Common_Utility.IRequestPermissionCallback;
import com.jb.smartsetting.Common_Utility.LocationListViewHolder;
import com.jb.smartsetting.Common_Utility.ObjectReaderWriter;
import com.jb.smartsetting.Common_Utility.PermissionManager;
import com.jb.smartsetting.GPS_Utility.ProximityLocationService;
import com.jb.smartsetting.GPS_Utility.Stub_Location_Object;
import com.jb.smartsetting.R;

import java.util.ArrayList;

public class Main_LocationList_Activity extends AppCompatActivity implements View.OnClickListener, IRequestPermissionCallback{
    private final int RECYCLER_VIEW_NORMAL_MODE = 1;
    private final int RECYCLER_VIEW_DELETE_MODE = 2;

    private final String TAG = getClass().getName();
    private boolean isDebug = true;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout ItemLayout;

    private LocationItemAdapter locationItemAdapter;
    private Toolbar toolbar;
    private FloatingActionButton fab_newLocation;
    private DividerItemDecoration dividerItemDecoration;

    private PermissionManager permissionManager;

    private ArrayList<Stub_Location_Object> items;
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

        locationItemAdapter = new LocationItemAdapter();
        locationItemAdapter.setRECYCLER_VIEW_MODE(RECYCLER_VIEW_NORMAL_MODE);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(locationItemAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        permissionManager = new PermissionManager(this, getApplicationContext());
        permissionManager.isPermissionCheck();
    }

    private void init_View(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.location_listview);
        fab_newLocation = (FloatingActionButton) findViewById(R.id.fab_add_location);
        ItemLayout = new LinearLayout(getApplicationContext());
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

    @Override
    public void onRequestPermission() {

    }

    private class LocationItemAdapter extends Adapter<LocationListViewHolder> {
        private int RECYCLER_MODE = -1;

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

            // StubObject의 isEnabled값을 가져와 ToggleSwitch의 상태를 변경
            if(arrLocationList.get(position).isEnabled){
                holder.toggleButton.setChecked(true);
            }else{
                holder.toggleButton.setChecked(false);
            }



            holder.indentification.setText(arrLocationList.get(position).indentificationNumber+"");
            holder.locationName.setText(arrLocationList.get(position).getLocationName());
            holder.toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 활성화 -> 비활성화
                    if(arrLocationList.get(position).isEnabled){
                        arrLocationList.get(position).setEnabled(false);
                        objectReaderWriter.saveObject(arrLocationList.get(position));
                        stopService(new Intent(getApplicationContext(), ProximityLocationService.class));

                    }else{
                        arrLocationList.get(position).setEnabled(true);
                        objectReaderWriter.saveObject(arrLocationList.get(position));
                        startService(new Intent(getApplicationContext(), ProximityLocationService.class));

                    }
                }
            });
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle = new Bundle();
                    bundle.putString("DISPLAY_MODE", "MODIFY");
                    bundle.putDouble("indentificationNumber", arrLocationList.get(position).indentificationNumber);
                    Intent intent = new Intent(Main_LocationList_Activity.this, Sub_ItemSetting_Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrLocationList.size();
        }

    }
}
