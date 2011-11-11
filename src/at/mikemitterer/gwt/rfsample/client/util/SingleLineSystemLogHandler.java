package at.mikemitterer.gwt.rfsample.client.util;

import java.util.logging.Level;

import com.google.gwt.logging.client.SystemLogHandler;

public class SingleLineSystemLogHandler extends SystemLogHandler {
	public SingleLineSystemLogHandler() {
		setFormatter(new SingleLineTextLogFormatter(true));
		setLevel(Level.ALL);
	}
}
