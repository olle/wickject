Wickject
========

Because, starting that application context just takes forever.
--------------------------------------------------------------

This small project is a very real itch that I just can't wait to scratch!

When working with Wicket and web projects that uses Spring dependency injection, unit testing can easily become a waiting game. Caused by large application contexts that needs to be fully started, or even create database schemas sometimes, the time for tests can quickly become a painfully slow.

In an effort to reduce startup time I want to throw together this little helper, that can be used as a simple setup for unit-testing and mocking, in conjuction with the Wicket tester.



