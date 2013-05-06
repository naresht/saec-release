package com.bfds.saec.domain.activity;

import com.bfds.saec.domain.Address;
import com.bfds.saec.domain.ZipCode;
import org.junit.Test;

import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;

public class AddressChangeTest {

    /**
     * Verify {@link com.bfds.saec.domain.activity.AddressChange#getDescription()} when address research({@link com.bfds.saec.domain.Address#getResearchChangeActivity()}) related fields are not changed.
     * This means that AddressChange object is not created as a result of address research - send or receive.
     */
    @Test
    public void verifyActivityDescription() {
        AddressChange activity = newAddressChange();
        assertThat(activity.getDescription()).isEqualTo(" From : "+activity.getFrom().getAddressLinesAsString()+" To : "+activity.getTo().getAddressLinesAsString());
    }

    @Test
    public void verifyActivityTypeDescription() {
        AddressChange activity = newAddressChange();
        assertThat(activity.getActivityTypeDescription()).isEqualTo(AddressChange.ACTIVITY_TYPE_DESCRIPTION);
    }

	@Test
	public void verifyActivityTypeDescriptionOnAddressResearchSend() {
        AddressChange activity = newAddressChange();

		activity.getTo().setResearchChangeActivity(new AddressResearchChangeActivity());
		activity.getTo().getResearchChangeActivity().setResearchSent(true);
		activity.getTo().getResearchChangeActivity().setStatus("research desc");
		assertThat(activity.getActivityTypeDescription()).isEqualTo("research desc");

	}

    @Test
    public void verifyActivityTypeDescriptionOnAddressResearchReceive() {
        AddressChange activity = newAddressChange();

        activity.getTo().setResearchChangeActivity(new AddressResearchChangeActivity());
        activity.getTo().getResearchChangeActivity().setResearchReturned(true);
        activity.getTo().getResearchChangeActivity().setStatus("research desc");

        assertThat(activity.getActivityTypeDescription()).isEqualTo("research desc");
    }


    private AddressChange newAddressChange() {
        AddressChange activity = new AddressChange();
        Activity.setActivityDefaults(activity);
        Activity.setActivityDefaults(activity);
        Address from = newAddress();
        activity.setFrom(from);

        Address to = newAddress();
        activity.setTo(from);
        return activity;
    }

    private Address newAddress() {
        Address from = new Address();
        from.setAddress1("a1");
        from.setAddress2("a2");
        from.setAddress3("a3");
        from.setAddress4("a4");
        from.setAddress5("a5");
        from.setAddress6("a6");
        from.setCity("city");
        from.setStateCode("state");
        from.setCountryCode("US");
        from.setZipCode(new ZipCode("1111", "22"));
        return from;
    }
}
