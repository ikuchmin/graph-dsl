package ru.osslabs.lang

import ru.osslabs.model.datasource.ExternalDataSource

/**
 * Created by ikuchmin on 10.01.16.
 */
class CMDBuildQueryResolver extends FactoryBuilderSupport {

    ExternalDataSource wrapperWS

    CMDBuildQueryResolver(boolean init) {
        super(init)
    }

    CMDBuildQueryResolver(Map args) {
        super(args.init ?: false)
        this.wrapperWS = args.wrapperWS
    }
//    def methodMissing(String name, def args) {
//        // resolve query
//        def proxyBuilder = new CMDBuildObjectBuilder()[args[-1]]
//        return proxyBuilder
//    }



    @Override
    Object invokeMethod(String methodName, Object args) {
        proxyBuilder = new CMDBuildObjectBuilder()
        return super.invokeMethod(methodName, args)
    }
}
