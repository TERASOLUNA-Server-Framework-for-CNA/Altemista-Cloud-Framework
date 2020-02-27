/**
 * 
 */
package cloud.altemista.fwk.core.performance.model;

import java.io.Serializable;

/*
 * #%L
 * altemista-cloud performance: execution performance statistics
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import com.fasterxml.jackson.annotation.JsonView;

/**
 * Represents information about a measured task
 * @author NTT DATA
 */
public interface TaskInfo extends Serializable {
	
	/**
	 * Returns the task description
	 * @return the task description
	 */
	@JsonView(MeasuredTask.View.class)
	String getDescription();

}
