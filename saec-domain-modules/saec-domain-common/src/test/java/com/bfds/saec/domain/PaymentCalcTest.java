package com.bfds.saec.domain;

import static org.fest.assertions.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.mock.staticmock.AnnotationDrivenStaticEntityMockingControl;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

import com.bfds.saec.domain.reference.PaymentComponentType;

@MockStaticEntityMethods
public class PaymentCalcTest {

    /**
     * The nettAmout must default to the grossAmount.
     */
	@Test
	public void nettAmountMustBeEqualToGrossAmountWhenNoWithholding() {
		PaymentCalc calc = new PaymentCalc();
		assertThat(calc.getNettAmount()).isNull();
		
		calc.setGrossAmount(new BigDecimal(100.55));
		
		assertThat(calc.getNettAmount()).isEqualTo(new BigDecimal(100.55));
	}

    /**
     *  Test the {@link PaymentCalc#multiply(java.math.BigDecimal)} method.
     */
    @Test
    public void multiply() {
        PaymentCalc calc = Builder.n().grossAmount(new BigDecimal(500d)).fees(new BigDecimal(100.25d)).interest(new BigDecimal(50d)).other1(new BigDecimal(100d)).other2(new BigDecimal(75)).settlementAmount(new BigDecimal(175d)).nettAmount(new BigDecimal(400d))
                .get();

        PaymentCalc actual = calc.clone();
        actual.multiply(new BigDecimal(0.50d));

        PaymentCalc expected = Builder.n().grossAmount(new BigDecimal(250d)).fees(new BigDecimal("50.12")).interest(new BigDecimal(25d)).other1(new BigDecimal(50d)).other2(new BigDecimal(37.5)).settlementAmount(new BigDecimal(87.5d)).nettAmount(new BigDecimal(200d))
                .get();
        //This is just to sen the scale and rounding.
        expected.multiply(new BigDecimal(1));

        assertThat(actual).isEqualTo(expected);
    }

    /**
     *  Test the {@link PaymentCalc#percentageOfNettAmount(java.math.BigDecimal)} method.
     */
    @Test
    public void percentageOfNettAmount() {
        PaymentCalc calc = Builder.n().nettAmount(new BigDecimal(175))
                .get();        
        assertThat(calc.percentageOfNettAmount(new BigDecimal(87.5))).isEqualTo(new BigDecimal(50).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    /**
     *  Test the {@link PaymentCalc#percentageOfNettAmount(java.math.BigDecimal)} method.
     *
     *  The amount is a double.
     *
     *  @see https://bostonfinancial.atlassian.net/browse/PIA-203
     */
    @Test
    public void percentageOfNettAmountOfTypeDouble() {
        PaymentCalc calc = Builder.n().nettAmount(new BigDecimal(100.75)).get();
        assertThat(calc.percentageOfNettAmount(new BigDecimal(10))).isEqualTo(new BigDecimal(9.92).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
    
    @Test
    public void getComponentValueByDescription() {
    	EventPaymentConfig config = newEventPaymentConfig();
    	
    	EventPaymentConfig.findByDescription("Settlement Amount");    	
    	AnnotationDrivenStaticEntityMockingControl.expectReturn(config);
    	AnnotationDrivenStaticEntityMockingControl.playback();
    	
    	PaymentCalc calc = Builder.n().fees(new BigDecimal(100d)).interest(new BigDecimal(50d)).other1(new BigDecimal(100d)).other2(new BigDecimal(75)).settlementAmount(new BigDecimal(175d))
                .get();
    	
    	assertThat(calc.getComponentValueByDescription("Settlement Amount")).isEqualTo(calc.getPaymentComp1());
    	
    }

	private EventPaymentConfig newEventPaymentConfig() {
		EventPaymentConfig config = new EventPaymentConfig();
    	config.setDefaultDescription("Settlement Amount");
    	PaymentComponentTypeLov paymentComponentType = new PaymentComponentTypeLov();
    	paymentComponentType.setCode("paymentComp1");
    	config.setPaymentComponentType(paymentComponentType);
		return config;
	}
    
    @Test
    public void setComponentValueByDescription() {
    	EventPaymentConfig config = newEventPaymentConfig();
    	
    	EventPaymentConfig.findByDescription("Settlement Amount");    	
    	AnnotationDrivenStaticEntityMockingControl.expectReturn(config);
    	AnnotationDrivenStaticEntityMockingControl.playback();
    	
    	PaymentCalc calc = Builder.n().fees(new BigDecimal(100d)).interest(new BigDecimal(50d)).other1(new BigDecimal(100d)).other2(new BigDecimal(75)).settlementAmount(new BigDecimal(175d))
                .get();
    	
    	assertThat(calc.getPaymentComp1()).isEqualTo(new BigDecimal(175d));
    	
    	calc.setComponentValueByDescription("Settlement Amount", new BigDecimal(125d));
    	assertThat(calc.getPaymentComp1()).isEqualTo(new BigDecimal(125d));
    }
    
    @Test
    public void getDescriptionByPaymentComponentType() {
    	EventPaymentConfig config = newEventPaymentConfig();
    	
    	EventPaymentConfig.findByPaymentComponentType(PaymentComponentType.paymentComp1);    	
    	AnnotationDrivenStaticEntityMockingControl.expectReturn(config);
    	AnnotationDrivenStaticEntityMockingControl.playback();
    	
    	PaymentCalc calc = Builder.n().fees(new BigDecimal(100d)).interest(new BigDecimal(50d)).other1(new BigDecimal(100d)).other2(new BigDecimal(75)).settlementAmount(new BigDecimal(175d))
                .get();
    	
    	assertThat(calc.getDescriptionByPaymentComponentType(PaymentComponentType.paymentComp1)).isEqualTo("Settlement Amount");
    	
    }    

    /**
     * A fluent API for building PaymentCalc
     */
    public static class Builder {
        private PaymentCalc ret;
        public static Builder n() {
            return new Builder();
        }
        private Builder() {
            ret = new PaymentCalc();
        }

        public Builder grossAmount(BigDecimal param) {
            ret.setGrossAmount(param);
            return this;
        }

        public Builder nettAmount(BigDecimal param) {
            ret.setNettAmount(param);
            return this;
        }

        public Builder settlementAmount(BigDecimal param) {
            ret.setPaymentComp1(param);
            return this;
        }

        public Builder interest(BigDecimal param) {
            ret.setPaymentComp2(param);
            return this;
        }

        public Builder fees(BigDecimal param) {
            ret.setPaymentComp3(param);
            return this;
        }

        public Builder other1(BigDecimal param) {
            ret.setPaymentComp4(param);
            return this;
        }

        public Builder other2(BigDecimal param) {
            ret.setPaymentComp5(param);
            return this;
        }

        public PaymentCalc get() {
            return ret;
        }
    }
}
