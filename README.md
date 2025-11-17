# BIP Teste Integrado

Aplicação fullstack desenvolvida em camadas para gerenciamento de benefícios, utilizando Java (Spring Boot + EJB), PostgreSQL e Angular.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Pré-requisitos](#pré-requisitos)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Configuração do Banco de Dados](#configuração-do-banco-de-dados)
- [Configuração do WildFly](#configuração-do-wildfly)
- [Instalação e Execução](#instalação-e-execução)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [API](#api)
- [Testes](#testes)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)

## Visão Geral

Esta aplicação é uma solução completa em camadas que permite o gerenciamento de benefícios, incluindo operações CRUD e transferência de valores entre benefícios. A arquitetura segue o padrão de separação de responsabilidades com:

- **Domain Module**: Modelos de domínio e interfaces de repositório
- **Persistence Module**: Implementação de persistência JPA
- **EJB Module**: Serviços de negócio (EJB)
- **Backend Module**: API REST Spring Boot
- **Frontend**: Interface Angular

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17** ou superior
- **Maven 3.6+**
- **PostgreSQL 12+**
- **WildFly 28+** (ou versão compatível)
- **Node.js 18+** e **npm**
- **Angular CLI 20+**

## Estrutura do Projeto

```
bip-teste-integrado/
├── backend-module/          # Módulo Spring Boot (API REST)
│   └── src/main/resources/
│       └── db/migration/    # Migrações Flyway (V01, V02, ...)
├── domain-module/           # Módulo de domínio (modelos e interfaces)
├── ejb-module/             # Módulo EJB (serviços de negócio)
├── persistence-module/      # Módulo de persistência JPA
├── frontend/               # Aplicação Angular
├── db/                     # Scripts SQL (schema e seed) - referência
├── docker/                 # Configurações Docker (futuro)
├── docs/                   # Documentação adicional
└── configurar-wildfly.cli  # Script de configuração do WildFly
```

## Configuração do Banco de Dados

### 1. Criar o Banco de Dados

Execute os seguintes comandos no PostgreSQL:

```sql
CREATE DATABASE bit_teste;
```

### 2. Configurar Flyway

A aplicação utiliza **Flyway** para gerenciar as migrações do banco de dados automaticamente. As migrações estão localizadas em `backend-module/src/main/resources/db/migration/`.
O Spring já cuida das configurações.

### 3. Executar Migrações

As migrações serão executadas **automaticamente** quando o backend Spring Boot for iniciado. O Flyway irá:

1. Verificar o histórico de migrações no banco
2. Executar as migrações pendentes na ordem (V01, V02, etc.)
3. Registrar as migrações executadas na tabela `flyway_schema_history`

> **Nota**: Não é necessário executar os scripts SQL manualmente. O Flyway cuida disso automaticamente na primeira execução do backend.

### 4. Verificar Configuração

Certifique-se de que o PostgreSQL está rodando na porta padrão `5432` e que as credenciais padrão são:
- **Usuário**: `postgres`
- **Senha**: `postgres`
- **Banco**: `bit_teste`

> **Nota**: Se suas credenciais forem diferentes, atualize os arquivos de configuração:
> - `backend-module/src/main/resources/application-dev.properties`
> - `backend-module/src/main/resources/application-production.properties`

## Configuração do WildFly

O WildFly precisa ser configurado para:
1. Instalar o driver JDBC do PostgreSQL
2. Criar o DataSource `BeneficioDS`
3. Habilitar o remoting para comunicação com EJBs

### Passo 1: Preparar o Driver PostgreSQL

1. Baixe o driver JDBC do PostgreSQL (versão 42.7.8 ou superior)
2. Coloque o arquivo `postgresql-42.7.8.jar` em um diretório acessível (exemplo: `D:/PostgreDrive/`)

> **Importante**: Se o driver estiver em outro local, edite o arquivo `configurar-wildfly.cli` e atualize o caminho na linha 7.

### Passo 2: Editar o Script de Configuração

Abra o arquivo `configurar-wildfly.cli` e verifique/ajuste as seguintes configurações:

- **Caminho do driver** (linha 7): Atualize `D:/PostgreDrive/postgresql-42.7.8.jar` para o caminho correto do seu driver
- **URL do banco** (linha 21): Verifique `jdbc:postgresql://localhost:5432/bit_teste`
- **Usuário do banco** (linha 22): Verifique `postgres`
- **Senha do banco** (linha 23): Verifique `postgres`

### Passo 3: Executar o Script de Configuração

> **Importante**: Para executar o comando `jboss-cli.bat` (ou `jboss-cli.sh` no Linux/Mac), você precisa:
> - Estar com o terminal na pasta `bin` do WildFly, **OU**
> - Ter a variável de ambiente `WILDFLY_HOME` configurada e o caminho `%WILDFLY_HOME%\bin` (ou `$WILDFLY_HOME/bin`) adicionado ao PATH do sistema

#### Opção 1: Executar a partir da pasta bin do WildFly

```bash
# Navegue até a pasta bin do WildFly
cd %WILDFLY_HOME%\bin
# ou, se não tiver a variável configurada:
cd C:\caminho\para\wildfly\bin

# No Windows (PowerShell ou CMD)
jboss-cli.bat --file=C:\caminho\completo\para\configurar-wildfly.cli

# No Linux/Mac
cd $WILDFLY_HOME/bin
# ou
cd /caminho/para/wildfly/bin
./jboss-cli.sh --file=/caminho/completo/para/configurar-wildfly.cli
```

#### Opção 2: Executar com WildFly rodando (via conexão remota)

Se o WildFly estiver rodando, você pode executar o script via conexão remota:

```bash
# Navegue até a pasta bin do WildFly
cd %WILDFLY_HOME%\bin

# Conecte ao WildFly
jboss-cli.bat --connect

# Execute o script (use o caminho completo ou relativo)
run-batch --file=C:\caminho\completo\para\configurar-wildfly.cli
```

> **Dica**: Se você copiar o arquivo `configurar-wildfly.cli` para a pasta `bin` do WildFly, pode executar simplesmente com `--file=configurar-wildfly.cli` sem precisar do caminho completo.

### Passo 4: Verificar Configuração

Após executar o script, verifique se:

1. O módulo PostgreSQL foi criado em `$WILDFLY_HOME/modules/org/postgresql/main/`
2. O DataSource `BeneficioDS` foi criado e está habilitado
3. O remoting está configurado corretamente

Você pode verificar através do console de administração do WildFly ou via CLI:

```bash
# Navegue até a pasta bin do WildFly
cd %WILDFLY_HOME%\bin

# Conecte ao WildFly
jboss-cli.bat --connect

# Verifique o DataSource
/subsystem=datasources/data-source=BeneficioDS:read-resource
```

### Passo 5: Criar Usuário para EJB Remoting

Crie um usuário para acesso remoto ao EJB:

```bash
# Navegue até a pasta bin do WildFly
cd %WILDFLY_HOME%\bin
# ou, se não tiver a variável configurada:
cd C:\caminho\para\wildfly\bin

# Execute o script de criação de usuário
add-user.bat

# Escolha a opção:
# a) Management User (para administração)
# b) Application User (para aplicações)

# Para EJB remoting, crie um Application User:
# Username: bip
# Password: bip
# Realm: ApplicationRealm
```

> **Nota**: O comando `add-user.bat` (ou `add-user.sh` no Linux/Mac) também precisa ser executado a partir da pasta `bin` do WildFly.

> **Nota**: As credenciais devem corresponder às configuradas em `backend-module/src/main/resources/application.properties` (linhas 27-28).

## Instalação e Execução

### Backend

#### 1. Compilar o Projeto

Na raiz do projeto, execute:

```bash
mvn clean install
```

Isso irá compilar todos os módulos na ordem correta:
1. domain-module
2. persistence-module
3. ejb-module
4. backend-module

#### 2. Deploy do EJB no WildFly

Após compilar, o módulo EJB gera **dois arquivos JAR**:

1. **`ejb-module-1.0-SNAPSHOT.jar`**: JAR padrão usado como dependência pelo backend Spring Boot (já configurado no `pom.xml`)
2. **`ejb-module-1.0-SNAPSHOT-jar-with-dependencies.jar`**: JAR com todas as dependências incluídas, usado para deploy no WildFly

> **Importante**: Apenas o JAR com sufixo `-jar-with-dependencies` deve ser deployado no WildFly, pois contém todas as dependências necessárias.

Faça o deploy do módulo EJB no WildFly:

```bash
# No Windows (PowerShell ou CMD)
copy ejb-module\target\ejb-module-1.0-SNAPSHOT-jar-with-dependencies.jar %WILDFLY_HOME%\standalone\deployments\

# No Linux/Mac
cp ejb-module/target/ejb-module-1.0-SNAPSHOT-jar-with-dependencies.jar $WILDFLY_HOME/standalone/deployments/
```

Verifique no console do WildFly se o deploy foi bem-sucedido. O arquivo `.jar.deployed` será criado no diretório `deployments/` quando o deploy for concluído com sucesso.

#### 3. Executar o Backend

```bash
cd backend-module
mvn spring-boot:run
```

Ou execute diretamente o JAR:

```bash
cd backend-module
java -jar target/backend-module-1.0-SNAPSHOT.jar
```

O backend estará disponível em: `http://localhost:8081`

#### 4. Perfis de Execução

Para executar com perfil de desenvolvimento:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Para produção:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=production
```

### Frontend

#### 1. Instalar Dependências

```bash
cd frontend
npm install
```

#### 2. Executar em Modo de Desenvolvimento

```bash
npm start
# ou
ng serve
```

O frontend estará disponível em: `http://localhost:4200`

#### 3. Build para Produção

```bash
npm run build
```

Os arquivos compilados estarão em `frontend/dist/`.

## API

### Endpoints Disponíveis

A API REST está disponível em `http://localhost:8081/api`.

#### Benefícios

- `GET /api/beneficios` - Lista todos os benefícios
- `GET /api/beneficios/{id}` - Busca um benefício por ID
- `POST /api/beneficios` - Cria um novo benefício
- `PUT /api/beneficios/{id}` - Atualiza um benefício
- `DELETE /api/beneficios/{id}` - Remove um benefício
- `POST /api/beneficios/transferir` - Transfere valor entre benefícios

### Documentação Swagger

Quando o backend estiver rodando, a documentação Swagger estará disponível em:

```
http://localhost:8081/swagger-ui.html
```

Ou:

```
http://localhost:8081/api/swagger-ui.html
```

### Exemplo de Requisição

**Criar Benefício:**
```bash
curl -X POST http://localhost:8081/api/beneficios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Benefício Teste",
    "descricao": "Descrição do benefício",
    "valor": 1500.00,
    "ativo": true
  }'
```

**Transferir Valor:**
```bash
curl -X POST http://localhost:8081/api/beneficios/transferir \
  -H "Content-Type: application/json" \
  -d '{
    "beneficioOrigemId": 1,
    "beneficioDestinoId": 2,
    "valor": 100.00
  }'
```

## Testes

### Backend

Execute os testes do backend:

```bash
# Todos os módulos
mvn test

# Apenas backend
cd backend-module
mvn test
```

### Frontend

Execute os testes do frontend:

```bash
cd frontend
npm test
```

## Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA**
- **WildFly 28** (EJB Container)
- **PostgreSQL**
- **Maven**

### Frontend
- **Angular 20**
- **Angular Material**
- **TypeScript**
- **RxJS**

### Ferramentas
- **Swagger/OpenAPI** (Documentação da API)
- **JUnit** (Testes)
- **Karma/Jasmine** (Testes Frontend)

## Notas Importantes

1. **Ordem de Inicialização**: 
   - PostgreSQL deve estar rodando
   - WildFly deve estar rodando e configurado
   - EJB deve estar deployado no WildFly
   - Backend pode ser iniciado
   - Frontend pode ser iniciado

2. **Portas Utilizadas**:
   - PostgreSQL: `5432`
   - WildFly: `8080`
   - Backend Spring Boot: `8081`
   - Frontend Angular: `4200`

3. **Configuração de CORS**: O backend está configurado para aceitar requisições do frontend em `http://localhost:4200` e `http://localhost:8080`.

## Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## Licença

Este projeto é um teste técnico.

---

