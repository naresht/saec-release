package com.bfds.saec.rpo.awd;

import static org.fest.assertions.Assertions.assertThat;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import com.bfds.saec.batch.rpo.util.DataGenerator;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath*:/META-INF/spring/applicationContext.xml" })
//@Transactional
public class AwdRpoJobTest {

	@Autowired
	private JobLauncher launcher;

	@Autowired
	private Job awdRpoUploadJob;
	
	//@Before
	public void setup() {
		DataGenerator.deleteData();
		DataGenerator.createRpoItemWriterData();
	}

	//@Test
	public void runAwdRpoJobTest() throws Exception {
		
//		Payment c1 = Payment.findCheckByNumberAndAmount("123450", 900);
//		assertThat(c1).isNotNull();
//		assertThat(c1.getPaymentStatus()).isEqualTo(PaymentStatus.VOIDED);
//		assertThat(c1.getPaymentCode()).isEqualTo(PaymentCode.VF);
		
		JobParameters jobParameters = new JobParametersBuilder().addString("1",
				"1").toJobParameters();

		JobExecution jobExecution = launcher.run(awdRpoUploadJob, jobParameters);

		assertThat(ExitStatus.COMPLETED)
				.isEqualTo(jobExecution.getExitStatus());

	}

}
