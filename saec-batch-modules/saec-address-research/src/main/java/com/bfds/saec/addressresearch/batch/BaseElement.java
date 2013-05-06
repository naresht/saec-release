package com.bfds.saec.addressresearch.batch;

public class BaseElement {
 
    protected Long jobExecutionId;
    protected Long stepExecutionId;
    protected String jobName;
    protected int processSkipCount;
    protected String stepName;
    protected String readSkipCount;
    protected String writeSkipCount;
 
    public String getJobName() {
        return jobName;
    }
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    public Long getStepExecutionId() {
        return stepExecutionId;
    }
    public void setStepExecutionId(Long stepExecutionId) {
        this.stepExecutionId = stepExecutionId;
    }
    public Long getJobExecutionId() {
        return jobExecutionId;
    }
    public void setJobExecutionId(Long jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
    }
	/**
	 * @return the processSkipCount
	 */
	public int getProcessSkipCount() {
		return processSkipCount;
	}
	/**
	 * @param processSkipCount the processSkipCount to set
	 */
	public void setProcessSkipCount(int processSkipCount) {
		this.processSkipCount = processSkipCount;
	}
	/**
	 * @return the stepName
	 */
	public String getStepName() {
		return stepName;
	}
	/**
	 * @param stepName the stepName to set
	 */
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	
	public void setReadSkipCount(String readSkipCount) {
		this.readSkipCount = readSkipCount;
	}
	public String getReadSkipCount() {
		return readSkipCount;
	}
	public void setWriteSkipCount(String writeSkipCount) {
		this.writeSkipCount = writeSkipCount;
	}
	public String getWriteSkipCount() {
		return writeSkipCount;
	}
 
}