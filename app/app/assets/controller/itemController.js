var moduleItem = angular.module('moduleItem', []);

moduleItem.directive('directiveModalCadastrarItem', function() {
	return {
		templateUrl : '/assets/directives/directiveModalCadastrarItem.html'
	};
});
//
//moduleItem.directive('directiveModalAdicionarValorItem', function() {
//	return {
//		templateUrl : '/assets/directives/directiveModalAdicionarValorItem.html'
//	};
//});

moduleItem.directive('directiveModalEditarItem', function() {
	return {
		templateUrl : '/assets/directives/directiveModalEditarItem.html'
	};
});

moduleItem.directive('directiveModalRemoverItem', function() {
	return {
		templateUrl : '/assets/directives/directiveModalRemoverItem.html'
	};
});

moduleItem.controller('controllerItem', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Aqui são cadastrados os itens que farão parte das categorias.' +
	' Os itens possuem dois tipos: Entrada pelo Usuário  ou Calculado Automaticamente.';
	$scope.caminhoVideo = 'assets/videos/add-value-item.mp4';

	$scope.alertaSistemaTemplate = {};
	$scope.itemList = [];

	findAllItemList();
	function findAllItemList() {

		$scope.itemList = [];

		$http.get('/itemController/findAll').success(function(itemListResponse) {
			$scope.itemList = itemListResponse;
		});
	};
	/** INICIO MODAIS **/
	$scope.abrirModalCadastrarItem = function() {
		var modalInstance = $modal.open({
			templateUrl: 'idModalCadastrarItem',
			controller: modalControlerCadastrarItem,
			size: 'md'
		});

		modalInstance.result.then(function (abstractResponse) {
			findAllItemList();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
		});
	};

//	$scope.abrirModalAdicionarValorItem = function(item) {
//
//		var modalInstance = $modal.open({
//            templateUrl: 'idModalAdicionarValorItem',
//            controller: modalControlerAdicionarValorItem,
//            size: 'lg',
//            resolve: {
//            	itemParametro: function () {
//                    return item;
//                }
//            }
//        });
//
//		modalInstance.result.then(function (abstractResponse) {
//			findAllItemList();
//        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
//        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
//        	$scope.alertaSistemaTemplate.show = true;
//        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
//			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
//		}, function (parametro) {
//	    });
//	};

	$scope.abrirModalEditarItem = function(item) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalEditarItem',
            controller: modalControlerEditarItem,
            size: 'md',
            resolve: {
            	itemParametro: function () {
                    return item;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllItemList();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};

	$scope.abrirModalRemoverItem = function(item) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalRemoverItem',
            controller: modalControlerRemoverItem,
            size: 'md',
            resolve: {
            	itemParametro: function () {
                    return item;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllItemList();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};

	/** FIM MODAIS **/
});

var modalControlerCadastrarItem = function ($scope, $modalInstance, $http, $timeout, $sce) {

//    $http.get('/itemController/getEnumVisivel').success(function(enumResponse) {
//    	$scope.visivelList = enumResponse;
//    });

    $http.get('/itemController/getEnumUnidade').success(function(enumResponse) {
    	$scope.unidadeList = enumResponse;
    });
//
//    $http.get('/itemController/getEnumTipo').success(function(enumResponse) {
//    	$scope.tipoList = enumResponse;
//    });

	$scope.objetoItem = {};
	$scope.objetoItem.unidade = 0;
//	$scope.objetoItem.tipo = 1;
//	$scope.objetoItem.visivel = 1;

	$scope.cadastrarItem = function() {

        $http.post('/itemController/cadastrarItem', $scope.objetoItem).success(function(abstractResponse) {

        	$scope.objetoItem = {};
        	$modalInstance.close(abstractResponse);
        });
	}

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};


//var modalControlerAdicionarValorItem = function ($scope, $modalInstance, $http, $timeout, $sce, itemParametro) {
//
//	$scope.paginaSelecionada = 1;
//
//    $scope.proximo = function() {
//
//    	$('#page'+$scope.paginaSelecionada).hide( "slide", { direction: "left" }, 600 );
//    	$scope.paginaSelecionada++;
//    	$('#page'+$scope.paginaSelecionada).show( "slide", { direction: "right" }, 600 );
//    }
//
//    $scope.voltar = function() {
//
//    	$('#page'+$scope.paginaSelecionada).hide( "slide", { direction: "right" }, 600 );
//    	$scope.paginaSelecionada--;
//    	$('#page'+$scope.paginaSelecionada).show( "slide", { direction: "left" }, 600 );
//    }
//
//	$scope.itemValorList = [];
//	$scope.itensListSelecionar = [];
//	$scope.relVariavelItemList = [];
//	$scope.inputFormulaDisabled = true;
//
//	$scope.objetoItemValor = {};
//	$scope.objetoItemValor.itemId = itemParametro.id;
//	$scope.objetoItemValor.itemNome = itemParametro.nome;
////	$scope.objetoItemValor.tipo = itemParametro.tipo;
////	$scope.objetoItemValor.formula = itemParametro.formula;
//	$scope.objetoItemValor.unidade = itemParametro.unidade;
//
//	$scope.objetoItemValorEditar = {};
//	$scope.objetoItemValorEditar.itemId = itemParametro.id;
////	$scope.objetoItemValorEditar.formula = itemParametro.formula;
//	$scope.objetoItemValorEditar.tipo = itemParametro.tipo;
//
//	if($scope.objetoItemValor.unidade == 1) {
//		$scope.objetoItemValorEditar.tipoMoeda = 1;
//	}
//
//	$scope.objetoItemValorEditar.idItens = [];
//	$scope.objetoItemValorEditar.variavelFormulaList = [];
//	$scope.objetoItemValorEditar.ordemItemList = [];
//
////	var letras = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
//
//	if($scope.objetoItemValor.tipo == 1) {
//
//		$http.get('/itemController/findAllItemValorByItem/' + $scope.objetoItemValor.itemId).success(function(response) {
//			$scope.itemValorList = response;
//		});
//	}
//
////	if($scope.objetoItemValor.tipo != 1) {
////
////		var contadorLetras = 0;
////
////		$http.get('/itemController/findAllRelItemVariavelByItem/' + $scope.objetoItemValor.itemId).success(function(response) {
////			$scope.relVariavelItemList = response;
////
////	        $http.get('/itemController/findAll').success(function(itemResponse) {
////
////	        	angular.forEach(itemResponse, function(item, key) {
////
////	        		angular.forEach($scope.relVariavelItemList, function(relItemVariavel, key) {
////
////	        			if(!item.selecionado) {
////	        				if(item.id == relItemVariavel.id) {
////	        					item.selecionado = true;
////	        					item.variavel = relItemVariavel.variavel;
////	        					item.ordem = relItemVariavel.ordem;
////	        				} else {
////	        					item.selecionado = false;
////	        				}
////	        			}
////	        		});
////	        		$scope.itensListSelecionar.push(item);
////	    		});
////	        });
////		});
////	}
//
//	$scope.adicionarFormula = function() {
//		$scope.inputFormulaDisabled = !$scope.inputFormulaDisabled;
//	}
//
//	$scope.adicionaValorItem = function() {
//
//		angular.forEach($scope.itensListSelecionar, function(item, key) {
//			if(item.selecionado) {
//				$scope.objetoItemValorEditar.idItens.push(item.id);
//				$scope.objetoItemValorEditar.variavelFormulaList.push(item.variavel);
//				$scope.objetoItemValorEditar.ordemItemList.push(item.ordem);
//			}
//		});
//
//		$http.post('/itemController/adicionaValorItem', $scope.objetoItemValorEditar).success(function(abstractResponse) {
//
//			$scope.objetoItemValor = {};
//			$scope.objetoItemValorEditar = {};
//			$modalInstance.close(abstractResponse);
//		});
//	}
//
//	$scope.fechar = function() {
//		$modalInstance.dismiss();
//	};
//};

var modalControlerEditarItem = function ($scope, $modalInstance, $http, $timeout, $sce, itemParametro) {

//    $http.get('/itemController/getEnumVisivel').success(function(enumResponse) {
//    	$scope.visivelList = enumResponse;
//    });

    $http.get('/itemController/getEnumUnidade').success(function(enumResponse) {
    	$scope.unidadeList = enumResponse;
    });
//
//    $http.get('/itemController/getEnumTipo').success(function(enumResponse) {
//    	$scope.tipoList = enumResponse;
//    });

	$scope.objetoItem = {};
	$scope.objetoItem = itemParametro;

	$scope.objetoItemEditar = {};
	$scope.objetoItemEditar.id = itemParametro.id;
	$scope.objetoItemEditar.nome = itemParametro.nome;
	$scope.objetoItemEditar.descricao = itemParametro.descricao;
//	$scope.objetoItemEditar.visivel = itemParametro.visivel;
	$scope.objetoItemEditar.unidade = itemParametro.unidade;
//	$scope.objetoItemEditar.tipo = itemParametro.tipo;

	$http.get('/itemController/findAllItemValorByItem/' + $scope.objetoItemEditar.id).success(function(response) {
		$scope.itemValorList = response;
	});
	
	$scope.alterarItem = function() {

		$http.post('/itemController/alterarItem', $scope.objetoItemEditar).success(function(abstractResponse) {

			$scope.objetoItem = {};
			$modalInstance.close(abstractResponse);
		});
	};

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};


var modalControlerRemoverItem = function ($scope, $modalInstance, $http, $timeout, $sce, itemParametro) {

	$scope.objetoItem = {};
	$scope.objetoItem = itemParametro;

	$scope.removerItem = function() {

		$http.get('/itemController/removerItem/' + $scope.objetoItem.id).success(function(abstractResponse) {

			$scope.objetoItem = {};
			$modalInstance.close(abstractResponse);
		});
	};

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};