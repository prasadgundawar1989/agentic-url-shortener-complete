package com.example.urlshortener.orchestrator;
import java.util.*;
public class WorkflowInstance {
 private final String id; private final WorkflowScenario scenario; private final ExecutionContext context; private final Map<String,WorkflowTask> tasks;
 volatile WorkflowStatus status=WorkflowStatus.PENDING; final long createdNanos=System.nanoTime(); boolean metricsStarted;
 public WorkflowInstance(String id,WorkflowScenario scenario,ExecutionContext context,List<WorkflowTask> taskList){this.id=id;this.scenario=scenario;this.context=context;Map<String,WorkflowTask> m=new LinkedHashMap<>();taskList.forEach(t->m.put(t.id(),t));this.tasks=m;}
 public String id(){return id;} public WorkflowScenario scenario(){return scenario;} public ExecutionContext context(){return context;} public Map<String,WorkflowTask> tasks(){return Collections.unmodifiableMap(tasks);} public WorkflowStatus status(){return status;}
}
