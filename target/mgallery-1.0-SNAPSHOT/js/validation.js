/**
 * hide、show error message
 * @param onOff true verification pass，hide error message; false verification fail，show error message
 * @param input single form field selector
 * @param errSelector error selector
 * @param message error message
 * @returns
 */
function switchValid(onOff,input,errSelector,message){
    if(onOff == false){
        $(errSelector).text(message);
        $(input).addClass("error_input");
        $(errSelector).addClass("error_message");
    }else{
        $(errSelector).text("");
        $(input).removeClass("error_input");
        $(errSelector).removeClass("error_message");
    }
}
/**
 * check if input is empty
 * @param input single field selector
 * @param errSelector error selector
 * @returns true-success  false-fail
 */
function checkEmpty(input,errSelector){
    var val = $(input).val();
    if($.trim(val) == ""){
        switchValid(false,input,errSelector,"Please input");
        return false;
    }else{
        switchValid(true,input,errSelector);
        return true;
    }
}

/**
 * check category
 * @param input single field selector
 * @param errSelector error selector
 * @returns true-verification sueccess  false-verification fail
 */
function checkCategory(input,errSelector){
    var val = $(input).val();
    if(val == -1){
        switchValid(false,input,errSelector,"Please select painting type.");
        return false;
    }else{
        switchValid(true,input,errSelector);
        return true;
    }
}

/**
 * price has to be positive integer
 * @param input single field selector
 * @param errSelector error selector
 * @returns true-verification sueccess  false-verification fail
 */
function checkPrice(input,errSelector){
    var val = $(input).val();
    var regex=/^[1-9][0-9]*$/;
    if(!regex.test(val)){
        switchValid(false,input,errSelector,"Invalid price");
        return false;
    }else{
        switchValid(true,input,errSelector);
        return true;
    }
}

/**
 * upload file check
 * @param input single field selector
 * @param errSelector error selector
 * @returns true-verification sueccess  false-verification fail
 */
function checkFile(input,errSelector){
    if(checkEmpty(input,errSelector) == false){
        return false;
    }

    var val = $(input).val().toLowerCase(); //JPG,jPg,jpg

    if(val.length<4){//x.xxx
        switchValid(false,input,errSelector,"Please select a valid picture");
        return false;
    }
    suffix = val.substring(val.length-3);
    if(suffix=="jpg"||suffix=="png"||suffix=="gif"){
        switchValid(true,input,errSelector);
        return true;
    }else{
        switchValid(false,input,errSelector,"Please select a valid picture");
        return false;
    }


}