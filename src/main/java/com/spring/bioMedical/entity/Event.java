package com.spring.bioMedical.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;


@Entity
@Table(name = "event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title", nullable = false)
    @NotEmpty(message = "Please provide title")
    private String title;


    @Column(name = "description", nullable = false)
    @NotEmpty(message = "Please provide description")
    private String description;

    @Column(name = "location", nullable = false)
    private String location;


    @Column(name = "start")
    @NotEmpty(message = "Please provide start time")
    private Date start;


    @Column(name = "end")
    @NotEmpty(message = "Please provide end time")
    private Date end;


    @Column(name = "creation", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date creation;

    @Column(name = "modification")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date modification;

    @Column(name = "going")
    private int going;

    @Column(name = "not_going")
    private int notGoing;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Admin createdBy;


}