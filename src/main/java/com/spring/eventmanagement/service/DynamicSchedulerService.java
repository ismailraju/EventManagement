package com.spring.eventmanagement.service;

import com.spring.eventmanagement.entity.Event;
import com.spring.eventmanagement.repository.EventRepository;
import com.spring.eventmanagement.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    @Autowired
    EmailService emailService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(poolScheduler);


        // Next execution time is taken from DB, so if the value in DB changes, next execution time will change too.
        taskRegistrar.addTriggerTask(() -> scheduledDatabase(eventRepository.findFirst1ByIs1hourEmailTransferFalseAndIsDeletedFalseOrderByStartAsc()), t -> {
            Calendar nextExecutionTime = new GregorianCalendar();
            Date lastActualExecutionTime = t.lastActualExecutionTime();
            nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
            Event event = eventRepository.findFirst1ByIs1hourEmailTransferFalseAndIsDeletedFalseOrderByStartAsc( );
            if (event == null) {
                nextExecutionTime.add(Calendar.SECOND, 5 * 60);
            } else {
                long secend = (event.getStart().getTime() - new Date().getTime()) / 1000;
                long diff = secend - Utils.BEFORE_1HOUR_SECEND;
                nextExecutionTime.add(Calendar.SECOND, (diff < 0L) ? (int) 2L : (int) diff);
            }
            log.info("nextExecutionTime:  {}", nextExecutionTime.getTime());
            return nextExecutionTime.getTime();
        });
    }

    public void scheduledDatabase(Event event) {
        log.info("scheduledDatabase: Next execution event of this will be taken from DB -> {}", event);
        if (event == null) return;
        emailService.before1hour(event);
        event.set1hourEmailTransfer(true);
        eventRepository.save(event);
    }

}
