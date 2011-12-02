package at.mikemitterer.gwt.rfsample.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class Person extends DatastoreObject {
	private static Logger	logger	= LoggerFactory.getLogger(Person.class.getSimpleName());

	@Size(min = 3, max = 30)
	@Column(name = "vornameX")
	private String			vorname;

	@Size(min = 3, max = 30)
	@Column(name = "nachnameX")
	private String			nachname;

	public Person() {
		logger.debug("Constructor Person");
	}

	public String getVorname() {
		return this.vorname;
	}

	public void setVorname(final String vorname) {
		logger.debug("setVorname to: " + vorname);
		this.vorname = vorname;
	}

	public String getNachname() {
		return this.nachname;
	}

	public void setNachname(final String nachname) {
		logger.debug("setNachname to: " + nachname);
		this.nachname = nachname;
	}
}
