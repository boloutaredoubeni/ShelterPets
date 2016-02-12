package com.boloutaredoubeni.neighborly.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
  protected List<Location> mPlaces;

  public List<Location> getPlaces() {
    return mPlaces;
  }

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
              this, Manifest.permission.ACCESS_FINE_LOCATION)) {

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
      new OSMXMLParserTask().execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setupDatabase() {
    Log.d(TAG, "Initializing the database");
    mDatabaseTable = DatabaseTable.getInstance(this);
  }

  class OSMXMLParserTask extends AsyncTask<Void, Integer, List<Location>> {

    private final String TAG = OSMXMLParserTask.class.getCanonicalName();
    private final String NAMESPACE = null;


    @Override
    protected List<Location> doInBackground(Void... noParams) {
      List<Location> locations = new ArrayList<>();
      try {
        OSMXAPIClient.getInstance().run();
        InputStream in =
            new ByteArrayInputStream(OSMXAPIClient.getResponse().getBytes());
        locations = parse(in);
        Log.d(TAG, "Running in the background");
      } catch (Exception ex) {
        Log.e(TAG, ex.getMessage());
      }
      return locations;
    }

    @Override
    protected void onPostExecute(List<Location> locations) {
      super.onPostExecute(locations);
      Log.d(TAG, "Running post execute stuff");
      mPlaces = locations;
    }

    private List<Location> parse(InputStream in)
        throws XmlPullParserException, IOException {
      try {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        return readNodes(parser);
      } finally {
        in.close();
      }
    }

    private List<Location> readNodes(XmlPullParser parser)
        throws IOException, XmlPullParserException {
      List<Location> locations = new ArrayList<>();
      parser.require(XmlPullParser.START_TAG, NAMESPACE, "osm");
      while (parser.next() != XmlPullParser.END_DOCUMENT) {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
          continue;
        }
        String name = parser.getName();
        switch (name) {
        case "node": {
          Location location = readTags(parser);
          if (location != null){
            locations.add(location);
          }
          break;
        }
        default:
          skip(parser);
        }
      }
      return locations;
    }

    private Location readTags(XmlPullParser parser)
        throws IOException, XmlPullParserException {

      Location.Builder builder = new Location.Builder();
      double latitude =
          Double.valueOf(parser.getAttributeValue(NAMESPACE, "lat"));
      double longitude =
          Double.valueOf(parser.getAttributeValue(NAMESPACE, "lon"));

      builder.coordinates(latitude, longitude);
      parser.require(XmlPullParser.START_TAG, NAMESPACE, "node");

      int depth = 1;
      while (depth != 0) {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
          depth++;
          continue;
        } else if (parser.getEventType() != XmlPullParser.START_TAG) {
          depth--;
          continue;
        }

        if (parser.getName().equals("tag")) {
          String key = parser.getAttributeValue(NAMESPACE, "k");
          String value = parser.getAttributeValue(NAMESPACE, "v");
          Log.d("IT WORKS", "Got " + key + " = " + value);

          switch (key) {
            case "name":
              builder.name(value);
              break;
            case "amenity":
            case "shop":
              builder.amenity(value);
              break;
            case "website":
              builder.url(value);
              break;
            case "opening_hours":
              builder.hours(value);
              break;
            default:
              break;
          }
        }
      }

      return builder.build();
    }

    private void skip(XmlPullParser parser)
        throws XmlPullParserException, IOException {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        throw new IllegalStateException();
      }
      int depth = 1;
      while (depth != 0) {
        switch (parser.next()) {
        case XmlPullParser.END_TAG:
          depth--;
          break;
        case XmlPullParser.START_TAG:
          depth++;
          break;
        }
      }
    }
  }
}
