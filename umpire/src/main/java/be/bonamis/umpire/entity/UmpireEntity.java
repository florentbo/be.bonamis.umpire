package be.bonamis.umpire.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "persons")
public class UmpireEntity implements be.bonamis.umpire.entity.Entity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long umpireId;

	@Column(name = "ranking", nullable = false)
	private String ranking;

}
