package com.van.testService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.van.processor.annotation.CalculatedColumn;
import com.van.processor.annotation.RepositoryOnItem;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ADDRESS")
//@RestControllerOnItem(collectionResourceRel = ADDRESS_SERVICE_TAG, path = ADDRESS_URL)
@RepositoryOnItem(allOperFilterBeanName = "prbrFilter", beforeAllOperBeanName = "beforeAnyApply")
class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, nullable = false)
    protected Long entityId = null;

    @Column(name = "PRBR_ID")
    @JsonIgnore
    protected Long prbrId;

    @Column(name = "STREET")
    private String street;

    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;

    @Column(name = "ZIP_CODE")
    private String zipCode;

	public Long getEntityId() {
		return entityId;
	}

	public void setId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getPrbrId() {
		return prbrId;
	}

	public void setPrbrId(Long prbrId) {
		this.prbrId = prbrId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}

@Entity
@Table(name = "COMPANY", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
//@RestControllerOnItem(collectionResourceRel = "companies", path = "/companies", security = false)
//@RepositoryOnItem(mFilterName = "prbrFilter")
class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, nullable = false)
    protected Long entityId = null;

    @Column(name = "PRBR_ID")
    @JsonIgnore
    protected Long prbrId;

    @Column(name = "NAME", nullable = false)
    @ApiModelProperty(value = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "ADDRESS_ID")
    @JsonManagedReference
    @ApiModelProperty(value = "addresses")
    private List<Address> addresses;

	public Long getEntityId() {
		return entityId;
	}

	public void setId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getPrbrId() {
		return prbrId;
	}

	public void setPrbrId(Long prbrId) {
		this.prbrId = prbrId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<com.van.testService.model.Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<com.van.testService.model.Address> addresses) {
		this.addresses = addresses;
	}
}

@Entity
@Table(name = "EMPLOYEE")
//@RestControllerOnItem(collectionResourceRel = "employee", path = "/employee")
@RepositoryOnItem(allOperFilterBeanName = "prbrFilter", beforeCreateBeanName = "prbrCreateFilter")
class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, nullable = false)
    protected Long entityId = null;

    @Column(name = "PRBR_ID")
    @JsonIgnore
    protected Long prbrId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @CalculatedColumn(value = "CONCAT(name,surname)")
    private String fulName;

    @Transient
    @JsonIgnore
    private String test;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    @JsonBackReference
    private Company company;

	public Long getEntityId() {
		return entityId;
	}

	public void setId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getPrbrId() {
		return prbrId;
	}

	public void setPrbrId(Long prbrId) {
		this.prbrId = prbrId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFulName() {
		return fulName;
	}

	public void setFulName(String fulName) {
		this.fulName = fulName;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public com.van.testService.model.Company getCompany() {
		return company;
	}

	public void setCompany(com.van.testService.model.Company company) {
		this.company = company;
	}
}
