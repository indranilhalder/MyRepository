
	function constructDepartmentHierarchy(inputArray) {
		var output = [];
		for (var i = 0; i < inputArray.length; i++) {
			var categoryArray = inputArray[i].split("/");
			var currentNode = output;
			//Construct 'All' tree node initially for search page
			if(i==0 && $('#isCategoryPage').val() == '') {
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
	if($('#isCategoryPage').val() == 'true'){	
		// Assign tree object to category page
		$("#categoryPageDeptHierTree").tree({
			data: output,
			autoOpen: true
	
		});
	}else {
		// Assign tree object to search page
		$("#searchPageDeptHierTree").tree({
			data: output,
			autoOpen: expandTree
	
		});
		
		// persist search text in search text box
		 var isConceirge = $('#isConceirge').val();
			if(isConceirge!='true') {
			ACC.autocomplete.bindSearchText($('#text').val());
			}
	}
	
	$('#categoryPageDeptHierTree').bind(
			'tree.click',
			function(event) {
				var node = event.node;
				if(node.categoryType != 'All') {
					var actionText = ACC.config.contextPath;
					actionText = (actionText + '/Categories/' + node.label + '/c/' + node.categoryCode);
					$('#categoryPageDeptHierTreeForm').attr('action',actionText);
					$('#categoryPageDeptHierTreeForm').submit();
				}
			}
	);
	
	$('#searchPageDeptHierTree').bind(
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
				
				$('#searchPageDeptHierTreeForm').submit();
				
			}
	);
		
	}
