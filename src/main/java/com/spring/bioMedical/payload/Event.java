package com.spring.bioMedical.payload;

import com.spring.bioMedical.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private Integer id;
    private String title;
    private String description;
    private String location;
    private Date start;
    private Date end;
    private Date creation;
    private Date modification;
    private int going;
    private int notGoing;
    private Admin createdBy;

}