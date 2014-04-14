package com.osmrecommend.persistence.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.TypeDef;

import com.osmrecommend.util.HstoreUserType;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Represents a way. A way is an ordered list of nodes which normally also has at least one tag.
 * 
 * See the wiki <a href="http://wiki.openstreetmap.org/wiki/Way">
 * article</a> for more details.
 * 
 * @author Vivek
 */
@Entity
@Table(name = "ways")
@TypeDef(name = "hstore", typeClass = HstoreUserType.class)
@Cacheable
public class Way implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7941769011539363185L;

	/**
	 * Primary key for the row in table.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	/**
	 * The ID to represent it across the system.
	 * Used for preserving historical information. 
	 */
	@Column(name = "way_id")
	private Long wayId;
	
	/**
	 * The version of the way this Object represents. 
	 */
	@Column(name = "version")
	private Integer version;
	
	
	/**
	 * The {@link User} that edited this version. 
	 */
	@OneToOne
	@PrimaryKeyJoinColumn(name = "user_id")
	private User user;
	
	/**
	 * Timestamp when this version of the Way was edited.
	 */
	@Column(name = "tstamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	/**
	 * The changeset that this version of the way belongs to. 
	 */
	@Column(name = "changeset_id")
	private Long changesetId;
	
	/**
	 * All the tags this Way contains. 
	 */
	@Column(name = "tags")
	private HashMap<String, Object> tags; 
	
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
	public HashMap<String, Object> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(HashMap<String, Object> tags) {
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
