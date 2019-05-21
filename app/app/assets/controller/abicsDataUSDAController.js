var moduleAbicsDataUSDA = angular.module('moduleAbicsDataUSDA', []);

moduleAbicsDataUSDA.controller('controllerAbicsDataUSDA', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Tela de informações de comercialização dos países. Fonte USDA, com dados anuais.'+
	'A quantidade representa o volume total de sacas comercializados para todos países.'+
	'\nPara consultar escolha o(s) pais(es), o(s) ano(s) e qual tipo de café.';
	$scope.caminhoVideo = 'assets/videos/abicsDataUSDA.mp4';
	
	/**USDA**/
	$scope.paisListUSDA = [];
	$scope.anoListUSDA = [];
//	$scope.modelRadioCommodity = 'ROBUSTA'
	$scope.modelRadioCommodity = 'GRAO'

	$scope.paisSelecionadoListUSDA = [];
	$scope.anosSelecionadoListUSDA = [];
	
	$http.get('/abicsDataUSDAController/findDistinctPais').success(function(responseList) {
		$scope.paisListUSDA = responseList;
	});

	$http.get('/abicsDataUSDAController/findDistinctAno').success(function(responseList) {
		$scope.anoListUSDA = responseList;
	});
	
	$scope.changeModelRadio = function() {
		
		try {
			graficoUSDAConsume.destroy();
			graficoUSDAImportacao.destroy();
			graficoUSDAExportacao.destroy();
		} catch (exception) {

		}
	};
	
	$scope.consultarUSDA = function() {

		var tipoParametro = 'production';
		if($scope.modelRadioCommodity.toLowerCase() == 'soluble') {
			tipoParametro = 'consume domestic';
		}
		
		var objConsulta = new FormData();
		objConsulta.append('anos_list', $scope.anosSelecionadoListUSDA);
		objConsulta.append('pais_list', $scope.paisSelecionadoListUSDA);
		objConsulta.append('commodity', $scope.modelRadioCommodity);
		
		if($scope.modelRadioCommodity.toLowerCase() != 'grao') {
			
			$('#divConsumeUSDA').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
			$http.post('/abicsDataUSDAController/graficoUSDA/'+tipoParametro+'', objConsulta, 
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
		}
		if($scope.modelRadioCommodity.toLowerCase() != 'arabica'
			&& $scope.modelRadioCommodity.toLowerCase() != 'robusta') {
			
			$('#divImportacaoUSDA').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
			$http.post('/abicsDataUSDAController/graficoUSDA/import', objConsulta, 
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
		}
		
		if($scope.modelRadioCommodity.toLowerCase() != 'arabica'
			&& $scope.modelRadioCommodity.toLowerCase() != 'robusta') {
			$('#divExportacaoUSDA').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
			$http.post('/abicsDataUSDAController/graficoUSDA/export', objConsulta, 
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
		}
	};
});
