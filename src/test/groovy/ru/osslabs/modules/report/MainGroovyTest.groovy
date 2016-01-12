package ru.osslabs.modules.report

import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.junit.Test

/**
 * Created by ikuchmin on 03.01.16.
 */
class MainGroovyTest {
//    def firstName = "firstName"
//    def lastName = "lastName"

//    class Person {
//        Integer id
//        String firstName
//        String lastName
//        List children
//    }

    @Test
//    void 'builder should create not nested objects'() {
    void firstTest() {
        def nodeBuilder = new NodeBuilder()
        def personList = nodeBuilder.persons {
            person(id: 0, lastName: 'Иванов', firstName: 'Николай', children: [1, 2])
            person(id: 1, lastName: 'Иванов', firstName: 'Илья', children: [1, 2])
            person(id: 2, lastName: 'Иванов', firstName: 'Антон', children: [1, 2])
        }

//        assert false

        assert personList.person.@id.join(', '), '0, 1, 2'
//        builder.build {
//            person(id: 12) { // Person is a head queryName
//                firstName
//                lastName
//                childs(offset:0, count: 10) {
//                    firstName
//                    lastName
//                }
//            }
//        }
    }


    @Test
    public void testSecond() throws Exception {

        def builder = new CMDBuildQueryResolver(false)

//        builder.registerFactory('person', new CommonObjectFactory(nameField: 'person'))
//        builder.registerFactory('meta', new CommonObjectFactory(nameField: 'meta'))
//        builder.registerFactory('childs', new CommonObjectFactory(nameField: 'childs'))
//        builder.registerFactory('firstName', new CommonObjectFactory(nameField: 'firstName'))
//        builder.registerFactory('firstName', new CommonObjectFactory())
//        builder.registerFactory('lastName', new CommonObjectFactory())
//        builder.registerFactory('persons', new CommonObjectFactory(nameField: 'persons'))

//        def firstName = "first2Name"
//        def lastName = "lastName"
        /**
         * If result for field is a List then value will be List
         * If result for field is a Object then value will be Object
         * If field not exist then field will not made
         */
        def build = builder.persons { // First symbol should be registered factory or explicit method and must to be the query to cmdbuild data
            person(firstName: "Ivan") {
                firstName
                lastName
                person(firstName: 'Petr', predicate: { salary > 10 }) {
                    firstName {
                        meta {
                            description
                        }
                        value
                    }
                    lastName
                    salary
                }
                childs {
                    firstName
                    lastName
                    birthDate
                }
            }
            person(firstName: "Alexander") {
                firstName
                lastName
                birthDay
            }
            person(firstName: "Alexander") {
                firstName
                lastName
                birthDay
            }
        }

        println build



    }

    @Test
    public void testJSONBuilder() throws Exception {
        JsonBuilder builder = new JsonBuilder()
        builder.records {
            car {
                make 'Holden'
                year 2006
                country 'Australia'
                record {
                    type 'speed'
                    description 'production pickup truck with speed of 271kph'
                }
            }
        }
        String json = JsonOutput.prettyPrint(builder.toString())
        println json
    }
}
