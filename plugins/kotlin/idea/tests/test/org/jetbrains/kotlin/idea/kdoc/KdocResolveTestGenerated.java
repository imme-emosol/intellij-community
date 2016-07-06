/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.kdoc;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.idea.resolve.AbstractReferenceResolveTest;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/testData/kdoc/resolve")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class KdocResolveTestGenerated extends AbstractReferenceResolveTest {
    public void testAllFilesPresentInResolve() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/kdoc/resolve"), Pattern.compile("^(.+)\\.kt$"), true);
    }

    @TestMetadata("AmbiguousReference.kt")
    public void testAmbiguousReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/AmbiguousReference.kt");
        doTest(fileName);
    }

    @TestMetadata("AmbiguousReferenceTypeParameter.kt")
    public void testAmbiguousReferenceTypeParameter() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/AmbiguousReferenceTypeParameter.kt");
        doTest(fileName);
    }

    @TestMetadata("ClassSelfReference.kt")
    public void testClassSelfReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/ClassSelfReference.kt");
        doTest(fileName);
    }

    @TestMetadata("CodeReference.kt")
    public void testCodeReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/CodeReference.kt");
        doTest(fileName);
    }

    @TestMetadata("CompanionObjectMember.kt")
    public void testCompanionObjectMember() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/CompanionObjectMember.kt");
        doTest(fileName);
    }

    @TestMetadata("ConstructorParamReference.kt")
    public void testConstructorParamReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/ConstructorParamReference.kt");
        doTest(fileName);
    }

    @TestMetadata("ImportedClassReference.kt")
    public void testImportedClassReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/ImportedClassReference.kt");
        doTest(fileName);
    }

    @TestMetadata("Overloads.kt")
    public void testOverloads() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/Overloads.kt");
        doTest(fileName);
    }

    @TestMetadata("ParamReference.kt")
    public void testParamReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/ParamReference.kt");
        doTest(fileName);
    }

    @TestMetadata("PropertyTypeParamReference.kt")
    public void testPropertyTypeParamReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/PropertyTypeParamReference.kt");
        doTest(fileName);
    }

    @TestMetadata("QualifiedCodeReference.kt")
    public void testQualifiedCodeReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/QualifiedCodeReference.kt");
        doTest(fileName);
    }

    @TestMetadata("QualifiedNameFunctionReference.kt")
    public void testQualifiedNameFunctionReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/QualifiedNameFunctionReference.kt");
        doTest(fileName);
    }

    @TestMetadata("QualifiedNameReference.kt")
    public void testQualifiedNameReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/QualifiedNameReference.kt");
        doTest(fileName);
    }

    @TestMetadata("SeeReference.kt")
    public void testSeeReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/SeeReference.kt");
        doTest(fileName);
    }

    @TestMetadata("StaticMember.kt")
    public void testStaticMember() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/StaticMember.kt");
        doTest(fileName);
    }

    @TestMetadata("TypeParamReference.kt")
    public void testTypeParamReference() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("idea/testData/kdoc/resolve/TypeParamReference.kt");
        doTest(fileName);
    }
}
