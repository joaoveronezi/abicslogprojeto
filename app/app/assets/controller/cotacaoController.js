var moduleCotacao = angular.module('moduleCotacao', []);

moduleCotacao.directive('directiveModalVisualizarCotacao', function() {
	return {
		templateUrl : '/assets/directives/directiveModalVisualizarCotacao.html'
	};
});

moduleCotacao.directive('directiveModalCadastrarCotacao', function() {
	return {
		templateUrl : '/assets/directives/directiveModalCadastrarCotacao.html'
	};
});

moduleCotacao.directive('directiveModalEditarCotacao', function() {
	return {
		templateUrl : '/assets/directives/directiveModalEditarCotacao.html'
	};
});

moduleCotacao.directive('directiveModalRemoverCotacao', function() {
	return {
		templateUrl : '/assets/directives/directiveModalRemoverCotacao.html'
	};
});

moduleCotacao.controller('controllerCotacao', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Aqui são cadastradas as cotações, vinculando às categorias.';
	$scope.caminhoVideo = 'assets/videos/add-categoria-to-cotacao.mp4';

	$scope.alertaSistemaTemplate = {};
	$scope.cotacaoList = [];
	function findAllCotacao() {

		$http.get('/cotacaoController/findAll').success(function(cotacaoListResponse) {
			$scope.cotacaoList = cotacaoListResponse;
		});
	}

	findAllCotacao();

	/**INICIO MODAIS*/
	$scope.abrirModalVisualizarCotacao = function(cotacao) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalVisualizarCotacao',
            controller: modalControlerVisualizarCotacao,
            size: 'lg',
            resolve: {
            	cotacaoParametro: function () {
                    return cotacao;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllCotacao();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};

	$scope.abrirModalCadastrarCotacao = function() {

		var modalInstance = $modal.open({
            templateUrl: 'idModalCadastrarCotacao',
            controller: modalControlerCadastrarCotacao,
            size: 'md'
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllCotacao();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};

	$scope.abrirModalAlterarCotacao = function(cotacao) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalEditarCotacao',
            controller: modalControlerEditarCotacao,
            size: 'md',
            resolve: {
            	cotacaoParametro: function () {
                    return cotacao;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllCotacao();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};

	$scope.abrirModalRemoverCotacao = function(cotacao) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalRemoverCotacao',
            controller: modalControlerRemoverCotacao,
            size: 'md',
            resolve: {
            	cotacaoParametro: function () {
                    return cotacao;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllCotacao();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};

	/**FIM MODAIS*/
});

var modalControlerVisualizarCotacao = function ($scope, $modalInstance, $http, $timeout, $sce, cotacaoParametro) {

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

	$scope.objetoCotacao = {};
	$scope.objetoCotacao = cotacaoParametro;
	$scope.categoriasCotacaoList = [];
	$scope.categoriaList = [];

	$scope.objetoCotacaoCategoriaEditar = {};
	$scope.objetoCotacaoCategoriaEditar.idCategorias = [];
	$scope.objetoCotacaoCategoriaEditar.ordemList = [];
	$scope.categoriaSelecionarList = [];

    $http.get('/cotacaoController/findCategoriasCotacao/' + $scope.objetoCotacao.id).success(function(responseList) {
    	$scope.categoriasCotacaoList = responseList;

        $http.get('/cotacaoController/findAllCategoria').success(function(categoriaResponse) {
        	$scope.categoriaList = categoriaResponse;

        	angular.forEach($scope.categoriaList, function(categoria, key) {

        		angular.forEach($scope.categoriasCotacaoList, function(relCotacaoCategoria, key) {
        			if(!categoria.selecionado) {
	        			if(categoria.id == relCotacaoCategoria.categoriaId) {
	        				categoria.selecionado = true;
	        				categoria.ordem = relCotacaoCategoria.ordem;
		    			} else {
		    				categoria.selecionado = false;
		    			}
        			}
        		});
        		$scope.categoriaSelecionarList.push(categoria);
    		});
        });
    });

    $scope.salvar = function() {

		angular.forEach($scope.categoriaSelecionarList, function(categoria, key) {
			if(categoria.selecionado) {
				$scope.objetoCotacaoCategoriaEditar.idCategorias.push(categoria.id);
				$scope.objetoCotacaoCategoriaEditar.ordemList.push(categoria.ordem);
			}
		});

    	$scope.objetoCotacaoCategoriaEditar.cotacaoId = cotacaoParametro.id;

		$http.post('/cotacaoController/vincularCategoriaCotacao', $scope.objetoCotacaoCategoriaEditar).success(function(abstractResponse) {

			$scope.objetoCotacao = {};
			$scope.objetoCotacaoCategoriaEditar = {};
			$modalInstance.close(abstractResponse);
		});
    };

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};

var modalControlerCadastrarCotacao = function ($scope, $modalInstance, $http, $timeout, $sce) {

	$scope.objetoCotacao = {};

	$scope.cadastrarCotacao = function() {

        $http.post('/cotacaoController/cadastrarCotacao', $scope.objetoCotacao).success(function(abstractResponse) {

        	$scope.objetoCotacao = {};
        	$modalInstance.close(abstractResponse);
        });
	}

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};

var modalControlerEditarCotacao = function ($scope, $modalInstance, $http, $timeout, $sce, cotacaoParametro) {

	$scope.objetoCotacao = {};
	$scope.objetoCotacao = cotacaoParametro;

	$scope.objetoCotacaoEditar = {};
	$scope.objetoCotacaoEditar.id = cotacaoParametro.id;
	$scope.objetoCotacaoEditar.nome = cotacaoParametro.nome;
	$scope.objetoCotacaoEditar.descricao = cotacaoParametro.descricao;
	$scope.objetoCotacaoEditar.nacional = cotacaoParametro.nacional;

	$scope.alterarCotacao = function() {

        $http.post('/cotacaoController/alterarCotacao', $scope.objetoCotacaoEditar).success(function(abstractResponse) {

        	$scope.objetoCotacao = {};
        	$scope.objetoCotacaoEditar = {};
        	$modalInstance.close(abstractResponse);
        });
	}

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};

var modalControlerRemoverCotacao = function ($scope, $modalInstance, $http, $timeout, $sce, cotacaoParametro) {

	$scope.objetoCotacao = {};
	$scope.objetoCotacao = cotacaoParametro;

	$scope.removerCotacao = function() {

        $http.get('/cotacaoController/removerCotacao/' + cotacaoParametro.id).success(function(abstractResponse) {

        	$scope.objetoCotacao = {};
        	$modalInstance.close(abstractResponse);
        });
	}

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};