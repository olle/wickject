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

/**
 * Tests that assert actual injection and bean resolution scenarios.
 */
public class InjectionTest {

  private WicketTester tester;

  @Before
  public void setUp() {

    this.tester = new WicketTester();
  }

  @Test(expected = IllegalStateException.class)
  public void ensureThrowsWhenFieldFoundWhichIsNotProvidedFor() {

    Wickject.addInjectorTo(this.tester);

    new Foo();
  }

  @Test
  public void ensureFieldIsInjectedWhenProvidesObjectForType() {

    Wickjector injector = Wickject.addInjectorTo(this.tester);

    FooService mockedService = Mockito.mock(FooService.class);
    injector.provides(mockedService, FooService.class);

    Mockito.when(mockedService.foo()).thenReturn("mocked-foo");

    FooService injectedService = new Foo().foo;

    Assert.assertNotNull("Not injected", injectedService);
    Assert.assertEquals("Not injected with same mock", mockedService.foo(), injectedService.foo());
  }

  @Test
  public void ensureBothFieldsInjectedWhenProvidesBothTypes() throws Exception {

    Wickjector injector = Wickject.addInjectorTo(this.tester);

    FooService fooMock = Mockito.mock(FooService.class);
    BarService barMock = Mockito.mock(BarService.class);
    injector.provides(fooMock, FooService.class);
    injector.provides(barMock, BarService.class);

    Mockito.when(fooMock.foo()).thenReturn("mocked-foo");
    Mockito.when(barMock.bar()).thenReturn("mocked-bar");

    Foobar foobar = new Foobar();

    Assert.assertNotNull("Not injected", foobar.foo);
    Assert.assertEquals("Not injected with same mock", fooMock.foo(), foobar.foo.foo());

    Assert.assertNotNull("Not injected", foobar.bar);
    Assert.assertEquals("Not injected with same mock", barMock.bar(), foobar.bar.bar());
  }

  @Test
  public void ensureInjectsWicketComponent() {

    MyFoo myFoo = new MyFoo();
    Wickject.addInjectorTo(this.tester).provides(myFoo, FooService.class);

    FooComponent foo = new FooComponent("foo");

    Assert.assertNotNull("Was not injected", foo.fooService);
    Assert.assertEquals("Not injected with same object", myFoo.foo(), foo.fooService.foo());
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
    Assert.assertEquals("Not injected with same object", myBar.bar(), bar.barService.bar());
  }

  private interface FooService {
    String foo();
  }

  private interface BarService {
    String bar();
  }

  private class MyFoo
      implements FooService {
    @Override
    public String foo() {
      return "real-foo";
    }
  }

  private class MyBar
      implements BarService {
    @Override
    public String bar() {
      return "real-bar";
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
    private FooService fooService;

    public FooComponent(String id) {
      super(id);
    }
  }

  private class BarBehavior
      extends Behavior {

    @Inject
    private BarService barService;

    private static final long serialVersionUID = 1L;

    public BarBehavior() {
      // Ok
    }
  }
}
