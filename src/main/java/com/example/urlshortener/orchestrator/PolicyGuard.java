package com.example.urlshortener.orchestrator;
import org.springframework.stereotype.Component;
@Component public class PolicyGuard { public void validate(WorkflowTask t){String n=t.name().toLowerCase(); if((n.contains("implementation")||n.contains("release"))&&!t.requiresApproval()) throw new SecurityException("High-impact task must require human approval: "+t.id()); } }