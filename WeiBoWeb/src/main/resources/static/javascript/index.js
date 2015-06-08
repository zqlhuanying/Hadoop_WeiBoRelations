/**
 * Created by root on 15-5-11.
 */
$(function(){

    $("#relations").submit(function(){
        $(this).ajaxSubmit({
            url : "showRelations/find",
            type : "post",
            data : {
                name : $("#name").val(),
                isByMR : $("#isByMR").val()
            },
            dataType : "json",
            beforeSubmit : valid,
            success : function(data){
                if(data != null || data != "") paint(data);
            }
        });
        return false;
    });
});

function valid(){
    var name = $("#name").val();

    if(!name || name == ""){
        alert("请输入待查找的人");
        return false;
    }
    return true;
}

function paint(dataset){
    var width = 1000;
    var height = 1000;

    // 可以理解为设置为600*600大小的画布
    var svg = d3.select("body").append("svg")
                    .attr("width", width)
                    .attr("height", height);

    /** 原始的数据不适合画力学图force，通过d3的layout.force()函数对原始数据进行转化，
     *  可以通过console.log(dataset)查看转化后的格式。但注意console.log(dataset)和
     *  将参数d直接输出的结果是不同的，原因不明。即通过查看转化后的数据是否一致的情况并
     *  不能说明程序运行是否出错。画力学图时，tick函数必须有，否则程序无法正确运行。
     */
    var force = d3.layout.force()
                    .nodes(dataset.nodes)
                    .links(dataset.edges)
                    .size([width, height])
                    .linkDistance([50])
                    .charge([-100])
                    .start();

    var colors = d3.scale.category10();

// 开始绘图
// 将节点画成一个个Bubble
    var nodes = svg.selectAll("circle")
                        .data(dataset.nodes)
                        .enter()
                        .append("circle")
                        .attr("r", 10)
                        .style("fill", function(d){ return colors(d.name); })
                        .call(force.drag);

// 节点之间的连线
    var edges = svg.selectAll("line")
                        .data(dataset.edges)
                        .enter()
                        .append("line")
                        .style("stroke", "#ccc")
                        .style("stroke-width", 2);

    force.on("tick", function(){
        edges.attr("x1", function(d){ return d.source.x; })
            .attr("y1", function(d){ return d.source.y })
            .attr("x2", function(d){ return d.target.x })
            .attr("y2", function(d){ return d.target.y });

        nodes.attr("cx", function(d){ return d.x })
            .attr("cy", function(d){ return d.y });
    });
}