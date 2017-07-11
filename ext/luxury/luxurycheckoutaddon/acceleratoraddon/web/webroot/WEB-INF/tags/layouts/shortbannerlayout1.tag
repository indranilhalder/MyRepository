<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="look-book">
    <div class="look-book-img">
        <div class="look-book-list clearfix">
            <div class="colmn">
                <c:forEach items="${firstCol}" var="medias">
                <ul class="list-unstyled clearfix">
                    <c:forEach items="${medias.medias}" var="media">
                        <c:choose>
                            <c:when test="${media.mediaFormat.qualifier eq 'desktop'}">
                                <a href="${media.urlLink}">
                                    <img src="${media.url}">
                                </a>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                    </c:forEach>
                </ul>
            </div>
            <div class="colmn">
                <c:forEach items="${secondCol}" var="medias">
                <ul class="list-unstyled clearfix">
                    <c:forEach items="${medias.medias}" var="media">
                    <c:choose>
                    <c:when test="${media.mediaFormat.qualifier eq 'desktop'}">
                        <a href="${media.urlLink}">
                            <img src="${media.url}">
                        </a>
                    </c:when>
                    </c:choose>
                    </c:forEach>
                    </c:forEach>
            </div>
        </div>
    </div>
</div>


<div class="look-book hide">
    <div class="look-book-img">
        <div class="look-book-list clearfix">
            <div class="colmn">
                <c:forEach items="${firstCol}" var="medias">
                <ul class="list-unstyled clearfix">
                    <c:forEach items="${medias.medias}" var="media">
                        <c:choose>
                            <c:when test="${media.mediaFormat.qualifier eq 'mobile'}">
                                <a href="${media.urlLink}">
                                    <img src="${media.url}">
                                </a>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                    </c:forEach>
                </ul>
            </div>
            <div class="colmn">
                <c:forEach items="${secondCol}" var="medias">
                <ul class="list-unstyled clearfix">
                    <c:forEach items="${medias.medias}" var="media">
                    <c:choose>
                    <c:when test="${media.mediaFormat.qualifier eq 'mobile'}">
                        <a href="${media.urlLink}">
                            <img src="${media.url}">
                        </a>
                    </c:when>
                    </c:choose>
                    </c:forEach>
                    </c:forEach>
            </div>
        </div>
    </div>
</div>
