<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="carousel-component">
<div class="feature-collections">

<!-- changes for tpr-599 -->
<input type="hidden" id="slideByOffer" value="${component.slideBy}">
<input type="hidden" id="autoPlayOffer" value="${component.autoPlay}">
<input type="hidden" id="autoplayTimeoutOffer" value="${component.autoplayTimeout}">
<h2>${component.title}</h2>
</div>
	<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference offersCarousel" id="shopByLookCarousel">
		<c:forEach items="${component.collectionItems}" var="item">
			<c:url value="${item.url}" var="url" />
			<div class="item slide">
				<div class="product-tile">
					<div class="image">
						<a href="${url}" class="product-tile">
						<c:if test="${not empty item.media.url}">
							<div class="home-best-pick-carousel-img"> 
								<img src="${item.media.url}"></img>
							</div>
						</c:if>
						</a>
					</div>
					<div class="details short-info">
					<c:set var="itemContent" value="${item.content}"></c:set>
						<c:if test="${not empty itemContent}">
							${itemContent}  
						</c:if>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
	<!-- sprint8(tpr-1672(home page,clp,blp)) -->
	<a href="<c:if test='${not empty component.buttonLink}'>
							${component.buttonLink}  
						</c:if>" class="view-best-offers">
						<c:if test="${not empty component.buttonText}">
							${component.buttonText}  
						</c:if>
						</a>
	<!-- sprint8(tpr-1672(home page,clp,blp)) -->
						
</div> 

