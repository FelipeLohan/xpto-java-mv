# Projeto de Controle Financeiro - Desafio XPTO

API REST desenvolvida como solução para o desafio da MV

Com base nas informações fornecidas, aqui está uma versão atualizada do README que incorpora o uso de Docker Compose e o arquivo `.env` para uma configuração de ambiente mais simplificada e robusta.

-----

## Tecnologias Utilizadas

  * **Java 17:** Versão LTS (Long-Term Support) da linguagem Java.
  * **Spring Boot:** Framework principal para a construção da aplicação e da API REST.
  * **Spring Data JPA / Hibernate:** Para a camada de persistência e comunicação com o banco de dados.
  * **Oracle Database:** Sistema de Gerenciamento de Banco de Dados relacional utilizado.
  * **Maven:** Ferramenta de gerenciamento de dependências e build do projeto.
  * **Lombok:** Biblioteca para redução de código boilerplate (getters, setters, construtores, etc.) nas entidades e DTOs.
  * **Docker & Docker Compose:** Para criação e gerenciamento de contêineres para o ambiente de desenvolvimento.

## Configuração do Ambiente com Docker

Para garantir um ambiente de desenvolvimento consistente e de fácil configuração, utilizamos o Docker Compose para orquestrar os serviços necessários, como o banco de dados.

### Pré-requisitos

  * JDK 17 ou superior.
  * Maven 3.6 ou superior.
  * Git.
  * Docker e Docker Compose.

### Passos para Configuração

1.  **Clone o repositório:**

    ```bash
    git clone git@github.com:FelipeLohan/xpto-java-mv.git
    cd xpto-java-mv
    ```

2.  **Configure as Variáveis de Ambiente:**
    O projeto utiliza um arquivo `.env` para gerenciar todas as credenciais e configurações. Para criar o seu, copie o arquivo de exemplo:

    ```bash
    cp .env.example .env
    ```

    O arquivo `.env` já contém os valores padrão necessários para a comunicação entre a aplicação e o banco de dados no ambiente Docker. Sinta-se à vontade para ajustá-lo conforme sua necessidade.

## Como Executar o Projeto

Com o Docker e os pré-requisitos instalados, siga os passos abaixo:

1.  **Inicie os Contêineres Docker:**
    Execute o comando a seguir na raiz do projeto para iniciar o banco de dados Oracle e o DBeaver (ferramenta de gerenciamento de banco de dados web):

    ```bash
    docker compose up -d
    ```

    O sufixo `-d` executa os contêineres em modo "detached" (em segundo plano).

2.  **Compile e Instale as Dependências:**
    Use o Maven para compilar o projeto e baixar todas as dependências necessárias.

    ```bash
    mvn clean install
    ```

3.  **Execute a Aplicação Spring Boot:**
    Após a conclusão do passo anterior, inicie a aplicação:

    ```bash
    mvn spring-boot:run
    ```

### Acessando os Serviços

  * **API REST:** A aplicação estará disponível em `http://localhost:8080`.
  * **DBeaver (CloudBeaver):** A interface web para gerenciamento do banco de dados estará acessível em `http://localhost:8978`. Você pode usar as credenciais definidas no seu arquivo `.env` para se conectar ao banco de dados Oracle.

## Boas Práticas de Desenvolvimento Utilizadas

Durante o desenvolvimento, diversas boas práticas foram adotadas para garantir um código limpo, manutenível e escalável.

  * **Arquitetura em Camadas:** O projeto foi estruturado em camadas de responsabilidade bem definidas, promovendo o desacoplamento e a organização do código:

      * **Controllers (`@RestController`):** Responsáveis por expor os endpoints da API REST, receber as requisições e retornar as respostas.
      * **Services (`@Service`):** Contêm a lógica de negócio da aplicação, orquestrando as operações e garantindo a aplicação das regras.
      * **Repositories (`@Repository`):** Camada de acesso a dados, que abstrai a comunicação com o banco de dados utilizando Spring Data JPA.
      * **Entities (`@Entity`):** Representam o modelo de domínio e o mapeamento para as tabelas do banco de dados.

  * **Injeção de Dependências por Construtor:** Em vez da anotação `@Autowired` em campos, foi utilizada a injeção via construtor nos Controllers e Services usando o `@RequiredArgsConstructor` do Jackson. Esta abordagem torna as dependências de um componente explícitas, facilita a criação de testes unitários e melhora a imutabilidade.

  * **Padrão DTO (Data Transfer Object):** Foram utilizados DTOs para trafegar dados entre as camadas, principalmente entre o Controller e o Service. Isso evita a exposição das entidades de persistência (`@Entity`) na API, desacoplando o contrato da API do modelo interno de dados e aumentando a segurança.

  * **Design de API RESTful:** Os endpoints foram projetados seguindo os princípios REST:

      * Uso correto dos verbos HTTP (`GET`, `POST`, `PUT`).
      * URLs claras e focadas em recursos (ex: `/api/v1/clientes/pessoas-fisicas`).
      * Retorno de códigos de status HTTP apropriados (`200 OK`, `201 Created`, `404 Not Found`).
      * Para a criação de recursos (`POST`), a API retorna o status `201 Created`.

  * **Tratamento de Exceções:** A aplicação utiliza exceções customizadas (`BusinessLogicException`) e padrão (`EntityNotFoundException`) para lidar com erros de negócio e de dados, fornecendo respostas claras e adequadas na API.

  * **Gerenciamento de Relacionamentos Bidirecionais:** Para evitar problemas de recursão infinita na serialização JSON, foram utilizadas as anotações `@JsonManagedReference` e `@JsonBackReference` da biblioteca Jackson.

  * **Gerenciamento de Transações:** A anotação `@Transactional` foi utilizada nos métodos de serviço que realizam operações de escrita, garantindo a atomicidade das operações. Para métodos de leitura, foi usada a otimização `@Transactional(readOnly = true)`.

  * **Uso de variáveis de ambiente**: Foi utilizado variáveis de ambiente na configuração do docker e application.properties, essa abordagem permite maior segurança com dados sensiveis.

## Padrões de Projeto Utilizados

  * **Repository Pattern:** Abstrai a camada de acesso a dados, permitindo que a camada de serviço interaja com uma interface que esconde os detalhes da implementação da persistência. Este padrão é fornecido nativamente pelo Spring Data JPA.

  * **Dependency Injection Pattern:** Utilizado extensivamente pelo Spring Framework para gerenciar o ciclo de vida dos componentes e suas dependências. Adotamos a forma de injeção via construtor.

  * **Data Transfer Object (DTO) Pattern:** Utilizado para transferir dados entre as camadas da aplicação (principalmente na API), evitando o acoplamento com as entidades de persistência.

  * **Facade Pattern:** O `ClienteService` atua como uma fachada, simplificando uma operação complexa — a criação de um cliente completo (com Conta, Endereço e Movimentação inicial) — em uma única chamada de método.

  * **Singleton Pattern:** O Spring gerencia todos os beans (`@Service`, `@Repository`, `@RestController`, etc.) como Singletons por padrão, garantindo que exista apenas uma instância de cada componente na aplicação.