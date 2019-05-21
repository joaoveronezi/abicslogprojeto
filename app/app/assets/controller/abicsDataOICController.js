var moduleAbicsDataOIC = angular.module('moduleAbicsDataOIC', []);

moduleAbicsDataOIC.controller('controllerAbicsDataOIC', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Tela de informações de comercialização dos países. Fonte USDA e ICO, com dados anuais.'+
	'Para USDA, temos o volume de sacas comercializados.'+
	'Para ICO, em estudo.'+
	'Escolha o(s) pais(es) e o(s) ano(s).';
	$scope.caminhoVideo = 'assets/videos/dashboard.mp4';
	
	/**USDA**/
	$scope.paisListUSDA = [];
	$scope.anoListUSDA = [];

	$http.get('/abicsDataMundoController/findDistinctPais').success(function(responseList) {
		$scope.paisListUSDA = responseList;
	});

	$http.get('/abicsDataMundoController/findDistinctAno').success(function(responseList) {
		$scope.anoListUSDA = responseList;
	});
	
	$scope.paisSelecionadoListUSDA = [];
	$scope.anosSelecionadoListUSDA = [];
	
	$scope.consultarUSDA = function() {
		
//		console.log($scope.paisSelecionadoListUSDA);
//		console.log('-------');
//		console.log($scope.anosSelecionadoListUSDA);
		
		var objConsulta = new FormData();
		objConsulta.append('anos_list', $scope.anosSelecionadoListUSDA);
		objConsulta.append('pais_list', $scope.paisSelecionadoListUSDA);
		
		$('#divImportacaoUSDA').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataMundoController/graficoUSDA/import', objConsulta, 
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
						
			try {
				graficoUSDAImportacao.destroy();
			} catch (exception) {

			}

			graficoUSDAImportacao = c3.generate({
				data: {
					columns: [],
					type : 'line'
				},
				axis: {
					x: {
						type: 'category',
						tick: {
			               rotate: 75,
			               multiline: false
						},
						height: 80
					}
				},
				grid: {
					y : {show: true}
				}
			});

			$("#graficoUSDAImportacao").append(graficoUSDAImportacao.element);
			graficoUSDAImportacao.categories(listResponse.categories);
			graficoUSDAImportacao.load(listResponse);

			$('#divImportacaoUSDA').unblock({});
		});
		
		$('#divExportacaoUSDA').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataMundoController/graficoUSDA/export', objConsulta, 
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
						
			try {
				graficoUSDAExportacao.destroy();
			} catch (exception) {

			}

			graficoUSDAExportacao = c3.generate({
				data: {
					columns: [],
					type : 'line'
				},
				axis: {
					x: {
						type: 'category',
						tick: {
			               rotate: 75,
			               multiline: false
						},
						height: 80
					}
				},
				grid: {
					y : {show: true}
				}
			});

			$("#graficoUSDAExportacao").append(graficoUSDAExportacao.element);
			graficoUSDAExportacao.categories(listResponse.categories);
			graficoUSDAExportacao.load(listResponse);

			$('#divExportacaoUSDA').unblock({});
		});
		
		$('#divConsumeUSDA').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataMundoController/graficoUSDA/consume', objConsulta, 
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
						
			try {
				graficoUSDAConsume.destroy();
			} catch (exception) {

			}

			graficoUSDAConsume = c3.generate({
				data: {
					columns: [],
					type : 'line'
				},
				axis: {
					x: {
						type: 'category',
						tick: {
			               rotate: 75,
			               multiline: false
						},
						height: 80
					}
				},
				grid: {
					y : {show: true}
				}
			});

			$("#graficoUSDAConsume").append(graficoUSDAConsume.element);
			graficoUSDAConsume.categories(listResponse.categories);
			graficoUSDAConsume.load(listResponse);

			$('#divConsumeUSDA').unblock({});
		});
		
	};
	
	/**USDA**/
	
});
