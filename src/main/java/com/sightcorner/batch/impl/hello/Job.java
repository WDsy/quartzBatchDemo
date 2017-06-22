package com.sightcorner.batch.impl.hello;

import com.sightcorner.batch.base.BatchJob;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouweidong
 * @date 2017年1月22日
 */
public class Job extends BatchJob {


    @Override
    protected org.springframework.batch.core.Job getJob() {

        return jobBuilderFactory
                .get(this.getJobName())
                .flow(step())
                .end()
                .build();
    }

    @Override
    protected JobParameters getJobParameters() {
        Map<String, JobParameter> map = new HashMap<>();
        map.put("timestamp", new JobParameter(System.currentTimeMillis()));
        map.put("thread", new JobParameter(Thread.currentThread().getName()));
        return new JobParameters(map);
    }

    public Step step() {
        return stepBuilderFactory
                .get("step")
                .chunk(5)
                .reader(new Reader())
                .processor(new Processor())
                .writer(new Writer())
                .build();
    }

}
