package org.altemista.cloudfwk.core.soap.wss;

/*
 * #%L
 * altemista-cloud SOAP client with WS-Security
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

import org.apache.commons.lang3.StringUtils;
import org.apache.wss4j.common.ext.WSPasswordCallback;

/**
 * Conveniece base class for {@link CallbackHandler}s that only handle the {@link WSPasswordCallback}
 * @author NTT DATA
 */
public abstract class AbstractPasswordCallbackHandler implements CallbackHandler {

	/* (non-Javadoc)
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */
	@Override
	public void handle(Callback[] callbacks) throws IOException {

		// Looks for the WSPasswordCallback 
		WSPasswordCallback pc = null;
		for (Callback callback : callbacks) {
			if (callback instanceof WSPasswordCallback) {
				pc = (WSPasswordCallback) callback;
				break;
			}
		}

		// (not found)
		if ((pc == null) || (!StringUtils.isNotEmpty(pc.getIdentifier()))) {
			return;
		}
		
		// Do the actual handling of the WSPasswordCallback
		this.doHandle(pc);
	}

	/**
	 * Actual handler of the WSPasswordCallback
	 * @param pc the WSPasswordCallback
	 * @return true if the WSPasswordCallback was handled successfully; false otherwise
	 * @exception java.io.IOException if an input or output error occurs
	 */
	protected abstract boolean doHandle(WSPasswordCallback pc) throws IOException;
}
