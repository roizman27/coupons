
package app.Clients;

import app.exceptions.CouponSystemException;
import app.facades.AdminFacade;
import app.facades.ClientFacade;
import app.facades.CompanyFacade;
import app.facades.CustomerFacade;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginManager {
	/*
	 * returns a business facade that matches the logger input
	 */
	private static LoginManager instance = new LoginManager();

	private LoginManager() {
	}
	
	public static LoginManager getInstance() {
		return instance;
	}
	
	public ClientFacade Login(String email , String password , ClientType clientType) throws CouponSystemException {
		//creating the facade to be used by the client
		AdminFacade adminF = new AdminFacade();
		CompanyFacade companyF = new CompanyFacade();
		CustomerFacade customerF = new CustomerFacade();
		try {
			if((adminF.Login(email, password) & clientType.equals(ClientType.Administrator))) {
				return adminF;
			}
			else if((companyF.Login(email, password)) & clientType.equals(ClientType.Company)) {
				return companyF;
			}
			else if((customerF.Login(email, password)) & clientType.equals(ClientType.Customer)) {
				return customerF;
			}
			else {
				return null;				
			}
		}catch (CouponSystemException e) {
			throw e;
		}
	}
}
