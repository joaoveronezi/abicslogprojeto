var moduleAbicsDataRelatorioBrasil = angular.module('moduleAbicsDataRelatorioBrasil', []);

moduleAbicsDataRelatorioBrasil.directive('directiveModalValidacaoRelatorioBrasil', function() {
	return {
		templateUrl : '/assets/directives/directiveModalValidacaoRelatorioBrasil.html'
	};
});

moduleAbicsDataRelatorioBrasil.controller('controllerAbicsDataRelatorioBrasil', function($scope, $http, $timeout, $modal, $location) {

	$scope.informacoes = 'Exportações do Brasil, discriminados por país. Fontes Abics e Comtrade, com dados mensais.'+
	'Os dados são referentes a quanto o Brasil exportou ou importou de cada país.'+
	'\nPara consultar escolha qual tipo de informação deseja, exportação ou importação, e o tipo de café.'+
	'Escolha quais países, ou todos países de determinado continente, ou todos países de determinado bloco econômico.'+
	'Qual a fonte desejada e o período desejado.';
	$scope.caminhoVideo = 'assets/videos/abicsDataRelatorioBrasil.mp4';

	$scope.mesList = [01,02,03,04,05,06,07,08,09,10,11,12];
	$scope.anoList = [];
	
	var paisIdListAntigo = '';
	var continenteIdListAntigo = '';
	var blocoEconomicoIdListAntigo = '';
	var anoInicialAntigo = '';
	var anoFinalAntigo = '';
	var mesInicialAntigo = '';
	var mesFinalAntigo = '';
	var tiposCafeAntigo = '';
	
	$scope.mesInicial = '';
	$scope.mesFinal = '';
	
	var anoInicialInt = 1995;
	var anoFinalInt = new Date().getFullYear();
	var anoLength = anoFinalInt - anoInicialInt;
		
	$scope.anoInicial = anoInicialInt.toString();
	$scope.anoFinal = anoFinalInt.toString();
	
	for(i=0; i<=anoLength; i++) {
		$scope.anoList.push(anoInicialInt.toString());
		anoInicialInt++;
	}
	
	$scope.tabelaList = [];
	$scope.modelRadioTipo = 'exportacao';
	$scope.paisList = [];
	$scope.continenteList = [];
	$scope.blocoEconomicoList = [];

	$scope.paisSelecionadoList = [];
	$scope.continenteSelecionadoList = [];
	$scope.blocoEconomicoSelecionadoList = [];

	$scope.fonteSelecionado = 'abics';
	$scope.fonte = 'abics';
	$scope.tipoDadosGrafico = '';
	
	$scope.agregado = false;
	$scope.botaoTipoGrafico = false;
	
	$scope.voltarTemplate = function() {
		$location.path('templateAbicsData');
	};
	
	function orderByNome(a,b) {
	  if (a.nome < b.nome)
	    return -1;
	  if (a.nome > b.nome)
	    return 1;
	  return 0;
	}
	
	$http.get('/abicsDataRelatorioBrasilController/findAllPais').success(function(response) {
		$scope.paisList = response;
		$scope.paisList.sort(orderByNome);
	});
	
	$http.get('/abicsDataRelatorioBrasilController/findAllContinente').success(function(response) {
		$scope.continenteList = response;
		$scope.continenteList.sort(orderByNome);
	});
	
	$http.get('/abicsDataRelatorioBrasilController/findAllBlocoEconomico').success(function(response) {
		$scope.blocoEconomicoList = response;
		$scope.blocoEconomicoList.sort(orderByNome);
	});
	
	$scope.imprimirRelatorio = function() {

		$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		
		var paisIdList = [];
		for(i = 0; i < $scope.paisSelecionadoList.length; i++) {
			paisIdList.push($scope.paisSelecionadoList[i].id); 
		}
		
		var continenteIdList = [];
		for(i = 0; i < $scope.continenteSelecionadoList.length; i++) {
			continenteIdList.push($scope.continenteSelecionadoList[i].id); 
		}
		
		var blocoEconomicoIdList = [];
		for(i = 0; i < $scope.blocoEconomicoSelecionadoList.length; i++) {
			blocoEconomicoIdList.push($scope.blocoEconomicoSelecionadoList[i].id); 
		}
		
		var tiposCafe = [];
		tiposCafe.push('soluvel');
		
		var objConsulta = new FormData();
		objConsulta.append('fonte', $scope.fonte);
		objConsulta.append('paisList', paisIdListAntigo);
		objConsulta.append('continenteList', continenteIdListAntigo);
		objConsulta.append('blocoEconomicoList', blocoEconomicoIdListAntigo);
		objConsulta.append('tipo', $scope.modelRadioTipo);
		objConsulta.append('anoInicial', anoInicialAntigo);
		objConsulta.append('anoFinal', anoFinalAntigo);
		objConsulta.append('mesInicial', mesInicialAntigo);
		objConsulta.append('mesFinal', mesFinalAntigo);
		
		objConsulta.append('tipoCafe', tiposCafeAntigo);
		
		if($scope.agregado) {
			var agregadoRequest = 'Agregado';
		} else {
			var agregadoRequest = '';
		}
        
		$http.post('/abicsDataRelatorioBrasilController/imprimirRelatorio' + agregadoRequest, objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}, responseType: 'arraybuffer'}).success(function(data) {

			   var a = document.createElement("a");
			   document.body.appendChild(a);
			   a.style = "display: none";
			   var file = new Blob([data], {type: 'application/vnd.ms-excel'}); // data.contents
			   var fileURL = URL.createObjectURL(file);
			   a.target = "_blank";
			   a.href = fileURL;
			   a.download = "relatorio_mensal_" + $scope.modelRadioTipo + "_" + $scope.fonte + ".xls";
			   a.click();
			   $('#abicsDataRelatorioBrasil').unblock();
		});
	};
	
	function montarGraficosAbics(tipoAgregado, objConsulta) {
		var agregado = '';
		
		if(tipoAgregado == true) {
			agregado = 'Agregado';
			$scope.agregado = true;
		} else {
			$scope.agregado = false;
		}
		
		var paisIdList = [];
		for(i = 0; i < $scope.paisSelecionadoList.length; i++) {
			paisIdList.push($scope.paisSelecionadoList[i].id); 
		}
		
		$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataRelatorioBrasilController/consultarGraficoAbics' + agregado + '/peso', objConsulta, {transformRequest: angular.identity, headers: {'Content-Type': undefined}})
		.success(function(listResponse, status, headers) {
			$scope.graficoAbicsDadosPeso = listResponse;
			
			try {
				graficoAbicsPeso.destroy();
			} catch (exception) {

			}
			
			graficoAbicsPeso = c3.generate({
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
			
			$("#graficoAbicsPeso").append(graficoAbicsPeso.element);
			graficoAbicsPeso.categories(listResponse.categories);
			graficoAbicsPeso.load(listResponse);
			$('#abicsDataRelatorioBrasil').unblock({});
		});
		
		$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataRelatorioBrasilController/consultarGraficoAbics' + agregado + '/receita', objConsulta, {transformRequest: angular.identity, headers: {'Content-Type': undefined}})
		.success(function(listResponse, status, headers) {
								
			$scope.graficoAbicsDadosReceita = listResponse;
			try {
				graficoAbicsReceita.destroy();
			} catch (exception) {

			}

			graficoAbicsReceita = c3.generate({
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

			$("#graficoAbicsReceita").append(graficoAbicsReceita.element);
			graficoAbicsReceita.categories(listResponse.categories);
			graficoAbicsReceita.load(listResponse);
			$('#abicsDataRelatorioBrasil').unblock({});
		});
		
		$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataRelatorioBrasilController/consultarGraficoAbics' + agregado + '/saca60kg', objConsulta, {transformRequest: angular.identity, headers: {'Content-Type': undefined}})
		.success(function(listResponse, status, headers) {
			
			$scope.graficoAbicsDadosSaca60kg = listResponse;
			try {
				graficoAbicsSaca60kg.destroy();
			} catch (exception) {

			}

			graficoAbicsSaca60kg = c3.generate({
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

			$("#graficoAbicsSaca60kg").append(graficoAbicsSaca60kg.element);
			graficoAbicsSaca60kg.categories(listResponse.categories);
			graficoAbicsSaca60kg.load(listResponse);
			$('#abicsDataRelatorioBrasil').unblock({});
		});
		
		if($scope.anual) {
			var stringVariacaoConsulta = ''; 
			
			objConsulta.append('pais_list', paisIdList);
			
//			var tamanhoArrayAno = parseInt(objConsulta.get('anoFinal')) - parseInt(objConsulta.get('anoInicial'));
			var tamanhoArrayAno = parseInt(anoFinalAntigo) - parseInt(anoInicialAntigo);
			$scope.anosSelecionadoList = [];
			
			while(tamanhoArrayAno >= 0) {
				$scope.anosSelecionadoList.push(parseInt(anoInicialAntigo) + tamanhoArrayAno);
				tamanhoArrayAno--;
			}
			
			objConsulta.append('anos_list', $scope.anosSelecionadoList.reverse());
			
			var tamanhoArrayMes = parseInt(mesFinalAntigo) - parseInt(mesInicialAntigo);
			$scope.mesSelecionadoList = [];
			
			while(tamanhoArrayMes >= 0) {
				$scope.mesSelecionadoList.push(parseInt(mesInicialAntigo) + tamanhoArrayMes);
				tamanhoArrayMes--;
			}
			
			objConsulta.append('period_list', $scope.mesSelecionadoList.reverse());
			
			$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
			$http.post('/abicsDataRelatorioBrasilController/variacaoAbics/peso', objConsulta,
					{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

				try {
					graficoAbicsVariacaoPeso.destroy();
				} catch (exception) {

				}

				graficoAbicsVariacaoPeso = c3.generate({
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
								{axis: 'y', start: 0, end: 1000, class: 'regionGreen'},
								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
							]
				});

				$("#graficoAbicsVariacaoPeso").append(graficoAbicsVariacaoPeso.element);
				graficoAbicsVariacaoPeso.categories(listResponse.categories);
				graficoAbicsVariacaoPeso.load(listResponse);

				$('#divGraficoAbicsPeso').unblock({});
			});
			
			$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
			$http.post('/abicsDataRelatorioBrasilController/variacaoAbics/receita', objConsulta,
					{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

				try {
					graficoAbicsVariacaoReceita.destroy();
				} catch (exception) {

				}

				graficoAbicsVariacaoReceita = c3.generate({
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
								{axis: 'y', start: 0, end: 1000, class: 'regionGreen'},
								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
							]
				});

				$("#graficoAbicsVariacaoReceita").append(graficoAbicsVariacaoReceita.element);
				graficoAbicsVariacaoReceita.categories(listResponse.categories);
				graficoAbicsVariacaoReceita.load(listResponse);

				$('#graficoAbicsVariacaoReceita').unblock({});
			});
			
			$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
			$http.post('/abicsDataRelatorioBrasilController/variacaoAbics/saca60kg', objConsulta,
					{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

				try {
					graficoAbicsVariacaoSaca60kg.destroy();
				} catch (exception) {

				}

				graficoAbicsVariacaoSaca60kg = c3.generate({
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
								{axis: 'y', start: 0, end: 1000, class: 'regionGreen'},
								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
							]
				});

				$("#graficoAbicsVariacaoSaca60kg").append(graficoAbicsVariacaoSaca60kg.element);
				graficoAbicsVariacaoSaca60kg.categories(listResponse.categories);
				graficoAbicsVariacaoSaca60kg.load(listResponse);

				$('#divGraficoAbicsSaca60kg').unblock({});
			});
		}
	}
	
	function montarGraficosComtrade(tipoAgregado, objConsulta) {
		var agregado = '';
		
		if(tipoAgregado == true) {
			agregado = 'Agregado';
			$scope.agregado = true;
		} else {
			$scope.agregado = false;
		}
		
		var paisIdList = [];
		for(i = 0; i < $scope.paisSelecionadoList.length; i++) {
			paisIdList.push($scope.paisSelecionadoList[i].id); 
		}
		
		$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataRelatorioBrasilController/consultarGraficoComtrade' + agregado + '/NetWeight', objConsulta, 
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse, status, headers) {

			$scope.graficoComtradeDadosNetWeight = listResponse;
			try {
				graficoComtradeNetWeight.destroy();
			} catch (exception) {

			}

			graficoComtradeNetWeight = c3.generate({
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

			$("#graficoComtradeNetWeight").append(graficoComtradeNetWeight.element);
			graficoComtradeNetWeight.categories(listResponse.categories);
			graficoComtradeNetWeight.load(listResponse);
			$('#abicsDataRelatorioBrasil').unblock({});
		});
		
		
		$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataRelatorioBrasilController/consultarGraficoComtrade' + agregado + '/tradeValue', objConsulta, 
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse, status, headers) {
					
			$scope.graficoComtradeTradeValueDados = listResponse;
			try {
				graficoComtradeTradeValue.destroy();
			} catch (exception) {

			}

			graficoComtradeTradeValue = c3.generate({
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

			$("#graficoComtradeTradeValue").append(graficoComtradeTradeValue.element);
			graficoComtradeTradeValue.categories(listResponse.categories);
			graficoComtradeTradeValue.load(listResponse);
			$('#abicsDataRelatorioBrasil').unblock({});
		});
		
		if($scope.anual) {
			var stringVariacaoConsulta = ''; 
			
			objConsulta.append('pais_list', paisIdList);
			
			var tamanhoArrayAno = parseInt(objConsulta.get('anoFinal')) - parseInt(objConsulta.get('anoInicial'));
			$scope.anosSelecionadoList = [];
			
			while(tamanhoArrayAno >= 0) {
				$scope.anosSelecionadoList.push(parseInt(objConsulta.get('anoInicial')) + tamanhoArrayAno);
				tamanhoArrayAno--;
			}
			
			objConsulta.append('anos_list', $scope.anosSelecionadoList.reverse());
			
			var tamanhoArrayMes = parseInt(objConsulta.get('mesFinal')) - parseInt(objConsulta.get('mesInicial'));
			$scope.mesSelecionadoList = [];
			
			while(tamanhoArrayMes >= 0) {
				$scope.mesSelecionadoList.push(parseInt(objConsulta.get('mesInicial')) + tamanhoArrayMes);
				tamanhoArrayMes--;
			}
			
			objConsulta.append('period_list', $scope.mesSelecionadoList.reverse());
			
			$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
			$http.post('/abicsDataRelatorioBrasilController/variacaoComtrade/netweight', objConsulta,
					{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

				try {
					graficoComtradeVariacaoNetWeight.destroy();
				} catch (exception) {

				}

				graficoComtradeVariacaoNetWeight = c3.generate({
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
								{axis: 'y', start: 0, end: 1000, class: 'regionGreen'},
								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
							]
				});

				$("#graficoComtradeVariacaoNetWeight").append(graficoComtradeVariacaoNetWeight.element);
				graficoComtradeVariacaoNetWeight.categories(listResponse.categories);
				graficoComtradeVariacaoNetWeight.load(listResponse);

				$('#divGraficoComtradeNetWeight').unblock({});
			});
			
			$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
			$http.post('/abicsDataRelatorioBrasilController/variacaoComtrade/tradevalue', objConsulta,
					{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

				try {
					graficoComtradeVariacaoTradeValue.destroy();
				} catch (exception) {

				}

				graficoComtradeVariacaoTradeValue = c3.generate({
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
								{axis: 'y', start: 0, end: 1000, class: 'regionGreen'},
								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
							]
				});

				$("#graficoComtradeVariacaoTradeValue").append(graficoComtradeVariacaoTradeValue.element);
				graficoComtradeVariacaoTradeValue.categories(listResponse.categories);
				graficoComtradeVariacaoTradeValue.load(listResponse);

				$('#graficoComtradeVariacaoTradeValue').unblock({});
			});
		}
		
	}
	
	function montarTabela(objConsulta) {
		
		$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataRelatorioBrasilController/consultarTabela', objConsulta, 
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(response, status, headers) {
					
			$scope.tabelaList = response;
			
			$scope.listaPaisSeparado = [];
			$scope.totalGeralPeso = 0;
			$scope.totalGeralReceita = 0;
			$scope.totalGeralSaca60kg = 0;
			$scope.totalGeralNetWeight = 0;
			$scope.totalGeralTradeValue = 0;
			var pais = '';
			var posicao = -1;
			var anoComparacao = anoInicialAntigo -1;
			
			angular.forEach($scope.tabelaList, function(value, key) {
				if($scope.fonteSelecionado == 'abics') {
//					if(objConsulta.get('tipoGrafico').toLowerCase() == 'anual') {
					if(tipoGraficoAnualSequencialGlobal.toLowerCase() == 'anual') {
						if(value.ano == anoComparacao) {
							$scope.listaPaisSeparado[posicao].somaPeso += parseInt(value.peso);
							$scope.listaPaisSeparado[posicao].somaReceita += parseInt(value.receita);
							$scope.listaPaisSeparado[posicao].somaSaca60kg += parseInt(value.saca60kg);
							
							$scope.paisListaObjSeparado.push(value);
							
							if(pais != value.pais) {
//								anoComparacao = objConsulta.get('anoInicial') -1;
								anoComparacao = anoInicialAntigo -1;
							}
							
						} else {
							if(pais != value.pais) {
								anoComparacao = anoInicialAntigo -1;
							}
							
							pais = value.pais;
							posicao++;
							anoComparacao++;
							
							$scope.paisListaObjSeparado = [];
							
							$scope.listaPaisSeparado.push($scope.paisListaObjSeparado);
							$scope.paisListaObjSeparado.push(value);
							
							$scope.listaPaisSeparado[posicao].pais = pais;
							$scope.listaPaisSeparado[posicao].somaPeso = parseInt(value.peso);
							$scope.listaPaisSeparado[posicao].somaReceita = parseInt(value.receita);
							$scope.listaPaisSeparado[posicao].somaSaca60kg = parseInt(value.saca60kg);
						}
						
					} else if(tipoGraficoAnualSequencialGlobal.toLowerCase() == 'sequencial') {
						if(value.pais.toLowerCase() == pais.toLowerCase()) {
							$scope.listaPaisSeparado[posicao].somaPeso += parseInt(value.peso);
							$scope.listaPaisSeparado[posicao].somaReceita += parseInt(value.receita);
							$scope.listaPaisSeparado[posicao].somaSaca60kg += parseInt(value.saca60kg);
							
							$scope.paisListaObjSeparado.push(value);
							
						} else {
							pais = value.pais;
							posicao++;
							
							$scope.paisListaObjSeparado = [];
							
							$scope.listaPaisSeparado.push($scope.paisListaObjSeparado);
							$scope.paisListaObjSeparado.push(value);
							
							$scope.listaPaisSeparado[posicao].pais = pais;
							$scope.listaPaisSeparado[posicao].somaPeso = parseInt(value.peso);
							$scope.listaPaisSeparado[posicao].somaReceita = parseInt(value.receita);
							$scope.listaPaisSeparado[posicao].somaSaca60kg = parseInt(value.saca60kg);
						}
					}
					
					$scope.totalGeralPeso += parseInt(value.peso);
					$scope.totalGeralReceita += parseInt(value.receita);
					$scope.totalGeralSaca60kg += parseInt(value.saca60kg);
					
				} else if($scope.fonteSelecionado == 'comtrade'){
					if(tipoGraficoAnualSequencialGlobal.toLowerCase() == 'anual') {
						if(value.ano == anoComparacao) {
							$scope.listaPaisSeparado[posicao].somaNetWeight += parseInt(value.netWeight);
							$scope.listaPaisSeparado[posicao].somaTradeValue += parseInt(value.tradeValue);
							
							$scope.paisListaObjSeparado.push(value);
							
							if(pais != value.pais) {
								anoComparacao = anoInicialAntigo -1;
							}
							
						} else {
							if(pais != value.pais) {
								anoComparacao = anoInicialAntigo -1;
							}
							
							pais = value.pais;
							posicao++;
							anoComparacao++;
							
							$scope.paisListaObjSeparado = [];
							
							$scope.listaPaisSeparado.push($scope.paisListaObjSeparado);
							$scope.paisListaObjSeparado.push(value);
							
							$scope.listaPaisSeparado[posicao].pais = pais;
							$scope.listaPaisSeparado[posicao].somaNetWeight = parseInt(value.netWeight);
							$scope.listaPaisSeparado[posicao].somaTradeValue = parseInt(value.tradeValue);
						}
						
					} else if(tipoGraficoAnualSequencialGlobal.toLowerCase() == 'sequencial') {
						if(value.pais.toLowerCase() == pais.toLowerCase()) {
							$scope.listaPaisSeparado[posicao].somaNetWeight += parseInt(value.netWeight);
							$scope.listaPaisSeparado[posicao].somaTradeValue += parseInt(value.tradeValue);
							
							$scope.paisListaObjSeparado.push(value);
							
						} else {
							pais = value.pais;
							posicao++;
							
							$scope.paisListaObjSeparado = [];
							
							$scope.listaPaisSeparado.push($scope.paisListaObjSeparado);
							$scope.paisListaObjSeparado.push(value);
							
							$scope.listaPaisSeparado[posicao].pais = pais;
							$scope.listaPaisSeparado[posicao].somaNetWeight = parseInt(value.netWeight);
							$scope.listaPaisSeparado[posicao].somaTradeValue = parseInt(value.tradeValue);
						}
					}
					$scope.totalGeralNetWeight += parseInt(value.netWeight);
					$scope.totalGeralTradeValue += parseInt(value.tradeValue);
				}
			});
			
			$('#abicsDataRelatorioBrasil').unblock();
			
			document.getElementById("tabelaFontes").innerHTML = "";
			$scope.divSomaTotal = false;
			
			angular.forEach($scope.listaPaisSeparado, function(value1, key1) {
				var idTabela = 'tabelaFonte' + key1;
				
				var tabelaString = '<table id="' + idTabela +'" class="table table-striped" style="text-align: center">'
					+  '<thead class="th-color">'
					+ 	'<tr>'
					+	  '<th class="text-center">Tipo</th>'
					+     '<th class="text-center">Café</th>'
					+	  '<th class="text-center">Pais</th>'
					+	  '<th class="text-center">Mês</th>'
					+     '<th class="text-center">Ano</th>';
				
				if($scope.fonteSelecionado == 'abics') {
					tabelaString +=   '<th class="text-center">Peso</th>'
									+ '<th class="text-center">Receita</th>'
									+ '<th class="text-center">Saca 60kg</th>';
					
				} else { 
					tabelaString +=   '<th class="text-center">Trade Value</th>'
									+ '<th class="text-center">Net Weight</th>'
				}
				
				tabelaString += '</tr>'
					+	'</thead>'
					+   '<tbody>';
				
				angular.forEach($scope.listaPaisSeparado[key1], function(value2, key2) {
					tabelaString += '<tr><td class="text-center">' + value2.tipo + '</td>'
					+	'<td class="text-center">' + value2.tipoCafe + '</td>'
					+	'<td class="text-center">' + value2.pais + '</td>'
					+	'<td class="text-center">' + value2.mes + '</td>'
					+	'<td class="text-center">' + value2.ano + '</td>';
				
					if($scope.fonteSelecionado == 'abics') {
						tabelaString += '<td class="text-center">' + value2.peso + '</td>'
							+		   '<td class="text-center">' + value2.receita + '</td>'
							+		   '<td class="text-center">' + value2.saca60kg + '</td>';
					
					} else {
						tabelaString += '<td class="text-center">' + value2.tradeValue + '</td>'
							+		   '<td class="text-center">' + value2.netWeight + '</td>';
					}
					
					tabelaString += '</tr>';
				});
				tabelaString += '</tbody>'
					+			'<tfoot>'
					+			  '<tr>'
					+				'<td><b>SOMA DO PAÍS</b></td>'
					+				'<td><b>-</b></td>'
					+				'<td><b>-</b></td>'
					+				'<td><b>-</b></td>'
					+				'<td><b>-</b></td>'
					
				if($scope.fonteSelecionado == 'abics') {
					tabelaString += '<td class="text-center"><b>' + value1.somaPeso + '</b></td>'
						+			'<td class="text-center"><b>' + value1.somaReceita + '</b></td>'
						+			'<td class="text-center"><b>' + value1.somaSaca60kg + '</b></td>';
					
				} else {
					tabelaString += '<td class="text-center"><b>' + value1.somaTradeValue + '</b></td>'
						+			'<td class="text-center"><b>'+ value1.somaNetWeight + '</b></td>';
				}
				
				tabelaString += '</tr></tfoot></table></br>';
				
				$("#tabelaFontes").append(tabelaString);
				if($scope.listaPaisSeparado.length > 1) {
					$scope.divSomaTotal = true;
				}
			});
			
		});
	}
	
	function montarTabelaAgregada(objConsulta) {
		
		$('#abicsDataRelatorioBrasil').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataRelatorioBrasilController/consultarTabelaAgregada', objConsulta, 
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(response, status, headers) {
					
			$scope.tabelaList = response;
			
			$scope.listaPaisSeparado = [];
			$scope.totalGeralPeso = 0;
			$scope.totalGeralReceita = 0;
			$scope.totalGeralSaca60kg = 0;
			$scope.totalGeralNetWeight = 0;
			$scope.totalGeralTradeValue = 0;
			var pais = '';
			var posicao = -1;
			
			document.getElementById("tabelaFontes").innerHTML = "";
			$scope.divSomaTotal = false;
			
			var tabelaString = '<table id="tabelaFonte0" class="table table-striped" style="text-align: center">'
			+  '<thead class="th-color">'
			+ 	'<tr>'
			+	  '<th class="text-center">Tipo</th>'
			+     '<th class="text-center">Café</th>'
			+	  '<th class="text-center">Pais</th>'
			+	  '<th class="text-center">Mês</th>'
			+     '<th class="text-center">Ano</th>';
		
			if($scope.fonteSelecionado == 'abics') {
				tabelaString +=   '<th class="text-center">Peso</th>'
								+ '<th class="text-center">Receita</th>'
								+ '<th class="text-center">Saca 60kg</th>';
				
			} else { 
				tabelaString +=   '<th class="text-center">Trade Value</th>'
								+ '<th class="text-center">Net Weight</th>'
			}
			
			tabelaString += '</tr>'
				+	'</thead>'
				+   '<tbody>';
			
			angular.forEach($scope.tabelaList, function(value1, key1) {
				
				tabelaString += '<tr><td class="text-center">' + value1.tipo + '</td>'
				+	'<td class="text-center">' + value1.tipoCafe + '</td>'
				+	'<td class="text-center">' + value1.pais + '</td>'
				+	'<td class="text-center">' + value1.mes + '</td>'
				+	'<td class="text-center">' + value1.ano + '</td>';
			
				if($scope.fonteSelecionado == 'abics') {
					tabelaString += '<td class="text-center">' + value1.peso + '</td>'
						+		   '<td class="text-center">' + value1.receita + '</td>'
						+		   '<td class="text-center">' + value1.saca60kg + '</td>';
					
					$scope.totalGeralPeso += parseInt(value1.peso);
					$scope.totalGeralReceita += parseInt(value1.receita);
					$scope.totalGeralSaca60kg += parseInt(value1.saca60kg);
				
				} else {
					tabelaString += '<td class="text-center">' + value1.tradeValue + '</td>'
						+		   '<td class="text-center">' + value1.netWeight + '</td>';
					
					$scope.totalGeralNetWeight += parseInt(value1.netWeight);
					$scope.totalGeralTradeValue += parseInt(value1.tradeValue);
				}
				
				tabelaString += '</tr>';
				
			});
			
			tabelaString += '</tbody>';
			
			tabelaString += '</table></br>';
			$("#tabelaFontes").append(tabelaString);
			if($scope.tabelaList.length > 1) {
				$scope.divSomaTotal = true;
			}
			
			$('#abicsDataRelatorioBrasil').unblock();
		});
	}
	
	$scope.limparConsulta = function() {
		
		$scope.paisSelecionadoList = [];
		$scope.blocoEconomicoSelecionadoList = [];
		$scope.continenteSelecionadoList = [];
		
		$scope.botaoTipoGrafico = false;
		
		$scope.fonteSelecionado = 'abics';
		$scope.fonte = 'abics';
		
		$scope.mesInicial = '';
		$scope.mesFinal = '';
		
		$scope.anoInicial = '1995';
		$scope.anoFinal = anoFinalInt.toString();
		
		$scope.tipoDadosGrafico = '';
		$scope.tabelaList = [];
		$scope.listaPaisSeparado = [];
		$scope.totalGeralPeso = 0;
		$scope.totalGeralReceita = 0;
		$scope.totalGeralSaca60kg = 0;
		$scope.totalGeralNetWeight = 0;
		$scope.totalGeralTradeValue = 0;
		document.getElementById("tabelaFontes").innerHTML = "";
		$scope.divSomaTotal = false;

	};
	var tipoGraficoAnualSequencialGlobal = '';
	$scope.consultar = function(tipoAgregado, tipoAnualSequencial) {
		
		tipoGraficoAnualSequencialGlobal = tipoAnualSequencial;
		
		$scope.fonte = $scope.fonteSelecionado;
		
		var paisIdList = [];
		for(i = 0; i < $scope.paisSelecionadoList.length; i++) {
			paisIdList.push($scope.paisSelecionadoList[i].id); 
		}
		
		var continenteIdList = [];
		for(i = 0; i < $scope.continenteSelecionadoList.length; i++) {
			continenteIdList.push($scope.continenteSelecionadoList[i].id); 
		}
		
		var blocoEconomicoIdList = [];
		for(i = 0; i < $scope.blocoEconomicoSelecionadoList.length; i++) {
			blocoEconomicoIdList.push($scope.blocoEconomicoSelecionadoList[i].id); 
		}
		
		var tiposCafe = [];
		tiposCafe.push('soluvel');
		
		var objConsulta = new FormData();
		objConsulta.append('fonte', $scope.fonte);
		objConsulta.append('paisList', paisIdList);
		objConsulta.append('continenteList', continenteIdList);
		objConsulta.append('blocoEconomicoList', blocoEconomicoIdList);
		objConsulta.append('tipo', $scope.modelRadioTipo);
		objConsulta.append('mesInicial', $scope.mesInicial);
		objConsulta.append('mesFinal', $scope.mesFinal);
		objConsulta.append('anoInicial', $scope.anoInicial);
		objConsulta.append('anoFinal', $scope.anoFinal);
		objConsulta.append('tipoCafe', tiposCafe);

		if(parseInt($scope.mesFinal) < parseInt($scope.mesInicial)) {
			tipoAnualSequencial = 'sequencial';
		}
		
		objConsulta.append('tipoGrafico', tipoAnualSequencial);
		
		if(tipoAnualSequencial == 'anual') {
			$("#buttonGraficoAnual").removeClass("btn-default");
			$("#buttonGraficoAnual").addClass("btn-success");
			$("#buttonGraficoAnual").css("color", "white");
			$("#buttonGraficoSequencial").addClass("btn-default");
			$("#buttonGraficoSequencial").css("color", "black");
			$scope.anual = true;
			
		} else if(tipoAnualSequencial == 'sequencial') {
			$("#buttonGraficoAnual").addClass("btn-default");
			$("#buttonGraficoAnual").css("color", "black");
			$("#buttonGraficoSequencial").removeClass("btn-default");
			$("#buttonGraficoSequencial").addClass("btn-success");
			$("#buttonGraficoSequencial").css("color", "white");
			$scope.anual = false;
		}
		
		paisIdListAntigo = paisIdList;
		continenteIdListAntigo = continenteIdList;
		blocoEconomicoIdListAntigo = blocoEconomicoIdList;
		anoInicialAntigo = $scope.anoInicial;
		anoFinalAntigo = $scope.anoFinal;
		mesInicialAntigo = $scope.mesInicial;
		mesFinalAntigo = $scope.mesFinal;
		tiposCafeAntigo = tiposCafe;

		var mensagemValidacao = '';
		/**Validacoes**/
		if(tiposCafe.length == 0) {
			mensagemValidacao += 'Selecione ao menos um tipo de café: Arábica, Robusta ou Solúvel';
			mensagemValidacao += ';;';
		}

		if(paisIdList.length == 0 && continenteIdList.length == 0 && blocoEconomicoIdList.length == 0) {
			mensagemValidacao += 'Selecione ao menos um pais ou continente ou bloco economico';
			mensagemValidacao += ';;';
		}
		
		if($scope.mesInicial == '' || $scope.mesFinal == '') {
			mensagemValidacao += 'Selecione o(s) mês(es) e o(s) ano(s) inicial e final';
			mensagemValidacao += ';;';
		}
		
		if($scope.fonteSelecionado == '' || $scope.fonteSelecionado.length == 0) {
			mensagemValidacao += 'Selecione uma fonte';
			mensagemValidacao += ';;';
		}
		
		if($scope.anoFinal < $scope.anoInicial) {
			mensagemValidacao += 'Verifique o período: Ano Final deve ser maior que Ano Inicial';
			mensagemValidacao += ';;';			
		}
		else {
			if($scope.anoFinal == $scope.anoInicial) {
				if(Number.parseInt($scope.mesFinal) < Number.parseInt($scope.mesInicial)) {
					mensagemValidacao += 'Verifique o período: Mês Final deve ser maior que Mês Inicial';
					mensagemValidacao += ';;';					
				}
			}
		}
		
		if(mensagemValidacao != '') {
			abrirModalValidacaoRelatorioBrasil(mensagemValidacao);
			return;
		}
		
		$scope.botaoTipoGrafico = true;
		
		if($scope.fonte == 'abics') {
			montarGraficosAbics(tipoAgregado, objConsulta);
			$scope.modificarDadosGrafico('peso','abics');
		}
		
		if($scope.fonte == 'comtrade') {
			montarGraficosComtrade(tipoAgregado, objConsulta);
			$scope.modificarDadosGrafico('netweight','comtrade');
		}
		
		if(tipoAgregado == false) {
			montarTabela(objConsulta);
		} else {
			montarTabelaAgregada(objConsulta);
		}
		
	};
	
	function abrirModalValidacaoRelatorioBrasil(mensagem) {
		var modalInstance = $modal.open({
            templateUrl: 'idModalValidacaoRelatorioBrasil',
            controller: modalControlerValidacaoRelatorioBrasil,
            size: 'lg',
            resolve: {
            	mensagemParametro: function () {
                    return mensagem;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
		}, function (parametro) {
	    });
	};
	
	$scope.modificarDadosGrafico = function(tipoDados, fonte) {
		$scope.tipoDadosGrafico = tipoDados;
		changeButtonStyle(tipoDados,fonte)
	};
	
	function changeButtonStyle(tipoGrafico, fonte) {
		if(fonte == 'abics') {
			document.getElementById('buttonTipoGraficoPeso').className = '';
			document.getElementById('buttonTipoGraficoReceita').className = '';
			document.getElementById('buttonTipoGraficoSaca').className = '';
		} else {
			document.getElementById('buttonTipoGraficoNetWeight').className = '';
			document.getElementById('buttonTipoGraficoTradeValue').className = '';
		}

		if(tipoGrafico == 'peso') {
			document.getElementById('buttonTipoGraficoPeso').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonTipoGraficoReceita').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonTipoGraficoSaca').className = 'btn btn-info btn-xs pull-right';
		} else if(tipoGrafico == 'receita') {
			document.getElementById('buttonTipoGraficoPeso').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonTipoGraficoReceita').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonTipoGraficoSaca').className = 'btn btn-info btn-xs pull-right';
		} else if(tipoGrafico == 'saca60kg') {
			document.getElementById('buttonTipoGraficoPeso').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonTipoGraficoReceita').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonTipoGraficoSaca').className = 'btn btn-primary btn-xs pull-right';
		} else if(tipoGrafico == 'netweight') {
			document.getElementById('buttonTipoGraficoNetWeight').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonTipoGraficoTradeValue').className = 'btn btn-info btn-xs pull-right';
		} else if(tipoGrafico == 'tradevalue') {
			document.getElementById('buttonTipoGraficoNetWeight').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonTipoGraficoTradeValue').className = 'btn btn-primary btn-xs pull-right';
		}
	};
	
});

var modalControlerValidacaoRelatorioBrasil = function ($scope, $modalInstance, $http, $timeout, $sce, mensagemParametro) {

	$scope.mensagem = mensagemParametro;
	$scope.mensagemArray = mensagemParametro.split(';;'); 
	$scope.mensagemArray = $scope.mensagemArray.slice(0, $scope.mensagemArray.length - 1);

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};