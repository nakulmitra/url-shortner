package com.devportal.bean;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "url_mapping", schema = "projects")
public class URLMapping {

	@Id
	@Column(name = "short_code")
	private String shortCode;

	@Column(name = "original_url")
	private String originalUrl;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "hit_count")
	private int hitCount = 0;

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public int getHitCount() {
		return hitCount;
	}

	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}

	@Override
	public String toString() {
		return "shortCode=" + shortCode + ", originalUrl=" + originalUrl + ", createdAt=" + createdAt + ", hitCount="
				+ hitCount;
	}

}
