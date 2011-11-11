package at.mikemitterer.gwt.rfsample.server.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatastoreObject {
	private static Logger	logger	= LoggerFactory.getLogger(DatastoreObject.class.getSimpleName());

	@Id
	@Column(name = "id", nullable = false)
	private Long			id;

	@Version
	@Column(name = "version")
	private Integer			version	= 0;

	/**
	 * Auto-increment version # whenever persisted
	 */
	@PrePersist
	void onPersist() {

		this.version++;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		logger.debug("setId to " + id);
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(final Integer version) {
		logger.debug("setVersion to: " + version);
		this.version = version;
	}

}
