function (wgt, dataValue) {
	$('.language_select_all > input').change(function(){  //"select all" change 
	    var status = this.checked; // "select all" checked status
		$('.language > input').each(function(){ //iterate all listed checkbox items
			this.checked = status; //change ".checkbox" checked status
		});
	});

	$('.language > input').change(function(){ //".checkbox" change 
	//uncheck "select all", if one of the listed checkbox item is unchecked
		var selectAll = $('.language_select_all > input')[0];
		if(this.checked == false){ //if this item is unchecked
			selectAll.checked = false; //change "select all" checked status to false
		}

		//check "select all" if all checkbox items are checked
		if ($('.language > input:checked').length == $('.language > input').length ){ 
			selectAll.checked = true; //change "select all" checked status to true
		}
	});
	
	$('.media_select_all > input').change(function(){  //"select all" change 
	    var status = this.checked; // "select all" checked status
		$('.media > input').each(function(){ //iterate all listed checkbox items
			this.checked = status; //change ".checkbox" checked status
		});
	});

	$('.media > input').change(function(){ //".checkbox" change 
	//uncheck "select all", if one of the listed checkbox item is unchecked
		var selectAll = $('.media_select_all > input')[0];
		if(this.checked == false){ //if this item is unchecked
			selectAll.checked = false; //change "select all" checked status to false
		}

		//check "select all" if all checkbox items are checked
		if ($('.media > input:checked').length == $('.media > input').length ){ 
			selectAll.checked = true; //change "select all" checked status to true
		}
	});
	
	$('.connotation_select_all > input').change(function(){  //"select all" change 
	    var status = this.checked; // "select all" checked status
		$('.connotation > input').each(function(){ //iterate all listed checkbox items
			this.checked = status; //change ".checkbox" checked status
		});
	});

	$('.connotation > input').change(function(){ //".checkbox" change 
	//uncheck "select all", if one of the listed checkbox item is unchecked
		var selectAll = $('.connotation_select_all > input')[0];
		if(this.checked == false){ //if this item is unchecked
			selectAll.checked = false; //change "select all" checked status to false
		}

		//check "select all" if all checkbox items are checked
		if ($('.connotation > input:checked').length == $('.connotation > input').length ){ 
			selectAll.checked = true; //change "select all" checked status to true
		}
	});

}