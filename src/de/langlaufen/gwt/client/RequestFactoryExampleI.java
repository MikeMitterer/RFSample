package de.langlaufen.gwt.client;

import java.util.logging.Level;
import java.util.logging.Logger;

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

import de.langlaufen.gwt.client.RequestFactoryX.PersonRequestContext;
import de.langlaufen.gwt.client.model.PersonProxy;

public class RequestFactoryExampleI implements EntryPoint {
	private static Logger					logger			= Logger.getLogger("NameOfYourLogger");
	private static final EventBus			eventBus		= new SimpleEventBus();
	private static final RequestFactoryX	requestFactory	= GWT.create(RequestFactoryX.class);
	//private PersonProxy						personProxy		= null;
	//private PersonRequestContext			context			= null;
	//private PersonRequest personRequest = null;

	final TextBox							idTextbox		= new TextBox();
	final TextBox							vornameTextbox	= new TextBox();
	final TextBox							nachnameTextbox	= new TextBox();

	private PersonProxy createNew() {
		// ein neues personProxy wird �ber die .create-Methode erzeugt. Es ist
		// damit per default mutable (editierbar). Da es dem hier verwendeten
		// personRequest zugeordnet ist
		// kann es auch nur in diesem Context f�r Requests verwendet werden.
		// Jedes entityProxy kann gleichzeitig nur von genau einem
		// requestContext in den Editierstatus versetzt werden
		// es ist somit nicht m�glich, diese Objekt erneut in den EditMode zu
		// versetzen.
		final PersonRequestContext context = requestFactory.context();

		//personRequest = requestFactory.personRequest();
		final PersonProxy personProxy = context.create(PersonProxy.class);
		return personProxy;
	}

	private void populate(final PersonProxy personProxy) {
		if (personProxy.getId() == null) {
			idTextbox.setText("<neu>");
		}
		else {
			idTextbox.setText(personProxy.getId().toString());
		}
		vornameTextbox.setText(personProxy.getVorname());
		nachnameTextbox.setText(personProxy.getNachname());
	}

	@Override
	public void onModuleLoad() {
		requestFactory.initialize(eventBus);

		final RootPanel rootPanel = RootPanel.get();
		final DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setSize("290px", "180px");
		rootPanel.add(decPanel);

		final AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setSize("290px", "180px");
		decPanel.add(absolutePanel);

		final Label idLabel = new Label("Id");
		absolutePanel.add(idLabel, 0, 16);
		idLabel.setSize("52px", "24px");

		final Label vornameLabel = new Label("Vorname");
		absolutePanel.add(vornameLabel, 0, 45);
		vornameLabel.setSize("52px", "24px");

		final Label lblNachname = new Label("Nachname");
		absolutePanel.add(lblNachname, 0, 74);
		lblNachname.setSize("62px", "24px");

		idTextbox.setEnabled(false);
		absolutePanel.add(idTextbox, 71, 16);
		idTextbox.setSize("50px", "12px");

		absolutePanel.add(vornameTextbox, 71, 45);
		vornameTextbox.setSize("206px", "12px");

		absolutePanel.add(nachnameTextbox, 71, 74);
		nachnameTextbox.setSize("206px", "12px");

		//this.createNew();
		//this.populate(personProxy);

		final Button newButton = new Button("neu");
		absolutePanel.add(newButton, 10, 129);
		newButton.setSize("60px", "32px");

		final Button loadPrevButton = new Button("<");
		absolutePanel.add(loadPrevButton, 80, 129);
		loadPrevButton.setSize("25px", "32px");

		final Button loadNextButton = new Button(">");
		absolutePanel.add(loadNextButton, 115, 129);
		loadNextButton.setSize("25px", "32px");

		final Button saveButton = new Button("speichern");
		absolutePanel.add(saveButton, 150, 129);
		saveButton.setSize("65px", "32px");
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				//PersonProxy personProxy = createNew();
				PersonRequestContext context = requestFactory.context();
				PersonProxy personProxy = context.create(PersonProxy.class);

				if (personProxy.getId() == null) {
					// personProxy wurde mit .create des RequestContext erzeugt
					// und ist somit per default mutable (editierbar).
					// Damit steht der aktuelle RequestContext (personRequest)
					// f�r einen Request zur Verf�gung, das aktuelle EntityProxy (personProxy)
					// ist diesem RequestContext zugeordnet.
					// Keine weitere Aktion erforderlich
				}
				else {
					// personProxy wurde mittels einer Service-Methode vom
					// Server �bermittelt und ist somit per default immutable
					// (readonly)
					// Ein neuer RequestContext muss erzeugt werden, da der urspr�ngliche RequestContext
					// nicht mehr f�r einen neuen Request verf�gbar ist. Anschlie�end muss das (bisherige) personProxy auf
					// editierbar gesetzt werden. Dabei entsteht ein neues personProxy, das zugleich dem neuen requestContext
					// zugewiesen ist
					// Die RequestFactory liefert einen neuen RequestContext, der im personRequest gespeichert wird.
					context = requestFactory.context();
					// durch Verwendung der .edit Methode entsteht aus dem alten personProxy das neuen personProxy
					personProxy = context.edit(personProxy);
				}
				personProxy.setVorname(vornameTextbox.getText());
				personProxy.setNachname(nachnameTextbox.getText());
				context.save(personProxy).fire(new Receiver<PersonProxy>() {
					// Object wird persistiert. Wichtig ist, dass dabei eine ID
					// vergeben wird (falls es sich um ein neues Object handelt)
					@Override
					public void onSuccess(final PersonProxy persistedPersonProxy) {
						final PersonProxy personProxy = persistedPersonProxy;
						logger.log(Level.FINE, personProxy.getVorname());
						//populate(personProxy);
					}
				});
			}
		});

		final Button deleteButton = new Button("l&ouml;schen");
		absolutePanel.add(deleteButton, 225, 129);
		deleteButton.setSize("60px", "32px");

	}
}
