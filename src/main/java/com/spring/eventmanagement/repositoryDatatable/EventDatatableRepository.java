package com.spring.eventmanagement.repositoryDatatable;

import com.spring.eventmanagement.entity.Admin;
import com.spring.eventmanagement.entity.Event;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface EventDatatableRepository extends DataTablesRepository<Event, Integer> {

//    DataTablesOutput<Event> findAllByCreatedByAndIsDeletedFalseOrderByIdDesc(Admin admin, DataTablesInput dataTablesInput);
}


