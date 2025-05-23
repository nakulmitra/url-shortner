package com.devportal.to.response;

import java.util.List;

import com.devportal.bean.URLMapping;
import com.devportal.constants.URLConstants;

public class ShortenURLResponse extends ApiResponse {

	private URLMapping urlMapping;
	private String shortURL;
	private String originalURL;
	private List<URLMapping> urlMappingList;
	private Long count;

	public ShortenURLResponse() {
		super();
	}

	public URLMapping getUrlMapping() {
		return urlMapping;
	}

	public void setUrlMapping(URLMapping urlMapping) {
		this.urlMapping = urlMapping;
	}

	public String getShortURL() {
		return shortURL;
	}

	public void setShortURL(String shortURL) {
		this.shortURL = URLConstants.PREFIX + shortURL;
	}

	public String getOriginalURL() {
		return originalURL;
	}

	public void setOriginalURL(String originalURL) {
		this.originalURL = originalURL;
	}

	public List<URLMapping> getUrlMappingList() {
		return urlMappingList;
	}

	public void setUrlMappingList(List<URLMapping> urlMappingList) {
		this.urlMappingList = urlMappingList;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
