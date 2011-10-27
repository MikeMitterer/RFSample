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
		// Wenn Funktion nicht implementiert ist dann kommt eine Fehlermeldung dass die Funktion im TO nicht vorhanden ist (Person) - stimmt aber nicht
		Request<PersonProxy> save(PersonProxy person);

		Request<PersonProxy> persistPerson(final PersonProxy person);

		Request<PersonProxy> findPerson(Long id);

		Request<PersonProxy> getPrevPerson(final PersonProxy person);

		Request<PersonProxy> getNextPerson(final PersonProxy person);

		Request<PersonProxy> deletePerson(final PersonProxy person);
	}

	PersonRequestContext context();

}
