FROM amd64/alpine:3.16
RUN apk add --no-cache gcompat libc6-compat zlib
COPY build/native/nativeCompile/q-url-api /run/app
ENTRYPOINT /run/app