# [translationCompanyPM](https://translationcompany.p-e.kr)

단일 엑셀 파일로 관리되던 프로젝트를 웹 애플리케이션으로 전환하여, 프로젝트 공유와 관리에 필요한 커뮤니케이션 비용을 줄이고 생산성을 높이기 위해 개발한 개인 프로젝트입니다.

## 📌주요 기능
- [x] 거래 회사 등록 및 수정
- [x] 관리자 등록 및 수정
- [x] 번역가 등록 및 수정
- [x] 프로젝트 관리
- [x] 엑셀 양식 다운로드
- [x] 엑셀 업로드

## 시스템 아키텍처
![image](https://github.com/user-attachments/assets/bffe6567-60ab-462d-a758-b2c5958eaf37)

## 기술 스택
<img src="https://img.shields.io/badge/html5-%23E34F26.svg?&style=for-the-badge&logo=html5&logoColor=white" />
<img src="https://img.shields.io/badge/bootstrap-%237952B3.svg?&style=for-the-badge&logo=bootstrap&logoColor=white" />
<img src="https://img.shields.io/badge/java-%23007396.svg?&style=for-the-badge&logo=java&logoColor=white" />
<img src="https://img.shields.io/badge/github%20actions-%232088FF.svg?&style=for-the-badge&logo=github%20actions&logoColor=white" />
<img src="https://img.shields.io/badge/thymeleaf-%23005F0F.svg?&style=for-the-badge&logo=thymeleaf&logoColor=white" />
<img src="https://img.shields.io/badge/supabase-%233ECF8E.svg?&style=for-the-badge&logo=supabase&logoColor=white" />
<img src="https://img.shields.io/badge/redis-%23DC382D.svg?&style=for-the-badge&logo=redis&logoColor=white" />

## 설계
* [ERD](https://dbdiagram.io/d/translation-company-669e22968b4bb5230efb3a72)
* 모놀리식 아키텍처
* [notion 정리](https://www.notion.so/TranslationCompanyPM-12f12cace575803b8a5cd0a4ba3919ca?pvs=4)

## Local에서 실행 방법

1. 명칭에 맞는 환경변수를 셋팅해줍니다. 또는, `application-test.yml`을 수정합니다.<br/>
```shell
 REDIS_HOST
 REDIS_PORT
 REDIS_USERNAME
 REDIS_PASSWORD
```
2. 운영자로부터 `config`파일과 `pem`파일을 전달받아, `~/.oci/` 폴더에 위치시킵니다.
3. 프로젝트를 빌드해줍니다.<br/>
```shell
./gradlew clean build
```
4. jar 실행
```shell
java -jar [path]/[applicationName].jar --spring.profiles.active=test
```

## 이슈 작성 방법
### 기능 개발
이슈명은 feature 탬플릿으로 작성.
관련 브런치는 feature/(issue번호)/(간단한 설명)로 만들것
ex) `feature/7/excelUpload`
### 버그 FIX
이슈명은 bug 탬플릿으로 작성.
관련 브런치는 feature/(issue번호)/(간단한 설명)로 만들것  
ex) `fix/7/contentTypeError`
### commit role
#### 기능 개발
```markdown
feture: 엑셀 업로드 개발

- 부연설명 1
- 부연설명 2

#5 <-- 해당 부분에 이슈 번호를 #[번호]로 작성
```
#### bug fix
```markdown
fix: 엑셀 업로드 에러 수정

- 부연설명 1
- 부연설명 2

#5 <-- 해당 부분에 이슈 번호를 #[번호]로 작성
```
#### docs 추가
```markdown
docs: 문서 수정

- 부연설명 1
- 부연설명 2

#5 <-- 해당 부분에 이슈 번호를 #[번호]로 작성. 옵션
```

