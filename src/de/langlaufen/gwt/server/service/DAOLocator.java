package de.langlaufen.gwt.server.service;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class DAOLocator implements ServiceLocator {
	public DAOLocator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	// wird nur nach dem starten des Servers beim ersten Request aufgerufen
	public Object getInstance(final Class<?> clazz) {
		//return new PersonDAO();
		try {
			return clazz.newInstance();
		}
		catch (final InstantiationException e) {
			throw new RuntimeException(e);
		}
		catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
