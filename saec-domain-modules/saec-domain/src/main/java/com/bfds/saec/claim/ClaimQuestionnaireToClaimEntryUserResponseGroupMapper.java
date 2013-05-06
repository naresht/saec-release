package com.bfds.saec.claim;


import com.bfds.wss.domain.ClaimEntryUserResponseGroup;
import com.bfds.wss.domain.ClaimEntryUserResponses;
import com.bfds.wss.domain.UserResponse;
import com.bfds.wss.domain.UserResponseGroup;

public class ClaimQuestionnaireToClaimEntryUserResponseGroupMapper extends ClaimQuestionnaireToUserResponseGroupMapper {

    @Override
    protected UserResponse newUserResponse() {
        return new ClaimEntryUserResponses();
    }

    @Override
    protected UserResponseGroup newUserResponseGroup() {
        return new ClaimEntryUserResponseGroup();
    }

}