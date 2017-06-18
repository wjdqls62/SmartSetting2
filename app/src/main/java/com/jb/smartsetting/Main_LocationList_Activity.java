package com.jb.smartsetting;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.jb.smartsetting.Common_Utility.LocationListViewHolder;

public class Main_LocationList_Activity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getName();
    private boolean isDebug = true;

    private RecyclerView recyclerView;

    private LocationItemAdapter locationItemAdapter;
    private Toolbar toolbar;
    private FloatingActionButton fab_newLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__location_list_);
        setSupportActionBar(toolbar);

        init_View();
        init_Listener();

        locationItemAdapter = new LocationItemAdapter();
        recyclerView.setAdapter(locationItemAdapter);

    }

    private void init_View(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.location_listview);
        fab_newLocation = (FloatingActionButton) findViewById(R.id.fab_add_location);
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            // FloatingActionButton
            case R.id.fab_add_location :
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;
        }
    }


    public class LocationItemAdapter extends Adapter<LocationListViewHolder> {

        @Override
        public LocationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(LocationListViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
