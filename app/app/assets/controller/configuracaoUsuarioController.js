var moduleConfiguracaoUsuario = angular.module('moduleConfiguracaoUsuario', []);

moduleConfiguracaoUsuario.controller('controllerConfiguracaoUsuario', ['$scope', '$http', '$modal', '$timeout', '$window', '$location', '$q', '$cookies', 'userService',
						function($scope, $http, $modal, $timeout, $window, $location, $q, $cookies, userService) {

	$scope.informacoes = 'Atualize suas informações como nome e senha.';
	$scope.caminhoVideo = 'assets/videos/first-login.mp4';

	$scope.alertaSistemaTemplate = {};
	$scope.objetoUsuario = {};
	$scope.objetoUsuario = userService.getUsuario()
	$scope.objetoUsuario.senha = '';
	$scope.senhaDisabled = true;
	$scope.login = userService.getLogin();

	if($scope.objetoUsuario.primeiroLogin == 0) {
//		drawerExample.style.display = "none";
		$scope.senhaDisabled = false;
		$scope.objetoUsuario.nome = '';
		$scope.objetoUsuario.senha = '';
		$scope.repetirSenha = '';
	}
	
	$scope.checkRepetirSenha = function() {
		if($scope.repetirSenha != $scope.objetoUsuario.senha || $scope.objetoUsuario.senha == '' || $scope.objetoUsuario.senha == undefined) {
			$scope.formConfiguracaoUsuario.$invalid = true;
		} else {
			$scope.formConfiguracaoUsuario.$invalid = false;
		}
	};

	$scope.salvar = function() {

		if($scope.objetoUsuario.nome == '') {

        	$scope.alertaSistemaTemplate.message = 'Nome é obrigatório!'
        	$scope.alertaSistemaTemplate.type = 'danger'
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = 'remove';
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
			return;
		}
		
		if(!$scope.senhaDisabled && $scope.objetoUsuario.senha == '') {

        	$scope.alertaSistemaTemplate.message = 'Senha é obrigatório!'
        	$scope.alertaSistemaTemplate.type = 'danger'
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = 'remove';
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
			return;
		}

		//nao modificar a senha
		if($scope.senhaDisabled) {
			$scope.objetoUsuario.senha = '';
		}

        $http.post('/usuarioController/atualizar', $scope.objetoUsuario).success(function(abstractResponse) {

        	userService.setTipoRelatorio($scope.objetoUsuario.tipoRelatorio);
        	userService.setNome($scope.objetoUsuario.nome);
        	userService.setPrimeiroLogin(1)

//        	drawerExample.style.display = "";
        	$scope.senhaDisabled = true;
        	
        	if($scope.objetoUsuario.senha != undefined && $scope.objetoUsuario.senha != '') {
        		$scope.logoutSistema(true);
        	} else {
        		$scope.alertaSistemaTemplate.message = 'Informações alteradas!'
            	$scope.alertaSistemaTemplate.type = 'success'
            	$scope.alertaSistemaTemplate.show = true;
            	$scope.alertaSistemaTemplate.icon = 'thumbs-up';
            	$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
            	
            	$scope.$parent.nomeUsuarioLogado = $scope.objetoUsuario.nome;
            	$scope.$parent.primeiroLogin = 1;
            	$scope.objetoUsuario.primeiroLogin = 1;
            	$scope.objetoUsuario.senha = '';
        	}
        });
	}
}]);
