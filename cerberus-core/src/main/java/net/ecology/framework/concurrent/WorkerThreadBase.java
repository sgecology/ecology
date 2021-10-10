/**
 * 
 */
package net.ecology.framework.concurrent;

import java.util.concurrent.Callable;

import net.ecology.domain.Context;
import net.ecology.framework.component.BasisComp;

/**
 * @author ducbq
 *
 */
public abstract class WorkerThreadBase extends BasisComp implements Callable<Context> {
	private static final long serialVersionUID = -1054379747356010375L;

	private Context executionContext;

  protected WorkerThreadBase(Context executionContext) {
      this.executionContext = Context.builder().build();
      this.executionContext.putAll(executionContext);
  }

	@Override
	public Context call() throws Exception {
		return perform();
	}

	protected Context perform() {
		throw new RuntimeException("Not implemented yet!");
		//return this.executionContext;
	}
}
