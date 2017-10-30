<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- Accordtion Panel 2 For Select Return Type -->

<div class="payment-method">
										
							</div>
		
				<div class="accordtionTataCliq  secondTataCliq col-md-12">
					<div class="accHeading"><spring:theme code="text.order.returns.returntypelable" /></div>
					<c:set var="customerBankDetails" value="${customerBankDetails}"> </c:set>
					<div class="accContents reasonType col-md-12">
						<div class="col-md-4">
							<!-- <b>Return Type :</b> <br/> -->
							<div class="selectReason selectRefund col-md-12">
								<div class="selectRadio col-md-2 col-sm-2 col-xs-2">
							<!-- 	<input class="radioButton" onclick="changeRadioColor('refund')" type="radio" value="refund" name="return" checked /> -->
									<form:radiobutton class="radioButton" onclick="changeRadioColor('refund')" path="refundType"  value="R" checked="checked" />
								</div>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<b><spring:theme code="text.order.returns.returnandrefund"/></b> <br/>
									<%-- <div class="returnPayment">${subOrder.mplPaymentInfo.paymentOption}</div> --%>
									<c:choose>
									<c:when test="${subOrder.mplPaymentInfo.paymentOption eq 'COD'}">
									<div class="returnPayment">Cheque/NEFT</div>
									<span class="returnPaymentMessage"><spring:theme code="text.order.returns.codpaymentmessage" arguments="${subOrder.mplPaymentInfo.paymentOption}"/></span>
									</c:when>
									<c:otherwise>
									<div class="returnPayment">${subOrder.mplPaymentInfo.paymentOption}</div>  <%-- ${subOrder.mplPaymentInfo.paymentOption} --%>
									<span><spring:theme code="text.order.returns.cardpaymentmessage" arguments="${subOrder.mplPaymentInfo.paymentOption eq null ? '<b>CliqCash</b>' : subOrder.mplPaymentInfo.paymentOption}" /> </span>
									</c:otherwise>

									</c:choose>
									
								</div>
							</div>
							<!-- <div class="selectReason selectReplace col-md-12">
								<div class="selectRadio col-md-2 col-sm-2 col-xs-2">
									<input onclick="changeRadioColor('replace')" class="radioButton" type="radio" value="replace" name="return" disabled />
								</div>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<b>Return and Replace</b> <br/>
									<span>Estimated replacement timing: item will be shipped 1 day after we receive the item</span>
								</div>
							</div> -->
						</div>
						<!-- <div class="col-md-8 slectionReplace">
							<b>Choose Replacement Product</b> <br/><br/>
							<div class="row">
								<div class="col-md-3">
									<div class="image_holder">
										<img src="/_ui/desktop/theme-blue/images/missing-product-300x300.jpg" />
									</div>
								</div>
								<div class="col-md-7 productDetails">
										<span class="brandName">Nike</span><br/>
										<span class="productName">Nike Air Max 1</span><br/>
										<span class="productName">Ultra Moire</span><br/>
										<span class="otherAttributes"></span><br/>
										<span class="size">Size: </span><br/>
										<select name="size" class="sizeSelect" path="selectSizs">
											<option selected='selected' disabled="disabled">8</option>
												<option value="1">9</option>
												<option value="1">10</option>									
										</select><br/><br/>
										<span class="size">Colour: </span> <br/>
										<input class="checkradioButton" style="background: red;" type="radio" value="#000" name="color" checked />
										<input class="checkradioButton" style="background: green;" type="radio" value="#000" name="color" /> 
										<input class="checkradioButton" style="background: orange;" type="radio" value="#000" name="color" />  
								</div>
							</div>
						</div>
						 -->
						<c:choose>
						<c:when test="${subOrder.mplPaymentInfo.paymentOption eq 'COD'}">
						<div class="col-md-8 slectionRefund">
							<b class="bankDetailsText"></b>
							<b class="choosebankdetailsText"><spring:theme code="text.order.returns.choosebankdetails"/></b>
							<div class="suggestionText">
									<span><spring:theme code="text.order.returns.safetyinformationlable1"/></span>
									<span><spring:theme code="text.order.returns.safetyinformationlable2"/></span>
								</div>
							<div class="col-md-12 col-sm-12 col-xs12">
								<div class="col-md-4 col-sm-4 accountnumber">

									<b><spring:theme code="text.order.returns.accountnumber"/></b> <br/>
									<!-- Start INC144316970- SPACE in between bank details in COD_SELFSHIP -->
									<form:password onkeypress="return event.charCode >= 48 && event.charCode <= 57" maxlength="24" minlength="16" path="accountNumber" name="accountNumber" placeholder="Account Number" value="${customerBankDetails.bankAccount}" onkeyup="this.value=this.value.replace(/[^0-9]/g,''); this.value=this.value.trim().replace(/\s\s+/g,'');"/>
								    <!-- End INC144316970- SPACE in between bank details in COD_SELFSHIP -->
								</div>
								<div class="col-md-4 col-sm-4 reenteraccountnumber">
									<b><spring:theme code="text.order.returns.reenteraccountnumber"/></b> <br/>
									<!-- Start INC144316970- SPACE in between bank details in COD_SELFSHIP -->
									<form:input onkeypress="return event.charCode >= 48 && event.charCode <= 57" maxlength="24" minlength="16" type="text" path="reEnterAccountNumber" name="reaccountNumber" placeholder="Re-Account Number" value="${customerBankDetails.bankAccount}" onkeyup="this.value=this.value.replace(/[^0-9]/g,''); this.value=this.value.trim().replace(/\s\s+/g,'');"/>
								    <!-- End INC144316970- SPACE in between bank details in COD_SELFSHIP -->
								</div>
							</div>
							<div class="col-md-12 col-sm-12">
								<div class="col-md-4 col-sm-4 accountholdername">
									<b><spring:theme code="text.order.returns.accountholdername"/></b> <br/>
									<form:input type="text" onkeyup="this.value=this.value.replace(/[^A-z\s]/g,''); this.value=this.value.trim().replace(/\s\s+/g,'');"  maxlength="40"  path="accountHolderName" name="accountHolderName" placeholder="Account Holder Name" value="${customerBankDetails.name}" />
								</div>
							
								 <div class="col-md-3 col-sm-4">
									<b><spring:theme code="text.order.returns.refundmode"/></b> <br/>
									<form:select name="size" class="refundMode" path="refundMode" value="${customerBankDetails.transactionType}">
										    <form:option value="N">NEFT</form:option>
											<form:option value="R">RTGS</form:option>
											<form:option value="C">CHEQUE</form:option>
											<form:option value="B">Bank Transfer</form:option>						
									</form:select>
								</div>
								<div class="col-md-2 col-sm-2">
									<b><spring:theme code="text.order.returns.title"/></b> <br/>
									<form:select name="size" class="refundMode" path="title" value="${customerBankDetails.title}">
										    <form:option  value="mr" >MR</form:option>
											<form:option value="Mrs">MRs</form:option>
											<form:option value="Company">Company</form:option>			
											
											<form:option  value="0001" >Ms.</form:option>
											<form:option value="0002">Mr.</form:option>
											<form:option value="0003">Company</form:option>	
											<form:option value="0004">Mr. and Mrs.</form:option>		
									</form:select>
								</div> 
							</div>
							<div class="col-md-12 col-sm-12">
								<div class="col-md-4 col-sm-4 bankname">
									<b><spring:theme code="text.order.returns.bankname"/></b> <br/>
									<form:input  onkeyup="this.value=this.value.replace(/[^A-z\s]/g,''); this.value=this.value.trim().replace(/\s\s+/g,'');" path="bankName" type="text"  name="bankName" placeholder="Bank Name" value="${customerBankDetails.bankName}" />
								</div>
								<div class="col-md-4 col-sm-4 ifsccode">
									<b><spring:theme code="text.order.returns.ifsccode"/></b> <a href="#nogo" class="ifscPopOver"><spring:theme code="text.order.returns.reverseSeal.popUp"></spring:theme></a> <br/>
									<form:input  path="iFSCCode"  onkeyup="this.value=this.value.replace(/[^A-z0-9]/g,''); this.value=this.value.trim().replace(/\s\s+/g,'');"   maxlength="11" minlength="" type="text" name="ifscCode" placeholder="IFSC CODE"  value="${customerBankDetails.bankKey}"/>
								</div>
								<div class="col-md-4 col-sm-4 suggestionText" style="display : none;" id="ifscPopOverHelp">
									<span><spring:theme code="text.order.returns.safetyinformationlabl3"/></span><br/>
								</div>
							</div>
						
						</div>
						</c:when>
						<c:otherwise>
						<div class="col-md-7 slectionRefund">
							
							<div class="paymentCC">
								<b class="bankDetailsText"></b>
								<span class="paymentText">Your <b>transaction was completed</b> using</span><br/>
								<span class="paymentText">${subOrder.mplPaymentInfo.paymentOption}</span>
								
								<c:if  test="${subOrder.mplPaymentInfo.paymentOption eq null}">
									<b>CliqCash</b>
								</c:if>
								
								<c:set var="cardNumberMasked"
									value="${subOrder.mplPaymentInfo.cardIssueNumber}" />
								<c:set var="cardNumberLength"
									value="${fn:length(cardNumberMasked)}" />
								<c:set var="cardNumEnd"
									value="${fn:substring(cardNumberMasked, cardNumberLength-4, cardNumberLength)}" />

								<c:if
									test="${subOrder.mplPaymentInfo.paymentOption eq 'Credit Card'}">
									<c:set var="creditCardBillingAddress"
										value="${subOrder.mplPaymentInfo.billingAddress}" />
								</c:if>
								<!--  TISBOX-1182 -->
								<span class="paymentText">${subOrder.mplPaymentInfo.cardAccountHolderName}</span><br/>
								<c:if
									test="${subOrder.mplPaymentInfo.paymentOption eq 'Credit Card' or 'EMI' or 'Debit Card'}">
									<span class="paymentText"><b>${subOrder.mplPaymentInfo.cardCardType} ending in
										${cardNumEnd}</b></span><br/>
									<span class="paymentText">Expires on:
										${subOrder.mplPaymentInfo.cardExpirationMonth}/${subOrder.mplPaymentInfo.cardExpirationYear}</span><br/>
								</c:if>
								<c:if
									test="${subOrder.mplPaymentInfo.paymentOption eq 'Netbanking'}">
									<p>${subOrder.mplPaymentInfo.bank}</p>
								</c:if>
									
							</div>		
						</div>
						</c:otherwise>
						</c:choose>
						
						<p style="clear:both"></p>
						<div class="button_holder">
							<button type="button" onclick="goBackToFirstTataCliq()" class="light-blue newRequest" >Back</button> 
							<button onclick="checkSecondTataCliq('${subOrder.mplPaymentInfo.paymentOption}')"  class="light-blue submit-request" ><spring:theme code="text.returRequest.continueButton" text="Continue"/></button>
						</div>
					</div>
				</div>
				
