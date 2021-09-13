/**
 * 
 */
package net.ecology.listener;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ducbq
 *
 */
//@WebListener
public class CustomHttpSessionListener implements HttpSessionListener {
  private static final Logger LOG= LoggerFactory.getLogger(CustomHttpSessionListener.class);

  private final AtomicInteger activeSessionCount = new AtomicInteger();

  /**
   * This method will be called when session created
   * @param sessionEvent
   */
  @Override
  public void sessionCreated(HttpSessionEvent sessionEvent) {
      LOG.info("-------Incrementing Session Counter--------");
      activeSessionCount.incrementAndGet();
      LOG.info("-------New Session Created--------");
      updateActiveSessionCounter(sessionEvent);
  }

  /**
   * This method will be automatically called when session destroyed
   * @param sessionEvent
   */
  @Override
  public void sessionDestroyed(HttpSessionEvent sessionEvent) {
      LOG.info("-------Decrementing Session Counter--------");
      activeSessionCount.decrementAndGet();
      LOG.info("-------Session Destroyed--------");
      updateActiveSessionCounter(sessionEvent);
  }

  private void updateActiveSessionCounter(HttpSessionEvent sessionEvent){
      LOG.info("-------Set session in the context--------");
      sessionEvent.getSession().getServletContext()
              .setAttribute("activeSession", activeSessionCount.get());
      LOG.info("Total Active Session : {} ", activeSessionCount.get());
  }
}
