<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- <div id="navigation" style="margin-top: -2px;"></div> -->


<c:if test="${component.uid eq 'ShopByBrandComponent' }">
	<div class="toggle shop_brand">
		<span><spring:theme code="navigation.department.shopBy" /></span> <span><span><spring:theme
					code="navigation.brand.shopByBrand" /></span>
	</div>
	<span id="mobile-menu-toggle" class="mainli"></span>
</c:if>


<ul>

	<c:forEach items="${brandComponentCollection}" var="component">
		<%-- <li class="lazy-brands"><cms:component component="${brand}" evaluateRestriction="true" /></li> thread dump changes --%>
		<li class="lazy-brands">
		<c:if test="${not empty component.layout}">
			<c:set var="cssClass" value="brandClass" />
			<c:if test="${component.layout eq 'AtoZ'}">
				<c:set var="cssClass" value="A-ZBrands" />
				<input type="hidden" id="componentUid" value="${component.uid}" />
			</c:if>
			<div class="toggle ${cssClass}">
				<c:url var="masterBrandUrl" value="${component.masterBrandURL}" />
				<a class="${cssClass}" href="${masterBrandUrl}"
					id="${component.uid}">${component.masterBrandName}</a>
			</div>
			<span id="mobile-menu-toggle" class="mainli"></span>

			<!-- Rendering the sub-brand images if the layout is FiveBrandImages  -->
			<c:if test="${component.layout eq 'FiveBrandImages'}">
				<ul class="images" id="${component.uid}"></ul>
			</c:if>

			<c:if test="${component.layout eq 'OneCloumnOneBrandImage'}">
				<ul id="${component.uid}" class="images">
				</ul>
			</c:if>
			<!-- Rendering the brands alphabetically if the layout is AtoZ -->
			<c:if test="${component.layout eq 'AtoZ'}">
				<ul class="a-z">
					<li class="short images" id="atozbrandsdiplay">
						<div class="view_brands">
							<a href="${masterBrandUrl}"><h4>
									<b> <spring:theme code="navigation.brand.viewAll" />
										${component.masterBrandName}

									</b>
								</h4></a>
						</div>
					</li>
				</ul>
			</c:if>
		</c:if>
		</li>
	</c:forEach>
</ul>


<!-- </div> -->




