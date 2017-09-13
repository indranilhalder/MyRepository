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


<style>
	.Tanishq {
	  font-family: Montserrat;
	  font-size: 12px;
	  line-height: 1.33;
	  letter-spacing: 0.6px;
	  text-align: left;
	  color: #000000;
	  color: var(--black);
	}
	
	.Diwali-GIft-Card {
	  font-family: Montserrat;
	  font-size: 20px;
	  line-height: 1.4;
	  letter-spacing: 0.6px;
	  text-align: left;
	  color: #000000;
	  color: var(--black);
	}
	
	.Oval {
	  background-color: #a9133d;
	  color: white;
	  border-radius: 50%;
	  padding: 0px 5px;
	}

	/* .giftContSub {z-index: 9;} */
	.giftLabel img {width: 100%;}
	.giftCard_input {
	  width: 100%;
	  height: 37px !important;
	  border-radius: 4px;
	  border: none !important;
	  background-color: #ededed;
	}
	.giftCard_textarea {
	  width: 100%;
	  max-width: 510px;
	  padding: 10px;
	  min-height: 153px;
	  border-radius: 4px;
	  border: none !important;
	  background-color: #ededed;
	  font-size: 12px;
	}
	.giftLabel {cursor: pointer;}
	.moreGiftTemplates {color: #a9133d; cursor: pointer;}
	.giftAmountBtns {
	  width: 72px;
	  height: 32px;
	  margin: 5px;
	  border-radius: 4px;
	  background-color: #ffffff;
	  background-color: var(--white-two);
	  box-shadow: 0 1px 1px 0 rgba(0, 0, 0, 0.18);
	}
	.giftAmountBtns:focus {border-color: #a9133d;}
	.qtyField {width: 35px; height: 35px; border-radius: 4px; text-align: center; padding: 0 !important;}
	.textRight {text-align: right;}
	.giftBuyBtn {
	  width: 100%;
	  max-width: 200px;
	  min-width: 140px;
	  height: 42px;
	  line-height: 12px;
	  background-color: #a9143c;
	  color: white;
	  font-size: 12px;
	  font-weight: 400;
	}
	.btn.active {border: 1px solid #a9133d;}
	
	.giftImgRight {width: 500px;}
	
	.giftFinalTemp {
		border-radius: 8px;
		border: 1px solid #ccc;
		box-shadow: 0px 0px 10px #888888;
	}
	
	.giftFinalTempTop {
		height: 250px;
		background-color: yellowgreen;
		border-top-left-radius: 10px; border-top-right-radius: 10px; 
		/* background-image: url("https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png");
		background-repeat: no-repeat; */
	}
	
	.giftFinalTempMiddle {
		padding: 15px;
		color: darkgrey;
		min-height: 100px;
		line-height: middle;
		border-top: 1px solid #ccc;
	}
	
	.giftFinalTempBottom {
		padding: 15px;
		font-size: 30px;
		border-top: 1px solid #ccc;
	}
	
	.giftTermsDesc {
		font-size: 12px;
		color: darkgrey;
		text-align: right;
	}
		
	
	@media(max-width: 650px){
		.giftContSub {padding-bottom: 10%;}
		.giftContSub1 {border-bottom: 1px solid #ddd;}
		.giftContSub2 {padding-top: 5%;}
		.giftTermsDesc {text-align: left;}
		.giftCategoryTab button {padding: 5px !important; font-size: 8px !important; overflow: hidden;}
		.giftBuyBtn {max-width: 100%; bottom: 0; left: 0; right: 0; position: fixed; z-index: 12;}
	}
	
	@media(min-width: 651px){
		.headingTexts {text-align: right; padding-top: 2%;}
		.giftContSub1 {float: right;}
		.giftContSub1, .giftContSub3 {padding-left: 10%;}
		/* .stepTexts {text-align: center;} */
	}
	
	/* POPUP CODE */
	/* The Modal (background) */
	.giftDesignModel {
	    display: none; /* Hidden by default */
	    position: fixed; /* Stay in place */
	    z-index: 99999; /* Sit on top */
	    padding-top: 90px; /* Location of the box */
	    left: 0;
	    top: 0;
	    width: 100%; /* Full width */
	    height: 100%; /* Full height */
	    overflow: auto; /* Enable scroll if needed */
	    background-color: rgb(0,0,0); /* Fallback color */
	    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
	}
	
	/* Modal Content */
	.giftDesignModelContent {
	    background-color: #fefefe;
	    margin: auto;
	    padding: 20px;
	    border: 1px solid #888;
	    width: 80%;
	}
	
	/* The Close Button */
	.giftDesignModelClose {
	    float: right;
	    font-size: 28px;
	    font-weight: bold;
	    top: 80px;
	    right: 9%;
	    background-color: black;
	    color: white;
	    padding: 1px 7px;
	    border-radius: 50%;
	    position: absolute;
	}
	
	.giftDesignModelClose:hover,
	.giftDesignModelClose:focus {
	    cursor: pointer;
	}
	
	.giftModalContainer {
	
	}
	
	.giftCategoryTab {
		float: left;
	    border: 1px solid #ccc;
	    border-right: none;
	    background-color: #f1f1f1;
	    width: 15%;
	    height: 500px;
	}
	
	.giftCategoryTab button {
	    display: block;
	    background-color: inherit;
	    color: black;
	    font-size: 14px !important;
	    padding: 15px;
	    width: 100%;
	    border: none;
	    outline: none;
	    text-align: left;
	    cursor: pointer;
	    transition: 0.3s;
	    font-size: 17px;
	}
	
	.giftCategoryTab button:hover {
	    background-color: #ddd;
	}
	
	.giftCategoryTab button.active {
	    background-color: #fff;
	}
	
	.giftCategoryTabcontent {
	    float: left;
	    padding: 12px;
	    border: 1px solid #ccc;
	    width: 85%;
	    border-left: none;
	    height: 500px;
	    overflow-y: auto;
	}
	
	.extGiftTempItem {
	    cursor: pointer;
	}
	
	.extGiftTempItem:hover {
		border: 1px solid #a9133d;
	}
	
	.extGiftTempItem p {
		padding: 0px 8px;
	}
	
	.extGiftTemplateImg {
		height: 230px;
	}
	
	.extGiftDesc {
		height: 24px;
		overflow: hidden;
	}
	
	.extGiftTemplateImg img {
		width: 100%;
	}
</style>
<template:page pageTitle="${pageTitle}">
<div class="clearfix">
	<div class="col-sm-6 giftContSub giftContSub1">
		<div class="col-sm-12">
			<br />
			<div class="giftFinalTemp">
				<div class="giftFinalTempTop">
					
				</div>
				<div class="giftFinalTempMiddle">
					<span><i id="updatedGiftCardMessageText">Your message here</i></span>
				</div>
				<div class="giftFinalTempBottom">
					<span>&#8377;<span id="updatedCustomGiftValue"></span></span>
				</div>
			</div>
		</div>
	</div>
	
	<div class="col-sm-6 giftContSub giftContSub2">
		
		<div class="clearfix">
			<div class="col-sm-12">
				<span class="Tanishq">Tanishq</span><br />
				<span class="Diwali-GIft-Card">Diwali Gift Card</span><br />&nbsp;
			</div>
		</div>
		<div class="clearfix">
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
		<br />&nbsp;
		<div class="clearfix">
			<div class="col-sm-12">
				<span id="showMoreGiftDesignTemplates" class="moreGiftTemplates">See More Designs</span><br />&nbsp;
				<div id="moreGiftDesignTemplates" class="giftDesignModel">
					  Modal content
					  <div class="giftDesignModelContent">
						<span class="giftDesignModelClose">&times;</span>
						<div class="giftModalContainer clearfix">
							<div class="giftCategoryTab">
							  <button class="tablinks" onclick="selectCategory(event, 'giftBirthdays')" id="defaultGiftCategory">Birthdays</button>
							  <button class="tablinks" onclick="selectCategory(event, 'giftAnniversary')">Anniversary</button>
							  <button class="tablinks" onclick="selectCategory(event, 'giftFestivals')">Festivals</button>
							  <button class="tablinks" onclick="selectCategory(event, 'giftGraduations')">Graduations</button>
							  <button class="tablinks" onclick="selectCategory(event, 'giftBrands')">Brands</button>
							</div>
							
							<div id="giftBirthdays" class="giftCategoryTabcontent">
								  <div class="extGiftTempItem col-sm-3">
									<div class="extGiftTemplateImg"><img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" /></div>
									<div>
										<p><strong>Tanishq</strong></p><p class="extGiftDesc">Special Birthday Gift Cards with additional benifits...</p>
										<br /><p><strong>&#8377; 500</strong></p>
									</div>
								  </div>
								  
								  <div class="extGiftTempItem col-sm-3">
									<div class="extGiftTemplateImg"><img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" /></div>
									<div>
										<p><strong>Tanishq</strong></p><p class="extGiftDesc">Special Birthday Gift Cards with additional benifits...</p>
										<br /><p><strong>&#8377; 500</strong></p>
									</div>
								  </div>
								  
								  <div class="extGiftTempItem col-sm-3">
									<div class="extGiftTemplateImg"><img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" /></div>
									<div>
										<p><strong>Tanishq</strong></p><p class="extGiftDesc">Special Birthday Gift Cards with additional benifits...</p>
										<br /><p><strong>&#8377; 500</strong></p>
									</div>
								  </div>
								  
								  <div class="extGiftTempItem col-sm-3">
									<div class="extGiftTemplateImg"><img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" /></div>
									<div>
										<p><strong>Tanishq</strong></p><p class="extGiftDesc">Special Birthday Gift Cards with additional benifits...</p>
										<br /><p><strong>&#8377; 500</strong></p>
									</div>
								  </div>
								  
								  <div class="extGiftTempItem col-sm-3">
									<div class="extGiftTemplateImg"><img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" /></div>
									<div>
										<p><strong>Tanishq</strong></p><p class="extGiftDesc">Special Birthday Gift Cards with additional benifits...</p>
										<br /><p><strong>&#8377; 500</strong></p>
									</div>
								  </div>
								  
								  <div class="extGiftTempItem col-sm-3">
									<div class="extGiftTemplateImg"><img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" /></div>
									<div>
										<p><strong>Tanishq</strong></p><p class="extGiftDesc">Special Birthday Gift Cards with additional benifits...</p>
										<br /><p><strong>&#8377; 500</strong></p>
									</div>
								  </div>
								  
								  <div class="extGiftTempItem col-sm-3">
									<div class="extGiftTemplateImg"><img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" /></div>
									<div>
										<p><strong>Tanishq</strong></p><p class="extGiftDesc">Special Birthday Gift Cards with additional benifits...</p>
										<br /><p><strong>&#8377; 500</strong></p>
									</div>
								  </div>
								  
								  <div class="extGiftTempItem col-sm-3">
									<div class="extGiftTemplateImg"><img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" /></div>
									<div>
										<p><strong>Tanishq</strong></p><p class="extGiftDesc">Special Birthday Gift Cards with additional benifits...</p>
										<br /><p><strong>&#8377; 500</strong></p>
									</div>
								  </div>
								  
								  <div class="extGiftTempItem col-sm-3">
									<div class="extGiftTemplateImg"><img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/93E01EE1-736A-4931-861C-368B8536DA9E.png" /></div>
									<div>
										<p><strong>Tanishq</strong></p><p class="extGiftDesc">Special Birthday Gift Cards with additional benifits...</p>
										<br /><p><strong>&#8377; 500</strong></p>
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
				STEP &nbsp;<span class="Oval">2</span><br />&nbsp;
			</div>
			<div class="col-sm-10">
				<span>ENTER DETAILS FOR YOUR GIFT CARD</span><br />&nbsp;
			</div>
		</div>
		<form:form method="POST" id="egvDetailsform" onsubmit="return validateForm();" 
						action="${request.contextPath}/checkout/multi/payment-method/giftCartPayment"
						commandName="egvDetailsform">
		<div class="clearfix">
			<div class="col-sm-2 headingTexts">
				Amount<br />&nbsp;
			</div>
			<div class="col-sm-10">
				<div class="btn-group" data-toggle="buttons">
					<span class="btn giftAmountBtns">
						<input type="radio" name="giftRange" value="500">&#8377; 500
					</span>
					<span class="btn giftAmountBtns">
						<input type="radio" name="giftRange" value="1000">&#8377; 1000
					</span>
					<span class="btn giftAmountBtns">
						<input type="radio" name="giftRange" value="1500">&#8377; 1500
					</span>
					<span class="btn giftAmountBtns">
						<input type="radio" name="giftRange" value="2000">&#8377; 2000
					</span>
					<span class="btn giftAmountBtns">
						<input type="radio" name="giftRange" value="2500">&#8377; 2500
					</span>
				</div>
				<div>or<br />&nbsp;
				<div class="alert alert-warning" id="customAmountError">
					
				</div>
				</div>
						<input  class="giftCard_input" id="customGiftValue" type="text" placeholder="Enter Custom Amount" onkeypress="return isNumber(event)" /><br />&nbsp;
						<form:input path="giftRange" type="hidden" id="customAmount" />
						 <%-- <form:input path="productCode" type="hidden" value="987654321" id="productCode" />   --%>
						
		
					
				
			</div>
		</div>
		<div class="clearfix">
			<div class="col-sm-2 headingTexts">
				To<br />&nbsp;
			</div>
			<div class="col-sm-10">
				<form:input path="toEmailAddress"  class="giftCard_input giftCard_toEmail" type="email" placeholder="Enter Recipient e-mail Address" /><br />&nbsp;
				<div class="alert alert-warning" id="giftCardEmailError"></div>
			</div>
		</div>
	 	<div class="clearfix">
			<div class="col-sm-2 headingTexts">
				From<br />&nbsp;
			</div>
			<div class="col-sm-10">
				<form:input path="fromEmailAddress" class="giftCard_input giftCard_fromName" type="text" placeholder="Your Name" /><br />&nbsp;
				<div class="alert alert-warning" id="giftCardFromNameError"></div>
			</div>
		</div>
		<div class="clearfix">
			<div class="col-sm-2 headingTexts">
				Message<br />&nbsp;
			</div>
			<div class="col-sm-10">
				<input type="hidden" id="customAmount" />
				<form:textarea path="messageBox" id="giftCardMessageText" class="giftCard_textarea" placeholder="Write a message"></form:textarea><br />&nbsp;
			</div>
		</div>
		<div class="clearfix">
			<div class="col-sm-2">
				&nbsp;
			</div>
			<div class="col-sm-10">
				Qty &nbsp; <input  type="text" class="qtyField" disabled value="1" />
				<button  type="submit" class="giftBuyBtn pull-right">BUY NOW</button>
			</div>
		</div> 
		</form:form>
	
	</div>
	
	<div class="col-sm-6 giftContSub giftContSub3">
		<div class="col-sm-12">
			<br /> &nbsp; <br /> &nbsp; <br /> &nbsp;
			<div class="giftTermsDesc">
				<span> Sold by <strong>QwikCilver Solutions Pvt. Ltd.</strong> and delivered by Tata CliQ. Credit and Debit Cards issued outside India cannot be used to purchase Tata CliQ Gift Cards. Tata CliQ Gift Cards are subjected to Terms and Conditions. Have a Tata CliQ Gift Card? <a href="#">Redeem</a> your gift card</span>
			</div>
		</div>
	</div>
</div>
<br />&nbsp;
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
$("#giftCardEmailError").hide();
var formValid = true;
function validateForm() {
	formValid = true;
	var fname = $(".giftCard_fromName").val();
	var toEmail = $(".giftCard_toEmail").val();
	var letters = new RegExp(/^[A-z]*$/);
	var emailValidExpression = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	
	if(document.getElementById('customAmount').value >= 15 && document.getElementById('customAmount').value < 15000) {
		$("#customAmountError").hide();
	} else {
		$("#customAmountError").show();
		$("#customAmountError").text('Please enter amount between 15 to 15000');
		formValid = false;
	}
	
  //Name Validation
	 if(fname == null || fname.trim() == '' ){
			$("#giftCardFromNameError").show();
			$("#giftCardFromNameError").text("Name cannot be Blank");
			formValid = false;
	}else if(letters.test(fname) == false){
		$("#giftCardFromNameError").show();
		$("#giftCardFromNameError").text("Name should contain only alphabets");
			formValid = false;
	}else {
		$("#giftCardFromNameError").hide();
	}
  
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
		$("#giftCardMessageText").keyup(function(){
	       var giftCardMessage = document.getElementById('updatedGiftCardMessageText');
	       giftCardMessage.style.fontStyle = 'normal';
	       giftCardMessage.innerHTML = '&nbsp; &nbsp; &nbsp; &nbsp; '+$(this).val();
	    });
		
		//Updating Amount
		$("input[name=giftRange]").on('change', function (){
			document.getElementById('customGiftValue').value = '';
			document.getElementById('updatedCustomGiftValue').innerHTML = $("input[name=giftRange]:checked").val();
			document.getElementById('customAmount').value = $("input[name=giftRange]:checked").val();
		});
		
		$("#customGiftValue").keyup(function(){
			$('input[name=giftRange]:checked').parents().find('.active').removeClass('active');
			$("input[name=giftRange]").prop('checked', false);
	       document.getElementById('updatedCustomGiftValue').innerHTML = $(this).val();
	       document.getElementById('customAmount').value = $(this).val();
	    });
	});
	
	  // GIFT DESIGN POPUP
    // Get the modal
		var giftDesignModal = document.getElementById('moreGiftDesignTemplates');
		
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
		}
		
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