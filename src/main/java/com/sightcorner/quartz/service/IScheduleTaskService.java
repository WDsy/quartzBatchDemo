package com.sightcorner.quartz.service;

import com.sightcorner.quartz.model.ScheduleTask;
import org.quartz.Scheduler;

import java.util.List;

/**
 * @author wdsy
 * @date 2017年1月22日
 */
public interface IScheduleTaskService {


	void proceedTask(List<ScheduleTask> list) throws Exception;

	void setScheduler(Scheduler scheduler);
}
