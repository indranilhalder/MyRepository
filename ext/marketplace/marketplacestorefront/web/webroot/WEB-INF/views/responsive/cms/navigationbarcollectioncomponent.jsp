<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:if test="${component.visible}">
<div class="toggle shop_dept"><span><spring:theme code="navigation.department.shopBy"/></span>
	<span><spring:theme code="navigation.department.shopByDepartment"/></span></div> <!-- TPR-561 -->
		<span id="mobile-menu-toggle" class="mainli"></span>
			<ul>
			<li></li>
				<!-- <li class="backShopDept hidden-md hidden-lg">
					<a class="sm-back js-toggle-sm-navigation" href="#">Back</a>
				</li> -->     <!-- TPR-561 -->
			<%-- ${component.name} --%>
			
				<c:forEach items="${components}" var="component">
					<c:if test="${component.navigationNode.visible}">
						<cms:component component="${component}" evaluateRestriction="true"
							navigationType="offcanvas" />
					</c:if>
				</c:forEach>
			</ul>
</c:if>