var moduleCompetitividade = angular.module('moduleCompetitividade', []);

moduleCompetitividade.controller('controllerCompetitividade', function($scope, $http, $timeout, $modal) {

	$scope.listPartners = [];
	$http.get('/competitividadeController/findAllPartners').success(function(response) {
		$scope.listPartners = response;
	});

	$http.get('/competitividadeController/findAllComtradeDataSumarizacao').success(function(response) {
		
	});
	
	$http.get('/competitividadeController/graficoCoffeeNetweight/' + 1).success(function(response) {

//		$('#divCompetitividade').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		
		try {
			graficoCoffeeNetweightImport.destroy();
		} catch (exception) {
	
		}
		
		graficoCoffeeNetweightImport = c3.generate({
			bindto: '#graficoCoffeeNetweightImport',
			data: {
				columns: [],
				type : 'line'
			},
			axis: {
				x: {
					type: 'category',
					tick: {
		               rotate: 75
					},
					height: 75
				}
			}
		});

		graficoCoffeeNetweightImport.categories(response.categories);
		graficoCoffeeNetweightImport.load(response);
		
//		$('#divCompetitividade').unblock({});
	});
	
	$http.get('/competitividadeController/graficoCoffeeNetweight/' + 2).success(function(response) {

//		$('#divCompetitividade').block({ message: '<img src="/assets/images/loading_gif.gif" />' });
		
		try {
			graficoCoffeeNetweightExport.destroy();
		} catch (exception) {
	
		}
		
		graficoCoffeeNetweightExport = c3.generate({
			bindto: '#graficoCoffeeNetweightExport',
			data: {
				columns: [],
				type : 'line'
			},
			axis: {
				x: {
					type: 'category',
					tick: {
		               rotate: 75
					},
					height: 75
				}
			}
		});

		graficoCoffeeNetweightExport.categories(response.categories);
		graficoCoffeeNetweightExport.load(response);
		
//		$('#divCompetitividade').unblock({});
	});
});