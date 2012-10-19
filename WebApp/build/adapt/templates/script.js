var currentRow=-1;

function SelectRow(newRow) {
	alert('SelectRow ' + newRow);
	var table = document.getElementById('datatable');
	document.DataForm.id.value = table.rows[newRow].cells[0].firstChild.textContent;
	document.DataForm.naziv.value = table.rows[newRow].cells[1].firstChild.textContent;
				
	for (i = 1; i < table.rows.length; i++) {
		table.rows[i].cells[0].style.background='#FFF';
		table.rows[i].cells[1].style.background='#FFF';
	}
	
	table.rows[newRow].cells[0].style.background='#AAF';
	table.rows[newRow].cells[1].style.background='#AAF';
		
	currentRow=newRow;
}

function goUp() {
	alert('goUp ' + currentRow--);
	SelectRow(currentRow--);
}

function goDown() {
	alert('goUp ' + currentRow++);
	SelectRow(currentRow++);
}