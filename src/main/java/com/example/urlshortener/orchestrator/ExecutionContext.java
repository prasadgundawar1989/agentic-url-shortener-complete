package com.example.urlshortener.orchestrator;
import java.time.Instant; import java.util.*; import java.util.concurrent.ConcurrentHashMap;
public class ExecutionContext {
 private final String workflowId; private volatile String requirement; private final Instant startedAt=Instant.now();
 private final Map<String,Object> artifacts=new ConcurrentHashMap<>(); private final Map<String,String> decisions=new ConcurrentHashMap<>();
 public ExecutionContext(String workflowId,String requirement){this.workflowId=workflowId;this.requirement=requirement;}
 public String workflowId(){return workflowId;} public String requirement(){return requirement;} public void requirement(String r){requirement=r;} public Instant startedAt(){return startedAt;}
 public Map<String,Object> artifacts(){return Map.copyOf(artifacts);} public Map<String,String> decisions(){return Map.copyOf(decisions);}
 public void putArtifact(String k,Object v){artifacts.put(k,v);} public Object artifact(String k){return artifacts.get(k);} public void decision(String k,String v){decisions.put(k,v);}
}