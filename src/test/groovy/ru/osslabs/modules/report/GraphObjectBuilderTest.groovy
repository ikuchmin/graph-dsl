package ru.osslabs.modules.report

import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.builder.DirectedGraphBuilder
import ru.osslabs.model.datasource.DataObject
import spock.lang.Specification

/**
 * Created by ikuchmin on 19.01.16.
 */
class GraphObjectBuilderTest extends Specification {

    def "builder should make graph for one vertex without parentheses in leaf"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()

        when:
        def graph = builder.graph {
            persons {}
        }


        then:
        graph == new DirectedGraphBuilder(
                new DefaultDirectedGraph(
                        new GraphEdgeFactory<>()))
                .addVertex(new GraphVertex(name: "persons", gvId: 0))
                .buildUnmodifiable();
    }

    def "builder should make graph for one vertex with parentheses in leaf"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()

        when:
        def graph = builder.graph {
            persons
        }

        then:
        graph == new DirectedGraphBuilder(
                new DefaultDirectedGraph(
                        new GraphEdgeFactory<>()))
                .addVertex(new GraphVertex(name: "persons", gvId: 0))
                .buildUnmodifiable();

    }

    def "builder should make graph for nested fields without parentheses in leaf"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()

        when:
        def graph = builder.graph {
            persons {
                firstName
            }
        }

        then:
        graph == new DirectedGraphBuilder<>(
                new DefaultDirectedGraph(
                        new GraphEdgeFactory()))
                .addEdge(new GraphVertex(name: "persons", gvId: 0), new GraphVertex(name: "firstName", gvId: 1))
                .buildUnmodifiable();
    }

    def "builder should make graph for nested fields with parentheses in leaf"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()

        when:
        def graph = builder.graph {
            persons {
                firstName {}
            }
        }

        then:
        graph == new DirectedGraphBuilder<>(
                new DefaultDirectedGraph(
                        new GraphEdgeFactory()))
                .addEdge(new GraphVertex(name: "persons", gvId: 0), new GraphVertex(name: "firstName", gvId: 1))
                .buildUnmodifiable();
    }

    def "builder should make graph for nested object with same field name and it is true tree"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()

        when:
        def graph = builder.graph {
            person {
                firstName {
                    value
                }
                lastName {
                    value
                }
            }
        }

        then:
        graph == new DirectedGraphBuilder<>(
                new DefaultDirectedGraph(
                        new GraphEdgeFactory()))
                .addEdgeChain(new GraphVertex(name: "person", gvId: 0), new GraphVertex(name: "firstName", gvId: 1), new GraphVertex(name: "value", gvId: 2))
                .addEdgeChain(new GraphVertex(name: "person", gvId: 0), new GraphVertex(name: "lastName", gvId: 3), new GraphVertex(name: "value", gvId: 4))
                .buildUnmodifiable()

        graph.edgeSet().size() == graph.vertexSet().size() - 1 // https://en.wikipedia.org/wiki/Tree_(graph_theory)
    }

    def "field in builder can has explicit property id"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()

        when:
        def graph = builder.graph {
            person(id: 123)
        }

        then:
        graph == new DirectedGraphBuilder<>(new DefaultDirectedGraph(new GraphEdgeFactory()))
                .addVertex(new GraphVertex(name: "person", gvId: 0, attributes: [id: 123]))
                .buildUnmodifiable()
    }

    def "method in builder can has explicit property id"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()

        when:
        def graph = builder.graph {
            person(id: 123) {}
        }

        then:
        graph == new DirectedGraphBuilder<>(new DefaultDirectedGraph(new GraphEdgeFactory()))
                .addVertex(new GraphVertex(name: "person", gvId: 0, attributes: [id: 123]))
                .buildUnmodifiable()
    }

    def "method in builder can has implicit property id"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()

        when:
        def graph = builder.graph {
            person(123) {}
        }

        then:
        graph == new DirectedGraphBuilder<>(new DefaultDirectedGraph(new GraphEdgeFactory()))
                .addVertex(new GraphVertex(name: "person", gvId: 0, attributes: [id: 123]))
                .buildUnmodifiable()
    }

    def "parameter id shouldn't have concrete type"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()

        when:
        def graph = builder.graph {
            person(id: "i13dsr") {}
        }

        then:
        graph == new DirectedGraphBuilder<>(new DefaultDirectedGraph(new GraphEdgeFactory()))
                .addVertex(new GraphVertex(name: "person", gvId: 0, attributes: [id: "i13dsr"]))
                .buildUnmodifiable()
    }

    def "method in builder can has explicit filter as closure"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()
        def filter = { it.age > 12 }

        when:
        def graph = builder.graph {
            persons(filter: filter) {
                age
            }
        }

        then:
        graph == new DirectedGraphBuilder<>(new DefaultDirectedGraph(new GraphEdgeFactory())).addEdge(
                new GraphVertex(name: "persons", gvId: 0, attributes: [filter: filter]),
                new GraphVertex(name: "age", gvId: 1)
        ).buildUnmodifiable()
    }


    def "fields are using in filter must be in graph"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()

        when:
        def graph = builder.graph {
            persons(filter: { it.age > 12 }) {}
        }

        then:
        thrown(Exception)
    }

    def "builder might include reference on external fragments"() {
        given:
        GraphObjectBuilder builder = new GraphObjectBuilder()
        def personFields = {
            firstName
            lastName
        }

        when:
        def graph = builder.graph {
            person personFields
        }

        then:
        graph == new DirectedGraphBuilder<>(
                new DefaultDirectedGraph(
                        new GraphEdgeFactory()))
                .addEdgeChain(new GraphVertex(name: "person", gvId: 0), new GraphVertex(name: "firstName", gvId: 1))
                .addEdgeChain(new GraphVertex(name: "person", gvId: 0), new GraphVertex(name: "lastName", gvId: 2))
                .buildUnmodifiable()
    }
}
