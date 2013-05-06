package com.bfds.saec.batch.stale_date;

import java.util.Date;
import java.util.List;

import com.bfds.saec.util.IMailSender;
import com.bfds.scheduling.domain.MailingList;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.bfds.saec.dao.PaymentDao;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.Payment;
import com.bfds.saec.util.ConverterUtils;

public class StaleDateItemWriter implements ItemWriter<Long> {

	private StaleDateNotificationDto notificationDto;
	
	@Autowired
	private PaymentDao dao;
	
	@Autowired
	private IMailSender mailSender;
	
	@Override
	public void write(List<? extends Long> items) throws Exception {
		Payment check = null;
		for(Long checkId : items) {
			check = Payment.findPayment(checkId);
			check.markStaleDated();			
			notificationDto.setChecksStaleDated(notificationDto.getChecksStaleDated() + 1);
		}
	}
	
	/**
	 * dao.getOutstandingCheckCount()-dao.getStaleDatedCheckCount() is done to get only outstanding checks that need to be stale dated
	 */
	@BeforeStep
	public void updateNotificationWithOutstandingCheckCount() {
		notificationDto = new StaleDateNotificationDto();
		notificationDto.setScanDate(new Date());
		notificationDto.setBusinessArea(getBusinessAreaFromConfig());
		notificationDto.setTotalOutstandingChecks(dao.getOutstandingCheckCount()-dao.getStaleDatedCheckCount());
	}

	@AfterStep
	public void sendNotificationMail() {
		mailSender.send(MailingList.findByCode("batch.jobs.checkstaledate"), "Check Stale Date Scan", getNotificationText(notificationDto));
	}
	
	private String getNotificationText(final StaleDateNotificationDto notificationVo) {
		final StringBuilder sb = new StringBuilder("Business Area :: ");
		sb.append(StringUtils.hasText(notificationVo.getBusinessArea()) ? notificationVo.getBusinessArea() : "NA" ).append("\n");
		sb.append("Scan Date ::").append(ConverterUtils.getFormattedDate1(notificationVo.getScanDate())).append("\n");
		sb.append("Total outstanding checks ::").append(notificationVo.getTotalOutstandingChecks()).append("\n");
		sb.append("Total payment objects updated to stale dated ::").append(notificationVo.getChecksStaleDated()).append("\n");
		
		return sb.toString();
	}
	
	private String getBusinessAreaFromConfig() {
		return Event.getCurrentEvent().getCode();
	}

	public StaleDateNotificationDto getNotificationDto() {
		return notificationDto;
	}

	public void setNotificationDto(StaleDateNotificationDto notificationDto) {
		this.notificationDto = notificationDto;
	}
	
		

}
