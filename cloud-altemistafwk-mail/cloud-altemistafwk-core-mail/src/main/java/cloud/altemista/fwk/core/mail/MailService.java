
package cloud.altemista.fwk.core.mail;

import java.util.concurrent.Future;

/*
 * #%L
 * altemista-cloud mail server
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import cloud.altemista.fwk.core.mail.model.Mail;
import cloud.altemista.fwk.core.mail.model.MailStatus;

/**
 * Mail server.
 * @author NTT DATA
 * @see cloud.altemista.fwk.core.mail.AsyncMailService
 */
public interface MailService {
	
	/**
	 * Sends a mail.
	 * @param mail the mail to be sent
	 */
	void send(Mail mail);
	
	/**
	 * Sends a mail async.
	 * @param mail the mail to be sent
	 * @return the future status of the request
	 */
	Future<MailStatus> sendAsync(Mail mail);

}
