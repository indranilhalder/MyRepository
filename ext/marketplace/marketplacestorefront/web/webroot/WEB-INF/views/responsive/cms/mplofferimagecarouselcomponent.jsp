<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="carousel-component">
<div class="feature-collections">
<h1>${component.title}</h1>
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
						<c:if test="${not empty item.content}">
							${item.content}  
						</c:if>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div> 

