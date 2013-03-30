/***********************************************************************************************************************
 *
 * Maven Tiles
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/
package it.session.maven.tiles.plugin;

import java.util.StringTokenizer;

public class TilesUtils {

  public static final String TILE_EXTENSION = "pom";
  public static final String TILE_PROPERTY_PREFIX = "tile.";

  public static StringTokenizer getTilesTokens(String propertyValue) {
    return new StringTokenizer(propertyValue, ":");
  }

  public static String getTilesKey(StringTokenizer propertyTokens) {
    String groupId = propertyTokens.nextToken();
    String artifactId = propertyTokens.nextToken();
    String version = propertyTokens.nextToken();

    return String.format("'%s:%s:%s'",
        groupId,
        artifactId,
        version);
  }

  public static String getTilesKey(String propertyValue) {
    StringTokenizer propertyTokens = getTilesTokens(propertyValue);
  return getTilesKey(propertyTokens);
  }
}
