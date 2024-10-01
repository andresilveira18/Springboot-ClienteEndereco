# 1. Cliente

## 1.1 Criar um novo cliente (POST /clientes)
```bash
curl -X POST http://localhost:8080/clientes -H "Content-Type: application/json" -d "{\"nome\":\"Joao Silva\", \"email\":\"joao.silva@gmail.com\", \"cpf\":\"123.456.789-00\", \"telefone\":\"(11) 99999-9999\", \"dataNascimento\":\"1990-05-10\", \"enderecos\":[]}"
```

## 1.2 Atualizar um cliente existente (PUT /clientes/{id})
```bash
curl -X PUT http://localhost:8080/clientes/1 -H "Content-Type: application/json" -d "{\"nome\":\"Joao Silva Atualizado\", \"email\":\"joao.silva.atualizado@gmail.com\", \"cpf\":\"987.654.321-00\", \"telefone\":\"(21) 98888-8888\", \"dataNascimento\":\"1990-05-10\", \"enderecos\":[]}"
```

## 1.3 Listar todos os clientes (GET /clientes)
```bash
curl -X GET http://localhost:8080/clientes
```

## 1.4 Buscar um cliente por ID (GET /clientes/{id})
```bash
curl -X GET http://localhost:8080/clientes/1
```

## 1.5. Remover um cliente (DELETE /clientes/{id})
```bash
curl -X DELETE http://localhost:8080/clientes/1
```

# 2. Endereço

## 2.1 Adicionar um novo endereço (POST /enderecos)
```bash
curl -X POST http://localhost:8080/enderecos -H "Content-Type: application/json" -d "{\"rua\":\"Rua Exemplo\", \"numero\":\"123\", \"bairro\":\"Bairro Central\", \"cidade\":\"São Paulo\", \"estado\":\"SP\", \"cep\":\"12345-678\", \"cliente\":{\"id\":1}}"
```

## 2.2 Atualizar um endereço específico associado a um cliente (PUT /enderecos/{clienteId}/{enderecoId})
```bash
curl -X PUT http://localhost:8080/enderecos/1/1 -H "Content-Type: application/json" -d "{\"rua\":\"Nova Rua\", \"numero\":\"456\", \"bairro\":\"Novo Bairro\", \"cidade\":\"Rio de Janeiro\", \"estado\":\"RJ\", \"cep\":\"98765-432\"}"
```
Neste exemplo:
O ID do cliente é 1 e o ID do endereço é 1.
Os dados do endereço estão sendo atualizados.

## 2.3 Remover um endereço (DELETE /enderecos/{id})
```bash
curl -X DELETE http://localhost:8080/enderecos/1
```

## 2.4 Remover um endereço associado a um cliente (DELETE /enderecos/{clienteId}/{enderecoId})
```bash
curl -X DELETE http://localhost:8080/enderecos/1/1
```
Neste exemplo, estamos removendo o endereço com ID 1 que está associado ao cliente com ID 1.

## 2.5 Listar endereços associados a um cliente (GET /enderecos/cliente/{clienteId})
```bash
curl -X GET http://localhost:8080/enderecos/cliente/1
```
Este comando lista todos os endereços associados ao cliente com ID 1.