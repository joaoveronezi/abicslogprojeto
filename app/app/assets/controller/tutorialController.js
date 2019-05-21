var moduleTutorial = angular.module('moduleTutorial', []);

moduleTutorial.controller('controllerTutorial', function($scope, $http, $timeout, $modal) {
	
	$scope.caminhoVideo = 'assets/videos/first-login.mp4';
	$scope.nomeVideo = 'Primeiro Login';
	
	$scope.carregarVideo = function(nome, caminhoVideo) {
		
		$scope.caminhoVideo = 'assets/videos/' + caminhoVideo;	
		$scope.nomeVideo = nome;
		
		var player = document.getElementById('videoCaminhoVideo');
		var mp4Vid = document.getElementById('sourceCaminhoVideo');
		player.pause();
		mp4Vid.src = $scope.caminhoVideo;
		player.load();
		player.play();
	};
});