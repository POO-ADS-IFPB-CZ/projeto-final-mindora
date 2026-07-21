-- ============================================================
-- Mindora — Checkpoint 2: Criação do banco de dados
-- SGBD: PostgreSQL
-- Banco: mindora
-- ============================================================

-- Cria o banco (executar conectado ao postgres, fora do mindora)
-- Observação: se o banco já existir, o script aborta com erro.
-- Para recriar do zero, use: DROP DATABASE mindora; antes.

CREATE DATABASE mindora
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    TEMPLATE = template0;

-- Após rodar este script, conecte-se ao banco mindora
-- (\c mindora no psql) e execute 02_create_tables.sql
