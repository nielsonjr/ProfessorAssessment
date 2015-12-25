package com.pa.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;

import com.pa.database.impl.DatabaseFacade;
import com.pa.entity.Group;
import com.pa.exception.InvalidPatternFileException;
import com.pa.extractor.MultipleXMLExtractor;
 
@ManagedBean(name="createGroupBean")
@ViewScoped
public class CreateGroupBean {
     
	private boolean existingGroup;
	private String groupName;
	private List<String> fileNames = new ArrayList<String>();
	private List<InputStream> inputFiles = new ArrayList<InputStream>();

	private DualListModel<String> groups;
	     
    @PostConstruct
    public void init() {
        List<String> groupsSource = new ArrayList<String>();
        List<String> groupsTarget = new ArrayList<String>();
         
        List<Group> groupsDatabase = DatabaseFacade.getInstance().listAllGroups();
        for (Group group : groupsDatabase) {
        	groupsSource.add(group.getName());
		}
         
        groups = new DualListModel<String>(groupsSource, groupsTarget);
    }
    
    public void handleFileUpload(FileUploadEvent event) throws IOException {
    	InputStream newInput = event.getFile().getInputstream();
    	
    	if (!inputFiles.contains(newInput)) {
    		inputFiles.add(newInput);
    		fileNames.add(FilenameUtils.getName(event.getFile().getFileName()));
		}
    }
    
    public void create() {
    	MultipleXMLExtractor extractor = new MultipleXMLExtractor();
    	
    	try {
			Group group = extractor.lattesExtractor(groupName, inputFiles);
			if (extractor.checkCurriculoExistence(group.getCurriculos())) {
				// Show message to user
			}
			else {
				this.save(group, false);
			}
		} catch (InvalidPatternFileException e) {
			e.printStackTrace();
		}
	}
    
    public void save(Group group, boolean overwrite) {
    	MultipleXMLExtractor extractor = new MultipleXMLExtractor();
    	extractor.saveGroup(group, overwrite);
    }
	
	public boolean isExistingGroup() {
		return existingGroup;
	}

	public void setExistingGroup(boolean isExistingGroup) {
		this.existingGroup = isExistingGroup;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public DualListModel<String> getGroups() {
		return groups;
	}

	public void setGroups(DualListModel<String> groupsDualList) {
		this.groups = groupsDualList;
	}     

	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileName) {
		this.fileNames = fileName;
	}

	public List<InputStream> getInputFiles() {
		return inputFiles;
	}

	public void setInputFiles(List<InputStream> inputFiles) {
		this.inputFiles = inputFiles;
	}
}