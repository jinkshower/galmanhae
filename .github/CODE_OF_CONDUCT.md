### 갈만해? 프로젝트 Code of Conduct

갈만해? 프로젝트에서 지켜야할 규칙입니다.

#### Git 전략

- Trunk-based Development 전략을 사용합니다.[참고](https://trunkbaseddevelopment.com/)  
- `main` 브랜치를 기반으로 개발을 진행하며, 각 변경사항 별로 브랜치 작업, PR에서 CI이후 'main' merge 후 배포합니다.  
- Merge는 Rebase를 사용합니다. 브랜치 checkout 전, main 업데이트 이후 꼭 최신의 main을 rebase 해주세요.  

- 브랜치 명명 규칙은 다음과 같습니다.

  ```
    feat/  : 새로운 기능
    fix/     : 버그 수정
    docs/    : 문서 관련 변경
    style/   : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
    refactor/: 리팩토링
    test/    : 테스트 추가, 테스트 리팩토링 (프로덕션 코드 변경 없음)
    cicd/    : CI, CD 관련 변경
    chore/   : 기타 변경사항
  ```

ex) 'feat/signup', 'fix/login', 'docs/README' 등 영어로 작성해주세요.

- 커밋 헤더의 작성 규칙은 다음과 같습니다.

  ```
    feat    : 새로운 기능
    fix     : 버그 수정
    docs    : 문서 관련 변경
    style   : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
    refactor: 리팩토링
    test    : 테스트 추가, 테스트 리팩토링 (프로덕션 코드 변경 없음)
    cicd    : CI, CD 관련 변경
    chore   : 기타 변경사항
  ```

ex) 'feat: 회원가입 기능 추가', 'fix: 로그인 버그 수정' 등 '헤더: 내용' 형식으로 헤더 이후 한글로 작성해주세요.  
- 본문은 커밋 제목에 한줄을 띄우고 작성합니다.

#### Pull Request 작성법

- 'main' 브랜치는 보호되어 있습니다. PR을 통해 코드를 merge 해주세요.  
- PR에는 관련된 이슈를 언급해주세요.  
- PR 템플릿의 체크리스트를 지켜주세요.   
  
```
- [ ] 코드 포맷터를 적용했습니다
- [ ] 변경에 해당하는 테스트(단위 or 통합)를 작성했습니다
```

#### Issue 작성법

- 이슈는 라벨링을 사용합니다. 기능개발(enhancement), 문서(documentation), 버그 수정(bug)등 목적에 맞는 라벨을 붙여주세요.  
- 이슈 템플릿을 이용해 작업 예상 내용을 간결하게 설명, 할 일을 체크리스트로 작성해주세요.  

#### 문서화

- 프로젝트 히스토리 관리는 [노션](https://jinkshower.notion.site/9a6104649a9446ef9d0a778a99fb9c26?pvs=4)에서 진행합니다.  
- 프로젝트 진행관련 포스팅은 [기술 블로그](https://jinkshower.github.io/)에서 진행합니다.

#### 코드 포맷터

- 코드 포맷터는 [intellij-java-google-style.xml](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)을 사용합니다.
- Tab size, indent를 각각 4로 조정해주세요.
