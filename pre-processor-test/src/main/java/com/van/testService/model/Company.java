package com.van.testService.model;

import com.van.processor.annotation.RepositoryOnItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "COMPANY", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
//@RestControllerOnItem(collectionResourceRel = "companies", path = "/companies", security = false)
@RepositoryOnItem(allOperFilterBeanName = "prbrFilter"
		, beforeAllOperBeanName = "beforeAnyApply"
		, beforeCreateBeanName = "companyBeforeCreateApply")
public class Company extends GenericEntity{

    @Column(name = "NAME", nullable = false)
    private String name;

	@Column(name = "TEST_NAME", nullable = true)
	private String testName;

	@Column(name = "ZIP", nullable = true)
	private Long zip;
}
