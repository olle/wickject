package com.studiomediatech.wickject.serializationteststuff;

import org.apache.wicket.markup.html.link.Link;

public class PageB
    extends AbstractBaseWebPage {

  private static final long serialVersionUID = 1L;

  public PageB() {
    super("PageB");
    add(new Link<Void>("link") {

      private static final long serialVersionUID = 1L;

      @Override
      public void onClick() {
        setResponsePage(PageA.class);
      }
    });
  }

}
