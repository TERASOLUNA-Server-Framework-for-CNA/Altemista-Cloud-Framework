package org.altemista.cloudfwk.remindersjsf.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.stereotype.Controller;

/**
 * @author NTT DATA
 * 
 *         ManagedBean that handles the alerts which are thrown by the
 *         application It recovers the message from the FacesContext and
 *         filtered them by severity
 *
 */
@Controller
@SessionScoped
public class AlertMessages {

	public static final String CLIENT_ID = "messageHandler";

	/**
	 * @return List of fatal messages
	 */
	public List<String> getFatals() {
		return getMessages(FacesMessage.SEVERITY_FATAL);
	}

	/**
	 * @return List of error messages
	 */
	public List<String> getErrors() {
		return getMessages(FacesMessage.SEVERITY_ERROR);
	}

	/**
	 * @return List of warnings messages
	 */
	public List<String> getWarnings() {
		return getMessages(FacesMessage.SEVERITY_WARN);
	}

	/**
	 * @return List of information messages
	 */
	public List<String> getInfos() {
		return getMessages(FacesMessage.SEVERITY_INFO);
	}

	/**
	 * Util method to execute the recovering and message filtering from
	 * FacesContext
	 * 
	 * @param severity
	 * @return List of filtered messages
	 */
	private List<String> getMessages(Severity severity) {
		List<String> messages = new ArrayList<String>(0);
		FacesContext context = FacesContext.getCurrentInstance();
		for (FacesMessage msg : context.getMessageList(CLIENT_ID)) {
			if (msg.getSeverity().equals(severity)) {
				messages.add(msg. getDetail());
			}
		}
		return messages;
	}

}