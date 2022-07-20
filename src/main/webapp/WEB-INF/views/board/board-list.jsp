<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">

<head>
    <%@ include file="../include/static-head.jsp" %>

    <style>
        .board-list {
            width: 70%;
            margin: 230px auto 0;
        }

        .board-list .articles {
            margin: 10px auto 100px;
            border-collapse: collapse;
            font-size: 1.5em;
            border-radius: 10px;
        }

        .board-list .amount {
            display: flex;
            /* background: skyblue; */
            /* 오른쪽 끝으로 보냄 */
            justify-content: flex-end; 
            list-style: none;
        }

        .board-list .amount li {
            width: 8%;
            margin-right: 10px;
        }

        .board-list .amount li a {
            width: 100%;
        }

        header {
            background: #222;
            border-bottom: 1px solid #2c2c2c;
        }

         /* pagination style */
        /*나중에 설정한 값이 적용되지 않게 하려면 속성값 뒤에 !important를 붙입니다.  */
        .bottom-section {
            margin-top: -50px;
            display: flex;
        }
        /* 비율 9 */
        .bottom-section .nav {
            flex: 9; 
            display: flex;
            justify-content: center;
        }
        /* 비율 1 */
        .bottom-section .btn-write {
            flex: 1;
        }

        .pagination-custom a {
            color: #444 !important;
        }

        .pagination-custom li.active a 
        , .pagination-custom li:hover a{
            background: #333 !important;
            color: #fff !important;
        }
        


    </style>
</head>

<body>

    <div class="wrap">

        <%@ include file="../include/header.jsp" %>

        <div class="board-list">

            <div class="amount"> 
                <li><a class="btn btn-danger" href="/board/list?amount=10">10</a></li>
                <li><a class="btn btn-danger" href="/board/list?amount=20">20</a></li>
                <li><a class="btn btn-danger" href="/board/list?amount=30">30</a></li>
            </div>

            <table class="table table-dark table-striped table-hover articles">
                <tr>
                    <th>번호</th>
                    <th>작성자</th>
                    <th>제목</th>
                    <th>조회수</th>
                    <th>작성시간</th>
                </tr>
<!-- td중 멀눌러도 tr가고 첫째자식불러들임 -->
<!-- title 속성은 요소에 마우스를 포커스했을대의 설명을 지정 -->
                <c:forEach var="b" items="${bList}">
                    <tr>
                        <td>${b.boardNo}</td>
                        <td>${b.writer}</td>
                        <td title="${b.title}">${b.shortTitle}</td>
                        <td>${b.viewCnt}</td>
                        <td>${b.prettierDate}</td>
                    </tr>
                </c:forEach>
            </table>

            <!-- 게시글 목록 하단 영역 -->
            <div class="bottom-section">

                <!-- 페이지 버튼 영역 -->
                <nav aria-label="Page navigation example">
                    <ul class="pagination pagination-lg pagination-custom">

                        <c:if test="${pm.prev}">
                            <li class="page-item"><a class="page-link"
                                    href="/board/list?pageNum=${pm.beginPage - 1}&amount=${pm.page.amount}">prev</a></li>
                        </c:if>
                            <!-- li에 붙여야 다른숫자붙여도 appendPage할수있음 -->
                        <c:forEach var="n" begin="${pm.beginPage}" end="${pm.endPage}" step="1">
                            <li data-page-num="${n}" class="page-item">
                                <a class="page-link" href="/board/list?pageNum=${n}&amount=${pm.page.amount}">${n}</a>
                            </li>
                        </c:forEach>

                        <c:if test="${pm.next}">
                            <li class="page-item"><a class="page-link"
                                    href="/board/list?pageNum=${pm.endPage + 1}&amount=${pm.page.amount}">next</a></li>
                        </c:if>

                    </ul>
                </nav>


                <!-- 글쓰기 버튼 영역 -->
                <div class="btn-write">
                    <a class="btn btn-outline-danger btn-lg" href="/board/write">글쓰기</a>
                </div>
            </div>
        </div>


        <%@ include file="../include/footer.jsp" %>

    </div>
<!-- td중 멀눌러도 tr가고 첫째자식불러들임 -->
    <script>

        function alertServerMessage() {
            const msg = '${msg}';
            console.log('msg: ', msg);

            if (msg === 'reg-success') {
                alert('게시물이 정상 등록되었습니다.');
            }
        }
        
        function detailEvent() {

            //상세보기 요청 이벤트
            const $table = document.querySelector(".articles");
            $table.addEventListener('click', e => {
                if (!e.target.matches('.articles td')) return;

                console.log('tr 클릭됨! - ', e.target);
                //  td중 멀눌러도 tr가고 첫째자식불러들임 /부모에게갓다가 첫쨰에게 
                let bn = e.target.parentElement.firstElementChild.textContent;
                console.log('글번호: ' + bn);

                location.href = '/board/content/' + bn 
                                + "?pageNum=${pm.page.pageNum}"
                                + "&amount=${pm.page.amount}";
            });

        }

        //현재 위치한 페이지에 active 스타일 부여하기
        function appendPageActive() {

            // 현재 내가 보고 있는 페이지 넘버
            const curPageNum = '${pm.page.pageNum}';
            // console.log("현재페이지: ", curPageNum);

            // 페이지 li태그들을 전부 확인해서 
            // 현재 위치한 페이지 넘버와 텍스트컨텐츠가 일치하는
            // li를 찾아서 class active 부여
            const $ul = document.querySelector('.pagination');

            for (let $li of [...$ul.children]) {
                if (curPageNum === $li.dataset.pageNum) {
                    $li.classList.add('active');
                    break;
                }
            }

        }


        (function () {

            alertServerMessage();
            detailEvent();
            appendPageActive();

        })();


        

    </script>

</body>

</html>