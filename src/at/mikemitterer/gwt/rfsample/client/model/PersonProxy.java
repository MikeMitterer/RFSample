package at.mikemitterer.gwt.rfsample.client.model;

import at.mikemitterer.gwt.rfsample.server.domain.Person;
import at.mikemitterer.gwt.rfsample.server.service.UniversalEntityLocator;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;


@ProxyFor(value = Person.class, locator = UniversalEntityLocator.class)
public interface PersonProxy extends EntityProxy {

	String getNachname();

	String getVorname();

	void setNachname(String nachname);

	void setVorname(String vorname);

	public Long getId();

	public Integer getVersion();
}
