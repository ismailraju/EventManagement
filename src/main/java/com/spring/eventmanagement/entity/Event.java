package com.spring.eventmanagement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event implements Serializable {

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
//    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="dd MMM yyyy HH:mm:ss")
//    @Type(type="org.jadira.usertype.dateandtime.legacyjdk.PersistentDate")
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

    @Column(name = "is_deleted", nullable = false, columnDefinition = "bit(1) default false")
    private boolean isDeleted = false;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Admin createdBy;

    @Column(name = "is_1hour_email_transfer", nullable = false, columnDefinition = "bit(1) default false")
    private boolean is1hourEmailTransfer = false;

    @Column(name = "is_repeat", nullable = false, columnDefinition = "bit(1) default false")
    private boolean isRepeat = false;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> dow ;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date start2;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date end2;

//    @Transient
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String startTime;
//
//    @Transient
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String endTime;

//
//    @Transient
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String startRecur;
//
//    @Transient
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String endRecur;


}