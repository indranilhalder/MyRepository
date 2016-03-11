<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<%--  <c:set var = "addressFlag" scope="session" value = "${addressFlag}" />  --%>



<template:page pageTitle="${pageTitle}" hideHeaderLinks="true" showOnlySiteLogo="true">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=geometry,places&callback=initializeGoogleMaps"></script>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<div class="checkout-content cart checkout wrapper">

	<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
		<jsp:body>
					<style>
					 	.latlng {
					 		display: none;
					 	}
					 	
					 	.radio_btn {
							display: block !important;;
						    height: 15px;
						    width: 15px;
						    cursor: pointer;
						   	float: left;
						    margin-left: -38px !important;
						    border-radius: 10px;
						}
						
						.radio_btn2 {
							display: block !important;;
						    height: 15px;
						    width: 15px;
						    cursor: pointer;
						    border-radius: 10px;
						    background-color: #fff !important;
						}
						
						input[type="radio"]:checked+label{ font-weight: bold; }
						
						input[type="radio"]:checked {
							background-color: #000 !important;
						} 
						
						.continue_btn {
							border: none;
						    background-color: #00cbe9;
						    color: #fff;
						    clear: both;
						    line-height: 30px;
						    width: 250px !important;
						    text-align: center;
						    height: 32px;
						    padding: 4px 25px 8px 25px;
						    letter-spacing: 1.2px;
						    font-weight: bold;
						    font-size: 12px;
						    text-decoration: none;
						    cursor: pointer;
						}
						
						.continue_btn:hover {
							 background: #009fb6 !important;
							 text-decoration: none;
							 cursor: pointer;
							 color: #fff !important;
						}
						
						.continue_btn_a, .continue_btn_a:hover, .continue_btn_a:link, .continue_btn_a:visited {
							color: #fff !important;
							text-decoration: none;
						}
						
						.continue_holder {
							width: 100% !important;
							text-align: center;
						}
						
						.change_pincode_block {
							padding: 10px 0px 0px 0px;
						}
						
						.change_txt {
							font-size: 14px;
							letter-spacing: 1px;
						}
						
						.cd-popup {
							    z-index: 999;
						}
						
						.label_txt {
							margin-left: -137px;
						}
						
						.error_txt {
							color: red;
							font-size: 10px;
							font-weight: bold;
						}
						
						.pickupDetails {
							text-align: center;
							font-size: 12px !important;
						}
						
						.pickUpPersonAjax {
						    text-align: left;
						    padding: 5px 0px 5px 5px;
						    /* padding-top: 5px; */
						    background: #D2F7F3;
						    margin-bottom: 10px;
						    color: #000;
						    margin-top: -15px;
						}
						
						@media (max-width:650px){
							
						}
						
						@media (min-width: 1024px) {
						.checkout.wrapper .product-block li.header > ul li:first-child, .checkout.wrapper .product-block li.item > ul li:first-child {
						  width: 38% !important; }
						  .checkout.wrapper .product-block li.header > ul li.delivery, .checkout.wrapper .product-block li.item > ul li.delivery {
						  width: 28% !important;
						  }		
						  #pickup {
						  	width: 920px !important;
						  }
						  .savenewid {
						  	background: #00cbe9 !important;
						  	border-color: #00cbe9 !important;
						  	height: 40px !important;
						  }
						  
						 }
						 
						 @media (min-width: 1366px) and (max-width: 2160px) {
						.checkout.wrapper .product-block li.header > ul li:first-child, .checkout.wrapper .product-block li.item > ul li:first-child {
						  width: 38% !important; }
						  .checkout.wrapper .product-block li.header > ul li.delivery, .checkout.wrapper .product-block li.item > ul li.delivery {
						  width: 28% !important;
						  }	
						  }
						 
						  .checkout.wrapper .product-block li.header > ul li.delivery li label:after, .checkout.wrapper .product-block li.item > ul li.delivery li label:after {
						  	display: none !important;
						  } 
						  .radio_color {
						  	color: #ADA6A6;
						  }	
						  
						  .colorChange {
						  	color: #000 !important;
						  }	
						  .productCount {
						  	font-size: 12px;
						  	font-weight: bold;
						  	margin-top: -10px;
    						margin-bottom: 12px;
						  }
						  .submitPincode {
					  	    width: 98px !important;
						    margin-left: 88px;
						  }
						  
						  .scrollThis {
						  	height: 188px;
						  	padding-left: 5px;
							overflow-x: hidden;
							overflow-y: scroll;
						  }					 	
					</style>
					<script>
					//TISST-13010
					$(document).ready(function() {
						showPromotionTag();
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
					  // window.location = '${request.contextPath}/cart';
					}

					function goActive() {
					      startTimer();
					}
					</script>
						
	<script type="text/javascript">
		function allLetter(inputtxt) { 
	        var letters = new RegExp(/^(\w+\s?)*\s*$/);
	        var number = new RegExp(/\d/g);
	        if(inputtxt.value.match(letters))
	        {
	        	if(inputtxt.value.match(number))
		        {
		            return false;
		        }
		        else
		        {
		            return true;
		        }
	        }
	        else
	        {
	            return false;
	        }
	    }
	
		$(document).ready(function(){
			$(".pickUpPersonAjax").hide();
			$(".pickupPersonSubmitError").hide();
			
			$("#pickupPersonSubmit").hide();
			$("#pickupPersonName").keyup(function(){
				var pickupPersonName = $("#pickupPersonName").val();
				var pickUpPersonNam = document.pickupPersonDetails.pickupPersonName;
				var statusName = allLetter(pickUpPersonNam);
				if(statusName == false) {
					$(".pickupPersonNameError").show();
					$(".pickupPersonNameError").text("Please Enter Only Alphabets");
				}
				else {
					$(".pickupPersonNameError").hide();
				}
			});
			$("#pickupPersonMobile").keyup(function(){
				var pickupPersonMobile = $("#pickupPersonMobile").val();
				var isString = isNaN($('#pickupPersonMobile').val());
				if(isString==true) {
						$(".pickupPersonMobileError").show();
						$(".pickupPersonMobileError").text("Enter only numbers");
					}
				else if($('#pickupPersonMobile').val().length >= "11") {
					$(".pickupPersonMobileError").show();
					$(".pickupPersonMobileError").text("Enter only 10 Digit Number");
				} else {
					$(".pickupPersonMobileError").hide();
				}
			});
			$("#savePickupPersondDetails").click(function(){
				$(".pickupPersonSubmitError").hide();
				$(".pickUpPersonAjax").hide();
				//alert("hello")
				$(".pickupPersonMobileError").hide();
				$(".pickupPersonNameError").hide();
				$(".pickupDetails").hide();
				
				console.log("Working");
				var pickupPersonName = $("#pickupPersonName").val();
				
				var pickupPersonMobile = $("#pickupPersonMobile").val();
				var isString = isNaN($('#pickupPersonMobile').val());
				var pickUpPersonNam = document.pickupPersonDetails.pickupPersonName;
				var statusName = allLetter(pickUpPersonNam);
				if($('#pickupPersonName').val().length <= "3"){ 
					$(".pickupPersonNameError").show();
					$(".pickupPersonNameError").text("Enter Atleast 4 Letters");
				}
				else if(!(/^\S{3,}$/.test(pickupPersonName))){
					   $(".pickupPersonNameError").show();
					   $(".pickupPersonNameError").text("Spaces cannot be allowed");
				 }
				else if(statusName == false) {
					$(".pickupPersonNameError").show();
					$(".pickupPersonNameError").text("Please Enter Only Alphabets");
				}
				else if($('#pickupPersonMobile').val().length <= "9") {
					if(isString==true) {
						$(".pickupPersonMobileError").show();
						$(".pickupPersonMobileError").text("Enter only numbers");
					}
					else {
						$(".pickupPersonMobileError").show();
						$(".pickupPersonMobileError").text("Enter 10 Digit Number");
					}
				}
				else if($('#pickupPersonMobile').val().length > "10") {
					$(".pickupPersonMobileError").show();
					$(".pickupPersonMobileError").text("Enter only 10 Digit Number");
				}
				else if(isString==true) {
					$(".pickupPersonMobileError").show();
					$(".pickupPersonMobileError").text("Enter only numbers");
				}
				else {
					var requiredUrl = ACC.config.encodedContextPath +"/checkout/multi/delivery-method/addPickupPersonDetails";
					var dataString = 'pickupPersonName=' + pickupPersonName+ '&pickupPersonMobile=' + pickupPersonMobile;
						$.ajax({
						url : requiredUrl,
						data : dataString,
						success : function(data) {
							//console.log("success call for pickup person details"+data);
							$("#pickupPersonSubmit").text("1");
							$(".pickUpPersonAjax").fadeIn(100);
							$(".pickUpPersonAjax").text("Pickup Person Details Have Successfully Added.");
	
						},
						error : function(xhr, status, error) {
							console.log(error);	
						}
					});
				}
					
				
			});
			
			$(".pickupDetails").hide();
		});
	</script>
	<script type="text/javascript">

	function openPopForAdddPosToCartEntry(ussId,posName){
		//var productCode = $("#product").val();
		//alert(ussId+"@@@"+posName);
		var requiredUrl = ACC.config.encodedContextPath +"/checkout/multi/delivery-method/addPosToOrderEntry";
		var dataString = 'ussId=' + ussId+ '&posName=' + posName;
			$.ajax({
			url : requiredUrl,
			data : dataString,
			success : function(data) {
				
			},
			error : function(xhr, status, error) {
				alert(error);	
			}
		});
	}
</script>
					
					<ycommerce:testId code="checkoutStepTwo">
						<div class="checkout-shipping-items  left-block">
			<h1>
					<spring:theme code="checkout.multi.cnc.header"></spring:theme>
					<br>
			</h1>
			
			
			<div class="productCount">
				(for ${cnccount} out of ${delModeCount + cnccount} items in your bag)
			</div>
			
			<ul id="deliveryradioul" class="checkout-table product-block">
						
						<c:forEach items="${pwpos}" var="poses" varStatus="status1">
							<li class="header item${status1.index}">
							<ul class="headline">
								<li class="header2"><spring:theme code="text.product.information"/></li>
								<li class="store header5"><spring:theme code="checkout.multi.cnc.store.closeto"/>
								
								<c:if test="${not empty defaultPincode}">
									<span style="color: #00cbe9!important;" id="changeValue${status1.index}">
										${defaultPincode}
									</span>	
								</c:if>
								</li>
								<li class="delivery header4"><a 
														onclick="history.go(-1);" style="color: #00cbe9 !important;"><spring:theme code="checkout.multi.cnc.store.change.delivery.mode"/></a></li>
								
								<%-- <li class="delivery header4"><a class="cd-popup-trigger${status1.index}"
														style="color: #00cbe9 !important;" data-toggle="modal" data-target="#myModal">Change Delivery Mode</a></li>
 --%>								
						</ul>
							<div class="cd-popup cdpop${status1.index}" role="alert">
								<div class="cd-popup-container">
									<h1 style="text-align: center; padding-top: 10px;">Change Delivery Method</h1>
									
									<div class="row">
										<div class="col-md-4"></div><div class="col-md-8">
											<input type="radio" name="deliveryMode" value="2199" id="radio_1_home-delivery" class="radio_btn home${status1.index}" checked="checked">
											<span class="label_txt">Home Delivery</span>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4"></div><div class="col-md-8">										
											<input type="radio" name="deliveryMode" value="2199" id="radio_1_express-delivery" class="radio_btn">
											<span class="label_txt">Express Delivery</span>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4"></div><div class="col-md-8">
											<input type="radio" name="deliveryMode" value="2199" id="radio_1_click-and-collect" class="radio_btn">
											<span class="label_txt">Click and Collect</span>
										</div>
									</div>
									<div class="row" style="text-align: center; height: 50px; padding-bottom: 15px; padding-left: 100px;">	
										<button class="submitPincodedelivery${status1.index}" name="submitDlivery" style="margin-bottom: 10px; width: 50%;">Submit</button>
									</div>	
									
									<a href="#0" class="cd-popup-close img-replace closepp${status1.index}">Close</a>
								</div> <!-- cd-popup-container -->
							</div> <!-- cd-popup -->
							<script>
								$(document).ready(function(){
									
									$(".submitPincodedelivery${status1.index}").click(function (){
										$(".item${status1.index}").hide();
									});
									
									
									 //open popup
									$('.cd-popup-trigger${status1.index}').on('click', function(event){
										event.preventDefault();
										$('.cdpop${status1.index}').addClass('is-visible');
									});
									
									//close popup
									$('.cdpop${status1.index}').on('click', function(event){
										if( $(event.target).is('.closepp${status1.index}') || $(event.target).is('.cd-popup') ) {
											event.preventDefault();
											$(this).removeClass('is-visible');
										}
									});
									//close popup when clicking the esc keyboard button
									$(document).keyup(function(event){
								    	if(event.which=='27'){
								    		$('.cd-popup').removeClass('is-visible');
									    }
								    });
								});
							</script>
						</li>
							<li class="item delivery_options item${status1.index}">
								<ul>
										<li>
											<div>
												<div class="thumb product-img">
													<a href="${poses.product.url}"><product:productPrimaryImage
															product="${poses.product}" format="thumbnail" /></a>
												</div>
												<div class="details product" >
													<h3 class="product-brand-name"><a href="">${poses.product.brand.brandname}</a></h3>
													<ycommerce:testId code="cart_product_name">
														<a href="${poses.product.url}"><div class="name product-name">${poses.product.name}</div></a>
													</ycommerce:testId>
																									<!-- start TISEE-4631 TISUAT-4229 -->
												
												<c:if test="${fn:toUpperCase(poses.product.rootCategory) != 'ELECTRONICS'}">
												 	
												 	<ycommerce:testId code="cart_product_size">
												 		<c:if test="${not empty poses.product.size}">
												 			<div class="size"><b><spring:theme code="text.size"/>${poses.product.size}</b></div>
												 		</c:if>
														
													</ycommerce:testId>
													<ycommerce:testId code="cart_product_colour">
														<c:if test="${not empty poses.product.colour}">
															<div class="colour"><b><spring:theme code="text.colour"/>${poses.product.colour}</b></div>
														</c:if>
													</ycommerce:testId>
													${poses.product.code}
													<div class="item-price delivery-price">
														<format:price priceData="${poses.product.price}"/>
													</div>
												 </c:if>
												<!-- end TISEE-4631 TISUAT-4229 -->
												<!-- end TISEE-4631 TISUAT-4229 -->
												<ycommerce:testId code="cart_product_colour">
													<c:if test="${not empty sellerName}">
														<div class="colour"><b><spring:theme code="text.seller.name"/>	<b>${poses.sellerName}</b></b></div>
													</c:if>
												</ycommerce:testId>
												</div>
									      </div>
										</li>
							<div class="latlng latlng${status1.index}"><c:forEach items="${poses.pointOfServices}" var="pos" varStatus="status"><c:if test="${(status.index != 0)}">@</c:if> '${pos.displayName}', ${pos.geoPoint.latitude}, ${pos.geoPoint.longitude}</c:forEach>
							</div>
							<li class="delivery">
									<div class="error_txt pincodeServicable${status1.index}" style="width: 200px;font-size: 12px;"></div>
									<ul class="delivered scrollThis delivered${status1.index}">
							<c:forEach items="${poses.pointOfServices}" var="pos" varStatus="status">
										<li style="width: 240px !important;" class="removeColor${status1.index}">
											<%-- <input class="radio_btn" type="radio" name="address" id="address${status.index}" value="address${status.index}"> --%>
											<input class="radio_btn radio_btn${status1.index}" type="radio" name="address${status1.index}" id="address${status1.index}${status.index}" value="address${status.index}">
												<div class='pin bounce'>
													<span class="text_in">${status.count}</span>
														</div>
															<label class="radio_sel${status1.index}${status.index} displayName${status1.index}${status.index} radio_color delivery-address" style="color: #ADA6A6;">${pos.displayName}
															</label>
																<%-- <span class="radio_sel${status1.index}${status.index} radio_color displayName${status1.index}${status.index}">${pos.displayName}</span> --%>
																<span class="radio_sel${status1.index}${status.index} radio_color address1${status1.index}${status.index}">
																	<c:if test="${not empty pos.address.line1}">
																		${fn:escapeXml(pos.address.line1)}
																	</c:if>
																</span>
																<span class="radio_sel${status1.index}${status.index} radio_color address2${status1.index}${status.index}">
																	<c:if test="${not empty pos.address.line1}">
																		${fn:escapeXml(pos.address.line2)}
																	</c:if>
																</span>
																<span class="radio_sel${status1.index}${status.index} radio_color address3${status1.index}${status.index}">
																	<c:if test="${not empty pos.address.state}">
																		${pos.address.state}
																	</c:if>
																</span>
																<span class="radio_sel${status1.index}${status.index} radio_color address4${status1.index}${status.index}">
																	<c:if test="${not empty pos.address.postalCode}">
																		${pos.address.postalCode}
																	</c:if>
																</span>
																<span class="radio_sel${status1.index}${status.index} radio_color address1${status1.index}${status.index}" style="text-transform: lowercase" >STORE TIMINGS
																<br/>
																<c:if test="${not empty pos.mplOpeningTime && not empty pos.mplClosingTime}">
																	${pos.mplOpeningTime}AM - ${pos.mplClosingTime}PM
																	</c:if>
																</span>
																
																
										</li>
										<script>
											$(document).ready(function() {
												$(".select_store").hide();
												var checked${status1.index} = $("input[name='address${status1.index}']:checked").val();
												$(".continue_btn").click(function(e){
													$(".pickupDetails").hide();
													$(".pickupPersonSubmitError").hide();
													var pickupPersonSubmit = $("#pickupPersonSubmit").text();
													if(pickupPersonSubmit == "1") {
														//$(".pickupPersonSubmitError").show();
														//$(".pickupPersonSubmitError").text("Pickup Person Details Have Been Successfully Added");
														//console.log("Pickup Person Details Have Been Successfully Added");
														} else {
														e.preventDefault();
														$(".pickupPersonSubmitError").show();
														$(".pickupPersonSubmitError").text("Please Submit Pickup Person Details");
														//console.log("Please Submit Pickup Person Details");
														}
													var checked${status1.index} = $("input[name='address${status1.index}']:checked").val();
													if(checked${status1.index}=="address${status1.index}" || checked${status1.index}=="address${status.index}" || checked${status1.index}=="address1" || checked${status1.index}=="address0" || checked${status1.index}=="address2" || checked${status1.index}=="address3" || checked${status1.index}=="address4" || checked${status1.index}=="address5" || checked${status1.index}=="address6" || checked${status1.index}=="address7") {
														if($('#pickupPersonName').val().length <= "3" || $('#pickupPersonMobile').val().length <= "9") {
															$(".pickupDetails").show();
															e.preventDefault();
														}
													} else {
														$(".select_store").show();
														e.preventDefault();
													}
													
												});
												$("#address${status1.index}${status.index}").click(function(){
													$(".removeColor${status1.index} .radio_color").removeClass("colorChange");
													$(".select_store").hide();
													var name${status.index} = $(".displayName${status1.index}${status.index}").text();
													openPopForAdddPosToCartEntry('${poses.ussId}',name${status.index});
													$(".radio_sel${status1.index}${status.index}").addClass("colorChange");
												});
											});
										</script>
							</c:forEach>
							</ul>
								</li>
							
							<li>
													<ul id="map${status1.index}" style="width: 300px; height: 200px; position: relative; overflow: hidden; transform: translateZ(0px); background-color: rgb(229, 227, 223);"></ul>
													<ul id="maphide${status1.index}" style="width: 300px; height: 200px; position: relative; overflow: hidden; transform: translateZ(0px); background-color: rgb(229, 227, 223);padding: 10px; font-weight: 600">Unable to find Stores</ul>
													<div class="change_pincode_block block${status1.index}">
														<span class="change_txt txt${status1.index}">Change Pincode?</span>
														<div class="input${status1.index} row" style="width: 111%">
															<div class="col-md-8 col-sm-8 col-xs-8">
																<input type="text" name="changepin${status1.index}" class="changepin${status1.index}" placeholder="Enter Pincode to Change.">
															</div>
															<div class="col-md-4 col-sm-4 col-xs-4">
																<button class="submitPincode submitPincode${status1.index}" style="height: 40px !important; background: #333 !important; color: #fff !important;" name="submitPincode${status1.index}">Submit</button>
															</div>
														</div>
														<div class="pincodeValidation error_txt" style="margin-left: 15px;width: 200px;">
														
														</div>
													</div>
														
						<script>
						 //alert("Hello");
							$(document).ready(function() {
								$("input[name='address${status1.index}']").prop('checked', false);
								$(".input${status1.index}").hide();
								$(".pincodeServicable${status1.index}").hide();
								$("#maphide${status1.index}").hide();
								$(".txt${status1.index}").click(function(){
									$(".txt${status1.index}").hide();
									$(".input${status1.index}").show();
								});
								
								var loc${status1.index} = $(".latlng${status1.index}").text();
								loc${status1.index} = loc${status1.index}.split("@");
								
								var length${status1.index} = loc${status1.index}.length;
								
								for(var i=0;i<length${status1.index};i++){
									loc${status1.index}[i] = loc${status1.index}[i].split(",");
									for(var j=0;j<loc${status1.index}[i].length;j++) {
									}
								}
								
								 // Setup the different icons and shadows
							    var iconURLPrefix${status1.index} = '${request.contextPath}/_ui/responsive/theme-blue/images/storemarkericons/';
							    
							    iconURLPrefix${status1.index} = iconURLPrefix${status1.index}.replace("/mpl/en/","/");
							    
							    //console.log(iconURLPrefix${status1.index});
							    
							    
							    var icons = [
														      iconURLPrefix${status1.index} + 'markergrey1.png',
														      iconURLPrefix${status1.index} + 'markergrey2.png',
														      iconURLPrefix${status1.index} + 'markergrey3.png',
														      iconURLPrefix${status1.index} + 'markergrey4.png',
														      iconURLPrefix${status1.index} + 'markergrey5.png',
														      iconURLPrefix${status1.index} + 'markergrey6.png'
														    ]
							    
							    var icons${status1.index} = [
							      iconURLPrefix${status1.index} + 'markergrey1.png',
							      iconURLPrefix${status1.index} + 'markergrey2.png',
							      iconURLPrefix${status1.index} + 'markergrey3.png',
							      iconURLPrefix${status1.index} + 'markergrey4.png',
							      iconURLPrefix${status1.index} + 'markergrey5.png',
							      iconURLPrefix${status1.index} + 'markergrey6.png'
							    ]
							    var iconsLength = icons${status1.index}.length;
							    
							    $(".radio_btn${status1.index}").click(function(){
							    	var number = $(this).val();
							    	icons${status1.index} = [
														      iconURLPrefix${status1.index} + 'markergrey1.png',
														      iconURLPrefix${status1.index} + 'markergrey2.png',
														      iconURLPrefix${status1.index} + 'markergrey3.png',
														      iconURLPrefix${status1.index} + 'markergrey4.png',
														      iconURLPrefix${status1.index} + 'markergrey5.png',
														      iconURLPrefix${status1.index} + 'markergrey6.png'
														    ]
							    	number = number.replace("address","");
							    	//number++;
							    	var myCounter = "1";
							    	var iconNumber = parseInt(number) + parseInt(myCounter);
							    	var url =  iconURLPrefix${status1.index} + 'marker' + parseInt(iconNumber) +'.png';
							    	icons${status1.index}[number] = url;
							    	processMap${status1.index}();
							    	//console.log(url);
							    });
								
							    
								$(".submitPincode${status1.index}").click(function(){
									$(".removeColor${status1.index} .radio_color").removeClass("colorChange");
									$(".pincodeServicable${status1.index}").hide();
									$(".pincodeValidation").hide();
									var pinvalue${status1.index} = $(".changepin${status1.index}").val();
									var pinlength = pinvalue${status1.index}.length;
									if(pinlength == "6") {
										var productcode${status1.index} = "${poses.product.code}";
										var sellerId = "${poses.ussId}";
										var dataString${status1.index} = "pin=" + pinvalue${status1.index} + "&productCode="+ productcode${status1.index} + "&sellerId="+sellerId;
										$.ajax({
									          url :  ACC.config.encodedContextPath +"/checkout/multi/delivery-method/updatePincodeCheck",
									          type: "GET",
									          dataType : "json",
									    	  cache: false,
									    	  contentType : "application/json; charset=utf-8",
									          data : dataString${status1.index},   
									          success : function(data) {
									        	 //console.log(data);
										          var response${status1.index} = JSON.stringify(data);
										          var jsonObject${status1.index} = JSON.parse(response${status1.index});
										          $("#changeValue${status1.index}").text(pinvalue${status1.index});
										          //console.log(jsonObject${status1.index});
										          //console.log(jsonObject${status1.index}.length);
										          if(jsonObject${status1.index}.length != "0") {
										        	  $("input[name='address${status1.index}']").prop('checked', false);
										        	  $(".removeColor${status1.index} .radio_color").removeClass("colorChange");
										        	  icons${status1.index} = icons;
										        	  processMap${status1.index}();
										        	  $(".delivered${status1.index}").show();
										        	  $("#map${status1.index}").show();
										        	  $("#maphide${status1.index}").hide();
											          var changecordinates${status1.index} = " ";
											          for(var i=0;i<jsonObject${status1.index}.length;i++) {
											        	  
											        	  if(jsonObject${status1.index}[i]['displayName'] != null) {
											        	  	  $(".displayName${status1.index}"+i).text(jsonObject${status1.index}[i]['displayName']);
											        	  } else {
											        		  $(".displayName${status1.index}"+i).text("");
											        	  } if(jsonObject${status1.index}[i]['address']['line1'] != null) {
										        			  $(".address1${status1.index}"+i).text(jsonObject${status1.index}[i]['address']['line1']);
											        	  } else { 
											        		  $(".address1${status1.index}"+i).text("");
											        	  } if(jsonObject${status1.index}[i]['address']['line2'] != null) {
											        	  	$(".address2${status1.index}"+i).text(jsonObject${status1.index}[i]['address']['line2']);
											        	  } else {
											        		  $(".address2${status1.index}"+i).text(""); 
											        	  }if(jsonObject${status1.index}[i]['address']['state'] != null) {
											        	  	$(".address3${status1.index}"+i).text(jsonObject${status1.index}[i]['address']['state']);
											        	  } else {
											        		$(".address3${status1.index}"+i).text("");
											        	  }if(jsonObject${status1.index}[i]['address']['postalCode'] != null) {
											        	  	$(".address4${status1.index}"+i).text(jsonObject${status1.index}[i]['address']['postalCode']);
											        	  } else {
											        		  $(".address4${status1.index}"+i).text("");  
											        	  }
											        	/*   console.log(jsonObject${status1.index}[i]['name']);
											        	  console.log(jsonObject${status1.index}[i]['address']['line1']);
											        	  console.log(jsonObject${status1.index}[i]['geoPoint']['latitude']);
											        	  console.log(jsonObject${status1.index}[i]['geoPoint']['longitude']);
											        	 */  if(i!='0') {
											        		  //console.log("Printing");
											        		 changecordinates${status1.index} += " @ ";
											        	  }
											        	  changecordinates${status1.index} += "'"+jsonObject${status1.index}[i]['name']+"', "+jsonObject${status1.index}[i]['geoPoint']['latitude']+", "+jsonObject${status1.index}[i]['geoPoint']['longitude'];
											        	  
											          	}
											          $(".latlng${status1.index}").text(changecordinates${status1.index});
											          processMap${status1.index}();
											          $('.scrollThis').each(function(){
															if($(this).find("li").length <= '2'){
																$(this).css({"overflow-y" : "hidden"});	
															}
															else {
																$(this).css({"overflow-y" : "scroll"});
															}
														});
										        	} else {
										        		$(".pincodeServicable${status1.index}").show();
										        		$(".delivered${status1.index}").hide();
										        		$("#map${status1.index}").hide();
										        		$("#maphide${status1.index}").show();
										        		$(".pincodeServicable${status1.index}").text("This pincode is not servicable");
										        	}
				
									           
									          },
									          error : function(xhr, data, error) {
									        	  console.log("Error in processing Ajax. Error Message : " +error+" Data : " +data)
												}
									         });
										
										$(".txt${status1.index}").show();
										$(".input${status1.index}").hide();
									} else {
										$(".pincodeValidation").show();
										$(".pincodeValidation").text("Please Enter 6 digits Only");
									}
								});
								processMap${status1.index}();
								
							   
							    
						    function processMap${status1.index}() {	
							    var map = new google.maps.Map(document.getElementById('map${status1.index}'), {
							      zoom: 10,
							      center: new google.maps.LatLng(-37.92, 151.25),
							      mapTypeId: google.maps.MapTypeId.ROADMAP,
							      mapTypeControl: false,
							      streetViewControl: false,
							      panControl: false,
							      zoomControlOptions: {
							         position: google.maps.ControlPosition.LEFT_BOTTOM
							      }
							    });
							
							    var infowindow = new google.maps.InfoWindow({
							      maxWidth: 160
							    });

							    var markers = new Array();
							    
							    var iconCounter = 0;
							    
							    // Add the markers and infowindows to the map
							    for (var i = 0; i < length${status1.index}; i++) {  
							      var marker = new google.maps.Marker({
							        position: new google.maps.LatLng(loc${status1.index}[i][1], loc${status1.index}[i][2]),
							        map: map,
							        icon: icons${status1.index}[iconCounter]
							      });

							      markers.push(marker);

							      google.maps.event.addListener(marker, 'click', (function(marker, i) {
							        return function() {
							          infowindow.setContent(loc${status1.index}[i][0]);
							          infowindow.open(map, marker);
							          
							        }
							      })(marker, i));
							      
							      iconCounter++;
							      // We only have a limited number of possible icon colors, so we may have to restart the counter
							      if(iconCounter >= iconsLength) {
							      	iconCounter = 0;
							      	
							      }
							    }

							    function autoCenter() {
							      //  Create a new viewpoint bound
							      var bounds = new google.maps.LatLngBounds();
							      //  Go through each...
							      for (var i = 0; i < markers.length; i++) {  
											bounds.extend(markers[i].position);
											
							      }
							      //  Fit these bounds to the map
							      map.fitBounds(bounds);
							    }
							    autoCenter();
							    
							}
							    
						});
							  </script> 	
						 		</li>
						 		</ul>
							</li>
							
					</c:forEach>
			</ul>
			<span class="select_store error_txt" style="text-align: left;font-size: 15px;">
			<spring:theme code="checkout.multi.cnc.select.products.validate.msg"/></span>
			<div class="container" id='pickup'>
       		<div class="panel panel-default pickuppersonWidth" style="height: auto!important; width: 100%!important;">
     			 <div class="panel panel-body" style="margin-top: 14px;">
     			 	<div class="col-md-12 pickupDetails error_txt">
     			 	<spring:theme code="checkout.multi.cnc.pickup.details.validation.msg"/>
     			 	</div>
     			 	<div class="pickUpPersonAjax">
     			 	</div>
     			 	<form name="pickupPersonDetails" action="#">
     				 <div class="col-md-3">
      					 <span class="pickupperson"><h5 id="pickup"><spring:theme code="checkout.multi.cnc.pickup.person.name"/></h5></span></div>
       					 <div class="col-md-3">
        					<input type="text" id="pickupPersonName" name="pickupPersonName" class="inputname" placeholder="Enter Full Name"  value="${pickupPersonName}"/><br/>
        					<div class="error_txt pickupPersonNameError"></div>
            			</div>
            			<div class="col-md-3">
							<input type="text" id="pickupPersonMobile" class="inputmobile" placeholder="Enter Mobile Number" value="${pickUpPersonMobile}"/><br/>
							<div class="error_txt pickupPersonMobileError"></div>
        			    </div>
			             <div class="col-md-3">
			             <button type="button"  class="savenewid" id="savePickupPersondDetails" style="height: 40px !important"><spring:theme code="checkout.multi.cnc.pickup.details.submit"/></button>
			          <div id="pickupPersonSubmit"></div>
			          <div class="error_txt pickupPersonSubmitError"></div>
			            </div>
			           </form>
			            <div class="col-md-12" style="padding-top: 10px;">
			            	<spring:theme code="checkout.multi.cnc.pickup.details.below.msg"/>
			            </div>
					</div>
			</div>
		</div>
		</div>
		
		<div class="right-block shipping" style="margin-top: 74px;">
				<div class="checkout-order-summary">
					<multi-checkout:orderTotals cartData="${cartData}"
						showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" />
				</div>
			</div>
			<div>
				<input type="hidden" name="CSRFToken"
											value="${CSRFToken}">
			</div>
		<div class="continue_holder">
			<c:choose>
				<c:when test="${delModeCount gt 0}">
				
				<form:form id="selectDeliveryMethodForm1" action="${request.contextPath}/checkout/multi/delivery-method/select" method="post" commandName="deliveryMethodForm">
								<button class="continue_btn" id="deliveryMethodSubmit1" type="submit" class="checkout-next" style="border: none;"><spring:theme code="checkout.multi.deliveryMethod.continue" text="Next"/></button>
				</form:form>
				</c:when>
				
				<c:otherwise>
				<a class="continue_btn_a" href="${request.contextPath}/checkout/multi/delivery-method/invReservation" type="button">
					<div class="continue_btn">
						CONTINUE
					</div>
				</a>
				</c:otherwise>
			</c:choose>
		</div>
		<script>
			$(document).ready(function(){
				$('.scrollThis').each(function(){
					if($(this).find("li").length <= '2'){
						$(this).css({"overflow-y" : "hidden"});	
					}
					else {
						$(this).css({"overflow-y" : "scroll"});
					}
				});
					
			});
		</script>
					</ycommerce:testId>
					</jsp:body>
	</multi-checkout:checkoutSteps>
	
</div>
</template:page>