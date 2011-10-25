package de.langlaufen.gwt.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(de.langlaufen.gwt.server.domain.Person.class)
public interface PersonProxy extends EntityProxy {
		
	String getNachname();

	String getVorname();

	void setNachname(String nachname);

	void setVorname(String vorname);
	
	public Long getId();

	public Integer getVersion();
}
