var moduleDashboardNacional = angular.module('moduleDashboardNacional', []);

moduleDashboardNacional.controller('controllerDashboardNacional', ['$scope', '$http', '$timeout', '$modal', '$location', 'dashboardService', 
		function($scope, $http, $timeout, $modal, $location, dashboardService) {

	$scope.informacoes = 'Neste dashboard estão sumarizadas as informações da cotação Internacional ' +
			'em comparação com as cotações nacionais.' +
			'Os valores da cotação do Espírito Santo é a média do valores do CEPEA e do CCCV. ' +
			'Os valores do diferencial demonstra o quão competitivo está o café nacional. ' +
			'Caso o café nacional esteja mais competitivo, o valor fica na cor azul, e vermelho caso contrário.';
	$scope.caminhoVideo = 'assets/videos/dashboard.mp4';
	
	$scope.cotacaoInternacionalSelecionada = dashboardService.getCotacao();
	$scope.dataCambioIsOpen = false;
	$scope.objetoDataCambioSelecionada = dashboardService.getDataSelecionada();
	$scope.dataSelecionada = $.datepicker.formatDate(dashboardService.getDataSelecionada());
	$scope.isAplicarLogistica = dashboardService.getIsAplicarLogistica();
	$scope.cambio = {};
	$scope.cotacaoList = [];
	$scope.cotacaoNacionalList = [];
	$scope.cotacaoInternacionalList = [];
	
	$scope.tipoMoeda = dashboardService.getTipoMoeda();
	changeButtonStyle($scope.tipoMoeda);
	
	$scope.openDataCambio = function() {
		$scope.dataCambioIsOpen = true;
	};

	changeDataCambio();
	function changeDataCambio() {
		
		$scope.cotacaoNacionalList = [];
		$scope.cotacaoInternacionalList = [];
		$scope.dataSelecionada = $.datepicker.formatDate($scope.objetoDataCambioSelecionada);
		
		$http.get('/dashboardController/findCambioByData/' + $scope.objetoDataCambioSelecionada.getTime()).success(function(cambioResponse) {
			$scope.cambio = cambioResponse;
		});

		montarGraficos();
		simularCotacoes();
	};

	$scope.changeDataCambio = function() {
		changeDataCambio();
	}

	$scope.modificarMoedaGrafico = function(tipoMoeda) {
		
		$scope.tipoMoeda = tipoMoeda;
		changeButtonStyle(tipoMoeda);
		montarGraficos();
	};
	
	function changeButtonStyle(tipoMoeda) {
		
		document.getElementById('buttonNacionalEvolucaoDolar').className = '';
		document.getElementById('buttonNacionalEvolucaoReal').className = '';
		document.getElementById('buttonNacionalDiferencialESDolar').className = '';
		document.getElementById('buttonNacionalDiferencialESReal').className = '';
		document.getElementById('buttonNacionalDiferencialRODolar').className = '';
		document.getElementById('buttonNacionalDiferencialROReal').className = '';
		
		if(tipoMoeda == 'dolar') {
			document.getElementById('buttonNacionalEvolucaoDolar').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonNacionalEvolucaoReal').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonNacionalDiferencialESDolar').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonNacionalDiferencialESReal').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonNacionalDiferencialRODolar').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonNacionalDiferencialROReal').className = 'btn btn-info btn-xs pull-right';
		} else if(tipoMoeda == 'real') {
			document.getElementById('buttonNacionalEvolucaoDolar').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonNacionalEvolucaoReal').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonNacionalDiferencialESDolar').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonNacionalDiferencialESReal').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonNacionalDiferencialRODolar').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonNacionalDiferencialROReal').className = 'btn btn-primary btn-xs pull-right';
		}
	};
	
	function montarGraficos() {
		
		$('#divGraficoEvolucaoPrecoNacional').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.get('/dashboardController/graficoEvolucaoPrecoNacional/' + $scope.objetoDataCambioSelecionada.getTime() + '/' 
				+ $scope.cotacaoInternacionalSelecionada.id + '/' + $scope.tipoMoeda + '/' + $scope.isAplicarLogistica).success(function(listResponse) {

			try {
				graficoEvolucaoPrecoNacional.destroy();
			} catch (exception) {

			}

			graficoEvolucaoPrecoNacional = c3.generate({
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
//					{axis: 'y', start: 3500, end: 4000, class: 'regionRed'},
//					{axis: 'y', start: 2000, end: 2500, class: 'regionGreen'},
//					]
			});

			$("#graficoEvolucaoPrecoNacional").append(graficoEvolucaoPrecoNacional.element);
			graficoEvolucaoPrecoNacional.categories(listResponse.categories);
			graficoEvolucaoPrecoNacional.load(listResponse);

			$('#divGraficoEvolucaoPrecoNacional').unblock({});
		});

		$('#divGraficoNacionalDiferencaPrecoES').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.get('/dashboardController/graficoDiferencaPrecoNacional/1/' + $scope.objetoDataCambioSelecionada.getTime() + '/' 
				+ $scope.cotacaoInternacionalSelecionada.id + '/' + $scope.tipoMoeda + '/' + $scope.isAplicarLogistica).success(function(listResponse) {

			try {
				graficoNacionalDiferencaPrecoES.destroy();
			} catch (exception) {

			}

			graficoNacionalDiferencaPrecoES = c3.generate({
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
//					{axis: 'y', start: -1000, end: 0, class: 'regionRed'},
//					{axis: 'y', start: 0, end: 1000, class: 'regionGreen'},
//					]
			});

			$("#graficoNacionalDiferencaPrecoES").append(graficoNacionalDiferencaPrecoES.element);
			graficoNacionalDiferencaPrecoES.categories(listResponse.categories);
			graficoNacionalDiferencaPrecoES.load(listResponse);

			$('#divGraficoNacionalDiferencaPrecoES').unblock({});
		});

		$('#divGraficoNacionalDiferencaPrecoRO').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.get('/dashboardController/graficoDiferencaPrecoNacional/0/' + $scope.objetoDataCambioSelecionada.getTime() + '/' 
				+ $scope.cotacaoInternacionalSelecionada.id + '/' + $scope.tipoMoeda + '/' + $scope.isAplicarLogistica).success(function(listResponse) {

			try {
				graficoNacionalDiferencaPrecoRO.destroy();
			} catch (exception) {

			}

			graficoNacionalDiferencaPrecoRO = c3.generate({
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
//					{axis: 'y', start: -1000, end: 0, class: 'regionRed'},
//					{axis: 'y', start: 0, end: 1000, class: 'regionGreen'},
//					]
			});

			$("#graficoNacionalDiferencaPrecoRO").append(graficoNacionalDiferencaPrecoRO.element);
			graficoNacionalDiferencaPrecoRO.categories(listResponse.categories);
			graficoNacionalDiferencaPrecoRO.load(listResponse);

			$('#divGraficoNacionalDiferencaPrecoRO').unblock({});
		});
	}

	$scope.selecionarCotacaoInternacional = function(cotacaoInternacional) {
		
		for(var i = $scope.cotacaoNacionalList.length - 1; i >= 0; i--) {
		    if($scope.cotacaoNacionalList[i].nacional != 0) {
		    	$scope.cotacaoNacionalList.splice(i, 1);
		    }
		}
		$scope.cotacaoNacionalList.shift(cotacaoInternacional);
		
		$scope.cotacaoInternacionalSelecionada = cotacaoInternacional; 
		dashboardService.setCotacao(cotacaoInternacional);
		
		montarGraficos();
		simularCotacoes();
	};
	
	function simularCotacoes() {
		
//		var calendar31Agosto = new Date(2016, 8, 01);
		
		$scope.cotacaoList = [];
		$scope.cotacaoNacionalList = [];
		$scope.cotacaoInternacionalList = [];
		
		$http.get('/dashboardController/simularRelatorio/' + $scope.objetoDataCambioSelecionada.getTime() + '/' 
				+ $scope.isAplicarLogistica).success(function(listResponse) {
			$scope.cotacaoList = listResponse;

			if($scope.cotacaoList != null && $scope.cotacaoList.length > 0) {

				angular.forEach($scope.cotacaoList, function(cotacao, key) {

					if(cotacao.nacional == 1) {
//						if(($scope.objetoDataCambioSelecionada.getTime() > calendar31Agosto.getTime()) && cotacao.id == 7) {
//							cotacao.valorCustoImportacaoReal = "0.0";
//							cotacao.valorCustoImportacaoDolar = "0.0";
//						}
						$scope.cotacaoNacionalList.push(cotacao);
					} else {
						
//						if($scope.objetoDataCambioSelecionada.getTime() > calendar31Agosto.getTime()) {
//							cotacao.diferencialPercentualRO = "0.0";
//							cotacao.diferencialRO = "0.0";
//							cotacao.diferencialRODolar = "0.0";
//						}
						
						$scope.cotacaoInternacionalList.push(cotacao);
						if($scope.cotacaoInternacionalSelecionada.id == cotacao.id) {
							$scope.cotacaoInternacionalSelecionada = cotacao;
							$scope.cotacaoNacionalList.unshift($scope.cotacaoInternacionalSelecionada);
						}
					}

					angular.forEach(cotacao.categoriaList, function(categoria, key2) {

						angular.forEach(categoria.itemList, function(item, key3) {
						});
					});
				});
			}
		});
	};
	
	$scope.voltarPagina = function() {
		
		dashboardService.setCotacao($scope.cotacaoInternacionalSelecionada);
		dashboardService.setDataSelecionada($scope.objetoDataCambioSelecionada);
		dashboardService.setTipoMoeda($scope.tipoMoeda);
		dashboardService.setIsVoltar(true);
		dashboardService.setIsAplicarLogistica($scope.isAplicarLogistica);
		
		$location.path('/dashboard');
	};
	
	$scope.aplicarLogistica = function() {
		$scope.isAplicarLogistica = !$scope.isAplicarLogistica;
		
		simularCotacoes();
		montarGraficos();
	};
	
	$scope.abrirModalDetalhes = function(cotacao) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalDetalhesCotacao',
            controller: modalControlerDetalhesCotacao,
            size: 'lg',
            resolve: {
            	cotacaoParametro: function () {
                    return cotacao;
                },
                unixtimeParametro: function() {
                	return $scope.objetoDataCambioSelecionada.getTime();
                },
                cambioParametro: function() {
                	return $scope.cambio;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
		}, function (parametro) {
	    });
	};
}]);