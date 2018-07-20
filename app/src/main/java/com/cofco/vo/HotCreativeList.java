package com.cofco.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class HotCreativeList implements Serializable{
	private ArrayList<CreativeItem> problemList;
	private ArrayList<CreativeItem> solutionList ; 
	private ArrayList<CreativeItem> newIdeaList ; 
	public ArrayList<CreativeItem> getProblemList() {
		return problemList;
	}

	public void setProblemList(ArrayList<CreativeItem> problemList) {
		this.problemList = problemList;
	}

	public ArrayList<CreativeItem> getSolutionList() {
		return solutionList;
	}

	public void setSolutionList(ArrayList<CreativeItem> solutionList) {
		this.solutionList = solutionList;
	}

	public ArrayList<CreativeItem> getNewIdeaList() {
		return newIdeaList;
	}

	public void setNewIdeaList(ArrayList<CreativeItem> newIdeaList) {
		this.newIdeaList = newIdeaList;
	}
}
