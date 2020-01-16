package org.altemista.cloudfwk.remindersjsf.module.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.altemista.cloudfwk.remindersjsf.module.entity.RmndrsTable;
import org.altemista.cloudfwk.remindersjsf.module.exception.RemindersException;
import org.altemista.cloudfwk.remindersjsf.module.model.TaskDTO;
import org.altemista.cloudfwk.remindersjsf.module.repository.RmndrsRepository;
import org.altemista.cloudfwk.remindersjsf.module.service.RemindersService;

/**
 * Implementation of Reminders Service for Consume JPA-Hibernate Database
 * 
 * @author NTT DATA
 *
 */
@Service
@Transactional
public class RemindersServiceJPAImpl extends AbstractRemindersServiceImpl implements RemindersService {

	@Autowired
	private RmndrsRepository repository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#getTodosList()
	 */
	public List<TaskDTO> getTodosList(String user) {
		List<TaskDTO> taskList = new ArrayList<TaskDTO>(0);
		List<RmndrsTable> list = repository.findAllByCompletedAndUsername(false, getCurrentUserId(user));
		for (RmndrsTable task : list) {
			taskList.add(mapper(task));
		}
		return taskList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#getDetail(long)
	 */
	public TaskDTO getDetail(long id) {
		TaskDTO detail = mapper(repository.findOne(id));
		return detail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#getCompleteTasks()
	 */
	public List<TaskDTO> getCompleteTasks(String user) {
		List<TaskDTO> taskList = new ArrayList<TaskDTO>(0);
		List<RmndrsTable> list = repository.findAllByCompletedAndUsername(true, getCurrentUserId(user));
		for (RmndrsTable task : list) {
			taskList.add(mapper(task));
		}
		return taskList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.altemista.cloudfwk.reminders.service.impl.AbstractRemindersServiceImpl#addTask(
	 * org.altemista.cloudfwk.reminders.TaskDTO)
	 */
	public void addTask(TaskDTO task, String user) {
		super.addTask(task);
		RmndrsTable data = mapper(task, getCurrentUserId(user));
		repository.save(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.altemista.cloudfwk.reminders.service.impl.AbstractRemindersServiceImpl#editTask(
	 * org.altemista.cloudfwk.reminders.TaskDTO)
	 */
	public void editTask(TaskDTO task, String user) {
		super.editTask(task);
		RmndrsTable data = mapper(task, getCurrentUserId(user));
		data.setId(task.getId());
		repository.save(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#deleteTask(long)
	 */
	public void deleteTask(long id) {
		repository.delete(id);
	}

	/*
	 * First, find the reminder by id and then edit it with "completed" field
	 * set as true
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#complete(long)
	 */
	public void complete(long id) {
		RmndrsTable data = repository.findOne(id);
		data.setCompleted(true);
		repository.save(data);
	}

	/**
	 * @return {String} with the Id of the current authenticated user
	 */
	private String getCurrentUserId(String user) {
		if (user != null) {
			return user;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new RemindersException("unauthenticated");
		}
		return authentication.getName();
	}

	/**
	 * Converter for RmndrsTable -> TaskDto
	 * 
	 * @param data
	 *            (Database entity)
	 * @return TaskDTO model
	 */
	private TaskDTO mapper(RmndrsTable data) {
		TaskDTO detail = data != null
				? new TaskDTO(data.getId(), data.getText(), data.getDueDate(), data.getCompleted()) : null;
		return detail;
	}

	/**
	 * Converter for TaskDto -> RmndrsTable
	 * 
	 * @param data
	 * @return
	 */
	private RmndrsTable mapper(TaskDTO data, String user) {
		RmndrsTable detail = new RmndrsTable();
		detail.setDueDate(data.getDate());
		detail.setText(data.getName());
		detail.setUsername(user);
		detail.setCompleted(data.isCompleted());
		return detail;
	}
}
