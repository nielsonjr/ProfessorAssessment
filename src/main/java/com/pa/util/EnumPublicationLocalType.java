package com.pa.util;

public enum EnumPublicationLocalType {
	PERIODIC("Peri�dico"),
	CONFERENCE("Confer�ncia");
	
	private String name;
	
	private EnumPublicationLocalType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}