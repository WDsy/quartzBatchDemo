package com.sightcorner.quartz.service;

import com.sightcorner.quartz.model.ScheduleTask;
import com.sightcorner.quartz.util.ScheduleTaskNetwork;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;



/**
 * @author zhouweidong
 * @date 2017年1月22日
 */
@Service("scheduleTaskService")
public class ScheduleTaskService implements IScheduleTaskService {
	
	private final String STATUS_STOP = "0";
	private final String STATUS_START = "1";
	
	/**
	 * 
	 */
	private Scheduler scheduler = null;
	
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	
	/**
	 * 暴露接口，唯一提供可调用的接口
	 */
    @Override
	public void proceedTask(List<ScheduleTask> list) throws Exception {
		
		if(null == list || list.size() <= 0) {
			return;
		}
		
		Set<String> taskCodeSet = this.getCurrentTaskCodeSet();
		
		for(ScheduleTask scheduleTask : list) {
			if(null == scheduleTask) {
				continue;
			}
			
			if(STATUS_START.equals(scheduleTask.getStatus())) {//启用任务
				if(!this.isTaskCodeExist(scheduleTask.getTaskCode(), taskCodeSet)) {//未在进程中
					this.startTask(scheduleTask);
				}
				
			} else if (STATUS_STOP.equals(scheduleTask.getStatus())) {//停用任务
				if(this.isTaskCodeExist(scheduleTask.getTaskCode(), taskCodeSet)) {//已在进程中
					this.stopTask(scheduleTask);
				}
			}
		}
		
	}
	
	
	
	
	//
	//
	// 以下方法皆为 私有方法，请勿调用
	//
	//
	
	

	private void startTask(ScheduleTask scheduleTask) throws Exception {
		
		if(!this.validate(scheduleTask)) {
			return;
		}
		
		ScheduleTaskNaming naming = new ScheduleTaskNaming(scheduleTask);
		Class<? extends Job> clazz = this.getJobClass(naming.getClassName());
		
		JobDetail jobDetail = this.getJobDetail(clazz, naming.getJobName());
		Trigger trigger = this.getTrigger(naming.getTriggerName(), naming.getScheduleConfiguration());
		
		this.scheduler.scheduleJob(jobDetail, trigger);
	}

	private void stopTask(ScheduleTask scheduleTask) throws Exception {
		
		if(!this.validate(scheduleTask)) {
			return;
		}
		
		ScheduleTaskNaming naming = new ScheduleTaskNaming(scheduleTask);
		
		this.scheduler.unscheduleJob(TriggerKey.triggerKey(naming.getTriggerName(), Scheduler.DEFAULT_GROUP));
		this.scheduler.deleteJob(JobKey.jobKey(naming.getJobName(), Scheduler.DEFAULT_GROUP));
	}
	


	private boolean validate(ScheduleTask scheduleTask) {
		if(null == scheduleTask) {
			return false;
		} else if (null == scheduleTask.getTaskCode()
				|| null == scheduleTask.getTaskName()
				|| null == scheduleTask.getTaskConf()
				|| null == scheduleTask.getTaskClass()
				|| null == scheduleTask.getTaskServerIp()) {
			return false;
		}
		
		if (!isLocalIp(scheduleTask.getTaskServerIp())) {
			return false;
		}
		
		return true;
	}


	private Class<? extends Job> getJobClass(String className) throws ClassNotFoundException {
		return (Class<? extends Job>) Class.forName(className);
	}
	


	private JobDetail getJobDetail(Class<? extends Job> clazz, String jobName) {
		JobDetail jobDetail = newJob(clazz)
				.withIdentity(jobName, Scheduler.DEFAULT_GROUP)
				.build()
		;
		
		return jobDetail;
	}
	
	


	private Trigger getTrigger(String triggerName, String conf) {
		CronTrigger trigger = newTrigger()
				.withIdentity(triggerName, Scheduler.DEFAULT_GROUP)
				.withSchedule(CronScheduleBuilder.cronSchedule(conf))
				.startNow()
				.build();
		
		return trigger;
	}


	private boolean isLocalIp(String ip) {
		
		if(null != ip && ip.length() >= 0) {
			return ScheduleTaskNetwork.isExist(ip);
		}
		
		return false;
	}
	
	
	private boolean isTaskCodeExist(String taskCode, Set<String> set) {
		
		return set.contains(taskCode);
	}
	
	
	private Set<String> getCurrentTaskCodeSet() throws Exception{
		
		Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(Scheduler.DEFAULT_GROUP));
		Set<String> taskCodeSet = new HashSet<String>();
		for(JobKey jobKey : jobKeySet) {
			taskCodeSet.add(jobKey.getName());
		}
		
		return taskCodeSet;
		
	}


	class ScheduleTaskNaming {
		private String taskCode = "taskCode";
		private String className;
		private String triggerName;
		private String jobName;
		private String scheduleConfiguration;
		
		ScheduleTaskNaming(ScheduleTask scheduleTask) {
			if(null != scheduleTask.getTaskCode()) {
				this.taskCode = scheduleTask.getTaskCode();
			}
			this.className = scheduleTask.getTaskClass();
			this.triggerName = this.taskCode;
			this.jobName = this.taskCode;
			this.scheduleConfiguration = scheduleTask.getTaskConf();
		}

		public String getClassName() {
			return className;
		}

		public String getTriggerName() {
			return triggerName;
		}

		public String getJobName() {
			return jobName;
		}

		public String getScheduleConfiguration() {
			return scheduleConfiguration;
		}
	}


}
