package com.bfds.saec.rip.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.transaction.support.TransactionTemplate;

import com.bfds.saec.rip.domain.AddressChangeRipObject;
import com.bfds.saec.rip.domain.RipObject;
import com.bfds.saec.rip.integration.RipGateway;
import com.bfds.saec.rip.transformer.AddressChangeRipObjectTransformerTest;
import com.bfds.saec.util.FileUtils;
@RunWith(PowerMockRunner.class)
@PrepareForTest( { RipObject.class })
public class RipEventListenerImplTest {
	
	@Test
	@Ignore
	public void testAddressChangeSentByCSRRole() {
		
		final AddressChangeRipObject ripObject=  AddressChangeRipObjectTransformerTest.getDefaultTestAddressChangeRipObject();
								
		final RipGateway ripGatewayMock = mock(RipGateway.class);
		
		RipEventListenerImpl ripListener = new RipEventListenerImpl();	
		
		ripListener.setRipGateway(ripGatewayMock);
		
		ripListener.addressChanged(ripObject);
		
		verify(ripGatewayMock).sendRipObject(ripObject);
		
	}
	
	//@Test
	public void testAddressChangeSentByAnyOtherRole() {		
		
		final AddressChangeRipObject ripObject=  AddressChangeRipObjectTransformerTest.getDefaultTestAddressChangeRipObject();
		ripObject.setCreatedByUser("OPS");		
				
		final RipOutboundAdapter outboundAdapter = mock(RipOutboundAdapter.class);
		final TransactionTemplate txTemplate = mock(TransactionTemplate.class);
		
		RipEventListenerImpl ripListenerMock = new RipEventListenerImpl() {

			@Override
			public TransactionTemplate getTxTemplate() {
				return txTemplate;
			}
			
		};		
		
		ripListenerMock.addressChanged(ripObject);
		
		verify(outboundAdapter).send(FileUtils.getFileContents("awd/address-change.dat"));
		
	}	

}
