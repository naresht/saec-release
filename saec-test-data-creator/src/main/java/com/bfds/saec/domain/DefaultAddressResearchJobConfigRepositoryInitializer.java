package com.bfds.saec.domain;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import com.bfds.saec.batch.jobParameters.AccountType;
import com.bfds.saec.batch.jobParameters.ResearchType;
import com.bfds.saec.batch.scheduling.JobConfigFactory;
import com.bfds.saec.util.CsvReader.GarbageLineException;
import com.bfds.saec.util.CsvReader.NoSuchColumnException;
import com.bfds.saec.util.CsvReader.UnsupportedTypeException;
import com.bfds.scheduling.domain.JobConfig;
import com.bfds.scheduling.service.SchedulingService;

@Repository
public class DefaultAddressResearchJobConfigRepositoryInitializer implements
		ApplicationContextAware {

	private ApplicationContext applicationContext;
	@Autowired
    @Qualifier("schedulingServiceImpl")
	private SchedulingService schedulingService;

	@Value("${enableRepositoryInitializer}")
	private Boolean enabled;

	@PostConstruct
	public void initialize() throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		if (skip()) {
			return;
		}

		loadJobConfig();
	}

	private void loadJobConfig() {
		load_corpAddressResearchJobConfig();
		load_individualAddressResearchJobConfig();
		
		load_outCorpAddressResearchJobConfig();
		load_outIndividualAddressResearchJobConfig();
		load_outCorpPreScrubJobConfig();
		load_outIndividualPreScrubJobConfig();
	}

	private void load_corpAddressResearchJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("corporateAddressRecevieJob", "Corporate Address Research", true);
		jc.setVendorName("InfoAge");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		
		jc.getJobParameters().put("researchType", ResearchType.RESEARCH);
        jc.getJobParameters().put("dda", "all");
		jc.persist();

		schedulingService.scheduleJob(jc.merge());
	}


	private void load_individualAddressResearchJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("individualAddressReceiveJob", "Individual Address Research", true);
		jc.setVendorName("InfoAge");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		
		jc.getJobParameters().put("researchType", ResearchType.RESEARCH);
        jc.getJobParameters().put("dda", "all");
		jc.persist();

		schedulingService.scheduleJob(jc.merge());
	}

	private void load_outCorpAddressResearchJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("corporateAddressSendJob", "Out Corp Address Research", false);
		jc.setVendorName("InfoAge");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		
		jc.getJobParameters().put("researchType", ResearchType.RESEARCH);
		jc.getJobParameters().put("accountType", AccountType.CORPORATE);
        jc.getJobParameters().put("dda", "all");
		jc.persist();

		schedulingService.scheduleJob(jc.merge());
	}

	private void load_outIndividualAddressResearchJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("individualAddressSendJob", "Out Individual Address Research", false);
		jc.setVendorName("InfoAge");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		
		jc.getJobParameters().put("researchType", ResearchType.RESEARCH);
		jc.getJobParameters().put("accountType", AccountType.INDIVIDUAL);
        jc.getJobParameters().put("dda", "all");
		jc.persist();

		schedulingService.scheduleJob(jc.merge());
	}
	
	private void load_outCorpPreScrubJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("corporateAddressSendJob", "Out Corp Prescrub", false);
		jc.setVendorName("InfoAge");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		
		jc.getJobParameters().put("researchType", ResearchType.PRESCRUB);
		jc.getJobParameters().put("accountType", AccountType.CORPORATE);
        jc.getJobParameters().put("dda", "all");
		jc.persist();

		schedulingService.scheduleJob(jc.merge());
	}
	
	private void load_outIndividualPreScrubJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("individualAddressSendJob", "Out Individual Prescrub", false);
		jc.setVendorName("InfoAge");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		
		jc.getJobParameters().put("researchType", ResearchType.PRESCRUB);
		jc.getJobParameters().put("accountType", AccountType.INDIVIDUAL);
        jc.getJobParameters().put("dda", "all");
		jc.persist();

		schedulingService.scheduleJob(jc.merge());
	}
	
	

	private boolean skip() {
		if (!enabled) {
			return true;
		}
		try {
			Map map = applicationContext.getBean("testProps", Map.class);
			if (map != null && map.containsKey("skipRepositoryInitializer")) {
				return true;
			}
		} catch (NoSuchBeanDefinitionException e) {

		}

		return false;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}

}
