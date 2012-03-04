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
		
		
		
		/*
		$('table.display tbody tr').live('click', function () {
			var id = this.id;
			var index = jQuery.inArray(id, aSelected);
			
			if ( index === -1 ) {
				aSelected.push( id );
			} else {
				aSelected.splice( index, 1 );
			}
			
			$(this).toggleClass('row_selected');
		} );
		
		/*
		$('td', oTable.fnGetNodes()).editable( '/editableajax', {
			"tooltip": "Click to edit...",
			"submitdata": function(value, setting) {
				var column = oTable.fnGetPosition( this )[2];
				var colName = '';
				$.each(oTable.fnSettings().aoColumns, function(index) {
					if (this.iDataSort == column) {
						colName = this.mDataProp;
						return false;
					}
				});
				
				return {
					"invType": "general",
					"id": $(this).parent().attr('id'),
					"colName": colName
				};
			},
			"callback": function( sValue, y ) {
				var aPos = oTable.fnGetPosition( this );
				oTable.fnUpdate( sValue, aPos[0], aPos[1] );
				//oTable.fnDraw();
			}
		} );
		*/
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
	   { },//rowButton
	   { },//poNumber
	   { //description
		   "cssclass": "required"
	   }, 
	   { }, //decalNumber
	   { }, //propertyNumber
	   { //location
	   "cssclass": "required"
	   }, 
	   { }, //custodian
	   { //quantity
		   "cssclass": "quantity number"
	   }, 
	   { //status
		   "cssclass": "required"
		   /*
		   "type": "autocomplete",
		   "autocomplete": {
			 "url": "/autocomplete"   
		   }
		   */
	   }, 
	   { //notes 
		   "type": 'textarea',
		   "submit":'Save changes'
	   }
	]
	/*
	"tooltip": "Click to edit...",
	"submitdata": function(value, setting) {
		var column = oTable.fnGetPosition( this )[2];
		var colName = '';
		$.each(oTable.fnSettings().aoColumns, function(index) {
			if (this.iDataSort == column) {
				colName = this.mDataProp;
				return false;
			}
		});
		
		return {
			"invType": "general",
			"id": $(this).parent().attr('id'),
			"colName": colName
		};
	},
	"callback": function( sValue, y ) {
		var aPos = oTable.fnGetPosition( this );
		oTable.fnUpdate( sValue, aPos[0], aPos[1] );
		//oTable.fnDraw();
	}
	*/
};

var thisPage = {
	'init': function() {
		
		$("#formAddGeneralItemRow #invType").val("general");
		
		//$('#tabs').tabs({});
		
		/*
		$('#tabs').tabs( {
			
			"show": function(event, ui) {
				var oTable = $('div.dataTables_scrollBody>table.display', ui.panel).dataTable();
				if ( oTable.length > 0 ) {
					oTable.fnAdjustColumnSizing();
				}
			},
			
			"select": function(event, ui) {
				if(ui.index == 0) {
					$("#generalTab").css("display", "block");	
					$("#electronicsTab").css("display", "none");
					$("#keysTab").css("display", "none");
					return true;
					
				}
				else if (ui.index == 1) {
					$("#generalTab").css("display", "none");
					$("#electronicsTab").css("display", "block");
					$("#keysTab").css("display", "none");
					return true;
					
				}
				else if (ui.index == 2) {
					$("#generalTab").css("display", "none");
					$("#electronicsTab").css("display", "none");
					$("#keysTab").css("display", "block");
					return true;
				}
			}
		});
		
		
		$.editable.addInputType('autocomplete', {
	        element : $.editable.types.text.element,
	        plugin : function(settings, original) {
	            $('input', this).autocomplete(settings.autocomplete.url, {                                                 
	                dataType:'json',
	                parse : function(data) {    		                                                                                                        
	                    return $.map(data, function(item){
	                        return {
	                                data : item,
	                                value : item.Key,
	                                result: item.value                                                                                     
	                               }
	                    })
                    },
	                formatItem: function(row, i, n) {                                                        
                        return row.value;
                    },
	                mustMatch: false,
	                focus: function(event, ui) {                                                
		                $('#table.display tbody td[title]').val(ui.item.label);
		                return false;
		            }
                });                                        
	        }
	    });
		*/
		
		
		
		
		var aStatus = ["ACTIVE", "LOST", "STOLEN", "DISPOSED", "BROKEN"];
		$("form #status").autocomplete({source:aStatus});
		
		generalTable = $("#generalTable").dataTable(oGenralTableOptions).makeEditable(oGenralTableEditableOptions);
		
		/*
		if ( generalTable.length > 0 ) {
			generalTable.fnAdjustColumnSizing();
		}
		*/
		
	
		//tabs.tabs('select', "electronicsTab");
		
		//$('#generalTab').tabshow();		
	}
	





	/*
	'inventoryTypeChange': function() {
		var labels = $("#inventoryType label");
		labels.removeClass("ui-state-active");
		$(this).addClass("ui-state-active");
		
		var inventoryType = $(this).children(".ui-button-text").html();
		
		$(".inventoryTable").hide();
		
		switch (inventoryType) {
			case "Keys":
				thisPage.loadKeyTable();
				break;
				
			case "Electronics":
				thisPage.loadElectronicTable();
				break;
			
			default: // General
				thisPage.loadGeneralTable();
				break;
		}
	},
	'textEditDblClick': function() {
		var htmlDom = $('<span class="container"><input type="text" /></span>');
		
		htmlDom.children("input").val($(this).html());
		htmlDom.children("input").dblclick(function(event) {
			event.preventDefault();
			return false;
		});
		htmlDom.children("input").keyup(function(event) {
			var key = event.keyCode ? event.keyCode : event.charCode;
			if (key == 13) {
				var textEditDom = $(this).parent(".container").parent();
				textEditDom.html($(this).val());
				textEditDom.removeClass("selectedCell");
			}
		});
		htmlDom.children("input").focusout(function() {
			var textEditDom = $(this).parent(".container").parent();
			textEditDom.html($(this).val());
			textEditDom.removeClass("selectedCell");
		});
		var width = parseInt($(this).css("width").replace("px", ""));
		htmlDom.children("input").css("width", width + 50);
		$(this).html(htmlDom);
		$(this).addClass("selectedCell");
		$(this).children(".container").children("input").focus();
	},
	'loadKeyTable': function() {
		var keyList = new Array();
		keyList.push({
			'location': 'A-20',
			'description': 'Teacher Restroom',
			'id': '0001',
			'inStock': '5',
			'custodian': 'MCGROIN, HOLDEN',
			'issueDate': 'Jan 01, 2011',
			'returnDate': 'Jan 31, 2011',
			'status': 'Active',
			'notes': 'Can of awesomeness.',
			'lastEditUser': 'honky@maili.k12.hi.us',
			'lastEditDate': 'Jan 31, 2011'
		});		
		
		
		$("#keysTable .inventoryRow").remove();
		
		$.each(keyList, function(index) {
			this.index = index;
			thisPage.displayKeyRecord(this);
		});
		
		$("#keysTable .info .ui-icon").tooltip({
			delay: 500,
			effect: "slide",
			relative: true,
			position: "bottom left",
			offset: [10, 50]
		});
		
		$("#keysTable .textEdit").dblclick(thisPage.textEditDblClick);
		
		$("#keysTable").show();
	}, 
	'displayKeyRecord': function(keyInfo) {
		var html = '<tr class="inventoryRow">'
				 + 		'<td class="textEdit index">' + keyInfo.index + '</td>'
				 + 		'<td class="textEdit location">' + keyInfo.location + '</td>'
				 + 		'<td class="textEdit description">' + keyInfo.description + '</td>'
				 + 		'<td class="textEdit id">' + keyInfo.id + '</td>'
				 + 		'<td class="textEdit inStock">' + keyInfo.inStock + '</td>'
				 + 		'<td class="textEdit custodian">' + keyInfo.custodian + '</td>'
				 + 		'<td class="textEdit issueDate">' + keyInfo.issueDate + '</td>'
				 + 		'<td class="textEdit returnDate">' + keyInfo.returnDate + '</td>'
				 + 		'<td class="textEdit status">' + keyInfo.status + '</td>'
				 + 		'<td class="info">' 
				 + 			'<span class="ui-corner-all">'
				 + 				'<span class="ui-icon ui-icon-note"></span>'
				 + 				'<div class="tooltip">'
				 + 					'<div class="tooltip-content">'
				 +						keyInfo.notes
				 +					'</div>'
				 + 					'<div class="tooltip-arrow-border tooltip-arrow-border-top-right"></div>'
				 + 					'<div class="tooltip-arrow tooltip-arrow-top-right"></div>'
				 + 				'</div>'
				 + 			'</span>'
				 + 			'<span class="ui-corner-all">'
				 + 				'<span class="ui-icon ui-icon-person"></span>'
				 + 				'<div class="tooltip">'
				 + 					'<div class="tooltip-content">'
				 + 						'<table>'
				 + 							'<tr>'
				 + 								'<th class="user">User:</th>'
				 + 								'<td class="user">' + keyInfo.lastEditUser + '</td>'
				 + 							'</tr>'
				 + 							'<tr>'
				 + 								'<th class="date">Date:</th>'
				 + 								'<td class="date">' + keyInfo.lastEditDate + '</td>'
				 + 							'</tr>'
				 + 						'</table>'
				 +					'</div>'
				 + 					'<div class="tooltip-arrow-border tooltip-arrow-border-top-right"></div>'
				 + 					'<div class="tooltip-arrow tooltip-arrow-top-right"></div>'
				 + 				'</div>'
				 + 			'</span>'
				 + 		'</td>'
				 + '</tr>';
		$("#keysTable tbody").append(html);
	},
	'loadElectronicTable': function() {
		var electronicList = new Array();
		electronicList.push({
			'poNumber': 'PA12345',
			'poSentDate': 'Feb 02, 2011',
			'poRecieveDate': 'Feb 10, 2011',
			'description': 'Computer for id10t',
			'make': 'Apple',
			'model': 'iPad 2',
			'serialNumber': '99-0002222',
			'decalNumber': '257-9929',
			'propertyNumber': '2570123456789',
			'location': 'G-01',
			'custodian': 'HAUGE, DISA',
			'funder': 'eSIS',
			'status': 'ACTIVE',
			'notes': 'Can of awesomeness.',
			'lastEditUser': 'honky@maili.k12.hi.us',
			'lastEditDate': 'Jan 31, 2011'
		});		
		
		
		$("#electronicsTable .inventoryRow").remove();
		
		$.each(electronicList, function(index) {
			this.index = index;
			thisPage.displayElectronicRecord(this);
		});
		
		$("#electronicsTable .info .ui-icon").tooltip({
			delay: 500,
			effect: "slide",
			relative: true,
			position: "bottom left",
			offset: [10, 50]
		});
		
		$("#electronicsTable .poNumber .ui-icon").tooltip({
			delay: 500,
			effect: "slide",
			relative: true,
			position: "bottom right",
			offset: [10, -50]
		});
		
		$("#electronicsTable .textEdit").dblclick(thisPage.textEditDblClick);
		
		$("#electronicsTable").show();
	},
	'displayElectronicRecord': function(electronicInfo) {
		var html = '<tr class="inventoryRow">'
				 + 		'<td class="textEdit index">' + electronicInfo.index + '</td>'
			 	 + 		'<td class="poNumber">'
				 +  		electronicInfo.poNumber  
				 + 			'<span class="ui-corner-all">'
				 + 				'<span class="ui-icon ui-icon-info"></span>'
				 + 				'<div class="tooltip">'
				 + 					'<div class="tooltip-content">'
				 + 						'<table>'
				 + 							'<tr>'
				 + 								'<th class="user">Date:</th>'
				 + 								'<td class="user">' + electronicInfo.poSentDate + '</td>'
				 + 							'</tr>'
				 + 							'<tr>'
				 + 								'<th class="date">Recieve Date:</th>'
				 + 								'<td class="date">' + electronicInfo.poRecieveDate + '</td>'
				 + 							'</tr>'
				 + 						'</table>'
				 +					'</div>'
				 + 					'<div class="tooltip-arrow-border tooltip-arrow-border-top-left"></div>'
				 + 					'<div class="tooltip-arrow tooltip-arrow-top-left"></div>'
				 + 				'</div>'
				 + 			'</span>'
				 + 		'</td>'
				 + 		'<td class="textEdit description">' + electronicInfo.description + '</td>'
				 + 		'<td class="textEdit make">' + electronicInfo.make + '</td>'
				 + 		'<td class="textEdit model">' + electronicInfo.model + '</td>'
				 + 		'<td class="textEdit serialNumber">' + electronicInfo.serialNumber + '</td>'
				 + 		'<td class="textEdit decalNumber">' + electronicInfo.decalNumber + '</td>'
				 + 		'<td class="textEdit propertyNumber">' + electronicInfo.propertyNumber + '</td>'
				 + 		'<td class="textEdit location">' + electronicInfo.location + '</td>'
				 + 		'<td class="textEdit custodian">' + electronicInfo.custodian + '</td>'
				 + 		'<td class="textEdit funder">' + electronicInfo.funder + '</td>'
				 + 		'<td class="textEdit status">' + electronicInfo.status + '</td>'
				 + 		'<td class="info">' 
				 + 			'<span class="ui-corner-all">'
				 + 				'<span class="ui-icon ui-icon-note"></span>'
				 + 				'<div class="tooltip">'
				 + 					'<div class="tooltip-content">'
				 +						electronicInfo.notes
				 +					'</div>'
				 + 					'<div class="tooltip-arrow-border tooltip-arrow-border-top-right"></div>'
				 + 					'<div class="tooltip-arrow tooltip-arrow-top-right"></div>'
				 + 				'</div>'
				 + 			'</span>'
				 + 			'<span class="ui-corner-all">'
				 + 				'<span class="ui-icon ui-icon-person"></span>'
				 + 				'<div class="tooltip">'
				 + 					'<div class="tooltip-content">'
				 + 						'<table>'
				 + 							'<tr>'
				 + 								'<th class="user">User:</th>'
				 + 								'<td class="user">' + electronicInfo.lastEditUser + '</td>'
				 + 							'</tr>'
				 + 							'<tr>'
				 + 								'<th class="date">Date:</th>'
				 + 								'<td class="date">' + electronicInfo.lastEditDate + '</td>'
				 + 							'</tr>'
				 + 						'</table>'
				 +					'</div>'
				 + 					'<div class="tooltip-arrow-border tooltip-arrow-border-top-right"></div>'
				 + 					'<div class="tooltip-arrow tooltip-arrow-top-right"></div>'
				 + 				'</div>'
				 + 			'</span>'
				 + 		'</td>'
				 + '</tr>';
		$("#electronicsTable tbody").append(html);
	},
	'loadGeneralTable': function() {
		var generalList = new Array();
		generalList.push({
			'poNumber': 'PX98765',
			'poSentDate': 'Feb 02, 2011',
			'poRecieveDate': 'Feb 10, 2011',
			'description': 'Ka noho',
			'decalNumber': '257-0001',
			'propertyNumber': '2570987654321',
			'location': 'G-01',
			'custodian': 'HAUGE, DISA',
			'quantity': '1',
			'status': 'ACTIVE',
			'notes': "A'ole maopopo.",
			'lastEditDate': 'Jan 31, 2011',
			'lastEditUser': 'kanaka@maili.k12.hi.us'
		});		
		
		
		$("#generalTable .inventoryRow").remove();
		
		$.each(generalList, function(index) {
			this.index = index;
			thisPage.displayGeneralRecord(this);
		});
		
		$("#generalTable .info .ui-icon").tooltip({
			delay: 500,
			effect: "slide",
			relative: true,
			position: "bottom left",
			offset: [10, 50]
		});
		
		$("#generalTable .poNumber .ui-icon").tooltip({
			delay: 500,
			effect: "slide",
			relative: true,
			position: "bottom right",
			offset: [10, -50]
		});
		
		
		$("#generalTable .textEdit").dblclick(thisPage.textEditDblClick);
		
		$("#generalTable").show();
	},
	'displayGeneralRecord': function(generalInfo) {
		var html = '<tr class="inventoryRow">'
				 //+ 		'<td class="textEdit index">' + 0 + '</td>'
				 + 		'<td class="poNumber">'
				 +			generalInfo.poNumber  
				 + 			'<span class="ui-corner-all">'
				 + 				'<span class="ui-icon ui-icon-info"></span>'
				 + 				'<div class="tooltip">'
				 + 					'<div class="tooltip-content">'
				 + 						'<table>'
				 + 							'<tr>'
				 + 								'<th class="user">Date:</th>'
				 + 								'<td class="user">' + generalInfo.poSentDate + '</td>'
				 + 							'</tr>'
				 + 							'<tr>'
				 + 								'<th class="date">Recieve Date:</th>'
				 + 								'<td class="date">' + generalInfo.poRecieveDate + '</td>'
				 + 							'</tr>'
				 + 						'</table>'
				 +					'</div>'
				 + 					'<div class="tooltip-arrow-border tooltip-arrow-border-top-left"></div>'
				 + 					'<div class="tooltip-arrow tooltip-arrow-top-left"></div>'
				 + 				'</div>'
				 + 			'</span>'
				 + 		'</td>'
				 + 		'<td class="textEdit description">' + generalInfo.description + '</td>'
				 + 		'<td class="textEdit decalNumber">' + generalInfo.decalNumber + '</td>'
				 + 		'<td class="textEdit propertyNumber">' + generalInfo.propertyNumber + '</td>'
				 + 		'<td class="textEdit location">' + generalInfo.location + '</td>'
				 + 		'<td class="textEdit custodian">' + generalInfo.custodian + '</td>'
				 + 		'<td class="textEdit quantity">' + generalInfo.quantity + '</td>'
				 + 		'<td class="textEdit status">' + generalInfo.status + '</td>'
				 + 		'<td class="info">' 
				 + 			'<span class="ui-corner-all">'
				 + 				'<span class="ui-icon ui-icon-note"></span>'
				 + 				'<div class="tooltip">'
				 + 					'<div class="tooltip-content">'
				 +						generalInfo.notes
				 +					'</div>'
				 + 					'<div class="tooltip-arrow-border tooltip-arrow-border-top-right"></div>'
				 + 					'<div class="tooltip-arrow tooltip-arrow-top-right"></div>'
				 + 				'</div>'
				 + 			'</span>'
				 + 			'<span class="ui-corner-all">'
				 + 				'<span class="ui-icon ui-icon-person"></span>'
				 + 				'<div class="tooltip">'
				 + 					'<div class="tooltip-content">'
				 + 						'<table>'
				 + 							'<tr>'
				 + 								'<th class="user">User:</th>'
				 + 								'<td class="user">' + generalInfo.lastEditUser + '</td>'
				 + 							'</tr>'
				 + 							'<tr>'
				 + 								'<th class="date">Date:</th>'
				 + 								'<td class="date">' + generalInfo.lastEditDate + '</td>'
				 + 							'</tr>'
				 + 						'</table>'
				 +					'</div>'
				 + 					'<div class="tooltip-arrow-border tooltip-arrow-border-top-right"></div>'
				 + 					'<div class="tooltip-arrow tooltip-arrow-top-right"></div>'
				 + 				'</div>'
				 + 			'</span>'
				 + 		'</td>'
				 + '</tr>';
		//$("#generalTable tbody").append(html);
		$("#generalTable").dataTable().fnAddData([0, generalInfo.poNumber, 
		                                          generalInfo.description, generalInfo.decalNumber,
		                                          generalInfo.propertyNumber, generalInfo.location,
		                                          generalInfo.custodian, generalInfo.quantity,
		                                          generalInfo.status, generalInfo.notes]);
		
	}
	*/
}
