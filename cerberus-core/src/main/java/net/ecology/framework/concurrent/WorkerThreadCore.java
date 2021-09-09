/**
 * 
 */
package net.ecology.framework.concurrent;

import net.ecology.framework.component.ComponentRoot;
import net.ecology.model.Context;

/**
 * @author ducbq
 *
 */
public abstract class WorkerThreadCore extends ComponentRoot implements Runnable {
	private static final long serialVersionUID = -2857158059074111900L;

	protected Context executionContext;

	public WorkerThreadCore(Context executionContext) {
		this.executionContext = Context.builder().build();
		this.executionContext.putAll(executionContext);
	}

	@Override
	public void run() {
		perform();
	}

	protected abstract void perform();
}
