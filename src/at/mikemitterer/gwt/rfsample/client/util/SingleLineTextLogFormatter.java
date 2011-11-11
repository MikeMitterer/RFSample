package at.mikemitterer.gwt.rfsample.client.util;

import java.util.Date;
import java.util.logging.LogRecord;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.logging.impl.FormatterImpl;

class SingleLineTextLogFormatter extends FormatterImpl {
	private final boolean	showStackTraces;

	public SingleLineTextLogFormatter(final boolean showStackTraces) {
		this.showStackTraces = showStackTraces;
	}

	@Override
	public String format(final LogRecord event) {
		final StringBuilder message = new StringBuilder();
		message.append(getRecordInfo(event, "\n"));
		message.append(event.getMessage());
		if (showStackTraces) {
			message.append(getStackTraceAsString(event.getThrown(), "\n", "\t"));
		}
		return message.toString();
	}

	//---------------------------------------------------------------------------------
	// protected
	//---------------------------------------------------------------------------------
	@Override
	protected String getRecordInfo(final LogRecord event, final String newline) {
		final Date date = new Date(event.getMillis());
		final DateTimeFormat formatter = DateTimeFormat.getFormat("yyyy-MM-dd kk:mm:ss,SSS");
		final StringBuilder s = new StringBuilder();
		s.append(formatter.format(date));
		s.append(" ");
		s.append("CLN [" + event.getLoggerName() + "] ");
		//s.append(event.getLevel().getName());
		//s.append(": ");
		return s.toString();
	}
}
