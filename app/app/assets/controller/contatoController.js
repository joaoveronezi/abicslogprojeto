var moduleContato = angular.module('moduleContato', []);

moduleContato.controller('controllerContato', function($scope, $http, $timeout, $modal, userService) {
	
	$scope.informacoes = 'Possui alguma dúvida? Alguma sugestão? Envie para nós!';
	$scope.caminhoVideo = 'assets/videos/contato.mp4';
	
	$scope.alertaSistemaTemplate = {};
	$scope.objetoUsuario = userService.getUsuario();
	$scope.assuntoEmail = '';
	$scope.corpoEmail = '';
	
	$scope.enviarEmailContato = function() {

		$scope.objetoEmail = {};
		$scope.objetoEmail.assuntoEmail = $scope.assuntoEmail;
		$scope.objetoEmail.corpoEmail = $scope.corpoEmail;
		
        $http.post('/contatoController/enviarEmailContato', $scope.objetoEmail).success(function(abstractResponse) {

    		$scope.assuntoEmail = '';
    		$scope.corpoEmail = '';
    		
        	$scope.alertaSistemaTemplate.message = abstractResponse.message;
        	$scope.alertaSistemaTemplate.type = abstractResponse.type;
        	$scope.alertaSistemaTemplate.show = true;
        	$scope.alertaSistemaTemplate.icon = abstractResponse.icon;
			$timeout(function() { $scope.alertaSistemaTemplate.show = false; }, 5000);
        });
		
	}
});