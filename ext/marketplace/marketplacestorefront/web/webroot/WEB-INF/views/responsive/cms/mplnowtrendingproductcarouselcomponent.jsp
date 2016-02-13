<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>

<div class="carousel-component">
	<%-- <div class="headline">
		<center>${component.title}</center>
	</div>

	<div style="padding-left: 504px;">
		<select id="categories" name="categoryCode">
			<option value="MPH1">All Departments</option>
			<c:forEach items="${component.categories}" var="category">
				<option value="${category.code}">${category.name}</option>

			</c:forEach>
		</select>
	</div> --%>
	
	<div class="headline">
		<span>${component.title}</span>
		<span>
		<select id="categories" name="categoryCode">
			<option value="MPH1"><spring:theme code="text.all.departments"/></option>
			<c:forEach items="${component.categories}" var="category">
		        <option value="${category.code}">${category.name}</option>
		    </c:forEach>
		</select>
		</span>
	</div>
</div>



<c:choose>
	<c:when test="${not empty productData}">
		<div class="carousel-component">



			<div
				class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference"
				id="defaultNowTrending">

				<c:forEach items="${productData}" var="product">

					<c:url value="${product.url}" var="productUrl" />

					<div class="item slide">
						<a href="${productUrl}" class="product-tile">
							<div class="image">
								<product:productPrimaryImage product="${product}"
									format="product" />
							</div>
							<div class="short-info">
								<ul class="color-swatch">

									
									<c:forEach items="${product.variantOptions}"
										var="variantOption">
										<li><c:if test="${not empty variantOption.defaultUrl}">
												<c:url value="${variantOption.defaultUrl}" var="variantUrl" />
												<a href="${variantUrl}"> <c:forEach
														items="${variantOption.colour}" var="color">
														<%-- <c:out value="${color}" /> --%>
														<img src="${variantOption.image.url}" alt="" />
														<c:if test="${variantOption.defaultUrl eq product.url}">
															<c:set var="currentColor" value="${color}" />
															<!--  set current selected color -->
														</c:if>
													</c:forEach>
												</a>
											</c:if></li>
									</c:forEach>
								</ul>
						
								<p class="company">${product.brand.brandname}</p>
								<h3 class="product-name">${product.name}</h3>
								<div class="price">
									<format:fromPrice priceData="${product.productMRP}" />
								</div>
							</div>
						</a>
						<div id="quick">
							<a href="${productUrl}/quickView" class="js-reference-item"><spring:theme code="text.quickview"/></a>
						</div>
					</div>

				</c:forEach>
			</div>
		</div>
		<c:url value="/Categories/c/MPH1" var="categoryUrl" />
		<%-- <form id="trendingProductsForm" action="${categoryUrl}"
			style="padding-left: 451px;">
			<button type="submit" class="btn primary"
				style="text-transform: uppercase; background-color: #3399FF; color: white">View
				all trending products</button>
		</form> --%>
		
<form id="trendingProductsForm" action="${categoryUrl}">
			<button type="submit" class="button"><spring:theme code="text.view.all.trending"/></button>
		</form>

	</c:when>

	<c:otherwise>
		<component:emptyComponent />
	</c:otherwise>
</c:choose>