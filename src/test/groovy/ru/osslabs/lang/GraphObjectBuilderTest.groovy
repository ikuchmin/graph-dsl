package ru.osslabs.lang

import spock.lang.Ignore
import spock.lang.Specification

/**
 * Created by ikuchmin on 19.01.16.
 */
class GraphObjectBuilderTest extends Specification {

    GraphObjectBuilder builder = new GraphObjectBuilder()

    def "builder should make graph for one vertex with parentheses in leaf"() {
        when:
        def graph = builder.graph {
            persons {}
        }

        then:
        graph.findVertexByName('persons').first()
    }

    def "builder should make graph for one vertex without parentheses in leaf"() {
        when:
        def graph = builder.graph {
            persons
        }

        then:
        graph.findVertexByName('persons').first()
    }

    def "builder should make graph for nested fields without parentheses in leaf"() {
        when:
        def graph = builder.graph {
            persons {
                firstName
            }
        }
        def persons = graph.findVertexByName('persons').first()
        def fn = graph.findVertexByName('firstName').first()

        then:
        persons
        graph.containsOutgoingVertices(persons, fn) == [true]

    }

    def "builder should make graph for nested fields with parentheses in leaf"() {
        when:
        def graph = builder.graph {
            persons {
                firstName {}
            }
        }
        def persons = graph.findVertexByName('persons').first()
        def fn = graph.findVertexByName('firstName').first()

        then:
        persons
        graph.containsOutgoingVertices(persons, fn) == [true]
    }

    def "builder should make graph for nested object with same field name and it is true tree"() {
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
        def person = graph.findVertexByName('person').first()
        def fn = graph.findVertexByName('firstName').first()
        def ln = graph.findVertexByName('lastName').first()

        then:
        person
        graph.containsOutgoingVertices(person, fn, ln) == [true, true]
        graph.containsOutgoingVertices(person, fn, ln) == [true, true]
        graph.outgoingEdgesOf(fn).find { e -> 'value' == e.target.name }
        graph.outgoingEdgesOf(ln).find { e -> 'value' == e.target.name }

//        graph.edgeSet().size() == graph.vertexSet().size() - 1 // https://en.wikipedia.org/wiki/Tree_(graph_theory)
    }

    def "field in builder can has explicit property id"() {
        when:
        def graph = builder.graph {
            person(id: 123)
        }

        then:
        graph.findVertexByName('person').first().id == 123
    }

    def "method in builder can has explicit property id"() {
        when:
        def graph = builder.graph {
            person(id: 123) {}
        }

        then:
        graph.findVertexByName('person').first().id == 123
    }

    def "method in builder can has implicit property id"() {
        when:
        def graph = builder.graph {
            person(123) {}
        }

        then:
        graph.findVertexByName('person').first().id == 123
    }

    def "parameter id shouldn't have concrete type"() {
        when:
        def graph = builder.graph {
            person(id: "i13dsr") {}
        }

        then:
        graph.findVertexByName('person').first().id == 'i13dsr'
    }

    def "method in builder can has explicit filter as closure"() {
        given:
        def filter = { it.age > 12 }

        when:
        def graph = builder.graph {
            persons(filter: filter) {
                age
            }
        }
        def persons = graph.findVertexByName('persons').first()
        def age = graph.findVertexByName('age').first()

        then:
        persons.filter == filter
        graph.containsOutgoingVertices(persons, age) == [true]
    }

    @Ignore('TODO')
    def "fields are using in filter must be in graph"() {
        when:
        def graph = builder.graph {
            persons(filter: { it.age > 12 }) {}
        }

        then:
        thrown(Exception)
    }

    def "builder might include reference on external fragments"() {
        given:
        def personFields = {
            firstName
            lastName
        }

        when:
        def graph = builder.graph {
            person personFields
        }

        def person = graph.findVertexByName('person').first()
        def fn = graph.findVertexByName('firstName').first()
        def ln = graph.findVertexByName('lastName').first()

        then:
        person
        graph.containsOutgoingVertices(person, fn, ln) == [true, true]
    }

    def "builder might include reference on fragment with fields which is concrete type"() {
        when:
        def graph = builder.graph {
            person {
                firstName
                fragment(type: 'Mother') {
                    haveIsHusband
                }
            }
        }

        def person = graph.findVertexByName('person').first()
        def fn = graph.findVertexByName('firstName').first()
        def fragment = graph.findVertexByName('fragment').first()
        def hIH = graph.findVertexByName('haveIsHusband').first()

        then:
        person
        fragment.type == 'Mother'
        graph.containsOutgoingVertices(person, fn, fragment) == [true, true]
        graph.containsOutgoingVertices(fragment, hIH) == [true]
    }

    def "builder might include section with same block type"() {
        when:
        def graph = builder.graph {
            person(id: "Ivanov", type: "Person") {
                firstName
                fragment(type: 'Mother') {
                    haveIsHusband
                }
            }
            person(id: "Ivanov", type: "Person") {
                firstName
                fragment(type: 'Mother') {
                    haveIsHusband
                }
            }
        }
        def person = graph.findVertexByName('person')
        def fn = graph.findVertexByName('firstName')
        def fragment = graph.findVertexByName('fragment')
        def hIH = graph.findVertexByName('haveIsHusband')

        then:
        person.size() == 2
        fn.size() == 2
        fragment.size() == 2
        hIH.size() == 2
    }

//    def "What have i get in this case in graph-view?"() {
//        when:
//        def graph = builder.graph {
//            a {
//                field1
//                field2 {
//                    inclField1
//                    inclField2
//                }
//            }
//            b {
//                field1
//                field2 {
//                    inclField1
//                    inclField2
//                }
//            }
//        }
//
//        then:
//        graph == new DirectedGraphBuilder<>(
//                new DefaultDirectedGraph(
//                        new GraphEdgeFactory()))
//                .addEdgeChain(new BuilderEdge.GraphVertex(name: "person", gvId: 0, id: "Ivanov", type: "Person"), new BuilderEdge.GraphVertex(name: "firstName", type: "Person", gvId: 1))
//                .addEdgeChain(new BuilderEdge.GraphVertex(name: "person", gvId: 0), new BuilderEdge.GraphVertex(name: "haveIsHusband", type: "Mother", gvId: 2))
//                .buildUnmodifiable()
//    }
}
