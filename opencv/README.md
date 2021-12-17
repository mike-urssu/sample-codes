# OpenCV

OpenCV를 이용하여 이미지/동영상 편집하는 기능

OpenCV 다운로드 위치: https://github.com/opencv/opencv/releases/tag/3.4.15

*※ 주의사항 ※*

OpenCV 기능을 수행할 때 **한글이 포함되면 안된다.**

한글 이슈를 해결하기 위해 SHA256으로 암호화하여 OpenCV 기능을 수행하고 다시 원본명으로 수정한다.

## Mosaic

https://github.com/mike-urssu/sample-codes/blob/develop/opencv/src/opencv/Mosaic.java

주어진 jsonData으로부터 영역을 추출하여 **이미지를 Blur 처리**한다.

### 실행 결과

blur 전 이미지
![before](https://user-images.githubusercontent.com/69888508/145524273-1ef6e81e-3ef3-464e-9709-f95474666c9e.jpg)

blur 후 이미지
![after](https://user-images.githubusercontent.com/69888508/145524316-c0d50471-781b-4787-af42-515835a3ea23.jpg)

## FrameExtractor

https://github.com/mike-urssu/sample-codes/blob/develop/opencv/src/opencv/FrameExtractor.java

동영상으로부터 Frame을 추출한다.

# FFMPEG

FFMPEG 다운로드 위치: https://ffmpeg.org/download.html#build-windows

FFMPEG 옵션: https://hamelot.io/visualization/using-ffmpeg-to-convert-a-set-of-images-into-a-video/

*※ 주의사항 ※*

FFMPEG 기능을 수행할 때 **한글이 포함되면 안된다.**

한글 이슈를 해결하기 위해 SHA256으로 암호화하여 FFMPEG 기능을 수행하고 다시 원본명으로 수정한다.

## FrameMerger

https://github.com/mike-urssu/sample-codes/blob/develop/opencv/src/ffmpeg/FrameMerger.java

특정 경로에 있는 frame 파일을 합쳐 새로운 동영상으로 생성한다.
