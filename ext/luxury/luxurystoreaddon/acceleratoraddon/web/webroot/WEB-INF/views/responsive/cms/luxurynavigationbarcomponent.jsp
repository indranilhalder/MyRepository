<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<li>
	<c:choose>
		<c:when test="${not empty navigationNode.links}">
			<cms:component component="${navigationNode.links[0]}" evaluateRestriction="true" />
		</c:when>
		<c:otherwise>
			<a href="#">${navigationNode.title}</a>
		</c:otherwise>
	</c:choose>
	
	
	<c:if test="${not empty navigationNode.children}">
	<span class="sub-menu-toggle"></span>
	<div class="sub-menu">
		<div class="sub-menu-inner">
			<div class="row">
				<c:forEach items="${navigationNode.children}" var="child">
					<c:if test="${child.visible}">
						<div class="col-md-3">
							<ul>
								<li>
									<c:if test="${not empty child.links and fn:length(child.links) eq 1}">
										<cms:component component="${child.links[0]}" evaluateRestriction="true" />
									</c:if>
									<c:if test="${not empty child.links and fn:length(child.links) gt 1}">
										<a href="#">${child.title}</a>
										<span class="sub-menu-toggle"></span>
										<ul class="sub-menu">
											<c:forEach items="${child.links}"  var="childlink" varStatus="i">
												<li>
													<cms:component component="${childlink}" evaluateRestriction="true" />
												</li>
											</c:forEach>
										</ul>
									</c:if>
								</li>
							</ul>
						</div>
					</c:if>
				</c:forEach>
			</div>
		</div>
	</div>
	</c:if>
</li>