var currentRow=-1;

function SelectRow(newRow) {
	var table = document.getElementById('datatable');
	//document.DataForm.id.value = table.rows[newRow].cells[0].firstChild.textContent;
	//document.DataForm.naziv.value = table.rows[newRow].cells[1].firstChild.textContent;
				
	for (i=1; i<table.rows.length; i++) {
		for(j=0; j<table.rows[i].cells.length-1; j++) {
			table.rows[i].cells[j].style.background='#FFF';
		}
	}

	
	for(k=0; k<table.rows[newRow].cells.length-1; k++) {
		table.rows[newRow].cells[k].style.background='#AAF';
	}
		
	currentRow=newRow;
	return true;
}

function GetSelectedRow() {
   return currentRow;
}

function highlight() {
	var menu = document.getElementById('menu');
	var anchors = menu.getElementsByTagName('a');
	var loc = document.location.pathname;
	if(anchors != null) {
		for( var i = 0; i < anchors.length ; i++){
			var href = anchors[i].href.substring(21);
			if(href == loc) {
				anchors[i].style.backgroundColor = "#FFFFFF";
				anchors[i].style. color = "#000000";
				anchors[i].style.borderBottom = "1px solid #FFFFFF";
			}
		}
	}
	
}