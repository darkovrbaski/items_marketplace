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

<p align="center">
  <a href="#-code-status">Code Status</a> •
  <a href="#-features">Features</a> •
  <a href="#-installation">Installation</a> •
  <a href="#-technologies-stack">Technologies Stack</a> •
  <a href="#-screenshots">Screenshots</a> •
  <a href="#-docs">Docs</a>
</p>

<br>
<br>

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

- Automatic make trades for placed buy/sell orders

- Manage inventory and wallet funds

- Track personal order history

- Fast load public images and secured user images

## 📦 Installation

### Backend

1. Make sure you have docker installed and running. You will also need docker-compose.

2. Clone the repository.

3. Fill out the values in the `.env` file. You will need AWS accont with S3 and two CloudFront distributions.

4. Run `docker-compose up`. This should build the docker image and start the container and Postgres DB running.

5. Head over to http://localhost:8080/docs (or a different port if you changed it) to make sure that backend Spring Boot application is running.

### Frontend

1. Change directory to `angular-app/`.

2. Install the dependencies `npm install`.

3. Run the Angular application `npm start`.

4. Head over to http://localhost:4200/ and feel free to register and explore the application.

## 📱 Technologies Stack

![Tech Stack](./images/tech-stack.svg?raw=true)

<!-- https://github-readme-tech-stack.vercel.app/api/cards?title=&showBorder=false&lineCount=6&hideBg=true&hideTitle=true&theme=github&line1=SpringBoot,Spring%20Boot,6DB33F;SpringSecurity,Spring%20Security,6DB33F;JUnit5,JUnit5,25A162&line2=Angular,Angular,DD0031;html5,html5,2831a9;sass,SCSS,CC6699;Bootstrap,Bootstrap,7952B3&line3=ESLint,ESLint,4B32C3;google,Checkstyle,34A7C1;Prettier,Prettier,7B93E;&line4=AmazonAWS,AWS,232F3E;AmazonS3,S3,569A31;AmazonCloudWatch,Cloudfront,66459B;,Secrets%20Manager,EE0000&line5=GitHubActions,GitHub%20Actions,2088FF;SonarCloud,SonarCloud,F3702A;Dependabot,Dependabot,025E8C&line6=Docker,Docker,2496ED -->

## 📷 Screenshots

![Login Page](./images/login-page.png?raw=true)
<br>
<br>
![Market Page](./images/market-page.png?raw=true)
<br>
<br>
![Orderbook Page](./images/orderbook-page.png?raw=true)
<br>
<br>
![Inventory Page](./images/inventory-page.png?raw=true)
<br>
<br>
![Wallet Page](./images/wallet-page.png?raw=true)
<br>
<br>
![Profile Page](./images/profile-page.png?raw=true)


## 📃 Docs

### Class Diagram

![Class Diagram](./images/class-diagram.png?raw=true)


<br><hr>
[🔼 Back to top](#items-marketplace)
