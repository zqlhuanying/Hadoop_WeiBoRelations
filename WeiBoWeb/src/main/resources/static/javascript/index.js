/**
 * Created by root on 15-5-11.
 */
$(function(){

    $("#relations").submit(function(){
        $(this).ajaxSubmit({
            url : "showRelations/find",
            type : "post",
            beforeSubmit : valid,
            success : function(data){
                alert(data);
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