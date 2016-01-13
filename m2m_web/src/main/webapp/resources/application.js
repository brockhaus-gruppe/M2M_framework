var selectedSensors = [],
	getSensorsURL = "http://localhost:8080/sensorData/rest/config/getConfig4Element/sensors",
	dataFromSensorsURL = "http://localhost:8080/sensorData/SensorDataServer",
	chart;



//Transforms timeStamp from string to timeStamp standard format
function parseTimeStamp(time){
	var elems = time.split(/,|:|-/),
	    d = new Date(elems[0], elems[1], elems[2], elems[3], elems[4], elems[5]); // Date(month, day, year, hour, minutes, seconds);
	return (d.getTime()); 
}



//@param: arrayOfvariables, contains a list of variables names.
function generateOptions(arrayOfvariables,maxRange){

	var options  = {
	  chart : {
	    renderTo: 'dataChart',
	    type : 'line',
	    events: {
		        load: function(){
				chartData = this.series;
			}	
				
		    }
	  },
	 
	  title : {
	    text : 'Sensors data'
	  },
	  xAxis : {
	    type : 'datetime',
	    minRange :  60*1000
	  },
	  yAxis : {
	    title : {
	      text : "value"
	    },
	    max: maxRange
	  },
	   legend: {
	   	 	align: 'right',
            verticalAlign: 'top',
            layout: 'vertical',
            x: 0,
            y: 100
        },
	  exporting: {
            enabled: false
          },
	  plotOptions : {
	    series : {
	      threshold : 0,
	      marker : {
		enabled : false
	      }
	    }
	  },
	  series : (function () {
		       
			var series = [];
		        for (i = 0; i < arrayOfvariables.length; i += 1) {
			series.push({ id:arrayOfvariables[i]  ,name : arrayOfvariables[i], data : [] });
			}
		        
		        return series;
		    }())
	}	

return options;
} 

function getSensors(URL){
	selectedSensors = []; //clean selected sensors list
	
	var	sensorList =[],
     	s = $("#sensor_list ul");
    
	s.empty(); //cleaning the old elements
    
	/////////
    $.ajaxSetup({
        async: false
    });

    ////
	$.getJSON( URL, function( data ) {
  	
	  	$.each( data, function( key, val ) {
	  		var res = key.split("."),
	  		     id = res[0]+"-"+res[1]+"-"+res[2]+"-"+res[3];
	  		sensorList.push(key);
		 	s.append( "<li><a href='#' id="+ id+" > ."+res[2]+"."+res[3]+"</a></li>");
	
		    $("#"+id+"").click(function() { //adding thee venti
		 		var parsedID = this.id.replace(/\-/g,'.'); //in DOM ther is problems with "." ands we have "-"
		 		//selectedSensors.push(parsedID); 
		 		if(!isSensorInSelected(parsedID)){//if is not already selected
		 			selectedSensors.push(parsedID); 
		 			
		 		}else{
		 			selectedSensors.splice(getIndexofElement(parsedID),1);
		 		}
		 		$( this ).toggleClass( "bt-selected" );
	     		
			 }); 
	  	});
	 
	});
/////////////////
    $.ajaxSetup({
        async: true
    });

//////////////////////7    
	return sensorList
}

function parseSensorId(id){

}
function setSensorsList(){
	 getSensors(getSensorsURL);

}


//creates a new chart 
function setChart(Sen_IDs){
	
	chart = new Highcharts.Chart(generateOptions(Sen_IDs,1));
    
	getSensorDataFromHTTP(dataFromSensorsURL);
}




function loadListeners(){
	//events
$("#get_sensors").click(function() {
     setSensorsList();
	}); 

$("#table_gen").click(function() {
  	setChart(selectedSensors);
	}); 
}

function isSensorInSelected(id){
	for(var i=0; i < selectedSensors.length; i += 1) {
		if(selectedSensors[i]===id) return true;
	}
	
	return false;
}

function getIndexofElement(id){ //in selectedSensors
	for(var i=0; i < selectedSensors.length; i += 1) {
		if(selectedSensors[i]===id) return i;
	}
	return -1;
}
function getSensorsFromJSON(jsonData){
		
		
		if (isSensorInSelected(jsonData.sensorId)){
			var parsedTime = parseTimeStamp(jsonData.time),
				chartDataIndex = getIndexofElement(jsonData.sensorId),
				point = [parsedTime,  parseFloat(jsonData.value)], 
				Shift = false; //chartData[chartDataIndex].data.length > 70;
			
			console.log("EEEE"+jsonData.sensorId + "value:" +jsonData.value +"DataCHartINDEX: "+chartDataIndex);
			chartData[chartDataIndex].addPoint(point, true, Shift); 
		}

 }
	

//@parman data_url -> server url: "http://localhost:8080/sensorData/SensorDataServer"
function getSensorDataFromHTTP(data_url){
 
 if (typeof (EventSource) !== "undefined") {

		   var source = new EventSource(data_url);
		   
		   source.onmessage = function(event) {
			
			var dataJSONObject = jQuery.parseJSON(event.data); 
		    
		    getSensorsFromJSON(dataJSONObject);  
		   
		   };
	} else {
		  	alert("Sorry, your browser does not support server-sent events...");
	}
}


function init(){
   Highcharts.setOptions({
        global : {
            useUTC : false
        }
   });
  
   loadListeners();
}


$(function(){ 

init();

//setChart(SelectedSensors);
//requestData();//testing

});

