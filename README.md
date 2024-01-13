### 1. Install Node
- https://nodejs.org/en/download
- ```node -v```
### 2. Install Vue CLI
- https://cli.vuejs.org/#getting-started
- ```vue --version```
### 3. setup frontend base
- Create base project: ```vue create frontend```
- if it doesn't working Administrator CMD: ```Set-ExecutionPolicy RemoteSigned```
- setup package for connecting: ```npm install axios```
- Run ```npm run serve```

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
- Add router ```npm install vue-router```
- https://getbootstrap.com/docs/4.0/examples/sign-in/

