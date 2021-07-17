package cn.misection.cvac.semantic;

import cn.misection.cvac.ast.type.ICvaType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MI6 root 1/13.
 */
public final class ClassBinding {
    private String parent;
    private Map<String, ICvaType> fieldMap;
    private Map<String, MethodType> methodMap;

    public ClassBinding(String parent) {
        this.setParent(parent);
        this.setFieldMap(new HashMap<>());
        this.setMethodMap(new HashMap<>());
    }

    public ClassBinding(String parent,
                        Map<String, ICvaType> fieldMap,
                        Map<String, MethodType> methodMap) {
        this.parent = parent;
        this.fieldMap = fieldMap;
        this.methodMap = methodMap;
    }

    public void putField(String literal, ICvaType type) {
        if (fieldMap.containsKey(literal)) {
            System.out.printf("duplicated class field: %s%n", literal);
            System.exit(1);
        } else {
            fieldMap.put(literal, type);
        }
    }

    public void putMethod(String literal, MethodType type) {
        if (methodMap.containsKey(literal)) {
            System.out.printf("duplicated class method: %s%n", literal);
            System.exit(1);
        } else {
            methodMap.put(literal, type);
        }
    }

    /**
     * // null for non-existing base class
     */
    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Map<String, ICvaType> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, ICvaType> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public Map<String, MethodType> getMethodMap() {
        return methodMap;
    }

    public void setMethodMap(Map<String, MethodType> methodMap) {
        this.methodMap = methodMap;
    }
}
