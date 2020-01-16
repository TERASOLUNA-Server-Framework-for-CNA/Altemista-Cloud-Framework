/**
 * 
 */
package org.altemista.cloudfwk.remindersjsf.module.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.altemista.cloudfwk.remindersjsf.module.entity.RmndrsTable;

/**
 * @author NTT DATA
 * 
 * Reminders Repository
 *
 */
@Transactional(propagation = Propagation.MANDATORY)
public interface RmndrsRepository extends JpaRepository<RmndrsTable, Long>, QueryDslPredicateExecutor<RmndrsTable> {
	
	/**
	 * Specific query that filter by username and the status (completed or not)
	 * 
	 * @param completed
	 * @return
	 */
	List<RmndrsTable> findAllByCompletedAndUsername(boolean completed, String username);

}
