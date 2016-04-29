
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${not empty component.layout}">
	<c:set var="cssClass"
		value="${fn:replace(component.masterBrandName,' ', '') }" />
	<c:if test="${component.layout eq 'AtoZ'}">
		<c:set var="cssClass" value="A-ZBrands" />
	</c:if>
	<div class="toggle ${cssClass}">
		<c:url var="masterBrandUrl" value="${component.masterBrandURL}" />


		<a class="${cssClass}" href="${masterBrandUrl}">${component.masterBrandName}</a>
	</div>

	<!-- Rendering the sub-brand images if the layout is FiveBrandImages  -->

	<c:if test="${component.layout eq 'FiveBrandImages'}">
		<ul class="images">
			<li class="short images">
				<%-- <div class="toggle">
				<a href="${masterBrandUrl}">${component.masterBrandName}</a>
				<c:url var="subBrandUrl" value="${subBrand.subBrandUrl}"></c:url>
			</div> 
			<div>
				<a href="${masterBrandUrl}"><h4>
						<b> <spring:theme code="navigation.brand.viewAll" />${component.masterBrandName}

						</b>
					</h4></a>
			</div> --%> <c:forEach items="${component.subBrandList}"
					var="subBrand">
					<!-- TISPRD-1381 Brand Issue Fix -->
					<c:if test="${not empty subBrand.subBrandUrl}">
						<c:url var="subBrandUrl" value="${subBrand.subBrandUrl}"></c:url>
					</c:if>
					<c:if test="${empty subBrand.subBrandUrl}">
						<c:url var="subBrandUrl" value="#"></c:url>
					</c:if> 
					<%-- <c:url var="subBrandUrl" value="${subBrand.subBrandUrl}"></c:url> --%>
					<a href="${subBrandUrl}">
						<div class="multibrand-wrapper">
							<img class="multibrand-logo" src="${subBrand.subBrandImage.URL}" />
							<img class="logo" src="${subBrand.subBrandLogo.URL}">
							<p class="multibrand-name">${subBrand.subBrandName}</p>
						</div>
					</a>
				</c:forEach>
			</li>
		</ul>
	</c:if>
	<!-- Rendering the  sub brand  names in one column and the brand image if the layout is OneCloumnOneBrandImage-->
	<c:if test="${component.layout eq 'OneCloumnOneBrandImage'}">


		<ul id="kids" class="images">
			<li class="short images">
				<%-- <div class="toggle">
				<a href="${masterBrandUrl}">${component.masterBrandName} 
				</a>
				</div>
				
					<div>
						<a href="${masterBrandUrl}"><h4>
								<b> <spring:theme code="navigation.brand.viewAll" />${component.masterBrandName}

								</b>
							</h4></a>
					</div> --%>

				<ul class="words" style="width: 50%; float: left">
					<c:forEach items="${component.subBrands}" var="subBrand">
						<c:url var="subBrandUrl"
							value="/Categories/${subBrand.name}/c/${subBrand.code}"></c:url>
						<li class="long words"><div class="toggle"
								style="font-weight: normal; text-transform: capitalize">
								<a href="${subBrandUrl}">${subBrand.name}</a>
							</div></li>
					</c:forEach>
				</ul> <c:set var="count" value="0" /> <c:forEach
					items="${component.subBrandList}" var="subBrand">
					<div class="" style="width: 50%; float: right">
						<div>

							<c:if test="${(not empty subBrand.subBrandImage) && (count<1)}">
								<img width="200" height="300"
									src="${subBrand.subBrandImage.URL}" />
								<br>
								<c:url var="subBrandUrl" value="${subBrand.subBrandUrl}"></c:url>
								<a
									style="padding-left: 60px; font-size: 12px; letter-spacing: 1px; text-align: center; font-weight: bold; text-transform: uppercase;"
									href="${subBrandUrl}"><spring:theme code="text.shop" />
									${subBrand.subBrandName} </a>
								<c:set var="count" value="${count+1}" />
							</c:if>
						</div>


					</div>
				</c:forEach>
			</li>
		</ul>
	</c:if>
	<!-- Rendering the brands alphabetically if the layout is AtoZ -->
	<c:if test="${component.layout eq 'AtoZ'}">
		<ul class="images">



			<li class="short images" id="atozbrandsdiplay">
				<%-- <div class="toggle">
				<a href="">${component.masterBrandName}</a>
			</div> <c:url var="brandlistUrl" value="/brands/brandlist?cat=A-ZBrands" />
			<div>
				<a href="${brandlistUrl}"><h4>
						<b> <spring:theme code="navigation.brand.viewAllBrands" />

						</b>
					</h4></a>
			</div> --%>
			</li>
		</ul>
	</c:if>
</c:if>
