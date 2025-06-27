package com.example.sipame.views;

import com.example.sipame.entitys.Aluno;
import com.example.sipame.entitys.Mensalidade;
import com.example.sipame.entitys.Pagamento;
import com.example.sipame.services.AlunoService;
import com.example.sipame.services.MensalidadeService;
import com.example.sipame.services.PagamentoService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Route(value = "mensalidades", layout = MainView.class)
public class MensalidadeView extends VerticalLayout {

    private final MensalidadeService mensalidadeService;
    private final PagamentoService  pagamentoService;
    private final AlunoService alunoService;

    private Grid<Mensalidade> grid;
    private TextField filterField;
    private Binder<Mensalidade> binder;

    public MensalidadeView(MensalidadeService mensalidadeService, AlunoService alunoService, PagamentoService  pagamentoService) {
        this.mensalidadeService = mensalidadeService;
        this.alunoService = alunoService;
        this.pagamentoService = pagamentoService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        configureGrid();
        createToolbar();

        add(createToolbar(), grid);
        updateGrid();
    }

    private void configureGrid() {
        grid = new Grid<>(Mensalidade.class, false);
        grid.addColumn(m -> m.getAluno().getNome()).setHeader("Aluno");
        grid.addColumn(Mensalidade::getValor).setHeader("Valor");
        grid.addColumn(m -> m.getMesReferencia().toString()).setHeader("Mês Referência");
        grid.addColumn(Mensalidade::getDataVencimento).setHeader("Data Vencimento");
        grid.addColumn(m -> m.isPaga() ? "Sim" : "Não").setHeader("Paga");
        grid.addComponentColumn(this::createAcoesButton).setHeader("Ações");
        grid.setSizeFull();
    }

    private Component createToolbar() {
        filterField = new TextField("Pesquisar por nome");
        filterField.setPlaceholder("Ex: João");
        filterField.setClearButtonVisible(true);
        filterField.addValueChangeListener(e -> updateGrid());

        Button novoBtn = new Button("Nova Mensalidade", e -> openDialogAdd(null));
        novoBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return new HorizontalLayout(filterField, novoBtn);
    }

    private HorizontalLayout createAcoesButton(Mensalidade mensalidade) {
        Button detalhesBtn = new Button("Detalhes", e -> mostrarDetalhes(mensalidade));
        detalhesBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout layout = new HorizontalLayout(detalhesBtn);

        // Só mostra botão de pagamento se ainda não foi paga
        if (!mensalidade.isPaga()) {
            Button pagarBtn = new Button("Pagar", e -> abrirDialogPagamento(mensalidade));
            pagarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            layout.add(pagarBtn);
        } else {
            Span pago = new Span("✔ Pago");
            pago.getStyle().set("color", "green").set("font-weight", "bold");
            layout.add(pago);
        }

        return layout;
    }


    private void abrirDialogPagamento(Mensalidade mensalidade) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Efetuar Pagamento via e-Mola");
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        TextField numeroTelefone = new TextField("Número e-Mola");
        numeroTelefone.setPlaceholder("Ex: 82xxxxxxx");
        numeroTelefone.setWidthFull();

        Button pagarBtn = new Button("Confirmar Pagamento", event -> {
            if (numeroTelefone.isEmpty() || numeroTelefone.getValue().length() < 8) {
                Notification.show("Número inválido").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            // Simula pagamento (futuro: integrar API e-Mola)
            Pagamento pagamento = new Pagamento();
            pagamento.setMensalidade(mensalidade);
            pagamento.setValorPago(mensalidade.getValor());
            pagamento.setDataPagamento(LocalDateTime.now());
            pagamento.setEstado("CONCLUIDO"); // poderia ser "PENDENTE" em caso real
            pagamento.setReferencia("REF-" + UUID.randomUUID().toString().substring(0, 8));

            pagamentoService.save(pagamento);

            // Atualiza mensalidade como paga
            mensalidade.setPaga(true);
            mensalidadeService.update(mensalidade);

            Notification.show("Pagamento efetuado com sucesso!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            dialog.close();
            atualizarGridMensalidade(); // atualiza a grid principal

        });

        pagarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        Button cancelar = new Button("Cancelar", e -> dialog.close());
        cancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout botoes = new HorizontalLayout(pagarBtn, cancelar);

        dialog.add(new VerticalLayout(numeroTelefone, botoes));
        dialog.open();
    }

    private void atualizarGridMensalidade() {
        if (!filterField.isEmpty()) {
            grid.setItems(mensalidadeService.find(filterField.getValue()));
        } else {
            grid.setItems(mensalidadeService.findAll());
        }
    }


	private void mostrarDetalhes(Mensalidade mensalidade) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Detalhes da Mensalidade");

        dialog.add(new Span("Aluno: " + mensalidade.getAluno().getNome()));
        dialog.add(new Span("Mês: " + mensalidade.getMesReferencia()));
        dialog.add(new Span("Valor: " + mensalidade.getValor()));
        dialog.add(new Span("Estado: " + (mensalidade.isPaga() ? "Pago" : "Pendente")));

        Button fechar = new Button("Fechar", e -> dialog.close());
        dialog.add(fechar);
        dialog.open();
    }


	private void openDialogAdd(Mensalidade mensalidadeExistente) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(mensalidadeExistente == null ? "Nova Mensalidade" : "Editar Mensalidade");
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);

        Mensalidade mensalidade = mensalidadeExistente != null ? mensalidadeExistente : new Mensalidade();

        ComboBox<Aluno> alunoCombo = new ComboBox<>("Aluno");
        alunoCombo.setItems(alunoService.findAll());
        alunoCombo.setItemLabelGenerator(Aluno::getNome);

        NumberField valorField = new NumberField("Valor");
        DatePicker mesReferenciaPicker = new DatePicker("Mês Referência");
        DatePicker vencimentoPicker = new DatePicker("Data Vencimento");
        Checkbox pagaCheck = new Checkbox("Paga");

        if (mensalidadeExistente != null) {
            alunoCombo.setValue(mensalidade.getAluno());
            valorField.setValue(mensalidade.getValor());
            mesReferenciaPicker.setValue(LocalDate.of(mensalidade.getMesReferencia().getYear(), mensalidade.getMesReferencia().getMonth(), 1));
            vencimentoPicker.setValue(mensalidade.getDataVencimento());
            pagaCheck.setValue(mensalidade.isPaga());
        }

        binder = new Binder<>(Mensalidade.class);
        binder.forField(alunoCombo).asRequired("Aluno obrigatório").bind(Mensalidade::getAluno, Mensalidade::setAluno);
        binder.forField(valorField).asRequired("Valor obrigatório").bind(Mensalidade::getValor, Mensalidade::setValor);
        binder.forField(vencimentoPicker).asRequired("Data obrigatória").bind(Mensalidade::getDataVencimento, Mensalidade::setDataVencimento);
        binder.forField(pagaCheck).bind(Mensalidade::isPaga, Mensalidade::setPaga);

        VerticalLayout layout = new VerticalLayout(alunoCombo, valorField, mesReferenciaPicker, vencimentoPicker, pagaCheck);

        Button salvar = new Button("Salvar", event -> {
            if (binder.writeBeanIfValid(mensalidade)) {
                // converte LocalDate → YearMonth
                if (mesReferenciaPicker.getValue() != null) {
                    LocalDate refDate = mesReferenciaPicker.getValue();
                    mensalidade.setMesReferencia(YearMonth.of(refDate.getYear(), refDate.getMonth()));
                }
                mensalidadeService.save(mensalidade);
                updateGrid();
                dialog.close();
                Notification.show("Mensalidade salva com sucesso", 3000, Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });

        Button cancelar = new Button("Cancelar", e -> dialog.close());

        HorizontalLayout botoes = new HorizontalLayout(salvar, cancelar);
        dialog.add(layout, botoes);
        dialog.open();
    }

    private void updateGrid() {
        if (filterField.getValue() == null || filterField.getValue().isEmpty()) {
            grid.setItems(mensalidadeService.findAll());
        } else {
            grid.setItems(mensalidadeService.find(filterField.getValue()));
        }
    }
}

