# encryption

## Encoder

SHA 256 암호화 사이트: https://coding.tools/kr/sha256

코드: https://github.com/mike-urssu/sample-codes/blob/develop/utils/src/encryption/Encoder.java

### 설명

Text를 **입력한 암호화 방식**을 이용하여 암호화한다.

사용가능한 암호화 방식: MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512

암호화된 text를 **대문자로 출력할지 소문자로 출력할지 설정**할 수 있다.

### main 실행 결과

text: `1234`

algorithms: `SHA-256`

encrypted text: `3ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4`

# zip

## Unzip

### 설명

`zipFile`을 `directory` 경로에 푼다.

`zip`과 `tar`의 경우 `ArchiveInputStream`를 상속받기 때문에 `unzipZipOrTar()`로 묶어서 처리했다.

`ArchiveStreamFactory`에서 지원하는 타입은 `ar`, `arj`, `cpio`, `dump`, `jar`가 있는데 모두 `ArchiveInputStream`를 상속받기 때문에 `unzipZipOrTar()` 메소드의 **`getArchiveName()`에 확장자를 추가하여 공통으로 사용**할 수 있다.

반면 `7z`의 경우 `ArchiveInputStream`을 상속받지 않고 `SevenZFile`를 사용하므로 `unzip7z()` 메소드로 분리했다.
