package at.mikemitterer.gwt.rfsample.server.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.mikemitterer.gwt.rfsample.server.domain.Person;

public class PersonDAO {
	private static Logger					logger			= LoggerFactory.getLogger(PersonDAO.class.getSimpleName());

	private static HashMap<Long, Person>	personStorage	= new HashMap<Long, Person>();
	private static Long						idCounter		= new Long(0);

	public PersonDAO() {
		logger.debug("New instance of PersionDAO created");
	}

	public Person save(final Person person) {
		logger.debug("Save person object");

		return persistPerson(person);
	}

	public Person persistPerson(final Person persistPerson) {
		if (persistPerson.getId() == null) {
			idCounter++;
			persistPerson.setId(idCounter);
			persistPerson.setVersion(1);
			logger.debug("getID() for Person was null - so created a new ID and set the version to 1");
		}
		else {
			persistPerson.setVersion(persistPerson.getVersion() + 1);
			logger.debug("getID() returned: " + persistPerson.getId() + ", getVersion() became: " + persistPerson.getVersion());
		}
		personStorage.put(persistPerson.getId(), persistPerson);
		logger.debug("Person (" + persistPerson.getVorname() + " " + persistPerson.getNachname() + ") was made persistent.");

		return persistPerson;
	}

	public Person findPerson(final Long id) {
		logger.debug("Looking for Person with ID: " + id);
		return personStorage.get(id);
	}

	public Person getPrevPerson(final Person person) {
		if (person != null) {
			logger.debug("(getPrevPerson) Looking for Person with ID < " + person.getId());

			for (int i = personStorage.entrySet().size() - 1; i >= 0; i--) {
				@SuppressWarnings("unchecked")
				final Map.Entry<Long, Person> map = (Entry<Long, Person>) personStorage.entrySet().toArray()[i];
				if (person.getId() == null || map.getKey() < person.getId()) {
					// ohne id wird die letzte Person zurückgeliefert
					final Person prevPerson = map.getValue();
					if (prevPerson != null) {
						logger.debug("getPrevPerson returns: " + prevPerson.getVorname() + " " + prevPerson.getNachname());
					}
					else {
						logger.debug("getPrevPerson could not find a Person - so null will be returned");
					}
					return prevPerson;
				}
			}
		}

		logger.debug("getPrevPerson was called with no reference Person - so null will be returend");

		return null;
	}

	public Person getNextPerson(final Person person) {
		if (person != null) {
			logger.debug("(getNextPerson) Looking for Person with ID > " + person.getId());

			for (final Map.Entry<Long, Person> map : personStorage.entrySet()) {
				if (person.getId() == null || map.getKey() > person.getId()) {
					// ohne id wird die erste Person zurückgeliefert
					final Person nextPerson = map.getValue();
					if (nextPerson != null) {
						logger.debug("getNextPerson returns: " + nextPerson.getVorname() + " " + nextPerson.getNachname());
					}
					else {
						logger.debug("getNextPerson could not find a Person - so null will be returned");
					}
					return nextPerson;
				}
			}
		}

		logger.debug("getNextPerson was called with no reference Person - so null will be returend");
		return null;
	}

	public Person deletePerson(final Person person) {
		Person nextPerson = null;

		if (person != null) {
			logger.debug("delete Person with ID > " + person.getId());

			nextPerson = getNextPerson(person);
			personStorage.remove(person.getId());

			if (nextPerson != null) {
				logger.debug("delete Person with ID > " + person.getId());
			}
		}
		return nextPerson;
	}

}
