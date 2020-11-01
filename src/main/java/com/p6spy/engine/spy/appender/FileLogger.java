package com.p6spy.engine.spy.appender;

import com.p6spy.engine.spy.P6SpyOptions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;


public class FileLogger extends StdoutLogger {

	private String fileName = null;
	private PrintStream printStream = null;

	private void init() {
		if (fileName == null) {
			throw new IllegalStateException("setLogfile() must be called before init()");
		}
		try {
			printStream = new PrintStream(new FileOutputStream(fileName, P6SpyOptions.getActiveInstance().getAppend()));
		} catch (IOException e) {
			throw new IllegalStateException("couldn't create PrintStream for " + fileName, e);
		}
	}

	@Override
	protected PrintStream getStream() {
		if (printStream == null) {
			synchronized (this) {
				if (printStream == null) {
					init();
				}
			}
		}
		return printStream;
	}

	public void setLogfile(String fileName) {
		this.fileName = fileName;
	}
}
