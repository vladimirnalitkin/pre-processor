package com.van.processor;

import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.truth0.Truth.ASSERT;

class MainProcessorTest {

    @Test
    public void entitiesClassCompiles() throws MalformedURLException {
        final MainProcessor processor = new MainProcessor();
        ASSERT.about(JavaSourceSubjectFactory.javaSource())
                .that(JavaFileObjects.forResource("TestEntities.java"))
                .processedWith(processor)
                .compilesWithoutError();
    }


 /*   @Test
    public void EmptyClassCompiles() throws MalformedURLException {
        JavaFileObject helloWorld = JavaFileObjects.forResource("TestEntities.java");
        Compilation compilation =
                javac()
                        .withProcessors(new NoHelloWorld())
                        .compile(helloWorld);
        assertThat(compilation).succeeded();
    }*/
}