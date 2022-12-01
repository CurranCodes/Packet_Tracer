//used to display network
function refresh() {
    const http = new XMLHttpRequest();

    http.onreadystatechange = function(){
            if(http.readyState === 4 && http.status === 200) {
                // create a chart from the loaded data
                const JSONresponse = http.responseText;
                const networkJSON = JSON.parse(JSONresponse);
                var chart = anychart.graph(networkJSON);

                //enable labels of nodes
                chart.nodes().labels().enabled(true);
                chart.edges().labels().enabled(true);

                //configure labels of nodes
                chart.nodes().labels().format("{%id}");
                chart.nodes().labels().fontSize(12);
                chart.nodes().labels().fontWeight(600);
                chart.nodes().labels().fontColor("#00bfa5");

                //configures labels of edges
                chart.edges().labels().format("{%weight}");
                chart.edges().labels().fontSize(12);
                chart.edges().labels().fontWeight(600);

                //configure labels of nodes


                // draw the chart
                chart.container("container").draw();
            }
        }


        const url = 'http://localhost:8080/getNetworkGraph';
        http.open("GET", url);
        http.send();
}

anychart.onDocumentReady(refresh());

