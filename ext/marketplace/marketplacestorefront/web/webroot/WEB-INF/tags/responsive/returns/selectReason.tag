<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- Accordtion Panel 1 For Select Reason For Return -->
				<p class="return-title">Return</p>
				<div class="accordtionTataCliq activeTataCliq  firstTataCliq col-md-12">
					<div class="accHeading"><spring:theme code="text.order.return.selectreason"></spring:theme></div>
					<div class="accContents selectReason col-md-12">
				<c:set var="eachSubOrderEntry" value="${subOrderEntry}"/>								
					
						<div class="col-md-5 col-sm-5 col-xs-12">
							<div class="row">
							<c:forEach items="${returnProductMap[eachSubOrderEntry.transactionId]}" var="entryReturn" >
								<div class="col-md-5 col-sm-5 col-xs-5 return-img">
									<div class="image_holder">
											<product:productPrimaryImage
													product="${entryReturn.product}" format="thumbnail" lazyLoad="false"/>
									</div>
								</div>
							
								
								<div class="col-md-7 productDetails col-sm-7 col-xs-7 return-prod-desc">
										<span class="productName">${entryReturn.brandName}</span>
										<span class="productName">${entryReturn.product.name}</span>
										<%-- <span><spring:theme code="text.order.returns.quantitylable"/> ${entryReturn.quantity}</span><br/> --%>
												<c:if test="${not empty entryReturn.product.size}">
				 									<%-- <span class="size"><spring:theme code="text.order.returns.sizelable"/>${entryReturn.product.size}</span> --%>
				 									<c:choose>
														<c:when test="${(not empty entryReturn.product.rootCategory) && (entryReturn.product.rootCategory == 'FineJewellery' || entryReturn.product.rootCategory == 'FashionJewellery') }">
															<spring:theme code="product.variant.size.noSize" var="noSize"/>
															<input type="hidden" id="productCategoryTypeReturn" value="${entryReturn.product.rootCategory}"/>
															<c:if test="${entryReturn.product.size ne noSize}">
																<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.jewellery.category')" var="lengthVariant"/>
																<c:set var = "categoryListArray" value = "${fn:split(lengthVariant, ',')}" />
																<c:forEach items="${entryReturn.product.categories}" var="categories">
																	<c:forEach items = "${categoryListArray}" var="lengthVariantArray">
																		<c:if test="${categories.code eq lengthVariantArray}">
																			<c:set var="lengthSize" value="true"/>
																		</c:if> 
																	</c:forEach>
																</c:forEach>	  
																<c:choose>
																	<c:when test="${true eq lengthSize}">
																	  <span class="size"><spring:theme code="product.variant.length"/>${entryReturn.product.size}</span>
																	</c:when>
																	<c:otherwise>
																	  <span class="size"><spring:theme code="text.order.returns.sizelable"/>${entryReturn.product.size}</span>
																	</c:otherwise>
																</c:choose>
															</c:if>
														</c:when>
														<c:otherwise>
															<span class="size"><spring:theme code="text.order.returns.sizelable"/>${entryReturn.product.size}</span>
														</c:otherwise>
													</c:choose>
												</c:if>
												<c:if test="${not empty entryReturn.product.colour}">
													<span class="productName"><spring:theme code="text.order.returns.colourlable"/> ${entryReturn.product.colour}</span>
												</c:if>
												<%-- <c:forEach items="${entryReturn.product.seller}" var="seller">
													<c:if test="${ussid eq seller.ussid}">
														<c:set var="sellerId" value="${seller.sellerID}" />
													</c:if>
												</c:forEach>
												<span>Seller ID: ${sellerId} </span><br/> --%>
												<div class="attributes">

															<p>
																<span><spring:theme code="text.orderHistory.price" />&nbsp;</span>
																<ycommerce:testId
																	code="orderDetails_productTotalPrice_label">
																	<format:price priceData="${entryReturn.amountAfterAllDisc}"
																		displayFreeForZero="true" />
																</ycommerce:testId>
															</p>
															<p>
																<%-- <spring:theme text="Delivery Charges:" /> --%>

																<span><spring:theme code="text.account.order.delivery2" text="Scheduled Delivery and Shipping Charges:"/>&nbsp;</span>
															<c:choose>
																<c:when test="${entryReturn.currDelCharge.value=='0.0'}">
																	<%-- <spring:theme code="order.free"  /> --%>
																	
															      <c:choose>
																	<c:when test="${not empty entryReturn.scheduledDeliveryCharge}">
																	    ${entryReturn.scheduledDeliveryCharge}
																	</c:when>
																	<c:otherwise>
																	<ycommerce:testId
																		code="orderDetails_productTotalPrice_label">
																		<format:price priceData="${entryReturn.currDelCharge}"
																			displayFreeForZero="true" />
																	</ycommerce:testId>
																    </c:otherwise>
																</c:choose>
																</c:when>
																<c:otherwise>
																	<format:price priceData="${entryReturn.currDelCharge}" />
																</c:otherwise>
															</c:choose>
															</p>
														</div>
												<span class="otherAttributes"><spring:message code="seller.order.code"> </spring:message>&nbsp;${subOrder.code} </span>
											<!-- 	TPR-6288 | My account Order Return -->
												<input type="hidden" id ="dtmPrdtReturnCat" value =" ${entryReturn.product.rootCategory}"/>
												<input type="hidden" id ="dtmPrdtReturnCode" value ="${entryReturn.product.code}"/>
								</div>
								<p style="clear:both"></p>
								</c:forEach>
							</div>
						</div>
						<div class="col-md-7 col-sm-7 col-xs-12 selectReasonForReturn">
							<b><spring:theme code="text.order.returns.reasonvalidation.message"></spring:theme></b> <br/><br/>
							
							<form:select name="reasonList" class="reasonSelectBox" path="returnReason" >
								    <option selected='selected' value="NA"><spring:theme code="text.requestDropdown.selected"/></option>
									<c:forEach items="${reasonDataList}" var="reason"
										varStatus="reasonStatus">
										<form:option value="${reason.code}">${reason.reasonDescription}</form:option>	
									</c:forEach>									
							</form:select>
						</div>
						
						<br/><br/>
						<br/><br/>
						<br/><br/>
						
						<!-- TPR-4134 -->
						<input type="hidden" id="ifShowReverseSeal" value="${showReverseSeal}">
						<c:if test="${showReverseSeal ne null && showReverseSeal eq 'true'}">
							<div class="col-md-7 col-sm-7 col-xs-12 reverseSealJwlry">
								<b><spring:theme code="text.order.returns.reverseSeal.message"></spring:theme> <a href="#nogo" class="revSeal" id="revSeal"><spring:theme code="text.order.returns.reverseSeal.popUp"></spring:theme></a></b> <br/><br/>
								<form:radiobutton class="radioButton" path="reverseSeal" value="Y"/><label><spring:theme code="text.order.returns.reverseSeal.radioYes"></spring:theme></label>
								<form:radiobutton class="radioButton" path="reverseSeal" value="N"/><label><spring:theme code="text.order.returns.reverseSeal.radioNo"></spring:theme></label>
								<div id="revSealHelpContent" style="display : none;">
									<%-- <span class="revSealText"><input id="revSealHelpText" value="<spring:theme code="text.order.returns.reverseSeal.helpText"/>"></span> --%>
									<span class="revSealText"><p id="revSealHelpText"><spring:theme code="text.order.returns.reverseSeal.helpText"/></p></span>
									<span class="revSealImage"><img src="${commonResourcePath}/images/Jewellery_ReverseSeal.jpg" alt=""></span>
								</div>
							</div>
					   </c:if>
						
						<p style="clear:both"></p>
						<div class="button_holder">
								<button onclick="checkReturnSelected()" type="button" class="light-blue submit-request" ><spring:theme code="text.returRequest.continueButton" text="Continue"/></button>
						</div>
					</div>
				</div>
				<!-- <style>
				.upload-image-title {
				    cursor: pointer;
					margin: 10px 18px;
					color: #a9143c;
				}
				#upload-photo {
				   opacity: 0;
				   position: absolute;
				   z-index: -1;
				}
				.add-comment-title{
					color: #a9143c;
				    font-size: 14px;
				    margin: 10px 0;
				}
				</style> -->
