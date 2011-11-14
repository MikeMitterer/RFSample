package at.mikemitterer.gwt.rfsample.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.mikemitterer.gwt.rfsample.server.db.PersonDAO;
import at.mikemitterer.gwt.rfsample.server.domain.DatastoreObject;

import com.google.web.bindery.requestfactory.shared.Locator;

public class UniversalEntityLocator extends Locator<DatastoreObject, Long> {
	//GWT Logger: private static Logger	logger	= Logger.getLogger(UniversalEntityLocator.class.getName());
	private static Logger	logger	= LoggerFactory.getLogger(UniversalEntityLocator.class.getSimpleName());

	@Override
	public DatastoreObject create(final Class<? extends DatastoreObject> clazz) {
		try {
			final DatastoreObject obj = clazz.newInstance();
			logger.debug("new instance for " + clazz.getName() + " created (UniversalEntityLocator)");
			return obj;
		}
		catch (final InstantiationException e) {
			throw new RuntimeException(e);
		}
		catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public DatastoreObject find(final Class<? extends DatastoreObject> clazz, final Long id) {
		final PersonDAO daoBase = new PersonDAO();
		logger.debug("find DatastoreObject with ID: " + id);

		return daoBase.findPerson(id);
	}

	@Override
	public Class<DatastoreObject> getDomainType() {
		logger.debug("getDomainType was called");

		// Never called
		return null;
	}

	@Override
	public Long getId(final DatastoreObject domainObject) {
		logger.debug("returns ID:" + domainObject.getId());
		return domainObject.getId();
	}

	@Override
	public Class<Long> getIdType() {
		logger.debug("returns getIdType:" + Long.class.getSimpleName());
		return Long.class;
	}

	@Override
	public Object getVersion(final DatastoreObject domainObject) {
		logger.debug("returns Version:" + domainObject.getVersion());
		return domainObject.getVersion();
	}
}
