package com.bfds.saec.claim;


import com.google.common.collect.Lists;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * A container for all the answers and answers of a claim.
 */
@RooJavaBean
@RooToString
public class ClaimQuestionnaire implements Serializable {

    private List<ClaimAnswerGroup> answerGroups = Lists.newArrayList();

    public void prepareForRendering() {
        java.util.Collections.sort(getAnswerGroups(), new Comparator<ClaimAnswerGroup>() {
            @Override
            public int compare(ClaimAnswerGroup o1, ClaimAnswerGroup o2) {
                return o1.getQuestionGroup().getSequence() - o2.getQuestionGroup().getSequence();
            }
        });

        for(ClaimAnswerGroup answerGroup : getAnswerGroups()) {
            answerGroup.prepareForRendering();
        }
    }

    public void prepareForPersistence() {
        for(ClaimAnswerGroup answerGroup : getAnswerGroups()) {
            answerGroup.prepareForPersistence();
        }
    }
}
