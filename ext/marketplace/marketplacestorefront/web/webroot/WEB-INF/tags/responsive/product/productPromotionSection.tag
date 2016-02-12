<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>

<!-- Displaying details of promotional product -->
	<div class="promo-block" id="promotionDetailsId" style="display:none">
	<ycommerce:testId code="productDetails_promotion_label">
		<c:if test="${not empty product.potentialPromotions}">
			<%-- <c:choose>
				<c:when test="${not empty product.potentialPromotions[0].couldFireMessages}">
					<p>${product.potentialPromotions[0].couldFireMessages[0]}</p>
				</c:when>
				<c:otherwise> --%>
				
				<c:choose>
				<c:when test="${not empty product.potentialPromotions[0].channels}">
				<c:forEach var="channel"
							items="${product.potentialPromotions[0].channels}">
				<c:if test="${channel eq 'Web'||channel eq ''||channel==null}">			
								<span class="promo-img">
									<c:forEach
										items="${product.potentialPromotions[0].giftProduct}"
										var="promotionProduct">
										<a href="${request.contextPath}${promotionProduct.url}">
										<product:productPrimaryImage
												product="${promotionProduct}" format="thumbnail" />
												</a>
									</c:forEach>
									</span>
					<div class="pdp-promoDesc">
					<h3 class="product-name highlight"  >${product.potentialPromotions[0].title}</h3>
					<h3 class="promo-price"></h3>
					<%-- <p>${product.potentialPromotions[0].description}</p> --%>
					<input type="hidden" id="promotedSellerId" value="${product.potentialPromotions[0].allowedSellers}"/>
					
					<c:forEach items="${product.potentialPromotions[0].giftProduct}"
					var="promotionProduct">
					<%-- <a href="${request.contextPath}${promotionProduct.url}">View Details</a>  --%>					
					</c:forEach>
					
					<br/>
					<p></p>
					<a class="showDate">View Details</a><br><br>
					<div class="show-date" style="display:none">
					<p>${product.potentialPromotions[0].description}</p>
				
					From: <fmt:formatDate pattern="dd/MM/yyyy h:mm:ss a" 
                    value="${product.potentialPromotions[0].startDate}" />  To: <fmt:formatDate pattern="dd/MM/yyyy h:mm:ss a" value="${product.potentialPromotions[0].endDate}"/></div>
					</div>
					</c:if> <!-- end if check for channel web -->
					</c:forEach>
			    	</c:when>
				   <c:otherwise>
				   <c:if test="${not empty product.potentialPromotions[0]}">
								<span class="promo-img">
									<c:forEach
										items="${product.potentialPromotions[0].giftProduct}"
										var="promotionProduct">
										<a href="${request.contextPath}${promotionProduct.url}">
										<product:productPrimaryImage
												product="${promotionProduct}" format="thumbnail" />
												</a>
									</c:forEach>
									</span>
					<div class="pdp-promoDesc">
					<h3 class="product-name highlight"  >${product.potentialPromotions[0].title}</h3>
					<h3 class="promo-price"></h3>
					<%-- <p>${product.potentialPromotions[0].description}</p> --%>
					<input type="hidden" id="promotedSellerId" value="${product.potentialPromotions[0].allowedSellers}"/>
					
					<c:forEach items="${product.potentialPromotions[0].giftProduct}"
					var="promotionProduct">
					<%-- <a href="${request.contextPath}${promotionProduct.url}">View Details</a>  --%>					
					</c:forEach>
					
					<br/>
					<p></p>
					<a class="showDate">View Details</a><br><br>
					<div class="show-date" style="display:none">
					<p>${product.potentialPromotions[0].description}</p>
				
					From: <fmt:formatDate pattern="dd/MM/yyyy h:mm:ss a" 
                    value="${product.potentialPromotions[0].startDate}" />  To: <fmt:formatDate pattern="dd/MM/yyyy h:mm:ss a" value="${product.potentialPromotions[0].endDate}"/></div>
					</div>
					</c:if>
				</c:otherwise>
				</c:choose>
				<%-- </c:otherwise>
			</c:choose> --%>
		</c:if>
	</ycommerce:testId>
	</div>
	