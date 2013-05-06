package com.bfds.saec.addressresearch.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class SampleStepExecutionListener implements StepExecutionListener {
	final Logger log = LoggerFactory.getLogger(SampleStepExecutionListener.class);
    public void beforeStep(StepExecution stepExecution){
        Long jobExecutionId =stepExecution.getJobExecutionId();
        String jobName=stepExecution.getJobExecution().getJobInstance().getJobName();
        stepExecution.getExecutionContext().put("jobExecutionId", jobExecutionId);
        stepExecution.getExecutionContext().put("stepExecutionId", stepExecution.getId());
        stepExecution.getExecutionContext().put("jobName", jobName);
        stepExecution.getExecutionContext().put("stepName", stepExecution.getStepName());
        stepExecution.getExecutionContext().put("processSkipCount", stepExecution.getProcessSkipCount());
        stepExecution.getExecutionContext().put("writeSkipCount", stepExecution.getWriteSkipCount());
        stepExecution.getExecutionContext().put("readSkipCount", stepExecution.getReadSkipCount());
    }
 
    public ExitStatus afterStep(StepExecution stepExecution){
    	log.info("skip count after reading records "+stepExecution.getReadSkipCount());
    	log.info("skip count after processing records "+stepExecution.getProcessSkipCount());
    	log.info("skip count after writing records "+stepExecution.getWriteSkipCount());
     	log.info("step name: "+stepExecution.getStepName());
     	log.info("Job Name: "+stepExecution.getJobExecution().getJobInstance().getJobName());
     	log.info("Job start time: "+stepExecution.getJobExecution().getStartTime());
     	if(!(ExitStatus.FAILED.getExitCode().equals( 
     			stepExecution.getExitStatus().getExitCode())) &&
     			stepExecution.getSkipCount() > 0) { 
     			stepExecution.setExitStatus(new ExitStatus("COMPLETED WITH SKIPS"));
     			}  
     	log.info("Step Execution Status: "+stepExecution.getExitStatus().getExitCode());
        return null;
    }
 
}