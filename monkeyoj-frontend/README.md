# monkeyoj-frontend

## Project setup

```
npm install
```

### Compiles and hot-reloads for development

```
npm run serve
```

### Compiles and minifies for production

```
npm run build
```

### Lints and fixes files

```
npm run lint
```

### Customize configuration

See [Configuration Reference](https://cli.vuejs.org/config/).

### 生成后端接口代码
```
openapi --input http://localhost:8121/api/v2/api-docs --output ./generated --client axios
```