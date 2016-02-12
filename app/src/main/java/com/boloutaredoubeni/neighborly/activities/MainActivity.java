package com.boloutaredoubeni.neighborly.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.boloutaredoubeni.neighborly.R;
import com.boloutaredoubeni.neighborly.db.DatabaseTable;
import com.boloutaredoubeni.neighborly.fragments.DashboardFragment;
import com.boloutaredoubeni.neighborly.fragments.DetailFragment;
import com.boloutaredoubeni.neighborly.fragments.MapFragment;
import com.boloutaredoubeni.neighborly.models.Location;
import com.boloutaredoubeni.neighborly.osmapi.OSMXAPIClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.util.GeoPoint;

public class MainActivity extends AppCompatActivity
    implements LocationListener, MapFragment.OnOverlayItemClickedListener,
               GoogleApiClient.ConnectionCallbacks,
               GoogleApiClient.OnConnectionFailedListener {

  private static final String TAG = MainActivity.class.getCanonicalName();
  private static final int REQUEST_LOCATION_PERMISSIONS = 1;
  private static final int MILE = 1609;
  public static final String USER_LOCATION = "u53r10c4t10n";

  private LocationManager mLocationManager;
  private Location mUserLocation;
  private GoogleApiClient mGoogleApiClient;
  private DatabaseTable mDatabaseTable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setupGoogleApi();
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
    setupDatabase();
    getApiData();
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
    // TODO: Implement search for recents, the db and from the web
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onStart() {
    mGoogleApiClient.connect();
    super.onStart();
  }

  @Override
  protected void onStop() {
    mGoogleApiClient.disconnect();
    super.onStop();
  }

  // TODO: DRY these out
  private void setupDashboardFragment() {
    if (findViewById(R.id.dashboard_frame) != null) {
      DashboardFragment dashboardFragment = new DashboardFragment();
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
    requestLocationPermission();
    mLocationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER, AlarmManager.INTERVAL_FIFTEEN_MINUTES,
        MILE, this);

    android.location.Location lastLocation =
        mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    mUserLocation =
        new Location.Builder()
            .name("You are here")
            .coordinates(lastLocation != null ? lastLocation.getLatitude()
                                              : 40.7398848,
                         lastLocation != null ? lastLocation.getLongitude()
                                              : -73.9900818)
            .build();
  }

  private void setupGoogleApi() {
    if (mGoogleApiClient == null) {
      mGoogleApiClient = new GoogleApiClient.Builder(this)
                             .addConnectionCallbacks(this)
                             .addOnConnectionFailedListener(this)
                             .addApi(LocationServices.API)
                             .build();
    }
  }

  @Override
  public void onLocationChanged(android.location.Location location) {
    double latitude = location.getLatitude();
    double longitude = location.getLongitude();

    if (mUserLocation == null) {
      mUserLocation = new Location.Builder()
                          .name("You are here")
                          .coordinates(latitude, longitude)
                          .build();
    }
    mUserLocation.setCoordinates(latitude, longitude);

    DetailFragment detailFragment =
        ((DetailFragment)getFragmentManager().findFragmentByTag(
            DetailFragment.TAG));
    MapFragment mapFragment =
        ((MapFragment)getFragmentManager().findFragmentByTag(MapFragment.TAG));
    if (detailFragment != null) {
      detailFragment.updateLocation(mUserLocation);
      mapFragment.getMapController().setCenter(
          new GeoPoint(latitude, longitude));
    }
    getApiData();
    // TODO: send a signal to redraw the entire view
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {}

  @Override
  public void onProviderEnabled(String provider) {}

  @Override
  public void onProviderDisabled(String provider) {}

  @Override
  public void update(Location location) {
    DetailFragment detailFragment = new DetailFragment();
    detailFragment.updateLocation(location);

    //    PhotoViewFragment photoViewFragment = new PhotoViewFragment();
    getFragmentManager()
        .beginTransaction()
        .replace(R.id.dashboard_frame, detailFragment, DetailFragment.TAG)
        .commit();

    //    getFragmentManager()
    //        .beginTransaction()
    //        .replace(R.id.map_frame, photoViewFragment)
    //        .commit();
  }

  @Override
  public void onConnected(Bundle bundle) {
    requestLocationPermission();
    android.location.Location lastLocation =
        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    if (lastLocation != null) {
      double latitude = lastLocation.getLatitude();
      double longitude = lastLocation.getLongitude();
      if (mUserLocation == null) {
        mUserLocation = new Location.Builder()
                            .name("You are here")
                            .coordinates(latitude, longitude)
                            .build();
      }
      mUserLocation.setCoordinates(latitude, longitude);
    }
  }

  @Override
  public void onConnectionSuspended(int i) {}

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {}

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    switch (requestCode) {
    case REQUEST_LOCATION_PERMISSIONS: {
      if (grantResults.length > 0 &&
          grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // TODO: use that permission
      }
    }
    }
  }

  private void requestLocationPermission() {
    Log.d(TAG, "requesting location permisions");
    if (ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
      // FIXME: handle permission requesting in Android 6.0
      if (ActivityCompat.shouldShowRequestPermissionRationale(
              this, Manifest.permission_group.LOCATION)) {

      } else {
        ActivityCompat.requestPermissions(
            this, new String[] {Manifest.permission_group.LOCATION},
            REQUEST_LOCATION_PERMISSIONS);
      }
    }
  }

  private void getApiData() {
    Log.d(TAG, "Starting Http request");
    try {
      OSMXAPIClient.setLocation(mUserLocation);
      OSMXAPIClient.getInstance().run();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setupDatabase() {
    Log.d(TAG, "Initializing the database");
    mDatabaseTable = DatabaseTable.getInstance(this);
  }
}
