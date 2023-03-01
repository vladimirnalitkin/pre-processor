package com.van.testService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.van.processor.annotation.CalculatedColumn;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
//@RestControllerOnItem(collectionResourceRel = "employee", path = "/employee")
//@RepositoryOnItem(mFilterName = "prbrFilter")
public class Employee extends GenericEntity {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "SURNAME", nullable = false)
    private String surname;

    @CalculatedColumn(value = "CONCAT(name,surname)")
    private String fulName;

    @Transient
    @JsonIgnore
    private String test;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID")
    @JsonBackReference
    private Address address;
}
