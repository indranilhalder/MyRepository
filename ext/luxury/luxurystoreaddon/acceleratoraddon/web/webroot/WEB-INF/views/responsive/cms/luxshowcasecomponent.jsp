<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>




<c:forEach items="${ShowCase}" var="ShowCase" varStatus="status">			
<section class="look-book look-book-wrapper"> 
	<h3 class="section-title mb-10">${ShowCase.title}</h3>
	<p class="mb-30">${ShowCase.description}</p>
	<div class="look-book-list clearfix">
		<div class="colmn ${ShowCase.bannerImagePosition}">
			<c:forEach items="${ShowCase.bannerImage}" var="Image" varStatus="status">
			<a href="${Image.urlLink}"><img src="${Image.media.url}" alt="${Image.name}"></a>
			</c:forEach>
		</div>
		<div class="colmn lookbook-mob-slider">
			<ul class="list-unstyled clearfix mob-slicker">
				<c:forEach items="${ShowCase.productImages}" var="Image" varStatus="status">
				<li><a href="${Image.urlLink}"><img src="${Image.media.url}" alt=""></a></li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<a href="${ShowCase.shopNowLink}" class="btn btn-default mt-30">${ShowCase.shopNowName}</a>
</section>
</c:forEach>