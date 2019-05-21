var moduleCategoria = angular.module('moduleCategoria', []);

moduleItem.directive('directiveModalCadastrarCategoria', function() {
	return {
		templateUrl : '/assets/directives/directiveModalCadastrarCategoria.html'
	};
});

moduleItem.directive('directiveModalVisualizarCategoria', function() {
	return {
		templateUrl : '/assets/directives/directiveModalVisualizarCategoria.html'
	};
});

moduleItem.directive('directiveModalEditarCategoria', function() {
	return {
		templateUrl : '/assets/directives/directiveModalEditarCategoria.html'
	};
});

moduleItem.directive('directiveModalRemoverCategoria', function() {
	return {
		templateUrl : '/assets/directives/directiveModalRemoverCategoria.html'
	};
});

moduleCategoria.controller('controllerCategoria', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Aqui são cadastradas as categorias que fazem parte de uma cotação.';
	$scope.caminhoVideo = 'assets/videos/add-itens-to-categoria.mp4';

	$scope.alertaSistemaTemplate = {};
	$scope.categoriaList = [];

	function findAllCategoria() {
		$http.get('/categoriaController/findAll').success(function(categoriaListResponse) {
			$scope.categoriaList = categoriaListResponse;
		});
	}

	findAllCategoria();

/** INICIO MODAIS **/
	$scope.abrirModalCadastrarCategoria = function(categoria) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalCadastrarCategoria',
            controller: modalControlerCadastrarCategoria,
            size: 'md'
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllCategoria();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};

	$scope.abrirModalVisualizarCategoria = function(categoria) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalVisualizarCategoria',
            controller: modalControlerVisualizarCategoria,
            size: 'lg',
            resolve: {
            	categoriaParametro: function () {
                    return categoria;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllCategoria();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};

	$scope.abrirModalEditarCategoria = function(categoria) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalEditarCategoria',
            controller: modalControlerEditarCategoria,
            size: 'md',
            resolve: {
            	categoriaParametro: function () {
                    return categoria;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllCategoria();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};


	$scope.abrirModalRemoverCategoria = function(categoria) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalRemoverCategoria',
            controller: modalControlerRemoverCategoria,
            size: 'md',
            resolve: {
            	categoriaParametro: function () {
                    return categoria;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllCategoria();
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


var modalControlerCadastrarCategoria = function ($scope, $modalInstance, $http, $timeout, $sce) {

	$scope.objetoCategoria = {};

	$scope.cadastrarCategoria = function() {

        $http.post('/categoriaController/cadastrarCategoria', $scope.objetoCategoria).success(function(abstractResponse) {

        	$scope.objetoCategoria = {};
        	$modalInstance.close(abstractResponse);
        });
	}

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};

var modalControlerVisualizarCategoria = function ($scope, $modalInstance, $http, $timeout, $sce, categoriaParametro) {

	$scope.paginaSelecionada = 1;

    $scope.proximo = function() {

    	$('#page'+$scope.paginaSelecionada).hide( "slide", { direction: "left" }, 600 );
    	$scope.paginaSelecionada++;
    	$('#page'+$scope.paginaSelecionada).show( "slide", { direction: "right" }, 600 );
    }

    $scope.voltar = function() {

    	$('#page'+$scope.paginaSelecionada).hide( "slide", { direction: "right" }, 600 );
    	$scope.paginaSelecionada--;
    	$('#page'+$scope.paginaSelecionada).show( "slide", { direction: "left" }, 600 );
    }

    $scope.objetoCategoria = {};
	$scope.objetoCategoria = categoriaParametro;
	$scope.objetoCategoriaItemEditar = {};
	$scope.objetoCategoriaItemEditar.idItens = [];
	$scope.objetoCategoriaItemEditar.ordemItemList = [];

	$scope.itensCategoria = [];
	$scope.itemList = [];
	$scope.itensCategoriaSelecionar = [];

    $http.get('/categoriaController/findItemCategoria/' + $scope.objetoCategoria.id).success(function(dataResponse) {
    	$scope.itensCategoria = dataResponse;

        $http.get('/categoriaController/findAllItem').success(function(itemResponse) {
        	$scope.itemList = itemResponse;

        	angular.forEach($scope.itemList, function(item, key) {

        		angular.forEach($scope.itensCategoria, function(relCategoriaItem, key) {
        			if(!item.selecionado) {
	        			if(item.id == relCategoriaItem.itemId) {
		    				item.selecionado = true;
		    				item.ordem = relCategoriaItem.ordem;
		    				if(relCategoriaItem.isCategoriaTotal == 1) {
		    					item.totalCategoria = true;
		    				} else {
		    					item.totalCategoria = false;
		    				}
		    			} else {
		    				item.selecionado = false;
		    			}
        			}
        		});
        		$scope.itensCategoriaSelecionar.push(item);
    		});
        });
    });

    $scope.salvar = function() {

		angular.forEach($scope.itensCategoriaSelecionar, function(item, key) {
			if(item.selecionado) {
				$scope.objetoCategoriaItemEditar.idItens.push(item.id);
				$scope.objetoCategoriaItemEditar.ordemItemList.push(item.ordem);

				if(item.totalCategoria) {
					console.log(item);
					$scope.objetoCategoriaItemEditar.categoriaIdTotalCategoria = item.id;
				}
			}
		});

    	$scope.objetoCategoriaItemEditar.categoriaId = categoriaParametro.id;

		$http.post('/categoriaController/vincularItemCategoria', $scope.objetoCategoriaItemEditar).success(function(abstractResponse) {

			$scope.objetoCategoria = {};
			$scope.objetoCategoriaItemEditar = {};
			$modalInstance.close(abstractResponse);
		});
    };

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};

var modalControlerEditarCategoria = function ($scope, $modalInstance, $http, $timeout, $sce, categoriaParametro) {

	$scope.objetoCategoria = {};
	$scope.objetoCategoria = categoriaParametro;

	$scope.objetoCategoriaEditar = {};
	$scope.objetoCategoriaEditar.id = categoriaParametro.id;
	$scope.objetoCategoriaEditar.nome = categoriaParametro.nome;
	$scope.objetoCategoriaEditar.descricao = categoriaParametro.descricao;

	$scope.alterarCategoria = function() {

        $http.post('/categoriaController/alterarCategoria', $scope.objetoCategoriaEditar).success(function(abstractResponse) {

        	$scope.objetoCategoria = {};
        	$scope.objetoCategoriaEditar = {};
        	$modalInstance.close(abstractResponse);
        });
	}

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};

var modalControlerRemoverCategoria = function ($scope, $modalInstance, $http, $timeout, $sce, categoriaParametro) {

	$scope.objetoCategoria = {};
	$scope.objetoCategoria = categoriaParametro;

	$scope.removerCategoria = function() {

        $http.get('/categoriaController/removerCategoria/' + categoriaParametro.id).success(function(abstractResponse) {

        	$scope.objetoCategoria = {};
        	$modalInstance.close(abstractResponse);
        });
	}

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};