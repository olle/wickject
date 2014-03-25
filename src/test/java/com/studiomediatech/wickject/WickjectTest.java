package com.studiomediatech.wickject;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

/**
 * Tests for the Wickject static injector utility.
 */
public class WickjectTest {

  @Test(expected = IllegalArgumentException.class)
  public void ensureWickjectAddInjectorThrowsOnNullParameter() {
    Wickject.addInjectorTo(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void ensureWickjectorThrowsWhenPassedNullType() {
    WicketTester tester = new WicketTester();
    Wickject.addInjectorTo(tester).provides(this, null);
  }

}
