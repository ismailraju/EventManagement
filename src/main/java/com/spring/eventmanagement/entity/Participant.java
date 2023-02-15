package com.spring.eventmanagement.entity;

import com.spring.eventmanagement.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;


@Entity
@Table(name = "participant",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email","event_id"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    @NotEmpty(message = "Please provide name")
    private String name;


    @Column(name = "email", nullable = false )
    @Email(message = "Please provide a valid e-mail")
    @NotEmpty(message = "Please provide an e-mail")
    private String email;

    @Column(name = "status")
    private Status status;


    @Column(name = "creation")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date creation;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;


}