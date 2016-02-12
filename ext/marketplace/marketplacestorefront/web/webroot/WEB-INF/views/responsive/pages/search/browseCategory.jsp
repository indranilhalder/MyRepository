<%@ page trimDirectiveWhitespaces="true"%>
<%@ page
	import="de.hybris.platform.commercefacades.product.data.CategoryData"%>
<%@ page
	import="com.tisl.mpl.marketplacecommerceservices.service.MplCategoryService"%>
<%@ page import="com.tisl.mpl.data.MplDepartmentHierarchyData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<script>
	
	/* $(function() {
	
		
		
		var isConceirge = ${isConceirge};
		if(isConceirge!='true') {
		ACC.autocomplete.bindSearchText($('#text').val());
		}
		var inputArray = ${departmentHierarchyData.hierarchyList};
		var output = [];
		for (var i = 0; i < inputArray.length; i++) {
			var categoryArray = inputArray[i].split("/");
			var currentNode = output;
			//Construct 'All' tree node initially
			if(i==0) {
				output[0] = {label: "All", children: [], categoryCode: "", categoryType: "All", categoryName: ""};
			}
			//Other tree nodes are constructed here
			for (var j = 0; j < categoryArray.length; j++) {
				if(categoryArray[j] != null && categoryArray[j].length > 0){
					var categoryDetails = categoryArray[j].split(":");
					var categoryCode = categoryDetails[0];
					var categoryName = categoryDetails[1];
					var categoryType = "category";
					if(categoryDetails[3] == 'true') {
						categoryType = "department"
					}
					var lastNode = currentNode;
					for (var k = 0; k < currentNode.length; k++) {
						if (currentNode[k].categoryName == categoryName) {
							currentNode = currentNode[k].children;
							break;
						}
					}
					if (lastNode == currentNode) {
						var newNode = currentNode[k] = {label: categoryName, children: [], categoryCode: categoryCode, categoryType: categoryType, categoryName: categoryName};
						currentNode = newNode.children;
					}
				}
			}
		}

	var expandTree = false;
	if(output.length == 2) {
		expandTree = true;
	}
		
	$("#tree1").tree({
		data: output,
		autoOpen: expandTree

	});
	
	$('#tree1').bind(
			'tree.click',
			function(event) {
				var node = event.node;
						
				if(node.categoryType == 'All') {
					$('#q').val($('#text').val() + ":relevance");
					$('#searchCategoryTree').val("all");
				}
				else{
					$('#q').val($('#text').val() + ":relevance:category:" + node.categoryCode);
					$('#searchCategoryTree').val(node.categoryCode);
				} 
				
				$('#departmentHierarchyTreeForm').submit();
				
			}
	);
	
	}); */

</script>

<!-- a div with id="tree1" has been removed from the last line(by Saikat) -->
