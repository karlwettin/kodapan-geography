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
import java.util.NoSuchElementException;

/**
 * @author kalle
 * @since 2010-jun-23 19:08:26
 */
public abstract class AbstractEnvelope
    extends AbstractPolygon
    implements Envelope {


  private static final long serialVersionUID = 1l;

  protected void coordinateFactory() {
    throw new UnsupportedOperationException("Not implemented in " + getClass().getName());
  }

  public void addBounds(Polygon polygon) {
    for (Iterator<Coordinate> it = polygon.iterateCoordinates(); it.hasNext();) {
      Coordinate coordinate = it.next();
      addBounds(coordinate.getLatitude(), coordinate.getLongitude());
    }
  }

  public final void addBounds(Coordinate... coordinates) {
    for (Coordinate coordinate : coordinates) {
      addBounds(coordinate.getLatitude(), coordinate.getLongitude());
    }
  }

  public final void addBounds(double latitude, double longitude) {

    if (getNortheast() == null) {
      coordinateFactory();
      getNortheast().setLatitude(latitude);
      getSouthwest().setLatitude(latitude);

      getNortheast().setLongitude(longitude);
      getSouthwest().setLongitude(longitude);

    } else {
      if (latitude > getNortheast().getLatitude()) {
        getNortheast().setLatitude(latitude);
      } else if (latitude < getSouthwest().getLatitude()) {
        getSouthwest().setLatitude(latitude);
      }

      if (getNortheast().getLongitude() > longitude) {
        getNortheast().setLongitude(longitude);
      } else if (getSouthwest().getLongitude() < longitude) {
        getSouthwest().setLongitude(longitude);
      }
    }
  }


  @Override
  public Iterator<Coordinate> iterateCoordinates() {
    return new Iterator<Coordinate>() {
      int state = 0;

      @Override
      public boolean hasNext() {
        return state < 4;
      }

      @Override
      public Coordinate next() {
        Coordinate coordinate;
        switch (state) {
          case 0:
            coordinate = getNortheast();
            break;
          case 1:
            coordinate = getNorthwest();
            break;
          case 2:
            coordinate = getSouthwest();
            break;
          case 3:
            coordinate = getSoutheast();
            break;
          default:
            throw new NoSuchElementException();
        }
        state++;
        return coordinate;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  public boolean contains(Coordinate coordinate) {

    if (getNortheast().getLongitude() > getSouthwest().getLongitude()) {

      // spans international date line
      // todo these should be transient

      EnvelopeImpl asianSide = new EnvelopeImpl();
      asianSide.setNortheast(new CoordinateImpl(getNortheast().getLatitude(), 180d));
      asianSide.setSouthwest(new CoordinateImpl(getSouthwest().getLatitude(), getSouthwest().getLongitude()));

      EnvelopeImpl americanSide = new EnvelopeImpl();
      asianSide.setNortheast(new CoordinateImpl(getNortheast().getLatitude(), getNortheast().getLongitude()));
      asianSide.setSouthwest(new CoordinateImpl(getSouthwest().getLatitude(), -180d));

      return asianSide.contains(coordinate) || americanSide.contains(coordinate);

    } else {

      return coordinate.getLatitude() <= getNortheast().getLatitude()
          && coordinate.getLongitude() >= getNortheast().getLongitude()
          && coordinate.getLatitude() >= getSouthwest().getLatitude()
          && coordinate.getLongitude() <= getSouthwest().getLongitude();
    }
  }


  @Override
  public Coordinate getSoutheast() {
    return new AbstractCoordinate() {

      private static final long serialVersionUID = 1l;

      @Override
      public Double getLatitude() {
        return getSouthwest().getLatitude();

      }

      @Override
      public Double getLongitude() {
        return getNortheast().getLongitude();
      }

      @Override
      public void setLatitude(Double latitude) {
        getSouthwest().setLatitude(latitude);
      }

      @Override
      public void setLongitude(Double longitude) {
        getNortheast().setLatitude(longitude);
      }

    };
  }

  @Override
  public AbstractCoordinate getNorthwest() {
    return new AbstractCoordinate() {

      private static final long serialVersionUID = 1l;

      @Override
      public Double getLatitude() {
        return getNortheast().getLatitude();

      }

      @Override
      public Double getLongitude() {
        return getSouthwest().getLongitude();
      }

      @Override
      public void setLatitude(Double latitude) {
        getNortheast().setLatitude(latitude);
      }

      @Override
      public void setLongitude(Double longitude) {
        getSouthwest().setLongitude(longitude);
      }

    };
  }

  @Override
  public final AbstractCoordinate getCentroid() {
    return new AbstractCoordinate() {

      private static final long serialVersionUID = 1l;

      @Override
      public Double getLatitude() {
        return (getSouthwest().getLatitude() + getNortheast().getLatitude()) / 2d;

      }

      @Override
      public Double getLongitude() {
        return (getSouthwest().getLongitude() + getNortheast().getLongitude()) / 2d;
      }

      @Override
      public void setLatitude(Double latitude) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void setLongitude(Double longitude) {
        throw new UnsupportedOperationException();
      }

    };

  }

  @Override
  public double arcDistanceDiagonal() {
    return getSouthwest().arcDistance(getNortheast());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Envelope.class.isAssignableFrom(o.getClass())) return false;

    Envelope envelope = (Envelope) o;

    if (getNortheast() != null ? !getNortheast().equals(envelope.getNortheast()) : envelope.getNortheast() != null) return false;
    if (getSouthwest() != null ? !getSouthwest().equals(envelope.getSouthwest()) : envelope.getSouthwest() != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = getSouthwest() != null ? getSouthwest().hashCode() : 0;
    result = 31 * result + (getNortheast() != null ? getNortheast().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
        "southwest=" + getSouthwest() +
        ", northeast=" + getNortheast() +
        '}';
  }
}
