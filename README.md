# Alimentech API

O projeto Alimentech visa oferecer uma solução para pessoas com insegurança alimentar moderada ou alta, que enfrentam dificuldades para obter alimentos que atendam às suas necessidades nutricionais básicas. A proposta é desenvolver um programa educacional online que utiliza inteligência artificial generativa para ensinar técnicas de agricultura sustentável, como agricultura vertical e aquaponia. A plataforma proporcionará orientações personalizadas de acordo com as características e recursos disponíveis para cada usuário, tornando a produção de alimentos de qualidade acessível.

## Integrantes do grupo:
- RM 93442 – Gabriel Hitoshi Furone Yokogawa
- RM 93150 – Guilherme Martins Nascimento
- RM 93205 – Luis Fernando Nascimento de Oliveira
- RM 94990 – Pedro Augusto Pereira Viana
- RM 95800 – Yasmin Cabral Dias

## Link do Pitch
- https://youtu.be/Wk6lKO3An-c

## Visão Geral
A solução da Alimentech é um aplicativo móvel que oferece uma interface acessível e de fácil utilização para pessoas que desejam aprender práticas de agricultura sustentável. O aplicativo utiliza inteligência artificial generativa, especificamente o ChatGPT, para gerar conteúdo informativo e instrucional adaptado às necessidades e recursos de cada usuário. Os principais recursos são:

- **Perfil do Usuário:** Após se cadastrar, os usuários criam um perfil fornecendo informações básicas, como nome, e-mail e senha.
- **Login**: Os usuários podem fazer login no aplicativo usando o e-mail e a senha cadastrados.
- **Questionário:** Após o login, os usuários são solicitados a preencher um questionário que coleta informações importantes sobre sua localização, espaço disponível para o cultivo, prazo desejado para a colheita e capacidade de investimento.
- **Instruções Geradas pela IA:** As informações coletadas dos usuários são processadas pelo ChatGPT, que gera um guia abrangente e didático, com instruções passo a passo, para a montagem de uma área de cultivo com base nos requisitos específicos do usuário.
- **Salvar Instruções:** Os usuários têm a opção de salvar as instruções geradas para consultas futuras, permitindo que eles as acessem a qualquer momento.
- **Assistente de Voz:** O aplicativo também possui um assistente de voz para usuários com dificuldades de leitura ou escrita.
---

# Arquitetura

<img align="center" alt="Diagrama-UML" src="ArquiteturaAlimentech.jpeg">

---

## Pré-requisitos
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

---

## Teste os endpoints por aqui
- Postman: https://www.postman.com/altimetry-astronaut-37339768/workspace/alimentech/collection/23202593-1f7b59a0-b414-465c-abaa-9aaa09dabb14?action=share&creator=23202593

---

## Clonando o projeto
- Clone o repositório em sua máquina:

```bash
{
	git clone https://github.com/augustopdro/alimentech-dbe.git
}
```

---
---

## Entre no diretório do projeto:

```bash
{
	cd seu-projeto
}
```

---
---

## Executando o projeto
- Navegue até a pasta do projeto clonado e execute o seguinte comando:

```bash
{
	docker-compose up
}
```

O Docker Compose irá baixar e executar as imagens necessárias e o projeto será executado em localhost:8080.

---
---

## Configurações
- A porta padrão da API é 8080. Se desejar mudá-la, altere a configuração no arquivo docker-compose.yml.

- O usuário do banco de dados padrão é normaluser e a senha é 140203. Se desejar alterar essas configurações, modifique o arquivo docker-compose.yml e o arquivo Dockerfile.db.

- Os dados do banco de dados são persistidos em um volume chamado db-volume. Se você deseja persistir os dados em um diretório diferente, altere a configuração no arquivo docker-compose.yml.

---
---

---

## Endpoints
- Usuário
  - [Cadastrar](#cadastrar)
  - [Atualizar Cadastro](#atualizar-cadastro)
  - [Login](#login)
- Agricultura
  - [Recurso](#recurso)
  - [Deletar Instrução](#deletar-instrução)
  - [Histórico](#histórico)
  - [Instrução](#instrução)

---

## Cadastrar
`POST` /api/usuario/cadastrar

| Campo | Tipo | Obrigatório | Descrição
|:-------:|:------:|:-------------:|--
| nome | string | sim | é o nome do usuário, deve respeitar o Regex(^[a-zA-Z]{3,}$)
| email | string | sim | é o email do usuário, deve respeitar o ReGex(^[A-Za-z0-9+_.-]+@(.+)$)
| senha | string | sim | é a senha do usuário, deve ter no mínimo 8 caracteres
| cidade | string | sim | é a cidade do usuário


**Exemplo de corpo do request**
```js
{
	"nome": "Pedro Augusto",
	"email": "pedro.silva@gmail.com",
	"senha": "Senha123",
	"cidade": "São Paulo"
}
```

**Exemplo de corpo de response**
```js
{
    "id": 1,
    "nome": "Pedro Augusto",
    "email": "pedro.silva@gmail.com",
    "senha": "$2a$10$GqssjsKV5bTF1h9g0G13X.mEKH5WyKI6JxKNNnI1sdN4pLqA9jjqy",
    "cidade": "São Paulo"
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/usuario/cadastrar"
        },
        "atualizar": {
            "href": "http://localhost:8080/api/usuario/2"
        },
        "logar": {
            "href": "http://localhost:8080/api/usuario/login"
        }
    }
}
```

**Códigos de Resposta**
| Código | Descrição
|:-:|-
| 201 | Usuario cadastrado com sucesso
| 400 | Erro na requisição

---

---

## Atualizar Cadastro
`PUT` /api/usuario/{id}

**Exemplo de corpo do request**
```js
{
	"nome": "Pedro Augusto",
	"email": "pedro.silva@gmail.com",
	"senha": "Senha123",
	"cidade": "Rio de Janeiro"
}
```

**Códigos de Resposta**
| Código | Descrição
|:-:|-
| 200 | Usuario atualizado com sucesso
| 400 | Erro na requisição
| 404 | Usuario não encontrado

---

---

## Login
`POST` /api/usuario/login

| Campo | Tipo | Obrigatório | Descrição
|:-------:|:------:|:-------------:|--
| email | string | sim | é o email cadastrado pelo usuário
| senha | string | sim | é a senha cadastrada pelo usuário

**Exemplo de corpo do request**
```js
{
	"email": "usuario@email.com",
	"senha": "senha123"
}
```

**Exemplo de corpo do response**

| Campo | Tipo | Descrição
|:-------:|:------:|-------------
|token | string | token do usuario que identifica o usuário no sistema

```js
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJwZWRyb0BlbWFpbC5jb20uYnIiLCJleHAiOjE2ODQ3ODk5NTcsImlzcyI6IkRyZWFtQ29udHJvbCJ9.dii4kCQsnJEpl4ycu8Z2Mh687_0234MkyNIh_sZPPcQ",
    "type": "JWT",
    "prefix": "Bearer",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/usuario/login"
        },
        "cadastrar": {
            "href": "http://localhost:8080/api/usuario/cadastrar"
        }
    }
}
```

**Códigos de Resposta**
| Código | Descrição
|:-:|-
| 200 | Usuario validado com sucesso
| 401 | Usuário ou Senha incorreto

---

---

## Recurso
`POST` /api/agricultura/{userId}/recurso

| Campo | Tipo | Obrigatório | Descrição
|:-------:|:------:|:-------------:|--
| areaDoTerreno | string | sim | é a area do terreno disponivel para plantio
| dinheiroDisponivel | string | sim | é o dinheiro que o usuario tem para investir em sementes
| prazoParaColheita | string | sim | é o prazo que o usuario está disposto a esperar pela colheita



**Exemplo de corpo do request**
```js
{
    "areaDoTerreno": "10 metros",
    "dinheiroDisponivel": "70 reais",
    "prazoParaColheita": "6 meses"
}
```

**Códigos de Resposta**
| Código | Descrição
|:-:|-
| 201 | Recurso cadastrado com sucesso
| 400 | Erro na requisição
| 404 | Usuario não encontrado
| 422 | Erro ao processar a requisição

---

`GET` /api/agricultura/{userId}/recurso

**Exemplo de corpo do response**

| Campo | Tipo | Descrição
|:-------:|:------:|-------------
| areaDoTerreno | string | é a area do terreno disponivel para plantio
| dinheiroDisponivel | string | é o dinheiro que o usuario tem para investir em sementes
| prazoParaColheita | string | é o prazo que o usuario está disposto a esperar pela colheita

```js
{
    "id": 1,
    "areaDoTerreno": "10 metros",
    "dinheiroDisponivel": "70 reais",
    "prazoParaColheita": "6 meses",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/agricultura/1/recurso"
        },
        "recuperar": {
            "href": "http://localhost:8080/api/agricultura/1/recurso"
        }
    }
}
```

**Códigos de Resposta**
| Código | Descrição
|:-:|-
| 200 | Recurso recuperado com sucesso
| 404 | Usuario não encontrado
| 404 | Objetivo não encontrado
| 400 | Erro na requisição

---

---


## Deletar Instrução
`DELETE` /api/agricultura/{userId}/deletar/{instrucaoId}

**Códigos de Resposta**
| Código | Descrição
|:-:|-
| 204 | Objeto deletado com sucesso
| 404 | Usuario não encontrado
| 404 | Objeto não encontrado

---

---

## Histórico
`GET` /api/agricultura/{userId}/historico

| Campo | Tipo | Descrição
|:-------:|:------:|--
| instrucao | ArrayList<Instrucao> | é a lista que contém as instruções geradas pelo chatGPT.

**Exemplo de corpo do response**
```js
{
    "content": [
        {
            "id": 1,
            "texto": "Texto de resposta gerado pela api",
            "data": "2023-04-03"
        },
        {
            "id": 2,
            "texto": "Texto de resposta gerado pela api",
            "data": "2023-04-03"
        }
    ],
    "number": 0,
    "totalElements": 2,
    "totalPages": 1,
    "first": true,
    "last": true,
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/agricultura/1/historico"
        },
        "relatorio": {
            "href": "http://localhost:8080/api/agricultura/1/recurso"
        }
    }
}
```

**Códigos de Resposta**
| Código | Descrição
|:-:|-
| 200 | Historico recuperado com sucesso
| 400 | Erro na requisição
| 404 | Usuario não encontrado
| 404 | Historico não encontrado

---

---

## Instrução
`GET` /api/agricultura/{userId}/instrucao

| Campo | Tipo | Descrição
|-------|------|--
| texto | string | é o texto gerado pelo gpt
| data | LocalDate | é a data que o texto foi gerado


**Exemplo de corpo do response**
```js
{
	"id": 1,
	"texto": "Texto de resposta gerado pela api",
	"data": "2023-04-03"
}
```

**Códigos de Resposta**
| Código | Descrição
|:-:|-
| 200 | Instrução recuperada com sucesso
| 400 | Erro na requisição
| 404 | Usuario não encontrado

---
