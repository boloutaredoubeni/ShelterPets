package com.boloutaredoubeni.neighborly.osmapi;

import android.support.annotation.Nullable;

import com.boloutaredoubeni.neighborly.models.Location;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class MapDataXMLParser {

  private static final String NAMESPACE = null;
  private static final String START_TAG = "osm";
  private static final String NODE_TAG = "node";
  private static final String TAG_TAG = "tag";
  private static final String LAT_ATTR = "lat";
  private static final String LON_ATTR = "lon";
  private static final String K_ATTR = "k";
  private static final String V_ATTR = "v";

  private MapDataXMLParser() { throw new AssertionError(); }

  public static List<Location> parse(InputStream in)
      throws XmlPullParserException, IOException {
    try {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      XmlPullParser parser = factory.newPullParser();
      parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
      parser.setInput(in, null);
      parser.nextTag();
      return readOSM(parser);
    } finally {
      in.close();
    }
  }

  /**
   * Parses the data from the osm tag, and pass coordinate data to the builder
   * @param parser
   * @return
   * @throws XmlPullParserException
   * @throws IOException
   */
  private static List<Location> readOSM(XmlPullParser parser)
      throws XmlPullParserException, IOException {
    List<Location> locations = new LinkedList<>();

    parser.require(XmlPullParser.START_TAG, NAMESPACE, START_TAG);
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      String name = parser.getName();
      if (name.equals(NODE_TAG)) {
        // FIXME: handle null
        // Assuming the data is formatted correctly
        double latitude =
            Double.valueOf(parser.getAttributeValue(NAMESPACE, LAT_ATTR));
        double longitude =
            Double.valueOf(parser.getAttributeValue(NAMESPACE, LON_ATTR));
        // pass the latlon attributes to the next function
        final Location location = readNode(parser, latitude, longitude);
        if (location != null) {
          locations.add(location);
        }
      } else {
        // Skip the way tags for now
        skip(parser);
      }
    }
    return locations;
  }

  /**
   * Reads data from the node tag
   * @param parser
   * @return
   * @throws XmlPullParserException
   * @throws IOException
   */
  @Nullable
  private static Location readNode(XmlPullParser parser, double longitude,
                                   double latitude)
      throws XmlPullParserException, IOException {
    parser.require(XmlPullParser.START_TAG, NAMESPACE, NODE_TAG);
    Location.Builder builder =
        new Location.Builder().coordinates(latitude, longitude);
    parser.nextTag();
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.END_TAG) {
        continue;
      }
      String name = parser.getName();
      if (name.equals(TAG_TAG)) {
        setValue(parser, builder);
      }
    }
    Location location;
    try {
      location = builder.build();
    } catch (IllegalArgumentException ex) {
      return null;
    }
    return location;
  }

  private static void setValue(XmlPullParser parser,
                               final Location.Builder builder) {
    String key = parser.getAttributeValue(NAMESPACE, K_ATTR);
    String value = parser.getAttributeValue(NAMESPACE, V_ATTR);
    if (key != null && value != null) {
      switch (key) {
      case "amenity":
        builder.amenity(value);
        break;
      case "name":
        builder.name(value);
        break;
      case "opening_hours":
        builder.hours(value);
        break;
      case "shop":
        builder.amenity(value);
        break;
      case "website":
        builder.url(value);
        break;
      default:
        break;
      }
    }
  }

  private static void skip(XmlPullParser parser)
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
