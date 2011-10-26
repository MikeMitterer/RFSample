package de.langlaufen.gwt.client.model;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import de.langlaufen.gwt.server.domain.Person;
import de.langlaufen.gwt.server.service.PersonLocator;

@ProxyFor(value = Person.class, locator = PersonLocator.class)
public interface PersonProxy extends EntityProxy {

	String getNachname();

	String getVorname();

	void setNachname(String nachname);

	void setVorname(String vorname);

	public Long getId();

	public Integer getVersion();
}
