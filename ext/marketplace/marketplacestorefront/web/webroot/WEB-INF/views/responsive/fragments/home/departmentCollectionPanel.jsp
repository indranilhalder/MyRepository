<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:url value="/${firstLevelCategory.name}/c-${firstLevelCategory.code}" var="departmentUrl" />
<li><a href="${departmentUrl}" class="view_dept">View ${firstLevelCategory.name}</a></li>
<c:forEach items="${secondLevelCategoryMap}" var="entry">

	<c:set var="values" value="${entry.value}" />
	<c:forEach items="${values}" var="item">
	<c:set var="catName" value="${fn:split(item.name, '||')}" />
		<c:url value="/${catName[1]}/c-${fn:toLowerCase(item.code)}"
			var="secondLevelDepartmentUrl" />
		<li class="short words"><div class="toggle">
				<a href="${secondLevelDepartmentUrl}">${catName[0]}</a>
			</div>
			<span id="mobile-menu-toggle"></span>
			</li>


		<!-- Iterating the third level of categories -->
		<c:forEach items="${thirdLevelCategoryMap}" var="entry">
			<c:if test="${entry.key== item.code}">
				<!-- Iterating through the values of third level category map-->
				<c:set var="values" value="${entry.value}" />
				<c:forEach items="${values}" var="item">
				
					<c:set var="catName" value="${fn:split(item.name, '||')}" />
					<c:url value="/${catName[1]}/c-${fn:toLowerCase(item.code)}"
						var="thirdLevelDepartmentUrl" />
					<li class="long words"><div class="toggle">
							<a href="${thirdLevelDepartmentUrl}" style="font-weight: normal;">${catName[0]}</a>
						</div></li>
				</c:forEach>
			</c:if>

		</c:forEach>


	</c:forEach>

</c:forEach>