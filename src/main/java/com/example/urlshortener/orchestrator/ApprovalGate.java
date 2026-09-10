package com.example.urlshortener.orchestrator;
import org.springframework.stereotype.Component; import java.util.concurrent.*;
@Component public class ApprovalGate {
 private final ConcurrentMap<String,Boolean> approvals=new ConcurrentHashMap<>(); private String k(String w,String t){return w+":"+t;}
 public void request(String w,String t){approvals.putIfAbsent(k(w,t),false);} public void approve(String w,String t){approvals.put(k(w,t),true);}
 public void revoke(String w,String t){approvals.remove(k(w,t));} public boolean approved(String w,String t){return approvals.getOrDefault(k(w,t),false);}
}
