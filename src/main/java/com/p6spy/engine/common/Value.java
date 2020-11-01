package com.p6spy.engine.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.p6spy.engine.logging.P6LogLoadableOptions;
import com.p6spy.engine.logging.P6LogOptions;
import com.p6spy.engine.logging.format.BinaryFormat;
import com.p6spy.engine.spy.P6SpyOptions;

public class Value {

	private Object value;

	public Value(Object valueToSet) {
		this();
		this.value = valueToSet;
	}

	public Value() {
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return convertToString(this.value);
	}

	public String convertToString(Object value) {
		String result;

		if (value == null) {
			result = "NULL";
		} else {

			if (value instanceof byte[]) {
				// P6LogFactory may not be registered
				P6LogLoadableOptions logOptions = P6LogOptions.getActiveInstance();
				if (logOptions != null && logOptions.getExcludebinary()) {
					result = "[binary]";
				} else {
					BinaryFormat binaryFormat = P6SpyOptions.getActiveInstance()
							.getDatabaseDialectBinaryFormatInstance();
					return binaryFormat.toString((byte[]) value);
				}
			} else if (value instanceof Timestamp) {
				result = new SimpleDateFormat(P6SpyOptions.getActiveInstance().getDatabaseDialectTimestampFormat())
						.format(value);
			} else if (value instanceof Date) {
				result = new SimpleDateFormat(P6SpyOptions.getActiveInstance().getDatabaseDialectDateFormat())
						.format(value);
			} else if (value instanceof Boolean) {
				if ("numeric".equals(P6SpyOptions.getActiveInstance().getDatabaseDialectBooleanFormat())) {
					result = Boolean.FALSE.equals(value) ? "0" : "1";
				} else {
					result = value.toString();
				}
			} else {
				result = value.toString();
			}

			result = quoteIfNeeded(result, value);
		}

		return result;
	}

	private String quoteIfNeeded(String stringValue, Object obj) {
		if (stringValue == null) {
			return null;
		}
		if (Number.class.isAssignableFrom(obj.getClass()) || Boolean.class.isAssignableFrom(obj.getClass())) {
			return stringValue;
		} else {
			return "'" + escape(stringValue) + "'";
		}
	}

	private String escape(String stringValue) {
		return stringValue.replaceAll("'", "''");
	}

}
