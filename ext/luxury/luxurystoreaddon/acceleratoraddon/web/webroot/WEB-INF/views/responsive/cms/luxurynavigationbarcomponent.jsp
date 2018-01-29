<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<c:if test="${not empty navigationNode.children}">
	<c:forEach items="${navigationNode.children}" var="child">
		<c:if test="${child.visible}">
		<li>
				<c:choose>
					<c:when test="${not empty child.links}">
						<cms:component component="${child.links[0]}" evaluateRestriction="true" />
					</c:when>
					<c:otherwise>
						<a href="#">${child.title}</a>
					</c:otherwise>
				</c:choose>
				<span class="sub-menu-toggle"></span>
				
				<c:if test="${not empty child.children}">
					<div class="sub-menu">
						<div class="sub-menu-inner">
							<div class="row">
									<c:forEach items="${child.children}" var="childL2">
										<div class="col-md-3">
											<ul>
												<li>
											         <c:if test="${childL2.visible}">
											         	<c:if test="${not empty childL2.links and fn:length(childL2.links) eq 1}">
															<cms:component component="${childL2.links[0]}" evaluateRestriction="true" element="h4" />
														</c:if>
														
														<c:if test="${not empty childL2.links and fn:length(childL2.links) gt 1}">
															<a href="#"><h4>${childL2.title}</h4></a>
															<span class="sub-menu-toggle"></span>
															<ul class="sub-menu">
																<c:forEach items="${childL2.links}"  var="childL3link" varStatus="i">
																	<li>
																		<cms:component component="${childL3link}" evaluateRestriction="true" />
																	</li>
																</c:forEach>
															</ul>	
														</c:if>
											         </c:if>
										         </li>
									         </ul>
								         </div>
								    </c:forEach> 
							 </div>
						</div>
				</div>     
			</c:if>
										
						</li>
					</c:if>
	</c:forEach>
</c:if>
