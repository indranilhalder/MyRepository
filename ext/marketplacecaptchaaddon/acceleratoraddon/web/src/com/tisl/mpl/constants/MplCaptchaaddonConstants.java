package com.tisl.mpl.constants;

/**
 * @author TCS
 *
 */
public final class MplCaptchaaddonConstants
{
	public static final String CAPTCA_ENABLED_FOR_CURRENT_STORE = "captcaEnabledForCurrentStore";
	public static final String RECAPTCHA_PUBLIC_KEY = "recaptchaPublicKey";
	public static final String CHALLENGE = "challenge";
	public static final String RESPONSE = "response";
	public static final String RECAPTCHA_CHALLANGE_ANSWERED = "recaptchaChallangeAnswered";
	public static final String RECAPTCHA_CHALLENGE_FIELD_INVALID = "recaptcha.challenge.field.invalid";
	public static final String CHALLENGE_ANSWER_INVALID = "Challenge Answer is invalid.";

	public static final String LINK_LOGIN_CAPTCHA = "/login/captcha";

	public static final String LINK_WIDGETNAME = "/widget/{widgetName:.*}";
	public static final String LINK_ADDON_MARKETPLACECAPTCHAADDON_PAGES = "addon:/marketplacecaptchaaddon/pages/";
	public static final String LINK_SAVEDATA = "/savedata/{responseFieldValue:.*}";
	public static final String LINK_CHALLENGE = "/challenge/";
	public static final String LINK_CHALLENGEFIELDVALUE = "{challengeFieldValue:.*}";
	public static final String RESPONSE_FIELD_VALUE = "responseFieldValue";
	public static final String CHALLENGE_FIELD_VALUE = "challengeFieldValue";

	public static final String SUCCESS = "success";
	public static final String FAIL = "fail";

	public static final String part_PRIVATEKEY = "privatekey=";
	public static final String part_REMOTEIP = "&remoteip=";
	public static final String part_CHALLENGE = "&challenge=";
	public static final String part_RESPONSE = "&response=";
	public static final String MSG_NULL_READ_FROM_SERVER = "Null read from server";

	public static final String NEXT_LINE = "\r?\n";

	public static final String MSG_NO_ANS_FROM_CAPTCHA = "No answer returned from recaptcha: ";
	public static final String part_AMP_ERROR = "&amp;error=";
	public static final String MSG_RECAPTCHA_MISSING_ERROR_MSG = "recaptcha4j-missing-error-message";
	public static final String THEME = "theme";
	public static final String TABINDEX = "tabindex";


}
