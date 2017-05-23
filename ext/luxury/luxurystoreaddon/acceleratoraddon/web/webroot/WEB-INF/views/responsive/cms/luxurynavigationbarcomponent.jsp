<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<li>
<a href="#">${navigationNode.title }</a>
<span class="sub-menu-toggle"></span>
<div class="sub-menu">
							<div class="sub-menu-inner">
								<div class="row">
<c:forEach items="${navigationNode.children}" var="child">
	<div class="col-md-3">
		<ul>
			<li>
				<a href="#">${child.title }</a><span class="sub-menu-toggle"></span>
				<ul class="sub-menu">
					<c:forEach items="${child.links}"  var="childlink" varStatus="i">
						<li><a href="#">${childlink.linkName }</a></li>
					</c:forEach>
				</ul>
			</li>
		</ul>
	</div>
</c:forEach>
</div></div></div>
</li>