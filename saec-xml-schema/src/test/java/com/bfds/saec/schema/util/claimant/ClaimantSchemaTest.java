package com.bfds.saec.schema.util.claimant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ClaimantSchemaTest {
	
	private Claimants claimants;
	@Before
	public void setup() throws Exception  {
		Resource resource = new ClassPathResource("util-claimants.xsd");
		JAXBContext jc = JAXBContext.newInstance(Claimants.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		File schema = resource.getFile();
		SchemaFactory sf = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		unmarshaller.setSchema(sf.newSchema(schema));

		Resource xml = new ClassPathResource("util-claimants.xml");
		claimants  = (Claimants)unmarshaller.unmarshal(xml.getInputStream());
	}
	
	@Test
	public void checkForNotNullResult() {
		assertNotNull(claimants);
		assertEquals("Address size mismatch.", 2, claimants.getClaimant().get(0).getAddress().size());
		assertEquals("Payment size mismatch.", 2, claimants.getClaimant().get(0).getPayment().size());
        assertEquals("Leter size mismatch.", 2, claimants.getClaimant().get(0).getLetter().size());
	}
	
	@Test
	public void countAddress() {				
		assertEquals("Address size mismatch.", 2, claimants.getClaimant().get(0).getAddress().size());		
	}
	
	@Test
	public void countChecks() {				
		assertEquals("Payment size mismatch.", 2, claimants.getClaimant().get(0).getPayment().size());		
	}
	
	@Test
	public void checkStatusCode() {				
		Payment p1 = 	claimants.getClaimant().get(0).getPayment().get(0);
		assertEquals("Status Code must be IS.", "FIRST_ISSUE_CASHED_C_FIC", p1.getPaymentCode().name());
		Payment p2 = 	claimants.getClaimant().get(0).getPayment().get(1);
		assertEquals("Status Code must be IS.", "FIRST_ISSUE_CREATED_FI_FI", p2.getPaymentCode().name());
	}	
	
	@Test
	public void testWrite() throws Exception {
		Claimants claimants = new Claimants();
		JAXBContext jc = JAXBContext.newInstance(Claimants.class);
		Marshaller unmarshaller = jc.createMarshaller();
		StringWriter stringWriter = new StringWriter();
		unmarshaller.marshal(claimants, stringWriter);
		assertNotNull(stringWriter.getBuffer().toString());
		//System.out.println(stringWriter.getBuffer().toString());
	}
	
	
}
