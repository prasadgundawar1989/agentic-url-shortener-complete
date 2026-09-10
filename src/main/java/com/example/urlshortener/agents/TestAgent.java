package com.example.urlshortener.agents;
import com.example.urlshortener.orchestrator.*; import java.util.*;
public class TestAgent implements Agent { public String name(){return "TestAgent";} public AgentResult execute(ExecutionContext c){
 if(c.requirement().contains("SIMULATE_TEST_FAILURE")) throw new IllegalStateException("Simulated validation failure for retry/rollback demonstration");
 c.putArtifact("testPlan",List.of("URL create/resolve unit tests","Approval-gate workflow test","Retry test","Cycle detection test","HTTP smoke test"));
 return AgentResult.ok("Test plan generated",Map.of()); } }
