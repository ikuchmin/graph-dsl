package ru.osslabs.lang

import org.jgrapht.EdgeFactory

/**
 * Created by ikuchmin on 20.01.16.
 */
class GraphEdgeFactory<V, E> implements EdgeFactory<V, E> {
    @Override
    public E createEdge(V sourceVertex, V targetVertex) {
        [sourceVertex, targetVertex] as E
    }
}
