package com.bfds.saec.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import com.bfds.saec.domain.reference.MailType;
import com.google.common.base.Preconditions;

@RooJavaBean
@RooToString(excludeFields = { "id", "version" })
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @NamedQueries({
	@NamedQuery(name="findCorrespondenceRequestType", query="from RequestType o where o.mailType = :mailType")
} )
public class RequestType implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String name;
	
	private MailType mailType;

	public RequestType() {

	}

	public RequestType(final String name) {
		this.name = name;
	}

	/**
	 * @param mailType
	 *            - The {@link MailType} for which the {@link RequestType}s have
	 *            to be fetched.
	 * @return A {@link List<RequestType>} for the given {@link MailType}. If
	 *         {@link MailType} is null then it defaults to
	 *         {@link MailType#OUTGOING}
	 */
	public static List<RequestType> findCorrespondenceRequestType(final MailType mailType) {
		Preconditions.checkNotNull(mailType, "mailType cannot be null");
        return entityManager()
        .createNamedQuery("findCorrespondenceRequestType", RequestType.class).setParameter("mailType", mailType)
        .getResultList();
	}

	
	public MailType getMailType() {
		return mailType;
	}

	public void setMailType(MailType mailType) {
		this.mailType = mailType;
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
		RequestType other = (RequestType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
