package com.osmrecommend.persistence.domain;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
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
@Table(name = "ways")
@TypeDef(name = "hstore", typeClass = HstoreUserType.class)
@Cacheable
public class Way implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 705971055750956266L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "way_id")
	private Long wayId;
	
	@Column(name = "version")
	private Integer version;
	
	@OneToOne
	@PrimaryKeyJoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "tstamp")
	private Date timestamp;
	
	@Column(name = "changeset_id")
	private Long changesetId;
	
	@Type(type = "hstore")
	@Column(name = "tags", columnDefinition = "hstore")
	private Object2ObjectOpenHashMap<String, String> tags = new Object2ObjectOpenHashMap<String, String>(); 
	
	@Column(name = "bbox")
	private Geometry bbox;
	
	@Column(name = "linestring")
	private Geometry linestring;
	
	@Column(name = "nodes")
	private Long[] nodes;
	
	/**
	 * @return the wayId
	 */
	public Long getWayId() {
		return wayId;
	}

	/**
	 * @param wayId the wayId to set
	 */
	public void setWayId(Long wayId) {
		this.wayId = wayId;
	}

	/**
	 * @return the nodes
	 */
	public Long[] getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(Long[] nodes) {
		this.nodes = nodes;
	}

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

	/**
	 * @param bbox the bbox to set
	 */
	public void setBbox(Geometry bbox) {
		this.bbox = bbox;
	}

	/**
	 * @param linestring the linestring to set
	 */
	public void setLinestring(Geometry linestring) {
		this.linestring = linestring;
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

}
