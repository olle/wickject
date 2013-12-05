package com.studiomediatech.wickject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.IBehaviorInstantiationListener;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;

/**
 * TODO:
 * 
 * @author Olle Törnström olle@studiomediatech.com
 */
public class Wickject {

  /**
   * TODO:
   * 
   * @param tester to add the Wickject injector to
   */
  public static final Wickjection addInjector(WicketTester tester) {
    WebApplication app = tester.getApplication();
    Private injector = new Private(app);
    app.getComponentInstantiationListeners().add(injector);
    return new Wickjection(injector);
  }

  public final static class Wickjection {

    private final Private injector;

    private Wickjection(Private injector) {
      this.injector = injector;
    }

    public Wickjection provides(Object object, Class<?> forType) {
      this.injector.context.put(forType, object);
      return this;
    }
  }

  private final static class Private extends Injector implements IComponentInstantiationListener, IBehaviorInstantiationListener, IFieldValueFactory {

    private final Map<Class<?>, Object> context = new HashMap<>();

    public <T> Private(WebApplication app) {
      bind(app);
    }

    @Override
    public void onInstantiation(Component component) {
      // TODO Auto-generated method stub
      System.out.println("WAH!");
    }

    @Override
    public void onInstantiation(Behavior behavior) {
      // TODO Auto-generated method stub
      System.out.println("OUCH!");
    }

    @Override
    public void inject(Object object) {
      inject(object, this);
    }

    @Override
    public Object getFieldValue(final Field field, Object fieldOwner) {
      return this.context.get(field.getType());
    }

    @Override
    public boolean supportsField(Field field) {
      return field.isAnnotationPresent(Inject.class);
    }

  }
}
