var moduleDashboard = angular.module('moduleDashboard', []);

moduleDashboard.directive('directiveModalDetalhesCotacao', function() {
	return {
		templateUrl : '/assets/directives/directiveModalDetalhesCotacao.html'
	};
});

moduleDashboard.directive('directiveModalGraficoCotacao', function() {
	return {
		templateUrl : '/assets/directives/directiveModalGraficoCotacao.html'
	};
});

moduleDashboard.controller('controllerDashboard', ['$scope', '$http', '$timeout', '$modal', '$location', 'dashboardService',
		function($scope, $http, $timeout, $modal, $location, dashboardService) {

	$scope.informacoes = 'Neste dashboard estão sumarizadas as informações das cotações Internacionais. ' +
			'Os valores da cotação do Espírito Santo é a média do valores do CEPEA e do CCCV. ' +
			'Os valores do diferencial demonstra o quão competitivo está o café nacional. ' +
			'Caso o café nacional esteja mais competitivo, o valor fica na cor azul, e vermelho caso contrário.';
	$scope.caminhoVideo = 'assets/videos/dashboard.mp4';

	$scope.dataCambioIsOpen = false;
	$scope.objetoDataCambioSelecionada = moment().add(-1, 'days').toDate();
	$scope.dataSelecionada = $.datepicker.formatDate($scope.objetoDataCambioSelecionada);
	$scope.tipoMoeda = 'real';
	$scope.isAplicarLogistica = false;
	$scope.cambio = {};
	$scope.cotacaoList = [];
	$scope.cotacaoInternacionalList = [];

	if(dashboardService.getIsVoltar()) {

		$scope.objetoDataCambioSelecionada = dashboardService.getDataSelecionada();
		$scope.dataSelecionada = $.datepicker.formatDate($scope.objetoDataCambioSelecionada);
		$scope.tipoMoeda = dashboardService.getTipoMoeda();
		$scope.isAplicarLogistica = dashboardService.getIsAplicarLogistica();
		changeButtonStyle($scope.tipoMoeda);
	}

	$scope.openDataCambio = function() {
		$scope.dataCambioIsOpen = true;
	};

	changeDataCambio();
	function changeDataCambio() {

		$scope.cotacaoInternacionalList = [];

		$scope.dataSelecionada = $.datepicker.formatDate($scope.objetoDataCambioSelecionada);

		$http.get('/dashboardController/findCambioByData/' + $scope.objetoDataCambioSelecionada.getTime()).success(function(cambioResponse) {
			$scope.cambio = cambioResponse;
		});

		simularRelatorio();
		montarGraficos();
	};

	$scope.changeDataCambio = function() {
		changeDataCambio();
	};

	$scope.aplicarLogistica = function() {
		$scope.isAplicarLogistica = !$scope.isAplicarLogistica;
		
		simularRelatorio();
		montarGraficos();
	};
	
	$scope.modificarMoedaGrafico = function(tipoMoeda) {

		$scope.tipoMoeda = tipoMoeda;
		changeButtonStyle(tipoMoeda)
		montarGraficos();
	};

	function changeButtonStyle(tipoMoeda) {

		document.getElementById('buttonEvolucaoDolar').className = '';
		document.getElementById('buttonEvolucaoReal').className = '';
		document.getElementById('buttonDiferencialESDolar').className = '';
		document.getElementById('buttonDiferencialESReal').className = '';
		document.getElementById('buttonDiferencialRODolar').className = '';
		document.getElementById('buttonDiferencialROReal').className = '';

		if(tipoMoeda == 'dolar') {
			document.getElementById('buttonEvolucaoDolar').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonEvolucaoReal').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonDiferencialESDolar').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonDiferencialESReal').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonDiferencialRODolar').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonDiferencialROReal').className = 'btn btn-info btn-xs pull-right';
		} else if(tipoMoeda == 'real') {
			document.getElementById('buttonEvolucaoDolar').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonEvolucaoReal').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonDiferencialESDolar').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonDiferencialESReal').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonDiferencialRODolar').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonDiferencialROReal').className = 'btn btn-primary btn-xs pull-right';
		}
	};

	function simularRelatorio() {
		
		$scope.cotacaoList = [];
		$scope.cotacaoInternacionalList = [];
		
		var calendar31Agosto = new Date(2016, 8, 01);
		
		$http.get('/dashboardController/simularRelatorio/' + $scope.objetoDataCambioSelecionada.getTime()
				 + '/' + $scope.isAplicarLogistica).success(function(listResponse) {
			$scope.cotacaoList = listResponse;
			if($scope.cotacaoList != null && $scope.cotacaoList.length > 0) {

				angular.forEach($scope.cotacaoList, function(cotacao, key) {

					if(cotacao.nacional == 0) {
//						console.log(cotacao);
//						if($scope.objetoDataCambioSelecionada.getTime() > calendar31Agosto.getTime()) {
//							cotacao.diferencialPercentualRO = "0.0";
//							cotacao.diferencialRO = "0.0";
//							cotacao.diferencialRODolar = "0.0";
//						}
						$scope.cotacaoInternacionalList.push(cotacao);
					}
				});
			};
		});
	}
	
	function montarGraficos() {

		$('#divGraficoEvolucaoPrecoInternacional').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.get('/dashboardController/graficoEvolucaoPreco/' + $scope.objetoDataCambioSelecionada.getTime() +
				'/' + $scope.tipoMoeda + '/' + $scope.isAplicarLogistica).success(function(listResponse) {

			try {
				graficoEvolucaoPrecoInternacional.destroy();
			} catch (exception) {

			}

			graficoEvolucaoPrecoInternacional = c3.generate({
				data: {
					columns: [],
					type : 'line',
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
//			        	title: function (d) { return 'Data ' + d; },
			            value: function (value, ratio, id) {
			            	
//			            	console.log('v ' + value);
//			            	console.log('r ' + ratio);
//			            	console.log(id);
//			                var format = id === 'data1' ? d3.format(',') : d3.format('$');
//			                return format(value);
			            	
			            	if(value != null && value.toString().length > 0) {
			            		return Number(value).toFixed(2).toString().replace(".",",");
			            	}
			            	return value;
			            }
			        }
			    }
			});
			
			$("#graficoEvolucaoPrecoInternacional").append(graficoEvolucaoPrecoInternacional.element);
			graficoEvolucaoPrecoInternacional.categories(listResponse.categories);
			graficoEvolucaoPrecoInternacional.load(listResponse);
			graficoEvolucaoPrecoInternacional.unload('Uganda ');
//			graficoEvolucaoPrecoInternacional.hide('Uganda ');
			
			$('#divGraficoEvolucaoPrecoInternacional').unblock({});
		});

		$('#divGraficoDiferencaPrecoES').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.get('/dashboardController/graficoDiferencaPreco/1/' + $scope.objetoDataCambioSelecionada.getTime() +
				'/' + $scope.tipoMoeda + '/' + $scope.isAplicarLogistica).success(function(listResponse) {

			try {
				graficoDiferencaPrecoES.destroy();
			} catch (exception) {

			}

			graficoDiferencaPrecoES = c3.generate({
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
			            		return Number(value).toFixed(2).toString().replace(".",",");
			            	}
			            	return value;
			            }
			        }
			    }
			});

			$("#graficoDiferencaPrecoES").append(graficoDiferencaPrecoES.element);
			graficoDiferencaPrecoES.categories(listResponse.categories);
			graficoDiferencaPrecoES.load(listResponse);

			$('#divGraficoDiferencaPrecoES').unblock({});
		});

		$('#divGraficoDiferencaPrecoRO').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.get('/dashboardController/graficoDiferencaPreco/0/' + $scope.objetoDataCambioSelecionada.getTime() +
				'/' + $scope.tipoMoeda + '/' + $scope.isAplicarLogistica).success(function(listResponse) {

			try {
				graficoDiferencaPrecoRO.destroy();
			} catch (exception) {

			}

			graficoDiferencaPrecoRO = c3.generate({
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
			            		return Number(value).toFixed(2).toString().replace(".",",");
			            	}
			            	return value;
			            }
			        }
			    }
			});

			$("#graficoDiferencaPrecoRO").append(graficoDiferencaPrecoRO.element);
			graficoDiferencaPrecoRO.categories(listResponse.categories);
			graficoDiferencaPrecoRO.load(listResponse);

			$('#divGraficoDiferencaPrecoRO').unblock({});
		});
	}

	$scope.selecionarCotacao = function(cotacao) {

		dashboardService.setCotacao(cotacao);
		dashboardService.setDataSelecionada($scope.objetoDataCambioSelecionada);
		dashboardService.setTipoMoeda($scope.tipoMoeda);
		dashboardService.setIsVoltar(false);
		dashboardService.setIsAplicarLogistica($scope.isAplicarLogistica);
		
		$location.path('dashboardNacional');
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

	$scope.abrirModalGrafico = function(cotacao) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalGraficoCotacao',
            controller: modalControlerGraficoCotacao,
            size: 'lg',
            resolve: {
            	cotacaoParametro: function () {
                    return cotacao;
                },
                unixtimeParametro: function() {
                	return $scope.objetoDataCambioSelecionada.getTime();
                },
                tipoMoedaParametro: function() {
                	return $scope.tipoMoeda;
                },
                isAplicarLogisticaParametro: function() {
                	return $scope.isAplicarLogistica;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
		}, function (parametro) {
	    });
	};
}]);

var modalControlerDetalhesCotacao = function ($scope, $modalInstance, $http, $timeout, $sce, cotacaoParametro, unixtimeParametro, cambioParametro) {

	$scope.cotacao = cotacaoParametro;
	$scope.data = unixtimeParametro;
	$scope.cambio = cambioParametro;

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};

var modalControlerGraficoCotacao = function ($scope, $modalInstance, $http, $timeout, $sce, cotacaoParametro, unixtimeParametro, tipoMoedaParametro, isAplicarLogisticaParametro) {

	$scope.tipoMoedaModal = tipoMoedaParametro;
	$scope.cotacao = cotacaoParametro;
	$scope.isAplicarLogistica = isAplicarLogisticaParametro;

	$http.get('/dashboardController/montarGraficoCotacao/' + cotacaoParametro.id + '/' + unixtimeParametro 
			+ '/' + tipoMoedaParametro + '/' + isAplicarLogisticaParametro).success(function(listResponse) {

		try {
			graficoCotacaoModal.destroy();
		} catch (exception) {

		}

		graficoCotacaoModal = c3.generate({
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
		            		return Number(value).toFixed(2).toString().replace(".",",");
		            	}
		            	return value;
		            }
		        }
		    }
		});

		$("#graficoCotacaoModal").append(graficoCotacaoModal.element);
		graficoCotacaoModal.categories(listResponse.categories);
		graficoCotacaoModal.load(listResponse);
	});

	$http.get('/dashboardController/montarGraficoDiferencialCotacao/' + cotacaoParametro.id + '/' + unixtimeParametro 
			+ '/' + tipoMoedaParametro + '/' + isAplicarLogisticaParametro).success(function(listResponse) {

		try {
			graficoDiferencialModal.destroy();
		} catch (exception) {

		}

		graficoDiferencialModal = c3.generate({
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
		            		return Number(value).toFixed(2).toString().replace(".",",");
		            	}
		            	return value;
		            }
		        }
		    }
		});

		$("#graficoDiferencialModal").append(graficoDiferencialModal.element);
		graficoDiferencialModal.categories(listResponse.categories);
		graficoDiferencialModal.load(listResponse);
	});

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};