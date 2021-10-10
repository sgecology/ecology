/**
 * 
 */
package net.ecology.framework.concurrent;

import net.ecology.domain.Context;
import net.ecology.framework.component.BasisComp;

/**
 * @author ducbq
 *
 */
public abstract class WorkerThreadCore extends BasisComp implements Runnable {
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
