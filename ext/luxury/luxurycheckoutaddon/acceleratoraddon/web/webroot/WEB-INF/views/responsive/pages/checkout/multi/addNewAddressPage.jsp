<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/formElement" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/common"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/nav" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/address"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/cart" %>

					<script>
					//TISST-13010
					$(document).ready(function() {
						showPromotionTag();
						if($(".choose-address .acc_content").children(".address-list").length == 0){
							$(".add-address").css({
							  //margin : "0px auto",
							  float: "none"
						});
							$(".checkout-shipping.addNewAddress .formaddress").css({
								//margin : "0px auto",
								float: "none",
								width: "80%",
								overflow: "hidden"
							});
						//$(".choose-address .acc_head").css("text-align","center");
						}
						  $(".cancelBtn").click(function() {
							  //alert('here');
							  	
						        $(".editnewAddresPage, .formaddress").slideUp();
						        $(".add-address").slideDown();
						    });
						  $('.checkout.wrapper .formaddress select[name="state"]').on("change",function(){$(this).css("color","#000");});
					});
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
					//TPR-1214
					$("#newAddressButton,#newAddressButtonUp").click(function() {

				     	$("form#addressForm :input[type=text]").each(function(){
				    		 var input = $(this);    
				    		 $(this).val($(this).val().trim());    		     		
				    	});  

						var validate=true;
						var regPostcode = /^([1-9])([0-9]){5}$/;
					    var mob = /^[1-9]{1}[0-9]{9}$/;
					    var letters = /^[a-zA-Z]+$/; 
					    var cityPattern = /^[a-zA-Z]+([\s]?[a-zA-Z]+)*$/;
					    var firstName = document.getElementById("address.firstName");
						var lastName = document.getElementById("address.surname");
						var address1 = document.getElementById("address.line1");
						var regAddress = /^[0-9a-zA-Z\-\/\,\s]+$/;
						var address2 = document.getElementById("address.line2");
						var address3 = document.getElementById("address.line3");
						var city= document.getElementById("address.townCity");
						var stateValue = document.getElementById("address.states");
						var zipcode = document.getElementsByName("postcode")[0].value;
						var txtMobile = document.getElementsByName("MobileNo")[0].value;
						var result=firstName.value;
						 
						if(result == undefined || result == "" )
						{	
							$("#firstnameError").show();
							$("#firstnameError").html("<p>First Name cannot be Blank</p>");
							validate= false;
						}
						else if(letters.test(result) == false)  
						{ 
							$("#firstnameError").show();
							/*Error message changed TISPRD-427*/
							$("#firstnameError").html("<p>First name should not contain any special characters or space</p>");
							validate= false;
						}  
						else
						{
							$("#firstnameError").hide();
						}
								
						 result=lastName.value;
						if(result == undefined || result == "")
						{	
							$("#lastnameError").show();
							$("#lastnameError").html("<p>Last Name cannot be Blank</p>");
							validate= false;
						}
						else if(letters.test(result) == false)  
						{ 
							$("#lastnameError").show();
							/*Error message changed TISPRD-427*/
							$("#lastnameError").html("<p>Last name should not contain any special characters or space</p>");
							validate= false;
						} 
						else
						{
							$("#lastnameError").hide();
						}
						
						result=address1.value;
						if(result == undefined || result == "")
						{	
							$("#address1Error").show();
							$("#address1Error").html("<p>Address Line 1 cannot be blank</p>");	
							validate= false;
						}
						else
						{
							$("#address1Error").hide();
						}	
						  result=city.value;
						if(result == undefined || result == "")
						{	
							$("#cityError").show();
							$("#cityError").html("<p>City cannot be blank</p>");
							 validate=false;
						}
						else if(cityPattern.test(result) == false)  
						{ 
							$("#cityError").show();
							$("#cityError").html("<p>City must be alphabet only</p>");
							validate= false;
						}
						else
						{
							$("#cityError").hide();
						}

						result=stateValue.value;
						if(result == undefined || result == "")
						{	
							$("#stateError").show();
							$("#stateError").html("<p>Please choose a state</p>");
							 validate = false;
						}
						else
						{
							$("#stateError").hide();
						}
						
					   if(zipcode == undefined || zipcode == "")
						{	
							$("#pincodeError").show();
							$("#pincodeError").html("<p>Please enter a pincode</p>");
							validate = false;
						}
					    else if(regPostcode.test(zipcode) == false){
					        $("#pincodeError").show();
					        $("#pincodeError").html("<p>Please enter a valid pincode</p>");
							validate= false;  
						}
					    else
						{
							$("#pincodeError").hide();
						}
					 
					   if(txtMobile  == undefined || txtMobile == "")
						{	
							$("#mobileError").show();
							$("#mobileError").html("<p>Please enter mobile no.</p>");
					        validate = false;
						}
					    else if (mob.test(txtMobile) == false) {
							$("#mobileError").show();
							$("#mobileError").html("<p> Please enter correct mobile no.</p>");
							 validate=false;   
					    }
					       else
						{
							$("#mobileError").hide();
						}
					   
						if(validate==false)
						{
							return false;
						}
						else
						{
							
							
							//address1.value=encodeURIComponent(address1.value);
					    	//address2.value=encodeURIComponent(address2.value);
					   		//address3.value=encodeURIComponent(address3.value);
					    	
							$.ajax({
						 		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/new-address",
						 		type: "POST",
						 		data:$("#addressForm").serialize().replace(/\+/g,'%20'),
						 		cache: false,
						 		dataType: "json",
						 		success : function(response) {
						 			//TPR-4745
						 			if(typeof utag !="undefined"){
								 		 utag.link({ link_text : 'add_new_address_saved' ,event_type : 'add_new_address_saved'});
								 		 }
						 		if(response.hasOwnProperty("error")){
						 			
						 		}else if(response.hasOwnProperty("redirect_url")){
						 		var redirectUrl = response.redirect_url;
						 		var url = redirectUrl.substr(redirectUrl.indexOf(':')+1,redirectUrl.length);
						 		
						 		window.location.href = ACC.config.encodedContextPath + url;
						 		}	
						 		},
						 		error : function(resp) {
						 			
						 		}
						 		
						 		});
						}
						return false;
					});
					</script>
					<ycommerce:testId code="checkoutStepTwo">
						<div class="checkout-shipping addNewAddress">
					<c:choose>
					<c:when test="${edit eq true}">
						<ycommerce:testId code="multicheckout_saveAddress_button">
							<button  id="editAddressButtonUp"  class="btn btn-primary btn-block" type="submit">
								<spring:theme code="checkout.multi.saveAddress" text="Save address"/>
							</button>
						</ycommerce:testId>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${accountPageAddress eq true}">
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<button id="newAddressButtonAccountUp" class=" btn btn-primary btn-block" type="submit">
										<spring:theme code="checkout.multi.deliveryAddress.continue" text="Continue"/>
									</button>
								</ycommerce:testId>
							</c:when>
							<c:otherwise>
								<ycommerce:testId code="multicheckout_saveAddress_button">
									<%-- <button id="newAddressButtonUp" class="button" type="submit">
										<spring:theme code="checkout.multi.deliveryAddress.continue" text="Continue"/>
									</button> --%>
								</ycommerce:testId>
							</c:otherwise>
						</c:choose>						
					</c:otherwise>
				</c:choose> 
						 <div class="formaddress" style="display: block;">
		<div class="heading-form">
														<h3>Add New Address</h3>
													
													</div>
	  
	  
	   <div class="checkout-indent left-block address-form">
								
									<ul class="product-block addresses new-form account-section">
									  	
									  	<li
																class="item account-section-content	 account-section-content-small ">
									  	<address:addressFormSelector
																	supportedCountries="${countries}" regions="${regions}"
																	cancelUrl="${currentStepUrl}" />
									  	</li>
									  	</ul>
									  	
									</div>
	  
	  </div> 
							</div>
						
					</ycommerce:testId>
