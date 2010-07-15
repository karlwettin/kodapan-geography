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
package se.kodapan.geography.core;

/**
 * @author kalle
 * @since 2010-jun-23 21:26:42
 */
public class EnvelopeImpl extends CoordinatedEnvelope {

  private static final long serialVersionUID = 1l;


  private String polygonName;

  public EnvelopeImpl() {
  }

  public EnvelopeImpl(String polygonName) {
    this.polygonName = polygonName;
  }

  public String getPolygonName() {
    return polygonName;
  }

  public void setPolygonName(String polygonName) {
    this.polygonName = polygonName;
  }
}