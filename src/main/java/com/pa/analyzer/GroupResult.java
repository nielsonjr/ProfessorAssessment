package com.pa.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pa.entity.Publication;
import com.pa.util.EnumQualisClassification;

public class GroupResult {
	private Map<EnumQualisClassification, List<Publication>> conferenceByQualis;
	private Map<EnumQualisClassification, List<Publication>> periodicsByQualis;
	private int concludedOrientations;
	private int onGoingOrientations;
	
	public GroupResult() {
		periodicsByQualis = new HashMap<EnumQualisClassification, List<Publication>>();
		conferenceByQualis = new HashMap<EnumQualisClassification, List<Publication>>();
		concludedOrientations = 0;
		onGoingOrientations = 0;
	}

	public Map<EnumQualisClassification, List<Publication>> getPeriodicsByQualis() {
		return periodicsByQualis;
	}

	public void setPeriodicsByQualis(
			Map<EnumQualisClassification, List<Publication>> periodicsByQualis) {
		this.periodicsByQualis = periodicsByQualis;
	}

	public int getConcludedOrientations() {
		return concludedOrientations;
	}

	public void setConcludedOrientations(int concludedOrientations) {
		this.concludedOrientations = concludedOrientations;
	}

	public int getOnGoingOrientations() {
		return onGoingOrientations;
	}

	public void setOnGoingOrientations(int onGoingOrientations) {
		this.onGoingOrientations = onGoingOrientations;
	}

	public Map<EnumQualisClassification, List<Publication>> getConferencesByQualis() {
		return conferenceByQualis;
	}

	public void setConferencesByQualis(Map<EnumQualisClassification, List<Publication>> conferencesByQualis) {
		this.conferenceByQualis = conferencesByQualis;
	}
	
}
