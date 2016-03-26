package ru.osslabs.lang

/**
 * Created by ikuchmin on 03.01.16.
 */
class CommonObjectFactory extends AbstractFactory{

    String nameField

    @Override
    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        println "ObjectFactory setChild $nameField $parent $child"
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        println "newInstace $nameField $builder - $name - $value - $attributes"
        return new Expando();
    }

//    @Override
//    boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
//        println "onNodeChildren $node $childContent"
//        return true;
//    }
//
    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        println "ObjectFactory setParent $nameField $parent $child"
        super.setParent(builder, parent, child)
    }
}
