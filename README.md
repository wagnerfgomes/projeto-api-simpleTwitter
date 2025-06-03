# Api-simpleTwitter

Uma versão simplificada do Twitter, desenvolvida em Java 21 com Spring Boot. Este projeto demonstra conceitos modernos de autenticação, autorização, persistência de dados, organização de APIs RESTful e integração com banco de dados relacional.

## ✨ Funcionalidades

- **Cadastro e autenticação de usuários** com níveis de acesso (ADMIN, BASIC)
- **Publicação de mensagens** (“tweets”)
- **Feed paginado** mostrando os tweets mais recentes
- **Segurança** baseada em OAuth2 Resource Server com JWT (chaves RSA)
- **Integração pronta para frontend** (ideal para estudos e MVPs)
- **Banco de dados MySQL** pronto para uso via Docker Compose

---

## 🏗️ Estrutura do Projeto

```
projeto-api-simpleTwitter/
├── src/main/java/com/estudos/simpleTwitter/
│   ├── SimpleTwitterApplication.java   # Bootstrap da aplicação
│   ├── entity/                        # Entidades JPA (User, Role, etc)
│   └── controller/dto/                # DTOs das rotas (FeedDto, etc)
├── src/main/resources/
│   ├── data.sql                       # Dados iniciais (roles)
│   ├── rsapubkey.pem                  # Chave pública JWT
│   └── rsaprivkey.pem                 # Chave privada JWT
├── docker/docker-compose.yml          # Sobe o MySQL já configurado
├── pom.xml                            # Dependências Maven
└── README.md
```

---

## 🚀 Como rodar localmente

### Pré-requisitos

- Java 21+
- Maven 3.x
- Docker (opcional, recomendado para o banco de dados)

### Passos

1. **Suba o banco de dados MySQL com Docker:**
   ```bash
   docker-compose -f docker/docker-compose.yml up -d
   ```
   O banco estará disponível em `localhost:3307` com usuário `admin` e senha `123` (veja detalhes no `docker-compose.yml`).

2. **Configure a conexão com o banco**
   
   No seu `application.properties`:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3307/mydb
   spring.datasource.username=admin
   spring.datasource.password=123
   ```
   > *As configurações exatas podem variar conforme seu ambiente.*

3. **Rode a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

---

## 🔒 Segurança

- **JWT com chaves RSA:**  
  As chaves estão em `src/main/resources/rsapubkey.pem` e `rsaprivkey.pem`.
- **Roles ADMIN e BASIC** já criadas automaticamente por `data.sql`.
- **Senhas** são protegidas e codificadas usando o PasswordEncoder do Spring Security.

---

## 📦 Principais Dependências

- `spring-boot-starter-data-jpa`
- `spring-boot-starter-oauth2-resource-server`
- `spring-boot-starter-security`
- `spring-boot-starter-web`
- `mysql-connector-j`

---

## 📝 Observações

- Este projeto é uma base para estudos. Novas funcionalidades podem ser facilmente adicionadas.
- Para detalhes de implementação, consulte os pacotes `entity`, `controller/dto` e afins.

---
