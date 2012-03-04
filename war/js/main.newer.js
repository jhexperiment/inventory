/* Define two custom functions (asc and desc) for string sorting */
jQuery.fn.dataTableExt.oSort['string-case-asc']  = function(x,y) {
	return ((x < y) ? -1 : ((x > y) ?  1 : 0));
};

jQuery.fn.dataTableExt.oSort['string-case-desc'] = function(x,y) {
	return ((x < y) ?  1 : ((x > y) ? -1 : 0));
};

jQuery.fn.dataTableExt.oSort['numeric-comma-asc']  = function(a,b) {
	var x = (a == "-") ? 0 : a.replace( /,/, "." );
	var y = (b == "-") ? 0 : b.replace( /,/, "." );
	x = parseFloat( x );
	y = parseFloat( y );
	return ((x < y) ? -1 : ((x > y) ?  1 : 0));
};

jQuery.fn.dataTableExt.oSort['numeric-comma-desc'] = function(a,b) {
	var x = (a == "-") ? 0 : a.replace( /,/, "." );
	var y = (b == "-") ? 0 : b.replace( /,/, "." );
	x = parseFloat( x );
	y = parseFloat( y );
	return ((x < y) ?  1 : ((x > y) ? -1 : 0));
};

jQuery.fn.dataTableExt.oSort['po-number-asc']  = function(a,b) {
	var x = a.replace( "PX", "" );
	var y = b.replace( "PX", "" );
	x = parseInt( x );
	y = parseInt( y );
	return ((x < y) ? -1 : ((x > y) ?  1 : 0));
};

jQuery.fn.dataTableExt.oSort['po-number-desc'] = function(a,b) {
	var x = a.replace( "PX", "" );
	var y = b.replace( "PX", "" );
	x = parseInt( x );
	y = parseInt( y );
	return ((x < y) ?  1 : ((x > y) ? -1 : 0));
};

jQuery.fn.dataTableExt.aTypes.push( function ( sData ) {
	var sValidChars = "0123456789";
	var Char;
	/* Check the numeric part */
	for ( i=2 ; i<sData.length ; i++ ) {
		Char = sData.charAt(i);
		if (sValidChars.indexOf(Char) == -1) {
			return null;
		}
	}
	if ( sData.substring(0,2) == 'PX' ) {
		return 'po-number';
	}
	
	return null;
});

$(document).ready(function() {
	thisPage.init();

	$(window).load(function() {
		
	});
});


var aSelected = [];
var generalTable;
var tabs;

var oGenralTableOptions = {
	"bJQueryUI": true,
	"bPaginate": false,
	"sPaginationType": "full_numbers",
	"bProcessing": true,
	"bServerSide": true,
	"sAjaxSource": "/list",
	"bAutoWidth": false,
	"sScrollY": "400px",
	"bScrollCollapse": true,
	//"bSort": false,
	"aaSorting": [[ 1, 'asc' ], [ 2, 'asc' ], [ 3, 'asc' ]],
	"sDom": '<"H"<"add_delete_toolbar">lfr>t<"F"ip>',
	"aoColumns": [
		{ "sName": "id", "mDataProp": "id", "sType": 'numeric'},
		//{ "sName": "rowButton", "mDataProp": "rowButton", "sType": 'string' },
		{ "sName": "poNumber", "mDataProp": "poNumber", "sType": 'po-number'},
		{ "sName": "description", "mDataProp": "description", "sType": 'string'},
		{ "sName": "decalNumber", "mDataProp": "decalNumber", "sType": 'string'},
		{ "sName": "propertyNumber", "mDataProp": "propertyNumber", "sType": 'numeric'},
		{ "sName": "location", "mDataProp": "location", "sType": 'string'},
		{ "sName": "custodian", "mDataProp": "custodian", "sType": 'string'},
		{ "sName": "quantity", "mDataProp": "quantity", "sType": 'numeric'},
		{ "sName": "status", "mDataProp": "status", "sType": 'string'},
		{ "sName": "notes", "mDataProp": "notes", "sType": 'string'}
	],
	"aoColumnDefs": [
		{ 
			"sName": "id", 
			"sClass": "id",
			"sWidth": '25px', 
			"bVisible": false, 
			"bSearchable": false, 
			"bSortable": false, 
			"aTargets": [ "id" ] 
		},
		{ 
			"sName": "rowButton", 
			"sClass": "rowButton",
			"sWidth":"25px", 
			"bSearchable": false, 
			"bSortable": false, 
			"aTargets": [ "rowButton" ]
		},
		{ "sName": "poNumber", "sClass": "poNumber", "sWidth": '100px', "aTargets": [ "poNumber" ] },
		{ "sName": "description", "sClass": "description", "sWidth": '150px', "aTargets": [ "description" ] },
		{ "sName": "decalNumber", "sClass": "decalNumber", "sWidth": '60px', "aTargets": [ "decalNumber" ] },
		{ "sName": "propertyNumber", "sClass": "propertyNumber", "sWidth": '100px', "aTargets": [ "propertyNumber" ] },
		{ "sName": "location", "sClass": "location", "sWidth": '65px', "aTargets": [ "location" ] },
		{ "sName": "custodian", "sClass": "custodian", "sWidth": '150px', "aTargets": [ "custodian" ] },
		{ "sName": "quantity", "sClass": "quantity", "sWidth": '35px', "aTargets": [ "quantity" ] },
		{ "sName": "status", "sClass": "status", "sWidth": '100px', "aTargets": [ "status" ] },
		{ "sName": "notes", "sClass": "notes", "sWidth": null, "bSortable": false, "aTargets": [ "notes" ] }
	],
	"fnHeaderCallBack": function( nHead, aasData, iStart, iEnd, aiDisplay ) {},
	"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
		return nRow;
	},
	"fnServerData": function ( sSource, aoData, fnCallback ) {
		var iColumns, sColumns, iSortCol_0, iSortingCols;
		var aoOut = [];
		var amDataProp = [];
		var abRegex = [];
		var abSearchable = [];
		var asSearch = [];
		var abSortable = [];
		var aiSortCol = [];
		var asSortDir = [];
		var aRemove = ["rowButton"];
		
		$.each(aoData, function() {
			if (this.name.indexOf("mDataProp_") != -1) {
				amDataProp.push(this.value);
			} 
			else if (this.name.indexOf("bRegex_") != -1) {
				abRegex.push(this.value);
			} 
			else if (this.name.indexOf("bSearchable_") != -1) {
				abSearchable.push(this.value);
			} 
			else if (this.name.indexOf("sSearch_") != -1) {
				asSearch.push(this.value);
			} 
			else if (this.name.indexOf("bSortable_") != -1) {
				abSortable.push(this.value);
			} 
			else if (this.name.indexOf("iSortCol_") != -1) {
				aiSortCol.push(this.value);
			} 
			else if (this.name.indexOf("iSortDir_") != -1) {
				asSortDir.push(this.value);
			} 
			else if ("iColumns" == this.name) {
				iColumns = this.value;
			} 
			else if ("sColumns" == this.name) {
				sColumns = this.value;
			} 
			else if ("iSortingCols" == this.name) {
				iSortingCols = this.value;
			} 
			else {
				aoOut.push(this);
			}
		});
		
		var bSomethingRemoved = false;
	
		$.each(aRemove, function() {
			var sRemove = this.toString();
			
			sColumns = sColumns.replace(sRemove, "").replace(",,", ",").replace(", ,", ",");
			if (sColumns.charAt(0) == ",") {
				sColumns = sColumns.substring(1);
			}
			
			var iRemoveIndex = -1;
			$.each(amDataProp, function(index) {
				smDataProp = this.toString();
				if (sRemove == smDataProp) {
					iRemoveIndex = index;
					return false;
				}
			});
			if (iRemoveIndex != -1) {
				bSomethingRemoved = true;
				
				amDataProp.splice(iRemoveIndex, 1);
				abRegex.splice(iRemoveIndex, 1);
				abSearchable.splice(iRemoveIndex, 1);
				asSearch.splice(iRemoveIndex, 1);
				abSortable.splice(iRemoveIndex, 1);
				
				for (i = 0; i < iSortingCols; i++) {
					aiSortCol[i] -= 1;
				}
			}
			
		});
		
		if (bSomethingRemoved) {
			iColumns = iColumns - aRemove.length;
			
			
		}
		
		aoOut.push({"name": "iColumns", "value": iColumns});
		aoOut.push({"name": "sColumns", "value": sColumns});
		//aoOut.push({"name": "iSortCol_0", "value": iSortCol_0});
		aoOut.push({"name": "iSortingCols", "value": iSortingCols});
		
		for (i = 0; i < iSortingCols; i++) {
			aoOut.push({"name": "iSortCol_" + i, "value": aiSortCol[i]});
			aoOut.push({"name": "iSortDir_" + i, "value": asSortDir[i]});
		}
		
		for (i = 0; i < iColumns; i++) {
			aoOut.push({"name": "mDataProp_" + i, "value": amDataProp[i]});
			aoOut.push({"name": "bRegex_" + i, "value": abRegex[i]});
			aoOut.push({"name": "bSearchable_" + i, "value": abSearchable[i]});
			aoOut.push({"name": "bSearch_" + i, "value": asSearch[i]});
			aoOut.push({"name": "bSortable_" + i, "value": abSortable[i]});
		}
		
		
		$('#generalTable_wrapper th.rowButton').remove();
		$("#generalTable_wrapper td.rowButton").remove();
		
		$.ajax( {
			"dataType": 'json',
			"type": "GET",
			"url": sSource,
			"data": aoOut,
			"success": fnCallback
		});
	},
	"fnPreDrawCallback": function( oSettings ) {},
	"fnInitComplete": function() {},
	"fnDrawCallback": function () {
		var invType = $(".ui-tabs-selected a").html().toLowerCase();
		$("#formAddNewRow #invType").val(invType);
		
		var button = $(".add_delete_toolbar button");
		if ( ! button.hasClass("ui-button") ) {
			button.addClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only");
		}
				
		var rowDomList = $('#generalTable_wrapper #generalTable tbody tr');
		rowDomList.click( function() {
			if ( $(this).hasClass('row_selected') ) {
				$(this).removeClass('row_selected');
			}
			else {
				$(this).addClass('row_selected');
			}
			return false;
		} );
		
		/*
		 * Insert a 'details' column to the table
		 */
		var style = 'style="width:25px' + generalTable.fnSettings().aoColumns + '"';
		var html = '<th class="rowButton center ui-state-default" ' + style + '>'
				 +		'<div class="DataTables_sort_wrapper">' 
				 +			'<span class=""></span>'
				 +		'</div>'
				 + "</th>";
		var thDom = $(html);
		var tmpHeader = $('#generalTable_wrapper .dataTables_scrollBody thead tr');
		if (tmpHeader.children('th.rowButton').length == 0) {
			tmpHeader.prepend(thDom.clone());
		}
		
		var mainHeader = $('#generalTable_wrapper .dataTables_scrollHeadInner thead tr');
		if (mainHeader.children('th.rowButton').length == 0) {
			mainHeader.prepend(thDom.clone());
		}
						
		var html = '<td class="rowButton center" rowspan="1" colspan="1">'
				 +		'<img src="/images/details_open.png">' 
				 + "</th>";
		var tdDom = $(html);
		if (rowDomList.children('th.rowButton').length == 0) {
			rowDomList.prepend(tdDom.clone());
		}
		
		
		var settings = generalTable.fnSettings();
		var aoColumn = $.extend({}, settings.aoColumns[0]);
		aoColumn.mDataProp = "rowButton";
		aoColumn.nTh = $("#generalTable_wrapper .dataTables_scrollHeadInner thead tr th.rowButton")[0];
		aoColumn.sName = "rowButton";
		aoColumn.title = "rowButton";
		aoColumn.sType = null;
		aoColumn.bVisible = true;
		aoColumn.bSortable = false;
		settings.aoColumns.splice(1,0,aoColumn);
		var aoPreSearchCols = $.extend({}, settings.aoPreSearchCols[0]);
		settings.aoPreSearchCols.splice(1,0,aoPreSearchCols);
		
		$('#generalTable_wrapper #generalTable tbody tr td.id').remove();
		
		var idFromAoColumns = generalTable.fnSettings().aoColumns.shift();
		
		generalTable.fnAdjustColumnSizing(false);

		/* Add event listener for opening and closing details
		 * Note that the indicator for showing which row is open is not controlled by DataTables,
		 * rather it is done here
		 */
		$("#generalTable_wrapper td.rowButton img").click( function (event) {
			var nTr = this.parentNode.parentNode;
			if ( this.src.match('details_close') )
			{
				/* This row is already open - close it */
				this.src = "/images/details_open.png";
				generalTable.fnClose( nTr );
			}
			else
			{
				/* Open this row */
				this.src = "/images/details_close.png";
				var aData = generalTable.fnGetData( nTr );
				var html = '<div>'
						 + 		'<label for="poDate">PO Date:</label>'
						 +		'<span id="poDate">' + aData.poDate + '</span>'
						 + 		'<br />'
						 + 		'<label for="poRecieveDate">PO Recieve Date:</label>'
						 +		'<span id="poRecieveDate">' + aData.poRecieveDate + '</span>'
						 + 		'<br />'
						 + 		'<label for="lastEditUser">Last Edit User:</label>'
						 +		'<span id="lastEditUser">' + aData.lastEditUser + '</span>'
						 + 		'<br />'
						 + 		'<label for="lastEditDate">Last Edit Date:</label>'
						 +		'<span id="lastEditDate">' + aData.lastEditDate + '</span>'
						 + 		'<br />'
						 + '</div>';
				generalTable.fnOpen( nTr, html, 'details' );
				var dates = $("#generalTable_wrapper td.details #poDate, #generalTable_wrapper td.details #poRecieveDate");
				/*
				dates.validate({
					"submitHandler": function(form) {
						
						return true;
					}
				
				});
				*/
				
				dates.editable('/update', {
						"indicator": "saving...",
						/*
						"callback": function(value, settings) {
							var tmp = "";
						},
						"data": function(value, settings) {
							var tmp = "";
							return value;
						},
						*/
						"submitdata": function(value, setting) {
							var rowDom = $(this).parent("div").parent("td.details").parent("tr").prev()[0];
							var rowIndex = generalTable.fnGetPosition(rowDom);
							var aData = generalTable.fnGetData(rowIndex);
							var columnName = $(this).attr("id");
							//value = $(this).children("form").children("input").val();
							
							
							return {
								//"dataType": "text",
								"invType": "general",
								"id": aData.id,
								"columnName": columnName
							};
							
						}
						
					});
			}
			//event.preventDefault();
			return false;
		} );
		
	}
}

var oGenralTableEditableOptions = {
	//"sUpdateURL": "/update",
	"placeholder": "",
	"sAddURL": "/add",
	"sAddNewRowFormId": "formAddGeneralItemRow",
	"sDeleteURL": "/delete",
	"sUpdateURL": function(value, settings) {
		var aPos = generalTable.fnGetPosition( this );
		var rowIndex = aPos[0];
		var colIndexNoHidden = aPos[1];
		var colIndexWithHidden = aPos[2];
		var aData = generalTable.fnGetData( rowIndex );
		var aoColumns = generalTable.fnSettings().aoColumns;
		var data = {
			"type": "POST",
			"returnType": "text",
			"invType": $(".ui-tabs-selected a").html().toLowerCase(),
			"columnName": aoColumns[colIndexWithHidden].mDataProp,
			"id": aData.id,
			"value": value
		}
		querySite("/update", data, function() {});
		return value;
	},
	"aoColumns": [
	    { //poNumber
	    	"placeholder": ""
	    },
		{ //description
	    	"placeholder": "",
	    	"cssclass": "required"
		}, 
		{ //decalNumber
			"placeholder": ""
		}, 
		{ //propertyNumber
			"placeholder": ""
		}, 
		{ //location
			"placeholder": "",
			"cssclass": "required"
		}, 
		{ //custodian
			"placeholder": "",
			"success": function() {
				var tmp = '';
			}
		}, 
		{ //quantity
			"placeholder": "",
			"cssclass": "quantity number"
		}, 
		{ //status
			"placeholder": "",
			"cssclass": "required"
		}, 
		{ //notes 
			"placeholder": "",
			"type": 'textarea',
			"submit":'Save changes'
		}
	]
};

var thisPage = {
	'init': function() {
		$("#formAddGeneralItemRow #invType").val("general");
		
		var aStatus = ["ACTIVE", "LOST", "STOLEN", "DISPOSED", "BROKEN"];
		$("form #status").autocomplete({source:aStatus});
		
		generalTable = $("#generalTable")
						.dataTable(oGenralTableOptions)
						.makeEditable(oGenralTableEditableOptions);
	}
}
