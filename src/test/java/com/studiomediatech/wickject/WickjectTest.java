package com.studiomediatech.wickject;

import javax.inject.Inject;

import com.studiomediatech.wickject.Wickject.Wickjection;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class WickjectTest {

  private WicketTester tester;

  @Before
  public void setUp() {

    this.tester = new WicketTester();
  }

  @Test
  public void ensureFieldIsNotInjectedWhenNothingProvided() {

    Wickject.addInjector(this.tester);

    FooService injectedService = new Foo().foo;

    Assert.assertNull("Was injected", injectedService);
  }

  @Test
  public void ensureFieldIsInjectedWhenProvidesObjectForType() {

    Wickjection injector = Wickject.addInjector(this.tester);

    FooService mockedService = Mockito.mock(FooService.class);
    injector.provides(mockedService, FooService.class);

    FooService injectedService = new Foo().foo;

    Assert.assertNotNull("Not injected", injectedService);
    Assert.assertEquals("Not injected with same mock", mockedService, injectedService);
  }

  @Test
  public void ensureFieldIsNotInjectedWhenProvidesObjectForOtherTypeOnly() throws Exception {

    Wickjection injector = Wickject.addInjector(this.tester);

    BarService mockedService = Mockito.mock(BarService.class);
    injector.provides(mockedService, BarService.class);

    FooService injectedService = new Foo().foo;

    Assert.assertNull("Was injected", injectedService);
  }

  @Test
  public void ensureBothFieldsInjectedWhenProvidesBothTypes() throws Exception {

    Wickjection injector = Wickject.addInjector(this.tester);

    FooService fooMock = Mockito.mock(FooService.class);
    BarService barMock = Mockito.mock(BarService.class);
    injector.provides(fooMock, FooService.class);
    injector.provides(barMock, BarService.class);

    Foobar foobar = new Foobar();

    Assert.assertNotNull("Not injected", foobar.foo);
    Assert.assertEquals("Not injected with same mock", fooMock, foobar.foo);

    Assert.assertNotNull("Not injected", foobar.bar);
    Assert.assertEquals("Not injected with same mock", barMock, foobar.bar);

  }

  private interface FooService {
    void foo();
  }

  private interface BarService {
    void bar();
  }

  private class Foo {
    @Inject
    private FooService foo;

    public Foo() {
      Injector.get().inject(this);
    }
  }

  private class Foobar {
    @Inject
    private FooService foo;

    @Inject
    private BarService bar;

    public Foobar() {
      Injector.get().inject(this);
    }
  }
}
