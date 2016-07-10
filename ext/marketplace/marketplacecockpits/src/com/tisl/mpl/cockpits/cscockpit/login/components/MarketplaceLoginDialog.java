package com.tisl.mpl.cockpits.cscockpit.login.components;

import de.hybris.platform.cockpit.components.login.LoginDialog;
import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.api.Label;

import com.tisl.mpl.cockpits.cscockpit.forms.login.MarketplaceCscockpitLoginForm;

public class MarketplaceLoginDialog extends LoginDialog
{
	private static final long serialVersionUID = 1L;

	public MarketplaceLoginDialog()
	{
		setShadow(false);

		UITools.addBusyListener(this, "onOK", new EventListener()
		{
			public void onEvent(final Event event) throws Exception
			{
				if (MarketplaceLoginDialog.this.getLoginForm().doOK())
				{
					MarketplaceLoginDialog.this.doOK();
				}
				else
					
				{	
					
					if(!MarketplaceLoginDialog.this.getLoginForm().getConfirmPassword()){
					final Label statusLabel = MarketplaceLoginDialog.this.getLoginForm().getStatusLabel();
					if (statusLabel != null)
					{
						statusLabel.setValue(Labels.getLabel("login.error.selected.language.na"));
						statusLabel.setVisible(true);
					}
				}}
			}
		}, null, null);

		addEventListener("onCreate", new EventListener()
		{
			public void onEvent(final Event event) throws Exception
			{
				
				MarketplaceLoginDialog.this.getLoginForm().init(MarketplaceLoginDialog.this);
				MarketplaceLoginDialog.this.showTimeoutNotice();
				MarketplaceLoginDialog.this.getLoginForm().setLoginFormDialogWindow(MarketplaceLoginDialog.this);
			}
		});
	}




	@Override
	protected MarketplaceCscockpitLoginForm getLoginForm()
	{
		return (MarketplaceCscockpitLoginForm) SpringUtil.getBean("LoginForm");
	}

}
