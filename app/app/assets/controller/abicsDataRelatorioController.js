var moduleAbicsDataRelatorio = angular.module('moduleAbicsDataRelatorio', []);

moduleAbicsDataRelatorio.directive('directiveModalValidacaoRelatorioMundo', function() {
	return {
		templateUrl : '/assets/directives/directiveModalValidacaoRelatorioMundo.html'
	};
});

moduleAbicsDataRelatorio.controller('controllerAbicsDataRelatorio', function($scope, $http, $timeout, $modal, $location) {
	
	$scope.voltarTemplate = function() {
		$location.path('templateAbicsData');
	};
	
	$scope.informacoes = 'Produção, Importação e Exportações acumulado dos países para o mundo. Fontes USDA e Comtrade, com dados anuais.'+
	'Os dados são referentes a quanto o um determinado país produziu, exportou ou importou, de forma acumulada para o mundo.'+
	'\nPara consultar escolha qual tipo de informação deseja, produção, exportação ou importação, e o tipo de café.'+
	'Escolha quais países, ou todos países de determinado continente, ou todos países de determinado bloco econômico.'+
	'Qual a fonte desejada e o período desejado.';
	$scope.caminhoVideo = 'assets/videos/abicsDataRelatorio.mp4';

	$scope.anoList = [];
	
	var paisIdListAntigo = '';
	var continenteIdListAntigo = '';
	var blocoEconomicoIdListAntigo = '';
	var anoInicialAntigo = '';
	var anoFinalAntigo = '';
	var tiposCafeAntigo = '';
	
	var anoInicialInt = 2000;
	var anoFinalInt = new Date().getFullYear();
	var anoLength = anoFinalInt - anoInicialInt;
		
	$scope.anoInicial = anoInicialInt.toString();
	$scope.anoFinal = anoFinalInt.toString();
	
	for(i=0; i<=anoLength; i++) {
		$scope.anoList.push(anoInicialInt.toString());
		anoInicialInt++;
	}
	
	$scope.tabelaList = [];
	$scope.paisList = [];
	$scope.continenteList = [];
	$scope.blocoEconomicoList = [];
	
	$scope.paisSelecionadoList = [];
	$scope.continenteSelecionadoList = [];
	$scope.blocoEconomicoSelecionadoList = [];

	$scope.fonteSelecionado = 'comtrade';
	$scope.fonte = 'comtrade';
	
	$scope.exportacaoGrao = false;
	$scope.importacaoGrao = false;
	$scope.producaoArabica = false;
	$scope.exportacaoSoluvel = false;
	$scope.importacaoSoluvel = false;
	$scope.producaoRobusta = false;
	
	$scope.modelRadioTipo = 'exportação';
	$scope.modelTipo = 'exportação';
	
	$scope.tipoDadosGrafico = '';
	
	$scope.agregado = false;
	
	$scope.agregadoCafe = false;
	$scope.agregadoPais = false;
	
	function orderByNome(a,b) {
	  if (a.nome < b.nome)
	    return -1;
	  if (a.nome > b.nome)
	    return 1;
	  return 0;
	}
	
	$http.get('/abicsDataRelatorioController/findAllPais').success(function(response) {
		$scope.paisList = response;
		$scope.paisList.sort(orderByNome);
	});
	
	$http.get('/abicsDataRelatorioController/findAllContinente').success(function(response) {
		$scope.continenteList = response;
		$scope.continenteList.sort(orderByNome);
	});
	
	$http.get('/abicsDataRelatorioController/findAllBlocoEconomico').success(function(response) {
		$scope.blocoEconomicoList = response;
		$scope.blocoEconomicoList.sort(orderByNome);
	});
	
	$scope.imprimirRelatorio = function() {

		$('#abicsDataRelatorio').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		
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
		
		if($scope.exportacaoGrao || $scope.importacaoGrao) {
			tiposCafe.push('grao');
		}
		if($scope.exportacaoSoluvel || $scope.importacaoSoluvel) {
			tiposCafe.push('soluvel');
		} 
		if($scope.producaoArabica) {
			tiposCafe.push('arabica');
		}
		if($scope.producaoRobusta) {
			tiposCafe.push('robusta');
		}
		
		var objConsulta = new FormData();
		objConsulta.append('fonte', $scope.fonte);
		objConsulta.append('paisList', paisIdListAntigo);
		objConsulta.append('continenteList', continenteIdListAntigo);
		objConsulta.append('blocoEconomicoList', blocoEconomicoIdListAntigo);
		objConsulta.append('tipo', $scope.modelTipo);
		objConsulta.append('anoInicial', anoInicialAntigo);
		objConsulta.append('anoFinal', anoFinalAntigo);
		
		objConsulta.append('tipoCafe', tiposCafeAntigo);
		
		if($scope.agregado) {
			var agregadoRequest = 'Agregado';
			
			if($scope.fonte == 'usda') {
				if($scope.agregadoCafe) {
					objConsulta.append('agregadoUSDA', 'cafe');
					
				} else if($scope.agregadoPais){
					objConsulta.append('agregadoUSDA', 'pais');
				}
				
			} else {
				objConsulta.append('agregadoUSDA', '');
			}
			
		} else {
			var agregadoRequest = '';
		}
        
		$http.post('/abicsDataRelatorioController/imprimirRelatorio' + agregadoRequest, objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}, responseType: 'arraybuffer'}).success(function(data) {

			   var a = document.createElement("a");
			   document.body.appendChild(a);
			   a.style = "display: none";
			   var file = new Blob([data], {type: 'application/vnd.ms-excel'}); // data.contents
			   var fileURL = URL.createObjectURL(file);
			   a.target = "_blank";
			   a.href = fileURL;
			   a.download = "relatorio_anual_" + $scope.modelTipo + "_" + $scope.fonte + ".xls";
			   a.click();
			   $('#abicsDataRelatorio').unblock();
		});
	};
	
	function montarGraficosComtrade(tipoAgregado, objConsulta) {
		var agregado = '';
		
		if(tipoAgregado == true) {
			agregado = 'Agregado';
			$scope.agregado = true;
		} else {
			$scope.agregado = false;
		}

		$('#abicsDataRelatorio').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataRelatorioController/consultarGraficoComtrade' + agregado + '/NetWeight', objConsulta, 
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
			$('#abicsDataRelatorio').unblock({});
		});
		
		
		$('#abicsDataRelatorio').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataRelatorioController/consultarGraficoComtrade' + agregado + '/tradeValue', objConsulta, 
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
			$('#abicsDataRelatorio').unblock({});
			
			$scope.fonte = 'comtrade';
		});
		
	}
	
	function montarGraficosUSDA(tipoAgregado, objConsulta, agregadoUSDA) {
		var stringRequest = '';
		
		if(tipoAgregado == true) {
			stringRequest = '/abicsDataRelatorioController/consultarGraficoUSDAAgregado/' + agregadoUSDA;
			$scope.agregado = true;
			if(agregadoUSDA == 'pais') {
				$scope.agregadoCafe = false;
				$scope.agregadoPais = true;
				
			} else if(agregadoUSDA == 'cafe') {
				$scope.agregadoPais = false;
				$scope.agregadoCafe = true;
				
			} else { 
				$scope.agregadoPais = false;
				$scope.agregadoCafe = false;
			}
		} else {
			stringRequest = '/abicsDataRelatorioController/consultarGraficoUSDA';
			$scope.agregado = false;
			$scope.agregadoPais = false;
			$scope.agregadoCafe = false;
		}
		
		$('#abicsDataRelatorio').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post(stringRequest, objConsulta, 
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(listResponse, status, headers) {

			$scope.graficoUSDA = listResponse;
			
			try {
				graficoGeral.destroy();
			} catch (exception) {

			}

			graficoGeral = c3.generate({
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

			$("#graficoGeral").append(graficoGeral.element);
			graficoGeral.categories(listResponse.categories);
			graficoGeral.load(listResponse);
			$('#abicsDataRelatorio').unblock({});
			
			$scope.fonte = 'usda';
		});
	}
	
	function montarTabela(objConsulta) {
		
		$('#abicsDataRelatorio').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataRelatorioController/consultarTabela', objConsulta, 
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(response, status, headers) {
					
			$scope.tabelaList = response;
			
			$scope.listaPaisSeparado = [];
			$scope.totalGeralNetWeight = 0;
			$scope.totalGeralTradeValue = 0;
			$scope.totalGeralQuantidade = 0;
			var pais = '';
			var posicao = -1;
			
			angular.forEach($scope.tabelaList, function(value, key) {
				
				if($scope.fonteSelecionado == 'usda') {
					if(value.pais.toLowerCase() == pais.toLowerCase()) {
						$scope.listaPaisSeparado[posicao].somaQuantidade += parseInt(value.quantidade);
						
						$scope.paisListaObjSeparado.push(value);
						
					} else {
						pais = value.pais;
						posicao++;
						
						$scope.paisListaObjSeparado = [];
						
						$scope.listaPaisSeparado.push($scope.paisListaObjSeparado);
						$scope.paisListaObjSeparado.push(value);
						
						$scope.listaPaisSeparado[posicao].pais = pais;
						$scope.listaPaisSeparado[posicao].somaQuantidade = parseInt(value.quantidade);
					}
					
					$scope.totalGeralQuantidade += parseInt(value.quantidade);
					
				} else if($scope.fonteSelecionado == 'comtrade'){
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
					
					$scope.totalGeralNetWeight += parseInt(value.netWeight);
					$scope.totalGeralTradeValue += parseInt(value.tradeValue);
				}
				
				$('#abicsDataRelatorio').unblock();
			});
			
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
					+     '<th class="text-center">Ano</th>';
				
				if($scope.fonteSelecionado == 'usda') {
					tabelaString +=   '<th class="text-center">Quantidade</th>';
					
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
					+	'<td class="text-center">' + value2.ano + '</td>';
				
					if($scope.fonteSelecionado == 'usda') {
						tabelaString += '<td class="text-center">' + value2.quantidade + '</td>';
					
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
					
				if($scope.fonteSelecionado == 'usda') {
					tabelaString += '<td class="text-center"><b>' + value1.somaQuantidade + '</b></td>';
					
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
		
		$('#abicsDataRelatorio').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/abicsDataRelatorioController/consultarTabelaAgregada', objConsulta, 
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function(response, status, headers) {
					
			$scope.tabelaList = response;
			
			$scope.listaPaisSeparado = [];
			$scope.totalGeralQuantidade = 0;
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
			+     '<th class="text-center">Ano</th>';
		
			if($scope.fonteSelecionado == 'usda') {
				tabelaString +=   '<th class="text-center">Quantidade</th>';
				
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
				+	'<td class="text-center">' + value1.ano + '</td>';
			
				if($scope.fonteSelecionado == 'usda') {
					tabelaString += '<td class="text-center">' + value1.quantidade + '</td>';
					
					$scope.totalGeralQuantidade += parseInt(value1.quantidade);
				
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
			
			$('#abicsDataRelatorio').unblock();
		});
	}
	
	$scope.limparConsulta = function() {
		
		$scope.paisSelecionadoList = [];
		$scope.blocoEconomicoSelecionadoList = [];
		$scope.continenteSelecionadoList = [];
		
		$scope.fonteSelecionado = 'comtrade';
		$scope.fonte = 'comtrade';
		
		$scope.anoInicial = '2000';
		$scope.anoFinal = anoFinalInt.toString();
		
		$scope.modelRadioTipo = 'exportação';
		$scope.modelTipo = 'exportação';
		
		$scope.exportacaoGrao = false;
		$scope.exportacaoSoluvel = false;
		$scope.importacaoGrao = false;
		$scope.producaoArabica = false;
		$scope.importacaoSoluvel = false;
		$scope.producaoRobusta = false;
		
		$scope.tipoDadosGrafico = '';
		$scope.tabelaList = [];
		$scope.listaPaisSeparado = [];
		$scope.totalGeralQuantidade = 0;
		$scope.totalGeralNetWeight = 0;
		$scope.totalGeralTradeValue = 0;
		document.getElementById("tabelaFontes").innerHTML = "";
		$scope.divSomaTotal = false;
		
	};
	
	$scope.changeRadio = function(tipo) {
		if(tipo == 'exportacao') {
			$scope.importacaoGrao = false;
			$scope.producaoArabica = false;
			$scope.importacaoSoluvel = false;
			$scope.producaoRobusta = false;
			$scope.modelRadioTipo = 'exportação';
			
		} else if(tipo == 'importacao') {
			$scope.exportacaoGrao = false;
			$scope.producaoArabica = false;
			$scope.exportacaoSoluvel = false;
			$scope.producaoRobusta = false;
			$scope.modelRadioTipo = 'importação';
			
		} else if(tipo == 'producao') {
			$scope.exportacaoGrao = false;
			$scope.importacaoGrao = false;
			$scope.exportacaoSoluvel = false;
			$scope.importacaoSoluvel = false;
			$scope.modelRadioTipo = 'produção';
		}
	}

	$scope.changeFontes = function() {
		
		$scope.exportacaoGrao = false;
		$scope.importacaoGrao = false;
		$scope.exportacaoSoluvel = false;
		$scope.importacaoSoluvel = false;
		$scope.producaoRobusta = false;
		$scope.producaoArabica = false;
		
		$scope.modelRadioTipo = 'exportação';
		$scope.modelRadioTipo = 'exportação';
	};
	
	$scope.consultar = function(tipoAgregado, agregadoUSDA) {
		
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
		
		if($scope.exportacaoGrao || $scope.importacaoGrao) {
			tiposCafe.push('grao');
		}
		if($scope.exportacaoSoluvel || $scope.importacaoSoluvel) {
			tiposCafe.push('soluvel');
		} 
		if($scope.producaoArabica) {
			tiposCafe.push('arabica');
		}
		if($scope.producaoRobusta) {
			tiposCafe.push('robusta');
		}
		
		var mensagemValidacao = '';
		/**Validacoes**/
		if(tiposCafe.length == 0) {
			mensagemValidacao += 'Selecione ao menos um tipo de café: Arábica, Robusta, Café Verde ou Solúvel';
			mensagemValidacao += ';;';
		}

		if(paisIdList.length == 0 && continenteIdList.length == 0 && blocoEconomicoIdList.length == 0) {
			mensagemValidacao += 'Selecione ao menos um pais ou continente ou bloco economico';
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
		
		if(mensagemValidacao != '') {
			abrirModalValidacaoRelatorioMundo(mensagemValidacao);
			return;
		}
		
		var objConsulta = new FormData();
		objConsulta.append('fonte', $scope.fonteSelecionado);
		objConsulta.append('paisList', paisIdList);
		objConsulta.append('continenteList', continenteIdList);
		objConsulta.append('blocoEconomicoList', blocoEconomicoIdList);
		objConsulta.append('tipo', $scope.modelRadioTipo);
		objConsulta.append('anoInicial', $scope.anoInicial);
		objConsulta.append('anoFinal', $scope.anoFinal);
		
		objConsulta.append('tipoCafe', tiposCafe);
		
		paisIdListAntigo = paisIdList;
		continenteIdListAntigo = continenteIdList;
		blocoEconomicoIdListAntigo = blocoEconomicoIdList;
		anoInicialAntigo = $scope.anoInicial;
		anoFinalAntigo = $scope.anoFinal;
		tiposCafeAntigo = tiposCafe;
		
		if($scope.fonteSelecionado == 'comtrade') {
			montarGraficosComtrade(tipoAgregado, objConsulta);
			$scope.modificarDadosGrafico('netweight');
		}
		
		if($scope.fonteSelecionado == 'usda') {
			montarGraficosUSDA(tipoAgregado, objConsulta, agregadoUSDA);
		}
		
		if(tipoAgregado == false) {
			montarTabela(objConsulta);
			$scope.modelTipo = $scope.modelRadioTipo;
		} else {
			if($scope.fonteSelecionado == 'usda') {
				objConsulta.append('agregadoUSDA', agregadoUSDA);
			} else {
				objConsulta.append('agregadoUSDA', '');
			}
			
			montarTabelaAgregada(objConsulta);
		}
	};
	
	$scope.modificarDadosGrafico = function(tipoDados) {
		$scope.tipoDadosGrafico = tipoDados;
		changeButtonStyle(tipoDados)
	};
	
	function changeButtonStyle(tipoGrafico) {
		document.getElementById('buttonTipoGraficoNetWeight').className = '';
		document.getElementById('buttonTipoGraficoTradeValue').className = '';

		if(tipoGrafico == 'netweight') {
			document.getElementById('buttonTipoGraficoNetWeight').className = 'btn btn-primary btn-xs pull-right';
			document.getElementById('buttonTipoGraficoTradeValue').className = 'btn btn-info btn-xs pull-right';
			
		} else if(tipoGrafico == 'tradevalue') {
			document.getElementById('buttonTipoGraficoNetWeight').className = 'btn btn-info btn-xs pull-right';
			document.getElementById('buttonTipoGraficoTradeValue').className = 'btn btn-primary btn-xs pull-right';
		}
	};
	
	function abrirModalValidacaoRelatorioMundo(mensagem) {
		
		var modalInstance = $modal.open({
            templateUrl: 'idModalValidacaoRelatorioMundo',
            controller: modalControlerValidacaoRelatorioMundo,
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
	
});


var modalControlerValidacaoRelatorioMundo = function ($scope, $modalInstance, $http, $timeout, $sce, mensagemParametro) {

	$scope.mensagem = mensagemParametro;
	$scope.mensagemArray = mensagemParametro.split(';;'); 
	
	$scope.mensagemArray = $scope.mensagemArray.slice(0, $scope.mensagemArray.length - 1);

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};
