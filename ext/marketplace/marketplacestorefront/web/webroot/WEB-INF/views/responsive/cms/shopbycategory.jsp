<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<section class="shop-by-catagory text-center">
	<h3 class="section-title text-center">${component.title}</h3>
	<ul class="list-unstyled shop-by-catagory-slider circle-pager">
		<c:forEach items="${component.relatedImage}" var="Image" varStatus="status">
		<li>
			<a href="${Image.urlLink}">
				<img src="${Image.media.url}" alt="${Image.headline}">
				<h5>${Image.headline}</h5>
			</a>
		</li>
		</c:forEach>
	</ul>
	<a href="${component.shopNowLink}" class="link">${component.shopNowName}</a>
</section>