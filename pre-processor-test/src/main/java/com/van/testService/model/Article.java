package com.van.testService.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.van.processor.annotation.RepositoryOnItem;
import com.van.processor.annotation.RestControllerOnItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import java.util.List;

import static com.van.testService.common.Constant.*;

@Entity
@Table(name = "ARTICLE")
@Data
@EqualsAndHashCode(callSuper=true)
@RestControllerOnItem(collectionResourceRel = ARTICLE_SERVICE_TAG, path = ARTICLE_URL)
@RepositoryOnItem(allOperFilterBeanName = "prbrFilter", beforeAllOperBeanName = "beforeAnyApply")
public class Article extends GenericEntity {
	@Column(name = "NAME")
	private String name;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "CONTENT")
	private String content;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "article", fetch = FetchType.EAGER, orphanRemoval = true)
	@JsonManagedReference
	private List<Comment> comments;
}
