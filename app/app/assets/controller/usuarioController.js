var moduleUsuario = angular.module('moduleUsuario', []);

moduleItem.directive('directiveModalCadastrarUsuario', function() {
	return {
		templateUrl : '/assets/directives/directiveModalCadastrarUsuario.html'
	};
});

moduleItem.directive('directiveModalRemoverUsuario', function() {
	return {
		templateUrl : '/assets/directives/directiveModalRemoverUsuario.html'
	};
});

moduleUsuario.controller('controllerUsuario', function($scope, $http, $timeout, $modal) {

	$scope.informacoes = 'Convidar usuário. Insira o email do usuário e o sistema enviará um email ' + 
		'de convite ao usuário. Os perfis são divididos em: Diretor, pode convidar outros usuários; ' +
		'Convidado, pode ver todas as informações mas NÃO pode convidar outros usuários.';
	$scope.caminhoVideo = 'assets/videos/invite.mp4';

	$scope.alertaSistemaTemplate = {};
	$scope.usuarioList = [];

	function findAllUsuario() {
		$scope.usuarioList = [];
		$http.get('/usuarioController/findAll').success(function(usuarioListResponse) {
			$scope.usuarioList = usuarioListResponse;
		});
	}

	findAllUsuario();

	/**INICIO MODAIS*/

/*	$scope.abrirModalCadastrarUsuario = function() {

		var modalInstance = $modal.open({
            templateUrl: 'idModalCadastrarUsuario',
            controller: modalControlerCadastrarUsuario,
            size: 'md'
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllUsuario();
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
		}, function (parametro) {
	    });
	};*/

	$scope.abrirModalRemoverUsuario = function(usuario) {

		var modalInstance = $modal.open({
            templateUrl: 'idModalRemoverUsuario',
            controller: modalControlerRemoverUsuario,
            size: 'md',
            resolve: {
            	usuarioParametro: function () {
                    return usuario;
                }
            }
        });

		modalInstance.result.then(function (abstractResponse) {
			findAllUsuario();
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

//var modalControlerCadastrarUsuario = function ($scope, $modalInstance, $http, $timeout, $sce) {
//
//	$scope.objetoUsuario = {};
//
//	$scope.cadastrarUsuario = function() {
//
//        $http.post('/usuarioController/cadastrarUsuario', $scope.objetoUsuario).success(function(abstractResponse) {
//
//        	$scope.objetoUsuario = {};
//        	$modalInstance.close(abstractResponse);
//        });
//	}
//
//	$scope.fechar = function() {
//		$modalInstance.dismiss();
//	};
//};

var modalControlerRemoverUsuario = function ($scope, $modalInstance, $http, $timeout, $sce, usuarioParametro) {

	$scope.objetoUsuario = {};
	$scope.objetoUsuario = usuarioParametro;

	$scope.removerUsuario = function() {

		if($scope.objetoUsuario.ativo == 1)  {
			
			$http.get('/usuarioController/removerUsuario/' + usuarioParametro.id).success(function(abstractResponse) {
				
				$scope.objetoUsuario = {};
				$modalInstance.close(abstractResponse);
			});
		} else {

			$http.get('/usuarioController/ativarUsuario/' + usuarioParametro.id).success(function(abstractResponse) {
				
				$scope.objetoUsuario = {};
				$modalInstance.close(abstractResponse);
			});
		}
	}
	

	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};
