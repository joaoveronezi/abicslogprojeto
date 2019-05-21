var moduleAbicsDataComtrade = angular.module('moduleAbicsDataComtrade', []);

moduleAbicsDataComtrade.controller('controllerAbicsDataComtrade', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Tela de informações de Exportação e Importação dos países. Fonte COMTRADE, com dados mensais.'+
	'Dados são discriminados por país. O quanto um país importou/exportou para um ou mais destinos.'+
	'\nPara consultar escolha o tipo de origem/destino. Uma origem para vários destinos, ou várias origens para um destino.'+
	'Escolha qual(is) origem(ns). Escolha qual(is) destino(s).'+
	'Escolha o tipo de comparação, anual ou mensal. Anual: um ou vários aos, ou mensal: um ano e um ou vários meses.';
	$scope.caminhoVideo = 'assets/videos/abicsDataComtrade.mp4';

	/**MENSAL**/
	$scope.anoMensalList = [2016, 2015, 2014, 2013, 2012, 2011, 2010];
	$scope.anoMensal = -1;
	$scope.tipoOrigemDestino = 0; // 0)) 1 origem > N destiino  1)) N origens > 1 destino
	$scope.tipoData = 0; //0 anual 1 mensal
	$scope.reporterList = [];
	$scope.partnerList = [];
	$scope.reporterSelecionadoList = [];
	$scope.partnerSelecionadoList = [];
	$scope.allAno = false;
	$scope.anosSelecionadoList = [];
	$scope.allMes = false;
	$scope.mesesSelecionadoList = [];

	$scope.ano2016 = false;
	$scope.ano2015 = false;
	$scope.ano2014 = false;
	$scope.ano2013 = false;
	$scope.ano2012 = false;
	$scope.ano2011 = false;
	$scope.ano2010 = false;

	$http.get('/abicsDataComtradeController/findAllReporter').success(function(response) {
		$scope.reporterList = response;
	});
	$http.get('/abicsDataComtradeController/findAllPartner').success(function(response) {
		$scope.partnerList = response;
	});

	$scope.changeTipoOrigemDestino = function(tipoOrigemParametro) {
		$scope.tipoOrigemDestino = tipoOrigemParametro;
		$scope.reporterSelecionadoList = [];
		$scope.partnerSelecionadoList = [];

		if($scope.tipoOrigemDestino == 0) {
			document.getElementById('reporter').multiple = false;
			document.getElementById('partner').multiple = true;

			document.getElementById('partner').className = 'form-control select-height-active';
			document.getElementById('reporter').className = 'form-control';

			document.getElementById('buttonUmaOrigem').className = 'btn btn-primary';
			document.getElementById('buttonMultiOrigem').className = 'btn btn-info';
		} else {
			document.getElementById('reporter').multiple = true;
			document.getElementById('partner').multiple = false;

			document.getElementById('reporter').className = 'form-control select-height-active';
			document.getElementById('partner').className = 'form-control';

			document.getElementById('buttonMultiOrigem').className = 'btn btn-primary';
			document.getElementById('buttonUmaOrigem').className = 'btn btn-info';
		}

	};

	$scope.clickReporter = function(partner) {
		console.log($scope.reporterSelecionadoList);
	};

	$scope.clickParter = function(partner) {
		console.log($scope.partnerSelecionadoList);
	};

	$scope.changeTipoData = function() {
		$scope.anoMensal = -1;

		console.log('change');
		$scope.anosSelecionadoList = [];
		$scope.mesesSelecionadoList = [];

		$scope.allAno = false;
		$scope.allMes = false;
		
		$scope.ano2016 = false;
		$scope.ano2015 = false;
		$scope.ano2014 = false;
		$scope.ano2013 = false;
		$scope.ano2012 = false;
		$scope.ano2011 = false;
		$scope.ano2010 = false;

		$scope.mes1 = false;
		$scope.mes2 = false;
		$scope.mes3 = false;
		$scope.mes4 = false;
		$scope.mes5 = false;
		$scope.mes6 = false;
		$scope.mes7 = false;
		$scope.mes8 = false;
		$scope.mes9 = false;
		$scope.mes10 = false;
		$scope.mes11 = false;
		$scope.mes12 = false;
	};

//	$scope.clickAnoSelecionado = function() {
////		console.log(anoParametro);
////		$scope.anosSelecionadoList.push(anoParametro);
//		if($scope.allAno) {
//			
//			console.log('entrou')
//			$scope.anosSelecionadoList.push(2016);
//			$scope.anosSelecionadoList.push(2015);
//			$scope.anosSelecionadoList.push(2014);
//			$scope.anosSelecionadoList.push(2013);
//			$scope.anosSelecionadoList.push(2012);
//			$scope.anosSelecionadoList.push(2011);
//			$scope.anosSelecionadoList.push(2010);
//		} else {
//			
//			console.log('nao nao')
//			$scope.anosSelecionadoList = [];
//		}
//		
//	};

	$scope.clickMesSelecionado = function(mesParametro) {
		console.log(mesParametro);
//		$scope.mesesSelecionadoList.push(mesParametro);
	};

	$scope.consultarComparativo = function() {

//		objConsulta.append('anos_list', $scope.anosSelecionadoList);
//		objConsulta.append('meses_list', $scope.mesesSelecionadoList);
//		objConsulta.append('ano_mensal', $scope.anoMensal);
//		objConsulta.append('tipo_origem_destino', $scope.tipoOrigemDestino);

		var objConsulta = montarObjetoConsulta();
		console.log(objConsulta);

//		$http.post('/abicsDataController/consultarComparativo', objConsulta).success(function(listResponse) {
//		});
//		objConsulta.append('tipo_data_comtrade', 'netweight');
		/**EXPORTACAO**/
		$('#divGraficoComtradeExportacaoNetWeight').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataComtradeController/consultarComparativo/netweight/2', objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

			try {
				graficoComtradeExportacaoNetWeight.destroy();
			} catch (exception) {

			}

			graficoComtradeExportacaoNetWeight = c3.generate({
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
//			            value: function (value, ratio, id) {
//
//			            	if(value != null && value.toString().length > 0) {
//			            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//			            	}
//			            	return value;
//			            }
			        }
			    }
			});

			$("#graficoComtradeExportacaoNetWeight").append(graficoComtradeExportacaoNetWeight.element);
			graficoComtradeExportacaoNetWeight.categories(listResponse.categories);
			graficoComtradeExportacaoNetWeight.load(listResponse);

			$('#divGraficoComtradeExportacaoNetWeight').unblock({});
		});

//		objConsulta.append('tipo_data_comtrade', 'tradevalue');
		$('#divGraficoComtradeExportacaoTradeValue').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataComtradeController/consultarComparativo/tradevalue/2', objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

			try {
				graficoComtradeExportacaoTradeValue.destroy();
			} catch (exception) {

			}

			graficoComtradeExportacaoTradeValue = c3.generate({
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
//			            value: function (value, ratio, id) {
//
//			            	if(value != null && value.toString().length > 0) {
//			            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//			            	}
//			            	return value;
//			            }
			        }
			    }
			});

			$("#graficoComtradeExportacaoTradeValue").append(graficoComtradeExportacaoTradeValue.element);
			graficoComtradeExportacaoTradeValue.categories(listResponse.categories);
			graficoComtradeExportacaoTradeValue.load(listResponse);

			$('#divGraficoComtradeExportacaoTradeValue').unblock({});
		});

//		objConsulta.append('tipo_data_comtrade', 'saca60kg');
		$('#divGraficoComtradeExportacaoSaca60kg').block({ message: '<img src="/assets/images/loading_gif.gif" />' });

		$http.post('/abicsDataComtradeController/consultarComparativo/saca60kg/2', objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

			try {
				graficoComtradeExportacaoSaca60kg.destroy();
			} catch (exception) {

			}

			graficoComtradeExportacaoSaca60kg = c3.generate({
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
//			            value: function (value, ratio, id) {
//
//			            	if(value != null && value.toString().length > 0) {
//			            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//			            	}
//			            	return value;
//			            }
			        }
			    }
			});

			$("#graficoComtradeExportacaoSaca60kg").append(graficoComtradeExportacaoSaca60kg.element);
			graficoComtradeExportacaoSaca60kg.categories(listResponse.categories);
			graficoComtradeExportacaoSaca60kg.load(listResponse);

			$('#divGraficoComtradeExportacaoSaca60kg').unblock({});
		});

		/**IMPORTACAO**/

		$('#divGraficoComtradeImportacaoNetWeight').block({ message: '<img src="/assets/images/loading_gif.gif" />' });

		$http.post('/abicsDataComtradeController/consultarComparativo/netweight/1', objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

			try {
				graficoComtradeImportacaoNetWeight.destroy();
			} catch (exception) {

			}

			graficoComtradeImportacaoNetWeight = c3.generate({
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
//			            value: function (value, ratio, id) {
//
//			            	if(value != null && value.toString().length > 0) {
//			            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//			            	}
//			            	return value;
//			            }
			        }
			    }
			});

			$("#graficoComtradeImportacaoNetWeight").append(graficoComtradeImportacaoNetWeight.element);
			graficoComtradeImportacaoNetWeight.categories(listResponse.categories);
			graficoComtradeImportacaoNetWeight.load(listResponse);

			$('#divGraficoComtradeImportacaoNetWeight').unblock({});
		});

//		objConsulta.append('tipo_data_comtrade', 'tradevalue');
		$('#divGraficoComtradeImportacaoTradeValue').block({ message: '<img src="/assets/images/loading_gif.gif" />' });

		$http.post('/abicsDataComtradeController/consultarComparativo/tradevalue/1', objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

			try {
				graficoComtradeImportacaoTradeValue.destroy();
			} catch (exception) {

			}

			graficoComtradeImportacaoTradeValue = c3.generate({
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
//			            value: function (value, ratio, id) {
//
//			            	if(value != null && value.toString().length > 0) {
//			            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//			            	}
//			            	return value;
//			            }
			        }
			    }
			});

			$("#graficoComtradeImportacaoTradeValue").append(graficoComtradeImportacaoTradeValue.element);
			graficoComtradeImportacaoTradeValue.categories(listResponse.categories);
			graficoComtradeImportacaoTradeValue.load(listResponse);

			$('#divGraficoComtradeImportacaoTradeValue').unblock({});
		});

//		objConsulta.append('tipo_data_comtrade', 'saca60kg');
		$('#divGraficoComtradeImportacaoSaca60kg').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataComtradeController/consultarComparativo/saca60kg/1', objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

			try {
				graficoComtradeImportacaoSaca60kg.destroy();
			} catch (exception) {

			}

			graficoComtradeImportacaoSaca60kg = c3.generate({
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
//			            value: function (value, ratio, id) {
//
//			            	if(value != null && value.toString().length > 0) {
//			            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//			            	}
//			            	return value;
//			            }
			        }
			    }
			});

			$("#graficoComtradeImportacaoSaca60kg").append(graficoComtradeImportacaoSaca60kg.element);
			graficoComtradeImportacaoSaca60kg.categories(listResponse.categories);
			graficoComtradeImportacaoSaca60kg.load(listResponse);

			$('#divGraficoComtradeImportacaoSaca60kg').unblock({});
		});
	};

	function montarObjetoConsulta() {

		$scope.anosSelecionadoList = [];

		if($scope.allAno) {
			
			$scope.anosSelecionadoList.push(2016);
			$scope.anosSelecionadoList.push(2015);
			$scope.anosSelecionadoList.push(2014);
			$scope.anosSelecionadoList.push(2013);
			$scope.anosSelecionadoList.push(2012);
			$scope.anosSelecionadoList.push(2011);
			$scope.anosSelecionadoList.push(2010);
		} else {
			if($scope.ano2016) {
				$scope.anosSelecionadoList.push(2016);
			}
			if($scope.ano2015) {
				$scope.anosSelecionadoList.push(2015);
			}
			if($scope.ano2014) {
				$scope.anosSelecionadoList.push(2014);
			}
			if($scope.ano2013) {
				$scope.anosSelecionadoList.push(2013);
			}
			if($scope.ano2012) {
				$scope.anosSelecionadoList.push(2012);
			}
			if($scope.ano2011) {
				$scope.anosSelecionadoList.push(2011);
			}
			if($scope.ano2010) {
				$scope.anosSelecionadoList.push(2010);
			}
		}

		$scope.mesesSelecionadoList = [];
		
		if($scope.allMes) {
			$scope.mesesSelecionadoList.push(1);
			$scope.mesesSelecionadoList.push(2);
			$scope.mesesSelecionadoList.push(3);
			$scope.mesesSelecionadoList.push(4);
			$scope.mesesSelecionadoList.push(5);
			$scope.mesesSelecionadoList.push(6);
			$scope.mesesSelecionadoList.push(7);
			$scope.mesesSelecionadoList.push(8);
			$scope.mesesSelecionadoList.push(9);
			$scope.mesesSelecionadoList.push(10);
			$scope.mesesSelecionadoList.push(11);
			$scope.mesesSelecionadoList.push(12);
		} else {
			if($scope.mes1) {
				$scope.mesesSelecionadoList.push(1);
			}
			if($scope.mes2) {
				$scope.mesesSelecionadoList.push(2);
			}
			if($scope.mes3) {
				$scope.mesesSelecionadoList.push(3);
			}
			if($scope.mes4) {
				$scope.mesesSelecionadoList.push(4);
			}
			if($scope.mes5) {
				$scope.mesesSelecionadoList.push(5);
			}
			if($scope.mes6) {
				$scope.mesesSelecionadoList.push(6);
			}
			if($scope.mes7) {
				$scope.mesesSelecionadoList.push(7);
			}
			if($scope.mes8) {
				$scope.mesesSelecionadoList.push(8);
			}
			if($scope.mes9) {
				$scope.mesesSelecionadoList.push(9);
			}
			if($scope.mes10) {
				$scope.mesesSelecionadoList.push(10);
			}
			if($scope.mes11) {
				$scope.mesesSelecionadoList.push(11);
			}
			if($scope.mes12) {
				$scope.mesesSelecionadoList.push(12);
			}
		}
		
		var objConsulta = new FormData();
		objConsulta.append('anos_list', $scope.anosSelecionadoList);
		objConsulta.append('meses_list', $scope.mesesSelecionadoList);
		objConsulta.append('ano_mensal', $scope.anoMensal);
		objConsulta.append('tipo_origem_destino', $scope.tipoOrigemDestino);
		objConsulta.append('tipo_data', $scope.tipoData);
		objConsulta.append('reporter_selecionado_list',$scope.reporterSelecionadoList);
		objConsulta.append('partner_selecionado_list', $scope.partnerSelecionadoList);
		return objConsulta;
	}
	/**MENSAL**/

});
