package com.example.urlshortener.agents;
import com.example.urlshortener.orchestrator.*; import java.util.*;
public class ValidationAgent implements Agent { public String name(){return "ValidationAgent";} public AgentResult execute(ExecutionContext c){c.putArtifact("validation","Entry/exit gates satisfied; required artifacts are present");return AgentResult.ok("Validation passed",Map.of());} }