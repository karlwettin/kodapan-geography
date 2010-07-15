/*
   Copyright 2010 Kodapan

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package se.kodapan.geography.geocoding;

import junit.framework.TestCase;
import org.junit.Test;
import se.kodapan.geography.core.PolygonTools;

import java.io.File;

/**
 * @author kalle
 * @since 2010-jun-23 02:01:03
 */
public class TestGeocoder extends TestCase {

  private Geocoder geocoder;

  @Override
  protected void setUp() throws Exception {
    com.google.maps.geocoding.GoogleGeocoder googleGeocoder = new com.google.maps.geocoding.GoogleGeocoder();
    googleGeocoder.setCachePath(new File("target/cache/google/geocoder"));
    googleGeocoder.open();
    geocoder = new GoogleGeocoder(googleGeocoder);
  }

  @Test
  public void test() throws Exception {

    double km;

    Geocoding halmstad = geocoder.geocode("Halmstad, Hallands län, Sverige");
    Geocoding halland = geocoder.geocode("Hallands län, Sverige");
    Geocoding sverige = geocoder.geocode("Sverige");

    assertTrue(halmstad.isSuccess());
    assertTrue(halland.isSuccess());
    assertTrue(sverige.isSuccess());

    assertTrue(sverige.contains(halland));
    assertTrue(halland.contains(halmstad));

    assertFalse(halmstad.contains(halland));
    assertFalse(halland.contains(sverige));

    assertTrue(halmstad.contains(halmstad));
    assertTrue(halland.contains(halland));
    assertTrue(sverige.contains(sverige));


    assertEquals(0d, halmstad.archDistance(halland));
    assertEquals(0d, halmstad.archDistance(sverige));

    assertEquals(0d, halland.archDistance(halmstad));
    assertEquals(0d, halland.archDistance(sverige));

    assertEquals(0d, sverige.archDistance(halmstad));
    assertEquals(0d, sverige.archDistance(halland));


    Geocoding laholm = assertArchDistance(halmstad, "Laholm, Hallands län, Sverige", 17, 20);
    assertFalse(halmstad.contains(laholm));
    assertFalse(laholm.contains(halmstad));
    assertCentroidDistance(halmstad, laholm, 30, 40);

    Geocoding stockholm = assertArchDistance(halmstad, "Stockholm, Stockholms län, Sverige", 350, 370);
    assertFalse(halmstad.contains(stockholm));
    assertFalse(stockholm.contains(halmstad));
    assertFalse(stockholm.contains(laholm));
    assertFalse(laholm.contains(stockholm));

    assertCentroidDistance(halmstad, stockholm, 415, 430);


    // test bounds

    Geocoding opgs = geocoder.geocode("Olof palmes gatan 23", stockholm);
    assertTrue(opgs.isSuccess());
    assertTrue(stockholm.contains(opgs));

    Geocoding umeå = geocoder.geocode("Umeå, Sverige");
    assertTrue(umeå.isSuccess());
    Geocoding opgu = geocoder.geocode("Olof palmes gatan 23", umeå);
    assertTrue(opgu.isSuccess());
    assertTrue(umeå.contains(opgu));


    // test proximity scorer
    ProximityScorer proximityScorer = new ProximityScorer(geocoder.geocode("Olof palmes gatan 23"));
    proximityScorer.add(stockholm);
    proximityScorer.add(umeå);
    Geocoding opg = proximityScorer.filter();
    assertFalse(opg.isSuccess());


    proximityScorer = new ProximityScorer(geocoder.geocode("Olof palmes gatan 23"));
    proximityScorer.add(stockholm);
    opg = proximityScorer.filter();

    ScoreThreadsholdFilter scoreThreadsholdFilter = new ScoreThreadsholdFilter(opg);
    opg = scoreThreadsholdFilter.filter();

    assertTrue(opg.isSuccess());
    assertTrue(stockholm.contains(opg));

    // this will fail
    assertFalse(geocoder.geocode("Svalgränd 4, 5 tr, hiss sålt").isSuccess());
    System.currentTimeMillis();


    assertEquals(halmstad, PolygonTools.findSmallestEnclosingBounds(halland, halmstad, sverige));
    assertEquals(halland, PolygonTools.findSmallestEnclosingBounds(halland, halmstad, sverige, laholm));
    assertEquals(laholm, PolygonTools.findSmallestEnclosingBounds(laholm, sverige, halland));
    assertEquals(sverige, PolygonTools.findSmallestEnclosingBounds(halland, stockholm, umeå, halmstad, sverige));
    System.currentTimeMillis();
  }

  private void assertCentroidDistance(Geocoding from, Geocoding to, double minimumDistance, double maximumDistance) {
    double km;
    km = to.getCentroid().archDistance(from.getCentroid());
    assertTrue(km >= minimumDistance);
    assertTrue(km <= maximumDistance);
  }

  private Geocoding assertArchDistance(Geocoding from, String textQuery, double minimumKilometers, double maximumKilometers) throws Exception {
    double km;
    Geocoding to = geocoder.geocode(textQuery);


    km = to.archDistance(from);
    assertEquals(km, from.archDistance(to));

    assertTrue(km >= minimumKilometers);
    assertTrue(km <= maximumKilometers);

    return to;
  }

}