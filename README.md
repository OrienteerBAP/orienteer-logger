# Orienteer Logger

## What is it?

It is small and lightweight package for events logging in orienteer for subsequent analysis

You can use it for:

1. Remote incidents report for your java application
2. Usage events reports
3. Android applications fault reports

Library is extremely small with no extra dependencies and can be used for reporting not only to Orienteer servers, but your own

## How to use it

```java
OLogger.log("My message here");
OLogger.log(unhandledExceptionToBeReported);
```

## How to configure it

```java
OLogger.set(new OLoggerBuilder().create(new MyCustomOLoggerConfiguration()));
OLogger.set(new OLoggerBuilder().setLoggerEventDispatcher(new MyCustomEventsDispatcher()).create());
```

### Default Configuration

Default OLogger configuration supports the following system properties:

1. ologger.application - application name
2. ologger.nodeId - node id
3. ologger.collectorUrl - URL of orienteer installation for collecting of logs