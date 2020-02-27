
package cloud.altemista.fwk.performance.jdbc.model;

/*
 * #%L
 * altemista-cloud performance: JDBC monitoring and performance
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import cloud.altemista.fwk.common.model.SparseList;
import cloud.altemista.fwk.common.util.SqlUtil;
import cloud.altemista.fwk.core.performance.model.MethodExecutionTaskInfo;

/**
 * Information about the execution of one Statement (and its related ResultSets) as a measured task
 * @author NTT DATA
 */
public class JdbcTaskInfo extends MethodExecutionTaskInfo {

	/** The serialVersionUID long */
	private static final long serialVersionUID = 8020289465146395869L;

	/** The unformatted SQL query string */
	private String sqlQuery;
	
	/** The SQL parameters, mapped by index */
	private transient Map<Integer, Object> sqlParameters;
	
	/** Flag to explicitly inform that a statement accessed the ResultSet */
	private boolean resultSetAccessed;
	
	/** The count of fields accessed via the ResultSet */
	private int fieldCount;
	
	/** The count of results iterated by the ResultSet */
	private int resultCount;
	
	/**
	 * Flag to stop counting fields after the second result has been accessed
	 * (i.e.: count only the field of the first result)
	 */
	private boolean stopFieldCount = false;
	
	/**
	 * Clears the stored SQL parameters
	 */
	public void clearSqlParameters() {
		
		if (this.sqlParameters != null) {
			this.sqlParameters.clear();
		}
	}
	
	/**
	 * Sets one of the SQL parameters
	 * @param index int
	 * @param value Object
	 */
	public void setSqlParameter(int index, Object value) {
		
		if (this.sqlParameters == null) {
			this.sqlParameters = new HashMap<Integer, Object>();
		}
		this.sqlParameters.put(index, value);
	}
	
	/**
	 * Gets the stored SQL parameters
	 * @return List of the stored SQL parameters
	 */
	public List<Object> getSqlParameters() {
		
		if (this.sqlParameters == null) {
			return Collections.emptyList();
		}
		
		// Uses a SparseList to return the SQL parameters as a List,
		// with offset 1 because the SQL parameter indexes are not zero-based but one-based
		return new SparseList<Object>(this.sqlParameters, 1);
	}
	
	/**
	 * Marks that the statement accessed the ResultSet
	 */
	public void markResultSetAccessed() {
		
		this.resultSetAccessed = true;
	}
	
	/**
	 * Returns if the statement accessed the ResultSet
	 * @return if the statement accessed the ResultSet
	 */
	public boolean isResultSetAccessed() {
		
		return this.resultSetAccessed;
	}
	
	/**
	 * Increases the count of fields that have been accessed via the ResultSet
	 */
	public void incFieldCount() {
		
		// (do not modify the field count after the second result has been accessed)
		if (!this.stopFieldCount) {
			this.fieldCount++;
		}
		this.resultSetAccessed = true;
	}
	
	/**
	 * @return the count of fields accessed via the ResultSet
	 */
	public int getFieldCount() {
		
		return this.fieldCount;
	}
	
	/**
	 * Increases the count of results iterated by the ResultSet
	 */
	public void incResultCount() {
		
		this.resultCount++;
		this.resultSetAccessed = true;
		
		// (will stop counting fields after the second result has been accessed)
		this.stopFieldCount = this.resultCount > 1;
	}
	
	/**
	 * @return the count of results iterated by the ResultSet
	 */
	public int getResultCount() {
		
		return this.resultCount;
	}
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.common.performance.model.TaskInfo#getDescription()
	 */
	@Override
	public String getDescription() {

		List<String> fragments = new ArrayList<String>();
		
		if (StringUtils.isNotEmpty(this.getSqlQuery())) {
			if ((this.sqlParameters == null) || this.sqlParameters.isEmpty()) {
				fragments.add(this.sqlQuery);
			} else {
				fragments.add(SqlUtil.replacePlaceholder(this.sqlQuery, this.getSqlParameters()));
			}
		}
		
		if (this.resultSetAccessed) {
			fragments.add(String.format("read %d results", this.getResultCount()));
			if (this.getResultCount() != 0) {
				fragments.add(String.format("of %d fields", this.getFieldCount()));
			}
		}
		
		if (StringUtils.isNotEmpty(this.getThrownException())) {
			fragments.add("thrown " + this.getThrownException());
		}
		
		return StringUtils.join(fragments, " ");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		// Uses the description also for toString()
		return new ToStringBuilder(this).append(this.getDescription()).toString();
	}

	/**
	 * @return the sqlQuery
	 */
	public String getSqlQuery() {
		return sqlQuery;
	}

	/**
	 * @param sqlQuery the sqlQuery to set
	 */
	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
}
