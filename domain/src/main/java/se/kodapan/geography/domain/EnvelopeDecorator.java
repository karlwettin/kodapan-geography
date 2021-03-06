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
package se.kodapan.geography.domain;

import java.util.Iterator;

/**
 * @author kalle
 * @since 2010-jul-20 01:16:15
 */
public abstract class EnvelopeDecorator implements Envelope {

  private static final long serialVersionUID = 1l;

  public abstract Envelope getDecoratedEnvelope();   

  @Override
  public Coordinate getSouthwest() {
    return getDecoratedEnvelope().getSouthwest();
  }

  @Override
  public Coordinate getSoutheast() {
    return getDecoratedEnvelope().getSoutheast();
  }

  @Override
  public Coordinate getNortheast() {
    return getDecoratedEnvelope().getNortheast();
  }

  @Override
  public Coordinate getNorthwest() {
    return getDecoratedEnvelope().getNorthwest();
  }

  @Override
  public void addBounds(Polygon polygon) {
    getDecoratedEnvelope().addBounds(polygon);
  }

  @Override
  public void addBounds(Coordinate... coordinates) {
    getDecoratedEnvelope().addBounds(coordinates);
  }

  @Override
  public void addBounds(double v, double v1) {
    getDecoratedEnvelope().addBounds(v, v1);
  }

  @Override
  public Iterator<Coordinate> iterateCoordinates() {
    return getDecoratedEnvelope().iterateCoordinates();
  }

  @Override
  public boolean contains(Coordinate coordinate) {
    return getDecoratedEnvelope().contains(coordinate);
  }

  @Override
  public boolean contains(Polygon polygon) {
    return getDecoratedEnvelope().contains(polygon);
  }

  @Override
  public Coordinate getCentroid() {
    return getDecoratedEnvelope().getCentroid();
  }

  @Override
  public double arcDistance(Polygon polygon) {
    return getDecoratedEnvelope().arcDistance(polygon);
  }

  @Override
  public double arcDistance(Coordinate coordinate) {
    return getDecoratedEnvelope().arcDistance(coordinate);
  }

  @Override
  public int hashCode() {
    return getDecoratedEnvelope().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return getDecoratedEnvelope().equals(o);
  }

  @Override
  public String toString() {
    return getDecoratedEnvelope().toString();
  }
}
