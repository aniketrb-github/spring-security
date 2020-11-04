package io.jbrains.springsecjpa.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Pojo/Model class : Role
 *
 * @author Aniket Bharsakale
 */
@Entity
@Table(name = "role" )
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private String roleId;
	
	@Column(name = "role")
	private String role;
	
	public Role() {}
	
	public Role(String roleId, String role) {
		this.roleId = roleId;
		this.role = role;
	}

	public Role(String role) {
		this.role = role;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setId(String roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
