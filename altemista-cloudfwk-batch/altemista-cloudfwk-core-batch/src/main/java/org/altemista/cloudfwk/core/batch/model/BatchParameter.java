/**
 * 
 */
package org.altemista.cloudfwk.core.batch.model;

/*
 * #%L
 * altemista-cloud batch
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.Serializable;
import java.util.Date;

import org.altemista.cloudfwk.common.util.DefensiveUtil;

/**
 * A parameter for a job execution
 * Only the following types can be parameters: String, Long, Date, and Double
 * @param <T> The actual type of the parameter
 * @author NTT DATA
 */
public abstract class BatchParameter<T extends Serializable> implements Serializable {
	
	/** The serialVersionUID long */
	private static final long serialVersionUID = -7366438395109555890L;
	
	/** The name of the parameter */
	private final String name;

	/** The value of the parameter */
	private final T value;
	
	/** Is the parameter identifier of the job instance? */
	private final boolean identifier;
	
	/**
	 * Constructor
	 * @param value the value of the parameter
	 * @param identifier Is the parameter identifier of the job instance?
	 */
	private BatchParameter(String name, T value, boolean identifier) {
		super();
		
		this.name = name;
		this.value = value;
		this.identifier = identifier;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @return the identifier
	 */
	public boolean isIdentifier() {
		return identifier;
	}
	
	/**
	 * An job parameter whose value is a String
	 * @author NTT DATA
	 */
	public static final class StringParameter extends BatchParameter<String> {
		
		/** The serialVersionUID long */
		private static final long serialVersionUID = 6038241325059915539L;

		/**
		 * Constructor
		 * @param name the name of the parameter
		 * @param value the value of the parameter
		 * @param identifier is the parameter identifier of the job instance?
		 */
		public StringParameter(String name, String value, boolean identifier) {
			super(name, value, identifier);
		}
	}
	
	/**
	 * An job parameter whose value is a Long
	 * @author NTT DATA
	 */
	public static final class LongParameter extends BatchParameter<Long> {
		
		/** The serialVersionUID long */
		private static final long serialVersionUID = 6328787857259101793L;

		/**
		 * Constructor
		 * @param name the name of the parameter
		 * @param value the value of the parameter
		 * @param identifier is the parameter identifier of the job instance?
		 */
		public LongParameter(String name, Long value, boolean identifier) {
			super(name, value, identifier);
		}
	}
	
	/**
	 * An job parameter whose value is a Double
	 * @author NTT DATA
	 */
	public static final class DoubleParameter extends BatchParameter<Double> {
		
		/** The serialVersionUID long */
		private static final long serialVersionUID = -293064291043948026L;

		/**
		 * Constructor
		 * @param name the name of the parameter
		 * @param value the value of the parameter
		 * @param identifier is the parameter identifier of the job instance?
		 */
		public DoubleParameter(String name, Double value, boolean identifier) {
			super(name, value, identifier);
		}
	}
	
	/**
	 * An job parameter whose value is a Boolean
	 * @author NTT DATA
	 */
	public static final class BooleanParameter extends BatchParameter<Boolean> {
		
		/** The serialVersionUID long */
		private static final long serialVersionUID = -5530415596327771644L;

		/**
		 * Constructor
		 * @param name the name of the parameter
		 * @param value the value of the parameter
		 * @param identifier is the parameter identifier of the job instance?
		 */
		public BooleanParameter(String name, Boolean value, boolean identifier) {
			super(name, value, identifier);
		}
	}
	
	/**
	 * An job parameter whose value is a Date
	 * @author NTT DATA
	 */
	public static final class DateParameter extends BatchParameter<Date> {
		
		/** The serialVersionUID long */
		private static final long serialVersionUID = 7522992242601967382L;

		/**
		 * Constructor
		 * @param name the name of the parameter
		 * @param value the value of the parameter
		 * @param identifier is the parameter identifier of the job instance?
		 */
		public DateParameter(String name, Date value, boolean identifier) {
			super(name, DefensiveUtil.copyOf(value), identifier);
		}
		
		/* (non-Javadoc)
		 * @see org.altemista.cloudfwk.core.batch.model.BatchParameter#getValue()
		 */
		@Override
		public Date getValue() {
			return DefensiveUtil.copyOf(super.getValue());
		}
	}
}
