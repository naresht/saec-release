package com.bfds.saec.rip.transformer;

import com.bfds.saec.rip.domain.RipObject;

public interface RipObjectTransformer<T extends RipObject> {

	public abstract String buildRip(T ripObject);

}