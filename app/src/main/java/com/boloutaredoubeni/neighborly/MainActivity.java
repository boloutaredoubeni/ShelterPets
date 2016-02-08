package com.boloutaredoubeni.neighborly;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity {

  private final int DEFAULT_ZOOM_LEVEL = 15;

  private MapView mMap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    if (savedInstanceState == null) {
      setupDashboardFragment();
    }
    setupMap();



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

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Setups up the map for the display. Centers the map at the last known position and displays a marker at the last known GPS position
   */
  private void setupMap() {
    mMap = (MapView)findViewById(R.id.map);
    mMap.setTileSource(TileSourceFactory.MAPNIK);
//     FIXME: remake the zoom buttons
    mMap.setBuiltInZoomControls(true);
    mMap.setMultiTouchControls(true);


    IMapController controller = mMap.getController();
    controller.setZoom(DEFAULT_ZOOM_LEVEL);
    GeoPoint point = new GeoPoint(40.7398848, -73.9922705);
    controller.setCenter(point);

    // TODO: display a marker
  }

  private void setupDashboardFragment() {
    if (findViewById(R.id.dashboard_frame) != null) {
      DashboardFragment dashboardFragment = new DashboardFragment();
      dashboardFragment.setArguments(getIntent().getExtras());
      getFragmentManager().beginTransaction().add(R.id.dashboard_frame, dashboardFragment, "_dashboard").commit();
    }
  }
}
