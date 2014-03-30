package com.osmrecommend.persistence.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.postgis.PGgeometry;

@Entity
@Table(name = "ways")
public class Way {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "version")
	private Integer version;
	
	@OneToOne
	@Column(name = "user_id")
	private User user;
	
	@Column(name = "tstamp")
	private Date timestamp;
	
	@Column(name = "changeset_id")
	private Long changesetId;
	
	@Column(name = "bbox")
	private PGgeometry bbox;
	
	@Column(name = "linestring")
	private PGgeometry linestring;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Long getChangesetId() {
		return changesetId;
	}

	public void setChangesetId(Long changesetId) {
		this.changesetId = changesetId;
	}

	public PGgeometry getBbox() {
		return bbox;
	}

	public void setBbox(PGgeometry bbox) {
		this.bbox = bbox;
	}

	public PGgeometry getLinestring() {
		return linestring;
	}

	public void setLinestring(PGgeometry linestring) {
		this.linestring = linestring;
	}
	
}
