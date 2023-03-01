package com.van.processor;

import com.google.auto.service.AutoService;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.van.processor.annotation.*;
import com.van.processor.model.EntityDescription;
import com.van.processor.model.FieldDescription;
import com.van.processor.model.RefEntityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.persistence.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

import static com.van.processor.common.Constant.*;
import static com.van.processor.common.Utils.*;

@SupportedAnnotationTypes({ENTITY_ANNOTATION, REPOSITORY_ON_ITEM_ANNOTATION, REST_CONTROLLER_ON_ITEM_ANNOTATION})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class MainProcessor extends BaseAbstractProcessor {
	private static String UNKNOW = "Unknow";
	private static String VOID_CLASS = "void";
	private static Logger log = LoggerFactory.getLogger(MainProcessor.class);
	private List<Class<? extends Annotation>> annotatedIdFieldsFilter = Collections.singletonList(Id.class);
	private List<Class<? extends Annotation>> annotatedFieldsIncluded = Arrays.asList(Column.class, CalculatedColumn.class, ManyToOne.class, OneToMany.class);
	private List<Class<? extends Annotation>> annotatedFieldsSelect = Arrays.asList(Column.class, CalculatedColumn.class);
	private List<Class<? extends Annotation>> annotatedFieldsInsert = Collections.singletonList(Column.class);
	private List<Class<? extends Annotation>> annotatedSubFieldsIncluded = Arrays.asList(ManyToOne.class, OneToMany.class);
	private List<Class<? extends Annotation>> annotatedFieldsExcluded = Collections.singletonList(Transient.class);
	//private List<Class<? extends Annotation>> annotatedRunMethodsFilter = Arrays.asList(RunBeforeCreate.class, RunBeforeUpdate.class);

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		log.debug("start annotations = " + annotations);

		//----------- this is fields by table ---------------
		// in case the entity does not have any ManyToOne.class, OneToMany.class - in this list we wil have one table
		final Map<String, EntityDescription> entityDescriptionMap = new HashMap<>();

		return !annotations.isEmpty()
				&& entityProcess(
				annotations.stream()
						.filter(el -> ENTITY_ANNOTATION.equals(el.toString()))
						.collect(Collectors.toSet())
				, roundEnv, entityDescriptionMap)
				&& repositoryProcess(
				annotations.stream()
						.filter(el -> REPOSITORY_ON_ITEM_ANNOTATION.equals(el.toString()))
						.collect(Collectors.toSet())
				, roundEnv, entityDescriptionMap)
				&& controllerProcess(annotations.stream()
						.filter(el -> REST_CONTROLLER_ON_ITEM_ANNOTATION.equals(el.toString()))
						.collect(Collectors.toSet())
				, roundEnv, entityDescriptionMap)
				;
	}

	private static void debug(StringBuilder sb, String str) {
		if (log.isDebugEnabled()) {
			sb.append(str);
			log.debug(sb.toString());
		}
	}

	private boolean entityProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv, Map<String, EntityDescription> entityDescriptionMap) {
		log.debug("------------------------------------- entity process start -------------------------------------");

		for (TypeElement annotation : annotations) {
			if (log.isDebugEnabled()) {
				log.debug("annotation :" + annotation.getSimpleName().toString());
			}

			for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
				log.debug("------------------------------->  start new Entity ");
				if (element.getKind() != ElementKind.CLASS) {
					log.error("Can be applied to class.");
					return true;
				}
				TypeElement typeElement = (TypeElement) element;
				String className = typeElement.getQualifiedName().toString();
				if (log.isDebugEnabled()) {
					log.debug("className :" + className);
				}

				Table table = element.getAnnotation(Table.class);
				String tableName = table != null ? table.name() : getLastWord(className);
				if (log.isDebugEnabled()) {
					log.debug("table name :" + tableName);
				}
				RepositoryOnItem repositoryOnItem = element.getAnnotation(RepositoryOnItem.class);
				RestControllerOnItem restControllerOnItem = element.getAnnotation(RestControllerOnItem.class);

				Map<Boolean, List<Element>> annotatedElementsByType = getAnnotatedElementsByType(typeElement);
				if (log.isDebugEnabled()) {
					log.debug("annotatedElementsByType :" + annotatedElementsByType.toString());
				}

				List<Element> annotatedFields = getAnnotatedFields(annotatedElementsByType, annotatedFieldsIncluded, annotatedFieldsExcluded);
				if (log.isDebugEnabled()) {
					log.debug("all field Name :" + annotatedFields.toString());
				}

				Optional<FieldDescription> idField = getIdField(annotatedFields);
				if (log.isDebugEnabled()) {
					log.debug("idField :" + idField.orElseGet(() -> FieldDescription.builder()
							.nameInJava(UNKNOW)
							.nameInDb(UNKNOW)
							.type(UNKNOW)
							.build()
					).toString());
				}

				log.debug("select fields: start");
				List<FieldDescription> selectFields = annotatedFields
						.stream()
						.map(el -> {
									StringBuilder logStr = new StringBuilder();
									if (isAnnotated(el, annotatedSubFieldsIncluded)) {
										debug(logStr, "select fields: annotatedSubFieldsIncluded -> " + el.getSimpleName().toString());
										String refEntityClassName = null;
										String refFieldName = null;
										if (el.getAnnotation(ManyToOne.class) != null) { //  -- ManyToOne
											debug(logStr, ", annotation -> ManyToOne");
											ManyToOne manyToOne = el.getAnnotation(ManyToOne.class);

											TypeMirror value = null;
											try {
												manyToOne.targetEntity();
											} catch (MirroredTypeException mte) {
												value = mte.getTypeMirror();
											}

											if (value != null) {
												refEntityClassName = value.toString();
												debug(logStr, ", oneToMany.targetEntity() = " + value.toString());
												// ref class name
												if (!VOID_CLASS.equals(value.toString())) {
													debug(logStr, ", targetEntity() = " + refEntityClassName);
													refEntityClassName = refEntityClassName;
												} else {
													debug(logStr, ", targetEntity() = void.class");
													refEntityClassName = getClassOfGeneric((el.asType().toString()));
												}
											}
											// ref field
											if (el.getAnnotation(JoinColumn.class) != null) {
												refFieldName = el.getAnnotation(JoinColumn.class).name();
											} else {
												refFieldName = getLastWord(el.asType().toString()) + PLUS_ID;
											}
										} else if (el.getAnnotation(OneToMany.class) != null) { // -- OneToMany
											debug(logStr, ", annotation -> OneToMany");
											OneToMany oneToMany = el.getAnnotation(OneToMany.class);
											debug(logStr, oneToMany.toString());

											TypeMirror value = null;
											try {
												oneToMany.targetEntity();
											} catch (MirroredTypeException mte) {
												value = mte.getTypeMirror();
											}

											if (value != null) {
												refEntityClassName = value.toString();
												debug(logStr, ", oneToMany.targetEntity() = " + value.toString());
												// ref class name
												if (!VOID_CLASS.equals(value.toString())) {
													debug(logStr, ", targetEntity() = " + refEntityClassName);
													refEntityClassName = refEntityClassName;
												} else {
													debug(logStr, ", targetEntity() = void.class, get class from java");
													refEntityClassName = getClassOfGeneric(el.asType().toString());
												}
											}
											// ref field
											if (el.getAnnotation(JoinColumn.class) != null) {
												refFieldName = el.getAnnotation(JoinColumn.class).name();
											} else {
												if (!Strings.isNullOrEmpty(oneToMany.mappedBy())) {
													refFieldName = oneToMany.mappedBy();
												}
											}
										} else {
											debug(logStr, ", incorrect annotation");
											throw new RuntimeException("incorrect annotation");
										}
										assert refFieldName != null;
										return FieldDescription.builder()
												.nameInJava(el.getSimpleName().toString())
												.nameInDb(UNKNOW)
												.type(getClassOfGeneric(el.asType().toString()))
												.refTable(RefEntityClass.of(refEntityClassName, refFieldName.toUpperCase(), getLastWord(refEntityClassName), getCollectioType(el.asType().toString())))
												.build();
									} else {
										debug(logStr, "select fields: not annotatedSubFieldsIncluded -> " + el.getSimpleName().toString());
										return FieldDescription.builder()
												.nameInJava(el.getSimpleName().toString())
												.nameInDb(el.getAnnotation(Column.class) != null ? el.getAnnotation(Column.class).name() : "(" + el.getAnnotation(CalculatedColumn.class).value() + ")")
												.type(getLastWord(el.asType().toString()))
												.build();
									}
								}
						)
						.collect(Collectors.toList());

				if (log.isDebugEnabled()) {
					log.debug("---> select fields");
					selectFields.forEach(e -> log.debug(e.toString()));
					log.debug("---> /select fields");
				}

				List<FieldDescription> insertFields = annotatedFields
						.stream()
						.filter(el -> isAnnotated(el, annotatedFieldsInsert))
						.map(el -> FieldDescription.builder()
								.nameInJava(el.getSimpleName().toString())
								.nameInDb(el.getAnnotation(Column.class).name())
								.type(getLastWord(el.asType().toString()))
								.build()
						)
						.collect(Collectors.toList());
				if (log.isDebugEnabled()) {
					log.debug("---> insert fields");
					insertFields.forEach(e -> log.debug(e.toString()));
					log.debug("---> /insert fields");
				}

				log.debug("MAP_MANDATORY_FILTER_NAME start");

      /*          List<? extends Element> annotatedRunMethods = annotatedElementsByType.get(false)
                        .stream()
                        .filter(el -> el.getKind() == ElementKind.METHOD && isAnnotated(el, annotatedRunMethodsFilter)).collect(Collectors.toList());

                if (log.isDebugEnabled()) {
                    log.debug("annotatedMethods :" + annotatedRunMethods);
                }

                Multimap<String, String> mapByRunMethods = annotatedRunMethods
                        .stream()
                        .map(el -> el.getAnnotationMirrors()
                                .stream()
                                .map(annotationName ->
                                        new Pair<>(getLastWord(annotationName.toString()), el.getSimpleName().toString())
                                ).collect(Collectors.toList())
                        ).collect(Collectors.toList())
                        .stream()
                        .flatMap(List::stream)
                        .collect(MultimapCollector.toMultimap(el -> ((Pair) el).getKey().toString(), el -> ((Pair) el).getValue().toString()));

                if (log.isDebugEnabled()) {
                    log.debug("annotatedRunMethods :" + mapByRunMethods);
                }*/
				log.debug("MAP_MANDATORY_FILTER_NAME end");

				entityDescriptionMap.put(className
						, EntityDescription.of(tableName, idField, selectFields, insertFields, ArrayListMultimap.create() //mapByRunMethods
								, restControllerOnItem != null
								, repositoryOnItem != null)
				);
			}
		}
		log.debug("-------------------------------------  entity process end -------------------------------");
		if (log.isDebugEnabled()) {
			entityDescriptionMap.forEach((key, value) -> log.debug(key + "->" + value.toString()));
		}
		return true;
	}

	private boolean repositoryProcess(Set<? extends TypeElement> annotations
			, RoundEnvironment roundEnv, Map<String, EntityDescription> entityDescriptionMap) {
		log.debug("------------------------------------- BuilderProcessorRepository process start -------------------------------------");
		for (TypeElement annotation : annotations) {
			log.debug("annotation :" + annotation.getSimpleName().toString());

			for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
				log.debug("------------------------------->  start new Repository ");
				if (element.getKind() != ElementKind.CLASS) {
					log.error("Can be applied to class.");
					return true;
				}

				RepositoryOnItem repositoryOnItem = element.getAnnotation(RepositoryOnItem.class);
				TypeElement typeElement = (TypeElement) element;
				String className = typeElement.getQualifiedName().toString();
				log.debug("className :" + className);

				EntityDescription entityDescription = entityDescriptionMap.get(className);
				if (log.isDebugEnabled()) {
					log.debug("entityDescription :" + entityDescription);
				}
				if (entityDescription == null) {
					throw new RuntimeException("Class annotated @RepositoryOnItem should has @Entity annotation");
				}

				// list of select fields
				log.debug("------------------------------->  selectFields");
				final List<FieldDescription> subEntities = new LinkedList<>();
				List<FieldDescription> selectFields = entityDescription.getSelectFields()
						.stream()
						.map(fd -> {
							if (log.isDebugEnabled()) {
								log.debug("fd-> " + fd);
							}
							FieldDescription result = fd;
							if (fd.getRefEntityClass() != null
									&& entityDescriptionMap.containsKey(fd.getRefEntityClass().getEntityClassName())
									&& entityDescriptionMap.get(fd.getRefEntityClass().getEntityClassName()).getHasRepositoryOnItem()) {
								if (log.isDebugEnabled()) {
									log.debug("subEntity fd -> :" + fd);
								}
								EntityDescription subEntityDescription = entityDescriptionMap.get(fd.getRefEntityClass().getEntityClassName());
								if (log.isDebugEnabled()) {
									log.debug("subEntityDescription :" + subEntityDescription);
								}
								FieldDescription subIdField = subEntityDescription.getIdField().orElse(FieldDescription.builder().build());
								if (log.isDebugEnabled()) {
									log.debug("subIdField :" + subIdField);
								}
								result = FieldDescription.builder()
										.nameInJava(fd.getNameInJava())
										.nameInDb(fd.getRefEntityClass().getFieldForMerge())
										.type(getLastWord(subIdField.getType()))
										.refTable(fd.getRefEntityClass())
										.build();
								subEntities.add(result);
							}
							if (log.isDebugEnabled()) {
								log.debug("result :" + result);
							}
							return result;
						})
						.collect(Collectors.toList());
				if (log.isDebugEnabled()) {
					selectFields.forEach(item -> log.debug(item.toString()));
				}
				log.debug("------------------------------->  /selectFields");

				// list of sub entities
				if (log.isDebugEnabled()) {
					log.debug("------------------------------->  subEntities");
					subEntities.forEach(item -> log.debug(item.toString()));
					log.debug("------------------------------->  /subEntities");
				}
				selectFields = selectFields.stream().filter(fd -> fd.getRefEntityClass() == null).collect(Collectors.toList());

				Map<String, Object> context = getNames(className, ImmutableMap.<String, Object>builder()
						.put(PARAM_TABLE_NAME, entityDescription.getTableName())
						.put(PARAM_ID_FIELD, entityDescription.getIdField().orElseGet(this::getDefIdField))
						.put(PARAM_MANDATORY_FILTER_NAME, repositoryOnItem.allOperFilterBeanName())
						.put(PARAM_ON_ITEM_BEFORE_ANY_NAME, repositoryOnItem.beforeAllOperBeanName())
						.put(PARAM_ON_ITEM_BEFORE_CREATE_NAME, repositoryOnItem.beforeCreateBeanName())
						.put(PARAM_ON_ITEM_BEFORE_UPDATE_NAME, repositoryOnItem.beforeUpdateBeanName())
						.put(PARAM_ON_ITEM_BEFORE_DELETE_NAME, repositoryOnItem.beforeDeleteBeanName())
						.put(PARAM_ON_ITEM_AFTER_CREATE_NAME, repositoryOnItem.afterCreateBeanName())
						.put(PARAM_ON_ITEM_AFTER_UPDATE_NAME, repositoryOnItem.afterUpdateBeanName())
						.put(UN_PROVIDED, UN_PROVIDED)
						.put(PARAM_SELECT_FIELDS_NAME, selectFields)
						.put(PARAM_SUB_ENTITIES, subEntities)
						.put(PARAM_INSERT_FIELDS_NAME, entityDescription.getInsertFields())
						.put(PARAM_RUN_BEFORE_CREATE_NAME, entityDescription.getMapByRunMethods().get(PARAM_RUN_BEFORE_CREATE_NAME))
						.put(PARAM_RUN_BEFORE_UPDATE_NAME, entityDescription.getMapByRunMethods().get(PARAM_RUN_BEFORE_UPDATE_NAME))
						.build()
				);
				try {
					generateContent(context.get(MAP_BUILDER_CLASS_NAME).toString() + REPOSITORY_PREFIX
							, "repository.ftl", context);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		log.debug("------------------------------------- BuilderProcessorRepository process end -------------------------------------");
		return true;
	}

	private boolean controllerProcess(Set<? extends TypeElement> annotations, RoundEnvironment
			roundEnv, Map<String, EntityDescription> fieldByTable) {
		log.debug("BuilderProcessorController process start");
		for (TypeElement annotation : annotations) {
			log.debug("annotation :" + annotation.getSimpleName().toString());

			for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {

				if (element.getKind() != ElementKind.CLASS) {
					log.error("Can be applied to class.");
					return true;
				}

				RestControllerOnItem restControllerOnItem = element.getAnnotation(RestControllerOnItem.class);
				TypeElement typeElement = (TypeElement) element;
				String className = typeElement.getQualifiedName().toString();
				log.debug("className :" + className);

				Map<Boolean, List<Element>> annotatedElementsByType = getAnnotatedElementsByType(typeElement);
				if (log.isDebugEnabled()) {
					log.debug("annotatedElementsByType :" + annotatedElementsByType.toString());
				}

				List<Element> annotatedFields = getAnnotatedFields(annotatedElementsByType, annotatedFieldsIncluded, annotatedFieldsExcluded);
				if (log.isDebugEnabled()) {
					log.debug("all field Name :" + annotatedFields.toString());
				}

				Optional<FieldDescription> idField = getIdField(annotatedFields);
				log.debug("aidField :" + idField.toString());

				Map<String, Object> context = getNames(className
						, ImmutableMap.<String, Object>builder()
								.put(MAP_HTTP_URL_NAME, restControllerOnItem.path())
								.put(MAP_SECURITY_NAME, restControllerOnItem.security())
								.put(PARAM_ID_FIELD, idField.orElseGet(this::getDefIdField))
								.build()
				);
				try {
					generateContent(context.get(MAP_BUILDER_CLASS_NAME).toString() + SERVICE_PREFIX
							, "service.ftl", context);
					generateContent(context.get(MAP_BUILDER_CLASS_NAME).toString() + CONTROLLER_PREFIX
							, "controller.ftl", context);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		log.debug("BuilderProcessorController process end");
		return true;
	}

	//----------------------------------- utils functions --------------------------------------------------
	private Map<Boolean, List<Element>> getAnnotatedElementsByType(TypeElement typeElement) {
		return processingEnv.getElementUtils().getAllMembers(typeElement)
				.stream()
				.filter(el -> el.getKind() == ElementKind.FIELD || el.getKind() == ElementKind.METHOD)
				.collect(Collectors.partitioningBy(el -> ((Element) el).getKind() == ElementKind.FIELD)
				);
	}

	private List<Element> getAnnotatedFields(Map<Boolean, List<Element>> source
			, List<Class<? extends Annotation>> included
			, List<Class<? extends Annotation>> excluded
	) {
		return source.get(true)
				.stream()
				.filter(el -> isAnnotated(el, included) && isNotAnnotated(el, excluded)
				).collect(Collectors.toList());
	}

	private Optional<FieldDescription> getIdField(List<Element> annotatedFields) {
		return annotatedFields
				.stream()
				.filter(el -> isAnnotated(el, annotatedIdFieldsFilter))
				.findFirst()
				.map(el -> FieldDescription.builder()
						.nameInJava(el.getSimpleName().toString())
						.nameInDb(el.getAnnotation(Column.class) != null ? el.getAnnotation(Column.class).name() : "(" + el.getAnnotation(CalculatedColumn.class).value() + ")")
						.type(getLastWord(el.asType().toString()))
						.build()
				);
	}

	private static String convert(Map map) {
		return Joiner.on(",").withKeyValueSeparator(" - > ").join(map);
	}
}
