#include <iostream>
#include <fstream>
#include <string>
#include <ctime>
#include <limits>
#include <iomanip>

using namespace std;

// Enum para representar os diferentes tipos de sorvete
enum class TipoSorvete {
    CasquinhaAcai,
    CascãoAcai,
    SorveteCasquinha,
    SorveteCascao,
    Sundae,
    MilkShake,
    MilkShakeEspecial
};

// Registro para representar as informações do cliente e suas compras
struct RegistroCliente {
    string cpf;
    string primeiraCompra;
    int sorvetesPorTipo[7];
};

// Função para verificar se um CPF tem exatamente 11 dígitos
bool validarCpf(const string& cpf) {
    if (cpf.size() != 11) {
        return false;
    }

    for (char c : cpf) {
        if (!isdigit(c)) {
            return false;
        }
    }

    return true;
}

// Função para salvar as informações dos clientes no arquivo
void salvarRegistros(RegistroCliente registros[], int tamanho) {
    ofstream arquivo("registros_sorvetes.txt");
    if (arquivo.is_open()) {
        for (int i = 0; i < tamanho; i++) {
            arquivo << registros[i].cpf << " ";
            arquivo << registros[i].primeiraCompra << " ";

            for (int j = 0; j < 7; j++) {
                arquivo << registros[i].sorvetesPorTipo[j] << " ";
            }

            arquivo << endl;
        }
        arquivo.close();
    } else {
        cout << "Erro ao abrir o arquivo para salvar as informações dos clientes.\n";
    }
}

// Função para ler as informações dos clientes do arquivo
int lerRegistros(RegistroCliente registros[]) {
    ifstream arquivo("registros_sorvetes.txt");
    int tamanho = 0;
    if (arquivo.is_open()) {
        string linha;
        while (getline(arquivo, linha)) {
            RegistroCliente registro;
            size_t pos = linha.find(" ");
            if (pos != string::npos) {
                registro.cpf = linha.substr(0, pos);
                size_t posSegundaParte = linha.find(" ", pos + 1);
                if (posSegundaParte != string::npos) {
                    registro.primeiraCompra = linha.substr(pos + 1, posSegundaParte - pos - 1);

                    size_t inicio = posSegundaParte + 1;
                    size_t fim = linha.find(" ", inicio);
                    int j = 0;

                    while (fim != string::npos && j < 7) { // Limitar a leitura a 7 tipos de sorvetes
                        string quantidadeStr = linha.substr(inicio, fim - inicio);
                        try {
                            int quantidade = stoi(quantidadeStr);
                            registro.sorvetesPorTipo[j] = quantidade;
                            j++;
                        } catch (const std::invalid_argument& e) {
                            // Error handling for invalid data
                            cout << "Erro ao ler as informações do sorvete: " << e.what() << endl;
                        }
                        inicio = fim + 1;
                        fim = linha.find(" ", inicio);
                    }
                }
            }
            registros[tamanho] = registro;
            tamanho++;
        }
        arquivo.close();
    } else {
        cout << "Erro ao abrir o arquivo para ler as informações dos clientes.\n";
    }
    return tamanho;
}

// Função para encontrar um cliente nos registros pelo CPF
int encontrarClientePorCpf(RegistroCliente registros[], int tamanho, const string& cpf) {
    for (int i = 0; i < tamanho; i++) {
        if (registros[i].cpf == cpf) {
            return i;
        }
    }
    return -1;
}

// Função para exibir a comanda com os tipos de sorvetes disponíveis e obter o tipo de sorvete escolhido pelo cliente
TipoSorvete comanda() {
    cout << "=================================\n";
    cout << "\t\tMenu\n";
    cout << "=================================\n";
    cout << "1 - Casquinha de Açaí\n";
    cout << "2 - Cascão de Açaí\n";
    cout << "3 - Sorvete de Casquinha\n";
    cout << "4 - Sorvete de Cascão\n";
    cout << "5 - Sundae\n";
    cout << "6 - Milk Shake\n";
    cout << "7 - Milk Shake Especial\n";
    cout << "---------------------------------\n";
    cout << "0 - Fechar Comanda\n";
    cout << "=================================\n";

    int opcao;
    cout << "\nAnotar pedido do cliente (digite 0 para fechar a comanda): ";
    cin >> opcao;
    while (opcao != 0 && (opcao < 1 || opcao > 7)) {
        cout << "\t Opção inválida. Tente novamente: ";
        cin >> opcao;
    }

    return static_cast<TipoSorvete>(opcao);
}

// Função para obter a quantidade de sorvetes desejada pelo cliente
int obterQuantidadeSorvete() {
    int quantidade;
    cout << "\tQuantos? ";
    cin >> quantidade;
    return quantidade;
}

// Função para atualizar o registro do cliente com os pedidos feitos
void atualizarRegistroCliente(RegistroCliente& cliente, TipoSorvete tipo, int quantidade) {
    switch (tipo) {
        case TipoSorvete::CasquinhaAcai: cliente.sorvetesPorTipo[0] += quantidade; break;
        case TipoSorvete::CascãoAcai: cliente.sorvetesPorTipo[1] += quantidade; break;
        case TipoSorvete::SorveteCasquinha: cliente.sorvetesPorTipo[2] += quantidade; break;
        case TipoSorvete::SorveteCascao: cliente.sorvetesPorTipo[3] += quantidade; break;
        case TipoSorvete::Sundae: cliente.sorvetesPorTipo[4] += quantidade; break;
        case TipoSorvete::MilkShake: cliente.sorvetesPorTipo[5] += quantidade; break;
        case TipoSorvete::MilkShakeEspecial: cliente.sorvetesPorTipo[6] += quantidade; break;
        default: cout << "\tTipo de Sorvete Inválido";
    }
}

int main() {
    const int MAX_CLIENTES = 100;
    RegistroCliente registros[MAX_CLIENTES];
    int tamanhoRegistros = lerRegistros(registros);

    string cpfCliente;
    TipoSorvete tipoSorvete;

    while (true) {
        cout << "\n\n Abrir comanda (digite o CPF do cliente ou 0 para encerrar): ";
        cin >> cpfCliente;

        // Verifica se o usuário deseja encerrar o programa
        if (cpfCliente == "0") {
            break;
        }

        // Verifica se o CPF foi digitado corretamente
        while (!validarCpf(cpfCliente)) {
            cout << "\tCPF inválido (são 11 dígitos). Tente novamente: ";
            cin >> cpfCliente;
        }

        int clienteIndex = encontrarClientePorCpf(registros, tamanhoRegistros, cpfCliente);
        if (clienteIndex == -1) {
            time_t dataAtual = time(0);
            struct tm* now = localtime(&dataAtual);
            char dataStr[11];
            strftime(dataStr, sizeof(dataStr), "%d/%m/%Y", now);
            registros[tamanhoRegistros] = {cpfCliente, dataStr, {0, 0, 0, 0, 0, 0, 0}};
            clienteIndex = tamanhoRegistros; // Define o índice do novo cliente
            tamanhoRegistros++;
        }

        cout << "\n=================================";
        cout << "   \n    -- Status do Cliente -- ";
        cout << "\n=================================";
        cout << "\nCPF: " << registros[clienteIndex].cpf << "\n";
        cout << "---------------------------------\n";
        cout << "Data da primeira compra: " << registros[clienteIndex].primeiraCompra << "\n";
        cout << "\n";
        cout << "      Casquinha de Açaí: " << registros[clienteIndex].sorvetesPorTipo[0] << "\n";
        cout << "         Cascão de Açaí: " << registros[clienteIndex].sorvetesPorTipo[1] << "\n";
        cout << "   Sorvete de Casquinha: " << registros[clienteIndex].sorvetesPorTipo[2] << "\n";
        cout << "      Sorvete de Cascão: " << registros[clienteIndex].sorvetesPorTipo[3] << "\n";
        cout << "                 Sundae: " << registros[clienteIndex].sorvetesPorTipo[4] << "\n";
        cout << "             Milk Shake: " << registros[clienteIndex].sorvetesPorTipo[5] << "\n";
        cout << "    Milk Shake Especial: " << registros[clienteIndex].sorvetesPorTipo[6] << "\n";
        cout << "=================================\n\n";

        while (true) {
            tipoSorvete = comanda();
            if (tipoSorvete == TipoSorvete::CasquinhaAcai) {
                break; // Encerra a compra se a opção for zero
            }

            int quantidade = obterQuantidadeSorvete();

            atualizarRegistroCliente(registros[clienteIndex], tipoSorvete, quantidade);

            cout << "\nO Cliente " << cpfCliente << " comprou " << quantidade << " ";
            switch (tipoSorvete) {
                case TipoSorvete::CasquinhaAcai: cout << "Casquinha de Açaí"; break;
                case TipoSorvete::CascãoAcai: cout << "Cascão de Açaí"; break;
                case TipoSorvete::SorveteCasquinha: cout << "Sorvete de Casquinha"; break;
                case TipoSorvete::SorveteCascao: cout << "Sorvete de Cascão"; break;
                case TipoSorvete::Sundae: cout << "Sundae"; break;
                case TipoSorvete::MilkShake: cout << "Milk Shake"; break;
                case TipoSorvete::MilkShakeEspecial: cout << "Milk Shake Especial"; break;
                default: cout << "Tipo de Sorvete Inválido";
            }
            cout << "!\n";
        }

        salvarRegistros(registros, tamanhoRegistros); // Salvar as informações somente quando a comanda for fechada
    }

    return 0;
}
