package ru.osslabs.lang

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import ru.osslabs.graph.Edge

/**
 * Created by ikuchmin on 20.01.16.
 */
@Canonical
@CompileStatic
class BuilderEdge implements Edge<BuilderVertex> {
    final BuilderVertex source;
    final BuilderVertex target;
}
