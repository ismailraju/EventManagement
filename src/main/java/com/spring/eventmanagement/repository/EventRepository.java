package com.spring.eventmanagement.repository;

import com.spring.eventmanagement.entity.Admin;
import com.spring.eventmanagement.entity.Event;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Query("select b from Event b where b.start >= ?1 and b.end <= ?2 and b.createdBy.id <= ?3")
    public List<Event> findByDateBetween(Date start, Date end, Integer adminId);

    @Query("select b from Event b where b.start >= ?1 and b.end <= ?2 and b.createdBy.id <= ?3 and b.isDeleted = ?4")
    public List<Event> findByDateBetween(Date start, Date end, Integer adminId,boolean isDeleted);

    List<Event> findAllByCreatedBy(Admin admin);

    List<Event> findAllByCreatedBy(Admin admin, Pageable pageable);

    Long countByCreatedBy(Admin admin);

    List<Event> findAllByCreatedByOrderByIdDesc(Admin build, Pageable of);

    List<Event> findAllByIsDeletedTrue();

    List<Event> findAllByCreatedByAndIsDeletedTrue(Admin build);

    List<Event> findAllByIsDeletedFalse();

    List<Event> findAllByCreatedByAndIsDeletedFalse(Admin build);

    List<Event> findAllByCreatedByAndIsDeletedFalseOrderByIdDesc(Admin build, Pageable of);

    Long countByCreatedByAndIsDeletedFalse(Admin build);

    List<Event> findAllByCreatedByAndIsDeletedFalseAndStartAndEnd(Admin admin, Date start, Date end);
}