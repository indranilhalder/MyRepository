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
		<ul class="sub-menu-inner">           
             
                <c:forEach items="${navigationNode.children}" var="child">
                <li>
					<c:if test="${child.visible}">         
               					    <c:if test="${not empty child.links and fn:length(child.links) eq 1}">
										<cms:component component="${child.links[0]}" evaluateRestriction="true" />
										<span class="sub-menu-toggle"></span>
									</c:if>
						    		 <c:if test="${not empty child.children}"> 							
										
						                    <div class="sup-menu">
						                        <div class="sup-menu-inner">
						                            <div class="row">
							                            <c:forEach items="${child.children}" var="childL2">
												         <c:if test="${childL2.visible}"> 
							                                <div class="col-md-2">
							                                    <ul>
																	<li>
																		<c:if test="${not empty childL2.links and fn:length(childL2.links) eq 1}">
																			<cms:component component="${childL2.links[0]}" evaluateRestriction="true" element="h4" />
																		</c:if>
																		
																		<c:if test="${not empty childL2.links and fn:length(childL2.links) gt 1}">
																			<a href="#"><h4>${childL2.title}</h4></a>
																			<span class="sub-menu-toggle"></span>
																				<c:forEach items="${childL2.links}"  var="childL3link" varStatus="i">
																					<li>
																						<cms:component component="${childL3link}" evaluateRestriction="true" />
																					</li>
																				</c:forEach>
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
                </c:if>
           		</li> 
              </c:forEach>
           
        </ul>
	
	</div>
	</c:if>
	</li>