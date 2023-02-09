package com.spring.eventmanagement.service;

import com.spring.eventmanagement.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
@Slf4j
public class DynamicSchedulerService implements SchedulingConfigurer {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    TaskScheduler poolScheduler;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(poolScheduler);


        // Next execution time is taken from DB, so if the value in DB changes, next execution time will change too.
        taskRegistrar.addTriggerTask(() -> scheduledDatabase(eventRepository.findNextEventTime().get().getConfigValue()), t -> {
            Calendar nextExecutionTime = new GregorianCalendar();
            Date lastActualExecutionTime = t.lastActualExecutionTime();
            nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
            nextExecutionTime.add(Calendar.SECOND, Integer.parseInt(eventRepository.findById("next_exec_time").get().getConfigValue()));
            return nextExecutionTime.getTime();
        });
    }

    public void scheduledDatabase(String time) {
        log.info("scheduledDatabase: Next execution time of this will be taken from DB -> {}", time);
    }

}
