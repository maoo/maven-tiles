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
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.interpolation.ModelInterpolator;
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

  private void interpolateDependencies(List<Dependency> dependencies, Properties properties) {
    for(Dependency dependency : dependencies) {
      String artifactId = dependency.getArtifactId();
      artifactId = interpolateString(artifactId, properties);
      dependency.setArtifactId(artifactId);
      String groupId = dependency.getGroupId();
      groupId = interpolateString(groupId, properties);
      dependency.setGroupId(groupId);
      String version = dependency.getVersion();
      version = interpolateString(version, properties);
      dependency.setVersion(version);
    }
  }

  private ModelBuildingRequest createModelBuildingRequest( Properties p )
  {
    ModelBuildingRequest config = new DefaultModelBuildingRequest();
    config.setSystemProperties( p );
    return config;
  }

  public void merge(Model target, Model source, boolean sourceDominant, Map<?, ?> hints, ModelInterpolator modelInterpolator) {

    Map<Object, Object> context = new HashMap<Object, Object>();
    if ( hints != null )
    {
      context.putAll( hints );
    }

    //Now we can merge the source tile with the target model
    super.merge(target, source, sourceDominant, hints);

    //We need to interpolate model's placeholders using tiles and target model properties
    Properties props = new Properties();
    props.putAll(source.getProperties());
    props.putAll(target.getProperties());
    ModelBuildingRequest mbr = createModelBuildingRequest(props);
    SimpleProblemCollector collector = new SimpleProblemCollector();
    modelInterpolator.interpolateModel(target, target.getProjectDirectory(), mbr, collector);
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

      List<Resource> resources = build.getResources();
      if (resources != null) {
        interpolateResources(resources,properties);
      }
    }
  }

  private void interpolateResources(List<Resource> resources, Properties properties) {
    for(Resource resource : resources) {
      interpolateResource(resource, properties);
    }
  }

  private void interpolateResource(Resource resource, Properties properties) {
    String directory = resource.getDirectory();
    directory = interpolateString(directory, properties);
    resource.setDirectory(directory);
    String filtering = resource.getFiltering();
    filtering = interpolateString(filtering, properties);
    resource.setFiltering(filtering);
    String targetPath = resource.getTargetPath();
    targetPath = interpolateString(targetPath, properties);
    resource.setTargetPath(targetPath);
  }

  private void interpolatePlugin(Properties properties, Plugin sourcePlugin) {
    String version = sourcePlugin.getVersion();
    version = interpolateString(version, properties);
    sourcePlugin.setVersion(version);
    interpolateDependencies(sourcePlugin.getDependencies(), properties);
  }

  private String interpolateString(String string, Properties properties) {
    if (StringUtils.isEmpty(string)) {
      return string;
    }
    String ret = StringUtils.interpolate(string,properties);
    if (string.equals(ret)) {
      System.out.println("Interpolated string "+string+" : "+ret);
      System.out.println(properties);
      return ret;
    } else {
      return interpolateString(ret,properties);
    }
  }
}
