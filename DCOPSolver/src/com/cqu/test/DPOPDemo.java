package com.cqu.test;

import com.cqu.core.Agent;
import com.cqu.core.AgentConstructor;
import com.cqu.core.AgentManager;
import com.cqu.core.DFSTree;
import com.cqu.core.MessageMailer;
import com.cqu.core.Problem;
import com.cqu.core.TreeGenerator;
import com.cqu.dpop.DPOPAgent;
import com.cqu.parser.ProblemParser;
import com.cqu.visualtree.TreeFrame;

public class DPOPDemo {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String instance="problems/RandomDCOP_7_3_1.xml";
		//parse problem xml
		ProblemParser parser=new ProblemParser(instance);
		Problem problem=parser.parse(TreeGenerator.TREE_GENERATOR_TYPE_DFS);
		if(problem==null)
		{
			return;
		}
		
		//display DFS tree，back edges not included
		TreeFrame treeFrame=new TreeFrame(DFSTree.toTreeString(problem.agentNames, problem.parentAgents, problem.childAgents));
		treeFrame.showTreeFrame();
		
		//set whether to print running data records
		Debugger.init(problem.agentNames);
		Debugger.debugOn=false;
		
		//construct agents
		AgentManager agentManager=new AgentManager(problem, new AgentConstructor() {
			
			@Override
			public Agent constructAgent(int id, String name, int level, int[] domain) {
				// TODO Auto-generated method stub
				return new DPOPAgent(id, name, level, domain);
			}
		});
		
		//start agents and MessageMailer
		MessageMailer msgMailer=new MessageMailer(agentManager);
		agentManager.startAgents(msgMailer);
		msgMailer.start();
	}
}
