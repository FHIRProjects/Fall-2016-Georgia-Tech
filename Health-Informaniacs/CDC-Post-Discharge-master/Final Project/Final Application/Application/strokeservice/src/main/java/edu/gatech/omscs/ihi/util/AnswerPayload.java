package edu.gatech.omscs.ihi.util;

import java.io.Serializable;

public class AnswerPayload implements Serializable
{
	private static final long serialVersionUID = -1693238231132711914L;
	
	private String id;
	
	private String field;
	
	private String text;
	
	private String parentid;
	
	private String value;
	
	private String type;
	
	public AnswerPayload()
	{
		
	}
	
	public AnswerPayload(String pid, 
						 String pfield,
						 String ptext,
						 String pparent,
						 String ptype,
						 String pvalue)
	{
		id = pid;
		field = pfield;
		text = ptext;
		parentid = pparent;
		type = ptype;
		value = pvalue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParentId() {
		return parentid;
	}

	public void setParentId(String parent) {
		this.parentid = parent;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}