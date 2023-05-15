<br>
<div align="center">
  <img src="./images/brand_dark_mode.svg#gh-dark-mode-only" width="120px">
  <img src="./images/brand_light_mode.svg#gh-light-mode-only" width="120px">
</div>
<br>
<h1 align="center">Items marketplace</h1>
<h4 align="center">Marketplace with orderbooks, buy/sell items platform.</h4>
<br>

<p align="center">
  <a href="https://github.com/darkovrbaski/items_marketplace/releases"
     title="GitHub release (latest SemVer)">
    <img src="https://img.shields.io/github/v/release/darkovrbaski/items_marketplace?sort=semver">
  </a>
  <a href="#"
     title="GitHub Repo stars">
    <img src="https://img.shields.io/github/stars/darkovrbaski/items_marketplace?">
  </a>
  <a href="https://linkedin.com/in/darko-vrbaški-b45a00242"
     title="LinkedIn">
    <img src="https://img.shields.io/badge/LinkedIn-0077B5?&logo=linkedin&logoColor=white">
  </a>
<p>

<br>
<br>

<p align="center">
  <a href="#-code-status">🟢 Code Status</a> •
  <a href="#-features">✨ Features</a> •
  <a href="#-architecture">🏛️ Architecture</a> •
  <a href="#-installation">📦 Installation</a> •
  <a href="#-technologies-stack">📱 Technologies Stack</a> •
  <a href="#-screenshots">📷 Screenshots</a> •
  <a href="#-docs">📃 Docs</a>
</p>

<br>

![Home Page](./images/home-page.png?raw=true)

## 🟢 Code Status

<p align="center">
  <a href="https://github.com/darkovrbaski/items_marketplace/actions/workflows/ci-backend.yml"
     title="Backend CI workflow status">
    <img src="https://github.com/darkovrbaski/items_marketplace/actions/workflows/ci-backend.yml/badge.svg?branch=main">
  </a>
  <a href="https://sonarcloud.io/summary/new_code?id=darkovrbaski_items_marketplace_backend"
     title="Backend code quality status">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=darkovrbaski_items_marketplace_backend&metric=alert_status">
  </a>
  <a href="https://sonarcloud.io/summary/overall?id=darkovrbaski_items_marketplace_backend"
     title="Backend code coverage">
    <img src="https://img.shields.io/sonar/coverage/darkovrbaski_items_marketplace_backend/main?server=https%3A%2F%2Fsonarcloud.io">
  </a>
<p>
  
<p align="center">
  <a href="https://github.com/darkovrbaski/items_marketplace/actions/workflows/ci-frontend.yml"
     title="Frontend CI workflow status">
    <img src="https://github.com/darkovrbaski/items_marketplace/actions/workflows/ci-frontend.yml/badge.svg?branch=main">
  </a>
  <a href="https://sonarcloud.io/summary/new_code?id=darkovrbaski_items_marketplace_frontend"
     title="Frontend code quality status">
    <img src="https://sonarcloud.io/api/project_badges/measure?project=darkovrbaski_items_marketplace_frontend&metric=alert_status">
  </a>
<p>

<p align="center">
  <a href="https://github.com/darkovrbaski/items_marketplace/actions/workflows/codeql.yml"
     title="Code quality workflow status">
    <img src="https://github.com/darkovrbaski/items_marketplace/actions/workflows/codeql.yml/badge.svg">
  </a>
<p>

## ✨ Features

- Easily view and search market items

- Automaticly make trades on one click

- Manage inventory and wallet funds

- Track personal order history

- Fast load public images and secured user images

## 🏛️ Architecture

<div align="center">
  <img src="./docs/component%20diagram/architecture.svg?raw=true">
</div>

## 📦 Installation

### Backend

1. Clone the repository.

2. Make sure you have Terraform installed and provided IAM credentials to authenticate the Terraform AWS provider.

3. Change directory to `infrastructure/aws_resorces/`.

4. Run next two commands to generate required Cloudfront key group keys.
``` sh
  openssl genrsa -out private_key.pem 2048
```
``` sh
  openssl rsa -pubout -in private_key.pem -out public_key.pem
``` 

5. Run `terraform init` then `terraform apply --auto-aprove` to provision S3 and Cloudfronts on AWS.

#### Docker

6. Make sure you have docker installed and running. You will also need docker-compose.

7. Change directory to `infrastructure/`

8. Fill out the values in the `.env` file with outputs from terraform provisioned resorces.

9. Run `docker-compose up`. This should build the docker image and start the container and Postgres DB running.

10. Head over to http://localhost:8080/docs (or a different port if you changed it) to make sure that backend Spring Boot application is running.

#### IDE

6. Open Spring Boot application in your IDE located in `items-marketplace/`

7. Set environment variables:
    - `APP_PORT`
    - `HTTP_PORT`
    - `POSTGRES_DB_URL`
    - `POSTGRES_DB_USERNAME`
    - `POSTGRES_DB_PASSWORD`
    - `FRONTEND_URL`
    - `STRIPE_SECRET_KEY`
    - `STRIPE_ENDPOINT_SECRET`
    - `JWT_SECRET` (generate encryption key)
    - `S3_BUCKET_NAME` (terraform output)
    - `CLOUDFRONT_DOMAIN_PUBLIC` (terraform output)
    - `CLOUDFRONT_DOMAIN_PRIVATE` (terraform output)
    - `KEY_PAIR_ID` (terraform output)
    - `PRIVATE_KEY_NAME` (terraform output)

8. Execute the main method in the `me.darkovrbaski.items.marketplace.ItemsMarketplaceApplication` class.

9. Head over to http://localhost:8080/docs (or a different port if you changed it) to make sure that backend Spring Boot application is running.

### Frontend

1. Change directory to `angular-app/`.

2. Install the dependencies `npm install`.

3. Run the Angular application `npm start`.

4. Head over to http://localhost:4200/ and feel free to register and explore the application.

## 📱 Technologies Stack

![Tech Stack](./images/tech-stack.svg?raw=true)

<!-- https://github-readme-tech-stack.vercel.app/api/cards?title=&showBorder=false&lineCount=8&hideBg=true&hideTitle=true&theme=github&line1=SpringBoot,Spring%20Boot,6DB33F;SpringSecurity,Spring%20Security,6DB33F;JUnit5,JUnit5,25A162&line2=Angular,Angular,DD0031;html5,html5,2831a9;sass,SCSS,CC6699;Bootstrap,Bootstrap,7952B3&line3=ESLint,ESLint,4B32C3;google,Checkstyle,34A7C1;Prettier,Prettier,7B93E;&line4=AmazonAWS,AWS,232F3E;AmazonS3,S3,569A31;,Cloudfront,66459B;,SSM,&line5=MicrosoftAzure,Azure,0078D4;,ACR,;,ACI,;,CDN,;,Static%20Web%20App,;&line6=GitHubActions,GitHub%20Actions,2088FF;SonarCloud,SonarCloud,F3702A;Dependabot,Dependabot,025E8C&line7=Docker,Docker,2496ED;PostgreSQL,PostgreSQL,4169E1;Terraform,Terraform,7B42BC&line8=Stripe,Stripe,008CDD;
-->

<!-- https://simpleicons.org/ -->

## 📷 Screenshots

### Login Page

![Login Page](./images/login-page.png?raw=true)
<br>
<br>

### Market Page

![Market Page](./images/market-page.png?raw=true)
<br>
<br>

### Orderbook Page

![Orderbook Page](./images/orderbook-page.png?raw=true)
<br>
<br>

### Place Buy Order

![Place Buy Order](./images/place-buy-order.png?raw=true)
<br>
<br>

### Inventory Page

![Inventory Page](./images/inventory-page.png?raw=true)
<br>
<br>

### Place Sell Order

![Place Sell Order](./images/place-sell-order.png?raw=true)
<br>
<br>

### Wallet Page

![Wallet Page](./images/wallet-page.png?raw=true)
<br>
<br>

### Orders Page

![Orders Page](./images/orders-page.png?raw=true)
<br>
<br>

### Order Page

![Order Page](./images/order-page.png?raw=true)
<br>
<br>

### Profile Page

![Profile Page](./images/profile-page.png?raw=true)


## 📃 Docs

### Class Diagram

![Class Diagram](./docs/class%20diagram//class-diagram.svg?raw=true)


<br><hr>
[🔼 Back to top](#items-marketplace)
