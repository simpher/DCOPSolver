package com.cqu.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cqu.adopt.AdoptAgent;
import com.cqu.bfsdpop.BFSDPOPAgent;
import com.cqu.bnbadopt.BnBAdoptAgent;
import com.cqu.bnbmergeadopt.AgentModel;
import com.cqu.dpop.DPOPAgent;
import com.cqu.util.CollectionUtil;
import com.cqu.util.FileUtil;

public class AgentManager {
	
	public static final String[] AGENT_TYPES=new String[]{"ADOPT", "BNBADOPT", "BNBMergeADOPT" , "DPOP", "BFSDPOP"};
	
	private Map<Integer, Agent> agents;
	
	public AgentManager(Problem problem, AgentConstructor agentConstructor) {
		// TODO Auto-generated constructor stub
		
		agents=new HashMap<Integer, Agent>();
		for(Integer agentId : problem.agentNames.keySet())
		{
			Agent agent=agentConstructor.constructAgent(agentId, problem.agentNames.get(agentId), problem.agentLevels.get(agentId), problem.domains.get(problem.agentDomains.get(agentId)));
			Map<Integer, int[]> neighbourDomains=new HashMap<Integer, int[]>();
			Map<Integer, int[][]> constraintCosts=new HashMap<Integer, int[][]>();
			int[] neighbourAgentIds=problem.neighbourAgents.get(agentId);
			Map<Integer, Integer> neighbourLevels=new HashMap<Integer, Integer>();
			for(int i=0;i<neighbourAgentIds.length;i++)
			{
				neighbourDomains.put(neighbourAgentIds[i], problem.domains.get(problem.agentDomains.get(neighbourAgentIds[i])));
				neighbourLevels.put(neighbourAgentIds[i], problem.agentLevels.get(neighbourAgentIds[i]));
			}
			String[] neighbourAgentCostNames=problem.agentConstraintCosts.get(agentId);
			for(int i=0;i<neighbourAgentCostNames.length;i++)
			{
				constraintCosts.put(neighbourAgentIds[i], 
						CollectionUtil.toTwoDimension(problem.costs.get(neighbourAgentCostNames[i]), 
								problem.domains.get(problem.agentDomains.get(agentId)).length, 
								problem.domains.get(problem.agentDomains.get(neighbourAgentIds[i])).length));
			}
			
			agent.setNeibours(problem.neighbourAgents.get(agentId), problem.parentAgents.get(agentId), 
					problem.childAgents.get(agentId), problem.allParentAgents.get(agentId), 
					problem.allChildrenAgents.get(agentId), neighbourDomains, constraintCosts, neighbourLevels);
			
			agents.put(agent.getId(), agent);
			
			{
				String str="-----------"+agent.name+"-----------\n";
				str+="Parent: "+agent.parent+"\n";
				str+="Children: "+CollectionUtil.arrayToString(agent.children)+"\n";
				str+="AllParents: "+CollectionUtil.arrayToString(agent.allParents)+"\n";
				str+="AllChildren: "+CollectionUtil.arrayToString(agent.allChildren)+"\n";
				FileUtil.writeStringAppend(str, "dfsTree.txt");
			}
		}
	}
	
	public Agent getAgent(int agentId)
	{
		if(agents.containsKey(agentId))
		{
			return agents.get(agentId);
		}else
		{
			return null;
		}
	}
	
	public int getAgentCount()
	{
		return this.agents.size();
	}
	
	public void startAgents(MessageMailer msgMailer)
	{
		for(Agent agent : agents.values())
		{
			agent.setMessageMailer(msgMailer);
			agent.start();
		}
	}
	
	public void printResults(List<Map<String, Object>> results)
	{
		if(results.size()>0)
		{
			Agent agent=null;
			for(Integer agentId : this.agents.keySet())
			{
				agent=this.agents.get(agentId);
				break;
			}
			agent.printResults(results);
		}
	}
	
	public String easyMessageContent(Message msg)
	{
		Agent senderAgent=this.getAgent(msg.getIdSender());
		Agent receiverAgent=this.getAgent(msg.getIdReceiver());
		return senderAgent.easyMessageContent(msg, senderAgent, receiverAgent);
	}
	
	public static AgentConstructor getAgentConstructor(String agentType)
	{
		if(agentType.equals("DPOP"))
		{
            return new AgentConstructor() {
				
				@Override
				public Agent constructAgent(int id, String name, int level, int[] domain) {
					// TODO Auto-generated method stub
					return new DPOPAgent(id, name, level, domain);
				}
			};
		}else if(agentType.equals("BNBADOPT"))
		{
            return new AgentConstructor() {
				
				@Override
				public Agent constructAgent(int id, String name, int level, int[] domain) {
					// TODO Auto-generated method stub
					return new BnBAdoptAgent(id, name, level, domain);
				}
			};
		}else if(agentType.equals("BNBMergeADOPT"))
		{
            return new AgentConstructor() {
				
				@Override
				public Agent constructAgent(int id, String name, int level, int[] domain) {
					// TODO Auto-generated method stub
					return new AgentModel(id, name, level, domain);
				}
			};
		}else if(agentType.equals("BFSDPOP"))
		{
            return new AgentConstructor() {
				
				@Override
				public Agent constructAgent(int id, String name, int level, int[] domain) {
					// TODO Auto-generated method stub
					return new BFSDPOPAgent(id, name, level, domain);
				}
			};
		}else
		{
            return new AgentConstructor() {
				
				@Override
				public Agent constructAgent(int id, String name, int level, int[] domain) {
					// TODO Auto-generated method stub
					return new AdoptAgent(id, name, level, domain);
				}
			};
		}
	}
}
