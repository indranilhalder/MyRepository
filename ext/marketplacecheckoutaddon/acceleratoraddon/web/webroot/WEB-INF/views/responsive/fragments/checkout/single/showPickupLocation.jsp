<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>

<%--  <c:set var = "addressFlag" scope="session" value = "${addressFlag}" />  --%>


<!-- <script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"> -->
<div class="checkout-content cart checkout wrapper">

	<script>
					//TISST-13010
					$(document).ready(function() {
						showPromotionTag();
					});
					/* var timeoutID;
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
					} */
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
				if(ACC.singlePageCheckout.getIsResponsive())
				{
					ACC.singlePageCheckout.getPickUpPersonPopUpMobile();
					//$("#singlePagePickupPersonPopup").modal('show');
				}
				$(".continue_btn, .continue_btn_a").css("pointer-events", "all");
				$(".continue_btn, .continue_btn_a").css("cursor", "pointer"); 
				$(".continue_btn, .continue_btn_a").attr("href", $(".continue_btn, .continue_btn_a").attr("data-id"));
			},
			error : function(xhr, status, error) {
				alert("Oops something went wrong!!!");	
			}
		});
	}
	
</script>
	<div class="checkout-shipping-items  left-block left-block-width">
	<span class="cnc_arrow"></span>
		<div class="cnc_title_search">
		<h1>
			Select nearby store to pick up from (pincode ${defaultPincode}) <span>[?]</span>
		</h1>
		<div class="cnc_search_wrapper">
		<input class="cncStoreSearch" type="text" id="cncStoreSearch${entryNumber}" name="cncStoreSearch" placeholder="Search nearby store">
		<button onclick="ACC.singlePageCheckout.searchCNCStores('cncStoreSearch${entryNumber}','${entryNumber}');" type="button"></button>
		</div>
		</div>
		<%-- <div class="productCount">(for ${cnccount} out of ${delModeCount + cnccount}
			items in your bag)</div> --%>

		<!-- Parent UL under which every thing happens -->
		<ul id="deliveryradioul"
			class="checkout-table product-block classRadio">

			<!-- Loop runs for every product ietm in cart page -->
			<c:forEach items="${pwpos}" var="poses" varStatus="status1">
				<li class="item delivery_options item${entryNumber} cnc_item">
					<ul>
						<%-- <li class="latlng-wrapper">
							<div class="latlng latlng${entryNumber}">
								<c:forEach items="${poses.pointOfServices}" var="pos"
									varStatus="status">
									<c:if test="${(status.index != 0)}">@</c:if> '${pos.displayName}', ${pos.geoPoint.latitude}, ${pos.geoPoint.longitude}
								</c:forEach>
							</div>
						</li> --%>
						<li class="delivery">
							<div class="error_txt pincodeServicable${entryNumber}" style="width: 200px; font-size: 12px;">
							</div>
							<ul class="delivered scrollThis delivered${entryNumber} owl-carousel cnc_carousel" id="cnc_carousel">
								<c:forEach items="${poses.pointOfServices}" var="pos" varStatus="status">
									<li class="removeColor${entryNumber}">
										<div class="cncStoreAddressSection"><!-- START:Address Section -->
											<input class="radio_btn radio_btn${entryNumber}"
											type="radio" name="address${entryNumber}"
											id="address${entryNumber}${status.index}"
											value="address${status.index}">
											<div class='pin bounce'>
												<span class="text_in">${status.count}</span>
											</div> <label
											class="radio_sel${entryNumber}${status.index} displayName${entryNumber}${status.index} radio_color delivery-address searchable">
											${pos.displayName}</label> <label
											class="radio_sel${entryNumber}${status.index} name${entryNumber}${status.index} radio_color delivery-address searchable"
											style="display: none;">${pos.name}</label> <%-- <span class="radio_sel${entryNumber}${status.index} radio_color displayName${entryNumber}${status.index}">${pos.displayName}</span> --%>
											<span
											class="radio_sel${entryNumber}${status.index} radio_color address1${entryNumber}${status.index} searchable">
												<c:if test="${not empty pos.address.line1}">
																			${fn:escapeXml(pos.address.line1)}
																		</c:if>
											</span> 
											<span
											class="radio_sel${entryNumber}${status.index} radio_color address2${entryNumber}${status.index} searchable">
												<c:if test="${not empty pos.address.line2}">
																			${fn:escapeXml(pos.address.line2)}
																		</c:if>
											</span>
											<span
											class="radio_sel${entryNumber}${status.index} radio_color address3${entryNumber}${status.index} searchable">
												<c:if test="${not empty pos.address.state}">
																			${pos.address.state}
																		</c:if>
											</span>
											<span
											class="radio_sel${entryNumber}${status.index} radio_color address4${entryNumber}${status.index} searchable">
												<c:if test="${not empty pos.address.postalCode}">
																			${pos.address.postalCode}
																		</c:if>
											</span>
										</div><!-- END:Address Section -->
										<span class="cnc_border"></span>
										<span
										class="radio_sel${entryNumber}${status.index} radio_color"
										style="text-transform: none; display: inline-block;">Pick
											up hrs :</span> <c:if
											test="${not empty pos.mplOpeningTime && not empty pos.mplClosingTime}">
											<span
												class="pickup${entryNumber}${status.index} radio_sel${entryNumber}${status.index} radio_color"
												style="display: inline-block; margin-left: 0px;">${pos.mplOpeningTime}
												- ${pos.mplClosingTime}</span>
										</c:if> <span
										class="collectionDays${entryNumber}${status.index} collectionDays"><c:if
												test="${not empty pos.mplWorkingDays}">${pos.mplWorkingDays}</c:if></span>
										<span
										class="weeklyOff${entryNumber}${status.index} radio_sel${entryNumber}${status.index} radio_color"
										style="text-transform: capitalize;"></span>


									
									<script>
											$(document).ready(function() {
												var time = "";
												time = $(".pickup${entryNumber}${status.index}").text().split("-");
												$(".pickup${entryNumber}${status.index}").text("");
												for(i=0;i<time.length;i++) {
													var spaceRemove = "";
													spaceRemove = time[i].replace(" ","");
													spaceRemove = spaceRemove.split(":");
													$(".pickup${entryNumber}${status.index} ").append(spaceRemove[0]+":"+spaceRemove[1]);
													if(i!=time.length-1) {
														$(".pickup${entryNumber}${status.index}").append(" - ");
													}
												}
												
												var	collectionDays${entryNumber}${status.index} = $(".collectionDays${entryNumber}${status.index}").text().split(",");
												//var	collectionDays${entryNumber}${status.index} = ["0","1","2","3","4","5","6"];
												var weekDays = ["0","1","2","3","4","5","6"];
												var collectionWeek = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
												    missing${entryNumber}${status.index} = new Array();
												var count = 0;
												var i = 0,
												    lenC = weekDays.length;

												for ( ; i < lenC; i++ ) {
												    if ( collectionDays${entryNumber}${status.index}.indexOf(weekDays[i]) == -1 ) {
													 	missing${entryNumber}${status.index}[count] = weekDays[i]; count++; 
													}
												}
												//console.log(missing${entryNumber}${status.index});
												if(missing${entryNumber}${status.index}.length < 1) {
													$(".weeklyOff${entryNumber}${status.index}").text("Weekly Off : All Days Open");
												}
												else {
													$(".weeklyOff${entryNumber}${status.index}").text("Weekly Off : ");
													for(var y = 0; y < missing${entryNumber}${status.index}.length; y++) {
														$(".weeklyOff${entryNumber}${status.index}").append(collectionWeek[missing${entryNumber}${status.index}[y]]);
														if(y != missing${entryNumber}${status.index}.length-1) {
															$(".weeklyOff${entryNumber}${status.index}").append(", ");
														}
													}
												}
												$(".select_store").hide();
												var checked${entryNumber} = $("input[name='address${entryNumber}']:checked").val();
												/* $(".continue_btn").click(function(e){
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
													var checked${entryNumber} = $("input[name='address${entryNumber}']:checked").val();
													if(checked${entryNumber}=="address${entryNumber}" || checked${entryNumber}=="address${status.index}" || checked${entryNumber}=="address1" || checked${entryNumber}=="address0" || checked${entryNumber}=="address2" || checked${entryNumber}=="address3" || checked${entryNumber}=="address4" || checked${entryNumber}=="address5" || checked${entryNumber}=="address6" || checked${entryNumber}=="address7") {
														if($('#pickupPersonName').val().length <= "3" || $('#pickupPersonMobile').val().length <= "9") {
															$(".pickupDetails").show();
															e.preventDefault();
														}
													} else {
														$(".select_store").show();
														e.preventDefault();
													}
													
												}); */
												/* $("#del_continue_btn").click(function(e){												
													var checked${entryNumber} = $("input[name='address${entryNumber}']:checked").val();
													if(checked${entryNumber}=="address${entryNumber}" || checked${entryNumber}=="address${status.index}" || checked${entryNumber}=="address1" || checked${entryNumber}=="address0" || checked${entryNumber}=="address2" || checked${entryNumber}=="address3" || checked${entryNumber}=="address4" || checked${entryNumber}=="address5" || checked${entryNumber}=="address6" || checked${entryNumber}=="address7") {
														
													} else {
														$(".select_store").show();
														alert("in here");
														e.preventDefault();
													}
													
												}); */
												$("#address${entryNumber}${status.index}").click(function(){
													$(".removeColor${entryNumber} .radio_color").removeClass("colorChange");
													$(".select_store").hide();
													var name${status.index} = $(".name${entryNumber}${status.index}").text();
													openPopForAdddPosToCartEntry('${poses.ussId}',name${status.index});
													$(".radio_sel${entryNumber}${status.index}").addClass("colorChange");
												});
											});
										</script>
										</li>
								</c:forEach>
							</ul>
							<p class="page_count_cnc"></p>
						</li>
					</ul>
				</li>

			</c:forEach>
		</ul>
		<!-- End of main UL where every thing happens -->
		<!-- Start:Below span is used to display error if no stores are selected -->
		<span class="${entryNumber}_select_store select_store error_txt"
			style="text-align: left; font-size: 15px;"> <spring:theme
				code="checkout.multi.cnc.select.products.validate.msg" />
		</span>
		<!-- End:Above span is used to display error if no stores are selected -->

	</div>


	<div>
		<input type="hidden" name="CSRFToken" value="${CSRFToken}">
	</div>
</div>
