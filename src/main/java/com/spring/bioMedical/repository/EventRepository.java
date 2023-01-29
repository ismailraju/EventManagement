package com.spring.bioMedical.repository;

import com.spring.bioMedical.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    public List<Event> findByStartGreaterThanEqualAndEndLessThanEqual(Date start, Date end);


    @Query("select b from Event b where b.start >= ?1 and b.end <= ?2")
    public List<Event> findByDateBetween(Date start, Date end);

}