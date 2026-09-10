package com.example.urlshortener.orchestrator;
import java.util.*; import java.util.concurrent.atomic.AtomicInteger;
public class WorkflowTask {
 private final String id,name; private final List<String> dependencies; private final boolean requiresApproval; private final int maxAttempts; private final Agent agent; private final Runnable rollback;
 private volatile TaskStatus status=TaskStatus.PENDING; private final AtomicInteger attempts=new AtomicInteger(); private volatile String lastError;
 public WorkflowTask(String id,String name,List<String> dependencies,boolean requiresApproval,int maxAttempts,Agent agent,Runnable rollback){this.id=id;this.name=name;this.dependencies=List.copyOf(dependencies);this.requiresApproval=requiresApproval;this.maxAttempts=maxAttempts;this.agent=agent;this.rollback=rollback;}
 public String id(){return id;} public String name(){return name;} public List<String> dependencies(){return dependencies;} public boolean requiresApproval(){return requiresApproval;} public int maxAttempts(){return maxAttempts;} public Agent agent(){return agent;} public Runnable rollback(){return rollback;}
 public TaskStatus status(){return status;} public void status(TaskStatus s){status=s;} public int incrementAttempts(){return attempts.incrementAndGet();} public int attempts(){return attempts.get();} public String lastError(){return lastError;} public void lastError(String e){lastError=e;}
 public void reset(){status=TaskStatus.PENDING;lastError=null;attempts.set(0);}
}