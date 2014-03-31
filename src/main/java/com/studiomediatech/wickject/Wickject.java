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
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Now this is the story, all about how, this class got made, and where and why.
 * So I like to take a minute just sit right there, I'll tell you how to get
 * around the this little source code right here.
 * 
 * <p>
 * The main idea is to present a single, and very very sleek, interface to the
 * user. The <strong>{@code addInjectorTo}</strong> method returns its own
 * instance that may be chained, so it's very much a fluid API that should be
 * convenient to the developer. The user probably wants to be able to easily add
 * provision for several mocks or stubs at once.
 * </p>
 * 
 * <p>
 * This is how it can look, when used together with the <i>Mockito</i> mocking
 * library:
 * </p>
 * 
 * <pre>
 * WicketTester tester = new WicketTester();
 * 
 * Wickject.addInjectorTo(tester)
 *   .provides(Mockito.mock(CustomerService.class), CustomerService.class)
 *   .provides(Mockito.mock(OrderService.class), OrderService.class);
 * </pre>
 * 
 * <p>
 * In the example above we provide two mocked services for fields annotated with
 * <strong>{@code @Inject}</strong> that are of the matching service types.
 * </p>
 * 
 * <p>
 * During injection <b>Wickject</b> will throw {@code IllegalStateException} if
 * it encounters any annotated fields that are not provided for. A lenient mode
 * is currently not available.
 * </p>
 * 
 * @author Olle Törnström olle@studiomediatech.com
 */
public final class Wickject {

  /**
   * The default empty constructor is hidden from public use. This is a utility
   * class with a single, simple, static method.
   * 
   * @see #addInjectorTo(WicketTester)
   */
  private Wickject() {
    // Ok
  }

  /**
   * Adds the Wickject injector capabilities to the given {@code WicketTester}
   * instance.
   * 
   * <p>
   * This method ensures that the given tester can inject components and
   * behaviors using Wickject.
   * </p>
   * 
   * @param tester
   *          to add the Wickject injector to, may not be {@code null}
   * 
   * @return a {@code Wickjector} that can be used to setup provisioning, or
   *         bean-wiring
   * 
   * @see Wickjector
   */
  public static final Wickjector addInjectorTo(WicketTester tester) {

    Args.notNull(tester, "tester");

    WebApplication application = tester.getApplication();

    Wickjection injector = new Wickjection(application);

    application.getBehaviorInstantiationListeners().add(injector);
    application.getComponentInstantiationListeners().add(injector);

    return new Wickjector(injector);
  }

  /**
   * Provides the chainable API for our injector, allowing it to add as many
   * {@link #provides(Object, Class)} calls as it wants to the backing injector.
   * 
   * <p>
   * Here is a simple example on how the {@code Wickjector} can be used.
   * </p>
   * 
   * <pre>
   *   ...
   *   Wickjector provider = Wickject.addInjectorTo(tester);
   *   provider.provides(mock, Service.class);
   *   provider.provides(myObject, MyClass.class);
   * </pre>
   * 
   * <p>
   * This shows how easy it is to provide beans for injection. The {@code mock}
   * instance will be provided for all {@code Service} fields. The second
   * provisioning shows how it's possible to provide an actual object for fields
   * too.
   * </p>
   */
  public final static class Wickjector {

    private final Wickjection injection;

    private Wickjector(Wickjection injector) {
      this.injection = injector;
    }

    /**
     * Adds an {@code Object}, of a specified {@code Class} type, to be injected
     * into fields of the given type.
     * 
     * <p>
     * This adds a provisioning to the backing injector, returning it at the
     * end, in order to provide a chaining API.
     * </p>
     * 
     * @param object
     *          provided during injection, like a mock or a stub object, never
     *          {@code null}
     * 
     * @param forType
     *          of the field to inject into, like a class or interface (usually
     *          in the type hierarchy of the object to inject) - never
     *          {@code null}
     * 
     * @param <T>
     *          type of object to provide
     * 
     * @return this instance, for chaining
     */
    public <T> Wickjector provides(T object, Class<T> forType) {

      Args.notNull(object, "object");
      Args.notNull(forType, "forType");

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
  final static class Wickjection
      extends Injector
      implements IComponentInstantiationListener, IBehaviorInstantiationListener, IFieldValueFactory {

    private final Map<Class<?>, Object> context = new HashMap<>();

    public Wickjection(WebApplication application) {
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

      if (!this.context.containsKey(field.getType())) {
        String message = formatErrorMessage(field, fieldOwner);
        throw new IllegalStateException(message);
      }

      return this.context.get(field.getType());
    }

    private String formatErrorMessage(final Field field, Object fieldOwner) {
      String name = field.getName();
      String className = fieldOwner.getClass().getCanonicalName();
      String typeName = field.getType().getCanonicalName();
      String tpl = "Found a field '%s' in the class %s of type %s without a provided object.";
      String message = String.format(tpl, name, className, typeName);
      return message;
    }

    /**
     * <strong>NOTE: Please note that we only support the framework agnostic
     * {@code javax.inject.Inject} annotation!!</strong>
     * 
     * <p>
     * This is so we don't have to have any unnecessary dependencies on special
     * IOC containers or frameworks - you know what I'm talking about. Anyway
     * since it works just as well you should probably use it instead too.
     * </p>
     */
    @Override
    public boolean supportsField(Field field) {
      return field.isAnnotationPresent(Inject.class);
    }
  }

}
