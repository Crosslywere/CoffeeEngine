package com.crossly.coffee_engine.component;

/**
 * @author Jude Ogboru
 */
public abstract class SharedComponent implements Component {

	private int referenceCount = 0;
	private boolean deleted = false;

	@Override
	public final Component get() {
		referenceCount++;
		return this;
	}

	@Override
	public final void delete() {
		if (--referenceCount <= 0 && !deleted) {
			deleted = true;
			cleanup();
		}
	}

	protected abstract void cleanup();
}
