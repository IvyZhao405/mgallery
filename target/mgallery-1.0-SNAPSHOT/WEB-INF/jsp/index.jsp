<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
	<link rel="stylesheet" type="text/css" href="css\common.css">
	<script type="text/javascript" src="js\js1.js"></script>
</head>
<body>
	<c:if test="${param.c != null}">
		<c:set var="categoryParam" value="&c=${param.c}"></c:set>
	</c:if>
	<c:if test="${param.c == null}">
		<c:set var="categoryParam" value=""></c:set>
	</c:if>
	<div class="header">
		<div class="logo">
			<img src="image/logo.png">
		</div>
		<div class="menu"   onclick="show_menu()" onmouseleave="show_menu1()">
			 <div class="menu_title" ><a href="###">Category</a></div>
			 <ul id="title">
				<li><a href="page?c=1">Realism</a></li>
				<li><a href="page?c=2">Abstract</a></li>
			 </ul>
		</div>
		<div class="auth">
			<ul>
				<li><a href="#">Sign in</a></li>
				<li><a href="#">Register</a></li>
			</ul>
		</div>
	</div>
	<div class="content">
	  <div class="banner">
  		<img src="image/welcome.png" class="banner-img">
	  </div>
	  <div class="img-content">
		<ul>
			<c:forEach items="${pageModel.pageData}" var="painting">
			<li>
				<img src="${painting.preview}" class="img-li">
				<div class="info">
					<h3>${painting.pname}</h3>
					<p>
						${painting.description}
					</p>
					<div class="img-btn">
						<div class="price"><fmt:formatNumber pattern="$0.00" value="${painting.price}"></fmt:formatNumber></div>
							<a href="#" class="cart">
						       <div class="btn">
							      <img src="image/cart.svg">
						       </div>
						    </a>
					</div>
				</div>
			</li>
			</c:forEach>
		</ul>
	  </div>
	  <div class="page-nav">
		<ul>
			<li><a href="page?p=1${categoryParam}">Home</a></li>
			<li><a href="page?p=${pageModel.hasPreviousPage?pageModel.page-1:1}${categoryParam}">Prev</a></li>
			<c:forEach begin="1" end="${pageModel.totalPages}" var="pno" step="1">
				<li><span ${pno==pageModel.page?"class='first-page'":""}>
					<!-- c doesn't exist, href="page?p=1" -->
					<!-- c exists, href="page?p=1&c=1"-->
					<a href="page?p=${pno}${categoryParam}">
						${pno}
					</a>
				</span></li>
			</c:forEach>
			<li><a href="page?p=${pageModel.hasNextPage?pageModel.page+1:pageModel.totalPages}${categoryParam}">Next</a></li>
			<li><a href="page?p=${pageModel.totalPages}${categoryParam}">Last</a></li>
		</ul>
	  </div>
	</div>
	<div class="footer">
		<p><span>M-GALLARY</span>©2021 POWERED BY IMOOC.INC</p>
	</div>
</body>
</html>