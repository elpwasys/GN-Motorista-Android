package br.com.wasys.gn.motorista.services;

/**
 * Created by fernandamoncores on 4/22/16.
 */
public class TransportesConfirmadosData {

    private String id;
    private String dataInicial;
    private String tipo;
    private String tipoCarro;
    private String distancia;
    private String valorTotal;
    private String origem;
    private String destino;
    private String situacao;
    private String snapshot;

    public TransportesConfirmadosData(String id, String dataInicial, String tipo, String tipoCarro,String distancia, String valorTotal, String origem, String destino,String situacao)
    {
        setId(id);
        setDataInicial(dataInicial);
        setTipo(tipo);
        setTipoCarro(tipoCarro);
        setDistancia(distancia);
        setOrigem(origem);
        setDestino(destino);
        setSituacao(situacao);
        setValorTotal(valorTotal);
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoCarro() {
        return tipoCarro;
    }

    public void setTipoCarro(String tipoCarro) {
        this.tipoCarro = tipoCarro;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }
}
