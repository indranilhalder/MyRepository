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
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
<div class="checkout-content cart checkout wrapper">

	<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
		<jsp:body>
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
	        /* var letters = new RegExp(/^(\w+\s?)*\s*$/); */	/* comment for INC144316084  */
	        var number = new RegExp(/\d/g);
	        /* start change of INC144316084  */
	        if(number.test(inputtxt))
	        {
	        /* if(inputtxt.value.match(letters))
	        {
	        	if(inputtxt.value.match(number))
		        { */
	        /* end change of INC144316084  */
		            return false;
		        /* start comment of INC144316084  */  
		        /* }
		        else
		        {
		            return true;
		        } 
	        } */
		    /* end comment of INC144316084  */
	        }
	        else
	        {
	            return true;	/* change for TMPPPRD issue of INC144316084  */	
	        }
	    }
		
		function checkMobileNumberSpace(number) {			
			return /\s/g.test(number);
		}
		
	    function checkWhiteSpace(text) {
	        var letters = new RegExp(/^(\w+\s?)*\s*$/);
	        var number = new RegExp(/\d/g);
	        if(letters.test(text))
		        {
		        	if(number.test(text))
			        {
			            return false;
			        }
			        else
			        {
			            var enteredText = text.split(" ");
	                    var length = enteredText.length;
	                    var count = 0;
	                    var countArray = new Array();
	                    for(var i=0;i<=length-1;i++) {
	                        if(enteredText[i]==" " || enteredText[i]=="" || enteredText[i]==null) {
	                            countArray[i] = "space";
	                            count++;
	                        } else {
	                            countArray[i] = "text";
	                        }
	                    }
	                    var lengthC = countArray.length;
	                    for(var i=0;i<=lengthC-1;i++) {
	                        //console.log(countArray[i+1]);
	                        if(countArray[i] == "space" && countArray[i+1] == "space" || countArray[i] == "text" && countArray[i+1] == "space" && countArray[i+2] == "text" || countArray[i] == "text" && countArray[i+1] == "space") {
	                            return false;
	                            break;
	                        } else if (i == lengthC-1) {
	                        	return true;
	                        	break;
	                        }   
	                    }
			        }
		        }
		        else
		        {
		            return false;
		        }
	    }
	
		$(document).ready(function(){
			
			if($(document).width() >= "1100") {
				$(".right-block").css("width", "324px");
				var mapWidth = $(".header4").width();
				mapWidth = parseInt(mapWidth)+10;
				$(".mapWidth").css("width", mapWidth+"px");
				console.log("Greater than 1100 changed map width"); 
				
			} else {
				console.log("changed map width"); 
				var mapWidth = $(document).width()-30;
				$(".mapWidth").css("width", mapWidth+"px");
				$(".continue_btn").css("width", mapWidth+"px");
			}
			$(".pickUpPersonAjax").hide();
			$(".pickupPersonSubmitError").hide();
			
			$("#pickupPersonSubmit").hide();
			$("#pickupPersonName").keyup(function(){
				$(".pickupPersonSubmitError").hide();
				$(".pickUpPersonAjax").hide();
				//alert("hello")
				$(".pickupPersonMobileError").hide();
				$(".pickupPersonNameError").hide();
				$(".pickupDetails").hide();
				var pickupPersonName = $("#pickupPersonName").val();
				var pickupPersonMobile = $("#pickupPersonMobile").val();
				var pickUpPersonNam = document.pickupPersonDetails.pickupPersonName;
				var statusName = allLetter(pickUpPersonNam);
				var nameCheck = checkWhiteSpace($("#pickupPersonName").val());
				if($('#pickupPersonName').val().length <= "3"){ 
					$(".pickupPersonNameError").show();
					$(".pickupPersonNameError").text("Enter Atleast 4 Letters");
				}
				else if(nameCheck == false){
					   $(".pickupPersonNameError").show();
					   $(".pickupPersonNameError").text("Spaces cannot be allowed");
				 }
				else if(statusName == false) {
					$(".pickupPersonNameError").show();
					$(".pickupPersonNameError").text("Please Enter Only Alphabets");
				}
			});
			$("#pickupPersonMobile").keyup(function(){
				$(".pickupPersonSubmitError").hide();
				$(".pickUpPersonAjax").hide();
				//alert("hello")
				$(".pickupPersonMobileError").hide();
				$(".pickupPersonNameError").hide();
				$(".pickupDetails").hide();
				var pickupPersonMobile = $("#pickupPersonMobile").val();
				var isString = isNaN($('#pickupPersonMobile').val());
				var mobileSpaceCheck = checkMobileNumberSpace($('#pickupPersonMobile').val());
				if($('#pickupPersonMobile').val().length <= "10") {
					if(isString==true || mobileSpaceCheck==true) {
						$(".pickupPersonMobileError").show();
						$(".pickupPersonMobileError").text("Enter only numbers");
					}
					else if ($('#pickupPersonMobile').val().length <= "9") {
						$(".pickupPersonMobileError").show();
						$(".pickupPersonMobileError").text("Enter 10 Digit Number");
					}
					else if($('#pickupPersonMobile').val().indexOf("-") > -1 || $('#pickupPersonMobile').val().indexOf("+") > -1 ) {
						$(".pickupPersonMobileError").show();
						$(".pickupPersonMobileError").text("Enter only numbers");
					}
				}
			});
			
			function submitPickupPersionDetails() {
				var pickupPersonName = $("#pickupPersonName").val();
				var pickupPersonMobile = $("#pickupPersonMobile").val();
				var requiredUrl = ACC.config.encodedContextPath +"/checkout/multi/delivery-method/addPickupPersonDetails";
				var dataString = 'pickupPersonName=' + pickupPersonName+ '&pickupPersonMobile=' + pickupPersonMobile;
					$.ajax({
					url : requiredUrl,
					data : dataString,
					success : function(data) {
						//console.log("success call for pickup person details"+data);
						$(".pickUpPersonAjax").fadeIn(100);
						if($("#pickupPersonSubmit").text() != "1") {
							$(".pickUpPersonAjax").append("<span class='pickupText'>Pickup Person Details Have Successfully Added.</span>");
						}
						$("#pickupPersonSubmit").text("1");
						
						<!---TPR-639-->
						utag.link(
								{link_text: 'collectatstore_pickup_submit' , event_type : 'collectatstore_pickup_submit'}
							 );
						<!---End of TPR-639-->

					},
					error : function(xhr, status, error) {
						console.log(error);	
						//TPR-6369 |Error tracking dtm
		 				dtmErrorTracking("Add pick up person details Error","errorname");
					}
				});
			}
			
			function submitPickupPersonDetailsOnLoad() {
				if($("#pickupPersonName").val().length >= "1" && $("#pickupPersonMobile").val().length >= "1") {
					submitPickupPersionDetails();
				}
			}
			
			setTimeout(submitPickupPersonDetailsOnLoad(), 2000);
			
			$("#savePickupPersondDetails").click(function(){
				$(".pickupPersonSubmitError").hide();
				$(".pickUpPersonAjax").hide();
				//alert("hello")
				$(".pickupPersonMobileError").hide();
				$(".pickupPersonNameError").hide();
				$(".pickupDetails").hide();
				
				//console.log("Working");
				var pickupPersonName = $("#pickupPersonName").val();
				var pickupPersonMobile = $("#pickupPersonMobile").val();
				var isString = isNaN($('#pickupPersonMobile').val());
				var pickUpPersonNam = document.pickupPersonDetails.pickupPersonName;
				var statusName = allLetter(pickUpPersonNam);
				var nameCheck = checkWhiteSpace($("#pickupPersonName").val());
				var mobileSpaceCheck = checkMobileNumberSpace($('#pickupPersonMobile').val());
				if($('#pickupPersonName').val().length <= "3"){ 
					$(".pickupPersonNameError").show();
					$(".pickupPersonNameError").text("Enter Atleast 4 Letters");
				}
				else if(nameCheck == false){
					   $(".pickupPersonNameError").show();
					   $(".pickupPersonNameError").text("Spaces cannot be allowed");
				 }
				else if(statusName == false) {
					$(".pickupPersonNameError").show();
					$(".pickupPersonNameError").text("Please Enter Only Alphabets");
				}
				else if($('#pickupPersonMobile').val().length <= "10") {
					if(isString==true || mobileSpaceCheck==true) {
						$(".pickupPersonMobileError").show();
						$(".pickupPersonMobileError").text("Enter only numbers");
					}
					else if ($('#pickupPersonMobile').val().length <= "9") {
						$(".pickupPersonMobileError").show();
						$(".pickupPersonMobileError").text("Enter 10 Digit Number");
					}
					else if($('#pickupPersonMobile').val().indexOf("-") > -1 || $('#pickupPersonMobile').val().indexOf("+") > -1 ) {
						$(".pickupPersonMobileError").show();
						$(".pickupPersonMobileError").text("Enter only numbers");
					}
				
					/* else if($('#pickupPersonMobile').val().length > "10") {
						$(".pickupPersonMobileError").show();
						$(".pickupPersonMobileError").text("Enter only 10 Digit Number");
					} */
					/* else if(isString==true) {
						$(".pickupPersonMobileError").show();
						$(".pickupPersonMobileError").text("Enter only numbers");
					} */
					else {
						submitPickupPersionDetails();
					}
					
					<!------ TPR - 639 --------->
					/* 
					utag.link(
								{link_text: 'collectatstore_pickup_submit' , event_type : 'collectatstore_pickup_submit'}
							 ); */
					<!------ TPR - 639 --------->
				}
			});
			
			$(".pickupDetails").hide();
		});
	</script>
	<script type="text/javascript">

	function openPopForAdddPosToCartEntry(ussId,posName){
		//var productCode = $("#product").val();
		//alert(ussId+"@@@"+posName);
		$(".continue_btn, .continue_btn_a").css("pointer-events", "none");
		$(".continue_btn, .continue_btn_a").css("cursor", "default"); 
		$(".continue_btn, .continue_btn_a").attr("data-id", $(".continue_btn, .continue_btn_a").attr("href"));
		$(".continue_btn, .continue_btn_a").removeAttr("href");
		var requiredUrl = ACC.config.encodedContextPath +"/checkout/multi/delivery-method/addPosToOrderEntry";
		var dataString = 'ussId=' + ussId+ '&posName=' + posName;
			$.ajax({
			url : requiredUrl,
			data : dataString,
			success : function(data) {
				$(".continue_btn, .continue_btn_a").css("pointer-events", "all");
				$(".continue_btn, .continue_btn_a").css("cursor", "pointer"); 
				$(".continue_btn, .continue_btn_a").attr("href", $(".continue_btn, .continue_btn_a").attr("data-id"));
			},
			error : function(xhr, status, error) {
				alert("Oops something went wrong!!!");	
			}
		});
	}
	
	function test(i, j) {
		console.log(i+"@@"+j);
		//alert("Hello");
		//var posData = $(".addPos"+i).text();
		//posData = posData.split('@');
		//openPopForAdddPosToCartEntry(posData[0], posData[1]);
		
		//Change color for Radio Button
		
		$(".removeColor"+i+" .radio_color").removeClass("colorChange");
		$(".select_store").hide();
		var name = $(".name"+i+""+j).text();
		$(".radio_sel"+i+""+j).addClass("colorChange");
		
		
		// Load Map for the selected Store
		
		//console.log(posData[0]+" @@ "+posData[1]);
		var iconURLPrefix = '${request.contextPath}/_ui/responsive/theme-blue/images/storemarkericons/';
	    
	    iconURLPrefix = iconURLPrefix.replace("/mpl/en/","/");
	    var icons = new Array();
		for(var y=0;y<25;y++) {
			k = parseInt(y)+1;
    		icons[y] = iconURLPrefix + 'markergrey' + k +'.png'
	    }
		
		//console.log(icons);
    	//number = number.replace("address","");
    	//number++;
    	var myCounter = "1";
    	var iconNumber = parseInt(j) + parseInt(myCounter);
    	var url =  iconURLPrefix + 'marker' + parseInt(iconNumber) +'.png';
    	icons[j] = url;	
    	console.log(icons[j]);
		var loc = $(".latlng"+i).text();
		//console.log(loc);
		loc = loc.split("@");
		var length = loc.length;
		
		for(var k=0;k<length;k++){
			loc[k] = loc[k].split(",");
			for(var l=0;l<loc[k].length;l++) {
			}
		}
	    var map = new google.maps.Map(document.getElementById('map'+i), {
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
	    for (var y = 0; y < length; y++) {  
	      var marker = new google.maps.Marker({
	        position: new google.maps.LatLng(loc[y][1], loc[y][2]),
	        map: map,
	        icon: icons[iconCounter]
	      });

	      markers.push(marker);

	      console.log("Lat :"+loc[y][1]+", Lng :"+loc[y][2]);
	      
	      iconCounter++;
	      // We only have a limited number of possible icon colors, so we may have to restart the counter
	      if(iconCounter >= "25") {
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
		
		//openPopForAdddPosToCartEntry(ussid,name);
	}
</script>
					
					<ycommerce:testId code="checkoutStepTwo">
						<div class="checkout-shipping-items  left-block left-block-width">
			<h1>
					<spring:theme code="checkout.multi.cnc.header"></spring:theme>
					<br>
			</h1>
			
			
			<div class="productCount">
				(for ${cnccount} out of ${delModeCount + cnccount} items in your bag)
			</div>
			
			<ul id="deliveryradioul" class="checkout-table product-block classRadio">
						
						<c:forEach items="${pwpos}" var="poses" varStatus="status1">
							<li class="header item${status1.index}">
							<ul class="headline">
								<li class="header2 headerWidth"><spring:theme code="text.product.information"/></li>
								<li class="store header5"><spring:theme code="checkout.multi.cnc.store.closeto"/>
								
								<c:if test="${not empty defaultPincode}">
									<span style="color: #A9143C!important;" id="changeValue${status1.index}">
										${defaultPincode}
									</span>	
								</c:if>
								</li>
								<li class="delivery header4 deliveryWidth"><button class="changeDeliveryMethod"><spring:theme code="checkout.multi.cnc.store.change.delivery.mode"/></button></li>
								
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
						
						<!-- Freebie Product Details -->
							
								
								 <c:if test="${not empty poses.product.freebieProducts}">
									<c:forEach items="${poses.product.freebieProducts}" var="freebieProds">
										<%-- ${freebieProds.associateProductData.code}
										${freebieProds.associateProductData} --%>
										<%-- ${freebieProds.product.code}
										${freebieProds.sellerName}
										${freebieProds.qty} --%>
										<li class="item delivery_options">
											<ul>
												<li>
													<div>
														<div class="thumb product-img">
															<a href="${request.contextPath}/${freebieProds.product.url}"><product:productPrimaryImage
																	product="${freebieProds.product}" format="thumbnail" /></a>
														</div>
														<div class="details product">
															<h3 class="product-brand-name">
																<a href="">${freebieProds.product.brand.brandname}</a>
															</h3>
															<ycommerce:testId code="cart_product_name">
																<a href="${request.contextPath}/${freebieProds.product.url}"><div
																		class="name product-name">${freebieProds.product.name}</div></a>
															</ycommerce:testId>
															<c:if test="${not empty freebieProds.product.code}">
															<div class="freebieId">Product ID: ${freebieProds.product.code}</div>
															</c:if>
															<c:if test="${not empty freebieProds.product.size}">
															<div class="freebieSize"><spring:theme code="text.size"/> ${freebieProds.product.size}</div>
															</c:if>
															<c:if test="${not empty freebieProds.product.colour}">
															<div class="freebieColor"><spring:theme code="text.colour"/> ${freebieProds.product.colour}</div>
															</c:if>
															<c:if test="${not empty freebieProds.sellerName}">
															<div class="sellerName"><spring:theme code="text.seller.name"/> ${freebieProds.sellerName}</div>
															</c:if>
															<c:if test="${not empty freebieProds.qty}">
															<div class="freebieQty"><spring:theme code="text.qty"/> ${freebieProds.qty}</div>
															</c:if>
														</div>
													</div>
												</li>
											</ul>
										</li>
										
								</c:forEach>
							</c:if>
							<!-- /. Freebie Product Details -->
							
							<li class="item delivery_options item${status1.index}">
								<ul>
										<li class="headerWidth">
											<div>
												<div class="thumb product-img">
													<a class="productUrlName" href="${poses.product.url}"><product:productPrimaryImage
															product="${poses.product}" format="thumbnail" /></a>
												</div>
												<div class="details product" >
													<h3 class="product-brand-name">${poses.product.brand.brandname}</h3>
													<ycommerce:testId code="cart_product_name">
														<a class="productUrlName" href="${poses.product.url}"><div class="name product-name">${poses.product.name}</div></a>
													</ycommerce:testId>
																									<!-- start TISEE-4631 TISUAT-4229 -->
												<c:if test="${not empty poses.product.code}">
															<div class="freebieId">Product ID: ${poses.product.code}</div>
												</c:if>
												<c:if test="${fn:toUpperCase(poses.product.rootCategory) != 'ELECTRONICS'}">
												 	<ycommerce:testId code="cart_product_size">
												 		<c:if test="${not empty poses.product.size}">
												 			<div class="size"><spring:theme code="text.size"/>${poses.product.size}</div>
												 		</c:if>
													</ycommerce:testId>
												 	<ycommerce:testId code="cart_product_colour">
														<c:if test="${not empty poses.product.colour}">
															<div class="colour"><spring:theme code="text.colour"/>${poses.product.colour}</div>
														</c:if>
													</ycommerce:testId>
													<div class="item-price delivery-price">
														<format:price priceData="${poses.product.price}"/>
													</div>
												 </c:if>
												<!-- end TISEE-4631 TISUAT-4229 -->
												<!-- end TISEE-4631 TISUAT-4229 -->
												<ycommerce:testId code="cart_product_colour">
													<c:if test="${not empty poses.sellerName}">
														<div class="colour"><spring:theme code="text.seller.name"/> ${poses.sellerName}</div>
													</c:if>
													<ycommerce:testId code="cart_product_quantity">
														<c:if test="${not empty poses.quantity}">
															<div class="quantity"><spring:theme code="text.qty"/> ${poses.quantity}</div>
														</c:if>
													</ycommerce:testId>
												</ycommerce:testId>
												</div>
									      </div>
										</li>
							<div class="latlng latlng${status1.index}"><c:forEach items="${poses.pointOfServices}" var="pos" varStatus="status"><c:if test="${(status.index != 0)}">@</c:if> '${pos.displayName}', ${pos.geoPoint.latitude}, ${pos.geoPoint.longitude}</c:forEach>
							</div>
							<li class="delivery deliveryWidth">
									<div class="error_txt pincodeServicable${status1.index}" style="width: 200px;font-size: 12px;"></div>
									<ul class="delivered scrollThis delivered${status1.index}">
							<c:forEach items="${poses.pointOfServices}" var="pos" varStatus="status">
										<li class="removeColor${status1.index}">
											<%-- <input class="radio_btn" type="radio" name="address" id="address${status.index}" value="address${status.index}"> --%>
											<input class="radio_btn radio_btn${status1.index}" type="radio" name="address${status1.index}" id="address${status1.index}${status.index}" value="address${status.index}">
												<div class='pin bounce'>
													<span class="text_in">${status.count}</span>
														</div>
															<label class="radio_sel${status1.index}${status.index} displayName${status1.index}${status.index} radio_color delivery-address" style="color: #ADA6A6;">${pos.displayName}</label>
															<label class="radio_sel${status1.index}${status.index} name${status1.index}${status.index} radio_color delivery-address" style="display: none;">${pos.name}</label>
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
																<span class="radio_sel${status1.index}${status.index} radio_color" style="text-transform: none; display: inline-block;" >PiQ up hrs :</span>
																
																<c:if test="${not empty pos.mplOpeningTime && not empty pos.mplClosingTime}">
																	<span class="pickup${status1.index}${status.index} radio_sel${status1.index}${status.index} radio_color" style=" display: inline-block; margin-left: 0px;">${pos.mplOpeningTime} - ${pos.mplClosingTime}</span>
																	</c:if>
																
																<span class="collectionDays${status1.index}${status.index} collectionDays"><c:if test="${not empty pos.mplWorkingDays}">${pos.mplWorkingDays}</c:if></span>
																<span class="weeklyOff${status1.index}${status.index} radio_sel${status1.index}${status.index} radio_color" style="text-transform: capitalize;"></span>
																
																
										</li>
										<script>
											$(document).ready(function() {
												var time = "";
												time = $(".pickup${status1.index}${status.index}").text().split("-");
												$(".pickup${status1.index}${status.index}").text("");
												for(i=0;i<time.length;i++) {
													var spaceRemove = "";
													spaceRemove = time[i].replace(" ","");
													spaceRemove = spaceRemove.split(":");
													$(".pickup${status1.index}${status.index} ").append(spaceRemove[0]+":"+spaceRemove[1]);
													if(i!=time.length-1) {
														$(".pickup${status1.index}${status.index}").append(" - ");
													}
												}
												
												var	collectionDays${status1.index}${status.index} = $(".collectionDays${status1.index}${status.index}").text().split(",");
												//var	collectionDays${status1.index}${status.index} = ["0","1","2","3","4","5","6"];
												var weekDays = ["0","1","2","3","4","5","6"];
												var collectionWeek = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
												    missing${status1.index}${status.index} = new Array();
												var count = 0;
												var i = 0,
												    lenC = weekDays.length;

												for ( ; i < lenC; i++ ) {
												    if ( collectionDays${status1.index}${status.index}.indexOf(weekDays[i]) == -1 ) {
													 	missing${status1.index}${status.index}[count] = weekDays[i]; count++; 
													}
												}
												//console.log(missing${status1.index}${status.index});
												if(missing${status1.index}${status.index}.length < 1) {
													$(".weeklyOff${status1.index}${status.index}").text("Weekly Off : All Days Open");
												}
												else {
													$(".weeklyOff${status1.index}${status.index}").text("Weekly Off : ");
													for(var y = 0; y < missing${status1.index}${status.index}.length; y++) {
														$(".weeklyOff${status1.index}${status.index}").append(collectionWeek[missing${status1.index}${status.index}[y]]);
														if(y != missing${status1.index}${status.index}.length-1) {
															$(".weeklyOff${status1.index}${status.index}").append(", ");
														}
													}
												}
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
													var name${status.index} = $(".name${status1.index}${status.index}").text();
													openPopForAdddPosToCartEntry('${poses.ussId}',name${status.index});
													$(".radio_sel${status1.index}${status.index}").addClass("colorChange");
												});
											});
										</script>
							</c:forEach>
							</ul>
								</li>
							
							<li class="mapConatinerParent">
													<ul class="mapWidth" id="map${status1.index}" style="width: 324px; height: 200px; position: relative; overflow: hidden; transform: translateZ(0px); background-color: rgb(229, 227, 223);"></ul>
													<ul id="maphide${status1.index}" style="display: none; width: 300px; height: 200px; position: relative; overflow: hidden; transform: translateZ(0px); background-color: rgb(229, 227, 223);padding: 10px; font-weight: 600">Unable to find Stores</ul>
													<div class="change_pincode_block block${status1.index}">
														<span class="change_txt txt${status1.index}">Change Pincode?</span>
														<div class="input${status1.index} row" style="width: 111%">
															<div class="col-md-8 col-sm-4 col-xs-4" style="padding:0px;">
																<input style="width: 100%" type="text" name="changepin${status1.index}" class="changepin${status1.index}" maxlength="6" placeholder="Enter Pincode to Change.">
															</div>
															<div class="col-md-4 col-sm-2 col-xs-2">
																<button class="submitPincode submitPincode${status1.index}" style="height: 40px !important; background: #A9143C !important; border: none !important; color: #fff !important;" name="submitPincode${status1.index}">Submit</button>
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
							    
							    var icons = new Array();
							    for(var i=1;i<=25;i++) {
						    		icons[i-1] = iconURLPrefix${status1.index} + 'markergrey' + i +'.png'
								}
							    var icons${status1.index} = new Array();
							    for(var i=1;i<=25;i++) {
						    		icons${status1.index}[i-1] = iconURLPrefix${status1.index} + 'markergrey' + i +'.png'
							    }
 
							    var iconsLength = icons${status1.index}.length;
							    
							    $(".radio_btn${status1.index}").click(function(){
							    	var number = $(this).val();
							    	for(var i=1;i<=25;i++) {
							    		icons${status1.index}[i-1] = iconURLPrefix${status1.index} + 'markergrey' + i +'.png'
								    }
							    	number = number.replace("address","");
							    	//number++;
							    	var myCounter = "1";
							    	var iconNumber = parseInt(number) + parseInt(myCounter);
							    	var url =  iconURLPrefix${status1.index} + 'marker' + parseInt(iconNumber) +'.png';
							    	icons${status1.index}[number] = url;
							    	processMap${status1.index}();
							    	<!-- TPR-639 -->
									utag.link(
									{link_text: 'collectatstore_store_selection' , event_type : 'collectatstore_store_selection'}
									);
									<!-- TPR-639 -->
							    	//console.log(url);
							    });
								
							    $(".changepin${status1.index}").keyup(function(){
							    	$(".pincodeValidation").hide();
								    var pinvalue${status1.index} = $(".changepin${status1.index}").val();
									var pinlength = pinvalue${status1.index}.length;
									var isString = isNaN($(".changepin${status1.index}").val());
									var mobileSpaceCheck = checkMobileNumberSpace($(".changepin${status1.index}").val());
									if($(".changepin${status1.index}").val().length <= "6") {
										if(isString==true || mobileSpaceCheck==true) {
											$(".pincodeValidation").show();
											$(".pincodeValidation").text("Enter Only Digits");
										}
										else if($(".changepin${status1.index}").val().indexOf("-") > -1 || $(".changepin${status1.index}").val().indexOf("+") > -1 ) {
											$(".pincodeValidation").show();
											$(".pincodeValidation").text("Enter Only Digits");
										}
									}
							    });
								$(".submitPincode${status1.index}").click(function(){
									$(".removeColor${status1.index} .radio_color").removeClass("colorChange");
									$(".pincodeServicable${status1.index}").hide();
									$(".pincodeValidation").hide();
									var pinvalue${status1.index} = $(".changepin${status1.index}").val();
									var pinlength = pinvalue${status1.index}.length;
									var isString = isNaN($(".changepin${status1.index}").val());
									var mobileSpaceCheck = checkMobileNumberSpace($(".changepin${status1.index}").val());
									if($(".changepin${status1.index}").val().length <= "6") {
										if(isString==true || mobileSpaceCheck==true) {
											$(".pincodeValidation").show();
											$(".pincodeValidation").text("Enter Only Digits");
										}
										else if ($(".changepin${status1.index}").val().length <= "5") {
											$(".pincodeValidation").show();
											$(".pincodeValidation").text("Please Enter 6 Digits");
										}
										else if($(".changepin${status1.index}").val().indexOf("-") > -1 || $(".changepin${status1.index}").val().indexOf("+") > -1 ) {
											$(".pincodeValidation").show();
											$(".pincodeValidation").text("Enter Only Digits");
										}
										else {
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
										        	  //icons${status1.index} = icons;
										        	  for(var i=1;i<=25;i++) {
												    	icons${status1.index}[i-1] = iconURLPrefix${status1.index} + 'markergrey' + i +'.png'
												      }
										        	  processMap${status1.index}();
										        	  $(".delivered${status1.index}").show();
										        	  $("#map${status1.index}").show();
										        	  $("#maphide${status1.index}").hide();
											          var changecordinates${status1.index} = " ";
											          $(".removeColor${status1.index}").remove();
											          openPopForAdddPosToCartEntry("${poses.ussId}", "n/a");
											          for(var i=0;i<jsonObject${status1.index}.length;i++) {
											        	  var count = parseInt(i) + 1;
											        	  var hello = "${status1.index}";
											        	  var ussid = '"${poses.ussId}"';
											        	  var posName = '"'+jsonObject${status1.index}[i]['name']+'"';
											        	  //var name = jsonObject${status1.index}[i]['name'];
											        	  $(".delivered${status1.index}").append("<li style='width: 240px !important;' class='removeColor${status1.index} remove${status1.index}"+i+"'><input onclick='test("+hello+", "+i+"); openPopForAdddPosToCartEntry("+ussid+", "+posName+");' class='radio_btn radio_btn${status1.index}' name='address${status1.index}' id='address${status1.index}"+i+"' value='address0' type='radio'><div class='pin bounce'><span class='text_in'>"+count+"</span></div></li>")
											        	  $(".remove${status1.index}"+i).append("<label class='radio_sel${status1.index}"+i+" displayName${status1.index}"+i+" radio_color delivery-address' style='color: #ADA6A6;'></label>");
											        	  $(".remove${status1.index}"+i).append("<label class='addPos"+i+"' style='display: none;'>${poses.ussId}@"+jsonObject${status1.index}[i]['name']+"</label>");
											        	  $(".remove${status1.index}"+i).append("<label class='radio_sel${status1.index}"+i+" name${status1.index}"+i+" radio_color delivery-address' style='display:none; color: #ADA6A6;'></label>");
											        	  var initial = 1;
											        	  $(".remove${status1.index}"+i).append("<span class='radio_sel${status1.index}"+i+" radio_color address"+initial+"${status1.index}"+i+"'></span>");
											        	  initial++;
											        	  $(".remove${status1.index}"+i).append("<span class='radio_sel${status1.index}"+i+" radio_color address"+initial+"${status1.index}"+i+"'></span>");
											        	  initial++;
											        	  $(".remove${status1.index}"+i).append("<span class='radio_sel${status1.index}"+i+" radio_color address"+initial+"${status1.index}"+i+"'></span>");
											        	  initial++;
											        	  $(".remove${status1.index}"+i).append("<span class='radio_sel${status1.index}"+i+" radio_color address"+initial+"${status1.index}"+i+"'></span>");
											        	  $(".remove${status1.index}"+i).append("<span class='radio_sel${status1.index}"+i+" radio_color' style='text-transform: capitalize; display: inline-block;'>PiQ up hrs : </span>");
											        	  $(".remove${status1.index}"+i).append("<span class='pickup${status1.index}"+i+" radio_sel${status1.index}"+i+" radio_color' style='display: inline-block; margin-left: 0px;'></span>");
											        	  $(".remove${status1.index}"+i).append("<span class='weeklyOff${status1.index}"+i+" radio_sel${status1.index}"+i+" radio_color' style='text-transform: capitalize;'></span>");
											        	  if(jsonObject${status1.index}[i]['displayName'] != null) {
											        	  	  $(".displayName${status1.index}"+i).text(jsonObject${status1.index}[i]['displayName']);
											        	  } else {
											        		  $(".displayName${status1.index}"+i).text("");
											        	  }if(jsonObject${status1.index}[i]['name'] != null) {
											        	  	  $(".name${status1.index}"+i).text(jsonObject${status1.index}[i]['name']);
											        	  } else {
											        		  $(".name${status1.index}"+i).text("");
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
											        	  }if(jsonObject${status1.index}[i]['mplClosingTime'] != null && jsonObject${status1.index}[i]['mplOpeningTime'] != null) {
											        		  openingTime = jsonObject${status1.index}[i]['mplOpeningTime'].split(":");
											        		  closingTime = jsonObject${status1.index}[i]['mplClosingTime'].split(":");
											        	  	$(".pickup${status1.index}"+i).text(openingTime[0]+":"+openingTime[1]+" - "+closingTime[0]+":"+closingTime[1]);
											        	  } else {
											        		  $(".pickup${status1.index}"+i).text("");  
											        	  }
											        	  
											        	  //console.log(jsonObject${status1.index}[i]['mplWorkingDays']);
											        	  var	collectionDays${status1.index} = jsonObject${status1.index}[i]['mplWorkingDays'].split(",");
															//var	collectionDays${status1.index}${status.index} = ["0","1","2","3","4","5","6"];
															var weekDays = ["0","1","2","3","4","5","6"];
															var collectionWeek = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
															    missing${status1.index} = new Array();
															var count = 0;
															var k = 0,
															    lenC = weekDays.length;

															for ( ; k < lenC; k++ ) {
															    if ( collectionDays${status1.index}.indexOf(weekDays[k]) == -1 ) {
																 	missing${status1.index}[count] = weekDays[k]; count++; 
																}
															}
															$(".weeklyOff${status1.index}"+i).text("Changed");
															if(missing${status1.index}.length < 1) {
																$(".weeklyOff${status1.index}"+i).text("Weekly Off : All Days Open");
															}
															else {
																/* console.log("Working"); */
																$(".weeklyOff${status1.index}"+i).text("Weekly Off : ");
																for(var y = 0; y < missing${status1.index}.length; y++) {
																	$(".weeklyOff${status1.index}"+i).append(collectionWeek[missing${status1.index}[y]]);
																	if(y != missing${status1.index}.length-1) {
																		$(".weeklyOff${status1.index}"+i).append(", ");
																	}
																}
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
											          
										        	} else {
										        		$(".pincodeServicable${status1.index}").show();
										        		$(".delivered${status1.index}").hide();
										        		$("#map${status1.index}").hide();
										        		$("#maphide${status1.index}").show();
										        		$(".pincodeServicable${status1.index}").text("This pincode is not servicable");
										        		$("#changeValue${status1.index}").text(pinvalue${status1.index});
										        	}
				
									           
									          },
									          error : function(xhr, data, error) {
									        	  console.log("Error in processing Ajax. Error Message : " +error+" Data : " +data)
									        	  	$(".pincodeServicable${status1.index}").show();
									        		$(".delivered${status1.index}").hide();
									        		$("#map${status1.index}").hide();
									        		$("#maphide${status1.index}").show();
									        		$(".pincodeServicable${status1.index}").text("This pincode is not servicable");
									        		$("#changeValue${status1.index}").text(pinvalue${status1.index});
												}
									         });
										
										$(".txt${status1.index}").show();
										$(".input${status1.index}").hide();
									} 
									}
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
				<div class="panel">
					<div class="pickUpPersonAjax"></div>
   			 	</div>
       			<div class="panel panel-default pickuppersonWidth">
       				<h2 class="panel-defaultHeading">Collect in store: Personal Info</h2>
       				<p class="retail-bag"></p>
       				<div class="retail-detail">
       					Please provide us with your name and phone number so we can assist you quickly and easily
       				</div>
     			 	<div class="panel panel-body">
     			 		<div class="pickupDetails error_txt">
     			 		<spring:theme code="checkout.multi.cnc.pickup.details.validation.msg"/>
     			 		</div>
	     			 	<form name="pickupPersonDetails" action="#">
	     				 <div style="display:none;">
	      					 <span class="pickupperson"><h5 id="pickup"><spring:theme code="checkout.multi.cnc.pickup.person.name"/></h5></span></div>
	       					 <div class="nameFullSubPanel">
	        					<input type="text" id="pickupPersonName" name="pickupPersonName"  maxlength="30" class="inputname" placeholder="Enter Full Name*"  value="${pickupPersonName}"/>
	        					<div class="error_txt pickupPersonNameError"></div>
	            			</div>
	            			 <div class="nameBlankSubPanel">
	        					<input type="text" name="pickupPersonName"  maxlength="30" class="inputname"/>
	        					<div class="error_txt pickupPersonNameError"></div>
	            			</div>
	            			<div class="mobileSubPanel">
								<input type="text" id="pickupPersonMobile" class="inputmobile" maxlength="10" placeholder="Enter Mobile Number*" value="${pickUpPersonMobile}"/><br/>
								<div class="error_txt pickupPersonMobileError"></div>
	        			    </div>
				             <div class="submitSubPanel">
				             <button type="button"  class="savenewid" id="savePickupPersondDetails"><spring:theme code="checkout.multi.cnc.pickup.details.submit"/></button>
				          <div id="pickupPersonSubmit"></div>
				          <div class="error_txt pickupPersonSubmitError"></div>
				            </div>
				            <div id="pickUpDetailsMsg" style="padding-top: 10px;display: none;"><spring:theme code="checkout.multi.cnc.pickup.details.below.msg"/></div>
				            <p style="clear:both;"></p>&nbsp;
				           </form>
			            
					</div>
			</div>
		</div>
		</div>
		
		<div class="right-block shipping right-block-width">
				<c:choose>
					<c:when test="${expCheckout gt 0}">
						<a class="continue_btn" href="${request.contextPath}/checkout/multi/delivery-method/invReservation" type="button">	
							<div class="continue_btn_anchor"><spring:theme code="checkout.multi.deliveryAddress.continue"  text="Next"/></div>
						</a>
					</c:when>
					<c:when test="${delModeCount gt 0}">
					
					<form:form id="selectDeliveryMethodForm1" action="${request.contextPath}/checkout/multi/delivery-method/select" method="post" commandName="deliveryMethodForm">
									<button class="continue_btn" id="deliveryMethodSubmit1" type="submit" class="checkout-next" style="border: none;"><spring:theme code="checkout.multi.deliveryMethod.continue" text="Next"/></button>
					</form:form>
					</c:when>
					
					<c:otherwise>
					<a class="continue_btn" href="${request.contextPath}/checkout/multi/delivery-method/invReservation" type="button">
							<div class="continue_btn_anchor"><spring:theme code="checkout.multi.deliveryAddress.continue"  text="Next"/></div>
					</a>
					</c:otherwise>
				</c:choose>
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
				<c:when test="${expCheckout gt 0}">
					<a class="continue_btn_a" href="${request.contextPath}/checkout/multi/delivery-method/invReservation" type="button">
					<div class="continue_btn">
						<spring:theme code="checkout.multi.deliveryAddress.continue"  text="Next"/>
					</div>
					</a>
				</c:when>
				<c:when test="${delModeCount gt 0}">
				
				<form:form id="selectDeliveryMethodForm1" action="${request.contextPath}/checkout/multi/delivery-method/select" method="post" commandName="deliveryMethodForm">
								<button class="continue_btn" id="deliveryMethodSubmit1" type="submit" class="checkout-next" style="border: none;display: none;"><spring:theme code="checkout.multi.deliveryMethod.continue" text="Next"/></button>
				</form:form>
				</c:when>
				
				<c:otherwise>
				<a class="continue_btn_a" href="${request.contextPath}/checkout/multi/delivery-method/invReservation" type="button">
					<div class="continue_btn">
						<spring:theme code="checkout.multi.deliveryAddress.continue"  text="Next"/>
					</div>
				</a>
				</c:otherwise>
			</c:choose>
		</div>
		<script>
			$(document).ready(function(){
				var productUrlNew = $(".productUrlName").attr("href");
				var latestProductUrl = ACC.config.encodedContextPath + productUrlNew;
				$(".productUrlName").attr("href", latestProductUrl);
				
				$(".changeDeliveryMethod").click(function(e){
					//var attr = $(this).attr();
					e.preventDefault();
					location.replace("${request.contextPath}/checkout/multi/delivery-method/changeDeliveryMode");
				});
					
			});
		</script>
					</ycommerce:testId>
					</jsp:body>
	</multi-checkout:checkoutSteps>
	
</div>
</template:page>
