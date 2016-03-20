package net.bytebuddy.implementation.bind.annotation;

import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class FieldValueBinderTest extends AbstractAnnotationBinderTest<FieldValue> {

    private static final String FOO = "foo", BAR = "bar";

    @Mock
    private FieldDescription.InDefinedShape fieldDescription;

    @Mock
    private TypeDescription.Generic fieldType, targetType;

    @Mock
    private TypeDescription rawFieldType;

    public FieldValueBinderTest() {
        super(FieldValue.class);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(fieldDescription.asDefined()).thenReturn(fieldDescription);
        when(fieldDescription.getType()).thenReturn(fieldType);
        when(target.getType()).thenReturn(targetType);
        when(fieldType.asErasure()).thenReturn(rawFieldType);
    }

    @Override
    protected TargetMethodAnnotationDrivenBinder.ParameterBinder<FieldValue> getSimpleBinder() {
        return FieldValue.Binder.INSTANCE;
    }

    @Test(expected = IllegalStateException.class)
    public void testFieldOfArrayThrowsException() throws Exception {
        doReturn(Object[].class).when(annotation).declaringType();
        FieldValue.Binder.INSTANCE.bind(annotationDescription, source, target, implementationTarget, assigner);
    }

    @Test(expected = IllegalStateException.class)
    public void testFieldOfPrimitiveThrowsException() throws Exception {
        doReturn(int.class).when(annotation).declaringType();
        FieldValue.Binder.INSTANCE.bind(annotationDescription, source, target, implementationTarget, assigner);
    }

    @Test
    public void testLegalAssignment() throws Exception {
        doReturn(void.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(FOO);
        when(instrumentedType.getDeclaredFields()).thenReturn(new FieldList.Explicit<FieldDescription.InDefinedShape>(fieldDescription));
        when(fieldDescription.getSourceCodeName()).thenReturn(FOO);
        when(fieldDescription.isVisibleTo(instrumentedType)).thenReturn(true);
        when(target.getDeclaredAnnotations()).thenReturn(new AnnotationList.Empty());
        when(stackManipulation.isValid()).thenReturn(true);
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(true));
    }

    @Test
    public void testIllegalAssignmentNonAssignable() throws Exception {
        doReturn(void.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(FOO);
        when(instrumentedType.getDeclaredFields()).thenReturn(new FieldList.Explicit<FieldDescription.InDefinedShape>(fieldDescription));
        when(fieldDescription.getSourceCodeName()).thenReturn(FOO);
        when(fieldDescription.isVisibleTo(instrumentedType)).thenReturn(true);
        when(target.getDeclaredAnnotations()).thenReturn(new AnnotationList.Empty());
        when(stackManipulation.isValid()).thenReturn(false);
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(false));
    }

    @Test
    public void testIllegalAssignmentStaticMethod() throws Exception {
        doReturn(void.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(FOO);
        when(instrumentedType.getDeclaredFields()).thenReturn(new FieldList.Explicit<FieldDescription.InDefinedShape>(fieldDescription));
        when(fieldDescription.getSourceCodeName()).thenReturn(FOO);
        when(fieldDescription.isVisibleTo(instrumentedType)).thenReturn(true);
        when(target.getDeclaredAnnotations()).thenReturn(new AnnotationList.Empty());
        when(stackManipulation.isValid()).thenReturn(true);
        when(source.isStatic()).thenReturn(true);
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(false));
    }

    @Test
    public void testLegalAssignmentStaticMethodStaticField() throws Exception {
        doReturn(void.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(FOO);
        when(instrumentedType.getDeclaredFields()).thenReturn(new FieldList.Explicit<FieldDescription.InDefinedShape>(fieldDescription));
        when(fieldDescription.getSourceCodeName()).thenReturn(FOO);
        when(fieldDescription.isVisibleTo(instrumentedType)).thenReturn(true);
        when(target.getDeclaredAnnotations()).thenReturn(new AnnotationList.Empty());
        when(stackManipulation.isValid()).thenReturn(true);
        when(source.isStatic()).thenReturn(true);
        when(fieldDescription.isStatic()).thenReturn(true);
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(true));
    }

    @Test
    public void testIllegalAssignmentNoField() throws Exception {
        doReturn(void.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(FOO);
        when(instrumentedType.getDeclaredFields()).thenReturn(new FieldList.Empty<FieldDescription.InDefinedShape>());
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testIllegalAssignmentNonVisible() throws Exception {
        doReturn(void.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(FOO);
        when(instrumentedType.getDeclaredFields()).thenReturn((FieldList) new FieldList.Explicit<FieldDescription>(fieldDescription));
        when(fieldDescription.getSourceCodeName()).thenReturn(FOO);
        when(fieldDescription.isVisibleTo(instrumentedType)).thenReturn(false);
        when(target.getDeclaredAnnotations()).thenReturn(new AnnotationList.Empty());
        when(stackManipulation.isValid()).thenReturn(true);
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(false));
    }

    @Test
    public void testLegalAssignmentExplicitType() throws Exception {
        doReturn(Foo.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(FOO);
        when(target.getDeclaredAnnotations()).thenReturn(new AnnotationList.Empty());
        when(stackManipulation.isValid()).thenReturn(true);
        when(instrumentedType.isAssignableTo(new TypeDescription.ForLoadedType(Foo.class))).thenReturn(true);
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(true));
    }

    @Test
    public void testIllegalAssignmentExplicitTypeNonAssignable() throws Exception {
        doReturn(Foo.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(FOO);
        when(target.getDeclaredAnnotations()).thenReturn(new AnnotationList.Empty());
        when(stackManipulation.isValid()).thenReturn(true);
        when(instrumentedType.isAssignableTo(new TypeDescription.ForLoadedType(Foo.class))).thenReturn(false);
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(false));
    }

    @Test
    public void testIllegalAssignmentExplicitTypeNonAssignableFieldType() throws Exception {
        doReturn(Foo.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(FOO);
        when(target.getDeclaredAnnotations()).thenReturn(new AnnotationList.Empty());
        when(stackManipulation.isValid()).thenReturn(true);
        when(instrumentedType.isAssignableTo(new TypeDescription.ForLoadedType(Foo.class))).thenReturn(false);
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(false));
    }

    @Test
    public void testIllegalAssignmentExplicitTypeStaticMethod() throws Exception {
        doReturn(Foo.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(FOO);
        when(target.getDeclaredAnnotations()).thenReturn(new AnnotationList.Empty());
        when(source.isStatic()).thenReturn(true);
        when(stackManipulation.isValid()).thenReturn(true);
        when(instrumentedType.isAssignableTo(new TypeDescription.ForLoadedType(Foo.class))).thenReturn(true);
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(false));
    }

    @Test
    public void testLegalAssignmentExplicitTypeStaticMethodStaticField() throws Exception {
        doReturn(FooStatic.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(FOO);
        when(target.getDeclaredAnnotations()).thenReturn(new AnnotationList.Empty());
        when(source.isStatic()).thenReturn(true);
        when(stackManipulation.isValid()).thenReturn(true);
        when(instrumentedType.isAssignableTo(new TypeDescription.ForLoadedType(FooStatic.class))).thenReturn(true);
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(true));
    }

    @Test
    public void testIllegalAssignmentExplicitTypeNoField() throws Exception {
        doReturn(Foo.class).when(annotation).declaringType();
        when(annotation.value()).thenReturn(BAR);
        when(target.getDeclaredAnnotations()).thenReturn(new AnnotationList.Empty());
        when(stackManipulation.isValid()).thenReturn(true);
        when(instrumentedType.isAssignableTo(new TypeDescription.ForLoadedType(Foo.class))).thenReturn(true);
        MethodDelegationBinder.ParameterBinding<?> binding = FieldValue.Binder.INSTANCE.bind(annotationDescription,
                source,
                target,
                implementationTarget,
                assigner);
        assertThat(binding.isValid(), is(false));
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(FieldValue.Binder.class).apply();
        ObjectPropertyAssertion.of(FieldValue.Binder.Delegate.class).applyBasic();
    }

    public static class Foo {

        public String foo;
    }

    public static class FooStatic {

        public static String foo;
    }
}