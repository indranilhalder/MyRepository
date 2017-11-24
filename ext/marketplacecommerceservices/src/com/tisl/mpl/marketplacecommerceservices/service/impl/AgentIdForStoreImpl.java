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
		String agentID = StringUtils.EMPTY;
		String sellerIdorLoginId = StringUtils.EMPTY;
		final JaloSession jSession = JaloSession.getCurrentSession();
		if (jSession != null)
		{
			agentID = (String) jSession.getAttribute("sellerId");
		}
		if (StringUtils.isNotEmpty(agentID))
		{
			if (agentID.contains("-"))
			{
				sellerIdorLoginId = agentID.split("-")[0];
			}
			else
			{
				sellerIdorLoginId = agentID;
			}
			final UserModel user = userService.getUserForUID(agentID);
			final Set<PrincipalGroupModel> userGroups = user.getAllGroups();

			for (final PrincipalGroupModel ug : userGroups)
			{
				if (ug != null && ug.getUid().equalsIgnoreCase(agentGroup))
				{
					return sellerIdorLoginId;
				}
			}
		}
		return StringUtils.EMPTY;
	}
}
