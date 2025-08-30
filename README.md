# Desafio Backend Estagiário - NeoApp

Este projeto implementa uma API REST para gerenciamento de clientes pessoa física, conforme o desafio proposto pela NeoApp.

## Acesso à API Hospedada (VPS)

A API está hospedada em uma VPS:

Para acessar a documentação interativa da API (Swagger UI) na VPS, utilize a seguinte URL:

*   **Swagger UI:** [http://82.29.57.38:8085/swagger-ui/index.html](http://82.29.57.38:8085/swagger-ui/index.html)


## Acesso à API local

## Pré-requisitos

Para executar este projeto, você precisará ter instalado:

*   **Java Development Kit (JDK) 17**
*   **Apache Maven** (para gerenciamento de dependências e build do projeto)
*   **PostgreSQL** (ou outro banco de dados compatível com Spring Data JPA, como H2 para testes)

## Configuração do Banco de Dados (PostgreSQL)

1.  Crie um banco de dados PostgreSQL com o nome de sua preferência (ex: `neoapp_db`).
2.  **Atualize o `pom.xml`:**
    **Adicionar:**
    ```xml
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    ```

3.  **Atualize as configurações de conexão no `src/main/resources/application.properties`:**
    Certifique-se de ajustar `spring.datasource.url`, `spring.datasource.username` e `spring.datasource.password` com suas credenciais do PostgreSQL.

    Exemplo de `application.properties` para PostgreSQL:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/neoapp_db
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```

## Execução da Aplicação

1.  Navegue até o diretório raiz do projeto (`neo`).
2.  Compile o projeto utilizando Maven:
    ```bash
    mvn clean install
    ```
3.  Execute a aplicação Spring Boot:
    ```bash
    mvn spring-boot:run
    ```

A aplicação será iniciada na porta padrão 8080 (ou na porta configurada em `application.properties`).

## Acesso à API e Documentação (Swagger UI) - Local

Após iniciar a aplicação localmente, você pode acessar a documentação interativa da API através do Swagger UI no seguinte endereço:

*   [http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html)

## Endpoints Principais

Os principais endpoints da API são:

*   `POST /api/register`: Registra um novo cliente.
*   `POST /api/login`: Realiza o login e retorna um token JWT para acesso aos endpoints protegidos.
*   `GET /api/auth/users`: Lista clientes com paginação e filtros por nome, CPF e e-mail (requer token JWT).
*   `PUT /api/update/user/{id}`: Atualiza informações de um cliente existente (requer token JWT).
*   `DELETE /api/delete/{id}`: Exclui um cliente pelo ID (requer token JWT).

## Credenciais de Teste

Não há credenciais de teste pré-definidas. Você pode registrar um novo usuário através do endpoint `/api/register` e então utilizar essas credenciais para realizar o login e obter um token JWT.
