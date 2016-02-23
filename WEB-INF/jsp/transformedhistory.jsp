<head>
<!--Load the AJAX API-->
<style type="text/css">
#chart{
    width: 90%;
    margin: 0 auto;
    height: 90%;
    }
    #grouped_table{
    width: 60%;
    height: 90%;
    margin: 0 auto;
    }
#grouped_table1{
    width: 90%;
    margin: 0 auto;
    }
</style>


<script type="text/javascript">
     $(function(){	 
    	 var self = this;
    	 var content = $('#chart');
    	 var panel = content.closest('div[id^="kp"]');
    	 $K.kaiten('maximize', panel);
    	 drawChart()
          function drawChart()
	    {
	    $('#chart').html('<span><img src="images/buttons/loading.gif" alt=""></span>');
	    $('#chart_div').html('<span><img src="images/buttons/loading.gif" alt=""></span>');
	    $('#grouped_table').html('<span><img src="images/buttons/loading.gif" alt=""></span>');
	    $.ajax({
	    type:'GET',
	    url:"UrlApi?isApi=true&action=list&type=Transformation",
	  	data:"organizationId="+<%=request.getAttribute("getOrganizationId()")%>,
	    success:function(data) {
	    	/* console.log(data); */
	    var sets = [];
	    var headers = ["Parent Dataset Name","Date","Total Items","Valid Items","Invalid Items","Target Schema","Mapping"];
	
	    sets.push(headers);
	    for(var i=0; i<data.result.length; i++)
	    {
	    var parent = data.result[i].parentDataset;
	    var date = new Date(data.result[i].lastModified);
	    var items = data.result[i].itemCount;
	    var valid = data.result[i].validItems;
	    var invalid = data.result[i].invalidItems;
	    var target = data.result[i].targetSchema;
	    var mapping = data.result[i].mappingUsed;
	    sets.push([parent, date,items,valid,invalid,target,mapping]);
	    }   
	
	    var newdata = google.visualization.arrayToDataTable(sets);
	    
        
		var options = {'showRowNumber': true,'title': '<%=request.getAttribute("getName()")%>'};	    
	    var table = new google.visualization.Table(document.getElementById('chart'));
	    table.draw(newdata, options);
	    
	    var grouped_dt = google.visualization.data.group(
      	      newdata, [5],[{'column': 3, 'aggregation': google.visualization.data.sum, 'type': 'number'}]);
      
      var grouped_table = new google.visualization.ColumnChart(document.getElementById('grouped_table'));
      grouped_table.draw(grouped_dt, options);
      
	     if (data.result.length !==0){
  	   		 var g_dt = google.visualization.data.group(newdata, [1, 3]);
  	     	 var chart = new google.visualization.ScatterChart(document.getElementById('chart_div'));
      	 	 chart.draw(g_dt, options);      
  	    }
	     else{
	    	 
	    	 $('#chart').html('<span> None </span>');
	    	 $('#chart_div').html('<span> None </span>');
	     }
	     
	     google.visualization.events.addListener(table, 'select', function() {
	    	  chart.setSelection(table.getSelection());
	    	});

	    	google.visualization.events.addListener(chart, 'select', function() {
	    	  table.setSelection(chart.getSelection());
	    	});
	     
	    }
	    });
	    }     
     })
    </script>
</head>


<div class="panel-body" style="height: 100%; width: 100%">

	<div class="block-nav" style="height: 100%; width: 100%" >
		<div class="summary">
			<div class="label">
			Transformations History : <%=request.getAttribute("getName()")%>
			</div>
			<div class="info"><br/></div>
		</div>
		
        <div id="grouped_table1" style="width: 960px; height: 900px;">

       		  		  
  		    <h3>Transformations History</h3>	
   		 <div id="chart_div" style="height: 300px;"> None </div>
			<h3>Transformations</h3>	
			<div id="chart" style="height: 300px;width: 960px;" ></div>	  
			<h3>Transformed items per schema</h3>	
  		  <div id="grouped_table"style="height: 300px; "> None </div>
  		  
       </div>
    	
       		
	</div>
</div>
