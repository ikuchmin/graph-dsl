package ru.osslabs.modules.report

import ru.osslabs.datasources.cmdbuild.CMDBuildWrapperWS
import spock.lang.Specification

/**
 * Created by ikuchmin on 10.01.16.
 */
class CMDBuildQueryResolverTest extends Specification {
    def "first symbol in builder can be used as query"() {
        given:
        CMDBuildQueryResolver queryResolver = new CMDBuildQueryResolver(wrapperWS: new CMDBuildWrapperWS())

        when:
        List persons = queryResolver.persons {
            firstName
        }

        then:
        persons*.firstName == ["Ivan", "Alexander", "Petr"]
    }

    def "first symbol in builder can be used as query with id"() {
        given:
        CMDBuildQueryResolver queryResolver = new CMDBuildQueryResolver(wrapperWS: new CMDBuildWrapperWS())

        when:
        def person = queryResolver.person(id: 1234) {
            firstName
        }

        then:
        person.firstName == "Ivan"

    }

    def "first symbol in builder can be used as explicit method"() {
        given:
        CMDBuildQueryResolver queryResolver = new CMDBuildQueryResolver()
        queryResolver.registerExplicitMethod("ivan", { new CMDBuildWrapperWS().getObject("Person", 1234)})

        when:
        def person = queryResolver.ivan {
            firstName
        }

        then:
        person.firstName == "Ivan"
    }

    def "first symbol in builder can be form by<Property> as implicit method"() {
        given:
        CMDBuildQueryResolver queryResolver = new CMDBuildQueryResolver(wrapperWS: new CMDBuildWrapperWS())

        when:
        def person = queryResolver.findPersonById(1234) {
            firstName
        }

        then:
        person.firstName == "Ivan"

        when:
        person = queryResolver.findPersonByFirstName("Ivan") {
            firstName
        }

        then:
        person.firstName == "Ivan"
    }

    def "first symbol in builder can be used as query with offset and pagination"() {
        given:
        CMDBuildQueryResolver queryResolver = new CMDBuildQueryResolver(wrapperWS: new CMDBuildWrapperWS())

        when:
        def person = queryResolver.persons(max: 2, offset: 1) {
            firstName
        }

        then:
        persons*.firstName == ["Alexander", "Petr"]
    }

    def "first symbol in builder can be used as query with predicate on fields object"() {
        given:
        CMDBuildQueryResolver queryResolver = new CMDBuildQueryResolver(wrapperWS: new CMDBuildWrapperWS())

        when:
        def person = queryResolver.persons(predicate: { it.age < 21 }) {
            firstName
        }

        then:
        persons*.firstName == ["Petr"]
    }

    def "first symbol in builder can be used as query with predicate on fields nested object"() {
        given:
        CMDBuildQueryResolver queryResolver = new CMDBuildQueryResolver(wrapperWS: new CMDBuildWrapperWS())

        when:
        def person = queryResolver.persons(predicate: { it.children*.age > 12 }) {
            firstName
        }

        then:
        persons*.firstName == ["Alexander"]
    }
}
