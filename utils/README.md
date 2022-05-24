# encryption

## Encoder(암호화)

[암호화 사이트](https://coding.tools/kr/sha256)

[소스 코드](https://github.com/mike-urssu/sample-codes/blob/develop/utils/src/encryption/Encoder.java)

### 설명

`MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512` 방식을 이용하여 Text를 암호화한다.

# zip

## 사용한 library

- commons-compress-1.21.jar
    - 압축 관련 메소드 사용
- xz-1.9.jar
    - 7z 압축 파일을 푸는데 사용
- commons-io-2.11.0.jar
    - FileUtils, FilenameUtils 사용

## Zip(폴더 압축하기)

[소스 코드](https://github.com/mike-urssu/sample-codes/blob/main/utils/src/zip/Zip.java)

### 설명

directory 안에 있는 모든 파일을 zipFile로 압축한다.

## Unzip(압축파일 풀기)

[소스 코드](https://github.com/mike-urssu/sample-codes/blob/main/utils/src/zip/Unzip.java)

### 설명

zipFile을 directory 경로에 푼다.

`zip, tar`의 경우 **ArchiveInputStream를 상속받기** 때문에 unzipZipOrTar()로 묶어서 처리한다.

`ArchiveStreamFactory`에서 지원하는 `ar, arj, cpio, dump, jar`는 **ArchiveInputStream을 상속받기** 때문에 unzipZipOrTar() 메소드의 getArchiveName()에 확장자를 추가하여 공통으로 사용할 수 있다.

반면 `7z`의 경우 **ArchiveInputStream을 상속받지 않고** `SevenZFile`를 사용하므로 `unzip7z()` 메소드로 분리해서 사용한다.
