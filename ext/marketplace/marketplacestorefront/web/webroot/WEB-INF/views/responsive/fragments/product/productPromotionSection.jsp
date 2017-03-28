<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%--created the file to be used in size select ajax calls --%>
<span>
<product:productPromotionSection product="${product}" />
</span>
<span id="sizeSelectAjaxData" style="display:none">
${ajaxData}
</span>


