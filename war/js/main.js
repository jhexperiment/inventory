$(document).ready(function() {
  oMain.fnInit();
  
  
  $.fn.suggestable = function(aOptions) {
    var fnSuggestPopup = function(event) {
      var aSavedOptions = $(this).data('aOptions');
      var aData = {
        'sSearch': $(this).val(),
        'sType': aSavedOptions.sType,
        'sHash': aSavedOptions.sHash
      };
      $.ajax({
        "dataType": 'json',
        "type": "POST",
        "url": '/suggest',
        "data": aData,
        "success": function(aData, sTextStatus, oJqXHR) {
          var oElement = $('#suggest-' + aData.sHash);
          var oUl = oElement.parent().find("ul");
          
          if (oUl.length == 0) {
            var oDrowdownIcon = oElement.parents('.info-item').find(".icon");
            var iHeight = oElement.height() + 10;
            var iWidth = oElement.width() + oDrowdownIcon.width();
            oUl = $('<ul class="suggest-popup ui-widget-content"></ul>');
            oUl.css("top", iHeight);
            oUl.width(iWidth);
            oUl.data("sHash", aData.sHash);
            oElement.parent().append(oUl);
          }
          else {
            oUl.empty();
          }
          
          if (aData.aRecordList.length == 0) {
            var oLi = $('<li class="no-result">No Results Found</li>');
            oUl.append(oLi);
          }
          else {
            $.each(aData.aRecordList, function() {
              var oLi = $('<li>' + this.name + '</li>');
              oLi.click(function() {
                var oUl = $(this).parents("ul");
                var sHash = oUl.data("sHash");
                $("#suggest-" + sHash).val($(this).html());
                oUl.remove();
                //$("#suggest-" + sHash).removeAttr("id");
              });
              oUl.append(oLi);
            });
          }
        }
      });
    };
    var sOldHash = "";
    $.each(this, function() {
      var sHash = $.md5(new Date().getTime());
      while($("#suggest-" + sHash).length != 0) {
        sHash = $.md5(new Date().getTime());
      }
      sOldHash = sHash;
      
      $(this).attr("id", "suggest-" + sHash);
      $(this).parent().css("position", "relative");
      aOptions.sHash = sHash;
      aOptions.sDialog = $(this).parents(".dialog").attr("id");
      $(this).data('aOptions', $.extend({}, aOptions));
      
      
      $(this).keyup(fnSuggestPopup);
      $(this).click(fnSuggestPopup);
    });
    
  };
  
  
  thisPage.init();
});

var buttonSet;
var inventoryTable;
var thisPage;

buttonSet = {
  'init': function() {
    $("#inventory-type #general").click(buttonSet.generalClick);
    $("#inventory-type #electronics").click(buttonSet.electronicsClick);
    $("#inventory-type #keys").click(buttonSet.keysClick);
  },
  'generalClick': function() {
    $("#inventory-type .ui-button").removeClass("ui-state-active");
    $(this).addClass("ui-state-active");
    $("#general-tab").removeClass('state-hide');
    $("#electronics-tab").addClass('state-hide');
    $("#keys-tab").addClass('state-hide');
    inventoryTable.getList();
  },
  'electronicsClick': function() {
    $("#inventory-type .ui-button").removeClass("ui-state-active");
    $(this).addClass("ui-state-active");
    $("#general-tab").addClass('state-hide');
    $("#electronics-tab").removeClass('state-hide');
    $("#keys-tab").addClass('state-hide');
    inventoryTable.getList();
  },
  'keysClick': function() {
    $("#inventory-type .ui-button").removeClass("ui-state-active");
    $(this).addClass("ui-state-active");
    $("#general-tab").addClass('state-hide');
    $("#electronics-tab").addClass('state-hide');
    $("#keys-tab").removeClass('state-hide');
    inventoryTable.getList();
  }
}

inventoryTable = {
  'aSortList': [],
  'init': function() {
    $("body").click(function() {
      $(".suggest-popup").remove();
    });
    
    $("#app-nav-bar #more-nav-item").click(function() {
      menuItemList = new Array();
      menuItemList.push({
        'name': 'CSV Import ' + $("#inventory-type .ui-button.ui-state-active .ui-button-text").html(), 
        'action': thisPage.csvImport,
        'data': {}
      });
      menuItemList.push({
        'name': 'CSV Export ' + $("#inventory-type .ui-button.ui-state-active .ui-button-text").html(), 
        'action': thisPage.csvExport,
        'data': {}
      });

        thisPage.displayPopupMenu(this, menuItemList);
    });
    
    
    $("#result-container .label #clear-button").click(function() {
      $("#result-container table#result").html("");
    });
    
    
    $(".dialog #location .suggestable").suggestable({'sType': 'location'});
    $(".dialog #custodian .suggestable").suggestable({'sType': 'custodian'});
    $(".dialog #status .suggestable").suggestable({'sType': 'status'});
    
    $(".dialog .datepicker").datepicker();
    
    $(".dialog .info-item .icon").click(function() {
       var oInfoItem = $(this).parents(".info-item");
       oInfoItem.find(".input input").click();
    });
    
    
    $("#search-container #delete-button").click(function() {
      if ($(this).hasClass('ui-state-disabled')) {
        return false;
      }
      
      var aIdList = [];
      $.each($(".inventory-table .data-row #info-btn input[type='checkbox']:checked"), function() {
        var sId = $(this).parents(".data-row").find("#id").html();
        aIdList.push(sId);
      });
      
      var aData = {
        'sInvType': $("#inventory-type label.ui-state-active").attr("id"),
        'aIdList': aIdList
      };
      $.ajax({
        "dataType": 'json',
        "type": "POST",
        "url": '/delete',
        "data": aData,
        "success": function(aData, sTextStatus, oJqXHR) {
          var oCheckboxList = $(".inventory-table td#info-btn input[type='checkbox']");
          $.each(oCheckboxList, function(iIndex) {
            var oDataRow = $(this).parents(".data-row");
            if ($.inArray(oDataRow.find("#id").html(), aData.aIdList) != -1) {
              //remove extra-info-row
              oDataRow.next().remove();
              // remove data-row;
              oDataRow.remove();
            }
            /*
            if ((iIndex + 1) >= aData.aIdList.length) {
              return false;
            }
            */
            
            $('.inventory-table th#info-btn input[type="checkbox"]').removeAttr("checked");
          });
          
          $("#search-container #delete-button").addClass('ui-state-disabled').removeClass("ui-state-default");
        }
      });
    });
    
    $("#search-container #search-query").val("");
    
    $("#search-container #search-button").click(function() {
      inventoryTable.getList();
    });
    
    $("#search-container #search-query").keyup(function(e) {
      switch (e.keyCode) {
        case 13: // enter
          inventoryTable.getList();
          break;
      }
    });
    
    $(".inventory-table th").disableSelection();
    
    $(".inventory-table th#info-btn input[type='checkbox']").click(function() {
      var oInventoryTableList = $(".inventory-table");
      var oTable = null;
      $.each(oInventoryTableList, function() {
        if ( ! $(this).parents(".ui-tabs-panel").hasClass("state-hide")) {
          oTable = $(this);
          return false;
        }
      });
      
      var oCheckboxList = oTable.find("td#info-btn input[type='checkbox']");
      
      if ($(this).attr("checked")) {
        oCheckboxList.attr("checked", true);
      }
      else {
        oCheckboxList.attr("checked", false);
      }
      oCheckboxList.change();
    });
    
    $(".inventory-table th.sortable").click(function(event) {
      // update this header's sort icon
      var oUiIcon = $(this).find(".ui-icon");
      if (oUiIcon.hasClass("ui-icon-carat-2-n-s")) {
        oUiIcon.removeClass("ui-icon-carat-2-n-s");
        oUiIcon.addClass("ui-icon-carat-1-n");
      }
      else if (oUiIcon.hasClass("ui-icon-carat-1-n")) {
        oUiIcon.removeClass("ui-icon-carat-1-n");
        oUiIcon.addClass("ui-icon-carat-1-s");
      }
      else if (oUiIcon.hasClass("ui-icon-carat-1-s")) {
        oUiIcon.removeClass("ui-icon-carat-1-s");
        oUiIcon.addClass("ui-icon-carat-2-n-s");
      }
      
      // get all sortable headers
      var aHeaderList = $(".inventory-table th.sortable");
      var iThisHeaderIndex = $.inArray(this, aHeaderList);
      // remove this header from list
      aHeaderList.splice(iThisHeaderIndex, 1);
      
      if (event.shiftKey) {
        // if multi column sort
        var iSortIndex = $.inArray(this, inventoryTable.aSortList);
        if (iSortIndex == -1) {
          inventoryTable.aSortList.push(this);
        }
      }
      else {
        // if single column sort
        // set other header's icon back to default
        aHeaderList.find(".ui-icon")
          .removeClass("ui-icon-carat-1-s ui-icon-carat-1-n")
          .addClass("ui-icon-carat-2-n-s");
        // set aSortList to only this header
        inventoryTable.aSortList = [this];
      }
      
      // if not sorting on this column, remove from aSortList
      if (oUiIcon.hasClass("ui-icon-carat-2-n-s")) {
        var iSortIndex = $.inArray(this, inventoryTable.aSortList);
        inventoryTable.aSortList.splice(iSortIndex, 1);
      }
      
      
      // reload table with sorting
      var oGeneralTableTdList = $("#general-table tbody");
      inventoryTable.getList();
    });
    
    $("#search-container #add-button").click(function() {
      var sInventoryType = $("#inventory-type .ui-state-active").attr("id");
      switch (sInventoryType) {
        case "general":
          $("#general-item-dialog").dialog("open");
          break;
        case "electronics":
          $("#electronics-item-dialog").dialog("open");
          break;
        case "keys":
          $("#keys-item-dialog").dialog("open");
          break;
        
      }
      
    });
    
    $("#general-item-dialog").dialog({
      'minWidth': 450,
      'autoOpen': false,
      'draggable': true,
      'modal': true,
      'buttons': {
        'Add': function() {
          var sErrMsg = '';
          var sId = '';
          $.each($("#general-item-dialog .required"), function() {
            if ($(this).val() == "") {
              var oInfoItem = $(this).parents(".info-item");
              var oInput = $(this).parents(".input");
              oInput.addClass("state-error");
              $(this).keypress(function(event) {
                $(this).parents(".input").removeClass("state-error");
              });
              
              sId = thisPage.capitalizeMe(oInfoItem.attr("id").replace("-", " "));
              sErrMsg += sId + " is required.\n";
            }
          });
          
          $.each($("#general-item-dialog .number"), function() {
            if ($(this).val() == "" && $(this).hasClass("required")) {
              if (! isNaN($(this).val())) {
                $(this).parents(".input").addClass("state-error");
                sId = thisPage.capitalizeMe($(this).attr("id").replace("-", " "));
                sErrMsg += sId + " must be a number.\n";
              }
            }
          });
          
          if ($("#general-item-dialog .state-error").length != 0) {
            alert(sErrMsg);
          }
          else {
            var oDialog = $("#general-item-dialog");
            var aData = {
              'sInvType': $("#inventory-type label.ui-state-active").attr("id"),
              'sDescription': oDialog.find("#description input").val(),
              'sPoNumber': oDialog.find("#po-number input").val(),
              'sPoDate': oDialog.find("#po-date input").val(),
              'sPoRecieveDate': oDialog.find("#po-recieve-date input").val(),
              'sDecalNumber': oDialog.find("#decal-number input").val(),
              'sPropertyNumber': oDialog.find("#property-number input").val(),
              'sLocation': oDialog.find("#location input").val(),
              'sCustodian': oDialog.find("#custodian input").val(),
              'iQuantity': oDialog.find("#quantity input").val(),
              'sStatus': oDialog.find("#status input").val(),
              'sNotes': oDialog.find("#notes textarea").val()
            };
            $.ajax({
              "dataType": 'json',
              "type": "POST",
              "url": '/add',
              "data": aData,
              "success": function(aData, sTextStatus, oJqXHR) {
                inventoryTable.getList();
                oDialog.dialog("close");
              }
            });
          }
        },
        'Cancel': function() {
          $("#general-item-dialog").dialog("close");
        }
      },
      'open': function() {
        $("#general-item-dialog .state-error").removeClass("state-error");
      }
    });
    
    $("#electronics-item-dialog").dialog({
      'minWidth': 450,
      'autoOpen': false,
      'draggable': true,
      'modal': true,
      'buttons': {
        'Add': function() {
          var sErrMsg = '';
          var sId = '';
          $.each($("#electronics-item-dialog .required"), function() {
            if ($(this).val() == "") {
              var oInfoItem = $(this).parents(".info-item");
              var oInput = $(this).parents(".input");
              oInput.addClass("state-error");
              $(this).keypress(function(event) {
                $(this).parents(".input").removeClass("state-error");
              });
              
              sId = thisPage.capitalizeMe(oInfoItem.attr("id").replace("-", " "));
              sErrMsg += sId + " is required.\n";
            }
          });
          
          $.each($("#electronics-item-dialog .number"), function() {
            if ($(this).val() == "" && $(this).hasClass("required")) {
              if (! isNaN($(this).val())) {
                $(this).parents(".input").addClass("state-error");
                sId = thisPage.capitalizeMe($(this).attr("id").replace("-", " "));
                sErrMsg += sId + " must be a number.\n";
              }
            }
          });
          
          if ($("#electronics-item-dialog .state-error").length != 0) {
            alert(sErrMsg);
          }
          else {
            var oDialog = $("#electronics-item-dialog");
            var aData = {
              'sInvType': $("#inventory-type label.ui-state-active").attr("id"),
              'sDescription': oDialog.find("#description input").val(),
              'sType': oDialog.find("#type input").val(),
              'sMake': oDialog.find("#make input").val(),
              'sModel': oDialog.find("#model input").val(),
              'sSerialNumber': oDialog.find("#serial-number input").val(),
              'sPoNumber': oDialog.find("#po-number input").val(),
              'sPoDate': oDialog.find("#po-date input").val(),
              'sPoRecieveDate': oDialog.find("#po-recieve-date input").val(),
              'sDecalNumber': oDialog.find("#decal-number input").val(),
              'sPropertyNumber': oDialog.find("#property-number input").val(),
              'sLocation': oDialog.find("#location input").val(),
              'sCustodian': oDialog.find("#custodian input").val(),
              'sFunder': oDialog.find("#funder input").val(),
              'sStatus': oDialog.find("#status input").val(),
              'sNotes': oDialog.find("#notes textarea").val()
            };
            $.ajax({
              "dataType": 'json',
              "type": "POST",
              "url": '/add',
              "data": aData,
              "success": function(aData, sTextStatus, oJqXHR) {
                inventoryTable.getList();
                oDialog.dialog("close");
              }
            });
          }
        },
        'Cancel': function() {
          $("#electronics-item-dialog").dialog("close");
        }
      },
      'open': function() {
        $("#electronics-item-dialog .state-error").removeClass("state-error");
      }
    });
    
    $("#keys-item-dialog").dialog({
      'minWidth': 450,
      'autoOpen': false,
      'draggable': true,
      'modal': true,
      'buttons': {
        'Add': function() {
          var sErrMsg = '';
          var sId = '';
          $.each($("#keys-item-dialog .required"), function() {
            if ($(this).val() == "") {
              var oInfoItem = $(this).parents(".info-item");
              var oInput = $(this).parents(".input");
              oInput.addClass("state-error");
              $(this).keypress(function(event) {
                $(this).parents(".input").removeClass("state-error");
              });
              
              sId = thisPage.capitalizeMe(oInfoItem.attr("id").replace("-", " "));
              sErrMsg += sId + " is required.\n";
            }
          });
          
          $.each($("#keys-item-dialog .number"), function() {
            if ($(this).val() == "" && $(this).hasClass("required")) {
              if (! isNaN($(this).val())) {
                $(this).parents(".input").addClass("state-error");
                sId = thisPage.capitalizeMe($(this).attr("id").replace("-", " "));
                sErrMsg += sId + " must be a number.\n";
              }
            }
          });
          
          if ($("#keys-item-dialog .state-error").length != 0) {
            alert(sErrMsg);
          }
          else {
            var oDialog = $("#keys-item-dialog");
            var aData = {
              'sInvType': $("#inventory-type label.ui-state-active").attr("id"),
              'sLocation': oDialog.find("#location input").val(),
              'sDescription': oDialog.find("#description input").val(),
              'sKeyId': oDialog.find("#key-id input").val(),
              'iInStock': oDialog.find("#in-stock input").is(":checked") ? 1 : 0,
              'sCustodian': oDialog.find("#custodian input").val(),
              'sIssuedDate': oDialog.find("#issued-date input").val(),
              'sReturnedDate': oDialog.find("#returned-date input").val(),
              'sStatus': oDialog.find("#status input").val(),
              'sNotes': oDialog.find("#notes textarea").val()
            };
            $.ajax({
              "dataType": 'json',
              "type": "POST",
              "url": '/add',
              "data": aData,
              "success": function(aData, sTextStatus, oJqXHR) {
                inventoryTable.getList();
                oDialog.dialog("close");
              }
            });
          }
        },
        'Cancel': function() {
          $("#keys-item-dialog").dialog("close");
        }
      },
      'open': function() {
        $("#keys-item-dialog .state-error").removeClass("state-error");
      }
    });
  
    $("#search-container .show-count").change(function() {
      inventoryTable.getList();
    });
    
    $("#paginate .ui-button").click(inventoryTable.fnPaginateClick);
  },
  'setEvents': function() {
    $(".inventory-table .editable").dblclick(function() {
      
      if ($(this).data('isEditing') != true) { 
        var oHtmlDom;
        
        $(this).data('isEditing', true);
        var sCurValue = $(this).html();
        $(this).data('sOrigValue', sCurValue);
        
        if ($(this).attr("id") == "notes") {
          $(".inventoryTable td#notes .cancel-btn").click();
          oHtmlDom = inventoryTable.editTextArea(sCurValue);
        }
        else {
          oHtmlDom = inventoryTable.editTextInput(sCurValue);
          
        }
        
        $(this).html(oHtmlDom);
        oHtmlDom.focus();
      }
    });

    
    $(".inventory-table #info-btn div").click(function() {
      var oIconElement = $(this).find("span"); 
      if (oIconElement.hasClass("ui-icon-circle-arrow-s")) {
        $(this).parents(".data-row").next(".extra-info-row").removeClass('state-hide');
        oIconElement.removeClass("ui-icon-circle-arrow-s");
        oIconElement.addClass("ui-icon-circle-arrow-n");
      }
      else {
        $(this).parents(".data-row").next(".extra-info-row").addClass('state-hide');
        oIconElement.removeClass("ui-icon-circle-arrow-n");
        oIconElement.addClass("ui-icon-circle-arrow-s");
      }
      
    });
    
    $(".inventory-table td#info-btn input[type='checkbox']").change(function() {
      var oDeleteButton = $("#search-container #delete-button");
      if ($(this).attr("checked")) {
        $(this).parents(".data-row").addClass("row-selected");        
        oDeleteButton.removeClass("ui-state-disabled").addClass("ui-state-default");
      }
      else {
        $(this).parents(".data-row").removeClass("row-selected");  
      }
      
      // disable delete button if no checkboxes checked
      if ($(".inventory-table td#info-btn input[type='checkbox']:checked").length == 0) {
        oDeleteButton.removeClass("ui-state-default").addClass("ui-state-disabled");
      }
      
      
    });
    
    $(".inventory-table th#info-btn input[type='checkbox']").removeAttr("checked");
  },
  'readTable': function(sTableName) {
    $.each($("#" + sTableName + "-table tr.data-row"), function() {
      var aData = {};
      aData['id'] = $(this).find("id").html();
      aData['po-number'] = $(this).find("po-number").html();
      aData['description'] = $(this).find("description").html();
      aData['decal-number'] = $(this).find("decal-number").html();
      aData['property-number'] = $(this).find("property-number").html();
      aData['location'] = $(this).find("location").html();
      aData['custodian'] = $(this).find("custodian").html();
      aData['quantity'] = $(this).find("quantity").html();
      aData['status'] = $(this).find("status").html();
      aData['notes'] = $(this).find("notes").html();
      
      aData['po-date'] = $(this).next().find("po-date").html();
      aData['id'] = $(this).next().find("id").html();
      aData['id'] = $(this).next().find("id").html();
      aData['id'] = $(this).next().find("id").html();
      aData['id'] = $(this).next().find("id").html();
      
    });
  },
  'saveCell': function(sNewValue, oElement) {
    var iId;
    var sColumnName;
    var sHash = $.md5(new Date().getTime());
    oElement.parents(".editable").addClass(sHash);
    if (oElement.parent().get(0).tagName == "SPAN") {
      iId = parseInt(oElement.parents("tr").prev("tr").find("#id").html());
      sColumnName = oElement.parents(".editable").attr("id");
    }
    else {
      // should be a TD element
      iId = parseInt(oElement.parents("tr").find("#id").html());
      sColumnName = oElement.parents("td").attr("id");
    }
    
    var aData = {
      'sInvType': $("#inventory-type label.ui-state-active").attr("id"),
      'sColumnName': sColumnName,
      'sNewValue': sNewValue,
      'iId': iId,
      'sHash': sHash
    };
    
    oElement.parents("td").html(aData.sNewValue);
    
    $.ajax({
      "dataType": 'json',
      "type": "POST",
      "url": '/update',
      "data": aData,
      "success": function(aData, sTextStatus, oJqXHR) {
        if (aData.bError) {
          alert('Error saving cell. ' + aData.sErrorMsg);
        }
        else {
          oElement = $("." + aData.sHash);
          oElement.data('isEditing', false);
          var sId = oElement.attr('id');
          if (sId == 'po-date' || sId == 'po-recieve-date') {
            var oDate = new Date(aData.sNewValue);
            aData.sNewValue = $.datepicker.formatDate('mm/dd/yy', oDate);
          }
          oElement.html(aData.sNewValue);
          var oLastEditDate = new Date(aData.sLastEditDate);
          var oTr = oElement.parents("tr");
          if (oTr.hasClass("data-row")) {
            oTr = oTr.next("tr");
          }
          oTr.find("#last-edit-date").html($.datepicker.formatDate('mm/dd/yy', oLastEditDate));
          oTr.find("#last-edit-user").html(aData.sLastEditUser);
          oElement.removeClass(aData.sHash);
        }
      }
    });
  },
  'resetCell': function(oTdElement) {
    oTdElement.data('isEditing', false);
    oTdElement.html(oTdElement.data("sOrigValue"));
  },
  'editTextArea': function (sCurValue) {
    var sHtml = '';
    var oHtmlDom;
    
    sHtml = 
      '<div id="note-editor">' + 
        '<textarea>' + sCurValue + '</textarea>' + 
        '<button class="save-btn ui-button ui-widget ui-state-default ui-corner-all">Save</button>' +
        '<button class="cancel-btn ui-button ui-widget ui-state-default ui-corner-all">Cancel</button>' +
      '</div>';
    oHtmlDom = $(sHtml);
    
    oHtmlDom.find(".save-btn").click(function(){
      var sNewValue = $(this).parent("div#note-editor").find("textarea").val();
      inventoryTable.saveCell(sNewValue, $(this));
    });
    
    oHtmlDom.find(".cancel-btn").click(function(){
      inventoryTable.resetCell($(this).parents("td"));
    });
    
    
    oHtmlDom.keyup(function(e) {
      switch (e.keyCode) {
        case 27: // esc
          $(this).find(".cancel-btn").click();
          break;
      }
    });
    return oHtmlDom;
  },
  'editTextInput': function(sCurValue) {
    var sHtml = '';
    var oHtmlDom;
    
    sHtml = '<input type="text" value="' + sCurValue + '" />';
    oHtmlDom = $(sHtml);
    
    oHtmlDom.blur(function() {
      inventoryTable.resetCell($(this).parent());
    });
    
    oHtmlDom.keyup(function(e) {
      switch (e.keyCode) {
        case 13: // enter
          inventoryTable.saveCell($(this).val(), $(this));
          break;
        case 27: // esc
          $(this).blur();
          break;
      }
    });
    return oHtmlDom;
  },
  'fnPaginateClick': function() {
    var oPaginateNumbers = $("#paginate span");
    var iSelectedPage = parseInt(oPaginateNumbers.find(".ui-state-selected").html());
    
    if ($(this).hasClass("ui-state-disabled")) {
      return false;
    }
    else if ($(this).hasClass("first")) {
      inventoryTable.getList(1);
    }
    else if ($(this).hasClass("previous")) {
      inventoryTable.getList(iSelectedPage - 1);
    }
    else if ($(this).hasClass("next")) {
      inventoryTable.getList(iSelectedPage + 1);
    }
    else if ($(this).hasClass("last")) {
      var sPageNum = $.trim(oPaginateNumbers.find(".ui-button:last-child").html());
      inventoryTable.getList(parseInt(sPageNum));
    }
    else if ($(this).hasClass("fg-button")) {
      var sPageNum = $.trim($(this).html());
      inventoryTable.getList(parseInt(sPageNum));
    }
        
      
  },
  'getList': function(iPage) {
    
    
    var aSortList = [];
    $.each(inventoryTable.aSortList, function() {
      var oUiIcon = $(this).find(".ui-icon");
      var sSortDirection;
      if (oUiIcon.hasClass("ui-icon-carat-1-n")) {
        sSortDirection = "ASC"; 
      }
      else if (oUiIcon.hasClass("ui-icon-carat-1-s")) {
        sSortDirection = "DESC"; 
      }
      aSortList.push($(this).find(".server-sort-name").val() + ' ' + sSortDirection);
    });
    
    var oModal = $("#modal");
    oModal.addClass('state-show');
    oModal.find(".message-box .message").html("Retrieving list...");
    
    var oDisplayInfo = $("#display-info");
    var sDisplayStart = oDisplayInfo.find(".start").html();
    
    var iDisplayLength = parseInt($("#search-container .show-count select").val()); 
    var iDisplayStart = (sDisplayStart == "??") ? 1 : parseInt($.trim(sDisplayStart));
    if ($.fnIsEmpty(iPage)) {
      iPage = 0;
    }
    else {
      iPage -= 1;
    }
    iDisplayStart = (iPage * iDisplayLength) + 1;
    
    var aData = {
      'sType': $("#inventory-type .ui-state-active").attr("id"),
      'aSortList': aSortList,
      'sSearch': $("#search-container #search-query").val(),
      'iDisplayLength': iDisplayLength,
      'iDisplayStart': iDisplayStart,
      'iSelectedPage': iPage,
    };
    $.ajax( {
      "dataType": 'json',
      "type": "GET",
      "url": '/list',
      "data": aData,
      "success": function(aData, sTextStatus, oJqXHR) {
        var oModal = $("#modal");
        oModal.find(".message-box .message").html("Rendering list....");
        
        var oDisplayInfo = $("#display-info");
        
        $("#search-container .record-count").html("Total: " + aData.iTotal);
        oDisplayInfo.find(".start").html(aData.iDisplayStart);
        oDisplayInfo.find(".length").html(aData.iDisplayEnd);
        oDisplayInfo.find(".total").html(aData.iTotal);
        
        
        var oPaginate = $("#paginate");
        oPaginate.find(".ui-button").addClass('ui-state-disabled');
        
        var oPaginateNumberList = oPaginate.find("span").empty();
        for (i = 1; i <= aData.iPages; i++) {
          var sCss = '';
          if (i == aData.iSelectedPage) {
            sCss = 'ui-state-disabled ui-state-selected';
          }
          
          var oHtml = $('<a class="fg-button ui-button ui-state-default ' + sCss + '" tabindex="0">' + i + '</a>');
          oHtml.click(inventoryTable.fnPaginateClick);
          oPaginateNumberList.append(oHtml);
        }
        
        if (aData.iPages > 1) {
          oPaginate.find("#next, #last").removeClass('ui-state-disabled');
        }
        
        if (aData.iSelectedPage > 1) {
          oPaginate.find("#previous, #first").removeClass('ui-state-disabled');
        }
        
        if (aData.iSelectedPage == aData.iPages) {
          oPaginate.find("#next, #last").addClass('ui-state-disabled');
        }
        
        switch (aData.sType) {
          case "general":
            inventoryTable.fillGeneralTable(aData.aRecordList);
            inventoryTable.addSortingClasses();
            break;
          case "electronics":
            inventoryTable.fillElectronicTable(aData.aRecordList);
            inventoryTable.addSortingClasses();
            break;
          case "keys":
            inventoryTable.fillKeyTable(aData.aRecordList);
            inventoryTable.addSortingClasses();
            break;
        }
        
        oModal.removeClass('state-show');
      }
    });
  },
  'addSortingClasses': function() {
    $.each(inventoryTable.aSortList, function(iIndex) {
      var sId = $(this).attr("id");
      var aTdList = $(".inventory-table td#" + sId);
      var sSortClass = (iIndex > 2) ? "sorting_2" : "sorting_" + iIndex;
      aTdList.removeClass("sorting_0 sorting_1 sorting_2");
      aTdList.addClass(sSortClass);
    });
    
  },
  'fillGeneralTable': function(aRecordList) {
    var oGeneralTableDom = $("#general-table tbody");
    oGeneralTableDom.html("");
    $.each(aRecordList, function(iIndex, aRecord) {
      inventoryTable.addGeneralTableRow(oGeneralTableDom, iIndex, aRecord);
    });
    
    // initialize events after table has data.
    inventoryTable.setEvents();
  },
  'addGeneralTableRow': function(oTable, iIndex, aRecord) {
    var sTrClass = (iIndex % 2 == 0) ? "even" : "odd";
    var oPoDate = "";
    if (aRecord.poDate != null) {
      oPoDate = $.datepicker.formatDate('mm/dd/yy', new Date(aRecord.poDate));
    }
    var oPoRecieveDate = "";
    if (aRecord.poRecieveDate != null) {
      oPoRecieveDate = $.datepicker.formatDate('mm/dd/yy', new Date(aRecord.poRecieveDate));
    }
    var oLastEditDate = "";
    if (aRecord.lastEditDate != null) {
      oLastEditDate = $.datepicker.formatDate('mm/dd/yy', new Date(aRecord.lastEditDate));
    }
    var sHtml = 
      '<tr class="data-row ' + sTrClass + '">' +
        '<td id="info-btn">' +
          '<div class="ui-state-default ui-corner-all">' +
            '<span class="ui-icon ui-icon-circle-arrow-s"></span>' + 
          '</div>'+
          '<input type="checkbox" />' +
        '</td>' +
        '<td id="id">' + aRecord.id + '</td>' +
        '<td id="po-number" class="editable">' + aRecord.poNumber + '</td>' +
        '<td id="description" class="editable">' + aRecord.description + '</td>' +
        '<td id="decal-number" class="editable">' + aRecord.decalNumber + '</td>' +
        '<td id="property-number" class="editable">' + aRecord.propertyNumber + '</td>' +
        '<td id="location" class="editable">' + aRecord.location + '</td>' +
        '<td id="custodian" class="editable">' + aRecord.custodian + '</td>' +
        '<td id="quantity" class="editable">' + aRecord.quantity + '</td>' +
        '<td id="status" class="editable">' + aRecord.status + '</td>' +
        '<td id="notes" class="editable">' + aRecord.notes + '</td>' + 
      '</tr>' + 
      '<tr class="extra-info-row ' + sTrClass + ' state-hide">' +
        '<td id="info" colspan="11">' +
           '<span class="label">PO Date:</span>' + 
           '<span id="po-date" class="editable">' + 
             oPoDate + 
           '</span>' + 
           '<br />' + 
           '<span class="label">PO Recieve Date:</span>' +
           '<span id="po-recieve-date" class="editable">' + 
             oPoRecieveDate + 
           '</span>' +
           '<br />' +
           '<span class="label">Last Edit User:</span>' +
           '<span id="last-edit-user">' + aRecord.lastEditUser + '</span>' +
           '<br />' +
           '<span class="label">Last Edit Date:</span>' +
           '<span id="last-edit-date">' + 
             oLastEditDate + 
           '</span>' +
        '</td>' +
      '</tr>';
    var oHtml = $(sHtml);
    oTable.append(oHtml);
  },
  'fillElectronicTable': function(aRecordList) {
    var oElectronicTableDom = $("#electronics-table tbody");
    oElectronicTableDom.html("");
    $.each(aRecordList, function(iIndex, aRecord) {
      inventoryTable.addElectronicTableRow(oElectronicTableDom, iIndex, aRecord);
    });
    
    // initialize events after table has data.
    inventoryTable.setEvents();
  },
  'addElectronicTableRow': function(oTable, iIndex, aRecord) {
    var sTrClass = (iIndex % 2 == 0) ? "even" : "odd";
    var oPoDate = "&nbsp;";
    if (aRecord.poDate != null) {
      oPoDate = $.datepicker.formatDate('mm/dd/yy', new Date(aRecord.poDate));
    }
    var oPoRecieveDate = "&nbsp;";
    if (aRecord.poRecieveDate != null) {
      oPoRecieveDate = $.datepicker.formatDate('mm/dd/yy', new Date(aRecord.poRecieveDate));
    }
    var oLastEditDate = "&nbsp;";
    if (aRecord.lastEditDate != null) {
      oLastEditDate = $.datepicker.formatDate('mm/dd/yy', new Date(aRecord.lastEditDate));
    }
    var sHtml = 
      '<tr class="data-row ' + sTrClass + '">' +
        '<td id="info-btn">' +
          '<div class="ui-state-default ui-corner-all">' +
            '<span class="ui-icon ui-icon-circle-arrow-s"></span>' + 
          '</div>'+
          '<input type="checkbox" />' +
        '</td>' +
        '<td id="id">' + aRecord.id + '</td>' +
        '<td id="po-number" class="editable">' + aRecord.poNumber + '</td>' +
        '<td id="description" class="editable">' + aRecord.description + '</td>' +
        '<td id="type" class="editable">' + aRecord.type + '</td>' +
        '<td id="make" class="editable">' + aRecord.make + '</td>' +
        '<td id="model" class="editable">' + aRecord.model + '</td>' +
        '<td id="serial-number" class="editable">' + aRecord.serialNumber + '</td>' +
        '<td id="decal-number" class="editable">' + aRecord.decalNumber + '</td>' +
        '<td id="property-number" class="editable">' + aRecord.propertyNumber + '</td>' +
        '<td id="location" class="editable">' + aRecord.location + '</td>' +
        '<td id="custodian" class="editable">' + aRecord.custodian + '</td>' +
        '<td id="funder" class="editable">' + aRecord.funder + '</td>' +
        '<td id="status" class="editable">' + aRecord.status + '</td>' +
        '<td id="notes" class="editable">' + aRecord.notes + '</td>' + 
      '</tr>' + 
      '<tr class="extra-info-row ' + sTrClass + '" style="display:none;">' +
        '<td id="info" colspan="11">' +
           '<span class="label">PO Date:</span>' + 
           '<span id="po-date" class="editable">' + 
             oPoDate + 
           '</span>' + 
           '<br />' + 
           '<span class="label">PO Recieve Date:</span>' +
           '<span id="po-recieve-date" class="editable">' + 
             oPoRecieveDate + 
           '</span>' +
           '<br />' +
           '<span class="label">Last Edit User:</span>' +
           '<span id="last-edit-user">' + aRecord.lastEditUser + '</span>' +
           '<br />' +
           '<span class="label">Last Edit Date:</span>' +
           '<span id="last-edit-date">' + 
             oLastEditDate + 
           '</span>' +
        '</td>' +
      '</tr>';
    var oHtml = $(sHtml);
    oTable.append(oHtml);
  },
  'fillKeyTable': function(aRecordList) {
    var okeyTableDom = $("#keys-table tbody");
    okeyTableDom.html("");
    $.each(aRecordList, function(iIndex, aRecord) {
      inventoryTable.addKeyTableRow(okeyTableDom, iIndex, aRecord);
    });
    
    // initialize events after table has data.
    inventoryTable.setEvents();
  },
  'addKeyTableRow': function(oTable, iIndex, aRecord) {
    var sTrClass = (iIndex % 2 == 0) ? "even" : "odd";
    var oIssuedDate = "&nbsp;";
    if (aRecord.issuedDate != null) {
      oIssuedDate = $.datepicker.formatDate('mm/dd/yy', new Date(aRecord.issuedDate));
    }
    var oReturnedDate = "&nbsp;";
    if (aRecord.returnedDate != null) {
      oReturnedDate = $.datepicker.formatDate('mm/dd/yy', new Date(aRecord.returnedDate));
    }
    var oLastEditDate = "&nbsp;";
    if (aRecord.lastEditDate != null) {
      oLastEditDate = $.datepicker.formatDate('mm/dd/yy', new Date(aRecord.lastEditDate));
    }
    var sHtml = 
      '<tr class="data-row ' + sTrClass + '">' +
        '<td id="info-btn">' +
          '<div class="ui-state-default ui-corner-all">' +
            '<span class="ui-icon ui-icon-circle-arrow-s"></span>' + 
          '</div>'+
          '<input type="checkbox" />' +
        '</td>' +
        '<td id="id">' + aRecord.id + '</td>' +
        '<td id="location" class="editable">' + aRecord.location + '</td>' +
        '<td id="description" class="editable">' + aRecord.description + '</td>' +
        '<td id="key-id" class="editable">' + aRecord.keyId + '</td>' +
        '<td id="in-stock" class="editable">' + aRecord.inStock + '</td>' +
        '<td id="custodian" class="editable">' + aRecord.custodian + '</td>' +
        '<td id="status" class="editable">' + aRecord.status + '</td>' +
        '<td id="notes" class="editable">' + aRecord.notes + '</td>' + 
      '</tr>' + 
      '<tr class="extra-info-row ' + sTrClass + '" style="display:none;">' +
        '<td id="info" colspan="11">' +
           '<span class="label">Issued Date:</span>' + 
           '<span id="issued-date" class="editable">' + 
           aRecord.issuedDate + 
           '</span>' + 
           '<br />' + 
           '<span class="label">Returned Date:</span>' +
           '<span id="returned-recieve-date" class="editable">' + 
           aRecord.returnedDate + 
           '</span>' +
           '<br />' +
           '<span class="label">Last Edit User:</span>' +
           '<span id="last-edit-user">' + aRecord.lastEditUser + '</span>' +
           '<br />' +
           '<span class="label">Last Edit Date:</span>' +
           '<span id="last-edit-date">' + 
             oLastEditDate + 
           '</span>' +
        '</td>' +
      '</tr>';
    var oHtml = $(sHtml);
    oTable.append(oHtml);
  }
  
  
}

thisPage = {
  'init': function() {
    buttonSet.init();
    inventoryTable.init();
    
    
    var icon = $("#result-container .label #icon");
    icon.mousedown(thisPage.mouseDown);
    icon.mouseup(function() {
      $(this).removeClass("ui-state-active");
      var childDom = $(this).children(".ui-icon");
      if (childDom.hasClass("ui-icon-arrowthickstop-1-s")) {
        thisPage.showSearchResults();
      }
      else if (childDom.hasClass("ui-icon-arrowthickstop-1-n")) {
        thisPage.hideSearchResults();
      }
    });
    //icon.hoverIntent(thisPage.hoverOn, thisPage.hoverOff);
    
    
    $("#inventory-type #general").click();
  },
  'capitalizeMe': function (sText) {
    var newVal = '';
    var val = sText.split(' ');
    for(var c=0; c < val.length; c++) {
      newVal += val[c].substring(0,1).toUpperCase() + val[c].substring(1,val[c].length) + ' ';
    }
    return newVal;
  },
  'displayPopupMenu': function(srcDom, menuItemList) {
    if (! isEmpty(menuItemList) ) {
      $("#popupMenu").remove();


      var popupMenuDom = $('<div id="popupMenu" class="ui-widget-content"></div>');
      $.each(menuItemList, function() {
        var html = '<div class="item">'
             +    this.name
             + '</div>';
        var htmlDom = $(html);
        htmlDom.click(this.action);
        htmlDom.data('data', this.data);
        popupMenuDom.append(htmlDom);
      });

      var srcDomOffset = $(srcDom).offset();
      popupMenuDom.css('position', 'absolute');
      popupMenuDom.css('left', srcDomOffset.left);
      var top = srcDomOffset.top + $(srcDom).height() 
              + parseInt($(srcDom).css('padding-top').replace('px',''))
              + parseInt($(srcDom).css('padding-bottom').replace('px', ''));
      popupMenuDom.css('top', top);

      var popupBackground = $('<div id="popupBackground"></div>');
      popupBackground.click(function(event) {
        $("#popupBackground").remove();
        $("#file-upload-menu").remove();
      })
      
      
      popupBackground.append(popupMenuDom);
      $("body").append(popupBackground);

    }
  },
  'csvImport': function(event) {
    event.preventDefault();
    var sInventoryType = $("#inventory-type .ui-button.ui-state-active .ui-button-text").html().toLowerCase();
    
    var html  = '<div id="file-upload-menu" class="ui-widget-content auraGreen">'
          + '<form action="/import" method="POST" type="multipart/form-data">'
          +   '<input type="hidden" name="inventory-type" value="' + sInventoryType + '">'
          +   '<input type="hidden" id="record-limit" name="record-limit" value="">'
          +   '<input type="file" id="csv-file" name="csv-file">'
          + '</form>'
          + '</div>'
          + '<div id="file-upload-menu-tooltip" class="tooltip">'
          + '<div class="tooltip-content">'
          +   'Import record limit: <input type="text"> records.'
          + '</div>'
          + '<div class="tooltip-arrow-border"></div>'
          + '<div class="tooltip-arrow"></div>'
          + '</div>';
    
    var htmlDom = $(html);
    var srcDomOffset = $(this).offset();
    htmlDom.css('position', 'absolute');
    var top = srcDomOffset.top
        - parseInt($(this).parent().css('border-top-width').replace('px',''));
    htmlDom.css('top', top);
    var left = srcDomOffset.left + $(this).width() 
            + parseInt($(this).css('padding-left').replace('px',''))
            + parseInt($(this).css('padding-right').replace('px', ''));
    htmlDom.css('left', left);
    htmlDom.children("form").children("#csv-file").change(function(){
      $("#file-upload-menu form #record-limit").val($("#file-upload-menu-tooltip .tooltip-content input").val());
      $("#file-upload-menu form").submit();
    });
    htmlDom.children('form').ajaxForm({
      "dataType": "json",
      "success": function(info, textStatus, jqXHR) {
        var oTitle = $("#result-container .title");
        oTitle.find(".text").html("Csv Import Results:");
        oTitle.find(".count").html(info['inventory-list'].length + " records processed.");
        thisPage.clearSearchResults();
        $.each(info['inventory-list'], function(index) {
          thisPage.displaySearchResult(this, index);
        })
        thisPage.showSearchResults();
        $("#file-upload-menu").remove();
        $("#file-upload-menu-tooltip").remove();
        $("#result-container table#result #date").show();
        $("#result-container table#result #form-submitted").hide();
        
        $("#content-header #inventory-type .ui-state-active").click();
        
        
      }
    });
    $("body").append(htmlDom);
    
    //var title = "Import record limit: " + $("#importRecordLimit").val() + " records.";
    $("#file-upload-menu-tooltip .tooltip-arrow")
      .removeClass()
      .addClass("tooltip-arrow")
      .addClass("tooltip-arrow-top-right");
    $("#file-upload-menu-tooltip .tooltip-arrow-border")
      .removeClass()
      .addClass("tooltip-arrow-border")
      .addClass("tooltip-arrow-border-top-right");
    $("#file-upload-menu-tooltip .tooltip-content input").val($("#importRecordLimit").val());
    $("#file-upload-menu").tooltip({
      delay: 500,
      effect: "slide",
      relative: true,
      position: "bottom center",
      offset: [0, -47]
    });
    //$("#file-upload-menu .tooltip").show();
    
    
    return false;
  },
  'csvExport': function() {
    //<input type="hidden" id="exportdata" name="exportdata" />
    var sInventoryType = $("#inventory-type .ui-button.ui-state-active .ui-button-text").html().toLowerCase();
    var html = '<input name="inventory-type" value="' + sInventoryType + '">';
    $("body").append('<form id="exportform" action="/export" method="post" target="_blank">' + html + '</form>');
    //$("#exportdata").val(header_string + export_string);
    $("#exportform").submit().remove(); 
  },
  'hideSearchResults': function() {
    var searchResultsDom = $("#result-container .table-container");
    var minHeight = parseInt(searchResultsDom.css("min-height").replace("px",""));    
    var height = parseInt(searchResultsDom.css("height").replace("px",""));
    if (height != minHeight) {
      searchResultsDom.animate({"height": minHeight}, 250, function(){});
    }
    var icon = $("#result-container .label span#icon");
    icon.removeClass("ui-state-active");
    var childDom = icon.children(".ui-icon");
    childDom.removeClass("ui-icon-arrowthickstop-1-n");
    childDom.addClass("ui-icon-arrowthickstop-1-s");
  },
  'showSearchResults': function() {
    var searchResultsDom = $("#result-container .table-container");
    var maxHeight = 200;
    var height = parseInt(searchResultsDom.css("height").replace("px",""));
    if (height ==  parseInt(searchResultsDom.css("min-height").replace("px",""))) {
      searchResultsDom.animate({"height": maxHeight}, 250, function(){}); 
    }
    var icon = $("#result-container .label span#icon");
    icon.removeClass("ui-state-active");
    var childDom = icon.children(".ui-icon");
    childDom.removeClass("ui-icon-arrowthickstop-1-s");
    childDom.addClass("ui-icon-arrowthickstop-1-n");
  },
  'clearSearchResults': function() {
    
    var html = '<tr>'
         +    '<th class="description ui-widget-header">Description</th>'
         +    '<th class="info ui-widget-header">Info</th>'
         + '</tr>';
    $("#result-container #result").html(html);
    
    
  },
  'displaySearchResult': function(aInventoryItem, index) {
    var sBackgroundCss = (index % 2 == 0) ? "bg1" : "bg2";
    var sDuplicate = (aInventoryItem.duplicate) ? "Inventory item already exists. " : "";
    var sSuccess = "";
    if (aInventoryItem.action == "add") {
      sSuccess = "Successfully added inventory item.";
    } 
    else if (aInventoryItem.action == "update") {
      sSuccess = "Successfully updated inventory item.";
    } 
    else if (aInventoryItem.action == "remove") {
      sSuccess = "Successfully removed inventory item.";
    } 
    
    var sErrorClass = "ui-state-error";
    if (aInventoryItem.error == null) {
      aInventoryItem.error = sSuccess;
      sErrorClass = "ui-state-active";
    }
    
    var sErr = sDuplicate + aInventoryItem.error;
    var sHtml = '<tr class="item" style="' + sBackgroundCss + '">'
         +    '<input class="inventory-id" type="hidden" value="' + aInventoryItem.id +  '">'
         +    '<td class="description">' + aInventoryItem.description + '</td>' 
         +    '<td class="info ' + sErrorClass + '" title="' + sErr + '">' + sErr + '</td>'
         + '</tr>';
    var oHtml = $(sHtml);
    
    var oResultTable = $("#result-container table#result");
    oResultTable.append(oHtml);
    
  }
  
}




















