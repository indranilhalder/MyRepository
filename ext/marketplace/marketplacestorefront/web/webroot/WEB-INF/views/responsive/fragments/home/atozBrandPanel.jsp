<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div id="groups"
	style="min-height: 28px; border-bottom: 4px solid; letter-spacing: 1px; font-size: 17px;">
	<c:forEach items="${groupedAlphabets}" var="entry">
		<div id="group" style="float: left; margin-right: 10px;">

			<a class="brandGroupLink" href="#"
				data-tab="${entry.key}-${entry.value}">${entry.key}-${entry.value}</a>
		</div>
	</c:forEach>
</div>

<div id="appendedAtoZBrands" class="azWrapper">
	<div id="A-E" class="range current" style="display:block;">


		<c:forEach items="${AToEBrands}" var="entry">

			<ul class="a-z-ul">
				<h3>${entry.key}</h3>
				<c:set var="values" value="${entry.value}" />
				<c:forEach items="${values}" var="item" begin='1' end='10'>
					<c:url var="brandlistUrl" value="/Categories/c/${item.code}"></c:url>
					<li class="a-z-li"><a href="${brandlistUrl}">${item.name}</a></li>

				</c:forEach>
			</ul>


		</c:forEach>
	</div>
	<div id="F-J" class="range" style="display:none;">
		<c:forEach items="${FToJBrands}" var="entry">

			<ul class="a-z-ul">
				<h3>${entry.key}</h3>
				<c:set var="values" value="${entry.value}" />
				<c:forEach items="${values}" var="item" begin='0' end='9'>
					<%-- <c:url var="subBrandUrl" value="${item.subBrandUrl}"></c:url> --%>
					<c:url var="brandlistUrl" value="/Categories/c/${item.code}"></c:url>
					<li class="a-z-li"><a href="${brandlistUrl}">${item.name}</a></li>

				</c:forEach>
			</ul>

		</c:forEach>
	</div>


	<div id="K-O" class="range" style="display:none;">
		<c:forEach items="${KToOBrands}" var="entry">

			<ul class="a-z-ul">
				<h3>${entry.key}</h3>
				<c:set var="values" value="${entry.value}" />
				<c:forEach items="${values}" var="item" begin='0' end='9'>
					<%-- 	<c:url var="subBrandUrl" value="${item.subBrandUrl}"></c:url> --%>
					<c:url var="brandlistUrl" value="/Categories/c/${item.code}"></c:url>
					<li class="a-z-li"><a href="${brandlistUrl}">${item.name}</a></li>

				</c:forEach>
			</ul>

		</c:forEach>
	</div>


	<div id="P-T" class="range" style="display:none;">
		<c:forEach items="${PToTBrands}" var="entry">

			<ul class="a-z-ul">
				<h3>${entry.key}</h3>
				<c:set var="values" value="${entry.value}" />
				<c:forEach items="${values}" var="item" begin='0' end='9'>


					<%-- <c:url var="subBrandUrl" value="${item.subBrandUrl}"></c:url> --%>
					<c:url var="brandlistUrl" value="/Categories/c/${item.code}"></c:url>
					<li class="a-z-li"><a href="${brandlistUrl}">${item.name}</a></li>

				</c:forEach>
			</ul>

		</c:forEach>
	</div>

	<div id="U-Z" class="range" style="display:none;">
		<c:forEach items="${UToZBrands}" var="entry">

			<ul class="a-z-ul">
				<h3>${entry.key}</h3>
				<c:set var="values" value="${entry.value}" />
				<c:forEach items="${values}" var="item" begin='0' end='9'>
					<c:url var="brandlistUrl" value="/Categories/c/${item.code}"></c:url>
					<li class="a-z-li"><a href="${brandlistUrl}">${item.name}</a></li>

				</c:forEach>
			</ul>


		</c:forEach>
	</div>



	<div id="A-J" class="range"
		style="padding-top: 40px; font-size: 12px; letter-spacing: 1px;">
		<c:forEach items="${AToJBrands}" var="entry">

			<ul class="a-z-ul">
				<h3>${entry.key}</h3>
				<c:set var="values" value="${entry.value}" />
				<c:forEach items="${values}" var="item" begin='0' end='9'>
					<c:url var="brandlistUrl" value="/Categories/c/${item.code}"></c:url>
					<li class="a-z-li" style="color: blue; padding: 0px !important"><a
						href="${brandlistUrl}">${item.name}</a></li>

				</c:forEach>
			</ul>


		</c:forEach>
	</div>


	<div id="F-O" class="range"
		style="padding-top: 40px; font-size: 12px; letter-spacing: 1px;">
		<c:forEach items="${FToOBrands}" var="entry">

			<ul class="a-z-ul">
				<h3>${entry.key}</h3>
				<c:set var="values" value="${entry.value}" />
				<c:forEach items="${values}" var="item" begin='0' end='9'>
					<c:url var="brandlistUrl" value="/Categories/c/${item.code}"></c:url>
					<li class="a-z-li" style="color: blue; padding: 0px !important"><a
						href="${brandlistUrl}">${item.name}</a></li>

				</c:forEach>
			</ul>


		</c:forEach>
	</div>

	<div id="K-T" class="range"
		style="padding-top: 40px; font-size: 12px; letter-spacing: 1px;">
		<c:forEach items="${KToTBrands}" var="entry">

			<ul class="a-z-ul">
				<h3>${entry.key}</h3>
				<c:set var="values" value="${entry.value}" />
				<c:forEach items="${values}" var="item" begin='0' end='9'>
					<c:url var="brandlistUrl" value="/Categories/c/${item.code}"></c:url>
					<li class="a-z-li" style="color: blue; padding: 0px !important"><a
						href="${brandlistUrl}">${item.name}</a></li>

				</c:forEach>
			</ul>

		</c:forEach>
	</div>


	<div id="P-Z" class="range">
		<c:forEach items="${PToZBrands}" var="entry">

			<ul class="a-z-ul">
				<h3>${entry.key}</h3>
				<c:set var="values" value="${entry.value}" />
				<c:forEach items="${values}" var="item" begin='0' end='9'>
					<%-- 	<c:url var="subBrandUrl" value="${item.subBrandUrl}"></c:url> --%>
					<c:url var="brandlistUrl" value="/Categories/c/${item.code}"></c:url>
					<li class="a-z-li" style="color: blue; padding: 0px !important"><a
						href="${brandlistUrl}">${item.name}</a></li>

				</c:forEach>
			</ul>

		</c:forEach>
	</div>

</div>