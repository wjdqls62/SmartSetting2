package com.jb.smartsetting.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jb.smartsetting.Common_Utility.ObjectReaderWriter;
import com.jb.smartsetting.Common_Utility.PermissionManager;
import com.jb.smartsetting.GPS_Utility.ProximityLocationService;
import com.jb.smartsetting.GPS_Utility.SavedCustomLocation;
import com.jb.smartsetting.R;

import java.io.File;
import java.util.ArrayList;

public class Main_LocationList_Activity extends AppCompatActivity implements View.OnClickListener {
    private final int RECYCLER_VIEW_NORMAL_MODE = 1;
    private final int RECYCLER_VIEW_DELETE_MODE = 2;

    private final String TAG = getClass().getName();
    private SharedPreferences pref;
    private boolean isDebug = false;

    private RelativeLayout delete_layout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout ItemLayout;
    private Intent intent;

    private LocationItemAdapter locationItemAdapter;
    private Toolbar toolbar;
    private FloatingActionButton fab_newLocation;
    private DividerItemDecoration dividerItemDecoration;

    private PermissionManager permissionManager;

    private ArrayList<SavedCustomLocation> items;
    private ArrayList<SavedCustomLocation> arrLocationList;
    private ObjectReaderWriter objectReaderWriter;
    private Bundle bundle;

    private void getPreference() {
        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        isDebug = pref.getBoolean("setting_dev_mode", false);
    }

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

        getPreference();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        permissionManager = new PermissionManager(this, getApplicationContext());
        permissionManager.isPermissionCheck();
    }

    private void init_View() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.location_listview);
        delete_layout = (RelativeLayout) findViewById(R.id.main_delete_layout);
        fab_newLocation = (FloatingActionButton) findViewById(R.id.fab_add_location);
        setSupportActionBar(toolbar);

    }

    private void init_Listener() {
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
            Intent intent = new Intent(Main_LocationList_Activity.this, Sub_Setting_Fragment.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_delete){
            if(locationItemAdapter.getRECYCLER_VIEW_MODE() != RECYCLER_VIEW_DELETE_MODE){
                locationItemAdapter.setRECYCLER_VIEW_MODE(RECYCLER_VIEW_DELETE_MODE);
                locationItemAdapter.notifyDataSetChanged();
                delete_layout.setVisibility(View.VISIBLE);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void action_Add_Location() {
        Intent intent = new Intent(Main_LocationList_Activity.this, Sub_MapView_Activity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(locationItemAdapter.getRECYCLER_VIEW_MODE() == RECYCLER_VIEW_DELETE_MODE){
            locationItemAdapter.setRECYCLER_VIEW_MODE(RECYCLER_VIEW_NORMAL_MODE);
            locationItemAdapter.notifyDataSetChanged();
            delete_layout.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // FloatingActionButton
            case R.id.fab_add_location:
                intent = new Intent(Main_LocationList_Activity.this, Sub_MapView_Activity.class);
                startActivity(intent);
                break;
        }
    }

    private class LocationItemAdapter extends Adapter<LocationListViewHolder> {
        private int RECYCLER_MODE = -1;

        public LocationItemAdapter() {
            items = new ArrayList<SavedCustomLocation>();
        }

        public void testAdd(String locationName) {
            SavedCustomLocation test = new SavedCustomLocation();
            test.locationName = locationName;
            items.add(test);
        }

        public void setRECYCLER_VIEW_MODE(int i) {
            RECYCLER_MODE = i;
        }

        public int getRECYCLER_VIEW_MODE(){
            return RECYCLER_MODE;
        }

        @Override
        public LocationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_location_list_item, parent, false);
            return new LocationListViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final LocationListViewHolder holder, final int position) {
            // Item 삭제 및 일반 선택모드의 구분
            if (RECYCLER_MODE == RECYCLER_VIEW_NORMAL_MODE) {
                holder.checkBox.setVisibility(View.GONE);
                holder.toggleButton.setVisibility(View.VISIBLE);
                fab_newLocation.setVisibility(View.VISIBLE);

            } else if (RECYCLER_MODE == RECYCLER_VIEW_DELETE_MODE) {
                fab_newLocation.setVisibility(View.GONE);
                holder.toggleButton.setVisibility(View.GONE);
                holder.checkBox.setVisibility(View.VISIBLE);
            }

            // StubObject의 isEnabled값을 가져와 ToggleSwitch의 상태를 변경
            if (arrLocationList.get(position).isEnabled) {
                holder.toggleButton.setChecked(true);
            } else {
                holder.toggleButton.setChecked(false);
            }

            holder.indentification.setText(arrLocationList.get(position).indentificationNumber + "");
            holder.locationName.setText(arrLocationList.get(position).getLocationName());
            holder.toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 활성화 -> 비활성화
                    if (arrLocationList.get(position).isEnabled) {
                        arrLocationList.get(position).setEnabled(false);
                        objectReaderWriter.saveObject(arrLocationList.get(position));
                        stopService(new Intent(getApplicationContext(), ProximityLocationService.class));

                    } else {
                        arrLocationList.get(position).setEnabled(true);
                        objectReaderWriter.saveObject(arrLocationList.get(position));
                        startService(new Intent(getApplicationContext(), ProximityLocationService.class));
                    }
                }
            });
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(RECYCLER_MODE == RECYCLER_VIEW_NORMAL_MODE){
                        bundle = new Bundle();
                        bundle.putString("DISPLAY_MODE", "MODIFY");
                        bundle.putDouble("indentificationNumber", arrLocationList.get(position).indentificationNumber);
                        Intent intent = new Intent(Main_LocationList_Activity.this, Sub_ItemSetting_Activity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else if(RECYCLER_MODE == RECYCLER_VIEW_DELETE_MODE){
                           // 삭제모드 아이템 선택시 구현부
                        if(!holder.checkBox.isChecked()){

                            holder.checkBox.setChecked(true);
                        }else{

                            holder.checkBox.setChecked(false);
                        }
                    }
                }
            });

            //holder.locationImage.setImageBitmap(BitmapFactory.decodeFile(arrLocationList.get(position).objFilePath+"crop_"+arrLocationList.get(position).imgFileName));
            if(holder.locationImage != null){
                holder.locationImage.setScaleType(ImageView.ScaleType.CENTER);
                Glide.with(getApplicationContext())
                        .load(new File(arrLocationList.get(position).objFilePath + "crop_" + arrLocationList.get(position).imgFileName))
                        .into(holder.locationImage);
            }else{
                Toast.makeText(getApplicationContext(), "Thumbnail is null..", Toast.LENGTH_SHORT).show();
            }



        }

        @Override
        public int getItemCount() {
            return arrLocationList.size();
        }

    }

    public class LocationListViewHolder extends RecyclerView.ViewHolder {

        public TextView locationName;
        public ImageView locationImage;
        public Switch toggleButton;
        public CheckBox checkBox;
        public LinearLayout cardView;
        public TextView indentification;

        public LocationListViewHolder(View itemView) {
            super(itemView);
            locationImage = (ImageView) itemView.findViewById(R.id.location_image);
            locationName = (TextView) itemView.findViewById(R.id.location_name);
            toggleButton = (Switch) itemView.findViewById(R.id.toggle);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            cardView = (LinearLayout) itemView.findViewById(R.id.card_layout);
            indentification = (TextView) itemView.findViewById(R.id.indentification);
        }
    }
}
