package at.mikemitterer.gwt.rfsample.shared.util;

/**
 * More infos about Loglevels: http://download.oracle.com/javase/6/docs/api/java/util/logging/Level.html
 * 
 * @author mikemitterer
 */
public class Level extends java.util.logging.Level {
	public static Level	DEBUG	= new Level("DEBUG", Level.INFO.intValue());
	public static Level	WARNING	= new Level("WARNING", Level.WARNING.intValue());
	public static Level	ERROR	= new Level("ERROR", Level.SEVERE.intValue());

	public Level(final String name, final int value) {
		super(name, value);
	}
}
