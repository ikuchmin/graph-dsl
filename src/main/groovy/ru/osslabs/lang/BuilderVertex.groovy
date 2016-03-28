package ru.osslabs.lang

import groovy.transform.Canonical
import groovy.transform.CompileStatic

/**
 * Created by ikuchmin on 26.03.16.
 */
@Canonical
class BuilderVertex extends GraphVertex {
    final Integer gvId
    final Map attributes

    BuilderVertex(String name, Integer gvId, Map attributes) {
        super(name)
        this.gvId = gvId
        this.attributes = attributes
    }

    BuilderVertex(HashMap args) {
        this(args.name, args.gvId, args.attributes)
    }

    def propertyMissing(String name) {
        return attributes[name]
    }
}
