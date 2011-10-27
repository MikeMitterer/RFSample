package de.langlaufen.gwt.server.service;

import com.google.web.bindery.requestfactory.shared.Locator;

import de.langlaufen.gwt.server.domain.Person;

public class PersonLocator extends Locator<Person, Long> {

	public PersonLocator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Person create(final Class<? extends Person> clazz) {
		return new Person();
	}

	@Override
	public Person find(final Class<? extends Person> clazz, final Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<Person> getDomainType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getId(final Person domainObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<Long> getIdType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getVersion(final Person domainObject) {
		// TODO Auto-generated method stub
		return null;
	}
}
