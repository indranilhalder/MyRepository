<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="look-book shortbanner-layout2">
    <div class="look-book-img">
        <div class="look-book-list clearfix">
          
                	<c:forEach items="${firstCol}" var="medias">
                	  <div class="layout-width-25 lookbook-pdr-10">
            	<ul class="list-unstyled clearfix">
	                	<li>
		                    <c:forEach items="${medias.medias}" var="media">
		                        <c:choose>
		                            <c:when test="${media.mediaFormat.qualifier eq 'desktop'}">
		                                <a class="shortbanner-desktop" href="${media.urlLink}">
		                                    <img src="${media.url}">
		                                </a>
		                            </c:when>
		                            <c:when test="${media.mediaFormat.qualifier eq 'mobile'}">
		                                <a class="shortbanner-mobile" href="${media.urlLink}">
		                                    <img src="${media.url}">
		                                </a>
		                            </c:when>
		                        </c:choose>
		                    </c:forEach>
	                    </li>
	                          </ul>
            </div>
                    </c:forEach>
          
           
                	<c:forEach items="${secondCol}" var="medias">
                	 <div class="layout-width-25 lookbook-pdl-10">
             	<ul class="list-unstyled clearfix">
	               		<li>
		                    <c:forEach items="${medias.medias}" var="media">
			                    <c:choose>
				                    <c:when test="${media.mediaFormat.qualifier eq 'desktop'}">
				                        <a class="shortbanner-desktop" href="${media.urlLink}">
				                            <img src="${media.url}">
				                        </a>
				                    </c:when>
				                     <c:when test="${media.mediaFormat.qualifier eq 'mobile'}">
				                        <a class="shortbanner-mobile" href="${media.urlLink}">
				                            <img src="${media.url}">
				                        </a>
				                    </c:when>
			                    </c:choose>
		                    </c:forEach>
	                    </li>
	                    </ul>
            </div>
                    </c:forEach>
            	
        </div>
    </div>
</div>
