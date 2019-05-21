var moduleHistorico = angular.module('moduleHistorico', []);

moduleDashboard.controller('controllerHistorico', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Valores histórico das cotações. '+
		'Escolha qual o período deseja visualizar as informações ' +
		'Aplique ou não o valor de logística' +
		'Escolha a moeda R$ ou US$.'+
		'Visualize os dados em formato EXCEL e o gráfico em formato PDF';
	$scope.caminhoVideo = 'assets/videos/history.mp4';

	$scope.dataInicial = moment().add(-1, 'months').toDate();
	$scope.dataFinal = moment().add(-1, 'days').toDate();
	$scope.isAplicarLogistica = false;
	$scope.dataInicialIsOpen = false;
	$scope.dataFinalIsOpen = false;
	$scope.tipoMoeda = 'real'; 
	$scope.isGraficoHabilitado = false;
		
	$scope.openDataInicial = function() {
		$scope.dataInicialIsOpen = true;
	};

	$scope.openDataFinal = function() {
		$scope.dataFinalIsOpen = true;
	};

	$scope.aplicarLogistica = function() {
		$scope.isAplicarLogistica = !$scope.isAplicarLogistica;
		$scope.gerarRelatorioCompleto();
	};
	
	$scope.modificarMoedaHistorico = function(tipoMoeda) {
		$scope.tipoMoeda = tipoMoeda;
		changeButtonStyle(tipoMoeda) 
		$scope.gerarRelatorioCompleto();
	};
	
	function changeButtonStyle(tipoMoeda) {

		document.getElementById('buttonHistoricoDolar').className = '';
		document.getElementById('buttonHistoricoReal').className = '';

		if(tipoMoeda == 'dolar') {
			document.getElementById('buttonHistoricoDolar').className = 'btn btn-primary btn-sm';
			document.getElementById('buttonHistoricoReal').className = 'btn btn-info btn-sm';
		} else if(tipoMoeda == 'real') {
			document.getElementById('buttonHistoricoDolar').className = 'btn btn-info btn-sm';
			document.getElementById('buttonHistoricoReal').className = 'btn btn-primary btn-sm';
		}
	};


//	$scope.modificarTipoGrafico = function() {
//
//		if(tipoGraficoTonelada) {
//            $("#fa-grafico").addClass('rotate180');
//            tipoGraficoTonelada = false;
//            $scope.saca60kgTonelada = 'Saca 60kg';
//        } else {
//            $("#fa-grafico").removeClass('rotate180');
//            tipoGraficoTonelada = true;
//            $scope.saca60kgTonelada = 'Tonelada';
//        }
//	};

	$scope.gerarRelatorioCompleto = function() {

		$scope.isGraficoHabilitado = true;
		
		$scope.cotacaoSelectedList = [];

		$http.get('/dashboardController/findAllCotacao').success(function(listResponse) {
			angular.forEach(listResponse, function(cotacao, key) {
				$scope.cotacaoSelectedList.push(cotacao.id);
			});

			if($scope.cotacaoSelectedList.length != 0) {

//		       var formData = new FormData();
//		       formData.append('data_inicial', $scope.dataInicial.getTime());
//		       formData.append('data_final', $scope.dataFinal.getTime());
//		       formData.append('aplicar_logistica', $scope.isAplicarLogistica);
//		       formData.append('tipo_moeda', $scope.tipoMoeda);

		       var formData = {};
		       formData.data_inicial = $scope.dataInicial.getTime();
		       formData.data_final = $scope.dataFinal.getTime();
		       formData.aplicar_logistica = $scope.isAplicarLogistica;
		       formData.tipo_moeda = $scope.tipoMoeda;
				
				$('#divRelatorioSelecionado').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
//				$http.post('/historicoController/relatorioCompleto', formData, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse) {
				$http.post('/historicoController/relatorioCompleto', formData).success(function(listResponse, status, headers, response) {
					try {
						graficoHistoricoCotacoes.destroy();
					} catch (exception) {

					}

					graficoHistoricoCotacoes = c3.generate({
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

					$("#graficoHistoricoCotacoes").append(graficoHistoricoCotacoes.element);
					graficoHistoricoCotacoes.categories(listResponse.categories);
					graficoHistoricoCotacoes.load(listResponse);

					$('#divRelatorioSelecionado').unblock({});
				});
			}
		});
	};

	$scope.imprimirRelatorio = function() {

		$('#divHistorico').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		
		var formData = {};
	    formData.data_inicial = $scope.dataInicial.getTime();
	    formData.data_final = $scope.dataFinal.getTime();
        formData.aplicar_logistica = $scope.isAplicarLogistica;
        formData.tipo_moeda = $scope.tipoMoeda;
        
		$http.post('/historicoController/imprimirRelatorio', formData,
				 {responseType: 'arraybuffer'}).success(function(data) {

			   var a = document.createElement("a");
			   document.body.appendChild(a);
			   a.style = "display: none";
//			   var file = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}); // data.contents
			   var file = new Blob([data], {type: 'application/vnd.ms-excel'}); // data.contents
			   var fileURL = URL.createObjectURL(file);
			   a.target = "_blank";
			   a.href = fileURL;
			   a.download = "relatorio_comparativo.xls";
			   a.click();
			   $('#divHistorico').unblock();
		});
	};
	
	$scope.imprimirGrafico = function() {
		
	   $('#divHistorico').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
       $('defs').remove();
       // Copy CSS styles to Canvas
       var styles = graficoUtilInlineAllStyles();
       // Create PNG image
       var canvas = document.createElement('canvas');
       canvas.width = $('#graficoHistoricoCotacoes').width() * 1; // qnto maior, menor o grafico no pdf
       canvas.height = $('#graficoHistoricoCotacoes').height() * 1; // qnto maior, menor o grafico no pdf

       var canvasContext = canvas.getContext('2d');
       var svg = $.trim($('#graficoHistoricoCotacoes > div svg')[0].outerHTML);
       canvasContext.drawSvg(svg, 0, 0);

       var byteArr = canvas.toDataURL("png");
		
		var formData = new FormData();
		formData.append('grafico_byte_array', byteArr);
	    formData.append('data_inicial', $scope.dataInicial.getTime());
	    formData.append('data_final', $scope.dataFinal.getTime());
	    formData.append('aplicar_logistica', $scope.isAplicarLogistica);
	    formData.append('tipo_moeda', $scope.tipoMoeda);
	    
		$http.post('/historicoController/imprimirGrafico', formData,
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
			   $('#divHistorico').unblock();
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