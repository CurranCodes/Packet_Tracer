//used to draw initial network
let chart, networkJson, routingTables, packet;
const routingTableDOM = document.getElementById('RoutingTable');
function draw() {
    const http = new XMLHttpRequest();

    http.onreadystatechange = function(){
        if(http.readyState === 4 && http.status === 200) {
            const JsonResponse = http.responseText;
            networkJson = JSON.parse(JsonResponse);
            // create and draw a chart from the loaded data
            chart = anychart.graph(networkJson);

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

function removeAllChildNodes(parent) {
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
}

function getRoutingTables(){
    const http = new XMLHttpRequest();

    http.onreadystatechange = function(){
        if(http.readyState === 4 && http.status === 200) {
            const JsonResponse = http.responseText;
            routingTables = JSON.parse(JsonResponse);
        }
    }


    const url = 'http://localhost:8080/getRoutingTables';
    http.open("GET", url);
    http.send();
}

function findTable(routerName){
    for(const key in routingTables){
        if (key.routerName === routerName){
            return key;
        }
    }
}

function displayRoutingTable(routerName){
    removeAllChildNodes(routingTableDOM);

    //creates the <tr> and <th> elements for the router name
        const tableHeaderRow = document.createElement('tr');
        const tableHeader = document.createElement("th");
        tableHeader.innerText = "Router Name: " + routerName;

    //appends the elements created
        tableHeaderRow.appendChild(tableHeader);
        routingTableDOM.appendChild(tableHeaderRow);

    // creates a 'keyRow' with data which lets the user know
    // what each data field is for and appends it to the table
        const keyRow = document.createElement('tr');
        keyRow.className = "keyRow";

        const destinationKey = document.createElement('td');
        destinationKey.innerHTML = "DESTINATION";
        const lineKey = document.createElement('td');
        lineKey.innerHTML = "LINE";
        const costKey = document.createElement('td');
        costKey.innerHTML = "COST(ms)";

        keyRow.appendChild(destinationKey);
        keyRow.appendChild(lineKey);
        keyRow.appendChild(costKey);


        routingTableDOM.appendChild(keyRow);

    // Dynamically populates the rest of the table with the correct routing table object fields that were
    // passed from backend

        //gets the object that corresponds to a certain routing table
        const rows = findTable(routerName).rows;

        //iterates through routing table object rows and creates corresponding <tr> elements to add to the
        //routing table in the DOM
        for (const key in rows){
            const currentDataRow = document.createElement('tr');

            const line = document.createElement('td');
            line.innerHTML = key.line;
            currentDataRow.appendChild(line);

            const cost = document.createElement('td');
            cost.innerHTML = key.cost;
            currentDataRow.appendChild(cost);

            const destination = document.createElement('td');
            destination.innerHTML = key.destination;
            currentDataRow.appendChild(destination);

            routingTableDOM.appendChild(currentDataRow);
        }
}

function buildPacket(sourceName, destinationName){
    packet = {source : sourceName, destination : destinationName};
}

//methodStub
function routePacket(sourceName, destinationName){
    buildPacket(sourceName, destinationName);
    var currentNode = sourceName;

    while (!(currentNode === destinationName)){

    }
}


anychart.onDocumentReady(draw());
getRoutingTables();
displayRoutingTable('R1')

//DO NOT TOUCH ABOVE THIS LINE LOL
