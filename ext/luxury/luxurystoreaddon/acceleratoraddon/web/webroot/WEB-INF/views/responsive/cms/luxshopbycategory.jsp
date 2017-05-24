<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



	<h3 class="section-title">${component.title}</h3>
	
		<a href="#">
		<c:forEach items="${component.relatedImage}" var="Image" varStatus="status">
		<li>
			<ul class="list-unstyled  circle-pager">
				<img src="${Image.media.url}" alt="${Image.name}">
					<h5>${Image.name}</h5>
			</ul>
			</li>
		</c:forEach>
		
				</a>
