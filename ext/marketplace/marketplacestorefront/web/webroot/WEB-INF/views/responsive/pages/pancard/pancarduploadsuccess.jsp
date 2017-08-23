<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/style.css"/>
<body>
	<div class="wrapper-block pan-card-block">
		<section>
			<div class="pan-detail-sec">
				<h2 class="pan-heading">PAN Card Details</h2>
				<p class="lead-text">Your order is just one step away!</p>
				
				<div class="form-section">
					<div class="panfield-wrapper clearfix orderId-left">
							<div class="pan-label">Order ID:</div>
							<div class="pan-label-value disable-color">${orderreferancenumber}</div>
					</div>
					<div class="panfield-wrapper clearfix orderName-right">
							<div class="pan-label">Name:</div>
							<div class="pan-label-value disable-color">${customername}</div>
					</div>
					<div class="pan-card-fields">
							<div class="pan-details-success" id="panDetailsMsg">PanDetails Uploaded Successfully</div>
					</div>
				</div>
			</div>
		</section>
	</div>
</body>
</html>