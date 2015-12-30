package com.tisl.mpl.cockpits.cscockpit.forms.login;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import de.hybris.platform.cockpit.forms.login.LoginForm;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

public class MarketplaceCscockpitLoginForm extends LoginForm
{
	private Window loginDialogWindow;
    private boolean confirmPassword=false;
	private UserService userService;
	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*&.]).{8,16})";
	private static final String MAX_NUMBER_OF_ATTEMPTS = "maxnumberofattempts";
	private static final String LOGIN_DISABLED = "logindisabled";
    private static final String MAX_NUMBER_OF_ATTEMPTS_TITLE = "maxnumberofattemptstitle";
	private static final String NEW_PASSWORD_SAME_AS_PREVIOUS = "newpasswordsameasprevious";
	private static final String NEW_PASSWORD_SAME_AS_PREVIOUS_TITLE = "newpasswordsameasprevioustitle";
	private static final String INVALID_PASSWORD_FORMAT = "invalidpasswordformat";
	private static final String INVALID_PASSWORD_FORMAT_TITLE = "invalidpasswordformattitle";
	private static final String PASSWORD_RECONFIRM_PASSWORD_NOT_SAME = "passwordreconfirmpasswordnotsame";
	private static final String PASSWORD_RECONFIRM_PASSWORD_NOT_SAME_TITLE = "passwordreconfirmpasswordnotsametitle";
	private static final String PASSWORD_IS_GOING_TO_EXPIRE = "passwordisgoingtoexpire";
	private static final String PASSWORD_IS_GOING_TO_EXPIRE_TITLE = "passwordisgoingtoexpiretitle";
	private static final String PASSWORD_HAS_EXPIRED = "passwordhasexpired";
	private static final String PASSWORD_HAS_EXPIRED_TITLE = "passwordhasexpiredtitle";
	private static final Logger LOG = Logger
			.getLogger(MarketplaceCscockpitLoginForm.class);
	
	@Autowired
	private ModelService modelService;
	@Autowired
	private ConfigurationService configurationService;

	public UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	/**
	 * @return
	 */
	public Window getLoginDialogWindow()
	{
		return loginDialogWindow;
	}
	
	private int total_no_of_attempts;
	private int total_no_of_login_attempts;
	
	public void init(){
		total_no_of_attempts = configurationService.getConfiguration().getInt("cscockpit.login.tempPassword.totalnoofattempts");
//		total_no_of_login_attempts = configurationService.getConfiguration().getInt("cscockpit.login.tempPassword.totalnoofloginattempts");
	}
	
	@Override
	public boolean doOK() {
		boolean success = false;
		
		//Added
		final Window loginDialog = getLoginDialogWindow();

		if (loginDialog != null)
		{
			final String userName = ((Textbox) (loginDialog.getFellow("login"))).getValue();
			
			final Textbox pwd = (Textbox) loginDialog.getFellow("pw");
			final String previousPassword = pwd.getValue();
			 String message = null;

			if (null != getLoginService().doLogin(userName, previousPassword, getSelectedLanguage()) && checkForTemporaryPassword(loginDialog, userName))
			{
				
				setConfirmPassword(true);
				
			if (changeTemporaryPassword(loginDialog, userName,
						previousPassword, message))
				{
					
					Session session = Executions.getCurrent().getDesktop().getSession();
					try {
						session.setAttribute("px_preferred_locale", getLoginService()
								.getLocale(getSelectedLanguage()));
						getLoginService().setSessionLanguage(getSelectedLanguage());
						success = true;
					} catch (IllegalArgumentException iae) {
						success = false;
					}
				}
		
		//Ended
			}
			else if (checkForPasswordExpiry(loginDialog, userName, message)) {
				Session session = Executions.getCurrent().getDesktop()
						.getSession();
				try {
					session.setAttribute("px_preferred_locale",
							getLoginService().getLocale(getSelectedLanguage()));
					getLoginService().setSessionLanguage(getSelectedLanguage());
					success = true;
				} catch (IllegalArgumentException iae) {
					success = false;
				}
			}
			else
			{  
				final EmployeeModel emp = (EmployeeModel) userService.getUserForUID(userName);
				Session session = Executions.getCurrent().getDesktop().getSession();
				if(null != getLoginService().doLogin(userName, previousPassword, getSelectedLanguage())) {
					try {
						session.setAttribute("px_preferred_locale", getLoginService()
								.getLocale(getSelectedLanguage()));
						getLoginService().setSessionLanguage(getSelectedLanguage());
						success = true;
					} catch (IllegalArgumentException iae) {
						success = false;
					}
				}
				else {
					 if(emp.isLoginDisabled()){
					    	try {
								Messagebox.show(LabelUtils.getLabel(message, LOGIN_DISABLED, new Object[0]),LabelUtils.getLabel(message, MAX_NUMBER_OF_ATTEMPTS_TITLE, new Object[0]), Messagebox.OK, Messagebox.ERROR);
								return true;
							} catch (InterruptedException e) {
								LOG.warn("Login Disabled", e);
							}
					    }
					if (total_no_of_attempts <= 0) {
						try {
							Messagebox.show(LabelUtils.getLabel(message, MAX_NUMBER_OF_ATTEMPTS, new Object[0]),LabelUtils.getLabel(message, MAX_NUMBER_OF_ATTEMPTS_TITLE, new Object[0]), Messagebox.OK, Messagebox.ERROR);
							emp.setLoginDisabled(true);
							modelService.save(emp);
							Sessions.getCurrent().invalidate();
							return true;

						} catch (InterruptedException e) {
							LOG.warn("Exceeded maximum nbr of attempts", e);
						}
					}
					else {
						total_no_of_attempts = total_no_of_attempts - 1;
						try {
							session.setAttribute("px_preferred_locale", getLoginService()
									.getLocale(getSelectedLanguage()));
							getLoginService().setSessionLanguage(getSelectedLanguage());
							success = true;
						} catch (IllegalArgumentException iae) {
							success = false;
						}
					}
				}
			}
		
		}
		return success;
		
	}

//	private boolean checkForLoginAttemptCount(Window loginDialog,
//			String userName, String previousPassword, String message) {
//		final EmployeeModel emp = (EmployeeModel) userService.getUserForUID(userName);
//		if(null == getLoginService().doLogin(userName, previousPassword, getSelectedLanguage())) {
//			 if(emp.isLoginDisabled()){
//			    	try {
//						Messagebox.show(LabelUtils.getLabel(message, LOGIN_DISABLED, new Object[0]),LabelUtils.getLabel(message, MAX_NUMBER_OF_ATTEMPTS_TITLE, new Object[0]), Messagebox.OK, Messagebox.ERROR);
//						return true;
//					} catch (InterruptedException e) {
//						LOG.warn("Login Disabled", e);
//					}
//			    }
//			if (total_no_of_attempts <= 0) {
//				try {
//					Messagebox.show(LabelUtils.getLabel(message, MAX_NUMBER_OF_ATTEMPTS, new Object[0]),LabelUtils.getLabel(message, MAX_NUMBER_OF_ATTEMPTS_TITLE, new Object[0]), Messagebox.OK, Messagebox.ERROR);
//					emp.setLoginDisabled(true);
//					modelService.save(emp);
//					return true;
//
//				} catch (InterruptedException e) {
//					LOG.warn("Exceeded maximum nbr of attempts", e);
//				}
//			}
//			else {
//				total_no_of_attempts = total_no_of_attempts - 1;
//			}
//		}
//		return false;
//	}

	/**
	 * @param loginDialog
	 */
	public void showChangePassword(final Window loginDialog)
	{
		
		
		final Textbox changePass = (Textbox) loginDialog.getFellow("chpwd");
		changePass.setVisible(true);
		final Label status1 = (Label) loginDialog.getFellow("status1");
		status1.setVisible(true);
		final Textbox reconfirmPass = (Textbox) loginDialog.getFellow("reconpwd");
		reconfirmPass.setVisible(true);
		final Label status2 = (Label) loginDialog.getFellow("status2");
		status2.setVisible(true);
		final Textbox pwd = (Textbox) loginDialog.getFellow("pw");
		pwd.setVisible(false);
		final Label pwdLabel = (Label) loginDialog.getFellow("pwdLabel");
		pwdLabel.setVisible(false);


	}

	/**
	 * @param loginDialog
	 * @return
	 */
	public void setLoginFormDialogWindow(final Window loginDialog)
	{
		
		if (loginDialog != null)
		{
			loginDialogWindow = loginDialog;
		}

	}

	/**
	 * @param loginDialog
	 * @param userName
	 * @return boolean
	 */
	public boolean checkForTemporaryPassword(final Window loginDialog, final String userName)
	{
		
		final EmployeeModel emp = (EmployeeModel) userService.getUserForUID(userName);
		if (emp != null && emp.getIsTemporaryPasswordChanged() != null && !emp.getIsTemporaryPasswordChanged())
		{
					showChangePassword(loginDialog);
					return true;
		}
		return false;
	}

	/**
	 * @param loginDialog
	 * @param userName
	 * @param previousPassword
	 * @return boolean
	 */
	public boolean changeTemporaryPassword(final Window loginDialog, final String userName, final String previousPassword, String message)
	{
		
		final EmployeeModel emp = (EmployeeModel) userService.getUserForUID(userName);
		final Textbox changePass = (Textbox) loginDialog.getFellow("chpwd");
		final Textbox reconfirmPass = (Textbox) loginDialog.getFellow("reconpwd");
	    boolean match = false;
	    if(emp.isLoginDisabled()){
	    	try {
				Messagebox.show(LabelUtils.getLabel(message, LOGIN_DISABLED, new Object[0]),LabelUtils.getLabel(message, MAX_NUMBER_OF_ATTEMPTS_TITLE, new Object[0]), Messagebox.OK, Messagebox.ERROR);
				return true;
			} catch (InterruptedException e) {
				LOG.warn("Login Disabled", e);
			}
	    }
	    if (total_no_of_attempts <= 0) {
			try {
				Messagebox.show(LabelUtils.getLabel(message, MAX_NUMBER_OF_ATTEMPTS, new Object[0]),LabelUtils.getLabel(message, MAX_NUMBER_OF_ATTEMPTS_TITLE, new Object[0]), Messagebox.OK, Messagebox.ERROR);
				emp.setLoginDisabled(true);
				modelService.save(emp);
				Sessions.getCurrent().invalidate();
				return true;

			} catch (InterruptedException e) {
				LOG.warn("Exceeded maximum nbr of attempts", e);
			}
		} else {
		if(changePass.getValue().equals(previousPassword)){
			total_no_of_attempts = total_no_of_attempts - 1;
		
			try {
				Messagebox.show(LabelUtils.getLabel(message, NEW_PASSWORD_SAME_AS_PREVIOUS, new Object[0]),LabelUtils.getLabel(message, NEW_PASSWORD_SAME_AS_PREVIOUS_TITLE, new Object[0]), Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				LOG.warn("Failed to validate the password", e);
			}
		}
		
		if (changePass.getValue() != null && !changePass.getValue().isEmpty() && !changePass.getValue().equals(previousPassword))
		{   
			if(!passwordValidator(changePass.getValue(), match)){
				total_no_of_attempts = total_no_of_attempts - 1;
			
				try {
					Messagebox.show(LabelUtils.getLabel(message, INVALID_PASSWORD_FORMAT, new Object[0]),LabelUtils.getLabel(message, INVALID_PASSWORD_FORMAT_TITLE, new Object[0]), Messagebox.OK, Messagebox.ERROR);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					LOG.warn("Failed to validate the password format", e);
				}
			}
			//else if (null != reconfirmPass.getValue() && !reconfirmPass.getValue().isEmpty() && !changePass.getValue().equals(reconfirmPass.getValue())) {
			else if (reconfirmPass.getValue() !=null && !reconfirmPass.getValue().isEmpty()){
				if( !changePass.getValue().equals(reconfirmPass.getValue())) {
				total_no_of_attempts = total_no_of_attempts - 1;
				try {
					Messagebox.show(LabelUtils.getLabel(message, PASSWORD_RECONFIRM_PASSWORD_NOT_SAME, new Object[0]),LabelUtils.getLabel(message, PASSWORD_RECONFIRM_PASSWORD_NOT_SAME_TITLE, new Object[0]), Messagebox.OK, Messagebox.ERROR);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					LOG.warn("Failed to validate the password", e);
				}
			}
			else{
				userService.setPasswordWithDefaultEncoding(emp, changePass.getValue());
				emp.setIsTemporaryPasswordChanged(Boolean.TRUE);
				modelService.save(emp);
				Calendar cal = Calendar.getInstance();
				Date today = cal.getTime();
				emp.setLastPasswordChanged(today);
				modelService.save(emp);
				Textbox password=(Textbox)loginDialog.getFellow("pw");
				password.setValue(changePass.getValue());
				return true;
			}
			}
		}
		}
		return false;


	}
	
	

	public boolean checkForPasswordExpiry(final Window loginDialog,
			final String userName, String message) {
		final EmployeeModel emp = (EmployeeModel) userService
				.getUserForUID(userName);
		final int validity_for_temp_password = configurationService.getConfiguration().getInt("cscockpit.login.tempPassword.validity");
		final int password_is_about_to_expire = configurationService.getConfiguration().getInt("cscockpit.login.tempPassword.abouttoexpire");
		if (emp != null && emp.getLastPasswordChanged() != null) {
			Calendar cal = Calendar.getInstance();
			Date today = cal.getTime();
			Calendar lastPasswordChangeCal = Calendar.getInstance();
			lastPasswordChangeCal.setTime(emp.getLastPasswordChanged());
			int days = Days.daysBetween(
					new DateTime(lastPasswordChangeCal.getTime()),
					new DateTime(today)).getDays();
			if (days <= validity_for_temp_password && days >= password_is_about_to_expire) {
				try {
					Messagebox.show(LabelUtils.getLabel(message, PASSWORD_IS_GOING_TO_EXPIRE, new Object[0]),LabelUtils.getLabel(message, PASSWORD_IS_GOING_TO_EXPIRE_TITLE, new Object[0]), Messagebox.OK, Messagebox.ERROR);
				} catch (InterruptedException e) {
					LOG.warn("Password is about to expire", e);
				}
				return true;
			}
			if (days > validity_for_temp_password ) {
				try {
					Messagebox.show(LabelUtils.getLabel(message, PASSWORD_HAS_EXPIRED, new Object[0]),LabelUtils.getLabel(message, PASSWORD_HAS_EXPIRED_TITLE, new Object[0]), Messagebox.OK, Messagebox.ERROR);
				} catch (InterruptedException e) {
					LOG.warn("Password has already expired", e);
				}
				return true;
			}
		}
		return false;
	}
	
	private boolean passwordValidator(final String password, boolean match)
	{
		if(password.matches(PASSWORD_PATTERN)) {
			match=true;
		}
		return match;

	}

	public boolean getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(boolean confirmPassword) {
		this.confirmPassword = confirmPassword;
	}





}
