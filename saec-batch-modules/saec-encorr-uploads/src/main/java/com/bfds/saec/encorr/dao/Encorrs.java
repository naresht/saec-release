package com.bfds.saec.encorr.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Encorrs {

	private List<EncorrItem> rpo;

	public List<EncorrItem> getRpo() {
		if (rpo == null) {
			rpo = new ArrayList<EncorrItem>();
		}
		return this.rpo;
	}

	public Encorrs withRpo(EncorrItem... values) {
		if (values != null) {
			for (EncorrItem value : values) {
				getRpo().add(value);
			}
		}
		return this;
	}

	public Encorrs withRpo(Collection<EncorrItem> values) {
		if (values != null) {
			getRpo().addAll(values);
		}
		return this;
	}

}
