/**
 * 
 */
package net.ecology.framework.concurrent;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import net.ecology.common.CollectionsUtility;
import net.ecology.domain.Context;
import net.ecology.framework.component.BasisComp;
import net.ecology.global.GlobalConstants;

/**
 * @author ducbq
 *
 */
@Component
public class ExecutorServiceHelper extends BasisComp {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6717449694098903669L;

  @Inject
  private ApplicationContext appContext;

	private WorkerThreadBase invokeWorkerThread(Context executionContext) {
		Class<?> beanClass = (Class<?>)executionContext.get(GlobalConstants.KEY_CONTEXT_CLASS);
		return (WorkerThreadBase)appContext.getBean(beanClass, executionContext);
	}

	private ExecutorService getExecutorService() {
		return Executors.newFixedThreadPool(GlobalConstants.defaultNumberOfThreads);
	}

	public Future<Context> startThread(Context executionContext) throws InterruptedException{
		WorkerThreadBase workerThread = invokeWorkerThread(executionContext);
		ExecutorService executorService = getExecutorService();

		List<Future<Context>> futures = executorService.invokeAll(CollectionsUtility.newList(workerThread));
		return futures.get(0);//CompletableFuture.completedFuture(executionContext);
	}
}
