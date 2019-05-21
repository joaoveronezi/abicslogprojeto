var moduleCambio = angular.module('moduleCambio', []);

moduleCambio.directive('directiveModalEditarCambio', function() {
	return {
		templateUrl : '/assets/directives/directiveModalEditarCambio.html'
	};
});

moduleCambio.directive('directiveModalEditarValorMercadoria', function() {
	return {
		templateUrl : '/assets/directives/directiveModalEditarValorMercadoria.html'
	};
});

moduleCambio.controller('controllerCambio', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Os valores histórico da variação do câmbio e das cotações.' +
	' Os valores do câmbio são desde 01/01/2016. As cotações são desde 27/08/2016.';
	$scope.caminhoVideo = 'assets/videos/manual-sync.mp4';

	$scope.alertaSistemaTemplate = {};
	$scope.cambioList = [];
	$scope.cotacaoList = [];
	$scope.cotacaoNacionalList = [];
	$scope.cotacaoInternacionalList = []

	var dataHojeGrafico = moment();
	var mesGrafico = dataHojeGrafico.month() + 1;
	var anoGrafico = dataHojeGrafico.year();

	$scope.anoGrafico = anoGrafico + '';
	$scope.mesGrafico = mesGrafico + '';
	var mesAtual = 0;
	var inicioMesUnix = moment().startOf('month').unix();
	var inicioMesMoment = moment([anoGrafico, mesGrafico-1]);
	$scope.inicioMes = inicioMesMoment.format('DD-MM-YYYY');
	$scope.fimMes = moment(inicioMesMoment).endOf('month').format('DD-MM-YYYY');

	findAllCambio();
	findAllCotacao();
	findAllValorMercadoria();
	montarGraficoCambio();
	montarGraficoValorMercadoria();

	function findAllCotacao() {
		$http.get('/cambioController/findAllCotacao').success(function(responseList) {
			$scope.cotacaoNacionalList = [];
			$scope.cotacaoInternacionalList = [];
			$scope.cotacaoList = responseList;

			angular.forEach($scope.cotacaoList, function(cotacao, key) {

				if(cotacao.nacional == 1) {
					$scope.cotacaoNacionalList.push(cotacao);
				} else {
					$scope.cotacaoInternacionalList.push(cotacao);
				}
			});
		});
	}

	function findAllCambio() {
		$http.get('/cambioController/findByPeriodo/' + inicioMesUnix).success(function(cambioListResponse) {
			$scope.cambioList = cambioListResponse;
		});
	}

	function findAllValorMercadoria() {
		$http.get('/cambioController/findValorMercadoriaByPeriodo/' + inicioMesUnix).success(function(responseList) {
			$scope.valorMercadoriaCotacaoList = responseList;
		});
	}

	function montarGraficoCambio() {

		$('#divGraficoCambio').block({ message: '<img src="/assets/images/loading_gif.gif" />' });

		$http.get('/cambioController/findGraficoVariacaoCambio/' + inicioMesUnix).success(function(cambioListResponse) {
			try {
				graficoCambio.destroy();
			} catch (exception) {

			}

			graficoCambio = c3.generate({
//				bindto: '#graficoCambio',
				data: {
					columns: [],
					type : 'line'
//					types : {'mediaEuro': 'line', 'mediaDolar' : 'line'}
				},
				grid: {
					y : {
						show: true
//						lines: [{value: 3, text: '3'}, {value: 3.75, text: '3.75', class: 'grid800'}]
					}
				},
				axis: {
					x: {
						type: 'category',
						tick: {
			               rotate: 75
						},
						height: 75
					}
//					y2: {
//			            show: true
//			        }
//				},
//				grid: {
//					y : {
//						lines: [{value: 1, text: 'Real'}]
//					}
				},
				tooltip: {
			        format: {
			            value: function (value, ratio, id) {
			            	if(value != null && value.toString().length > 0) {
//			            		return value.toString().replace(".",",");
			            		return Number(value).toFixed(2).toString().replace(".",",");
			            	}
			            	return value;
			            }
			        }
				}
			});
			$("#graficoCambio").append(graficoCambio.element);
			graficoCambio.categories(cambioListResponse.categories);
			graficoCambio.load(cambioListResponse);

			$('#divGraficoCambio').unblock({});
		});
	};

	function montarGraficoValorMercadoria() {

		$('#divGraficoValorMercadoria').block({ message: '<img src="/assets/images/loading_gif.gif" />' });

		$http.get('/cambioController/findGraficoVariacaoValoresMercadoria/' + inicioMesUnix).success(function(cambioListResponse) {
			try {
				graficoValorMercadoria.destroy();
			} catch (exception) {

			}

			graficoValorMercadoria = c3.generate({
//				bindto: '#graficoValorMercadoria',
				data: {
					columns: [],
					type : 'line'
	//				types : {'mediaEuro': 'line', 'mediaDolar' : 'line'}
				},
				grid: {
					y : {show: true}
				},
				axis: {
					x: {
						type: 'category',
						tick: {
			               rotate: 75
						},
						height: 75
					}
//					y2: {
//						show: true
//					}
	//			},
	//			grid: {
	//				y : {
	//					lines: [{value: 1, text: 'Real'}]
	//				}
				},
				tooltip: {
			        format: {
			            value: function (value, ratio, id) {
			            	if(value != null && value.toString().length > 0) {
//			            		return value.toString().replace(".",",");
			            		return Number(value).toFixed(2).toString().replace(".",",");
			            	}
			            	return value;
			            }
			        }
				}
//				regions: [
//				          {axis: 'y', start: 0, end: 3000, class: 'regionWhite'},
//				          {axis: 'y', start: 3500, end: 4000, class: 'regionY'},
//				          {axis: 'x', start: 0, end: 10, class: 'regionWhite'},
//				          {axis: 'x', start: 20, end: 30, class: 'regionWhite'},
//				          {axis: 'x', start: 10, end: 20, class: 'regionY'},
//				          {axis: 'x', start: 5, class: 'regionX'},
//				          {axis: 'y', end: 50, class: 'regionY'},
//				          {axis: 'y', start: 80, end: 140, class: 'regionY'},
//				          {axis: 'y', start: 400, class: 'regionY'},
//				          {axis: 'y2', end: 900, class: 'regionY2'},
//				          {axis: 'y2', start: 1150, end: 1250, class: 'regionY2'},
//				          {axis: 'y2', start: 1300, class: 'regionY2'},
//				      ]
			});
			$("#graficoValorMercadoria").append(graficoValorMercadoria.element);
			graficoValorMercadoria.categories(cambioListResponse.categories);
			graficoValorMercadoria.load(cambioListResponse);

			$('#divGraficoValorMercadoria').unblock({});
		});
	};

	$scope.modificarPeriodoGraficoMes = function(periodo) {

		mesAtual = mesAtual + periodo;

		if(mesAtual == 0) {

			dataHojeGrafico = moment().startOf('month');
		} else {

			if(mesAtual > 0) {
				dataHojeGrafico = moment().startOf('month').add(mesAtual, 'month');
			} else if(mesAtual < 0) {

				var valorSubtracao = mesAtual * -1;
				dataHojeGrafico = moment().startOf('month').subtract(valorSubtracao, 'month');
			}
		}

		mesGrafico = dataHojeGrafico.month() + 1;
		anoGrafico = dataHojeGrafico.year();
		inicioMesUnix = dataHojeGrafico.unix();

		inicioMesMoment = moment([anoGrafico, mesGrafico-1]);
		$scope.inicioMes = inicioMesMoment.format('DD-MM-YYYY');
		$scope.fimMes = moment(inicioMesMoment).endOf('month').format('DD-MM-YYYY');
		$scope.anoGrafico = anoGrafico + '';
		$scope.mesGrafico = mesGrafico + '';
		montarGraficoCambio();
		montarGraficoValorMercadoria();
		findAllCambio();
		findAllValorMercadoria();
	};

	$scope.objetoCambio = {};
	$scope.objetoCambio.valorReal = 1;
	$scope.dataCotacaoIsOpen = false;
	$scope.objetoDataSelecionada = moment().toDate();

	$scope.openDataCotacao = function() {
		$scope.dataCotacaoIsOpen = true;
	}

	$scope.cadastrarCambio = function() {

		$scope.objetoCambio.data = $scope.objetoDataSelecionada.getTime();

	    $http.post('/cambioController/cadastrarCambio', $scope.objetoCambio).success(function(abstractResponse) {
	    	$scope.objetoCambio = {};
//	    	$scope.objetoCambio.listaRelValorMercadoriaCotacao = [];
			findAllCambio();
			montarGraficoCambio();
//			montarGraficoValorMercadoria();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
	    });
	}

	$scope.cadastrarValorMercadoriaCotacao = function(idCotacao, valorColetado) {
		
		var valorMercadoriaCotacao = {};
		valorMercadoriaCotacao.idCotacao = idCotacao;
		valorMercadoriaCotacao.valorColetado = valorColetado;
		valorMercadoriaCotacao.dataCadastro = $scope.objetoDataSelecionada.getTime();

		$http.post('/cambioController/cadastrarValorMercadoriaCotacao', valorMercadoriaCotacao)
			.success(function(abstractResponse, status, headers, response) {
				
				findAllCotacao();
				findAllValorMercadoria();
				montarGraficoValorMercadoria();
	        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
	        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
	        	$scope.alertaSistemaTemplate.show = true;
	        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
				$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		});
	};

	$scope.abrirFonte = function(fonte) {
		
		window.open(''+fonte)
	};
	
	/**INICIO MODAIS*/
	$scope.abrirModalAlterarCambio = function(cambio) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalEditarCambio',
            controller: modalControlerEditarCambio,
            size: 'md',
            resolve: {
            	cambioParametro: function () {
                    return cambio;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllCambio();
			montarGraficoCambio();
//			montarGraficoValorMercadoria();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};

	$scope.abrirModalAlterarValorMercadoria = function(mercadoriaCotacao) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalEditarValorMercadoria',
            controller: modalControlerEditarValorMercadoria,
            size: 'md',
            resolve: {
            	valorMercadoriaCotacaoParametro: function () {
                    return mercadoriaCotacao;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllValorMercadoria();
			montarGraficoValorMercadoria();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};

	/**FIM MODAIS*/

});

var modalControlerEditarCambio = function ($scope, $modalInstance, $http, $timeout, $sce, cambioParametro) {

	$scope.objetoCambio = {};
	$scope.objetoCambio = cambioParametro;

	$scope.objetoCambioEditar = {};
	$scope.objetoCambioEditar.id = cambioParametro.id;
//	$scope.objetoCambioEditar.data = cambioParametro.data;
	$scope.objetoCambioEditar.valorReal = cambioParametro.valorReal;
	$scope.objetoCambioEditar.valorDolar = cambioParametro.valorDolar;
	$scope.objetoCambioEditar.valorEuro = cambioParametro.valorEuro;

	$scope.alterarCambio = function() {

        $http.post('/cambioController/atualizarCambio', $scope.objetoCambioEditar).success(function(abstractResponse) {

        	$scope.objetoCambio = {};
        	$scope.objetoCambioEditar = {};
        	$modalInstance.close(abstractResponse);
        });
	}

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};

var modalControlerEditarValorMercadoria = function ($scope, $modalInstance, $http, $timeout, $sce, valorMercadoriaCotacaoParametro) {

	$scope.objetoValorMercadoria = {};
	$scope.objetoValorMercadoria = valorMercadoriaCotacaoParametro;
	$scope.objetoValorMercadoria.data = valorMercadoriaCotacaoParametro.data;

	$scope.objetoValorMercadoriaEditar = {};
	$scope.objetoValorMercadoriaEditar.id = valorMercadoriaCotacaoParametro.id;
	$scope.objetoValorMercadoriaEditar.valorColetado = valorMercadoriaCotacaoParametro.valorColetado;

	$scope.alterarValorMercadoria = function() {

        $http.post('/cambioController/atualizarValorMercadoria', $scope.objetoValorMercadoriaEditar).success(function(abstractResponse) {

        	$scope.objetoValorMercadoria = {};
        	$scope.objetoValorMercadoriaEditar = {};
        	$modalInstance.close(abstractResponse);
        });
	}

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};