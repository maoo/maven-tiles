package it.session.maven.plugin;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.merge.ModelMerger;

import java.util.HashMap;
import java.util.Map;

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

    super.merge(target, source, sourceDominant, hints);

    if (source.getBuild() != null) {
      super.merge(target, source, sourceDominant,context);
      for(Plugin sourcePlugin : source.getBuild().getPlugins()) {
        Plugin targetPlugin = target.getBuild().getPluginsAsMap().get(sourcePlugin.getKey());
        super.mergePlugin(targetPlugin, sourcePlugin, sourceDominant, context);
      }
    }
  }
}
