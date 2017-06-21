<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/user"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>


<spring:url value="/my-account/profile" var="profileUrl"/>
<spring:url value="/my-account/update-profile" var="updateProfileUrl"/>
<spring:url value="/my-account/update-password" var="updatePasswordUrl"/>
<spring:url value="/my-account/update-email" var="updateEmailUrl"/>
<spring:url value="/my-account/address-book" var="addressBookUrl"/>
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl"/>
<spring:url value="/my-account/orders" var="ordersUrl"/>
<spring:url value="/my-account/default/wishList" var="wishlistUrl"/>
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl"/>

<template:page pageTitle="${pageTitle}">
		<c:url var="mainUrl" value="/my-account/update-profile"></c:url>
		<c:if test="${not empty result && result eq 'failure'}">
			<div class="alert alert-danger">
				<a href="#nogo" onclick="changeUrl('${mainUrl}')" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    		<strong><b>ERROR: </b></strong><spring:theme code="text.account.profile.duplicate.email.failure" text="An account already exists for this e-mail address"/>
  			</div>
		</c:if>
<div class="account">
  <%-- <h1 class="account-header"><spring:theme code="text.account.headerTitle" text="My Tata CLiQ" /> 
 <div class="luxury-mobile-myaccount visible-xs">
  <select class="menu-select" onchange="window.location=this.options[this.selectedIndex].value;">
          <optgroup label="<spring:theme code="header.flyout.myaccount" />">
                  <option value=/store/mpl/en/my-account/ data-href="/store/mpl/en/my-account/"><spring:theme code="header.flyout.overview" /></option>
                  <option value=/store/mpl/en/my-account/marketplace-preference data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme code="header.flyout.marketplacepreferences" /></option> 
                  <option value=/store/mpl/en/my-account/update-profile data-href="account-info.php"  selected><spring:theme code="header.flyout.Personal" /> </option>
                  <option value=/store/mpl/en/my-account/orders data-href="order-history.php"> <spring:theme code="header.flyout.orders" /></option>
                  <option value=/store/mpl/en/my-account/payment-details data-href="account-cards.php"><spring:theme code="header.flyout.cards" /></option>
                  <option value=/store/mpl/en/my-account/address-book data-href="account-addresses.php"><spring:theme code="header.flyout.address" /></option>
              </optgroup>
         
        	 <optgroup label="Share">
                  <option value=/store/mpl/en/my-account/friendsInvite data-href="account-invite.php"><spring:theme code="header.flyout.invite" /></option>
              </optgroup> 
      </select>
     </div>
</h1>--%>
<div class="luxury-mobile-myaccount visible-xs">
  <select class="menu-select" onchange="window.location=this.options[this.selectedIndex].value;">
          <optgroup label="<spring:theme code="header.flyout.myaccount" />">
                  <option value=/store/mpl/en/my-account/ data-href="/store/mpl/en/my-account/"><spring:theme code="header.flyout.overview" /></option>
                  <%-- <option value=/store/mpl/en/my-account/marketplace-preference data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme code="header.flyout.marketplacepreferences" /></option> --%>
                  <option value=/store/mpl/en/my-account/update-profile data-href="account-info.php"  selected><spring:theme code="header.flyout.Personal" /> </option>
                  <option value=/store/mpl/en/my-account/orders data-href="order-history.php"> <spring:theme code="header.flyout.orders" /></option>
                  <option value=/store/mpl/en/my-account/payment-details data-href="account-cards.php"><spring:theme code="header.flyout.cards" /></option>
                  <option value=/store/mpl/en/my-account/address-book data-href="account-addresses.php"><spring:theme code="header.flyout.address" /></option>
              </optgroup>
         
         <%--  <optgroup label="Share">
                  <option value=/store/mpl/en/my-account/friendsInvite data-href="account-invite.php"><spring:theme code="header.flyout.invite" /></option>
              </optgroup> --%>
      </select>
     </div>
<div class="wrapper">		
		
	<!----- Left Navigation Starts --------->

			<user:accountLeftNav pageName="personalInfo"/>
			<!----- Left Navigation ENDS --------->
			
			<div class="right-account">
				<h2><spring:theme code="profile.heading.text" text="Personal Information"/></h2>
				<div class="info">
					
					<p><spring:theme code="profile.heading.deatils" text="Change your name, email, phone number here. "/></p>
					<%-- <form> --%>
					<fieldset>
						<form:form action="update-parsonal-detail" method="post" commandName="mplCustomerProfileForm" name="mplCustomerProfileForm" onSubmit="return validateForm();" class="clarfix">
							 <input type="hidden" name="isLux" value="${param.isLux}"/>
							 <div class="half halfwidth">
									<%-- <label><spring:theme code="text.mplCustomerProfileForm.firstName" text="First Name" /></label> --%>
										<form:input path="firstName" id="profilefirstName"
										onkeyup="kpressfn()" maxlength="40" placeholder="First Name"/>
									<div class="errorMessage"><div id="errfn"></div></div>
									</div> 
									
				
				
			
							<div class="half halfwidth">
									<%-- <label><spring:theme code="text.mplCustomerProfileForm.lastName" text="Last Name" /></label> --%>
										<form:input path="lastName" id="profilelastName"
											onkeyup="kpressln()" maxlength="40" placeholder="Last Name"/>
										<div class="errorMessage"><div id="errln"></div></div>
									</div>
				
							<div class="half halfwidth account-profileEmail"><%-- <formElement:formInputBox idKey="profile.email" labelKey="profile.email" path="emailId" inputCSS="form-control" mandatory="false"/> --%>
									<%-- <label><spring:theme code="text.mplCustomerProfileForm.emailId" text="E-mail Address*" /></label> --%>
										<form:input path="emailId" id="profileEmailID"
											onkeyup="kpressemail()" maxlength="240" placeholder="E-mail Address"/>
										<div class="errorMessage"><div id="errEmail"></div></div>
							</div>							
							
							<!-- <div class="full">
    							<label>Email Address *</label>
    							<input type="text">
    				        </div> -->
    				        <div class="full phone halfwidth">
							<%-- <label><spring:theme code="text.mplCustomerProfileForm.phoneNumber" text="Mobile Number"/></label> --%>	
								<%-- <div class="mobile_greyed"  >
									<form:input type="text" value="+91" id="myInput" inputCSS="form-text" path="mobileNumber" disabled="true"/>
									<select name="countryList"
											disabled="disabled">
										
													<option value="${country.name}">${country.name}</option>
												<option value="IN">+91</option>
											
										</select>
								</div> --%>
								
								 <div class="mobile_main">
									<%-- <form:input type="tel" id="mobileNo" inputCSS="form-text" path="mobileNumber" onblur="validateForm()"
												onkeypress="kpressmob()" maxlength="10"/>  --%>
										<form:input path="mobileNumber" id="profileMobileNumber"
											onkeyup="kpressmob()" maxlength="10" />
									<div class="errorMessage"><div id="errMob"></div></div>			
								</div>
									
							</div>
							
							
							
							     	 <div class="select gender  le-radio clearfix">
					        	<c:forEach items="${genderData}" var="varGender" varStatus="i">
					        	<div class="le-radio halfwidth">
						        	 <form:radiobutton path="gender" value="${varGender.code}"/>
						             <label for="gender${i.count}">${varGender.name}</label>
					             </div>
					            </c:forEach>
					        </div>
					        
					        
					        
							
							<div class="select calender halfwidth select-bar" >
							    <%--  <label><spring:theme code="profile.dateOfBirth"
											text="Date Of Birth" /></label>	 --%>
								 <div class="half_span">
								 <%--<formElement:formSelectBox idKey="profile.dateOfBirth" labelKey="profile.dateOfBirth" path="dateOfBirthDay" mandatory="false" skipBlank="false" 
							       skipBlankMessageKey="profile.select.day" items="${dayList}" selectCSSClass="form-control"/></div> --%>
							     
							     
											
									<form:select name="dateList" id="dateOfBirth" path="dateOfBirthDay" onchange="selectBoxChange();">
										<option value="selectDay">Date</option>
										<c:forEach items="${dayList}" var="day"
											varStatus="dayStatus">
											<option value="${day.name}"
												<c:if test="${not empty dobDay && dobDay eq day.name}">
													selected="selected"
												</c:if>
											>${day.name}</option>
										</c:forEach>
									</form:select>
								
									</div>
								<div class="half_span label_disabled">
								<%-- <formElement:formSelectBox idKey="profile.blank" labelKey="profile.blank" path="dateOfBirthMonth" mandatory="false" skipBlank="false" 
							       skipBlankMessageKey="profile.select.month" items="${monthList}" selectCSSClass="form-control"/></div>	 --%>
							       
									<form:select name="monthList" id="monthOfBirth" path="dateOfBirthMonth" onchange="selectBoxChange();">
										<option value="selectmonth">Month</option>
										<c:forEach items="${monthList}" var="month"
											varStatus="monthStatus">
											<option value="${month.code}"
												<c:if test="${not empty dobMonth && dobMonth eq month.code}">
													selected="selected"
												</c:if>
											>${month.name}</option>
										</c:forEach>
									</form:select>
								</div>
							 			
								<div class="half_span label_disabled">
								
								<%-- <formElement:formSelectBox idKey="profile.blank" labelKey="profile.blank" path="dateOfBirthYear" mandatory="false" skipBlank="false" 
							      skipBlankMessageKey="profile.select.year" items="${yearList}" selectCSSClass="form-control"/></div>  --%>
							      
							      <form:select name="yearList" id="yearOfBirth" path="dateOfBirthYear" onchange="selectBoxChange();">
										<option value="selectyear">Year</option>
										<c:forEach items="${yearList}" var="year"
											varStatus="yearStatus">
											<option value="${year.name}"
												<c:if test="${not empty dobYear && dobYear eq year.name}">
													selected="selected"
												</c:if>
											>${year.name}</option>
										</c:forEach>
									</form:select>
							    </div>
							    
							    <div class="errorMessage">
										<div id="errdobDay"></div>
									</div>
							</div>
							
							<div class=" select calender halfwidth select-bar"  >
								<%-- <label><spring:theme code="profile.dateOfAnniversary"
											text="Anniversary Date" /></label> --%>
								<div class="half_span">
								<%-- <formElement:formSelectBox idKey="profile.dateOfAnniversary" labelKey="profile.dateOfAnniversary" path="dateOfAnniversaryDay" mandatory="false" skipBlank="false" 
							     skipBlankMessageKey="profile.select.day" items="${dayList}" selectCSSClass="form-control"/></div> --%>
							      
							       
											
									<form:select name="dateList" id="dateOfAnniversary" path="dateOfAnniversaryDay" onchange="selectBoxChange();">
										<option value="selectDay">Date</option>
										<c:forEach items="${dayList}" var="day"
											varStatus="dayStatus">
											<option value="${day.name}"
												<c:if test="${not empty doaDay && doaDay eq day.name}">
													selected="selected"
												</c:if>
											>${day.name}</option>
										</c:forEach>
									</form:select>
							     </div>
							     
								<div class="half_span label_disabled">
								<%-- <formElement:formSelectBox idKey="profile.blank" labelKey="profile.blank" path="dateOfAnniversaryMonth" mandatory="false" skipBlank="false" 
							 skipBlankMessageKey="profile.select.month" items="${monthList}" selectCSSClass="form-control"/></div>	 --%>	
							 
							     <form:select name="monthList" id="monthOfAnniversary" path="dateOfAnniversaryMonth" onchange="selectBoxChange();">
										<option value="selectmonth">Month</option>
										<c:forEach items="${monthList}" var="month"
											varStatus="monthStatus">
											<option value="${month.code}"
												<c:if test="${not empty doaMonth && doaMonth eq month.code}">
													selected="selected"
												</c:if>
											>${month.name}</option>
										</c:forEach>
									</form:select>
							   </div>		
								<div class="half_span label_disabled">
								<%-- <formElement:formSelectBox idKey="profile.blank" labelKey="profile.blank" path="dateOfAnniversaryYear" mandatory="false" skipBlank="false" 
							       skipBlankMessageKey="profile.select.year" items="${yearAnniversaryList}" selectCSSClass="form-control"/></div> --%>
							       <form:select name="yearList" id="yearOfAnniversary" path="dateOfAnniversaryYear" onchange="selectBoxChange();">
										<option value="selectyear">Year</option>
										<c:forEach items="${yearList}" var="year"
											varStatus="yearStatus">
											<option value="${year.name}"
												<c:if test="${not empty doaYear && doaYear eq year.name}">
													selected="selected"
												</c:if>
											>${year.name}</option>
										</c:forEach>
									</form:select>
							     </div> 
							     <div class="errorMessage">
										<div id="errdoaDay"></div>
									</div>
							      <div class="errorMessage" >
										<div id="errdata"></div>
									</div>
							</div>
							<%-- <div class="select gender halfwidth">
								<formElement:formSelectBox idKey="profile.gender" labelKey="profile.gender" path="gender" mandatory="false" skipBlank="false" 
							 skipBlankMessageKey="profile.select.gender" items="${genderData}" selectCSSClass="form-control"/></div> --%>
							 
							 <%-- <div class="select gender halfwidth le-radio clearfix">
					        	<c:forEach items="${genderData}" var="varGender" varStatus="i">
					        	<div class="le-radio halfwidth">
						        	 <form:radiobutton path="gender" value="${varGender.code}"/>
						             <label for="gender${i.count}">${varGender.name}</label>
					             </div>
					            </c:forEach>
					        </div> --%>

					        					       					        
		                       <div class="half halfwidth block-element">
		                     <a class="button" href="update-password"> <spring:theme code="text.account.profile.changePassword.mobile" text="Change password"/></a>
		                      </div>
					        					       					        
							<div class="half halfwidth block-element">
								<button type="submit" class="blue"><spring:theme code="cart.modal.save.changes" text="Save Changes" /></button>
							</div>
						</form:form>
						
						</fieldset>
					<%-- </form> --%>
				</div>
			
				<!-- End of Personal Info -->
			
				<!-- Update Password -->
				
				<!-- <div class="password"> -->
					<%-- <h2><spring:theme code="profile.Password" text="Password"/></h2>	
					<p><spring:theme code="profile.Password.details" text="If you aren't a bot, you can change your password here."/></p> --%>
					
					<div >
						<form:form id="frmUpdatePassword" action="update-password" method="post" commandName="updatePasswordForm" autocomplete="off">
					
						
						<%-- <div class="full span password-input halfwidth">
									<label><spring:theme code="text.mplCustomerProfileForm.CurrPwd" text="Current Password*" /></label>
										<!-- <input type="password" path="currentPassword" id="currentPassword"
										onkeypress="kpresscp()"	 maxlength="140" /> 
										
										<form:password path="currentPassword" onkeyup="kpresscp()" placeholder="Current Password"/>
										
									<div class="errorMessage"><div id="errCurpwd"></div></div>
									</div>  
						
						
						 <div class="half password-input halfwidth">
									<label><spring:theme code="text.mplCustomerProfileForm.NewPwd" text="New Password*" /></label>
										<!-- <input type="password" path="newPassword" id="newPassword"
										onkeypress="kpressnp()"	 maxlength="140" /> 
										<form:password path="newPassword" onkeyup="kpressnp()"  placeholder="New Password"/>
									<div class="errorMessage"><div id="errNewpwd"></div></div>
									</div>  
						
			        	  <div class="half password-input halfwidth">
									<label><spring:theme code="text.mplCustomerProfileForm.CnfNewPwd" text="Confirm New Password*" /></label>
										<!-- <input type="password"  path="checkNewPassword" id="checkNewPassword"
										onkeypress="kpresscnp()" maxlength="140" /> 
										<form:password path="checkNewPassword" onkeyup="kpresscnp()" placeholder="New Password"/>
									<div class="errorMessage"><div id="errCnfNewpwd"></div></div>
									</div>  
						
						
							<div class="half password-input">
								<formElement:formPasswordBox idKey="profile.checkNewPassword" labelKey="profile.checkNewPassword" path="checkNewPassword" inputCSS="form-control password" mandatory="true"/>
							</div> 
							
							<div>
								<button type=button class="blue" onClick="return validatePassword();"><spring:theme code="cart.modal.save.changes" text="Save Changes"/></button>
							</div> --%>
						</form:form>

					</div>
				</div>
				<!-- End of Update Password -->
			
				<!-- Update NickName -->
				<%-- <div class="nickname">
					<h2><spring:theme code="profile.Nickname" text="Nickname"/></h2>
					<p><spring:theme code="profile.Nickname.details" text="What's your alter ego called?"/></p>
						<form:form action="update-nickName" method="post" commandName="mplCustomerProfileForm">
						<fieldset>
							<div class="full">
								<label><spring:theme text="Nick Name" /></label>
										<form:input path="nickName" id="profilenickName"
										onkeyup="kpressnn()"	 maxlength="40" placeholder="Nick Name"/>
								<div class="errorMessage"><div id="errnn"></div></div>
							<formElement:formInputBox idKey="profile.nickName" labelKey="profile.nickName" path="nickName" inputCSS="form-control" mandatory="false"/>
							</div>
							</fieldset>
								<button type="submit" class="blue" onClick="return validateNickName();"><spring:theme code="text.account.setyourName" text="Set Your Preferred Name" /></button>
						</form:form>
				</div> --%>
			<!-- End of Update Password -->
			
			
		</div>
</div>
</div>
</template:page>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script> --%>