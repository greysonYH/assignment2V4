<#-- @ftlvariable name="message" type="java.lang.String" -->


<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Fly me to Mars - a mission registration system.</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Fly me to Mars - a mission registration system.">
</head>

<body>
<div>
    <#if message??>
        <span><h3>${message}</h3></span>
    <#else>
        <span><h3>Welcome to Mars Mission!</h3></span>
    </#if>
</div>

</body>
</html>