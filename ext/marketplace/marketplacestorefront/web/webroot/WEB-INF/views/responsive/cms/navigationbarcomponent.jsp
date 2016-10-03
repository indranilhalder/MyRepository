<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<c:set value="${component.styleClass} ${dropDownLayout}" var="bannerClasses"/>

<li class="${bannerClasses} <c:if test="${not empty component.navigationNode.children}"> has-sub js-enquire-has-sub</c:if>">
<%-- **${component.link.url}
+++${component.navigationNode.links[0].linkName} --%>

<div class="toggle departmenthover" id="depts${component.link.category.code}">
<a href="${component.link.url}">${component.navigationNode.title}</a>
</div>  <!-- TPR-561 -->
	<%-- <cms:component component="${component.link}" evaluateRestriction="true"/> --%>
	<c:choose>
	<c:when test="${not empty component.navigationNode.children}">
			<span id="mobile-menu-toggle" class=""></span>		<!-- TPR-561 -->
			
			<!-- <a class="sm-back js-enquire-sub-close" href="#">Back</a> -->
			<ul class="words depts${component.link.category.code}"> <!-- TPR-561 -->
				<li></li>		<!-- TPR-561 -->
				<c:forEach items="${component.navigationNode.children}" var="child">
					<c:if test="${child.visible}">
		
					
						
					<c:set value="${fn:length(child.links)/component.wrapAfter}" var="columns"/>

						<c:choose>
							<c:when test="${columns > 0 && columns <= 1}">
								<c:set value="col-md-4" var="sectionClass" />
							</c:when>

							<c:when test="${columns > 1 && columns < 3}">
								<c:set value="col-md-8" var="sectionClass" />
								<c:set value="column-2" var="columnClass" />
							</c:when>

							<c:when test="${columns > 2 && columns < 4}">
								<c:set value="col-md-12" var="sectionClass" />
								<c:set value="column-3" var="columnClass" />
							</c:when>

							<c:when test="${columns > 3 && columns < 5}">
								<c:set value="col-md-12" var="sectionClass" />
								<c:set value="column-4" var="columnClass" />
							</c:when>
							
							<c:otherwise>
								<c:set value="col-md-12" var="sectionClass" />
								<c:set value="column-5" var="columnClass" />
							</c:otherwise>
						</c:choose>

							<c:if test="${not empty child.title}">
								<li class="short words">
								<div class="toggle">
								<a href="${child.links[0].url}">${child.title}</a>
								</div>
								<span id="mobile-menu-toggle" class=""></span>		<!-- TPR-561 -->
								</li>  <!-- TPR-561 -->
							</c:if>
							
							<c:if test="${columns > 1}">
								<!-- <div class="row"> -->
							</c:if>
							
								<c:forEach items="${child.links}" step="${component.wrapAfter}" var="childlink" varStatus="i">
									<c:if test="${columns > 1}">
										<%-- <div class=" sub-navigation-section-column ${columnClass} "> --%>
									</c:if>

								
										<c:forEach items="${child.links}" var="childlink" begin="${i.index+1}" end="${i.index + component.wrapAfter - 1}">
											<li class="long words">
											<div class="toggle">
											<cms:component component="${childlink}" evaluateRestriction="true" />
											</div>
											</li>   <!-- TPR-561 -->
										</c:forEach>
										

									<c:if test="${columns > 1}">
									</c:if>
								</c:forEach>
							<c:if test="${columns > 1}">
								
							</c:if>
							
							
							
							
					</c:if>
				</c:forEach>
				</ul>		<!-- TPR-561 -->
				
	</c:when>
	<c:otherwise>
		<ul class="words"></ul>
	</c:otherwise>		
	</c:choose>			<!-- TPR-561 -->

</li>