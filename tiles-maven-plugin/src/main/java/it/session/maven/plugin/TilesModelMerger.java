package it.session.maven.plugin;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.merge.ModelMerger;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.StringUtils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * TilesModelMerger by-passes the invocation to ModelMerger.merge() by adding the merge of Plugin configuration.
 */
public class TilesModelMerger extends ModelMerger {

  public void merge( Model target, Model source, boolean sourceDominant, Map<?, ?> hints ) {

    Map<Object, Object> context = new HashMap<Object, Object>();
    if ( hints != null )
    {
      context.putAll( hints );
    }

//    Enumeration sourcePropertyNames =  source.getProperties().keys();
//    while(sourcePropertyNames.hasMoreElements()) {
//      String sourcePropertyName = (String)sourcePropertyNames.nextElement();
//      String targetPropertyValue = target.getProperties().getProperty(sourcePropertyName);
//      if (targetPropertyValue != null) {
//        source.getProperties().put(sourcePropertyName,targetPropertyValue);
//        System.out.println("Setting property '"+sourcePropertyName+"' to value '"+targetPropertyValue+"'");
//      }
//    }

//    //@TODO - do we really need this? It can potentially duplicate logic, needs to be carefully tested
    if (source.getBuild() != null) {
//      super.merge(target, source, sourceDominant,context);

//      source.

      for(Plugin sourcePlugin : source.getBuild().getPlugins()) {
        String version = sourcePlugin.getVersion();
        version = StringUtils.interpolate(version,target.getProperties());
        sourcePlugin.setVersion(version);
        System.out.println("Setting version '"+version+"' to plugin '"+sourcePlugin.getArtifactId()+"'");
//        Plugin targetPlugin = target.getBuild().getPluginsAsMap().get(sourcePlugin.getKey());
//        if (targetPlugin != null) {
//          super.mergePlugin(targetPlugin, sourcePlugin, sourceDominant, context);
//        }
      }
    }

    super.merge(target, source, sourceDominant, hints);

    //Properties defined in the target - by default - override the *values* of tile's model properties (source) but
    //not in the plugin's versions. We need to force this resolution
//    Properties targetProperties = target.getProperties();
//    while(sourcePropertyNames.hasMoreElements()) {
//      String sourcePropertyName = (String)sourcePropertyNames.nextElement();
//      String targetPropertyValue = target.getProperties().getProperty(sourcePropertyName);
//      if (targetPropertyValue != null) {
//        targetProperties.put(sourcePropertyName,targetPropertyValue);
//        System.out.println("Setting property '"+sourcePropertyName+"' to value '"+targetPropertyValue+"'");
//      }
//    }
    //target.setProperties(targetProperties);
  }
}
