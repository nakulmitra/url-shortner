package com.devportal.to.response;

import java.util.List;

import com.devportal.bean.UrlMapping;

public class ShortenUrlResponse extends ApiResponse {

	private UrlMapping urlMapping;
	private String shortCode;
	private String originalURL;
	private List<UrlMapping> urlMappingList;
	private Long count;

	public ShortenUrlResponse() {
		super();
	}

	public UrlMapping getUrlMapping() {
		return urlMapping;
	}

	public void setUrlMapping(UrlMapping urlMapping) {
		this.urlMapping = urlMapping;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getOriginalURL() {
		return originalURL;
	}

	public void setOriginalURL(String originalURL) {
		this.originalURL = originalURL;
	}

	public List<UrlMapping> getUrlMappingList() {
		return urlMappingList;
	}

	public void setUrlMappingList(List<UrlMapping> urlMappingList) {
		this.urlMappingList = urlMappingList;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
