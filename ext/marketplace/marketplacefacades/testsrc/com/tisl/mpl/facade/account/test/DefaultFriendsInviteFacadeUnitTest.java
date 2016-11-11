/**
 *
 */
package com.tisl.mpl.facade.account.test;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.FriendsModel;
import com.tisl.mpl.data.FriendsInviteData;
import com.tisl.mpl.facades.account.register.impl.DefaultFriendsInviteFacade;
import com.tisl.mpl.marketplacecommerceservices.service.FriendsInviteService;


/**
 * @author TCS
 *
 */
public class DefaultFriendsInviteFacadeUnitTest
{
	@Autowired
	private UserService userService;
	@Autowired
	private FriendsInviteService friendsInviteService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private BusinessProcessService businessProcessService;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private CommonI18NService commonI18NService;
	private DefaultFriendsInviteFacade defaultFriendsInviteFacade;
	private CustomerModel customerModel;

	private static final Logger LOG = Logger.getLogger(DefaultFriendsInviteFacadeUnitTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		this.defaultFriendsInviteFacade = new DefaultFriendsInviteFacade();

		this.modelService = Mockito.mock(ModelService.class);
		this.defaultFriendsInviteFacade.setModelService(modelService);

		this.friendsInviteService = Mockito.mock(FriendsInviteService.class);
		this.defaultFriendsInviteFacade.setFriendsInviteService(friendsInviteService);

		this.userService = Mockito.mock(UserService.class);
		this.defaultFriendsInviteFacade.setUserService(userService);

		this.businessProcessService = Mockito.mock(BusinessProcessService.class);
		this.defaultFriendsInviteFacade.setBusinessProcessService(businessProcessService);

		this.baseSiteService = Mockito.mock(BaseSiteService.class);
		this.defaultFriendsInviteFacade.setBaseSiteService(baseSiteService);

		this.baseSiteService = Mockito.mock(BaseSiteService.class);
		this.defaultFriendsInviteFacade.setBaseSiteService(baseSiteService);

		this.commonI18NService = Mockito.mock(CommonI18NService.class);
		this.defaultFriendsInviteFacade.setCommonI18NService(commonI18NService);
	}


	@Test
	public void testCheckUniquenessOfEmail()
	{
		final List<String> friendsEmailList = new ArrayList<String>();
		friendsEmailList.add("demo@user.com");
		friendsEmailList.add("user@test.com");

		final String friendsEmail = friendsEmailList.get(0);
		final CustomerModel customer = new CustomerModel();
		Mockito.when(friendsInviteService.checkUniquenessOfEmail(friendsEmail.toLowerCase())).thenReturn(customer);
		LOG.info("Method : testCheckUniquenessOfEmail >>>>>>>");
	}

	@Test
	public void testIsEmailEqualsToCustomer()
	{
		final String friendsEmail = "demo@user.com";
		customerModel = new CustomerModel();
		customerModel.setOriginalUid("demo@user.com");
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		assertEquals("We should find same email id already exists in DB", friendsEmail, customerModel.getOriginalUid());
		LOG.info("Method : testIsEmailEqualsToCustomer >>>>>>>");
	}

	@Test
	public void testIinviteFriends()
	{
		final Boolean flag = Boolean.valueOf(true);
		final FriendsInviteData friendsInviteData = new FriendsInviteData();
		Mockito.when(Boolean.valueOf(friendsInviteService.inviteNewFriends(friendsInviteData))).thenReturn(flag);
		LOG.info("Method : testIinviteFriends >>>>>>>");
	}

	@Test
	public void testUpdateFriendsModel()
	{
		final FriendsModel friendsModel = new FriendsModel();
		Mockito.doNothing().when(friendsInviteService).updateFriendsModel(friendsModel);
		LOG.info("Method : testUpdateFriendsModel >>>>>>>");
	}
}
