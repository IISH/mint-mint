<head>
<!--Load the AJAX API-->

<style type="text/css">
#grouped_table{
    width: 80%;
    margin: 0 auto;
    }
    #chart{
    width: 80%;
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
	    
	    $.ajax({
	    type:'GET',
	    url:"UrlApi?isApi=true&action=list&type=DataUpload",
	  	data:"organizationId="+<%=request.getAttribute("getOrganizationId()")%>,
	    success:function(data) {
	    var sets = [];
	    var headers = ["Name","Date","Items","Invalid items"];    
	    sets.push(headers);

	  
	    
	    for(var i=0; i<data.result.length; i++)
	    {
	    	var name = data.result[i].name;
	        var date = new Date(data.result[i].lastModified);
	        var items = 0;
	        if (data.result[i].itemCount != -1) {
	        	items = data.result[i].itemCount;
	        }
	        var invalid = 0;
	        if (data.result[i].invalidItems != -1) {
	        	invalid = data.result[i].invalidItems;
	        }


	        sets.push([name, date,items,invalid]);
	    }   
	
	    var newdata = google.visualization.arrayToDataTable(sets);

	    
		var options = {'title': 'Imports : <%=request.getAttribute("getName()")%>'};

	    var table = new google.visualization.Table(document.getElementById('chart'));
	    
	    table.draw(newdata, options);
	 
	    
		if (data.result.length !==0){
	    var grouped_dt = google.visualization.data.group(newdata, [1, 2]);
	    var chart = new google.visualization.ScatterChart(document.getElementById('chart_div'));
        chart.draw(grouped_dt, options);
	    }
	    }
	    });	    
	    }
      
	 })
    </script>
</head>


<div class="panel-body">

<div class="block-nav" style="height: 100%; width: 100%" >
		<div class="summary">
			<div class="label">
			Imports History : <%=request.getAttribute("getName()")%>
		</div>
		<div class="info"><br/></div>
		 </div>
		 <div id="grouped_table" style = "width: 960px;" >
		 <!--  -->	
		<h3>Imports History</h3>	
      	<div id="chart_div" style = "height: 300px;"></div>	
		<h3>Imports</h3>
		<div id="chart" style = "width: 960px; height: 300px;" >
		</div>
		</div>
	</div>
</div>
