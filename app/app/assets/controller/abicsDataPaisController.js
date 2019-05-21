var moduleAbicsDataPais = angular.module('moduleAbicsDataPais', []);

moduleAbicsDataPais.controller('controllerAbicsDataPais', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Exportações do Brasil para vários países. Fonte Abics, com dados mensais.'+
	'Os valores são o volume comercializado equivalente em sacas de 60kg, a receita cambial e o peso líquido.'+
	'Há também, a variação entre os mesmos meses de distintos anos.'+
	'\nPara consultar escolha o(s) pais(es) e o(s) ano(s) a serem comparados.';
	$scope.caminhoVideo = 'assets/videos/abicsDataPais.mp4';

	$scope.anoList = [2016, 2015, 2014, 2013, 2012, 2011, 2010];
	$scope.anoSelecionadoList = [];
	$scope.paisList = [];
	$scope.paisSelecionadoList = [];
	
	$http.get('/abicsDataPaisController/findAllPais').success(function(response) {
		$scope.paisList = response;
	});
	
	$scope.consultar = function() {

		var formData = new FormData();
		formData.append('ano_selecionado_list', $scope.anoSelecionadoList);
//		formData.append('show_selecionar_paises', $scope.showSelecionarPaises);
		
//		if($scope.showSelecionarPaises == 1) {
			formData.append('pais_selecionado_list', $scope.paisSelecionadoList);
//		} else {
//			formData.append('qte_maiores_paises', $scope.qteMaioresPaises);
//		}

		$('#divGraficoEquivalenteSaca').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataPaisController/grafico/equivalenteSaca60kg',
			formData, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponseEquivalente) {
				try {
					graficoEquivalenteSaca.destroy();
				} catch (exception) {

				}
				
				graficoEquivalenteSaca = c3.generate({
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
				        	title: function (d) { return 'Mês  ' + ++d; }
				        }
				    }
				});

				$("#graficoEquivalenteSaca").append(graficoEquivalenteSaca.element);
				graficoEquivalenteSaca.categories(listResponseEquivalente.categories);
				graficoEquivalenteSaca.load(listResponseEquivalente);

				$http.post('/abicsDataPaisController/variacao/equivalenteSaca60kg',
						formData, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listVariacaoEquivalenteResponse) {

					try {
						graficoVariacaoEquivalenteSaca.destroy();
					} catch (exception) {

					}

					graficoVariacaoEquivalenteSaca = c3.generate({
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
					        	title: function (d) { return 'Mês  ' + ++d; },
					            value: function (value, ratio, id) {
					            	
					            	if(value != null && value.toString().length > 0) {
					            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
					            	}
					            	return value;
					            }
					        }
					    },
						regions: [
						          	{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
									{axis: 'y', start: -100, end: 0, class: 'regionRed'}
								]
					});

					$("#graficoVariacaoEquivalenteSaca").append(graficoVariacaoEquivalenteSaca.element);
					graficoVariacaoEquivalenteSaca.categories(listVariacaoEquivalenteResponse.categories);
					graficoVariacaoEquivalenteSaca.load(listVariacaoEquivalenteResponse);
				});

				$('#divGraficoEquivalenteSaca').unblock({});
		});

		$('#divGraficoReceitaCambial').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataPaisController/grafico/receita',
				formData, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listReceitaResponse) {

				try {
					graficoReceitaCambial.destroy();
				} catch (exception) {

				}
				
				graficoReceitaCambial = c3.generate({
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
				        	title: function (d) { return 'Mês  ' + ++d; }
				        }
				    }
				});

				$("#graficoReceitaCambial").append(graficoReceitaCambial.element);
				graficoReceitaCambial.categories(listReceitaResponse.categories);
				graficoReceitaCambial.load(listReceitaResponse);


				$http.post('/abicsDataPaisController/variacao/receita',
						formData, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listVariacaoReceitaResponse) {

					try {
						graficoVariacaoReceitaCambial.destroy();
					} catch (exception) {

					}

					graficoVariacaoReceitaCambial = c3.generate({
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
					        	title: function (d) { return 'Mês  ' + ++d; },
					            value: function (value, ratio, id) {
					            	
					            	if(value != null && value.toString().length > 0) {
					            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
					            	}
					            	return value;
					            }
					        }
					    },
						regions: [
						          	{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
									{axis: 'y', start: -100, end: 0, class: 'regionRed'}
								]
					});

					$("#graficoVariacaoReceitaCambial").append(graficoVariacaoReceitaCambial.element);
					graficoVariacaoReceitaCambial.categories(listVariacaoReceitaResponse.categories);
					graficoVariacaoReceitaCambial.load(listVariacaoReceitaResponse);
				});

				$('#divGraficoReceitaCambial').unblock({});
		});

		$('#divGraficoPesoLiquido').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataPaisController/grafico/peso',
				formData, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listPesoResponse) {
				try {
					graficoPesoLiquido.destroy();
				} catch (exception) {

				}

				graficoPesoLiquido = c3.generate({
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
				        	title: function (d) { return 'Mês  ' + ++d; }
				        }
				    }
				});

				$("#graficoPesoLiquido").append(graficoPesoLiquido.element);
				graficoPesoLiquido.categories(listPesoResponse.categories);
				graficoPesoLiquido.load(listPesoResponse);

				$http.post('/abicsDataPaisController/variacao/peso',
						formData, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listVariacaoPesoResponse) {

					try {
						graficoVariacaoPesoLiquido.destroy();
					} catch (exception) {

					}

					graficoVariacaoPesoLiquido = c3.generate({
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
					        	title: function (d) { return 'Mês  ' + ++d; },
					            value: function (value, ratio, id) {
					            	
					            	if(value != null && value.toString().length > 0) {
					            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
					            	}
					            	return value;
					            }
					        }
					    },
						regions: [
									{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
									{axis: 'y', start: -100, end: 0, class: 'regionRed'}
								]
					});

					$("#graficoVariacaoPesoLiquido").append(graficoVariacaoPesoLiquido.element);
					graficoVariacaoPesoLiquido.categories(listVariacaoPesoResponse.categories);
					graficoVariacaoPesoLiquido.load(listVariacaoPesoResponse);
				});

				$('#divGraficoPesoLiquido').unblock({});
		});
	}
	
});
