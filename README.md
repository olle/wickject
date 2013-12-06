Wickject
========

When starting that application context just takes forever.
----------------------------------------------------------

This small project deals with a very real itch that I just can't wait to scratch!

### The long wait

When working with Wicket and web projects that uses Spring dependency injection, unit testing can easily become a waiting game. Caused by large application contexts that needs to be fully started, or even create database schemas sometimes, the time for tests can quickly become a painfully slow.

One way to do it (doing it wrong) is to write specific test application contexts. Yeah right, like I'm like, doing that for like, a lot of tests.... NOT!

### A quicker draw

In an effort to reduce startup time I want to throw together this little helper, that can be used as a simple setup for unit-testing and mocking in conjunction with the Wicket tester.

Attacking the core issue, __Wickject__ provides a small IOC tool for testing, that makes dependency injection a selective task rather than a boring configuration chore.

### Getting started

__1 Add dependency to your Wicket project__

NOTE: Currently there's no artifact in Maven Central or Sonatype OSS, but I'm working on it. There will be a POM dependency definition here in the near future.

For now you will have to clone this repository, do a `mvn install` and then use the current SNAPSHOT version.

__2 Add an injector to your Wicket Tester__

    WicketTester tester = new WicketTester();
    Wickjector injector = Wickject.addInjector(tester);

    
__3 Add provides for your services and beans__

    injector.provides(Mockito.mock(ServiceA.class), ServiceA.class);
    injector.provides(Mockito.mock(ServiceB.class), ServiceB.class);
    injector.provides(new ServiceCImpl(), ServiceC.class);
    
__4 Done!__

As usual there's no step 4. Just go testing already. Yes! It's that simple!
    
### The fluid interface

You can use a shorter syntax with the chaining API too, which probably better fit the needs when mocking for tests.

    Wickject.addInjector(tester)
        .provides(Mockito.mock(A.class), A.class)
        .provides(Mockito.mock(B.class), B.class)
        .provides(new C(), C.class);
        
 
Happy blazingly fast testing with Wicket!