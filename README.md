
---

# OCS Project (Online Charging System)

##  Overview

The **OCS Project** (Online Charging System) is a distributed, scalable telecommunications solution that handles real-time charging for services like voice, SMS, and data. The architecture includes microservices, distributed caching, containerization, and automated deployment.

##  Architecture

**Main Components:**

* **Diameter Gateway (DGW)** – Diameter message handling with Akka Cluster.
* **Charging Gateway Function (CGF)** – Business rule application and event processing.
* **Account Order Management (AOM)** – Manages user accounts and orders.
* **Account Balance Management Function (ABMF)** – Manages balances and top-ups.
* **Notification Framework (NF)** – Sends real-time notifications.
* **Traffic Generator Function (TGF)** – Generates synthetic traffic for testing and validation.
* **Mobile App** – End-user application.

**Microservices use:**

* **Spring Boot**
* **Kafka** & **Kafdrop**
* **Hazelcast Cluster**
* **Apache Ignite**
* **Docker & Kubernetes**

## 🗂 System Design

###  Architectural Design

![Architectural Design](./Pictures/architecturaldesign.png)

###  Database Design

![Database Design](./Pictures/dbdesign.png)

###  Use Case Diagram

![Use Case](./Pictures/usecase.png)

##  Modules and Their Diagrams

* **Diameter Gateway**
  ![DGW](./Pictures/dgw.png)

* **Charging Gateway Function**
  ![CGF](./Pictures/cgf.png)

* **Account Order Management**
  ![AOM](./Pictures/aom.png)

* **Account Balance Management Function**
  ![ABMF](./Pictures/abmf.png)

* **Notification Framework**
  ![NF](./Pictures/nf.png)

* **Traffic Generator Function**
  ![TGF](./Pictures/tgf.png)

* **Mobile App**
  ![Mobile App](./Pictures/mobileapp.png)

##  CI/CD Pipeline

![CI/CD Pipeline](./Pictures/ci-cd.png)

*Describes the continuous integration and deployment workflow from development to production.*

## ⚙ Technologies

* **Java 17** / **Spring Boot**
* **Hazelcast / Ignite / Kafka**
* **Docker / Kubernetes**
* **Akka Cluster**
* **PostgreSQL / MongoDB** *(if applicable to DB design)*

## ✅ Development Principles

* Business logic isolated in **service layers**.
* Lightweight **controllers**.
* Follows **SOLID** and **clean architecture**.
* Automated testing and containerized deployment.

## 🔍 Monitoring

* **Hazelcast Management Center**
* **Ignite Dashboard**
* **Kafdrop** for Kafka topic inspection.

## 📜 License

MIT License.

---

