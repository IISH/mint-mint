
function initWithCollection( datasetId ) {
	this.createCollection(datasetId);
}

function createCollection( datasetId )  {
	var self = this;
	this.datasetId = datasetId;
	var collPopup = $('<div id ="collPopup">Collection name :<p><\p></div>');
	var name = $("<input>", { type: "text" }).appendTo(collPopup);
	
	var collPopup2 = $('<div id ="collPopup2">Collection description :<p><\p></div>').appendTo(collPopup);
	var description = $("<input>", { type: "text" }).appendTo(collPopup2);
    
	$('<div>Is it a public collection ?<p><\p></div>').appendTo(collPopup);
	$('<div><p><\p></div>').appendTo(collPopup);

    var publicButton = $('<div style="width: 50px; margin-left: 1px;" ><p><\p></div>').addClass("toggle toggle-modern");
    publicButton.appendTo(collPopup);
    var ispub = true;
  //  publicButton.toggles({type:'select'});
    publicButton.toggles({on:true,type:'select',text:{on:'Yes',off:'No'}});
    publicButton.on();
    publicButton.on('toggle', function (e, active) {
    	if (active) {
    		ispub = true;
    	} else {
    		ispub=false;
    	}
    	console.log("ispub value is: ",ispub);
    });
    //publicButton.toggles({text:{on:'Yes',off:'No'}}).toggles({type:'select'});
    //<div class="toggle-slide"><div style="width: 118px; margin-left: -48px;" class="toggle-inner"><div style="height: 22px; width: 59px; text-indent: -7.33333px; line-height: 22px;" class="toggle-on">HI</div><div style="height: 22px; width: 22px; margin-left: -11px;" class="toggle-blob"></div><div style="height: 22px; width: 59px; margin-left: -11px; text-indent: 7.33333px; line-height: 22px;" class="toggle-off active">BYE</div></div></div>
    
    
    
    
    
    


    function onTokenReceive( token ) {
    	// maybe some feedback that WITH login was successful
    	// or it failed
    	if( token != undefined ) {	
    	/*	$.ajax({
    			url: "ExportToWith",
    			data: {
    				datasetId: self.datasetId,
    				token: token,
    				collectionName: name.val(),
    				isPublic: publicButton.val()
    			},
    			error: function(response) {
    				//console.log(response);
    				console.log(response);
    				
    				var text=response.result;
    				
    				
    				alert(text);
    				//var attribute = $(err).attr(INSERT-ATTRIBUTE-NAME);
    				//var r=JSON.parse(response.responseText.message);
    				//alert(JSON.stringify(response.errorMessage));
    				//alert("An error occured. Please try again. " );
    				//alert(JSON.stringify(r));    				
    			},
    			success: function(response){
    				console.log(response);
    				
    				alert(response.result);
    				collPopup.dialog("close");
    			}
    		});*/

    		 $.ajax({
    			    type:'GET',
    			    url:"TestWith",
    			    data: {
        				datasetId: self.datasetId,
        				token: token,
        				collectionName: name.val(),
        				collectionDescription: description.val(),
        				isPublic: ispub
        			},
    			     success:function(response) {
    			    	 	console.log(response);		
    	    				alert(response.result);
    	    				collPopup.dialog("close");
    			  
    			    }								   
    			    });
    		
    		
    		
    	} else {
    		// some error indicating that WITH is not logged in
    		console.log( "Not logged into WITH");
    	}
    }

    collPopup.dialog({
	  dialogClass: "toClose",
	  title: "Create WITH Collection",
	  buttons: [
	    {
	      text: "Create Collection",
	      click: function() {
	    	  // call WITH for token to login
	    	  requestToken( onTokenReceive, withBaseUrl );
	      } 
	    },
	    {
	      text: "Cancel",
	      click: function() {
		        $( this ).dialog("close");
		  }
	    }
	  ]
	});
}










