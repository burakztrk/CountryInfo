package com.ozturkburak.mapconquer.model.restcountries;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class RegionalBlocsItem{

	@SerializedName("otherNames")
	private List<String> otherNames;

	@SerializedName("acronym")
	private String acronym;

	@SerializedName("name")
	private String name;

	@SerializedName("otherAcronyms")
	private List<Object> otherAcronyms;

	public void setOtherNames(List<String> otherNames){
		this.otherNames = otherNames;
	}

	public List<String> getOtherNames(){
		return otherNames;
	}

	public void setAcronym(String acronym){
		this.acronym = acronym;
	}

	public String getAcronym(){
		return acronym;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setOtherAcronyms(List<Object> otherAcronyms){
		this.otherAcronyms = otherAcronyms;
	}

	public List<Object> getOtherAcronyms(){
		return otherAcronyms;
	}

	@Override
 	public String toString(){
		return 
			"RegionalBlocsItem{" + 
			"otherNames = '" + otherNames + '\'' + 
			",acronym = '" + acronym + '\'' + 
			",name = '" + name + '\'' + 
			",otherAcronyms = '" + otherAcronyms + '\'' + 
			"}";
		}
}