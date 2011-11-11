package at.mikemitterer.gwt.rfsample.client;

import at.mikemitterer.gwt.rfsample.client.model.PersonProxy;
import at.mikemitterer.gwt.rfsample.server.db.PersonDAO;
import at.mikemitterer.gwt.rfsample.server.service.DAOServiceLocator;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;


public interface PersonRequestFactory extends RequestFactory {

	@Service(value = PersonDAO.class, locator = DAOServiceLocator.class)
	// ServiceLocator wird benötigt damit die Requstfactory eine Instanz für
	// das PersonDAO Objekt erzeugen kann.
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

	PersonRequestContext getPersonRequestContext();

}
