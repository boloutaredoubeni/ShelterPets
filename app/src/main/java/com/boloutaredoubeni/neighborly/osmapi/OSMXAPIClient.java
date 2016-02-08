package com.boloutaredoubeni.neighborly.osmapi;

import com.boloutaredoubeni.neighborly.models.Location;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright 2016 Boloutare Doubeni
 *
 * Retrieves places data from an external source
 */
public class OSMXAPIClient {

  static OSMXAPIClient INSTANCE;
  static String[] API_URLS = {
      "http://overpass.osm.rambler.ru/cgi/xapi_meta?",
      "http://www.overpass-api.de/api/xapi_meta?",
      "http://api.openstreetmap.fr/xapi?",
  };
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
    Request request = new Request.Builder().url(API_URLS[0]).build();
    Response response = getInstance().mClient.newCall(request).execute();
    return response.body().string();
  }

  //  private static String buildBox(final Location location) {
  //    final String URL = "bbox";
  //    final Location.Coordinates coordinates = location.getCoordinates();
  //    final double left = coordinates.getLongitude() + RADIUS;
  //    final double bottom = coordinates.getLatitude() - RADIUS;
  //    final double right = coordinates.getLongitude() - RADIUS;
  //    final double top = coordinates.getLatitude() + RADIUS;
  //  }
}
