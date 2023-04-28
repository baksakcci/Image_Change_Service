# Image_Change_Service
이미지를 S3에 담아서 변환 후 사용자에게 반환하는 백엔드 API 서버

# 참조
프로젝트 참조

[Spring_SimpleAPI](https://github.com/baksakcci/Spring_SimpleAPI)

[Spring_S3Storage]()

# 프로젝트 핵심 기능
- 이미지 업로드 → AI 서버에서 변형 후 다운로드
- StyleGen 모델을 이용한 이미지 변환

# API 명세
| API 이름 |HTTP Method|URL|Type|RequestBody| ResponseBody                                                   |
|--------|----|----|----|----|----------------------------------------------------------------|
| 이미지 변환 |POST|/image/imageChange|multipart/form-data|key : image, value : imageFile.png| { "url": "s3 bucket", "message": "File uploaded Successful!" } |