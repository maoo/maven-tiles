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

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.interpolation.ModelInterpolator;
import org.apache.maven.model.merge.ModelMerger;
import org.codehaus.plexus.logging.Logger;

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

  private Logger logger;

  private ModelBuildingRequest createModelBuildingRequest(Properties p) {
    ModelBuildingRequest config = new DefaultModelBuildingRequest();
    config.setSystemProperties(p);
    return config;
  }

  public void merge(Model target, Model source, boolean sourceDominant, Map<?, ?> hints, ModelInterpolator modelInterpolator) {

    //POM merging does not apply to plugin versions, therefore we need to do it manually
    if (source.getBuild() != null && target.getBuild() != null) {
      for(Plugin plugin : source.getBuild().getPlugins()) {
        Plugin targetPlugin = resolvePlugin(target.getBuild().getPlugins(),plugin);
        if (targetPlugin != null) {
          logger.debug("[Maven Tiles - merging] setting new version for plugin "+targetPlugin.getArtifactId()+": "+plugin.getVersion());
          targetPlugin.setVersion(plugin.getVersion());
        }
      }
      if (source.getBuild().getPluginManagement() != null && target.getBuild().getPluginManagement() != null) {
        for(Plugin plugin : source.getBuild().getPluginManagement().getPlugins()) {
          Plugin targetPlugin = resolvePlugin(target.getBuild().getPluginManagement().getPlugins(),plugin);
          if (targetPlugin != null) {
            logger.debug("[Maven Tiles - merging] setting new version for pluginManagement "+targetPlugin.getArtifactId()+": "+plugin.getVersion());
            targetPlugin.setVersion(plugin.getVersion());
          }
        }
      }
    }

    Map<Object, Object> context = new HashMap<Object, Object>();
    if (hints != null) {
      context.putAll(hints);
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

    logger.debug("Merging tile " + source.getArtifactId() + " with POM " + target.getArtifactId());
    printMessages("[Maven Tiles - merging] Fatal Error: ", collector.getFatals(), Logger.LEVEL_FATAL);
    printMessages("[Maven Tiles - merging] Error: ", collector.getErrors(), Logger.LEVEL_ERROR);
    printMessages("[Maven Tiles - merging] Warning: ", collector.getWarnings(), Logger.LEVEL_WARN);
  }

  private Plugin resolvePlugin(List<Plugin> plugins, Plugin pluginToResolve) {
    for(Plugin plugin : plugins) {
      if (pluginToResolve.getArtifactId().equals(plugin.getArtifactId()) &&
          pluginToResolve.getGroupId().equals(plugin.getGroupId())) {
        return plugin;
      }
    }
    return null;
  }

  public void mergeProfiles() {
    //      logger.info("Before Model injected profiles: "+currentProject.getInjectedProfileIds());
    //      logger.info("Before Model active profiles: "+currentProject.getActiveProfiles());

    //      logger.info("POM info: "+currentPomInformation);

    //Add profiles to the current list of injected ones
    //      List<String> tileProfileIds = new ArrayList<String>();
    //      for (Profile p : tileModel.getProfiles()) {
    //        mavenSession.getProjectBuildingRequest().addProfile(p);
    //        tileProfileIds.add(p.getId());
    //        logger.info("Adding tile profile: "+p.getId());
    //
    //      }

    //      List<String> activeProfiles = currentProject.getInjectedProfileIds().get("external");
    //      if (tileProfileIds != null) {
    //        activeProfiles.addAll(tileProfileIds);
    //      }

    //      logger.info("Model injected profiles: "+currentProject.getInjectedProfileIds());
    //      logger.info("Model active profiles: "+currentProject.getActiveProfiles());

    //      currentProject.setInjectedProfileIds("external",tileProfileIds);
  }

  private void printMessages(String messageTitle, List<String> messages, int level) {
    if (messages.size() > 0) {
      for (String message : messages) {
        switch (level) {
          case Logger.LEVEL_FATAL:
            logger.fatalError(messageTitle + message);
          case Logger.LEVEL_ERROR:
            logger.error(messageTitle + message);
          case Logger.LEVEL_WARN:
            logger.warn(messageTitle + message);
        }
      }
    }
  }

  public void setLogger(Logger logger) {
    this.logger = logger;
  }
}
