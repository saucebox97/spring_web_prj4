
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title></title>

<!-- jquery -->
<script src="/js/jquery-3.3.1.min.js"></script>

<style>

    .fileDrop {
        width: 800px;
        height: 400px;
        border: 1px dashed gray;
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 1.5em;
    }
    .uploaded-list {
        display: flex;
    }
    .img-sizing {
        display: block;
        width: 100px;
        height: 100px;
    }

</style>

</head>
<body>

    <!-- 동기 처리 -->
    <!-- 파일 업로드를 위한 form / name은 내가정하는거 / enctype을 해야 보낼수있음 / 내가보내고있는것이 파일이다-->
    <!-- 44줄 multiple 이있어야 한번에 여러개올릴수있음 -->
    <form action="/upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file" multiple>
        <button type="submit">업로드</button>
    </form>

    <!-- 비동기 통신을 위한 실시간 파일 업로드 처리 / 시작-->
    <div class = "fileDrop">
        <span>DROP HERE!!</span>
    </div>

    <!-- 
        - 파일 정보를 서버로 보내기 위해서는 <input type="file"> 이 필요
        - 해당 input태그는 사용자에게 보여주어 파일을 직접 선택하게 할 것이냐
          혹은 드래그앤 드롭으로만 처리를 할 것이냐에 따라 display를 상태를 결정
     -->
    <div class="uploadDiv"> 
        <input type="file" name="files" id="ajax-file" style="display:none;">
    </div>

    <!-- 업로드된 이미지의 썸네일을 보여주는 영역 -->
    <div class="uploaded-list">
        
    </div>

    <script>

        // start JQuery 
        $(document).ready(function () {

            function isImageFile(originFileName) {
                //정규표현식 / $는 머머로 끝나는 / 저거 3개로끝나면 true / i는 대소문자구분 x 대문자 JPG등 도 걸림
                const pattern = /jpg$|gif$|png$/i;
                return originFileName.match(pattern);
            }

            // 파일의 확장자에 따른 렌더링 처리
            function checkExtType(fileName) {

                // 원본 파일 명 추출
                let originFileName = fileName.substring(fileName.indexOf("_" + 1));

                // 확장자 추출후 이미지인지까지 확인
                if (isImageFile(originFileName)) { // 파일의 이미지라면
                    const $img = document.createElement('img');
                    $img.classList.add('img-sizing');
                    $img.setAttribute('src', '/loadFile?fileName=' + fileName); // 풀경로 컨트롤러/98줄
                    $img.setAttribute('alt', originFileName); // 풀경로에서 원본파일이름만뽑음
                    $('.uploaded-list').append($img);
                }
                // 이미지가 아니라면 다운로드 링크를 생성
                else {

                    const $a = document.createElement('a');
                    $a.setAttribute('href', '/loadFile?fileName=' + fileName) // loadFile하면 byte배열보내줌

                    const $img = document.createElement('img');
                    $img.classList.add('img-sizing');
                    $img.setAttribute('src', '/img/file_icon.jpg'); 
                    $img.setAttribute('alt', originFileName); 

                    $a.append($img);
                    $a.innerHTML += '<span>' + originFileName + '</span>';

                    $('.uploaded-list').append($a);

                }

            }

            // 드롭한 파일
            function showFileData(fileNames) {

                // 이미지인지? 이미지가 아닌지에 따라 구분하여 처리
                // 이미지면 썸네일을 렌덜이하고 아니면 다운로드 링크를 렌더링한다.
                for (let fileName of fileNames) {
                    checkExtType(fileName);
                }
            }

            // drag & drop 이벤트
            const $dropBox = $('.fileDrop');

            // drag 진입 이벤트
            $dropBox.on('dragover dragenter', e => {
                e.preventDefault(); // 기본기능방지
                $dropBox
                    .css('border-color', 'red')
                    .css('background', 'lightgray');
            });

            // drag 탈출 이벤트
            $dropBox.on('dragleave', e => {
                e.preventDefault(); 
                $dropBox
                    .css('border-color', 'gray')
                    .css('background', 'transparent');
            });

            // drop 이벤트
            $dropBox.on('drop', e => {
                e.preventDefault(); // 이게있어야 안에넣엇을때 탭이안넘어감
                // console.log('드롭 이벤트 작동!');

                // 드롭된 파일 정보를 서버로 전송

                // 1. 드롭된 파일 데이터 읽기
                // console.log(e);

                const files = e.originalEvent.dataTransfer.files;
                // console.log('drop file data: ', files);

                // 2. 읽은 파일 데이터를 input[type-file]태그에 저장 / 59줄
                const $fileInput = $('#ajax-file'); 
                $fileInput.prop('files', files); // input에 파일정보를 담는다 /form태그라생각

                // console.log($fileInput);

                // 3. 파일 데이터를 비동기 전송하기 위해서는 FormData객체가 필요
                const formData = new FormData();

                 // 4. 전송할 파일들을 전부 FormData안에 포장
                 for (let file of $fileInput[0].files) {
                    formData.append('files', file); 
                }

                // 5. 비동기 요청 전송
                const reqInfo = {
                    method: 'POST',
                    body: formData
                    // headers: { / form-data는 기본값이라서 굳이안써도됌
                    //     'content-type': 'multipart/form-data'
                    // }
                }
                fetch('/ajax-upload', reqInfo) //controller 76줄에 감 reqInfo안에있는 formData안에있는 file이
                    .then(res => {
                        // console.log(res.status);
                        return res.json();
                    })
                    .then(fileNames => { // 컨트롤러에서 파일보냄 res.json()가 fileNames
                        console.log(fileNames);

                        showFileData(fileNames);
                    });

            });


        });
        // end jQuery

    </script>

</body>
</html>