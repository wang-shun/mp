$.fn.dataTableExt.oPagination.lnPagination={
    
    /*
     *override oPagination.fnInt
     *override oPagination.fnUpdate
     *Update:2012-06-14
     *Update Time:11:10
     *Update Author：LN（Allison Lee）
     *Descript:Add more UI and Actions
     *Update Date:2012-12-10
     *Update Time:
     *Update Tips:修改索引模块在搜索列表后出现的问题
     */
    "fnInit": function ( oSettings, nPaging, fnCallbackDraw )
    {
        nFirst = document.createElement( 'span' );
        nPrevious = document.createElement( 'span' );
        nNext = document.createElement( 'span' );
        nLast = document.createElement( 'span' );
        nSplit=document.createElement('em');
        nFilt=document.createElement('input');
        nFilt.type="text"
        nFilt.className="Js_pagination pagination_jump";
        nFilt.id="Js_pagination"
       // nRefresh=document.createElement("em");
        nFirst.appendChild( document.createTextNode( oSettings.oLanguage.oPaginate.sFirst ));
        nPrevious.appendChild( document.createTextNode( oSettings.oLanguage.oPaginate.sPrevious ));
        nNext.appendChild( document.createTextNode( oSettings.oLanguage.oPaginate.sNext ) );
        nLast.appendChild( document.createTextNode( oSettings.oLanguage.oPaginate.sLast ) );
        
        nFirst.className = "paginate_button first";
        nPrevious.className = "paginate_button previous";
        nNext.className="paginate_button next";
        nLast.className = "paginate_button last";
        nSplit.className="dropSplit"
       // nRefresh.className="datatable_refresh_ln"
         
       
        nPaging.appendChild(nSplit);
        nPaging.appendChild( nFirst );
        nPaging.appendChild( nPrevious );
        nPaging.appendChild(nFilt)
        nPaging.appendChild(nSplit);
        nPaging.appendChild( nNext );
        nPaging.appendChild( nLast );
        nPaging.appendChild(nSplit);
        
        
         
        $(nFirst).click( function () {
            if($(this).attr("class")=="paginate_disabled_first")
            	return;
            else
            {
	            oSettings.oApi._fnPageChange( oSettings, "first" );
	            fnCallbackDraw( oSettings );
            }
        } );
         
        $(nPrevious).click( function() {
            if($(this).attr("class")=="paginate_disabled_previous")
            	return;
            else
            {
	            oSettings.oApi._fnPageChange( oSettings, "previous" );
	            fnCallbackDraw( oSettings );
            }
        } );
         
        $(nNext).click( function() {
            if($(this).attr("class")=="paginate_disabled_next"){
            	return;
            }else{
            	oSettings.oApi._fnPageChange( oSettings, "next" );
                //这里控制分页的起始记录
                //oSettings._iDisplayStart=3;
                fnCallbackDraw( oSettings );
            }
        } );
         
        $(nLast).click( function() {
            if($(this).attr("class")=="paginate_disabled_last"){
            	return;
            }else{
            	oSettings.oApi._fnPageChange( oSettings, "last" );
                fnCallbackDraw( oSettings );
            }
        } );

        $(nFilt).keyup(function(e){
            var intReg=/^\d+$/;
            var curPageInt=0;
            var _this=$(this)
            if((intReg).test(parseInt(_this.val())))
            {
        		if($.trim(_this.val())==0){
        			curPageInt=1
        		}else{
	        		curPageInt=_this.val()
	                oSettings.oApi._fnPageChange( oSettings,  parseInt(curPageInt)-1 )
	                fnCallbackDraw( oSettings );
        		}
            }
            else
            {
                _this.val("")
            }
            e.preventDefault();
        });
        
         /*insert split element*/
         $(nSplit).insertAfter($(nPrevious))
         $(nSplit).clone().insertAfter($(nLast))
         $(nSplit).clone().insertBefore($(nFirst))
          /*insert element*/

        
          var insEle='<div class="ln_paginationbox"><input type="text" id="cloenpos" class="pagination_jump" value="1" style="display:none" />/<label class="Js_totalPage">1</label></div>'
          $(insEle).insertBefore($(nNext)) 
          //$(nRefresh).insertAfter($(nLast))        
         // $(nSplit).clone().insertBefore($(nRefresh))
          setTimeout(function(){
             $(".Js_pagination").each(function(){
                $(this).insertBefore($(this).next(".ln_paginationbox").find(".pagination_jump"))
             })
          })
        

        /* Disallow text selection */
        $(nFirst).bind( 'selectstart', function () { return false; } );
        $(nPrevious).bind( 'selectstart', function () { return false; } );
        $(nNext).bind( 'selectstart', function () { return false; } );
        $(nLast).bind( 'selectstart', function () { return false; } );
        
        //
        
    },
     
    /*
     * Function: oPagination.lnPagination.fnUpdate
     * Purpose:  Update the list of page buttons shows
     * Returns:  -
     * Inputs:   object:oSettings - dataTables settings object
     *           function:fnCallbackDraw - draw function which must be called on update
     */
    "fnUpdate": function ( oSettings, fnCallbackDraw )
    {
        if ( !oSettings.aanFeatures.p )
        {
            return;
        }
        /*show total pages*/
        var currentTableWrap=$("#"+$(oSettings.nTable).attr("id")+"_wrapper")
      
       var iPageCount = Math.ceil((oSettings.fnRecordsDisplay()) / oSettings._iDisplayLength);
       if(iPageCount==0){
        currentTableWrap.find(".Js_totalPage").text("1")
       }else
        currentTableWrap.find(".Js_totalPage").text(iPageCount);
        //lnpagination 插件  添加 id
        //当前第几页
        if(iPageCount==0 || iPageCount==0){
           currentTableWrap.find(".Js_pagination").val(1)
        }
        else
        {
            currentTableWrap.find(".Js_pagination").val(Math.ceil(parseInt(oSettings._iDisplayStart) / parseInt(oSettings._iDisplayLength)) + 1)
        }
        /* Loop over each instance of the pager */
        var an = oSettings.aanFeatures.p;
        for ( var i=0, iLen=an.length ; i<iLen ; i++ )
        {
            var buttons = an[i].getElementsByTagName('span');
            if ( oSettings._iDisplayStart === 0 )
            {
                buttons[0].className = "paginate_disabled_first";
                buttons[1].className = "paginate_disabled_previous";
            }
            else
            {
                buttons[0].className = "paginate_enabled_first";
                buttons[1].className = "paginate_enabled_previous";
            }
             
            if ( oSettings.fnDisplayEnd() == oSettings.fnRecordsDisplay() || currentTableWrap.find(".Js_pagination").val()==currentTableWrap.find(".Js_totalPage").text())
           //if(parseInt(parseInt(oSettings._iDisplayStart)+parseInt(oSettings._iDisplayLength))== oSettings.fnRecordsDisplay())
            {
                buttons[2].className = "paginate_disabled_next";
                buttons[3].className = "paginate_disabled_last";
            }
            else
            {
                buttons[2].className = "paginate_enabled_next";
                buttons[3].className = "paginate_enabled_last";
            }
        }
        if($(oSettings.nTable).find("th input:checkbox:checked")){
        	if($(".SID-checkAll").next("span.SID-page-tar").length==0 || "cur"==$(".SID-checkAll").val()){
        		$(oSettings.nTable).find("th input:checkbox:checked").removeAttr("checked");
        	}else if("all"==$(".SID-checkAll").val() && $(".SID-checkAll").is(":checked")){
				$("#grid-table input[type=checkbox]").attr("checked","checked");
				$("#grid-table tr").addClass("selectedtr");
        	}
        }
    }
}

 
