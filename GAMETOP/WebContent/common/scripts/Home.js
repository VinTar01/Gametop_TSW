/*$(document).ready(function() {
	
	if($("#products").children().length == 0){
		
		$.post("/GAMETOP/ProductControl", {"operation" : "retrieveAll"}, function(data){
			
			let table = "<table><tr>";
				
			$.each(data, function(i, product) {
				table += '<td><a href="/GAMETOP/ProductControl?operation=retrieve&product=' + product.productId + '">' + product.productName + '</a><td>';
				if((i + 1) % 4 == 0)
					table += "</tr><tr>";
			});
			
			table += "</tr></table>";
			$("#products").append(table);
		})
		
		$.post("/GAMETOP/Admin/CategoryControl", {"categoryOperation" : "retrieveAll"}, function(data){	
			
			let list = "<ul>";
			
			$.each(data, function(i, category) {
				list += '<li><a href="/GAMETOP/HomeControl?operation=retrieveProducts&category=' + category.categoryId + '">' + category.categoryName + '</a></li>';
			});
			
			list += "</ul>";
			$("#categories").append(list);
		})
	}
});*/