## README

### Running

I've been exporting the traces to LightStep - this is how you can run:

```
OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=https://ingest.lightstep.com:443 OTEL_EXPORTER_OTLP_TRACES_HEADERS=lightstep-access-token=SuURZnL6OSWdlFkAbK0jE9S5EN4ubbfoALj/R8qb1KyewH80qa0+WG3k96kovCnqS5ashDjrDKfZ8Yf2Ft5BHLRxo+7wDAfZ4Yl2DGfd OTEL_SERVICE_NAME=sandbox OTEL_PROPAGATORS=b3multi ./gradlew run
```

I've used a sandbox API credentials up there, feel free to reuse or generate your own.

### What's Going Wrong

* (4) traces from `breakingSpans`
* One with `breakingSpans` -> `blockingWork` (`7846c3a6a96cdad5a3c25e2d6c26b9af`)
* One with `breakingSpans` -> `usingIOPool` (`da56dd65b948f270955506297fb4661e`)
* One with just `fetchUpstreamResource` (`6bce8b301c6777b097e2f561782d63f6`)
* One with `breakingSpans` (`4e999daba7edfda8e13e8f593efaa756`)

The durations reported on LightStep also inaccurate.

Three different _traces_ being emitted from the same run of a function (search for `breakingSpans` for example) in here.

We should see a single Trace ID, and one span from each of the method runs. Here's a [link](https://github.com/open-telemetry/opentelemetry-java-instrumentation/issues/6502) to another failed run.

```
[otel.javaagent 2022-09-07 16:03:28:325 -0400] [main] INFO io.opentelemetry.exporter.logging.LoggingSpanExporter - 'Companion.blockingWork' : 7846c3a6a96cdad5a3c25e2d6c26b9af 0be2e0edd5f7d601 INTERNAL [tracer: io.opentelemetry.opentelemetry-extension-annotations-1.0:1.17.0-alpha] AttributesMap{data={code.namespace=Sandbox$Companion, code.function=blockingWork, thread.id=1, thread.name=main}, capacity=128, totalAddedValues=4}
[otel.javaagent 2022-09-07 16:03:28:325 -0400] [main] INFO io.opentelemetry.exporter.logging.LoggingSpanExporter - 'Companion.breakingSpans' : 7846c3a6a96cdad5a3c25e2d6c26b9af 0bb1b3084d578a17 INTERNAL [tracer: io.opentelemetry.opentelemetry-extension-annotations-1.0:1.17.0-alpha] AttributesMap{data={code.namespace=Sandbox$Companion, code.function=breakingSpans, thread.id=1, thread.name=main}, capacity=128, totalAddedValues=4}

[otel.javaagent 2022-09-07 16:03:30:353 -0400] [main] DEBUG io.opentelemetry.javaagent.tooling.AgentInstaller$TransformLoggingListener - Transformed kotlinx.coroutines.scheduling.TaskImpl -- jdk.internal.loader.ClassLoaders$AppClassLoader@67f89fa3
[otel.javaagent 2022-09-07 16:03:30:362 -0400] [main] INFO io.opentelemetry.exporter.logging.LoggingSpanExporter - 'Companion.usingIOPool' : da56dd65b948f270955506297fb4661e a9d5e48169337190 INTERNAL [tracer: io.opentelemetry.opentelemetry-extension-annotations-1.0:1.17.0-alpha] AttributesMap{data={code.namespace=Sandbox$Companion, code.function=usingIOPool, thread.id=1, thread.name=main}, capacity=128, totalAddedValues=4}
[otel.javaagent 2022-09-07 16:03:30:362 -0400] [main] INFO io.opentelemetry.exporter.logging.LoggingSpanExporter - 'Companion.breakingSpans' : da56dd65b948f270955506297fb4661e 7413531bc34f7e22 INTERNAL [tracer: io.opentelemetry.opentelemetry-extension-annotations-1.0:1.17.0-alpha] AttributesMap{data={code.namespace=Sandbox$Companion, code.function=breakingSpans, thread.id=1, thread.name=main}, capacity=128, totalAddedValues=4}

[otel.javaagent 2022-09-07 16:03:30:363 -0400] [DefaultDispatcher-worker-2] INFO io.opentelemetry.exporter.logging.LoggingSpanExporter - 'Companion.fetchUpstreamResource' : 6bce8b301c6777b097e2f561782d63f6 ddc0308ca9532de8 INTERNAL [tracer: io.opentelemetry.opentelemetry-extension-annotations-1.0:1.17.0-alpha] AttributesMap{data={code.namespace=Sandbox$Companion, code.function=fetchUpstreamResource, thread.id=19, thread.name=DefaultDispatcher-worker-2}, capacity=128, totalAddedValues=4}
Finished getting upstream resource

[otel.javaagent 2022-09-07 16:03:32:376 -0400] [main] INFO io.opentelemetry.exporter.logging.LoggingSpanExporter - 'Companion.breakingSpans' : 4e999daba7edfda8e13e8f593efaa756 4de0924ab305ff58 INTERNAL [tracer: io.opentelemetry.opentelemetry-extension-annotations-1.0:1.17.0-alpha] AttributesMap{data={code.namespace=Sandbox$Companion, code.function=breakingSpans, thread.id=1, thread.name=main}, capacity=128, totalAddedValues=4}
```
