<h1 align="center">ms-etaskify-reg</h1>

## Table of Contents

- [Introduction](#introduction)
- [How to build & How to run](#how-to-build--how-to-run)
- [Dependencies](#dependencies)

## Introduction

Introduction

Microservice for register organization and user for taskify

## How to build & How to run

```shell script
$ ./gradlew build
```

```shell script
$ java -jar ms-etaskify-reg.jar --spring.profiles.active=dev(-Dspring.profiles.active=dev)
)
```

## Dependencies
1. [ms-etaskify-user](https://github.com/random-source/etaskify/ms-etaksify-user)
2. redis-server
