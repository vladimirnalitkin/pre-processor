package com.van.testService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.van.processor.annotation.RepositoryOnItem;
import lombok.Data;

import javax.persistence.*;

import static com.van.testService.common.Utils.getCurrentPrbrId;

@Entity
@Table(name = "HOUSE")
@Data
//@RestControllerOnItem(collectionResourceRel = HOUSE_SERVICE_TAG, path = HOUSE_URL)
@RepositoryOnItem(allOperFilterBeanName = "prbrFilter")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, nullable = false)
    protected Integer houseId = null;

    @Column(name = "PRBR_ID")
    @JsonIgnore
    protected long prbrId;

    @Column(name = "NUM", nullable = false)
    private Integer num;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID")
    @JsonBackReference
    private Address address;

    void setPrbrIdBefore() {
        setPrbrId(getCurrentPrbrId());
    }

    void setPrbrIdAfter() {
        setPrbrId(getCurrentPrbrId());
    }
}