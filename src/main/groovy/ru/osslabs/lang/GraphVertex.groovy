package ru.osslabs.lang

import groovy.transform.Canonical
import groovy.transform.Immutable

/**
 * Created by ikuchmin on 19.01.16.
 */

@Immutable
class GraphVertex {
    Integer gvId
    String name
    Map attributes

    def propertyMissing(String name) {
        return attributes[name]
    }
}
