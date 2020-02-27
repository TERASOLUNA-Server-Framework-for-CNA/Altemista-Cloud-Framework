package cloud.altemista.fwk.common.monitoring.model;

/*
 * #%L
 * altemista-cloud monitoring: common interfaces
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


/**
 * Status of a monitored resource in a particular execution, as stored in one {@link MonitoringInfo}
 * @author NTT DATA
 */
public enum Status {

	/** Unknown state (probably, not executed) */
	UNKNOWN,

	/** OK, functioning as expected */
	OK,

	/** Unexpected failure */
	FAILED
}
