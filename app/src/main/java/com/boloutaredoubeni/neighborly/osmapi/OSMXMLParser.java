package com.boloutaredoubeni.neighborly.osmapi;

import android.util.Log;

import com.boloutaredoubeni.neighborly.models.Location;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class OSMXMLParser {

  private static final String TAG = OSMXMLParser.class.getCanonicalName();
  private static final String NAMESPACE = null;
  private OSMXMLParser() { throw new AssertionError(); }

  public static List<Location> parse(InputStream in)
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

  private static List<Location> readNodes(XmlPullParser parser)
      throws IOException, XmlPullParserException {
    parser.require(XmlPullParser.START_TAG, NAMESPACE, "osm");
    while (parser.next() != XmlPullParser.END_DOCUMENT) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      String name = parser.getName();
      switch (name) {
      case "node": {
        Log.d(TAG, "Got name = " + name);
        // if (!parser.isEmptyElementTag()) {
        readTags(parser);
        //}
        break;
      }
      default:
        skip(parser);
      }
    }
    return null;
  }

  private static Location readTags(XmlPullParser parser)
      throws IOException, XmlPullParserException {

    Log.d(TAG, "in readTags");

    double latitude =
        Double.valueOf(parser.getAttributeValue(NAMESPACE, "lat"));
    double longitude =
        Double.valueOf(parser.getAttributeValue(NAMESPACE, "lon"));
    Log.d(TAG, "Got coordinates " + latitude + "," + longitude);

    // if (!parser.isEmptyElementTag()) {
    // readTag(parser);
    parser.require(XmlPullParser.START_TAG, NAMESPACE, "node");
    //    while(parser.next() != XmlPullParser.END_TAG) {
    //      if (parser.getEventType() != XmlPullParser.START_TAG) {
    //        continue;
    //      }
    //      Log.d(TAG, "parser.next() "+parser.getName());
    //    }

    int depth = 1;
    while (depth != 0) {
      int next = parser.next();
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        depth++;
        continue;
      } else if (parser.getEventType() != XmlPullParser.START_TAG) {
        depth--;
        continue;
      }
      //      switch (next) {
      //        case XmlPullParser.END_TAG:
      //          depth--;
      //          break;
      //        case XmlPullParser.START_TAG:
      //          depth++;
      //          break;
      //        default:
      Log.d(TAG, "element is " + parser.getName());
      if (parser.getName().equals("tag")) {
        String key = parser.getAttributeValue(NAMESPACE, "k");
        String value = parser.getAttributeValue(NAMESPACE, "v");
        Log.d("IT WORKS", "Got " + key + " = " + value);
      }
      //      }
    }
    //    if (parser.isEmptyElementTag()) {
    //      Log.d(TAG, "Got an empty tag ... skipping");
    //      return null;
    //    }

    //    while (parser.next() != XmlPullParser.END_TAG) {
    //      if ((parser.getEventType() != XmlPullParser.START_TAG ) ||
    //      (parser.isEmptyElementTag())) {
    //        Log.d(TAG, "Got an empty tag ... skipping");
    //        continue;
    //      }
    //      Log.d(TAG, "Not empty ... getting lat & long");
    //
    //      String name = parser.getName();
    //      if (!name.equals("tag")) {
    //        Log.d(TAG, "skipping...");
    //        skip(parser);
    //      } else {
    //        Log.d(TAG, "it's a tag!");
    //        return readTag(parser);
    //      }
    //    }

    return null;
  }

  private static Location readTag(XmlPullParser parser)
      throws IOException, XmlPullParserException {
    Log.d(TAG, "in readTag");
    parser.require(XmlPullParser.START_TAG, NAMESPACE, "tag");
    // parser.nextTag();
    // parser.require(XmlPullParser.START_TAG, NAMESPACE, "k");
    if (parser.isEmptyElementTag()) {
      String key = parser.getAttributeValue(NAMESPACE, "k");
      String value = parser.getAttributeValue(NAMESPACE, "v");
      Log.d(TAG, "Got " + key + " = " + value);
    }
    return null;
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
