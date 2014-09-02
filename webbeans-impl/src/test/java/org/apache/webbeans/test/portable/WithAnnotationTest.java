/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.test.portable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.webbeans.test.AbstractUnitTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test &#064WithAnnotation; annotation
 */
public class WithAnnotationTest extends AbstractUnitTest
{

    @Test
    public void testWithAnnotation()
    {
        WithAnnotationExtension.scannedClasses = 0;

        addExtension(new WithAnnotationExtension());
        startContainer(WithoutAnyAnnotation.class, WithAnnotatedClass.class, WithAnnotatedField.class, WithAnnotatedMethod.class);

        Assert.assertEquals(3, WithAnnotationExtension.scannedClasses);
    }


    public static class WithAnnotationExtension implements Extension
    {
        public static int scannedClasses = 0;

        public void processClassess(@Observes @WithAnnotations(MyAnnoation.class) ProcessAnnotatedType pat)
        {
            scannedClasses += 1;
        }

        public void dontProcessClassess(@Observes @WithAnnotations(AnotherAnnoation.class) ProcessAnnotatedType pat)
        {
            throw new IllegalStateException("This observer must not get called by the container!");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
    public static @interface MyAnnoation
    {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
    public static @interface AnotherAnnoation
    {
    }


    /**
     * This class should not get picked up by the {@link org.apache.webbeans.test.portable.WithAnnotationTest.WithAnnotationExtension}
     */
    @ApplicationScoped
    public static class WithoutAnyAnnotation
    {
        public int getMeanintOfLife()
        {
            return 42;
        }
    }


    @ApplicationScoped
    @MyAnnoation
    public static class WithAnnotatedClass
    {
        public int getMeanintOfLife()
        {
            return 42;
        }
    }

    @ApplicationScoped
    public static class WithAnnotatedField
    {
        @MyAnnoation
        private int x;

        public int getMeanintOfLife()
        {
            return 42;
        }
    }

    @ApplicationScoped
    public static class WithAnnotatedMethod
    {
        @MyAnnoation
        public int getMeanintOfLife()
        {
            return 42;
        }
    }

}
