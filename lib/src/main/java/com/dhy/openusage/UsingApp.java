package com.dhy.openusage;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class UsingApp {
    public String appId;
    public String name;
    public String url;

    @Override
    public boolean equals(Object o) {
        return o instanceof UsingApp && o.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(appId)
                .append(name)
                .append(url)
                .toHashCode();
    }
}
