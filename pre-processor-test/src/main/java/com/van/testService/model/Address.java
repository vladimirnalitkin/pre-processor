package com.van.testService.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.van.processor.annotation.RepositoryOnItem;
import com.van.processor.annotation.RestControllerOnItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

import static com.van.testService.common.Constant.ADDRESS_SERVICE_TAG;
import static com.van.testService.common.Constant.ADDRESS_URL;

@Entity
@Table(name = "ADDRESS")
@Data
@EqualsAndHashCode(callSuper=true)
@RestControllerOnItem(collectionResourceRel = ADDRESS_SERVICE_TAG, path = ADDRESS_URL)
@RepositoryOnItem(allOperFilterBeanName = "prbrFilter", beforeAllOperBeanName = "beforeAnyApply")
public class Address extends GenericEntity {

    @Column(name = "STREET")
    private String street;

    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "address", fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<House> houses;

}