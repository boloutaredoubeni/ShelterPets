package com.boloutaredoubeni.neighborly.osmapi;

import com.boloutaredoubeni.neighborly.models.Location;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright 2016 Boloutare Doubeni
 *
 * Retrieves places data from an external source
 */
public class OSMXAPIClient {

  private final static String TAG = OSMXAPIClient.class.getCanonicalName();

  static OSMXAPIClient INSTANCE;
  static String API_URL = "http://api06.dev.openstreetmap.org/";
  public final static String VERSION = "0.6";
  private final static double RADIUS = 2.0;

  private OkHttpClient mClient;

  private OSMXAPIClient() {}

  /**
   *
   * @return the Api Client
   */
  static OSMXAPIClient getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new OSMXAPIClient();
    }
    if (INSTANCE.mClient == null) {
      INSTANCE.mClient = new OkHttpClient();
    }
    return INSTANCE;
  }

  // TODO: handle exceptions; caller receives either data or null
  public static String getPlacesFromLocation(Location location)
      throws IOException {
    final String url = API_URL; //+ "apibuildBoxParams(location);
    Request request = new Request.Builder().url(url).build();
    Response response = getInstance().mClient.newCall(request).execute();
    return response.body().string();
  }

  public static String buildBoxParams(final Location location) {
    final List<LatLng> box = location.getCoordinates().getBox();
    // FIXME: use max and mins
    final Double left = box.get(0).longitude;
    final Double bottom = box.get(1).latitude;
    final Double right = box.get(2).longitude;
    final Double top = box.get(3).latitude;

    return left + "," + bottom + "," + right + "," + top + "";
  }
}
