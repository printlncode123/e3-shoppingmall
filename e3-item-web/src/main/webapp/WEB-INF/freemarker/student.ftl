<html>
	<head>
		<title>学生信息</title>
	</head>
	<body>
		学号:${student.sno}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		姓名:${student.name}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		年龄:${student.age}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		性别:${student.genda}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br><br>
		<table border="1">
			<h3>学生列表</h3>
			<th>序号</th>
			<th>学号</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>性别</th>
			<#list stuList as stu>
				<#if stu_index%2==0>
				<tr bgcolor="red">
				<#else>
				<tr bgcolor="green">
				</#if>
					<td>${stu_index}</td>					
					<td>${stu.sno}</td>
					<td>${stu.name}</td>
					<td>${stu.age}</td>
					<td>${stu.genda}</td>
				</tr>
			
			</#list>
		</table>
		<br>
		当前日期：${date?date}<br>
		当前时间:${date?time}<br>
		当前时间点:${date?datetime}<br>
		指定格式的当前时间点:${date?string("yyyy/MM/dd hh:mm:ss")}<br>
		空值的处理:${val!"空"}<br>
		空值的判断:<#if val??>
					值不为空，有内容!!!
					<#else>
					值为空
				</#if>
		<br>
		pk空值的处理:${pk!"空值"}<br>
		pk判空:<#if pk??>
		        ${pk}
				<#else>
				此处为空
			  </#if><br>
		引用模板测试:<#include "hello.ftl">
	</body>
</html>