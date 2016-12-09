<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${component.visible}">
<div class="toggle shop_dept"><span><spring:theme code="navigation.department.shopBy"/></span>
	<span><spring:theme code="navigation.department.shopByDepartment"/></span></div> <!-- TPR-561 -->
		<span id="mobile-menu-toggle" class="mainli"></span>
			<ul>
			<!-- <li></li> --> <!-- commented to fix TISQAUAT-660 -->
				<!-- <li class="backShopDept hidden-md hidden-lg">
					<a class="sm-back js-toggle-sm-navigation" href="#">Back</a>
				</li> -->     <!-- TPR-561 -->
			<%-- ${component.name} --%>
				<!-- Changes for IA section -->
				<c:forEach items="${departmentList}"
					var="department">
					<c:set var="deptName" value="${fn:split(department.name, '||')}" />
					<c:url
						value="/${deptName[1]}/c-${fn:toLowerCase(department.code)}" var="departmentUrl" />
						<li>
					<div class="toggle departmenthover" id="dept${department.code}">
					<a href="${departmentUrl}">${deptName[0]}</a>
					<input type="hidden" id="for_ia_hot_dropdown_name" value="${department.name}">
					<input type="hidden" id="for_ia_hot_dropdown_code" value="${department.code}">  
					</div>
					<span id="mobile-menu-toggle" class=""></span>

					
					<ul class="words dept${department.code}">
					</ul>

					</li>
				</c:forEach>
				
				<c:forEach items="${components}" var="component">
					<c:if test="${component.navigationNode.visible}">
						<cms:component component="${component}" evaluateRestriction="true"
							navigationType="offcanvas" />
					</c:if>
				</c:forEach>
			</ul>
</c:if>