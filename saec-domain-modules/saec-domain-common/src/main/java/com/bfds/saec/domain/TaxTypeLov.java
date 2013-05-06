package com.bfds.saec.domain;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

import com.bfds.saec.domain.reference.Lov;

/**
 * This class will store TaxType values to be retrieved from database
 * 
 * Valid options primary:
				Valid options secondary:
	•	1098		
				•	1098
				•	1098-C
				•	1098-E
				•	1098-T
	•	1099	•	1099-A
				•	1099-B
				•	1099-C
				•	1099-CAP
				•	1099-DIV
				•	1099-G
				•	1099-H
				•	1099-INT
				•	1099-LTC
				•	1099-MISC
				•	1099-OID
				•	1099-PATR
				•	1099-Q
				•	1099-R
				•	1099-S
	•	5498	•	5498
				•	5498-ESA
				•	5498-SA
	•	W-2G	

 * @author raj
 *
 */
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory", finders="findTaxTypesByCategory")
@DiscriminatorValue("TaxType")
@NamedQueries({
	@NamedQuery(name="getTaxTypesByCategory", query="from TaxTypeLov o where o.category = :category"),
	@NamedQuery(name="getDistinctTaxTypeCategories", query="Select distinct tt.category from TaxTypeLov tt ") 
} )
public class TaxTypeLov extends Lov {

    public static final List<TaxTypeLov> findTaxTypesByCategory(String argCategory) {
    	final Query query = entityManager()
                .createNamedQuery("getTaxTypesByCategory", TaxTypeLov.class)
                .setParameter("category", argCategory) ;
    	
    	return query.getResultList() ;
    }
    
    public static final List findDistinctTaxTypeCategories() {
    	final Query query = entityManager()
                .createNamedQuery("getDistinctTaxTypeCategories", String.class) ;
                
    	return query.getResultList() ; 
    }
}
