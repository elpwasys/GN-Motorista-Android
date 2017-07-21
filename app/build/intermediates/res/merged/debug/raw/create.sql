create table endereco (
    _id integer primary key autoincrement,
    cidade text not null,
    completo text not null,
    latitude number not null,
    longitude number not null
);

create table realizado (
    _id integer primary key autoincrement,
    inicio_id integer not null,
    termino_id integer not null,
    solicitacao_id number not null,
    data text not null,
    duracao number not null,
    distancia number not null,
    pedagio number null,
    estacionamento number null,
    foreign key (inicio_id) references endereco (_id),
    foreign key (termino_id) references endereco (_id)
);

create table marcacao (
    _id integer primary key autoincrement,
    realizado_id number null,
    solicitacao_id number not null,
    data text not null,
    ordem integer not null,
    latitude number not null,
    longitude number not null,
    foreign key (realizado_id) references realizado (_id)
);

