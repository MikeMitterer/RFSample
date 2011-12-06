package at.mikemitterer.gwt.rfsample.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import at.mikemitterer.gwt.module.log.SingleLineConsoleLogHandler;
import at.mikemitterer.gwt.module.log.SingleLineSystemLogHandler;
import at.mikemitterer.gwt.rfsample.client.PersonRequestFactory.PersonRequestContext;
import at.mikemitterer.gwt.rfsample.client.model.PersonProxy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class RFSample implements EntryPoint {
	private static Logger						logger					= Logger.getLogger("RFSample");
	private static final EventBus				eventBus				= new SimpleEventBus();
	private static final PersonRequestFactory	requestFactory			= GWT.create(PersonRequestFactory.class);
	private PersonProxy							personProxy				= null;
	private PersonRequestContext				personRequestContext	= null;

	final TextBox								idTextbox				= new TextBox();
	final TextBox								vornameTextbox			= new TextBox();
	final TextBox								nachnameTextbox			= new TextBox();

	public RFSample() {
		// http://grepcode.com/file/repo1.maven.org/maven2/com.google.gwt/gwt-user/2.4.0/com/google/gwt/logging/client/LogConfiguration.java?av=f
		Logger.getLogger("").addHandler(new SingleLineSystemLogHandler());
		Logger.getLogger("").addHandler(new SingleLineConsoleLogHandler());
	}

	private void createNew() {
		// ein neues personProxy wird über die .create-Methode erzeugt. Es ist
		// damit per default mutable (editierbar). Da es dem hier verwendeten
		// personRequest zugeordnet ist
		// kann es auch nur in diesem Context für Requests verwendet werden.
		// Jedes entityProxy kann gleichzeitig nur von genau einem
		// requestContext in den Editierstatus versetzt werden
		// es ist somit nicht möglich, diese Objekt erneut in den EditMode zu
		// versetzen.
		personRequestContext = requestFactory.getPersonRequestContext();
		personProxy = personRequestContext.create(PersonProxy.class);

		logger.log(Level.INFO, "Created new context");
	}

	private void populateFormFields(final PersonProxy personProxy) {
		if (personProxy.getId() == null) {
			idTextbox.setText("<neu>");
		}
		else {
			idTextbox.setText(personProxy.getId().toString());
		}
		vornameTextbox.setText(personProxy.getVorname());
		nachnameTextbox.setText(personProxy.getNachname());

		logger.log(Level.INFO, "Formfields set...");
	}

	@Override
	public void onModuleLoad() {
		requestFactory.initialize(eventBus);
		logger.log(Level.INFO, "RequestFactory initialized...");

		final RootPanel rootPanel = RootPanel.get("root");
		final DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setSize("300px", "180px");
		rootPanel.add(decPanel);

		final AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setSize("290px", "180px");
		decPanel.add(absolutePanel);

		final Label idLabel = new Label("Id:");
		absolutePanel.add(idLabel, 0, 16);
		idLabel.setSize("52px", "24px");

		final Label vornameLabel = new Label("Firstname:");
		absolutePanel.add(vornameLabel, 0, 45);
		vornameLabel.setSize("52px", "24px");

		final Label lblNachname = new Label("Lastname:");
		absolutePanel.add(lblNachname, 0, 74);
		lblNachname.setSize("62px", "24px");

		idTextbox.setEnabled(false);
		absolutePanel.add(idTextbox, 71, 16);
		idTextbox.setSize("50px", "12px");

		absolutePanel.add(vornameTextbox, 71, 45);
		vornameTextbox.setSize("206px", "12px");

		absolutePanel.add(nachnameTextbox, 71, 74);
		nachnameTextbox.setSize("206px", "12px");

		this.createNew();
		this.populateFormFields(personProxy);

		final Button newButton = new Button("new");
		absolutePanel.add(newButton, 10, 129);
		newButton.setSize("60px", "32px");
		newButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				personRequestContext = requestFactory.getPersonRequestContext();
				personProxy = personRequestContext.create(PersonProxy.class);
				logger.log(Level.INFO, "PersonProxy created in ClickHandler (new)");

				populateFormFields(personProxy);
			}
		});

		final Button loadPrevButton = new Button("<");
		absolutePanel.add(loadPrevButton, 80, 129);
		loadPrevButton.setSize("25px", "32px");
		loadPrevButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (personProxy.getId() != null) {
					// In diesem Fall wurde der personRequest bereits einmal
					// verwendet, es muss ein neuer personRequest generiert werden.
					// Eine Id kann das personProxy nur durch einen Aufruf
					// des Servers (mittels requestContext) erhalten haben.
					// Hat das entityProxy keine Id, so wurde es über .create erzeugt. Der
					// zugehörige personRequest wurde in diesem Fall noch nicht
					// verwendet. Das PersonProxy   m u s s   deshalb über den gleichen personRequest zum
					// Server gesendet werden.
					personRequestContext = requestFactory.getPersonRequestContext();
					logger.log(Level.INFO, "( ClickHandler for < ) personProxy.getID is not null (ID was already set on server-side), a new RequestContext was created");
				}
				personRequestContext.getPrevPerson(personProxy).fire(new Receiver<PersonProxy>() {
					@Override
					public void onSuccess(final PersonProxy prevPersonProxy) {
						if (prevPersonProxy == null) {
							createNew();
						}
						else {
							personProxy = prevPersonProxy;
							logger.log(Level.INFO, "prevPersonProxy became personProxy");
						}
						populateFormFields(personProxy);
					}

					@Override
					public void onFailure(final ServerFailure error) {
						// die onFailure-Implementierung ist optional und wird in den nachfolgenden Implementierungen weggelassen
						logger.log(Level.SEVERE, "onFailur was called");
						super.onFailure(error);
					}
				});
			}
		});

		final Button loadNextButton = new Button(">");
		absolutePanel.add(loadNextButton, 115, 129);
		loadNextButton.setSize("25px", "32px");
		loadNextButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (personProxy.getId() != null) {
					// In diesem Fall wurde der personRequest bereits einmal
					// verwendet, es muss ein neuer personRequest generiert werden.
					// Eine Id kann das personProxy nur durch einen Aufruf
					// des Servers (mittels requestContext) erhalten haben.
					// Hat das entityProxy keine Id, so wurde es über .create erzeugt. Der
					// zugehörige personRequest wurde in diesem Fall noch nicht
					// verwendet. Das PersonProxy   m u s s   deshalb über den gleichen personRequest zum
					// Server gesendet werden.
					personRequestContext = requestFactory.getPersonRequestContext();

					logger.log(Level.INFO, "( ClickHandler for > ) personProxy.getID is not null (ID was already set on server-side), a new RequestContext was created");
				}
				personRequestContext.getNextPerson(personProxy).fire(new Receiver<PersonProxy>() {
					@Override
					public void onSuccess(final PersonProxy nextPersonProxy) {
						if (nextPersonProxy == null) {
							createNew();
						}
						else {
							personProxy = nextPersonProxy;
						}
						populateFormFields(personProxy);
					}
				});
			}
		});

		final Button saveButton = new Button("save");
		absolutePanel.add(saveButton, 150, 129);
		saveButton.setSize("65px", "32px");
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				final Long id = personProxy.getId();

				if (personProxy.getId() == null) {
					// personProxy wurde mit .create des RequestContext erzeugt
					// und ist somit per default mutable (editierbar).
					// Damit steht der aktuelle RequestContext (personRequest)
					// für einen Request zur Verfügung, das aktuelle EntityProxy (personProxy)
					// ist diesem RequestContext zugeordnet.
					// Keine weitere Aktion erforderlich
				}
				else {
					// personProxy wurde mittels einer Service-Methode vom
					// Server übermittelt und ist somit per default immutable
					// (readonly)
					// Ein neuer RequestContext muss erzeugt werden, da der ursprüngliche RequestContext
					// nicht mehr für einen neuen Request verfügbar ist. Anschliessend muss das (bisherige) personProxy auf
					// editierbar gesetzt werden. Dabei entsteht ein neues personProxy, das zugleich dem neuen requestContext
					// zugewiesen ist
					// Die RequestFactory liefert einen neuen RequestContext, der im personRequest gespeichert wird.
					personRequestContext = requestFactory.getPersonRequestContext();
					// durch Verwendung der .edit Methode entsteht aus dem alten personProxy das neuen personProxy
					personProxy = personRequestContext.edit(personProxy);

					logger.log(Level.INFO, "( ClickHandler for save ) personProxy.getID is not null (ID was already set on server-side), a new RequestContext was created");
				}
				personProxy.setVorname(vornameTextbox.getText());
				personProxy.setNachname(nachnameTextbox.getText());
				logger.log(Level.INFO, "fire persistPerson");

				personRequestContext.persistPerson(personProxy).fire(new Receiver<PersonProxy>() {
					// Object wird persistiert. Wichtig ist, dass dabei eine ID
					// vergeben wird (falls es sich um ein neues Object handelt)
					@Override
					public void onSuccess(final PersonProxy persistedPersonProxy) {
						personProxy = persistedPersonProxy;
						populateFormFields(personProxy);
						logger.log(Level.INFO, "Data was saved on server");
					}

					@Override
					public void onConstraintViolation(final Set<ConstraintViolation<?>> violations) {
						Window.alert(createViolationMessage(violations));
						logger.log(Level.SEVERE, "ConstrainedViolation - refere to at.mikemitterer.gwt.rfsample.server.domain.Person for more infos");

						createNew(); // RequestContext my only be used once, after calling "fire" RC is invalid, and we need a new one.
						// if we don't create a new one we get the onConstraintViolation function called multiple times.
						logger.log(Level.INFO, "After fiering a call to the server the RequestContext is not valid anymore, a new RequestContext was created");
					}
				});
			}
		});

		final Button deleteButton = new Button("delete");
		absolutePanel.add(deleteButton, 225, 129);
		deleteButton.setSize("60px", "32px");
		deleteButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (personProxy.getId() != null) {
					// das Löschen ist nur erforderlich, wenn das PersonProxy bereits persistiert wurde
					// in diesem Fall benötigen wir auch einen neuen PersonRequest
					// die Methode 'deletePerson' liefert automatisch das nächste PersonProxy, dieses wird anschließend angezeigt
					personRequestContext = requestFactory.getPersonRequestContext();
					personRequestContext.deletePerson(personProxy).fire(new Receiver<PersonProxy>() {
						@Override
						public void onSuccess(final PersonProxy nextPersonProxy) {
							if (nextPersonProxy == null) {
								createNew();
								logger.log(Level.INFO, "delete came back with 'null' ");
							}
							else {
								personProxy = nextPersonProxy;
							}
							populateFormFields(personProxy);
						}
					});
				}
			}
		});
	}

	//---------------------------------------------------------------------------------------------
	// private
	//---------------------------------------------------------------------------------------------

	private String createViolationMessage(final Set<ConstraintViolation<?>> violations) {
		String message = "";

		for (final ConstraintViolation<?> violation : violations) {
			final List<String> pathsegments = new ArrayList<String>();

			final String propertypath = violation.getPropertyPath().toString();
			message += propertypath + " " + violation.getMessage() + "\n";

			//for (final Path.Node node : violation.getPropertyPath()) {
			//	pathsegments.add(node.getName());
			//}
			//message += Joiner.on(".").join(pathsegments) + " " + violation.getMessage() + "\n";
		}
		return message;
	}
}
