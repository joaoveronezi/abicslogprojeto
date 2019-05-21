var moduleAbicsData = angular.module('moduleAbicsData', []);

moduleAbicsData.controller('controllerAbicsData', function($scope, $http, $timeout, $modal, $location) {

//	d3.select(window).on("resize", throttle);
//
//	var zoom = d3.behavior.zoom()
//	.scaleExtent([1, 9])
//	.on("zoom", move);
//
//	var width = document.getElementById('divD3WordMap').offsetWidth;
//	var height = width / 2;
//
//	var topo,projection,path,svg,g;
//
//	var graticule = d3.geo.graticule();
//
//	var tooltip = d3.select("#divD3WordMap").append("div").attr("class", "tooltip hidden");
//
//	var colordefault = "";
//
//	setup(width,height);
//
//	function setup(width,height){
//		projection = d3.geo.mercator()
//		.translate([(width/2), (height/2)])
//		.scale( width / 2 / Math.PI);
//
//		path = d3.geo.path().projection(projection);
//
//		svg = d3.select("#divD3WordMap").append("svg")
//		.attr("width", width)
//		.attr("height", height)
//		.call(zoom)
//		.on("click", click)
//		.append("g");
//
//		g = svg.append("g");
//
//	}
//
//	d3.json("assets/data/world-topo-min.json", function(error, world) {
//
//		var countries = topojson.feature(world, world.objects.countries).features;
//
//		topo = countries;
//		draw(topo);
//	});
//	
//	function draw(topo) {
//
//		svg.append("path")
//		.datum(graticule)
//		.attr("class", "graticule")
//		.attr("d", path);
//
//		g.append("path")
//		.datum({type: "LineString", coordinates: [[-180, 0], [-90, 0], [0, 0], [90, 0], [180, 0]]})
//		.attr("class", "equator")
//		.attr("d", path);
//
//		var country = g.selectAll(".country").data(topo);
//
//		country.enter().insert("path")
//		.attr("class", "country")
//		.attr("d", path)
//		.attr("id", function(d,i) { return d.id; })
//		.attr("data-comtrade", function(d,i) { return d.properties.comtrade; })
//		.attr("title", function(d,i) { return d.properties.name; })
//		.style("fill", function(d,i) { return d.properties.color; })
//		.on("click", function(d,i) { onClickMapa(d); });
//
//		//offsets for tooltips
//		var offsetL = document.getElementById('divD3WordMap').offsetLeft+20;
//		var offsetT = document.getElementById('divD3WordMap').offsetTop+10;
//
//		//tooltips
//		country.on("mousemove", function(d,i) {
//
////		  var isPaisSelecionado = false;
//	      var mouse = d3.mouse(svg.node()).map( function(d) { return parseInt(d); } );
//	      var tooltipStr = '';
//
//	      console.log(d.properties)
//	      
////	      console.log('mousemove');
////	      $scope.exportacaoAbicsMesDataList = [];
//	      
////	      $scope.exportacaoAbicsMesList.forEach(function(abicsData) {
////	    	  console.log('abicsData');
////	    	  console.log(abicsData);
////	    	  console.log('******************');
////	    	  console.log(d.properties.abics);
////	    	  if(abicsData.paisId == d.properties.abics) {
////	    		  $scope.exportacaoAbicsMesDataList.push(abicsData);
////	    	  }
////	      });
//	      
////	      if($scope.exportacaoAbicsMesDataList.length != 0) {
////	    	  tooltipStr = '<table class="table">'+
////				'<thead>'+
////	    			'<tr>'+
////	    				'<th style="color:red;">'+ d.properties.name +'</th>'+
////	    			'</tr>'+
////					'<tr>'+
////						'<th>Mês/Ano</th>'+
////						'<th>Receita</th>'+
////						'<th>Peso</th>'+
////						'<th>Saca 60Kg</th>'+
////					'</tr>'+
////				'</thead>'+
////				'<tbody>';
////	    	  
////	    	  $scope.exportacaoAbicsMesDataList.sort(function(a, b){
////	    		    return a.mes-b.mes
////	    	  })
////	    	  
////			  for(var j = 0; j < $scope.exportacaoAbicsMesDataList.length; j++) {
////				  	tooltipStr += '<tr>';
////					var abicsData = $scope.exportacaoAbicsMesDataList[j];
////			  		tooltipStr += '<td>'+ abicsData.mes + "/" + abicsData.ano +'</td>'+
////			  					  '<td>'+ abicsData.receita +'</td>'+
////			  					  '<td>'+ abicsData.peso +'</td>' +
////			  					  '<td>'+ abicsData.saca +'</td>';
////			  }
////			  tooltipStr += '</tr>';
////			  tooltipStr += '</tbody>';
////			  tooltipStr += '</table>';
////	      }
//	    	  
////	      $scope.comtradeExportacaoList.forEach(function(comtradeData) {
////
////	    	  console.log('comtradeData')
////	    	  console.log(comtradeData)
////	    	  console.log('******************')
////	    	  console.log(d.properties.comtrade);
////	    	  
////	    	  if(comtradeData.ptCode == d.properties.comtrade) {
////
////	    		  console.log('entrouuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu');
////	    		  
//////	    		isPaisSelecionado = true;
////	    		tooltipStr = '<table class="table">'+
////		      			'<thead>'+
////			      			'<tr>'+
////			      				'<th style="color:red;">'+ d.properties.name +'</th>'+
////			      			'</tr>'+
////		      				'<tr>'+
////		      					'<th>Mês</th>'+
////		      					'<th>Valor</th>'+
////		      					'<th>Peso</th>'+
////		      				'</tr>'+
////		      			'</thead>'+
////	      			'<tbody>';
////				for(var j = 0; j < comtradeData.comtradeDataSumarizacaoList.length; j++) {
////
////					var comtradeDataSumarizacao = comtradeData.comtradeDataSumarizacaoList[j];
////					tooltipStr +=
////			      				'<tr>'+
////				      				'<td>'+ comtradeDataSumarizacao.period +'</td>'+
////				      				'<td>'+ comtradeDataSumarizacao.tradeValue +'<td>'+
////				      				'<td>'+ comtradeDataSumarizacao.netWeight +'<td>'+
////				      			'</tr>';
////				}
////	    	  }
////	      });
//	      
//	      tooltip.classed("hidden", false)
//	      .attr("style", "left:"+(mouse[0]+offsetL)+"px;top:"+(mouse[1]+offsetT)+"px")
//	      // .html(d.properties.name);
//	      .html(tooltipStr);
//
//		})
//		.on("mouseout", function(d,i) {
//			tooltip.classed("hidden", true);
//		});
//
//		d3.csv("assets/data/country-capitals.csv", function(err, capitals) {
//
//			capitals.forEach(function(i){
//				addpoint(i.CapitalLongitude, i.CapitalLatitude, i.CapitalName );
//			});
//		});
//	}
//
//	function redraw() {
//		width = document.getElementById('divD3WordMap').offsetWidth;
//		height = width / 2;
//		d3.select('svg').remove();
//		setup(width,height);
//		draw(topo);
//	}
//
//	function move() {
//
//		var t = d3.event.translate;
//		var s = d3.event.scale;
//		zscale = s;
//		var h = height/4;
//
//
//		t[0] = Math.min(
//			(width/height)  * (s - 1),
//			Math.max( width * (1 - s), t[0] )
//		);
//
//		t[1] = Math.min(
//			h * (s - 1) + h * s,
//			Math.max(height  * (1 - s) - h * s, t[1])
//		);
//
//		zoom.translate(t);
//		g.attr("transform", "translate(" + t + ")scale(" + s + ")");
//
//		//adjust the country hover stroke width based on zoom level
//		d3.selectAll(".country").style("stroke-width", 1.5 / s);
//
//	}
//
//	var throttleTimer;
//	function throttle() {
//		window.clearTimeout(throttleTimer);
//		throttleTimer = window.setTimeout(function() {
//			redraw();
//		}, 200);
//	}
//
//
//	//geo translation on mouse click in map
//	function click(id) {
//		var latlon = projection.invert(d3.mouse(this));
//	}
//
//	function checkboxSelect(id) {
//		var x = document.querySelectorAll(id);
//
//		console.log(x);
//		colordefault = x.attributes.style;
//		console.log(colordefault);
//
//		for(var i=0; i<x.length; i++) {
//			x[i].style.fill = 'rgb(141, 0, 158)';
//		}
//	}
//
//	//function to add points and text to the map (used in plotting capitals)
//	function addpoint(lat,lon,text) {
//
//		var gpoint = g.append("g").attr("class", "gpoint");
//		var x = projection([lat,lon])[0];
//		var y = projection([lat,lon])[1];
//
//		gpoint.append("svg:circle")
//		.attr("cx", x)
//		.attr("cy", y)
//		.attr("class","point")
//		.attr("r", 1.5);
//
//		//conditional in case a point has no associated text
//		if(text.length>0){
//
//			gpoint.append("text")
//			.attr("x", x+2)
//			.attr("y", y+2)
//			.attr("class","text")
//			.text(text);
//		}
//
//	}
//	
//	function tooltipHide() {
//		
//		document.getElementById("legendaTooltip").setAttribute("style", "display: none");
//	};
//	
//	function tooltipShow(obj) {
//		
//		console.log(obj)
////		var nomePais = obj.path[0].id.substring(7);
//		
////		var tooltipStr = '';
////		
////		tooltipStr= '<table class="table">'+
////			'<thead>'+
////				'<tr>'+
////					'<th style="color:red;">'+nomePais+'</th>'+
////				'</tr>'+
////				'<tr>'+
////					'<th>Mês/Ano</th>'+
////					'<th>Receita</th>'+
////					'<th>Peso</th>'+
////					'<th>Saca 60Kg</th>'+
////				'</tr>'+
////			'</thead>';
//		
//		
//		
////		$scope.exportacaoAbicsMesList.sort(function(a, b){
////		    return a.mes-b.mes
////		})
////		
////		for(var i=0; i<$scope.exportacaoAbicsMesList.length; i++) {
////			if($scope.exportacaoAbicsMesList[i].paisNomeComtrade === nomePais){
////				tooltipStr += '<tr>';
////				var abicsData = $scope.exportacaoAbicsMesList[i];
////		  		tooltipStr += '<td>'+ abicsData.mes + "/" + abicsData.ano +'</td>'+
////		  					  '<td>'+ abicsData.receita +'</td>'+
////		  					  '<td>'+ abicsData.peso +'</td>' +
////		  					  '<td>'+ abicsData.saca +'</td>';
////			}
////		}
////		tooltipStr += '</tr>';
////		tooltipStr += '</tbody>';
////		tooltipStr += '</table>';
////		
////		document.getElementById("legendaTooltip").innerHTML = tooltipStr;
////		document.getElementById("legendaTooltip").setAttribute("style", "display: show; left: " + obj.clientX + "px;");
//	};
//	
//	function onClickMapa(d) {
//		
//		console.log('click map');
//		console.log(d);
//	}
	
	$scope.voltarTemplate = function() {
		$location.path('templateAbicsData');
	};
	
	$scope.informacoes = 'Tela de informações de Exportação e Importação do Brasil para o cada país do mundo, ' + 
			'com período mensal. As fontes são: ABICS, dados de Exportação. COMTRADE, dados de Exportação e Importação.' +
			'Primeiro escolha o tipo de distribuição, selecionando os países ou escolhendo os X maiores. Escolha o(s) ano(s).'+
			'Escolha o(s) pais(es), ou os X maiores.';
	$scope.caminhoVideo = 'assets/videos/dashboard.mp4';
	
	$scope.dataAcordosComerciaisList = ['09-2016', '10-2016', '01-2017']
	$scope.paisList = [];
	$scope.relPaisAcordoComercialList = [];
//	$scope.listaGraficosAbics = [];
//	$scope.tipoMostrar = -1;
//	$scope.legendaAbics = false;
//	$scope.legendaComtrade = false;
//	$scope.exportacaoAbicsList = [];
//	$scope.exportacaoAbicsMesList = [];
//	$scope.exportacaoAbicsMesDataList = [];
	
	$http.get('/abicsDataController/findPaisAcordoComercial/' + $scope.dataAcordosComerciaisList[0]).success(function(listResponse) {
		
		$scope.paisList = listResponse;
	});
	
	$scope.consultar = function() {

		console.log("id" + $scope.paisSelecionadoId);
//		console.log($scope.paisSelecionado["id"]);
		
		$http.get('/abicsDataController/findAcordoComercial/' + $scope.dataAcordoComercial + '/' + $scope.paisSelecionadoId).success(function(listResponse) {
			$scope.relPaisAcordoComercialList = listResponse;
			$("#tabelaAcordos").addClass("table-overflow");
		});
//		onClickMapa();
	}

	$scope.downloadPlanilhaAcordoComercial = function() {
		$http.get('/abicsDataController/downloadPlanilhaAcordoComercial/' + $scope.dataAcordoComercial, {responseType: 'arraybuffer'}).success(function(data) {
	
			   var a = document.createElement("a");
			   document.body.appendChild(a);
			   a.style = "display: none";
	//		   var file = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}); // data.contents
			   var file = new Blob([data], {type: 'application/vnd.ms-excel'}); // data.contents
			   var fileURL = URL.createObjectURL(file);
			   a.target = "_blank";
			   a.href = fileURL;
			   a.download = "planilha_acordo_comercial_data_"+$scope.dataAcordoComercial+".xls";
			   a.click();
//			   $('#divHistorico').unblock();
		});
	};
	
//	$scope.resetColor = function(){
//		var country = g.selectAll(".country").data(topo);
//		country.style("fill", function(d, i) { return d.properties.color; })
//
//		$('#export').empty();
//		$('#import').empty();
//	}
	
});
