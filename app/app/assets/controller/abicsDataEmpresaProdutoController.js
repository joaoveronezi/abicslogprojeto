var moduleAbicsDataEmpresaProduto = angular.module('moduleAbicsDataEmpresaProduto', []);

moduleAbicsDataEmpresaProduto.controller('controllerAbicsDataEmpresaProduto', function($scope, $http, $timeout, $modal, $location) {

	$scope.voltarTemplate = function() {
		$location.path('templateAbicsData');
	};
	
	$scope.informacoes = 'Exportações do Brasil para vários países. Fonte Abics, com dados mensais.'+
	'Os valores são o volume comercializado equivalente em sacas de 60kg, a receita cambial e o peso líquido.'+
	'Há também, a variação entre os mesmos meses de distintos anos.'+
	'\nPara consultar escolha qual informação deseja, se sobre empresas ou sobre o tipo de café solúvel.'+
	'Escolha o(s) ano(s) a serem comparados, e depois quais empresas ou tipos de café, deseja comparar. ';
	$scope.caminhoVideo = 'assets/videos/abicsDataEmpresaProduto.mp4';

	$scope.modelRadioEmpresaProduto = 'empresa';
	$scope.empresaList = [];
	$scope.empresaSelecionadaList = [];

	$scope.produtoList = [];
	$scope.produtoSelecionadaList = [];
	
	$scope.anosSelecionadoList = [];
	$scope.anoList = [];
	
	var anoInicialInt = 1995;
	var anoFinalInt = new Date().getFullYear();
	var anoLength = anoFinalInt - anoInicialInt;
	
	$scope.anoInicial = anoInicialInt.toString();
	$scope.anoFinal = anoFinalInt.toString();
	
	for(i=0; i<=anoLength; i++) {
		$scope.anoList.push(anoInicialInt.toString());
		anoInicialInt++;
	}
	
	function orderByNome(a,b) {
		  if (a.nome < b.nome)
		    return -1;
		  if (a.nome > b.nome)
		    return 1;
		  return 0;
		}
	
//	$scope.ano2016 = false;
//	$scope.ano2015 = false;
//	$scope.ano2014 = false;
//	$scope.ano2013 = false;
//	$scope.ano2012 = false;
//	$scope.ano2011 = false;
//	$scope.ano2010 = false;
//	$scope.checkboxAll = false;
//	$scope.checkValidadeForm = false;
	
	
	$scope.clickCheckboxAll = function() {
		
		if($scope.checkboxAll) {
			$scope.anosSelecionadoList = $scope.anoList;
		} else {
			$scope.anosSelecionadoList = [];
		}
//			$scope.checkboxAll = false;
//			$scope.ano2016 = false;
//			$scope.ano2015 = false;
//			$scope.ano2014 = false;
//			$scope.ano2013 = false;
//			$scope.ano2012 = false;
//			$scope.ano2011 = false;
//			$scope.ano2010 = false;
//			$scope.checkValidadeForm = false;
//		} else {
//			$scope.checkboxAll = true;
//			$scope.ano2016 = true;
//			$scope.ano2015 = true;
//			$scope.ano2014 = true;
//			$scope.ano2013 = true;
//			$scope.ano2012 = true;
//			$scope.ano2011 = true;
//			$scope.ano2010 = true;
//			$scope.checkValidadeForm = true;
//		}
	};
	
	$scope.clickCheckboxAno = function() {
		
		if($scope.anosSelecionadoList.length == $scope.anoList.length) {
			$scope.checkboxAll = true;
		} else {
			$scope.checkboxAll = false;
//			$scope.checkValidadeForm = false;
		}
		
//		if($scope.ano2016 && $scope.ano2015 && $scope.ano2014 && $scope.ano2013 && $scope.ano2012 && $scope.ano2011 && $scope.ano2010) {
//			$scope.checkboxAll = true;
//		} else {
//			$scope.checkboxAll = false;
//			$scope.checkValidadeForm = false;
//		}
//		
//		if($scope.ano2016 || $scope.ano2015 || $scope.ano2014 || $scope.ano2013 || $scope.ano2012 || $scope.ano2011 || $scope.ano2010) {
//			$scope.checkValidadeForm = true;
//		}
	};
	
	$scope.changeModelRadio = function() {
		
		$scope.empresaSelecionadaList = [];
		$scope.produtoSelecionadaList = [];
		try {
			graficoAbicsPeso.destroy();
			graficoAbicsVariacaoPeso.destroy();
			graficoAbicsReceita.destroy();
			graficoAbicsVariacaoReceita.destroy();
			graficoAbicsSaca60kg.destroy();
			graficoAbicsVariacaoSaca60kg.destroy();
		} catch (exception) {

		}
	}
	
	$http.get('/abicsDataEmpresaProdutoController/findAllAbicsEmpresa').success(function(response) {
		$scope.empresaList = response;
		$scope.empresaList.sort(orderByNome);
	});

	$http.get('/abicsDataEmpresaProdutoController/findAllAbicsProduto').success(function(response) {
		$scope.produtoList = response;
		$scope.produtoList.sort(orderByNome);
	});
	
	$scope.consultar = function() {
		
//		$scope.anosSelecionadoList = [];
		
//		if($scope.ano2016) {
//			$scope.anosSelecionadoList.push(2016);
//		}
//		if($scope.ano2015) {
//			$scope.anosSelecionadoList.push(2015);
//		}
//		if($scope.ano2014) {
//			$scope.anosSelecionadoList.push(2014);
//		}
//		if($scope.ano2013) {
//			$scope.anosSelecionadoList.push(2013);
//		}
//		if($scope.ano2012) {
//			$scope.anosSelecionadoList.push(2012);
//		}
//		if($scope.ano2011) {
//			$scope.anosSelecionadoList.push(2011);
//		}
//		if($scope.ano2010) {
//			$scope.anosSelecionadoList.push(2010);
//		}

//		if($scope.anosSelecionadoList == null || $scope.anosSelecionadoList.length == 0) {
//			alert('Selecione um ou mais anos para a comparação');
//			return;
//		}
		
		var objConsulta = new FormData();
//		objConsulta.append('produto_list', $scope.produtoSelecionadaList);
//		objConsulta.append('anos_list', anosSelecionadoList);
//
//		$('#divGraficoAbicsProdutoPeso').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//		$http.post('/abicsDataEmpresaProdutoController/graficoAbicsProduto/peso', objConsulta,
//				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
		
		
		var stringConsulta = '';
		var stringVariacaoConsulta = ''; 
		var objConsulta = new FormData();
		
		if($scope.modelRadioEmpresaProduto == 'empresa') {
			$scope.listaIdEmpresas = [];
			
			angular.forEach($scope.empresaSelecionadaList, function(value, key) {
				  $scope.listaIdEmpresas.push(value.id);
			});
			
			stringVariacaoConsulta = 'variacaoAbicsEmpresa';
			stringConsulta = 'graficoAbicsEmpresa'
			objConsulta.append('empresa_list', $scope.listaIdEmpresas);
		} else {
			$scope.listaIdProdutos = [];
			
			angular.forEach($scope.produtoSelecionadaList, function(value, key) {
				  $scope.listaIdProdutos.push(value.id);
			});
			
			stringVariacaoConsulta = 'variacaoAbicsProduto';
			stringConsulta = 'graficoAbicsProduto';
			objConsulta.append('produto_list', $scope.listaIdProdutos);
		}
		
		objConsulta.append('anos_list', $scope.anosSelecionadoList.reverse());

		$('#divGraficoAbicsPeso').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataEmpresaProdutoController/'+stringConsulta+'/peso', objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

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
				},
				tooltip: {
			        format: {
			        	title: function (d) { return 'Mês  ' + ++d; }
			        }
			    }
			});

			$("#graficoAbicsPeso").append(graficoAbicsPeso.element);
			graficoAbicsPeso.categories(listResponse.categories);
			graficoAbicsPeso.load(listResponse);

			$http.post('/abicsDataEmpresaProdutoController/'+stringVariacaoConsulta+'/peso', objConsulta,
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
								{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
							]
				});

				$("#graficoAbicsVariacaoPeso").append(graficoAbicsVariacaoPeso.element);
				graficoAbicsVariacaoPeso.categories(listResponse.categories);
				graficoAbicsVariacaoPeso.load(listResponse);

				$('#divGraficoAbicsPeso').unblock({});
			});

		});

		$('#divGraficoAbicsReceita').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataEmpresaProdutoController/'+stringConsulta+'/receita', objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

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
				},
				tooltip: {
			        format: {
			        	title: function (d) { return 'Mês  ' + ++d; }
			        }
			    }
			});

			$("#graficoAbicsReceita").append(graficoAbicsReceita.element);
			graficoAbicsReceita.categories(listResponse.categories);
			graficoAbicsReceita.load(listResponse);

			$http.post('/abicsDataEmpresaProdutoController/'+stringVariacaoConsulta+'/receita', objConsulta,
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
								{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
							]
				});

				$("#graficoAbicsVariacaoReceita").append(graficoAbicsVariacaoReceita.element);
				graficoAbicsVariacaoReceita.categories(listResponse.categories);
				graficoAbicsVariacaoReceita.load(listResponse);

				$('#divGraficoAbicsReceita').unblock({});
			});

		});

		$('#divGraficoAbicsSaca60kg').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataEmpresaProdutoController/'+stringConsulta+'/saca60kg', objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

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
				},
				tooltip: {
			        format: {
			        	title: function (d) { return 'Mês  ' + ++d; }
			        }
			    }
			});

			$("#graficoAbicsSaca60kg").append(graficoAbicsSaca60kg.element);
			graficoAbicsSaca60kg.categories(listResponse.categories);
			graficoAbicsSaca60kg.load(listResponse);

			$http.post('/abicsDataEmpresaProdutoController/'+stringVariacaoConsulta+'/saca60kg', objConsulta,
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
								{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
							]
				});

				$("#graficoAbicsVariacaoSaca60kg").append(graficoAbicsVariacaoSaca60kg.element);
				graficoAbicsVariacaoSaca60kg.categories(listResponse.categories);
				graficoAbicsVariacaoSaca60kg.load(listResponse);

				$('#divGraficoAbicsSaca60kg').unblock({});
			});
		});
	};

	
	


//	$scope.allEmpresa = false;
//	$scope.ano2016 = false;
//	$scope.ano2015 = false;
//	$scope.ano2014 = false;
//	$scope.ano2013 = false;
//	$scope.ano2012 = false;
//	$scope.ano2011 = false;
//	$scope.ano2010 = false;
//
//	$scope.allProduto = false;
//	$scope.anoProduto2016 = false;
//	$scope.anoProduto2015 = false;
//	$scope.anoProduto2014 = false;
//	$scope.anoProduto2013 = false;
//	$scope.anoProduto2012 = false;
//	$scope.anoProduto2011 = false;
//	$scope.anoProduto2010 = false;
//
//	

//	$scope.clickAllEmpresa = function() { 
//		if($scope.allEmpresa) {
//			$scope.ano2016 = true;
//			$scope.ano2015 = true;
//			$scope.ano2014 = true;
//			$scope.ano2013 = true;
//			$scope.ano2012 = true;
//			$scope.ano2011 = true;
//			$scope.ano2010 = true;
//		} else {
//			$scope.ano2016 = false;
//			$scope.ano2015 = false;
//			$scope.ano2014 = false;
//			$scope.ano2013 = false;
//			$scope.ano2012 = false;
//			$scope.ano2011 = false;
//			$scope.ano2010 = false;
//		}
//	};
//
//	$scope.clickAllProduto = function() { 
//		if($scope.allProduto) {
//			$scope.anoProduto2016 = true;
//			$scope.anoProduto2015 = true;
//			$scope.anoProduto2014 = true;
//			$scope.anoProduto2013 = true;
//			$scope.anoProduto2012 = true;
//			$scope.anoProduto2011 = true;
//			$scope.anoProduto2010 = true;
//		} else {
//			$scope.anoProduto2016 = false;
//			$scope.anoProduto2015 = false;
//			$scope.anoProduto2014 = false;
//			$scope.anoProduto2013 = false;
//			$scope.anoProduto2012 = false;
//			$scope.anoProduto2011 = false;
//			$scope.anoProduto2010 = false;
//		}
//	};

	
//	$scope.consultarEmpresa = function() {
//
//		var anosSelecionadoList = [];
//
//		if($scope.ano2016) {
//			anosSelecionadoList.push(2016);
//		}
//		if($scope.ano2015) {
//			anosSelecionadoList.push(2015);
//		}
//		if($scope.ano2014) {
//			anosSelecionadoList.push(2014);
//		}
//		if($scope.ano2013) {
//			anosSelecionadoList.push(2013);
//		}
//		if($scope.ano2012) {
//			anosSelecionadoList.push(2012);
//		}
//		if($scope.ano2011) {
//			anosSelecionadoList.push(2011);
//		}
//		if($scope.ano2010) {
//			anosSelecionadoList.push(2010);
//		}
//
//		if(anosSelecionadoList == null || anosSelecionadoList.length == 0) {
//			alert('Selecione um ou mais anos para a comparação');
//			return;
//		}
//		
//		var objConsulta = new FormData();
//		objConsulta.append('empresa_list', $scope.empresaSelecionadaList);
//		objConsulta.append('anos_list', anosSelecionadoList);
//
//		$('#divGraficoAbicsEmpresaPeso').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//		$http.post('/abicsDataEmpresaProdutoController/graficoAbicsEmpresa/peso', objConsulta,
//				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//			try {
//				graficoAbicsEmpresaPeso.destroy();
//			} catch (exception) {
//
//			}
//
//			graficoAbicsEmpresaPeso = c3.generate({
//				data: {
//					columns: [],
//					type : 'line'
//				},
//				axis: {
//					x: {
//						type: 'category',
//						tick: {
//			               rotate: 75,
//			               multiline: false
//						},
//						height: 80
//					}
//				},
//				grid: {
//					y : {show: true}
//				},
//				tooltip: {
//			        format: {
//			        	title: function (d) { return 'Mês  ' + ++d; }
//			        }
//			    }
//			});
//
//			$("#graficoAbicsEmpresaPeso").append(graficoAbicsEmpresaPeso.element);
//			graficoAbicsEmpresaPeso.categories(listResponse.categories);
//			graficoAbicsEmpresaPeso.load(listResponse);
//
//			$http.post('/abicsDataEmpresaProdutoController/variacaoAbicsEmpresa/peso', objConsulta,
//					{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//				try {
//					graficoAbicsEmpresaVariacaoPeso.destroy();
//				} catch (exception) {
//
//				}
//
//				graficoAbicsEmpresaVariacaoPeso = c3.generate({
//					data: {
//						columns: [],
//						type : 'line'
//					},
//					axis: {
//						x: {
//							type: 'category',
//							tick: {
//				               rotate: 75,
//				               multiline: false
//							},
//							height: 80
//						}
//					},
//					grid: {
//						y : {show: true}
//					},
//					tooltip: {
//				        format: {
//				        	title: function (d) { return 'Mês  ' + ++d; },
//				            value: function (value, ratio, id) {
//
//				            	if(value != null && value.toString().length > 0) {
//				            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//				            	}
//				            	return value;
//				            }
//				        }
//				    },
//					regions: [
//								{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
//								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
//							]
//				});
//
//				$("#graficoAbicsEmpresaVariacaoPeso").append(graficoAbicsEmpresaVariacaoPeso.element);
//				graficoAbicsEmpresaVariacaoPeso.categories(listResponse.categories);
//				graficoAbicsEmpresaVariacaoPeso.load(listResponse);
//
//				$('#divGraficoAbicsEmpresaPeso').unblock({});
//			});
//
//		});
//
//		$('#divGraficoAbicsEmpresaReceita').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//		$http.post('/abicsDataEmpresaProdutoController/graficoAbicsEmpresa/receita', objConsulta,
//				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//			try {
//				graficoAbicsEmpresaReceita.destroy();
//			} catch (exception) {
//
//			}
//
//			graficoAbicsEmpresaReceita = c3.generate({
//				data: {
//					columns: [],
//					type : 'line'
//				},
//				axis: {
//					x: {
//						type: 'category',
//						tick: {
//			               rotate: 75,
//			               multiline: false
//						},
//						height: 80
//					}
//				},
//				grid: {
//					y : {show: true}
//				},
//				tooltip: {
//			        format: {
//			        	title: function (d) { return 'Mês  ' + ++d; }
//			        }
//			    }
//			});
//
//			$("#graficoAbicsEmpresaReceita").append(graficoAbicsEmpresaReceita.element);
//			graficoAbicsEmpresaReceita.categories(listResponse.categories);
//			graficoAbicsEmpresaReceita.load(listResponse);
//
//			$http.post('/abicsDataEmpresaProdutoController/variacaoAbicsEmpresa/receita', objConsulta,
//					{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//				try {
//					graficoAbicsEmpresaVariacaoReceita.destroy();
//				} catch (exception) {
//
//				}
//
//				graficoAbicsEmpresaVariacaoReceita = c3.generate({
//					data: {
//						columns: [],
//						type : 'line'
//					},
//					axis: {
//						x: {
//							type: 'category',
//							tick: {
//				               rotate: 75,
//				               multiline: false
//							},
//							height: 80
//						}
//					},
//					grid: {
//						y : {show: true}
//					},
//					tooltip: {
//				        format: {
//				        	title: function (d) { return 'Mês  ' + ++d; },
//				            value: function (value, ratio, id) {
//
//				            	if(value != null && value.toString().length > 0) {
//				            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//				            	}
//				            	return value;
//				            }
//				        }
//				    },
//					regions: [
//								{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
//								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
//							]
//				});
//
//				$("#graficoAbicsEmpresaVariacaoReceita").append(graficoAbicsEmpresaVariacaoReceita.element);
//				graficoAbicsEmpresaVariacaoReceita.categories(listResponse.categories);
//				graficoAbicsEmpresaVariacaoReceita.load(listResponse);
//
//				$('#divGraficoAbicsEmpresaReceita').unblock({});
//			});
//
//		});
//
//		$('#divGraficoAbicsEmpresaSaca60kg').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//		$http.post('/abicsDataEmpresaProdutoController/graficoAbicsEmpresa/saca60kg', objConsulta,
//				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//			try {
//				graficoAbicsEmpresaSaca60kg.destroy();
//			} catch (exception) {
//
//			}
//
//			graficoAbicsEmpresaSaca60kg = c3.generate({
//				data: {
//					columns: [],
//					type : 'line'
//				},
//				axis: {
//					x: {
//						type: 'category',
//						tick: {
//			               rotate: 75,
//			               multiline: false
//						},
//						height: 80
//					}
//				},
//				grid: {
//					y : {show: true}
//				},
//				tooltip: {
//			        format: {
//			        	title: function (d) { return 'Mês  ' + ++d; }
//			        }
//			    }
//			});
//
//			$("#graficoAbicsEmpresaSaca60kg").append(graficoAbicsEmpresaSaca60kg.element);
//			graficoAbicsEmpresaSaca60kg.categories(listResponse.categories);
//			graficoAbicsEmpresaSaca60kg.load(listResponse);
//
//			$http.post('/abicsDataEmpresaProdutoController/variacaoAbicsEmpresa/saca60kg', objConsulta,
//					{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//				try {
//					graficoAbicsEmpresaVariacaoSaca60kg.destroy();
//				} catch (exception) {
//
//				}
//
//				graficoAbicsEmpresaVariacaoSaca60kg = c3.generate({
//					data: {
//						columns: [],
//						type : 'line'
//					},
//					axis: {
//						x: {
//							type: 'category',
//							tick: {
//				               rotate: 75,
//				               multiline: false
//							},
//							height: 80
//						}
//					},
//					grid: {
//						y : {show: true}
//					},
//					tooltip: {
//				        format: {
//				        	title: function (d) { return 'Mês  ' + ++d; },
//				            value: function (value, ratio, id) {
//
//				            	if(value != null && value.toString().length > 0) {
//				            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//				            	}
//				            	return value;
//				            }
//				        }
//				    },
//					regions: [
//								{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
//								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
//							]
//				});
//
//				$("#graficoAbicsEmpresaVariacaoSaca60kg").append(graficoAbicsEmpresaVariacaoSaca60kg.element);
//				graficoAbicsEmpresaVariacaoSaca60kg.categories(listResponse.categories);
//				graficoAbicsEmpresaVariacaoSaca60kg.load(listResponse);
//
//				$('#divGraficoAbicsEmpresaSaca60kg').unblock({});
//			});
//		});
//	};
//
//	$scope.consultarProduto = function() {
//
//		var anosSelecionadoList = [];
//
//		if($scope.anoProduto2016) {
//			anosSelecionadoList.push(2016);
//		}
//		if($scope.anoProduto2015) {
//			anosSelecionadoList.push(2015);
//		}
//		if($scope.anoProduto2014) {
//			anosSelecionadoList.push(2014);
//		}
//		if($scope.anoProduto2013) {
//			anosSelecionadoList.push(2013);
//		}
//		if($scope.anoProduto2012) {
//			anosSelecionadoList.push(2012);
//		}
//		if($scope.anoProduto2011) {
//			anosSelecionadoList.push(2011);
//		}
//		if($scope.anoProduto2010) {
//			anosSelecionadoList.push(2010);
//		}
//		if(anosSelecionadoList == null || anosSelecionadoList.length == 0) {
//			alert('Selecione um ou mais anos para a comparação');
//			return;
//		}
//		var objConsulta = new FormData();
//		objConsulta.append('produto_list', $scope.produtoSelecionadaList);
//		objConsulta.append('anos_list', anosSelecionadoList);
//
//		$('#divGraficoAbicsProdutoPeso').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//		$http.post('/abicsDataEmpresaProdutoController/graficoAbicsProduto/peso', objConsulta,
//				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//			try {
//				graficoAbicsProdutoPeso.destroy();
//			} catch (exception) {
//
//			}
//
//			graficoAbicsProdutoPeso = c3.generate({
//				data: {
//					columns: [],
//					type : 'line'
//				},
//				axis: {
//					x: {
//						type: 'category',
//						tick: {
//			               rotate: 75,
//			               multiline: false
//						},
//						height: 80
//					}
//				},
//				grid: {
//					y : {show: true}
//				},
//				tooltip: {
//			        format: {
//			        	title: function (d) { return 'Mês  ' + ++d; }
////			            value: function (value, ratio, id) {
////
////			            	if(value != null && value.toString().length > 0) {
////			            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
////			            	}
////			            	return value;
////			            }
//			        }
//			    }
//			});
//
//			$("#graficoAbicsProdutoPeso").append(graficoAbicsProdutoPeso.element);
//			graficoAbicsProdutoPeso.categories(listResponse.categories);
//			graficoAbicsProdutoPeso.load(listResponse);
//
//			$http.post('/abicsDataEmpresaProdutoController/variacaoAbicsProduto/peso', objConsulta,
//					{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//				try {
//					graficoAbicsProdutoVariacaoPeso.destroy();
//				} catch (exception) {
//
//				}
//
//				graficoAbicsProdutoVariacaoPeso = c3.generate({
//					data: {
//						columns: [],
//						type : 'line'
//					},
//					axis: {
//						x: {
//							type: 'category',
//							tick: {
//				               rotate: 75,
//				               multiline: false
//							},
//							height: 80
//						}
//					},
//					grid: {
//						y : {show: true}
//					},
//					tooltip: {
//				        format: {
//				        	title: function (d) { return 'Mês  ' + ++d; },
//				            value: function (value, ratio, id) {
//
//				            	if(value != null && value.toString().length > 0) {
//				            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//				            	}
//				            	return value;
//				            }
//				        }
//				    },
//					regions: [
//								{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
//								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
//							]
//				});
//
//				$("#graficoAbicsProdutoVariacaoPeso").append(graficoAbicsProdutoVariacaoPeso.element);
//				graficoAbicsProdutoVariacaoPeso.categories(listResponse.categories);
//				graficoAbicsProdutoVariacaoPeso.load(listResponse);
//
//				$('#divGraficoAbicsProdutoPeso').unblock({});
//			});
//		});
//
//		$('#divGraficoAbicsProdutoReceita').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//		$http.post('/abicsDataEmpresaProdutoController/graficoAbicsProduto/receita', objConsulta,
//				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//			try {
//				graficoAbicsProdutoReceita.destroy();
//			} catch (exception) {
//
//			}
//
//			graficoAbicsProdutoReceita = c3.generate({
//				data: {
//					columns: [],
//					type : 'line'
//				},
//				axis: {
//					x: {
//						type: 'category',
//						tick: {
//			               rotate: 75,
//			               multiline: false
//						},
//						height: 80
//					}
//				},
//				grid: {
//					y : {show: true}
//				},
//				tooltip: {
//			        format: {
//			        	title: function (d) { return 'Mês  ' + ++d; }
//			        }
//			    }
//			});
//
//			$("#graficoAbicsProdutoReceita").append(graficoAbicsProdutoReceita.element);
//			graficoAbicsProdutoReceita.categories(listResponse.categories);
//			graficoAbicsProdutoReceita.load(listResponse);
//
//			$http.post('/abicsDataEmpresaProdutoController/variacaoAbicsProduto/receita', objConsulta,
//					{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//				try {
//					graficoAbicsProdutoVariacaoReceita.destroy();
//				} catch (exception) {
//
//				}
//
//				graficoAbicsProdutoVariacaoReceita = c3.generate({
//					data: {
//						columns: [],
//						type : 'line'
//					},
//					axis: {
//						x: {
//							type: 'category',
//							tick: {
//				               rotate: 75,
//				               multiline: false
//							},
//							height: 80
//						}
//					},
//					grid: {
//						y : {show: true}
//					},
//					tooltip: {
//				        format: {
//				        	title: function (d) { return 'Mês  ' + ++d; },
//				            value: function (value, ratio, id) {
//
//				            	if(value != null && value.toString().length > 0) {
//				            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//				            	}
//				            	return value;
//				            }
//				        }
//				    },
//					regions: [
//								{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
//								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
//							]
//				});
//
//				$("#graficoAbicsProdutoVariacaoReceita").append(graficoAbicsProdutoVariacaoReceita.element);
//				graficoAbicsProdutoVariacaoReceita.categories(listResponse.categories);
//				graficoAbicsProdutoVariacaoReceita.load(listResponse);
//
//				$('#divGraficoAbicsProdutoReceita').unblock({});
//			});
//		});
//
//		$('#divGraficoAbicsProdutoSaca60kg').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//		$http.post('/abicsDataEmpresaProdutoController/graficoAbicsProduto/saca60kg', objConsulta,
//				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//			try {
//				graficoAbicsProdutoSaca60kg.destroy();
//			} catch (exception) {
//
//			}
//
//			graficoAbicsProdutoSaca60kg = c3.generate({
//				data: {
//					columns: [],
//					type : 'line'
//				},
//				axis: {
//					x: {
//						type: 'category',
//						tick: {
//			               rotate: 75,
//			               multiline: false
//						},
//						height: 80
//					}
//				},
//				grid: {
//					y : {show: true}
//				},
//				tooltip: {
//			        format: {
//			        	title: function (d) { return 'Mês  ' + ++d; }
//			        }
//			    }
//			});
//
//			$("#graficoAbicsProdutoSaca60kg").append(graficoAbicsProdutoSaca60kg.element);
//			graficoAbicsProdutoSaca60kg.categories(listResponse.categories);
//			graficoAbicsProdutoSaca60kg.load(listResponse);
//
//			$http.post('/abicsDataEmpresaProdutoController/variacaoAbicsProduto/saca60kg', objConsulta,
//					{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
//
//				try {
//					graficoAbicsProdutoVariacaoSaca60kg.destroy();
//				} catch (exception) {
//
//				}
//
//				graficoAbicsProdutoVariacaoSaca60kg = c3.generate({
//					data: {
//						columns: [],
//						type : 'line'
//					},
//					axis: {
//						x: {
//							type: 'category',
//							tick: {
//				               rotate: 75,
//				               multiline: false
//							},
//							height: 80
//						}
//					},
//					grid: {
//						y : {show: true}
//					},
//					tooltip: {
//				        format: {
//				        	title: function (d) { return 'Mês  ' + ++d; },
//				            value: function (value, ratio, id) {
//
//				            	if(value != null && value.toString().length > 0) {
//				            		return Number(value).toFixed(3).toString().replace(".",",").concat(" %");
//				            	}
//				            	return value;
//				            }
//				        }
//				    },
//					regions: [
//								{axis: 'y', start: 0, end: 100, class: 'regionGreen'},
//								{axis: 'y', start: -100, end: 0, class: 'regionRed'}
//							]
//				});
//
//				$("#graficoAbicsProdutoVariacaoSaca60kg").append(graficoAbicsProdutoVariacaoSaca60kg.element);
//				graficoAbicsProdutoVariacaoSaca60kg.categories(listResponse.categories);
//				graficoAbicsProdutoVariacaoSaca60kg.load(listResponse);
//
//				$('#divGraficoAbicsProdutoSaca60kg').unblock({});
//			});
//		});
//	};
});
