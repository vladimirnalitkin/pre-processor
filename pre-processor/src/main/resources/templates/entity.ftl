package com.van.testService.model;

import com.van.processor.annotation.RepositoryOnItem;
import com.van.processor.annotation.RestControllerOnItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ADDRESS")
@RestControllerOnItem(collectionResourceRel = "addresses", path = "/addresses")
@RepositoryOnItem(mFilterName = "prbrFilter")
public class Address extends GenericEntity {

    @Column(name = "STREET")
    private String street;

    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;

    @Column(name = "ZIP_CODE")
    private String zipCode;

}