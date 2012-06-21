var oMain = {
  'fnInit': function() {
    
    
    jQuery.fn.extend({
      'show': function() {
        return this.css('display','inline-block');
      }, 
      'hide': function() {
        return this.css('display', 'none');
      },
      /**
       * $("<css>").fnAllowOnlyNumberKeys()
       * 
       * Requires jQuery library to be loaded before invoking.
       * 
       * Function to only allow number keys and some control keys (backspace, delete, left arrow, 
       * right arrow, tab, home, end) to propagate to <element>. Fires on .keydown() event.
       * 
       */
      'fnAllowOnlyNumberKeys': function() {
        this.keydown(function(event) {
          // Allow only backspace, delete, left arrow, right arrow, tab, home, and end keys
          var aAllowedKeys = [46, 8, 37, 39, 9, 36, 35];
          if ( $.inArray(event.keyCode, aAllowedKeys) != -1 ) {
            // let it happen, don't do anything
          }
          else {
            // Ensure that it is a number and stop the keypress
            if ((event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
              event.preventDefault(); 
            }
            else if (event.shiftKey === true) {
              event.preventDefault(); 
            }
          }
        });
      },
      /**
       * $("<css>").fnDataFormat()
       * 
       * Requires jQuery library to be loaded before invoking.
       * 
       * Function to add auto formatting of <element>'s value. Fires on .focusout() event.
       * Class 'data-format' should be added to <element>. Custom attribue 'data-format=""' should
       * also be added to <element>. Multiple data formats may be added, space seperated.
       * 
       * Example 'data-format' attribue:
       *  <input type="text" class="data-format" data-format="alpha-numeric uppercase-word" />
       * 
       */
      'fnDataFormat': function() {
        $(this).focusout(function() {
          var aFormatRules = $(this).attr("data-format").split(" ");
          var sValue = $(this).val();
          $.each(aFormatRules, function() {
            var sFormat = this.toString();
            var sRegExp;
            switch (sFormat) {
              case "uppercase":
                sValue = sValue.toUpperCase();
                break;
              
              case "uppercase-word":
                sValue = $.fnUppercaseWord(sValue);
                break;
              
              case "alpha":
                sRegExp = "[A-Za-z]";
                sValue = $.fnCleanString(sRegExp, sValue);
                break;
                
              case "numeric":
                sRegExp = "[0-9]";
                sValue = $.fnCleanString(sRegExp, sValue);
                break;
                
              case "alpha-numeric":
                sRegExp = "[A-Za-z0-9]";
                sValue = $.fnCleanString(sRegExp, sValue);
                break;
                
              case "alpha-accent-dash-space":
                sRegExp = "[A-Za-z\-\ ÀÈÌÒÙàèìòùÁÉÍÓÚÝáéíóúýÂÊÎÔÛâêîôûÃÑÕãñõÄËÏÖÜŸäëïöüÿ]";
                sValue = $.fnCleanString(sRegExp, sValue);
                break;
                
              case "address":
                sRegExp = "[\.,#&\*\/\{\|\}A-Za-z0-9\-\ ÀÈÌÒÙàèìòùÁÉÍÓÚÝáéíóúýÂÊÎÔÛâêîôûÃÑÕãñõÄËÏÖÜŸäëïöüÿ]";
                sValue = $.fnCleanString(sRegExp, sValue);
                break;
            }
          });
          $(this).val(sValue);
        });
      },
      /**
       * $("<css>").fnIsValid()
       * 
       * Requires jQuery library to be loaded before invoking.
       * 
       * Function to perform validation of <element>'s value. Manually fired. Class 'validate'
       * should be added to <element>. Custom attribute 'validate=""' should be added to <element>
       * for custom validation operations. 
       * Example:
       *  <input type="text" class="validate" validate="name" />
       * 
       * Optional custom attribue 'minlength=""' used to validate the <element> having a minimum 
       * string length. Optional existing attribute 'maxlength=""' also used to validate the 
       * <element>'s string length doesn't exceed a maximum string length even though browser 
       * already enforces 'maxlength'.
       * Example:
       *  <input type="text" class="validate" validate="name" minlength="1" />
       *  <input type="text" class="validate" validate="name" maxlength="1" />
       *  <input type="text" class="validate" validate="name" minlength="1" maxlength="1" />
       *  
       * When supplying 'validate="password"', required custom attributes 'min-alpha=""', '
       * min-number=""', and 'min-symbol=""' must be supplied. 'min-alpha' will validate that 
       * <element>'s value contains a minimum amount of alphabet characters. 'min-number' will 
       * validate that <element>'s value contains a minimum amount of number characters. 
       * 'min-symbol' will validate that <element>'s value contains a minimum amount of 'symbol' 
       * characters.
       * Example:
       *  <input type="text" class="validate" validate="password" 
       *   min-alpha="1" min-number="1" min-symbol="0"
       *   minlength="4" maxlength="20" />
       * 
       * When supplying 'validate="postal"', required custom attribute 'countrycss=""' must be 
       * supplied. The 'countrycss' value will be used to find the <element> that contians the
       * country sting values used to determine which format to test the postal value against.
       * Supported county strings: 'usa', 'united states', 'united-states', 'mexico', 'canada'
       * Example:
       *  <input type="text" class="validate" validate="postal" 
       *   countrycss="#element .element select"
       *   minlength="5" maxlength="6" />
       *  <input type="text" class="validate" validate="postal" 
       *   countrycss="#element .element input"
       *   minlength="5" maxlength="6" />
       * 
       * @return Array Array containing 'bValid' boolean indicating validity and 'sError' String
       *  containing error messages indicating why validation failed.
       */
      'fnIsValid': function() {
        var aReturn = {
          "bValid": true,
          "sError": ""
        };
        
        if ( ! $(this).hasClass('validate')) {
          return aReturn;
        }
        
        var bValid = true;
        var sValidationType = $(this).attr("validate");
        var aValidationReturn;
        switch (sValidationType) {
          case "name":
            break;
          
          case "email":
            aValidationReturn = $.fnValidateEmail($(this));
            aReturn.bValid = aReturn.bValid && aValidationReturn.bValid;
            aReturn.sError = $.trim(aReturn.sError + " " + aValidationReturn.sError);
            break;
          
          case "password":
            aValidationReturn = $.fnValidatePassword($(this));
            aReturn.bValid = aReturn.bValid && aValidationReturn.bValid;
            aReturn.sError = $.trim(aReturn.sError + " " + aValidationReturn.sError);
            break;
          
          case "address":
            break;
          
          case "city":
            break;
          
          case "postal":
            aValidationReturn = $.fnValidatePostal($(this));
            aReturn.bValid = aReturn.bValid && aValidationReturn.bValid;
            aReturn.sError = $.trim(aReturn.sError + " " + aValidationReturn.sError);
            break;
        }
                
        aValidationReturn = $.fnValidateLength($(this));
        aReturn.bValid = aReturn.bValid && aValidationReturn.bValid;
        aReturn.sError = $.trim(aReturn.sError + " " + aValidationReturn.sError);
        return aReturn;
      }
    });
    
    
    jQuery.fnGetAge = function(sYear, sMonth, sDay) {
      if (sYear == "" || sMonth == "" || sDay == "") {
        return false;
      }
      var iMonth = parseInt(sMonth) - 1;
      // months in javascript Date is 0-11
      iMonth = iMonth - 1;
      var oBirthday = new Date(sYear, iMonth, sDay);
      var oNow = new Date();
      var fDiffDays = (oNow - oBirthday) / 1000 / (60 * 60 * 24);
      var iCalcYears = Math.floor(fDiffDays / 365.25);
      
      return iCalcYears;
    };
    
    /**
     * $.fnValidateEmail()
     * 
     * Requires jQuery library to be loaded before invoking.
     * 
     * Function to supliment $("<css>").fnIsValid() for validating email addresses.
     * Refer to $("<css>").fnIsValid() for use.
     * 
     * @param oInput Dom-Element <element> containing value to be tested and custom validating
     *  attributes.
     * @return Array Array containing 'bValid' boolean indicating validity and 'sError' String
     *  containing error messages indicating why validation failed.
     */
    jQuery.fnValidateEmail = function(oInput) {
      var aReturn = {
          "bValid": true,
          "sError": ""
      };
      
      if ($.fnIsEmpty($(oInput))) {
        aReturn.bValid = false;
        aReturn.sError = "Input element not found.";
        return aReturn;
      }
      
      var sValue = $(oInput).val();
      if ($.fnIsEmpty(sValue)) {
        aReturn.bValid = false;
        aReturn.sError = "Input element has no value.";
        return aReturn;
      }
      
      var sRegExp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$";
      var oRegExp = new RegExp(sRegExp);
      
      if (sValue.search(oRegExp) == -1) {
        aReturn.bValid = false;
        aReturn.sError = "Not a valid email address.";
      }
      
      return aReturn;
    };
    
    /**
     * $.fnValidatePassword()
     * 
     * Requires jQuery library to be loaded before invoking.
     * 
     * Function to supliment $("<css>").fnIsValid() for validating passwords.
     * Refer to $("<css>").fnIsValid() for use.
     * 
     * @param oInput Dom-Element <element> containing value to be tested and custom validating
     *  attributes.
     * @return Array Array containing 'bValid' boolean indicating validity and 'sError' String
     *  containing error messages indicating why validation failed.
     */
    jQuery.fnValidatePassword = function(oInput) {
      var aReturn = {
          "bValid": true,
          "sError": ""
      };
      
      if ($.fnIsEmpty($(oInput))) {
        aReturn.bValid = false;
        aReturn.sError = "Input element not found.";
        return aReturn;
      }
      
      var sValue = $(oInput).val();
      if ($.fnIsEmpty(sValue)) {
        aReturn.bValid = false;
        aReturn.sError = "Input element has no value.";
        return aReturn;
      }
      
      var aList = {
        "min-alpha": null,
        "min-numeric": null,
        "min-symbol": null
      };
      for (sType in aList) {
        var sTypeValue = $(oInput).attr(sType);
        if ($.fnIsEmpty(sTypeValue)) {
          aReturn.bValid = false;
          aReturn.sError = "Missing '" + sType + "' tag.";
          return aReturn;
        }
        var iValue = parseInt(sTypeValue);
        if ($.fnIsEmpty(iValue)) {
          aReturn.bValid = false;
          aReturn.sError = "'" + sType + "' tag missing value.";
          return aReturn;
        }
        else if (iValue == NaN) {
          aReturn.bValid = false;
          aReturn.sError = "'" + sType + "' tag not a number.";
          return aReturn;
        }
        
        aList[sType] = iValue;
      }
      
      var sRegExp;
      var oRegExp;
      var iMatchCount;
      
      if (aList['min-alpha'] > 0) {
        sRegExp = "[A-Za-z]";
        oRegExp = new RegExp(sRegExp, "g");
        aMatchList = sValue.match(oRegExp);
        if (aMatchList == null || aMatchList.length < aList['min-alpha']) {
          aReturn.bValid = false;
          aReturn.sError = $.trim(aReturn.sError + " Missing 'Alphabet' characters.");
        }
      }
      
      if (aList['min-numeric'] > 0) {
        sRegExp = "[0-9]";
        oRegExp = new RegExp(sRegExp, "g");
        aMatchList = sValue.match(oRegExp);
        if (aMatchList == null || aMatchList.length < aList['min-numeric']) {
          aReturn.bValid = false;
          aReturn.sError = $.trim(aReturn.sError + " Missing 'Number' characters.");
        }
      }
      
      if (aList['min-symbol'] > 0) {
        sRegExp = "[^A-Za-z0-9]";
        oRegExp = new RegExp(sRegExp, "g");
        aMatchList = sValue.match(oRegExp);
        if (aMatchList == null || aMatchList.length < aList['min-symbol']) {
          aReturn.bValid = false;
          aReturn.sError = $.trim(aReturn.sError + " Missing 'Symbol' characters.");
        }
      }
      
      
      return aReturn;
    };
    
    /**
     * $.fnValidatePostal()
     * 
     * Requires jQuery library to be loaded before invoking.
     * 
     * Function to supliment $("<css>").fnIsValid() for validating postal codes.
     * Refer to $("<css>").fnIsValid() for use.
     * 
     * @param oInput Dom-Element <element> containing value to be tested and custom validating
     *  attributes.
     * @return Array Array containing 'bValid' boolean indicating validity and 'sError' String
     *  containing error messages indicating why validation failed.
     */
    jQuery.fnValidatePostal = function(oInput) {
      var aReturn = {
          "bValid": true,
          "sError": ""
      };
      
      if ($.fnIsEmpty($(oInput))) {
        aReturn.bValid = false;
        aReturn.sError = "Input element not found.";
        return aReturn;
      }
      
      var sValue = $(oInput).val();
      if ($.fnIsEmpty(sValue)) {
        aReturn.bValid = false;
        aReturn.sError = "Input element has no value.";
        return aReturn;
      }
      
      var sCountryCss = $(oInput).attr("countrycss");
      if ($.fnIsEmpty(sCountryCss)) {
        aReturn.bValid = false;
        aReturn.sError = "Missing 'countrycss' tag.";
        return aReturn;
      }
      
      var oCountry = $(sCountryCss);
      if ($.fnIsEmpty(oCountry)) {
        aReturn.bValid = false;
        aReturn.sError = "Country element not found.";
        return aReturn;
      }
      
      var sCountry = oCountry.val();
      if ($.fnIsEmpty(sCountry)) {
        aReturn.bValid = false;
        aReturn.sError = "Country element has no value.";
        return aReturn;
      }
      
      var sRegExp = "";
      switch (sCountry.toLowerCase()) {
        case "mexico":
        case "usa":
        case "united states":
        case "united-states":
          sRegExp = "^[0-9]{5}$";
          break;
        
        case "canada":
          sRegExp = "^[a-zA-Z][0-9][a-zA-Z][0-9][a-zA-Z][0-9]$";
          break;
      }
      
      if ($.fnIsEmpty(sRegExp)) {
        aReturn.bValid = false;
        aReturn.sError = "Country '" + sCountry + "' not supported.";
        return aReturn;
      }
      
      var oRegExp = new RegExp(sRegExp);
      if (sValue.search(oRegExp) == -1) {
        aReturn.bValid = false;
        aReturn.sError = "Postal code not valid for country: '" + sCountry + "'.";
      }
      
      return aReturn;
    };
    
    /**
     * $.fnValidateLength()
     * 
     * Requires jQuery library to be loaded before invoking.
     * 
     * Function to supliment $("<css>").fnIsValid() for validating string lengths.
     * Refer to $("<css>").fnIsValid() for use.
     * 
     * @param oInput Dom-Element <element> containing value to be tested and custom validating
     *  attributes.
     * @return Array Array containing 'bValid' boolean indicating validity and 'sError' String
     *  containing error messages indicating why validation failed.
     */
    jQuery.fnValidateLength = function(oInput) {
      var aReturn = {
          "bValid": true,
          "sError": ""
      };
      var iMinLength = parseInt($(oInput).attr("minlength"));
      var iMaxLength = parseInt($(oInput).attr("maxlength"));
      var sValue = $(oInput).val();
      if (( ! isNaN(iMinLength) ) && (sValue.length < iMinLength)) {
        aReturn.bValid = false;
        aReturn.sError += "Minimum length not met.";
      }
      else if (( ! isNaN(iMaxLength) ) && (sValue.length > iMaxLength)) {
        aReturn.bValid = false;
        aReturn.sError += "Maximum length exceeded.";
      }
      return aReturn;
    };
    
    /**
     * $.fnIsEmpty()
     * 
     * Requires jQuery library to be loaded before invoking.
     * 
     * Custom function to test if supplied variable is empty (null, "", {}, [], undefined).
     * 
     * @param oObject Object Variable to be tested.
     * @return boolean Is empty or not.
     */
    jQuery.fnIsEmpty = function(oObject) {
      var bEmpty = false;
      var sType = typeof oObject;
      switch (sType) {
        case "boolean":
          break;
          
        case "number":
          break;
          
        case "string":
          bEmpty = (oObject == "");
          break;
        
        case "function":
          break;
        
        case "object":
          bEmpty = $.isEmptyObject(oObject) || oObject.length === 0;
          break;
          
        case "undefined":
          bEmpty = true;
          break;
      }
      
      return bEmpty;
    }
    
    /**
     * $.fnCleanString()
     * 
     * Requires jQuery library to be loaded before invoking.
     * 
     * Function to supliment $("<css>").fnDataFormat() for formatting strings. 
     * Will remove any character not part of supplied regular expression.
     * 
     * @param sRegExp String Regular expression to match.
     * @param sString String String to remove characters from.
     * @return String String containing only characters match with regular expression.
     */
    jQuery.fnCleanString = function(sRegExp, sString) {
      if (sRegExp == "" || sString == "") {
        return sString;
      }
      
      var oRegExp = new RegExp(sRegExp);
      var sCleanedString = "";
      for (var i = 0; i < sString.length; i++) {
        var sChar = sString.substr(i, 1);
        if (sChar.search(oRegExp) != -1) {
          // character part of regexp so keep, remove all others
          sCleanedString += sChar;
        }
      }
      
      return $.trim(sCleanedString);
    }
    
    /**
     * $.fnUppercaseWord()
     * 
     * Requires jQuery library to be loaded before invoking.
     * 
     * Function to supliment $("<css>").fnDataFormat() for formatting strings. 
     * Will uppercase the first letter of every string delimited by either a space or dash.
     * 
     * @param sString String String to convert.
     * @return String Converted string.
     */
    jQuery.fnUppercaseWord = function(sString) {
      if (sString == "") {
        return sString;
      }
      
      sString = $.trim(sString);
      var sFormattedString = "";
      var aWordSpaceList = sString.split(" ");
      $.each(aWordSpaceList, function() {
        var sSpaceWord = this.toString();
        if (sSpaceWord.indexOf("-") == -1) {
          // capitalize word
          sFormattedString += " " + $.fnUppercaseFirst(sSpaceWord);
        }
        else {
          // check for dashes and capitalize them too
          var aWordDashList = sSpaceWord.split("-");
          var sWordDashString = "";
          $.each(aWordDashList, function() {
            var sDashWord = this.toString();
            sWordDashString += "-" + $.fnUppercaseFirst(sDashWord);
          });
          
          if (sWordDashString.substr(0, 1) == "-") {
            sWordDashString = sWordDashString.substr(1);
          }
          
          sFormattedString += " " + sWordDashString;
        }
      });
      return $.trim(sFormattedString);
    }
    
    /**
     * $.fnUppercaseFirst()
     * 
     * Requires jQuery library to be loaded before invoking.
     * 
     * Function to supliment $("<css>").fnDataFormat() for formatting strings. 
     * Will uppercase the first letter supplied string.
     * 
     * @param sString String String to convert.
     * @return String Converted string.
     */
    jQuery.fnUppercaseFirst = function(sString) {
      if (sString == "") {
        return sString;
      }
      
      sString = $.trim(sString);
      var sFormattedString;
      var sFirst = sString.substr(0,1);
      if (sFirst != "&") {
        sFormattedString = $.fnUppercase(sFirst);
      }
      else {
        // TODO: handle input strings with html entities.
        sFormattedString = sFirst;
      }
      return sFormattedString + $.fnLowercase(sString.substr(1));
    };
    
    /**
     * $.fnUppercase()
     * 
     * Requires jQuery library to be loaded before invoking.
     * 
     * Function to supliment $("<css>").fnDataFormat() for formatting strings. 
     * Will uppercase supplied string including accents.
     * 
     * @param sString String String to convert.
     * @return String Converted string.
     */
    jQuery.fnUppercase = function(sString) {
      if (sString == "") {
        return sString;
      }
      
      sString = $.trim(sString);
      var sFormattedString;
      var aMap = {
        'à' : 'À',
        'è' : 'È',
        'ì' : 'Ì',
        'ò' : 'Ò',
        'ù' : 'Ù',
        'á' : 'Á',
        'é' : 'É',
        'í' : 'Í',
        'ó' : 'Ó',
        'ú' : 'Ú',
        'ý' : 'Ý',
        'â' : 'Â',
        'ê' : 'Ê',
        'î' : 'Î',
        'ô' : 'Ô',
        'û' : 'Û',
        'ñ' : 'Ñ',
        'õ' : 'Õ',
        'ä' : 'Ä',
        'ë' : 'Ë',
        'ï' : 'Ï',
        'ö' : 'Ö',
        'ü' : 'Ü',
        'ÿ' : 'Ÿ'
      }
      for (var i = 0; i < sString.length; i++) {
        var sCharacter = sString[i];
        if (sCharacter in aMap) {
          sFormattedString = aMap[sCharacter];
        }
        else {
          sFormattedString = sString.toUpperCase();
        }
      }
      return $.trim(sFormattedString);
    };
    
    /**
     * $.fnLowercase()
     * 
     * Requires jQuery library to be loaded before invoking.
     * 
     * Function to supliment $("<css>").fnDataFormat() for formatting strings. 
     * Will lowercase supplied string including accents.
     * 
     * @param sString String String to convert.
     * @return String Converted string.
     */
    jQuery.fnLowercase = function(sString) {
      if (sString == "") {
        return sString;
      }
      
      sString = $.trim(sString);
      var sFormattedString;
      var aMap = {
        'À' : 'à',
        'È' : 'è',
        'Ì' : 'ì',
        'Ò' : 'ò',
        'Ù' : 'ù',
        'Á' : 'á',
        'É' : 'é',
        'Í' : 'í',
        'Ó' : 'ó',
        'Ú' : 'ú',
        'Ý' : 'ý',
        'Â' : 'â',
        'Ê' : 'ê',
        'Î' : 'î',
        'Ô' : 'ô',
        'Û' : 'û',
        'Ñ' : 'ñ',
        'Õ' : 'õ',
        'Ä' : 'ä',
        'Ë' : 'ë',
        'Ï' : 'ï',
        'Ö' : 'ö',
        'Ü' : 'ü',
        'Ÿ' : 'ÿ'
      }
      for (var i = 0; i < sString.length; i++) {
        var sCharacter = sString[i];
        if (sCharacter in aMap) {
          sFormattedString = aMap[sCharacter];
        }
        else {
          sFormattedString = sString.toLowerCase();
        }
      }
      return $.trim(sFormattedString);
    };
    
    /**
     * $.fnFormatNumber
     * 
     * Requires jQuery library to be loaded before invoking.
     * 
     * Function to perform PHP's number_format() like functionality. Adds comma's after every
     * third digit and trims off excess decimal digits.
     * 
     * @param fNumber Float Number to be formatted.
     * @param iDecimal Integer Number of decimal places to perserve.
     * @return String Formatted number.
     */
    jQuery.fnFormatNumber = function(fNumber, iDecimal) {
      if (isNaN(fNumber)) {
        return fNumber;
      }
      
      var sNumber = fNumber.toString();
      var sFormatedNumber;
      if (iDecimal == null) {
        sFormatedNumber = sNumber.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
      }
      else {
        if (isNaN(iDecimal)) {
          return fNumber;
        }
        var iDecimalIndex = sNumber.indexOf('.');
        if (iDecimalIndex != -1) {
          var sDecimal = sNumber.substr(iDecimalIndex, iDecimal + 1);
          sNumber = sNumber.substring(0, iDecimalIndex);
          sFormatedNumber = sNumber.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
          sFormatedNumber += sDecimal;
        }
        else if (iDecimal > 0) {
          sFormatedNumber = sNumber.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
          sFormatedNumber += '.';
          for (var i = 0; i < iDecimal; i++) {
            sFormatedNumber += '0';
          }
        }
        else {
          sFormatedNumber = sNumber.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
        }
      }
      
      return sFormatedNumber;
    };
    
    jQuery.fnCenter = function(oElement) {
      var aWindow = {
        'height': $(window).height(),
        'width': $(window).width()
      };
      var aElement = {
        'height': oElement.height(),
        'width': oElement.width()
      };
      /*
      var aReturn = {
        'left': (aWindow.width / 2) - (aElement.width / 2),
        'top': (aWindow.height / 2) - (aElement.height / 2)
      }
      */
      var iLeft = (aWindow.width / 2) - (aElement.width / 2);
      var iTop = (aWindow.height / 2) - (aElement.height / 2);
      oElement.css("left", Math.max(iLeft, 16));
      oElement.css("top", Math.max(iTop, 16));
    };
  
    
    
    jQuery.KEY = {
      'CANCEL': 3,
      'HELP': 6,
      'BACK_SPACE': 8,
      'TAB': 9,
      'CLEAR': 12,
      'RETURN': 13,
      'ENTER': 14,
      'SHIFT': 16,
      'CONTROL': 17,
      'ALT': 18,
      'PAUSE': 19,
      'CAPS_LOCK': 20,
      'ESCAPE': 27,
      'SPACE': 32,
      'PAGE_UP': 33,
      'PAGE_DOWN': 34,
      'END': 35,
      'HOME': 36,
      'LEFT': 37,
      'UP': 38,
      'RIGHT': 39,
      'DOWN': 40,
      'PRINTSCREEN': 44,
      'INSERT': 45,
      'DELETE': 46,
      '0': 48,
      '1': 49,
      '2': 50,
      '3': 51,
      '4': 52,
      '5': 53,
      '6': 54,
      '7': 55,
      '8': 56,
      '9': 57,
      'SEMICOLON': 59,
      'EQUALS': 61,
      'A': 65,
      'B': 66,
      'C': 67,
      'D': 68,
      'E': 69,
      'F': 70,
      'G': 71,
      'H': 72,
      'I': 73,
      'J': 74,
      'K': 75,
      'L': 76,
      'M': 77,
      'N': 78,
      'O': 79,
      'P': 80,
      'Q': 81,
      'R': 82,
      'S': 83,
      'T': 84,
      'U': 85,
      'V': 86,
      'W': 87,
      'X': 88,
      'Y': 89,
      'Z': 90,
      'CONTEXT_MENU': 93,
      'NUMPAD0': 96,
      'NUMPAD1': 97,
      'NUMPAD2': 98,
      'NUMPAD3': 99,
      'NUMPAD4': 100,
      'NUMPAD5': 101,
      'NUMPAD6': 102,
      'NUMPAD7': 103,
      'NUMPAD8': 104,
      'NUMPAD9': 105,
      'MULTIPLY': 106,
      'ADD': 107,
      'SEPARATOR': 108,
      'SUBTRACT': 109,
      'DECIMAL': 110,
      'DIVIDE': 111,
      'F1': 112,
      'F2': 113,
      'F3': 114,
      'F4': 115,
      'F5': 116,
      'F6': 117,
      'F7': 118,
      'F8': 119,
      'F9': 120,
      'F10': 121,
      'F11': 122,
      'F12': 123,
      'F13': 124,
      'F14': 125,
      'F15': 126,
      'F16': 127,
      'F17': 128,
      'F18': 129,
      'F19': 130,
      'F20': 131,
      'F21': 132,
      'F22': 133,
      'F23': 134,
      'F24': 135,
      'NUM_LOCK': 144,
      'SCROLL_LOCK': 145,
      'COMMA': 188,
      'PERIOD': 190,
      'SLASH': 191,
      'BACK_QUOTE': 192,
      'OPEN_BRACKET': 219,
      'BACK_SLASH': 220,
      'CLOSE_BRACKET': 221,
      'QUOTE': 222,
      'META': 224
    }
  }
};



