var moduleTemplateRelatorioAbicsData = angular.module('moduleTemplateRelatorioAbicsData', []);

moduleTemplateRelatorioAbicsData.controller('controllerTemplateRelatorioAbicsData', function($scope, $http, $sce, $modal){

$scope.anoRelatorioGeralList = [];
	
	function backgroundBrowFontWhite(instance, td, row, col, prop, value, cellProperties) {
	    Handsontable.renderers.TextRenderer.apply(this, arguments);
	//	    td.style.fontWeight = 'bold';
		td.style.color = 'white';
		td.style.background = '#791500';
	}
	
	function backgroundBrowFontWhiteBold(instance, td, row, col, prop, value, cellProperties) {
		Handsontable.renderers.TextRenderer.apply(this, arguments);
		td.style.fontWeight = 'bold';
		td.style.color = 'white';
		td.style.background = '#791500';
	}
	
	function backgroundWhiteFontBrowBold(instance, td, row, col, prop, value, cellProperties) {
		Handsontable.renderers.TextRenderer.apply(this, arguments);
		td.style.fontWeight = 'bold';
		td.style.color = '#791500';
		td.style.background = '#white';
	}
	
	function backgroundBrowFontBrowBold(instance, td, row, col, prop, value, cellProperties) {
		Handsontable.renderers.TextRenderer.apply(this, arguments);
		td.style.fontWeight = 'bold';
		td.style.color = '#791500';
		td.style.background = '#ffe3dd';
	}
	
	function columnWidth(instance, td, row, col, prop, value, cellProperties) {
		Handsontable.renderers.TextRenderer.apply(this, arguments);
		//td.style.width = '0px';
		td.style['border-bottom'] = '0'; 
	}


	/**Filtro de Meses*/
	var anoInicialInt = 1995;
	var anoFinalInt = new Date().getFullYear();
	var anoLength = anoFinalInt - anoInicialInt;
	
	anoFinalInt = anoFinalInt - 1;
	$scope.anoRelatorioGeralInicial = anoFinalInt.toString();
	
	for(i=0; i<=anoLength; i++) {
		$scope.anoRelatorioGeralList.push(anoInicialInt.toString());
		anoInicialInt++;
	}
	
	$scope.anoRelatorioGeralList.reverse();
	$scope.mesRelatorioGeralList = [{"id" : 1, "nome" : "Janeiro"}, {"id" : 2, "nome" : "Fevereiro"}, {"id" : 3, "nome" : "Março"},
		       	                     {"id" : 4, "nome" : "Abril"}, {"id" : 5, "nome" : "Maio"}, {"id" : 6, "nome" : "Junho"}, 
		       	                     {"id" : 7, "nome" : "Julho"}, {"id" : 8, "nome" : "Agosto"}, {"id" : 9, "nome" : "Setembro"},
		       	                     {"id" : 10, "nome" : "Outubro"}, {"id" : 11, "nome" : "Novembro"}, {"id" : 12, "nome" : "Dezembro"}];
		
	$scope.mesRelatorioGeralInicial = {};
//	$scope.mesRelatorioGeralInicial = $scope.mesRelatorioGeralList[new Date().getMonth()];
	$scope.mesRelatorioGeralInicial = $scope.mesRelatorioGeralList[11];

	/**Filtro de Meses*/
	
	$scope.desempenhoMensal = function() {
		
		var primeiroAno = $scope.anoRelatorioGeralInicial;
		var segundoAno = primeiroAno - 1;
		var terceiroAno = primeiroAno - 2;
		
		try {
			document.getElementById('planilhaExcel').innerHTML = "";
		} catch(exception) {
			
		}
		
//		DESEMPENHO_MENSAL(0),
		$('#divRelatorioData').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.get('/templateRelatorioAbicsDataController/findData/0/' + $scope.anoRelatorioGeralInicial + '/' + 
				$scope.mesRelatorioGeralInicial.id).success(function(listResponse) {

			var example1 = document.getElementById('planilhaExcel');

			 listResponse.unshift({mesEmpresaPais: 'Empresa', pesoPrimeiroAno: primeiroAno, pesoSegundoAno: segundoAno, pesoTerceiroAno: terceiroAno, 
				 pesoPrimeiraVariacao: primeiroAno + 'x' + segundoAno, pesoSegundaVariacao: primeiroAno + 'x' + terceiroAno, 
				 sacaPrimeiroAno: primeiroAno, sacaSegundoAno: segundoAno, sacaTerceiroAno: terceiroAno, sacaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, 
				 sacaSegundaVariacao: primeiroAno + 'x' + terceiroAno, receitaPrimeiroAno: primeiroAno, receitaSegundoAno: segundoAno, 
				 receitaTerceiroAno: terceiroAno, receitaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, receitaSegundaVariacao: primeiroAno + 'x' + terceiroAno});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, pesoSegundoAno: '', pesoTerceiroAno: '', pesoPrimeiraVariacao: '',
				 pesoSegundaVariacao: '', sacaPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, sacaSegundoAno: '', sacaTerceiroAno: '', sacaPrimeiraVariacao: '',
				 sacaSegundaVariacao: '', receitaPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, receitaSegundoAno: '', receitaTerceiroAno: '', receitaPrimeiraVariacao: '',
				 receitaSegundaVariacao: ''});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: 'Peso Liquido (KG)', pesoSegundoAno: '', pesoTerceiroAno: '', 
				 pesoPrimeiraVariacao: 'Variação %', pesoSegundaVariacao: '', sacaPrimeiroAno: 'Equivalente saca 60kg', sacaSegundoAno: '', sacaTerceiroAno: '', 
				 sacaPrimeiraVariacao: 'Variação %', sacaSegundaVariacao: '', receitaPrimeiroAno: 'Receita Líquida', receitaSegundoAno: '', receitaTerceiroAno: '', 
				 receitaPrimeiraVariacao: 'Variação %', receitaSegundaVariacao: ''});
			 
			 var settings1 = {
					 data: listResponse,
		             rowHeaders: false,
		             colHeaders: false,
		             renderAllRows: true,
		             editor: false,
//							 colHeaders: ['ID', '', 'First name', 'Last name', 'IP', 'E-mail'],
		             columns: [
							      {data: 'mesEmpresaPais'},
							      {data: ''},
							      {data: 'pesoPrimeiroAno'},
							      {data: 'pesoSegundoAno'},
							      {data: 'pesoTerceiroAno'},
							      {data: ''},
							      {data: 'pesoPrimeiraVariacao'},
							      {data: 'pesoSegundaVariacao'},
							      {data: ''},
							      {data: 'sacaPrimeiroAno'},
							      {data: 'sacaSegundoAno'},
							      {data: 'sacaTerceiroAno'},
							      {data: ''},
							      {data: 'sacaPrimeiraVariacao'},
							      {data: 'sacaSegundaVariacao'},
							      {data: ''},
							      {data: 'receitaPrimeiroAno'},
							      {data: 'receitaSegundoAno'},
							      {data: 'receitaTerceiroAno'},
							      {data: ''},
							      {data: 'receitaPrimeiraVariacao'},
							      {data: 'receitaSegundaVariacao'}
						    ],
				    mergeCells: [
						{row: 0, col: 2, rowspan: 1, colspan: 3},
						{row: 1, col: 2, rowspan: 1, colspan: 3},
						{row: 0, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 9, rowspan: 1, colspan: 3},
						{row: 1, col: 9, rowspan: 1, colspan: 3},
						{row: 0, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 16, rowspan: 1, colspan: 3},
						{row: 1, col: 16, rowspan: 1, colspan: 3},
						{row: 0, col: 20, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 20, rowspan: 1, colspan: 2}//variacao
						],
					cell: [
					       	{row: 0, col: 2, className: "htCenter"},
					       	{row: 1, col: 2, className: "htCenter"},
					       	{row: 0, col: 6, className: "htCenter"},//variacao
							{row: 1, col: 6, className: "htCenter"},//variacao
							{row: 0, col: 9, className: "htCenter"},
							{row: 1, col: 9, className: "htCenter"},
							{row: 0, col: 13, className: "htCenter"},//variacao
							{row: 1, col: 13, className: "htCenter"},//variacao
							{row: 0, col: 16, className: "htCenter"},
							{row: 1, col: 16, className: "htCenter"},
							{row: 0, col: 20, className: "htCenter"},//variacao
							{row: 1, col: 20, className: "htCenter"},//variacao
							{row: 2, col: 0, className: "htCenter"},
							{row: 2, col: 2, className: "htCenter"},
							{row: 2, col: 3, className: "htCenter"},
							{row: 2, col: 4, className: "htCenter"},
							{row: 2, col: 6, className: "htCenter"},
							{row: 2, col: 7, className: "htCenter"},
							{row: 2, col: 9, className: "htCenter"},
							{row: 2, col: 10, className: "htCenter"},
							{row: 2, col: 11, className: "htCenter"},
							{row: 2, col: 13, className: "htCenter"},
							{row: 2, col: 14, className: "htCenter"},
							{row: 2, col: 16, className: "htCenter"},
							{row: 2, col: 17, className: "htCenter"},
							{row: 2, col: 18, className: "htCenter"},
							{row: 2, col: 20, className: "htCenter"},
							{row: 2, col: 21, className: "htCenter"}
					       ],
			       cells: function (row, col, prop) {
			    	      var cellProperties = {};

			    	      //rowssss
			    	      if(row === 0) {
			    	    	  
			    	    	  if(col === 2 || col === 6 || col === 7 || col === 9 || col === 10 || col === 11 || 
			    	    			  col === 13 || col === 14 || col === 16 || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhite;
//							    	    		  continue;
			    	    	  }
			    	    	  
			    	      } else if(row === 1) {
			    	    	  
			    	    	  if(col === 2 || col === 9 || col === 16) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	      } else if(row === 2) {
			    	    	  
			    	    	  if(col === 0) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhiteBold; 
			    	    	  }
			    	    	  
			    	    	  if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7
			    	    			  || col === 9|| col === 10 || col === 11 || col === 13 || col === 14 || col === 16
			    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		 cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      
			    	      //columnss
			    	      if(col === 0) {
			    	    	  
			    	    	  if(row > 2) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    	  cellProperties.renderer = columnWidth;
			    	      }
			    	      
			    	      if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7 || col === 9 || col === 10
			    	    		  || col === 11 || col === 13 || col === 14 || col === 16
		    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      return cellProperties;
			    	    },
			    	    
			    	    modifyColWidth: function(width, col){

//					    	    	if(col === 0) {
//					    	    		return 180;
//					    	    	}
			    	    	if(col === 2 || col === 3 || col === 4 || col === 9 || col === 10
				    	    		  || col === 11 || col === 16 || col === 17 || col === 18) {
			    	    		if(width > 80) {
			    	    			return 80;
			    	    		}
			    	    	}
			    	    	
			    	    	if(col === 6 || col === 7 || col === 13 || col === 14 || col === 20 || col === 21) {
			    	    		return 90;
			    	    	}
			    	    	
			    	    	if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    		return 10;
			    	    	}
			    	    }
			  };
				
			 var hot = new Handsontable(example1, settings1);
					
			}).error(function(data) {
			});
		
		$('#divRelatorioData').unblock();
	};
	
	$scope.exportadorMensalAcumuladoAno = function() {
		
		var primeiroAno = $scope.anoRelatorioGeralInicial;
		var segundoAno = primeiroAno - 1;
		var terceiroAno = primeiroAno - 2;
		
		try {
			document.getElementById('planilhaExcel').innerHTML = "";
			document.getElementById('planilhaExcelSegunda').innerHTML = "";
		} catch(exception) {
			
		}
		
//		EXPORTADOR_MENSAL(1),
		$('#divRelatorioData').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.get('/templateRelatorioAbicsDataController/findData/1/' + $scope.anoRelatorioGeralInicial + 
				'/' + $scope.mesRelatorioGeralInicial.id).success(function(listResponse) {

			 var example1 = document.getElementById('planilhaExcel');

			 listResponse.unshift({mesEmpresaPais: 'Empresa', pesoPrimeiroAno: primeiroAno, pesoSegundoAno: segundoAno, pesoTerceiroAno: terceiroAno, 
				 pesoPrimeiraVariacao: primeiroAno + 'x' + segundoAno, pesoSegundaVariacao: primeiroAno + 'x' + terceiroAno, 
				 sacaPrimeiroAno: primeiroAno, sacaSegundoAno: segundoAno, sacaTerceiroAno: terceiroAno, sacaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, 
				 sacaSegundaVariacao: primeiroAno + 'x' + terceiroAno, receitaPrimeiroAno: primeiroAno, receitaSegundoAno: segundoAno, 
				 receitaTerceiroAno: terceiroAno, receitaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, receitaSegundaVariacao: primeiroAno + 'x' + terceiroAno});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, pesoSegundoAno: '', pesoTerceiroAno: '', pesoPrimeiraVariacao: '',
				 pesoSegundaVariacao: '', sacaPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, sacaSegundoAno: '', sacaTerceiroAno: '', sacaPrimeiraVariacao: '',
				 sacaSegundaVariacao: '', receitaPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, receitaSegundoAno: '', receitaTerceiroAno: '', receitaPrimeiraVariacao: '',
				 receitaSegundaVariacao: ''});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: 'Peso Liquido (KG)', pesoSegundoAno: '', pesoTerceiroAno: '', 
				 pesoPrimeiraVariacao: 'Variação %', pesoSegundaVariacao: '', sacaPrimeiroAno: 'Equivalente saca 60kg', sacaSegundoAno: '', sacaTerceiroAno: '', 
				 sacaPrimeiraVariacao: 'Variação %', sacaSegundaVariacao: '', receitaPrimeiroAno: 'Receita Líquida', receitaSegundoAno: '', receitaTerceiroAno: '', 
				 receitaPrimeiraVariacao: 'Variação %', receitaSegundaVariacao: ''});
			 
			 var settings1 = {
					 data: listResponse,
		             rowHeaders: false,
		             colHeaders: false,
		             renderAllRows: true,
		             editor: false,
//					 colHeaders: ['ID', '', 'First name', 'Last name', 'IP', 'E-mail'],
		             columns: [
							      {data: 'mesEmpresaPais'},
							      {data: ''},
							      {data: 'pesoPrimeiroAno'},
							      {data: 'pesoSegundoAno'},
							      {data: 'pesoTerceiroAno'},
							      {data: ''},
							      {data: 'pesoPrimeiraVariacao'},
							      {data: 'pesoSegundaVariacao'},
							      {data: ''},
							      {data: 'sacaPrimeiroAno'},
							      {data: 'sacaSegundoAno'},
							      {data: 'sacaTerceiroAno'},
							      {data: ''},
							      {data: 'sacaPrimeiraVariacao'},
							      {data: 'sacaSegundaVariacao'},
							      {data: ''},
							      {data: 'receitaPrimeiroAno'},
							      {data: 'receitaSegundoAno'},
							      {data: 'receitaTerceiroAno'},
							      {data: ''},
							      {data: 'receitaPrimeiraVariacao'},
							      {data: 'receitaSegundaVariacao'}
						    ],
				    mergeCells: [
						{row: 0, col: 2, rowspan: 1, colspan: 3},
						{row: 1, col: 2, rowspan: 1, colspan: 3},
						{row: 0, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 9, rowspan: 1, colspan: 3},
						{row: 1, col: 9, rowspan: 1, colspan: 3},
						{row: 0, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 16, rowspan: 1, colspan: 3},
						{row: 1, col: 16, rowspan: 1, colspan: 3},
						{row: 0, col: 20, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 20, rowspan: 1, colspan: 2}//variacao
						],
					cell: [
					       	{row: 0, col: 2, className: "htCenter"},
					       	{row: 1, col: 2, className: "htCenter"},
					       	{row: 0, col: 6, className: "htCenter"},//variacao
							{row: 1, col: 6, className: "htCenter"},//variacao
							{row: 0, col: 9, className: "htCenter"},
							{row: 1, col: 9, className: "htCenter"},
							{row: 0, col: 13, className: "htCenter"},//variacao
							{row: 1, col: 13, className: "htCenter"},//variacao
							{row: 0, col: 16, className: "htCenter"},
							{row: 1, col: 16, className: "htCenter"},
							{row: 0, col: 20, className: "htCenter"},//variacao
							{row: 1, col: 20, className: "htCenter"},//variacao
							{row: 2, col: 0, className: "htCenter"},
							{row: 2, col: 2, className: "htCenter"},
							{row: 2, col: 3, className: "htCenter"},
							{row: 2, col: 4, className: "htCenter"},
							{row: 2, col: 6, className: "htCenter"},
							{row: 2, col: 7, className: "htCenter"},
							{row: 2, col: 9, className: "htCenter"},
							{row: 2, col: 10, className: "htCenter"},
							{row: 2, col: 11, className: "htCenter"},
							{row: 2, col: 13, className: "htCenter"},
							{row: 2, col: 14, className: "htCenter"},
							{row: 2, col: 16, className: "htCenter"},
							{row: 2, col: 17, className: "htCenter"},
							{row: 2, col: 18, className: "htCenter"},
							{row: 2, col: 20, className: "htCenter"},
							{row: 2, col: 21, className: "htCenter"}
					       ],
			       cells: function (row, col, prop) {
			    	      var cellProperties = {};

			    	      //rowssss
			    	      if(row === 0) {
			    	    	  
			    	    	  if(col === 2 || col === 6 || col === 7 || col === 9 || col === 10 || col === 11 || 
			    	    			  col === 13 || col === 14 || col === 16 || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhite;
//					    	    		  continue;
			    	    	  }
			    	    	  
			    	      } else if(row === 1) {
			    	    	  
			    	    	  if(col === 2 || col === 9 || col === 16) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	      } else if(row === 2) {
			    	    	  
			    	    	  if(col === 0) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhiteBold; 
			    	    	  }
			    	    	  
			    	    	  if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7
			    	    			  || col === 9|| col === 10 || col === 11 || col === 13 || col === 14 || col === 16
			    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		 cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      
			    	      //columnss
			    	      if(col === 0) {
			    	    	  
			    	    	  if(row > 2) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    	  cellProperties.renderer = columnWidth;
			    	      }
			    	      
			    	      if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7 || col === 9 || col === 10
			    	    		  || col === 11 || col === 13 || col === 14 || col === 16
		    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      return cellProperties;
			    	    },
			    	    
			    	    modifyColWidth: function(width, col){

//			    	    	if(col === 0) {
//			    	    		return 180;
//			    	    	}
			    	    	if(col === 2 || col === 3 || col === 4 || col === 9 || col === 10
				    	    		  || col === 11 || col === 16 || col === 17 || col === 18) {
			    	    		if(width > 80) {
			    	    			return 80;
			    	    		}
			    	    	}
			    	    	
			    	    	if(col === 6 || col === 7 || col === 13 || col === 14 || col === 20 || col === 21) {
			    	    		return 90;
			    	    	}
			    	    	
			    	    	if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    		return 10;
			    	    	}
			    	    }
			  };
				
			 var hot = new Handsontable(example1, settings1);
			
			 $('#divRelatorioData').unblock();
		}).error(function(data) {
			$('#divRelatorioData').unblock();
		});
		
//		EXPORTADOR_ACUMULADO(2),
		
		$http.get('/templateRelatorioAbicsDataController/findData/2/' + $scope.anoRelatorioGeralInicial + 
				'/' + $scope.mesRelatorioGeralInicial.id).success(function(listResponse) {

			 var example1 = document.getElementById('planilhaExcelSegunda');
			 var subTituloPlanilha = 'Janeiro a ' + $scope.mesRelatorioGeralInicial.nome;
			 
			 listResponse.unshift({mesEmpresaPais: 'Empresa', pesoPrimeiroAno: primeiroAno, pesoSegundoAno: segundoAno, pesoTerceiroAno: terceiroAno, 
				 pesoPrimeiraVariacao: primeiroAno + 'x' + segundoAno, pesoSegundaVariacao: primeiroAno + 'x' + terceiroAno, 
				 sacaPrimeiroAno: primeiroAno, sacaSegundoAno: segundoAno, sacaTerceiroAno: terceiroAno, sacaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, 
				 sacaSegundaVariacao: primeiroAno + 'x' + terceiroAno, receitaPrimeiroAno: primeiroAno, receitaSegundoAno: segundoAno, 
				 receitaTerceiroAno: terceiroAno, receitaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, receitaSegundaVariacao: primeiroAno + 'x' + terceiroAno});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: subTituloPlanilha, pesoSegundoAno: '', pesoTerceiroAno: '', pesoPrimeiraVariacao: '',
				 pesoSegundaVariacao: '', sacaPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, sacaSegundoAno: '', sacaTerceiroAno: '', sacaPrimeiraVariacao: '',
				 sacaSegundaVariacao: '', receitaPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, receitaSegundoAno: '', receitaTerceiroAno: '', receitaPrimeiraVariacao: '',
				 receitaSegundaVariacao: ''});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: 'Peso Liquido (KG)', pesoSegundoAno: '', pesoTerceiroAno: '', 
				 pesoPrimeiraVariacao: 'Variação %', pesoSegundaVariacao: '', sacaPrimeiroAno: 'Equivalente saca 60kg', sacaSegundoAno: '', sacaTerceiroAno: '', 
				 sacaPrimeiraVariacao: 'Variação %', sacaSegundaVariacao: '', receitaPrimeiroAno: 'Receita Líquida', receitaSegundoAno: '', receitaTerceiroAno: '', 
				 receitaPrimeiraVariacao: 'Variação %', receitaSegundaVariacao: ''});
			 var settings1 = {
					 data: listResponse,
		             rowHeaders: false,
		             colHeaders: false,
		             renderAllRows: true,
		             editor: false,
//					 colHeaders: ['ID', '', 'First name', 'Last name', 'IP', 'E-mail'],
		             columns: [
							      {data: 'mesEmpresaPais'},
							      {data: ''},
							      {data: 'pesoPrimeiroAno'},
							      {data: 'pesoSegundoAno'},
							      {data: 'pesoTerceiroAno'},
							      {data: ''},
							      {data: 'pesoPrimeiraVariacao'},
							      {data: 'pesoSegundaVariacao'},
							      {data: ''},
							      {data: 'sacaPrimeiroAno'},
							      {data: 'sacaSegundoAno'},
							      {data: 'sacaTerceiroAno'},
							      {data: ''},
							      {data: 'sacaPrimeiraVariacao'},
							      {data: 'sacaSegundaVariacao'},
							      {data: ''},
							      {data: 'receitaPrimeiroAno'},
							      {data: 'receitaSegundoAno'},
							      {data: 'receitaTerceiroAno'},
							      {data: ''},
							      {data: 'receitaPrimeiraVariacao'},
							      {data: 'receitaSegundaVariacao'}
						    ],
				    mergeCells: [
						{row: 0, col: 2, rowspan: 1, colspan: 3},
						{row: 1, col: 2, rowspan: 1, colspan: 3},
						{row: 0, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 9, rowspan: 1, colspan: 3},
						{row: 1, col: 9, rowspan: 1, colspan: 3},
						{row: 0, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 16, rowspan: 1, colspan: 3},
						{row: 1, col: 16, rowspan: 1, colspan: 3},
						{row: 0, col: 20, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 20, rowspan: 1, colspan: 2}//variacao
						],
					cell: [
					       	{row: 0, col: 2, className: "htCenter"},
					       	{row: 1, col: 2, className: "htCenter"},
					       	{row: 0, col: 6, className: "htCenter"},//variacao
							{row: 1, col: 6, className: "htCenter"},//variacao
							{row: 0, col: 9, className: "htCenter"},
							{row: 1, col: 9, className: "htCenter"},
							{row: 0, col: 13, className: "htCenter"},//variacao
							{row: 1, col: 13, className: "htCenter"},//variacao
							{row: 0, col: 16, className: "htCenter"},
							{row: 1, col: 16, className: "htCenter"},
							{row: 0, col: 20, className: "htCenter"},//variacao
							{row: 1, col: 20, className: "htCenter"},//variacao
							{row: 2, col: 0, className: "htCenter"},
							{row: 2, col: 2, className: "htCenter"},
							{row: 2, col: 3, className: "htCenter"},
							{row: 2, col: 4, className: "htCenter"},
							{row: 2, col: 6, className: "htCenter"},
							{row: 2, col: 7, className: "htCenter"},
							{row: 2, col: 9, className: "htCenter"},
							{row: 2, col: 10, className: "htCenter"},
							{row: 2, col: 11, className: "htCenter"},
							{row: 2, col: 13, className: "htCenter"},
							{row: 2, col: 14, className: "htCenter"},
							{row: 2, col: 16, className: "htCenter"},
							{row: 2, col: 17, className: "htCenter"},
							{row: 2, col: 18, className: "htCenter"},
							{row: 2, col: 20, className: "htCenter"},
							{row: 2, col: 21, className: "htCenter"}
					       ],
			       cells: function (row, col, prop) {
			    	      var cellProperties = {};

			    	      //rowssss
			    	      if(row === 0) {
			    	    	  
			    	    	  if(col === 2 || col === 6 || col === 7 || col === 9 || col === 10 || col === 11 || 
			    	    			  col === 13 || col === 14 || col === 16 || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhite;
//					    	    		  continue;
			    	    	  }
			    	    	  
			    	      } else if(row === 1) {
			    	    	  
			    	    	  if(col === 2 || col === 9 || col === 16) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	      } else if(row === 2) {
			    	    	  
			    	    	  if(col === 0) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhiteBold; 
			    	    	  }
			    	    	  
			    	    	  if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7
			    	    			  || col === 9|| col === 10 || col === 11 || col === 13 || col === 14 || col === 16
			    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		 cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      
			    	      //columnss
			    	      if(col === 0) {
			    	    	  
			    	    	  if(row > 2) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    	  cellProperties.renderer = columnWidth;
			    	      }
			    	      
			    	      if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7 || col === 9 || col === 10
			    	    		  || col === 11 || col === 13 || col === 14 || col === 16
		    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      return cellProperties;
			    	    },
			    	    
			    	    modifyColWidth: function(width, col){
			    	    	
//			    	    	if(col === 0) {
//			    	    		return 180;
//			    	    	}
			    	    	if(col === 2 || col === 3 || col === 4 || col === 9 || col === 10
				    	    		  || col === 11 || col === 16 || col === 17 || col === 18) {
			    	    		if(width > 80) {
			    	    			return 80;
			    	    		}
			    	    	}
			    	    	
			    	    	if(col === 6 || col === 7 || col === 13 || col === 14 || col === 20 || col === 21) {
			    	    		return 90;
			    	    	}
			    	    	 if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    		 return 10;
			    	    	 }
			    	    }
			  };
				
			 var hot = new Handsontable(example1, settings1);
			 $('#divRelatorioData').unblock();
		}).error(function(data) {
			$('#divRelatorioData').unblock();
		});
	};
	
	
	$scope.relatorioAbicsData = function(tipo) {
		
//		tipo == 
//		PAIS_MENSAL(3),
//		PAIS_ACUMULADO(4),
		
		var primeiroAno = $scope.anoRelatorioGeralInicial;
		var segundoAno = primeiroAno - 1;
		var terceiroAno = primeiroAno - 2;
		
		var url = '/templateRelatorioAbicsDataController/findData/' + tipo + 
		'/' + $scope.anoRelatorioGeralInicial + '/' + $scope.mesRelatorioGeralInicial.id;
		var tituloPlanilha = 'País - Destino';
		var subTituloPlanilha = '';
		
		try {
			document.getElementById('planilhaExcel').innerHTML = "";
			document.getElementById('planilhaExcelSegunda').innerHTML = "";
		} catch(exception) {
			
		}
		
		if(tipo == 3) {
			tituloPlanilha = 'País - Destino';
			subTituloPlanilha = $scope.mesRelatorioGeralInicial.nome;
		} else if(tipo == 4) {
			tituloPlanilha = 'País - Acumulado';
			subTituloPlanilha = 'Janeiro a ' + $scope.mesRelatorioGeralInicial.nome;
		}
		
		$('#divRelatorioData').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		
		$http.get(url).success(function(listResponse) {

			 var example1 = document.getElementById('planilhaExcel');

			 listResponse.unshift({mesEmpresaPais: tituloPlanilha, pesoPrimeiroAno: primeiroAno, pesoSegundoAno: segundoAno, pesoTerceiroAno: terceiroAno, 
				 pesoPrimeiraVariacao: primeiroAno + 'x' + segundoAno, pesoSegundaVariacao: primeiroAno + 'x' + terceiroAno, 
				 sacaPrimeiroAno: primeiroAno, sacaSegundoAno: segundoAno, sacaTerceiroAno: terceiroAno, sacaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, 
				 sacaSegundaVariacao: primeiroAno + 'x' + terceiroAno, receitaPrimeiroAno: primeiroAno, receitaSegundoAno: segundoAno, 
				 receitaTerceiroAno: terceiroAno, receitaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, receitaSegundaVariacao: primeiroAno + 'x' + terceiroAno});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: subTituloPlanilha, pesoSegundoAno: '', pesoTerceiroAno: '', pesoPrimeiraVariacao: '',
				 pesoSegundaVariacao: '', sacaPrimeiroAno: subTituloPlanilha, sacaSegundoAno: '', sacaTerceiroAno: '', sacaPrimeiraVariacao: '',
				 sacaSegundaVariacao: '', receitaPrimeiroAno: subTituloPlanilha, receitaSegundoAno: '', receitaTerceiroAno: '', receitaPrimeiraVariacao: '',
				 receitaSegundaVariacao: ''});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: 'Peso Liquido (KG)', pesoSegundoAno: '', pesoTerceiroAno: '', 
				 pesoPrimeiraVariacao: 'Variação %', pesoSegundaVariacao: '', sacaPrimeiroAno: 'Equivalente saca 60kg', sacaSegundoAno: '', sacaTerceiroAno: '', 
				 sacaPrimeiraVariacao: 'Variação %', sacaSegundaVariacao: '', receitaPrimeiroAno: 'Receita Líquida', receitaSegundoAno: '', receitaTerceiroAno: '', 
				 receitaPrimeiraVariacao: 'Variação %', receitaSegundaVariacao: ''});

			 var settings1 = {
					 data: listResponse,
		             rowHeaders: false,
		             colHeaders: false,
		             renderAllRows: true,
		             editor: false,
//					 colHeaders: ['ID', '', 'First name', 'Last name', 'IP', 'E-mail'],
		             columns: [
							      {data: 'mesEmpresaPais'},
							      {data: ''},
							      {data: 'pesoPrimeiroAno'},
							      {data: 'pesoSegundoAno'},
							      {data: 'pesoTerceiroAno'},
							      {data: ''},
							      {data: 'pesoPrimeiraVariacao'},
							      {data: 'pesoSegundaVariacao'},
							      {data: ''},
							      {data: 'sacaPrimeiroAno'},
							      {data: 'sacaSegundoAno'},
							      {data: 'sacaTerceiroAno'},
							      {data: ''},
							      {data: 'sacaPrimeiraVariacao'},
							      {data: 'sacaSegundaVariacao'},
							      {data: ''},
							      {data: 'receitaPrimeiroAno'},
							      {data: 'receitaSegundoAno'},
							      {data: 'receitaTerceiroAno'},
							      {data: ''},
							      {data: 'receitaPrimeiraVariacao'},
							      {data: 'receitaSegundaVariacao'}
						    ],
				    mergeCells: [
						{row: 0, col: 2, rowspan: 1, colspan: 3},
						{row: 1, col: 2, rowspan: 1, colspan: 3},
//						{row: 0, col: 6, rowspan: 1, colspan: 2},//variacao
//						{row: 1, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 9, rowspan: 1, colspan: 3},
						{row: 1, col: 9, rowspan: 1, colspan: 3},
						{row: 0, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 16, rowspan: 1, colspan: 3},
						{row: 1, col: 16, rowspan: 1, colspan: 3},
						{row: 0, col: 20, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 20, rowspan: 1, colspan: 2}//variacao
						],
//						className: ''
					cell: [
					       	{row: 0, col: 2, className: "htCenter"},
					       	{row: 1, col: 2, className: "htCenter"},
					       	{row: 0, col: 6, className: "htCenter htMiddle"},//variacao
							{row: 1, col: 6, className: "htCenter htMiddle"},//variacao
							{row: 0, col: 9, className: "htCenter"},
							{row: 1, col: 9, className: "htCenter"},
							{row: 0, col: 13, className: "htCenter htMiddle"},//variacao
							{row: 1, col: 13, className: "htCenter htMiddle"},//variacao
							{row: 0, col: 16, className: "htCenter"},
							{row: 1, col: 16, className: "htCenter"},
							{row: 0, col: 20, className: "htCenter htMiddle"},//variacao
							{row: 1, col: 20, className: "htCenter htMiddle"},//variacao
							{row: 2, col: 0, className: "htCenter"},
							{row: 2, col: 2, className: "htCenter"},
							{row: 2, col: 3, className: "htCenter"},
							{row: 2, col: 4, className: "htCenter"},
							{row: 2, col: 6, className: "htCenter"},
							{row: 2, col: 7, className: "htCenter"},
							{row: 2, col: 9, className: "htCenter"},
							{row: 2, col: 10, className: "htCenter"},
							{row: 2, col: 11, className: "htCenter"},
							{row: 2, col: 13, className: "htCenter"},
							{row: 2, col: 14, className: "htCenter"},
							{row: 2, col: 16, className: "htCenter"},
							{row: 2, col: 17, className: "htCenter"},
							{row: 2, col: 18, className: "htCenter"},
							{row: 2, col: 20, className: "htCenter"},
							{row: 2, col: 21, className: "htCenter"}
					       ],
			       cells: function (row, col, prop) {
			    	      var cellProperties = {};

			    	      if(row === 0) {
			    	    	  
			    	    	  if(col === 2 || col === 6 || col === 7 || col === 9 || col === 10 || col === 11 || 
			    	    			  col === 13 || col === 14 || col === 16 || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhite;
//			    	    		  continue;
			    	    	  }
			    	    	  
			    	      } else if(row === 1) {
			    	    	  
			    	    	  if(col === 2 || col === 9 || col === 16) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	      } else if(row === 2) {
			    	    	  
			    	    	  if(col === 0) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhiteBold; 
			    	    	  }
			    	    	  
			    	    	  if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7
			    	    			  || col === 9|| col === 10 || col === 11 || col === 13 || col === 14 || col === 16
			    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		 cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      
			    	      if(col === 0) {
			    	    	  
			    	    	  if(row > 2) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    	  cellProperties.renderer = columnWidth;
			    	      }
			    	      
			    	      
			    	      if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7 || col === 9 || col === 10
			    	    		  || col === 11 || col === 13 || col === 14 || col === 16
		    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      return cellProperties;
			    	    },
			    	    
			    	    modifyColWidth: function(width, col){

			    	    	if(col === 2 || col === 3 || col === 4 || col === 9 || col === 10
				    	    		  || col === 11 || col === 16 || col === 17 || col === 18) {
			    	    		if(width > 80) {
			    	    			return 80;
			    	    		}
			    	    	}
			    	    	
			    	    	if(col === 6 || col === 7 || col === 13 || col === 14 || col === 20 || col === 21) {
			    	    		return 90;
			    	    	}
			    	    	 if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    		 return 10;
			    	    	 }
			    	    }
			  };
			 var hot = new Handsontable(example1, settings1);
			 $('#divRelatorioData').unblock();
		}).error(function(data) {
			$('#divRelatorioData').unblock();
		});

	};
	
	
	$scope.relatorioAbicsDataProduto = function() {
		var primeiroAno = $scope.anoRelatorioGeralInicial;
		var segundoAno = primeiroAno - 1;
		var terceiroAno = primeiroAno - 2;
		
		try {
			document.getElementById('planilhaExcel').innerHTML = "";
			document.getElementById('planilhaExcelSegunda').innerHTML = "";
		} catch(exception) {
			
		}
		
//		PRODUTO_MENSAL(5),
		$('#divRelatorioData').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.get('/templateRelatorioAbicsDataController/findData/5/' + $scope.anoRelatorioGeralInicial + 
				'/' + $scope.mesRelatorioGeralInicial.id).success(function(listResponse) {

			 var example1 = document.getElementById('planilhaExcel');

			 listResponse.unshift({mesEmpresaPais: 'Empresa', pesoPrimeiroAno: primeiroAno, pesoSegundoAno: segundoAno, pesoTerceiroAno: terceiroAno, 
				 pesoPrimeiraVariacao: primeiroAno + 'x' + segundoAno, pesoSegundaVariacao: primeiroAno + 'x' + terceiroAno, 
				 sacaPrimeiroAno: primeiroAno, sacaSegundoAno: segundoAno, sacaTerceiroAno: terceiroAno, sacaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, 
				 sacaSegundaVariacao: primeiroAno + 'x' + terceiroAno, receitaPrimeiroAno: primeiroAno, receitaSegundoAno: segundoAno, 
				 receitaTerceiroAno: terceiroAno, receitaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, receitaSegundaVariacao: primeiroAno + 'x' + terceiroAno});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, pesoSegundoAno: '', pesoTerceiroAno: '', pesoPrimeiraVariacao: '',
				 pesoSegundaVariacao: '', sacaPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, sacaSegundoAno: '', sacaTerceiroAno: '', sacaPrimeiraVariacao: '',
				 sacaSegundaVariacao: '', receitaPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, receitaSegundoAno: '', receitaTerceiroAno: '', receitaPrimeiraVariacao: '',
				 receitaSegundaVariacao: ''});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: 'Peso Liquido (KG)', pesoSegundoAno: '', pesoTerceiroAno: '', 
				 pesoPrimeiraVariacao: 'Variação %', pesoSegundaVariacao: '', sacaPrimeiroAno: 'Equivalente saca 60kg', sacaSegundoAno: '', sacaTerceiroAno: '', 
				 sacaPrimeiraVariacao: 'Variação %', sacaSegundaVariacao: '', receitaPrimeiroAno: 'Receita Líquida', receitaSegundoAno: '', receitaTerceiroAno: '', 
				 receitaPrimeiraVariacao: 'Variação %', receitaSegundaVariacao: ''});
			 
			 var settings1 = {
					 data: listResponse,
		             rowHeaders: false,
		             colHeaders: false,
		             renderAllRows: true,
		             editor: false,
//					 colHeaders: ['ID', '', 'First name', 'Last name', 'IP', 'E-mail'],
		             columns: [
							      {data: 'mesEmpresaPais'},
							      {data: ''},
							      {data: 'pesoPrimeiroAno'},
							      {data: 'pesoSegundoAno'},
							      {data: 'pesoTerceiroAno'},
							      {data: ''},
							      {data: 'pesoPrimeiraVariacao'},
							      {data: 'pesoSegundaVariacao'},
							      {data: ''},
							      {data: 'sacaPrimeiroAno'},
							      {data: 'sacaSegundoAno'},
							      {data: 'sacaTerceiroAno'},
							      {data: ''},
							      {data: 'sacaPrimeiraVariacao'},
							      {data: 'sacaSegundaVariacao'},
							      {data: ''},
							      {data: 'receitaPrimeiroAno'},
							      {data: 'receitaSegundoAno'},
							      {data: 'receitaTerceiroAno'},
							      {data: ''},
							      {data: 'receitaPrimeiraVariacao'},
							      {data: 'receitaSegundaVariacao'}
						    ],
				    mergeCells: [
						{row: 0, col: 2, rowspan: 1, colspan: 3},
						{row: 1, col: 2, rowspan: 1, colspan: 3},
						{row: 0, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 9, rowspan: 1, colspan: 3},
						{row: 1, col: 9, rowspan: 1, colspan: 3},
						{row: 0, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 16, rowspan: 1, colspan: 3},
						{row: 1, col: 16, rowspan: 1, colspan: 3},
						{row: 0, col: 20, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 20, rowspan: 1, colspan: 2}//variacao
						],
					cell: [
					       	{row: 0, col: 2, className: "htCenter"},
					       	{row: 1, col: 2, className: "htCenter"},
					       	{row: 0, col: 6, className: "htCenter"},//variacao
							{row: 1, col: 6, className: "htCenter"},//variacao
							{row: 0, col: 9, className: "htCenter"},
							{row: 1, col: 9, className: "htCenter"},
							{row: 0, col: 13, className: "htCenter"},//variacao
							{row: 1, col: 13, className: "htCenter"},//variacao
							{row: 0, col: 16, className: "htCenter"},
							{row: 1, col: 16, className: "htCenter"},
							{row: 0, col: 20, className: "htCenter"},//variacao
							{row: 1, col: 20, className: "htCenter"},//variacao
							{row: 2, col: 0, className: "htCenter"},
							{row: 2, col: 2, className: "htCenter"},
							{row: 2, col: 3, className: "htCenter"},
							{row: 2, col: 4, className: "htCenter"},
							{row: 2, col: 6, className: "htCenter"},
							{row: 2, col: 7, className: "htCenter"},
							{row: 2, col: 9, className: "htCenter"},
							{row: 2, col: 10, className: "htCenter"},
							{row: 2, col: 11, className: "htCenter"},
							{row: 2, col: 13, className: "htCenter"},
							{row: 2, col: 14, className: "htCenter"},
							{row: 2, col: 16, className: "htCenter"},
							{row: 2, col: 17, className: "htCenter"},
							{row: 2, col: 18, className: "htCenter"},
							{row: 2, col: 20, className: "htCenter"},
							{row: 2, col: 21, className: "htCenter"}
					       ],
			       cells: function (row, col, prop) {
			    	      var cellProperties = {};

			    	      //rowssss
			    	      if(row === 0) {
			    	    	  
			    	    	  if(col === 2 || col === 6 || col === 7 || col === 9 || col === 10 || col === 11 || 
			    	    			  col === 13 || col === 14 || col === 16 || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhite;
//					    	    		  continue;
			    	    	  }
			    	    	  
			    	      } else if(row === 1) {
			    	    	  
			    	    	  if(col === 2 || col === 9 || col === 16) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	      } else if(row === 2) {
			    	    	  
			    	    	  if(col === 0) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhiteBold; 
			    	    	  }
			    	    	  
			    	    	  if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7
			    	    			  || col === 9|| col === 10 || col === 11 || col === 13 || col === 14 || col === 16
			    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		 cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      
			    	      //columnss
			    	      if(col === 0) {
			    	    	  
			    	    	  if(row > 2) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    	  cellProperties.renderer = columnWidth;
			    	      }
			    	      
			    	      if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7 || col === 9 || col === 10
			    	    		  || col === 11 || col === 13 || col === 14 || col === 16
		    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      return cellProperties;
			    	    },
			    	    
			    	    modifyColWidth: function(width, col){

//			    	    	if(col === 0) {
//			    	    		return 180;
//			    	    	}
			    	    	if(col === 2 || col === 3 || col === 4 || col === 9 || col === 10
				    	    		  || col === 11 || col === 16 || col === 17 || col === 18) {
			    	    		if(width > 80) {
			    	    			return 80;
			    	    		}
			    	    	}
			    	    	
			    	    	if(col === 6 || col === 7 || col === 13 || col === 14 || col === 20 || col === 21) {
			    	    		return 90;
			    	    	}
			    	    	
			    	    	if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    		return 10;
			    	    	}
			    	    }
			  };
				
			 var hot = new Handsontable(example1, settings1);
			 $('#divRelatorioData').unblock();
		}).error(function(data) {
			$('#divRelatorioData').unblock();
		});
		
//		PRODUTO_ACUMULADO(6),
		$http.get('/templateRelatorioAbicsDataController/findData/6/' + $scope.anoRelatorioGeralInicial + 
				'/' + $scope.mesRelatorioGeralInicial.id).success(function(listResponse) {

			 var example1 = document.getElementById('planilhaExcelSegunda');
			 var subTituloPlanilha = 'Janeiro a ' + $scope.mesRelatorioGeralInicial.nome;
			 
			 listResponse.unshift({mesEmpresaPais: 'Empresa', pesoPrimeiroAno: primeiroAno, pesoSegundoAno: segundoAno, pesoTerceiroAno: terceiroAno, 
				 pesoPrimeiraVariacao: primeiroAno + 'x' + segundoAno, pesoSegundaVariacao: primeiroAno + 'x' + terceiroAno, 
				 sacaPrimeiroAno: primeiroAno, sacaSegundoAno: segundoAno, sacaTerceiroAno: terceiroAno, sacaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, 
				 sacaSegundaVariacao: primeiroAno + 'x' + terceiroAno, receitaPrimeiroAno: primeiroAno, receitaSegundoAno: segundoAno, 
				 receitaTerceiroAno: terceiroAno, receitaPrimeiraVariacao: primeiroAno + 'x' + segundoAno, receitaSegundaVariacao: primeiroAno + 'x' + terceiroAno});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: subTituloPlanilha, pesoSegundoAno: '', pesoTerceiroAno: '', pesoPrimeiraVariacao: '',
				 pesoSegundaVariacao: '', sacaPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, sacaSegundoAno: '', sacaTerceiroAno: '', sacaPrimeiraVariacao: '',
				 sacaSegundaVariacao: '', receitaPrimeiroAno: $scope.mesRelatorioGeralInicial.nome, receitaSegundoAno: '', receitaTerceiroAno: '', receitaPrimeiraVariacao: '',
				 receitaSegundaVariacao: ''});
			 listResponse.unshift({mesEmpresaPais: '', pesoPrimeiroAno: 'Peso Liquido (KG)', pesoSegundoAno: '', pesoTerceiroAno: '', 
				 pesoPrimeiraVariacao: 'Variação %', pesoSegundaVariacao: '', sacaPrimeiroAno: 'Equivalente saca 60kg', sacaSegundoAno: '', sacaTerceiroAno: '', 
				 sacaPrimeiraVariacao: 'Variação %', sacaSegundaVariacao: '', receitaPrimeiroAno: 'Receita Líquida', receitaSegundoAno: '', receitaTerceiroAno: '', 
				 receitaPrimeiraVariacao: 'Variação %', receitaSegundaVariacao: ''});
			 var settings1 = {
					 data: listResponse,
		             rowHeaders: false,
		             colHeaders: false,
		             renderAllRows: true,
		             editor: false,
//					 colHeaders: ['ID', '', 'First name', 'Last name', 'IP', 'E-mail'],
		             columns: [
							      {data: 'mesEmpresaPais'},
							      {data: ''},
							      {data: 'pesoPrimeiroAno'},
							      {data: 'pesoSegundoAno'},
							      {data: 'pesoTerceiroAno'},
							      {data: ''},
							      {data: 'pesoPrimeiraVariacao'},
							      {data: 'pesoSegundaVariacao'},
							      {data: ''},
							      {data: 'sacaPrimeiroAno'},
							      {data: 'sacaSegundoAno'},
							      {data: 'sacaTerceiroAno'},
							      {data: ''},
							      {data: 'sacaPrimeiraVariacao'},
							      {data: 'sacaSegundaVariacao'},
							      {data: ''},
							      {data: 'receitaPrimeiroAno'},
							      {data: 'receitaSegundoAno'},
							      {data: 'receitaTerceiroAno'},
							      {data: ''},
							      {data: 'receitaPrimeiraVariacao'},
							      {data: 'receitaSegundaVariacao'}
						    ],
				    mergeCells: [
						{row: 0, col: 2, rowspan: 1, colspan: 3},
						{row: 1, col: 2, rowspan: 1, colspan: 3},
						{row: 0, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 6, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 9, rowspan: 1, colspan: 3},
						{row: 1, col: 9, rowspan: 1, colspan: 3},
						{row: 0, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 13, rowspan: 1, colspan: 2},//variacao
						{row: 0, col: 16, rowspan: 1, colspan: 3},
						{row: 1, col: 16, rowspan: 1, colspan: 3},
						{row: 0, col: 20, rowspan: 1, colspan: 2},//variacao
						{row: 1, col: 20, rowspan: 1, colspan: 2}//variacao
						],
					cell: [
					       	{row: 0, col: 2, className: "htCenter"},
					       	{row: 1, col: 2, className: "htCenter"},
					       	{row: 0, col: 6, className: "htCenter"},//variacao
							{row: 1, col: 6, className: "htCenter"},//variacao
							{row: 0, col: 9, className: "htCenter"},
							{row: 1, col: 9, className: "htCenter"},
							{row: 0, col: 13, className: "htCenter"},//variacao
							{row: 1, col: 13, className: "htCenter"},//variacao
							{row: 0, col: 16, className: "htCenter"},
							{row: 1, col: 16, className: "htCenter"},
							{row: 0, col: 20, className: "htCenter"},//variacao
							{row: 1, col: 20, className: "htCenter"},//variacao
							{row: 2, col: 0, className: "htCenter"},
							{row: 2, col: 2, className: "htCenter"},
							{row: 2, col: 3, className: "htCenter"},
							{row: 2, col: 4, className: "htCenter"},
							{row: 2, col: 6, className: "htCenter"},
							{row: 2, col: 7, className: "htCenter"},
							{row: 2, col: 9, className: "htCenter"},
							{row: 2, col: 10, className: "htCenter"},
							{row: 2, col: 11, className: "htCenter"},
							{row: 2, col: 13, className: "htCenter"},
							{row: 2, col: 14, className: "htCenter"},
							{row: 2, col: 16, className: "htCenter"},
							{row: 2, col: 17, className: "htCenter"},
							{row: 2, col: 18, className: "htCenter"},
							{row: 2, col: 20, className: "htCenter"},
							{row: 2, col: 21, className: "htCenter"}
					       ],
			       cells: function (row, col, prop) {
			    	      var cellProperties = {};

			    	      //rowssss
			    	      if(row === 0) {
			    	    	  
			    	    	  if(col === 2 || col === 6 || col === 7 || col === 9 || col === 10 || col === 11 || 
			    	    			  col === 13 || col === 14 || col === 16 || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhite;
//					    	    		  continue;
			    	    	  }
			    	    	  
			    	      } else if(row === 1) {
			    	    	  
			    	    	  if(col === 2 || col === 9 || col === 16) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	      } else if(row === 2) {
			    	    	  
			    	    	  if(col === 0) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhiteBold; 
			    	    	  }
			    	    	  
			    	    	  if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7
			    	    			  || col === 9|| col === 10 || col === 11 || col === 13 || col === 14 || col === 16
			    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    		 cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      
			    	      //columnss
			    	      if(col === 0) {
			    	    	  
			    	    	  if(row > 2) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    	  cellProperties.renderer = columnWidth;
			    	      }
			    	      
			    	      if(col === 2 || col === 3 || col === 4 || col === 6 || col === 7 || col === 9 || col === 10
			    	    		  || col === 11 || col === 13 || col === 14 || col === 16
		    	    			  || col === 17 || col === 18 || col === 20 || col === 21) {
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      return cellProperties;
			    	    },
			    	    
			    	    modifyColWidth: function(width, col){
			    	    	
			    	    	if(col === 2 || col === 3 || col === 4 || col === 9 || col === 10
				    	    		  || col === 11 || col === 16 || col === 17 || col === 18) {
			    	    		if(width > 80) {
			    	    			return 80;
			    	    		}
			    	    	}
			    	    	
			    	    	if(col === 6 || col === 7 || col === 13 || col === 14 || col === 20 || col === 21) {
			    	    		return 90;
			    	    	}
			    	    	 if(col === 1 || col === 5 || col === 8 || col === 12 || col === 15 || col === 19) {
			    	    		 return 10;
			    	    	 }
			    	    }
			  };
				
			 var hot = new Handsontable(example1, settings1);
			 $('#divRelatorioData').unblock();
		}).error(function(data) {
			$('#divRelatorioData').unblock();
		});
	};
	
	$scope.relatorioAbicsDataAnual = function(tipo) {
		
//		TIPO == 
//		PAIS_ANUAL(7),
//		PRODUTO_ANUAL(8),
//		PAIS_NCM_21011190(9),
//		PAIS_NCM_21011200(10);

		var quantidadeAnos = 8;
		var primeiroAno = $scope.anoRelatorioGeralInicial;
		var ultimoAno = primeiroAno - quantidadeAnos + 1; // +1 para contar o ano selecionado
		var anoList = [];
		
		for(var i = 0; i < quantidadeAnos; i++) {
			anoList[i] = primeiroAno - (quantidadeAnos - i) + 1; // +1 para contar o ano selecionado
		}
		
//		console.log(anoList)
		
		var url = '/templateRelatorioAbicsDataController/findDataAnual/' + tipo + 
		'/' + primeiroAno + '/' + ultimoAno + '/' + $scope.mesRelatorioGeralInicial.id;
		var tituloPlanilha = 'País - Destino';
//		var subTituloPlanilha = $scope.mesRelatorioGeralInicial.nome;
		
		try {
			document.getElementById('planilhaExcel').innerHTML = "";
			document.getElementById('planilhaExcelSegunda').innerHTML = "";
		} catch(exception) {
			
		}
		
		if(tipo == 7) {
			tituloPlanilha = 'País - Destino';
//			subTituloPlanilha = $scope.mesRelatorioGeralInicial.nome;
		} else if(tipo == 8) {
			tituloPlanilha = 'Produto - Destino';
//			subTituloPlanilha = 'Janeiro a ' + $scope.mesRelatorioGeralInicial.nome;
		} else if(tipo == 9) {
			tituloPlanilha = 'País anual NCM 2101.11.90';
		} else if(tipo == 10) {
			tituloPlanilha = 'País anual NCM 2101.12.00';
		}
		
		$('#divRelatorioData').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.get(url).success(function(listResponse) {

			 var example1 = document.getElementById('planilhaExcel');

			 listResponse.unshift({mesEmpresaPais: tituloPlanilha, pesoAno1: anoList[0], pesoAno2: anoList[1], pesoAno3: anoList[2],
				 pesoAno4: anoList[3], pesoAno5: anoList[4], pesoAno6: anoList[5], pesoAno7: anoList[6], pesoAno8: anoList[7],
				 receitaAno1: anoList[0], receitaAno2: anoList[1], receitaAno3: anoList[2],
				 receitaAno4: anoList[3], receitaAno5: anoList[4], receitaAno6: anoList[5], receitaAno7: anoList[6], receitaAno8: anoList[7]});
			 listResponse.unshift({mesEmpresaPais: '', pesoAno1: '', pesoAno2: '', pesoAno3: '',
				 pesoAno4: '', pesoAno5: '', pesoAno6: '', pesoAno7: '', pesoAno8: '',
				 receitaAno1: '', receitaAno2: '', receitaAno3: '',
				 receitaAno4: '', receitaAno5: '', receitaAno6: '', receitaAno7: '', receitaAno8: ''});
			 listResponse.unshift({mesEmpresaPais: '', pesoAno1: 'Peso Liquido (KG)', pesoAno2: '', pesoAno3: '',
				 pesoAno4: '', pesoAno5: '', pesoAno6: '', pesoAno7: '', pesoAno8: '',
				 receitaAno1: 'Receita Cambial (x1000 US$)', receitaAno2: '', receitaAno3: '',
				 receitaAno4: '', receitaAno5: '', receitaAno6: '', receitaAno7: '', receitaAno8: ''});
			 
			 var settings1 = {
					 data: listResponse,
		             rowHeaders: false,
		             colHeaders: false,
		             renderAllRows: true,
		             editor: false,
//					 colHeaders: ['ID', '', 'First name', 'Last name', 'IP', 'E-mail'],
		             columns: [
							      {data: 'mesEmpresaPais'},
							      {data: ''},
							      {data: 'pesoAno1'},
							      {data: 'pesoAno2'},
							      {data: 'pesoAno3'},
							      {data: 'pesoAno4'},
							      {data: 'pesoAno5'},
							      {data: 'pesoAno6'},
							      {data: 'pesoAno7'},
							      {data: 'pesoAno8'},
							      {data: ''},
							      {data: 'receitaAno1'},
							      {data: 'receitaAno2'},
							      {data: 'receitaAno3'},
							      {data: 'receitaAno4'},
							      {data: 'receitaAno5'},
							      {data: 'receitaAno6'},
							      {data: 'receitaAno7'},
							      {data: 'receitaAno8'}
						    ],
				    mergeCells: [
						{row: 0, col: 2, rowspan: 1, colspan: 8},
						{row: 1, col: 2, rowspan: 1, colspan: 8},
						{row: 0, col: 11, rowspan: 1, colspan: 8},//variacao
						{row: 1, col: 11, rowspan: 1, colspan: 8}//variacao
						],
//						className: ''
					cell: [
					       	{row: 0, col: 2, className: "htCenter"},
					       	{row: 1, col: 2, className: "htCenter"},
							{row: 0, col: 11, className: "htCenter"},
							{row: 1, col: 11, className: "htCenter"},
//							{row: 0, col: 13, className: "htCenter htMiddle"},//variacao
//							{row: 1, col: 20, className: "htCenter htMiddle"},//variacao
							{row: 2, col: 0, className: "htCenter"},
							{row: 2, col: 2, className: "htCenter"},
							{row: 2, col: 3, className: "htCenter"},
							{row: 2, col: 4, className: "htCenter"},
							{row: 2, col: 5, className: "htCenter"},
							{row: 2, col: 6, className: "htCenter"},
							{row: 2, col: 7, className: "htCenter"},
							{row: 2, col: 8, className: "htCenter"},
							{row: 2, col: 9, className: "htCenter"},
							{row: 2, col: 11, className: "htCenter"},
							{row: 2, col: 12, className: "htCenter"},
							{row: 2, col: 13, className: "htCenter"},
							{row: 2, col: 14, className: "htCenter"},
							{row: 2, col: 15, className: "htCenter"},
							{row: 2, col: 16, className: "htCenter"},
							{row: 2, col: 17, className: "htCenter"},
							{row: 2, col: 18, className: "htCenter"}
					       ],
			       cells: function (row, col, prop) {
			    	      var cellProperties = {};

			    	      if(row === 0) {
			    	    	  
			    	    	  if(col === 2 || col === 11) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhite;
//			    	    		  continue;
			    	    	  }
			    	    	  
			    	      } else if(row === 2) {
			    	    	  
			    	    	  if(col === 0) {
			    	    		  cellProperties.renderer = backgroundBrowFontWhiteBold; 
			    	    	  }
			    	    	  
			    	    	  if(col === 2 || col === 3 || col === 4 || col === 5 || col === 6 || col === 7
			    	    			  || col === 8 || col === 9 || col === 11 || col === 12 || col === 13 || col === 14
			    	    			  || col === 15 || col === 16 || col === 17 || col === 18) {
			    	    		 cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      
			    	      if(col === 0) {
			    	    	  
			    	    	  if(row > 2) {
			    	    		  cellProperties.renderer = backgroundWhiteFontBrowBold;
			    	    	  }
			    	    	  
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      if(col === 1 || col === 10) {
			    	    	  cellProperties.renderer = columnWidth;
			    	      }
			    	      
			    	      if(col === 2 || col === 3 || col === 4 || col === 5 || col === 6 || col === 7
		    	    			  || col === 8 || col === 9 || col === 11 || col === 12 || col === 13 || col === 14
		    	    			  || col === 15 || col === 16 || col === 17 || col === 18) {
			    	    	  if(row == this.instance.getData().length - 1) {
			    	    		  cellProperties.renderer = backgroundBrowFontBrowBold;
			    	    	  }
			    	      }
			    	      
			    	      return cellProperties;
			    	    },
			    	    
			    	    modifyColWidth: function(width, col){

			    	    	 if(col === 1 || col === 10) {
			    	    		 return 10;
			    	    	 }
			    	    }
			  };
			 var hot = new Handsontable(example1, settings1);
			 $('#divRelatorioData').unblock();
		}).error(function(data) {
			$('#divRelatorioData').unblock();
		});
	};
	
	$scope.imprimirRelatorioGeral = function() {
		
		$scope.tipoRelatorioSelecionadoList = ["Empresa","País","UE", "Produto"];
		
		var objConsulta = new FormData();
		objConsulta.append('tipoRelatorio', $scope.tipoRelatorioSelecionadoList);
		objConsulta.append('mes', ($scope.mesRelatorioGeralInicial.id).toString());
		objConsulta.append('ano', $scope.anoRelatorioGeralInicial.toString());
		
		$('#divRelatorioData').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		$http.post('/templateAbicsDataController/gerarExcel', objConsulta,
				{transformRequest: angular.identity, headers: {'Content-Type': undefined}, responseType: 'arraybuffer'}).success(function(data) {

			   var a = document.createElement("a");
			   document.body.appendChild(a);
			   a.style = "display: none";
			   var file = new Blob([data], {type: 'application/vnd.ms-excel'}); // data.contents
			   var fileURL = URL.createObjectURL(file);
			   a.target = "_blank";
			   a.href = fileURL;
			   var path = 'relatorio_geral';
			   
			   angular.forEach($scope.tipoRelatorioSelecionadoList, function(value, key) {
				   path += "_" + value;
			   });
			   
			   a.download = path + ".xls";
			   
			   a.click();
			   $('#divRelatorioData').unblock();
		});
	};
});