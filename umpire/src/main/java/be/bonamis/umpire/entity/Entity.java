package be.bonamis.umpire.entity;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface Entity {
	
	int hashCode();	
	
	boolean equals(Object obj);
		
}