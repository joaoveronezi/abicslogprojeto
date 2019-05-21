var moduleIndexVar = angular.module('moduleIndex', ['ui.bootstrap', 'ngRoute', 'ngCookies', 'angular-md5']);

moduleIndexVar.directive('directiveAlertaSistema', function() {
	return {
		templateUrl : '/assets/directives/directiveAlertaSistema.html'
	};
});

moduleIndexVar.directive('directiveModalRecuperarSenha', function() {
	return {
		templateUrl : '/assets/directives/directiveModalRecuperarSenha.html'
	};
});

moduleIndexVar.controller('indexController', ['$rootScope', '$scope', '$http', '$modal', '$timeout', '$window', '$cookies', '$location', 'md5',
                                              function($rootScope, $scope, $http, $modal, $timeout, $window, $cookies, $location, md5) {

//	$scope.dataInicial = moment().add(-1, 'months').toDate();
	$scope.dataInicialIsOpen = false;

	$scope.openDataInicial = function() {
		$scope.dataInicialIsOpen = true;
		console.log('iniciail ' + $scope.dataInicialIsOpen);
	};
	
	$scope.alertaSistema = {};
	$scope.userlogin = '';
	$scope.usersenha = '';
	$scope.loginSistema = function() {

		if(!$scope.userlogin || !$scope.usersenha) {
	
			$scope.alertaSistema = {type: 'danger', message: 'Usuario e senha obrigatorios', show: true, icon: 'remove'};
			$timeout(function() { $scope.alertaSistema.show = false; }, 5000);
		} else {
//			
			var userLogin = {};
			userLogin.login = $scope.userlogin;
			userLogin.senha = md5.createHash($scope.usersenha);
//			userLogin.password = $scope.userpassword;
//
			$http.post('/indexController/logarSistema', userLogin).success(function(dataResponse, status, headers, response) {

				if(dataResponse.type == 'success') {
					$window.location.href = '/redirectTemplate';
				} else {
//					
					$scope.alertaSistema = {type: 'danger', message: 'Usuario e/ou senha incorreto!', show: true, icon: 'remove'};
					$timeout(function() { $scope.alertaSistema.show = false; }, 5000);
////	                $scope.alert = {type: 'w', message: data.message, show: true};
////	                $scope.alert.type = data.type;
////	                $scope.alert.show = true;
////	                $scope.alert.icon = 'remove';
////	                $scope.alert.message = data.message;
////	                $timeout(function() { $scope.alert.show = false; }, 5000);
				}
//	
			}).error(function(data) {
//	
//				$scope.alertaSistema = {type: 'e', message: 'Erro ao fazer a consulta!', show: true};
//				$timeout(function() { $scope.alertaSistema.show = false; }, 5000);
//				
////				$scope.alert = {type: 'w', message: 'Erro ao fazer a consulta!', show: true};
////	            $scope.alert.type = data.type;
////	            $scope.alert.show = true;
////	            $scope.alert.message = data.message;
////	            $timeout(function() { $scope.alert.show = false; }, 5000);
			});
//			
		}
	};
	
	$scope.abrirModalRecuperarSenha = function() {
		var modalInstance = $modal.open({
            templateUrl: 'idModalRecuperarSenha',
            controller: modalControlerRecuperarSenha,
            size: 'md'
        });

		modalInstance.result.then(function (abstractResponse) {

        	$scope.alertaSistema.message = abstractResponse.message;
        	$scope.alertaSistema.type = abstractResponse.type;
        	$scope.alertaSistema.show = true;
        	$scope.alertaSistema.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistema.show = false; }, 5000);

		}, function (parametro) {
			
			
	    });
	};
}]);

var modalControlerRecuperarSenha = function ($scope, $modalInstance, $http, $timeout, $sce) {

	$scope.objetoEmail = {};
	
	$scope.recuperarSenha = function() {
		
        $http.post('/indexController/recuperarSenha', $scope.objetoEmail).success(function(abstractResponse) {

        	$modalInstance.close(abstractResponse);
        });
	}
	
	$scope.fechar = function() {
		$modalInstance.dismiss();
	};
};