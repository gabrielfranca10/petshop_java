# Sistema PetShop — Interface Desktop Java

## Tecnologias
- **Java 11+** — linguagem do backend
- **Swing** — interface gráfica desktop
- **JDBC puro** — conexão com o banco (sem ORM, sem frameworks)
- **PostgreSQL** — banco de dados

---

## Estrutura do Projeto

```
petshop/
└── src/main/java/petshop/
    ├── Main.java                  ← Ponto de entrada
    ├── db/
    │   └── Conexao.java           ← Conexão JDBC singleton
    ├── model/
    │   ├── Tutor.java
    │   └── Pet.java
    ├── dao/
    │   ├── TutorDAO.java          ← SQL puro: INSERT, UPDATE, DELETE, SELECT
    │   └── PetDAO.java            ← SQL puro: INSERT, UPDATE, DELETE, SELECT
    └── ui/
        ├── JanelaPrincipal.java   ← Janela com abas
        ├── PainelTutor.java       ← CRUD de Tutores
        └── PainelPet.java         ← CRUD de Pets
```

---

## Configuração do Banco

1. Crie o banco no PostgreSQL:
```sql
CREATE DATABASE petshop;
```

2. Execute os scripts na ordem:
```
criar_tabelas_petshop.sql
inserir_dados_petshop.sql
```

3. Se necessário, ajuste as credenciais em `Conexao.java`:
```java
private static final String URL     = "jdbc:postgresql://localhost:5432/petshop";
private static final String USUARIO = "postgres";
private static final String SENHA   = "postgres";
```

---

## Compilação e Execução

### Pré-requisitos
- JDK 11 ou superior
- Driver JDBC do PostgreSQL: [postgresql-42.x.x.jar](https://jdbc.postgresql.org/download/)

### Compilar
```bash
# Coloque o JAR do PostgreSQL em lib/
mkdir -p lib
# Baixe o driver JDBC e coloque em lib/postgresql-42.7.10.jar

# Compilar todos os fontes
javac -cp "lib/postgresql-42.7.10.jar" -d out \
  src/main/java/petshop/db/Conexao.java \
  src/main/java/petshop/model/Tutor.java \
  src/main/java/petshop/model/Pet.java \
  src/main/java/petshop/dao/TutorDAO.java \
  src/main/java/petshop/dao/PetDAO.java \
  src/main/java/petshop/ui/PainelTutor.java \
  src/main/java/petshop/ui/PainelPet.java \
  src/main/java/petshop/ui/JanelaPrincipal.java \
  src/main/java/petshop/Main.java
```

### Executar
```bash
java -cp "out:lib/postgresql-42.7.10.jar" petshop.Main
# No Windows use ; em vez de :
java -cp "out;lib/postgresql-42.7.10.jar" petshop.Main
```

---

## Funcionalidades

### Aba Tutores
| Operação | Como usar |
|----------|-----------|
| **Inserir** | Preencha os campos e clique em "Inserir" |
| **Alterar** | Clique em um tutor na tabela, edite os campos e clique em "Alterar" |
| **Deletar** | Clique em um tutor na tabela e clique em "Deletar" |

### Aba Pets
| Operação | Como usar |
|----------|-----------|
| **Inserir** | Preencha nome, espécie, raça, peso, selecione o tutor e clique em "Inserir" |
| **Alterar** | Clique em um pet na tabela, edite os campos e clique em "Alterar" |
| **Deletar** | Clique em um pet na tabela e clique em "Deletar" |

> O campo **Cód. Pet** é gerado automaticamente pela SEQUENCE do banco (SERIAL).

---

## Operações SQL utilizadas

Todas as queries são `PreparedStatement` — protegidas contra SQL Injection:

```java
// Exemplo — inserção de Pet (PetDAO.java)
String sql = "INSERT INTO pet (nome, especie, raca, peso, cpf_tutor) VALUES (?, ?, ?, ?, ?)";
PreparedStatement ps = conexao.prepareStatement(sql);
ps.setString(1, p.getNome());
// ...
ps.executeUpdate();
```
