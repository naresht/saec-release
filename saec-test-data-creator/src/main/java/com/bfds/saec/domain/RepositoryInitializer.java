package com.bfds.saec.domain;

import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import com.bfds.saec.domain.activity.PhoneCall;
import com.bfds.saec.domain.reference.AddressType;
import com.bfds.saec.domain.reference.BankLov;
import com.bfds.saec.domain.reference.BatchFileLov;
import com.bfds.saec.domain.reference.CallCode;
import com.bfds.saec.domain.reference.CallType;
import com.bfds.saec.domain.reference.CountryLov;
import com.bfds.saec.domain.reference.LetterType;
import com.bfds.saec.domain.reference.MailType;
import com.bfds.saec.domain.reference.PaymentCode;
import com.bfds.saec.domain.reference.PaymentType;
import com.bfds.saec.domain.reference.RPOType;
import com.bfds.saec.domain.reference.ReportCategory;
import com.bfds.saec.domain.reference.RoleType;
import com.bfds.saec.domain.util.AccountContext;
import com.bfds.saec.util.CsvReader;
import com.bfds.saec.util.CsvReader.GarbageLineException;
import com.bfds.saec.util.CsvReader.NoSuchColumnException;
import com.bfds.saec.util.CsvReader.UnsupportedTypeException;
import com.bfds.wss.domain.ClaimantClaim;
import com.bfds.wss.domain.ClaimantTransactions;
import com.bfds.wss.domain.reference.AdditionalQuestions;
import com.bfds.wss.domain.reference.AdditionalQuestionsResponses;
import com.bfds.wss.domain.reference.QuestionGroup;
import com.bfds.wss.domain.reference.QuestionGroupDisplayCode;
import com.bfds.wss.domain.reference.ReminderType;
import com.bfds.wss.domain.reference.ResponseDisplayCode;
import com.bfds.wss.domain.reference.SecurityFund;
import com.bfds.wss.domain.reference.SecurityRef;
import com.bfds.wss.domain.reference.TransactionType;
import com.google.common.collect.Lists;

@Repository
public class RepositoryInitializer implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Value("${enableRepositoryInitializer}")
	private Boolean enabled;

	@PostConstruct
	public void initialize() throws IOException, NoSuchColumnException,
			UnsupportedTypeException, GarbageLineException {
		if (skip()) {
			return;
		}
		initUsers();
		loadCallCodesInotDataBase();
		loadWorkTypes();
		loadLetterCodes();
		loadReportCodes();
		loadCorrespondenceDocuments();
		loadRequestTypes();
		loadBankLovs();
		loadBatchFileLovs();
		loadPaymentComponentLovs();
		loadTaxTypeLovs();
		loadGroupMailCodes();
		loadEvent();

		loadAccounsInotDataBase();
		loadCountryCodes();
		loadReminderTypes();
		loadSecurityFunds();
		//loadTransactionTypes();
		
		
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

	private void initUsers() {
		Role roleCsr = new Role();
		roleCsr.setRoleName(RoleType.CSR.name());
		roleCsr.persist();

		Role roleOps = new Role();
		roleOps.setRoleName(RoleType.OPERATIONS.name());
		roleOps.persist();

		Role roleSupervisor = new Role();
		roleSupervisor.setRoleName(RoleType.SUPERVISOR.name());
		roleSupervisor.persist();

		Role roleOpsManager = new Role();
		roleOpsManager.setRoleName(RoleType.OPS_MANAGER.name());
		roleOpsManager.persist();

		Role roleOtherParties = new Role();
		roleOtherParties.setRoleName(RoleType.OTHER_PARTIES.name());
		roleOtherParties.persist();

		Role roleOutreach = new Role();
		roleOutreach.setRoleName(RoleType.OUTREACH.name());
		roleOutreach.persist();

		Role rolePm = new Role();
		rolePm.setRoleName(RoleType.PROJECT_MANAGER.name());
		rolePm.persist();

		Role roleQA = new Role();
		roleQA.setRoleName(RoleType.QUALITY.name());
		roleQA.persist();

		Role roleAdmin = new Role();
		roleAdmin.setRoleName(RoleType.ADMIN.name());
		roleAdmin.persist();

		User user = new User();
		user.setUserName("csr");
		user.setPassword("csr");
		user.addRole((roleCsr));
		user.merge();

		User user2 = new User();
		user2.setUserName("ops");
		user2.setPassword("ops");
		user2.addRole(roleOps);
		user2.persist();

		User user3 = new User();
		user3.setUserName("supervisor");
		user3.setPassword("supervisor");
		user3.addRole(roleSupervisor);
		user3.persist();

		User user4 = new User();
		user4.setUserName("manager");
		user4.setPassword("manager");
		user4.addRole(roleOpsManager);
		user4.persist();

		User user5 = new User();
		user5.setUserName("other_parties");
		user5.setPassword("other_parties");
		user5.addRole((roleOtherParties));
		user5.persist();

		User user6 = new User();
		user6.setUserName("outreach");
		user6.setPassword("outreach");
		user6.addRole(roleOutreach);
		user6.persist();

		User user7 = new User();
		user7.setUserName("pm");
		user7.setPassword("pm");
		user7.addRole(rolePm);
		user7.persist();

		User user8 = new User();
		user8.setUserName("qa");
		user8.setPassword("qa");
		user8.addRole(roleQA);
		user8.persist();

		User user9 = new User();
		user9.setUserName("admin");
		user9.setPassword("admin");
		user9.addRole(roleAdmin);
		user9.persist();

		// Added for ActiveDirectory Testing
		User user10 = new User();
		user10.setUserName("pmudivarti");
		user10.setPassword("pmudivarti");
		user10.addRole(roleAdmin);
		user10.merge();

		User user11 = new User();
		user11.setUserName("rmalik");
		user11.setPassword("rmalik");
		user11.addRole(roleCsr);
		user11.merge();

		User user12 = new User();
		user12.setUserName("smann");
		user12.setPassword("smann");
		user12.addRole(roleAdmin);
		user12.merge();

		User user13 = new User();
		user13.setUserName("cferreira");
		user13.setPassword("cferreira");
		user13.addRole(roleAdmin);
		user13.merge();

		User user14 = new User();
		user14.setUserName("williamm");
		user14.setPassword("williamm");
		user14.addRole(roleOps);
		user14.merge();

		User user15 = new User();
		user15.setUserName("seperry");
		user15.setPassword("seperry");
		user15.addRole(roleAdmin);
		user15.merge();

		User user16 = new User();
		user16.setUserName("jgenewicz");
		user16.setPassword("jgenewicz");
		user16.addRole(roleAdmin);
		user16.merge();

	}

	private void loadBatchFileLovs() {
		BatchFileLov bl = new BatchFileLov();
		bl.setCode("Bank");
		bl.setDescription("Bank");
		bl.persist();

		bl = new BatchFileLov();
		bl.setCode("IFDS");
		bl.setDescription("IFDS");
		bl.persist();

		bl = new BatchFileLov();
		bl.setCode("InfoAge");
		bl.setDescription("InfoAge");
		bl.persist();

		bl = new BatchFileLov();
		bl.setCode("Damasco");
		bl.setDescription("Damasco");
		bl.persist();

		bl = new BatchFileLov();
		bl.setCode("DSTO");
		bl.setDescription("DSTO");
		bl.persist();

		bl = new BatchFileLov();
		bl.setCode("Encorr");
		bl.setDescription("Encorr");
		bl.persist();

		bl = new BatchFileLov();
		bl.setCode("RPO");
		bl.setDescription("RPO");
		bl.persist();

	}

	private void loadPaymentComponentLovs() {
		PaymentComponentTypeLov bl = new PaymentComponentTypeLov();
		bl.setCode("paymentComp1");
		bl.setDescription("Losses");
		bl.setCategory("");
		bl.persist();

		bl = new PaymentComponentTypeLov();
		bl.setCode("paymentComp2");
		bl.setDescription("Fee Type");
		bl.setCategory("A");
		bl.persist();

		bl = new PaymentComponentTypeLov();
		bl.setCode("paymentComp3");
		bl.setDescription("Interest");
		bl.setCategory("1");
		bl.persist();

		bl = new PaymentComponentTypeLov();
		bl.setCode("paymentComp4");
		bl.setDescription("Other Monies 1");
		bl.setCategory("1");
		bl.persist();

		bl = new PaymentComponentTypeLov();
		bl.setCode("paymentComp5");
		bl.setDescription("OM2");
		bl.setCategory("2");
		bl.persist();

		bl = new PaymentComponentTypeLov();
		bl.setCode("paymentComp6");
		bl.setDescription("paymentComp5");
		bl.setCategory("2");
		bl.persist();

	}

	private void loadGroupMailCodes() {
		GroupMailCode gmc = new GroupMailCode();
		gmc.setCode("101");
		gmc.persist();

		gmc = new GroupMailCode();
		gmc.setCode("102");
		gmc.persist();

		gmc = new GroupMailCode();
		gmc.setCode("103");
		gmc.persist();

		gmc = new GroupMailCode();
		gmc.setCode("104");
		gmc.persist();
	}

	private void loadTaxTypeLovs() {
		TaxTypeLov bl = new TaxTypeLov();
		bl.setCode("1098");
		bl.setDescription("1098");
		bl.setCategory("1098");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1098-C");
		bl.setDescription("1098-C");
		bl.setCategory("1098");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1098-E");
		bl.setDescription("1098-E");
		bl.setCategory("1098");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1098-T");
		bl.setDescription("1098-T");
		bl.setCategory("1098");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-A");
		bl.setDescription("1099-A");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-B");
		bl.setDescription("1099-B");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-C");
		bl.setDescription("1099-C");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-CAP");
		bl.setDescription("1099-CAP");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-DIV");
		bl.setDescription("1099-DIV");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-G");
		bl.setDescription("1099-G");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-H");
		bl.setDescription("1099-H");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-INT");
		bl.setDescription("1099-INT");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-LTC");
		bl.setDescription("1099-LTC");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-MISC");
		bl.setDescription("1099-MISC");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-OID");
		bl.setDescription("1099-OID");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-PATR");
		bl.setDescription("1099-PATR");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-Q");
		bl.setDescription("1099-Q");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-R");
		bl.setDescription("1099-R");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("1099-S");
		bl.setDescription("1099-S");
		bl.setCategory("1099");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("5498");
		bl.setDescription("5498");
		bl.setCategory("5498");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("5498-ESA");
		bl.setDescription("5498-ESA");
		bl.setCategory("5498");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("5498-SA");
		bl.setDescription("5498-SA");
		bl.setCategory("5498");
		bl.persist();

		bl = new TaxTypeLov();
		bl.setCode("W-2G");
		bl.setDescription("W-2G");
		bl.setCategory("W-2G");
		bl.persist();

	}

	private void loadBankLovs() {
		BankLov bl = new BankLov();
		bl.setCode("021001033");
		bl.setDescription("Deutsche Bank");
		bl.persist();

		bl = new BankLov();
		bl.setCode("011000028");
		bl.setDescription("State Street");
		bl.persist();

		bl = new BankLov();
		bl.setCode("");
		bl.setDescription("No bank required");
		bl.persist();

	}

	private void loadCountryCodes() {
		(new CountryLov("AF", "Afghanistan")).persist();
		(new CountryLov("AX", "ÎáÎíÎéland Islands")).persist();
		(new CountryLov("AL", "Albania")).persist();
		(new CountryLov("DZ", "Algeria")).persist();
		(new CountryLov("AS", "American Samoa")).persist();
		(new CountryLov("AD", "Andorra")).persist();
		(new CountryLov("AO", "Angola")).persist();
		(new CountryLov("AI", "Anguilla")).persist();
		(new CountryLov("AQ", "Antarctica")).persist();
		(new CountryLov("AG", "Antigua and Barbuda")).persist();
		(new CountryLov("AR", "Argentina")).persist();
		(new CountryLov("AM", "Armenia")).persist();
		(new CountryLov("AW", "Aruba")).persist();
		(new CountryLov("AU", "Australia")).persist();
		(new CountryLov("AT", "Austria")).persist();
		(new CountryLov("AZ", "Azerbaijan")).persist();
		(new CountryLov("BS", "Bahamas")).persist();
		(new CountryLov("BH", "Bahrain")).persist();
		(new CountryLov("BD", "Bangladesh")).persist();
		(new CountryLov("BB", "Barbados")).persist();
		(new CountryLov("BY", "Belarus")).persist();
		(new CountryLov("BE", "Belgium")).persist();
		(new CountryLov("BZ", "Belize")).persist();
		(new CountryLov("BJ", "Benin")).persist();
		(new CountryLov("BM", "Bermuda")).persist();
		(new CountryLov("BT", "Bhutan")).persist();
		(new CountryLov("BO", "Bolivia")).persist();
		(new CountryLov("BA", "Bosnia and Herzegovina")).persist();
		(new CountryLov("BW", "Botswana")).persist();
		(new CountryLov("BV", "Bouvet Island")).persist();
		(new CountryLov("BR", "Brazil")).persist();
		(new CountryLov("IO", "British Indian Ocean Territory")).persist();
		(new CountryLov("BN", "Brunei Darussalam")).persist();
		(new CountryLov("BG", "Bulgaria")).persist();
		(new CountryLov("BF", "Burkina Faso")).persist();
		(new CountryLov("BI", "Burundi")).persist();
		(new CountryLov("KH", "Cambodia")).persist();
		(new CountryLov("CM", "Cameroon")).persist();
		(new CountryLov("CA", "Canada")).persist();
		(new CountryLov("CV", "Cape Verde")).persist();
		(new CountryLov("KY", "Cayman Islands")).persist();
		(new CountryLov("CF", "Central African Republic")).persist();
		(new CountryLov("TD", "Chad")).persist();
		(new CountryLov("CL", "Chile")).persist();
		(new CountryLov("CN", "China")).persist();
		(new CountryLov("CX", "Christmas Island")).persist();
		(new CountryLov("CC", "Cocos (Keeling) Islands")).persist();
		(new CountryLov("CO", "Colombia")).persist();
		(new CountryLov("KM", "Comoros")).persist();
		(new CountryLov("CG", "Congo")).persist();
		(new CountryLov("CD", "Congo, The Democratic Republic of The"))
				.persist();
		(new CountryLov("CK", "Cook Islands")).persist();
		(new CountryLov("CR", "Costa Rica")).persist();
		(new CountryLov("CI", "Cote D'ivoire")).persist();
		(new CountryLov("HR", "Croatia")).persist();
		(new CountryLov("CU", "Cuba")).persist();
		(new CountryLov("CY", "Cyprus")).persist();
		(new CountryLov("CZ", "Czech Republic")).persist();
		(new CountryLov("DK", "Denmark")).persist();
		(new CountryLov("DJ", "Djibouti")).persist();
		(new CountryLov("DM", "Dominica")).persist();
		(new CountryLov("DO", "Dominican Republic")).persist();
		(new CountryLov("EC", "Ecuador")).persist();
		(new CountryLov("EG", "Egypt")).persist();
		(new CountryLov("SV", "El Salvador")).persist();
		(new CountryLov("GQ", "Equatorial Guinea")).persist();
		(new CountryLov("ER", "Eritrea")).persist();
		(new CountryLov("EE", "Estonia")).persist();
		(new CountryLov("ET", "Ethiopia")).persist();
		(new CountryLov("FK", "Falkland Islands (Malvinas)")).persist();
		(new CountryLov("FO", "Faroe Islands")).persist();
		(new CountryLov("FJ", "Fiji")).persist();
		(new CountryLov("FI", "Finland")).persist();
		(new CountryLov("FR", "France")).persist();
		(new CountryLov("GF", "French Guiana")).persist();
		(new CountryLov("PF", "French Polynesia")).persist();
		(new CountryLov("TF", "French Southern Territories")).persist();
		(new CountryLov("GA", "Gabon")).persist();
		(new CountryLov("GM", "Gambia")).persist();
		(new CountryLov("GE", "Georgia")).persist();
		(new CountryLov("DE", "Germany")).persist();
		(new CountryLov("GH", "Ghana")).persist();
		(new CountryLov("GI", "Gibraltar")).persist();
		(new CountryLov("GR", "Greece")).persist();
		(new CountryLov("GL", "Greenland")).persist();
		(new CountryLov("GD", "Grenada")).persist();
		(new CountryLov("GP", "Guadeloupe")).persist();
		(new CountryLov("GU", "Guam")).persist();
		(new CountryLov("GT", "Guatemala")).persist();
		(new CountryLov("GG", "Guernsey")).persist();
		(new CountryLov("GN", "Guinea")).persist();
		(new CountryLov("GW", "Guinea-bissau")).persist();
		(new CountryLov("GY", "Guyana")).persist();
		(new CountryLov("HT", "Haiti")).persist();
		(new CountryLov("HM", "Heard Island and Mcdonald Islands")).persist();
		(new CountryLov("VA", "Holy See (Vatican City State)")).persist();
		(new CountryLov("HN", "Honduras")).persist();
		(new CountryLov("HK", "Hong Kong")).persist();
		(new CountryLov("HU", "Hungary")).persist();
		(new CountryLov("IS", "Iceland")).persist();
		(new CountryLov("IN", "India")).persist();
		(new CountryLov("ID", "Indonesia")).persist();
		(new CountryLov("IR", "Iran, Islamic Republic of")).persist();
		(new CountryLov("IQ", "Iraq")).persist();
		(new CountryLov("IE", "Ireland")).persist();
		(new CountryLov("IM", "Isle of Man")).persist();
		(new CountryLov("IL", "Israel")).persist();
		(new CountryLov("IT", "Italy")).persist();
		(new CountryLov("JM", "Jamaica")).persist();
		(new CountryLov("JP", "Japan")).persist();
		(new CountryLov("JE", "Jersey")).persist();
		(new CountryLov("JO", "Jordan")).persist();
		(new CountryLov("KZ", "Kazakhstan")).persist();
		(new CountryLov("KE", "Kenya")).persist();
		(new CountryLov("KI", "Kiribati")).persist();
		(new CountryLov("KP", "Korea, Democratic People's Republic of"))
				.persist();
		(new CountryLov("KR", "Korea, Republic of")).persist();
		(new CountryLov("KW", "Kuwait")).persist();
		(new CountryLov("KG", "Kyrgyzstan")).persist();
		(new CountryLov("LA", "Lao People's Democratic Republic")).persist();
		(new CountryLov("LV", "Latvia")).persist();
		(new CountryLov("LB", "Lebanon")).persist();
		(new CountryLov("LS", "Lesotho")).persist();
		(new CountryLov("LR", "Liberia")).persist();
		(new CountryLov("LY", "Libyan Arab Jamahiriya")).persist();
		(new CountryLov("LI", "Liechtenstein")).persist();
		(new CountryLov("LT", "Lithuania")).persist();
		(new CountryLov("LU", "Luxembourg")).persist();
		(new CountryLov("MO", "Macao")).persist();
		(new CountryLov("MK", "Macedonia, The Former Yugoslav Republic of"))
				.persist();
		(new CountryLov("MG", "Madagascar")).persist();
		(new CountryLov("MW", "Malawi")).persist();
		(new CountryLov("MY", "Malaysia")).persist();
		(new CountryLov("MV", "Maldives")).persist();
		(new CountryLov("ML", "Mali")).persist();
		(new CountryLov("MT", "Malta")).persist();
		(new CountryLov("MH", "Marshall Islands")).persist();
		(new CountryLov("MQ", "Martinique")).persist();
		(new CountryLov("MR", "Mauritania")).persist();
		(new CountryLov("MU", "Mauritius")).persist();
		(new CountryLov("YT", "Mayotte")).persist();
		(new CountryLov("MX", "Mexico")).persist();
		(new CountryLov("FM", "Micronesia, Federated States of")).persist();
		(new CountryLov("MD", "Moldova, Republic of")).persist();
		(new CountryLov("MC", "Monaco")).persist();
		(new CountryLov("MN", "Mongolia")).persist();
		(new CountryLov("ME", "Montenegro")).persist();
		(new CountryLov("MS", "Montserrat")).persist();
		(new CountryLov("MA", "Morocco")).persist();
		(new CountryLov("MZ", "Mozambique")).persist();
		(new CountryLov("MM", "Myanmar")).persist();
		(new CountryLov("NA", "Namibia")).persist();
		(new CountryLov("NR", "Nauru")).persist();
		(new CountryLov("NP", "Nepal")).persist();
		(new CountryLov("NL", "Netherlands")).persist();
		(new CountryLov("AN", "Netherlands Antilles")).persist();
		(new CountryLov("NC", "New Caledonia")).persist();
		(new CountryLov("NZ", "New Zealand")).persist();
		(new CountryLov("NI", "Nicaragua")).persist();
		(new CountryLov("NE", "Niger")).persist();
		(new CountryLov("NG", "Nigeria")).persist();
		(new CountryLov("NU", "Niue")).persist();
		(new CountryLov("NF", "Norfolk Island")).persist();
		(new CountryLov("MP", "Northern Mariana Islands")).persist();
		(new CountryLov("NO", "Norway")).persist();
		(new CountryLov("OM", "Oman")).persist();
		(new CountryLov("PK", "Pakistan")).persist();
		(new CountryLov("PW", "Palau")).persist();
		(new CountryLov("PS", "Palestinian Territory, Occupied")).persist();
		(new CountryLov("PA", "Panama")).persist();
		(new CountryLov("PG", "Papua New Guinea")).persist();
		(new CountryLov("PY", "Paraguay")).persist();
		(new CountryLov("PE", "Peru")).persist();
		(new CountryLov("PH", "Philippines")).persist();
		(new CountryLov("PN", "Pitcairn")).persist();
		(new CountryLov("PL", "Poland")).persist();
		(new CountryLov("PT", "Portugal")).persist();
		(new CountryLov("PR", "Puerto Rico")).persist();
		(new CountryLov("QA", "Qatar")).persist();
		(new CountryLov("RE", "Reunion")).persist();
		(new CountryLov("RO", "Romania")).persist();
		(new CountryLov("RU", "Russian Federation")).persist();
		(new CountryLov("RW", "Rwanda")).persist();
		(new CountryLov("SH", "Saint Helena")).persist();
		(new CountryLov("KN", "Saint Kitts and Nevis")).persist();
		(new CountryLov("LC", "Saint Lucia")).persist();
		(new CountryLov("PM", "Saint Pierre and Miquelon")).persist();
		(new CountryLov("VC", "Saint Vincent and The Grenadines")).persist();
		(new CountryLov("WS", "Samoa")).persist();
		(new CountryLov("SM", "San Marino")).persist();
		(new CountryLov("ST", "Sao Tome and Principe")).persist();
		(new CountryLov("SA", "Saudi Arabia")).persist();
		(new CountryLov("SN", "Senegal")).persist();
		(new CountryLov("RS", "Serbia")).persist();
		(new CountryLov("SC", "Seychelles")).persist();
		(new CountryLov("SL", "Sierra Leone")).persist();
		(new CountryLov("SG", "Singapore")).persist();
		(new CountryLov("SK", "Slovakia")).persist();
		(new CountryLov("SI", "Slovenia")).persist();
		(new CountryLov("SB", "Solomon Islands")).persist();
		(new CountryLov("SO", "Somalia")).persist();
		(new CountryLov("ZA", "South Africa")).persist();
		(new CountryLov("GS", "South Georgia and The South Sandwich Islands"))
				.persist();
		(new CountryLov("ES", "Spain")).persist();
		(new CountryLov("LK", "Sri Lanka")).persist();
		(new CountryLov("SD", "Sudan")).persist();
		(new CountryLov("SR", "Suriname")).persist();
		(new CountryLov("SJ", "Svalbard and Jan Mayen")).persist();
		(new CountryLov("SZ", "Swaziland")).persist();
		(new CountryLov("SE", "Sweden")).persist();
		(new CountryLov("CH", "Switzerland")).persist();
		(new CountryLov("SY", "Syrian Arab Republic")).persist();
		(new CountryLov("TW", "Taiwan, Province of China")).persist();
		(new CountryLov("TJ", "Tajikistan")).persist();
		(new CountryLov("TZ", "Tanzania, United Republic of")).persist();
		(new CountryLov("TH", "Thailand")).persist();
		(new CountryLov("TL", "Timor-leste")).persist();
		(new CountryLov("TG", "Togo")).persist();
		(new CountryLov("TK", "Tokelau")).persist();
		(new CountryLov("TO", "Tonga")).persist();
		(new CountryLov("TT", "Trinidad and Tobago")).persist();
		(new CountryLov("TN", "Tunisia")).persist();
		(new CountryLov("TR", "Turkey")).persist();
		(new CountryLov("TM", "Turkmenistan")).persist();
		(new CountryLov("TC", "Turks and Caicos Islands")).persist();
		(new CountryLov("TV", "Tuvalu")).persist();
		(new CountryLov("UG", "Uganda")).persist();
		(new CountryLov("UA", "Ukraine")).persist();
		(new CountryLov("AE", "United Arab Emirates")).persist();
		(new CountryLov("GB", "United Kingdom")).persist();
		(new CountryLov("US", "United States")).persist();
		(new CountryLov("UM", "United States Minor Outlying Islands"))
				.persist();
		(new CountryLov("UY", "Uruguay")).persist();
		(new CountryLov("UZ", "Uzbekistan")).persist();
		(new CountryLov("VU", "Vanuatu")).persist();
		(new CountryLov("VE", "Venezuela")).persist();
		(new CountryLov("VN", "Viet Nam")).persist();
		(new CountryLov("VG", "Virgin Islands, British")).persist();
		(new CountryLov("VI", "Virgin Islands, U.S.")).persist();
		(new CountryLov("WF", "Wallis and Futuna")).persist();
		(new CountryLov("EH", "Western Sahara")).persist();
		(new CountryLov("YE", "Yemen")).persist();
		(new CountryLov("ZM", "Zambia")).persist();
		(new CountryLov("ZW", "Zimbabwe")).persist();

	}

	private void loadLetterCodes() {
		
		
		
		(new LetterCode("600", "Claim Form", LetterType.CLAIM_FORM)).persist(); // This code value is being used.
		(new LetterCode("601", "Optout Form", LetterType.OPTOUT_FORM)).persist(); // This code value is being used.

		// //////////////////////////////////
		(new LetterCode("101", "Payee Change Request (I)",
				LetterType.GENERAL_CORRESPONDENCE)).persist();
		(new LetterCode("102", "IRA Payee Change (I)",
				LetterType.GENERAL_CORRESPONDENCE)).persist();
		(new LetterCode("103", "Retirement acct payee change (I)",
				LetterType.GENERAL_CORRESPONDENCE)).persist();
		(new LetterCode("104", "Custodian Change Request (I)",
				LetterType.GENERAL_CORRESPONDENCE)).persist();
		(new LetterCode("105", "Stop/Replace/Reissue Request (I)",
				LetterType.GENERAL_CORRESPONDENCE)).persist();
		(new LetterCode("106", "Address Change (I)",
				LetterType.GENERAL_CORRESPONDENCE)).persist();
		(new LetterCode("107", "Return of Funding (I)",
				LetterType.GENERAL_CORRESPONDENCE)).persist();
		(new LetterCode("108", "Broker Dealer/TPA Check Return (I)",
				LetterType.GENERAL_CORRESPONDENCE)).persist();
		(new LetterCode("109", "Other (I)", LetterType.GENERAL_CORRESPONDENCE))
				.persist();

		(new LetterCode("201", "Claim Form - Blank (I)", LetterType.CLAIM_FORM))
				.persist();
		(new LetterCode("202", "Claim Form - Not signed (I)",
				LetterType.CLAIM_FORM)).persist();
		(new LetterCode("203", "Claim Form - Wrong signature (I)",
				LetterType.CLAIM_FORM)).persist();
		(new LetterCode("204", "Claim Form - Acknowledgement (I)",
				LetterType.CLAIM_FORM)).persist();
		(new LetterCode("301", "Opt Out - Not signed (I)",
				LetterType.OPTOUT_FORM)).persist();
		(new LetterCode("302", "Opt Out - Wrong signature (I)",
				LetterType.OPTOUT_FORM)).persist();
		(new LetterCode("303", "Oout - Missing requirements (I)",
				LetterType.OPTOUT_FORM)).persist();

		// ////////////////////////////////
		(new LetterCode("701",
				"Return of Funding � Need Multiple Account Information",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("702", "UGMA Cust Change ",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("703", "UGMA Payee Change",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("704", "Abandoned Retirement Plan",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("705", "Abandoned Retirement Plan/Bene",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("706", "Active Retirement Account Plan- Current Plan",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("707",
				"Active Retirement Acct Plan/Bene- Current Plan",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("708", "Closed for Check Processing",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("709", "Contact Us",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("710", "Copy of Presented Check",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("711", "Corp Dissolution",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("712", "Courtesy Letters",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("713", "Cover letter for Fulfillment Request",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("714", "Death Joint Tenant Payee Change both tenants",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode(
				"715",
				"Death Joint Tenant Payee Change both tenants other than surviving tenant",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("716",
				"Death Joint Tenant Payee Change both tenants Small Estate",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("717", "Death Payee Change",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("718", "Death UGMA Cust Child is Majority",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("719", "Death UGMA Cust Child is Minor",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("720", "Death/JT Tenant Payee Change",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("721", "Divorce Payee Change",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("722", "Invest Club Dissolution Payee Change",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("723", "IRA Payee Change",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("724", "IRA Payee Change/Death",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("725", "Legal Name Payee Change",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("726", "Medallion Failed /Illegible",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("727", "Medallion Failed /Ink",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("728", "Non-ERISA 403b Payee Change",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("729", "Non-ERISA 403b Payee Change Death ",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("730", "Partnership Dissolution Payee Change",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("731", "Payee change Corp Dissolution",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("732", "Restitution Amount Concern",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("733", "Restitution Amt Concern- No Payment Recvd.",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("734",
				"Return of Funding- Additional Check Data Needed",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("735", "Return of Funding- Incorrect registration",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("736", "Return of Funding �Stale check",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("737", "Return of Funding- Voided Check",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("738", "Trustee Death Payee Change",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("739", "Trustee resigns",
				LetterType.GENERAL_CORRESPONDENCE_CURE_LETTER)).persist();
		(new LetterCode("740", "Claim form - Blank",
				LetterType.CLAIM_FORM_CURE_LETTER)).persist();
		(new LetterCode("741", "Claim form - Unsigned",
				LetterType.CLAIM_FORM_CURE_LETTER)).persist();
		(new LetterCode("742", "Claim form - Wrong signature",
				LetterType.CLAIM_FORM_CURE_LETTER)).persist();
		(new LetterCode("743", "Claim form - Acknowledgement",
				LetterType.CLAIM_FORM_CURE_LETTER)).persist();
		(new LetterCode("744", "Opt out - Not signed",
				LetterType.OPTOUT_CURE_LETTER)).persist();
		(new LetterCode("745", "Opt out - Wrong signature",
				LetterType.OPTOUT_CURE_LETTER)).persist();
		(new LetterCode("746", "Opt out - Missing requirements",
				LetterType.OPTOUT_CURE_LETTER)).persist();

		(new User()).flush();
		(new User()).clear();
	}

	private void loadWorkTypes() {
		WorkType addrChngWorkType = new WorkType();
		addrChngWorkType.setDescription("TADDRSCHNG");
		addrChngWorkType.persist();
		
		WorkType chkReplaceWorkType = new WorkType();
		chkReplaceWorkType.setDescription("TCHKRPLACE");
		chkReplaceWorkType.persist();
		
		WorkType workType = new WorkType();
		workType.setDescription("SPECHANDL");
		workType.persist();
		
		WorkType tletterWorkType = new WorkType();
		tletterWorkType.setDescription("TLETTER");
		tletterWorkType.persist();
		
		WorkType tirapaychnWorkType = new WorkType();
		tirapaychnWorkType.setDescription("TIRAPAYCHN");
		tirapaychnWorkType.persist();
		
		WorkType tothpaychngWorkType = new WorkType();
		tothpaychngWorkType.setDescription("TOTHPAYCHN");
		tothpaychngWorkType.persist();
		
		WorkType tcstdchngWorkType = new WorkType();
		tcstdchngWorkType.setDescription("TCSTDCHNG");
		tcstdchngWorkType.persist();
		
		WorkType tnrapaychnWorkType = new WorkType();
		tnrapaychnWorkType.setDescription("TNRAPAYCHN");
		tnrapaychnWorkType.persist();
		
		WorkType tmiscworkWorkType = new WorkType();
		tmiscworkWorkType.setDescription("TMISCWORK");
		tmiscworkWorkType.persist();
		
		WorkType tfulfilmenWorkType = new WorkType();
		tfulfilmenWorkType.setDescription("TFULFILMEN");
		tfulfilmenWorkType.persist();
		
		WorkType webClaimWorkType = new WorkType();
		webClaimWorkType.setDescription("WebClaim1");
		webClaimWorkType.persist();
	}

	private void loadReportCodes() {
		(new ReportCode("101", "Broker Roll-up", ReportCategory.OUTREACH))
				.persist();
		(new ReportCode("102", "Data Extract", ReportCategory.OUTREACH))
				.persist();
		(new ReportCode("201", "Distribution Facts",
				ReportCategory.CHECK_DISTRIBUTION)).persist();
		(new ReportCode("202", "DSTO Check File Summary",
				ReportCategory.CHECK_DISTRIBUTION)).persist();
		(new ReportCode("203", "Stratification (Editable Dollar Bands)",
				ReportCategory.CHECK_DISTRIBUTION)).persist();
		(new ReportCode("204", "Issue/Void/Cancel/Check File",
				ReportCategory.CHECK_DISTRIBUTION)).persist();
		(new ReportCode("301", "High Dollar Outreach",
				ReportCategory.CHECK_STATUS)).persist();
		(new ReportCode("302", "Check Summary Dollar Bands",
				ReportCategory.CHECK_STATUS)).persist();
		(new ReportCode("303",
				"Checks Stopped, Replaced, Voided, Void/Reissue",
				ReportCategory.CHECK_STATUS)).persist();
		(new ReportCode("304", "Bank Stop Conformation File",
				ReportCategory.CHECK_STATUS)).persist();
		(new ReportCode("305", "Bank Paid Check File",
				ReportCategory.CHECK_STATUS)).persist();
		(new ReportCode("306", "Checks Held", ReportCategory.CHECK_STATUS))
				.persist();
		(new ReportCode("307", "Checks Released/Deleted",
				ReportCategory.CHECK_STATUS)).persist();
		(new ReportCode("308", "Check Void/Removed",
				ReportCategory.CHECK_STATUS)).persist();
		(new ReportCode("309", "No Issue", ReportCategory.CHECK_STATUS))
				.persist();
		(new ReportCode("310", "Alternate Payee", ReportCategory.CHECK_STATUS))
				.persist();
		(new ReportCode("501", "Registration and Address Change",
				ReportCategory.RPO)).persist();
		(new ReportCode("502", "RPO Summary", ReportCategory.RPO)).persist();
		(new ReportCode("503", "RPO Forwardable", ReportCategory.RPO))
				.persist();
		(new ReportCode("504", "RPO Non Forwardable", ReportCategory.RPO))
				.persist();
		(new ReportCode("505", "Voided RPO", ReportCategory.RPO)).persist();
		(new ReportCode("601", "Class Action Forms",
				ReportCategory.CLASS_ACTION)).persist();
		(new ReportCode("602", "Claim Summary", ReportCategory.CLASS_ACTION))
				.persist();
		(new ReportCode("701", "ROF By Account", ReportCategory.ROF)).persist();
		(new ReportCode("702", "New Check for ROF", ReportCategory.ROF))
				.persist();
		(new ReportCode("801", "Damasco Unsent Accounts (Outgoing)",
				ReportCategory.TAX)).persist();
		(new ReportCode("802", "Damasco Summary (Incoming)", ReportCategory.TAX))
				.persist();
		(new ReportCode("803", "Damasco Exception (Incoming)",
				ReportCategory.TAX)).persist();
		(new ReportCode("901", "InfoAge Billing", ReportCategory.MISC))
				.persist();
		(new ReportCode("902", "SAS Users", ReportCategory.MISC)).persist();
		(new ReportCode("903", "Telephone Reason Summary", ReportCategory.MISC))
				.persist();
	}

	private void loadCorrespondenceDocuments() {
		CorrespondenceDocument cd = new CorrespondenceDocument(
				"Letter of instruction");
		cd.persist();

		cd = new CorrespondenceDocument("Death Certificate");
		cd.persist();

		cd = new CorrespondenceDocument("Affidavit of Domicile");
		cd.persist();

		cd = new CorrespondenceDocument("Medalian guarantee");
		cd.persist();

		cd = new CorrespondenceDocument("Power of attorney");
		cd.persist();

		cd = new CorrespondenceDocument("Trust Documents");
		cd.persist();

		cd = new CorrespondenceDocument("Marriage certificate");
		cd.persist();

		cd = new CorrespondenceDocument("Birth Certificate");
		cd.persist();

		cd = new CorrespondenceDocument("Medallion/Signature guarantee");
		cd.persist();

		cd = new CorrespondenceDocument("W8 Tax Certification");
		cd.persist();

		cd = new CorrespondenceDocument("W9 Tax Certification");
		cd.persist();

		cd = new CorrespondenceDocument("W4P Form");
		cd.persist();

		cd = new CorrespondenceDocument("Miscellaneous Documents");
		cd.persist();

		cd = new CorrespondenceDocument("Completed Form");
		cd.persist();

		cd = new CorrespondenceDocument("BF/TPA Returned check");
		cd.persist();

		cd = new CorrespondenceDocument("Evidence of Appointment");
		cd.persist();

		cd = new CorrespondenceDocument("Corporate Resolution");
		cd.persist();

		cd = new CorrespondenceDocument("Divorce Decree");
		cd.persist();

		cd = new CorrespondenceDocument("Non Retirement Payee Change");
		cd.persist();

		cd = new CorrespondenceDocument("Small Estate Affidavit");
		cd.persist();

	}

	private void loadRequestTypes() {
		com.bfds.saec.domain.RequestType rt = new com.bfds.saec.domain.RequestType(
				"Address Change");
		rt.setMailType(MailType.INCOMING);
		rt.persist();

		rt = new com.bfds.saec.domain.RequestType("Death Payee Change");
		rt.setMailType(MailType.INCOMING);
		rt.persist();

		rt = new com.bfds.saec.domain.RequestType("Trustee Change");
		rt.setMailType(MailType.INCOMING);
		rt.persist();

		rt = new com.bfds.saec.domain.RequestType("Address Change - 1");
		rt.setMailType(MailType.OUTGOING);
		rt.persist();

		rt = new com.bfds.saec.domain.RequestType("Death Payee Change - 1");
		rt.setMailType(MailType.OUTGOING);
		rt.persist();

		rt = new com.bfds.saec.domain.RequestType("Trustee Change - 1");
		rt.setMailType(MailType.OUTGOING);
		rt.persist();

	}

	private void loadEvent() {
		Event e = Event.newEvent();
		e.setCode("E001");
		e.setDda("11111111");
		e.setDescription("Some Event!!!");
		e.setRequiresTaxInfo(true);
		e.setCheckStaleByDate(new Date(100, 1, 1));
		e.persist();

	}

	private void loadCallCodesInotDataBase() {
		CallCode c = new CallCode();
		c.setCode("1");
		c.setDescription("General Settlement Question.");
		c.persist();

		c = new CallCode();
		c.setCode("2");
		c.setDescription("Check Question.");
		c.persist();

		c = new CallCode();
		c.setCode("3");
		c.setDescription("Distribution Question.");
		c.persist();

		c = new CallCode();
		c.setCode("4");
		c.setDescription("Angry caller.");
		c.persist();

		c = new CallCode();
		c.setCode("5");
		c.setDescription("Mad caller.");
		c.persist();

		c = new CallCode();
		c.setCode("6");
		c.setDescription("Letter question.");
		c.persist();

		c = new CallCode();
		c.setCode("7");
		c.setDescription("Registration Update.");
		c.persist();

		c = new CallCode();
		c.setCode("8");
		c.setDescription("Address Update.");
		c.persist();

		c = new CallCode();
		c.setCode("9");
		c.setDescription("Contact Info Update.");
		c.persist();

		// c = new CallCode();
		// c.setCode("10");
		// c.setDescription("aaa.");
		// c.persist();
		//
		// c = new CallCode();
		// c.setCode("11");
		// c.setDescription("bbb.");
		// c.persist();
		//
		// c = new CallCode();
		// c.setCode("12");
		// c.setDescription("ccc.");
		// c.persist();
		//
		// c = new CallCode();
		// c.setCode("13");
		// c.setDescription("dddd.");
		// c.persist();
		//
		// c = new CallCode();
		// c.setCode("14");
		// c.setDescription("eee.");
		// c.persist();
		//
		// c = new CallCode();
		// c.setCode("15");
		// c.setDescription("fff.");
		// c.persist();
		//
		// c = new CallCode();
		// c.setCode("16");
		// c.setDescription("gggg.");
		// c.persist();
		//

	}

	public void loadAccounsInotDataBase() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		for (Claimant c : getClaimantsFromCsvFile()) {
			c.persist();
			addLetters(c);
		}
	}

	private List<Claimant> getClaimantsFromCsvFile() throws IOException,
			NoSuchColumnException, UnsupportedTypeException,
			GarbageLineException {
		List<Claimant> ret = new ArrayList<Claimant>();
		CsvReader csvr = new CsvReader(
				"/csv/AccountsTest_import_accounts_10.csv", false, true, true);
		csvr.setColumn("Number", String.class);
		csvr.setColumn("GivenName", String.class);
		csvr.setColumn("MiddleInitial", String.class);
		csvr.setColumn("Surname", String.class);
		csvr.setColumn("StreetAddress", String.class);
		csvr.setColumn("City", String.class);
		csvr.setColumn("State", String.class);
		csvr.setColumn("ZipCode", String.class);
		csvr.setColumn("Country", String.class);

		csvr.setColumn("EmailAddress", String.class);
		csvr.setColumn("TelephoneNumber", String.class);
		csvr.setColumn("NationalID", String.class);
		while (true) {
			try {
				Map<String, Object> line = csvr.readLine();
				ret.add(newClaimant(line));
			} catch (EOFException e) {
				break;
			}
		}
		addCallActivity(ret);
		addChecks(ret);
		// addLetters(ret);
		return ret;
	}

	private void addLetters(Claimant claimant) {
		Letter l1 = new Letter();
		l1.setLetterCode(LetterCode.findByDescription("Claim Form - Blank (I)"));
		l1.setMailDate(new Date());
		l1.setMailType(MailType.INCOMING);
		l1.setUserId("system");
		l1.setClaimant(claimant);
		l1.persist();

		l1 = new Letter();
		l1.setMailingControlNo(l1.getNextMailingControlNumber());
		l1.setMailDate(new Date());
		l1.setMailType(MailType.OUTGOING);
		l1.setLetterCode(LetterCode.findByDescription("Courtesy Letters"));
		l1.setDescription("Courtesy Letters");
		l1.setAuditable(true);
		l1.setSpecialPull(true);
		l1.setRpoDate(new Date());
		l1.setComments("");
		l1.setUserId("system");
		l1.setLetterCodeString("L1");
		l1.setClaimant(claimant);
		l1.persist();

		l1 = new Letter();
		l1.setMailingControlNo(l1.getNextMailingControlNumber());
		l1.setMailDate(new Date());
		l1.setMailType(MailType.OUTGOING);
		l1.setLetterCode(LetterCode.findByDescription("Claim form - Blank"));
		l1.setDescription("Claim form - Blank");
		l1.setAuditable(true);
		l1.setSpecialPull(true);
		l1.setRpoDate(new Date());
		l1.setComments("");
		l1.setUserId("system");
		l1.setLetterCodeString("L1");
		l1.setClaimant(claimant);
		l1.persist();

	}

	private void addChecks(List<Claimant> ret) {
		for (int i = 0; i < ret.size(); i++) {
			// if(i % 5 ==0) {
			addChecks(ret.get(i), i);
			// }
		}

	}

	private void addChecks(Claimant claimant, int counter) {

		Payment c = Payment.newPayment(PaymentCode.FIRST_ISSUE_CREATED_FI_FI);
		c.setPaymentType(PaymentType.CHECK);
		c.setSpecialPullCode("SP-1");
		c.setIdentificatonNo(getUniqueCheckNumber());
		c.setMailingControlNo(c.getIdentificatonNo());
		c.setPaymentAmount(new BigDecimal(100));
		c.setPayTo(claimant);
		c.setRpoType(RPOType.NONFORWARDABLE);
		claimant.addCheck(c);

		c = Payment.newPayment(PaymentCode.WIRE_REQUESTED_W_W);
		c.setPaymentType(PaymentType.WIRE);
		c.setSpecialPullCode("SP-1");
		// c.setIdentificatonNo(String.valueOf(25 + 20 * counter));
		// c.setIdentificatonNo(getUniqueCheckNumber());
		c.setMailingControlNo(c.getIdentificatonNo());
		c.setPaymentAmount(new BigDecimal(25 + 200 * counter));
		c.setStatusChangeDate(new Date(101, 7, 14));
		c.setPayTo(claimant);
		c.setRpoType(RPOType.NONFORWARDABLE);
		claimant.addCheck(c);

	}

	private void addCallActivity(List<Claimant> ret) {
		for (int i = 0; i < ret.size(); i++) {
			// if(i % 5 ==0) {
			addCallActivity(ret.get(i));
			// }
		}
	}

	private void addCallActivity(Claimant claimant) {
		newPhoneCall(claimant, newDate(1, 10), CallType.INBOUND, "Adam");
		newPhoneCall(claimant, newDate(1, 11), CallType.OUTBOUND, "Adam");
		newPhoneCall(claimant, newDate(1, 12), CallType.INBOUND, "Jack");
		newPhoneCall(claimant, newDate(1, 13), CallType.OUTBOUND, "Jack");
		newPhoneCall(claimant, newDate(1, 14), CallType.INBOUND, "Jack");
		newPhoneCall(claimant, newDate(1, 15), CallType.OUTBOUND, "Adam");
		newPhoneCall(claimant, newDate(1, 16), CallType.INBOUND, "Jack");
		newPhoneCall(claimant, newDate(1, 17), CallType.OUTBOUND, "Jack");
		newPhoneCall(claimant, newDate(1, 18), CallType.INBOUND, "Jack");
		newPhoneCall(claimant, newDate(1, 19), CallType.OUTBOUND, "Adam");
		newPhoneCall(claimant, newDate(1, 20), CallType.INBOUND, "Jack");
		newPhoneCall(claimant, newDate(1, 21), CallType.OUTBOUND, "Adam");
		newPhoneCall(claimant, newDate(1, 22), CallType.INBOUND, "Jack");
		newPhoneCall(claimant, newDate(1, 23), CallType.OUTBOUND, "Adam");
		newPhoneCall(claimant, newDate(1, 24), CallType.INBOUND, "Jack");
		newPhoneCall(claimant, newDate(1, 25), CallType.OUTBOUND, "Adam");
		newPhoneCall(claimant, newDate(1, 26), CallType.INBOUND, "Adam");
		newPhoneCall(claimant, newDate(1, 27), CallType.OUTBOUND, "Adam");
	}

	public final Date newDate(int month, int date) {
		Calendar c = Calendar.getInstance();
		c.set(2011, month, date);
		return c.getTime();
	}

	private PhoneCall newPhoneCall(Claimant claimant, Date date,
			CallType calltype, String user) {
		PhoneCall call = new PhoneCall();
		call.setActivityDate(date);
		call.setCallType(calltype);
		call.setClaimant(claimant);
		call.setComments("Called for blah blah blah.");
		call.setUserId(user);
		List calls = CallCode.findAllCallCodes();
		if (calls != null) {
			if (calls.size() > 3) {
				call.getCallCode().addAll(calls.subList(0, 3));
			} else {
				call.getCallCode().addAll(calls.subList(0, calls.size()));
			}
		}
		claimant.getPhoneCall().add(call);
		return call;
	}

	private Claimant newClaimant(Map<String, Object> line) {
		Claimant p = new Claimant();
		String id = (String) line.get("Number");
		// p.setReferenceNo(id);
		p.setFundAccountNo(id + "12345111" + id);
		p.setAccountStatus("Open");
		p.setAccountType("Custodial"); // ???
		p.setBrokerId("BRK-" + id + "121212" + id);
		p.setBin("BIN-" + id + "34334" + id);
		p.setOmniBus(Integer.parseInt(id) % 50 == 0);
		p.setSsn((String) line.get("NationalID"));
		p.setCertificationStatus("Certified"); // E
		final ClaimantEmploymentHistory empHistory = new ClaimantEmploymentHistory();
		empHistory.setServiceStartDate1(new Date());
		empHistory.setServiceStartDate2(new Date());
		empHistory.setServiceEndDate1(new Date());
		empHistory.setServiceEndDate2(new Date());
		p.setEmploymentHistory(empHistory);
		p.setCertificationType("W9-US Citizen");// E
		p.setUsCitizen(Boolean.TRUE);
		p.setTaxCountryCode("US");
		p.setCertificationDate(new Date());
		p.setCreatedBy(AccountContext.getCurrentUsername());
		p.setOrganization(Integer.parseInt(id) % 10 == 0);
		p.setCreatedDate(new Date());
		p.setMarketTimer(Integer.parseInt(id) % 5 == 0);
		p.setClaimantRegistration(new ClaimantRegistration());
		p.getClaimantRegistration().setRegistration1(
				(String) line.get("GivenName"));
		p.getClaimantRegistration().setRegistration2(
				(String) line.get("MiddleName"));
		p.getClaimantRegistration().setRegistration3(
				(String) line.get("Surname"));

		newPrimaryAddress(line, p);
		p.setPrimaryContact(newContact(line));
		return p;
	}

	private Contact newContact(Map<String, Object> line) {
		Contact c = new Contact();
		c.setEmail((String) line.get("EmailAddress"));
		c.setPhoneNo((String) line.get("TelephoneNumber"));
		return c;
	}

	private void newPrimaryAddress(Map<String, Object> line, Claimant p) {
		ClaimantAddress a = new ClaimantAddress();
		a.setAddressType(AddressType.ADDRESS_OF_RECORD);
		a.setMailingAddress(Boolean.TRUE);
		a.setAddress1((String) line.get("StreetAddress"));
		a.setCity((String) line.get("City"));
		a.setStateCode((String) line.get("State"));
		a.setCountryCode((String) line.get("Country"));
		a.setZipCode(new ZipCode((String) line.get("ZipCode"), null));
		a.setClaimant(p);
		p.addAddress(a);
	}

	private String getUniqueCheckNumber() {

		Random rand = new Random();
		long drand = (long) (rand.nextDouble() * 10000000000L);
		Long lObj = new Long(drand);
		String checkNo = lObj.toString();
		return checkNo;

	}

	private void loadReminderTypes() {
		ReminderType remindertype = new ReminderType();
		remindertype.setSendReminder(true);
		remindertype.setDescription("Submit Claim Form");
		remindertype.setDueDateOffset(5);
		remindertype.setDueDateReference(new Date());
		remindertype.persist();
		remindertype = new ReminderType();
		remindertype.setSendReminder(true);
		remindertype.setDescription("Submit Saved Data");
		remindertype.setDueDateOffset(-5);
		remindertype.setDueDateReference(new Date());
		remindertype.persist();
		remindertype = new ReminderType();
		remindertype.setSendReminder(true);
		remindertype.setDescription("Submit for NIGO");
		remindertype.setDueDateOffset(12);
		remindertype.setDueDateReference(new Date());
		remindertype.persist();
		remindertype = new ReminderType();
		remindertype.setSendReminder(true);
		remindertype.setDescription("Submit Documents");
		remindertype.setDueDateOffset(-13);
		remindertype.setDueDateReference(new Date());
		remindertype.persist();
		remindertype = new ReminderType();
		remindertype.setSendReminder(true);
		remindertype.setDescription("Cash Outstanding Check");
		remindertype.setDueDateOffset(7);
		remindertype.setDueDateReference(new Date());
		remindertype.persist();
	}



	private void loadQuestions(int i) {
		QuestionGroup qGroup1 = newQuestionGroup("MEDICAL PLAN",
				QuestionGroupDisplayCode.ROW, i, 3, false);
		List<AdditionalQuestions> singularQuestions = Lists.newArrayList();

		// question 1
		AdditionalQuestions dateOfBirth = newQuestion(1, qGroup1,
				"Date of Birth", "MP-DOB", ResponseDisplayCode.DATE, null);
		singularQuestions.add(dateOfBirth);

		// question2

		List<AdditionalQuestionsResponses> genderValues = Lists.newArrayList();
		AdditionalQuestionsResponses male = new AdditionalQuestionsResponses();
		male.setResponseDescription("Male");

		male.setSequence(Byte.valueOf(i + ""));
		genderValues.add(male);

		AdditionalQuestionsResponses female = new AdditionalQuestionsResponses();
		female.setResponseDescription("Female");
		female.setProofRequiredInd(true);
		female.setSequence(Byte.valueOf(i + 1 + ""));
		genderValues.add(female);

		AdditionalQuestions gender = newQuestion(2, qGroup1, "Sex", "MP-SEX", 
				ResponseDisplayCode.SELECTION, genderValues);
		singularQuestions.add(gender);

		// question 3

		List<AdditionalQuestionsResponses> maritalStatusValues = Lists
				.newArrayList();
		AdditionalQuestionsResponses single = new AdditionalQuestionsResponses();
		single.setResponseDescription("Single");

		single.setSequence(Byte.valueOf(i + ""));
		maritalStatusValues.add(single);

		AdditionalQuestionsResponses married = new AdditionalQuestionsResponses();
		married.setResponseDescription("Married");
		married.setProofRequiredInd(true);
		married.setSequence(Byte.valueOf(i + 1 + ""));
		maritalStatusValues.add(married);

		AdditionalQuestions maritalStatus = newQuestion(3, qGroup1,
				"Marital Status", "MP-MS", ResponseDisplayCode.SELECTION,
				maritalStatusValues);
		singularQuestions.add(maritalStatus);

		// question 4

		List<AdditionalQuestionsResponses> coverageValues = Lists
				.newArrayList();
		AdditionalQuestionsResponses individual = new AdditionalQuestionsResponses();
		individual.setResponseDescription("Individual");

		individual.setSequence(Byte.valueOf(i + ""));
		coverageValues.add(individual);

		AdditionalQuestionsResponses family = new AdditionalQuestionsResponses();
		family.setResponseDescription("Family");
		family.setProofRequiredInd(true);
		family.setSequence(Byte.valueOf(i + 1 + ""));
		coverageValues.add(family);

		AdditionalQuestions coverage = newQuestion(4, qGroup1, "Coverage", "MP-COVERAGE", 
				ResponseDisplayCode.SELECTION, coverageValues);
		singularQuestions.add(coverage);

		// question 5
		AdditionalQuestions medicalCoverage = newQuestion(5, qGroup1,
				"Medical Coverage", "MP-MED-COVERAGE", ResponseDisplayCode.TEXT, null);
		singularQuestions.add(medicalCoverage);
		//
		// question 6
		AdditionalQuestions planId = newQuestion(6, qGroup1, "Plan Id", "MP-PLAN-ID", 
				ResponseDisplayCode.TEXT, null);
		singularQuestions.add(planId);

		qGroup1.setAdditionalQuestions(singularQuestions);

	}

	private void loadMultiOccuringQuestions(int i) {
		QuestionGroup qGroup1 = newQuestionGroup("BENFICIARIES",
				QuestionGroupDisplayCode.GROUP, i, 3, true);

		List<AdditionalQuestions> multiQuestions = Lists.newArrayList();

		// question 1
		AdditionalQuestions name = newQuestion(1, qGroup1, "NAME", "BEN-NAME", 
				ResponseDisplayCode.TEXT, null);
		multiQuestions.add(name);

		// question 2
		AdditionalQuestions dateEligibilityChange = newQuestion(2, qGroup1,
				"DATE ELIGIBILITY CHANGE", "BEN-DATE-ELIG", ResponseDisplayCode.DATE, null);
		multiQuestions.add(dateEligibilityChange);

		// question 3

		List<AdditionalQuestionsResponses> relationshipValues = Lists
				.newArrayList();
		AdditionalQuestionsResponses child = new AdditionalQuestionsResponses();
		child.setResponseDescription("Child");

		child.setSequence(Byte.valueOf(i + ""));
		relationshipValues.add(child);

		AdditionalQuestionsResponses parent = new AdditionalQuestionsResponses();
		parent.setResponseDescription("Parent");
		parent.setProofRequiredInd(true);
		parent.setSequence(Byte.valueOf(i + 1 + ""));
		relationshipValues.add(parent);

		AdditionalQuestions relationship = newQuestion(3, qGroup1,
				"RELATIONSHIP", "BEN-RELN",  ResponseDisplayCode.SELECTION,
				relationshipValues);
		multiQuestions.add(relationship);

		// question 4
		AdditionalQuestions dob = newQuestion(4, qGroup1, "DOB", "BEN-DOB", 
				ResponseDisplayCode.DATE, null);
		multiQuestions.add(dob);

		// question 5
		AdditionalQuestions ssn = newQuestion(5, qGroup1, "SSN", "BEN-SSN", 
				ResponseDisplayCode.TEXT, null);
		multiQuestions.add(ssn);

		// question6

		List<AdditionalQuestionsResponses> genderValues = Lists.newArrayList();
		AdditionalQuestionsResponses male = new AdditionalQuestionsResponses();
		male.setResponseDescription("Male");

		male.setSequence(Byte.valueOf(i + ""));
		genderValues.add(male);

		AdditionalQuestionsResponses female = new AdditionalQuestionsResponses();
		female.setResponseDescription("Female");
		female.setProofRequiredInd(true);
		female.setSequence(Byte.valueOf(i + 1 + ""));
		genderValues.add(female);

		AdditionalQuestions gender = newQuestion(6, qGroup1, "Sex", "BEN-SEX", 
				ResponseDisplayCode.SELECTION, genderValues);
		multiQuestions.add(gender);

		// question 7
		AdditionalQuestions coverageEndDate = newQuestion(7, qGroup1,
				"COVERAGE END DATE", "BEN-COVER-END-DT", ResponseDisplayCode.DATE, null);
		multiQuestions.add(coverageEndDate);

		// question 8
		AdditionalQuestions medicare = newQuestion(8, qGroup1, "MEDICARE", "BEN-MEDICARE",
				ResponseDisplayCode.TEXT, null);
		multiQuestions.add(medicare);

		// question 9
		AdditionalQuestions medicareid = newQuestion(1, qGroup1, "MEDICARE ID", "BEN-MEDICARE-ID", 
				ResponseDisplayCode.TEXT, null);
		multiQuestions.add(medicareid);

		qGroup1.setAdditionalQuestions(multiQuestions);

	}

	private AdditionalQuestions newQuestion(int i, QuestionGroup qGroup, 
			String qDesc, String questionCode, ResponseDisplayCode responseDisplayCode,
			List<AdditionalQuestionsResponses> responses) {
		AdditionalQuestions questions = new AdditionalQuestions();
		questions.setQuestionCode(questionCode) ;
		questions.setKeyColumnSeq(Short.valueOf(i + ""));
		questions.setSequence(Byte.valueOf(i * 10 + ""));
		questions.setQuestionGroup(qGroup);
		questions.setQuestionDescription(qDesc);
		questions.setResponseDisplayCode(responseDisplayCode);
		questions.setLength(i * 10);
		questions.setDisplaySize(10);
		questions.setProofRequiredInd(true);

		if (ResponseDisplayCode.SELECTION.equals(responseDisplayCode)) {
			for (AdditionalQuestionsResponses response : responses) {
				response.setAdditionalQuestions(questions);
			}
			questions.setAdditionalQuestionsResponses(responses);
		}

		questions.persist();
		return questions;
	}

	private QuestionGroup newQuestionGroup(String desc,
			QuestionGroupDisplayCode displayCode, int i,
			int noOfColumnsToDisplay, boolean proofRequiredInd) {
		QuestionGroup qGroup1 = new QuestionGroup();
		qGroup1.setGroupDescription(desc);
		qGroup1.setProofRequiredInd(proofRequiredInd);
		qGroup1.setGroupDisplayCode(displayCode);
		qGroup1.setSequence(Byte.valueOf(i * 10 + ""));
		qGroup1.setNoOfColumnsToDisplay(noOfColumnsToDisplay);
		qGroup1.persist();
		return qGroup1;
	}	

	private void loadTransactionTypes(){
		TransactionType transactionType = new TransactionType();
		transactionType.setCode("Buy");
		transactionType.setCode("Sell");
		transactionType.persist();
	}
	
	private void loadSecurityFunds(){
		ClaimantClaim claimantClaim;
		ClaimantTransactions claimantTransactions;
		SecurityFund securityFund;
		SecurityRef securityRef;
		TransactionType transactionType;
		
		
		//1
		claimantClaim = new ClaimantClaim();
		claimantTransactions = new ClaimantTransactions();
		securityFund = new SecurityFund();
		securityRef = new SecurityRef();
		transactionType = new TransactionType();
		claimantClaim.setClaimIdentifier("100001");
		claimantClaim.setControlNumber(1001);
		claimantClaim.persist();
		
		securityRef.setSecurityId(1);
		securityRef.setName("XYZ Corp");
		securityRef.setTicker("XYZ");
		securityRef.setCusip("1234");
		securityRef.persist();
		
		transactionType.setCode("Buy");
		transactionType.setCode("Sell");
		transactionType.persist();
		
		securityFund.setFund("1000");
		securityFund.setSecurityRef(securityRef);
		securityFund.persist();
		
		claimantTransactions.setTransactionType(transactionType);
		claimantTransactions.setTradeDate(new Date());
		claimantTransactions.setSettlementDate(new Date());
		claimantTransactions.setQuantity(new BigDecimal(100));
		claimantTransactions.setSecurityFund(securityFund);
		claimantTransactions.setClaimantClaim(claimantClaim);
		claimantTransactions.persist();
		
		//2
		claimantClaim = new ClaimantClaim();
		claimantTransactions = new ClaimantTransactions();
		securityFund = new SecurityFund();
		securityRef = new SecurityRef();
		transactionType = new TransactionType();
		
		claimantClaim.setClaimIdentifier("100002");
		claimantClaim.setControlNumber(100002);
		claimantClaim.persist();
		
		securityRef.setSecurityId(2);
		securityRef.setName("ABC Corp");
		securityRef.setTicker("ABC");
		securityRef.setCusip("12345");
		securityRef.persist();
		
		securityFund.setFund("2000");
		securityFund.setSecurityRef(securityRef);
		securityFund.persist();
		
		transactionType.setCode("Buy1");
		transactionType.setCode("Sell1");
		transactionType.persist();
		
		claimantTransactions.setTransactionType(transactionType);
		claimantTransactions.setTradeDate(new Date());
		claimantTransactions.setSettlementDate(new Date());
		claimantTransactions.setQuantity(new BigDecimal(100));
		claimantTransactions.setSecurityFund(securityFund);
		claimantTransactions.setClaimantClaim(claimantClaim);
		claimantTransactions.persist();
		
		//3
		claimantClaim = new ClaimantClaim();
		claimantTransactions = new ClaimantTransactions();
		securityFund = new SecurityFund();
		securityRef = new SecurityRef();
		transactionType = new TransactionType();
		
		claimantClaim.setClaimIdentifier("100003");
		claimantClaim.setControlNumber(100003);
		claimantClaim.persist();
		
		securityRef.setSecurityId(3);
		securityRef.setName("PQR Corp");
		securityRef.setTicker("PQR");
		securityRef.setCusip("6789");
		securityRef.persist();
		
		transactionType.setCode("Buy2");
		transactionType.setCode("Sell2");
		transactionType.persist();
		
		securityFund.setFund("3000");
		securityFund.setSecurityRef(securityRef);
		securityFund.persist();
		
		claimantTransactions.setTransactionType(transactionType);
		claimantTransactions.setTradeDate(new Date());
		claimantTransactions.setSettlementDate(new Date());
		claimantTransactions.setQuantity(new BigDecimal(100));
		claimantTransactions.setSecurityFund(securityFund);
		claimantTransactions.setClaimantClaim(claimantClaim);
		claimantTransactions.persist();

		
	}
	

}
