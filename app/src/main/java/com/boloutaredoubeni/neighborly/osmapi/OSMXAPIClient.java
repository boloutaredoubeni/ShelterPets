package com.boloutaredoubeni.neighborly.osmapi;

import android.util.Log;

import com.boloutaredoubeni.neighborly.models.Coordinates;
import com.boloutaredoubeni.neighborly.models.Location;
import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
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
  public final static String VERSION = "0.6";
  final static String API_URL =
      "http://api06.dev.openstreetmap.org/api/" + VERSION + "/map?";
  static private boolean mIsRunning;

  private OkHttpClient mClient;
  private static Location mOrigin;
  private String mResponse;

  private OSMXAPIClient() {}

  /**
   *
   * @return the Api Client
   */
  public static OSMXAPIClient getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new OSMXAPIClient();
    }
    if (INSTANCE.mClient == null) {
      INSTANCE.mClient = new OkHttpClient();
    }
    return INSTANCE;
  }

  public static String buildBoxParams(final Location location) {
    List<LatLng> box = location.getCoordinates().getBox();
    LatLng mins = Coordinates.getMins(box);
    LatLng maxes = Coordinates.getMaxes(box);
    Double left = mins.longitude;
    Double bottom = mins.latitude;
    Double right = maxes.longitude;
    Double top = maxes.latitude;

    return String.format("bbox=%f,%f,%f,%f", left, bottom, right, top);
  }

  public static void setLocation(Location origin) {
    if (origin == null) {
      throw new IllegalArgumentException("origin can not be null");
    }
    mOrigin = origin;
  }

  public static List<Location> getPlaces() {
    if (mIsRunning) {
      return null;
    }
    if (INSTANCE.mResponse == null) {
      return null;
    }
    List<Location> locations = new ArrayList<>();
    try {
      mIsRunning = true;
      InputStream in = new ByteArrayInputStream(INSTANCE.mResponse.getBytes());
      locations = OSMXMLParser.parse(in);
    } catch (XmlPullParserException | IOException ex) {
      mIsRunning = false;
      ex.printStackTrace();
    }
    return locations;
  }

  public void run() throws Exception {
    if (mOrigin == null) {
      throw new Exception(
          "the origin is null, did you call OSMAPIClient.setLocation()");
    }
    String bboxParams = buildBoxParams(mOrigin);
    URL url = new URL(API_URL + bboxParams);
    Log.d(TAG, "The url is " + url.toString());
    Request request = new Request.Builder().url(url).build();

    mClient.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        // NOTE: do this for now
        e.printStackTrace();
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
          throw new IOException("Unexpected code " + response);
        }

        mResponse = response.body().string();
        //        Log.d(TAG, mResponse);
      }
    });
  }
}
