package com.bfds.saec.rpo.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.bfds.saec.batch.rpo.util.AwdRpoWorkType;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Letter;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.domain.reference.RPOType;
import com.bfds.saec.rpo.dto.RPOForwardableNotificationDto;
import com.bfds.saec.rpo.dto.RPONonForwardableNotificationDto;
import com.bfds.saec.rpo.dto.RpoItem;
import com.bfds.saec.util.ConverterUtils;
import com.google.common.base.Preconditions;

public class RpoItemWriter implements ItemWriter<RpoItem>, InitializingBean {

	Logger log = LoggerFactory.getLogger(RpoItemWriter.class);

	@Autowired
	private IMailSender mailSender;

	private EntityManagerFactory entityManagerFactory;

	private RPOForwardableNotificationDto rpoFWDNotificationDto = new RPOForwardableNotificationDto();

	private RPONonForwardableNotificationDto rpoNFWDNotificationDto = new RPONonForwardableNotificationDto();

	private Date rpoFileDate;

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public final void write(List<? extends RpoItem> items) {
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
			List<? extends RpoItem> items) {
		if (log.isDebugEnabled()) {
			log.debug("Writing to JPA with " + items.size() + " items.");
		}

		if (!items.isEmpty()) {
			for (RpoItem item : items) {
				if ((StringUtils.hasText(item.getCheckNo()))) {
					log.info("Updating RPO Check Item");
					updateCheckRpo(item);
				} else if ((StringUtils.hasText(item.getMailControlNo()))) {
					log.info("Updating RPO Letter Item");
					updateMailRpo(item);
				} else {
					log.error("RpoItem has empty checkNo and mailControlNo. One of these two must be present.");
					throw new IllegalArgumentException(
							"RpoItem has empty checkNo and mailControlNo. One of these two must be present.");
				}
			}
		}
	}

	private void updateCheckRpo(final RpoItem item) {
		Preconditions.checkArgument(StringUtils.hasText(item.getCheckNo()),
				"CheckNo cannot be empty");
		Payment payment = Payment.findCheckByNumberAndAmount(item.getCheckNo(),
				item.getCheckAmount().doubleValue());
		Preconditions.checkNotNull(
				payment,
				String.format("Check for RPO not found : %s, %s",
						item.getCheckNo(), item.getCheckAmount()));
		if (payment.canPaymentRpoed()) {
			if (item.getWorkType().equalsIgnoreCase(
					AwdRpoWorkType.FORWARDABLE_CHECK.toString())) {				
				payment.markPaymentRpoForwardable(false);
				rpoFWDNotificationDto
						.setRpoCheckTotalItems(rpoFWDNotificationDto
								.getRpoCheckTotalItems() + 1);
				log.info("RPO Forwardable Check Status changed....");
			} else if (item.getWorkType().equalsIgnoreCase(
					AwdRpoWorkType.NON_FORWARDABLE_CHECK.toString())) {
				payment.markPaymentRpoNonForwardable();
				rpoNFWDNotificationDto
						.setRpoCheckTotalItems(rpoNFWDNotificationDto
								.getRpoCheckTotalItems() + 1);
				log.info("RPO Non-Forwardable Check Status changed....");
			} else {
				throw new IllegalArgumentException(
						"Unknown worktype for check: " + item.getWorkType());
			}
			log.info(String.format("Marked check with checkNo %s RPO",
					item.getCheckNo()));
			payment.merge();
		} else {
			log.info(String.format(
					"Marked check with checkNo %s cannot be RPO(ed) ",
					payment.getIdentificatonNo()));
		}
	}

	private void updateMailRpo(final RpoItem item) {
		Letter letter = Letter.findByMailingControlNo(item.getMailControlNo());
		Preconditions.checkNotNull(
				letter,
				String.format("Letter for RPO not found : %s",
						item.getMailControlNo()));
		if (letter.canLetterRpoed()) {
			if (item.getWorkType().equalsIgnoreCase(
					AwdRpoWorkType.FORWARDABLE_LETTER.toString())) {
				letter.markLetterRpo(RPOType.FORWARDABLE);
				rpoFWDNotificationDto
						.setRpoMailTotalItems(rpoFWDNotificationDto
								.getRpoMailTotalItems() + 1);
				log.info("RPO Forwardable Letter Status changed....");
			} else if (item.getWorkType().equalsIgnoreCase(
					AwdRpoWorkType.NON_FORWARDABLE_LETTER.toString())) {
				letter.markLetterRpo(RPOType.NONFORWARDABLE);
				rpoNFWDNotificationDto
						.setRpoMailTotalItems(rpoNFWDNotificationDto
								.getRpoMailTotalItems() + 1);
				log.info("RPO Non Forwardable Letter Status changed....");
			} else {
				throw new IllegalArgumentException(
						"Unknown worktype for letter: " + item.getWorkType());
			}
			log.info(String.format(
					"Marked letter with mailingControlNo %s RPO",
					item.getMailControlNo()));
			letter.merge();
		} else {
			log.info(String
					.format("Marked letter with mailingControlNo %s cannot be RPO(ed) ",
							letter.getMailingControlNo()));
		}

	}

	@AfterStep
	public void sendNotification() {
		notifyAwdRPOForwardableMail();
		notifyAwdRPONonForwardableMail();
	}

	public void notifyAwdRPOForwardableMail() {
		// TODO Use StringBuilder
		RPOForwardableNotificationDto notification = rpoFWDNotificationDto;
		if (notification.getFileDate() != null) {
			rpoFileDate = notification.getFileDate();
		} else {
			rpoFileDate = new Date();
		}

		notification.setBusiNessArea(Event.getCurrentEvent().getCode());

		String mail = "Business Area :: " + notification.getBusiNessArea()
				+ "\n" + "FileDate :: "
				+ ConverterUtils.getFormatedYearDate(rpoFileDate, "MMddyyyy")
				+ "\n" + "RPO Mail totals :: "
				+ notification.getRpoMailTotalItems() + "\n"
				+ "RPO Check totals :: " + notification.getRpoCheckTotalItems();
		mailSender.send(MailingList.findByCode("batch.jobs.rpo"), getRPOForwardableMailSubject(), mail);
		log.info("Mail sent for RPO Forwardable Items......");
		rpoFWDNotificationDto.setRpoCheckTotalItems(new Integer(0));
		rpoFWDNotificationDto.setRpoMailTotalItems(new Integer(0));
	}

	public void notifyAwdRPONonForwardableMail() {
		// TODO Use StringBuilder
		RPONonForwardableNotificationDto notification = rpoNFWDNotificationDto;
		if (notification.getFileDate() != null) {
			rpoFileDate = notification.getFileDate();
		} else {
			rpoFileDate = new Date();
		}

		notification.setBusiNessArea(Event.getCurrentEvent().getCode());

		String mail = "Business Area :: " + notification.getBusiNessArea()
				+ "\n" + "FileDate :: "
				+ ConverterUtils.getFormatedYearDate(rpoFileDate, "MMddyyyy")
				+ "\n" + "RPO Mail totals :: "
				+ notification.getRpoMailTotalItems() + "\n"
				+ "RPO Check totals :: " + notification.getRpoCheckTotalItems();
		mailSender.send(MailingList.findByCode("batch.jobs.rpo"), getRPONonForwardableMailSubject(), mail);
		log.info("Mail sent for RPO Non-Forwardable Items......");
		rpoNFWDNotificationDto.setRpoCheckTotalItems(new Integer(0));
		rpoNFWDNotificationDto.setRpoMailTotalItems(new Integer(0));
	}

	private String getRPOForwardableMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "RPO FWD Upload_" + currentMonthDateYear + "";
		return subject;
	}

	private String getRPONonForwardableMailSubject() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String currentMonthDateYear = formatter.format(new Date());
		String subject = "RPO NF Upload_" + currentMonthDateYear + "";
		return subject;
	}
}
