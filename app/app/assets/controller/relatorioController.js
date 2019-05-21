var moduleRelatorio = angular.module('moduleRelatorio', []);

//moduleRelatorio.directive('onFinishRepeatCotacao', function ($timeout) {
//	return {
//		restrict: 'A',
//		link: function (scope, element, attr) {
//			
//			console.log(scope.divGrafico);
//			console.log(attr);
//			console.log('------------------');
//			console.log(element);
//			if (scope.$last === true) {
//				
////				console.log('ultimo');
////				$timeout(function () {
////					scope.$emit('ngRepeatFinished');
////				});
//			}
//		}
//	}
//});

moduleRelatorio.controller('controllerRelatorio', function($scope, $http, $timeout, $modal) {
	
	$scope.dataCambioIsOpen = false;
	$scope.cambio = {};
	$scope.valorMercadoriaCotacaoList = [];
	$scope.objetoDataCambioSelecionada = moment().toDate();
	$scope.valorMercadoria = 0;
	$scope.cotacaoList = [];

	$scope.divGrafico = [];
	var formData = new FormData();
	
	$scope.openDataCambio = function() {
		$scope.dataCambioIsOpen = true;
	};
	
	changeDataCambio();
	function changeDataCambio() {

		$('#divRelatorioImportacao').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		
		$http.get('/relatorioController/findCambioByData/' + $scope.objetoDataCambioSelecionada.getTime()).success(function(cambioResponse) {
			$scope.cambio = cambioResponse;
		});
		
		$http.get('/relatorioController/findRelValorMercadoriaByData/' + $scope.objetoDataCambioSelecionada.getTime()).success(function(response) {
			$scope.valorMercadoriaCotacaoList = response;
			
			if($scope.valorMercadoriaCotacaoList != null && $scope.valorMercadoriaCotacaoList.length > 0) {
				
				angular.forEach($scope.valorMercadoriaCotacaoList, function(valorMercadoriaCotacao, key) {
					formData.append('nome_' + valorMercadoriaCotacao.cotacaoId, valorMercadoriaCotacao.cotacaoNome);
					formData.append('valor_mercadoria_real_' + valorMercadoriaCotacao.cotacaoId, valorMercadoriaCotacao.valorMercadoriaReal);
					formData.append('valor_mercadoria_dolar_' + valorMercadoriaCotacao.cotacaoId, valorMercadoriaCotacao.valorMercadoriaDolar);
					formData.append('valor_mercadoria_euro_' + valorMercadoriaCotacao.cotacaoId, valorMercadoriaCotacao.valorMercadoria);
				});
			};
		});	
		
		$http.get('/relatorioController/simularRelatorio/' + $scope.objetoDataCambioSelecionada.getTime()).success(function(listResponse) {
			$scope.cotacaoList = listResponse;
			
			if($scope.cotacaoList != null && $scope.cotacaoList.length > 0) {
				
				angular.forEach($scope.cotacaoList, function(cotacao, key) {

					var colunasGrafico = {};
					colunasGrafico.columns = [];
					
					angular.forEach(cotacao.categoriaList, function(categoria, key2) {

						angular.forEach(categoria.itemList, function(item, key3) {
							
							if(item.isCategoriaTotal == 1) {
								var coluna = [];
								coluna.push(item.nome);
								coluna.push(item.valor);
								if(item.id == 3) {
									formData.append('valor_cif_real_' + cotacao.id, item.valor);
									formData.append('valor_cif_dolar_' + cotacao.id, item.valorDolar);
									formData.append('valor_cif_euro_' + cotacao.id, item.valorEuro);
								}
								if(item.id == 9) {
									formData.append('valor_tributacoes_real_' + cotacao.id, item.valor);
									formData.append('valor_tributacoes_dolar_' + cotacao.id, item.valorDolar);
									formData.append('valor_tributacoes_euro_' + cotacao.id, item.valorEuro);
								}
								if(item.id == 33) {
									formData.append('valor_despesas_aduaneiras_real_' + cotacao.id, item.valor);
									formData.append('valor_despesas_aduaneiras_dolar_' + cotacao.id, item.valorDolar);
									formData.append('valor_despesas_aduaneiras_euro_' + cotacao.id, item.valorEuro);
								}
								colunasGrafico.columns.push(coluna);
							}
						});
					});
					
					var divGraficoObj = {};
					divGraficoObj.cotacaoId = cotacao.id;
					divGraficoObj.colunasGrafico = colunasGrafico;
					$scope.divGrafico.push(divGraficoObj);
//					console.log(divGraficoObj);
					//var iDiv = document.getElementById('divGrafico_' + cotacao.id);
					var iDiv = document.createElement('div');
					iDiv.id = 'divGrafico_' + cotacao.id;
					
					try {
						document.getElementById('divGraficosTotalCategoria').appendChild(iDiv);
					} catch (exception) {
				
					}

//					try {
//						iDiv.destroy();
//					} catch (exception) {
//				
//					}
					iDiv = c3.generate({
						bindto: '#divGrafico_' + cotacao.id,
						data: {
							columns: [],
							type : 'pie'
						}
					});
					
					iDiv.load(colunasGrafico);
				});
			}
		});

		$http.get('/relatorioController/graficoComparativoValorTotalImportacao/' + $scope.objetoDataCambioSelecionada.getTime()).success(function(listResponse) {
			
			try {
				graficoValorMercadoria.destroy();
			} catch (exception) {
		
			}
			
			graficoValorMercadoria = c3.generate({
				bindto: '#graficoValorMercadoria',
				data: {
					columns: [],
					type : 'line'
	//				types : {'mediaEuro': 'line', 'mediaDolar' : 'line'}
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
	//			},
	//			grid: {
	//				y : {
	//					lines: [{value: 1, text: 'Real'}]
	//				}
				}
			});
	
			graficoValorMercadoria.categories(listResponse.categories);
			graficoValorMercadoria.load(listResponse);
			
			$('#divRelatorioImportacao').unblock({});
		});
	}
	
	$scope.changeDataCambio = function() {
		changeDataCambio();
	}
	
//	$scope.montar = function() {
//		
////		$scope.$emit('LastRepeaterElement');
////		console.log('montar');
//		for(i = 0; i < $scope.divGrafico.length; i++) {
//
//			var iDiv = document.getElementById('divGrafico_' + divGrafico[i].cotacaoId);
//			 try {
//			 	iDiv.destroy();
//			 } catch (exception) {
//		
//			 }
//			iDiv = c3.generate({
//				bindto: '#divGrafico_' + divGrafico[i].cotacaoId,
//				data: {
//					columns: [],
//					type : 'pie'
//				}
//			});
//			
//			iDiv.load(divGrafico[i].colunasGrafico);
//		}
//	};
	
//	$scope.$on('LastRepeaterElement', function(){
//		console.log('LastRepeaterElement')
//		for(i = 0; i < divGrafico.length; i++) {
//		
//			var iDiv = document.getElementById('divGrafico_' + divGrafico[i].cotacaoId);
//			console.log(iDiv);
//			 try {
//			 	iDiv.destroy();
//			 } catch (exception) {
//		
//			 }
//			iDiv = c3.generate({
//				bindto: '#divGrafico_' + divGrafico[i].cotacaoId,
//				data: {
//					columns: [],
//					type : 'pie'
//				}
//			});
//			
//			iDiv.load(divGrafico[i].colunasGrafico);
//		}
//	});
	
	$scope.gerarPDF = function() {

		$('#divRelatorioImportacao').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		
	 	$('defs').remove();
        // Copy CSS styles to Canvas
        var styles = graficoUtilInlineAllStyles();
//		var formData = new FormData();
		
		angular.forEach($scope.cotacaoList, function(cotacao, key) {

	       var canvasCambio = document.createElement('canvas');
	       canvasCambio.width = $('#divGrafico_' + cotacao.id).width() * 1; // qnto maior, menor o grafico no pdf
	       canvasCambio.height = $('#divGrafico_' + cotacao.id).height() * 1; // qnto maior, menor o grafico no pdf

	       var canvasContextCambio = canvasCambio.getContext('2d');
	       var svgCambio = $.trim($('#divGrafico_' + cotacao.id + ' > svg')[0].outerHTML); /// $.trim($('#graficoCambio > svg')[0].outerHTML);
	       canvasContextCambio.drawSvg(svgCambio, 0, 0);
	       var byteArrCambio = canvasCambio.toDataURL("png");
	       
	       formData.append('imagem_grafico_' + cotacao.id, byteArrCambio);
		});
		
		var canvasTotalImportacao = document.createElement('canvas');
		canvasTotalImportacao.width = $('#graficoValorMercadoria').width() * 1; // qnto maior, menor o grafico no pdf
		canvasTotalImportacao.height = $('#graficoValorMercadoria').height() * 1; // qnto maior, menor o grafico no pdf
		var canvasContextTotalImportacao = canvasTotalImportacao.getContext('2d');
		var svgTotalImportacao = $.trim($('#graficoValorMercadoria > svg')[0].outerHTML); /// $.trim($('#graficoCambio > svg')[0].outerHTML);
		canvasContextTotalImportacao.drawSvg(svgTotalImportacao, 0, 0);
		var byteArrTotalImportacao = canvasTotalImportacao.toDataURL("png");
		formData.append('grafico_valor_total_importacao', byteArrTotalImportacao);
		
       formData.append('data', $scope.objetoDataCambioSelecionada.getTime());
       
		$http.post('/relatorioController/gerarPDF', formData,
	    		   {transformRequest: angular.identity, headers: {'Content-Type': undefined},
   			responseType: 'arraybuffer'}).success(function(data) {
			
			   var a = document.createElement("a");
			   document.body.appendChild(a);
			   a.style = "display: none";
			   var file = new Blob([data], {type: 'application/pdf'}); // data.contents
			   var fileURL = URL.createObjectURL(file);
			   a.target = "_blank";
			   a.href = fileURL;
			   a.download = "graficos.pdf";
			   a.click();
			   $('#divRelatorioImportacao').unblock({});
		});
	};
	
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