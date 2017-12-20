<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="order" required="true"
	type="de.hybris.platform.commercefacades.order.data.OrderData"%>
<%@ attribute name="parentOrder" required="true"
	type="de.hybris.platform.commercefacades.order.data.OrderData"%>
<%@ attribute name="orderGroup" required="true"
	type="de.hybris.platform.commercefacades.order.data.OrderEntryGroupData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- <div class="orderList"> -->
<%-- 	<div class="headline"><spring:theme code="basket.page.title.yourDeliveryItems" text="Your Delivery Items"/></div>
 --%>
<!-- <table class="orderListTable"> -->
<c:forEach items="${orderGroup.entries}" var="entry">
	<c:if test="${entry.mplDeliveryMode.code eq 'click-and-collect'}">
		<c:if test="${storeId ne entry.deliveryPointOfService.address.id}">
			<div class="headline" style="margin-left: 16px;">
				<span style="font-weight: bold;"><spring:theme
						code="basket.page.title.pickupFrom" text="Pick Up from\:" /></span>
				<c:set var="pos" value="${entry.deliveryPointOfService.address}" />
				<c:set var="storeId"
					value="${entry.deliveryPointOfService.address.id}" />
				<address>
					<c:if test="${not empty entry.deliveryPointOfService.displayName}">${entry.deliveryPointOfService.displayName}<br>
					</c:if>
					<c:if test="${not empty pos.line1}">${pos.line1}&nbsp;</c:if>
					<c:if test="${not empty pos.line2}">${pos.line2}</c:if>
					<c:if test="${not empty pos.town}">	${pos.town}, <br>
					</c:if>
					<c:if test="${not empty pos.state}">	${pos.state},</c:if>
					<c:if test="${not empty pos.country.name}">${pos.country.name},</c:if>
					<c:if test="${not empty pos.postalCode}">	${pos.postalCode} &nbsp;</c:if>
					<c:if test="${not empty pos.country.isocode}">${pos.country.isocode} <br>
					</c:if>
					<c:if test="${not empty pos.phone}">+91&nbsp; ${pos.phone} <br>
					</c:if>
				</address>
			</div>
		</c:if>
	</c:if>
	<br>


	<c:url value="${entry.product.url}" var="productUrl" />
	<li class="item">
		<ul class="desktop">
			<li>
				<div class="product-img">
					<c:choose>
										<c:when test="${fn:toLowerCase(entry.product.luxIndicator)=='luxury'}">
												<a href="${productUrl}">
							<product:productPrimaryImage product="${entry.product}" format="luxuryCartIcon"/>
						</a>
					
										</c:when>
										<c:otherwise>
												<a href="${productUrl}">
							<product:productPrimaryImage product="${entry.product}" format="thumbnail"/>
						</a>
												
										</c:otherwise>
									</c:choose>
				</div>
				<div class="product">

					<p class="company"></p>
					<ycommerce:testId code="orderDetails_productName_link">
						<h3 class="product-brand-name">
							<a href="${entry.product.purchasable ? productUrl : ''}">${entry.product.brand.brandname}</a>
						</h3>
						<h3 class="product-name">
							<a href="${entry.product.purchasable ? productUrl : ''}">${entry.product.name}</a>
						</h3>
						<input type="hidden" name="productArrayForIA"
							value="${entry.product.code}" />
					</ycommerce:testId>
					<c:forEach items="${entry.product.baseOptions}" var="option">
						<c:if
							test="${not empty option.selected and option.selected.url eq entry.product.url}">
							<c:forEach items="${option.selected.variantOptionQualifiers}"
								var="selectedOption">
								<dl>
									<dt>${selectedOption.name}:</dt>
									<dd>${selectedOption.value}</dd>
								</dl>
							</c:forEach>
						</c:if>
					</c:forEach>
					<c:if test="${not empty parentOrder.appliedProductPromotions}">
						<ul>
							<c:set var="displayed" value="false" />
							<c:forEach items="${order.appliedProductPromotions}"
								var="promotion">
								<c:forEach items="${promotion.consumedEntries}"
									var="consumedEntry">
									<c:if
										test="${not displayed && not entry.isBOGOapplied && entry.giveAway && ((consumedEntry.adjustedUnitPrice - entry.amountAfterAllDisc.doubleValue) == '0.0' ||(consumedEntry.adjustedUnitPrice - entry.amountAfterAllDisc.doubleValue) == '0.00')}">
										<c:set var="displayed" value="true" />
										<li><span>${promotion.description}</span></li>
									</c:if>
								</c:forEach>
							</c:forEach>
						</ul>
					</c:if>

					<%-- <p class="item-info">
						<span> <spring:theme code="order.qty" />
							<ycommerce:testId code="orderDetails_productQuantity_label">&nbsp;${entry.quantity}</ycommerce:testId>
							<c:if test="${not empty entry.product.size}">
								<ycommerce:testId code="cart_product_size">
									<div class="size">
										<spring:theme code="text.size" />${entry.product.size}
									</div>
								</ycommerce:testId>
							</c:if>
						</span>
					</p> --%>
					<p class="item-info">
						<span> <spring:theme code="order.qty" />
							<ycommerce:testId code="orderDetails_productQuantity_label"> ${entry.quantity}</ycommerce:testId>
							<c:if test="${not empty entry.product.size}">
								<c:choose>
									<c:when test="${(not empty entry.product.rootCategory) && (entry.product.rootCategory == 'FineJewellery' || entry.product.rootCategory == 'FashionJewellery') }">
										<spring:theme code="product.variant.size.noSize" var="noSize"/>
										<c:if test="${entry.product.size ne noSize }">
											<ycommerce:testId code="cart_product_size">
												<div class="size">
													<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.jewellery.category')" var="lengthVariant"/>
											     	<c:set var = "categoryListArray" value = "${fn:split(lengthVariant, ',')}" />
													<c:forEach items="${entry.product.categories}" var="categories">
											   			<c:forEach items = "${categoryListArray}" var="lengthVariantArray">
											   				<c:if test="${categories.code eq lengthVariantArray}">
											   				 	<c:set var="lengthSize" value="true"/>
											   				</c:if> 
											   			</c:forEach>
											   		</c:forEach>
											   		<c:choose>
											   			<c:when test="${true eq lengthSize}">
											   				<spring:theme code="product.variant.length.colon"/>${entry.product.size}
											   			</c:when>
											   			<c:otherwise>
											   				<spring:theme code="text.size" />${entry.product.size}
											   			</c:otherwise>
											   		</c:choose>
												</div>
											</ycommerce:testId>
										</c:if>
									</c:when>
									<c:otherwise>
										<ycommerce:testId code="cart_product_size">
										<c:if test="${!fn:containsIgnoreCase(entry.product.size, 'No Size')}">
											<div class="size">
											
												<spring:theme code="text.size" />${entry.product.size}
											</div>
										</c:if>
										</ycommerce:testId>
									</c:otherwise>
								</c:choose>
							</c:if>
						</span>
					</p>
					<ul class="item-details">
						<%-- <li><b><spring:theme code="seller.order.code"/>&nbsp;${order.code}</b></li> --%>
					</ul>
				</div>

			</li>
			<li class="shipping">
				<ul class="collect">
                     <%--  <li class="deliver-type">${entry.mplDeliveryMode.name}</li> --%>
                   <!-- UF-306 start -->	
                   <c:choose>
				   	<c:when test="${entry.mplDeliveryMode.code eq 'home-delivery'}">
				   		<li class="deliver-type"><spring:theme code="text.home.delivery"/></li>
				   	</c:when>
				   	<c:when test="${entry.mplDeliveryMode.code eq 'express-delivery'}">
				   	    <li class="deliver-type"><spring:theme code="text.express.shipping"/></li>
				   	</c:when>
				   	<c:otherwise>
				   		<li class="deliver-type"><spring:theme code="text.clickandcollect.delivery"/></li>
				   	</c:otherwise>
				   </c:choose>
				   <!-- UF-306 ends -->
					<li class="deliver" style="font-size: 10px;"><c:choose>
							<c:when test="${entry.currDelCharge.value=='0.0'}">
								<%-- <spring:theme code="order.free"  /> --%>
								<ycommerce:testId code="orderDetails_productTotalPrice_label">
									<format:price priceData="${entry.currDelCharge}"
										displayFreeForZero="true" />
								</ycommerce:testId>
							</c:when>
							<c:otherwise>
								<format:price priceData="${entry.currDelCharge}" />
							</c:otherwise>
						</c:choose>
						</li>	
                      <li class="deliver deliver-desc">${entry.mplDeliveryMode.description}</li>
                    </ul> 
                  </li>
					<%-- <td headers="header5">
						<ycommerce:testId code="orderDetails_productItemPrice_label"><format:price priceData="${entry.basePrice}" displayFreeForZero="true"/></ycommerce:testId>
					</td> --%>
			<li class="price"><c:choose>
					<c:when
						test="${entry.isBOGOapplied eq true || entry.giveAway eq true}">
						<%-- <del>
															 <format:price priceData=0.0
																displayFreeForZero="true" />
														</del> --%>
						<%-- <span> <format:price priceData=0.0 displayFreeForZero="true"/></span> --%>
						<span>Free</span>
					</c:when>
					<c:otherwise>
						<%-- <ycommerce:testId code="orderDetails_productTotalPrice_label"><format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/></ycommerce:testId> --%>
						<ycommerce:testId code="orderDetails_productTotalPrice_label">
							<!-- TISEE-5560 - change from netSellingPrice to amountAfterAllDisc -->
							<c:choose>
								<c:when test="${not empty entry.amountAfterAllDisc}">
									<format:price priceData="${entry.amountAfterAllDisc}"
										displayFreeForZero="true" />
								</c:when>
								<c:otherwise>
									<format:price priceData="${entry.totalPrice}"
										displayFreeForZero="true" />
								</c:otherwise>
							</c:choose>
						</ycommerce:testId>

					</c:otherwise>
				</c:choose></li>
		</ul>
		<ul class="mobile-product">
			<li>
				<div class="product-img">
					<a href="${productUrl}"> <product:productPrimaryImage
							product="${entry.product}" format="thumbnail" />
					</a>
				</div>
				<div class="product">

					<p class="company"></p>
					<ycommerce:testId code="orderDetails_productName_link">
						<h3 class="product-name">
							<a href="${entry.product.purchasable ? productUrl : ''}">${entry.product.name}</a>
						</h3>
					</ycommerce:testId>

					<c:forEach items="${entry.product.baseOptions}" var="option">
						<c:if
							test="${not empty option.selected and option.selected.url eq entry.product.url}">
							<c:forEach items="${option.selected.variantOptionQualifiers}"
								var="selectedOption">
								<dl>
									<dt>${selectedOption.name}:</dt>
									<dd>${selectedOption.value}</dd>
								</dl>
							</c:forEach>
						</c:if>
					</c:forEach>
					<c:if test="${not empty order.appliedProductPromotions}">
						<ul>
							<c:forEach items="${order.appliedProductPromotions}"
								var="promotion">
								<c:set var="displayed" value="false" />
								<c:forEach items="${promotion.consumedEntries}"
									var="consumedEntry">
									<c:if
										test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
										<c:set var="displayed" value="true" />
										<li><span>${promotion.description}</span></li>
									</c:if>
								</c:forEach>
							</c:forEach>
						</ul>
					</c:if>

					<p class="item-info">
						<span> <spring:theme code="order.qty" />
							<ycommerce:testId code="orderDetails_productQuantity_label">&nbsp;${entry.quantity}</ycommerce:testId>
						</span>
					</p>
					<ul class="item-details">


						<%--  <li><b><spring:theme code="seller.order.code"/>&nbsp;${order.code}</b></li> --%>

					</ul>
					<h3 class="price">
						<ycommerce:testId code="orderDetails_productTotalPrice_label">
							<format:price priceData="${entry.totalPrice}"
								displayFreeForZero="true" />
						</ycommerce:testId>
					</h3>
					<!-- <ul class="item-details"></ul> -->
					</div>
                  </li>
                  <li class="shipping">
                     <p><spring:theme code="text.deliveryMethod" text="Available Delivery Options:"/></p>
                    <ul class="${entry.mplDeliveryMode.name}">
                       <li class="deliver-type">${entry.mplDeliveryMode.name}</li>
                       <!-- TISSQAUAT-1537 starts -->
                       <li class="deliver">
                       <c:choose>
							<c:when test="${entry.currDelCharge.value=='0.0'}">
										<%-- <spring:theme code="order.free"  /> --%>
										<ycommerce:testId code="orderDetails_productTotalPrice_label"><format:price priceData="${entry.currDelCharge}" displayFreeForZero="true"/></ycommerce:testId>
							</c:when>
						 	<c:otherwise>
									<format:price priceData="${entry.currDelCharge}"/>
							</c:otherwise>
						</c:choose>
                       </li>
                       <!-- TISSQAUAT-1537 ends -->
                      <li class="deliver deliver-desc">${entry.mplDeliveryMode.description}</li>
                    </ul>
                  </li>
                </ul> 
				</li>
			</c:forEach>
		 
	<!-- </table> -->
<!-- </div> -->