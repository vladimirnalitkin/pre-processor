package com.van.testService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static com.van.testService.common.Utils.getCurrentPrbrId;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public abstract class GenericEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, nullable = false)
    @Getter
    @Setter
    protected Long id = null;

    @Column(name = "PRBR_ID")
    @JsonIgnore
    @Getter
    @Setter
    protected Long prbrId;

}
