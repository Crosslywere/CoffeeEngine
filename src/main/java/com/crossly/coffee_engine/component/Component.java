package com.crossly.coffee_engine.component;

public abstract class Component {

    private int referenceCount = 0;
    private boolean deleted = false;

    public final Component get() {
        referenceCount ++;
        return this;
    }

    public final void delete() {
        if (--referenceCount <= 0 && !deleted) {
            deleted = true;
            cleanup();
        }
    }

    protected abstract void cleanup();
}
