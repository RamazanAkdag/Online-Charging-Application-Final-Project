
---

# OCS Project (Online Charging System)

## Overview

The **OCS Project** (Online Charging System) is a scalable and distributed solution designed to handle real-time charging operations for telecommunications services. The architecture leverages microservices and distributed caching for high availability, fault tolerance, and efficient data processing.

## Architecture Components

* **Diameter Gateway**
  Handles Diameter protocol messages, translating them into internal service calls.

* **Charging Gateway**
  Processes charging events, applying business rules and interacting with account management services.

* **Hazelcast Cluster**
  Provides distributed caching and clustering to ensure data consistency and horizontal scalability.

* **Apache Ignite**
  Used for distributed computation and caching, enhancing performance for data-intensive operations.

* **Kafka & Kafdrop**
  Kafka facilitates event streaming between services. Kafdrop is used for monitoring and managing Kafka topics.

* **Management Center**
  A monitoring dashboard that provides insights into cluster health and performance.

## Technologies Used

* **Java 17**
* **Spring Boot**
* **Hazelcast**
* **Apache Ignite**
* **Apache Kafka**
* **Docker & Kubernetes**
* **Akka Cluster (for Diameter Gateway)**

## Key Features

* **Real-Time Charging:**
  Supports instant charging for voice, data, and SMS services.

* **Microservices Architecture:**
  Each component runs independently for better scalability and maintainability.

* **High Availability & Fault Tolerance:**
  Uses clustering (Hazelcast, Ignite) and container orchestration for resilience.

* **Event-Driven Communication:**
  Kafka enables efficient, scalable messaging between services.

## Setup & Deployment

1. **Clone the repository**

   ```bash
   git clone <your-repository-url>
   ```

2. **Build microservices**

   ```bash
   ./mvnw clean package
   ```

3. **Docker & Kubernetes**

   * Use provided `Dockerfile`s and `k8s` manifests to deploy services.
   * Example:

     ```bash
     kubectl apply -f k8s/diameter-gateway.yaml
     kubectl apply -f k8s/charging-gateway.yaml
     ```

4. **Monitoring**

   * Access Management Center UI for Hazelcast and Ignite cluster metrics.
   * Use Kafdrop to inspect Kafka topics and message flows.

## Development Standards

* Business logic resides strictly in the **service layer**.
* Controllers are kept lightweight, focusing only on HTTP request handling.
* Code follows **SOLID** and **clean architecture** principles.

## Known Issues & Troubleshooting

* **Pod ContainerCreating Errors:**
  Ensure all volumes and network policies are correctly configured.

* **Detached HEAD Warnings in Git:**
  Always complete merges or resolve conflicts before switching branches.

## Contributing

1. Fork the repository.
2. Create a new branch: `feature/your-feature-name`.
3. Submit a pull request following the project's coding standards.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

---


