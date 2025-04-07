
# 📌 Banco Digital

Esse projeto é uma API REST de um banco Digital, desenvolvido em Java. O objetivo é aplicar conceitos de orientação a objetos, frameworks e persistência de dados.


# 🚀 Funcionalidades

✅ Cadastro e gerenciamento de clientes  
✅ Abertura, consulta, atualização e exclusão de contas bancárias  
✅ Emissão e administração de cartões  
✅ Realização de pagamentos e simulação de saldo  
✅ Tipos de clientes com vantagens  
✅ Tratamento de exceções personalizado  
✅ Persistência em banco de dados (H2 em memória)  
✅ Arquitetura baseada em camadas (Controller, Service, Repository, etc.)

# 🛠️ Tecnologias Utilizadas
- **Java 21**
- **Spring Boot** – Framework para facilitar o desenvolvimento e configuração  
- **Spring Data JPA** – Alternativa para persistência de dados com JPA  
- **H2 Database** – Banco de dados em memória para testes e desenvolvimento  
- **Bean Validation (Jakarta Validation)** – Validação de dados via anotações  
- **Lombok** – Geração automática de getters, setters, constructors, etc.  
- **Maven** – Gerenciador de dependências e build  
- **Postman** – Para testes dos endpoints REST

- ## 📂 Estrutura do Projeto

```plaintext
banco-digital
┣ 📂 src
┃ ┣ 📂 controller    # Endpoints da API 
┃ ┣ 📂 dto           # Objetos de transferência de dados 
┃ ┣ 📂 entity        # Entidades JPA que representam as tabelas do banco 
┃ ┣ 📂 repository    # Interfaces para acesso ao banco de dados com Spring Data JPA
┃ ┣ 📂 service       # Regras de negócio e lógica de aplicação
┃ ┣ 📂 exceptions    # Exceções personalizadas para regras de negócio
┃ ┣ 📂 handler       # Manipulação global de erros com @ControllerAdvice
┣ 📄 README.md       # Documentação do projeto
┣ 📄 pom.xml         # Arquivo de build com dependências
```
# 🔧 Como Executar o Projeto
1️⃣ Clone o repositório:
```bash
git clone https://github.com/seu-usuario/banco-digital.git
cd banco-digital
```

2️⃣ Execute a aplicação:

```bash
./mvnw spring-boot:run
Ou, se preferir, execute a main() da classe principal em sua IDE favorita (IntelliJ, Eclipse, VS Code).
```

3️⃣ Acesse o banco H2 (opcional):
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Usuário: sa
Senha: (deixe em branco)
```

# 🛡️ Tratamento de Exceções

O projeto possui um sistema de tratamento de erros personalizado usando:

- **Exceções customizadas na pasta exceptions**

- **Classe global de handler na pasta handler, utilizando @ControllerAdvice**

Dessa forma, Qualquer erro de negócio ou validação retorna uma resposta clara e padronizada pro cliente da API.

# 📈 Melhorias Futuras

🔹 Persistência com banco de dados real (PostgreSQL ou MySQL)  
🔹 Autenticação com Spring Security (JWT ou Basic Auth)  
🔹 Testes unitários com JUnit e Mockito  
🔹 Documentação de API com Swagger  
🔹 Interface frontend (React ou Angular) para visualização dos dados  

# 📌 Autor
👨‍💻 Gustavo Matachun Domingues
🔗 [LinkedIn](https://www.linkedin.com/in/gustavo-matachun/) | 📧 gustavomatachun.domingues@gmail.com

