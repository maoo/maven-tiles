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

import org.apache.maven.model.*;
import org.apache.maven.model.merge.ModelMerger;
import org.codehaus.plexus.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * TilesModelMerger intercepts the invocation to ModelMerger.merge() and enriches the source tile model with the target
 * property values before invoking super.merge().
 * Property interpolation is delegated to org.codehaus.plexus.util.StringUtils
 * Property interpolation is currently executed against plugin and pluginManagement <version> definitions (including
 * profiles)
 */
public class TilesModelMerger extends ModelMerger {

  public void merge( Model target, Model source, boolean sourceDominant, Map<?, ?> hints ) {

    Map<Object, Object> context = new HashMap<Object, Object>();
    if ( hints != null )
    {
      context.putAll( hints );
    }

    //Interpolate tile source *build* model with target's properties
    interpolateBuild(source.getBuild(), target.getProperties());

    //Interpolate tile source *profile* models with target's properties
    for (Profile profile : source.getProfiles()) {
      interpolateBuild(profile.getBuild(),target.getProperties());
    }

    //Now we can merge the source tile with the target model
    super.merge(target, source, sourceDominant, hints);
  }

  private void interpolateBuild(BuildBase build, Properties properties) {
    if (build != null) {

      //Interpolate *plugin management*, if present
      PluginManagement pluginManagement = build.getPluginManagement();
      if (pluginManagement != null) {
        List<Plugin> plugins = pluginManagement.getPlugins();
        if (plugins != null) {
          for(Plugin sourcePlugin : plugins) {
            interpolatePlugin(properties, sourcePlugin);
          }
        }
      }

      //Interpolate *build plugins*, if present
      List<Plugin> plugins = build.getPlugins();
      if (plugins != null) {
        for(Plugin sourcePlugin : plugins) {
          interpolatePlugin(properties, sourcePlugin);
        }
      }
    }
  }

  private void interpolatePlugin(Properties properties, Plugin sourcePlugin) {
    String version = sourcePlugin.getVersion();
    version = interpolateString(version, properties);
    sourcePlugin.setVersion(version);
  }

  private String interpolateString(String version, Properties properties) {
    String ret = StringUtils.interpolate(version,properties);
    System.out.println("Interpolated string "+version+" : "+ret);
    return ret;
  }
}
