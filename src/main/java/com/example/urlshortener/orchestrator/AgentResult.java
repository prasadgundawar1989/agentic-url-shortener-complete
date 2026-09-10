package com.example.urlshortener.orchestrator;
import java.util.Map;
public record AgentResult(boolean success,String summary,Map<String,Object> artifacts){ public static AgentResult ok(String s,Map<String,Object>a){return new AgentResult(true,s,a);} }