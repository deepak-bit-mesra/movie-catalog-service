package com.example.demo.models;

import brave.Span;

public class TraceInfo {
	private String spanIdStr;
	private String traceIdStr;
	private String parentIdStr;
	public TraceInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TraceInfo(Span span) {
		super();
		this.spanIdStr = span.context().spanIdString();
		this.traceIdStr = span.context().traceIdString();
		this.parentIdStr = span.context().parentIdString();
	}
	public TraceInfo(String spanIdStr, String traceIdStr, String parentIdStr) {
		super();
		this.spanIdStr = spanIdStr;
		this.traceIdStr = traceIdStr;
		this.parentIdStr = parentIdStr;
	}
	public String getSpanIdStr() {
		return spanIdStr;
	}
	public void setSpanIdStr(String spanIdStr) {
		this.spanIdStr = spanIdStr;
	}
	public String getTraceIdStr() {
		return traceIdStr;
	}
	public void setTraceIdStr(String traceIdStr) {
		this.traceIdStr = traceIdStr;
	}
	public String getParentIdStr() {
		return parentIdStr;
	}
	public void setParentIdStr(String parentIdStr) {
		this.parentIdStr = parentIdStr;
	}
	
	
	
	
}
