<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="multiCheckout" tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<style>
 .checkout-paymentmethod {
	display: none;
} 

</style>

<script>
	//Refresh the page if compare page is already visted
	if (sessionStorage.getItem("confirmationPageVisited") != null) {
		sessionStorage.removeItem("confirmationPageVisited");
		window.location.reload(true); // force refresh page1
	}
</script>
<c:url value="${currentStepUrl}" var="choosePaymentMethodUrl" />
<spring:url value="/checkout/multi/debitTermsAndConditions" var="getDebitTermsAndConditionsUrl"/>
<cart:tealiumCartParameters/>
<template:page pageTitle="${pageTitle}" hideHeaderLinks="true" showOnlySiteLogo="true">
				<div class="alert alert-danger alert-dismissable" id="juspayconnErrorDiv">	<!-- TPR-629 changes for error -->
					<button class="close juspayCloseButton" type="button">&times;</button>
					<span id="juspayErrorMsg">Some issues are there with payment</span>
				</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>

	
	<!-- TISCR-421 Starts -->	
	<script type="text/javascript" async="true" src="https://elistva.com/api/script.js?aid=${account_id}&sid=${session_id}">
	</script>

	<noscript>
	        <p style="background:url(//elistva.com/api/assets/clear.png?aid=${account_id}&sid=${session_id}"></p>
	</noscript>
	
	<object type="application/x-shockwave-flash" data="//elistva.com/api/udid.swf?aid=${account_id}&sid=${session_id}" width="1" height="1">
	        <param name="movie" value="//elistva.com/api/udid.swf?aid=${account_id}&sid=${session_id}" />
	</object>
	<!-- TISCR-421 Ends -->

	<div class="checkout-headline" id="checkout-headline">
		<spring:theme code="checkout.multi.secure.checkout"/>
	</div>
	<div class="checkout-content checkout-payment cart checkout wrapper">
		<multiCheckout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}" isCart="${isCart}">
			<jsp:body>
				<script>
    				$(document).ready(function(){
    					<%-- var updateItHereLink = "<%=request.getParameter("Id")%>";  --%>
    					var updateItHereLink=window.location.href;
    	
    					
    					if(updateItHereLink.indexOf("updateItHereLink")>=0)
    					{
    						displayCODForm();
    						$("#viewPaymentCOD").parent("li").addClass("active");
    						$(".checkout-paymentmethod").css("display","block");
    						document.getElementById("otpMobileNUMField").focus();    						
    					}
    					else
    					{
    						if($("#CreditCard").val()=="true")
        					{
        						//displayCreditCardForm();
        						$("#viewPaymentCredit").parent("li").addClass("active");
        						$(".checkout-paymentmethod").css("display","block");
        						setTimeout(function(){$('#viewPaymentCredit').click();},1000);
        					}
        					else if($("#DebitCard").val()=="true")
        					{
        						//displayDebitCardForm();
        						$("#viewPaymentDebit").parent("li").addClass("active");
        						$(".checkout-paymentmethod").css("display","block");
        						setTimeout(function(){$('#viewPaymentDebit').click();},1000);
        					}
        					else if($("#EMI").val()=="true")
        					{
        						//displayEMIForm();
        						$("#viewPaymentEMI").parent("li").addClass("active");
        						$(".checkout-paymentmethod").css("display","block");
        						setTimeout(function(){$('#viewPaymentEMI').click();},1000);
        					}
        					else if($("#Netbanking").val()=="true")
        					{
        						//displayNetbankingForm();
        						$("#viewPaymentNetbanking").parent("li").addClass("active");
        						$(".checkout-paymentmethod").css("display","block");
        						setTimeout(function(){$('#viewPaymentNetbanking').click();},1000);
        					}
        					else if($("#COD").val()=="true")
        					{
        						//displayCODForm();
        						$("#viewPaymentCOD").parent("li").addClass("active");
        						$(".checkout-paymentmethod").css("display","block");
        						setTimeout(function(){$('#viewPaymentCOD').click();},1000);
        					}	
    					}
    					});
				</script>
				
				<!-- Script for JusPay -->
				<!--Twitter Bootstrap resources-->
		        <!-- <link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/css/bootstrap-combined.min.css" rel="stylesheet"> -->
		        <script src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/js/bootstrap.min.js"></script>
		       <!--  <link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/css/bootstrap.no-icons.min.css" rel="stylesheet"> -->
				
				 <script type="text/javascript" 
            		src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js">
        		</script>
				
				<script type="text/javascript" src="${juspayJsName}" >
				</script>
				<!-- Script for JusPay ENDS-->
				
				<script>
					$(document).ready(function(){
						$("#selectMode button").on("click",function(){
							$("#selectMode button").css({
														"background":"rgb(237,243,244)",
														"color":"#000",
														})
							$(this).css({
										"background":"white",
										"color":"rgb(4,201,246)",								
										})
						});
					});
					
				</script>
				<c:if test="${isCart eq true}">
				<script>
					
					var timeoutID;
    				function setup() {
    				    this.addEventListener("mousemove", resetTimer, false);
    				    this.addEventListener("mousedown", resetTimer, false);
    				    this.addEventListener("keypress", resetTimer, false);
    				    this.addEventListener("DOMMouseScroll", resetTimer, false);
    				    this.addEventListener("mousewheel", resetTimer, false);
    				    this.addEventListener("touchmove", resetTimer, false);
    				    this.addEventListener("MSPointerMove", resetTimer, false);
    				    startTimer();
    				}
    				setup();

    				function startTimer() {
    				    // wait 2 seconds before calling goInactive
    				    timeoutID = window.setTimeout(goInactive, '${timeout}');
    				}

    				function resetTimer(e) {
    				    window.clearTimeout(timeoutID);

    				    goActive();
    				}

    				function goInactive() {
    				   window.location = '${request.contextPath}/cart';
    				}

    				function goActive() {
    				      startTimer();
    				}
				</script>
				</c:if>
				
				<!-- TISCR-305 starts -->					
					<button type="button" class="button btn-block payment-button make_payment_top_savedCard proceed-button" id="make_saved_cc_payment_up"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
					<button type="button" class="button btn-block payment-button make_payment_top_newCard proceed-button" id="make_cc_payment_up"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
					<button type="button" class="button btn-block payment-button make_payment_top_newCard proceed-button" id="make_dc_payment_up"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
					<button type="button" class="button btn-block payment-button make_payment_top_newCard proceed-button" id="make_saved_dc_payment_up"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
					<button type="button" class="button btn-block payment-button make_payment_top_nb proceed-button" id="make_nb_payment_up" onclick="submitNBForm()"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
					<button type="button" class="button btn-block payment-button make_payment_top_savedCard proceed-button" id="make_emi_payment_up"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
					<%-- <button type="button" class="positive right cod-otp-button_top" onclick="mobileBlacklist()" ><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.sendOTP" text="Verify Number" /></button> --%>
					<button type="button" class="button positive right cod_payment_button_top proceed-button" onclick="submitForm()" id="paymentButtonId_up"><spring:theme code="checkout.multi.paymentMethod.codContinue" /></button>
					<h1 class="payment-options"><spring:theme code="text.payment.options"/></h1>
						<p class="cart-items">You have an outstanding amount of &nbsp;&nbsp;<span class="prices"  id="outstanding-amount">
					<ycommerce:testId code="cart_totalPrice_label">
				<!-- Unwanted code commented -->
               <%--  <c:choose>
                    <c:when test="${showTax}">
                        <format:price priceData="${cartData.totalPriceWithTax}"/>
                    </c:when>
                    <c:otherwise>
                        <format:price priceData="${cartData.totalPrice}"/>
                    </c:otherwise>
                </c:choose> --%>
            </ycommerce:testId>
					</span></p>
				<!-- TISCR-305 ends -->	
				<div class="left-block choose-payment">
				
						<div class="checkout-indent payments tab-view smk_accordion acc_with_icon">
							<div class="checkout-paymentmethod nav">
							<div class="head-mobile">PAYMENT METHOD</div>
								<!-- CREDIT -->
								<c:forEach var="map" items="${paymentModes}">
									<c:if test="${map.value eq true}">
										<c:choose>
											<c:when test="${map.key eq 'Credit Card'}">
												<input type="hidden" id="CreditCard" value="${map.value}" />
												<div class="accordion_in">
												<div >
													<span id="viewPaymentCredit" >
														<spring:theme code="checkout.multi.paymentMethod.selectMode.CC" />
													</span>
												</div>
							<!-- div for Cards -->
							<div id="card">
							<ul class="product-block blocks">
							<h3>Pay by a credit card</h3>
							<!-- SAVED CREDIT CARD -->
								<c:if test="${not empty creditCards}">
									<li id="savedCard" class="item">
									<span class="mycards">My cards</span>
										<form class="form-inline" id="card_form" autocomplete="off" >
											<%-- <h4><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.enterSavedCardDetails"/></h4> --%>
											<%-- <multiCheckout:paymentError /> --%>
											<div id="maestroMessage"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.maestroMsg"/></div>
											<input type="hidden" class="merchant_id" id="merchant_id" value="${merchantID}" />
				                  			<input type="hidden" class="order_id" id="order_id_saved" />
				          					<!-- <input type="hidden" class="is_emi" id="is_emi" />
				                  			<input type="hidden" class="emi_tenure" id="emi_tenure" />
				                  			<input type="hidden" class="emi_bank" id="emi_bank">	 -->
				                  			<input type="hidden" class="redirect" value="${redirect}">	
				                  			<input type="hidden" id="ebsDownCheck" value="${ebsDownCheck}"/>
			                  			
			                  				<div id="savedCreditCard">
												<c:forEach var="map" items="${creditCards}" varStatus="status">
													<div class="credit-card-group card-sec">
			            								<div class="card card-num">
										        			<div class="radio">
										        			<span class="visa card_image"><img src="${commonResourcePath}/images/Visa.png" alt=""></span>
										                 		<input type="radio" data-id="savedCCard" name="creditCards" class="card_token creditCardsRadio" id="cc${status.index}"  value="${map.value.cardToken}" />
									                 	 		<label for="cc${status.index}" data-id="savedCCard" class="numbers">
									                 	 			${map.value.cardBrand} ending in ${map.value.cardEndingDigits}</label>
									                 	 			<!-- <span class="saved">Saved card</span> -->
									                  				<p>${map.value.nameOnCard}</p>
									                  				<p><spring:theme code="text.expires.on"/> ${map.value.expiryMonth}/${map.value.expiryYear}</p>
									                  			<input type="hidden" name="creditCardsBank" class="card_bank" value="${map.value.cardIssuer}" />
									                  			<input type="hidden" name="creditCardsBrand" class="card_brand" value="${map.value.cardBrand}" />
									                  			<input type="hidden" name="creditIsDomestic" class="card_is_domestic" value="${map.value.isDomestic}" />
									                  			<br>
									                  			<div id="ebsErrorSavedCard" class="card_ebsErrorSavedCard error-message">
																	<spring:theme code="checkout.multi.paymentMethod.savedCard.ebsError"/>
																</div>
										            		</div>
										       			</div>
										        		<div class="cvv right-align-form digits">
										        			<label class="sr-only" for="cvv${status.index+1}"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVV"/></label>
												            <input type="password" autocomplete="off" class="cvvValdiation form-control security_code" id="cvv${status.index+1}"  maxlength="4" onkeypress="return isNumber(event)" />
										        			<div id="cvvErrorSavedCard" class="card_cvvErrorSavedCard error-message">
																<spring:theme code="checkout.multi.paymentMethod.savedCard.cvvError"/>
															</div>
										        		</div>
													</div>
												</c:forEach>
											</div> 
										
											<div id="savedEMICard">
											</div>
											
											</form>
											<!-- <p class="redirect">You will be re-directed to secure payment gateway</p> -->
											</li>
											<li>	
		
				<!-- Terms & Conditions Link -->
											<div class="pay top-padding saved-card-button">
												<button type="submit" class="make_payment button btn-block payment-button" id="make_saved_cc_payment"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
												

												
												<%-- <p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a><p> --%>
											</div>
										
									</li>
									<div class="terms">
									<p class="redirect">You will be re-directed to secure payment gateway</p>
									<p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank" class="conditions"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a><p>
									</div>
								</c:if> 
								<!-- END SAVED CREDIT CARD -->
								<!--  CREDIT CARD SECTION  -->
								<li id="newCardCC" class="item new-form active">
									<form class="juspay_inline_form new-card" id="payment_form" autocomplete="off" >
										<%-- Payment new UI<h2><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.enterCardDetails"/></h2> --%>
									<%-- 	<p><spring:theme code="text.we.accept"/></p>
										<ul class="accepted-cards">
											<li><span class="visa"><img src="${commonResourcePath}/images/Visa.png"></span></li>
											<li><span class="master"><img src="${commonResourcePath}/images/Master_Card.png"></span></li>
											<li><span class="maestro"><img src="${commonResourcePath}/images/Maestro.png"></span></li>
											<li><span class="amex"><img src="${commonResourcePath}/images/American_Express.png"></span></li>
											<li><span class="diners"><img src="${commonResourcePath}/images/dinner_club.png"></span></li>
											<li><span class="discover"><img src="${commonResourcePath}/images/Discover.png"></span></li>
											<li><span class="jcb"><img src="${commonResourcePath}/images/JCB.png"></span></li>
										</ul> --%>
										<div id="newMaestroMessage"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.maestroMsg"/></div>
										<input type="hidden" class="merchant_id" id="merchant_id" value="${merchantID}" />
				                  		<input type="hidden" class="order_id" id="order_id_new" />
				                  		<!-- <input type="hidden" class="is_emi" id="is_emi" />
				                  		<input type="hidden" class="emi_tenure" id="emi_tenure" />
				                  		<input type="hidden" class="emi_bank" id="emi_bank"> -->
				                  		<input type="hidden" id="ebsDownCheck" value="${ebsDownCheck}"/>
				                  		<div class="radio creditDebitLabelRadio">
										 <input type="radio" data-id="newCCard" id="creditLabel"/>
										 <label for="creditLabel" class="numbers creditLabel" data-id="newCCard"><span>New Card</span></label>
								   		</div>
										<div class="card-group">
											<div class="form-group">
						                    	<fieldset>
						                        	<div class="full account-only">
					 									<label><spring:theme code="text.cardtype"/> *</label>
					 										<select>
					  											<option><spring:theme code="text.select"/></option>
					  										</select>
													</div>
						                            <div class="controls full">
						                            <!-- Static section start -->
						                            <!-- <div class="cardNumber">
									<input class="firstfour" placeholder="1111" id="cardNo1">
									<input placeholder="2222" id="cardNo2">
									<input placeholder="3333" id="cardNo3">
									<input class="last" placeholder="4444" id="cardNo4">
								</div> -->
								<!-- Static section end -->
						                            	<%-- <label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cardNo"/></label> --%>
						                            	<input type="text" class="card_number" id="cardNo" maxlength="16" autocomplete="off" placeholder="CARD NUMBER*"> 
						                            	 <!-- <input type="hidden" class="card_number" value="" /> -->  
						                            	<input type="hidden" id="cardType" disabled="disabled"/>
						                            	<span class="error-message" id="cardNoError"></span>
													</div>
						                            
						                            <div class="controls full">
						                            	<%-- <label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cardName"/></label> --%>
						                            	<input type="text" name="memberName" class="name_on_card name-card" maxlength="79" autocomplete="off" placeholder="NAME ON CARD*">
						                            	<span class="error-message" id="memberNameError"></span>
						                            </div>
						                           
						                            <div class="controls full exp ">
						                             	<label class="control-label expires">
						                             		<spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.expiryDate"/>
						                             	</label>
						                            	<select class="card_exp_month" name="expmm" > 	
							                            	<option value="month"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.mm"/></option>
															<option value="01"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.01"/></option>
															<option value="02"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.02"/></option>
															<option value="03"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.03"/></option>
															<option value="04"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.04"/></option>
															<option value="05"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.05"/></option>
															<option value="06"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.06"/></option>
															<option value="07"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.07"/></option>
															<option value="08"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.08"/></option>
															<option value="09"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.09"/></option>
															<option value="10"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.10"/></option>
															<option value="11"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.11"/></option>
															<option value="12"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.12"/></option>
														</select>  
														
														<c:set var="currentyear" value="<%= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>"></c:set>
														<select class="card_exp_year" name="expyy" >
							                            	<option value="year" selected><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.yyyy"/></option>
							                            	<c:forEach var="i" begin="${currentyear}" end="${currentyear + noOfYearsFromCurrentYear}">
															   <option value="${i}">${i}</option>
															</c:forEach>
							                           </select>
														<span class="error-message" id="expYYError"></span>
						                            </div>
						                            
													<div class="controls full cvv">
														
														<%-- <input type="hidden" id="cvvHelpContent" value="<spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVVHelpContent"/>"> --%>
														<input type="hidden" id="cvvHelpContent" value="${cvvHelp}">
						                            	<%-- <label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVV"/></label> --%>
						                           		<input type="password" autocomplete="new-password" class="security_code span1" name="cvv" maxlength="4" placeholder="CVV" />
						                           		<%-- <a href="#cvvHelpText" class="cvvHelp" id="cvvHelp"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVVHelp"/></a> --%>
						                           		<span class="error-message" id="cvvError"></span> 
						                            </div>
												</fieldset>
		            							<div class="controls remember" id="billingAddress">
					                            	<h2><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.billingAddress"/></h2>
					                             <c:choose>
						                             <c:when test="${isCart eq true}">
							                            <c:forEach var="cartItem" items="${cartData.entries}">
							                            <c:set var="deliveryMode" value="${cartItem.mplDeliveryMode.code}"/>
															 <c:if test="${deliveryMode ne 'click-and-collect'}"> 
																 <c:set var="flag" value="true"/>
															  </c:if>  
												    	</c:forEach>
												    </c:when>
												    <c:otherwise>
												    	<c:forEach var="orderItem" items="${orderData.entries}">
							                            <c:set var="deliveryMode" value="${orderItem.mplDeliveryMode.code}"/>
															 <c:if test="${deliveryMode ne 'click-and-collect'}"> 
																 <c:set var="flag" value="true"/>
															  </c:if>  
												    	</c:forEach>
												    </c:otherwise>
											    </c:choose>
										    	<c:if test="${flag eq true}">
					                            	<input type="checkbox" id="sameAsShipping" name="billing-shipping" checked="checked" /><label for="sameAsShipping"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.sameAsShipping"/></label>
					                           	</c:if>   	
					                           		<fieldset>
						                           		<div class="half">
							                           		<label><spring:theme code="text.first.name"/></label>
							                           		<input type="text" id="firstName" required="required" maxlength="40">
							                           		<span class="error-message" id="firstNameError"></span>
						                           		</div>
						                           		<div class="half">
							                           		<label><spring:theme code="text.last.name"/></label>
							                           		<input type="text" id="lastName" required="required" maxlength="40">
							                           		<span class="error-message" id="lastNameError"></span>
						                           		</div>
						                           		<div class="full">
							                           		<label><spring:theme code="text.addressline1"/></label>
							                           		<input type="text" id="address1" maxlength="40" required="required">
							                           		<span class="error-message" id="address1Error"></span>
						                           		</div>
						                           		<div class="full">
							                           		<label><spring:theme code="text.addressline2"/></label>
							                           		<input type="text" id="address2" maxlength="40">
							                           		<span class="error-message" id="address2Error"></span>
						                           		</div>
						                           		<div class="full">
							                           		<label><spring:theme code="text.landmark"/> </label>
							                           		<input type="text" id="address3" maxlength="40">
							                           		<span class="error-message" id="address3Error"></span>
						                           		</div>
						                           		<div class="full">
							                           		<label><spring:theme code="text.city"/></label>
							                           		<input type="text" id="city" required="required" maxlength="40">
							                           		<span class="error-message" id="cityError"></span>
						                           		</div>
						                           		<div class="half">
							                           		<label><spring:theme code="text.state"/></label>
							                           		<input type="text" id="state" required="required" maxlength="40">
							                           		<span class="error-message" id="stateError"></span>
						                           		</div>
						                           		<div class="half">
						                           			<label><spring:theme code="text.country"/></label>
						                           			<select id="country" >
						                           				<c:forEach var="countryName" items="${country}">
																	<option value="${countryName}">${countryName}</option>
																</c:forEach>
															</select>
						                           		</div>
						                           		<div class="full">
							                           		<label><spring:theme code="text.pincode"/></label>
							                           		<input type="text" id="pincode" maxlength="10" onchange="validatePin()" ><span class="error-message" id="pinError"></span>
						                           		</div>
					                           		</fieldset>
					                            </div> 
					                            <div class="controls remember">
					                            	<input type="checkbox" class="juspay_locker_save checkbox"  id="save-card" name="save-card" /><label for="save-card"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.saveCard"/></label>		                        	
					                            </div>
		            							<input type="hidden" class="redirect" value="${redirect}">	
			            					</div>
			            				</div>
			            			</form>
			            			<!-- <p class="redirect">You will be re-directed to secure payment gateway</p> -->
		            			</li>
		            		<li>
				<!-- Terms & Conditions Link -->
			            		<div class="pay newCardPaymentCC">
									
									<button type="submit" class="make_payment button btn-block payment-button" id="make_cc_payment"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
									<!-- <p class="payment-redirect">You will be re-directed to secure payment gateway</p> -->
									<%-- <p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a></p> --%>
								</div>
							</li>
						</ul>
						<div class="terms">
						<p class="redirect">You will be re-directed to secure payment gateway</p>
									<p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank" class="conditions"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a><p>
									</div>					
					</div>				
				<!-- Card ends -->
				</div>
		 									</c:when>
										</c:choose>
									</c:if>	
								</c:forEach>	

								<!--  DEBIT CARD -->
								<c:forEach var="map" items="${paymentModes}">
									<c:if test="${map.value eq true}">
										<c:choose>
		    								<c:when test="${map.key eq 'Debit Card'}">
		    									<input type="hidden" id="DebitCard" value="${map.value}" />
		    									<div class="accordion_in">
		    									<div>
		    										<span id="viewPaymentDebit" >
														<spring:theme code="checkout.multi.paymentMethod.selectMode.DC" />
													</span>
												</div>
							<div id="cardDebit">
							<ul class="product-block blocks">
							<h3>Pay by a debit card</h3>
								<c:if test="${not empty debitCards}">
									<li id="savedCardDebit" class="item">
									<span class="mycards">My cards</span>
										<form class="form-inline" id="card_form_saved_debit" autocomplete="off" >
											<%-- <h4><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.enterSavedCardDetails"/></h4> --%>
											<%-- <multiCheckout:paymentError /> --%>
											<div id="maestroMessage"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.maestroMsg"/></div>
											<input type="hidden" class="merchant_id" id="merchant_id" value="${merchantID}" />
				                  			<input type="hidden" class="order_id" id="order_id_saved_dc" />
				          					<!-- <input type="hidden" class="is_emi" id="is_emi" />
				                  			<input type="hidden" class="emi_tenure" id="emi_tenure" />
				                  			<input type="hidden" class="emi_bank" id="emi_bank"> -->	
				                  			<input type="hidden" class="redirect" value="${redirect}">	
				                  			<input type="hidden" id="ebsDownCheck" value="${ebsDownCheck}"/>
											<!--  SAVED DEBIT CARD -->
											<div id="savedDebitCard">
												<c:forEach var="map" items="${debitCards}" varStatus="status">                  						 
			                   						<div class="debit-card-group card-sec">
			            								
			            									<div class="card card-num">
										        				<div class="radio">
										        				<span class="visa card_image"><img src="${commonResourcePath}/images/Visa.png" alt=""></span>
										                    		<input type="radio" data-id="savedDCard" name="debitCards" class="card_token  debitCardsRadio" id="dc${status.index}"  value="${map.value.cardToken}"/>
										                    		<label for="dc${status.index}" data-id="savedDCard" class="numbers">${map.value.cardBrand} ending in ${map.value.cardEndingDigits}</label>
										                  				<p>${map.value.nameOnCard}</p>
										                  				<p><spring:theme code="text.expires.on"/> ${map.value.expiryMonth}/${map.value.expiryYear}</p>
										                   			<input type="hidden" name="debitCardsBank" class="card_bank" value="${map.value.cardIssuer}" />
										                   			<input type="hidden" name="debitCardsBrand" class="card_brand" value="${map.value.cardBrand}" />
										                   			<input type="hidden" name="debitIsDomestic" class="card_is_domestic" value="${map.value.isDomestic}" />
										                   		<br>
										            			<div id="ebsErrorSavedCard" class="card_ebsErrorSavedCard error-message">
																	<spring:theme code="checkout.multi.paymentMethod.savedCard.ebsError"/>
																</div>
										            			</div>
										        			</div>
										        			<div class="cvv right-align-form digits">
										        				<label class="sr-only" for="cvv${status.index+1}"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVV"/></label>
										            			<input type="password" autocomplete="off" class="cvvValdiation form-control security_code_hide" id="cvv${status.index+1}" maxlength="4" onkeypress="return isNumber(event)" />		
										        				<br>
										        				<div id="cvvErrorSavedCard" class="card_cvvErrorSavedCard error-message">
																	<spring:theme code="checkout.multi.paymentMethod.savedCard.cvvError"/>
																</div>
										        			</div>
										   			</div>
												</c:forEach>
											</div>
											
											<div id="savedEMICard">
											</div>
											
											</form>
											<!-- <p class="redirect">You will be re-directed to secure payment gateway</p> -->
											</li>
											
											<li>	
		
											<!-- Terms & Conditions Link -->
											<div class="pay top-padding saved-card-button">
												<button type="submit" class="make_payment button btn-block payment-button" id="make_saved_dc_payment"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
												<!-- <p class="payment-redirect">You will be re-directed to secure payment gateway</p> -->
												<%-- <p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a><p> --%>
											</div>
										
									</li>
									<div class="terms">
									<p class="redirect">You will be re-directed to secure payment gateway</p>
									<p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank" class="conditions"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a><p>
									</div>
								</c:if> 
								<!-- DEBIT NEW CARD -->
								<li id="debitCard" class="item new-form active">
								
									<form class="juspay_inline_form new-card" id="debit_payment_form" autocomplete="off" >
										<%-- Payment new UI<h2><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.enterCardDetails"/></h2> --%>
									<%-- 	<p><spring:theme code="text.we.accept"/></p>
										<ul class="accepted-cards">
											<li><span class="visa"><img src="${commonResourcePath}/images/Visa.png"></span></li>
											<li><span class="master"><img src="${commonResourcePath}/images/Master_Card.png"></span></li>
											<li><span class="maestro"><img src="${commonResourcePath}/images/Maestro.png"></span></li>
											<li><span class="amex"><img src="${commonResourcePath}/images/American_Express.png"></span></li>
											<li><span class="diners"><img src="${commonResourcePath}/images/dinner_club.png"></span></li>
											<li><span class="discover"><img src="${commonResourcePath}/images/Discover.png"></span></li>
											<li><span class="jcb"><img src="${commonResourcePath}/images/JCB.png"></span></li>
										</ul> --%>
										<div id="newMaestroMessage"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.maestroMsg"/></div>
										<input type="hidden" class="merchant_id" id="merchant_id" value="${merchantID}" />
				                  		<input type="hidden" class="order_id" id="order_id_new_dc" />
				                  		<!-- <input type="hidden" class="is_emi" id="is_emi" />
				                  		<input type="hidden" class="emi_tenure" id="emi_tenure" />
				                  		<input type="hidden" class="emi_bank" id="emi_bank"> -->
				                  		<input type="hidden" id="ebsDownCheck" value="${ebsDownCheck}"/>
				                  		<div class="radio creditDebitLabelRadio">
										 <input type="radio" data-id="newDCard" id="debitLabel"/>
										 <label for="debitLabel" data-id="newDCard" class="numbers debitLabel" data-id="newDCard"><span>New Card</span></label>
								   		</div>
				                  		<!-- <p>NEW CARD</p> -->
										<div class="card-group">
											<div class="form-group">
						                    	<fieldset>
						                        	<div class="full account-only">
					 									<label><spring:theme code="text.cardtype"/> *</label>
					 										<select>
					  											<option><spring:theme code="text.select"/></option>
					  										</select>
													</div>
						                            <div class="controls full">
						                            	<%-- <label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cardNo"/></label> --%>
						                            	
						                             <input type="text" class="card_number" id="cardNoDc" maxlength="16" autocomplete="off" placeholder="CARD NUMBER*"> 
						                            	<input type="hidden" id="cardTypeDc" disabled="disabled"/>
						                            	<span class="error-message" id="cardNoErrorDc"></span>
													</div>
						                            
						                            <div class="controls full">
						                            	<%-- <label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cardName"/></label> --%>
						                            	<input type="text" name="memberName" class="name_on_card name-card" maxlength="79" autocomplete="off" placeholder="NAME ON CARD*">
						                            	<span class="error-message" id="memberNameErrorDc"></span>
						                            </div>
						                           
						                            <div class="controls full exp">
						                             	<label class="control-label expires">
						                             		<spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.expiryDate"/>
						                             	</label>
						                            	<select class="card_exp_month" name="expmm" > 	
							                            	<option value="month"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.mm"/></option>
															<option value="01"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.01"/></option>
															<option value="02"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.02"/></option>
															<option value="03"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.03"/></option>
															<option value="04"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.04"/></option>
															<option value="05"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.05"/></option>
															<option value="06"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.06"/></option>
															<option value="07"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.07"/></option>
															<option value="08"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.08"/></option>
															<option value="09"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.09"/></option>
															<option value="10"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.10"/></option>
															<option value="11"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.11"/></option>
															<option value="12"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.12"/></option>
														</select>  
														
														<c:set var="currentyear" value="<%= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>"></c:set>
														<select class="card_exp_year" name="expyy" >
							                            	<option value="year" selected><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.yyyy"/></option>
							                            	<c:forEach var="i" begin="${currentyear}" end="${currentyear + noOfYearsFromCurrentYear}">
															   <option value="${i}">${i}</option>
															</c:forEach>
							                           </select>
														<span class="error-message" id="expYYErrorDc"></span>
						                            </div>
						                            
													<div class="controls full cvv">
														
														<%-- <input type="hidden" id="cvvHelpContent" value="<spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVVHelpContent"/>"> --%>
														<input type="hidden" id="cvvHelpContent" value="${cvvHelp}">
						                            	<%-- <label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVV"/></label> --%>
						                           		<input type="password" autocomplete="new-password" class="security_code span1" name="cvv" maxlength="4" />
						                           		<a href="#cvvHelpText" class="cvvHelp" id="cvvHelp"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVVHelp"/></a>
						                           		<span class="error-message" id="cvvErrorDc"></span> 
						                            </div>
												</fieldset>
		            							<%-- <div class="controls remember" id="billingAddress">
					                            	<h2><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.billingAddress"/></h2>
					                            <c:forEach var="cartItem" items="${cartData.entries}">
					                            <c:set var="deliveryMode" value="${cartItem.mplDeliveryMode.code}"/>
													 <c:if test="${deliveryMode ne 'click-and-collect'}"> 
														 <c:set var="flag" value="true"/>
													  </c:if>  
										    	</c:forEach>
					                            </div> --%> 
					                            <div class="controls remember">
					                            	<input type="checkbox" class="juspay_locker_save checkbox"  id="save-card-dc" name="save-card" /><label for="save-card-dc"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.saveCard"/></label>		                        	
					                            </div>
		            							<input type="hidden" class="redirect" value="${redirect}">	
			            					</div>
			            				</div>
			            			</form>
			            			<!-- <p class="redirect">You will be re-directed to secure payment gateway</p> -->
		            			</li>
		            		<li>
									<!-- Terms & Conditions Link -->
			            		<div class="pay newCardPayment">
									
									<button type="submit" class="make_payment button btn-block payment-button" id="make_dc_payment"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
									<!-- <p class="payment-redirect">You will be re-directed to secure payment gateway</p> -->
									<%-- <p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a></p> --%>
								</div>
							</li>
						</ul>	
						<div class="terms">
						<p class="redirect">You will be re-directed to secure payment gateway</p>
									<p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank" class="conditions"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a><p>
									</div>				
					</div>	
					</div>	
			  								</c:when>
										</c:choose>
									</c:if>	
								</c:forEach>	


								<c:forEach var="map" items="${paymentModes}">
									<c:if test="${map.value eq true}">
										<c:choose>
			    							<c:when test="${map.key eq 'Netbanking'}">
			    								<input type="hidden" id="Netbanking" value="${map.value}" />
			    								<div class="accordion_in">
			    								<div>
			      	 								<span id="viewPaymentNetbanking" >
			      	 									<spring:theme code="checkout.multi.paymentMethod.selectMode.NB" />
			      	 								</span>
			      	 							</div>
			      	 							<div id="netbanking">
								<ul class="product-block net-bank netbankingPanel blocks" ></ul>
									<%-- <p class="redirect"><spring:theme code="text.secure.payment.gateway"/></p> --%>
									<!-- TISPT-235 -->
									<div class="nbAjaxError error-message">
										<spring:theme code="checkout.multi.paymentMethod.netbanking.AjaxError"/>
									</div>
									<!-- Terms & Conditions Link -->
									<div class="pay top-padding nbButton">
										<button type="button" class="make_payment button btn-block payment-button" id="make_nb_payment" onclick="submitNBForm()"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
										<div class="terms">
										<p class="redirect"><spring:theme code="text.secure.payment.gateway"/></p>
										<p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank" class="conditions"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a></p>
										</div>
										
									</div> 
								</div>
								</div>
			    							</c:when>
										</c:choose>
									</c:if>	
								</c:forEach>	
								
								<!-- EMI START-->
								
								<div class="accordion_in">
				    							<div>
				       								<span id="viewPaymentEMI" >
														<spring:theme code="checkout.multi.paymentMethod.selectMode.EMI" />
													</span>
												</div>	
								<c:forEach var="map" items="${paymentModes}">
									<c:if test="${map.value eq true}">
										<c:choose>
			    							<c:when test="${map.key eq 'EMI'}">
			    											
										<div id="emi">
										<input type="hidden" id="EMI" value="${map.value}" />	
										<ul class="product-block emi blocks">
										<li class="item">
										<div class="bank-select">
														<%-- <label id="listOfEMiBank"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.listOfEMIBanks"/></label> --%>
														<p id="emi-notice"><spring:theme code="checkout.multi.paymentMethod.emi.notice"/></p>
														 <label class="control-label bank-label">
						                             		SELECT YOUR BANK </label>
														<select id="bankNameForEMI" onchange="getSelectedEMIBank()">
														</select>
														
														
														<span class="error-message" id="emiNoBankError">No Banks available.</span>
														<span class="error-message" id="emiPromoError"></span>
														<div id="radioForEMI" class="banks">
														<p class="emi-plan">Select EMI plan</p>	
										 					<table id="EMITermTable">
													 			<%-- <th>&nbsp;</th>
								        						<th><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.terms"/></th>
								       							<th><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.interestRate"/></th>
								       							<th><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.monthlyInstallment"/></th>
								       							<th><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.interestPayable"/></th> --%>
															</table> 
															<%-- <form:hidden path="selectedTerm" value="select"/> --%>
															<input type="hidden" name="selectedTerm" id="selectedTerm" value="select"/>
														</div>	
														<p id="emiRangeError"><spring:theme code="checkout.multi.paymentMethod.emi.emiRangeError"/></p>
										</div>
										<div class="credit_for_emi"></div>
										
										<c:forEach var="map" items="${paymentModes}">
											<c:if test="${map.value eq true}">
											<c:choose>
											<c:when test="${map.key eq 'Credit Card'}">
										<input type="hidden" id="CreditCard" value="${map.value}" />
											<!-- EMI CREDIT CARD -->
													<div id="cardEmi" style="display: none;">
														<!-- <ul class="product-block blocks"> -->
														<h3>Pay by a credit card</h3>
														<!--  CREDIT CARD SECTION  -->
														<div id="newCardCCEmi" class="item new-form new-card active">
										
										<form class="juspay_inline_form new-card" id="emi_payment_form" autocomplete="off" >
										<div id="newMaestroMessage"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.maestroMsg"/></div>
										<input type="hidden" class="merchant_id" id="merchant_id_emi" value="${merchantID}" />
				                  		<input type="hidden" class="order_id" id="order_id_new_emi" />
				                  		<input type="hidden" class="is_emi" id="is_emi" value="true"/>
				                  		<input type="hidden" class="emi_tenure" id="emi_tenure" />
				                  		<input type="hidden" class="emi_bank" id="emi_bank">
				                  		<input type="hidden" id="ebsDownCheck" value="${ebsDownCheck}"/>
				                  		<p>NEW CARD</p>
										<div class="card-group">
											<div class="form-group">
						                    	<fieldset>
						                        	<div class="full account-only">
					 									<label><spring:theme code="text.cardtype"/> *</label>
					 										<select>
					  											<option><spring:theme code="text.select"/></option>
					  										</select>
													</div>
						                            <div class="controls full">
						                            	<%-- <label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cardNo"/></label> --%>
						                            	<input type="text" class="card_number" id="cardNoEmi" maxlength="16" autocomplete="off" placeholder="CARD NUMBER*"> 
						                            	 <!-- <input type="hidden" class="card_number" value="" /> -->  
						                            	<input type="hidden" id="cardTypeEmi" disabled="disabled"/>
						                            	<span class="error-message" id="cardNoErrorEmi"></span>
													</div>
						                            
						                            <div class="controls full">
						                            	<%-- <label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cardName"/></label> --%>
						                            	<input type="text" name="memberName" class="name_on_card name-card" maxlength="79" autocomplete="off" placeholder="NAME ON CARD*">
						                            	<span class="error-message" id="memberNameErrorEmi"></span>
						                            </div>
						                           
						                            <div class="controls full exp ">
						                             	<label class="control-label expires">
						                             		<spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.expiryDate"/>
						                             	</label>
						                            	<select class="card_exp_month" name="expmm" > 	
							                            	<option value="month"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.mm"/></option>
															<option value="01"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.01"/></option>
															<option value="02"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.02"/></option>
															<option value="03"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.03"/></option>
															<option value="04"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.04"/></option>
															<option value="05"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.05"/></option>
															<option value="06"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.06"/></option>
															<option value="07"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.07"/></option>
															<option value="08"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.08"/></option>
															<option value="09"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.09"/></option>
															<option value="10"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.10"/></option>
															<option value="11"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.11"/></option>
															<option value="12"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.12"/></option>
														</select>  
														
														<c:set var="currentyear" value="<%= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>"></c:set>
														<select class="card_exp_year" name="expyy" >
							                            	<option value="year" selected><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.yyyy"/></option>
							                            	<c:forEach var="i" begin="${currentyear}" end="${currentyear + noOfYearsFromCurrentYear}">
															   <option value="${i}">${i}</option>
															</c:forEach>
							                           </select>
														<span class="error-message" id="expYYErrorEmi"></span>
						                            </div>
						                            
													<div class="controls full cvv">
														
														<%-- <input type="hidden" id="cvvHelpContent" value="<spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVVHelpContent"/>"> --%>
														<input type="hidden" id="cvvHelpContent" value="${cvvHelp}">
						                            	<%-- <label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVV"/></label> --%>
						                           		<input type="password" autocomplete="new-password" class="security_code span1" name="cvv" maxlength="4" placeholder="CVV" />
						                           		<%-- <a href="#cvvHelpText" class="cvvHelp" id="cvvHelp"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVVHelp"/></a> --%>
						                           		<span class="error-message" id="cvvErrorEmi"></span> 
						                            </div>
												</fieldset>
		            							<div class="controls remember" id="billingAddressEmi">
					                            	<h2><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.billingAddress"/></h2>
					                             <c:choose>
						                             <c:when test="${isCart eq true}">
							                            <c:forEach var="cartItem" items="${cartData.entries}">
							                            <c:set var="deliveryMode" value="${cartItem.mplDeliveryMode.code}"/>
															 <c:if test="${deliveryMode ne 'click-and-collect'}"> 
																 <c:set var="flag" value="true"/>
															  </c:if>  
												    	</c:forEach>
												    </c:when>
												    <c:otherwise>
												    	<c:forEach var="orderItem" items="${orderData.entries}">
							                            <c:set var="deliveryMode" value="${orderItem.mplDeliveryMode.code}"/>
															 <c:if test="${deliveryMode ne 'click-and-collect'}"> 
																 <c:set var="flag" value="true"/>
															  </c:if>  
												    	</c:forEach>
												    </c:otherwise>
											    </c:choose>
										    	<c:if test="${flag eq true}">
					                            	<input type="checkbox" id="sameAsShippingEmi" name="billing-shipping" checked="checked" /><label for="sameAsShippingEmi"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.sameAsShipping"/></label>
					                           	</c:if>   	
					                           		<fieldset>
						                           		<div class="half">
							                           		<%-- <label><spring:theme code="text.first.name"/></label> --%>
							                           		<input type="text" id="firstNameEmi" required="required" maxlength="40" placeholder="FIRST NAME*">
							                           		<span class="error-message" id="firstNameErrorEmi"></span>
						                           		</div>
						                           		<div class="half">
							                           		<%-- <label><spring:theme code="text.last.name"/></label> --%>
							                           		<input type="text" id="lastNameEmi" required="required" maxlength="40" placeholder="LAST NAME*">
							                           		<span class="error-message" id="lastNameErrorEmi"></span>
						                           		</div>
						                           		<div class="full">
							                           		<%-- <label><spring:theme code="text.addressline1"/></label> --%>
							                           		<input type="text" id="address1Emi" maxlength="40" required="required" placeholder="ADDRESS LINE 1*">
							                           		<span class="error-message" id="address1ErrorEmi"></span>
						                           		</div>
						                           		<div class="full">
							                           		<%-- <label><spring:theme code="text.addressline2"/></label> --%>
							                           		<input type="text" id="address2Emi" maxlength="40" placeholder="ADDRESS LINE 2*">
							                           		<span class="error-message" id="address2ErrorEmi"></span>
						                           		</div>
						                           		<div class="full">
							                           		<%-- <label><spring:theme code="text.landmark"/> </label> --%>
							                           		<input type="text" id="address3Emi" maxlength="40" placeholder="ADDRESS LINE 3*">
							                           		<span class="error-message" id="address3ErrorEmi"></span>
						                           		</div>
						                           		<div class="full">
							                           		<%-- <label><spring:theme code="text.city"/></label> --%>
							                           		<input type="text" id="cityEmi" required="required" maxlength="40" placeholder="CITY*">
							                           		<span class="error-message" id="cityErrorEmi"></span>
						                           		</div>
						                           		<div class="half">
							                           		<%-- <label><spring:theme code="text.state"/></label> --%>
							                           		<input type="text" id="stateEmi" required="required" maxlength="40" placeholder="STATE*">
							                           		<span class="error-message" id="stateErrorEmi"></span>
						                           		</div>
						                           		<div class="half">
						                           		<%-- 	<label><spring:theme code="text.country"/></label> --%>
						                           			<select id="countryEmi" >
						                           				<c:forEach var="countryName" items="${country}">
																	<option value="${countryName}">${countryName}</option>
																</c:forEach>
															</select>
						                           		</div>
						                           		<div class="full">
							                           		<%-- <label><spring:theme code="text.pincode"/></label> --%>
							                           		<input type="text" id="pincodeEmi" maxlength="10" onchange="validatePin()" placeholder="PINCODE*"><span class="error-message" id="pinErrorEmi"></span>
						                           		</div>
					                           		</fieldset>
					                            </div> 
					                            <div class="controls remember">
					                            	<input type="checkbox" class="juspay_locker_save checkbox"  id="save-card-emi" name="save-card-emi" /><label for="save-card-emi"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.saveCard"/></label>		                        	
					                            </div>
		            							<input type="hidden" class="redirect" value="${redirect}">	
			            					</div>
			            				</div>
			            				<!-- <p class="redirect">You will be re-directed to secure payment gateway</p> -->
			            			</form>
			            			<div class="pay newCardPaymentCCEmi">
									
									<button type="button" class="make_payment button btn-block payment-button" id="make_emi_payment"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
									<!-- <p class="payment-redirect">You will be re-directed to secure payment gateway</p> -->
									<%-- <p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a></p> --%>
								    </div>
								    <div class="terms">
								    <p class="redirect">You will be re-directed to secure payment gateway</p>
									<p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank" class="conditions"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a><p>
									</div>	
			            			</div>
			            			</div>
			            			</c:when>
			            			</c:choose>
			            			</c:if>
			            			</c:forEach>
			            			</li>
									</ul>
								</div> <!-- End of div id EMI -->
								</c:when>
								</c:choose>
								</c:if>
								</c:forEach>
								</div> <!-- End of accordian -->

								<c:forEach var="map" items="${paymentModes}">
									<c:if test="${map.value eq true}">
										<c:choose>
			    							<c:when test="${map.key eq 'COD'}">
			    								<input type="hidden" id="COD" value="${map.value}" />
			    								<ycommerce:testId code="paymentDetailsForm">
								
							<form:form id="silentOrderPostForm" name="silentOrderPostForm" class="create_update_payment_form" commandName="paymentForm" action="${newPaymentFormMplUrl}" autocomplete="off" method="POST">
								<form:hidden path="paymentModeValue"/>
								<input type="hidden" name="orderPage_receiptResponseURL" value="${silentOrderPageData.parameters['orderPage_receiptResponseURL']}"/>
								<input type="hidden" name="orderPage_declineResponseURL" value="${silentOrderPageData.parameters['orderPage_declineResponseURL']}"/>
								<input type="hidden" name="orderPage_cancelResponseURL" value="${silentOrderPageData.parameters['orderPage_cancelResponseURL']}"/>
								<%-- <input type="hidden" id="guid" value="${guid}"> --%>
								<form:hidden path="guid" id="guid" value="${guid}"/>
								<input type="hidden" id="promoAvailable" value="${promoAvailable}"/>
								<input type="hidden" id="bankAvailable" value="${bankAvailable}"/>
								<c:forEach items="${sopPaymentDetailsForm.signatureParams}" var="entry" varStatus="status">
									<input type="hidden" id="${entry.key}" name="${entry.key}" value="${entry.value}"/>
								</c:forEach>
								<c:forEach items="${sopPaymentDetailsForm.subscriptionSignatureParams}" var="entry" varStatus="status">
									<input type="hidden" id="${entry.key}" name="${entry.key}" value="${entry.value}"/>
								</c:forEach>
			    								<div class="accordion_in">
				    							<div>
				       								<span id="viewPaymentCOD" >
				       									<spring:theme code="checkout.multi.paymentMethod.selectMode.COD" />
				       								</span>
			       								</div>
			       								
				
								
				<!-- div for COD -->
								<div id="COD">
								<ul class="product-block net-bank blocks">
								<%-- <li class="header">
								<ul>
											<li class="paymentHeaderHeight"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cod.title"/></li>
										</ul>
								</li> --%>
								<li class="cod-container cash">
									<div id="otpNUM" >
										<!-- TISPT-235 Code changed for COD using AJAX -->
										<input type="hidden" id="cartValue" value="${cartValue}" />
										<input type="hidden" id="httpRequest" value="${request}" />
										<%-- <p style="color:#a9143c;"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cod.desc"/></p>
										<div class="amtPayable"><h4><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cod.amtPayable"/>
										&nbsp;<span id="codAmount"></span></h4>&nbsp;<span id="convChargeMessage"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cod.convChargeMsg"/></span>
										</div><br> 
										<div id="sendOTPNumber" >
											<input type="hidden" id="codEligible" value="${codEligible}" />
											<div class="description"><spring:theme code=""/></div>
											<label name="Enter OTP" class="cod-mob-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.mobileNo"/></label>
												<input type="text" id="mobilePrefix" name="mobilePrefix" value="+91" disabled="disabled" /><input type="text" id="otpMobileNUMField" name="otpNUM" value="${cellNo}" maxlength="10"/>
												<div id="mobileNoError" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.mobileNoErrorMessage"/></div>
												<p style="color:#333;"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.mobileNoMessage"/>
												&nbsp;<a href="${request.contextPath}/checkout/multi/payment-method/add?Id=updateItHereLink" class="cod-link"><spring:theme code="checkout.multi.paymentMethod.cod.updateItHereLink"/></a></p>
											
											<div id="sendOTPButton">
													
												<button type="button" class="positive right cod-otp-button" onclick="mobileBlacklist()" ><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.sendOTP" text="Verify Number" />
												</button>
												<div id="resendOTPMessage" class="description"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.codResendMessage"/></div>
											</div>
											<div id="OTPGenerationErrorMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.codMessage"/>
											</div>
										</div>
		
										<div id="enterOTP">
											<label name="Enter OTP"><spring:theme code="checkout.multi.paymentMethod.CODPayment.enterOTP" text="Enter OTP:&nbsp;"/>
												<input type="text" id="otpNUMField" name="otpNUM" onfocus="hideErrorMsg()" autocomplete="off"/>
											</label>
										</div> --%>
									</div>
									
									
									<!-- COD error messages -->
									<div id="codErrorMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.codErrorMessage"/>
									</div>
									
									<div id="codMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.codMessage"/>
									</div>
									
									
								 	<div id="customerBlackListMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.customerBlackListMessage"/>
									</div>
									
									
									<div id="otpValidationMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.otpValidationSuccessfulMessage"/>
									</div>
									
									
									<div id="wrongOtpValidationMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.wrongOtpValidationMessage"/>
									</div>
									
									<div id="otpSentMessage" class="error-message payment-notification"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.otpSentMessage"/>
									</div>
									
									<div id="expiredOtpValidationMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.expiredOtpValidationMessage"/>
									</div>
									
									<div id="fulfillmentMessage" class="error-message payment-notification"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.fulfillmentMessage"/>
									</div>
									
									<div id="codItemEligibilityMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.codItemEligibilityMessage"/>
									</div>
									
									<div id="emptyOTPMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.emptyOTPMessage"/>
									</div>
									
									<!-- COD error messages ends -->
										
										<div id="paymentFormButton" class="pay cont-del">	
						<!-- Terms & Conditions Link -->

								<button type="button" class="make_payment button btn-block payment-button confirm" onclick="submitForm()" id="paymentButtonId">		
									<div id="submitPaymentFormCODButton">	
										<spring:theme code="checkout.multi.paymentMethod.codContinue" />
									</div>
								</button>
								<%-- <p class="payment-redirect"><spring:theme code="text.secure.payment.gateway"/></p> --%>
									
							</div>
													
									</li>
									</ul>	
									<div class="terms cod"><p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><br/><a href="<c:url value="${tncLink}"/>" target="_blank" class="conditions"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a><p></div>	
								</div>
								</div>						
				<!-- End of COD -->					
										
						
							</form:form>
						</ycommerce:testId>
						
			    							</c:when>
										</c:choose>
									</c:if>	
								</c:forEach>	
							</ul>
							<input type="hidden" id="paymentMode" name="paymentMode"/>
							<ul class="tabs">
							<!-- <li class="change-payment">Change Payment Method</li> -->
						<%-- <ycommerce:testId code="paymentDetailsForm">
								
							<form:form id="silentOrderPostForm" name="silentOrderPostForm" class="create_update_payment_form" commandName="paymentForm" action="${newPaymentFormMplUrl}" autocomplete="off" method="POST">
								<form:hidden path="paymentMode"/>
								<input type="hidden" name="orderPage_receiptResponseURL" value="${silentOrderPageData.parameters['orderPage_receiptResponseURL']}"/>
								<input type="hidden" name="orderPage_declineResponseURL" value="${silentOrderPageData.parameters['orderPage_declineResponseURL']}"/>
								<input type="hidden" name="orderPage_cancelResponseURL" value="${silentOrderPageData.parameters['orderPage_cancelResponseURL']}"/>
								<input type="hidden" id="promoAvailable" value="${promoAvailable}"/>
								<input type="hidden" id="bankAvailable" value="${bankAvailable}"/>
								<c:forEach items="${sopPaymentDetailsForm.signatureParams}" var="entry" varStatus="status">
									<input type="hidden" id="${entry.key}" name="${entry.key}" value="${entry.value}"/>
								</c:forEach>
								<c:forEach items="${sopPaymentDetailsForm.subscriptionSignatureParams}" var="entry" varStatus="status">
									<input type="hidden" id="${entry.key}" name="${entry.key}" value="${entry.value}"/>
								</c:forEach>
				
			<!-- div for EMI -->	
										<li id="emi">
											<ul class="product-block emi">
												<li class="header">
													<ul>
														<li>
															<div class="paymentHeaderHeight"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentEMI"/></div>
														</li>
													</ul>
												</li>
												<li class="mobile-header">
													<h2><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentEMI"/></h2>
												</li>
						
												<li class="item">
													<div class="bank-select">
														<label id="listOfEMiBank"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.listOfEMIBanks"/></label>
														<p id="emi-notice"><spring:theme code="checkout.multi.paymentMethod.emi.notice"/></p>
														<select id="bankNameForEMI" onchange="getSelectedEMIBank()">
														</select>
														
														
														<span class="error-message" id="emiNoBankError">No Banks available.</span>
														<span class="error-message" id="emiPromoError"></span>
														<div id="radioForEMI" class="banks">
														<p class="emi-plan">Select a plan</p>	
										 					<table id="EMITermTable">
													 			<th>&nbsp;</th>
								        						<th><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.terms"/></th>
								       							<th><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.interestRate"/></th>
								       							<th><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.monthlyInstallment"/></th>
								       							<th><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.interestPayable"/></th>
															</table> 
															<form:hidden path="selectedTerm" value="select"/>
														</div>	
														<p id="emiRangeError"><spring:theme code="checkout.multi.paymentMethod.emi.emiRangeError"/></p>
													</div>	
												</li>
											</ul>	
										</li> 
								
				<!-- EMI ends -->							
								
				<!-- div for COD -->
								<li id="COD">
								<ul class="product-block net-bank">
								<li class="header">
								<ul>
											<li class="paymentHeaderHeight"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cod.title"/></li>
										</ul>
								</li>
								<li class="cod-container">
									<div id="otpNUM" >
										<!-- TISPT-235 Code changed for COD using AJAX -->
										<input type="hidden" id="cartValue" value="${cartValue}" />
										<input type="hidden" id="httpRequest" value="${request}" />
										<p style="color:#a9143c;"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cod.desc"/></p>
										<div class="amtPayable"><h4><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cod.amtPayable"/>
										&nbsp;<span id="codAmount"></span></h4>&nbsp;<span id="convChargeMessage"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cod.convChargeMsg"/></span>
										</div><br> 
										<div id="sendOTPNumber" >
											<input type="hidden" id="codEligible" value="${codEligible}" />
											<div class="description"><spring:theme code=""/></div>
											<label name="Enter OTP" class="cod-mob-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.mobileNo"/></label>
												<input type="text" id="mobilePrefix" name="mobilePrefix" value="+91" disabled="disabled" /><input type="text" id="otpMobileNUMField" name="otpNUM" value="${cellNo}" maxlength="10"/>
												<div id="mobileNoError" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.mobileNoErrorMessage"/></div>
												<p style="color:#333;"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.mobileNoMessage"/>
												&nbsp;<a href="${request.contextPath}/checkout/multi/payment-method/add?Id=updateItHereLink" class="cod-link"><spring:theme code="checkout.multi.paymentMethod.cod.updateItHereLink"/></a></p>
											
											<div id="sendOTPButton">
													
												<button type="button" class="positive right cod-otp-button" onclick="mobileBlacklist()" ><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.sendOTP" text="Verify Number" />
												</button>
												<div id="resendOTPMessage" class="description"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.codResendMessage"/></div>
											</div>
											<div id="OTPGenerationErrorMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.codMessage"/>
											</div>
										</div>
		
										<div id="enterOTP">
											<label name="Enter OTP"><spring:theme code="checkout.multi.paymentMethod.CODPayment.enterOTP" text="Enter OTP:&nbsp;"/>
												<input type="text" id="otpNUMField" name="otpNUM" onfocus="hideErrorMsg()" autocomplete="off"/>
											</label>
										</div>
									</div>
									
									
									<!-- COD error messages -->
									<div id="codErrorMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.codErrorMessage"/>
									</div>
									
									<div id="codMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.codMessage"/>
									</div>
									
									
								 	<div id="customerBlackListMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.customerBlackListMessage"/>
									</div>
									
									
									<div id="otpValidationMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.otpValidationSuccessfulMessage"/>
									</div>
									
									
									<div id="wrongOtpValidationMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.wrongOtpValidationMessage"/>
									</div>
									
									<div id="otpSentMessage" class="error-message payment-notification"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.otpSentMessage"/>
									</div>
									
									<div id="expiredOtpValidationMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.expiredOtpValidationMessage"/>
									</div>
									
									<div id="fulfillmentMessage" class="error-message payment-notification"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.fulfillmentMessage"/>
									</div>
									
									<div id="codItemEligibilityMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.codItemEligibilityMessage"/>
									</div>
									
									<div id="emptyOTPMessage" class="error-message"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.emptyOTPMessage"/>
									</div>
									
									<!-- COD error messages ends -->
										
										<div id="paymentFormButton" class="pay">	
						<!-- Terms & Conditions Link -->

								<button type="button" class="make_payment button btn-block payment-button" onclick="submitForm()" id="paymentButtonId">		
									<div id="submitPaymentFormCODButton">	
										<spring:theme code="checkout.multi.paymentMethod.codContinue" />
									</div>
								</button>
								<p class="payment-redirect"><spring:theme code="text.secure.payment.gateway"/></p>
									<p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a><p>
							</div>
													
									</li>
									</ul>		
								</li>
														
				<!-- End of COD -->					
										
						
							</form:form>
						</ycommerce:testId> --%>
						
				<!-- div for Netbanking -->	
								<%-- <li id="netbanking">
								<ul class="product-block net-bank netbankingPanel" >
								
								<li class="header">
										<ul>
											<li class="paymentHeaderHeight"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentNetbanking"/></li>
										</ul>
									</li>
									<li class="mobile-header">
										<h2><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentNetbanking"/></h2>
									</li>
									
									<!-- TISPT-235 Commented to make netbanking ajax call -->
									
									<li class="item cardForm">									
										<input type="hidden" id="juspayOrderId" />
										
									<div id="netbankingIssueError" class="error-message">
										<spring:theme code="checkout.multi.paymentMethod.netbankingIssue.Error"/>
									</div>
								
									<c:if test="${not empty popularBankNames}">
									<label class="popular-netbanks"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentNetbanking.popularBanks"/></label> 
									<ul>
									<c:forEach var="bank" items="${popularBankNames}" varStatus="status">
										
										<li>
											<input type="radio" class="NBBankCode" name="priority_banks" value="${bank.bankCode}" id="radioButton_bankCode${status.index}" onchange="deselectSelection()" />										
											<label for="radioButton_bankCode${status.index}"><img src='${bank.bankLogoUrl}'><c:if test="${empty bank.bankLogoUrl}"><c:out value="${bank.bankName}"/></c:if></label>
											<input type="hidden" name="NBBankName" id="NBBankName${status.index}" value="${bank.bankName}" />
										</li>
									</c:forEach>
										</ul>
									</c:if>
												
								
									<c:if test="${not empty otherBankNames}">
									<div class="bank-select">
										<label><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.otherNBBanks"/></label>
										<select name="NBBankCode" id="bankCodeSelection" onchange="deselectRadio()">
											<option value="select"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.selectBank"/></option>		
											<c:forEach var="bankMap" items="${otherBankNames}">
												<option value="${bankMap.value}">${bankMap.key}</option>	
											</c:forEach>
										</select>		
										
									</div>
									</c:if>
									
									<br>
									<div id="netbankingError">
										<spring:theme code="checkout.multi.paymentMethod.netbanking.Error"/>
									</div>
									
									
								</li>
									</ul>
									<!-- TISPT-235 -->
									<div class="nbAjaxError error-message">
										<spring:theme code="checkout.multi.paymentMethod.netbanking.AjaxError"/>
									</div>
									<!-- Terms & Conditions Link -->
									<div class="pay top-padding nbButton">
										<button type="button" class="make_payment button btn-block payment-button" id="make_nb_payment" onclick="submitNBForm()"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
										<p class="payment-redirect"><spring:theme code="text.secure.payment.gateway"/></p>
										<p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a></p>
										
									</div> 
								</li> --%>	
				<!-- End of Netbanking -->	
						
				<!-- div for Cards -->
						<%-- <li id="card">
							<ul class="product-block">
								<li class="header">
									<ul>
										<li>
											<div id="ccHeader" class="paymentHeaderHeight">
												<div class=""><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentCreditCard"/></div>
											</div>
											<div id="dcHeader" class="paymentHeaderHeight">
												<div class=""><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentDebitCard"/></div>
											</div>
										</li>
						
				<!-- STATIC COMPONENT ADDED -->
										<li class="savedCard pincode-button paymentHeaderHeight" onClick="savedCardForm()"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.savedCard"/></li>
										<li class="newCard pincode-button paymentHeaderHeight" onClick="newCardForm()"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.newCard"/></li>
				<!-- STATIC COMPONENT ENDS HERE -->
									</ul>
								</li>
								<li class="mobile-header">
									<div id="ccHeader">
										<h2><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentCreditCard"/></h2>
									</div>
									<div id="dcHeader">
										<h2><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentDebitCard"/></h2>
									</div>
								</li>		
								<c:if test="${not empty creditCards || not empty debitCards}">
									<li id="savedCard" class="item">
										<form class="form-inline" id="card_form" autocomplete="off" >
											<h4><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.enterSavedCardDetails"/></h4>
											<multiCheckout:paymentError />
											<div id="maestroMessage"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.maestroMsg"/></div>
											<input type="hidden" class="merchant_id" id="merchant_id" value="${merchantID}" />
				                  			<input type="hidden" class="order_id" id="order_id_saved" />
				          					<input type="hidden" class="is_emi" id="is_emi" />
				                  			<input type="hidden" class="emi_tenure" id="emi_tenure" />
				                  			<input type="hidden" class="emi_bank" id="emi_bank">	
				                  			<input type="hidden" class="redirect" value="${redirect}">	
				                  			<input type="hidden" id="ebsDownCheck" value="${ebsDownCheck}"/>
			                  			
			                  				<div id="savedCreditCard">
												<c:forEach var="map" items="${creditCards}" varStatus="status">
													<div class="credit-card-group">
			            								<div class="card">
										        			<div class="radio">
										                 		<input type="radio" name="creditCards" class="card_token creditCardsRadio" id="cc${status.index}"  value="${map.value.cardToken}" />
									                 	 		<label for="cc${status.index}">
									                 	 			${map.value.cardBrand} ending in ${map.value.cardEndingDigits}</label>
									                  				<p>${map.value.nameOnCard}</p>
									                  				<p><spring:theme code="text.expires.on"/> ${map.value.expiryMonth}/${map.value.expiryYear}</p>
									                  			<input type="hidden" name="creditCardsBank" class="card_bank" value="${map.value.cardIssuer}" />
									                  			<input type="hidden" name="creditCardsBrand" class="card_brand" value="${map.value.cardBrand}" />
									                  			<input type="hidden" name="creditIsDomestic" class="card_is_domestic" value="${map.value.isDomestic}" />
									                  			<br>
									                  			<div id="ebsErrorSavedCard" class="card_ebsErrorSavedCard error-message">
																	<spring:theme code="checkout.multi.paymentMethod.savedCard.ebsError"/>
																</div>
										            		</div>
										       			</div>
										        		<div class="cvv right-align-form">
										        			<label class="sr-only" for="cvv${status.index+1}"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVV"/></label>
												            <input type="password" autocomplete="off" class="cvvValdiation form-control security_code" id="cvv${status.index+1}"  maxlength="4" onkeypress="return isNumber(event)" />
										        			<br>
										        			<div id="cvvErrorSavedCard" class="card_cvvErrorSavedCard error-message">
																<spring:theme code="checkout.multi.paymentMethod.savedCard.cvvError"/>
															</div>
										        		</div>
													</div>
												</c:forEach>
											</div> 
										
											<div id="savedDebitCard">
												<c:forEach var="map" items="${debitCards}" varStatus="status">                  						 
			                   						<div class="debit-card-group">
			            								
			            									<div class="card">
										        				<div class="radio">
										                    		<input type="radio" name="debitCards" class="card_token  debitCardsRadio" id="dc${status.index}"  value="${map.value.cardToken}"/>
										                    		<label for="dc${status.index}">${map.value.cardBrand} ending in ${map.value.cardEndingDigits}</label>
										                  				<p>${map.value.nameOnCard}</p>
										                  				<p><spring:theme code="text.expires.on"/> ${map.value.expiryMonth}/${map.value.expiryYear}</p>
										                   			<input type="hidden" name="debitCardsBank" class="card_bank" value="${map.value.cardIssuer}" />
										                   			<input type="hidden" name="debitCardsBrand" class="card_brand" value="${map.value.cardBrand}" />
										                   			<input type="hidden" name="debitIsDomestic" class="card_is_domestic" value="${map.value.isDomestic}" />
										                   		<br>
										            			<div id="ebsErrorSavedCard" class="card_ebsErrorSavedCard error-message">
																	<spring:theme code="checkout.multi.paymentMethod.savedCard.ebsError"/>
																</div>
										            			</div>
										        			</div>
										        			<div class="cvv right-align-form">
										        				<label class="sr-only" for="cvv${status.index+1}"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVV"/></label>
										            			<input type="password" autocomplete="off" class="cvvValdiation form-control security_code_hide" id="cvv${status.index+1}" maxlength="4" onkeypress="return isNumber(event)" />		
										        				<br>
										        				<div id="cvvErrorSavedCard" class="card_cvvErrorSavedCard error-message">
																	<spring:theme code="checkout.multi.paymentMethod.savedCard.cvvError"/>
																</div>
										        			</div>
										   			</div>
												</c:forEach>
											</div>
											
											<div id="savedEMICard">
											</div>
											
											</form>
											</li>
											<li>	
		
				<!-- Terms & Conditions Link -->
											<div class="pay top-padding saved-card-button">
												<button type="submit" class="make_payment button btn-block payment-button" id="make_saved_cc_payment"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
												<p class="payment-redirect">You will be re-directed to secure payment gateway</p>
												<p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a><p>
											</div>
										
									</li>
								</c:if> 
							
								<li id="newCard" class="item new-form active">
									<form class="juspay_inline_form new-card" id="payment_form" autocomplete="off" >
										Payment new UI<h2><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.enterCardDetails"/></h2>
										<p><spring:theme code="text.we.accept"/></p>
										<ul class="accepted-cards">
											<li><span class="visa"><img src="${commonResourcePath}/images/Visa.png"></span></li>
											<li><span class="master"><img src="${commonResourcePath}/images/Master_Card.png"></span></li>
											<li><span class="maestro"><img src="${commonResourcePath}/images/Maestro.png"></span></li>
											<li><span class="amex"><img src="${commonResourcePath}/images/American_Express.png"></span></li>
											<li><span class="diners"><img src="${commonResourcePath}/images/dinner_club.png"></span></li>
											<li><span class="discover"><img src="${commonResourcePath}/images/Discover.png"></span></li>
											<li><span class="jcb"><img src="${commonResourcePath}/images/JCB.png"></span></li>
										</ul>
										<div id="newMaestroMessage"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.maestroMsg"/></div>
										<input type="hidden" class="merchant_id" id="merchant_id" value="${merchantID}" />
				                  		<input type="hidden" class="order_id" id="order_id_new" />
				                  		<input type="hidden" class="is_emi" id="is_emi" />
				                  		<input type="hidden" class="emi_tenure" id="emi_tenure" />
				                  		<input type="hidden" class="emi_bank" id="emi_bank">
				                  		<input type="hidden" id="ebsDownCheck" value="${ebsDownCheck}"/>
										<div class="card-group">
											<div class="form-group">
						                    	<fieldset>
						                        	<div class="full account-only">
					 									<label><spring:theme code="text.cardtype"/> *</label>
					 										<select>
					  											<option><spring:theme code="text.select"/></option>
					  										</select>
													</div>
						                            <div class="controls full">
						                            	<label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cardNo"/></label>
						                            	<input type="text" class="card_number" id="cardNo" maxlength="16" autocomplete="off">
						                            	<input type="hidden" id="cardType" disabled="disabled"/>
						                            	<span class="error-message" id="cardNoError"></span>
													</div>
						                            
						                            <div class="controls full">
						                            	<label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cardName"/></label>
						                            	<input type="text" name="memberName" class="name_on_card" maxlength="79" autocomplete="off">
						                            	<span class="error-message" id="memberNameError"></span>
						                            </div>
						                           
						                            <div class="controls full exp">
						                             	<label class="control-label">
						                             		<spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.expiryDate"/>
						                             	</label>
						                            	<select class="card_exp_month" name="expmm" > 	
							                            	<option value="month"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.mm"/></option>
															<option value="01"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.01"/></option>
															<option value="02"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.02"/></option>
															<option value="03"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.03"/></option>
															<option value="04"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.04"/></option>
															<option value="05"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.05"/></option>
															<option value="06"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.06"/></option>
															<option value="07"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.07"/></option>
															<option value="08"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.08"/></option>
															<option value="09"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.09"/></option>
															<option value="10"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.10"/></option>
															<option value="11"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.11"/></option>
															<option value="12"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.12"/></option>
														</select>  
														
														<c:set var="currentyear" value="<%= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>"></c:set>
														<select class="card_exp_year" name="expyy" >
							                            	<option value="year" selected><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.yyyy"/></option>
							                            	<c:forEach var="i" begin="${currentyear}" end="${currentyear + noOfYearsFromCurrentYear}">
															   <option value="${i}">${i}</option>
															</c:forEach>
							                           </select>
														<span class="error-message" id="expYYError"></span>
						                            </div>
						                            
													<div class="controls full cvv">
														
														<input type="hidden" id="cvvHelpContent" value="<spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVVHelpContent"/>">
														<input type="hidden" id="cvvHelpContent" value="${cvvHelp}">
						                            	<label class="control-label"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVV"/></label>
						                           		<input type="password" autocomplete="new-password" class="security_code span1" name="cvv" maxlength="4" />
						                           		<a href="#cvvHelpText" class="cvvHelp" id="cvvHelp"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.CVVHelp"/></a>
						                           		<span class="error-message" id="cvvError"></span> 
						                            </div>
												</fieldset>
		            							<div class="controls" id="billingAddress">
					                            	<h2><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.billingAddress"/></h2>
					                            <c:choose>
					                            <c:when test="${isCart eq true}">
					                            <c:forEach var="cartItem" items="${cartData.entries}">
					                            <c:set var="deliveryMode" value="${cartItem.mplDeliveryMode.code}"/>
													 <c:if test="${deliveryMode ne 'click-and-collect'}"> 
														 <c:set var="flag" value="true"/>
													  </c:if>  
										    	</c:forEach>
										    	</c:when>
										    	<c:otherwise>
										    	<c:forEach var="cartItem" items="${orderData.entries}">
					                            <c:set var="deliveryMode" value="${cartItem.mplDeliveryMode.code}"/>
													 <c:if test="${deliveryMode ne 'click-and-collect'}"> 
														 <c:set var="flag" value="true"/>
													  </c:if>  
										    	</c:forEach>
										    	</c:otherwise>
										    	</c:choose>
										    	<c:if test="${flag eq true}">
					                            	<input type="checkbox" id="sameAsShipping" name="billing-shipping" checked="checked" /><label for="sameAsShipping"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.sameAsShipping"/></label>
					                           	</c:if>   	
					                           		<fieldset>
						                           		<div class="half">
							                           		<label><spring:theme code="text.first.name"/></label>
							                           		<input type="text" id="firstName" required="required" maxlength="40">
							                           		<span class="error-message" id="firstNameError"></span>
						                           		</div>
						                           		<div class="half">
							                           		<label><spring:theme code="text.last.name"/></label>
							                           		<input type="text" id="lastName" required="required" maxlength="40">
							                           		<span class="error-message" id="lastNameError"></span>
						                           		</div>
						                           		<div class="full">
							                           		<label><spring:theme code="text.addressline1"/></label>
							                           		<input type="text" id="address1" maxlength="40" required="required">
							                           		<span class="error-message" id="address1Error"></span>
						                           		</div>
						                           		<div class="full">
							                           		<label><spring:theme code="text.addressline2"/></label>
							                           		<input type="text" id="address2" maxlength="40">
							                           		<span class="error-message" id="address2Error"></span>
						                           		</div>
						                           		<div class="full">
							                           		<label><spring:theme code="text.landmark"/> </label>
							                           		<input type="text" id="address3" maxlength="40">
							                           		<span class="error-message" id="address3Error"></span>
						                           		</div>
						                           		<div class="full">
							                           		<label><spring:theme code="text.city"/></label>
							                           		<input type="text" id="city" required="required" maxlength="40">
							                           		<span class="error-message" id="cityError"></span>
						                           		</div>
						                           		<div class="half">
							                           		<label><spring:theme code="text.state"/></label>
							                           		<input type="text" id="state" required="required" maxlength="40">
							                           		<span class="error-message" id="stateError"></span>
						                           		</div>
						                           		<div class="half">
						                           			<label><spring:theme code="text.country"/></label>
						                           			<select id="country" >
						                           				<c:forEach var="countryName" items="${country}">
																	<option value="${countryName}">${countryName}</option>
																</c:forEach>
															</select>
						                           		</div>
						                           		<div class="full">
							                           		<label><spring:theme code="text.pincode"/></label>
							                           		<input type="text" id="pincode" maxlength="10" onchange="validatePin()" ><span class="error-message" id="pinError"></span>
						                           		</div>
					                           		</fieldset>
					                            </div> 
					                            <div class="controls">
					                            	<input type="checkbox" class="juspay_locker_save checkbox"  id="save-card" name="save-card" /><label for="save-card"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.saveCard"/></label>		                        	
					                            </div>
		            							<input type="hidden" class="redirect" value="${redirect}">	
			            					</div>
			            				</div>
			            			</form>
		            			</li>
		            		<li>
				<!-- Terms & Conditions Link -->
			            		<div class="pay newCardPayment">
									
									<button type="submit" class="make_payment button btn-block payment-button" id="make_cc_payment"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.paymentButton"/></button>
									<p class="payment-redirect">You will be re-directed to secure payment gateway</p>
									<p onclick="teliumTrack()"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc.pretext" /><a href="<c:url value="${tncLink}"/>" target="_blank"><spring:theme code="checkout.multi.paymentMethod.selectMode.tnc" /></a></p>
								</div>
							</li>
						</ul>					
					</li>	 --%>			
				<!-- Card ends -->
				</div>		
				</div>
							</div><!--  -->
		</jsp:body>
		</multiCheckout:checkoutSteps>	
		<multiCheckout:checkoutOrderDetails cartData="${cartData}" showDeliveryAddress="true" showPaymentInfo="false" showTaxEstimate="false" showTax="true" isCart="${isCart}" orderData="${orderData}"/>
	</div>	
	
	
	
</template:page>

	  <script type="text/javascript">
		jQuery(document).ready(function($){


			$(".payments.tab-view").smk_Accordion({
				closeAble: true, //boolean
				showIcon: true
			});

		});
	</script>

