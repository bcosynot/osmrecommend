package com.osmrecommend.persistence.domain;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.osmrecommend.util.HstoreUserType;
import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "nodes")
@TypeDef(name = "hstore", typeClass = HstoreUserType.class)
public class Node {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name="node_id")
	private Long nodeId;
	
	/**
	 * @return the nodeId
	 */
	public Long getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "version")
	private Integer version;
	
	@OneToOne
	@PrimaryKeyJoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "tsamp")
	private Date tstamp;
	
	@Column(name = "changeset_id")
	private Long changesetId;
	
	@Column(name = "geom")
	private Geometry geom;
	
	@Type(type = "hstore")
	@Column(name = "tags", columnDefinition = "hstore")
	private Object2ObjectOpenHashMap<String, String> tags = new Object2ObjectOpenHashMap<String, String>();

	/**
	 * @return the tags
	 */
	public Object2ObjectOpenHashMap<String, String> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(Object2ObjectOpenHashMap<String, String> tags) {
		this.tags = tags;
	}

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

	public Geometry getGeom() {
		return geom;
	}

	public void setGeom(Geometry geom) {
		this.geom = geom;
	}
	
	
}
