package com.bfds.saec.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.fest.assertions.Assertions.assertThat;
import com.bfds.saec.domain.reference.PaymentComponentType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/spring/applicationContext-test.xml" })
@Transactional
public class EventPaymentConfigIntegrationTest {
	
    @Before
    public void createTestData() {
    	Event e = new Event();
    	e.setCode("AA");
    	e.persist();
    	PaymentComponentTypeLov componentType = new  PaymentComponentTypeLov();
    	componentType.setCode(PaymentComponentType.paymentComp1.name());
    	componentType.setDescription("NA");
    	componentType.persist();
    	EventPaymentConfig config = new EventPaymentConfig();
    	config.setPaymentComponentType(componentType);
    	config.setActiveDescription("Settlement Amount");
    	config.setDefaultDescription("Settlement Amount");
    	config.setEvent(e);
    	config.persist();
    	
    }
    
    @Test
    public void findByDescription() {
    	EventPaymentConfig config = EventPaymentConfig.findByDescription("Settlement Amount");    	
    	assertThat(config.getDescription()).isEqualTo("Settlement Amount");
    }
    
    @Test
    public void findByPaymentComponentType() {
    	EventPaymentConfig config = EventPaymentConfig.findByPaymentComponentType(PaymentComponentType.paymentComp1);    	
    	assertThat(config.getDescription()).isEqualTo("Settlement Amount");
    }

}
