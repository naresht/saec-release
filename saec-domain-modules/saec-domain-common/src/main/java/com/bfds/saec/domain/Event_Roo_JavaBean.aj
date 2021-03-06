// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.saec.domain;

import com.bfds.saec.domain.Bank;
import com.bfds.saec.domain.Event;
import com.bfds.saec.domain.EventPaymentConfig;
import com.bfds.saec.domain.FileNotificationConfig;
import com.bfds.saec.domain.SmallCheckConfig;
import com.bfds.saec.domain.reference.AccountType;
import com.bfds.saec.domain.reference.RpoEligibleOption;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

privileged aspect Event_Roo_JavaBean {
    
    public Boolean Event.getHideReminders() {
        return this.hideReminders;
    }
    
    public void Event.setHideReminders(Boolean hideReminders) {
        this.hideReminders = hideReminders;
    }
    
    public String Event.getCode() {
        return this.code;
    }
    
    public void Event.setCode(String code) {
        this.code = code;
    }
    
    public String Event.getName() {
        return this.name;
    }
    
    public void Event.setName(String name) {
        this.name = name;
    }
    
    public String Event.getDescription() {
        return this.description;
    }
    
    public void Event.setDescription(String description) {
        this.description = description;
    }
    
    public String Event.getEventType() {
        return this.eventType;
    }
    
    public void Event.setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    public String Event.getIssuingBankCode() {
        return this.issuingBankCode;
    }
    
    public void Event.setIssuingBankCode(String issuingBankCode) {
        this.issuingBankCode = issuingBankCode;
    }
    
    public Date Event.getStartDate() {
        return this.startDate;
    }
    
    public void Event.setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date Event.getTargetEndDate() {
        return this.targetEndDate;
    }
    
    public void Event.setTargetEndDate(Date targetEndDate) {
        this.targetEndDate = targetEndDate;
    }
    
    public String Event.getBankCode() {
        return this.bankCode;
    }
    
    public void Event.setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    
    public int Event.getBankABANumber() {
        return this.bankABANumber;
    }
    
    public void Event.setBankABANumber(int bankABANumber) {
        this.bankABANumber = bankABANumber;
    }
    
    public String Event.getDda() {
        return this.dda;
    }
    
    public void Event.setDda(String dda) {
        this.dda = dda;
    }
    
    public String Event.getDeutscheBankUserId() {
        return this.deutscheBankUserId;
    }
    
    public void Event.setDeutscheBankUserId(String deutscheBankUserId) {
        this.deutscheBankUserId = deutscheBankUserId;
    }
    
    public String Event.getCheckNameforBottomlineOutFile() {
        return this.checkNameforBottomlineOutFile;
    }
    
    public void Event.setCheckNameforBottomlineOutFile(String checkNameforBottomlineOutFile) {
        this.checkNameforBottomlineOutFile = checkNameforBottomlineOutFile;
    }
    
    public Boolean Event.getTaxAdministrator() {
        return this.taxAdministrator;
    }
    
    public void Event.setTaxAdministrator(Boolean taxAdministrator) {
        this.taxAdministrator = taxAdministrator;
    }
    
    public String Event.getTaxVendor() {
        return this.taxVendor;
    }
    
    public void Event.setTaxVendor(String taxVendor) {
        this.taxVendor = taxVendor;
    }
    
    public Boolean Event.getForeignAccountSolicitation() {
        return this.foreignAccountSolicitation;
    }
    
    public void Event.setForeignAccountSolicitation(Boolean foreignAccountSolicitation) {
        this.foreignAccountSolicitation = foreignAccountSolicitation;
    }
    
    public String Event.getLibraryId() {
        return this.libraryId;
    }
    
    public void Event.setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
    
    public Boolean Event.getClosedEvent() {
        return this.closedEvent;
    }
    
    public void Event.setClosedEvent(Boolean closedEvent) {
        this.closedEvent = closedEvent;
    }
    
    public Date Event.getClosedDate() {
        return this.closedDate;
    }
    
    public void Event.setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }
    
    public Boolean Event.getOmnibusOutreach() {
        return this.omnibusOutreach;
    }
    
    public void Event.setOmnibusOutreach(Boolean omnibusOutreach) {
        this.omnibusOutreach = omnibusOutreach;
    }
    
    public int Event.getCheckStaleInDays() {
        return this.checkStaleInDays;
    }
    
    public void Event.setCheckStaleInDays(int checkStaleInDays) {
        this.checkStaleInDays = checkStaleInDays;
    }
    
    public String Event.getSaecIssueId() {
        return this.saecIssueId;
    }
    
    public void Event.setSaecIssueId(String saecIssueId) {
        this.saecIssueId = saecIssueId;
    }
    
    public Boolean Event.getClaimCuttOff() {
        return this.claimCuttOff;
    }
    
    public void Event.setClaimCuttOff(Boolean claimCuttOff) {
        this.claimCuttOff = claimCuttOff;
    }
    
    public String Event.getComments() {
        return this.comments;
    }
    
    public void Event.setComments(String comments) {
        this.comments = comments;
    }
    
    public Boolean Event.getRequiresTaxInfo() {
        return this.requiresTaxInfo;
    }
    
    public void Event.setRequiresTaxInfo(Boolean requiresTaxInfo) {
        this.requiresTaxInfo = requiresTaxInfo;
    }
    
    public Boolean Event.getTaxableDistribution() {
        return this.taxableDistribution;
    }
    
    public void Event.setTaxableDistribution(Boolean taxableDistribution) {
        this.taxableDistribution = taxableDistribution;
    }
    
    public String Event.getTaxTypePrimary() {
        return this.taxTypePrimary;
    }
    
    public void Event.setTaxTypePrimary(String taxTypePrimary) {
        this.taxTypePrimary = taxTypePrimary;
    }
    
    public String Event.getTaxTypeSecondary() {
        return this.taxTypeSecondary;
    }
    
    public void Event.setTaxTypeSecondary(String taxTypeSecondary) {
        this.taxTypeSecondary = taxTypeSecondary;
    }
    
    public int Event.getTaxableThreshold() {
        return this.taxableThreshold;
    }
    
    public void Event.setTaxableThreshold(int taxableThreshold) {
        this.taxableThreshold = taxableThreshold;
    }
    
    public Set<EventPaymentConfig> Event.getPaymentConfigs() {
        return this.paymentConfigs;
    }
    
    public void Event.setPaymentConfigs(Set<EventPaymentConfig> paymentConfigs) {
        this.paymentConfigs = paymentConfigs;
    }
    
    public Boolean Event.getCureLetterLimit() {
        return this.cureLetterLimit;
    }
    
    public void Event.setCureLetterLimit(Boolean cureLetterLimit) {
        this.cureLetterLimit = cureLetterLimit;
    }
    
    public int Event.getCureLetterLimitNumber() {
        return this.cureLetterLimitNumber;
    }
    
    public void Event.setCureLetterLimitNumber(int cureLetterLimitNumber) {
        this.cureLetterLimitNumber = cureLetterLimitNumber;
    }
    
    public BigDecimal Event.getEventThreshold() {
        return this.eventThreshold;
    }
    
    public void Event.setEventThreshold(BigDecimal eventThreshold) {
        this.eventThreshold = eventThreshold;
    }
    
    public int Event.getCureLetterRangeStart() {
        return this.cureLetterRangeStart;
    }
    
    public void Event.setCureLetterRangeStart(int cureLetterRangeStart) {
        this.cureLetterRangeStart = cureLetterRangeStart;
    }
    
    public int Event.getCureLetterRangeEnd() {
        return this.cureLetterRangeEnd;
    }
    
    public void Event.setCureLetterRangeEnd(int cureLetterRangeEnd) {
        this.cureLetterRangeEnd = cureLetterRangeEnd;
    }
    
    public Boolean Event.getAddressResearch() {
        return this.addressResearch;
    }
    
    public void Event.setAddressResearch(Boolean addressResearch) {
        this.addressResearch = addressResearch;
    }
    
    public Boolean Event.getPreScrub() {
        return this.preScrub;
    }
    
    public void Event.setPreScrub(Boolean preScrub) {
        this.preScrub = preScrub;
    }
    
    public AccountType Event.getAccountType() {
        return this.accountType;
    }
    
    public void Event.setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public RpoEligibleOption Event.getRpoEligible() {
        return this.rpoEligible;
    }
    
    public void Event.setRpoEligible(RpoEligibleOption rpoEligible) {
        this.rpoEligible = rpoEligible;
    }
    
    public Boolean Event.getSendCountLimit() {
        return this.sendCountLimit;
    }
    
    public void Event.setSendCountLimit(Boolean sendCountLimit) {
        this.sendCountLimit = sendCountLimit;
    }
    
    public int Event.getMailSendCountLimit() {
        return this.mailSendCountLimit;
    }
    
    public void Event.setMailSendCountLimit(int mailSendCountLimit) {
        this.mailSendCountLimit = mailSendCountLimit;
    }
    
    public int Event.getPaymentSendCountLimit() {
        return this.paymentSendCountLimit;
    }
    
    public void Event.setPaymentSendCountLimit(int paymentSendCountLimit) {
        this.paymentSendCountLimit = paymentSendCountLimit;
    }
    
    public Set<FileNotificationConfig> Event.getFileNotificationConfigs() {
        return this.fileNotificationConfigs;
    }
    
    public void Event.setFileNotificationConfigs(Set<FileNotificationConfig> fileNotificationConfigs) {
        this.fileNotificationConfigs = fileNotificationConfigs;
    }
    
    public String Event.getNotificationEmailTo() {
        return this.notificationEmailTo;
    }
    
    public void Event.setNotificationEmailTo(String notificationEmailTo) {
        this.notificationEmailTo = notificationEmailTo;
    }
    
    public Long Event.getCheckStartingNo() {
        return this.checkStartingNo;
    }
    
    public void Event.setCheckStartingNo(Long checkStartingNo) {
        this.checkStartingNo = checkStartingNo;
    }
    
    public String Event.getMailDistributionList() {
        return this.mailDistributionList;
    }
    
    public void Event.setMailDistributionList(String mailDistributionList) {
        this.mailDistributionList = mailDistributionList;
    }
    
    public boolean Event.isRequiresAddressPrescrub() {
        return this.requiresAddressPrescrub;
    }
    
    public void Event.setRequiresAddressPrescrub(boolean requiresAddressPrescrub) {
        this.requiresAddressPrescrub = requiresAddressPrescrub;
    }
    
    public String Event.getInfoAgeReviewMailDistributionList() {
        return this.infoAgeReviewMailDistributionList;
    }
    
    public void Event.setInfoAgeReviewMailDistributionList(String infoAgeReviewMailDistributionList) {
        this.infoAgeReviewMailDistributionList = infoAgeReviewMailDistributionList;
    }
    
    public double Event.getCorrespondenceThresholdLimit() {
        return this.correspondenceThresholdLimit;
    }
    
    public void Event.setCorrespondenceThresholdLimit(double correspondenceThresholdLimit) {
        this.correspondenceThresholdLimit = correspondenceThresholdLimit;
    }
    
    public Bank Event.getBank() {
        return this.bank;
    }
    
    public void Event.setBank(Bank bank) {
        this.bank = bank;
    }
    
    public Date Event.getCheckStaleByDate() {
        return this.checkStaleByDate;
    }
    
    public void Event.setCheckStaleByDate(Date checkStaleByDate) {
        this.checkStaleByDate = checkStaleByDate;
    }
    
    public boolean Event.isCanChangeStausOfStaleCheck() {
        return this.canChangeStausOfStaleCheck;
    }
    
    public void Event.setCanChangeStausOfStaleCheck(boolean canChangeStausOfStaleCheck) {
        this.canChangeStausOfStaleCheck = canChangeStausOfStaleCheck;
    }
    
    public Long Event.getMailingControlSequence() {
        return this.mailingControlSequence;
    }
    
    public void Event.setMailingControlSequence(Long mailingControlSequence) {
        this.mailingControlSequence = mailingControlSequence;
    }
    
    public BigDecimal Event.getBankInterest() {
        return this.bankInterest;
    }
    
    public void Event.setBankInterest(BigDecimal bankInterest) {
        this.bankInterest = bankInterest;
    }
    
    public Boolean Event.getUseBottomLineForCheckNoAssignment() {
        return this.useBottomLineForCheckNoAssignment;
    }
    
    public void Event.setUseBottomLineForCheckNoAssignment(Boolean useBottomLineForCheckNoAssignment) {
        this.useBottomLineForCheckNoAssignment = useBottomLineForCheckNoAssignment;
    }
    
    public SmallCheckConfig Event.getSmallCheckConfig() {
        return this.smallCheckConfig;
    }
    
    public void Event.setSmallCheckConfig(SmallCheckConfig smallCheckConfig) {
        this.smallCheckConfig = smallCheckConfig;
    }
    
}
