package com.tisl.mpl.cockpits.cscockpit.login.components;

import de.hybris.platform.cmscockpit.components.login.CsLoginButton;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.api.Label;

import com.tisl.mpl.cockpits.cscockpit.forms.login.MarketplaceCscockpitLoginForm;


/**
 * 
 */
public class MarketplaceLoginButton extends CsLoginButton
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public MarketplaceLoginButton()
	{
		if (UISessionUtils.getCurrentSession().isUsingTestIDs())
		{
			UITools.applyTestID(this, "Login_Login_button");
		}
		addEventListener("onCreate", new EventListener()
		{
			public void onEvent(final Event event) throws Exception
			{
				MarketplaceLoginButton.this.focus();
			}
		});
		addEventListener("onClick", new EventListener()
		{
			public void onEvent(final Event event)
			{
				if (MarketplaceLoginButton.this.getLoginForm().doOK())
				{	
					MarketplaceLoginButton.this.doOK();
				}
				else
				{	
					if(MarketplaceLoginButton.this.getLoginForm().getConfirmPassword()){
						final Label statusLabel = MarketplaceLoginButton.this.getLoginForm().getStatusLabel();
						statusLabel.setVisible(false);
					}
					else{
						final Label statusLabel = MarketplaceLoginButton.this.getLoginForm().getStatusLabel();
						if (statusLabel != null)
						{
							statusLabel.setValue(Labels.getLabel("login.error.selected.language.na"));
							statusLabel.setVisible(true);
						}
					}
				}
			}
		});
	}

	@Override
	protected MarketplaceCscockpitLoginForm getLoginForm()
	{
		return (MarketplaceCscockpitLoginForm) SpringUtil.getBean("LoginForm");
	}



}
