package com.van.testService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.van.processor.annotation.RepositoryOnItem;
import com.van.processor.annotation.RestControllerOnItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import static com.van.testService.common.Constant.*;

@Entity
@Table(name = "ARTICLE_COMMENT")
@Data
@EqualsAndHashCode(callSuper=true)
@RestControllerOnItem(collectionResourceRel = ARTICLE_COMMENT_SERVICE_TAG, path = ARTICLE_COMMENT_URL)
@RepositoryOnItem(allOperFilterBeanName = "prbrFilter", beforeAllOperBeanName = "beforeAnyApply")
public class Comment extends GenericEntity{

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "ARTICLE_ID")
	@JsonBackReference
	private Article article;

	@Column(name = "POSTED_BY")
	private String postedBy;

	@Column(name = "CONTENT")
	private String content;
}
