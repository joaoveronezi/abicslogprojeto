var moduleLogistica = angular.module('moduleLogistica', []);

moduleLogistica.controller('controllerLogistica', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Os valores do custo de logística para a importação do café dos países abaixo. ' +
			'Fonte: CECAFE';
	$scope.caminhoVideo = 'assets/videos/log.mp4';

	$scope.cotacaoLogisticaList = [];
	$scope.valorMercadoriaCotacaoList = [];
	$scope.valorCustoImportacaoCotacaoList = [];

//	$scope.cotacaoInternacionalList = [{nome:'Vietnam'}, {nome:'Indonésia'}, {nome:'Uganda'}, {nome:'Bolsa de Londres'}];
	$scope.cotacaoInternacionalList = [{nome:'Vietnam'}, {nome:'Indonésia'}, {nome:'Bolsa de Londres'}];
	
	$scope.dataCambioIsOpen = false;
	$scope.objetoDataCambioSelecionada = moment().add(-1, 'days').toDate();

	$scope.openDataCambio = function() {
		$scope.dataCambioIsOpen = true;
	};
	
	$scope.changeDataCambio = function() {
		changeDataCambio();
	};
	
	changeDataCambio();
	
	function changeDataCambio() {
		$scope.dataSelecionada = $.datepicker.formatDate($scope.objetoDataCambioSelecionada);

		$http.get('/logisticaController/findCambioByData/' + $scope.objetoDataCambioSelecionada.getTime()).success(function(cambioResponse) {
			$scope.cambio = cambioResponse;
		});
		
		$http.get('/logisticaController/findValorMercadoria/' + $scope.objetoDataCambioSelecionada.getTime()).success(function(responseList) {
			$scope.valorMercadoriaCotacaoList = responseList;
		});
	};
	
	function valoresDefault() {
		
//		RelItemValor.Unidade porcentagem 0 real 1 dolar 2
//		2 vietnam, 1 indonesia, 3 uganda, 6 london
		$scope.valorCustoImportacaoCotacaoList = [];
		
		$scope.valorFreteList = [{cotacaoId: 2, unidade: 2, valor: 3100}, {cotacaoId: 1, unidade: 2, valor: 3100}, 
		                         {cotacaoId: 6, unidade: 2, valor: 0}];
		$scope.taxaSeguroList = [{cotacaoId: 2, unidade: 0, valor: 0}, {cotacaoId: 1, unidade: 0, valor: 0}, 
		                         {cotacaoId: 6, unidade: 0, valor: 0}];
		$scope.importacaoList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
		                         {cotacaoId: 6, unidade: 1, valor: 0}];
		$scope.demurrageContainerList = [{cotacaoId: 2, unidade: 2, valor: 0}, {cotacaoId: 1, unidade: 2, valor: 0}, 
		                                 {cotacaoId: 6, unidade: 2, valor: 0}];
		$scope.thcList = [{cotacaoId: 2, unidade: 1, valor: 800}, {cotacaoId: 1, unidade: 1, valor: 800}, 
		                   {cotacaoId: 6, unidade: 1, valor: 0}];
		
		$scope.liberacaoList = [{cotacaoId: 2, unidade: 1, valor: 373}, {cotacaoId: 1, unidade: 1, valor: 373}, 
		                        {cotacaoId: 6, unidade: 1, valor: 0}];
		$scope.desconsolidacaoList = [{cotacaoId: 2, unidade: 2, valor: 0}, {cotacaoId: 1, unidade: 2, valor: 0}, 
		                               {cotacaoId: 6, unidade: 2, valor: 0}];
		$scope.siscargaList = [{cotacaoId: 2, unidade: 2, valor: 0}, {cotacaoId: 1, unidade: 2, valor: 0}, 
		                        {cotacaoId: 6, unidade: 2, valor: 0}];
		$scope.ispsList = [{cotacaoId: 2, unidade: 2, valor: 11}, {cotacaoId: 2, unidade: 1, valor: 11}, 
		                    {cotacaoId: 6, unidade: 2, valor: 0}];
		$scope.siscomexList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
		                        {cotacaoId: 6, unidade: 1, valor: 0}];
		$scope.armazenagemList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
		                          {cotacaoId: 6, unidade: 1, valor: 0}];
		$scope.despachanteList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
		                          {cotacaoId: 6, unidade: 1, valor: 0}];
		$scope.sindicatoAduaneirosList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
		                                   {cotacaoId: 6, unidade: 1, valor: 0}];
		$scope.transportePortoFabricaList = [{cotacaoId: 2, unidade: 1, valor: 83}, {cotacaoId: 1, unidade: 1, valor: 83}, 
		                                      {cotacaoId: 6, unidade: 1, valor: 0}];
		$scope.marinhaMercanteList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
		                              {cotacaoId: 6, unidade: 1, valor: 0}];
		
//		$scope.valorFreteList = [{cotacaoId: 2, unidade: 2, valor: 3100}, {cotacaoId: 1, unidade: 2, valor: 3100}, 
//		                         {cotacaoId: 3, unidade: 2, valor: 1850}, {cotacaoId: 6, unidade: 2, valor: 0}];
//		$scope.taxaSeguroList = [{cotacaoId: 2, unidade: 0, valor: 0}, {cotacaoId: 1, unidade: 0, valor: 0}, 
//		                         {cotacaoId: 3, unidade: 0, valor: 0}, {cotacaoId: 6, unidade: 0, valor: 0}];
//		$scope.importacaoList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
//		                         {cotacaoId: 3, unidade: 1, valor: 0}, {cotacaoId: 6, unidade: 1, valor: 0}];
//		$scope.demurrageContainerList = [{cotacaoId: 2, unidade: 2, valor: 0}, {cotacaoId: 1, unidade: 2, valor: 0}, 
//		                                 {cotacaoId: 3, unidade: 2, valor: 0}, {cotacaoId: 6, unidade: 2, valor: 0}];
//		$scope.thcList = [{cotacaoId: 2, unidade: 1, valor: 800}, {cotacaoId: 1, unidade: 1, valor: 800}, 
//		                  {cotacaoId: 3, unidade: 1, valor: 800}, {cotacaoId: 6, unidade: 1, valor: 0}];
//		
//		$scope.liberacaoList = [{cotacaoId: 2, unidade: 1, valor: 373}, {cotacaoId: 1, unidade: 1, valor: 373}, 
//		                        {cotacaoId: 3, unidade: 1, valor: 373}, {cotacaoId: 6, unidade: 1, valor: 0}];
//		$scope.desconsolidacaoList = [{cotacaoId: 2, unidade: 2, valor: 0}, {cotacaoId: 1, unidade: 2, valor: 0}, 
//		                              {cotacaoId: 3, unidade: 2, valor: 0}, {cotacaoId: 6, unidade: 2, valor: 0}];
//		$scope.siscargaList = [{cotacaoId: 2, unidade: 2, valor: 0}, {cotacaoId: 1, unidade: 2, valor: 0}, 
//		                       {cotacaoId: 3, unidade: 2, valor: 0}, {cotacaoId: 6, unidade: 2, valor: 0}];
//		$scope.ispsList = [{cotacaoId: 2, unidade: 2, valor: 11}, {cotacaoId: 2, unidade: 1, valor: 11}, 
//		                   {cotacaoId: 3, unidade: 2, valor: 11}, {cotacaoId: 6, unidade: 2, valor: 0}];
//		$scope.siscomexList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
//		                       {cotacaoId: 3, unidade: 1, valor: 0}, {cotacaoId: 6, unidade: 1, valor: 0}];
//		$scope.armazenagemList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
//		                          {cotacaoId: 3, unidade: 1, valor: 0}, {cotacaoId: 6, unidade: 1, valor: 0}];
//		$scope.despachanteList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
//		                          {cotacaoId: 3, unidade: 1, valor: 0}, {cotacaoId: 6, unidade: 1, valor: 0}];
//		$scope.sindicatoAduaneirosList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
//		                                  {cotacaoId: 3, unidade: 1, valor: 0}, {cotacaoId: 6, unidade: 1, valor: 0}];
//		$scope.transportePortoFabricaList = [{cotacaoId: 2, unidade: 1, valor: 83}, {cotacaoId: 1, unidade: 1, valor: 83}, 
//		                                     {cotacaoId: 3, unidade: 1, valor: 83}, {cotacaoId: 6, unidade: 1, valor: 0}];
//		$scope.marinhaMercanteList = [{cotacaoId: 2, unidade: 1, valor: 0}, {cotacaoId: 1, unidade: 1, valor: 0}, 
//		                              {cotacaoId: 3, unidade: 1, valor: 0}, {cotacaoId: 6, unidade: 1, valor: 0}];
	};
	
	valoresDefault();
	$scope.valoresDefault = function() {
		valoresDefault();
//		simularLogistica();
		
		angular.forEach($scope.valorMercadoriaCotacaoList, function(valorMercadoriaCotacao, key) {
			valorMercadoriaCotacao.valorCustoLogisticaReal = valorMercadoriaCotacao.valorCustoImportacaoReal - valorMercadoriaCotacao.valorMercadoriaReal;
			valorMercadoriaCotacao.valorCustoLogisticaDolar = valorMercadoriaCotacao.valorCustoImportacaoDolar - valorMercadoriaCotacao.valorMercadoriaDolar;
		});
		
		$scope.valorCustoImportacaoCotacaoList = $scope.valorMercadoriaCotacaoList;
	};
	
	$scope.simularLogistica = function() {
		simularLogistica();
	};
	
	function simularLogistica() {
		
		$scope.valorCustoImportacaoCotacaoList = [];
		
		angular.forEach($scope.valorMercadoriaCotacaoList, function(valorMercadoriaCotacao, key) {
			
			var valorCustoLogisticaReal = 0;
			var valorCustoLogisticaDolar = 0;
			
//			angular.forEach($scope.valorFreteList, function(valorFrete, key) {
//				
//				if(valorMercadoriaCotacao.cotacaoId == valorFrete.cotacaoId) {
//					if(valorFrete.valor == undefined || valorFrete.valor == '') {
//						valorFrete.valor = 0;
//					}
//					valorFrete.valorLogistica = parseFloat(valorFrete.valor.toString().replace(',','.'));
//					valorCustoLogisticaReal = valorCustoLogisticaReal + valorFrete.valorLogistica;
//					valorFrete.valorLogistica = valorFrete.valorLogistica * $scope.cambio.valorDolar;
//					valorCustoLogisticaDolar = valorCustoLogisticaDolar + valorFrete.valorLogistica;
//				}
//			});
//			
//			angular.forEach($scope.taxaSeguroList, function(taxaSeguro, key) {
//			});
//			
//			angular.forEach($scope.importacaoList, function(importacao, key) {
//				
//				if(valorMercadoriaCotacao.cotacaoId == importacao.cotacaoId) {
//					if(importacao.valor == undefined || importacao.valor == '') {
//						importacao.valor = 0;
//					}
//					importacao.valorLogistica = parseFloat(importacao.valor.toString().replace(',','.'));
//					valorCustoLogisticaReal = valorCustoLogisticaReal + importacao.valorLogistica;
//					importacao.valorLogistica = importacao.valorLogistica / $scope.cambio.valorDolar;
//					valorCustoLogisticaDolar = valorCustoLogisticaDolar + importacao.valorLogistica;
//				}
//			});
//			
//			angular.forEach($scope.demurrageContainerList, function(demurrageContainer, key) {
//				
//				if(valorMercadoriaCotacao.cotacaoId == demurrageContainer.cotacaoId) {
//					if(demurrageContainer.valor == undefined || demurrageContainer.valor == '') {
//						demurrageContainer.valor = 0;
//					}
//					demurrageContainer.valorLogistica = parseFloat(demurrageContainer.valor.toString().replace(',','.'));
//					valorCustoLogisticaReal = valorCustoLogisticaReal + demurrageContainer.valorLogistica;
//					demurrageContainer.valorLogistica = demurrageContainer.valorLogistica * $scope.cambio.valorDolar;
//					valorCustoLogisticaDolar = valorCustoLogisticaDolar + demurrageContainer.valorLogistica;
//				}
//			});
//			
//			angular.forEach($scope.thcList, function(thc, key) {
//				
//				if(valorMercadoriaCotacao.cotacaoId == thc.cotacaoId) {
//					if(thc.valor == undefined || thc.valor == '') {
//						thc.valor = 0;
//					}
//					thc.valorLogistica = parseFloat(thc.valor.toString().replace(',','.'));
//					valorCustoLogisticaReal = valorCustoLogisticaReal + thc.valorLogistica;
//					thc.valorLogistica = thc.valorLogistica / $scope.cambio.valorDolar;
//					valorCustoLogisticaDolar = valorCustoLogisticaDolar + thc.valorLogistica;
//				}
//			});
			
			var retorno = {};
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.valorFreteList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
//			calcularValorLogistica(valorMercadoriaCotacao, $scope.taxaSeguroList, $scope.cambio, valorCustoLogisticaReal, valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.importacaoList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.demurrageContainerList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.thcList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			
//			console.log(valorCustoLogisticaReal);
//			console.log(valorCustoLogisticaDolar);
			
			//ADUANEIRAS
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.liberacaoList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.desconsolidacaoList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.siscargaList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.ispsList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.siscomexList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.armazenagemList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.despachanteList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.sindicatoAduaneirosList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.transportePortoFabricaList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			retorno = calcularValorLogistica(valorMercadoriaCotacao, $scope.marinhaMercanteList, $scope.cambio);
			valorCustoLogisticaReal += Number(retorno.valorCustoLogisticaReal);
			valorCustoLogisticaDolar += Number(retorno.valorCustoLogisticaDolar);
			
//			console.log(valorCustoLogisticaReal);
//			console.log(valorCustoLogisticaDolar);
			
			var objetoCustoImportacao = {};
			objetoCustoImportacao.valorCustoImportacaoReal = Number(valorMercadoriaCotacao.valorMercadoriaReal);
			objetoCustoImportacao.valorCustoImportacaoDolar = Number(valorMercadoriaCotacao.valorMercadoriaDolar);
			
			valorCustoLogisticaReal = Number(valorCustoLogisticaReal / 320).toFixed(3);
			valorCustoLogisticaDolar = Number(valorCustoLogisticaDolar / 320).toFixed(3);
			objetoCustoImportacao.valorCustoLogisticaReal = Number(valorCustoLogisticaReal);
			objetoCustoImportacao.valorCustoLogisticaDolar = Number(valorCustoLogisticaDolar);
			
			objetoCustoImportacao.valorCustoImportacaoReal = Number(objetoCustoImportacao.valorCustoImportacaoReal) + Number(valorCustoLogisticaReal);
			objetoCustoImportacao.valorCustoImportacaoDolar = Number(objetoCustoImportacao.valorCustoImportacaoDolar) + Number(valorCustoLogisticaDolar);
//			console.log(objetoCustoImportacao);
			$scope.valorCustoImportacaoCotacaoList.push(objetoCustoImportacao);
		});
	}
	
	function calcularValorLogistica(valorMercadoriaCotacaoParametro, listParametro, cambioParametro) {
		
		var objRetorno = {}
		objRetorno.valorCustoLogisticaReal = 0;
		objRetorno.valorCustoLogisticaDolar = 0;
		
		angular.forEach(listParametro, function(objeto, key) {
			
			if(valorMercadoriaCotacaoParametro.cotacaoId == objeto.cotacaoId) {
				if(objeto.valor == undefined || objeto.valor == '') {
					objeto.valor = 0;
				}
				objeto.valorLogistica = parseFloat(objeto.valor.toString().replace(',','.'));
//				objRetorno.valorCustoLogisticaReal = objRetorno.valorCustoLogisticaReal + objeto.valorLogistica;
//				RelItemValor.Unidade porcentagem 0 real 1 dolar 2
				if(objeto.unidade == 2) {
					objRetorno.valorCustoLogisticaDolar = objRetorno.valorCustoLogisticaDolar + objeto.valorLogistica;
					objeto.valorLogistica = Number(objeto.valorLogistica * cambioParametro.valorDolar).toFixed(3);
					objRetorno.valorCustoLogisticaReal = objRetorno.valorCustoLogisticaReal + objeto.valorLogistica;
				} else if(objeto.unidade == 1) {
					objRetorno.valorCustoLogisticaReal = objRetorno.valorCustoLogisticaReal + objeto.valorLogistica;
					objeto.valorLogistica = Number(objeto.valorLogistica / cambioParametro.valorDolar).toFixed(3);
					objRetorno.valorCustoLogisticaDolar = objRetorno.valorCustoLogisticaDolar + objeto.valorLogistica;
				}
				
//				objRetorno.valorCustoLogisticaDolar = objRetorno.valorCustoLogisticaDolar + objeto.valorLogistica;
			}
		});
//		console.log(objRetorno);
		return objRetorno;
	};
});