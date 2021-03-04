package com.dhy.openusage;

public class Honor {
    public String name;
    public String url;

    @Override
    public boolean equals(Object o) {
        return o instanceof Honor && o.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return (name + url).hashCode();
    }
}
