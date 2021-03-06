package com.cqu.mus;

import com.cqu.core.EventListener;
import com.cqu.core.Result;
import com.cqu.core.ResultCycle;
import com.cqu.core.ResultCycleAls;
import com.cqu.core.Solver;
import com.cqu.settings.Settings;

public class TestDga {
    public static void main(String[] args){
        Solver solver = new Solver();
        
        Settings.settings.setCycleCount(500);
        
        Settings.settings.setSelectProbability(0.4);
        Settings.settings.setSelectProbabilityA(0.1);
        Settings.settings.setSelectProbabilityB(0.01);
        Settings.settings.setSelectInterval(5);
        
        Settings.settings.setSelectProbabilityC(0.4);
        Settings.settings.setSelectProbabilityD(0.8);
        Settings.settings.setSelectRound(100);
        
        Settings.settings.setDisplayGraphFrame(false);
        
            solver.solve("problems/RandomDCOP_120_10_10.xml", "ALS_GDBA", false, false, new EventListener() {
//            solver.solve("problems/MS_100_20_20_1.xml", "PDSDSAN", false, false, new EventListener() {
                     @Override
            public void onStarted() {

            }

            @Override
            public void onFinished(Object result) {
            	
            	ResultCycle resultCycle = (ResultCycle) result;
                for (int i = 0; i < resultCycle.totalCostInCycle.length; i++){
                    System.out.println( i + "\t" + resultCycle.totalCostInCycle[i]);
                }
                System.out.println("");
                if(resultCycle.totalCostInCycle.length > 1000)
                	System.out.println( 1000 + "\t" + resultCycle.totalCostInCycle[1000]);
                System.out.println("total costs:" + ((Result)result).totalCost);
            	
//                ResultCycleAls resultCycleAls = (ResultCycleAls) result;
//                for (int i = 0; i < resultCycleAls.bestCostInCycle.length; i++){
//                    System.out.println( i + "\t" + resultCycleAls.bestCostInCycle[i]);
//                }
//                System.out.println("");
//                if(resultCycleAls.bestCostInCycle.length > 1000)
//                	System.out.println( 1000 + "\t" + resultCycleAls.bestCostInCycle[1000]);
//                System.out.println("total costs:" + ((Result)result).totalCost);
            }
        });

    }
}
