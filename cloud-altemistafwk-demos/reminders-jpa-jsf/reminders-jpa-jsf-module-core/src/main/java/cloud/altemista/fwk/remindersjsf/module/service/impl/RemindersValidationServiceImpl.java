package cloud.altemista.fwk.remindersjsf.module.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import cloud.altemista.fwk.remindersjsf.module.exception.RemindersException;
import cloud.altemista.fwk.remindersjsf.module.model.TaskDTO;
import cloud.altemista.fwk.remindersjsf.module.service.RemindersValidationService;

/**
 * Implementation of the business validations of the demo application
 * @author NTT DATA
 */
@Service
public class RemindersValidationServiceImpl implements RemindersValidationService {
	
	/* (non-Javadoc)
	 * @see cloud.altemista.fwk.reminders.service.RemindersValidationService#validateTask(cloud.altemista.fwk.reminders.TaskDTO)
	 */
	public void validateTask(TaskDTO task) {
		
		// The date when the task is to be completed must not be a past date
		if (DateUtils.truncatedCompareTo(task.getDate(), new Date(), Calendar.DATE) < 0) {
			throw new RemindersException("Past is gone!");
		}
	}
}
