$(document).ready(
    function(){
        $("#map_canvas").height( mapHeight());
        $("#mapJobList").height(mapListHeight() );

        $("#mapJobList > .row-fluid").bind("mouseover", function(){
            if(!$(this).hasClass("selected")){
                $(this).addClass("over");
            }
        });
        $("#mapJobList > .row-fluid").bind("mouseout", function(){
            if(!$(this).hasClass("selected")){
                $(this).removeClass("over");
            }
        });

        $("#mapJobList > .row-fluid").bind("click", function(){
           $("#mapJobList > .selected").removeClass("selected").removeClass("over");
           $(this).removeClass("over").addClass("selected");
        });
    }
);
$(window).resize(function() {
    $("#map_canvas").height( mapHeight());
    $("#mapJobList").height(mapListHeight() );
});

function mapHeight(){
    return ($(window).height() - 41)+"px";
}

function mapListHeight(){
    return ($(window).height() - 54)+"px";
}

var jobList = new Object();
var prevSelected = null;
var defaultImage = new google.maps.MarkerImage("http://maps.google.com/mapfiles/ms/icons/red-dot.png");

var selectedImage = new google.maps.MarkerImage("http://maps.google.com/mapfiles/ms/icons/green-dot.png");

var pinShadow = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_shadow",
    new google.maps.Size(40, 37),
    new google.maps.Point(0, 0),
    new google.maps.Point(12, 35));

var infowindow = new google.maps.InfoWindow({});

function jobObj(id, title, highlightedTitle, highlightedDescription, companyName, location, source, formattedDate, jobKey, jobUrl){
    this.id = id;
    this.title = title;
    this.highlightedTitle = highlightedTitle;
    this.highlightedDescription = highlightedDescription;
    this.companyName = companyName;
    this.location = location;
    this.source = source;
    this.formattedDate = formattedDate;
    this.markerList = new Array();
    this.jobKey = jobKey;
    this.jobUrl = jobUrl;


    this.addMarker = function(marker){
        this.markerList.push(marker);
    }

    this.firstMarker = function(){
          return this.markerList.length > 0 ? this.markerList[0] : null;
    }

    this.setMarkersDefault = function(){
        for(var i = 0; i < this.markerList.length; i++){
            this.markerList[i].setIcon(defaultImage);
        }
    }

    this.setMarkersSelected = function(){
        for(var i = 0; i < this.markerList.length; i++){
            this.markerList[i].setIcon(selectedImage);
        }
        if(this.markerList.length > 0){
            var marker = this.markerList[0];
            var map = marker.getMap();
            if(!map.getBounds().contains(marker.getPosition())){
                map.setCenter(marker.getPosition());
            }
        }
    }

    this.openInfoWindow = function(marker){
        infowindow.close();
        var template = $("#mapJobTemplate").html();
        var data = this.jsonData();
        var rendered = Mustache.to_html(template,this.jsonData());
        infowindow = new google.maps.InfoWindow({content: rendered});
        marker = marker == undefined ? this.firstMarker() : marker;
        if(marker != null){
            infowindow.open(map,marker);
        }

    }

    this.jsonData = function(){
        return {
            jobKey:this.jobKey,
            title:this.highlightedTitle,
            location:this.location,
            companyName:this.companyName,
            description:this.highlightedDescription,
            date:this.formattedDate,
            source:this.source,
            jobUrl:this.jobUrl
        };
    }

}

function jobDetail(jobId){
    var job = jobList[jobId];
    if(prevSelected != null){
        prevSelected.setMarkersDefault();
    }
    if(job != null){
        job.setMarkersSelected();
        prevSelected = job;
    }

    job.openInfoWindow();
}

