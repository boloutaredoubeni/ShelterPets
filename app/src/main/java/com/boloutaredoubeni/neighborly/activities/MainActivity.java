package com.boloutaredoubeni.neighborly.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.boloutaredoubeni.neighborly.R;
import com.boloutaredoubeni.neighborly.fragments.DashboardFragment;
import com.boloutaredoubeni.neighborly.fragments.MapFragment;
import com.boloutaredoubeni.neighborly.models.Location;
import com.boloutaredoubeni.neighborly.osmapi.MapDataXMLParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getCanonicalName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    Location userLocation = new Location.Builder()
                                .name("General Assembly")
                                .coordinates(40.7398848, -73.9922705)
                                .build();
    try {
      List<Location> locations =
          MapDataXMLParser.parse(getAssets().open("map.xml"));
      for (Location location : locations) {
        Log.d(TAG, location.toString());
      }
    } catch (IOException | XmlPullParserException ex) {
      Log.e(TAG, ex.getClass().getCanonicalName() + " " + ex.getMessage());
    }

    FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action",
                      Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show();
      }
    });

    if (savedInstanceState == null) {
      setupDashboardFragment();
    }
    setupMapFragment();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void setupDashboardFragment() {
    if (findViewById(R.id.dashboard_frame) != null) {
      DashboardFragment dashboardFragment = new DashboardFragment();
      dashboardFragment.setArguments(getIntent().getExtras());
      getFragmentManager()
          .beginTransaction()
          .add(R.id.dashboard_frame, dashboardFragment, "_map")
          .commit();
    }
  }

  private void setupMapFragment() {
    if (findViewById(R.id.map_frame) != null) {
      MapFragment mapFragment = new MapFragment();
      mapFragment.setArguments(getIntent().getExtras());
      getFragmentManager()
          .beginTransaction()
          .add(R.id.map_frame, mapFragment, "_dashboard")
          .commit();
    }
  }
}
