package de.langlaufen.gwt.client;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;

import de.langlaufen.gwt.client.model.PersonProxy;
import de.langlaufen.gwt.server.service.DAOLocator;
import de.langlaufen.gwt.server.service.PersonDAO;

public interface RequestFactoryX extends RequestFactory {

	@Service(value = PersonDAO.class, locator = DAOLocator.class)
	// DAOLocator erzeugt das PersonDAO objekt
	public interface PersonRequestContext extends RequestContext {
		// Diese Funktionen müssen in PersonDAO implementiert werden.
		Request<PersonProxy> findById(Long id);

		Request<PersonProxy> save(PersonProxy person);
	}

	PersonRequestContext context();

}
