package com.example.urlshortener.orchestrator;
import io.micrometer.core.instrument.*; import org.springframework.stereotype.Component; import java.util.concurrent.atomic.AtomicLong;
@Component public class WorkflowMetrics {
 private final Counter completed,failed,retries,rollbacks; private final Timer latency; private final AtomicLong active=new AtomicLong();
 public WorkflowMetrics(MeterRegistry r){completed=r.counter("agentic.workflow.completed");failed=r.counter("agentic.workflow.failed");retries=r.counter("agentic.task.retries");rollbacks=r.counter("agentic.task.rollbacks");latency=r.timer("agentic.workflow.latency");r.gauge("agentic.workflow.active",active);}
 public void started(){active.incrementAndGet();} public void completed(long nanos){active.decrementAndGet();completed.increment();latency.record(nanos,java.util.concurrent.TimeUnit.NANOSECONDS);} public void failed(){active.decrementAndGet();failed.increment();} public void retry(){retries.increment();} public void rollback(){rollbacks.increment();}
}