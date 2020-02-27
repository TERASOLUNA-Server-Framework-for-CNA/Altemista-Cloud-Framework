package cloud.altemista.fwk.example.batch;

/*
 * #%L
 * altemista-cloud batch: Spring Batch implementation CONF
 * %%
 * Copyright (C) 2017 - 2018 NTT DATA Corporation
 * %%
 * All rights reserved.
 * Todos los derechos reservados.
 * #L%
 */


import java.util.Arrays;
import java.util.Iterator;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

/**
 * Example ItemReader for testing the batch module
 * @author NTT DATA
 */
@Component
public class ExampleInfiniteReader implements ItemReader<ExampleBean> {
	
	/** Some data */
	private static final String[] EXAMPLE_NAMES = new String[]{
		"Nola Nigh",
		"Kelli Kulikowski",
		"Marg Maxwell",
		"Fanny Forsythe",
		"Sharee Stipe",
		"Bradford Burrill",
		"Letha Lupien",
		"Brande Bradbury",
		"Joesph Jovelv",
		"Megan Margulies",
		"Moshe Maag",
		"Chase Cork",
		"Chester Crews",
		"Floyd Fukuda",
		"Debra Dechant",
		"Sam Smither",
		"Garret Goodsell",
		"Slyvia Spillman",
		"Merry Mauldin",
		"Emilio Emerick"
	};
	
	/** Pointer for the data */
	private static Iterator<String> it; 

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@Override
	public ExampleBean read() {
		
		if ((it == null) || (!it.hasNext())) {
			it = Arrays.asList(EXAMPLE_NAMES).iterator();
		}
		
		final String newName = it.next();
		final ExampleBean newBean = new ExampleBean();
		String[] splitNewName = newName.split("\\s", 2);
		newBean.setName(splitNewName[0]);
		newBean.setSurname(splitNewName[1]);
		return newBean;
	}
}
