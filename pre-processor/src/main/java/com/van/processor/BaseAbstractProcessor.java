package com.van.processor;

import com.google.common.collect.ImmutableMap;
import com.van.processor.model.FieldDescription;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.van.processor.common.Constant.*;

public class BaseAbstractProcessor extends AbstractProcessor {
    private static final Logger log = LoggerFactory.getLogger(BaseAbstractProcessor.class);
    private static final Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
    private static final FieldDescription DEF_ID_FIELD = FieldDescription.builder()
            .nameInJava("Id")
            .nameInDb("ID")
            .type("Long")
            .build();

    static {
        log.debug("BaseAbstractProcessor freemarker config start");
        cfg.setClassForTemplateLoading(BaseAbstractProcessor.class, "/templates");
        try {
			URL url = BaseAbstractProcessor.class.getClassLoader().getResource("templates");
			assert url!=null : "Could not found /templates";
            cfg.setDirectoryForTemplateLoading(new File(url.getFile()));
        } catch (IOException exp) {
            log.error(exp.getLocalizedMessage());
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        //cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        //cfg.setFallbackOnNullLoopVariable(false);
        log.debug("BaseAbstractProcessor freemarker config end");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

    static Map<String, Object> getNames(String className, Map<String, Object> additionalParams) {
        log.debug("getNames start className =" + className);
        String packageName = "default";
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String simpleClassName = className.substring(lastDot + 1);
        String builderClassName = className;
        String builderSimpleClassName = builderClassName.substring(lastDot + 1);

        log.debug("getNames end className =" + className);
        return ImmutableMap.<String, Object>builder()
                .putAll(additionalParams)
                .put(MAP_PACKAGE_NAME, packageName)
                .put(MAP_SIMPLE_CLASS_NAME, simpleClassName)
                .put(MAP_SIMPLE_CLASS_NAME_UPPER, simpleClassName.toUpperCase())
                .put(MAP_BUILDER_CLASS_NAME, builderClassName)
                .put(MAP_BUILDER_SIMPLE_CLASS_NAME, builderSimpleClassName)
                .build();

    }

    void generateContent(String javaFileName, String templateName, Map context) throws IOException {
        log.debug("generateContent start javaFileName =" + javaFileName + ", templateName=" + templateName + ", context=" + context);
        try {
            Template template = cfg.getTemplate(templateName, DEFAULT_ENCODING);
            JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(javaFileName);
            try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                template.process(context, out);
            }
            log.debug("generateContent end");
        } catch (Exception e) {
            log.error("FreeMarker template doesn't exist", e);
        }
    }

    final boolean isAnnotated(Element target, List<Class<? extends Annotation>> annotationList) {
        return getAnnotation(target, annotationList) != null;
    }

    final boolean isNotAnnotated(Element target, List<Class<? extends Annotation>> annotationList) {
        return getAnnotation(target, annotationList) == null;
    }

    private Object getAnnotation(Element target, List<Class<? extends Annotation>> annotationList) {
        for (Class<? extends Annotation> annotationType : annotationList) {
            Annotation potentialAnnotation = target.getAnnotation(annotationType);
            if (potentialAnnotation != null) {
                return potentialAnnotation;
            }
        }
        return null;
    }

    FieldDescription getDefIdField() {
        return DEF_ID_FIELD;
    }
}
