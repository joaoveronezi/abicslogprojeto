var moduleGrafico = angular.module('moduleGrafico', []);

//moduleGrafico.directive('directiveModalDetalhesCotacao', function() {
//	return {
//		templateUrl : '/assets/directives/directiveModalDetalhesCotacao.html'
//	};
//});
//
//moduleGrafico.directive('directiveModalGraficoCotacao', function() {
//	return {
//		templateUrl : '/assets/directives/directiveModalGraficoCotacao.html'
//	};
//});

moduleGrafico.controller('controllerGrafico', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Aqui possui todas informações sumarizadas neste dashboard.';
	$scope.caminhoVideo = 'assets/videos/relatorio-complete-reports.mp4';

	var tipoGraficoTonelada = false;
	$scope.saca60kgTonelada = 'Saca 60kg';
	$scope.exibirMedia = false;

	$scope.dataCambioIsOpen = false;
	$scope.objetoDataCambioSelecionada = moment().add(-1, 'days').toDate();
	$scope.cambio = {};
	$scope.cotacaoList = [];
	$scope.cotacaoNacionalList = [];
	$scope.cotacaoInternacionalList = [];

	$scope.cotacaoSelectedList = [];
	$scope.buttonImprimirRelatorioCompletoEnable = false;

	$scope.selectedReltorioNacional = false;
	$scope.selectedGraficoNacional = false;
	$scope.selectedReltorioInternacional = false;
	$scope.selectedGraficoInternacional = false;

	$scope.dataInicial = moment().add(-1, 'months').toDate();
	$scope.dataFinal = moment().toDate();

//	$scope.tipoAgrupamento = 0;
	$scope.dataInicialIsOpen = false;

	$scope.openDataInicial = function() {
		$scope.dataInicialIsOpen = true;
	}

	$scope.dataFinalIsOpen = false;

	$scope.openDataFinal = function() {
		$scope.dataFinalIsOpen = true;
	}

	$scope.openDataCambio = function() {
		$scope.dataCambioIsOpen = true;
	};

	changeDataCambio();
	function changeDataCambio() {
		try {
			graficoValorMercadoria.destroy();
		} catch (exception) {

		}

		$scope.selectedReltorioNacional = false;
		$scope.selectedGraficoNacional = false;
		$scope.selectedReltorioInternacional = false;
		$scope.selectedGraficoInternacional = false;
		tipoGraficoTonelada = false;
		$scope.exibirMedia = false;
		$scope.saca60kgTonelada = 'Saca 60kg';

		$scope.cotacaoNacionalList = [];
		$scope.cotacaoInternacionalList = [];

		$('#divPaginaGrafico').block({ message: '<img src="/assets/images/loading_gif.gif" />' });

		$http.get('/relatorioController/findCambioByData/' + $scope.objetoDataCambioSelecionada.getTime()).success(function(cambioResponse) {
			$scope.cambio = cambioResponse;
		});

		$http.get('/relatorioController/simularRelatorio/' + $scope.objetoDataCambioSelecionada.getTime()).success(function(listResponse) {
			$scope.cotacaoList = listResponse;

			if($scope.cotacaoList != null && $scope.cotacaoList.length > 0) {

				angular.forEach($scope.cotacaoList, function(cotacao, key) {

					if(cotacao.nacional == 1) {
						$scope.cotacaoNacionalList.push(cotacao);
					} else {
						$scope.cotacaoInternacionalList.push(cotacao);
					}

					angular.forEach(cotacao.categoriaList, function(categoria, key2) {

						angular.forEach(categoria.itemList, function(item, key3) {

							if(item.isCategoriaTotal == 1) {
								if(item.id == 3) {

									cotacao.valorCIFReal = item.valor;
									cotacao.valorCIFDolar = item.valorDolar;
									cotacao.valorCIFEuro = item.valorEuro;

									cotacao.valorCIFRealSaca60kg = item.valorSaca60kg;
									cotacao.valorCIFDolarSaca60kg = item.valorDolarSaca60kg;
									cotacao.valorCIFEuroSaca60kg = item.valorEuroSaca60kg;
								}
							}
						});
					});
				});
			}

			$('#divPaginaGrafico').unblock({});
		});

//		$http.get('/relatorioController/graficoComparativoValorTotalImportacao/' + $scope.objetoDataCambioSelecionada.getTime()).success(function(listResponse) {
//
//			try {
//				graficoValorMercadoria.destroy();
//			} catch (exception) {
//
//			}
//
//			graficoValorMercadoria = c3.generate({
//				bindto: '#graficoValorMercadoria',
//				data: {
//					columns: [],
//					type : 'line'
//	//				types : {'mediaEuro': 'line', 'mediaDolar' : 'line'}
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
//	//			},
//	//			grid: {
//	//				y : {
//	//					lines: [{value: 1, text: 'Real'}]
//	//				}
//				}
//			});
//
//			graficoValorMercadoria.categories(listResponse.categories);
//			graficoValorMercadoria.load(listResponse);
//
			// $('#divRelatorioImportacao').unblock({});
//		});
	};

	$scope.changeDataCambio = function() {
		changeDataCambio();
	}


//	$http.get('/graficoController/findAllCotacaoCategoriaItem').success(function(listResponse) {
//		$scope.cotacaoList = listResponse;
//	});

	$scope.modificarTipoGrafico = function() {

		if(tipoGraficoTonelada) {
            $("#fa-grafico").addClass('rotate180');
            tipoGraficoTonelada = false;
            $scope.saca60kgTonelada = 'Saca 60kg';
        } else {
            $("#fa-grafico").removeClass('rotate180');
            tipoGraficoTonelada = true;
            $scope.saca60kgTonelada = 'Tonelada';
        }
	};

//	$scope.selectCheckBoxRelatorio = function(tipoNacional, idCotacao) {
//		console.log('RELATORIO  SIMPLESSS ' + tipoNacional + '  ' + idCotacao);
//	};
//
//	$scope.selectCheckBoxGrafico = function(tipoNacional, idCotacao) {
//		console.log('SGRAFICo IMPLESSS ' + tipoNacional + '  ' + idCotacao);
//	};

//	$scope.selectAllCheckBoxRelatorio = function(tipoNacional) {
////		console.log('relatorio ' + tipoNacional);
//
//		if(tipoNacional == 1) {
//			angular.forEach($scope.cotacaoNacionalList, function(cotacao, key) {
//				cotacao.selectedRelatorio = $scope.selectedRelatorioNacional;
//			});
//		} else {
//			angular.forEach($scope.cotacaoInternacionalList, function(cotacao, key) {
//				cotacao.selectedRelatorio = $scope.selectedRelatorioInternacional;
//			});
//		}
//	};

	$scope.selectAllCheckBoxGrafico = function(tipoNacional) {
//		console.log('grafico ' + tipoNacional);
		$scope.buttonImprimirRelatorioCompletoEnable = false;
		if(tipoNacional == 1) {
			angular.forEach($scope.cotacaoNacionalList, function(cotacao, key) {
				cotacao.selectedGrafico = $scope.selectedGraficoNacional;
			});
		} else {
			angular.forEach($scope.cotacaoInternacionalList, function(cotacao, key) {
				cotacao.selectedGrafico = $scope.selectedGraficoInternacional;
			});
		}
	};


	$scope.abrirModalDetalhes = function(cotacao) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalDetalhesCotacao',
            controller: modalControlerDetalhesCotacao,
            size: 'lg',
            resolve: {
            	cotacaoParametro: function () {
                    return cotacao;
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
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
		}, function (parametro) {
	    });
	};

	$scope.gerarRelatorioCompleto = function() {

		$scope.cotacaoSelectedList = [];

		angular.forEach($scope.cotacaoNacionalList, function(cotacao, key) {

			if(cotacao.selectedGrafico) {
				$scope.cotacaoSelectedList.push(cotacao.id);
			}
		});

		angular.forEach($scope.cotacaoInternacionalList, function(cotacao, key) {

			if(cotacao.selectedGrafico) {
				$scope.cotacaoSelectedList.push(cotacao.id);
			}
		});

		if($scope.cotacaoSelectedList.length != 0) {

			$scope.buttonImprimirRelatorioCompletoEnable = true;
	       var formData = new FormData();
	       formData.append('data_inicial', $scope.dataInicial.getTime());
	       formData.append('data_final', $scope.dataFinal.getTime());

	       formData.append('exibir_media', $scope.exibirMedia);
	       formData.append('tipo_grafico_tonelada', tipoGraficoTonelada);
//	       if($scope.exibirMedia) {
//	    	   formData.append('exibir_media', $scope.exibirMedia);
//	       } else {
//	    	   formData.append('exibir_media', $scope.exibirMedia);
//	       }

//	       if($scope.agruparIndividualMedia) {
//	    	   formData.append('tipo_agrupamento', 2);
//	       } else {
//	    	   formData.append('tipo_agrupamento', tipoGraficoTonelada ? 0 : 1);
//	       }

	       formData.append('cotacao_selected_list', $scope.cotacaoSelectedList);

			$('#divPaginaGrafico').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
			$http.post('/graficoController/relatorioCompleto', formData, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {

				try {
					graficoValorMercadoria.destroy();
				} catch (exception) {

				}

				graficoValorMercadoria = c3.generate({
//					bindto: '#graficoValorMercadoria',
					data: {
						columns: [],
						type : 'line'
//						types : {'Media Indonesia': 'line', 'Media Vietnam' : 'line', 'Media Costa do Marfim' : 'line',
//							'Media Cotacao Brasil' : 'spline', 'Media Nacional Teste' : 'spline',
//							'Media Nacional' : 'spline', 'Media Internacional' : 'line'}
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
					regions: [
						//{axis: 'y', start: 0, end: 3000, class: 'regionWhite'},
						{axis: 'y', start: 3500, end: 4000, class: 'regionY'},
						{axis: 'y', start: 2000, end: 2500, class: 'regionGreen'},
						]
				});

				$("#graficoValorMercadoria").append(graficoValorMercadoria.element);
				graficoValorMercadoria.categories(listResponse.categories);
				graficoValorMercadoria.load(listResponse);

				$('#divPaginaGrafico').unblock({});
			});
		} else {
        	$scope.alertaSistemaTemplate.message = 'Selecione ao menos uma cotação';
        	$scope.alertaSistemaTemplate.type = 'warning';
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = 'warning-sign';
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}
	};

	$scope.imprimirRelatorioAgregado = function() {

		$('#divPaginaGrafico').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$('defs').remove();
	    // Copy CSS styles to Canvas
	    var styles = graficoUtilInlineAllStyles();
	    // Create PNG image
	    var divGrafico = $('#graficoValorMercadoria')[0];
	    var canvas = document.createElement('canvas');
	    canvas.width = divGrafico.clientWidth * 1; // qnto maior, menor o grafico no pdf
	    canvas.height = divGrafico.clientHeight * 1; // qnto maior, menor o grafico no pdf

	    var canvasContext = canvas.getContext('2d');
	 //		   var svg = $.trim($('#graficoValorMercadoria > svg')[0].outerHTML);
	    var svg = $.trim(divGrafico.children[0].children[0].outerHTML);
	    canvasContext.drawSvg(svg, 0, 0);

//
//		  $('defs').remove();
//		   // Copy CSS styles to Canvas
//		   var styles = graficoUtilInlineAllStyles();
//		   // Create PNG image
//		   var canvas = document.createElement('canvas');
//		   canvas.width = $('#graficoValorMercadoria').width() * 1; // qnto maior, menor o grafico no pdf
//		   canvas.height = $('#graficoValorMercadoria').height() * 1; // qnto maior, menor o grafico no pdf
//
//		   var canvasContext = canvas.getContext('2d');
//		   var svg = $.trim($('#graficoValorMercadoria > svg')[0].outerHTML);
//		   canvasContext.drawSvg(svg, 0, 0);

		   var byteArr = canvas.toDataURL("png");

		   var formData = new FormData();
		   formData.append('imagem_chart_png', byteArr);
//		   formData.append('data_inicial', $scope.dataInicial.getTime());
//		   formData.append('data_final', $scope.dataFinal.getTime());
		   formData.append('data_hoje', $scope.objetoDataCambioSelecionada.getTime());


//		   if($scope.agruparIndividualMedia) {
//			   formData.append('tipo_agrupamento', 2);
//		   } else {
//			   formData.append('tipo_agrupamento', tipoGraficoTonelada ? 0 : 1);
//		   }

		   formData.append('cotacao_selected_list', $scope.cotacaoSelectedList);

			$http.post('/graficoController/imprimirRelatorioAgregado', formData,
			   {transformRequest: angular.identity, headers: {'Content-Type': undefined},
				responseType: 'arraybuffer'}).success(function(data) {

				   var a = document.createElement("a");
				   document.body.appendChild(a);
				   a.style = "display: none";
				   var file = new Blob([data], {type: 'application/pdf'}); // data.contents
				   var fileURL = URL.createObjectURL(file);
				   a.target = "_blank";
				   a.href = fileURL;
				   a.download = "grafico_agregado.pdf";
				   a.click();

				$('#divPaginaGrafico').unblock({});
			});
	};

//	$scope.imprimirRelatorioCompleto = function() {
//
//		$('#divPaginaGrafico').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//		  $('defs').remove();
//		   // Copy CSS styles to Canvas
//		   var styles = graficoUtilInlineAllStyles();
//		   // Create PNG image
//		   var divGrafico = $('#graficoValorMercadoria')[0];
//		   var canvas = document.createElement('canvas');
//		   canvas.width = divGrafico.clientWidth * 1; // qnto maior, menor o grafico no pdf
//		   canvas.height = divGrafico.clientHeight * 1; // qnto maior, menor o grafico no pdf
//
//		   var canvasContext = canvas.getContext('2d');
////		   var svg = $.trim($('#graficoValorMercadoria > svg')[0].outerHTML);
//		   var svg = $.trim(divGrafico.children[0].children[0].outerHTML);
//		   canvasContext.drawSvg(svg, 0, 0);
//
//		   var byteArr = canvas.toDataURL("png");
//
//		   var formData = new FormData();
//		   formData.append('imagem_chart_png', byteArr);
//		   formData.append('data_inicial', $scope.dataInicial.getTime());
//		   formData.append('data_final', $scope.dataFinal.getTime());
//		   formData.append('data_hoje', $scope.objetoDataCambioSelecionada.getTime());
//
//		   if($scope.agruparIndividualMedia) {
//			   formData.append('tipo_agrupamento', 2);
//		   } else {
//			   formData.append('tipo_agrupamento', tipoGraficoTonelada ? 0 : 1);
//		   }
//
//		   formData.append('cotacao_selected_list', $scope.cotacaoSelectedList);
//
//			$http.post('/graficoController/imprimirRelatorioCompleto', formData,
//			   {transformRequest: angular.identity, headers: {'Content-Type': undefined},
//				responseType: 'arraybuffer'}).success(function(data) {
//
//				   var a = document.createElement("a");
//				   document.body.appendChild(a);
//				   a.style = "display: none";
//				   var file = new Blob([data], {type: 'application/pdf'}); // data.contents
//				   var fileURL = URL.createObjectURL(file);
//				   a.target = "_blank";
//				   a.href = fileURL;
//				   a.download = "grafico_completo.pdf";
//				   a.click();
//
//				$('#divPaginaGrafico').unblock({});
//			});
//	};

//	$scope.imprimirRelatorioSimples = function() {
//
//		var cotacaoSelectedList = [];
//
//		angular.forEach($scope.cotacaoNacionalList, function(cotacao, key) {
//
//			if(cotacao.selectedRelatorio) {
//				cotacaoSelectedList.push(cotacao.id);
//			}
//		});
//
//		angular.forEach($scope.cotacaoInternacionalList, function(cotacao, key) {
//
//			if(cotacao.selectedRelatorio) {
//				cotacaoSelectedList.push(cotacao.id);
//			}
//		});
//
//		if(cotacaoSelectedList.length != 0) {
//
//		   var formData = new FormData();
//		   formData.append('data_hoje', $scope.objetoDataCambioSelecionada.getTime());
////		   formData.append('data_inicial', $scope.dataInicial.getTime());
////		   formData.append('data_final', $scope.dataFinal.getTime());
//
//		   formData.append('cotacao_selected_list', cotacaoSelectedList);
//
//			$('#divPaginaGrafico').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//			$http.post('/graficoController/imprimirRelatorioSimples', formData,
//			   {transformRequest: angular.identity, headers: {'Content-Type': undefined},
//				responseType: 'arraybuffer'}).success(function(data) {
//
//				   var a = document.createElement("a");
//				   document.body.appendChild(a);
//				   a.style = "display: none";
//				   var file = new Blob([data], {type: 'application/pdf'}); // data.contents
//				   var fileURL = URL.createObjectURL(file);
//				   a.target = "_blank";
//				   a.href = fileURL;
//				   a.download = "grafico_simples.pdf";
//				   a.click();
//
//				$('#divPaginaGrafico').unblock({});
//			});
//		} else {
//        	$scope.alertaSistemaTemplate.message = 'Selecione ao menos uma cotação';
//        	$scope.alertaSistemaTemplate.type = 'warning';
//        	$scope.alertaSistemaTemplate.show = true;
//        	$scope.alertaSistemaTemplate.icon = 'warning-sign';
//			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
//		}
//	};

	function formataData(s) { return (s < 10) ? '0' + s : s; }

	function graficoUtilInlineAllStyles() {

	    var styles, chartStyle, selector;
	    // Get rules from c3.css
	    for (var i = 0; i <= document.styleSheets.length - 1; i++) {
	        if (document.styleSheets[i].href && document.styleSheets[i].href.indexOf('c3.min.css') !== -1) {
	            if (document.styleSheets[i].rules !== undefined) {
	                chartStyle = document.styleSheets[i].rules;
	            } else {
	                chartStyle = document.styleSheets[i].cssRules;
	            }
	        }

	    }
	    if (chartStyle !== null && chartStyle !== undefined) {
	        // SVG doesn't use CSS visibility and opacity is an attribute, not a style property. Change hidden stuff to "display: none"
	        var changeToDisplay = function() {
	            if ($(this).css('visibility') === 'hidden' || $(this).css('opacity') === '0') {
	                $(this).css('display', 'none');
	            }
	        };
	        // Inline apply all the CSS rules as inline
	        for (i = 0; i < chartStyle.length; i++) {

	            if (chartStyle[i].type === 1) {
	                selector = chartStyle[i].selectorText;
	                styles = makeStyleObject(chartStyle[i]);
	                $('svg *').each(changeToDisplay);
	                // $(selector).hide();
	                $(selector).not($('.c3-chart path')).css(styles);
	            }
	            $('.c3-chart path')
	                .filter(function() {
	                    return $(this).css('fill') === 'none';
	                })
	                .attr('fill', 'none');

	            $('.c3-chart path')
	                .filter(function() {
	                    return !$(this).css('fill') === 'none';
	                })
	                .attr('fill', function() {
	                    return $(this).css('fill');
	                });
	        }
	    }

	    return styles;
	};
	// Create an object containing all the CSS styles.
	// TODO move into inlineAllStyles
	var makeStyleObject = function(rule) {
	    var styleDec = rule.style;
	    var output = {};
	    var s;
	    for (s = 0; s < styleDec.length; s++) {
	        output[styleDec[s]] = styleDec[styleDec[s]];
	    }
	    return output;
	};

});

var modalControlerDetalhesCotacao = function ($scope, $modalInstance, $http, $timeout, $sce, cotacaoParametro) {

	$scope.cotacao = cotacaoParametro;

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};

var modalControlerGraficoCotacao = function ($scope, $modalInstance, $http, $timeout, $sce, cotacaoParametro) {

	$scope.cotacao = cotacaoParametro;

	$http.get('/graficoController/montarGraficoCotacao/' + cotacaoParametro.id).success(function(listResponse) {

		try {
			graficoCotacaoModal.destroy();
		} catch (exception) {

		}

		graficoCotacaoModal = c3.generate({
//			bindto: '#graficoValorMercadoria',
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

		$("#graficoCotacaoModal").append(graficoCotacaoModal.element);
		graficoCotacaoModal.categories(listResponse.categories);
		graficoCotacaoModal.load(listResponse);

	});

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};
