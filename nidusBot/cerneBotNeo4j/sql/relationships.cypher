// Relacionamentos para as tags de cada prática
MATCH (p1:Pratica {codigo: '1.1.1 Sensibilização'}), 
      (t1:Tag {nome: 'Divulgação'}), 
      (t2:Tag {nome: 'Visita'})
CREATE (p1)-[:ASSOCIADA_A]->(t1), (p1)-[:ASSOCIADA_A]->(t2);

MATCH (p2:Pratica {codigo: '1.1.2 Prospecção'}), 
      (t3:Tag {nome: 'Evento'}), 
      (t4:Tag {nome: 'Convite'}), 
      (t5:Tag {nome: 'Lista de Presença'})
CREATE (p2)-[:ASSOCIADA_A]->(t3), (p2)-[:ASSOCIADA_A]->(t4), (p2)-[:ASSOCIADA_A]->(t5);

MATCH (p3:Pratica {codigo: '1.1.3 Qualificação de Potenciais Empreendedores'}), 
      (t6:Tag {nome: 'Capacitação'}), 
      (t7:Tag {nome: 'Curso'})
CREATE (p3)-[:ASSOCIADA_A]->(t6), (p3)-[:ASSOCIADA_A]->(t7);

MATCH (p4:Pratica {codigo: '1.2.1 Recepção de Propostas'}), 
      (t8:Tag {nome: 'Edital'}), 
      (t9:Tag {nome: 'Pitch'})
CREATE (p4)-[:ASSOCIADA_A]->(t8), (p4)-[:ASSOCIADA_A]->(t9);

MATCH (p5:Pratica {codigo: '1.2.2 Avaliação'}), 
      (t10:Tag {nome: 'Banca'}), 
      (t11:Tag {nome: 'Edital'})
CREATE (p5)-[:ASSOCIADA_A]->(t10), (p5)-[:ASSOCIADA_A]->(t11);

MATCH (p6:Pratica {codigo: '1.2.3 Contratação'}), 
      (t12:Tag {nome: 'Contrato'}), 
      (t13:Tag {nome: 'Incubação'}), 
      (t14:Tag {nome: 'Pré'})
CREATE (p6)-[:ASSOCIADA_A]->(t12), (p6)-[:ASSOCIADA_A]->(t13), (p6)-[:ASSOCIADA_A]->(t14);

MATCH (p7:Pratica {codigo: '1.3.1 Planejamento'}), 
      (t15:Tag {nome: 'Planejamento'}), 
      (t16:Tag {nome: 'Canvas'})
CREATE (p7)-[:ASSOCIADA_A]->(t15), (p7)-[:ASSOCIADA_A]->(t16);

MATCH (p8:Pratica {codigo: '1.3.2 Agregação de Valor'}), 
      (t17:Tag {nome: 'Conexão'}), 
      (t18:Tag {nome: 'Networking'})
CREATE (p8)-[:ASSOCIADA_A]->(t17), (p8)-[:ASSOCIADA_A]->(t18);

MATCH (p9:Pratica {codigo: '1.3.3 Monitoramento'}), 
      (t19:Tag {nome: 'Monitoramento'}), 
      (t20:Tag {nome: 'Acompanhento'}), 
      (t21:Tag {nome: 'Incubado'})
CREATE (p9)-[:ASSOCIADA_A]->(t19), (p9)-[:ASSOCIADA_A]->(t20), (p9)-[:ASSOCIADA_A]->(t21);

MATCH (p10:Pratica {codigo: '1.4.1 Graduação'}), 
      (t22:Tag {nome: 'Graduado'})
CREATE (p10)-[:ASSOCIADA_A]->(t22);

MATCH (p11:Pratica {codigo: '1.4.2 Relacionamento com Graduadas'}), 
      (t23:Tag {nome: 'Relacionamento'})
CREATE (p11)-[:ASSOCIADA_A]->(t23);

MATCH (p12:Pratica {codigo: '1.5.1 Estrutura Organizacional'}), 
      (t24:Tag {nome: 'Infraestrutura'}), 
      (t25:Tag {nome: 'Regimento'})
CREATE (p12)-[:ASSOCIADA_A]->(t24), (p12)-[:ASSOCIADA_A]->(t25);

MATCH (p13:Pratica {codigo: '1.5.2 Operação da Incubadora'}), 
      (t26:Tag {nome: 'Financeiro'}), 
      (t27:Tag {nome: 'Sustentabilidade'})
CREATE (p13)-[:ASSOCIADA_A]->(t26), (p13)-[:ASSOCIADA_A]->(t27);

MATCH (p14:Pratica {codigo: '1.5.3 Comunicação e Marketing'}), 
      (t28:Tag {nome: 'Divulgação'}), 
      (t29:Tag {nome: 'Arte'}), 
      (t30:Tag {nome: 'Matéria'})
CREATE (p14)-[:ASSOCIADA_A]->(t28), (p14)-[:ASSOCIADA_A]->(t29), (p14)-[:ASSOCIADA_A]->(t30);

MATCH (p15:Pratica {codigo: '2.1.1 Planejamento Estratégico'}), 
      (t31:Tag {nome: 'Estratégia'}), 
      (t32:Tag {nome: 'Plano'})
CREATE (p15)-[:ASSOCIADA_A]->(t31), (p15)-[:ASSOCIADA_A]->(t32);

MATCH (p16:Pratica {codigo: '2.1.2 Administração Estratégica'}), 
      (t33:Tag {nome: 'Administração'}), 
      (t34:Tag {nome: 'Status'})
CREATE (p16)-[:ASSOCIADA_A]->(t33), (p16)-[:ASSOCIADA_A]->(t34);

MATCH (p17:Pratica {codigo: '2.2.1 Ambientes de Ideação'}), 
      (t35:Tag {nome: 'Startup Run'}), 
      (t36:Tag {nome: 'Inovação'})
CREATE (p17)-[:ASSOCIADA_A]->(t35), (p17)-[:ASSOCIADA_A]->(t36);

MATCH (p18:Pratica {codigo: '2.2.2 Serviços a Organizações'}), 
      (t37:Tag {nome: 'Serviços'}), 
      (t38:Tag {nome: 'Maker'}), 
      (t39:Tag {nome: 'Fabricação de Peça'}), 
      (t40:Tag {nome: 'Reserva de Sala'})
CREATE (p18)-[:ASSOCIADA_A]->(t37), (p18)-[:ASSOCIADA_A]->(t38), 
       (p18)-[:ASSOCIADA_A]->(t39), (p18)-[:ASSOCIADA_A]->(t40);

MATCH (p19:Pratica {codigo: '2.3.1 Avaliação da Qualidade'}), 
      (t41:Tag {nome: 'Premiação'}), 
      (t42:Tag {nome: 'Reconhecimento'})
CREATE (p19)-[:ASSOCIADA_A]->(t41), (p19)-[:ASSOCIADA_A]->(t42);

MATCH (p20:Pratica {codigo: '2.3.2 Avaliação dos Impactos'}), 
      (t43:Tag {nome: 'Relatório'}), 
      (t44:Tag {nome: 'Gestão'}), 
      (t45:Tag {nome: 'Impacto'})
CREATE (p20)-[:ASSOCIADA_A]->(t43), (p20)-[:ASSOCIADA_A]->(t44), (p20)-[:ASSOCIADA_A]->(t45);

MATCH (p21:Pratica {codigo: '3.1.1 Interação com o Entorno'}), 
      (t46:Tag {nome: 'Interação'}), 
      (t47:Tag {nome: 'Conexões'}), 
      (t48:Tag {nome: 'Externo'})
CREATE (p21)-[:ASSOCIADA_A]->(t46), (p21)-[:ASSOCIADA_A]->(t47), (p21)-[:ASSOCIADA_A]->(t48);

MATCH (p22:Pratica {codigo: '3.1.2 Participação na Proposição de Políticas Públicas'}), 
      (t49:Tag {nome: 'Política'}), 
      (t50:Tag {nome: 'Público'}), 
      (t51:Tag {nome: 'Participação'})
CREATE (p22)-[:ASSOCIADA_A]->(t49), (p22)-[:ASSOCIADA_A]->(t50), (p22)-[:ASSOCIADA_A]->(t51);

MATCH (p23:Pratica {codigo: '3.2.1 Rede de Mentores'}), 
      (t52:Tag {nome: 'Mentoria'}), 
      (t53:Tag {nome: 'Rede'}), 
      (t54:Tag {nome: 'Contato'})
CREATE (p23)-[:ASSOCIADA_A]->(t52), (p23)-[:ASSOCIADA_A]->(t53), (p23)-[:ASSOCIADA_A]->(t54);

MATCH (p24:Pratica {codigo: '3.2.2 Parcerias'}), 
      (t55:Tag {nome: 'Parcerias'}), 
      (t56:Tag {nome: 'Networking'})
CREATE (p24)-[:ASSOCIADA_A]->(t55), (p24)-[:ASSOCIADA_A]->(t56);

MATCH (p25:Pratica {codigo: '3.3.1 Monitoramento de Tendências'}), 
      (t57:Tag {nome: 'Tendência'}), 
      (t58:Tag {nome: 'Tecnologia'})
CREATE (p25)-[:ASSOCIADA_A]->(t57), (p25)-[:ASSOCIADA_A]->(t58);

MATCH (p26:Pratica {codigo: '3.3.2 Relacionamento com Empresas Incubadas'}), 
      (t59:Tag {nome: 'Relacionamento'}), 
      (t60:Tag {nome: 'Conexão'}), 
      (t61:Tag {nome: 'Rede'})
CREATE (p26)-[:ASSOCIADA_A]->(t59), (p26)-[:ASSOCIADA_A]->(t60), (p26)-[:ASSOCIADA_A]->(t61);
