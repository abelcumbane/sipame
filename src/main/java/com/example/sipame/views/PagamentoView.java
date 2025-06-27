package com.example.sipame.views;

import com.example.sipame.entitys.Pagamento;
import com.example.sipame.services.PagamentoService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;

@Route(value = "pagamentos", layout = MainView.class)
public class PagamentoView extends VerticalLayout {

    private final PagamentoService pagamentoService;
    private final Grid<Pagamento> grid = new Grid<>(Pagamento.class, false);

    public PagamentoView(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 titulo = new H2("Histórico de Pagamentos");
        titulo.getStyle().set("margin-bottom", "20px");

        configurarGrid();
        add(titulo, grid);
        carregarPagamentos();
    }

    private void configurarGrid() {
        grid.setSizeFull();

        grid.addColumn(pagamento -> pagamento.getMensalidade().getAluno().getNome())
                .setHeader("Aluno");

        grid.addColumn(Pagamento::getValorPago)
                .setHeader("Valor Pago (MZN)");

        grid.addColumn(new LocalDateTimeRenderer<>(
        	    Pagamento::getDataPagamento,
        	    "dd/MM/yyyy HH:mm"
        	)).setHeader("Data do Pagamento");



        grid.addColumn(Pagamento::getEstado)
                .setHeader("Estado");

        grid.addColumn(Pagamento::getReferencia)
                .setHeader("Referência");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void carregarPagamentos() {
        grid.setItems(pagamentoService.findAll());
    }
}
