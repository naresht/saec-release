package com.bfds.saec.batch.uploaded_doc_ripper;

import com.bfds.saec.batch.test.AbstractSingleJobExecutionIntegrationTest;
import com.bfds.saec.batch.util.TestDataUtil;
import com.bfds.saec.test.SaecTestExecutionListener;
import com.bfds.wss.domain.ClaimFileUploaded;
import org.fest.assertions.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/bfds/saec/batch/out/db/dbInOut-IntegrationTest-context.xml")
@TestExecutionListeners({ SaecTestExecutionListener.class,DependencyInjectionTestExecutionListener.class })
public class UploadedDocIntegrationTest extends AbstractSingleJobExecutionIntegrationTest {
	private static final Logger log = LoggerFactory.getLogger(UploadedDocIntegrationTest.class);
	
	@Autowired
	private Job uploadedDocRipperJob;

    @Autowired
    private UploadedDocEventTestData eventTestData;

	@Override
	protected Job geJOb() {
		return uploadedDocRipperJob;
	}

	@Override
	protected JobParameters getJobParameters() {
		return new JobParametersBuilder().addString("x", "y").toJobParameters();
	}
	


	public void afterAllTests() {
		(new TestDataUtil()).deleteData();
	}

	public void beforeAllTests() {
		eventTestData.create();
	}

    /**
     * Once an {@link com.bfds.wss.domain.ClaimFileUploaded} object is Ripped it must a non-null dateRipped.
      */
    @Test
    @Transactional
    public void verifyAllRippedDocsHaveRipDate() {
        List<ClaimFileUploaded> fileUploadedList = ClaimFileUploaded.findAllClaimFileUploadeds();
        assertThat(fileUploadedList).onProperty("dateRipped").satisfies(new Condition<List<?>>("dateRipped should not be null") {

            @Override
            public boolean matches(List<?> value) {
                for(Object o : value) {
                    if(o == null) {
                        return false;
                    }
                }
                return true;
            }
        });
    }

}
