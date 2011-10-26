package de.langlaufen.gwt.server.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.web.bindery.requestfactory.shared.Locator;

import de.langlaufen.gwt.server.domain.Person;

public class PersonLocator extends Locator<Person, Long> {
	private static HashMap<Long, Person>	personStorage	= new HashMap<Long, Person>();
	private static Long						idCounter		= new Long(0);

	public Person persistPerson(final Person persistPerson) {
		if (persistPerson.getId() == null) {
			idCounter++;
			persistPerson.setId(idCounter);
			persistPerson.setVersion(1);
		}
		else {
			persistPerson.setVersion(persistPerson.getVersion() + 1);
		}
		personStorage.put(persistPerson.getId(), persistPerson);
		return persistPerson;
	}

	public Person findPerson(final Long id) {
		return personStorage.get(id);
	}

	public Person getPrevPerson(final Person person) {
		if (person != null) {
			for (int i = personStorage.entrySet().size() - 1; i >= 0; i--) {
				@SuppressWarnings("unchecked")
				final Map.Entry<Long, Person> map = (Entry<Long, Person>) personStorage.entrySet().toArray()[i];
				if (person.getId() == null || map.getKey() < person.getId()) {
					// ohne id wird die letzte Person zur�ckgeliefert
					return map.getValue();
				}
			}
		}
		return null;
	}

	public Person getNextPerson(final Person person) {
		if (person != null) {
			for (final Map.Entry<Long, Person> map : personStorage.entrySet()) {
				if (person.getId() == null || map.getKey() > person.getId()) {
					// ohne id wird die erste Person zur�ckgeliefert
					return map.getValue();
				}
			}
		}
		return null;
	}

	public Person deletePerson(final Person person) {
		Person nextPerson = null;
		if (person != null) {
			nextPerson = getNextPerson(person);
			personStorage.remove(person.getId());
		}
		return nextPerson;
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
