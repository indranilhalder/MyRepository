/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

/**
 * @author TCS
 *
 */
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.service.AgentIdForStore;


public class AgentIdForStoreImpl implements AgentIdForStore
{
	@Autowired
	private UserService userService;

	@Override
	public String getAgentIdForStore(final String agentGroup)
	{
		final String agentID = (String) JaloSession.getCurrentSession().getAttribute("sellerId");
		if (StringUtils.isNotEmpty(agentID))
		{
			final UserModel user = userService.getUserForUID(agentID);
			final Set<PrincipalGroupModel> userGroups = user.getAllGroups();

			for (final PrincipalGroupModel ug : userGroups)
			{
				if (ug != null && ug.getUid().equalsIgnoreCase(agentGroup))
				{
					return agentID;
				}
			}
		}
		return StringUtils.EMPTY;
	}
}
