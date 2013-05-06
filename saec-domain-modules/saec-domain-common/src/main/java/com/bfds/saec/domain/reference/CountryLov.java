package com.bfds.saec.domain.reference;

import javax.persistence.DiscriminatorValue;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;


/**
 * This class will contain the Bank Code Information to be retrieved from database
 *  •	United States of America = US
 * @author raj
 *
 */
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
 @DiscriminatorValue("Country")
public class CountryLov extends Lov {
    public CountryLov(String code, String description) {
        super(code, description);
    }

}
