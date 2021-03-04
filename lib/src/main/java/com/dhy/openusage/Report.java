package com.dhy.openusage;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

public class Report extends UsingApp {
    public List<Honor> honors;

    @Override
    public boolean equals(Object o) {
        return o instanceof Report && o.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(honors)
                .toHashCode();
    }
}
