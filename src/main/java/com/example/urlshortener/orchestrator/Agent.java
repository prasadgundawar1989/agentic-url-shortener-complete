package com.example.urlshortener.orchestrator;
public interface Agent { String name(); AgentResult execute(ExecutionContext context); }