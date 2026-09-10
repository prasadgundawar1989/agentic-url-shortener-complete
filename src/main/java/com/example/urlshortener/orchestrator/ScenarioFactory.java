package com.example.urlshortener.orchestrator;
import com.example.urlshortener.agents.*; import org.springframework.stereotype.Component; import java.util.*;
@Component public class ScenarioFactory {
 public String defaultRequirement(WorkflowScenario s){return switch(s){case GREENFIELD->"Build a URL shortener with core APIs, analytics and reliability features.";case BROWNFIELD->"Add analytics and reliability improvements to the existing URL shortener without breaking current APIs.";case AMBIGUOUS->"Make the URL shortener secure and fast.";};}
 public List<WorkflowTask> tasks(WorkflowScenario s){
   List<WorkflowTask> t=new ArrayList<>();
   t.add(task("REQ","Requirement understanding",List.of(),false,new RequirementAgent()));
   if(s==WorkflowScenario.BROWNFIELD)t.add(task("IMPACT","Brownfield impact analysis",List.of("REQ"),false,new BrownfieldAnalysisAgent()));
   String dep=s==WorkflowScenario.BROWNFIELD?"IMPACT":"REQ";
   t.add(task("ARCH","Architecture design",List.of(dep),false,new ArchitectureAgent()));
   t.add(task("SEC","Security and policy analysis",List.of(dep),false,new SecurityAgent()));
   if(s==WorkflowScenario.AMBIGUOUS)t.add(task("CLARIFY","Human clarification checkpoint",List.of("REQ"),true,new Agent(){public String name(){return "ClarificationAgent";}public AgentResult execute(ExecutionContext c){c.decision("ambiguityResolution","Proceed with documented assumptions for demo: p95 < 100ms reads, management APIs protected");return AgentResult.ok("Ambiguity resolved by human-approved assumptions",Map.of());}}));
   List<String> implDeps=new ArrayList<>(List.of("ARCH","SEC")); if(s==WorkflowScenario.AMBIGUOUS)implDeps.add("CLARIFY");
   t.add(new WorkflowTask("IMPL","Implementation",implDeps,true,3,new ImplementationAgent(),()->{}));
   t.add(task("TEST","Testing",List.of("IMPL"),false,new TestAgent()));
   t.add(task("VALIDATE","Validation gate",List.of("TEST"),false,new ValidationAgent()));
   t.add(task("DOCS","Documentation",List.of("VALIDATE"),false,new DocumentationAgent()));
   t.add(new WorkflowTask("RELEASE","Release readiness",List.of("DOCS"),true,1,new ReleaseAgent(),()->{}));
   return t;
 }
 private WorkflowTask task(String id,String name,List<String>d,boolean a,Agent ag){return new WorkflowTask(id,name,d,a,2,ag,()->{});}
}