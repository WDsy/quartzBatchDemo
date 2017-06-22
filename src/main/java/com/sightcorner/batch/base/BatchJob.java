package com.sightcorner.batch.base;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wdsy
 * @date 2017年1月22日
 */
public abstract class BatchJob implements IBatchJob{

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	protected JobBuilderFactory jobBuilderFactory;

	@Autowired
	protected StepBuilderFactory stepBuilderFactory;

	@Autowired
	public ApplicationContext applicationContext;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		applicationContext = new ClassPathXmlApplicationContext("application/application.xml");
		jobBuilderFactory = (JobBuilderFactory) applicationContext.getBean("jobBuilderFactory");
		stepBuilderFactory = (StepBuilderFactory) applicationContext.getBean("stepBuilderFactory");
		jobLauncher = (JobLauncher) applicationContext.getBean("jobLauncher");

		try {
			jobLauncher.run(getJob(), getJobParameters());
		} catch (Exception e) {
			throw new JobExecutionException(e);
		} 
	}

	protected abstract Job getJob();

	protected abstract JobParameters getJobParameters();

	public String getJobName() {
		return this.getClass().getName();
	}
	
		


	
}
