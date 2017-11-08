<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>

<spring:url value="/my-account/profile" var="profileUrl" />
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />

<template:page pageTitle="${pageTitle}">
	<c:url var="mainUrl" value="/my-account/update-profile"></c:url>
	<c:if test="${not empty result && result eq 'failure'}">
		<div class="alert alert-danger">
			<a href="#nogo" onclick="changeUrl('${mainUrl}')" class="close"
				data-dismiss="alert" aria-label="close">&times;</a> <strong><b>ERROR:
			</b></strong>
			<spring:theme code="text.account.profile.duplicate.email.failure"
				text="An account already exists for this e-mail address" />
		</div>
	</c:if>
	<div class="account">
		<h1 class="account-header">
			<spring:theme code="text.account.headerTitle" text="My Tata CLiQ" />
			<user:accountMobileViewMenuDropdown pageNameDropdown="personalInfo" />
		</h1>

		<div class="wrapper">

			<!----- Left Navigation Starts --------->

			<user:accountLeftNav pageName="personalInfo" />
			<!----- Left Navigation ENDS --------->

			<div class="right-account">
				<div class="info">
					<%-- <h2><spring:theme code="profile.heading.text" text="My Profile"/></h2>		<!--  UF-249 text change -->
					<p><spring:theme code="profile.heading.deatils" text="Manage your account details including your name, contact number and password"/></p> --%>
					<div class="global-alerts myprofile" style="display: none;">
						<div class="alert alert-warning alert-dismissable">
							<ul id="myProfileError">
							</ul>
						</div>
					</div>
					<h2>
						<spring:theme code="profile.heading.text" text="My Profile" />
					</h2>
					<!--  UF-249 text change -->
					<p class="commonAccountPara">
						<spring:theme code="profile.heading.deatils"
							text="Manage your account details including your name, contact number and password" />
					</p>
					<%-- <form> --%>
					<fieldset>
						<form:form action="update-parsonal-detail" method="post"
							commandName="mplCustomerProfileForm"
							name="mplCustomerProfileForm" onSubmit="return validateForm();">
							<input type="hidden" name="isLux" value="${param.isLux}" />

							<div class="half quarter titleName">
								<formElement:formSelectBox idKey="profile.gender"
									labelKey="profile.gender" path="gender" mandatory="false"
									skipBlank="true" 
									items="${genderData}" selectCSSClass="form-control" selectedValue="MALE" />
							</div>
							<!-- TPR-6013 -->
							<div class="half quarter">
								<label class="firstNameHideVisibility"><spring:theme
										code="text.mplCustomerProfileForm.firstName" text="First Name" /></label>
								<!-- TPR-6013  -->
								<form:input path="firstName" id="profilefirstName"
									onkeypress="return kpressfn(event)" maxlength="40"
									placeholder="First Name" />
								<div class="errorMessage">
									<div id="errfn"></div>
								</div>
							</div>
							<!-- TPR-6013 -->

							<div class="half quarter lastNaming">
									<label class="lastNameHideVisibility"><spring:theme code="text.mplCustomerProfileForm.lastName" text="Last Name" /></label>
										<form:input path="lastName" id="profilelastName"
											onkeypress="return kpressln(event)" maxlength="40" placeholder="Last Name" />
										<div class="errorMessage"><div id="errln"></div></div>
									</div>		<!-- TPR-6013 -->
				
							<div class="half account-profileEmail quarter"><%-- <formElement:formInputBox idKey="profile.email" labelKey="profile.email" path="emailId" inputCSS="form-control" mandatory="false"/> --%>
									<label><spring:theme code="text.mplCustomerProfileForm.emailId" text="Email ID*" /></label>
										<form:input path="emailId" id="profileEmailID"
											onkeyup="kpressemail()" maxlength="240" />
										<div class="errorMessage"><div id="errEmail"></div></div>
							</div>							
							<!-- TPR-6013 -->

							<div class="full phone quarter">
								<label><spring:theme
										code="text.mplCustomerProfileForm.phoneNumber"
										text="Mobile Number" /></label>
								<div class="mobile_greyed">
									<select name="countryList" disabled="disabled">
										<option value="IN">+91</option>
									</select>
								</div>

								<div class="mobile_main">
									<form:input path="mobileNumber" id="profileMobileNumber"
										 maxlength="10" />
									<div class="errorMessage">
										<div id="errMob"></div>
									</div>
								</div>
							</div>

							<div class="select calender quarter birthday">
								<div class="half_span">
									<label><spring:theme code="profile.dateOfBirth"
											text="Date Of Birth" /></label>
									<c:choose>
										<c:when test="${not empty dobDay and dobDay ne '' and not empty dobMonth and dobMonth ne '' and not empty dobYear and dobYear ne ''}">
											<form:input path="dateOfBrithPicker"
											id="dateOfBrithPicker" maxlength="10"
											placeholder="Date Of Birth" type="text"
											value="${dobYear}-${dobMonth}-${dobDay}" />
										</c:when>
										<c:otherwise>
											<form:input path="dateOfBrithPicker"
											id="dateOfBrithPicker" maxlength="10"
											placeholder="Date Of Birth" type="text"
											value="" />
										</c:otherwise>
									</c:choose>
								</div>

								<div class="errorMessage">
									<div id="errdobDay"></div>
								</div>
							</div>

							<div class="select calender quarter anniversaryday">
								<div class="half_span">
									<label><spring:theme code="profile.dateOfAnniversary"
											text="Anniversary Date" /></label>
									<c:choose>
										<c:when test="${not empty doaDay and doaDay ne '' and not empty doaMonth and doaMonth  ne '' and not empty doaYear  and doaYear  ne ''}">
											<form:input path="anniversaryDatePicker"
											id="anniversaryDatePicker" maxlength="10"
											placeholder="Date Of Anniversary" type="text"
											value="${doaYear}-${doaMonth}-${doaDay}" />
										</c:when>
										<c:otherwise>
											<form:input path="anniversaryDatePicker"
											id="anniversaryDatePicker" maxlength="10"
											placeholder="Date Of Anniversary" type="text"
											value="" />
										</c:otherwise>
									</c:choose>
								</div>
								<div class="errorMessage">
									<div id="errdoaDay"></div>
								</div>
								<div class="errorMessage">
									<div id="errdata"></div>
								</div>
							</div>
							<button type="submit" class="blue">
								<spring:theme code="cart.modal.save.changes" text="Save Changes" />
							</button>

						</form:form>

					</fieldset>
					<%-- </form> --%>
				</div>

				<!-- End of Personal Info -->

				<!-- Update Password -->

				<div class="password">
					<h2><spring:theme code="profile.Password" text="Password"/></h2>	
					<p><spring:theme code="profile.Password.Details" text="Already set correctly"/></p>
					<button type="button" class="blue changePass">CHANGE PASSWORD</button>
					<button type="button" class="blue changePassResponsive">CHANGE</button>
					<button type="button" class="blue crossPass"></button>

					<div>
						<form:form id="frmUpdatePassword" action="update-password"
							method="post" commandName="updatePasswordForm" autocomplete="off">
							<fieldset>

								<div class="full span password-input quarter">
									<label><spring:theme
											code="text.mplCustomerProfileForm.CurrPwd"
											text="Current Password*" /></label>
									<!-- <input type="password" path="currentPassword" id="currentPassword"
										onkeypress="kpresscp()"	 maxlength="140" /> -->

									<form:password path="currentPassword"  onkeyup="kpresscp()" />

									<div class="errorMessage">
										<div id="errCurpwd"></div>
									</div>
								</div>


								<div class="half password-input quarter newPasswordSet">
									<label><spring:theme
											code="text.mplCustomerProfileForm.NewPwd"
											text="New Password*" /></label>
									<!-- <input type="password" path="newPassword" id="newPassword"
										onkeypress="kpressnp()"	 maxlength="140" /> -->
									<form:password path="newPassword" cssClass="password-strength" onkeyup="kpressnp()" />
									<div class="errorMessage">
										<div id="errNewpwd"></div>
									</div>
								</div>

								<div class="half password-input quarter confirmPassword">
									<label><spring:theme
											code="text.mplCustomerProfileForm.CnfNewPwd"
											text="Confirm New Password*" /></label>
									<!-- <input type="password"  path="checkNewPassword" id="checkNewPassword"
										onkeypress="kpresscnp()" maxlength="140" /> -->
									<form:password path="checkNewPassword" onkeyup="kpresscnp()" />
									<div class="errorMessage">
										<div id="errCnfNewpwd"></div>
									</div>
								</div>
							</fieldset>
							<button type=button class="blue"
								onClick="return validatePassword();">
								<spring:theme code="cart.modal.save.changes1" text="Save Changes" />
							</button>
						</form:form>

					</div>
				</div>

				<!-- End of Update Password -->

				<!-- Start of sign-out -->

				<div class="signOut">
					<h2><spring:theme code="profile.SignOut" text="Sign Out"/></h2>	
					<p><spring:theme code="profile.SignOut.details" text="This will sign you out of account linked with this profile from this session"/></p>
					<a href="<c:url value='/logout'/>" class="blue changePass">SIGN OUT</a>
					
				</div>
			</div>
		</div>
	</div>	
	<script>
		$(document).ready(function(){
			
		    var pickerAnni = new Pikaday({ 
		    field: document.getElementById('anniversaryDatePicker'),
		    format: 'YYYY-MM-DD',
		    firstDay: 1,
	        yearRange: [1950,2030],
		    onSelect: function() {
	        }
		    });
		    var pickerDob = new Pikaday({ 
			    field: document.getElementById('dateOfBrithPicker'),
			    format: 'YYYY-MM-DD',
			    firstDay: 1,
		        yearRange: [1950,2030],
			    onSelect: function() {
		        }
			    });
		    $('#profileMobileNumber').on('keyup keypress', function(ev) {
				var regex = /^[1-9]\d{0,9}$/g;
				var data= $.trim($(this).val());
				if(!regex.test(data))
				{
					event.preventDefault();	
				    $(this).val(data.slice(1));					                
				}
				});

		});
	   
	</script>
</template:page>

