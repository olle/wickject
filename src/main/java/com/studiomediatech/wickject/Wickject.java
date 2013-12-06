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
 * Now this is the story, all about how, this class got made, and where and why.
 * So I like to take a minute just sit right there, I'll tell you how to get
 * around the this little source code right here.
 * 
 * <p>
 * The main idea is to present a single, and very very sleak, interface to the
 * user. The {@link #addInjector(tester)} method returns a chainable instance,
 * so it's very much a fluid micro-API that should be convenient. The user
 * probably wants to be able to add provision of mocks or stubs, and probably
 * not just one.
 * </p>
 * 
 * <p>
 * This is how it can look, when used together with the Mockito library:
 * </p>
 * 
 * <pre>
 * Wickject.addInjector(tester)
 *         .provides(Mockito.mock(CustomerService.class), CustomerService.class)
 *         .provides(Mokcito.mock(OrderServer.class), OrderService.class);
 * </pre>
 * 
 * <p>
 * In the example above we provide two Mockito mocks for any fields annotated
 * with <code>@Inject</code>, matching on the type.
 * </p>
 * 
 * @author Olle Törnström olle@studiomediatech.com
 */
public final class Wickject {

  /**
   * Wires the given tester with our Wickject injector capabilities, returning
   * the chainable provider, where we may add what we wish to provision when
   * injection is required.
   * 
   * <p>
   * NOTE: We have added injection for Wicket behaviors by default too.
   * </p>
   * 
   * @param tester to add the Wickject injector to, never {@code null}
   */
  public static final Wickjector addInjector(WicketTester tester) {
    assertArg("The tester must not be null.", tester);
    WebApplication application = tester.getApplication();
    Wickjection injector = new Wickjection(application);
    application.getBehaviorInstantiationListeners().add(injector);
    application.getComponentInstantiationListeners().add(injector);
    return new Wickjector(injector);
  }

  private static void assertArg(String message, Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Provides the chainable API for our injector, allowing it to add as many
   * {@link #provides(Object, Class)} calls as it wants to the backing injector.
   */
  public final static class Wickjector {

    private final Wickjection injection;

    private Wickjector(Wickjection injector) {
      this.injection = injector;
    }

    /**
     * Adds an object to be injected into fields that are of the specified type.
     * 
     * <p>
     * This adds a provisioning to the backing injector, returning it at the
     * end, in order to provide a chaining API.
     * </p>
     * 
     * @param object provided during injection, like a mock or a stub object,
     *          never {@code null}
     * @param forType of the field to inject into, like a class or interface
     *          (usually in the type hierarchy of the object to inject) - never
     *          {@code null}
     * 
     * @return this instance, for chaining
     */
    public Wickjector provides(Object object, Class<?> forType) {
      assertArg("The object to provide must not be null.", object);
      assertArg("The type to provide for must not be null.", forType);
      this.injection.context.put(forType, object);
      return this;
    }
  }

  /**
   * Our injector implementation, a real do-it-all class, but still very very
   * small thanks to the Wicket IOC library.
   * 
   * <p>
   * We basically only have to provide a small predicate, lookup and
   * object-to-type context (we use a simple map), and the rest is up to the
   * Wicket injector to handle.
   * </p>
   */
  private final static class Wickjection extends Injector implements IComponentInstantiationListener, IBehaviorInstantiationListener, IFieldValueFactory {

    private final Map<Class<?>, Object> context = new HashMap<>();

    public <T> Wickjection(WebApplication application) {
      bind(application);
    }

    @Override
    public void onInstantiation(Component component) {
      inject(component);
    }

    @Override
    public void onInstantiation(Behavior behavior) {
      inject(behavior);
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
