---
title: Java Quarkus
description: A Quarkus starter app.
tags:
  - Java
  - Quarkus
---

# Quarkus Example

This is a [Quarkus](https://quarkus.io) starter app that deploys to Railway.

[![Deploy on Railway](https://railway.com/button.svg)](https://railway.com/template/orZ9Pj?referralCode=D-ZQFL)

## ✨ Features

- Java
- Quarkus

# Projeto Global Solution - Elo

O Elo é uma plataforma digital desenvolvida com o objetivo de conectar voluntários a moderadores responsáveis por coordenar ações de resposta e ajuda em situações de enchentes. O projeto visa fortalecer a rede comunitária em momentos críticos, permitindo que cidadãos engajados possam colaborar diretamente com missões de apoio e resgate, enquanto moderadores organizam, divulgam e gerenciam as ações necessárias.

## Documentação da API

###  Usuário - Base URL.


  http://localhost:8080/usuario


#### Rotas para Usuário.

| Método   | Rota               | Descrição                                                        | Status Codes | Header                        |
| :------- | :----------------- | :--------------------------------------------------------------- |:-------------|:----------------------------- |
| `GET`    | `/usuario`         | Retorna a lista de todos os usuários cadastrados.                | 200          |                               |
| `GET`    | `/usuario/{id}`    | Retorna um único usuário pelo ID.                                | 200, 404     |                               |
| `POST`   | `/usuario/cadastro`| Cria um novo usuário.                                            | 201, 400     |                               |
| `POST`   | `/usuario/login`   | Realiza login e retorna um token JWT.                            | 200, 401    |                               |
| `PUT`    | `/usuario/{id}`    | Atualiza os dados de um usuário (apenas se autenticado com JWT). | 200, 400     | Authorization: Bearer {token} |
| `DELETE` | `/usuario/{id}`    | Deleta o usuário com o ID especificado.       | 200          ||
 
#### Body - Exemplo para Cadastro.
```http
  {
    "nome": "Usuário Elo",
    "email": "usuario.elo@gmail.com",
    "categoria": "Moderador",
    "telefone": "(11) 93476-8123",
    "senha": "exemploElo123",
    "confirmaSenha": "exemploElo123"
  }
``` 
#### Body - Exemplo para Login.
```http
  {
    "email": "usuario.elo@gmail.com",
    "senha": "exemploElo123"
  }
```
#### Body - Exemplo para atualizar Usuário.
```http
  {
    "nome": "Elo User",
    "email": "usuario.elo.exemplo@gmail.com",
    "categoria": "Moderador",
    "telefone": "(11) 93476-8123",
    "senha": "exemploElo456",
    "confirmaSenha": "exemploElo456"
  }
```

###  Missão - Base URL.


  http://localhost:8080/missao


#### Rotas para Missão.

| Método   | Rota           | Descrição                                   | Status Codes |
| :------- | :------------- | :------------------------------------------ |:-------------|
| `GET`    | `/missao`      | Retorna a lista de todas as missões criadas.| 200          |
| `GET`    | `/missao/listar`| Retorna todas missões não deletadas.       | 200          |
| `GET`    | `/missao/{id}` | Retorna uma única missão pelo ID.           | 200, 404     |
| `POST`   | `/missao/criar`| Cria uma nova missão.                       | 201, 400     |
| `PUT`    | `/missao/{id}` | Atualiza os dados de uma missão.            | 200          |
| `DELETE` | `/missao/{id}` | Deleta a missão com o ID especificado.      | 204          |

#### Body - Exemplo para criar Missão.
```http
  {
  "titulo": "Ação comunitária no Jardim Romano após enchente",
  "localizacao": "Avenida Doutor José Arthur Nova, Jardim Romano - São Paulo/SP",
  "organizacao": "Voluntários Unidos",
  "descricao": "Mutirão no dia 06/06/2025 às 13:30 para ajudar na limpeza das casas afetadas, distribuição de alimentos e arrecadação de móveis básicos para as famílias atingidas pela enchente."
  }
```

#### Body - Exemplo para atualizar Missão.
```http
  {
  "titulo": "Ação comunitária no Jardim Romano após enchente",
  "localizacao": "Avenida Doutor José Arthur Nova, Jardim Romano - São Paulo/SP",
  "organizacao": "Voluntários Unidos Sempre",
  "descricao": "Mutirão no dia 10/06/2025 às 13:30 para ajudar na limpeza das casas afetadas, distribuição de alimentos e arrecadação de móveis básicos para as famílias atingidas pela enchente."
  }
```

###  Alerta - Base URL.

  http://localhost:8080/alerta


#### Rotas para Alerta.

| Método   | Rota           | Descrição                                   | Status Codes  |
| :--------| :------------- | :------------------------------------------ | :------------ |
| `GET`    | `/alerta`      | Retorna a lista de todos os alertas criados.| 200           |
| `GET`    | `/alerta/listar`| Retorna todos alertas não deletados        | 200           |
| `GET`    | `/alerta/{id}` | Retorna um único alerta pelo ID.            | 200, 404      |
| `POST`   | `/alerta/criar`| Cria um novo alerta.                        | 201, 400      |
| `PUT`    | `/alerta/{id}` | Atualiza os dados de um alerta.             | 200           | 
| `DELETE` | `/alerta/{id}` | Deleta o alerta com o ID especificado.      | 204           |

#### Body - Exemplo para criar Alerta.
```http
  {
    "titulo": "Alagamento no Itaim Paulista após tempestade",
    "localizacao": "Avenida Marechal Tito, Itaim Paulista - São Paulo/SP",
    "descricao": "Chuvas fortes causaram alagamento nas ruas e prejudicaram o transporte público.",
    "recomendacao": "Permaneça em locais seguros, evite contato com a água da enchente e não dirija por ruas alagadas."
  }
```

#### Body - Exemplo para atualizar Alerta.
```http
  {
    "titulo": "Alagamento no Itaim Paulista após tempestade intensa",
    "localizacao": "Avenida Marechal Tito, Itaim Paulista - São Paulo/SP",
    "descricao": "Chuvas intensas causaram alagamento nas ruas e prejudicaram o transporte público.",
    "recomendacao": "Permaneça em um local seguro, evite contato com a água da enchente e não dirija por ruas alagadas."
  }
```
