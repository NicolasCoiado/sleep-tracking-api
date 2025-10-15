#  Sleep Tracking API

## Descrição do projeto:

O SleepTracking API gerencia contas de usuários com diferentes permissões e permite registrar o tempo em cama e o tempo efetivo de sono. Usuários podem consultar seu histórico de sono e obter métricas como horário ideal para dormir, enquanto administradores têm controle sobre todas as contas e registros. O sistema também oferece verificação de contas e recuperação de senha via e-mail.

A aplicação é construída com **Spring Boot**, usando **Spring Security** e **JWT** para autenticação e autorização. **MongoDB** armazena usuários e registros de sono, enquanto **Redis** gerencia filas de e-mails de verificação e redefinição de senha. O **Lombok** reduz boilerplate em entidades e DTOs. O envio de e-mails é feito com **spring-boot-starter-mail** e a documentação da API é gerada com **Springdoc (Swagger)**, permitindo exploração e teste interativo dos endpoints.

---

## Tecnologias utilizadas:

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

---

## Estrutura de Endpoints

---

### **Autenticação e Verificação**

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/auth/register` | Registra um novo usuário. Retorna detalhes do usuário e envia e-mail de verificação. |
| POST | `/auth/login` | Autentica usuário e retorna token JWT. |
| POST | `/auth/verify` | Verifica o código de registro enviado por e-mail. |
| POST | `/auth/resend-verification` | Reenvia o e-mail de verificação para o usuário. |
| POST | `/auth/reset-request` | Solicita o e-mail para redefinição de senha. |
| POST | `/auth/reset-confirm` | Confirma a redefinição de senha usando o código enviado por e-mail. |

---

### **Usuário (User)**

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/user` | Retorna detalhes do usuário autenticado (`UserResponse`). |
| GET | `/user/calculate/ideal-bedtime` | Calcula e retorna o horário ideal para dormir do usuário. |

> **Observação:** Endpoints de usuário exigem autenticação (Bearer Token).

---

### **Tentativas de Sono (Sleep Attempts)**

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/sleep` | Registra uma nova tentativa de sono. Retorna a tentativa criada. |
| GET | `/sleep` | Retorna todas as tentativas de sono do usuário autenticado. |
| GET | `/sleep/success` | Retorna apenas tentativas de sono bem-sucedidas. |
| GET | `/sleep/unsuccessfully` | Retorna apenas tentativas de sono mal-sucedidas. |
| GET | `/sleep/by-day?date=YYYY-MM-DD` | Retorna tentativas de sono em um dia específico. |
| GET | `/sleep/by-month?year=YYYY&month=MM` | Retorna tentativas de sono em um mês específico. |
| GET | `/sleep/by-range?start=YYYY-MM-DD&end=YYYY-MM-DD` | Retorna tentativas de sono em um intervalo de datas. |
| GET | `/sleep/attempt/{id}` | Retorna detalhes de uma tentativa de sono específica pelo ID. |
| PUT | `/sleep/attempt/{id}` | Atualiza uma tentativa de sono existente. |
| PATCH | `/sleep/attempt/{id}` | Atualiza parcialmente uma tentativa de sono existente. |
| DELETE | `/sleep/attempt/{id}` | Deleta uma tentativa de sono específica pelo ID. |

> **Observação:** Endpoints de Sleep Attempts exigem autenticação (Bearer Token).

---

### **Administração (Admin)**

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/admin/users` | Lista todos os usuários registrados. |
| GET | `/admin/users/{id}` | Retorna detalhes de um usuário específico pelo ID. |
| PATCH | `/admin/users/{id}/lock` | Bloqueia a conta de um usuário. |
| DELETE | `/admin/users/{id}` | Deleta um usuário pelo ID. |
| GET | `/admin/sleeps/{id}` | Lista todas as tentativas de sono de um usuário específico. |
| DELETE | `/admin/sleeps/{id}` | Deleta todas as tentativas de sono de um usuário específico. |

> **Observação:** Endpoints de administração exigem autenticação e privilégios de administrador.

---

## Documentação da API

A documentação completa da API está disponível via Swagger/OpenAPI nos endereços:

`/swagger-ui.html` ou `/v3/api-docs`.

---

## Como Rodar o Projeto:

### 1. Clonar o Repositório

```bash
git clone https://github.com/NicolasCoiado/sleep-tracking-api.git
cd sleep-tracking-api
```

---

### 2. Configurar Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```dotenv
# MongoDB
MONGO_USER=seu_usuario
MONGO_PASSWORD=sua_senha

# Redis
REDIS_PASSWORD=sua_senha_redis

# SMTP (Envio de E-mails)
SMTP_HOST=seu_host_smtp
SMTP_PORT=587
SMTP_USER=seu_usuario
SMTP_PASSWORD=sua_senha

# JWT
SECRET_JWT=sua_chave_secreta
```
---

### 3. Rodar o docker-compose

No terminal, execute:

```bash
docker-compose up -d
```

Isso irá:
* Baixar as imagens do MongoDB e Redis (caso não existam);
* Criar e iniciar os containers mongodb e redis;
* Mapear portas e volumes conforme definido no docker-compose.yaml.

---

**Pronto, o projeto já pode ser executado na sua IDE!**