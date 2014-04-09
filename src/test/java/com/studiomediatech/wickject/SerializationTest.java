package com.studiomediatech.wickject;

import javax.inject.Inject;

import com.studiomediatech.wickject.Wickject.Wickjector;
import com.studiomediatech.wickject.serializationteststuff.ITestservice;
import com.studiomediatech.wickject.serializationteststuff.PageA;
import com.studiomediatech.wickject.serializationteststuff.PageB;
import com.studiomediatech.wickject.serializationteststuff.Testservice;

import org.apache.wicket.core.util.io.SerializableChecker;
import org.apache.wicket.core.util.objects.checker.IObjectChecker.Result;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SerializationTest {

  private WicketTester tester;

  @Before
  public void setUp() {
    this.tester = new WicketTester();
  }

  @Test
  public void shouldPassSerializationCheck() {

    Wickject.addInjectorTo(this.tester).provides(new Testservice(), ITestservice.class);

    String id = "foobar";
    this.tester.startComponentInPage(new AComponent(id));
    AComponent c = (AComponent) this.tester.getComponentFromLastRenderedPage(id);

    SerializableChecker.ObjectSerializationChecker checker = new SerializableChecker.ObjectSerializationChecker();
    Result check = checker.check(c.service);

    Assert.assertTrue("Did not pass serializable check!", check.equals(Result.SUCCESS));
  }

  @Test
  public void shouldRenderPageAThenPageBWithoutSerializationError() {

    Wickjector injector = Wickject.addInjectorTo(this.tester);
    injector.provides(Mockito.mock(ITestservice.class), ITestservice.class);

    try {
      this.tester.startPage(PageA.class);
      this.tester.assertRenderedPage(PageA.class);

      Assert.assertNotNull("Not injected", ((PageA) this.tester.getLastRenderedPage()).testservice);

      this.tester.clickLink("link");
      this.tester.assertRenderedPage(PageB.class);

      this.tester.assertLabel("label", "PageB");

      this.tester.clickLink("link");
      this.tester.assertRenderedPage(PageA.class);

    } catch (Throwable e) {
      Assert.fail(e.getMessage());
    }
  }

  private static final class AComponent
      extends WebMarkupContainer {

    private static final long serialVersionUID = 1L;

    @Inject
    ITestservice service;

    public AComponent(String id) {
      super(id);
    }
  }

}
