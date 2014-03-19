package com.studiomediatech.wickject;

import javax.inject.Inject;

import com.studiomediatech.wickject.Wickject.Wickjector;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
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

  @Test(expected = IllegalArgumentException.class)
  public void ensureWickjectAddInjectorThrowsOnNullParameter() throws Exception {
    Wickject.addInjectorTo(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void ensureWickjectorThrowsWhenPassedNullType() throws Exception {
    Wickject.addInjectorTo(this.tester).provides(this, null);
  }

  @Test
  public void ensureFieldIsNotInjectedWhenNothingProvided() {

    Wickject.addInjectorTo(this.tester);

    FooService injectedService = new Foo().foo;

    Assert.assertNull("Was injected", injectedService);
  }

  @Test
  public void ensureFieldIsInjectedWhenProvidesObjectForType() {

    Wickjector injector = Wickject.addInjectorTo(this.tester);

    FooService mockedService = Mockito.mock(FooService.class);
    injector.provides(mockedService, FooService.class);

    FooService injectedService = new Foo().foo;

    Assert.assertNotNull("Not injected", injectedService);
    Assert.assertEquals("Not injected with same mock", mockedService, injectedService);
  }

  @Test
  public void ensureFieldIsNotInjectedWhenProvidesObjectForOtherTypeOnly() throws Exception {

    Wickjector injector = Wickject.addInjectorTo(this.tester);

    BarService mockedService = Mockito.mock(BarService.class);
    injector.provides(mockedService, BarService.class);

    FooService injectedService = new Foo().foo;

    Assert.assertNull("Was injected", injectedService);
  }

  @Test
  public void ensureBothFieldsInjectedWhenProvidesBothTypes() throws Exception {

    Wickjector injector = Wickject.addInjectorTo(this.tester);

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

  @Test
  public void ensureInjectsWicketComponent() {

    MyFoo myFoo = new MyFoo();
    Wickject.addInjectorTo(this.tester).provides(myFoo, FooService.class);

    FooComponent foo = new FooComponent("foo");

    Assert.assertNotNull("Was not injected", foo.fooService);
    Assert.assertEquals("Not injected with same object", myFoo, foo.fooService);
  }

  @Test
  public void ensureInjectsWicketBehavior() {
    MyFoo myFoo = new MyFoo();
    MyBar myBar = new MyBar();
    Wickject.addInjectorTo(this.tester).provides(myFoo, FooService.class).provides(myBar, BarService.class);

    FooComponent fooComponent = new FooComponent("foo");
    BarBehavior bar = new BarBehavior();
    fooComponent.add(bar);

    Assert.assertNotNull("Was not injected", bar.barService);
    Assert.assertEquals("Not injected with same object", myBar, bar.barService);
  }

  private interface FooService {
    void foo();
  }

  private interface BarService {
    void bar();
  }

  private class MyFoo
      implements FooService {
    @Override
    public void foo() {
      // ok
    }
  }

  private class MyBar
      implements BarService {
    @Override
    public void bar() {
      // Ok
    }
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

  private class FooComponent
      extends WebMarkupContainer {

    private static final long serialVersionUID = 1L;

    @Inject
    private FooService        fooService;

    public FooComponent(String id) {
      super(id);
    }
  }

  private class BarBehavior
      extends Behavior {

    @Inject
    private BarService        barService;

    private static final long serialVersionUID = 1L;

    public BarBehavior() {
      // Ok
    }
  }
}
