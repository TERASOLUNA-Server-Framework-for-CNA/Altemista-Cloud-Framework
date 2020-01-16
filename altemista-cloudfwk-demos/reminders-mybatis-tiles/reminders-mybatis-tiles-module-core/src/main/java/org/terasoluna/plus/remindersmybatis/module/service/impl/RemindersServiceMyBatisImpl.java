package org.altemista.cloudfwk.remindersmybatis.module.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.altemista.cloudfwk.remindersmybatis.module.exception.RemindersException;
import org.altemista.cloudfwk.remindersmybatis.module.model.RmndrsTable;
import org.altemista.cloudfwk.remindersmybatis.module.model.RmndrsTableExample;
import org.altemista.cloudfwk.remindersmybatis.module.model.TaskDTO;
import org.altemista.cloudfwk.remindersmybatis.module.repository.RmndrsTableMapper;
import org.altemista.cloudfwk.remindersmybatis.module.service.RemindersService;

/**
 * MyBatis based implementation of the business of the demo application
 * 
 * @author NTT DATA
 */
@Service
public class RemindersServiceMyBatisImpl extends AbstractRemindersServiceImpl implements RemindersService {

	/** The MyBatis repository */
	@Autowired
	private RmndrsTableMapper repository;


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#getTodosList()
	 */
	public List<TaskDTO> getTodosList(String user) {

		// Creates the criteria
		RmndrsTableExample criteria = new RmndrsTableExample();
		criteria.createCriteria().andCompletedEqualTo(false).andUsernameEqualTo(getCurrentUserId(user));

		// Queries the database
		List<RmndrsTable> entities = this.repository.selectByExample(criteria);

		List<TaskDTO> list = new ArrayList<TaskDTO>(0);
		for (RmndrsTable task : entities) {
			list.add(this.toModel(task));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#getCompleteTasks()
	 */
	public List<TaskDTO> getCompleteTasks(String user) {

		// Creates the criteria
		RmndrsTableExample criteria = new RmndrsTableExample();
		criteria.createCriteria().andCompletedEqualTo(true).andUsernameEqualTo(getCurrentUserId(user));

		// Queries the database
		List<RmndrsTable> entities = this.repository.selectByExample(criteria);

		List<TaskDTO> list = new ArrayList<TaskDTO>(0);
		for (RmndrsTable task : entities) {
			list.add(this.toModel(task));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#getDetail(long)
	 */
	public TaskDTO getDetail(long id) {

		RmndrsTable entity = repository.selectByPrimaryKey((int) id);
		TaskDTO model = this.toModel(entity);
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#addTask(org.altemista.cloudfwk.
	 * reminders.TaskDTO)
	 */
	public void addTask(TaskDTO task, String user) {
		super.addTask(task);
		user = getCurrentUserId(user);
		RmndrsTable entity = this.fromModel(task, user);
		entity.setUsername(user);
		this.repository.insert(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#editTask(org.altemista.cloudfwk.
	 * reminders.TaskDTO)
	 */
	public void editTask(TaskDTO task, String user) {
		super.editTask(task);
		user = getCurrentUserId(user);
		RmndrsTable data = this.fromModel(task, user);
		data.setUsername(user);
		this.repository.updateByPrimaryKey(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#complete(long)
	 */
	public void complete(long id) {
		RmndrsTable entity = repository.selectByPrimaryKey((int) id);
		entity.setCompleted(true);
		this.repository.updateByPrimaryKey(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.altemista.cloudfwk.reminders.service.RemindersService#deleteTask(long)
	 */
	public void deleteTask(long id) {
		this.repository.deleteByPrimaryKey((int) id);
	}

	/**
	 * Convenience method to transform a MyBatis entity into a model entity
	 * 
	 * @param data
	 *            RmndrsTable
	 * @return TaskDTO
	 */
	private TaskDTO toModel(RmndrsTable data) {
		System.err.println(data);
		TaskDTO detail = new TaskDTO(data.getId(), data.getText(), data.getDueDate(), data.getCompleted());
		return detail;
	}

	/**
	 * Convenience method to transform a model entity into a MyBatis entity
	 * 
	 * @param data
	 *            TaskDTO
	 * @return RmndrsTable
	 */
	private RmndrsTable fromModel(TaskDTO data, String user) {

		RmndrsTable detail = new RmndrsTable();
		detail.setId((data.getId() == TaskDTO.NO_ID) ? null : ((int) (data.getId())));
		detail.setUsername(getCurrentUserId(user));
		detail.setText(data.getName());
		detail.setDueDate(data.getDate());
		detail.setCompleted(data.isCompleted());
		return detail;
	}
	
	/**
	 * @return {String} Id of the current authenticated user
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

}
