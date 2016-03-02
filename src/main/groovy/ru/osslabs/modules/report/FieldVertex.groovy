package ru.osslabs.modules.report

import groovy.transform.Canonical

/**
 * Created by ikuchmin on 19.01.16.
 */
@Canonical
class FieldVertex {
    Integer gvId
    String name
    Map attributes

    FieldVertex(Map args) {
        this.name = args.name
        this.gvId = args.gvId
        this.attributes = new HashMap();
        this.attributes << ["objectClass": args.attributes?.objClass]
        this.attributes << ["type": args.attributes?.type]
    }

    def propertyMissing(String name) {
        return attributes[name]
    }
}
