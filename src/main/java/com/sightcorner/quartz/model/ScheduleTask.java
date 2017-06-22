package com.sightcorner.quartz.model;

import lombok.Getter;
import lombok.Setter;


/**
 * @author zhouweidong
 * @date 2017年1月22日
 */
@Getter
@Setter
public class ScheduleTask {

	/**
	 * 任务码
	 */
	private String taskCode;
	
	/**
	 * 任务名称
	 */
	private String taskName;
	
	/**
	 * 定时配置
	 */
	private String taskConf;
	
	/**
	 * 执行地址
	 */
	private String taskClass;
	
	/**
	 * 指定IP
	 */
	private String taskServerIp;
	
	/**
	 * 任务状态(0:可用;1:禁用)
	 */
	private String status;
	

	
	
	
}
