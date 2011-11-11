package at.mikemitterer.gwt.rfsample.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class DAOServiceLocator implements ServiceLocator {
	private static Logger	logger	= LoggerFactory.getLogger(DAOServiceLocator.class.getSimpleName());

	public DAOServiceLocator() {
		logger.debug("Constructor DAOServiceLocator() called");
	}

	@Override
	// wird nur nach dem starten des Servers beim ersten Request aufgerufen
	public Object getInstance(final Class<?> clazz) {
		logger.debug("getInstance for " + clazz.getSimpleName());

		//return new PersonDAO();
		try {
			return clazz.newInstance(); // de.langlaufen.gwt.server.service.PersonDAO wird erstellt
		}
		catch (final InstantiationException e) {
			throw new RuntimeException(e);
		}
		catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
