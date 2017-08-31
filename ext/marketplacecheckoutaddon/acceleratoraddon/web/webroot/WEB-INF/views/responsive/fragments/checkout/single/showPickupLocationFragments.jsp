<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>


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
									<li class="removeColor${entryNumber}" id="storeLiId${entryNumber}${status.index}" onclick="ACC.singlePageCheckout.selectStore('${pos.name}','${entryNumber}','${status.index}')" style="cursor:pointer;">
										<div class="cncStoreAddressSection"><!-- START:Address Section -->
											<input class="radio_btn radio_btn${entryNumber}"
											type="radio" name="address${entryNumber}"
											id="address${entryNumber}${status.index}"
											value="address${status.index}" data-storeName="${pos.name}"  onclick="ACC.singlePageCheckout.selectStore('${pos.name}','${entryNumber}','${status.index}')">
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
												/* $("#address${entryNumber}${status.index},#storeLiId${entryNumber}${status.index}").click(function(){
													$(".removeColor${entryNumber} .radio_color").removeClass("colorChange");
													$(".select_store").hide();
													$("#address${entryNumber}${status.index}").prop("checked",true);
													var name${status.index} = $(".name${entryNumber}${status.index}").text();
													var storeName =  $(".displayName${entryNumber}${status.index}").text().trim();
													openPopForAdddPosToCartEntry('${poses.ussId}',name${status.index});
													$(".radio_sel${entryNumber}${status.index}").addClass("colorChange");
													if(typeof(utag)!='undefined')
													{
														utag.link({
															link_text  : storeName+'_store_seleted', 
															event_type : storeName+'_store_seleted'
														});
													}
												}); */
											});
										</script>
										</li>
								</c:forEach>
							</ul>
							<p class="page_count_cnc"></p>
						</li>
					</ul>
				</li>
				<span id="productcode${entryNumber}" style="display:none;">${poses.product.code}</span>
				<span id="sellerId${entryNumber}" style="display:none;">${poses.ussId}</span>
			</c:forEach>
		</ul>