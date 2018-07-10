package com.ozturkburak.mapconquer.model.restcountries;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Translations{

	@SerializedName("br")
	private String br;

	@SerializedName("de")
	private String de;

	@SerializedName("pt")
	private String pt;

	@SerializedName("ja")
	private String ja;

	@SerializedName("it")
	private String it;

	@SerializedName("fr")
	private String fr;

	@SerializedName("es")
	private String es;

	public void setBr(String br){
		this.br = br;
	}

	public String getBr(){
		return br;
	}

	public void setDe(String de){
		this.de = de;
	}

	public String getDe(){
		return de;
	}

	public void setPt(String pt){
		this.pt = pt;
	}

	public String getPt(){
		return pt;
	}

	public void setJa(String ja){
		this.ja = ja;
	}

	public String getJa(){
		return ja;
	}

	public void setIt(String it){
		this.it = it;
	}

	public String getIt(){
		return it;
	}

	public void setFr(String fr){
		this.fr = fr;
	}

	public String getFr(){
		return fr;
	}

	public void setEs(String es){
		this.es = es;
	}

	public String getEs(){
		return es;
	}

	@Override
 	public String toString(){
		return 
			"Translations{" + 
			"br = '" + br + '\'' + 
			",de = '" + de + '\'' + 
			",pt = '" + pt + '\'' + 
			",ja = '" + ja + '\'' + 
			",it = '" + it + '\'' + 
			",fr = '" + fr + '\'' + 
			",es = '" + es + '\'' + 
			"}";
		}
}