package com.bfds.wss.domain;

import com.bfds.wss.domain.reference.ClaimantDistributionType;
import org.hibernate.envers.Audited;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@RooJavaBean
@RooJpaActiveRecord(persistenceUnit="entityManagerFactory")
@Audited
public class ClaimantDistribution implements java.io.Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "claimant_claim_fk", nullable = false)
    private ClaimantClaim claimantClaim;

    private String category1;
    private String category2;
    private String category3;

    @Enumerated(EnumType.STRING)
    private ClaimantDistributionType distributionType;

    private BigDecimal distributionAmount;

    /**
     * The date on which {@link #distributionAmount} was last calculated.
     */
    private Date distributionCalculatedDate;

    /**
     * If the distribution amount was provided by an external source it would identify the source, else would indicated it was provided by the system.
     */
    private String source;
    /**
     * TODO: Get the different status from Jim.
     */
    private String status;
    /**
     *  Distribution calculation could be one of the below categories. 
     * @return
     */
	public String getAllCategories() {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.hasText(this.category1)) {
			sb.append(this.category1);
		}
		if (StringUtils.hasText(this.category2)) {
			sb.append(",").append(this.category2);
		}
		if (StringUtils.hasText(this.category3)) {
			sb.append(",").append(this.category3);
		}
		return sb.toString().trim();
	}
    

}
