package com.studiomediatech.wickject.serializationteststuff;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public abstract class AbstractBaseWebPage
    extends WebPage {

  private static final long serialVersionUID = -6270724842476321082L;

  public AbstractBaseWebPage(String label) {
    super();

    add(new Label("label", Model.of(label)));
  }
}
