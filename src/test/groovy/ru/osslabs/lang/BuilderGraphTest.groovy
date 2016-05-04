package ru.osslabs.lang

import ru.osslabs.integrations.lang.domain.GraphEdge
import ru.osslabs.integrations.lang.domain.BuilderVertex
import spock.lang.Specification

/**
 * Created by ikuchmin on 06.04.16.
 */
class BuilderGraphTest extends Specification {
    def graph = new BuilderGraph(GraphEdge.metaClass.&invokeConstructor)

    def "if vertex with name has in graph that simple contains method in graph should return true"() {
        given:
        graph.addVertices(['v1'] as BuilderVertex, ['v2'] as BuilderVertex, ['v3'] as BuilderVertex)

        expect:
        graph.containsVertexByName('v1')
        graph.containsVertexByName('v2')
        graph.containsVertexByName('v3')
        !graph.containsVertexByName('v4')
    }

    def "if graph has edge between to vertices that outgoing contains method should return true"() {
        given:
        def v1 = ['v1'] as BuilderVertex
        def v2 = ['v2'] as BuilderVertex
        def v3 = ['v3'] as BuilderVertex
        graph.addVertices(v1, v2, v3).addEdge(v1, v2).addEdge(v1, v3)

        expect:
        graph.containsOutgoingVerticesByName('v1', 'v2', 'v3').every(true.&equals)
        graph.containsAllOutgoingVerticesByName('v1', 'v2', 'v3')
        graph.containsOutgoingVerticesByName('v1', 'v2', 'v3', 'v4') == [true, true, false]
        !graph.containsAllOutgoingVerticesByName('v1', 'v2', 'v3', 'v4')

    }

    def "if vertex with name has in graph that graph should return that by name"() {
        given:
        def v1 = ['v1'] as BuilderVertex
        graph.addVertices(v1)

        expect:
        graph.findVertexByName('v1').get() == v1
        !graph.findVertexByName('v2').isPresent()
    }


}
