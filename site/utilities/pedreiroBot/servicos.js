// ======= servicos.js =======

/*
  Este arquivo define os dados base para os serviços e materiais
  utilizados no cálculo do orçamento.
*/

// Objeto que armazena os serviços base disponíveis
const SERVICOS_BASE = {
  // ----------- PEDREIRO -----------
  assentamentoPisoCeramico: {
    id: "assentamentoPisoCeramico",
    nomeDisplay: "Assentamento de Piso Cerâmico",
    tempoPorM2: 0.25,
    materiais: [{
      id: "argamassaAC2",
      nome: "Argamassa AC-II",
      rendimentoPorM2: 5,
      unidade: "Kg",
      precoMedio: 2.50
    }]
  },
  rebocoParede: {
    id: "rebocoParede",
    nomeDisplay: "Reboco de Parede",
    tempoPorM2: 0.2,
    materiais: [{
      id: "areiaMedia",
      nome: "Areia Média",
      rendimentoPorM2: 0.04,
      unidade: "m³",
      precoMedio: 120.00
    }, {
      id: "cimento",
      nome: "Cimento CP-II",
      rendimentoPorM2: 0.02,
      unidade: "Kg",
      precoMedio: 0.50
    }]
  },
  assentamentoBlocoCeramico: {
    id: "assentamentoBlocoCeramico",
    nomeDisplay: "Assentamento de Bloco Cerâmico",
    tempoPorM2: 0.3,
    materiais: [{
      id: "blocoCeramico",
      nome: "Bloco Cerâmico",
      rendimentoPorM2: 12,
      unidade: "un",
      precoMedio: 1.20
    }, {
      id: "argamassaAssentamento",
      nome: "Argamassa para Assentamento",
      rendimentoPorM2: 0.03,
      unidade: "m³",
      precoMedio: 180.00
    }]
  },
  contrapiso: {
    id: "contrapiso",
    nomeDisplay: "Execução de Contrapiso",
    tempoPorM2: 0.15,
    materiais: [{
      id: "areiaGrossa",
      nome: "Areia Grossa",
      rendimentoPorM2: 0.04,
      unidade: "m³",
      precoMedio: 100.00
    }, {
      id: "cimento",
      nome: "Cimento CP-II",
      rendimentoPorM2: 0.02,
      unidade: "Kg",
      precoMedio: 0.50
    }]
  },

  // ----------- PINTOR -----------
  pinturaInterna: {
    id: "pinturaInterna",
    nomeDisplay: "Pintura Interna (Paredes)",
    tempoPorM2: 0.1,
    materiais: [{
      id: "tintaLatex",
      nome: "Tinta Látex Acrílica",
      rendimentoPorM2: 0.2,
      unidade: "Litro",
      precoMedio: 15.00
    }, {
      id: "massaCorrida",
      nome: "Massa Corrida",
      rendimentoPorM2: 0.25,
      unidade: "Kg",
      precoMedio: 3.00
    }, {
      id: "lixaParede",
      nome: "Lixa para Parede",
      rendimentoPorM2: 0.05,
      unidade: "un",
      precoMedio: 2.00
    }]
  },
  pinturaTextura: {
    id: "pinturaTextura",
    nomeDisplay: "Pintura com Textura",
    tempoPorM2: 0.12,
    materiais: [{
      id: "texturaAcrilica",
      nome: "Textura Acrílica",
      rendimentoPorM2: 0.4,
      unidade: "Kg",
      precoMedio: 8.00
    }, {
      id: "lixaParede",
      nome: "Lixa para Parede",
      rendimentoPorM2: 0.05,
      unidade: "un",
      precoMedio: 2.00
    }]
  },

  // ----------- ELETRICISTA -----------
  instalacaoEletrica: {
    id: "instalacaoEletrica",
    nomeDisplay: "Instalação Elétrica (Pontos)",
    tempoPorM2: 0.08,
    materiais: [{
      id: "fioEletrico2_5mm",
      nome: "Fio Elétrico 2.5mm²",
      rendimentoPorM2: 1.5,
      unidade: "m",
      precoMedio: 2.00
    }, {
      id: "tomada2pT",
      nome: "Tomada 2P+T",
      rendimentoPorM2: 0.02,
      unidade: "un",
      precoMedio: 15.00
    }, {
      id: "interruptorSimples",
      nome: "Interruptor Simples",
      rendimentoPorM2: 0.01,
      unidade: "un",
      precoMedio: 10.00
    }]
  },
  instalacaoQuadro: {
    id: "instalacaoQuadro",
    nomeDisplay: "Instalação de Quadro de Distribuição",
    tempoPorM2: 0.05,
    materiais: [{
      id: "quadroDistribuicao",
      nome: "Quadro de Distribuição",
      rendimentoPorM2: 0.01,
      unidade: "un",
      precoMedio: 200.00
    }, {
      id: "disjuntor",
      nome: "Disjuntor",
      rendimentoPorM2: 0.02,
      unidade: "un",
      precoMedio: 20.00
    }]
  },

  // ----------- ENCANADOR -----------
  instalacaoHidraulica: {
    id: "instalacaoHidraulica",
    nomeDisplay: "Instalação Hidráulica (Pontos)",
    tempoPorM2: 0.06,
    materiais: [{
      id: "tuboPVC25mm",
      nome: "Tubo PVC Água Fria 25mm",
      rendimentoPorM2: 0.8,
      unidade: "m",
      precoMedio: 8.00
    }, {
      id: "joelhoPVC25mm",
      nome: "Joelho PVC 25mm",
      rendimentoPorM2: 0.05,
      unidade: "un",
      precoMedio: 3.00
    }, {
      id: "raloLinear",
      nome: "Ralo Linear",
      rendimentoPorM2: 0.005,
      unidade: "un",
      precoMedio: 50.00
    }]
  },
  instalacaoAquecedor: {
    id: "instalacaoAquecedor",
    nomeDisplay: "Instalação de Aquecedor de Água",
    tempoPorM2: 0.07,
    materiais: [{
      id: "aquecedorGas",
      nome: "Aquecedor a Gás",
      rendimentoPorM2: 0.01,
      unidade: "un",
      precoMedio: 700.00
    }, {
      id: "flexivelGas",
      nome: "Mangueira Flexível para Gás",
      rendimentoPorM2: 0.1,
      unidade: "m",
      precoMedio: 15.00
    }]
  },

  // ----------- SERVIÇOS GERAIS -----------
  demolicaoLeve: {
    id: "demolicaoLeve",
    nomeDisplay: "Demolição Leve (Paredes não Estruturais)",
    tempoPorM2: 0.05,
    materiais: [{
      id: "sacoEntulho",
      nome: "Saco de Entulho",
      rendimentoPorM2: 0.1,
      unidade: "un",
      precoMedio: 1.00
    }]
  },
  limpezaPosObra: {
    id: "limpezaPosObra",
    nomeDisplay: "Limpeza Pós-Obra",
    tempoPorM2: 0.02,
    materiais: [{
      id: "sacoLixo",
      nome: "Saco de Lixo Resistente",
      rendimentoPorM2: 0.05,
      unidade: "un",
      precoMedio: 0.80
    }, {
      id: "materialLimpezaGeral",
      nome: "Kit Material de Limpeza (detergentes, vassouras etc.)",
      rendimentoPorM2: 0.005,
      unidade: "R$",
      precoMedio: 50.00
    }]
  }
};

// Objeto com preços médios gerais
const PRECOS_MEDIOS_GERAIS = {
  diariaPedreiro: 220.00,
  diariaPintor: 200.00,
  diariaEncanador: 230.00,
  diariaEletricista: 250.00,
  rendimentoRejuntePorM2: 0.5,
  rejunteKg: {
    cimenticio: 5.00,
    acrilico: 12.00,
    epoxi: 25.00
  }
};

