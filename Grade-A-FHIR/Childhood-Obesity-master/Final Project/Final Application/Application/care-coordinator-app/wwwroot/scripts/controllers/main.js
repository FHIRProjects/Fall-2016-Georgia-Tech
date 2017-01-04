'use strict';

/**
 * @ngdoc function
 * @name stuffApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the stuffApp
 */
angular.module('stuffApp')
    .controller('MainCtrl', ['$scope', 'data', '$routeParams', 'AngularShamNotification', function ($scope, data, $routeParams, angularShamNotification) {
        this.awesomeThings = [
            'HTML5 Boilerplate',
            'AngularJS',
            'Karma'
        ];

        $scope.counties = '';
        $scope.zipCodes = '';
        $scope.loadDates = false;
        // Set Date
        data.savedData.StartDate = new Date(data.savedData.StartDate);
        data.savedData.EndDate = new Date(data.savedData.EndDate);

        var defaultColDefs =
            [{name: 'ID', enableFiltering: true},
            {name: 'Weight', enableFiltering: true},
            {name: 'Race', enableFiltering: true},
            {name: 'Age', enableFiltering: true},
            {name: 'Height', enableFiltering: true},
            {name: 'Gender', enableFiltering: true},
            {name: 'BMI', enableFiltering: true},
            {name: 'Hypertension', enableFiltering: true},
            {name: 'Hemoglobin', enableFiltering: true}];

        var nameColDefs = [
            {name: 'FirstName', enableFiltering: true},
            {name: 'LastName', enableFiltering: true}
        ];

        console.log(data.savedData.StartDate);
        console.log(data.savedData.EndDate);

        setTimeout(function() {
            $scope.loadDates = true;
            $scope.$apply();
        },100);


        $scope.sliderAgeFilter = {
            options: {
                floor: 2,
                ceil: 50,
                showSelectionBar: true
            }
        };
        
        
        $scope.data = data.savedData;


        $scope.showMap = false;

        $scope.activeSeriesButtons = {
            hemoglobin: true,
            bmi: true,
            height: true,
            weight: true
        };

        $scope.activeEthnicityButtons = {
            Caucasian: true,
            Hispanic: true,
            Black: true
        };


        $scope.activeGenderButtons = {
            Male: true,
            Female: true
        };


        $scope.changeGender = function (type) {
            $scope.activeGenderButtons[type] = ($scope.activeGenderButtons[type]) ? false : true;
        };
        
        $scope.changeEthnicity = function (type) {
            $scope.activeEthnicityButtons[type] = ($scope.activeEthnicityButtons[type]) ? false : true;
        };


        $scope.changePieButtons = function (type) {
            applyPieChartData(type);
            if (type === 'Hypertension') {
                $scope.activePieButtons.NonHypertension = false;
            } else if (type === 'NonHypertension') {
                $scope.activePieButtons.Hypertension = false;
            }
            $scope.activePieButtons[type] = ($scope.activePieButtons[type]) ? false : true;
            applyPieChartData(type);
        };


        $scope.activeMapButtons = {
            hemoglobin: false,
            bmi: true
        };

        $scope.activePieButtons = {
            Hypertension: false,
            NonHypertension: true
        };

        $scope.activeGraphData = [];
        var tomorrow = new Date();
        var i = 0;

        function applyGraphData(){
            $scope.optionsRight = {
                "options": {
                    chart: {
                        zoomType: 'x'
                    },
                    rangeSelector: {
                        enabled: true
                    },
                    navigator: {
                        enabled: true
                    }
                },
                useHighStocks: true,
                "series": $scope.activeGraphData,
                "title": {
                    "text": "Historical Data"
                },
                "loading": false,
                "size": {}
            };
        }

        applyGraphData();

        function spliceOutData() {
            $scope.activeGraphData = [];
            var weight = [];
            var height = [];
            var bmi = [];
            var hemoglobin = [];
            var y = 0;
            var x = 0;

            for (; x < $scope.lineGraphData.length; x++) {
                y = 0;
                for (; y < $scope.lineGraphData[x].Data.length; y++) {
                    var dateObj = new Date($scope.lineGraphData[x].Data[y].Date);
                    var dataArray = [dateObj.getTime(), $scope.lineGraphData[x].Data[y].Value];
                    switch ($scope.lineGraphData[x].Name) {
                        case 'BMI':
                            bmi.push(dataArray);
                            break;
                        case 'Hemoglobin':
                            hemoglobin.push(dataArray);
                            break;
                        case 'Height':
                            height.push(dataArray);
                            break;
                        case 'Weight':
                            weight.push(dataArray);
                            break;
                    }
                }
            }
            $scope.activeGraphData = [{
                "id": "series-0",
                "data": bmi,
                "name": "BMI",
                "visible": $scope.activeSeriesButtons.bmi
            }, {
                "id": "series-1",
                "data": hemoglobin,
                "name": "Hemoglobin",
                "visible": $scope.activeSeriesButtons.hemoglobin
            },
            {
                "id": "series-2",
                "data": height,
                "name": "Height",
                "visible": $scope.activeSeriesButtons.height
            },
            {
                "id": "series-3",
                "data": weight,
                "name": "Weight",
                "visible": $scope.activeSeriesButtons.weight
            }
            ];
            console.log($scope.optionsRight);
            $scope.showGraph = true;
            applyGraphData();
            setTimeout(function () {
                $scope.optionsRight.series = $scope.activeGraphData;
                $scope.$apply();
            }, 500);
        }

        $scope.gridOptions = {
            enableSorting: true,
            enableFiltering: true,
            enableCellEditOnFocus: true,
            columnDefs: defaultColDefs
        };

        $scope.changeButton = function (type) {
            $scope.optionsRight.series = [];
            if (type === 'hemoglobin') {
                $scope.activeSeriesButtons.hemoglobin = ($scope.activeSeriesButtons.hemoglobin) ? false : true;
            } else if (type === 'bmi') {
                $scope.activeSeriesButtons.bmi = ($scope.activeSeriesButtons.bmi) ? false : true;
            } else if (type === 'height') {
                $scope.activeSeriesButtons.height = ($scope.activeSeriesButtons.height) ? false : true;
            } else if (type === 'weight') {
                $scope.activeSeriesButtons.weight = ($scope.activeSeriesButtons.weight) ? false : true;
            }
            spliceOutData();
        };

        $scope.changeMapButtons = function (type) {
            if (type === 'hemoglobin') {
                applyMapData('hemoglobin');
                $scope.activeMapButtons.hemoglobin = ($scope.activeMapButtons.hemoglobin) ? false : true;
                $scope.activeMapButtons.bmi = false;
            } else if (type === 'bmi') {
                applyMapData('bmi');
                $scope.activeMapButtons.bmi = ($scope.activeMapButtons.bmi) ? false : true;
                $scope.activeMapButtons.hemoglobin = false;
            }
        };

        $scope.gridOptions.data = [];
        $scope.dataCopy = angular.copy($scope.gridOptions.data);


        var initSlider = function () {

            $scope.dataCopy = angular.copy($scope.gridOptions.data);
            // var maxAge = Math.max.apply(Math,$scope.gridOptions.data.map(function(o){return o.age;}));
            // var minAge = Math.min.apply(Math,$scope.gridOptions.data.map(function(o){return o.age;}));
            var maxWeight = Math.max.apply(Math, $scope.gridOptions.data.map(function (o) {
                return o['Weight'];
            }));
            var minWeight = Math.min.apply(Math, $scope.gridOptions.data.map(function (o) {
                return o['Weight'];
            }));


            /*
             $scope.slider = {
             minValue: minAge,
             maxValue: maxAge,
             options: {
             floor: minAge - 10,
             ceil: maxAge + 10,
             showSelectionBar: true
             }
             };
             */

            $scope.sliderWeight = {
                minValue: minWeight,
                maxValue: maxWeight,
                options: {
                    floor: minWeight - 10,
                    ceil: maxWeight + 10,
                    showSelectionBar: true
                }
            };

        };

        function ageChange() {
            var x = 0,
                y = 0;
            $scope.tempData = [];

            /*
             for (; x < $scope.dataCopy.length;x++) {
             if ( $scope.dataCopy[x].age <= $scope.slider.maxValue && $scope.dataCopy[x].age >=  $scope.slider.minValue) {
             $scope.tempData.push($scope.dataCopy[x]);
             }
             }
             */
            console.log($scope.dataCopy);
            for (; y < $scope.dataCopy.length; y++) {
                if ($scope.dataCopy[y]['Weight'] <= $scope.sliderWeight.maxValue && $scope.dataCopy[y]['Weight'] >= $scope.sliderWeight.minValue) {
                    $scope.tempData.push($scope.dataCopy[y]);
                }
            }

            $scope.gridOptions.data = $scope.tempData;
        }

        /*
         $scope.$watch('slider.minValue', ageChange);
         $scope.$watch('slider.maxValue', ageChange);
         */
        $scope.$watch('sliderWeight.minValue', ageChange);
        $scope.$watch('sliderWeight.maxValue', ageChange);

        $scope.optionsLeft = {
            "options": {
                "chart": {
                    "type": "areaspline"
                },
                "plotOptions": {
                    "series": {
                        "stacking": ""
                    }
                }
            },
            "series": [{
                "name": "Weight",
                "data": [100, 200, 130, 100, 150],
                "id": "series-0"
            }, {
                "name": "BMI",
                "data": [20, 30, null, 19, 20],
                "connectNulls": true,
                "id": "series-1"
            }],
            "title": {
                "text": "Graphed Data"
            },
            "loading": false,
            "size": {}
        };

        $scope.mapData = [];

        $scope.mapConfig = {
            chartType: 'Map',
            title: {
                text: 'BMI'
            },
            options: {
                colorAxis: {},
                legend: {
                    layout: 'horizontal',
                    borderWidth: 0,
                    backgroundColor: 'rgba(255,255,255,0.85)',
                    floating: true,
                    verticalAlign: 'top',
                    y: 15
                },
                tooltip: {
                    pointFormat: '<b>State Level Data:</b><br>{point.name}: {point.value:.2f} <br><br><br> {point.counties} <br><br><br> {point.cdcData}'
                }
            },
            series: [{
                data: $scope.mapData,
                mapData: Highcharts.maps['countries/us/us-all'],
                joinBy: ['name']
            }]
        };

        $scope.download = function(type){
            getData(type);
        };

        $scope.pieConfig = {
            options: {
                chart: {
                    type:'pie'
                }
            },
            title: {
                text: 'BMI'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            series: [{
                name: 'Brands',
                colorByPoint: true,
                data: []
            }]
        };

        function toUpperCase(value) {
            return value.charAt(0).toUpperCase() + value.slice(1);
        }


        function loadMap(type) {
            $scope.mapConfig.title.text = toUpperCase(type);
            $scope.mapConfig.series[0].data = $scope.mapData;
            $scope.mapConfig.series[0].name = toUpperCase(type);

            console.log($scope.mapConfig);
        }

        function loadPie(type) {
           $scope.pieConfig.title.text = toUpperCase(type);
           $scope.pieConfig.series[0].data = $scope.pieData;
           $scope.pieConfig.series[0].name = toUpperCase(type);
           console.log($scope.pieConfig);
        }

        function getAllCountyData(counties) {
            var i = 0,
                countyString = '<br><b>County Level Data:</b><br>';
            for (; i < counties.length;i++){
                countyString += '<br>' + counties[i].Name + ': ' + '' + counties[i].Value.toFixed(2) + '';
            }
            return countyString;
        }

        function getAllCDCDATA(cdcValue) {
            if (cdcValue) {
                var i = 0,
                    cdcString = '<br><b>CDC Data:</b><br>';
                for (; i < cdcValue.length; i++) {
                    cdcString += '<br>' + cdcValue[i].Name + ': ' + '' + cdcValue[i].Value.toFixed(2) + '';
                }
            } else {
                cdcString = '<br><b>CDC Data:</b><br> N/A';
            }
            return cdcString;
        }


        function applyMapData(type) {
            var x = 0,
                y = 0;
            var features = {};
            features.bmi = [];
            features.hemoglobin = [];

            for (; x < $scope.allMapData.length; x++) {
                y = 0;
                for (; y < $scope.allMapData[x]['States'].length; y++) {
                    var sendArray = {
                        'value': $scope.allMapData[x]['States'][y]['Value'],
                        'name': $scope.allMapData[x]['States'][y]['Name'],
                        'counties': getAllCountyData($scope.allMapData[x]['States'][y]['Counties']),
                        'cdcData': getAllCDCDATA($scope.allMapData[x]['States'][y]['CDCValue'])
                    };
                    switch ($scope.allMapData[x]['MapType']) {
                        case 'BMI':
                            features.bmi.push(sendArray);
                            break;
                        case 'Hemoglobin':
                            features.hemoglobin.push(sendArray);
                            break;
                    }
                }
            }

            $scope.showMap = false;
            setTimeout(function () {
                $scope.mapData = features[type];
                loadMap(type);
                $scope.showMap = true;
                $scope.$apply();
            }, 1000);
        }


        function applyPieChartData(type) {
            var x = 0,
                y = 0,
                features = {},
                sendType = 'nonHyperTension';
            features.nonHyperTension = [];
            features.hypertension = [];

            if (type === 'NonHypertension') {
                type = 'Non-Hypertension';
            } else {
                sendType = 'hypertension';
            }

            for (; x < $scope.pieChartData.length; x++) {
                y = 0;
                for (; y < $scope.pieChartData[x]['DataPoints'].length; y++) {
                    var sendArray = {
                        'y': $scope.pieChartData[x]['DataPoints'][y]['Value'],
                        'name': $scope.pieChartData[x]['DataPoints'][y]['Name']
                    };
                    switch ($scope.pieChartData[x]['ChartType']) {
                        case 'Non-Hypertension':
                            features.nonHyperTension.push(sendArray);
                            break;
                        case 'Hypertension':

                            features.hypertension.push(sendArray);
                            break;
                    }
                }
            }
            $scope.showPie = false;
            setTimeout(function () {


                $scope.pieData = features[sendType];
                loadPie(type);
                $scope.showPie = true;
                $scope.$apply();
            }, 1000);
        }

        function initData(data) {
            $scope.gridOptions.columnDefs = [];
            if (data.Practitioner.ShowPatientNames) {
                $scope.gridOptions.columnDefs = nameColDefs.concat(defaultColDefs);
            } else {
                $scope.gridOptions.columnDefs = defaultColDefs;
            }
            
            $scope.gridOptions.data = data.GridData.rows;
            $scope.lineGraphData = data.LineCharts;
            $scope.allMapData = data.Maps;
            $scope.pieChartData = data.PieCharts;
            $scope.practitionerData = data.Practitioner;
            initSlider();
            applyMapData('bmi');
            spliceOutData();
            applyPieChartData('NonHypertension');
        }
        
        function getData(type) {
            $scope.data.Ethnicities = [];
            for (var x in $scope.activeEthnicityButtons) {
                if ($scope.activeEthnicityButtons.hasOwnProperty(x) && $scope.activeEthnicityButtons[x]) {
                    $scope.data.Ethnicities.push(x);
                }
            }

            $scope.data.Gender = [];
            for (var y in $scope.activeGenderButtons) {
                if ($scope.activeGenderButtons.hasOwnProperty(y) && $scope.activeGenderButtons[y]) {
                    $scope.data.Gender.push(y);
                }
            }
            $scope.data.Counties = [];
            $scope.counties = $scope.counties.replace(/\s+/g, " ");
            $scope.data.Counties = $scope.counties.split(',');
            $scope.data.ZipCodes = [];
            $scope.zipCodes = $scope.zipCodes.replace(/\s+/g, " ");
            $scope.data.ZipCodes = $scope.zipCodes.split(',');
            $scope.data.ProviderID = $routeParams.providerID;

            angularShamNotification.setDisabled(false);
            data.getData(type).then(function (data) {
                console.log('success');
                initData(data);
            }, function (data) {
                console.log(data);
                initData(data);
                console.log('error');
            });
        }
        
        $scope.updateUI = function(type) {
            getData(type);
        };

        $scope.open1 = function() {
            angularShamNotification.setDisabled(true);
            $scope.popup1.opened = true;
        };

        $scope.open2 = function() {
            angularShamNotification.setDisabled(true);
            $scope.popup2.opened = true;
        };

        $scope.formats = ['MM/dd/yyyy', 'shortDate'];
        $scope.format = $scope.formats[0];
        $scope.altInputFormats = ['M!/d!/yyyy'];

        $scope.dateOptions = {
            dateDisabled: false,
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.popup1 = {
            opened: false
        };

        $scope.popup2 = {
            opened: false
        };

        setTimeout(function(){
            getData();
        },2000);
    }]);


