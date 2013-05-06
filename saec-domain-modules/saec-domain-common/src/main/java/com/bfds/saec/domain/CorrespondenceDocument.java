package com.bfds.saec.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
@RooJavaBean
@RooToString(excludeFields = {"id", "version"})
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @NamedQueries({
	@NamedQuery(name="findCorrespondenceDocuments", query="FROM CorrespondenceDocument o ")
} )
public class CorrespondenceDocument implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String name;
	
	/**
	 * The threshold level in dollars is set in Event Config. Payments with a dollar value greater than threshold will pick CorrespondenceDocument with  withinThreshold= FALSE.
	 */
	private Boolean withinThreshold;
	
	private boolean selectedMissingDocs = false;
	
	public CorrespondenceDocument() {
		
	}
	
	public CorrespondenceDocument(final String name) {
		this.name= name;
	}	
	
	public static List<CorrespondenceDocument> findCorrespondenceDocuments(String requestType) {
			return entityManager().createNamedQuery("findCorrespondenceDocuments", CorrespondenceDocument.class)
            .getResultList();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CorrespondenceDocument other = (CorrespondenceDocument) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

	public boolean isSelectedMissingDocs() {
		return selectedMissingDocs;
	}

	public void setSelectedMissingDocs(boolean selectedMissingDocs) {
		this.selectedMissingDocs = selectedMissingDocs;
	}
	
	
}
