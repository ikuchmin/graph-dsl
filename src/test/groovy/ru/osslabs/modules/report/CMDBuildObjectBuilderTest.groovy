package ru.osslabs.modules.report

import ru.osslabs.model.datasource.DataObject
import spock.lang.Specification

/**
 * Created by ikuchmin on 12.01.16.
 */
class CMDBuildObjectBuilderTest extends Specification {

    def "first symbol in builder can be used as field in netsted object"() {
        given:
        CMDBuildObjectBuilder builder = new CMDBuildObjectBuilder(dataObject: new DataObject())

        when:
        List persons = builder.persons {
            firstName
        }

        then:
        persons*.firstName == ["Ivan", "Alexander", "Petr"]
    }

    def "any field should has meta-object"() {
        given:
        CMDBuildObjectBuilder builder = new CMDBuildObjectBuilder(dataObject: new DataObject())

        when:
        List persons = builder.persons {
            firstName {
                meta {
                    description
                }
                value
            }
        }

        then:
        persons*.firstName.value == ["Ivan", "Alexander", "Petr"]
        persons*.firstName.meta.description == ["Имя", "Имя", "Имя"]
    }

    def "object might include nested object"() {
        given:
        CMDBuildObjectBuilder builder = new CMDBuildObjectBuilder(dataObject: new DataObject())

        when:
        List persons = builder.persons {
            firstName
            mother {
                firstName
            }
        }

        then:
        persons*.firstName.value == ["Ivan", "Alexander", "Petr"]
        persons*.mother.firstName == ["Elena", "Mila", "Olga"]
    }

    def "object might include collection nested objects"() {
        given:
        CMDBuildObjectBuilder builder = new CMDBuildObjectBuilder(dataObject: new DataObject())

        when:
        List persons = builder.persons {
            firstName
            children {
                firstName
            }
        }

        then:
        persons*.firstName == ["Ivan", "Alexander", "Petr"]
        persons*.children*.firstName == ["Ivan II", "Mike", "Kate", "Ilya", "Anton", "Petr II"]
    }

    def "any field-collection can be used with implicit predicate that has field name and value"() {
        given:
        CMDBuildObjectBuilder builder = new CMDBuildObjectBuilder(dataObject: new DataObject())

        when:
        List persons = builder.persons(lastName: "Ivanov") {
            firstName
        }

        then:
        persons*.firstName == ["Ivan", "Alexander"]

        when:
        persons = builder.persons {
            firstName
            children(firstName: "Ivan") {
                firstName
            }
        }

        then:
        persons*.firstName == ["Ivan", "Alexander", "Petr"]
        persons*.children*.firstName == ["Ivan", "Ivan"]
    }

    def "any field-collection can be used with explicit predicate that has closure"() {
        given:
        CMDBuildObjectBuilder builder = new CMDBuildObjectBuilder(dataObject: new DataObject())

        when:
        List persons = builder.persons(predicate: { it.age < 21 }) {
            firstName
        }

        then:
        persons*.firstName == ["Petr"]

        when:
        persons = builder.persons(predicate: { it.children*.age > 12 }) {
            firstName
        }

        then:
        persons*.firstName == ["Alexander"]

        when:
        persons = builder.persons {
            firstName
            children(predicate: { it.age > 12 }) {
                firstName
            }
        }

        then:
        persons*.firstName == ["Ivan", "Alexander", "Petr"]
        persons*.children*.firstName == ["Kate", "Ilya"]
    }

    def "builder might include reference on external fragments"() {
        given:
        CMDBuildObjectBuilder builder = new CMDBuildObjectBuilder(dataObject: new DataObject())
        def fragmentMother = { firstName }
        def fragmentChild = { firstName }

        when:
        List persons = builder.persons {
            firstName
            mother fragmentMother
        }

        then:
        persons*.firstName.value == ["Ivan", "Alexander", "Petr"]
        persons*.mother.firstName == ["Elena", "Mila", "Olga"]

        when:
        persons = builder.persons {
            firstName
            children fragmentChild
        }

        then:
        persons*.firstName == ["Ivan", "Alexander", "Petr"]
        persons*.children*.firstName == ["Ivan II", "Mike", "Kate", "Ilya", "Anton", "Petr II"]
    }

    // TODO: Test which use specific Factory for create concrete type-object should be create
}
