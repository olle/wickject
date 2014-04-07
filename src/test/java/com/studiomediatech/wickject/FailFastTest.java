package com.studiomediatech.wickject;

import javax.inject.Inject;

import com.studiomediatech.wickject.Wickject.Wickjector;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Test;

public class FailFastTest {

  @Test
  public void ensureThrowsWithSpecificMessageWhenMissingProvides() {

    WicketTester tester = new WicketTester();

    Wickjector injector = Wickject.addInjectorTo(tester);
    injector.provides(Integer.valueOf(42), Integer.class);

    String actual = "";

    try {
      new Foo();
    } catch (IllegalStateException ex) {
      Assert.assertTrue(ex instanceof IllegalStateException);
      actual = ex.getMessage();
    } finally {
      String expected = "Found a field 'no' in the class com.studiomediatech.wickject.FailFastTest.Foo of type java.lang.String without a provided object.";
      Assert.assertEquals(expected, actual);
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
