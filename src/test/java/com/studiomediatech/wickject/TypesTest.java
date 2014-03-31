package com.studiomediatech.wickject;

import javax.inject.Inject;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TypesTest {

  private WicketTester tester;

  @Before
  public void setUp() {
    this.tester = new WicketTester();
  }

  @Test
  public void shouldInjectObjectForInterfaceType() {
    Wickject.addInjectorTo(this.tester).provides(new ImConcrete(), ImAnInterface.class);
    Foo foo = new Foo();
    Assert.assertNotNull("Was not injected.", foo.wiredInterfaceType);
  }

  @Test
  public void shouldInjectObjectForAbstractType() {
    Wickject.addInjectorTo(this.tester).provides(new ImConcrete(), ImAnAbstractClass.class);
    Bar bar = new Bar();
    Assert.assertNotNull("Was not injected.", bar.wiredAbstractType);
  }

  @Test
  public void shouldInjectMockOfSuperTypeForAbstractType() {
    Wickject.addInjectorTo(this.tester).provides(new ImConcrete(), ImConcrete.class);
    Baz baz = new Baz();
    Assert.assertNotNull("Was not injected.", baz.concreteType);
  }

  interface ImAnInterface {
    // OK
  }

  abstract class ImAnAbstractClass
      implements ImAnInterface {
    // OK
  }

  class ImConcrete
      extends ImAnAbstractClass {
    // OK
  }

  class Foo {
    @Inject
    ImAnInterface wiredInterfaceType;

    public Foo() {
      Injector.get().inject(this);
    }
  }

  class Bar {
    @Inject
    ImAnAbstractClass wiredAbstractType;

    public Bar() {
      Injector.get().inject(this);
    }
  }

  class Baz {
    @Inject
    ImConcrete concreteType;

    public Baz() {
      Injector.get().inject(this);
    }
  }

}
