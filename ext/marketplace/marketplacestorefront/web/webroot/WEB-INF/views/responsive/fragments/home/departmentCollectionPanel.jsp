<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach items="${secondLevelCategoryMap}" var="entry">

	<c:set var="values" value="${entry.value}" />
	<c:forEach items="${values}" var="item">
		<c:url value="/Categories/${item.name}/c/${item.code}"
			var="secondLevelDepartmentUrl" />
		<li class="short words"><div class="toggle">
				<a href="${secondLevelDepartmentUrl}">${item.name}</a>
			</div></li>


		<!-- Iterating the third level of categories -->
		<c:forEach items="${thirdLevelCategoryMap}" var="entry">
			<c:if test="${entry.key== item.code}">
				<!-- Iterating through the values of third level category map-->
				<c:set var="values" value="${entry.value}" />
				<c:forEach items="${values}" var="item">
					<c:url value="/Categories/${item.name}/c/${item.code}"
						var="thirdLevelDepartmentUrl" />
					<li class="long words"><div class="toggle">
							<a href="${thirdLevelDepartmentUrl}" style="font-weight: normal;">${item.name}</a>
						</div></li>
				</c:forEach>
			</c:if>

		</c:forEach>


	</c:forEach>

</c:forEach>