# Retrieval Augmented Generation with Azure OpenAI

Retrieval-augmented generation (RAG) is a method that improves the precision and dependability of generative AI models by incorporating information obtained from external sources.

This file contains instructions to build, configure and install RAG API to demonstrate RAG usecase.

Refer article here, https://vineetaparodkar.hashnode.dev/retrieval-augmentation-generation-using-spring-ai

## Pre-requisites

- [Java 21](https://www.oracle.com/java/technologies/downloads/)

- [SSL certificate](https://letsencrypt.org/)

- Keystore (Refer Appendix section)

## Initial Setup

1. Clone the repository and checkout to main branch

## API Setup

1. Update Application properties.

### **Local Deployment**

a. Update application properties from resources folder here, `api/src/main/resources`.

1. Create a project deployment folder.

2. Copy below files from `api/src/main/resources` to project deployment folder.

   - `api/src/main/resources/application.yaml`

c. Execute following command from project root directory to create project service jar.

`./gradlew build -x test`

d. New jar will be created here `rag-with-spring-ai/build/libs/rag-0.0.1-SNAPSHOT.jar`. Copy this same jar in project deployment folder.

e. Execute below command to start RAG API.

- Navigate to project deployment folder and execute below command.
  `java -jar rag-0.0.1-SNAPSHOT.jar`

## Swagger UI for API Specification.

- Use below URL to access Swagger UI.

  `https://<servername>:<port>/rag/api-docs/swagger-ui/index.html`

## Appendix

- Use below command to generate keystore.

  `openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out keystore.p12 -name tomcat  -CAfile chain.pem  -caname root`

- Check if the SSL certificate chain from your origin server is complete.To check use this.

  `https://www.ssllabs.com/ssltest/`
