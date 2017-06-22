package com.sightcorner.batch.impl.hello;

import org.springframework.batch.item.ItemProcessor;

/**
 * @author wdsy
 * @date 2017年1月22日
 */
public class Processor implements ItemProcessor<Object, Object> {


	public Object process(Object input) throws Exception {
		return input;
	}

}
