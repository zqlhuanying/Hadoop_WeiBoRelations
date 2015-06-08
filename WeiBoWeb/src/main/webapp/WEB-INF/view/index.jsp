<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WeiBoWeb</title>
    <meta charset="UTF-8">
    <script type="text/javascript" src="static/js/d3.js" charset="UTF-8"></script>
    <script type="text/javascript" src="static/js/jquery-2.1.4.js" charset="UTF-8"></script>
    <script type="text/javascript" src="static/js/jquery.form.js" charset="UTF-8"></script>
</head>
<body>


</body>
<form id="relations" method="post">
    <input type="text" name="name" id="name" />
    <input type="submit" value="SearchByAPI" onclick="check(false);"/>
    <input type="submit" value="SearchByMR" onclick="check(true);"/>
    <input type="hidden" name="isByMR" id="isByMR"/>
</form>
<script type="text/javascript" src="static/javascript/index.js" charset="UTF-8"></script>
<script type="text/javascript" charset="UTF-8">
    function check(isByMR){
        $("#isByMR").val(isByMR);
    }
</script>
</html>
