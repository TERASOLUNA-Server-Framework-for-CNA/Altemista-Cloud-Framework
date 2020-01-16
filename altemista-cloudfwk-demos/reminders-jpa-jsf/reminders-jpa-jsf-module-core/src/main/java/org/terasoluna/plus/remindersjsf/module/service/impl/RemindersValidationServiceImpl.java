package org.altemista.cloudfwk.remindersjsf.module.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.altemista.cloudfwk.remindersjsf.module.exception.RemindersException;
import org.altemista.cloudfwk.remindersjsf.module.model.TaskDTO;
import org.altemista.cloudfwk.remindersjsf.module.service.RemindersValidationService;

/**
 * Implementation of the business validations of the demo application
 * @author NTT DATA
 */
@Service
public class RemindersValidationServiceImpl implements RemindersValidationService {
	
	/* (non-Javadoc)
	 * @see org.altemista.cloudfwk.reminders.service.RemindersValidationService#validateTask(org.altemista.cloudfwk.reminders.TaskDTO)
	 */
	public void validateTask(TaskDTO task) {
		
		// The date when the task is to be completed must not be a past date
		if (DateUtils.truncatedCompareTo(task.getDate(), new Date(), Calendar.DATE) < 0) {
			throw new RemindersException("Past is gone!");
		}
	}
}
