package com.spring.eventmanagement.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event  implements Serializable {

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;


    @Column(name = "end")
    @NotEmpty(message = "Please provide end time")
    @Temporal(TemporalType.TIMESTAMP)
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

    @Column(name = "is_deleted", nullable = false,columnDefinition="bit(1) default false"  )
    private boolean isDeleted=false;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Admin createdBy;


}