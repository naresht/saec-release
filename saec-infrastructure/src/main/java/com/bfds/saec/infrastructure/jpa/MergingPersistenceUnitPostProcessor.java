package com.bfds.saec.infrastructure.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

/**
 * This merges all JPA entities from multiple jars. To use it, all entities must
 * be listed in their respective persistence.xml files using the <class> tag.
 * 
 * @see http://forum.springsource.org/showthread.php?t=61763
 */
public class MergingPersistenceUnitPostProcessor implements
		PersistenceUnitPostProcessor {
	Map<String, List<String>> puiClasses = new HashMap<String, List<String>>();

	public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
		List<String> classes = puiClasses.get(pui.getPersistenceUnitName());
		if (classes == null) {
			classes = new ArrayList<String>();
			puiClasses.put(pui.getPersistenceUnitName(), classes);
		}
		pui.getManagedClassNames().addAll(classes);

		final List<String> names = pui.getManagedClassNames();
		classes.addAll(pui.getManagedClassNames());
	}
}
