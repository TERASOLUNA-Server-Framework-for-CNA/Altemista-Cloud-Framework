package cloud.altemista.fwk.test.rules;

/*
 * #%L
 * altemista-cloud rules engine: Drools implementation CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import cloud.altemista.fwk.core.rules.RulesService;
import cloud.altemista.fwk.core.rules.ruleset.Ruleset;
import cloud.altemista.fwk.core.rules.session.StatefulRuleSession;
import cloud.altemista.fwk.test.AbstractSpringContextTest;
import cloud.altemista.fwk.test.rules.model.NumberGuessBean;

/**
 * Tests for the rules module, Drools implementation, using a stateful rule
 * session
 */
@ContextConfiguration(locations = { "classpath:spring/test-app-context-rules-stateful.xml" })
public class StatefulTest extends AbstractSpringContextTest {

	@Autowired
	private RulesService service;

	private StatefulRuleSession obtainSession() {
		// Gets the default rule set
		Ruleset ruleset = this.service.defaultRuleset();
		Assert.assertNotNull(ruleset);

		// Starts the stateful rule session
		StatefulRuleSession session = ruleset.createStatefulSession();
		Assert.assertNotNull(session);
		return session;
	}

	private NumberGuessBean getNumberGuessBean() {
		return new NumberGuessBean(RandomUtils.nextInt(NumberGuessBean.MINIMUM, NumberGuessBean.MAXIMUM + 1));
	}

	/**
	 * Tests for check add to session and get from session methods
	 */
	@Test
	public void testAddGetObjectToSession() {

		StatefulRuleSession session = obtainSession();
		try {
			NumberGuessBean guess = getNumberGuessBean();
			String id = session.addObject(guess);
			NumberGuessBean sessionBean = (NumberGuessBean) session.getObject(id);
			Assert.assertNotNull(sessionBean);
			Assert.assertEquals(guess, sessionBean);

			// Sanity checks
			Assert.assertNull(session.addObject(null));
			Assert.assertNull(session.getObject(null));
			Assert.assertNull(session.getObject(""));

		} finally {
			session.release();
		}
	}

	/**
	 * Tests for check get all beans from session
	 */
	@Test
	public void testGetAllObjectsFromSession() {

		StatefulRuleSession session = obtainSession();
		try {
			NumberGuessBean guess1 = getNumberGuessBean();
			NumberGuessBean guess2 = getNumberGuessBean();
			String id1 = session.addObject(guess1);
			String id2 = session.addObject(guess2);
			Map<String, Object> sessionBeans = session.getObjects();
			Assert.assertNotNull(sessionBeans);
			Assert.assertEquals(sessionBeans.size(), 2);
			Assert.assertEquals(guess1, sessionBeans.get(id1));
			Assert.assertEquals(guess2, sessionBeans.get(id2));

		} finally {
			session.release();
		}
	}

	/**
	 * Tests remove bean from session
	 */
	@Test
	public void testRemoveObjectFromSession() {

		StatefulRuleSession session = obtainSession();
		try {
			NumberGuessBean guess = getNumberGuessBean();
			String id = session.addObject(guess);
			Assert.assertNotNull((NumberGuessBean) session.getObject(id));
			session.removeObject(id);
			Assert.assertNull((NumberGuessBean) session.getObject(id));
			Map<String, Object> sessionBeans = session.getObjects();
			Assert.assertEquals(sessionBeans.size(), 0);

			// Sanity checks
			session.removeObject(null);
			session.removeObject("");

		} finally {
			session.release();
		}
	}

	/**
	 * Tests remove bean from session when trying to update an object to null
	 */
	@Test
	public void testRemoveObjectFromSessionUpdatingToNull() {

		StatefulRuleSession session = obtainSession();
		try {
			NumberGuessBean guess = getNumberGuessBean();
			String id = session.addObject(guess);
			Assert.assertNotNull((NumberGuessBean) session.getObject(id));
			session.updateObject(id, null);
			Assert.assertNull((NumberGuessBean) session.getObject(id));
			Map<String, Object> sessionBeans = session.getObjects();
			Assert.assertEquals(sessionBeans.size(), 0);

		} finally {
			session.release();
		}
	}

	/**
	 * Tests update bean from session
	 */
	@Test
	public void testUpdateObjectFromSession() {

		StatefulRuleSession session = obtainSession();
		try {
			String id = session.addObject(getNumberGuessBean());
			NumberGuessBean bean1 = (NumberGuessBean) session.getObject(id);
			Assert.assertNotNull(bean1);
			session.updateObject(id, getNumberGuessBean());
			NumberGuessBean bean2 = (NumberGuessBean) session.getObject(id);
			Assert.assertNotNull(bean2);

			// Check there are different
			Assert.assertNotEquals(bean1, bean2);

			// Check there is only one session bean.
			Map<String, Object> sessionBeans = session.getObjects();
			Assert.assertEquals(sessionBeans.size(), 1);

			// Sanity checks
			session.updateObject(id, null);
			session.updateObject(id, "");

		} finally {
			session.release();
		}
	}

	/**
	 * Tests reset all objects session
	 */
	@Test
	public void testReset() {

		StatefulRuleSession session = obtainSession();
		try {
			session.addObject(getNumberGuessBean());
			session.addObject(getNumberGuessBean());
			Assert.assertEquals(session.getObjects().size(), 2);
			session.reset();
			Assert.assertEquals(session.getObjects().size(), 0);

		} finally {
			session.release();
		}
	}

	/**
	 * Tests for check add and get globals from session methods
	 */
	@Test
	public void testAddGetGlobalToSession() {

		StatefulRuleSession session = obtainSession();
		try {
			Integer value = RandomUtils.nextInt(NumberGuessBean.MINIMUM, NumberGuessBean.MAXIMUM);

			// Sanity checks
			session.addGlobal(null, null);
			session.addGlobal(null, value);
			session.addGlobal("secretNumber", null);
			session.addGlobal("", null);
			session.addGlobal("", value);
			Assert.assertEquals(session.getGlobals().size(), 0);

			Assert.assertNull(session.getGlobal(""));
			Assert.assertNull(session.getGlobal(null));

			// Check correct use
			session.addGlobal("secretNumber", value);
			Integer sessionValue = (Integer) session.getGlobal("secretNumber");
			Assert.assertNotNull(sessionValue);
			Assert.assertEquals(value, sessionValue);

		} finally {
			session.release();
		}
	}

	/**
	 * Tests for check get all beans from session
	 */
	@Test
	public void testGetAllGlobalsFromSession() {

		StatefulRuleSession session = obtainSession();
		try {
			Integer value1 = RandomUtils.nextInt(NumberGuessBean.MINIMUM, NumberGuessBean.MAXIMUM);
			Integer value2 = RandomUtils.nextInt(NumberGuessBean.MINIMUM, NumberGuessBean.MAXIMUM);
			session.addGlobal("secretNumber1", value1);
			session.addGlobal("secretNumber2", value2);
			Map<String, Object> sessionGlobals = session.getGlobals();
			Assert.assertNotNull(sessionGlobals);
			Assert.assertEquals(sessionGlobals.size(), 2);
			Assert.assertEquals(value1, sessionGlobals.get("secretNumber1"));
			Assert.assertEquals(value2, sessionGlobals.get("secretNumber2"));

		} finally {
			session.release();
		}
	}

	/**
	 * Example of use for the stateful rule session
	 */
	@Test
	public void statefulExample() {
		
		System.out.println(this.innerStatefulExample() ? "Winner!" : "Loser!");
	}
	
	public boolean innerStatefulExample() {
		
		//tag::example[]
		StatefulRuleSession session =
				this.service.defaultRuleset().createStatefulSession(); //<1>
		session.addGlobal("secretNumber", RandomUtils.nextInt(1, 100)); //<2>
		try {
			NumberGuessBean guess = getNumberGuessBean();
			String id = session.addObject(guess); //<3>
			int myMinimum = 1, myMaximum = 100;
			
			// In this example we will iterate some beans over the rules.
			do {
				session.executeRules(); //<4>
				//end::example[]
				System.out.println(String.format("#%d: Tried with %d and is %s", guess.getGuessCount(),
						guess.getCurrentGuess(), guess.isCorrect() ? "corrrect"
								: guess.isTooHigh() ? "too high" : guess.isTooLow() ? "too low" : "undefined"));
				//tag::example[]
				
				// Algorithm checks
				if (guess.isCorrect()) {
					return true; // Winner!
				}
				if (guess.isTooHigh()) {
					myMaximum = guess.getCurrentGuess() - 1;
				}
				if (guess.isTooLow()) {
					myMinimum = guess.getCurrentGuess() + 1;
				}
				//end::example[]
				System.out.println(String.format("Number must be between %d and %d!", myMinimum, myMaximum));
				//tag::example[]
				
				guess.setCurrentGuess(RandomUtils.nextInt(myMinimum, myMaximum + 1));
				session.updateObject(id, guess); //<5>
			} while (guess.hasMoreGuesses());
			
			return false; // Loser!
			
		} finally {
			session.release(); //<6>
		}
		//end::example[]
	}
}
