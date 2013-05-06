package com.bfds.saec.encorr.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.bfds.saec.batch.domain.InRecordStatus;
import com.bfds.saec.batch.encorr.util.EncorrWorkType;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.reference.LetterStatus;
import com.bfds.saec.encorr.dto.EncorrNotificationDto;
import com.bfds.saec.util.ConverterUtils;
import com.google.common.base.Preconditions;

public class EncorrItemWriter implements ItemWriter<EncorrItem>,
		InitializingBean, JobExecutionListener {

	Logger log = LoggerFactory.getLogger(EncorrItemWriter.class);

	@Autowired
	private IMailSender mailSender;

	private EntityManagerFactory entityManagerFactory;

	private EncorrNotificationDto encorrNotification = new EncorrNotificationDto();

	private Date encorrFileDate;

	private Long jobExecutionId = 1L;

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public final void write(List<? extends EncorrItem> items) {
		EntityManager entityManager = EntityManagerFactoryUtils
				.getTransactionalEntityManager(entityManagerFactory);
		if (entityManager == null) {
			throw new DataAccessResourceFailureException(
					"Unable to obtain a transactional EntityManager");
		}
		doWrite(entityManager, items);
		entityManager.flush();
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(entityManagerFactory,
				"An EntityManagerFactory is required");
	}

	protected void doWrite(EntityManager entityManager,
			List<? extends EncorrItem> items) {
		if (log.isDebugEnabled()) {
			log.debug("Writing to JPA with " + items.size() + " items.");
		}

		if (!items.isEmpty()) {
			for (EncorrItem item : items) {
				if ((StringUtils.hasText(item.getMailControlNo()))) {
					updateEncorrMail(item);
				} else {
					log.error("Encorr Item has empty Mail Control Number.");
					throw new IllegalArgumentException(
							"Encorr Item has empty mailControlNo.");
				}
			}
		}
	}

	private void updateEncorrMail(final EncorrItem item) {
		Letter letter = Letter.findByMailingControlNo(item.getMailControlNo());
		if (letter != null
				&& item.getMailControlNo().equals(letter.getMailingControlNo())
				&& letter.getLetterStatus() != LetterStatus.LETTER_SENT) {
			if (item.getWorkType().equalsIgnoreCase(
					EncorrWorkType.TLETTER.toString())) {
				letter.setLetterStatus(LetterStatus.LETTER_SENT);
				letter.setMailDate(new Date());
				encorrNotification.setEncorrTletterItems(encorrNotification
						.getEncorrTletterItems() + 1);
				log.info("Encorr TLetter Status changed....");
			} else if (item.getWorkType().equalsIgnoreCase(
					EncorrWorkType.LETTER.toString())) {
				letter.setLetterStatus(LetterStatus.LETTER_SENT);
				letter.setMailDate(new Date());
				encorrNotification.setEncorrLetterItems(encorrNotification
						.getEncorrLetterItems() + 1);
				log.info("Encorr Letter Status changed....");
			} else {
				item.setStatus(InRecordStatus.CANNOT_PROCESS_SKIPPED);
				item.setJobExecutionId(getJobExecutionId());
				item.persist();
				log.info("Encorr Item " + item.getMailControlNo()
						+ " skipped....");
				throw new IllegalArgumentException(
						"Unknown worktype for letter: " + item.getWorkType());
			}
			log.info(String.format(
					"Marked letter with mailingControlNo %s ENCORR",
					item.getMailControlNo()));
			letter.merge();
		} else {
			item.setJobExecutionId(getJobExecutionId());
			item.setStatus(InRecordStatus.NOT_FOUND_IN_SAEC);
			item.persist();
			// To Do write to file
			log.info("No Match Mail Item ----> " + item.getMailControlNo()
					+ "\t and its WorkType is : " + item.getWorkType());
			encorrNotification.setNoMatchItems(encorrNotification
					.getNoMatchItems() + 1);

		}
	}

	public Long getJobExecutionId() {
		return jobExecutionId;
	}

	@AfterStep
	public void sendNotification() {
		notifyEncorrMail();
	}

	public void notifyEncorrMail() {
		// TODO Use StringBuilder
		EncorrNotificationDto notification = encorrNotification;
		if (notification.getFileDate() != null) {
			encorrFileDate = notification.getFileDate();
		} else {
			encorrFileDate = new Date();
		}

		notification.setBusiNessArea(Event.getCurrentEvent().getCode());

		String mail = "Business Area :: "
				+ notification.getBusiNessArea()
				+ "\n"
				+ "FileDate :: "
				+ ConverterUtils
						.getFormatedYearDate(encorrFileDate, "MMddyyyy") + "\n"
				+ "TLETTER totals :: " + notification.getEncorrTletterItems()
				+ "\n" + "LETTER totals :: "
				+ notification.getEncorrLetterItems() + "\n"
				+ "No match totals :: " + notification.getNoMatchItems();
		mailSender.send(MailingList.findByCode("batch.jobs.encore"), getEncorrMailSubject(), mail);
		log.info("Mail sent for Encorr Letter Items......");
		encorrNotification.setEncorrTletterItems(new Integer(0));
		encorrNotification.setEncorrLetterItems(new Integer(0));
		encorrNotification.setNoMatchItems(new Integer(0));
	}

	private String getEncorrMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "Encorr Upload" + currentMonthDateYear + "";
		return subject;
	}

	@Override
	public void beforeJob(final JobExecution jobExecution) {
		Preconditions.checkArgument(jobExecution.getId() != null,
				"Encorr Job Execution Id is null");
		jobExecutionId = jobExecution.getId();
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO:

	}
}
