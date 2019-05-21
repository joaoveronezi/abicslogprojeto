var moduleSumarizacaoSincronizacao = angular.module('moduleSumarizacaoSincronizacao', []);

moduleSumarizacaoSincronizacao.controller('controllerSumarizacaoSincronizacao', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Demonstrativo das cotações e do câmbio sincronizadas automaticamente.' +
	' As fontes das informações estão descritas no menu <Fontes>.';
	$scope.caminhoVideo = 'assets/videos/auto-sync.mp4';

	$scope.dataHoje = moment().format('DD/MM/YYYY');
	$scope.alertaSistemaTemplate = {};
//	$scope.sumarizacaoSincronizacaoList = [];
	$scope.sumarizacaoCambioList = [];
	$scope.sumarizacaoCotacaoList = [];


	function findAllSincronizacao() {

		$http.get('/sumarizacaoSincronizacaoController/findAll').success(function(response) {

			angular.forEach(response, function(sumarizacao, key) {

				if(sumarizacao.cambioId != null) {
					$scope.sumarizacaoCambioList.push(sumarizacao);
				} else {
					$scope.sumarizacaoCotacaoList.push(sumarizacao);
				}
			});
	//		$scope.sumarizacaoSincronizacaoList = response;
		});
	};

	findAllSincronizacao();

	$scope.rodarCrawlerCambio = function() {

		$('#divCambio').block({ message: '<img src="/assets/images/loading_gif.gif" />' });

		$http.get('/sumarizacaoSincronizacaoController/rodarCrawlerCambio').success(function(response) {

			$scope.sumarizacaoCambioList = [];
			$scope.sumarizacaoCotacaoList = [];
			findAllSincronizacao();
			$('#divCambio').unblock();
		});
	};

//	$scope.rodarCrawlerAntigo = function() {
//
//		$('#divCambio').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//		$('#divCommodity').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//
//		$http.get('/sumarizacaoSincronizacaoController/rodarCrawlerAntigo').success(function(response) {
//
//			$scope.sumarizacaoCambioList = [];
//			$scope.sumarizacaoCotacaoList = [];
//			findAllSincronizacao();
//			$('#divCambio').unblock();
//			$('#divCommodity').unblock();
//		});
//	};

	$scope.rodarCrawlerCotacao = function() {

		$('#divCommodity').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.get('/sumarizacaoSincronizacaoController/rodarCrawlerCotacao').success(function(response) {

			$scope.sumarizacaoCambioList = [];
			$scope.sumarizacaoCotacaoList = [];
			findAllSincronizacao();
			$('#divCommodity').unblock();
		});
	};
});