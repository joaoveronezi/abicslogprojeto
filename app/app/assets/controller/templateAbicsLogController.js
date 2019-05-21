var moduleTemplateAbicsLog = angular.module('moduleTemplateAbicsLog', []);

moduleTemplateAbicsData.controller('controllerTemplateAbicsLog', function($scope, $http, $timeout, $modal, $location){

	$("#logDropDown").removeClass("ng-hide");
	$("#abicsDropDown").removeClass("ng-hide");
//	$("#abicsDropDown").addClass("ng-hide");
	
	$scope.checkLog = function() {
		if($scope.primeiroLogin == 1) {
			if($scope.permissaoUsuarioLogado == 5){
				$("#divData").removeClass("col-md-offset-0");
				return false;
			} else if($scope.permissaoUsuarioLogado == 3){
				$("#divLog").addClass("col-md-offset-4");
				$("#divLog").removeClass("col-md-offset-2");
				return true;
				
			} else if($scope.permissaoUsuarioLogado == 4){
				$("#divLog").addClass("col-md-offset-4");
				$("#divLog").removeClass("col-md-offset-2");
				
				return true;
			} else if($scope.permissaoUsuarioLogado == 2 || $scope.permissaoUsuarioLogado == 1) {
				return true;
			} else if($scope.permissaoUsuarioLogado == 5) {
				return false;
			}
		} else {
			return false;
		}
	}
	
	$scope.mudarPagina = function(caminho) {
		$location.path(caminho);
	};
});