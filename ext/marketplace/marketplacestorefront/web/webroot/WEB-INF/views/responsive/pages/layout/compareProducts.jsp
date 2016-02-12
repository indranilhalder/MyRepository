<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
	sessionStorage.setItem("comparePageVisited", "True");
</script>
<div class="page-header">
	<h2>
		<spring:theme code="text.compare.now" />
	</h2>
</div>
<div class="wrapper compare">
	<table class="comparison-table products-compareTable">
		<tbody>
			<tr>
				<td>
					<ul class="jump">
						<li class="title"><spring:theme code="text.jump.to.section" /></li>

						<c:if test="${not empty ClassifyingCategories}">


							<c:forEach items="${ClassifyingCategories}" var="classification">

								<li><a href="#${classification.code}" rel="relativeanchor">${classification.name}</a></li>

							</c:forEach>

						</c:if>

					</ul>
				</td>
				<c:forEach items="${ProductDatas}" var="product">
					<td><c:url value="${product.url}" var="productUrl" /> <a
						href="${productUrl}/quickView" class="product-tile">
							<div class="image">
								<product:productPrimaryImage product="${product}"
									format="product" />
							</div>
							<div class="short-info">

								<!-- <ul class="star-review">
									<li class="full"></li>
									<li class="full"></li>
									<li class="full"></li>
									<li class="empty"></li>
									<li class="empty"></li>
								</ul> -->
								<p class="company">${product.brand.brandname}</p>
								<h3 class="product-name">${product.name}</h3>
								<div class="price">

									<c:choose>

										<c:when
											test="${product.productMRP.formattedValue != product.productMOP.formattedValue}">
											<p class="old">
												<format:fromPrice priceData="${product.productMRP}" />
											</p>
											<p class="sale">
												<format:fromPrice priceData="${product.productMOP}" />
											</p>
										</c:when>
										<c:otherwise>
											<format:fromPrice priceData="${product.productMRP}" />
										</c:otherwise>
									</c:choose>
								</div>
								<span class="shop-now"><spring:theme code="text.shop.now" /></span>
							</div>
					</a></td>
				</c:forEach>




			</tr>
		</tbody>
	</table>
	<c:if test="${not empty ClassifyingCategories}">

		<c:forEach items="${ClassifyingCategories}" var="classification">
			<table class="comparison-table stats">
				<thead id="${classification.code}">
					<tr>
						<td colspan="5">${classification.name}</td>
					</tr>
				</thead>

				<tbody>

					<c:forEach items="${classification.features}"
						var="classificationFeature" varStatus="inner">
						<tr>
							<td>${classificationFeature.name}</td>

							<c:forEach items="${ProductDatas}" var="product">
								<c:forEach items="${product.classifications}"
									var="classification">

									<c:forEach items="${classification.features}" var="feature">

										<c:if test="${feature.name eq classificationFeature.name}">
											<c:forEach items="${feature.featureValues}" var="value"
												varStatus="status">
												<c:choose>
													<c:when test="${not empty value.value}">
														<td>${value.value}</td>
													</c:when>
													<c:otherwise>
														<td>NA</td>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${feature.range}">
											${not status.last ? '-' : feature.featureUnit.symbol}
										</c:when>
													<c:otherwise>
											${feature.featureUnit.symbol}
											${not status.last ? '<br/>' : ''}
										</c:otherwise>
												</c:choose>
											</c:forEach>
										</c:if>

									</c:forEach>
								</c:forEach>
							</c:forEach>
						</tr>
					</c:forEach>

				</tbody>
			</table>
		</c:forEach>

	</c:if>
</div>
<script>
	/* $(document).ready(function(){
	 var divPosition = $('#${classification.name}').offset();
	 $('html, body').animate({scrollTop: divPosition.top}, "slow");
	 }); */
	$(document).ready(function() {
		$('a[rel="relativeanchor"]').click(function() {
			// 		alert("hell");
			if ($("header .bottom.active").is(':visible')) {
				$('html, body').animate({
					scrollTop : $($.attr(this, 'href')).offset().top - 210
				}, 500);
			} else {
				$('html, body').animate({
					scrollTop : $($.attr(this, 'href')).offset().top - 280
				}, 500);
			}

			return false;
		});
	});
</script>