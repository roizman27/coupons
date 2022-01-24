package app.core.beans;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@Setter
public class Company {
	/*
	 * company bean	
	 */
	private int id;
	private String name;
	private String email;
	private String password;
	private ArrayList <Coupon> coupons = new ArrayList<>();
	
	/*
	 * used to create a company to be add to the DB by system admin
	 */
	public Company(String name, String email, String passward) {
		super();
		this.id = 0;
		this.name = name;
		this.email = email;
		this.password = passward;
	}
	
	
	public Company(int id,String name, String email, String passward) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = passward;
	}
	
	//used for testing 
	public Company(int id) {
		super();
		this.id = id;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", company name=" + name + ", email=" + email + ", password=" + password
				+ ", coupons=" + coupons + "]";
	}

}
