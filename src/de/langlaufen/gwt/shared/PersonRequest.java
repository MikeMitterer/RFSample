package de.langlaufen.gwt.shared;


import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

import de.langlaufen.gwt.server.domain.Person;

@Service(Person.class)
public interface PersonRequest extends RequestContext {

	Request<PersonProxy> getPrevPerson(PersonProxy personProxy);
	Request<PersonProxy> getNextPerson(PersonProxy personProxy);
	Request<PersonProxy> persistPerson(PersonProxy personProxy);
	Request<PersonProxy> deletePerson(PersonProxy personProxy);
}
