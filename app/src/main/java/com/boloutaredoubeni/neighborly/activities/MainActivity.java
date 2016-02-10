package com.boloutaredoubeni.neighborly.activities;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.boloutaredoubeni.neighborly.R;
import com.boloutaredoubeni.neighborly.fragments.DashboardFragment;
import com.boloutaredoubeni.neighborly.fragments.DetailFragment;
import com.boloutaredoubeni.neighborly.fragments.MapFragment;
import com.boloutaredoubeni.neighborly.models.Location;

public class MainActivity extends AppCompatActivity
    implements LocationListener, MapFragment.OnOverlayItemClickedListener {

  private static final String TAG = MainActivity.class.getCanonicalName();
  private static final int MILE = 1609;
  private static final int TWENTY_MINS = 0x124F80;
  public static final String USER_LOCATION = "u53r10c4t10n";

  private LocationManager mLocationManager;
  private Location mUserLocation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setupUserLocation();

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

  // TODO: DRY these out
  private void setupDashboardFragment() {
    if (findViewById(R.id.dashboard_frame) != null) {
      DashboardFragment dashboardFragment = new DashboardFragment();
      Bundle bundle = new Bundle();
      bundle.putSerializable(USER_LOCATION, mUserLocation);
      dashboardFragment.setArguments(bundle);
      getFragmentManager()
          .beginTransaction()
          .add(R.id.dashboard_frame, dashboardFragment)
          .commit();
    }
  }

  private void setupMapFragment() {
    if (findViewById(R.id.map_frame) != null) {
      MapFragment mapFragment = new MapFragment();
      Bundle bundle = new Bundle();
      bundle.putSerializable(USER_LOCATION, mUserLocation);
      mapFragment.setArguments(bundle);
      getFragmentManager()
          .beginTransaction()
          .add(R.id.map_frame, mapFragment)
          .commit();
    }
  }

  private void setupUserLocation() {
    mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
    // TODO: handle permissions
    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                            TWENTY_MINS, MILE, this);
    mUserLocation = new Location.Builder()
                        .name("General Assembly")
                        .coordinates(40.7398848, -73.9922705)
                        .build();
  }

  @Override
  public void onLocationChanged(android.location.Location location) {
    double latitude = location.getLatitude();
    double longitude = location.getLongitude();
    mUserLocation.setCoordinates(latitude, longitude);

    // TODO: Make sure the map and the details are updated
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {}

  @Override
  public void onProviderEnabled(String provider) {}

  @Override
  public void onProviderDisabled(String provider) {}

  @Override
  public void update(Location location) {
    DetailFragment fragment = new DetailFragment();
    fragment.updateLocation(location);
    // TODO: remove the detail fragment
    getFragmentManager()
        .beginTransaction()
        .replace(R.id.dashboard_frame, fragment)
        .commit();
  }
}
