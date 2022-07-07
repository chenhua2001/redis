package com.ch.cache.model;

public enum RemoveType {



    EVICT("驱逐策略")
    ,EXPIRE("过期"),
    ;

    private String msg;





    public String msg() {
        return msg;
    }

    RemoveType(String msg) {
        this.msg=msg;
    }
}
