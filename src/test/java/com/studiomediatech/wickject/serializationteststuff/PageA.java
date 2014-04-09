package com.studiomediatech.wickject.serializationteststuff;

import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

public class PageA
    extends WebPage {

  private static final long serialVersionUID = 1L;

  @Inject
  public ITestservice testservice;

  public PageA() {
    super();
    add(new Link<Void>("link") {

      private static final long serialVersionUID = 1L;

      @Override
      public void onClick() {
        setResponsePage(PageB.class);
      }

    });
  }

}
