package com.osmrecommend.persistence.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.postgis.PGgeometry;

@Entity
@Table(name = "ndoes")
public class Node implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8265855470645903224L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "version")
	private Integer version;
	
	@Column(name = "user_id")
	private User user;
	
	@Column(name = "tsamp")
	private Date tstamp;
	
	@Column(name = "changeset_id")
	private Long changesetId;
	
	@Column(name = "geom")
	private PGgeometry geom;

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

	public Date getTstamp() {
		return tstamp;
	}

	public void setTstamp(Date tstamp) {
		this.tstamp = tstamp;
	}

	public Long getChangesetId() {
		return changesetId;
	}

	public void setChangesetId(Long changesetId) {
		this.changesetId = changesetId;
	}

	public PGgeometry getGeom() {
		return geom;
	}

	public void setGeom(PGgeometry geom) {
		this.geom = geom;
	}
	
	
}
