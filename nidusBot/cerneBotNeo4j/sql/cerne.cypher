// Criação dos nós Cerne
CREATE (cerne1:Cerne {id: 1, descricao: 'Cerne 1'});
CREATE (cerne2:Cerne {id: 2, descricao: 'Cerne 2'});
CREATE (cerne3:Cerne {id: 3, descricao: 'Cerne 3'});
CREATE (cerne4:Cerne {id: 4, descricao: 'Cerne 4'});

// Criação dos nós Pratica
CREATE (pratica1:Pratica {codigo: '1.1.1', descricao: 'Sensibilização'});
CREATE (pratica2:Pratica {codigo: '1.1.2', descricao: 'Prospecção'});
CREATE (pratica3:Pratica {codigo: '1.1.3', descricao: 'Qualificação de Potenciais Empreendedores'});
CREATE (pratica4:Pratica {codigo: '1.2.1', descricao: 'Recepção de Propostas'});
CREATE (pratica5:Pratica {codigo: '1.2.2', descricao: 'Avaliação'});
CREATE (pratica6:Pratica {codigo: '1.2.3', descricao: 'Contratação'});
CREATE (pratica7:Pratica {codigo: '1.3.1', descricao: 'Planejamento'});
CREATE (pratica8:Pratica {codigo: '1.3.2', descricao: 'Agregação de Valor'});
CREATE (pratica9:Pratica {codigo: '1.3.3', descricao: 'Monitoramento'});
CREATE (pratica10:Pratica {codigo: '1.4.1', descricao: 'Graduação'});
CREATE (pratica11:Pratica {codigo: '1.4.2', descricao: 'Relacionamento com Graduadas'});
CREATE (pratica12:Pratica {codigo: '1.5.1', descricao: 'Estrutura Organizacional'});
CREATE (pratica13:Pratica {codigo: '1.5.2', descricao: 'Operação da Incubadora'});
CREATE (pratica14:Pratica {codigo: '1.5.3', descricao: 'Comunicação e Marketing'});
CREATE (pratica15:Pratica {codigo: '2.1.1', descricao: 'Planejamento Estratégico'});
CREATE (pratica16:Pratica {codigo: '2.1.2', descricao: 'Administração Estratégica'});
CREATE (pratica17:Pratica {codigo: '2.2.1', descricao: 'Ambientes de Ideação'});
CREATE (pratica18:Pratica {codigo: '2.2.2', descricao: 'Serviços a Organizações'});
CREATE (pratica19:Pratica {codigo: '2.3.1', descricao: 'Avaliação da Qualidade'});
CREATE (pratica20:Pratica {codigo: '2.3.2', descricao: 'Avaliação dos Impactos'});
CREATE (pratica21:Pratica {codigo: '3.1.1', descricao: 'Interação com o Entorno'});
CREATE (pratica22:Pratica {codigo: '3.1.2', descricao: 'Participação na Proposição de Políticas Públicas'});
CREATE (pratica23:Pratica {codigo: '3.2.1', descricao: 'Rede de Mentores'});
CREATE (pratica24:Pratica {codigo: '3.2.2', descricao: 'Gestão de Ofertas e Demandas'});
CREATE (pratica25:Pratica {codigo: '3.2.3', descricao: 'Incubação Virtual'});
CREATE (pratica26:Pratica {codigo: '3.3.1', descricao: 'Gestão Ambiental'});
CREATE (pratica27:Pratica {codigo: '3.3.2', descricao: 'Responsabilidade Social'});
CREATE (pratica28:Pratica {codigo: '4.1.1', descricao: 'Internacionalização da Incubadora'});
CREATE (pratica29:Pratica {codigo: '4.1.2', descricao: 'Internacionalização dos Empreendimentos'});

// Relacionamentos para Cerne 1
MATCH (cerne1:Cerne {id: 1}),
      (p1:Pratica {codigo: '1.1.1'}), (p2:Pratica {codigo: '1.1.2'}), (p3:Pratica {codigo: '1.1.3'}),
      (p4:Pratica {codigo: '1.2.1'}), (p5:Pratica {codigo: '1.2.2'}), (p6:Pratica {codigo: '1.2.3'}),
      (p7:Pratica {codigo: '1.3.1'}), (p8:Pratica {codigo: '1.3.2'}), (p9:Pratica {codigo: '1.3.3'}),
      (p10:Pratica {codigo: '1.4.1'}), (p11:Pratica {codigo: '1.4.2'}),
      (p12:Pratica {codigo: '1.5.1'}), (p13:Pratica {codigo: '1.5.2'}), (p14:Pratica {codigo: '1.5.3'})
CREATE (cerne1)-[:CONTA_COM]->(p1), (cerne1)-[:CONTA_COM]->(p2), (cerne1)-[:CONTA_COM]->(p3),
       (cerne1)-[:CONTA_COM]->(p4), (cerne1)-[:CONTA_COM]->(p5), (cerne1)-[:CONTA_COM]->(p6),
       (cerne1)-[:CONTA_COM]->(p7), (cerne1)-[:CONTA_COM]->(p8), (cerne1)-[:CONTA_COM]->(p9),
       (cerne1)-[:CONTA_COM]->(p10), (cerne1)-[:CONTA_COM]->(p11), (cerne1)-[:CONTA_COM]->(p12),
       (cerne1)-[:CONTA_COM]->(p13), (cerne1)-[:CONTA_COM]->(p14);

// Relacionamentos para Cerne 2
MATCH (cerne2:Cerne {id: 2}),
      (p15:Pratica {codigo: '2.1.1'}), (p16:Pratica {codigo: '2.1.2'}), (p17:Pratica {codigo: '2.2.1'}),
      (p18:Pratica {codigo: '2.2.2'}), (p19:Pratica {codigo: '2.3.1'}), (p20:Pratica {codigo: '2.3.2'})
CREATE (cerne2)-[:CONTA_COM]->(p15), (cerne2)-[:CONTA_COM]->(p16), (cerne2)-[:CONTA_COM]->(p17),
       (cerne2)-[:CONTA_COM]->(p18), (cerne2)-[:CONTA_COM]->(p19), (cerne2)-[:CONTA_COM]->(p20);

// Relacionamentos para Cerne 3
MATCH (cerne3:Cerne {id: 3}),
      (p21:Pratica {codigo: '3.1.1'}), (p22:Pratica {codigo: '3.1.2'}), (p23:Pratica {codigo: '3.2.1'}),
      (p24:Pratica {codigo: '3.2.2'}), (p25:Pratica {codigo: '3.2.3'}),
      (p26:Pratica {codigo: '3.3.1'}), (p27:Pratica {codigo: '3.3.2'})
CREATE (cerne3)-[:CONTA_COM]->(p21), (cerne3)-[:CONTA_COM]->(p22), (cerne3)-[:CONTA_COM]->(p23),
       (cerne3)-[:CONTA_COM]->(p24), (cerne3)-[:CONTA_COM]->(p25), (cerne3)-[:CONTA_COM]->(p26),
       (cerne3)-[:CONTA_COM]->(p27);

// Relacionamentos para Cerne 4
MATCH (cerne4:Cerne {id: 4}),
      (p28:Pratica {codigo: '4.1.1'}), (p29:Pratica {codigo: '4.1.2'})
CREATE (cerne4)-[:CONTA_COM]->(p28), (cerne4)-[:CONTA_COM]->(p29);
