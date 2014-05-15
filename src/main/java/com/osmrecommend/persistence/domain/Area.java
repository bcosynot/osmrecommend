package com.osmrecommend.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "areas")
public class Area {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "x1")
	private Double x1;
	
	@Column(name = "x2")
	private Double x2;
	
	@Column(name = "y1")
	private Double y1;
	
	@Column(name = "y2")
	private Double y2;
	
	@Column(name = "the_geom")
	private Geometry theGeom;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the x1
	 */
	public Double getX1() {
		return x1;
	}

	/**
	 * @param x1 the x1 to set
	 */
	public void setX1(Double x1) {
		this.x1 = x1;
	}

	/**
	 * @return the x2
	 */
	public Double getX2() {
		return x2;
	}

	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(Double x2) {
		this.x2 = x2;
	}

	/**
	 * @return the y1
	 */
	public Double getY1() {
		return y1;
	}

	/**
	 * @param y1 the y1 to set
	 */
	public void setY1(Double y1) {
		this.y1 = y1;
	}

	/**
	 * @return the y2
	 */
	public Double getY2() {
		return y2;
	}

	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(Double y2) {
		this.y2 = y2;
	}

	/**
	 * @return the theGeom
	 */
	public Geometry getTheGeom() {
		return theGeom;
	}

	/**
	 * @param theGeom the theGeom to set
	 */
	public void setTheGeom(Geometry theGeom) {
		this.theGeom = theGeom;
	}
	
	
}
