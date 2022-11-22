//passes a string encoding of a json file to the server

function updateJson(fileName){
    const xmlHttp = new XMLHttpRequest();

    xmlHttp.onreadystatechange = function() {
        if (this.readyState != 4) return;

        if (this.status == 200) {
            const fileContent = JSON.parse(this.responseText);
            const fs = require('fs');
            const file = require(fileName);

            file.key = "new value";

            fs.writeFile(fileName, JSON.stringify(file), function writeJSON(err) {
                if (err) return console.log(err);
                console.log(fileContent);
            });
        }
    }

    var jsonEncoding = JSON.stringify(fileName);

    var args = "FileName=" + fileName + "&Encoding=" + jsonEncoding;

    xmlHttp.open( "GET", "http://localhost:8080/get-json?" + args, true);
    xmlHttp.send();

}



//used to display network
const createDeviceButton = document.getElementById("createDeviceButton");

function refresh() {
    anychart.data.loadJsonFile("dataset.json", function (data) {
        // create a chart from the loaded data
        var chart = anychart.graph(data);

        // set the title
        chart.title("Network Graph");

        //enable labels of nodes
        chart.nodes().labels().enabled(true);
        chart.edges().labels().enabled(true);

        //configure labels of nodes
        chart.nodes().labels().format("{%id}");
        chart.nodes().labels().fontSize(12);
        chart.nodes().labels().fontWeight(600);

        //configures labels of edges
        chart.edges().labels().format("{%weight}");
        chart.edges().labels().fontSize(12);
        chart.edges().labels().fontWeight(600);

        //configure labels of nodes
        chart.group("router").labels().fontColor("#00bfa5");
        // draw the chart
        chart.container("container").draw();
    })
}

refresh();
