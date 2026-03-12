# Spring Sparrow🐦

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Framework](https://img.shields.io/badge/Spring-7.0.5-brightgreen.svg)](https://spring.io/)
[![Hibernate](https://img.shields.io/badge/Hibernate-7.2.5-blue.svg)](https://hibernate.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)

**Legacy Sparrow** 是一個展示傳統 **Spring Framework 7** 實作風格的範例專案。

**Boot Sparrow(尚在實作)** 是專為展示 Spring Boot 3 實作風格而設計的範例專案。

> **注意：** 這是 **Legacy Sparrow** 專案中的 `legacy` 部分。
> 若要查看現代 Spring Boot 的實作，請參考根目錄下的 `boot` 目錄。

## 🚀 Legacy 專案特色

- **Config**：完全基於 Java 的 Spring 配置，無 XML。
無使用 Spring Boot，展示傳統 Spring Framework 的實作方式。
- **專案結構**：垂直分層結構，清晰分離 Controller、Service、Repository、Entity 等模組。
- **API 文件**：使用 Spring REST Docs 生成 API 文檔，官方的推行版。
- **容器化**：基於 Tomcat 11 的 Docker 配置，方便部署與測試。

Legacy 相關 Blog 文章：

- [Spring Sparrow Start](https://blog.codeicu.dev/posts/spring-sparrow/bean/)
- [Spring Sparrow AOP](https://blog.codeicu.dev/posts/spring-sparrow/aop/)
- [Spring Sparrow JPA](https://blog.codeicu.dev/posts/spring-sparrow/jpa/)
- [Spring Sparrow API](https://blog.codeicu.dev/posts/spring-sparrow/api/)
- [Spring Sparrow REST Docs](https://blog.codeicu.dev/posts/spring-sparrow/rest-docs/)
- [Spring Sparrow CRUD](https://blog.codeicu.dev/posts/spring-sparrow/crud/)
- [Spring Sparrow Integration Test](https://blog.codeicu.dev/posts/spring-sparrow/test/)

## 📁 Legacy 專案結構

```text
legacy/
├── src/main/java/sparrow/
│   ├── aspect/            # AOP 日誌記錄與監控
│   ├── config/            # Spring 全 Java Config 配置 (Web, JPA, App)
│   ├── exception/         # 全域異常處理與自定義 Exception
│   ├── post/              # Blog Post 核心功能 (Controller, Service, Repository, Entity, DTO)
│   ├── status/            # 系統狀態監控 API
│   └── user/              # 使用者管理實體與資源庫
├── src/main/asciidoc/     # Spring REST Docs 文檔源碼
├── Dockerfile             # 基於 Tomcat 11 的容器化配置
├── server.xml             # Tomcat 自定義配置
└── pom.xml                # Maven 依賴配置 (.war 封裝)
```

## 🛠️ 技術線

- **核心**: Java 21, Spring Framework 7.0.5
- **持久化**: Hibernate 7.2.5, Spring Data JPA 4.0.3, MySQL 9.6, HikariCP 5.1
- **Web**: Spring MVC, Jakarta Servlet 6.1
- **驗證**: Jakarta Validation 3.1
- **開發工具**: Spring REST Docs 4.0, Asciidoctor, Spotless

## 🚦 快速開始

### 環境需求

- Docker & Docker Compose
- Java 21 & Maven 3.9+ (本地端開發所需)

### 使用 Docker Compose 啟動

在專案根目錄執行：

```bash
docker-compose up -d
```

Legacy 應用將會啟動並監聽 `8080` 埠號。

### 存取應用程式

- **API 端點**: `http://localhost:8080/legacy-1.0/`
- **API 文件**: `http://localhost:8080/legacy-1.0/static/docs/index.html` (需先建置生成)

## 📖 開發指南

### 本地端建置與生成文件

```bash
mvn clean install prepare-package
```

這將會編譯程式碼、執行測試並生成 API 文件。

### 格式化程式碼

```bash
mvn spotless:apply
```

### 資料庫設定

連接資訊位於 `src/main/resources/application.properties`
