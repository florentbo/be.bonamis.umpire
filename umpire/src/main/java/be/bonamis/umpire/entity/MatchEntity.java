package be.bonamis.umpire.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "match")
public class MatchEntity implements be.bonamis.umpire.entity.Entity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long matchId;

	@Column(name = "umpire1")
	private UmpireEntity umpire1;
	@Column(name = "umpire2")
	private UmpireEntity umpire2;
	@Column(name = "match_day")
	private Date matchDay;
}
