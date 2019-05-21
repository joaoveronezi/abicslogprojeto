var moduleGraficoRelatorio = angular.module('moduleGraficoRelatorio', ['ui.bootstrap', 'ngRoute', 'ngCookies']);

moduleGraficoRelatorio.controller('controllerGraficoRelatorio', function($scope, $http, $timeout, $modal) {

	 var formData = new FormData();
//	   formData.append('imagem_chart_png', byteArr);
//	   formData.append('data_inicial', $scope.dataInicial.getTime());
//	   formData.append('data_final', $scope.dataFinal.getTime());
	
	// $http.post('/indexController/graficoRelatorio', formData,
	// 		   {transformRequest: angular.identity, headers: {'Content-Type': undefined},
	// 			responseType: 'arraybuffer'}).success(function(data) {
					
	// 			});
	
	try {
		graficoValorMercadoriaRelatorio.destroy();
	} catch (exception) {
	
	}
	
	graficoValorMercadoriaRelatorio = c3.generate({
		bindto: '#graficoValorMercadoriaRelatorio',
		data: {
			columns: [['data1', 30, 20, 50, 40, 60, 50],
			            ['data2', 200, 130, 90, 240, 130, 220],
			            ['data3', 300, 200, 160, 400, 250, 250],
			            ['data4', 200, 130, 90, 240, 130, 220],
			            ['data5', 130, 120, 150, 140, 160, 150],
			            ['data6', 90, 70, 20, 50, 60, 120]],
			type : 'bar',
			types : {'Media Indonesia': 'line', 'Media Vietnam' : 'line', 'Media Costa do Marfim' : 'line',
				'Media Cotacao Brasil' : 'spline', 'Media Nacional Teste' : 'spline',  
				'Media Nacional' : 'spline', 'Media Internacional' : 'line'}
		},
		axis: {
			x: {
				type: 'category',
				tick: {
	               rotate: 75,
	               multiline: false
				},
				height: 80
			}
		}
	});
	
	
});
