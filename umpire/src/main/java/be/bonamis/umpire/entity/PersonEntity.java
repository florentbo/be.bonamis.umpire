package be.bonamis.umpire.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "persons")
public abstract class PersonEntity implements be.bonamis.umpire.entity.Entity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long personId;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "login", nullable = false)
	private String login;
	@Column(name = "password")
	private String password;
	@Column(name = "bithDay")
	private Date bith_day;
	@Column(name = "email")
	private String email;
}
