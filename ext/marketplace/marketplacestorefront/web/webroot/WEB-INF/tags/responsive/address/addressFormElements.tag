<%@ attribute name="regions" required="true" type="java.util.List"%>
<%@ attribute name="country" required="false" type="java.lang.String"%>
<%@ attribute name="tabIndex" required="false" type="java.lang.Integer"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<!-- Move it to style.css before committing -->

<style>
	.address_landmarks, .address_landmarkOther, .address_landmarkOtherDiv {
		width: 100% !important;
		display: inline-block;
	}
	
	.address_landmarkOther, .address_landmarkOtherDiv label, .address_landmarkOtherDiv {
		display: inherit;
	}
	
	.address_landmarkOtherDiv {
		display: none;
	}
	
	.address_landmarkOtherDiv {
		margin: inherit;
	}
	
	.address_landmarkOtherDiv {
		margin: 0px !important;
	}
	
	
</style>


<c:choose>
	<c:when test="${country == 'US'}">
		<formElement:formSelectBox idKey="address.title"
			labelKey="address.title" path="titleCode" mandatory="true"
			skipBlank="false" skipBlankMessageKey="address.title.pleaseSelect"
			items="${titles}" selectedValue="${addressForm.titleCode}"
			selectCSSClass="form-control" />
		<formElement:formInputBox idKey="address.firstName"
			labelKey="address.firstName" path="firstName" inputCSS="form-control"
			mandatory="true" />
		<formElement:formInputBox idKey="address.surname"
			labelKey="address.surname" path="lastName" inputCSS="form-control"
			mandatory="true" />
		<formElement:formInputBox idKey="address.line1"
			labelKey="address.line1" path="line1" inputCSS="form-control"
			mandatory="true" />
		<formElement:formInputBox idKey="address.line2"
			labelKey="address.line2" path="line2" inputCSS="form-control"
			mandatory="false" />
		<%-- <formElement:formInputBox idKey="address.line3" labelKey="address.line3" path="line3" inputCSS="form-control" mandatory="true" />
		<formElement:formInputBox idKey="address.locality" labelKey="address.locality" path="locality" inputCSS="form-control" mandatory="true"/> --%>
		<formElement:formInputBox idKey="address.townCity"
			labelKey="address.townCity" path="townCity" inputCSS="form-control"
			mandatory="true" />
		<formElement:formSelectBox idKey="address.region"
			labelKey="address.state" path="regionIso" mandatory="true"
			skipBlank="false" skipBlankMessageKey="address.state"
			items="${regions}"
			itemValue="${useShortRegionIso ? 'isocodeShort' : 'isocode'}"
			selectedValue="${addressForm.regionIso}"
			selectCSSClass="form-control" />
		<formElement:formInputBox idKey="address.postcode"
			labelKey="address.zipcode" path="postcode" inputCSS="form-control"
			mandatory="true" />
	</c:when>
	<c:when test="${country == 'CA'}">
		<formElement:formSelectBox idKey="address.title"
			labelKey="address.title" path="titleCode" mandatory="true"
			skipBlank="false" skipBlankMessageKey="address.title.pleaseSelect"
			items="${titles}" selectedValue="${addressForm.titleCode}"
			selectCSSClass="form-control" />
		<formElement:formInputBox idKey="address.firstName"
			labelKey="address.firstName" path="firstName" inputCSS="form-control"
			mandatory="true" />
		<formElement:formInputBox idKey="address.surname"
			labelKey="address.surname" path="lastName" inputCSS="form-control"
			mandatory="true" />
		<formElement:formInputBox idKey="address.line1"
			labelKey="address.line1" path="line1" inputCSS="form-control"
			mandatory="true" />
		<formElement:formInputBox idKey="address.line2"
			labelKey="address.line2" path="line2" inputCSS="form-control"
			mandatory="false" />
		<%-- <formElement:formInputBox idKey="address.line3" labelKey="address.line3" path="line3" inputCSS="form-control" mandatory="true" />
		<formElement:formInputBox idKey="address.locality" labelKey="address.locality" path="locality" inputCSS="form-control" mandatory="true"/> --%>
		<formElement:formInputBox idKey="address.townCity"
			labelKey="address.townCity" path="townCity" inputCSS="form-control"
			mandatory="true" />
		<formElement:formSelectBox idKey="address.region"
			labelKey="address.province" path="regionIso" mandatory="true"
			skipBlank="false" skipBlankMessageKey="address.state"
			items="${regions}"
			itemValue="${useShortRegionIso ? 'isocodeShort' : 'isocode'}"
			selectedValue="${addressForm.regionIso}"
			selectCSSClass="form-control" />
		<formElement:formInputBox idKey="address.postcode"
			labelKey="address.postalcode" path="postcode" inputCSS="form-control"
			mandatory="true" />
	</c:when>
	<c:when test="${country == 'CN'}">
		<formElement:formInputBox idKey="address.postcode"
			labelKey="address.postalcode" path="postcode" inputCSS="form-control"
			mandatory="true" />
		<formElement:formSelectBox idKey="address.region"
			labelKey="address.province" path="regionIso" mandatory="true"
			skipBlank="false" skipBlankMessageKey="address.selectProvince"
			items="${regions}"
			itemValue="${useShortRegionIso ? 'isocodeShort' : 'isocode'}"
			selectedValue="${addressForm.regionIso}"
			selectCSSClass="form-control" />
		<formElement:formInputBox idKey="address.townCity"
			labelKey="address.townCity" path="townCity" inputCSS="form-control"
			mandatory="true" />
		<formElement:formInputBox idKey="address.line1"
			labelKey="address.street" path="line1" inputCSS="form-control"
			mandatory="true" />
		<formElement:formInputBox idKey="address.line2"
			labelKey="address.building" path="line2" inputCSS="form-control"
			mandatory="false" />
		<%-- <formElement:formInputBox idKey="address.line3" labelKey="address.line3" path="line3" inputCSS="form-control" mandatory="true" />
		<formElement:formInputBox idKey="address.locality" labelKey="address.locality" path="locality" inputCSS="form-control" mandatory="true"/> --%>
		<formElement:formInputBox idKey="address.surname"
			labelKey="address.surname" path="lastName" inputCSS="form-control"
			mandatory="true" />
		<formElement:formInputBox idKey="address.firstName"
			labelKey="address.firstName" path="firstName" inputCSS="form-control"
			mandatory="true" />
		<formElement:formSelectBox idKey="address.title"
			labelKey="address.title" path="titleCode" mandatory="true"
			skipBlank="false" skipBlankMessageKey="address.title.pleaseSelect"
			items="${titles}" selectedValue="${addressForm.titleCode}"
			selectCSSClass="form-control" />
	</c:when>
	<c:when test="${country == 'JP'}">
		<formElement:formSelectBox idKey="address.title"
			labelKey="address.title" path="titleCode" mandatory="true"
			skipBlank="false" skipBlankMessageKey="address.title.pleaseSelect"
			items="${titles}" selectedValue="${addressForm.titleCode}"
			selectCSSClass="form-control" />
		<formElement:formInputBox idKey="address.surname"
			labelKey="address.surname" path="lastName" inputCSS="form-control"
			mandatory="true" />
		<formElement:formInputBox idKey="address.firstName"
			labelKey="address.firstName" path="firstName" inputCSS="form-control"
			mandatory="true" />
		<formElement:formInputBox idKey="address.line1"
			labelKey="address.furtherSubarea" path="line1"
			inputCSS="form-control" mandatory="true" />
		<formElement:formInputBox idKey="address.line2"
			labelKey="address.subarea" path="line2" inputCSS="form-control"
			mandatory="true" />
		<%-- <formElement:formInputBox idKey="address.line3" labelKey="address.line3" path="line3" inputCSS="form-control" mandatory="true" />
		<formElement:formInputBox idKey="address.locality" labelKey="address.locality" path="locality" inputCSS="form-control" mandatory="true"/> --%>
		<formElement:formInputBox idKey="address.townCity"
			labelKey="address.townJP" path="townCity" inputCSS="form-control"
			mandatory="true" />
		<formElement:formSelectBox idKey="address.region"
			labelKey="address.prefecture" path="regionIso" mandatory="true"
			skipBlank="false" skipBlankMessageKey="address.selectPrefecture"
			items="${regions}"
			itemValue="${useShortRegionIso ? 'isocodeShort' : 'isocode'}"
			selectedValue="${addressForm.regionIso}"
			selectCSSClass="form-control" />
		<formElement:formInputBox idKey="address.postalcode"
			labelKey="address.postcode" path="postcode" inputCSS="form-control"
			mandatory="true" />

	</c:when>
	<c:otherwise>
		<%-- <formElement:formSelectBox idKey="address.title" labelKey="address.title" path="titleCode" mandatory="true" skipBlank="false" skipBlankMessageKey="address.title.pleaseSelect" items="${titles}" selectedValue="${addressForm.titleCode}" selectCSSClass="form-control"/> --%>
		<%-- <c:forEach items="${addressType}" var="type"> --%>
		<%-- <label  class="asd" for="${type}"></label><form:radiobutton path="addressType" value="${type}" /> --%>
		
		
		<input type="radio" name="addressType" id="new-address-option-1" value="Home"  checked="checked"/>
		<label class="residential" for="new-address-option-1"><spring:theme code="text.addressBook.Residentialaddress" text="Residential Address" /></label>
		<input type="radio" id="new-address-option-2" value="Work" name="addressType" />
		<label class="commercial" for="new-address-option-2"><spring:theme code="text.addressBook.commercialaddress" text="Commercial Address" /></label>
		<fieldset> 
		<div class='half'>
		
		<formElement:formInputBox idKey="address.firstName"
			labelKey="address.firstName" path="firstName" 
			mandatory="true" maxLength="40"/>
			<div class="help-block has-error" id="firstnameError" style="display: none;">		
			</div>	
			</div>
			<div class='half'>
		<formElement:formInputBox idKey="address.surname"
			labelKey="address.surname" path="lastName" 
			mandatory="true" maxLength="40"/>
			<div class="help-block has-error" id="lastnameError" style="display: none;">		
			</div>
			</div>
			<div class='full'>
		<formElement:formInputBox idKey="address.postcode" inputCSS="address_postcode"
			labelKey="address.postcode" path="postcode"
			mandatory="true" maxLength="6" />
		<div class="help-block has-error" id="addressPincodeServicableDiv"
			style="display: none;"></div>
			<div class="help-block has-error" id="pincodeError" style="display: none;">
			</div>
			</div>
			<div class='full'>
			<!-- TISUAT-4696  /TPR-215-->
		<formElement:formInputBox idKey="address.line1"
			labelKey="address.line1" path="line1"
			mandatory="true" maxLength="40"/>
			 <div class="help-block has-error" id="address1Error" style="display: none;">
			</div>
			</div>
			<div class='full'>
			<!-- TISUAT-4696  /TPR-215-->
		<formElement:formInputBox idKey="address.line2"
			labelKey="address.line2" path="line2" 
			mandatory="true" maxLength="40"/>
			 <div class="help-block has-error" id="address2Error" style="display: none;">
			</div>
			</div>
			<div class='full'>
			<!-- TISUAT-4696  /TPR-215-->
		<formElement:formInputBox idKey="address.line3"
			labelKey="address.line3" path="line3" 
			mandatory="true" maxLength="40" />
			 <div class="help-block has-error" id="address3Error" style="display: none;">
			</div>
			</div>
			
			<div class='half'>
				<div class="optionsLandmark">
					<formElement:formSelectBox  idKey="address.landmarks" selectCSSClass="address_landmarks"
						labelKey="Landmarks" path="landmark" mandatory="true"
						skipBlank="false" selectedValue="${addressForm.landmark}" skipBlankMessageKey="address.state.pleaseSelect"
						items="${abc}"
						itemValue="name"  />
				</div>
			</div>
			<div class='half'>
			
				<div class='half address_landmarkOtherDiv' data-value="${addressForm.landmark}">
						<formElement:formInputBox inputCSS="address_landmarkOther" idKey="otherLandmark"
							labelKey="Enter Nearest Landmark" path="otherLandmark"
							maxLength="30" />
							<div class="error_text otherLandMarkError"></div>
				</div>
			</div>
			
			
		<%-- <formElement:formInputBox idKey="address.locality" labelKey="address.locality" path="locality" inputCSS="form-control" mandatory="true"/> --%>
		<div class='half'>
		<!-- TISUAT-4696  /TPR-215-->
		<formElement:formInputBox idKey="address.townCity" inputCSS="address_townCity"
			labelKey="address.townCity" path="townCity" 
			mandatory="true" maxLength="40"/>
			<div class="help-block has-error" id="cityError" style="display: none;">
			</div>
			</div>
			<div class="half address-select">
				<div class="mainDrop">
				<formElement:formSelectBox idKey="address.states"
					selectCSSClass="address_states addressRead" labelKey="address.states"
					path="state" mandatory="true" skipBlank="false"
					skipBlankMessageKey="address.state.pleaseSelect"
					items="${stateDataList}" selectedValue="${addressForm.state}"
					itemValue="name" />
				<div class="help-block has-error" id="stateError"
					style="display: none;"></div>
				</div>
				
					<div class="dupDisplay">
					<label>State *</label>
					<div class="stateInput"></div>
						<div class="help-block has-error" id="stateError"
					style="display: none;"></div>
					</div>

			<%-- <div class="dupDisplay">
				
					<form:input path="state" id="address.statesReadOnly"
						class="address_states addressDup" maxlength="30" readonly="readonly"/>
					<div class="errorMessage">
							<div id="erraddressCity"></div>
					</div>
			</div> --%>
			</div>
				   		
		<div class="half country-select">
		<c:set var='count'  value='1' />
		<div class="country">
		<label><spring:theme code="address.selectCountry"/></label>
		
		    	<c:forEach items="${supportedCountries}" var="country">
					<c:if test="${country.isocode eq 'IN' and count==1}">
						<c:set var='countyName'  value='${country.name}' />
						<c:set var='countryIsoCode'  value='${country.isocode}' />
					</c:if>
				</c:forEach>
				<input type="text" id="country" value='${countyName}'  readonly /> 
			   	<input type="hidden" id="country"  name="countryIso" value="${countryIsoCode}"/>
			</div>
		</div>
				
		<%-- <formElement:formInputBox idKey="address.states" labelKey="address.states" path="state" inputCSS="form-control" mandatory="true"/> --%>

         <div class='half'>
          <label for="myselect"><spring:theme code="text.addressBook.PhoneNumber" text="Mobile Number" /></label>		
      <div class="showPhone">
		<select name="myselect" disabled="disabled">
       <option value="myselectedvalue" selected="selected">+91</option>
     </select>
        <input type="hidden" name="myselect" value="myselectedvalue" /> 
	 	<form:input type="text" idKey="address.mobile" id="mobileNonewForm"
			labelKey="address.mobile" path="MobileNo" inputCSS="form-control"
			mandatory="true" maxLength="10"/> 
			<div class="help-block has-error" id="mobileError" style="display: none;">		
			</div>	 
		</div>	
		</div>
			</fieldset>						
			

		<%-- <div>
			<strong><spring:theme code="address.mobile" text="Email Address"/></strong>	
				<div class="mobile_greyed">
					<form:input type="text" value="+91" id="myInput" inputCSS="form-text" path="MobileNo" 
					style="float: left; border:1px solid #ccc;border-radius:5px; height: 2.5em; width:10%;padding: 12px;" disabled="true"/>
				</div>
				<div class="mobile_main">
					<form:input type="text" id="myInput" inputCSS="form-text" path="MobileNo" 
					style="float: left; border:1px solid #ccc;border-radius:5px; height: 2.5em; width:35%; padding: 12px;"/>
				</div>	
		</div> --%>

	</c:otherwise>
</c:choose>
<script>
$(document).ready(function(){
	 var value = $(".address_landmarkOtherDiv").attr("data-value");
	  
	  setTimeout(function(){
	  if($(".address_landmarks option[value='"+value+"']").length > "0") {
		  
		  //alert(value+ " 2 in if x"+$(".address_landmarks option[value='"+value+"']").val());
			  $(".address_landmarks").val("");
			$(".address_landmarks option[value='"+value+"']").prop("selected",true);
			
			} else {
			//alert(value+ " 3 in else");
			  $(".address_landmarks").val("Other"); 
				changeFuncLandMark("Other"); 
			$(".address_landmarkOther").val(value);
			
		}
	  
	  });
	
});
</script>


