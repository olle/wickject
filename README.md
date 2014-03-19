[![Build Status](https://travis-ci.org/olle/wickject.png)](https://travis-ci.org/olle/wickject)

Wickject
========

### A minimal dependency injector for unit testing [Wicket](http://wicket.apache.org) projects.

## Getting started

__1. Add the `wickject` dependency to your project:__

_NOTE: There's currently no artifact in Maven Central or Sonatype OSS, but I'm working on it. Stay tuned for updates!_

For now, clone this repository, do a `mvn install` and then use the current SNAPSHOT version, adding the following to your project:

    <dependency>
      <groupId>com.studiomediatech.wickject</groupId>
      <artifactId>wickject</artifactId>
      <version>[[PROJECT VERSION]]</version>
    </dependency>

__2. Enhance your WicketTester with the Wickjects injector:__

    WicketTester tester = new WicketTester();
    Wickjector injector = Wickject.addInjectorTo(tester);

    
__3. Add `provides` for your services and beans:__

    injector.provides(Mockito.mock(ServiceA.class), ServiceA.class);
    injector.provides(Mockito.mock(ServiceB.class), ServiceB.class);
    injector.provides(new ServiceCImpl(), ServiceC.class);
    
__4. Done!__

And... there's no step four. Just go testing already. Yes! It's _that_ simple!
    
## Now why would I ever...

When working with Wicket and web projects that uses dependency injection, testing might easily become a waiting game. With large application contexts that needs to be fully started, sometimes even creating database schemas and filling in default data, the time it takes the tests to run may quickly become painfully slow, grinding productivity to a halt.

One way to solve this problem is to write specific test application contexts. Yeah right, like I'm like, doing that for like, a lot of tests.... NOT!

### A better approach

When your tests are suitable for mocking, you should always be able to quickly start your application and selectively mock out the services required for your tests.

Attacking the core issue, __Wickject__ provides a small IOC tool for testing, that makes dependency injection a selective task rather than a boring configuration chore.

### No extra code for tests

Using __Wickject__ you can remove the need for extra code just to make your components and classes testable. No more package protected constructors. This makes sure you're only testing actual production code and nothing else.

### The fluid interface

__Wickject__ has short and fluid syntax for easy programmatic use. The chaining API hits the sweet spot when setting up mocks.

    Wickject.addInjectorTo(tester)
        .provides(Mockito.mock(A.class), A.class)
        .provides(Mockito.mock(B.class), B.class)
        .provides(new C(), C.class);
        
 
Happy blazingly fast testing with Wicket!
