package de.langlaufen.gwt.server.domain;

public class Person {
	private Long	id;
	private Integer	version;
	private String	vorname;
	private String	nachname;

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

}
