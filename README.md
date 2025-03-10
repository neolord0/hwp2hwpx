# hwp2hwpx

이 라이브러리는 한글과 컴퓨터(한컴)에서 만든 워드프로세서 "한글"의 hwp 파일을 hwpx 파일로 변환하는 java 라이브러리 입니다.<br>

개인적인 취미 생활 또는 사회기여 활동 목적으로 시작한 hwplib, hwpxlib 프로젝트가 이 라이브러리를 기반으로 하는 상용 제품이 개발하여 판매하고 있습니다.
이 라이브러리의 저작권은 저 개인에게 있으므로, 라이브러리 사용, 버그 수정요청, 약간의 질문 등은 Apache-2.0 license에 의해 앞으로도 자유롭게 할 수 있습니다.
그 외에 많은 시간을 초래할 수 있는 기술지원 요청이나 유지보수 계약등은 제 메일로 상의해 주셨으면 합니다. <br>

* 사용한 라이브러리나 문서
    - 한글과컴퓨터에서 공개한  '한글 문서 파일 구조 5.0' 문서 ( http://www.hancom.com/etc/hwpDownload.do?gnb0=269&gnb1=271&gnb0=101&gnb1=140 ) <br>
      ( “본 제품은 한글과컴퓨터의 HWP 문서 파일(.hwp) 공개 문서를 참고하여 개발하였습니다." )
    - 한글과컴퓨터에서 공개한 'OWPML' 문서 ( http://www.hancom.com/etc/hwpDownload.do?gnb0=269&gnb1=271&gnb0=101&gnb1=140 ) <br>
      ( “본 제품은 한글과컴퓨터의 HWP 문서 파일(.hwp) 공개 문서를 참고하여 개발하였습니다." )
    - kr.dogfoot.hwplib 1.1.4 이상의 버전
    - kr.dogfoot.hwpxlib 1.0.1 이상의 버전

* 사용법
```java
        HWPFile fromFile = HWPReader.fromFile(InputFilePath);
        HWPXFile toFile = Hwp2Hwpx.toHWPX(fromFile);
        HWPXWriter.toFilepath(toFile, OutputFilePath);
```

* hwp 파일에 대한 라이브러리는 https://github.com/neolord0/hwplib 을 참조해 주세요.
* hwpx 파일에 대한 라이브러리는 https://github.com/neolord0/hwpxlib 을 참조해 주세요.

2025.03.10
=========================================================================================
* 이슈 1 : 포함된 이미지파일의 이름을 생성하는 루틴 변경.
* hwpxlib 1.0.5 버전으로 변경.

