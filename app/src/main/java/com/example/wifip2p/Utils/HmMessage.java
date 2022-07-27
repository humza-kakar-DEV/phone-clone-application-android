package com.example.wifip2p.Utils;

import java.io.Serializable;

public class HmMessage implements Serializable {

    public String name;

    public HmMessage(String name) {
        this.name = name;
    }
}
