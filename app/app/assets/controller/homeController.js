var moduleHome = angular.module('moduleHome', []);

moduleHome.controller('controllerHome', function($scope, $location) {
	
	$("#abicsDropDown").addClass("ng-hide");
	$("#logDropDown").addClass("ng-hide");

	$scope.mudarPagina = function(location) {
		$location.path(location);
	}
	
	$scope.checkData = function() {
		if($scope.primeiroLogin == 1) {
			if($scope.permissaoUsuarioLogado == 1 || $scope.permissaoUsuarioLogado == 2){
				return true;
				
			} else if($scope.permissaoUsuarioLogado == 5) {
				$("#divDataHome").removeClass("col-md-offset-3");
				$("#divDataHome").addClass("col-md-offset-5");
				
				return true;
			}
		}
		
		return false;
	}
	
	$scope.checkLog = function() {
		if($scope.primeiroLogin == 1) {
			if($scope.permissaoUsuarioLogado == 5){
				return false;
				
			} else if($scope.permissaoUsuarioLogado == 3 || $scope.permissaoUsuarioLogado == 4){
				$("#divLogHome").removeClass("col-md-offset-1");
				$("#divLogHome").addClass("col-md-offset-5");
				return true;
				
			} else if($scope.permissaoUsuarioLogado == 2 || $scope.permissaoUsuarioLogado == 1) {
				return true;
				
			}
		}
		
		return false;
	}
	
});
