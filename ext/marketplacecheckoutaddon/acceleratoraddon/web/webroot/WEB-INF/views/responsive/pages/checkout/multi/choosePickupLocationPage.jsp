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
						.checkout.wrapper .product-block li.header > ul li:first-child, .checkout.wrapper .product-block li.item > ul li:first-child {
						  width: 38% !important; }
						  .checkout.wrapper .product-block li.header > ul li.delivery, .checkout.wrapper .product-block li.item > ul li.delivery {
						  width: 28% !important;
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
		$(document).ready(function(){
			$(".pickUpPersonAjax").hide();
		
			$("#savePickupPersondDetails").click(function(){
				$(".pickUpPersonAjax").hide();
				//alert("hello")
				$(".pickupPersonMobileError").hide();
				$(".pickupPersonNameError").hide();
				$(".pickupDetails").hide();
				
				console.log("Working");
				var pickupPersonName = $("#pickupPersonName").val();
				
				var pickupPersonMobile = $("#pickupPersonMobile").val();
				var isString = isNaN($('#pickupPersonMobile').val());
				
				if($('#pickupPersonName').val().length <= "3"){ 
					$(".pickupPersonNameError").show();
					$(".pickupPersonNameError").text("Enter Atleast 4 Letters");
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
							console.log("success call for pickup person details"+data);
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

	function openPopForAdddPosToCartEntry(productCode,posName){
		//var productCode = $("#product").val();
		var requiredUrl = ACC.config.encodedContextPath +"/checkout/multi/delivery-method/addPosToOrderEntry";
		var dataString = 'productCode=' + productCode+ '&posName=' + posName;
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
			<p>
				<spring:theme code="checkout.multi.cnc.showing.delivery.options.pincode"></spring:theme>
				
					<c:if test="${not empty defaultPincode}">
						<span>
							${defaultPincode}
						</span>	
					</c:if>
			</p>
			
			<ul id="deliveryradioul" class="checkout-table product-block">
						
						<c:forEach items="${pwpos}" var="poses" varStatus="status1">
							<li class="header item${status1.index}">
							<ul class="headline">
								<li class="header2"><spring:theme code="text.product.information"/></li>
								<li class="store header5"><spring:theme code="checkout.multi.cnc.store.closeto"/>
								
								<c:if test="${not empty defaultPincode}">
									<span style="color: #00cbe9!important;">
										${defaultPincode}
									</span>	
								</c:if>
								</li>
								<li class="delivery header4"><a 
														style="color: #00cbe9 !important;"><spring:theme code="checkout.multi.cnc.store.change.delivery.mode"/></a></li>
								
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
										<button class="submitPincodedelivery${status1.index}" name="submitDlivery" style="margin-bottom: 10px; width: 50%">Submit</button>
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
														<div class="colour"><b><spring:theme code="text.seller.name"/>	<b>${sellerName}</b></b></div>
													</c:if>
												</ycommerce:testId>
												</div>
									      </div>
										</li>
							<div class="latlng latlng${status1.index}"><c:forEach items="${poses.pointOfServices}" var="pos" varStatus="status"><c:if test="${(status.index != 0)}">@</c:if> '${pos.displayName}', ${pos.geoPoint.latitude}, ${pos.geoPoint.longitude}</c:forEach>
							</div>
							<li class="delivery">
									<ul class="delivered">
							<c:forEach items="${poses.pointOfServices}" var="pos" varStatus="status">
										<li style="width: 240px !important;">
											<%-- <input class="radio_btn" type="radio" name="address" id="address${status.index}" value="address${status.index}"> --%>
											<input class="radio_btn" type="radio" name="address${status1.index}" id="address${status1.index}${status.index}" value="address${status.index}">
												<div class='pin bounce'>
													<span class="text_in">${status.index}</span>
														</div>
															<label class="radio_sel${status.index} radio_color delivery-address" style="color: #ADA6A6;">${pos.displayName}
															</label>
																<span class="radio_sel${status.index} radio_color displayName${status1.index}${status.index}">${pos.displayName}</span>
																<span class="radio_sel${status.index} radio_color address${status1.index}${status.index}">
																	<c:if test="${not empty pos.address.line1}">
																		${pos.address.line1}
																	</c:if>
																	</span>
																<span class="radio_sel${status.index} radio_color address${status1.index}${status.index}">
																	<c:if test="${not empty pos.address.state}">
																		${pos.address.state}
																	</c:if>
																</span>
																<span class="radio_sel${status.index} radio_color address${status1.index}${status.index}">
																	<c:if test="${not empty pos.address.postalCode}">
																		${pos.address.postalCode}
																	</c:if>
																</span>
																
										</li>
										<script>
											$(document).ready(function() {
												$(".select_store").hide();
												
												var checked${status1.index} = $("input[name='address${status1.index}']:checked").val();
												$(".continue_btn").click(function(e){
													var checked${status1.index} = $("input[name='address${status1.index}']:checked").val();
													if(checked${status1.index}=="address${status1.index}" || checked${status1.index}=="address${status.index}" || checked${status1.index}=="address1" || checked${status1.index}=="address0" || checked${status1.index}=="address2") {
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
													$(".radio_color").removeClass("colorChange");
													$(".select_store").hide();
													var name${status.index} = $(".displayName${status1.index}${status.index}").text();
													openPopForAdddPosToCartEntry('${poses.product.code}',name${status.index});
													$(".radio_sel${status.index}").addClass("colorChange");
												});
											});
										</script>
							</c:forEach>
							</ul>
								</li>
							
							<li style="width: 31%">
													<ul id="map${status1.index}" style="width: 300px; height: 200px; position: relative; overflow: hidden; transform: translateZ(0px); background-color: rgb(229, 227, 223);"></ul>
													<div class="change_pincode_block block${status1.index}">
														<span class="change_txt txt${status1.index}">Change Pincode?</span>
														<div class="input${status1.index} row" style="width: 111%">
															<div class="col-md-8 col-sm-8 col-xs-8">
																<input type="text" name="changepin${status1.index}" class="changepin${status1.index}" placeholder="Enter Pincode to Change.">
															</div>
															<div class="col-md-4 col-sm-4 col-xs-4">
																<button class="submitPincode${status1.index}" name="submitPincode${status1.index}">Submit</button>
															</div>
														</div>
													</div>	
						<script>
						 //alert("Hello");
							$(document).ready(function() {
								$(".input${status1.index}").hide();
								
								$(".txt${status1.index}").click(function(){
									$(".txt${status1.index}").hide();
									$(".input${status1.index}").show();
								});
								
								$(".submitPincode${status1.index}").click(function(){
									var pinvalue${status1.index} = $(".changepin${status1.index}").val();
									var productcode${status1.index} = "${poses.product.code}";
									var sellerId = ${sellerId};
									var dataString${status1.index} = "pin=" + pinvalue${status1.index} + "&productCode="+ productcode${status1.index} + "&sellerId="+sellerId;
									$.ajax({
								          url :  ACC.config.encodedContextPath +"/checkout/multi/delivery-method/updatePincodeCheck",
								          type: "GET",
								          dataType : "json",
								    	  cache: false,
								    	  contentType : "application/json; charset=utf-8",
								          data : dataString${status1.index},   
								          success : function(data) {
								          var response${status1.index} = JSON.stringify(data);
								          var jsonObject${status1.index} = JSON.parse(response${status1.index});
								          var changecordinates${status1.index} = " ";
								          for(var i=0;i<jsonObject${status1.index}.length;i++) {
								        	  $(".displayName${status1.index}"+i).text(jsonObject${status1.index}[i]['name']);	
								        	  $(".address${status1.index}"+i).text(jsonObject${status1.index}[i]['address']['line1']);
								        	  console.log(jsonObject${status1.index}[i]['name']);
								        	  console.log(jsonObject${status1.index}[i]['address']['line1']);
								        	  console.log(jsonObject${status1.index}[i]['geoPoint']['latitude']);
								        	  console.log(jsonObject${status1.index}[i]['geoPoint']['longitude']);
								        	  if(i!='0') {
								        		  //console.log("Printing");
								        		 changecordinates${status1.index} += " @ ";
								        	  }
								        	  changecordinates${status1.index} += "'"+jsonObject${status1.index}[i]['name']+"', "+jsonObject${status1.index}[i]['geoPoint']['latitude']+", "+jsonObject${status1.index}[i]['geoPoint']['longitude'];
								        	  
								          	}
								          $(".latlng${status1.index}").text(changecordinates${status1.index});
								          processMap${status1.index}();
								           
								          },
								          error : function(xhr, data, error) {
								        	  console.log("Error in processing Ajax. Error Message : " +error+" Data : " +data)
											}
								         });
									
									$(".txt${status1.index}").show();
									$(".input${status1.index}").hide();
								});
								processMap${status1.index}();

								function processMap${status1.index}() {
								var loc${status1.index} = $(".latlng${status1.index}").text();
								loc${status1.index} = loc${status1.index}.split("@");
								
								var length${status1.index} = loc${status1.index}.length;
								
								for(var i=0;i<length${status1.index};i++){
									loc${status1.index}[i] = loc${status1.index}[i].split(",");
									for(var j=0;j<loc${status1.index}[i].length;j++) {
									}
								}
								
							    // Setup the different icons and shadows
							    var iconURLPrefix = 'http://maps.google.com/mapfiles/ms/icons/';
							    
							    var icons = [
							      iconURLPrefix + 'red-dot.png',
							      iconURLPrefix + 'green-dot.png',
							      iconURLPrefix + 'blue-dot.png',
							      iconURLPrefix + 'orange-dot.png',
							      iconURLPrefix + 'purple-dot.png',
							      iconURLPrefix + 'pink-dot.png',      
							      iconURLPrefix + 'yellow-dot.png'
							    ]
							    var iconsLength = icons.length;

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
							        icon: icons[iconCounter]
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
       		<div class="panel panel-default" style="height: auto!important; width: 100%!important;">
     			 <div class="panel panel-body" style="margin-top: 14px;">
     			 	<div class="col-md-12 pickupDetails error_txt">
     			 	<spring:theme code="checkout.multi.cnc.pickup.details.validation.msg"/>
     			 	</div>
     			 	<div class="pickUpPersonAjax">
     			 	</div>
     				 <div class="col-md-3">
      					 <span class="pickupperson"><h5 id="pickup"><spring:theme code="checkout.multi.cnc.pickup.person.name"/></h5></span></div>
       					 <div class="col-md-3">
        					<input type="text" id="pickupPersonName" class="inputname" placeholder="Enter Full Name" /><br/>
        					<div class="error_txt pickupPersonNameError"></div>
            			</div>
            			<div class="col-md-3">
							<input type="text" id="pickupPersonMobile" class="inputmobile" placeholder="Enter Mobile Number" /><br/>
							<div class="error_txt pickupPersonMobileError"></div>
        			    </div>
			             <div class="col-md-3">
			             <button type="button"  class="savenewid" id="savePickupPersondDetails"><spring:theme code="checkout.multi.cnc.pickup.details.submit"/></button>
			            </div>
					</div>
			</div>
		</div>
		</div>
		
		<div class="right-block shipping" style="margin-top: 94px;">
				<div class="checkout-order-summary">
					<multi-checkout:orderTotals cartData="${cartData}"
						showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" />
				</div>
			</div>
			<div>
				<input type="hidden" name="CSRFToken"
											value="a4e8152b-c9cd-429c-b88b-87d23391e386">
			</div>
		<div class="continue_holder">
			<c:choose>
				<c:when test="${delModeCount gt 0}">
				
				<form:form id="selectDeliveryMethodForm1" action="${request.contextPath}/checkout/multi/delivery-method/select" method="post" commandName="deliveryMethodForm">
								<button class="continue_btn" id="deliveryMethodSubmit1" type="submit" class="checkout-next" style="border: none;"><spring:theme code="checkout.multi.deliveryMethod.continue" text="Next"/></button>
				</form:form>
				</c:when>
				
				<c:otherwise>
					<div class="continue_btn">
						<a class="continue_btn_a" href="${request.contextPath}/checkout/multi/payment-method/add">CONTINUE</a>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
					</ycommerce:testId>
					</jsp:body>
	</multi-checkout:checkoutSteps>
	
</div>
</template:page>
