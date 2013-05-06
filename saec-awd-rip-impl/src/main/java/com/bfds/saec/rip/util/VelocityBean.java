package com.bfds.saec.rip.util;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

//TODO: Delete this and use Spring VelocityEngineFactoryBean.
public class VelocityBean {
	static {
		try {
			// VelocityEngine ve = new VelocityEngine();
			// ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
			// ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH,
			// file.getAbsolutePath());
			// ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE,
			// "true");
			Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
			// Velocity.setProperty("file.resource.loader.class",
			// FileResourceLoader.class.getName());
			Velocity.setProperty("class.resource.loader.class",
					ClasspathResourceLoader.class.getName());
			Velocity.init();

		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static void initVelocity() {
		// Just to load the class.
	}
}
