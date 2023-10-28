package com.cruise.project_cruise.quartz.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public class AutoWiringSpringBeanJobFactory extends SpringBeanJobFactory {
    // Job Class에서는 Dependency를 해주지 않지만, 대부분의 기능을 Dependency를 통해서 수행한다.
    // AutoWiringSpringBeanJobFactory 는
    // JobClass에서 Dependency가 가능하도록 도와주는 기능을 담당한다.
    private transient AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        beanFactory = applicationContext.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }

}
