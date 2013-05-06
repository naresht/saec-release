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

import com.bfds.saec.batch.file.domain.out.dsto.DstoRec;
import com.bfds.saec.batch.scheduling.JobConfigFactory;
import com.bfds.saec.util.CsvReader.GarbageLineException;
import com.bfds.saec.util.CsvReader.NoSuchColumnException;
import com.bfds.saec.util.CsvReader.UnsupportedTypeException;
import com.bfds.scheduling.domain.JobConfig;
import com.bfds.scheduling.service.SchedulingService;

@Repository
public class DefaultJobConfigRepositoryInitializer implements
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
		loadHolidays();
		loadOutJobConfig();

		loadInJobConfig();

		loadStaleDateJobConfig();
		loadOutboundDomesticTaxReccActivityCreateJobConfig();

		loadFollowupRemindersJobConfig();
        loadClaimFormSupportingDocRipperJobConfig();

		schedulingService.registerHolidays();
		schedulingService.loadJobsAndTriggersConfiguratoin();
	}

	private void loadHolidays() {
		// TODO : load from .xls file.

	}

	private void loadOutJobConfig() {
		loaddb_issue_voidJobConfig();
		loaddb_stop_paymentJobConfig();
		loaddsto_check_fileJobConfig();
		loaddsto_print_fileJobConfig();
		loadifds_check_statusJobConfig();
		loadifds_issue_voidJobConfig();
		loadss_issue_voidJobConfig();
		loadss_stop_paymentJobConfig();
		loadss_bottom_line_outJobConfig();
		load_OutNcoaJobConfig();
		load_foreignTaxProcessingOutJob();

	}
	
	
	private void loadStaleDateJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("staleDateJob",
				"StaleDate Job", false);
		jc.setVendorName("Internal to SAEC");
		jc.getScheduleConfig().setCronExpression("0 0 8 * * ?");
		jc.persist();

	}
	
	private void loadFollowupRemindersJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("followupremindersJob",
				"FollowupReminders Job", false);
		jc.setVendorName("Internal to SAEC");
		jc.getScheduleConfig().setCronExpression("0 0 8 * * ?");
		jc.persist();

	}

    private void loadClaimFormSupportingDocRipperJobConfig() {
        JobConfig jc = JobConfigFactory.newJobConfig("uploadedDocRipperJob",
                "Uploaded Doc Ripper Job", false);
        jc.setVendorName("Internal to SAEC");
        jc.getScheduleConfig().setCronExpression("0 0 8 * * ?");
        jc.persist();

    }

	private void loadss_stop_paymentJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("ssStopPaymentJob",
				"SS Stop Payment", false);
		jc.setVendorName("State Street Bank");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.persist();
	}
	
	private void loadss_bottom_line_outJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("bottomlineOutJob",
				"SS Bottomline Out", false);
		jc.setVendorName("State Street Bank");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.persist();
	}

	private void loadss_issue_voidJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("bankIssueVoidJob",
				"SS Issue Void", false);
		jc.setVendorName("State Street Bank");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.getJobParameters().put("bank", "SS");
		jc.persist();
	}

	private void loadifds_issue_voidJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("IfdsIssueVoidJob",
				"IFDS Issue Void", false);
		jc.setVendorName("IFDS");
		jc.getScheduleConfig().setCronExpression("0 0 17 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.persist();
	}

	private void loadifds_check_statusJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("IfdsCheckStatusJob",
				"IFDS Check Status", false);
		jc.setVendorName("IFDS");
		jc.getScheduleConfig().setCronExpression("0 0 17 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.persist();
	}

	private void loaddsto_print_fileJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("dstoPrintFileJob",
				"DSTO Print File", false);
		jc.setVendorName("DSTO");
		jc.getScheduleConfig().setCronExpression("0 0 17-18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.getJobParameters().put("queryProviderFilter.dstoFileType", DstoRec.DstoFileType.PRINTFILE.toString());
		jc.persist();

	}

	private void loaddsto_check_fileJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("dstoCheckFileJob",
				"DSTO Check File", false);
		jc.setVendorName("DSTO");
		jc.getScheduleConfig().setCronExpression("0 0 17-18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.getJobParameters().put("queryProviderFilter.dstoFileType", DstoRec.DstoFileType.CHECKFILE.toString());
		jc.persist();
	}

	private void loaddb_stop_paymentJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("dbStopPaymentJob",
				"DB Stop Payment", false);
		jc.setVendorName("Deutsche Bank");		
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.persist();
	}

	private void loaddb_issue_voidJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("bankIssueVoidJob",
				"DB Issue Void", false);
		jc.setVendorName("Deutsche Bank");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.getJobParameters().put("bank", "DB");
		jc.persist();
	}
	
	private void load_OutNcoaJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("ncoaAddressSendJob","NCOA Address Send", false);
		jc.setVendorName("NCOA");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.persist();
		
		schedulingService.scheduleJob(jc.merge());
	}
	
	private void load_foreignTaxProcessingOutJob() {
		JobConfig jc = JobConfigFactory.newJobConfig("foreignTaxProcessOutJob","Foreign Tax Outbound", false);
		jc.setVendorName("Damasco");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.persist();
	}


	private void loadInJobConfig() {

		loaddb_cashed_checkJobConfig();
		loaddb_stop_confirmationJobConfig();
		loadss_cashed_checkJobConfig();
		loadss_bottom_line_in_JobConfig();
		load_NcoaJobConfig();
		loadDamascoDomesticInJobConfig();
		loadForeignTaxProcessInJobConfig();

	}

	private void loadss_cashed_checkJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("ssCashedCheckJob",
				"SS Cashed Check", true);
		jc.setVendorName("State Street Bank");
		jc.getJobParameters().put("dda", "all");
		jc.persist();

	}
	
	private void loadss_bottom_line_in_JobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("ssBottomlineInJob",
				"SS Bottomline In", true);
		jc.setVendorName("State Street Bank");
		jc.getJobParameters().put("dda", "all");
		jc.persist();

	}


	private void loaddb_stop_confirmationJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("dbStopConfirmationJob",
				"DB Stop Confirmation", true);
		jc.setVendorName("Deutsche Bank");
		jc.getJobParameters().put("dda", "all");
		jc.persist();

	}

	private void loaddb_cashed_checkJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("dbCashedCheckJob",
				"DB Cashed Check", true);
		jc.setVendorName("Deutsche Bank");
		jc.getJobParameters().put("dda", "all");
		jc.persist();

	}
	
	private void load_NcoaJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("ncoaInboundAddressResearchJob", "NCOA Address Research", true);
		jc.setVendorName("NCOA");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.persist();

		schedulingService.scheduleJob(jc.merge());
	}
	
	private void loadDamascoDomesticInJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("taxDomesticInJob", "Damasco Domestic Inbound", true);
		jc.setVendorName("Damasco");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.persist();
	}
	
	private void loadForeignTaxProcessInJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("foreignTaxInboundJob", "Foreign TaxProcess Inbound", true);
		jc.setVendorName("Damasco");
		jc.getScheduleConfig().setCronExpression("0 0 18 * * ?");
		jc.getJobParameters().put("dda", "all");
		jc.persist();
	}
	
	
	private void loadOutboundDomesticTaxReccActivityCreateJobConfig() {
		JobConfig jc = JobConfigFactory.newJobConfig("outboundDomesticTaxReccActivityCreateJob",
				"Tax Domestic Activity CreateJob", false);
		jc.setVendorName("Internal to SAEC");
		jc.getScheduleConfig().setCronExpression("0 0 8 * * ?");
		jc.persist();
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

		return User.findAllUsers().size() > 0;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}

}
