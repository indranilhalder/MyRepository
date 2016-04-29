/**
 *
 */
package com.tisl.mpl.constants;

/**
 * @author TCS
 *
 */
public final class MplConstants
{
	/**
	 *
	 */
	private MplConstants()
	{
		//empty to avoid instantiating this constant class
	}

	//	RegisterCustomerFacadeImpl
	public static final String EXTENDED_USER_SERVICE = "extendedUserService";
	public static final String REGISTER_DATA = "registerData";
	public static final String FIELD_UID_CANNOT_BE_EMPTY = "The field [uid] cannot be empty";
	public static final String USER_ALREADY_REGISTERED = "User already registered";
	public static final String ASSERT_LOGIN_MSG = "The field [Login] cannot be empty";
	public static final String M1_CANNOT_REGISTER = "Cannot register user ";
	public static final String M2_ALREADY_EXISTS = ". It already exists for site ";
	public static final String M3_ALREADY_EXISTS = "User already registered";

	//	ExtendedUserServiceImpl
	public static final String M4_ASSERT_UID_NULL = "The given userID is null!";
	public static final String M5_CANNOT_FIND_USER = "The given userID is null!";

	//	ExtDefaultCustomerServiceImpl
	public static final String M6_ASSERT_EMAIL_NOT_NULL = "The field [newEmail] cannot be empty";


	public static final String M7_ASSERT_CURR_PWD_NOT_NULL = "The field [currentPassword] cannot be empty";
	public static final String M8_ASSERT_UID_NOT_NULL = "The field [newUid] cannot be empty";
	public static final String M9_USER_WITH_EMAIL = "User with email ";
	public static final String M10_ALREADY_EXIST = " already exists.";
	public static final String CUSTOMER_MODEL = "customerModel";
	public static final String TEMP_OTP = "temp@";

	//	MplCustomerProfileFacadeImpl
	public static final String SEND_UPDATE = "sendUpdate-";
	public static final String SEND_UPDATE_EMAIL_PROCESS = "sendUpdateEmailProcess";

	//	DefaultFriendsInviteService
	public static final String CUSTOMER_MUST_NOT_BE_NULL = "Customer must not be null!";
	public static final String AFFILIATE_ID_MUST_NOT_BE_NULL = "Affiliate Id must not be null!";
	public static final String FRIENDS_EMAIL_MUST_NOT_BE_NULL = "Friends email Id must not be null!";

	//Hot-Folder DataImport Price
	public static final char PLUS_CHAR = '+';
	public static final char SEQUENCE_CHAR = 'S';
	public static final String EMPTY_STRING = "";
	public static final char BRACKET_END = '}';
	public static final char BRACKET_START = '{';
	public static final Integer PRICE_COLUMN = Integer.valueOf(2);
	public static final String MESSAGE_ROW_SYNTAX_BRACKETS = "Invalid row syntax [brackets not closed]: ";
	public static final String MESSAGE_ROW_SYNTAX_COLUMNS = "Invalid row syntax [invalid column number]: ";
	public static final String MESSAGE_MISSING_VALUE = "Missing value for";
	public static final String MESSAGE_PRICE_ZERO = "Price is Zero or Less than Zero";
	public static final String M11_CANNOT_FIND_THE_UID = "The field [UID] cannot be empty";

	//ForgetPasswordServiceImpl
	public static final String FORGOTTEN_PASSWORD = "forgottenPassword-";
	public static final String FORGOTTEN_PASSWORD_EMAIL_PROCESS = "forgottenPasswordEmailProcess";
	public static final String CUSTOMERMODEL = "customerModel";
	public static final String HYPHEN = "-";

	//Search related Constants
	public static final String CATEGORY = "category";
	public static final String DEPARTMENT = "department";
	public static final String MPL_ENHANCED_SEARCHBOX = "MplEnhancedSearchBox";
	public static final String LEVEL_ZERO = ":L0:";
	public static final String LEVEL_ONE = ":L1:";
	public static final String LEVEL_THREE = ":L3:";
	public static final String FORWARD_SLASH = "/";
	public static final String COLON = ":";
	public static final String SALES_HIERARCHY_ROOT_CATEGORY_CODE = "MSH1";
	public static final String INSTOCKFLAG_QUERY_PATTERN = "inStockFlag:true";
	public static final String RELEVANCE = "relevance";
	public static final String SINGLE_QUOTES = "'";
	public static final String COMMA = ",";


	//MplVariantComparator
	public static final String DOUBLE = "\\D+";

	public static final String PIPE = "|";

	//for cnc if oms is down
	public static final String DEFAULT_CNC_INVENTORY_COUNT= "10";
	public static final String DEFAULT_CNC_NO_INVENTORY= "0";
	
	//	for wishlist
	public static class USER
	{
		public static final String ANONYMOUS_CUSTOMER = "anonymous";
	}

	public static final String MPL_WISHLIST_COMMENT = "New Wishlist";
}