package ru.osslabs.lang

import org.jgrapht.DirectedGraph
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.builder.DirectedGraphBuilder

/**
 * Created by ikuchmin on 19.01.16.
 */
class GraphObjectBuilder extends BuilderSupport {

    Long nextID

    DirectedGraphBuilder<GraphVertex, GraphEdge, DirectedGraph<GraphVertex, GraphEdge>> graphBuilder

    GraphObjectBuilder() {
        graphBuilder = new DirectedGraphBuilder<>(new DefaultDirectedGraph<>(new GraphEdgeFactory<GraphVertex, GraphEdge>()))
        nextID = 0L
    }

    DirectedGraph<GraphVertex, GraphEdge> graph(Closure closure) {
        if (closure != null) {
            // let's register the builder as the delegate
            setClosureDelegate(closure, this);
            graphBuilder.addVertex(closure.call() as GraphVertex);
        }

        graphBuilder.buildUnmodifiable();
    }

    @Override
    protected void setParent(Object parent, Object child) {
        graphBuilder.addEdge(parent as GraphVertex, child as GraphVertex);
    }

    @Override
    protected Object createNode(Object name) {
        new GraphVertex(name: name, gvId: nextID++, attributes: [:])
    }

    @Override
    protected Object createNode(Object name, Object value) {
        new GraphVertex(name: name, gvId: nextID++, attributes: [id: value])
    }

    @Override
    protected Object createNode(Object name, Map attributes) {
        new GraphVertex(name: name, gvId: nextID++, attributes: attributes)
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
        new GraphVertex(name: name, gvId: nextID++, attributes: (attributes + [id: value]))
    }

    def propertyMissing(String methodName) {
        return invokeMethod(methodName, null)
    }

    def propertyMissing(String methodName, def arg) {
        return invokeMethod(methodName, arg)
    }
}
