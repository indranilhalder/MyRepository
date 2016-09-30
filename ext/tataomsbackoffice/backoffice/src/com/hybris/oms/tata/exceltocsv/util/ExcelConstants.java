package com.hybris.oms.tata.exceltocsv.util;

import de.hybris.platform.util.Config;


public class ExcelConstants
{
	public static final String EMPTY_SPACE = " ";
	public static final String ADD_COMMA = ",";
	public static final String AIR_PRIORITY1 = "P001,";
	public static final String AIR_PRIORITY2 = "P002,";
	public static final String AIR_PRIORITY3 = "P003,";
	public static final String AIR_PRIORITY4 = "P004,";
	public static final String AIR_PRIORITY5 = "P005,";
	public static final String SUR_PRIORITY1 = "P001,";
	public static final String SUR_PRIORITY2 = "P002,";
	public static final String SUR_PRIORITY3 = "P003,";
	// cod  air validation messages
	public static final String COD_PRIORITY1_VALIDATION_AIR = Config.getParameter("codPriority1ValidationAir");
	public static final String COD_PRIORITY2_VALIDATION_AIR = Config.getParameter("codPriority2ValidationAir");
	public static final String COD_PRIORITY1_NAME_VALIDATION_AIR = Config.getParameter("codPriority1NameValidationAir");
	public static final String COD_PRIORITY2_NAME_VALIDATION_AIR = Config.getParameter("codPriority2NameValidationAir");
	public static final String COD_PRIORITY3_NAME_VALIDATION_AIR = Config.getParameter("codPriority3NameValidationAir");
	public static final String COD_PRIORITY4_NAME_VALIDATION_AIR = Config.getParameter("codPriority4NameValidationAir");
	public static final String COD_PRIORITY5_NAME_VALIDATION_AIR = Config.getParameter("codPriority5NameValidationAir");
	public static final String COD_PRIORITY1AND2_SAME_AIR = Config.getParameter("codPriority1AndPriority2SameAir");
	public static final String COD_PRIORITY1_ISEMPTY_AIR = Config.getParameter("codPriority1IsEmptyAir");
	// prepaid  air validation messages
	public static final String PREPAID_PRIORITY1_VALIDATION_AIR = Config.getParameter("prepaidPriority1ValidationAir");
	public static final String PREPAID_PRIORITY2_VALIDATION_AIR = Config.getParameter("prepaidPriority2ValidationAir");
	public static final String PREPAID_PRIORITY1_NAME_VALIDATION_AIR = Config.getParameter("prepaidPriority1NameValidationAir");
	public static final String PREPAID_PRIORITY2_NAME_VALIDATION_AIR = Config.getParameter("prepaidPriority2NameValidationAir");
	public static final String PREPAID_PRIORITY3_NAME_VALIDATION_AIR = Config.getParameter("prepaidPriority3NameValidationAir");
	public static final String PREPAID_PRIORITY4_NAME_VALIDATION_AIR = Config.getParameter("prepaidPriority4NameValidationAir");
	public static final String PREPAID_PRIORITY5_NAME_VALIDATION_AIR = Config.getParameter("prepaidPriority5NameValidationAir");
	public static final String PREPAID_PRIORITY1AND2_SAME_AIR = Config.getParameter("prepaidPriority1AndPriority2SameAir");
	public static final String PREPAID_PRIORITY1_ISEMPTY_AIR = Config.getParameter("prepaidPriority1IsEmptyAir");

	// cod  surface validation messages
	public static final String COD_PRIORITY1_VALIDATION_SURFACE = Config.getParameter("codPriority1ValidationSurface");
	public static final String COD_PRIORITY2_VALIDATION_SURFACE = Config.getParameter("codPriority2ValidationSurface");
	public static final String COD_PRIORITY1_NAME_VALIDATION_SURFACE = Config.getParameter("codPriority1NameValidationSurface");
	public static final String COD_PRIORITY2_NAME_VALIDATION_SURFACE = Config.getParameter("codPriority2NameValidationSurface");
	public static final String COD_PRIORITY3_NAME_VALIDATION_SURFACE = Config.getParameter("codPriority3NameValidationSurface");
	public static final String COD_PRIORITY4_NAME_VALIDATION_SURFACE = Config.getParameter("codPriority4NameValidationSurface");
	public static final String COD_PRIORITY5_NAME_VALIDATION_SURFACE = Config.getParameter("codPriority5NameValidationSurface");
	public static final String COD_PRIORITY1AND2_SAME_SURFACE = Config.getParameter("codPriority1AndPriority2SameSurface");
	public static final String COD_PRIORITY1_ISEMPTY_SURFACE = Config.getParameter("codPriority1IsEmptySurface");
	//prepaid surface
	public static final String PREPAID_PRIORITY1_VALIDATION_SURFACE = Config.getParameter("prepaidPriority1ValidationSurface");
	public static final String PREPAID_PRIORITY2_VALIDATION_SURFACE = Config.getParameter("prepaidPriority2ValidationSurface");
	public static final String PREPAID_PRIORITY1_NAME_VALIDATION_SURFACE = Config
			.getParameter("prepaidPriority1NameValidationSurface");
	public static final String PREPAID_PRIORITY2_NAME_VALIDATION_SURFACE = Config
			.getParameter("prepaidPriority2NameValidationSurface");
	public static final String PREPAID_PRIORITY3_NAME_VALIDATION_SURFACE = Config
			.getParameter("prepaidPriority3NameValidationSurface");
	public static final String PREPAID_PRIORITY4_NAME_VALIDATION_SURFACE = Config
			.getParameter("prepaidPriority4NameValidationSurface");
	public static final String PREPAID_PRIORITY5_NAME_VALIDATION_SURFACE = Config
			.getParameter("prepaidPriority5NameValidationSurface");
	public static final String PREPAID_PRIORITY1AND2_SAME_SURFACE = Config.getParameter("prepaidPriority1AndPriority2SameSurface");
	public static final String PREPAID_PRIORITY1_ISEMPTY_SURFACE = Config.getParameter("prepaidPriority1IsEmptySurface");
	public static final String PREPAID_PRIORITY1_AIR_OR_SURFACE_ISEMPTY = Config
			.getParameter("prepaidPriority1AirOrSurfaceIsEmpty");

	//
	public static final String AIR_MODE = Config.getParameter("airMode");
	public static final String SURFACE_MODE = Config.getParameter("surfaceMode");

	public static final String CITY = Config.getParameter("city");
	public static final String STATE = Config.getParameter("state");

	public static final String STATE_VALIDATION = Config.getParameter("stateValidation");
	public static final String COD_COLUMN = Config.getParameter("codColumn");
	public static final String COD_COLUMN_VALIDATION = Config.getParameter("codColumnValidation");
	public static final String COD_LIMIT_MANDITORY = Config.getParameter("codLimitMandatory");
	public static final String PREPAID_LIMIT_MANDITORY = Config.getParameter("prepaidLimitMandatory");
	public static final String PINCODE = Config.getParameter("pincode");
	public static final String TRANSIT_TATCOD = Config.getParameter("transitTatCod");
	public static final String TRANSIT_TATPREPAID = Config.getParameter("transitTatPrepaid");
	public static final String ISRETURN_PINCODE = Config.getParameter("isReturnPincode");
	public static final String ISRETURN_SURFACE_PINCODE = Config.getParameter("surIsreturnPincode");
	public static final String PICKUP_VALIDATION = Config.getParameter("pickUpValidation");
	public static final String FORM_REQUIRED_VALIDATION = Config.getParameter("formRequiredValidation");
	//-Header Names for all the Logistic Partners
	public static final String BLUEDART_HEADER_NAME = Config.getParameter("bluedartHeaderName");
	public static final String ROADRUNNER_HEADER_NAME = Config.getParameter("roadrunnrHeaderName");
	public static final String GOJAVAS_HEADER_NAME = Config.getParameter("gojavasHeaderName");
	public static final String FEDEX_HEADER_NAME = Config.getParameter("fedexHeaderName");
	public static final String GATI_HEADER_NAME = Config.getParameter("gatiHeaderName");
	public static final String SHADOW_HEADER_NAME = Config.getParameter("shadowHeaderName");
	public static final String NUVOEX_HEADER_NAME = Config.getParameter("nuvoexHeaderName");
	public static final String ECOMEXPRESS_HEADER_NAME = Config.getParameter("ecomexpressHeaderName");
	public static final String DELHIVERY_HEADER_NAME = Config.getParameter("delhiveryHeaderName");
	public static final String EKART_HEADER_NAME = Config.getParameter("ekartHeaderName");
	//Ouput Names for all the five Logistic Partners
	public static final String BLUDART_OUTPUT_NAME = Config.getParameter("bluedartOutputName");
	public static final String ROADRUNNER_OUTPUT_NAME = Config.getParameter("roadrunnrOutputName");
	public static final String GOJAVAS_OUTPUT_NAME = Config.getParameter("gojavasOutputName");
	public static final String FEDEX_OUTPUT_NAME = Config.getParameter("fedexOutputName");
	public static final String GATI_OUTPUT_NAME = Config.getParameter("gatiOutputName");
	public static final String SHADOW_OUTPUT_NAME = Config.getParameter("shadowOutputName");
	public static final String NUVOEX_OUTPUT_NAME = Config.getParameter("nuvoexOutputName");
	public static final String ECOMEXPRESS_OUTPUT_NAME = Config.getParameter("ecomexpressOutputName");
	public static final String DELHIVERY_OUTPUT_NAME = Config.getParameter("delhiveryOutputName");
	public static final String EKART_OUTPUT_NAME = Config.getParameter("ekartOutputName");
	//Ouput Header Name for the Logistic Partner
	public static final String HEADER_COLUMNS = Config.getParameter("headerColumns");
	public static final String ROW_VALUE = Config.getParameter("rowValue");
	public static final String STATICDATA_HD = Config.getParameter("STATICDATAHD");
	public static final String STATICDATA_ED = Config.getParameter("STATICDATAED");
	//data mode ed or hd
	public static final String DATA_MODE_TYPE1 = Config.getParameter("dataModeType1");
	public static final String DATA_MODE_TYPE2 = Config.getParameter("dataModeType2");
	public static final String COUNTRY = Config.getParameter("country");
	public static final String NEW_LINE = "\n";
	public static final String TRANSPORT_MODE_TYPE = Config.getParameter("transportModeType");
	//surface cod column,surface prepaid limit column
	public static final String SURFACE_COD = Config.getParameter("surfaceCod");
	public static final String SURFACE_PREPAID_LIMIT = Config.getParameter("surfacePrepaidLimit");
	public static final String SURFACE_COD_LIMIT = Config.getParameter("surfaceCodLimit");
	//adding number of commas in  the begining of the file of csv file
	public static final String COMMAS_COUNTOF_BOF = Config.getParameter("commasCountOfBOF");
	//adding number of commas at the end of file of csv file
	public static final String COMMAS_COUNTOF_EOF = Config.getParameter("commasCountOfEOF");
	public static final String COD_COLUMN_INDEX = Config.getParameter("codColumnIndex");
	public static final String LAST_COLUMN_INDEX = Config.getParameter("lastColumnIndex");
	public static final String SURFACE_MODE_START_INDEX = Config.getParameter("surfaceModeStartingIndex");



}