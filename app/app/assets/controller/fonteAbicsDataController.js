var moduleFonteAbicsData = angular.module('moduleFonteAbicsData', []);

moduleFonteAbicsData.controller('controllerFonteAbicsData', function($scope, $http, $timeout, $modal, $location){

	$scope.informacoes = 'A fonte do câmbio, as fontes das cotações e a fonte de cálculo de logística. '+ 
		'As metodologias utilizadas para o cálculo dos custos de importação.';
	$scope.caminhoVideo = 'assets/videos/fonts.mp4';
	
	$scope.voltarTemplate = function() {
		$location.path('templateAbicsData');
	};

});