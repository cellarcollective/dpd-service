package co.cellarcollective.tools.chronopostapiemu;

public enum ErrorModel {

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
    E824("DPD-NÚMERO DE VOL.SUPERIOR SOLICITADO");

    private String description;

    ErrorModel(String description) {
        this.description = description;
    }

    public static ErrorModel fromIntCode(int parseInt) {
        return  ErrorModel.valueOf("E" + parseInt);
    }

    public String getDescription() {
        return description;
    }
}
