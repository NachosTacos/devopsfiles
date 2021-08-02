# syntax=docker/dockerfile:1
FROM alpine:latest 
RUN apk update
RUN apk add git

