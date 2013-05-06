package com.bfds.saec.rip.transformer;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;

import java.util.Map;

import com.bfds.saec.rip.domain.*;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component("ripTransformer")
public class GenericRipObjectTransformer implements RipObjectTransformer<RipObject> {
	private static final Map<Class<? extends RipObject>, AbstractRipObjectTransformer<?>> ripBuilders = Maps.newHashMap();
	
	static {
		ripBuilders.put(AddressChangeRipObject.class,
				new AddressChangeRipObjectTransformer());
		ripBuilders.put(CureLetterRipObject.class,
				new CureLetterRipObjectTransformer());
		ripBuilders.put(StopReplaceCheckRipObject.class,
				new StopReplaceCheckRipObjectTransformer());
		ripBuilders.put(SpecialhandlingRipObject.class,
				new SpecialhandlingRipObjectTransformer());
        ripBuilders.put(ClaimFormSupportingDocRipObject.class,
                new ClaimFormSupportingDocRipObjectTransformer());
        ripBuilders.put(FollowupRemindersRipObject.class,
                new FollowupRemindersRipObjectTransformer());
	}

	@Override
	public String buildRip(final RipObject ripObject) {
		final RipObjectTransformer ripBuilder =  getRipbuilders().get(ripObject
				.getClass());		
		checkState(
				ripBuilder != null,
				format("No Builder Found for %s in %s", ripObject.getClass(),
						ripBuilders));
		return ripBuilder.buildRip(ripObject);
	}

	public static Map<Class<? extends RipObject>, AbstractRipObjectTransformer<?>> getRipbuilders() {
		return ripBuilders;
	}

	
}
