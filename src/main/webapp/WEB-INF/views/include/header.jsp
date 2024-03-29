<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- header -->
<header>
    <div class="inner-header">
        <h1 class="logo">
            <a href="#">
                <img src="/img/logo.png" alt="로고이미지">
            </a>
        </h1>
        <h2 class="intro-text">Welcome
            <!-- MemberService 62줄 -->
            <c:if test="${loginUser != null}">
                ${loginUser.name}님 Hello!!
            </c:if>
        </h2>
        <a href="#" class="menu-open">
            <span class="menu-txt">MENU</span>
            <span class="lnr lnr-menu"></span>
        </a>
    </div>

    <nav class="gnb">
        <a href="#" class="close">
            <span class="lnr lnr-cross"></span>
        </a>
        <ul>
            <li><a href="/">Home</a></li>
            <li><a href="#">About</a></li>
            <li><a href="/board/list">Board</a></li>
            <li><a href="#">Contact</a></li>
            <!-- MemberService 62줄에서 옴 -->
            <c:if test="${loginUser == null}">
                <li><a href="/member/sign-up">SignUp</a></li>
                <li><a href="/member/sign-in">SignIn</a></li>
            </c:if>

            <c:if test="${loginUser != null}">
                <li><a href="#">My Page</a></li>
                <li><a href="/member/sign-out">Sign Out</a></li>
            </c:if>

        </ul>
    </nav>

</header>
<!-- //header -->