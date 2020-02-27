/**
 * 
 */
package cloud.altemista.fwk.test.rules.model;

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


/**
 * Simple bean for testing purposes only
 * @author NTT DATA
 */
public class NumberGuessBean {
	
	public static final int MINIMUM = 1;
	
	public static final int MAXIMUM = 100;
	
	public static final int MAXIMUM_GUESSES = 5;
	
	private int guessCount;
	
	private int currentGuess;
	
	private boolean moreGuesses;
	
	private boolean correct;
	
	private boolean tooLow;
	
	private boolean tooHigh;
	
	public NumberGuessBean(int initialGuess) {
		super();
		this.guessCount = 0;
		this.setCurrentGuess(initialGuess);
	}

	public void incGuessCount() {
		this.guessCount++;
	}

	public int getGuessCount() {
		return guessCount;
	}
	
	public void setCurrentGuess(int guess) {
		this.currentGuess = (guess < MINIMUM) ? MINIMUM : (guess > MAXIMUM) ? MAXIMUM : guess;		
	}

	public int getCurrentGuess() {
		return currentGuess;
	}

	public boolean hasMoreGuesses() {
		return moreGuesses;
	}

	public void setMoreGuesses(boolean moreGuesses) {
		this.moreGuesses = moreGuesses;
	}
	
	public void markAsCorrect() {
		this.correct = true;
		this.tooLow = false;
		this.tooHigh = false;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void markAsTooLow() {
		this.correct = false;
		this.tooLow = true;
		this.tooHigh = false;
	}

	public boolean isTooLow() {
		return tooLow;
	}

	public void markAsTooHigh() {
		this.correct = false;
		this.tooLow = false;
		this.tooHigh = true;
	}
	
	public boolean isTooHigh() {
		return tooHigh;
	}
}
