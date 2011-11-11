package at.mikemitterer.gwt.rfsample.client.util;

import java.util.logging.Level;

import com.google.gwt.logging.client.ConsoleLogHandler;

public class SingleLineConsoleLogHandler extends ConsoleLogHandler {
	public SingleLineConsoleLogHandler() {
		setFormatter(new SingleLineTextLogFormatter(true));
		setLevel(Level.ALL);
	}
}
