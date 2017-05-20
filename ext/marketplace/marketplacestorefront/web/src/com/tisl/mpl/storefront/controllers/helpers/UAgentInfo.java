/**
 *
 */
package com.tisl.mpl.storefront.controllers.helpers;

/**
 * This class encapsulates information about a browser's connection to your web site. You can use it to find out whether
 * the browser asking for your site's content is probably running on a mobile device. The methods were written so you
 * can be as granular as you want. For example, enquiring whether it's as specific as an iPod Touch or as general as a
 * smartphone class device. The object's methods return true, or false.
 */
public class UAgentInfo
{

	// User-Agent and Accept HTTP request headers
	private String userAgent = "";
	private String httpAccept = "";

	// Let's store values for quickly accessing the same info multiple times.
	private boolean initCompleted = false;
	private boolean isWebkit = false; //Stores the result of DetectWebkit()
	private boolean isMobilePhone = false; //Stores the result of DetectMobileQuick()
	private boolean isIphone = false; //Stores the result of DetectIphone()
	private boolean isAndroid = false; //Stores the result of DetectAndroid()
	private boolean isAndroidPhone = false; //Stores the result of DetectAndroidPhone()
	private boolean isTierTablet = false; //Stores the result of DetectTierTablet()
	private boolean isTierIphone = false; //Stores the result of DetectTierIphone()
	private boolean isTierRichCss = false; //Stores the result of DetectTierRichCss()
	private boolean isTierGenericMobile = false; //Stores the result of DetectTierOtherPhones()

	// Initialize some initial smartphone string variables.
	private static final String engineWebKit = "webkit";

	private static final String deviceIphone = "iphone";
	private static final String deviceIpod = "ipod";
	private static final String deviceIpad = "ipad";
	private static final String deviceMacPpc = "macintosh"; //Used for disambiguation

	private static final String deviceAndroid = "android";
	private static final String deviceGoogleTV = "googletv";
	private static final String deviceHtcFlyer = "htc_flyer"; //HTC Flyer

	private static final String deviceWinPhone7 = "windows phone os 7";
	private static final String deviceWinPhone8 = "windows phone 8";
	private static final String deviceWinMob = "windows ce";
	private static final String deviceWindows = "windows";
	private static final String deviceIeMob = "iemobile";
	private static final String devicePpc = "ppc"; //Stands for PocketPC
	private static final String enginePie = "wm5 pie"; //An old Windows Mobile

	private static final String deviceBB = "blackberry";
	private static final String deviceBB10 = "bb10"; //For the new BB 10 OS
	private static final String vndRIM = "vnd.rim"; //Detectable when BB devices emulate IE or Firefox
	private static final String deviceBBStorm = "blackberry95"; //Storm 1 and 2
	private static final String deviceBBBold = "blackberry97"; //Bold 97x0 (non-touch)
	private static final String deviceBBBoldTouch = "blackberry 99"; //Bold 99x0 (touchscreen)
	private static final String deviceBBTour = "blackberry96"; //Tour
	private static final String deviceBBCurve = "blackberry89"; //Curve 2
	private static final String deviceBBCurveTouch = "blackberry 938"; //Curve Touch 9380
	private static final String deviceBBTorch = "blackberry 98"; //Torch
	private static final String deviceBBPlaybook = "playbook"; //PlayBook tablet

	private static final String deviceSymbian = "symbian";
	private static final String deviceS60 = "series60";
	private static final String deviceS70 = "series70";
	private static final String deviceS80 = "series80";
	private static final String deviceS90 = "series90";

	private static final String devicePalm = "palm";
	private static final String deviceWebOS = "webos"; //For Palm's line of WebOS devices
	private static final String deviceWebOShp = "hpwos"; //For HP's line of WebOS devices
	private static final String engineBlazer = "blazer"; //Old Palm
	private static final String engineXiino = "xiino"; //Another old Palm

	private static final String deviceNuvifone = "nuvifone"; //Garmin Nuvifone
	private static final String deviceBada = "bada"; //Samsung's Bada OS
	private static final String deviceTizen = "tizen"; //Tizen OS
	private static final String deviceMeego = "meego"; //Meego OS

	private static final String deviceKindle = "kindle"; //Amazon Kindle, eInk one
	private static final String engineSilk = "silk-accelerated"; //Amazon's accelerated Silk browser for Kindle Fire

	//Initialize variables for mobile-specific content.
	private static final String vndwap = "vnd.wap";
	private static final String wml = "wml";

	//Initialize variables for other random devices and mobile browsers.
	private static final String deviceTablet = "tablet"; //Generic term for slate and tablet devices
	private static final String deviceBrew = "brew";
	private static final String deviceDanger = "danger";
	private static final String deviceHiptop = "hiptop";
	private static final String devicePlaystation = "playstation";
	private static final String devicePlaystationVita = "vita";
	private static final String deviceNintendoDs = "nitro";
	private static final String deviceNintendo = "nintendo";
	private static final String deviceWii = "wii";
	private static final String deviceXbox = "xbox";
	private static final String deviceArchos = "archos";

	private static final String engineOpera = "opera"; //Popular browser
	private static final String engineNetfront = "netfront"; //Common embedded OS browser
	private static final String engineUpBrowser = "up.browser"; //common on some phones
	private static final String engineOpenWeb = "openweb"; //Transcoding by OpenWave server
	private static final String deviceMidp = "midp"; //a mobile Java technology
	private static final String uplink = "up.link";
	private static final String engineTelecaQ = "teleca q"; //a modern feature phone browser
	private static final String engineObigo = "obigo"; //W 10 is a modern feature phone browser

	private static final String devicePda = "pda"; //some devices report themselves as PDAs
	private static final String mini = "mini"; //Some mobile browsers put "mini" in their names.
	private static final String mobile = "mobile"; //Some mobile browsers put "mobile" in their user agent strings.
	private static final String mobi = "mobi"; //Some mobile browsers put "mobi" in their user agent strings.

	//Use Maemo, Tablet, and Linux to test for Nokia"s Internet Tablets.
	private static final String maemo = "maemo";
	private static final String linux = "linux";
	private static final String qtembedded = "qt embedded"; //for Sony Mylo
	private static final String mylocom2 = "com2"; //for Sony Mylo also

	//In some UserAgents, the only clue is the manufacturer.
	private static final String manuSonyEricsson = "sonyericsson";
	private static final String manuericsson = "ericsson";
	private static final String manuSamsung1 = "sec-sgh";
	private static final String manuSony = "sony";
	private static final String manuHtc = "htc";

	//In some UserAgents, the only clue is the operator.
	private static final String svcDocomo = "docomo";
	private static final String svcKddi = "kddi";
	private static final String svcVodafone = "vodafone";

	//Disambiguation strings.
	private static final String disUpdate = "update"; //pda vs. update


	/**
	 * Initialize the userAgent and httpAccept variables
	 *
	 * @param userAgent
	 *           the User-Agent header
	 * @param httpAccept
	 *           the Accept header
	 */
	public UAgentInfo(final String userAgent, final String httpAccept)
	{
		if (userAgent != null)
		{
			this.userAgent = userAgent.toLowerCase();
		}
		if (httpAccept != null)
		{
			this.httpAccept = httpAccept.toLowerCase();
		}

		//Intialize key stored values.
		initDeviceScan();
	}

	/**
	 * Return whether the device is an Iphone or iPod Touch
	 * 
	 * @return isIphone
	 */
	public boolean getIsIphone()
	{
		return isIphone;
	}

	/**
	 * Return whether the device is in the Tablet Tier.
	 * 
	 * @return isTierTablet
	 */
	public boolean getIsTierTablet()
	{
		return isTierTablet;
	}

	/**
	 * Return whether the device is in the Iphone Tier.
	 * 
	 * @return isTierIphone
	 */
	public boolean getIsTierIphone()
	{
		return isTierIphone;
	}

	/**
	 * Return whether the device is in the 'Rich CSS' tier of mobile devices.
	 * 
	 * @return isTierRichCss
	 */
	public boolean getIsTierRichCss()
	{
		return isTierRichCss;
	}

	/**
	 * Return whether the device is a generic, less-capable mobile device.
	 * 
	 * @return isTierGenericMobile
	 */
	public boolean getIsTierGenericMobile()
	{
		return isTierGenericMobile;
	}

	/**
	 * Initialize Key Stored Values.
	 */
	public void initDeviceScan()
	{
		//Save these properties to speed processing
		this.isWebkit = detectWebkit();
		this.isIphone = detectIphone();
		this.isAndroid = detectAndroid();
		this.isAndroidPhone = detectAndroidPhone();

		//Generally, these tiers are the most useful for web development
		this.isMobilePhone = detectMobileQuick();
		this.isTierTablet = detectTierTablet();
		this.isTierIphone = detectTierIphone();

		//Optional: Comment these out if you NEVER use them
		this.isTierRichCss = detectTierRichCss();
		this.isTierGenericMobile = detectTierOtherPhones();

		this.initCompleted = true;
	}

	/**
	 * Detects if the current device is an iPhone.
	 * 
	 * @return detection of an iPhone
	 */
	public boolean detectIphone()
	{
		if ((this.initCompleted) || (this.isIphone))
		{
			return this.isIphone;
		}

		// The iPad and iPod touch say they're an iPhone! So let's disambiguate.
		return userAgent.indexOf(deviceIphone) != -1 && !detectIpad() && !detectIpod();
	}

	/**
	 * Detects if the current device is an iPod Touch.
	 * 
	 * @return detection of an iPod Touch
	 */
	public boolean detectIpod()
	{
		return userAgent.indexOf(deviceIpod) != -1;
	}

	/**
	 * Detects if the current device is an iPad tablet.
	 * 
	 * @return detection of an iPad
	 */
	public boolean detectIpad()
	{
		return userAgent.indexOf(deviceIpad) != -1 && detectWebkit();
	}

	/**
	 * Detects if the current device is an iPhone or iPod Touch.
	 * 
	 * @return detection of an iPhone or iPod Touch
	 */
	public boolean detectIphoneOrIpod()
	{
		//We repeat the searches here because some iPods may report themselves as an iPhone, which would be okay.
		return userAgent.indexOf(deviceIphone) != -1 || userAgent.indexOf(deviceIpod) != -1;
	}

	/**
	 * Detects *any* iOS device: iPhone, iPod Touch, iPad.
	 * 
	 * @return detection of an Apple iOS device
	 */
	public boolean detectIos()
	{
		return detectIphoneOrIpod() || detectIpad();
	}


	/**
	 * Detects *any* Android OS-based device: phone, tablet, and multi-media player. Also detects Google TV.
	 * 
	 * @return detection of an Android device
	 */
	public boolean detectAndroid()
	{
		if ((this.initCompleted) || (this.isAndroid))
		{
			return this.isAndroid;
		}

		if ((userAgent.indexOf(deviceAndroid) != -1) || detectGoogleTV())
		{
			return true;
		}
		//Special check for the HTC Flyer 7" tablet. It should report here.
		return userAgent.indexOf(deviceHtcFlyer) != -1;
	}

	/**
	 * Detects if the current device is a (small-ish) Android OS-based device used for calling and/or multi-media (like a
	 * Samsung Galaxy Player). Google says these devices will have 'Android' AND 'mobile' in user agent. Ignores tablets
	 * (Honeycomb and later).
	 * 
	 * @return detection of an Android phone
	 */
	public boolean detectAndroidPhone()
	{
		if ((this.initCompleted) || (this.isAndroidPhone))
		{
			return this.isAndroidPhone;
		}

		if (detectAndroid() && (userAgent.indexOf(mobile) != -1))
		{
			return true;
		}
		//Special check for Android phones with Opera Mobile. They should report here.
		if (detectOperaAndroidPhone())
		{
			return true;
		}
		//Special check for the HTC Flyer 7" tablet. It should report here.
		return userAgent.indexOf(deviceHtcFlyer) != -1;
	}

	/**
	 * Detects if the current device is a (self-reported) Android tablet. Google says these devices will have 'Android'
	 * and NOT 'mobile' in their user agent.
	 * 
	 * @return detection of an Android tablet
	 */
	public boolean detectAndroidTablet()
	{
		//First, let's make sure we're on an Android device.
		if (!detectAndroid())
		{
			return false;
		}

		//Special check for Opera Android Phones. They should NOT report here.
		if (detectOperaMobile())
		{
			return false;
		}
		//Special check for the HTC Flyer 7" tablet. It should NOT report here.
		if (userAgent.indexOf(deviceHtcFlyer) != -1)
		{
			return false;
		}

		//Otherwise, if it's Android and does NOT have 'mobile' in it, Google says it's a tablet.
		return (userAgent.indexOf(mobile) <= -1);
	}

	/**
	 * Detects if the current device is an Android OS-based device and the browser is based on WebKit.
	 * 
	 * @return detection of an Android WebKit browser
	 */
	public boolean detectAndroidWebKit()
	{
		return detectAndroid() && detectWebkit();
	}

	/**
	 * Detects if the current device is a GoogleTV.
	 * 
	 * @return detection of GoogleTV
	 */
	public boolean detectGoogleTV()
	{
		return userAgent.indexOf(deviceGoogleTV) != -1;
	}

	/**
	 * Detects if the current browser is based on WebKit.
	 * 
	 * @return detection of a WebKit browser
	 */
	public boolean detectWebkit()
	{
		if ((this.initCompleted) || (this.isWebkit))
		{
			return this.isWebkit;
		}

		return userAgent.indexOf(engineWebKit) != -1;
	}


	/**
	 * Detects if the current browser is EITHER a Windows Phone 7.x OR 8 device
	 * 
	 * @return detection of Windows Phone 7.x OR 8
	 */
	public boolean detectWindowsPhone()
	{
		return detectWindowsPhone7() || detectWindowsPhone8();
	}

	/**
	 * Detects a Windows Phone 7 device (in mobile browsing mode).
	 * 
	 * @return detection of Windows Phone 7
	 */
	public boolean detectWindowsPhone7()
	{
		return userAgent.indexOf(deviceWinPhone7) != -1;
	}

	/**
	 * Detects a Windows Phone 8 device (in mobile browsing mode).
	 * 
	 * @return detection of Windows Phone 8
	 */
	public boolean detectWindowsPhone8()
	{
		return userAgent.indexOf(deviceWinPhone8) != -1;
	}

	/**
	 * Detects if the current browser is a Windows Mobile device. Excludes Windows Phone 7.x and 8 devices. Focuses on
	 * Windows Mobile 6.xx and earlier.
	 * 
	 * @return detection of Windows Mobile
	 */
	public boolean detectWindowsMobile()
	{
		if (detectWindowsPhone())
		{
			return false;
		}
		//Most devices use 'Windows CE', but some report 'iemobile'
		//  and some older ones report as 'PIE' for Pocket IE.
		//  We also look for instances of HTC and Windows for many of their WinMo devices.
		if (userAgent.indexOf(deviceWinMob) != -1 || userAgent.indexOf(deviceWinMob) != -1 || userAgent.indexOf(deviceIeMob) != -1
				|| userAgent.indexOf(enginePie) != -1 || (userAgent.indexOf(manuHtc) != -1 && userAgent.indexOf(deviceWindows) != -1)
				|| (detectWapWml() && userAgent.indexOf(deviceWindows) != -1))
		{
			return true;
		}

		//Test for Windows Mobile PPC but not old Macintosh PowerPC.
		return userAgent.indexOf(devicePpc) != -1 && userAgent.indexOf(deviceMacPpc) == -1;

	}

	/**
	 * Detects if the current browser is any BlackBerry. Includes BB10 OS, but excludes the PlayBook.
	 * 
	 * @return detection of Blackberry
	 */
	public boolean detectBlackBerry()
	{
		return userAgent.indexOf(deviceBB) != -1 || httpAccept.indexOf(vndRIM) != -1 || detectBlackBerry10Phone();

	}

	/**
	 * Detects if the current browser is a BlackBerry 10 OS phone. Excludes tablets.
	 * 
	 * @return detection of a Blackberry 10 device
	 */
	public boolean detectBlackBerry10Phone()
	{
		return userAgent.indexOf(deviceBB10) != -1 && userAgent.indexOf(mobile) != -1;
	}

	/**
	 * Detects if the current browser is on a BlackBerry tablet device. Example: PlayBook
	 * 
	 * @return detection of a Blackberry Tablet
	 */
	public boolean detectBlackBerryTablet()
	{
		return userAgent.indexOf(deviceBBPlaybook) != -1;
	}

	/**
	 * Detects if the current browser is a BlackBerry device AND uses a WebKit-based browser. These are signatures for
	 * the new BlackBerry OS 6. Examples: Torch. Includes the Playbook.
	 * 
	 * @return detection of a Blackberry device with WebKit browser
	 */
	public boolean detectBlackBerryWebKit()
	{
		return detectBlackBerry() && detectWebkit();
	}

	/**
	 * Detects if the current browser is a BlackBerry Touch device, such as the Storm, Torch, and Bold Touch. Excludes
	 * the Playbook.
	 * 
	 * @return detection of a Blackberry touchscreen device
	 */
	public boolean detectBlackBerryTouch()
	{
		return detectBlackBerry()
				&& (userAgent.indexOf(deviceBBStorm) != -1 || userAgent.indexOf(deviceBBTorch) != -1
						|| userAgent.indexOf(deviceBBBoldTouch) != -1 || userAgent.indexOf(deviceBBCurveTouch) != -1);
	}

	/**
	 * Detects if the current browser is a BlackBerry device AND has a more capable recent browser. Excludes the
	 * Playbook. Examples, Storm, Bold, Tour, Curve2 Excludes the new BlackBerry OS 6 and 7 browser!!
	 * 
	 * @return detection of a Blackberry device with a better browser
	 */
	public boolean detectBlackBerryHigh()
	{
		//Disambiguate for BlackBerry OS 6 or 7 (WebKit) browser
		return !detectBlackBerryWebKit()
				&& detectBlackBerry()
				&& (detectBlackBerryTouch() || userAgent.indexOf(deviceBBBold) != -1 || userAgent.indexOf(deviceBBTour) != -1 || userAgent
						.indexOf(deviceBBCurve) != -1);
	}

	/**
	 * Detects if the current browser is a BlackBerry device AND has an older, less capable browser. Examples: Pearl,
	 * 8800, Curve1
	 * 
	 * @return detection of a Blackberry device with a poorer browser
	 */
	public boolean detectBlackBerryLow()
	{
		//Assume that if it's not in the High tier, then it's Low
		return detectBlackBerry() && !(detectBlackBerryHigh() || detectBlackBerryWebKit());
	}

	/**
	 * Detects if the current browser is the Symbian S60 Open Source Browser.
	 * 
	 * @return detection of Symbian S60 Browser
	 */
	public boolean detectS60OssBrowser()
	{
		//First, test for WebKit, then make sure it's either Symbian or S60.
		return detectWebkit() && (userAgent.indexOf(deviceSymbian) != -1 || userAgent.indexOf(deviceS60) != -1);
	}

	/**
	 *
	 * Detects if the current device is any Symbian OS-based device, including older S60, Series 70, Series 80, Series
	 * 90, and UIQ, or other browsers running on these devices.
	 * 
	 * @return detection of SymbianOS
	 */
	public boolean detectSymbianOS()
	{
		return userAgent.indexOf(deviceSymbian) != -1 || userAgent.indexOf(deviceS60) != -1 || userAgent.indexOf(deviceS70) != -1
				|| userAgent.indexOf(deviceS80) != -1 || userAgent.indexOf(deviceS90) != -1;
	}

	/**
	 * Detects if the current browser is on a PalmOS device.
	 * 
	 * @return detection of a PalmOS device
	 */
	public boolean detectPalmOS()
	{
		//Make sure it's not WebOS first
		if (detectPalmWebOS())
		{
			return false;
		}

		//Most devices nowadays report as 'Palm', but some older ones reported as Blazer or Xiino.
		return userAgent.indexOf(devicePalm) != -1 || userAgent.indexOf(engineBlazer) != -1 || userAgent.indexOf(engineXiino) != -1;
	}

	/**
	 * Detects if the current browser is on a Palm device running the new WebOS.
	 * 
	 * @return detection of a Palm WebOS device
	 */
	public boolean detectPalmWebOS()
	{
		return userAgent.indexOf(deviceWebOS) != -1;
	}

	/**
	 * Detects if the current browser is on an HP tablet running WebOS.
	 * 
	 * @return detection of an HP WebOS tablet
	 */
	public boolean detectWebOSTablet()
	{
		return userAgent.indexOf(deviceWebOShp) != -1 && userAgent.indexOf(deviceTablet) != -1;
	}

	/**
	 * Detects Opera Mobile or Opera Mini.
	 * 
	 * @return detection of an Opera browser for a mobile device
	 */
	public boolean detectOperaMobile()
	{
		return userAgent.indexOf(engineOpera) != -1 && (userAgent.indexOf(mini) != -1 || userAgent.indexOf(mobi) != -1);
	}

	/**
	 * Detects Opera Mobile on an Android phone.
	 * 
	 * @return detection of an Opera browser on an Android phone
	 */
	public boolean detectOperaAndroidPhone()
	{
		return userAgent.indexOf(engineOpera) != -1 && (userAgent.indexOf(deviceAndroid) != -1 && userAgent.indexOf(mobi) != -1);
	}

	/**
	 * Detects Opera Mobile on an Android tablet.
	 * 
	 * @return detection of an Opera browser on an Android tablet
	 */
	public boolean detectOperaAndroidTablet()
	{
		return userAgent.indexOf(engineOpera) != -1
				&& (userAgent.indexOf(deviceAndroid) != -1 && userAgent.indexOf(deviceTablet) != -1);
	}

	/**
	 * Detects if the current device is an Amazon Kindle (eInk devices only). Note: For the Kindle Fire, use the normal
	 * Android methods.
	 * 
	 * @return detection of a Kindle
	 */
	public boolean detectKindle()
	{
		return userAgent.indexOf(deviceKindle) != -1 && !detectAndroid();
	}

	/**
	 * Detects if the current Amazon device is using the Silk Browser. Note: Typically used by the the Kindle Fire.
	 * 
	 * @return detection of an Amazon Kindle Fire in Silk mode.
	 */
	public boolean detectAmazonSilk()
	{
		return userAgent.indexOf(engineSilk) != -1;
	}

	/**
	 * Detects if the current browser is a Garmin Nuvifone.
	 * 
	 * @return detection of a Garmin Nuvifone
	 */
	public boolean detectGarminNuvifone()
	{
		return userAgent.indexOf(deviceNuvifone) != -1;
	}

	/**
	 * Detects a device running the Bada smartphone OS from Samsung.
	 * 
	 * @return detection of a Bada device
	 */
	public boolean detectBada()
	{
		return userAgent.indexOf(deviceBada) != -1;
	}

	/**
	 * Detects a device running the Tizen smartphone OS.
	 * 
	 * @return detection of a Tizen device
	 */
	public boolean detectTizen()
	{
		return userAgent.indexOf(deviceTizen) != -1;
	}

	/**
	 * Detects a device running the Meego OS.
	 * 
	 * @return detection of a Meego device
	 */
	public boolean detectMeego()
	{
		return userAgent.indexOf(deviceMeego) != -1;
	}

	/**
	 * Detects the Danger Hiptop device.
	 * 
	 * @return detection of a Danger Hiptop
	 */
	public boolean detectDangerHiptop()
	{
		return userAgent.indexOf(deviceDanger) != -1 || userAgent.indexOf(deviceHiptop) != -1;
	}

	/**
	 * Detects if the current browser is a Sony Mylo device.
	 * 
	 * @return detection of a Sony Mylo device
	 */
	public boolean detectSonyMylo()
	{
		return userAgent.indexOf(manuSony) != -1 && (userAgent.indexOf(qtembedded) != -1 || userAgent.indexOf(mylocom2) != -1);
	}

	/**
	 * Detects if the current device is on one of the Maemo-based Nokia Internet Tablets.
	 * 
	 * @return detection of a Maemo OS tablet
	 */
	public boolean detectMaemoTablet()
	{
		if (userAgent.indexOf(maemo) != -1)
		{
			return true;
		}
		else if (userAgent.indexOf(linux) != -1 && userAgent.indexOf(deviceTablet) != -1 && !detectWebOSTablet()
				&& !detectAndroid())
		{
			return true;
		}
		return false;
	}

	/**
	 * Detects if the current device is an Archos media player/Internet tablet.
	 * 
	 * @return detection of an Archos media player
	 */
	public boolean detectArchos()
	{
		return userAgent.indexOf(deviceArchos) != -1;
	}

	/**
	 * Detects if the current device is an Internet-capable game console.
	 * 
	 * @return detection of any Game Console
	 */
	public boolean detectGameConsole()
	{
		return detectSonyPlaystation() || detectNintendo() || detectXbox();
	}

	/**
	 * Detects if the current device is a Sony Playstation.
	 * 
	 * @return detection of Sony Playstation
	 */
	public boolean detectSonyPlaystation()
	{
		return userAgent.indexOf(devicePlaystation) != -1;
	}

	/**
	 * Detects if the current device is a handheld gaming device with a touchscreen and modern iPhone-class browser.
	 * Includes the Playstation Vita.
	 * 
	 * @return detection of a handheld gaming device
	 */
	public boolean detectGamingHandheld()
	{
		return (userAgent.indexOf(devicePlaystation) != -1) && (userAgent.indexOf(devicePlaystationVita) != -1);
	}

	/**
	 * Detects if the current device is a Nintendo game device.
	 * 
	 * @return detection of Nintendo
	 */
	public boolean detectNintendo()
	{
		return userAgent.indexOf(deviceNintendo) != -1 || userAgent.indexOf(deviceWii) != -1
				|| userAgent.indexOf(deviceNintendoDs) != -1;
	}

	/**
	 * Detects if the current device is a Microsoft Xbox.
	 * 
	 * @return detection of Xbox
	 */
	public boolean detectXbox()
	{
		return userAgent.indexOf(deviceXbox) != -1;
	}

	/**
	 * Detects whether the device is a Brew-powered device.
	 * 
	 * @return detection of a Brew device
	 */
	public boolean detectBrewDevice()
	{
		return userAgent.indexOf(deviceBrew) != -1;
	}

	/**
	 * Detects whether the device supports WAP or WML.
	 * 
	 * @return detection of a WAP- or WML-capable device
	 */
	public boolean detectWapWml()
	{
		return httpAccept.indexOf(vndwap) != -1 || httpAccept.indexOf(wml) != -1;
	}

	/**
	 * Detects if the current device supports MIDP, a mobile Java technology.
	 * 
	 * @return detection of a MIDP mobile Java-capable device
	 */
	public boolean detectMidpCapable()
	{
		return userAgent.indexOf(deviceMidp) != -1 || httpAccept.indexOf(deviceMidp) != -1;
	}

	//*****************************
	// Device Classes
	//*****************************

	/**
	 * Check to see whether the device is any device in the 'smartphone' category.
	 * 
	 * @return detection of a general smartphone device
	 */
	public boolean detectSmartphone()
	{
		//Exclude duplicates from TierIphone
		return (detectTierIphone() || detectS60OssBrowser() || detectSymbianOS() || detectWindowsMobile() || detectBlackBerry() || detectPalmOS());
	}

	/**
	 * Detects if the current device is a mobile device. This method catches most of the popular modern devices. Excludes
	 * Apple iPads and other modern tablets.
	 * 
	 * @return detection of any mobile device using the quicker method
	 */
	public boolean detectMobileQuick()
	{
		//Let's exclude tablets
		if (detectTierTablet())
		{
			return false;
		}

		if ((initCompleted) || (isMobilePhone))
		{
			return isMobilePhone;
		}

		//Most mobile browsing is done on smartphones
		if (detectSmartphone())
		{
			return true;
		}

		if (detectWapWml() || detectBrewDevice() || detectOperaMobile())
		{
			return true;
		}

		if ((userAgent.indexOf(engineObigo) != -1) || (userAgent.indexOf(engineNetfront) != -1)
				|| (userAgent.indexOf(engineUpBrowser) != -1) || (userAgent.indexOf(engineOpenWeb) != -1))
		{
			return true;
		}

		if (detectDangerHiptop() || detectMidpCapable() || detectMaemoTablet() || detectArchos())
		{
			return true;
		}

		if ((userAgent.indexOf(devicePda) != -1) && (userAgent.indexOf(disUpdate) < 0)) //no index found
		{
			return true;
		}
		if (userAgent.indexOf(mobile) != -1)
		{
			return true;
		}

		//We also look for Kindle devices
		return detectKindle() || detectAmazonSilk();

	}

	/**
	 * The longer and more thorough way to detect for a mobile device. Will probably detect most feature phones,
	 * smartphone-class devices, Internet Tablets, Internet-enabled game consoles, etc. This ought to catch a lot of the
	 * more obscure and older devices, also -- but no promises on thoroughness!
	 * 
	 * @return detection of any mobile device using the more thorough method
	 */
	public boolean detectMobileLong()
	{
		if (detectMobileQuick() || detectGameConsole() || detectSonyMylo())
		{
			return true;
		}

		//detect older phones from certain manufacturers and operators.
		if (userAgent.indexOf(uplink) != -1)
		{
			return true;
		}
		if (userAgent.indexOf(manuSonyEricsson) != -1)
		{
			return true;
		}
		return userAgent.indexOf(manuericsson) != -1 || userAgent.indexOf(manuSamsung1) != -1 || userAgent.indexOf(svcDocomo) != -1
				|| userAgent.indexOf(svcKddi) != -1 || userAgent.indexOf(svcVodafone) != -1;

	}

	//*****************************
	// For Mobile Web Site Design
	//*****************************

	/**
	 * The quick way to detect for a tier of devices. This method detects for the new generation of HTML 5 capable,
	 * larger screen tablets. Includes iPad, Android (e.g., Xoom), BB Playbook, WebOS, etc.
	 * 
	 * @return detection of any device in the Tablet Tier
	 */
	public boolean detectTierTablet()
	{
		if ((this.initCompleted) || (this.isTierTablet))
		{
			return this.isTierTablet;
		}

		return detectIpad() || detectAndroidTablet() || detectBlackBerryTablet() || detectWebOSTablet();
	}

	/**
	 * The quick way to detect for a tier of devices. This method detects for devices which can display iPhone-optimized
	 * web content. Includes iPhone, iPod Touch, Android, Windows Phone 7 and 8, BB10, WebOS, Playstation Vita, etc.
	 * 
	 * @return detection of any device in the iPhone/Android/Windows Phone/BlackBerry/WebOS Tier
	 */
	public boolean detectTierIphone()
	{
		if ((this.initCompleted) || (this.isTierIphone))
		{
			return this.isTierIphone;
		}

		return detectIphoneOrIpod() || detectAndroidPhone() || detectWindowsPhone() || detectBlackBerry10Phone()
				|| (detectBlackBerryWebKit() && detectBlackBerryTouch()) || detectPalmWebOS() || detectBada() || detectTizen()
				|| detectGamingHandheld();
	}

	/**
	 * The quick way to detect for a tier of devices. This method detects for devices which are likely to be capable of
	 * viewing CSS content optimized for the iPhone, but may not necessarily support JavaScript. Excludes all iPhone Tier
	 * devices.
	 * 
	 * @return detection of any device in the 'Rich CSS' Tier
	 */
	public boolean detectTierRichCss()
	{
		if ((this.initCompleted) || (this.isTierRichCss))
		{
			return this.isTierRichCss;
		}

		boolean result = false;

		//The following devices are explicitly ok.
		//Note: 'High' BlackBerry devices ONLY
		if (detectMobileQuick())
		{

			//Exclude iPhone Tier and e-Ink Kindle devices.
			if (!detectTierIphone() && !detectKindle())
			{

				//The following devices are explicitly ok.
				//Note: 'High' BlackBerry devices ONLY
				//Older Windows 'Mobile' isn't good enough for iPhone Tier.
				if (detectWebkit() || detectS60OssBrowser() || detectBlackBerryHigh() || detectWindowsMobile()
						|| userAgent.indexOf(engineTelecaQ) != -1)
				{
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * The quick way to detect for a tier of devices. This method detects for all other types of phones, but excludes the
	 * iPhone and RichCSS Tier devices.
	 * 
	 * @return detection of a mobile device in the less capable tier
	 */
	public boolean detectTierOtherPhones()
	{
		if ((this.initCompleted) || (this.isTierGenericMobile))
		{
			return this.isTierGenericMobile;
		}

		//Exclude devices in the other 2 categories
		return detectMobileLong() && !detectTierIphone() && !detectTierRichCss();

	}
}