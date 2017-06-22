package com.sightcorner.batch.impl.hello;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @author wdsy
 * @date 2017年1月22日
 */
public class Writer implements ItemWriter<Object> {

	public void write(List<? extends Object> list) throws Exception {
		Thread current = Thread.currentThread();
		for(Object obj : list) {
			System.out.print(obj.toString());
		}
		System.out.println(" current: " + current.getName() + " " + current.getId());
	}

}
