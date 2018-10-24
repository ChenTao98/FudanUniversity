/**
 * Created by Bing Chen on 2017/7/22.
 */
$(document).ready(function () {
    $("#navSearchBtn").click(function () {
        //判断是否输入内容
        var searchString=document.getElementById("searchString").value;
        if(searchString===""){
            //未输入内容给出警告
            alert("请输入搜索内容");
        }else{
            //输入内容，提交表单
            document.getElementById("navSearchForm").submit();
        }
    });
});