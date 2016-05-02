<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


	<div class="toggle"><span><spring:theme code="navigation.department.shopBy"/></span>
	<span><spring:theme code="navigation.department.shopByDepartment"/></span></div>

			<ul>
				
				<!-- Iterating through the collection of departments -->
				<c:forEach items="${component.departmentCollection}"
					var="department">
					
					<c:url
						value="/${department.name}/c-${department.code}"
						var="departmentUrl" />
						<li>
					<div class="toggle departmenthover" id="dept${department.code}">
					<a href="${departmentUrl}">${department.name}</a>
					<input type="hidden" id="for_ia_hot_dropdown_name" value="${department.name}">
					<input type="hidden" id="for_ia_hot_dropdown_code" value="${department.code}">  
					</div>

					
					<ul class="words dept${department.code}">
					</ul>

					</li>
				</c:forEach>
				
			</ul>

		<!-- </div> -->

