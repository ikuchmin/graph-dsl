package ru.osslabs.lang

import groovy.transform.CompileStatic
import ru.osslabs.graph.DirectedGraph
import ru.osslabs.graph.GraphMap
import ru.osslabs.graph.impl.AbstractDirectedGraph
import ru.osslabs.graph.impl.DirectedEdgeContainer
import ru.osslabs.graph.impl.SimpleGraphMap

import java.util.function.BiFunction

/**
 * Created by ikuchmin on 27.03.16.
 */
@CompileStatic
class BuilderGraph extends AbstractDirectedGraph<BuilderVertex, BuilderEdge> implements DirectedGraph<BuilderVertex, BuilderEdge> {

    final BiFunction<BuilderVertex, BuilderVertex, BuilderEdge> edgeFactory
    final SimpleGraphMap<BuilderVertex, DirectedEdgeContainer<BuilderVertex, BuilderEdge>> innerMap = new SimpleGraphMap<>()

    BuilderGraph(BiFunction<BuilderVertex, BuilderVertex, BuilderEdge> edgeFactory) {
        this.edgeFactory = edgeFactory
    }

    @Override
    protected GraphMap<BuilderVertex, DirectedEdgeContainer<BuilderVertex, BuilderEdge>> getGraphMap() {
        return this.innerMap
    }

    @Override
    BiFunction<BuilderVertex, BuilderVertex, BuilderEdge> getEdgeFactory() {
        return this.edgeFactory
    }

    Collection<BuilderVertex> findVertexByName(String name) {
        return graphMap.keySet().findAll {k -> k.name.equals(name)}
    }
}
