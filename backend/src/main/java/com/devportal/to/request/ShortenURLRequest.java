package com.devportal.to.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ShortenURLRequest {

	@NotBlank(message = "URL cannot be blank")
	@Pattern(regexp = "^(http|https)://[^\\s$.?#].[^\\s]*$", message = "Invalid URL format")
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}