package com.studiomediatech.wickject.typesteststuff;

public abstract class ImAnAbstractClass
    implements ImAnInterface {
  // OK
  public ImAnAbstractClass() {
    super(); // doh!
  }
}
