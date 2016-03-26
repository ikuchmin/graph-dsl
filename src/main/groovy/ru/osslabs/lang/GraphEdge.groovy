package ru.osslabs.lang

import groovy.transform.Canonical
import ru.osslabs.graph.Edge

/**
 * Created by ikuchmin on 20.01.16.
 */
@Canonical
class GraphEdge implements Edge<GraphVertex> {
    final GraphVertex source;
    final GraphVertex target;
}
