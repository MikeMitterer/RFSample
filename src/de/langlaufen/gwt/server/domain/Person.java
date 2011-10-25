package de.langlaufen.gwt.server.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Person {

	private static Long						idCounter		= new Long(0);
	private static HashMap<Long, Person>	personStorage	= new HashMap<Long, Person>();

	private Long							id;
	private Integer							version;
	private String							vorname;
	private String							nachname;

	public String getVorname() {
		return this.vorname;
	}

	public void setVorname(final String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return this.nachname;
	}

	public void setNachname(final String nachname) {
		this.nachname = nachname;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(final Integer version) {
		this.version = version;
	}

	public static Person persistPerson(final Person persistPerson) {
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

	public static Person findPerson(final Long id) {
		return personStorage.get(id);
	}

	public static Person getPrevPerson(final Person person) {
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

	public static Person getNextPerson(final Person person) {
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

	public static Person deletePerson(final Person person) {
		Person nextPerson = null;
		if (person != null) {
			nextPerson = getNextPerson(person);
			personStorage.remove(person.getId());
		}
		return nextPerson;
	}
}
