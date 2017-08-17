<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/product"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>

<!-- Displaying details of promotional product -->
<div class="promo-block" id="promotionDetailsId" style="display: none;">
	<div id="OfferWrap" style="display: block;">
		<div class="Inner">
<!-- 		<h2>Offers</h2> -->
			<ycommerce:testId code="productDetails_promotion_label">
			
					<c:choose>
					<c:when test="${not empty product.potentialPromotions}"> 
								<input type="hidden" value='${product.potentialPromotions}' id="promolist"/>
			<%-- 	<c:if test="${not empty product.potentialPromotions}"> --%>
					<%-- <c:choose>
				<c:when test="${not empty product.potentialPromotions[0].couldFireMessages}">
					<p>${product.potentialPromotions[0].couldFireMessages[0]}</p>
				</c:when>
				<c:otherwise> --%>
					<ul>
						<c:choose>
							<c:when
								test="${not empty product.potentialPromotions[0].channels}">
								<c:forEach var="channel"
									items="${product.potentialPromotions[0].channels}">
									<c:if test="${channel eq 'Web'||channel eq ''||channel==null}">
									 <li>
									 <div class="offer-modal-heading">OFFER</div>
									 <div class="offer-outer-wrapper">
									 <h3 class="product-name highlight mob-promo primary_promo_title">
												${product.potentialPromotions[0].title}
											</h3>
										<div class="Left primary_promo_img"><c:forEach
												items="${product.potentialPromotions[0].giftProduct}"
												var="promotionProduct">
												<a href="${request.contextPath}${promotionProduct.url}">
													<product:productPrimaryImage product="${promotionProduct}"
														format="luxuryThumbnail" />
												</a>
											</c:forEach>
										</div>
										<div class="pdp-promoDesc right primary_promo_desc">
											<h3 class="product-name highlight desk-promo">
												${product.potentialPromotions[0].title}
											</h3>
											<h3 class="promo-price"></h3>
											<%-- <p>${product.potentialPromotions[0].description}</p> --%>
											<input type="hidden" id="promotedSellerId"
												value="${product.potentialPromotions[0].allowedSellers}" />

											<c:forEach
												items="${product.potentialPromotions[0].giftProduct}"
												var="promotionProduct">
												<%-- <a href="${request.contextPath}${promotionProduct.url}">View Details</a>  --%>
											</c:forEach>

											<!-- <br />
											<p></p> -->
											<!-- <a class="showDate">View Details</a> <br> <br>-->
											<div class="show-date">
												<p>${product.potentialPromotions[0].description}</p>
												<div class="offer-date">
												<div class="from-date">
												<span class="from">From:</span>
												<span class="date-time"><fmt:formatDate pattern="dd/MM/yyyy"
													value="${product.potentialPromotions[0].startDate}" /></span>
												<span class="date-time"><fmt:formatDate pattern="h:mm:ss a"
													value="${product.potentialPromotions[0].startDate}" /></span>
													</div>
												<div class="to-date">
												<span class="to">To:</span>
												<span class="date-time"><fmt:formatDate pattern="dd/MM/yyyy"
													value="${product.potentialPromotions[0].endDate}" /></span>
												<span class="date-time"><fmt:formatDate pattern="h:mm:ss a"
													value="${product.potentialPromotions[0].endDate}" /></span>
												</div>
												</div>
											</div>
										</div>
										</div>
									 </li>
									</c:if>
									<!-- end if check for channel web -->
								</c:forEach>
							</c:when>
							<c:otherwise>
								<c:if test="${not empty product.potentialPromotions[0]}">
								 <li>
								 <div class="offer-modal-heading">OFFER</div>
								 <div class="offer-outer-wrapper">
								 <h3 class="product-name highlight mob-promo primary_promo_title">
											${product.potentialPromotions[0].title}
										</h3>
									<div class="Left primary_promo_img"> <c:forEach
											items="${product.potentialPromotions[0].giftProduct}"
											var="promotionProduct">
											<a href="${request.contextPath}${promotionProduct.url}">
												<product:productPrimaryImage product="${promotionProduct}"
													format="luxuryThumbnail" />
											</a>
										</c:forEach>
									</div>
									<div class="pdp-promoDesc right primary_promo_desc">
										<h3 class="product-name highlight desk-promo">
											${product.potentialPromotions[0].title}
										</h3>
										<h3 class="promo-price"></h3>
										<%-- <p>${product.potentialPromotions[0].description}</p> --%>
										<input type="hidden" id="promotedSellerId"
											value="${product.potentialPromotions[0].allowedSellers}" />

										<c:forEach
											items="${product.potentialPromotions[0].giftProduct}"
											var="promotionProduct">
											<%-- <a href="${request.contextPath}${promotionProduct.url}">View Details</a>  --%>
										</c:forEach>

										<!-- <br />
										<p></p> -->
										<!-- <a class="showDate">View Details</a><br> 
										<br>-->
										<div class="show-date">
											<p>${product.potentialPromotions[0].description}</p>
											<div class="offer-date">
											<div class="from-date">
											<span class="from">From:</span>
											<span class="date-time"><fmt:formatDate pattern="dd/MM/yyyy"
												value="${product.potentialPromotions[0].startDate}" /></span>
												<span class="date-time"><fmt:formatDate pattern="h:mm:ss a"
												value="${product.potentialPromotions[0].startDate}" /></span>
												</div>
												<div class="to-date">
											<span class="to">To:</span>
											<span class="date-time"><fmt:formatDate pattern="dd/MM/yyyy"
												value="${product.potentialPromotions[0].endDate}" /></span>
											<span class="date-time"><fmt:formatDate pattern="h:mm:ss a"
												value="${product.potentialPromotions[0].endDate}" /></span>
												</div>
										</div>
										</div>
									</div>
									</div>
									</li>
								</c:if>
							</c:otherwise>
						</c:choose>
					</ul>
					<%-- </c:otherwise>
			</c:choose> --%>
			<%-- 	</c:if> --%>
			<!-- //Added for displaying Non HMC configurable offer messages , TPR-589 -->
			</c:when>
			<c:otherwise>		
	  			<input type="hidden" value="" id="promolist"/>		
	  			<ul>
					<li>
						 <div class="offer-modal-heading">OFFER</div>
						 <div class="offer-outer-wrapper">
						 <div class="pdp-promoDesc pdp-promo right offer-block" style="float: none; margin: 0px auto;"></div>
						</div>
					</li>
			</ul>
            	</c:otherwise>
            	</c:choose>
			</ycommerce:testId>
		</div>
	</div>
</div>