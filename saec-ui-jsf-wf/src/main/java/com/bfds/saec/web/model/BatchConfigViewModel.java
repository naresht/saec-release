package com.bfds.saec.web.model;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.transaction.annotation.Transactional;

import com.bfds.saec.batch.scheduling.SaecJobLauncherLocator;
import com.bfds.saec.web.util.JsfUtils;
import com.bfds.scheduling.domain.JobConfig;
import com.bfds.scheduling.service.JobConfigService;
import com.bfds.scheduling.service.JobConfigServiceLocator;
import com.bfds.scheduling.service.SchedulingService;
import com.bfds.scheduling.service.SchedulingServiceLocator;


public class BatchConfigViewModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	final Logger log = LoggerFactory.getLogger(BatchConfigViewModel.class);
	@Autowired
	private transient SchedulingServiceLocator schedulingServiceLocator;
	
	@Autowired
    private transient JobConfigServiceLocator jobConfigServiceLocator;
	
	@Autowired
	private transient SaecJobLauncherLocator saecJobLauncherLocator;
	
	private Boolean remote;
	
	private List<JobConfig> jobConfigList;
	
	private JobConfig selectedJobConfig;
	
	public boolean loadJobConfigList(final MessageContext messages) {
		this.jobConfigList = getJobConfigService().getJobConfigList();
		
		return true;		
	}
	
	public boolean loadJobConfig(final Long id) {
		this.selectedJobConfig= getJobConfigService().findJobConfig(id);
		return true;		
	}

	public boolean executeJob(final Long id, final MessageContext messageContext) {
		JobConfig jobConfig = getJobConfigService().findJobConfig(id);
		if(log.isInfoEnabled())
			log.info(String.format("Executing Job : %s",jobConfig.getJobName()));
		
		saecJobLauncherLocator.get(remote).run(jobConfig);		
		messageContext
				.addMessage(new MessageBuilder()
						.info()
						.defaultText(
								"Batch Execution Request submitted. Check Batch logs for execution status. ")
						.build());
		return true;
	}
	
	public boolean enableJobSchedule(final Long id, final Boolean enabled, final MessageContext messages) {
		getJobConfigService().enableJobSchedule(id, enabled);
		this.jobConfigList = getJobConfigService().getJobConfigList();
		return true;		
	} 
	
	@Transactional
	public boolean saveJobConfig(final MessageContext messageContext) {	
		//TODO: Move validatio to service
		if(!valid(this.selectedJobConfig, messageContext)){
			return false;
		}
		getJobConfigService().saveJobConfig(selectedJobConfig);
		selectedJobConfig = null;
		messageContext
		.addMessage(new MessageBuilder()
				.info()
				.defaultText("Batch Config Updated... ")				
				.build());
		log.info("Batch Config Updated... ");
		return true;		
	}

	/**
	 * If any new dates added as a holiday to holiday_table,updateHolidays() method will be used to register those 
	 * dates to HolidayCalendar.
	 */
	@Transactional
	public boolean updateHolidays(){
		getSchedulingService().registerHolidays();
		return true;
	}
	
	/**
	 * This is required only if the cron expression of one or more jobs is directly updated in the database.
	 */
	@Transactional
	public boolean rescheduleAllJobs(){	
		getSchedulingService().loadJobsAndTriggersConfiguratoin();
		return true;
	}
	
	private boolean valid(JobConfig jobConfig, final MessageContext messageContext) {
		
		Map<String, String> errorMessages = jobConfig.validate();
		
		if(errorMessages.size() == 0) {
			return true;
		}
		for(Map.Entry<String, String> entry : errorMessages.entrySet()) {
			messageContext
			.addMessage(new MessageBuilder().error()
								.source(JsfUtils.getClientId(entry.getKey()))					
								.defaultText(entry.getValue())
								.build());
		}
		
		return false;
	}

	public List<JobConfig> getJobConfigList() {
		return jobConfigList;
	}

	public void setJobConfigList(List<JobConfig> jobConfigList) {
		this.jobConfigList = jobConfigList;
	}

	public JobConfig getSelectedJobConfig() {
		return selectedJobConfig;
	}

	public void setSelectedJobConfig(JobConfig selectedJobConfig) {
		this.selectedJobConfig = selectedJobConfig;
	}

	public SchedulingService getSchedulingService() {
		return schedulingServiceLocator.get(remote);
	}

	public JobConfigService getJobConfigService() {
		return jobConfigServiceLocator.get(remote);
	}

	public void setRemote(Boolean remote) {
		this.remote = remote;
	}	
	
	
}
