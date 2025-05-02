# Order Manager API  

API para gestão de encomendas e movimentos de stock, com simulação de envio de emails e geração de registos (logs).  

## Tecnologias Utilizadas  

- **Java 8** e **Java EE**  
- **Hibernate** (ORM)  
- **PostgreSQL** (executado via Docker)  
- **Jersey** (JAX-RS para serviços REST)  
- **Maven** (gestão de dependências)  
- **Jetty** (servidor embutido)  
- **Log4j2** (sistema de logging)  

## Instruções de Execução  

**Pré-requisitos:**  
- Java 8 ou superior instalado  
- Maven instalado  
- Docker instalado e em execução  
- Terminal (Git Bash no Windows ou Linux/macOS)  

### Comandos Úteis  

**Para iniciar a aplicação e a base de dados (Docker):**  
```bash  
./start.sh  
```  

**Para parar a aplicação e os containers Docker:**
```bash  
./stop.sh  
```  

**URL de acesso após inicialização:**  
`http://localhost:8080/api`

## Endpoints Disponíveis

### Utilizadores
- `GET    /users` → Listar todos
- `GET    /users/{id}` → Obter por ID
- `POST   /users` → Criar novo
- `PUT    /users/{id}` → Atualizar
- `DELETE /users/{id}` → Remover

### Artigos
- `GET    /items` → Listar todos
- `GET    /items/{id}` → Obter por ID
- `POST   /items` → Criar novo
- `PUT    /items/{id}` → Atualizar
- `DELETE /items/{id}` → Remover

### Encomendas
- `GET    /orders` → Listar todas
- `GET    /orders/{id}` → Obter por ID
- `POST   /orders` → Criar nova
- `PUT    /orders/{id}` → Atualizar
- `DELETE /orders/{id}` → Remover
- `GET    /orders/{id}/progress` → Ver progresso
- `GET    /orders/{id}/stock-movements` → Movimentos de stock associados

### Movimentos de Stock
- `GET    /stock-movements` → Listar todos
- `GET    /stock-movements/{id}` → Obter por ID
- `POST   /stock-movements` → Registar novo
- `PUT    /stock-movements/{id}` → Atualizar
- `DELETE /stock-movements/{id}` → Remover

## Notas Adicionais

- **Base de dados:** As tabelas são criadas automaticamente no arranque.
- **Emails:** Simulados através de logs no terminal.
- **Registos (logs):** Armazenados em `logs/ordermanager.log`.
- **Coleção Postman:** Existe uma coleção Postman na raiz do projeto (`Order Manager API.postman_collection.json`) que pode ser usada para testar os endpoints facilmente.
```