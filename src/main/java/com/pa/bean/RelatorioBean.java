package com.pa.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import com.pa.analyzer.GroupAnalyzer;
import com.pa.analyzer.GroupResult;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;

import com.pa.comparator.ComparationVO;
import com.pa.database.impl.DatabaseFacade;
import com.pa.entity.Group;
import com.pa.entity.Publication;
import com.pa.entity.QualisData;
import com.pa.util.EnumPublicationLocalType;
import com.pa.util.EnumQualisClassification;

@ManagedBean(name="relatorioBean")
@ViewScoped
public class RelatorioBean {
	private DualListModel<Group> groups;
	private List<QualisData> qualisDataConference;
	private List<QualisData> qualisDataPeriodic;
	
	private Boolean checkQualisDataConference = true;
	private Boolean checkQualisDataPeriodic;
	private Boolean checkOrientations;
	
	private QualisData selectedQualisDataConference;
	private QualisData selectedQualisDataPeriodic;
	
	private TreeNode root = null;
	     
    @PostConstruct
    public void init() {
        List<Group> groupsTarget = new ArrayList<Group>();
         
        List<Group> groupsFromDatabase = DatabaseFacade.getInstance().listAllGroups();
        
        QualisData example = new QualisData();
        example.setType(EnumPublicationLocalType.CONFERENCE);
        qualisDataConference = DatabaseFacade.getInstance().listAllQualisData(example);
        
        QualisData examplePeriodic = new QualisData();
        examplePeriodic.setType(EnumPublicationLocalType.PERIODIC);
        qualisDataPeriodic = DatabaseFacade.getInstance().listAllQualisData(examplePeriodic);
         
        groups = new DualListModel<Group>(groupsFromDatabase, groupsTarget);
    }

    public void comparar(ActionEvent actionEvent) {
    	List<Group> selectedGroups = groups.getTarget();
    	
    	root = new DefaultTreeNode();
    	Map<String, TreeNode> mapTypeByNode = new HashMap<String, TreeNode>();
    	
    	for (Group group : selectedGroups) {
    		Map<EnumPublicationLocalType, QualisData> qualisDataMap = new HashMap<EnumPublicationLocalType, QualisData>();
    		qualisDataMap.put(EnumPublicationLocalType.PERIODIC, selectedQualisDataPeriodic);
    		qualisDataMap.put(EnumPublicationLocalType.CONFERENCE, selectedQualisDataConference);
    		
			GroupResult gR = GroupAnalyzer.getInstance().analyzerGroup(group, qualisDataMap);
			
    		if(checkQualisDataConference) {
    			TreeNode conferences = null;
    			
    			if(!mapTypeByNode.containsKey("conferences")) {
    				conferences = new DefaultTreeNode("conferences", new ComparationVO("Conferencias", "-"), root);
					mapTypeByNode.put("conferences", conferences);
				}
				else {
					conferences = mapTypeByNode.get("conferences");
					ComparationVO valueObject = (ComparationVO) conferences.getData();
					
					String[] valueConference = valueObject.getValue();
					valueConference[valueConference.length - 1] = "-";
					valueObject.setValue(valueConference);
				}
    			
    			
    			Map<EnumQualisClassification, List<Publication>> conferencesByQualis = gR.getConferencesByQualis();
    			EnumQualisClassification[] qualis = EnumQualisClassification.values();
    			
    			for (EnumQualisClassification enumQualisClassification : qualis) {
					List<Publication> publications = conferencesByQualis.get(enumQualisClassification);
					String value = null;
					
					if(publications == null) {
						value = "0";
					}
					else {
						value = String.valueOf(publications.size());
					}
					
					if(!mapTypeByNode.containsKey("conferences" + enumQualisClassification.toString())) {
						TreeNode conferencesQualis = new DefaultTreeNode("conferences" + enumQualisClassification.toString(), new ComparationVO(enumQualisClassification.toString(), value), conferences);
						mapTypeByNode.put("conferences" + enumQualisClassification.toString(), conferencesQualis);
					}
					else {
						TreeNode conferencesQualis = mapTypeByNode.get("conferences" + enumQualisClassification.toString());
						ComparationVO valueObject = (ComparationVO) conferencesQualis.getData();
						
						String[] valueConference = valueObject.getValue();
						valueConference[valueConference.length - 1] = value;
						valueObject.setValue(valueConference);
					}
				}
//    			ComparationVO cVO = new ComparationVO(name, value);
    		}
			
		}
    }
    
	public DualListModel<Group> getGroups() {
		return groups;
	}

	public void setGroups(DualListModel<Group> groups) {
		this.groups = groups;
	}

	public List<QualisData> getQualisDataConference() {
		return qualisDataConference;
	}

	public void setQualisDataConference(List<QualisData> qualisDataConference) {
		this.qualisDataConference = qualisDataConference;
	}

	public List<QualisData> getQualisDataPeriodic() {
		return qualisDataPeriodic;
	}

	public void setQualisDataPeriodic(List<QualisData> qualisDataPeriodic) {
		this.qualisDataPeriodic = qualisDataPeriodic;
	}

	public QualisData getSelectedQualisDataConference() {
		return selectedQualisDataConference;
	}

	public void setSelectedQualisDataConference(
			QualisData selectedQualisDataConference) {
		this.selectedQualisDataConference = selectedQualisDataConference;
	}

	public QualisData getSelectedQualisDataPeriodic() {
		return selectedQualisDataPeriodic;
	}

	public void setSelectedQualisDataPeriodic(QualisData selectedQualisDataPeriodic) {
		this.selectedQualisDataPeriodic = selectedQualisDataPeriodic;
	}

	public Boolean getCheckQualisDataConference() {
		return checkQualisDataConference;
	}

	public void setCheckQualisDataConference(Boolean checkQualisDataConference) {
		this.checkQualisDataConference = checkQualisDataConference;
	}

	public Boolean getCheckQualisDataPeriodic() {
		return checkQualisDataPeriodic;
	}

	public void setCheckQualisDataPeriodic(Boolean checkQualisDataPeriodic) {
		this.checkQualisDataPeriodic = checkQualisDataPeriodic;
	}

	public Boolean getCheckOrientations() {
		return checkOrientations;
	}

	public void setCheckOrientations(Boolean checkOrientations) {
		this.checkOrientations = checkOrientations;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}
}
