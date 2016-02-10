package com.boloutaredoubeni.neighborly.osmapi;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.boloutaredoubeni.neighborly.models.Location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Copyright 2016 Boloutare Doubeni
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapDataXMLParserTest {

  private List<Location> locations;
  private InputStream in;
  private static final String xmlData =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
      +
      "<osm version=\"0.6\" generator=\"OpenStreetMap server\" copyright=\"OpenStreetMap and contributors\" attribution=\"http://www.openstreetmap.org/copyright\" license=\"http://opendatacommons.org/licenses/odbl/1-0/\"><node id=\"4299549307\" changeset=\"63587\" timestamp=\"2015-06-17T15:42:11Z\" version=\"1\" visible=\"true\" user=\"test-dev\" uid=\"2972\" lat=\"40.7468668\" lon=\"-73.9737689\">\n"
      + "    <tag k=\"amenity\" v=\"cafe\"/>\n"
      + "    <tag k=\"name\" v=\"'wichcraft\"/>\n"
      + "    <tag k=\"opening_hours\" v=\"24/7\"/>\n"
      + "    <tag k=\"shop\" v=\"cafe\"/>\n"
      + "    <tag k=\"website\" v=\"http://53rdand6th.com/\"/>\n"
      + "  </node>"
      +
      " <way id=\"4294967535\" changeset=\"22472\" timestamp=\"2013-04-16T12:36:35Z\" version=\"4\" visible=\"true\" user=\"vampouille\" uid=\"1147\">\n"
      + "    <nd ref=\"4294969664\"/>\n"
      + "    <nd ref=\"4294969665\"/>\n"
      + "    <nd ref=\"4294969666\"/>\n"
      + "    <nd ref=\"4294969667\"/>\n"
      + "    <tag k=\"highway\" v=\"residential\"/>\n"
      + "    <tag k=\"maxspeed\" v=\"70\"/>\n"
      + "  </way>\n"
      +
      "  <node id=\"4299604490\" changeset=\"63858\" timestamp=\"2015-06-24T15:32:16Z\" version=\"2\" visible=\"true\" user=\"test-dev\" uid=\"2972\" lat=\"40.7504756\" lon=\"-73.9698851\">\n"
      + "    <tag k=\"amenity\" v=\"cafe\"/>\n"
      + "    <tag k=\"name\" v=\"La Goulette\"/>\n"
      + "    <tag k=\"opening_hours\" v=\"gfg\"/>\n"
      + "    <tag k=\"shop\" v=\"cafe\"/>\n"
      + "    <tag k=\"website\" v=\"fjhf\"/>\n"
      + "  </node>"
      + "</osm>";

  //  @Before
  //  public void setup() {
  //
  //    in = this.getClass().getResourceAsStream("assets/map.xml");
  //    if (in == null) {
  //      in = new ByteArrayInputStream(xmlData.getBytes());
  //    }
  //  }

  //  @Test(expected = IOException.class)
  //  public void test_readData() {
  //    try {
  //      locations = MapDataXMLParser.parse(in);
  //    } catch (XmlPullParserException ex) {
  //      fail("The parser failed: " + ex.getMessage());
  //    } catch (IOException ex) {
  //    }
  //    assertTrue(noneAreNull(locations));
  //  }

  @Test
  public void parsesOSMTag() {
    final String osmTag =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<osm></osm>";
    in = new ByteArrayInputStream(osmTag.getBytes());
    try {
      MapDataXMLParser.parse(in);
    } catch (XmlPullParserException | IOException ex) {
      fail("Exception was thrown " + ex.getMessage());
    }
  }

  @Test
  public void ignoresEmptyNodeTag() {
    final String nodeTag = "";
  }

  private static boolean noneAreNull(List<Location> locations) {
    for (Location location : locations) {
      if (location == null) {
        return false;
      }
    }
    return true;
  }
}
