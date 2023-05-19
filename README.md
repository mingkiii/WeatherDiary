# WeatherDiary Project
외부 API의 날씨데이터를 받아서 날씨 정보와 일기(텍스트)를 CRUD(생성/조회/수정/삭제)하는 프로젝트입니다.

## ⚙️ Tech Stack
- java 8
- IntelliJ(Ultimate)
- SpringBoot, Spring Web, Gradle, Lombok
- JPA
- DB : MySQL
- Open API : https://openweathermap.org/

## 📋 Process
추가 구현 사항
- 매일 새벽 1시에 날씨 데이터를 외부 API에서 받아 DB에 저장해둔다.
- logback 활용
- ExceptionHandler을 이용한 예외처리
- swagger 을 이용하여 API documentation
### POST - create
- 특정 날짜를 매개변수로 넘길 경우 그 날짜에 맞게 api데이터를 받아오도록 한다. 
  - ex) http://localhost:8080/create/diary?date=2023-05-16
- 오늘 일기 작성 : 만약 특정 날짜를 매개변수에 넣지않을 시 현재 날짜로 1시에 받아온 DB의 데이터를 사용하여 일기를 생성한다. 
  - ex) http://localhost:8080/create/diary
  
### GET - read
- 특정 날짜의 모든 일기를 조회하여 List<Diary>를 JSON 형식으로 반환
  - ex) http://localhost:8080/read/diary?date=2023-05-16
- 특정 기간의 날짜의 모든 일기를 조회하여 List<Diary>를 JSON 형식으로 반환
  - ex) http://localhost:8080/read/diaries?startDate=2023-02-01&endDate=2023-05-16
  - 날짜가 너무 과거이거나 미래면 throw new InvalidDate();이라는 사용자 정의 예외를 던진다.
  
### PUT - update
- 특정 날짜의 첫번째 일기 글을 새로운 텍스트로 수정
  - 해당 날짜에 일기가 없다면  throw new Nonexistent(); 사용자 정의 예외를 던진다.
  
### DELETE - delete
- 특정 날짜의 모든 일기를 삭제.
  - 해당 날짜에 일기가 없다면  throw new Nonexistent(); 사용자 정의 예외를 던진다.
