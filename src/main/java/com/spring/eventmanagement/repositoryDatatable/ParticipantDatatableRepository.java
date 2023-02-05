package com.spring.eventmanagement.repositoryDatatable;

import com.spring.eventmanagement.entity.Event;
import com.spring.eventmanagement.entity.Participant;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ParticipantDatatableRepository extends DataTablesRepository<Participant, Integer> {

}


