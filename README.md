### Build and Run
```bash
docker build -t license-store . && docker run -d --name license-store -p 8082:8080 -it license-store
```

### Build
```bash
docker build -t license-store .
```

### Run
```bash
docker run -d --name license-store -p 8082:8080 -it license-store
```

### Login
```bash
docker login -u leogenus
```
```password
dckr_pat_kxmJFdFLgYAFMTdqRMq_x6xhSUU
```

### Push
#### run once
```bash
docker buildx create --use && \
export DOCKER_CLI_BUILDKIT=1
```
#### run for build and push
```bash
docker buildx build --platform linux/amd64,linux/arm64 -t leogenus/license-store:0.0.1-alpha -t leogenus/license-store:latest --push .
```

