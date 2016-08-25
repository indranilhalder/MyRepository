<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="view" required="true" type="java.lang.String" %>
<%@ attribute name="image" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- TPR-628 -->

<c:choose>
<c:when test="${view eq 'mobileView'}">
       <div class="image_mobile">
              <img src="${image}">            
        </div>
</c:when>
<c:otherwise>

    <div class="image">
          <img src="${image}">
        </div>
</c:otherwise>
</c:choose>

