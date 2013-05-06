package com.bfds.saec.dao;

import org.junit.Test;

import com.bfds.saec.domain.WireInfo;
import static org.junit.Assert.assertNotNull;
public class WireInfoTest {

	@Test
	public void checkForNulls() {
		WireInfo w = new WireInfo();
		assertNotNull(w.getReceivingBank());
		assertNotNull(w.getOriginationInfo());
		assertNotNull(w.getOriginationInfo().getLines());
		assertNotNull(w.getReceivingBank().getAddress());
		assertNotNull(w.getReceivingBank().getAddress().getZipCode());
		assertNotNull(w.getReceivingBank().getRegistration());
	}
}
