package com.pa.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.pa.database.impl.DatabaseFacade;
import com.pa.entity.Curriculo;
import com.pa.entity.Group;
import com.pa.entity.Qualis;

@ManagedBean(name="indexBean")
@ViewScoped
public class IndexBean {
	private Integer groupsSize;
	private Integer curriculosSize;
	private Integer qualisSize;

	@PostConstruct
	public void init() {

		List<Group> groups = DatabaseFacade.getInstance().listAllGroups();
		List<Curriculo> curriculum = DatabaseFacade.getInstance().listAllCurriculos();
		List<Qualis> qualis = DatabaseFacade.getInstance().listAllQualis();

		groupsSize = groups.size();
		curriculosSize = curriculum.size();
		qualisSize = qualis.size();

	}

	public Integer getGroupsSize() {
		return groupsSize;
	}

	public void setGroupsSize(Integer groupsSize) {
		this.groupsSize = groupsSize;
	}

	public Integer getCurriculosSize() {
		return curriculosSize;
	}

	public void setCurriculosSize(Integer curriculosSize) {
		this.curriculosSize = curriculosSize;
	}

	public Integer getQualisSize() {
		return qualisSize;
	}

	public void setQualisSize(Integer qualisSize) {
		this.qualisSize = qualisSize;
	}
	
	
}
