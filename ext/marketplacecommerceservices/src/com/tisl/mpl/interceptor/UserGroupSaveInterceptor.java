/**
 *
 */
package com.tisl.mpl.interceptor;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class UserGroupSaveInterceptor implements PrepareInterceptor<PrincipalGroupModel>
{

	@Resource
	private ExtendedUserService extendedUserService;


	/**
	 * This method is called when User Group is saved from HMC. If there is list of User ID strings in User Group Model,
	 * then the User Models corresponding to the user IDs are fetched and added to the User Group
	 *
	 * @param principalGroupModel
	 * @param interCeptorCtx
	 *
	 */
	@Override
	public void onPrepare(final PrincipalGroupModel principalGroupModel, final InterceptorContext interCeptorCtx)
			throws InterceptorException
	{
		try
		{
			if (null != principalGroupModel && StringUtils.isNotEmpty(principalGroupModel.getUserList()))
			{
				final String userIds = principalGroupModel.getUserList(); //List of userIds separated by commas
				final List<PrincipalModel> newUserModelList = new ArrayList<PrincipalModel>();

				final StringTokenizer newUserIdTokens = new StringTokenizer(userIds,
						MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				while (newUserIdTokens.hasMoreTokens())
				{
					newUserModelList.add(getExtendedUserService().getUserByUid(newUserIdTokens.nextToken().trim()));

				}
				final Collection<PrincipalModel> existingPrincipalModelList = principalGroupModel.getMembers();

				final List<PrincipalModel> finalUserModelList = new ArrayList<PrincipalModel>();
				final Set<PrincipalModel> userModelSet = new HashSet<PrincipalModel>();
				if (CollectionUtils.isNotEmpty(existingPrincipalModelList))
				{
					finalUserModelList.addAll(existingPrincipalModelList);
				}
				finalUserModelList.addAll(newUserModelList);
				userModelSet.addAll(finalUserModelList);
				principalGroupModel.setMembers(userModelSet); //Setting the users in user group model

			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
		}


	}


	/**
	 * @return the extendedUserService
	 */
	public ExtendedUserService getExtendedUserService()
	{
		return extendedUserService;
	}


	/**
	 * @param extendedUserService
	 *           the extendedUserService to set
	 */
	public void setExtendedUserService(final ExtendedUserService extendedUserService)
	{
		this.extendedUserService = extendedUserService;
	}





}
