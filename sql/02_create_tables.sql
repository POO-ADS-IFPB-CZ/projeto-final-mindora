-- ============================================================
-- Mindora — Checkpoint 2: Criação das tabelas (esquema lógico)
-- SGBD: PostgreSQL
-- Conectar antes: \c mindora
-- ============================================================

-- 1) ALUNO --------------------------------------------------------
CREATE TABLE aluno (
    id          SERIAL          PRIMARY KEY,
    nome        VARCHAR(120)    NOT NULL,
    data_nasc   DATE            NOT NULL,
    nivel_sup   VARCHAR(50)     NOT NULL,
    observacao  TEXT
);

-- 2) RESPONSAVEL --------------------------------------------------
CREATE TABLE responsavel (
    id    SERIAL       PRIMARY KEY,
    nome  VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE
);

-- 3) ALUNO x RESPONSAVEL (N:N) ------------------------------------
CREATE TABLE aluno_responsavel (
    aluno_id        INTEGER NOT NULL,
    responsavel_id  INTEGER NOT NULL,
    PRIMARY KEY (aluno_id, responsavel_id),
    CONSTRAINT fk_ar_aluno
        FOREIGN KEY (aluno_id)        REFERENCES aluno (id)       ON DELETE CASCADE,
    CONSTRAINT fk_ar_responsavel
        FOREIGN KEY (responsavel_id)  REFERENCES responsavel (id) ON DELETE CASCADE
);

-- 4) RESPONSAVEL_TELEFONE (atributo multivalorado) ---------------
CREATE TABLE responsavel_telefone (
    id              SERIAL       PRIMARY KEY,
    responsavel_id  INTEGER      NOT NULL,
    telefone        VARCHAR(20)  NOT NULL,
    CONSTRAINT fk_rt_responsavel
        FOREIGN KEY (responsavel_id) REFERENCES responsavel (id) ON DELETE CASCADE
);

-- 5) PROFISSIONAL -------------------------------------------------
CREATE TABLE profissional (
    id            SERIAL       PRIMARY KEY,
    nome          VARCHAR(120) NOT NULL,
    especialidade VARCHAR(80)  NOT NULL,
    registro      VARCHAR(50)  NOT NULL UNIQUE
);

-- 6) ATIVIDADE ----------------------------------------------------
CREATE TABLE atividade (
    id         SERIAL        PRIMARY KEY,
    titulo     VARCHAR(120)  NOT NULL,
    tipo       VARCHAR(60)   NOT NULL,
    descricao  TEXT,
    nivel      VARCHAR(20)   NOT NULL,
    CONSTRAINT ck_atividade_nivel
        CHECK (nivel IN ('basico', 'intermediario', 'avancado'))
);

-- 7) SESSAO (sem FK para atividade — é N:N) ----------------------
CREATE TABLE sessao (
    id              SERIAL         PRIMARY KEY,
    data            DATE           NOT NULL,
    duracao_min     INTEGER        NOT NULL,
    status          VARCHAR(20)    NOT NULL,
    nota            NUMERIC(4, 2),
    aluno_id        INTEGER        NOT NULL,
    profissional_id INTEGER        NOT NULL,
    CONSTRAINT ck_sessao_duracao  CHECK (duracao_min > 0),
    CONSTRAINT ck_sessao_status
        CHECK (status IN ('agendada', 'realizada', 'cancelada')),
    CONSTRAINT ck_sessao_nota
        CHECK (nota IS NULL OR (nota >= 0 AND nota <= 10)),
    CONSTRAINT fk_sessao_aluno
        FOREIGN KEY (aluno_id)        REFERENCES aluno (id)        ON DELETE RESTRICT,
    CONSTRAINT fk_sessao_profissional
        FOREIGN KEY (profissional_id) REFERENCES profissional (id) ON DELETE RESTRICT
);

-- 8) SESSAO x ATIVIDADE (N:N) ------------------------------------
-- Como toda sessão precisa ter pelo menos 1 atividade (Sessão 1,N),
-- a regra de "no mínimo 1" será garantida pela aplicação
-- (ou por trigger, se desejado) — não há CHECK que cubra N:N no Postgres.
CREATE TABLE sessao_atividade (
    sessao_id    INTEGER NOT NULL,
    atividade_id INTEGER NOT NULL,
    PRIMARY KEY (sessao_id, atividade_id),
    CONSTRAINT fk_sa_sessao
        FOREIGN KEY (sessao_id)    REFERENCES sessao (id)    ON DELETE CASCADE,
    CONSTRAINT fk_sa_atividade
        FOREIGN KEY (atividade_id) REFERENCES atividade (id) ON DELETE CASCADE
);

-- ============================================================
-- Fim do esquema lógico. Próximo passo: 03_inserts_exemplo.sql
-- (opcional, para popular o banco com dados de teste).
-- ============================================================
