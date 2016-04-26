package ru.osslabs.lang

import ru.osslabs.integrations.lang.domain.GraphEdge
import ru.osslabs.integrations.lang.domain.UUIDGraphVertex

/**
 * Created by ikuchmin on 19.01.16.
 */
//@CompileStatic
class GraphObjectBuilder extends BuilderSupport {

    final BuilderGraph builderGraph;
    Long nextID

    GraphObjectBuilder() {
        super()
        builderGraph = new BuilderGraph(GraphEdge.metaClass.&invokeConstructor)
        nextID = 0L
    }

    BuilderGraph graph(Closure closure) {
        if (closure != null) {
            // let's register the builder as the delegate
            setClosureDelegate(closure, this);
            closure()
        }

        return builderGraph
    }

    @Override
    protected void setParent(Object parent, Object child) {
        builderGraph.addEdge(parent as UUIDGraphVertex, child as UUIDGraphVertex)
    }

    @Override
    protected Object createNode(Object name) {
//        def vertex = new UUIDGraphVertex(name: name, attributes: [:])
        def vertex = new UUIDGraphVertex(name)
        builderGraph.addVertex(vertex)
        return vertex
    }

    @Override
    protected Object createNode(Object name, Object value) {
//        def vertex = new UUIDGraphVertex(name: name, attributes: [id: value])
        def vertex = new UUIDGraphVertex(name)
        builderGraph.addVertex(vertex)
        return vertex
    }

    @Override
    protected Object createNode(Object name, Map attributes) {
//        def vertex = new UUIDGraphVertex(name: name, attributes: attributes)
        def vertex = new UUIDGraphVertex(name)
        builderGraph.addVertex(vertex)
        return vertex
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
//        def vertex = new BuilderVertex(name: name, gvId: nextID++, attributes: (attributes + [id: value]))
        def vertex = new UUIDGraphVertex(name)
        builderGraph.addVertex(vertex)
        return vertex
    }

    def propertyMissing(String methodName) {
        return invokeMethod(methodName, null)
    }

    def propertyMissing(String methodName, def arg) {
        return invokeMethod(methodName, arg)
    }
}
