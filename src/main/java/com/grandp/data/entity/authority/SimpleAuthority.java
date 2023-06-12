package com.grandp.data.entity.authority;

import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "authorities", uniqueConstraints = @UniqueConstraint(columnNames = "authority_name"))

public class SimpleAuthority implements GrantedAuthority {

	public static final SimpleAuthority ADMINISTRATOR = new SimpleAuthority("ADMINISTRATOR", "");
	public static final SimpleAuthority GUEST = new SimpleAuthority("GUEST", "");
	public static final SimpleAuthority TEACHER = new SimpleAuthority("TEACHER", "");
	public static final SimpleAuthority STUDENT = new SimpleAuthority("STUDENT", "");

	private static final long serialVersionUID = -7643915402806763835L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "authority_name")
    private String authorityName;

    @Column(name = "description")
    private String description;
	
    public SimpleAuthority() {
    	
    }
    
    @JsonCreator
	public SimpleAuthority(@JsonProperty("authority_name") String authorityName, @JsonProperty("description") String description) {
		this.authorityName = authorityName;
		this.description = description;
	}

	@Override
	public String getAuthority() {
		return this.authorityName;
	}

	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.authorityName;
	}

	public void setName(String authorityName) throws UnsupportedOperationException {
		if (ADMINISTRATOR.getAuthority().equals(this.authorityName) ||
			GUEST.getAuthority().equals(this.authorityName) ||
			TEACHER.getAuthority().equals(this.authorityName) ||
			STUDENT.getAuthority().equals(this.authorityName)) {
			throw new UnsupportedOperationException("Cannot modify System role: '" + this.authorityName + "'.");
		}

		this.authorityName = authorityName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SimpleAuthority that = (SimpleAuthority) o;
		return authorityName.equals(that.authorityName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(authorityName);
	}
}
