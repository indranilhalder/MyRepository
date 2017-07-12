<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="look-book shortbanner-layout3">
    <div class="look-book-img">
        <div class="look-book-list clearfix">
            <div class="col-xs-6 col-sm-6 col-lg-6 lookbook-pd-0">
            	<ul class="list-unstyled clearfix">
                	<c:forEach items="${firstCol}" var="medias">
	                	<li class="layout-width-50">
		                    <c:forEach items="${medias.medias}" var="media">
		                        <c:choose>
		                            <c:when test="${media.mediaFormat.qualifier eq 'desktop'}">
		                                <a href="${media.urlLink}">
		                                    <img src="${media.url}">
		                                </a>
		                            </c:when>
		                            <c:when test="${media.mediaFormat.qualifier eq 'desktop'}">
		                                <a href="${media.urlLink}">
		                                    <img src="${media.url}">
		                                </a>
		                            </c:when>
		                        </c:choose>
		                    </c:forEach>
	                    </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="col-xs-6 col-sm-6 col-lg-6 lookbook-pd-0">
             	<ul class="list-unstyled clearfix">
                	<c:forEach items="${secondCol}" var="medias">
	               		<li class="layout-width-50">
		                    <c:forEach items="${medias.medias}" var="media">
			                    <c:choose>
				                    <c:when test="${media.mediaFormat.qualifier eq 'desktop'}">
				                        <a href="${media.urlLink}">
				                            <img src="${media.url}">
				                        </a>
				                    </c:when>
				                     <c:when test="${media.mediaFormat.qualifier eq 'desktop'}">
				                        <a href="${media.urlLink}">
				                            <img src="${media.url}">
				                        </a>
				                    </c:when>
			                    </c:choose>
		                    </c:forEach>
	                    </li>
                    </c:forEach>
            	</ul>
            </div>
        </div>
    </div>
</div>
