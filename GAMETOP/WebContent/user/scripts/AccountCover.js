function changeAccountCover(){

	$("#accountCover").trigger("click");
	
	$("#accountCover").on('change',function(){
        $("#submitFile").trigger("click");
    });
}