package de.langlaufen.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import de.langlaufen.gwt.shared.PersonProxy;
import de.langlaufen.gwt.shared.PersonRequest;
import de.langlaufen.gwt.shared.RequestFactoryX;

public class RequestFactoryExampleI implements EntryPoint {

	private static final EventBus eventBus = new SimpleEventBus();
	private static final RequestFactoryX requestFactory = GWT.create(RequestFactoryX.class);
	private PersonProxy personProxy = null;
	private PersonRequest personRequest = null;

	final TextBox idTextbox = new TextBox();
	final TextBox vornameTextbox = new TextBox();
	final TextBox nachnameTextbox = new TextBox();

	private void createNew() {
		// ein neues personProxy wird über die .create-Methode erzeugt. Es ist
		// damit per default mutable (editierbar). Da es dem hier verwendeten
		// personRequest zugeordnet ist
		// kann es auch nur in diesem Context für Requests verwendet werden.
		// Jedes entityProxy kann gleichzeitig nur von genau einem
		// requestContext in den Editierstatus versetzt werden
		// es ist somit nicht möglich, diese Objekt erneut in den EditMode zu
		// versetzen.
		personRequest = requestFactory.personRequest();
		personProxy = personRequest.create(PersonProxy.class);
	}

	private void populate(PersonProxy personProxy) {
		if (personProxy.getId() == null) {
			idTextbox.setText("<neu>");
		} else {
			idTextbox.setText(personProxy.getId().toString());
		}
		vornameTextbox.setText(personProxy.getVorname());
		nachnameTextbox.setText(personProxy.getNachname());
	}

	public void onModuleLoad() {
		requestFactory.initialize(eventBus);

		RootPanel rootPanel = RootPanel.get();
		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setSize("290px", "180px");
		rootPanel.add(decPanel);

		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setSize("290px", "180px");
		decPanel.add(absolutePanel);

		Label idLabel = new Label("Id");
		absolutePanel.add(idLabel, 0, 16);
		idLabel.setSize("52px", "24px");

		Label vornameLabel = new Label("Vorname");
		absolutePanel.add(vornameLabel, 0, 45);
		vornameLabel.setSize("52px", "24px");

		Label lblNachname = new Label("Nachname");
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
		this.populate(personProxy);

		Button newButton = new Button("neu");
		absolutePanel.add(newButton, 10, 129);
		newButton.setSize("60px", "32px");
		newButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				personRequest = requestFactory.personRequest();
				personProxy = personRequest.create(PersonProxy.class);
				populate(personProxy);
			}
		});

		Button loadPrevButton = new Button("<");
		absolutePanel.add(loadPrevButton, 80, 129);
		loadPrevButton.setSize("25px", "32px");
		loadPrevButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (personProxy.getId() != null) {
					// In diesem Fall wurde der personRequest bereits einmal
					// verwendet, es muss ein neuer personRequest generiert werden.
					// Eine Id kann das personProxy nur durch einen Aufruf
					// des Servers (mittels requestContext) erhalten haben.
					// Hat das entityProxy keine Id, so wurde es über .create erzeugt. Der
					// zugehörige personRequest wurde in diesem Fall noch nicht
					// verwendet. Das PersonProxy   m u s s   deshalb über den gleichen personRequest zum
					// Server gesendet werden.
					personRequest = requestFactory.personRequest();
				}
				personRequest.getPrevPerson(personProxy).fire(new Receiver<PersonProxy>() {
					@Override
					public void onSuccess(final PersonProxy prevPersonProxy) {
						if (prevPersonProxy == null) {
							createNew();
						} else {
							personProxy = prevPersonProxy;
						}
						populate(personProxy);
					}

					@Override
					public void onFailure(ServerFailure error) {
						// die onFailure-Implementierung ist optional und wird in den nachfolgenden Implementierungen weggelassen
						super.onFailure(error);
					}
				});
			}
		});

		Button loadNextButton = new Button(">");
		absolutePanel.add(loadNextButton, 115, 129);
		loadNextButton.setSize("25px", "32px");
		loadNextButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (personProxy.getId() != null) {
					// In diesem Fall wurde der personRequest bereits einmal
					// verwendet, es muss ein neuer personRequest generiert werden.
					// Eine Id kann das personProxy nur durch einen Aufruf
					// des Servers (mittels requestContext) erhalten haben.
					// Hat das entityProxy keine Id, so wurde es über .create erzeugt. Der
					// zugehörige personRequest wurde in diesem Fall noch nicht
					// verwendet. Das PersonProxy   m u s s   deshalb über den gleichen personRequest zum
					// Server gesendet werden.
					personRequest = requestFactory.personRequest();
				}
				personRequest.getNextPerson(personProxy).fire(new Receiver<PersonProxy>() {
					@Override
					public void onSuccess(final PersonProxy nextPersonProxy) {
						if (nextPersonProxy == null) {
							createNew();
						} else {
							personProxy = nextPersonProxy;
						}
						populate(personProxy);
					}
				});
			}
		});

		Button saveButton = new Button("speichern");
		absolutePanel.add(saveButton, 150, 129);
		saveButton.setSize("65px", "32px");
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (personProxy.getId() == null) {
					// personProxy wurde mit .create des RequestContext erzeugt
					// und ist somit per default mutable (editierbar).
					// Damit steht der aktuelle RequestContext (personRequest)
					// für einen Request zur Verfügung, das aktuelle EntityProxy (personProxy)
					// ist diesem RequestContext zugeordnet.
					// Keine weitere Aktion erforderlich
				} else {
					// personProxy wurde mittels einer Service-Methode vom
					// Server übermittelt und ist somit per default immutable
					// (readonly)
					// Ein neuer RequestContext muss erzeugt werden, da der ursprüngliche RequestContext
					// nicht mehr für einen neuen Request verfügbar ist. Anschließend muss das (bisherige) personProxy auf
					// editierbar gesetzt werden. Dabei entsteht ein neues personProxy, das zugleich dem neuen requestContext
					// zugewiesen ist
					// Die RequestFactory liefert einen neuen RequestContext, der im personRequest gespeichert wird.
					personRequest = requestFactory.personRequest();
					// durch Verwendung der .edit Methode entsteht aus dem alten personProxy das neuen personProxy
					personProxy = personRequest.edit(personProxy);
				}
				personProxy.setVorname(vornameTextbox.getText());
				personProxy.setNachname(nachnameTextbox.getText());
				personRequest.persistPerson(personProxy).fire(new Receiver<PersonProxy>() {
					// Object wird persistiert. Wichtig ist, dass dabei eine ID
					// vergeben wird (falls es sich um ein neues Object handelt)
					@Override
					public void onSuccess(final PersonProxy persistedPersonProxy) {
						personProxy = persistedPersonProxy;
						populate(personProxy);
					}
				});
			}
		});

		Button deleteButton = new Button("l&ouml;schen");
		absolutePanel.add(deleteButton, 225, 129);
		deleteButton.setSize("60px", "32px");
		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (personProxy.getId() != null) {
					// das Löschen ist nur erforderlich, wenn das PersonProxy bereits persistiert wurde
					// in diesem Fall benötigen wir auch einen neuen PersonRequest
					// die Methode 'deletePerson' liefert automatisch das nächste PersonProxy, dieses wird anschließend angezeigt
					personRequest = requestFactory.personRequest();
					personRequest.deletePerson(personProxy).fire(new Receiver<PersonProxy>() {
						@Override
						public void onSuccess(final PersonProxy nextPersonProxy) {
							if (nextPersonProxy == null) {
								createNew();
							} else {
								personProxy = nextPersonProxy;
							}
							populate(personProxy);
						}
					});
				}
			}
		});
	}
}
