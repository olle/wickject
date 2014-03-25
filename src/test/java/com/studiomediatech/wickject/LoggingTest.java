package com.studiomediatech.wickject;

import javax.inject.Inject;

import com.studiomediatech.wickject.Wickject.Wickjector;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;

public class LoggingTest {

  @Test
  public void ensureLogsWarningOnFieldFoundWithoutProvides() {

    WicketTester tester = new WicketTester();

    Wickjector injector = Wickject.addInjectorTo(tester);
    injector.provides(Integer.valueOf(42), Integer.class);

    Logger loggerMock = Mockito.mock(org.slf4j.Logger.class);
    Wickjector.LOGGER = loggerMock;

    try {
      new Foo();
    } catch (IllegalStateException ex) {
      Assert.assertTrue(ex instanceof IllegalStateException);
    } finally {
      Mockito
          .verify(loggerMock)
          .error(
              Matchers
                  .eq("Found a field 'no' in the class com.studiomediatech.wickject.LoggingTest.Foo of type java.lang.String without a provided object."));
    }

  }

  @SuppressWarnings("unused")
  private class Foo {

    @Inject
    String no;

    @Inject
    Integer yes;

    public Foo() {
      Injector.get().inject(this);
    }
  }

}
