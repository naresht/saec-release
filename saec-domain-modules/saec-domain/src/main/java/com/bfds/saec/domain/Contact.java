package com.bfds.saec.domain;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 public class Contact implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "contact_first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "contact_last_name")),
            @AttributeOverride(name = "middleName", column = @Column(name = "contact_middle_name"))})
    private Name name;

    private String phoneNo;

    private String workPhoneNo;

    private String cellPhoneNo;

    private String email;

    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claimant_fk", nullable = true)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Claimant claimant;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primaryContactOf_fk", nullable = true)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Claimant primaryContactOf;

    public Name getName() {
        if (name == null) {
            name = new Name();
        }
        return this.name;
    }

    public void setFirstName(String firstName) {
        createNameIfNull();
        this.name.setFirstName(firstName);
    }

    public void setMiddleName(String middleName) {
        createNameIfNull();
        this.name.setMiddleName(middleName);
    }

    private void createNameIfNull() {
        if (this.name == null) {
            this.name = new Name();
        }
    }

    public void setLastName(String lastName) {
        createNameIfNull();
        this.name.setLastName(lastName);
    }

    public boolean isEmpty() {
        return !(StringUtils.hasText(phoneNo) || StringUtils.hasText(workPhoneNo)
                || StringUtils.hasText(cellPhoneNo) || StringUtils.hasText(email)
                || StringUtils.hasText(comments) || (name != null && !name.isEmpty())
        );
    }

    public final Contact clone() {
        final Contact contact = new Contact();
        if (this.getName() != null) {
            contact.setName(this.getName().copy());
        }
        contact.setPhoneNo(this.getPhoneNo());
        contact.setWorkPhoneNo(this.getWorkPhoneNo());
        contact.setCellPhoneNo(this.getCellPhoneNo());
        contact.setComments(this.getComments());
        contact.setEmail(this.getEmail());
        return contact;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((cellPhoneNo == null) ? 0 : cellPhoneNo.hashCode());
        result = prime * result
                + ((comments == null) ? 0 : comments.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((phoneNo == null) ? 0 : phoneNo.hashCode());
        result = prime * result
                + ((workPhoneNo == null) ? 0 : workPhoneNo.hashCode());
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
        Contact other = (Contact) obj;
        if (cellPhoneNo == null) {
            if (other.cellPhoneNo != null)
                return false;
        } else if (!cellPhoneNo.equals(other.cellPhoneNo))
            return false;
        if (comments == null) {
            if (other.comments != null)
                return false;
        } else if (!comments.equals(other.comments))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (phoneNo == null) {
            if (other.phoneNo != null)
                return false;
        } else if (!phoneNo.equals(other.phoneNo))
            return false;
        if (workPhoneNo == null) {
            if (other.workPhoneNo != null)
                return false;
        } else if (!workPhoneNo.equals(other.workPhoneNo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Email: ").append(getEmail()).append(", ");
        sb.append("PhoneNo: ").append(getPhoneNo()).append(", ");
        sb.append("CellPhoneNo: ").append(getCellPhoneNo()).append(", ");
        sb.append("WorkPhoneNo: ").append(getWorkPhoneNo());
        return sb.toString();
    }

}
