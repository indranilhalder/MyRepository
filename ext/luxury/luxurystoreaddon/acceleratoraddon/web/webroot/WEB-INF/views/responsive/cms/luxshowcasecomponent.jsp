<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<h3 class="section-title">${title}</h3>

<c:forEach items="${ShowCase}" var="ShowCase" varStatus="status">			
<section class="look-book"> 
	<h3 class="section-title">${ShowCase.title}</h3>
	<p class="mb-40">${ShowCase.description}</p>

				<c:forEach items="${ShowCase.bannerImage}" var="Image" varStatus="status">
				<li><img src="${Image.media.url}" alt="${Image.name}"></li>
				</c:forEach>
		
				<c:forEach items="${ShowCase.productImages}" var="Image" varStatus="status">
				<img src="${Image.media.url}" alt="">
				</c:forEach>
				
	<a href="${ShowCase.shopNowLink}" class="btn btn-default mt-30">${ShowCase.shopNowName}</a>
</section>
</c:forEach>