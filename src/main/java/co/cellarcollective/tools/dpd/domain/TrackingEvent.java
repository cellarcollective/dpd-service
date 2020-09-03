package co.cellarcollective.tools.dpd.domain;

public enum TrackingEvent {

    E800("ENDEREÇO DESCONHECIDO"),
    E801("ENDEREÇO INCORRETO"),
    E802("MUDANÇA DE ENDEREÇO"),
    E803("RECUSOU"),
    E804("FALTA DE PAGAMENTO"),
    E805("INTERIOR"),
    E806("EXPEDIDOR LONGE"),
    E807("CARGA NÃO PRONTA"),
    E808("NADA A COBRAR"),
    E809("FORA DE REGRAS"),
    E810("FALTA DE TEMPO"),
    E811("TEMPO EXCEDIDO"),
    E812("CIRCUITO ERRADO"),
    E813("LOCAL INACESSÍVEL"),
    E814("CONDIÇÕES DO TEMPO"),
    E815("EMBALAGEM POBRE"),
    E816("CONTATO DA PESSOA FALTA"),
    E817("DESPACHANTE NÃO NOTIFICADO"),
    E818("FALTA DE CAPACIDADE"),
    E820("LACK INFORM.ADICIONAL"),
    E821("SAIU PARA TRABALHAR"),
    E822("JÁ RECOLHIDO"),
    E823("NADA A COBRAR C ONTRA ENTREGA"),
    E824("DPD-NÚMERO DE VOL.SUPERIOR SOLICITADO"),
    E819("RECOLHA CANCELADA"),
    EOFD("ENCOMENDA EM TRANSITO"),
    ECOL("ENVIO RECOLHIDO"),
    EPEC("ENTRADA EM ARMAZEM"),
    E706("SMS DE DISTRIBUIÇÂO ENVIADO AO CLIENTE"),
    E001("ENVIO DEPOSITADO EM LOJA DPD"),
    EREC("RECOLHA EFECTUADA"),
    EPOD("ENCOMENDA ENTREGUE");

    private String description;

    TrackingEvent(String description) {
        this.description = description;
    }

    public static TrackingEvent fromCode(String code) {
        return  TrackingEvent.valueOf("E" + code);
    }

    public String getDescription() {
        return description;
    }
}
