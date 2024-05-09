### 1. Install Node
- https://nodejs.org/en/download
- ```node -v```
### 2. Install Vue CLI
- https://cli.vuejs.org/#getting-started
- ```vue --version```
### 3. setup frontend base
- Create base project: ```vue create frontend```
- if it doesn't working Administrator PowerShell: ```Set-ExecutionPolicy RemoteSigned```
- Install http   : ```npm install axios```
- Install router : ```npm install vue-router```
- Install vuex   : ```npm install vuex@next --save```
- Run ```npm run serve```

### front architecture
- main.js 
  - App.vue
    - AppHeader.vue
    - RouterViewer -> router.js
      - login.vue
      - cart.vue
    - AppFooter.vue

- stroe.js : save for member ID



### 4. frontend conditions 
- allow single name.
  - package.json
```
"rules": {
      "vue/multi-word-component-names": 0
    }
```
- change port
  -  package.json
```
  "scripts": {
    "serve": "vue-cli-service serve --port 3000",
    ... ...
  },
```
- setup proxy
  - vue.config.js
```
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/': '', // 이 부분은 /api를 제거합니다. 필요에 따라 조정 가능
        },
      },
    },
  }
```

### reference url
1. https://getbootstrap.com/docs/4.0/examples/jumbotron/#
2. https://getbootstrap.com/docs/4.0/examples/sign-in/
3. https://fontawesome.com/v4/

#  server setup
```
docker run -d --name mysqlDB `
-e MYSQL_DATABASE=vue_test `
-e MYSQL_USER=user01 `
-e MYSQL_PASSWORD=user01 `
-e MYSQL_ROOT_PASSWORD=password `
-e TZ='+08:00' `
-p 3306:3306 `
-v D:\docker\mysql:/var/lib/mysql `
mysql
```
```
jdbc:mysql://localhost:3306/vue_test?serverTimezone=Asia/Kuala_Lumpur
```
