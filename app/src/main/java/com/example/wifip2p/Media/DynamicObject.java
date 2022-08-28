package com.example.wifip2p.Media;

import java.io.Serializable;

public class DynamicObject implements Serializable {

    Object object;
    String objectType;

    public DynamicObject () {
    }

    public DynamicObject(Object object, String objectType) {
        this.object = object;
        this.objectType = objectType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}
