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

import com.bfds.saec.batch.scheduling.JobConfigFactory;
import com.bfds.saec.util.CsvReader.GarbageLineException;
import com.bfds.saec.util.CsvReader.NoSuchColumnException;
import com.bfds.saec.util.CsvReader.UnsupportedTypeException;
import com.bfds.scheduling.domain.JobConfig;
import com.bfds.scheduling.service.SchedulingService;

@Repository
public class DefaultEncorrJobConfigRepositoryInitializer implements ApplicationContextAware {
	
	private ApplicationContext applicationContext
	;
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
		JobConfig jc = JobConfigFactory.newJobConfig("encorrUploadJob", "Encorr Upload", true);
		jc.setVendorName("Encorr");
		jc.getScheduleConfig().setCronExpression("0 0 20 * * ?");
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
