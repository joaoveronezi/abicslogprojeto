var moduleLogisticaHistorico = angular.module('moduleLogisticaHistorico', []);

moduleLogisticaHistorico.controller('controllerLogisticaHistorico', function($scope, $http, $timeout, $modal){

	$scope.informacoes = 'Os valores do custo de logística para a importação do café dos países abaixo. ' +
	'Fonte: CECAFE';
	$scope.caminhoVideo = 'assets/videos/log.mp4';

	$scope.dataList = [{'data': '07/2016'}];

	$scope.cotacaoInternacionalList = [{nome:'Vietnam'}, {nome:'Indonésia'}, {nome:'Uganda'}, {nome:'Bolsa de Londres'}];
	$scope.valorFreteList = [{valor: 3100}, {valor: 3100}, {valor: 1850}, {valor: 0}];
	$scope.taxaSeguroList = [{valor: 0}, {valor: 0}, {valor: 0}, {valor: 0}];
	$scope.importacaoList = [{valor: 0}, {valor: 0}, {valor: 0}, {valor: 0}];
	$scope.demurrageContainerList = [{valor: 0}, {valor: 0}, {valor: 0}, {valor: 0}];
	$scope.thcList = [{valor: 800}, {valor: 800}, {valor: 800}, {valor: 0}];
	
	$scope.liberacaoList = [{valor: 373}, {valor: 373}, {valor: 373}, {valor: 0}];
	$scope.desconsolidacaoList = [{valor: 0}, {valor: 0}, {valor: 0}, {valor: 0}];
	$scope.siscargaList = [{valor: 0}, {valor: 0}, {valor: 0}, {valor: 0}];
	$scope.ispsList = [{valor: 11}, {valor: 11}, {valor: 11}, {valor: 0}];
	$scope.siscomexList = [{valor: 0}, {valor: 0}, {valor: 0}, {valor: 0}];
	$scope.armazenagemList = [{valor: 0}, {valor: 0}, {valor: 0}, {valor: 0}];
	$scope.despachanteList = [{valor: 0}, {valor: 0}, {valor: 0}, {valor: 0}];
	$scope.sindicatoAduaneirosList = [{valor: 0}, {valor: 0}, {valor: 0}, {valor: 0}];
	$scope.transportePortoFabricaList = [{valor: 83}, {valor: 83}, {valor: 83}, {valor: 0}];
	$scope.marinhaMercanteList = [{valor: 0}, {valor: 0}, {valor: 0}, {valor: 0}];
	
	$scope.selecionarLogistica = function() {

	};
});