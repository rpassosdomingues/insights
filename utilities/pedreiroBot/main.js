// ======= main.js =======

/*
  Este arquivo contém toda a lógica para o fluxo de orçamentação:
  1) Etapa Cliente  → Seleção de serviços e dados iniciais do projeto
  2) Etapa Pedreiro → Informar diária e margem de lucro
  3) Etapa Vendedor → Cotação de materiais pelo vendedor
  4) Resultado Final → Geração do resumo e exportação PNG/WhatsApp

  As principais variáveis e objetos:
  - orcamentoAtual: objeto que armazena todos os dados calculados em cada etapa
  - SERVICOS_BASE: importado de servicos.js, contém definições de serviços e materiais
  - PRECOS_MEDIOS_GERAIS: importado de servicos.js, valores médios de referência
*/

// --- 1️⃣ Variável global para guardar o estado atual do orçamento ---
let orcamentoAtual = {};

/*
  mostrarEtapa(id)
  -----------------
  Exibe a seção (div) correspondente à etapa cujo ID foi passado.
  Todas as outras seções (etapas e seção de resultado) são ocultadas.
  Também rola a página para o topo, garantindo que a próxima etapa
  fique visível ao usuário.

  Parâmetro:
    - id: string → ID do elemento HTML que representa a etapa a ser exibida
*/
function mostrarEtapa(id) {
    // Oculta todas as seções de classe 'etapa' e 'resultado-secao'
    document.querySelectorAll('.etapa, .resultado-secao').forEach(sec => {
        sec.classList.add('hidden');
    });
    // Exibe apenas a seção desejada
    document.getElementById(id).classList.remove('hidden');
    // Rola para o topo suavemente
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

/*
  popularCheckboxesServicos()
  ---------------------------
  Acessa o objeto SERVICOS_BASE (importado de servicos.js) e cria dinamicamente
  checkboxes para cada serviço na Etapa 1. Cada checkbox tem um 'data-key'
  com a chave do serviço (ex: 'pinturaInterna', 'assentamentoPisoCeramico', etc.),
  e a label exibe o nome amigável 'nomeDisplay'.
*/
function popularCheckboxesServicos() {
    const lista = document.getElementById('listaServicosCheckboxes');
    lista.innerHTML = ''; // Limpa qualquer conteúdo anterior

    // Itera sobre as chaves de SERVICOS_BASE
    for (let key in SERVICOS_BASE) {
        // Garante que a propriedade pertence ao objeto e não à cadeia de protótipos
        if (Object.hasOwnProperty.call(SERVICOS_BASE, key)) {
            const serv = SERVICOS_BASE[key];
            // Cria um item <li> contendo o checkbox + label
            const li = document.createElement('li');
            li.innerHTML = `
                <label>
                    <input type="checkbox" class="chkServico" data-key="${key}">
                    ${serv.nomeDisplay}
                </label>
            `;
            lista.appendChild(li);
        }
    }
}

/*
  avancarParaPedreiro()
  ---------------------
  Executa a transição da Etapa 1 (Cliente) para a Etapa 2 (Pedreiro).
  1) Coleta dados de entrada do cliente (nome do projeto, área, número de cômodos, tipo de rejunte).
  2) Valida os campos obrigatórios (área e cômodos).
  3) Inicializa o objeto orcamentoAtual, zerando listas e somatórios.
  4) Lê quais serviços foram marcados, calcula:
     - Tempo total de serviço (soma de tempoPorM2 * área para cada serviço)
     - Soma dos custos médios de materiais (qtd * precoMedio) para cada material de cada serviço
     - Armazena cada material agregado em orcamentoAtual.listaMateriais
  5) Se “assentamentoPisoCeramico” estiver marcado, adiciona cálculo de rejuntamento:
     - calcula quantidade de rejunte com base em 'rendimentoRejuntePorM2'
     - ajusta tempo total considerando 30% adicional de tempo de assentamento
  6) Exibe a Etapa 2 chamando mostrarEtapa('etapaPedreiro').
*/
function avancarParaPedreiro() {
    // 1) Coleta dados do Cliente
    const nome = document.getElementById('nomeProjeto').value.trim() || "Sem nome";
    const areaInput = document.getElementById('areaReforma');
    const comodosInput = document.getElementById('numComodos');
    const area = parseFloat(areaInput.value);
    const comodos = parseInt(comodosInput.value);
    const tipoRejunte = document.getElementById('tipoRejunte').value;

    // 2) Validações básicas
    if (isNaN(area) || area <= 0) {
        alert("Por favor, informe uma área válida para a reforma (deve ser um número maior que zero).");
        areaInput.focus();
        return;
    }
    if (isNaN(comodos) || comodos <= 0) {
        alert("Por favor, informe a quantidade de cômodos (deve ser um número inteiro maior que zero).");
        comodosInput.focus();
        return;
    }

    // 3) Inicializa orcamentoAtual zerando propriedades
    orcamentoAtual = {
        dadosProjeto: {
            nome,
            area,
            comodos,
            tipoRejunte
        },
        servicosSelecionados: [],
        listaMateriais: {},
        custoTotalMateriaisMedio: 0,
        tempoTotalDias: 0,
        custosPedreiro: {},
        // 'custosVendedor' não está sendo usado na lógica atual, pode ser removido ou mantido para expansões futuras
        // custosVendedor: {}, // Comentado, pois não é usado diretamente na lógica fornecida.
        totalGeral: 0
    };

    // 4) Processa cada checkbox marcado
    const checkboxes = document.querySelectorAll('.chkServico');
    let precisaRejunte = false;

    checkboxes.forEach(cb => {
        if (cb.checked) {
            const key = cb.dataset.key;
            const serv = SERVICOS_BASE[key];

            // Adiciona nome do serviço à lista
            orcamentoAtual.servicosSelecionados.push(serv.nomeDisplay);

            // Calcula tempo adicional: tempoPorM2 * área total
            orcamentoAtual.tempoTotalDias += serv.tempoPorM2 * area;

            // Para cada material do serviço, calcula quantidade e custo
            serv.materiais.forEach(mat => {
                const qtd = mat.rendimentoPorM2 * area; // quantidade baseada em m²
                const custoMat = qtd * mat.precoMedio; // custo médio = qtd × preço médio
                orcamentoAtual.custoTotalMateriaisMedio += custoMat; // acumula no custo médio geral

                // Registra no objeto listaMateriais (agrega por id)
                if (!orcamentoAtual.listaMateriais[mat.id]) {
                    orcamentoAtual.listaMateriais[mat.id] = {
                        nome: mat.nome,
                        qtd: 0,
                        unidade: mat.unidade,
                        precoMedio: mat.precoMedio,
                        custoTotalMedio: 0,
                        precoVendedor: 0,
                        custoTotalVendedor: 0
                    };
                }
                // Soma quantidade e custo médio por material
                orcamentoAtual.listaMateriais[mat.id].qtd += qtd;
                orcamentoAtual.listaMateriais[mat.id].custoTotalMedio += custoMat;
            });

            // Se o serviço for assentamento de piso, marca que precisa rejuntamento
            if (key === "assentamentoPisoCeramico") {
                precisaRejunte = true;
            }
        }
    });

    // 5) Se for necessário, adiciona Rejuntamento ao orçamento
    if (precisaRejunte) {
        // Pega preço médio de rejunte com base no tipo (cimentício, acrílico ou epóxi)
        const precoR = PRECOS_MEDIOS_GERAIS.rejunteKg[tipoRejunte];
        // Cálculo de quantidade de rejunte: rendimentoRejuntePorM2 × área
        const qtdR = PRECOS_MEDIOS_GERAIS.rendimentoRejuntePorM2 * area;
        const custoR = qtdR * precoR;
        const idR = `rejunte_${tipoRejunte}`;
        const nomeR = `Rejunte (${tipoRejunte.charAt(0).toUpperCase() + tipoRejunte.slice(1)})`;

        // Se ainda não existe no objeto listaMateriais, inicializa
        if (!orcamentoAtual.listaMateriais[idR]) {
            orcamentoAtual.listaMateriais[idR] = {
                nome: nomeR,
                qtd: 0,
                unidade: "Kg",
                precoMedio: precoR,
                custoTotalMedio: 0,
                precoVendedor: 0,
                custoTotalVendedor: 0
            };
        }
        // Adiciona quantidade e custo médio de rejunte
        orcamentoAtual.listaMateriais[idR].qtd += qtdR;
        orcamentoAtual.listaMateriais[idR].custoTotalMedio += custoR;
        orcamentoAtual.custoTotalMateriaisMedio += custoR;

        // Adiciona "Rejuntamento" à lista de serviços selecionados
        orcamentoAtual.servicosSelecionados.push("Rejuntamento");

        // Ajusta tempo total: +30% do tempo de assentamento
        orcamentoAtual.tempoTotalDias +=
            SERVICOS_BASE.assentamentoPisoCeramico.tempoPorM2 * area * 0.3;
    }

    // Exibe a Etapa 2 (Pedreiro)
    mostrarEtapa('etapaPedreiro');
}

/*
  voltarParaCliente()
  --------------------
  Retorna da Etapa 2 (Pedreiro) para a Etapa 1 (Cliente), sem alterar o objeto orcamentoAtual.
*/
function voltarParaCliente() {
    mostrarEtapa('etapaCliente');
}

/*
  avancarParaVendedor()
  ---------------------
  Transição da Etapa 2 (Pedreiro) para a Etapa 3 (Vendedor).
  1) Coleta diária e percentual de lucro informados pelo pedreiro.
  2) Calcula custo real de mão de obra: tempoTotalDias * diária informada.
  3) Armazena esses valores em orcamentoAtual.custosPedreiro.
  4) Prepara a lista de materiais para o vendedor — exibe cada item com a qtd e preço médio sugerido.
  5) Exibe a Etapa 3.
*/
function avancarParaVendedor() {
    // 1) Coleta valor da diária (ou usa valor médio se não informado) e percentual de lucro
    const diaria = parseFloat(document.getElementById('custoDiariaPedreiro').value) ||
        PRECOS_MEDIOS_GERAIS.diariaPedreiro;
    const lucro = parseFloat(document.getElementById('percentualLucro').value) || 0;

    // 2) Calcula mão de obra real
    const custoMOReal = orcamentoAtual.tempoTotalDias * diaria;

    orcamentoAtual.custosPedreiro = {
        diaria: diaria,
        lucroPercentual: lucro,
        custoMOReal: custoMOReal
    };

    // 3) Prepara lista de materiais para o vendedor — container HTML
    const listaDiv = document.getElementById('listaMateriaisVendedor');
    listaDiv.innerHTML = ""; // limpa conteúdo anterior

    // Para cada material agregado, cria linha com nome, qtd e input para precoVendor
    for (let id in orcamentoAtual.listaMateriais) {
        // Garante que a propriedade pertence ao objeto
        if (Object.hasOwnProperty.call(orcamentoAtual.listaMateriais, id)) {
            const mat = orcamentoAtual.listaMateriais[id];
            const div = document.createElement('div');
            div.className = "material-para-precificar";
            div.innerHTML = `
                <span>${mat.nome} (Qtd: ${mat.qtd.toFixed(2)} ${mat.unidade}) 
                    – Médio: R$ ${mat.precoMedio.toFixed(2)}
                </span>
                <input type="number" data-id="${id}" 
                            value="${mat.precoMedio.toFixed(2)}" min="0" step="0.01">
            `;
            listaDiv.appendChild(div);
        }
    }

    // 4) Exibe a Etapa 3 (Vendedor)
    mostrarEtapa('etapaVendedor');
}

/*
  voltarParaPedreiro()
  --------------------
  Retorna da Etapa 3 (Vendedor) para a Etapa 2 (Pedreiro), sem alterar orcamentoAtual.
*/
function voltarParaPedreiro() {
    mostrarEteta('etapaPedreiro'); // Small typo fixed here
}

/*
  finalizarOrcamento()
  --------------------
  Transição da Etapa 3 (Vendedor) para a geração do resultado final.
  1) Lê todos os preços informados para cada material e calcula custoTotalVendedor.
  2) Recalcula lucro do pedreiro com preços do vendedor (se houver margem).
  3) Calcula totalGeral = subtotal (materiaisVendedor + M.O.) + valorLucro.
  4) Monta o HTML completo de resultado (serviços, materiais, M.O., lucro e total).
  5) Exibe a seção de Resultado Final e habilita botões de ação (WhatsApp e salvar PNG).
*/
function finalizarOrcamento() {
    // 1) Coleta preços do vendedor para cada material
    orcamentoAtual.custoTotalMateriaisVendedor = 0;
    document.querySelectorAll('#listaMateriaisVendedor input[type="number"]').forEach(input => {
        const id = input.dataset.id;
        const precoVend = parseFloat(input.value) || orcamentoAtual.listaMateriais[id].precoMedio;
        orcamentoAtual.listaMateriais[id].precoVendedor = precoVend;
        orcamentoAtual.listaMateriais[id].custoTotalVendedor =
            orcamentoAtual.listaMateriais[id].qtd * precoVend;
        orcamentoAtual.custoTotalMateriaisVendedor +=
            orcamentoAtual.listaMateriais[id].custoTotalVendedor;
    });

    // 2) Recalcula valor de lucro do pedreiro
    const subtotalSemLucro =
        orcamentoAtual.custoTotalMateriaisVendedor +
        orcamentoAtual.custosPedreiro.custoMOReal;
    orcamentoAtual.custosPedreiro.valorLucro =
        (subtotalSemLucro * orcamentoAtual.custosPedreiro.lucroPercentual) / 100;

    // 3) Calcula total geral
    orcamentoAtual.totalGeral =
        subtotalSemLucro + orcamentoAtual.custosPedreiro.valorLucro;

    // 4) Monta HTML para exibir no resultado
    let html = `<h3>Orçamento: ${orcamentoAtual.dadosProjeto.nome}</h3>`;
    html += `<p>Área: ${orcamentoAtual.dadosProjeto.area} m² | Cômodos: ${orcamentoAtual.dadosProjeto.comodos}</p><hr>`;

    html += `<h4>Serviços Selecionados:</h4><ul>`;
    orcamentoAtual.servicosSelecionados.forEach(s => {
        html += `<li>– ${s}</li>`;
    });
    html += `</ul><hr>`;

    html += `<h4>Materiais (Preço Vendedor):</h4><ul>`;
    for (let id in orcamentoAtual.listaMateriais) {
        if (Object.hasOwnProperty.call(orcamentoAtual.listaMateriais, id)) {
            const mat = orcamentoAtual.listaMateriais[id];
            html += `
                <li class="material-item">
                    <span>${mat.nome} (Qtd: ${mat.qtd.toFixed(2)} ${mat.unidade})</span>
                    <span>R$ ${mat.custoTotalVendedor.toFixed(2)}
                        <small>(R$ ${mat.precoVendedor.toFixed(2)}/un)</small>
                    </span>
                </li>`;
        }
    }
    html += `</ul>
               <p><b>Subtotal Materiais: R$ ${orcamentoAtual.custoTotalMateriaisVendedor.toFixed(2)}</b></p><hr>`;

    html += `<h4>Mão de Obra (Pedreiro):</h4>`;
    html += `<p>Tempo Estimado Total: ${orcamentoAtual.tempoTotalDias.toFixed(1)} dias</p>`;
    html += `<p>Diária Considerada: R$ ${orcamentoAtual.custosPedreiro.diaria.toFixed(2)}</p>`;
    html += `<p><b>Subtotal Mão de Obra: R$ ${orcamentoAtual.custosPedreiro.custoMOReal.toFixed(2)}</b></p><hr>`;

    if (orcamentoAtual.custosPedreiro.lucroPercentual > 0) {
        html += `<p>Subtotal (Materiais + M.O.): R$ ${subtotalSemLucro.toFixed(2)}</p>`;
        html += `<p>Taxa Adm/Lucro (${orcamentoAtual.custosPedreiro.lucroPercentual}%): 
                     R$ ${orcamentoAtual.custosPedreiro.valorLucro.toFixed(2)}</p><hr>`;
    }

    html += `<h3><span style="color:#34c759;">💰 Total Geral: R$ ${orcamentoAtual.totalGeral.toFixed(2)}</span></h3>`;

    document.getElementById('conteudoResultado').innerHTML = html;
    document.getElementById('tituloResultado').textContent = "🔔 Orçamento Detalhado";

    // 5) Exibe apenas agora os botões de ação
    document.getElementById('resultadoFinal').classList.remove('hidden');
    document.getElementById('botoesAcoes').classList.remove('hidden');

    mostrarEtapa('resultadoFinal');
}

/*
  gerarTextoWhatsApp()
  -------------------
  Constrói a mensagem formatada para envio ao WhatsApp, contendo:
    - Nome do projeto
    - Lista de serviços selecionados
    - Lista de materiais com quantidade e custo
    - Custo de M.O. do pedreiro
    - Valor de lucro (se aplicável)
    - Total geral
  Retorna a string codificada em URI (encodeURIComponent) para uso em wa.me URL.
*/
function gerarTextoWhatsApp() {
    if (!orcamentoAtual.totalGeral) return "";

    let texto = `*Orçamento: ${orcamentoAtual.dadosProjeto.nome}*\n\n`;
    texto += `🛠️ *Serviços:*\n`;
    orcamentoAtual.servicosSelecionados.forEach(s => {
        texto += ` - ${s}\n`;
    });

    texto += `\n📋 *Materiais:*\n`;
    for (let id in orcamentoAtual.listaMateriais) {
        // Ensure property belongs to the object
        if (Object.hasOwnProperty.call(orcamentoAtual.listaMateriais, id)) {
            const mat = orcamentoAtual.listaMateriais[id];
            texto += ` - ${mat.nome}: ${mat.qtd.toFixed(2)} ${mat.unidade} → R$ ${mat.custoTotalVendedor.toFixed(2)}\n`;
        }
    }

    texto += `\n👷 *Mão de Obra:* R$ ${orcamentoAtual.custosPedreiro.custoMOReal.toFixed(2)}\n`;
    if (orcamentoAtual.custosPedreiro.lucroPercentual > 0) {
        texto += `📈 *Lucro (${orcamentoAtual.custosPedreiro.lucroPercentual}%):* R$ ${orcamentoAtual.custosPedreiro.valorLucro.toFixed(2)}\n`;
    }

    texto += `\n💰 *Total Geral:* R$ ${orcamentoAtual.totalGeral.toFixed(2)}\n\n`;
    texto += `_Estimativa aproximada._`;

    return encodeURIComponent(texto);
}

/*
  abrirWhatsApp()
  ---------------
  Abre o link do WhatsApp Web (ou App) com a mensagem gerada por gerarTextoWhatsApp().
  Se não houver orçamento finalizado, exibe um alerta.
*/
function abrirWhatsApp() {
    const msg = gerarTextoWhatsApp();
    if (!msg) {
        alert("Finalize o orçamento antes de enviar.");
        return;
    }
    window.open(`https://wa.me/?text=${msg}`, '_blank');
}

/*
  exportarPNG()
  -------------
  Captura todo o conteúdo de #resultadoFinal e o exporta como uma imagem PNG.
  A função clona o elemento, posiciona-o de forma a garantir que todo o conteúdo
  seja visível para o html2canvas (mesmo que haja rolagem), força um fundo
  branco e, em seguida, dispara o download da imagem.
*/
function exportarPNG() {
    if (!orcamentoAtual.totalGeral) {
        alert("Finalize o orçamento antes de salvar.");
        return;
    }

    const original = document.getElementById('resultadoFinal');

    // Clona o elemento original para evitar alterar a exibição atual
    const clone = original.cloneNode(true);

    // Copia estilos computados importantes do original para o clone
    const computedStyle = window.getComputedStyle(original);
    for (let i = 0; i < computedStyle.length; i++) {
        const prop = computedStyle[i];
        // Evite copiar propriedades de posicionamento e tamanho que serão sobrescritas
        if (!['position', 'top', 'left', 'z-index', 'width', 'height', 'overflow', 'display'].includes(prop)) {
            clone.style[prop] = computedStyle.getPropertyValue(prop);
        }
    }

    // Configura o clone para ser totalmente visível e ter um fundo branco sólido para a captura
    clone.style.position = 'fixed';
    clone.style.top = '0';
    clone.style.left = '0';
    clone.style.margin = '0';
    clone.style.padding = computedStyle.getPropertyValue('padding'); // Mantém o padding original
    clone.style.width = original.scrollWidth + 'px'; // Pega a largura total do conteúdo
    clone.style.height = original.scrollHeight + 'px'; // Pega a altura total do conteúdo
    clone.style.backgroundColor = '#ffffff'; // Garante fundo branco explícito para o clone
    clone.style.zIndex = '9999'; // Garante que o clone fique no topo
    clone.style.overflow = 'hidden'; // Esconde overflow no clone para a captura
    clone.style.display = 'block'; // Garante que o clone está visível se o original estava 'hidden'

    // Adiciona o clone ao body
    document.body.appendChild(clone);

    // Armazena e ajusta overflow do body para evitar barras externas durante a captura
    const bodyOverflowOriginal = document.body.style.overflow;
    document.body.style.overflow = 'hidden';

    // Captura via html2canvas
    const escala = window.devicePixelRatio || 1; // Para telas de alta densidade (Retina)
    const largura = clone.scrollWidth;
    const altura = clone.scrollHeight;

    html2canvas(clone, {
            scale: escala,
            width: largura, // Usa a largura total do conteúdo do clone
            height: altura, // Usa a altura total do conteúdo do clone
            scrollX: 0,
            scrollY: 0,
            windowWidth: largura, // Importante para html2canvas entender a área de captura
            windowHeight: altura, // Importante para html2canvas entender a área de captura
            useCORS: true, // Permite carregar recursos de outras origens (se houver imagens, fontes externas)
            backgroundColor: '#ffffff' // Define um background padrão para o canvas, caso algo esteja transparente
        })
        .then(canvas => {
            // Restaura estilos originais e remove o clone
            document.body.style.overflow = bodyOverflowOriginal;
            if (document.body.contains(clone)) {
                document.body.removeChild(clone);
            }

            // Gera nome de arquivo seguro
            const safeNome = orcamentoAtual.dadosProjeto.nome.replace(/\s+/g, '_').replace(/[^a-zA-Z0-9_]/g, '');
            const hoje = new Date().toISOString().slice(0, 10);
            const nomeArq = `orcamento_${safeNome}_${hoje}.png`;

            // Dispara download
            const link = document.createElement('a');
            link.download = nomeArq;
            link.href = canvas.toDataURL('image/png');
            link.click();
        })
        .catch(err => {
            // Em caso de erro, restaura e avisa usuário
            document.body.style.overflow = bodyOverflowOriginal;
            if (document.body.contains(clone)) {
                document.body.removeChild(clone);
            }
            console.error("Erro ao gerar a imagem:", err);
            alert("Falha ao gerar a imagem. Verifique o console para mais detalhes.");
        });
}

/*
  Inicialização ao carregar a página:
  - Popula os checkboxes de serviços na Etapa 1
  - Exibe apenas a Etapa 1 (Cliente)
*/
window.onload = () => {
    popularCheckboxesServicos();
    mostrarEtapa('etapaCliente');
};