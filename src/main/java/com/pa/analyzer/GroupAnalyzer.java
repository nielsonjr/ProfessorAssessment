package com.pa.analyzer;

import java.util.Map;

import com.pa.comparator.SetCurriculoMetrics;
import com.pa.comparator.SetCurriculoResult;
import com.pa.entity.Group;
import com.pa.entity.QualisData;
import com.pa.util.EnumPublicationLocalType;

public class GroupAnalyzer {
	private static GroupAnalyzer instance = null;

	private GroupAnalyzer() {}

	public static GroupAnalyzer getInstance() {
		if(instance == null) {
			instance = new GroupAnalyzer();
		}
		return instance;
	}
	
	public SetCurriculoResult analyzerGroup(Group group, Map<EnumPublicationLocalType, QualisData> qualisDataMap) {
		SetCurriculoResult gR = null;
		
		if(group != null){
			gR = new SetCurriculoResult();

//			for (Curriculo curriculo : group.getCurriculos()) {
//				CurriculoResult cR = CurriculoAnalyzer.getInstance().analyzerCurriculo(curriculo, qualisDataMap);
//				
//				gR.getConferencesByQualis().putAll(cR.getConferencesByQualis());
//				gR.getPeriodicsByQualis().putAll(cR.getPeriodicsByQualis());
//				
//				int currentConcludedOrientations = gR.getConcludedOrientations();
//				int currentOnGoingOrientations = gR.getOnGoingOrientations();
//				
//				gR.setConcludedOrientations(currentConcludedOrientations + cR.getConcludedOrientations());
//				gR.setOnGoingOrientations(currentOnGoingOrientations + cR.getOnGoingOrientations());
//			}
			
			gR = SetCurriculoMetrics.getInstance().calculateMetrics(group, qualisDataMap);
		}
		
		return gR;
	}
	
}
