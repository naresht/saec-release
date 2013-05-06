package com.bfds.wss.domain;

import com.bfds.wss.domain.reference.QuestionGroup;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.List;

@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", inheritanceType="TABLE_PER_CLASS")
@RooJavaBean
@Audited
public abstract class UserResponseGroup implements Serializable{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Question_Group_fk", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private QuestionGroup questionGroup;


    public abstract List<UserResponse> getUserResponses();

    public abstract void addUserResponse(UserResponse userResponse);

    public abstract void removeUserResponse(UserResponse userResponse);
}
