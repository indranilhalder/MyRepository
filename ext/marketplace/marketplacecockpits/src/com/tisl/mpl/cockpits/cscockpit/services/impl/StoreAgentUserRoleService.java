package com.tisl.mpl.cockpits.cscockpit.services.impl;

import java.util.Set;

import com.tisl.mpl.cockpits.cscockpit.services.StoreAgentUserRole;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.security.PrincipalGroupModel;

public class StoreAgentUserRoleService implements StoreAgentUserRole
{
	@Override
	public boolean isUserInRole(String groupName) {
		Set<PrincipalGroupModel> userGroups = UISessionUtils
				.getCurrentSession().getUser().getAllGroups();

		for (PrincipalGroupModel ug : userGroups) {
			if (ug.getUid().equalsIgnoreCase(groupName)) {
				return true;
			}
		}
		return false;
	}

}
