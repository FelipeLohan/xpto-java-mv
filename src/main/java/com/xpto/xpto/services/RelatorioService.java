package com.xpto.xpto.services;

import com.xpto.xpto.entities.*;
import com.xpto.xpto.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;
    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    @Autowired
    private ContaRepository contaRepository;

    /**
     * Gera um relatório de saldo para um cliente em um período e o salva em um arquivo .txt.
     * @param clienteId O ID do cliente.
     * @param dataInicio A data de início do período do relatório.
     * @param dataFim A data de fim do período do relatório.
     * @throws IOException se ocorrer um erro ao salvar o arquivo.
     */
    @Transactional(readOnly = true)
    public void gerarRelatorioSaldoCliente(Long clienteId, LocalDate dataInicio, LocalDate dataFim) throws IOException {
        // --- 1. BUSCA DE DADOS ---
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));

        Conta conta = contaRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada para o cliente."));

        String nomeCliente = getNomeCliente(cliente);
        String enderecoFormatado = formatarEndereco(conta.getCliente().getEndereco());

        LocalDateTime inicioPeriodo = dataInicio.atStartOfDay();
        LocalDateTime fimPeriodo = dataFim.atTime(LocalTime.MAX);

        List<Movimentacao> movimentacoesNoPeriodo = movimentacaoRepository.findMovimentacoesByClienteAndPeriodo(clienteId, inicioPeriodo, fimPeriodo);
        
        Movimentacao primeiraMovimentacao = movimentacaoRepository.findFirstMovimentacaoByClienteId(clienteId)
                .orElse(null);

        // --- 2. CÁLCULOS ---
        long creditos = movimentacoesNoPeriodo.stream().filter(m -> m.getTipoMovimentacao() == TipoMovimentacao.CREDITO).count();
        long debitos = movimentacoesNoPeriodo.size() - creditos;
        long totalMovimentacoes = movimentacoesNoPeriodo.size();
        
        BigDecimal valorPago = calcularCustoMovimentacoes(totalMovimentacoes);
        BigDecimal saldoInicial = (primeiraMovimentacao != null) ? primeiraMovimentacao.getValor() : BigDecimal.ZERO;
        BigDecimal saldoAtual = movimentacaoRepository.calcularSaldoTotalCliente(clienteId);

        // --- 3. FORMATAÇÃO DO TEXTO ---
        String conteudoRelatorio = formatarConteudoRelatorio(nomeCliente, cliente.getDataCadastro(), enderecoFormatado,
                creditos, debitos, totalMovimentacoes, valorPago, saldoInicial, saldoAtual);

        // --- 4. SALVAR ARQUIVO ---
        salvarRelatorioEmArquivo(nomeCliente, conteudoRelatorio);
    }
    
    /**
     * Calcula o custo das movimentações com base nas regras de negócio.
     */
    private BigDecimal calcularCustoMovimentacoes(long totalMovimentacoes) {
        BigDecimal custo;
        if (totalMovimentacoes <= 10) {
            custo = BigDecimal.valueOf(totalMovimentacoes).multiply(new BigDecimal("1.00")); 
        } else if (totalMovimentacoes <= 20) {
            custo = BigDecimal.valueOf(totalMovimentacoes).multiply(new BigDecimal("0.75")); 
        } else {
            custo = BigDecimal.valueOf(totalMovimentacoes).multiply(new BigDecimal("0.50")); 
        }
        return custo;
    }

    /**
     * Formata a string do relatório de acordo com o layout do PDF.
     */
    private String formatarConteudoRelatorio(String nomeCliente, LocalDate dataCadastro, String endereco, long creditos, long debitos, long total, BigDecimal valorPago, BigDecimal saldoInicial, BigDecimal saldoAtual) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DecimalFormat currencyFormatter = new DecimalFormat("R$ #,##0.00");

        StringBuilder sb = new StringBuilder();
        sb.append("Relatório de saldo do cliente").append("\n"); 
        sb.append("----------------------------------------\n");
        sb.append(String.format("Cliente: %s - Cliente desde: %s%n", nomeCliente, dataCadastro.format(dateFormatter))); 
        sb.append(String.format("Endereço: %s%n", endereco)); 
        sb.append(String.format("Movimentações de crédito: %d%n", creditos)); 
        sb.append(String.format("Movimentações de débito: %d%n", debitos)); 
        sb.append(String.format("Total de movimentações: %d%n", total)); 
        sb.append(String.format("Valor pago pelas movimentações: %s%n", currencyFormatter.format(valorPago))); 
        sb.append(String.format("Saldo inicial: %s%n", currencyFormatter.format(saldoInicial))); 
        sb.append(String.format("Saldo atual: %s%n", currencyFormatter.format(saldoAtual))); 
        sb.append("----------------------------------------\n");

        return sb.toString();
    }

    /**
     * Salva o conteúdo do relatório em um arquivo .txt na pasta 'relatorios'.
     */
    private void salvarRelatorioEmArquivo(String nomeCliente, String conteudo) throws IOException {
        // Cria o diretório 'relatorios' na raiz do projeto se ele não existir
        Path diretorioPath = Paths.get("relatorios");
        if (!Files.exists(diretorioPath)) {
            Files.createDirectories(diretorioPath);
        }

        // Define o nome do arquivo
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nomeArquivo = String.format("Relatorio_%s_%s.txt", nomeCliente.replaceAll("\\s+", "_"), timestamp);
        Path arquivoPath = diretorioPath.resolve(nomeArquivo);

        // Escreve o conteúdo no arquivo
        Files.writeString(arquivoPath, conteudo);
        System.out.println("Relatório salvo em: " + arquivoPath.toAbsolutePath());
    }

    // --- Métodos de Apoio ---
    private String getNomeCliente(Cliente cliente) {
        if (cliente.getTipoPessoa() == TipoPessoa.PF) {
            return pessoaFisicaRepository.findByClienteId(cliente.getId()).map(PessoaFisica::getNome).orElse("N/A");
        } else {
            return pessoaJuridicaRepository.findByClienteId(cliente.getId()).map(PessoaJuridica::getRazaoSocial).orElse("N/A");
        }
    }

    private String formatarEndereco(Endereco e) {
        if (e == null) return "Endereço não cadastrado.";
        return String.format("%s, %s, %s, %s, %s - %s/%s", e.getRua(), e.getNumero(), e.getComplemento(), e.getBairro(), e.getCidade(), e.getUf(), e.getCep());
    }
}