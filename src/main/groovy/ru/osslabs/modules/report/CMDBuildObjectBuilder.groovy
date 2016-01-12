package ru.osslabs.modules.report
/**
 * Created by ikuchmin on 03.01.16.
 */
class CMDBuildObjectBuilder extends FactoryBuilderSupport {
    private Factory defaultObjectFactory = new CommonObjectFactory();

    CMDBuildObjectBuilder() {
        this(false)
    }

    CMDBuildObjectBuilder(boolean init) {
        super(init)
        this.propertyMissingDelegate = {invokeMethod(it)}
    }

    @Override
    protected Factory resolveFactory(Object name, Map attributes, Object value) {
        def factory = super.resolveFactory(name, attributes, value)
        if (factory != null) {
            return factory
        }
        defaultObjectFactory;
    }
}