package com.crossly.coffee_engine.component;

import com.crossly.coffee_engine.entity.Entity;
import com.sun.management.GcInfo;

import javax.management.ObjectName;
import java.lang.management.GarbageCollectorMXBean;

public abstract class OwnedComponent extends Component {
    private Entity owner;

    public final Entity getOwner() {
        return owner;
    }

    public void setOwner(Entity owner) {
        if (!isOwned())
            this.owner = owner;
    }

    public final boolean isOwned() {
        return this.owner != null;
    }

    @Override
    public final Component get() {
        return this;
    }

    @Override
    public final void delete() {
        this.owner = null;
        cleanup();
    }

    protected abstract void cleanup();
}
