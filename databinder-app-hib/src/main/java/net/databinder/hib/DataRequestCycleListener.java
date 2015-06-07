package net.databinder.hib;

import java.util.HashSet;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataRequestCycleListener implements IRequestCycleListener {
	
	private static final Logger log = LoggerFactory.getLogger(DataRequestCycleListener.class);

	/** Keys for session factories that have been opened for this request */ 
	protected HashSet<Object> keys = new HashSet<Object>();

	
	/** Roll back active transactions and close session. */
	protected void closeSession(Object key) {
		Session sess = Databinder.getHibernateSession(key);
		
		if (sess.isOpen())
			try {
				if (sess.getTransaction().isActive()) {
					log.debug("Rolling back uncomitted transaction.");
					sess.getTransaction().rollback();
				}
			} finally {
				sess.close();
			}
	}
	
	/**
	 * Open a session and begin a transaction for the keyed session factory.
	 * @param key object, or null for the default factory
	 * @return newly opened session
	 */
	protected org.hibernate.Session openHibernateSession(Object key) {
		org.hibernate.Session sess = Databinder.getHibernateSessionFactory(key).openSession();
		sess.beginTransaction();
		ManagedSessionContext.bind(sess);
		keys.add(key);
		return sess;
	}

	@Override
	public void onBeginRequest(RequestCycle cycle) {
		openHibernateSession(null);
		for (Object key : keys) {
			openHibernateSession(key); 
		}		
	}

	@Override
	public void onEndRequest(RequestCycle cycle) {
		SessionFactory sf = Databinder.getHibernateSessionFactory(null);
		if (ManagedSessionContext.hasBind(sf)) {
			closeSession(null);
			ManagedSessionContext.unbind(sf);
		}
		for (Object key : keys) {
			SessionFactory sf2 = Databinder.getHibernateSessionFactory(key);
			if (ManagedSessionContext.hasBind(sf2)) {
				closeSession(key);
				ManagedSessionContext.unbind(sf2);
			}
		}
	}

	@Override
	public void onDetach(RequestCycle cycle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestHandlerResolved(RequestCycle cycle,
			IRequestHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestHandlerScheduled(RequestCycle cycle,
			IRequestHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public IRequestHandler onException(RequestCycle cycle, Exception ex) {
		onEndRequest(cycle);
		onBeginRequest(cycle);
		return null;
	}

	@Override
	public void onExceptionRequestHandlerResolved(RequestCycle cycle,
			IRequestHandler handler, Exception exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestHandlerExecuted(RequestCycle cycle,
			IRequestHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUrlMapped(RequestCycle cycle, IRequestHandler handler, Url url) {
		// TODO Auto-generated method stub

	}

}
