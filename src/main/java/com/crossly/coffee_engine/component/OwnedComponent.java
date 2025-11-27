package com.crossly.coffee_engine.component;

import com.crossly.coffee_engine.entity.Entity;

/**
 * @author Jude Ogboru
 */
public abstract class OwnedComponent implements Component {
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
