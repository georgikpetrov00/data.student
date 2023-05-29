package com.grandp.data.authorities;

import java.io.Serializable;
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

public class SimpleAuthority implements GrantedAuthority, Serializable {

	public static final SimpleAuthority ADMINISTRATOR = new SimpleAuthority("ROLE_ADMINISTRATOR", "");
	public static final SimpleAuthority GUEST = new SimpleAuthority("ROLE_GUEST", "");
	public static final SimpleAuthority TEACHER = new SimpleAuthority("ROLE_TEACHER", "");
	public static final SimpleAuthority STUDENT = new SimpleAuthority("ROLE_STUDENT", "");

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

	public String getName() {
		return this.authorityName;
	}

	public void setAuthorityName(String authorityName) {
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
