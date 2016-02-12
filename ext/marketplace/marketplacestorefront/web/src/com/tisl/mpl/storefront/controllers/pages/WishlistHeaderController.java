/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.util.AllWishListCompareByDate;
import com.tisl.mpl.util.ExceptionUtil;


/**
 *
 */
@Controller
@Scope("tenant")
@RequestMapping(value = RequestMappingUrlConstants.HEADER_WISHLIST)
public class WishlistHeaderController
{

	@Resource(name = "wishlistFacade")
	private WishlistFacade wishlistFacade;

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public String get(@RequestParam("productCount") final int productCount, final Model model, final HttpServletRequest request)
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		if (!userService.isAnonymousUser(currentCustomer))
		{

			try
			{
				final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
				final List<Wishlist2Model> allWishlistsModifiable = new ArrayList<Wishlist2Model>(allWishlists);
				final List<Wishlist2Model> latestThreeWishList = new ArrayList<Wishlist2Model>();

				if (!allWishlistsModifiable.isEmpty())
				{
					Collections.sort(allWishlistsModifiable, new AllWishListCompareByDate());

					if (allWishlistsModifiable.size() < productCount)
					{
						for (int i = 0; i < allWishlistsModifiable.size(); i++)
						{

							latestThreeWishList.add(allWishlistsModifiable.get(i));
						}
					}
					else
					{
						for (int i = 0; i < productCount; i++)
						{

							latestThreeWishList.add(allWishlistsModifiable.get(i));
						}
					}

				}
				model.addAttribute("latestThreeWishList", latestThreeWishList);
			}
			catch (final EtailBusinessExceptions etailBusinessExceptions)
			{
				ExceptionUtil.etailBusinessExceptionHandler(etailBusinessExceptions, null);
			}
			catch (final EtailNonBusinessExceptions etailNonBusinessExceptions)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(etailNonBusinessExceptions);
			}
			catch (final Exception exception)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception));
			}
			model.addAttribute("isSignedInUser", "yes");
		}
		return ControllerConstants.Views.Fragments.Home.WishlistPanel;
	}
}
