package ru.osslabs.lang;

import ru.osslabs.graph.DirectedGraph;
import ru.osslabs.graph.collection.SimpleGraphMap;
import ru.osslabs.graph.impl.AbstractDirectedGraph;
import ru.osslabs.graph.impl.DirectedEdgeContainer;
import ru.osslabs.integrations.lang.domain.GraphEdge;
import ru.osslabs.integrations.lang.domain.BuilderVertex;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by ikuchmin on 27.03.16.
 */

public class BuilderGraph extends AbstractDirectedGraph<BuilderVertex, GraphEdge<BuilderVertex>, BuilderGraph, SimpleGraphMap<BuilderVertex, DirectedEdgeContainer<BuilderVertex, GraphEdge<BuilderVertex>>>>
        implements DirectedGraph<BuilderVertex, GraphEdge<BuilderVertex>, BuilderGraph> {

    final BiFunction<BuilderVertex, BuilderVertex, GraphEdge<BuilderVertex>> edgeFactory;
    final SimpleGraphMap<BuilderVertex, DirectedEdgeContainer<BuilderVertex, GraphEdge<BuilderVertex>>> innerMap = new SimpleGraphMap<>();

    BuilderGraph(BiFunction<BuilderVertex, BuilderVertex, GraphEdge<BuilderVertex>> edgeFactory) {
        this.edgeFactory = edgeFactory;
    }

    @Override
    protected SimpleGraphMap<BuilderVertex, DirectedEdgeContainer<BuilderVertex, GraphEdge<BuilderVertex>>> getGraphMap() {
        return this.innerMap;
    }

    @Override
    public BiFunction<BuilderVertex, BuilderVertex, GraphEdge<BuilderVertex>> getEdgeFactory() {
        return this.edgeFactory;
    }

    public Optional<BuilderVertex> findVertexByName(String name) {
        return innerMap.keys().stream().filter(k -> k.getName().equals(name)).findFirst();
    }

    public List<Boolean> containsOutgoingVerticesByName(String vertex, String... vertices) {
        Optional<BuilderVertex> parent = findVertexByName(vertex);
        if (!parent.isPresent()) return Arrays.stream(vertices).map(v -> false).collect(Collectors.toList());

        Function<String, Optional<BuilderVertex>> outgoingVertexByName = v ->
                outgoingEdgesOf(parent.get()).stream()
                        .map(GraphEdge::getTarget)
                        .filter(ov -> ov.getName().equals(v))
                        .findFirst();

        return Arrays.stream(vertices)
                .map(outgoingVertexByName)
                .map(Optional::isPresent)
                .collect(Collectors.toList());
    }

    public boolean containsAllOutgoingVerticesByName(String vertex, String... vertices) {
        return !containsOutgoingVerticesByName(vertex, vertices).contains(false);
    }

    public boolean containsVertexByName(String name) {
        return findVertexByName(name).isPresent();
    }

}
