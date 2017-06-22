package com.sightcorner.quartz.scan;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;



public class DefaultScanJob implements Job {


    @Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println("default scan job starting...");
	}
}
