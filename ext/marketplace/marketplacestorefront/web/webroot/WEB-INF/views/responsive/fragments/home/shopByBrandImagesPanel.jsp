<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

	<c:if test="${layout eq 'FiveBrandImages'}">
			<li class="short images">
				<div class="toggle">
				<c:url var="subBrandUrl" value="${subBrandUrl}"></c:url>
			</div> 
			<div class="view_brands">
			</div> <c:forEach items="${subBrandList}"
					var="subBrand">
					<!-- TISPRD-1381 Brand Issue Fix -->
					<c:if test="${not empty subBrand.subBrandUrl}">
						<c:url var="subBrandUrl" value="${subBrand.subBrandUrl}"></c:url>
					</c:if>
					<c:if test="${empty subBrand.subBrandUrl}">
						<c:url var="subBrandUrl" value="#"></c:url>
					</c:if> 
					<c:url var="subBrandUrl" value="${subBrand.subBrandUrl}"></c:url>
					<a href="${subBrandUrl}">
						<div class="multibrand-wrapper">
							<img class="multibrand-logo lazy" src="${subBrand.subBrandImage.URL}" />
							<img class="logo lazy" src="${subBrand.subBrandLogo.URL}">
							<p class="multibrand-name">${subBrand.subBrandName}</p>
						</div>
					</a>
				</c:forEach>
			</li>
	</c:if>
	<c:if test="${layout eq 'OneCloumnOneBrandImage'}">	
				<li class="short images">
					<div class="view_brands">
					</div>

				<ul class="words" style="width: 50%; float: left">
					<c:forEach items="${subBrands}" var="subBrand">
						<c:url var="subBrandUrl"
							value="/Categories/${subBrand.name}/c-${subBrand.code}"></c:url>
						<li class="long words"><div class="toggle"
								style="font-weight: normal; text-transform: capitalize">
								<a href="${subBrandUrl}">${subBrand.name}</a>
							</div></li>
					</c:forEach>
				</ul> <c:set var="count" value="0" /> <c:forEach
					items="${subBrandList}" var="subBrand">
					<div class="" style="width: 50%; float: right">
						<div>

							<c:if test="${(not empty subBrand.subBrandImage) && (count<1)}">
								<img class="lazy" width="200" height="300"
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
</c:if>