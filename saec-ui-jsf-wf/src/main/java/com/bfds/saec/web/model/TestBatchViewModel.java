package com.bfds.saec.web.model;

import com.bfds.saec.web.util.CommonProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class TestBatchViewModel implements java.io.Serializable,
		ApplicationContextAware {

	static final private Logger logger = LoggerFactory
			.getLogger(TestBatchViewModel.class);
	static String ndmshare;

	static {
		loadProperties();
	}

	private static void loadProperties() {

		CommonProperties p = new CommonProperties();

		try {
			Hashtable hash = p.loadProperties();
			ndmshare = CommonProperties.getNdmshare();
		} catch (FileNotFoundException e) {
			logger.error("Error loading common.properties: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Error loading common.properties: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 1L;

	@Autowired
	private transient JobLauncher launcher;

	@Autowired
	private transient JobExplorer jobExplorer;

	@Autowired
	private transient Job importTestClaimantsJob;

	public boolean importTestClaimants(final MessageContext messageContext) {
		JobParameters jobParameters = new JobParametersBuilder().addString(
				"inputResource",
				// "file:c:/temp/s12/testImport/TestClaimants.xml")
				"file:" + ndmshare + "testImport/TestClaimants.xml")
				.toJobParameters();
		return runJob(jobParameters, importTestClaimantsJob, messageContext);
	}

	private boolean runJob(JobParameters jobParameters, Job job,
			final MessageContext messageContext) {
		try {

			Map<String, JobParameter> params = jobParameters.getParameters();
			params.put("run.count",
					new JobParameter(System.currentTimeMillis()));

			JobExecution jobExecution = launcher.run(job, new JobParameters(
					params));
		} catch (Exception e) {
			e.printStackTrace();
			messageContext.addMessage(new MessageBuilder().error()
					.defaultText(e.getMessage()).build());
			return false;
		}
		return true;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub

	}

}
