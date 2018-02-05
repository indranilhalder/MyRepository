<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<template:page pageTitle="${pageTitle}">
	<div>
		<br />
		<div class="clearfix">
			<div class="col-sm-6 giftContSub giftContSub1">
				<div class="giftFinalTempContainer">
					<br />
					<div class="giftFinalTemp">
						<div class="giftFinalTempTop">
							<img src="../_ui/responsive/theme-blue/images/GiftCard.jpg" />
						</div>
						<div class="giftFinalTempMiddle">
							<span><i id="updatedGiftCardMessageText"><spring:theme
										code="egv.product.msg.default" /></i></span>
						</div>
						<div class="giftFinalTempBottom">
							<span>&#8377;<span id="updatedCustomGiftValue"></span></span>
						</div>
					</div>
				</div>
			</div>

			<div class="col-sm-6 giftContSub giftContSub2">

				<div class="clearfix">
					<div class="">
						<div class="col-sm-2">
							<!-- <span class="Gift_Main_Heading">Tata CliQ</span> -->
						</div>
						<div class="col-sm-10">
							<span class="Gift_Card"><spring:theme
									code="egv.product.msg.giftCard" /></span> <br />&nbsp;
						</div>
					</div>
				</div>
				<!-- <div class="clearfix">
				<div class="col-sm-2 stepTexts">
					STEP &nbsp;<span class="Oval">1</span><br />&nbsp; 
				</div>
				<div class="col-sm-10">
					<span>SELECT A DESIGN FOR YOUR GIFT CARD</span><br />&nbsp;
				</div>
			</div>
			<div class="clearfix">
				<div class="col-xs-3">
					<label class="giftLabel" for="giftTemplate1">
					  <img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" class="img-rounded giftImg" alt="Gift Template" />
					</label>
				</div>
				<div class="col-xs-3">
					<label class="giftLabel" for="giftTemplate2">
					  <img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/743A55D2-5FEB-4F1D-ADD8-5DC670C2C4A6.png" class="img-rounded giftImg" alt="Gift Template" />
					</label>
				</div>
				<div class="col-xs-3">
					<label class="giftLabel" for="giftTemplate3">
					  <img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/79F15840-93C9-42C2-BD1A-D552CC00DB83.png" class="img-rounded giftImg" alt="Gift Template" />
					</label>
				</div>
				<div class="col-xs-3">
					<label class="giftLabel" for="giftTemplate4">
					  <img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" class="img-rounded giftImg" alt="Gift Template" />
					</label>
				</div>
			</div>
			<br />&nbsp; -->
				<div class="clearfix">
					<div class="col-sm-12">
						<!-- <span id="showMoreGiftDesignTemplates" class="moreGiftTemplates">See More Designs</span><br />&nbsp; -->
						<div id="moreGiftDesignTemplates" class="giftDesignModel">
							Modal content
							<div class="giftDesignModelContent">
								<span class="giftDesignModelClose">&times;</span>
								<div class="giftModalContainer clearfix">
									<div class="giftCategoryTab">
										<button class="tablinks"
											onclick="selectCategory(event, 'giftBirthdays')"
											id="defaultGiftCategory">Birthdays</button>
										<button class="tablinks"
											onclick="selectCategory(event, 'giftAnniversary')">Anniversary</button>
										<button class="tablinks"
											onclick="selectCategory(event, 'giftFestivals')">Festivals</button>
										<button class="tablinks"
											onclick="selectCategory(event, 'giftGraduations')">Graduations</button>
										<button class="tablinks"
											onclick="selectCategory(event, 'giftBrands')">Brands</button>
									</div>

									<div id="giftBirthdays" class="giftCategoryTabcontent">
										<div class="extGiftTempItem col-sm-3">
											<div class="extGiftTemplateImg">
												<img
													src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" />
											</div>
											<div>
												<p>
													<strong>Tanishq</strong>
												</p>
												<p class="extGiftDesc">Special Birthday Gift Cards with
													additional benifits...</p>
												<br />
												<p>
													<strong>&#8377; 500</strong>
												</p>
											</div>
										</div>

										<div class="extGiftTempItem col-sm-3">
											<div class="extGiftTemplateImg">
												<img
													src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" />
											</div>
											<div>
												<p>
													<strong>Tanishq</strong>
												</p>
												<p class="extGiftDesc">Special Birthday Gift Cards with
													additional benifits...</p>
												<br />
												<p>
													<strong>&#8377; 500</strong>
												</p>
											</div>
										</div>

										<div class="extGiftTempItem col-sm-3">
											<div class="extGiftTemplateImg">
												<img
													src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" />
											</div>
											<div>
												<p>
													<strong>Tanishq</strong>
												</p>
												<p class="extGiftDesc">Special Birthday Gift Cards with
													additional benifits...</p>
												<br />
												<p>
													<strong>&#8377; 500</strong>
												</p>
											</div>
										</div>

										<div class="extGiftTempItem col-sm-3">
											<div class="extGiftTemplateImg">
												<img
													src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" />
											</div>
											<div>
												<p>
													<strong>Tanishq</strong>
												</p>
												<p class="extGiftDesc">Special Birthday Gift Cards with
													additional benifits...</p>
												<br />
												<p>
													<strong>&#8377; 500</strong>
												</p>
											</div>
										</div>

										<div class="extGiftTempItem col-sm-3">
											<div class="extGiftTemplateImg">
												<img
													src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" />
											</div>
											<div>
												<p>
													<strong>Tanishq</strong>
												</p>
												<p class="extGiftDesc">Special Birthday Gift Cards with
													additional benifits...</p>
												<br />
												<p>
													<strong>&#8377; 500</strong>
												</p>
											</div>
										</div>

										<div class="extGiftTempItem col-sm-3">
											<div class="extGiftTemplateImg">
												<img
													src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" />
											</div>
											<div>
												<p>
													<strong>Tanishq</strong>
												</p>
												<p class="extGiftDesc">Special Birthday Gift Cards with
													additional benifits...</p>
												<br />
												<p>
													<strong>&#8377; 500</strong>
												</p>
											</div>
										</div>

										<div class="extGiftTempItem col-sm-3">
											<div class="extGiftTemplateImg">
												<img
													src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" />
											</div>
											<div>
												<p>
													<strong>Tanishq</strong>
												</p>
												<p class="extGiftDesc">Special Birthday Gift Cards with
													additional benifits...</p>
												<br />
												<p>
													<strong>&#8377; 500</strong>
												</p>
											</div>
										</div>

										<div class="extGiftTempItem col-sm-3">
											<div class="extGiftTemplateImg">
												<img
													src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" />
											</div>
											<div>
												<p>
													<strong>Tanishq</strong>
												</p>
												<p class="extGiftDesc">Special Birthday Gift Cards with
													additional benifits...</p>
												<br />
												<p>
													<strong>&#8377; 500</strong>
												</p>
											</div>
										</div>

										<div class="extGiftTempItem col-sm-3">
											<div class="extGiftTemplateImg">
												<img
													src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" />
											</div>
											<div>
												<p>
													<strong>Tanishq</strong>
												</p>
												<p class="extGiftDesc">Special Birthday Gift Cards with
													additional benifits...</p>
												<br />
												<p>
													<strong>&#8377; 500</strong>
												</p>
											</div>
										</div>
									</div>

									<div id="giftAnniversary" class="giftCategoryTabcontent">
										<h3>Anniversary</h3>
									</div>

									<div id="giftFestivals" class="giftCategoryTabcontent">
										<h3>Festivals</h3>
									</div>

									<div id="giftGraduations" class="giftCategoryTabcontent">
										<h3>Graduations</h3>
									</div>

									<div id="giftBrands" class="giftCategoryTabcontent">
										<h3>Brands</h3>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
				<div class="clearfix">
					<div class="col-sm-2 stepTexts">
						<!-- STEP &nbsp;<span class="Oval">1</span> -->
						<br />&nbsp;
					</div>
					<div class="col-sm-10">
						<span><spring:theme code="egv.product.msg.enterCardDetails" /></span><br />&nbsp;

						<c:if test="${not empty erroMsg}">
							<div id="backend_validation_section"
								class="backend_validation_fail">
								<span style="color: red;"> ${erroMsg} </span>
							</div>
						</c:if>
						<br />&nbsp;
					</div>
				</div>
				<form:form method="POST" id="egvDetailsform"
					onsubmit="return validateEgvForm();"
					action="${request.contextPath}/checkout/multi/payment-method/giftCartPayment"
					commandName="egvDetailsform">
					<div class="clearfix">
						<div class="col-sm-2 headingTexts">
							<spring:theme code="egv.product.msg.giftAmount" />
							<br />&nbsp;
						</div>
						<div class="col-sm-10">
							<div class="btn-group" data-toggle="buttons">
								<c:forEach items="${amountList}" var="amount">
									<span class="btn giftAmountBtns"> <input type="radio"
										name="giftRange" value="${amount}">&#8377; ${amount}
									</span>
								</c:forEach>
							</div>
							<div>
								<br />
								<spring:theme code="egv.product.msg.or" />
								<br />&nbsp;


								<div class="alert alert-warning" id="customAmountError"></div>
							</div>
							<input class="giftCard_input" id="customGiftValue" type="text"
								maxlength="5" placeholder="Enter Custom Amount"
								onkeypress="return isNumber(event)" /><br />&nbsp;
							<form:input path="giftRange" type="hidden" id="customAmount" />
							<form:input path="productCode" type="hidden"
								value="${product.code}" id="productCode" />
						</div>
					</div>
					<div class="clearfix">
						<div class="col-sm-2 headingTexts">
							<spring:theme code="egv.product.msg.to" />
							<br />&nbsp;
						</div>
						<div class="col-sm-10">
							<form:input path="toEmailAddress"
								class="giftCard_input giftCard_toEmail" type="email"
								placeholder="Enter Recipient e-mail Address" />
							<br />&nbsp;
							<div class="alert alert-warning" id="giftCardEmailError"></div>
						</div>
					</div>
					<%-- <div class="clearfix">
						<div class="col-sm-2 headingTexts">
							<spring:theme code="egv.product.msg.firstName" />
							<br />&nbsp;
						</div>
						<div class="col-sm-10">
							<form:input path="fromFirstName"
								class="giftCard_input giftCard_fromFirstName"
								id="giftCard_fromFirstName" type="text" placeholder="First Name" />
							<br />&nbsp;
							<div class="alert alert-warning" id="giftCardFromFirstNameError"></div>
						</div>
					</div> --%>
					<%-- <div class="clearfix">
						<div class="col-sm-2 headingTexts">
							<spring:theme code="egv.product.msg.lastName" />
							<br />&nbsp;
						</div>
						<div class="col-sm-10">
							<form:input path="fromLastName"
								class="giftCard_input giftCard_fromLastName"
								id="giftCard_fromLastName" type="text" placeholder="Last Name" />
							<br />&nbsp;
							<div class="alert alert-warning" id="giftCardFromLastNameError"></div>
						</div>
					</div> --%>
					<div class="clearfix">
						<div class="col-sm-2 headingTexts">
							<spring:theme code="egv.product.msg.from" />
							<br />&nbsp;
						</div>
						<div class="col-sm-10">
							<form:input path="fromEmailAddress"
								class="giftCard_input giftCard_fromName" id="giftCard_fromName"
								type="text" placeholder="From Name" />
							<br />&nbsp;
							<div class="alert alert-warning" id="giftCardFromNameError"></div>
						</div>
					</div>
					<%-- <div class="clearfix">
						<div class="col-sm-2 headingTexts">
							<spring:theme code="egv.product.msg.phone" />
							<br />&nbsp;
						</div>
						<div class="col-sm-10">
							<form:input path="fromPhoneNo"
								class="giftCard_input giftCard_phoneNo" type="text"
								maxlength="10" placeholder="Mobile Number"
								onkeypress="return isNumber(event)" />
							<br />&nbsp;
							<div class="alert alert-warning" id="giftCardPhoneNoError"></div>
						</div>
					</div> --%>
					<div class="clearfix">
						<div class="col-sm-2 headingTexts">
							<spring:theme code="egv.product.msg.message" />
							<br />&nbsp;
						</div>
						<div class="col-sm-10">
							<input type="hidden" id="customAmount" />
							<form:textarea path="messageBox" id="giftCardMessageText"
								class="giftCard_textarea" maxlength="150"
								placeholder="Write a message"></form:textarea>
							<br />&nbsp;
						</div>
					</div>
					<div class="clearfix">
						<div class="col-sm-2">&nbsp;</div>
						<div class="col-sm-10">
							<spring:theme code="egv.product.msg.qty" />
							&nbsp; <input type="text" class="qtyField" disabled value="1" />
							<c:choose>
								<c:when test="${isOTPValidtion ne true}">
									<div class="walletCreate">
										<button type="button" onclick="createWallet()"
											class="giftBuyBtn pull-right">
											<spring:theme code="egv.product.msg.buyNow" />
										</button>
									</div>
								</c:when>
								<c:otherwise>
									<button type="submit" class="giftBuyBtn pull-right">
										<spring:theme code="egv.product.msg.buyNow" />
									</button>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</form:form>

			</div>

			<div class="col-sm-6 giftContSub giftContSub3">
				<div class="col-sm-12">
					<br /> &nbsp;
					<div class="giftTermsDesc">
						<span><spring:theme code="egv.product.msg.soldBy" /><strong><spring:theme
									code="egv.product.msg.qc" /></strong> </span>
					</div>
				</div>
			</div>
		</div>
	</div>
	<br />&nbsp;
<div class="createWalletModel" id="createWalletPopup">
		<div class="createWalletModel-content">
		<span class="glyphicon glyphicon-remove-circle" onclick="closepop()" id="closePop"></span>
			<div id="createWalletData"></div>

		</div>

	</div>
</template:page>
<script>
	
//Allowing only numbers
function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}

//Form Validation for Gift Amount
$("#customAmountError").hide();
$("#giftCardFromNameError").hide();
$("#giftCardFromFirstNameError").hide();
$("#giftCardFromLastNameError").hide();
$("#giftCardEmailError").hide();
$("#giftCardPhoneNoError").hide();
var formValid = true;
function validateEgvForm() {
	formValid = true;
	var fname = $(".giftCard_fromName").val();
	/* var firstName = $(".giftCard_fromFirstName").val();
	var lastName = $(".giftCard_fromLastName").val(); */
	/* var fMobile = $(".giftCard_phoneNo").val(); */
	var toEmail = $(".giftCard_toEmail").val();
	var letters = new RegExp(/^[A-z]*$/);
	var emailValidExpression = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	
	if(document.getElementById('customAmount').value >= 15 && document.getElementById('customAmount').value < 15001) {
		$("#customAmountError").hide();
	} else {
		$("#customAmountError").show();
		$("#customAmountError").text('Please enter amount from 15 to 15000.');
		formValid = false;
	}
	
  //Name Validation
	if(fname == null || fname.trim() == '' ){
			$("#giftCardFromNameError").show();
			$("#giftCardFromNameError").text("From name cannot be blank.");
			formValid = false;
	}else {
		$("#giftCardFromNameError").hide();
	}
  
	//First Name Validation
	 /* if(firstName == null || firstName.trim() == '' ){
			$("#giftCardFromFirstNameError").show();
			$("#giftCardFromFirstNameError").text("First name cannot be blank.");
			formValid = false;
	}else {
		$("#giftCardFromFirstNameError").hide();
	}
	 */
	//Last Name Validation
	/*  if(lastName == null || lastName.trim() == '' ){
			$("#giftCardFromLastNameError").show();
			$("#giftCardFromLastNameError").text("Last name cannot be blank.");
			formValid = false;
	}else {
		$("#giftCardFromLastNameError").hide();
	} */
  
  //Mobile No Validation
	/* if(fMobile == null || fMobile.trim() == '' ){
			$("#giftCardPhoneNoError").show();
			$("#giftCardPhoneNoError").text("Phone number cannnot be blank.");
			formValid = false;
	}else if(isNaN(fMobile)){
		$("#giftCardPhoneNoError").show();
		$("#giftCardPhoneNoError").text("Phone number is invalid.");
			formValid = false;
	}else {
		$("#giftCardPhoneNoError").hide();
	} */
  
	//Email Validation
	 if(toEmail == null || toEmail.trim() == '' ){
			$("#giftCardEmailError").show();
			$("#giftCardEmailError").text("Email cannot be blank.");
			formValid = false;
	}else if(emailValidExpression.test(toEmail) == false){
		$("#giftCardEmailError").show();
		$("#giftCardEmailError").text("Please enter valid email address.");
			formValid = false;
	} else {
		$("#giftCardEmailError").hide();
	}
	 
	 
	 return formValid;
}


  $(document).ready(function (){
	  if($("#backend_validation_section").hasClass("backend_validation_fail")){
			setTimeout(function() {
				$("#backend_validation_section").hide();
			}, 5000);
		}
	  
		//Gift Cards Code
	     $(".giftLabel").click(function() {
	     	//alert("Click");
	     	$(".giftLabel").children().css("border", "none");
	   	 	$(this).children().css("border", "1px solid #a9133d");
	   	 	if($(window).width()>650){
	   	 		$(this).children().css("border-radius", "8px");
	   	 	} else {
	   	 		$(this).children().css("border-radius", "4px");
	   	 	}
	   	 	//this.children().css("pading", "5px"); 
	     });
		
		//Update Gift Template
		//Updating Card
		//$(".giftFinalTempTop").css('background','url(\_ui\responsive\theme-blue\images\GiftCard.jpg) no-repeat center');
		
		$(".giftLabel").click(function (){
			var giftImg = $(this).find('img').attr('src');
			$(".giftFinalTempTop").css('background-color','transparent');
			$(".giftFinalTempTop").css('background-image','url('+giftImg+')');
			//$(".giftFinalTempTop").css('background-repeat','no-repeat');
		});
		
		$(".extGiftTempItem").click(function (){
			var giftImg = $(this).find('img').attr('src');
			$(".giftFinalTempTop").css('background-color','transparent');
			$(".giftFinalTempTop").css('background-image','url('+giftImg+')');
			document.getElementById('moreGiftDesignTemplates').style.display = 'none';
		});
		
		//Updating Message
		$("#giftCardMessageText").on('keyup blur', function(){
	       var giftCardMessage = document.getElementById('updatedGiftCardMessageText');
	       giftCardMessage.style.fontStyle = 'normal';
	       giftCardMessage.style.wordWrap = 'break-word';
	       giftCardMessage.innerHTML = '&nbsp; &nbsp; &nbsp; &nbsp; '+$(this).val();
	    });
		
		//Auto Update Name from First and Last names
		/* $(".giftCard_fromFirstName, .giftCard_fromLastName").on('keyup blur', function(){
			var firstName = document.getElementById('giftCard_fromFirstName').value;
			var lastName = document.getElementById('giftCard_fromLastName').value;
			document.getElementById('giftCard_fromName').value = firstName+" "+lastName;
		    }); */
		
		//Updating Amount
		$("input[name=giftRange]").on('change', function (){
			document.getElementById('customGiftValue').value = '';
			document.getElementById('updatedCustomGiftValue').innerHTML = $("input[name=giftRange]:checked").val();
			document.getElementById('customAmount').value = $("input[name=giftRange]:checked").val();
		});
		
		$("#customGiftValue").on('keyup blur', function(){
			$('input[name=giftRange]:checked').parents().find('.active').removeClass('active');
			$("input[name=giftRange]").prop('checked', false);
	       document.getElementById('updatedCustomGiftValue').innerHTML = $(this).val();
	       document.getElementById('customAmount').value = $(this).val();
	    });
	});
	
	  // GIFT DESIGN POPUP
    // Get the modal
		/* var giftDesignModal = document.getElementById('moreGiftDesignTemplates');
		
		// Get the button that opens the modal
		var giftDesignBtn = document.getElementById("showMoreGiftDesignTemplates");
		
		// Get the <span> element that closes the modal
		var giftDesignClose = document.getElementsByClassName("giftDesignModelClose")[0];
		
		// When the user clicks the button, open the modal 
		giftDesignBtn.onclick = function() {
			giftDesignModal.style.display = "block";
		}
		
		// When the user clicks on <span> (x), close the modal
		giftDesignClose.onclick = function() {
			giftDesignModal.style.display = "none";
		}
		
		// When the user clicks anywhere outside of the modal, close it
		window.onclick = function(event) {
		    if (event.target == giftDesignModal) {
		    	giftDesignModal.style.display = "none";
		    }
		} */
		
	// GIFT CATEGORY MENU
	function selectCategory(evt, categoryName) {
	    var i, tabcontent, tablinks;
	    tabcontent = document.getElementsByClassName("giftCategoryTabcontent");
	    for (i = 0; i < tabcontent.length; i++) {
	        tabcontent[i].style.display = "none";
	    }
	    tablinks = document.getElementsByClassName("tablinks");
	    for (i = 0; i < tablinks.length; i++) {
	        tablinks[i].className = tablinks[i].className.replace(" active", "");
	    }
	    document.getElementById(categoryName).style.display = "block";
	    evt.currentTarget.className += " active";
	}
	
	// Get the element with id="defaultOpen" and click on it
	document.getElementById("defaultGiftCategory").click();
  </script>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">
var createWalletModel = document.getElementById('createWalletPopup');
var createWalletData = document.getElementById('createWalletData');
var createWalletPopup = document.getElementById('createWalletPopup');
function createWallet() {
	if(validateEgvForm()) {
		$.ajax({
			type : "GET",
			url : ACC.config.encodedContextPath+ "/wallet/walletOTPPopup",
			contentType : "html/text",
			success : function(response){
				createWalletData.innerHTML=response;
				createWalletModel.style.display = "block"; 
					  },	
					failure : function(data) {
					}
				}); 
	}
}

function closepop(){
	
	    	createWalletModel.style.display = "none";
	
}
window.onclick = function(event) {
/*     if (event.target == createWalletModel) {
    	createWalletModel.style.display = "none";
    } */
}
/* createWalletPopup.onclick = function() {
	createWalletModel.style.display = "none";
} */
var count=0;
function createWalletOTP(){
	    $(".mobileNumberError").hide();
	    $(".otpLastNameError").hide();
	    $(".otpFirstNameError").hide();
	    var mobileNo=$("#otpPhonenumber").val();
	    var firstName = $("#otpFirstName").val();
		var lastName = $("#otpLastName").val();
		var isFirstName = false;
		var isLastName = false;
		
     var isString = isNaN(mobileNo);

 	//First Name Validation
 	if(firstName == null || firstName.trim() == '' ){
 			$(".otpFirstNameError").show();
 			$(".otpFirstNameError").text("First name cannot be blank.");
 			isFirstName = false;
 	}else {
 		$(".otpFirstNameError").hide();
 		isFirstName = true;
 	}
 	
 	//Last Name Validation
     if(lastName == null || lastName.trim() == '' ){
 			$(".otpLastNameError").show();
 			$(".otpLastNameError").text("Last name cannot be blank.");
 			isLastName = false;
 	}else {
 		$(".otpLastNameError").hide();
 		isLastName = true;
 	} 
     if(isString==true || mobileNo.trim()==''){
			$(".mobileNumberError").show();
			$(".mobileNumberError").text("Enter only numbers");
	  	}
     else if(!/^[0-9]+$/.test(mobileNo))
        {
	  		  $(".mobileNumberError").show();
	          $(".mobileNumberError").text("Enter only Numbers");
      }
     else if(mobileNo.length > 0 && mobileNo.length < 9 ){
	    	  $(".mobileNumberError").show();
	          $(".mobileNumberError").text("Enter correct mobile number");
	  	}
     else{
    	 
   	 if(isFirstName && isLastName) {
   		count++;
   	 	var staticHost = $('#staticHost').val();
   		$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
   		$("body").append('<div class="loaderDiv" style="position: fixed; left: 45%;top:45%;z-index: 10000"><img src="'+staticHost+'/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
   		if(count <= 4){
   		 $.ajax({
   				type : "POST",
   				url : ACC.config.encodedContextPath + "/wallet/walletCreateOTP",
   				data :"mobileNumber="+mobileNo,
   				success : function(response) {
   					$("#no-click,.loaderDiv").remove();
   					if(response =='isUsed'){
   						$(".mobileNumberError").show();
   						$(".mobileNumberError").text("This mobile number is alredy used. Please enter different number and try again");
   						$('#otp-submit-section').hide();
   					}else{
   					$(".wcOTPError").show();
   					$(".wcOTPError").html("<span class='text-success'>OTP sent succesfully</span>");
   					$('#otp-submit-section').show();
   					}
   				}
   			}); 
   		 
   		}else{
   			$(".otpError").show();
   			$(".otpError").text("OTP limt exceeded 5 times, pleae try again");
   		}
   	 }
	  	}
} 

function editOtpField(fieldId) {
	var value = document.getElementById(fieldId);
	value.disabled = false;
	value.focus();
}

function submitWalletData(){
	var data = $("#walletForm").serialize();
	$(".mobileNumberError").hide();
	$(".wcOTPError").hide();
	var staticHost = $('#staticHost').val();
	$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append('<div class="loaderDiv" style="position: fixed; left: 45%;top:45%;z-index: 10000"><img src="'+staticHost+'/_ui/responsive/common/images/red_loader.gif" class="spinner"></div>');
	
	 $.ajax({
			type : "GET",
			url : ACC.config.encodedContextPath + "/wallet/validateWalletOTP",
			data :data,
			contentType: "text/application/html",
			success : function(response) {
				$("#no-click,.loaderDiv").remove();
			   if(response =="isUsed"){
					$(".mobileNumberError").text("This mobile number is alredy used. Please enter other mobile number for this account");
					$(".mobileNumberError").show();
				}
				else if(response=='OTPERROR'){
					$(".wcOTPError").text("OTP verification failed. Please try again");
					$(".wcOTPError").show();
				}
				else if(response=='qcDown'){
					$(".wcOTPError").text("Unable to verify mobile number due to server error. Please try after sometime");
					$(".wcOTPError").show();
				}
				else if(response == 'success'){
                    $('#egvDetailsform').submit();
				}else{
					$(".wcOTPError").text(response);
					$(".wcOTPError").show();
				}
				
			}
		}); 
} 

</script>
