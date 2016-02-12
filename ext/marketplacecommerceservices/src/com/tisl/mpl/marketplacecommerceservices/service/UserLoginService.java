/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;



/**
 * @author 314180
 *
 */
public interface UserLoginService
{
	public String loginUser(final String login, final String password);

	/**
	 * @param login
	 * @return
	 */
	public String loginUserForSocial(String login);
}
