var moduleTemplateAbicsData = angular.module('moduleTemplateAbicsData', []);

moduleTemplateAbicsData.controller('controllerTemplateAbicsData', function($scope, $http, $timeout, $modal, $location){
	
	$("#abicsDropDown").removeClass("ng-hide");
	$("#logDropDown").removeClass("ng-hide");
//	$("#logDropDown").addClass("ng-hide");
	
//	$scope.acordoComercial = function() {
//		$location.path('abicsData');
//	};
//	
//	$scope.relatorioBrasil = function() { 
//		$location.path('abicsDataRelatorioBrasil');
//	};
//	
//	$scope.relatorioMundo = function() {
//		$location.path('abicsDataRelatorio');
//	};
//	
//	$scope.abicsEmpresaProduto = function() {
//		$location.path('abicsDataEmpresaProduto');
//	};
//	
	$scope.mudarPagina = function(caminho) {
		$location.path(caminho);
	};
	
	$scope.tipoRelatorioSelecionadoList = [];
	$scope.anoRelatorioGeralList = [];
	
	var anoInicialInt = 1995;
	var anoFinalInt = new Date().getFullYear();
	var anoLength = anoFinalInt - anoInicialInt;
	
	$scope.anoRelatorioGeralInicial = anoFinalInt.toString();
	
	for(i=0; i<=anoLength; i++) {
		$scope.anoRelatorioGeralList.push(anoInicialInt.toString());
		anoInicialInt++;
	}
	
	$scope.anoRelatorioGeralList.reverse();
	
	$scope.mesRelatorioGeralList = [{"id" : "1", "nome" : "janeiro"}, {"id" : "2", "nome" : "fevereiro"}, {"id" : "3", "nome" : "março"},
		       	                     {"id" : "4", "nome" : "abril"}, {"id" : "5", "nome" : "maio"}, {"id" : "6", "nome" : "junho"}, 
		       	                     {"id" : "7", "nome" : "julho"}, {"id" : "8", "nome" : "agosto"}, {"id" : "9", "nome" : "setembro"},
		       	                     {"id" : "10", "nome" : "outubro"}, {"id" : "11", "nome" : "novembro"}, {"id" : "12", "nome" : "dezembro"}];
		
	$scope.mesRelatorioGeralInicial = (new Date().getMonth()).toString();
	
	angular.forEach($scope.mesRelatorioGeralList, function(value, key) {
		if(value.id == $scope.mesRelatorioGeralInicial) {
			$scope.mesRelatorioGeralInicial = $scope.mesRelatorioGeralList[key];
		}
	});
	
	$scope.tipoRelatorioList = ["Empresa","País","UE", "Produto"];
	
	$scope.clickCheckboxAll = function() {
		
		if($scope.checkboxAll) {
			$scope.tipoRelatorioSelecionadoList = $scope.tipoRelatorioList;
			
		} else {
			$scope.tipoRelatorioSelecionadoList = [];
		}
	};
	
	$scope.clickCheckboxRelatorio = function() {
		
		if($scope.tipoRelatorioSelecionadoList.length == $scope.tipoRelatorioList.length) {
			$scope.checkboxAll = true;
			
		} else {
			$scope.checkboxAll = false;
		}
	};
	
	$scope.imprimirRelatorioGeral = function() {
		
		var objConsulta = new FormData();
		objConsulta.append('tipoRelatorio', $scope.tipoRelatorioSelecionadoList);
		objConsulta.append('mes', ($scope.mesRelatorioGeralInicial.id).toString());
		objConsulta.append('ano', $scope.anoRelatorioGeralInicial.toString());
		
		$('#divData').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/templateAbicsDataController/gerarExcel', objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}, responseType: 'arraybuffer'}).success(function(data) {

			   var a = document.createElement("a");
			   document.body.appendChild(a);
			   a.style = "display: none";
			   var file = new Blob([data], {type: 'application/vnd.ms-excel'}); // data.contents
			   var fileURL = URL.createObjectURL(file);
			   a.target = "_blank";
			   a.href = fileURL;
			   var path = 'relatorio_geral';
			   
			   angular.forEach($scope.tipoRelatorioSelecionadoList, function(value, key) {
				   path += "_" + value;
			   });
			   
			   a.download = path + ".xls";
			   
			   a.click();
			   $('#divData').unblock();
		});
	};
	
});