package com.example.sipame.views;

import java.util.List;
import java.util.Optional;

import com.example.sipame.entitys.Aluno;
import com.example.sipame.services.AlunoService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;


@Route(value = "alunos", layout = MainView.class)
public class AlunoView extends VerticalLayout {
	
	private AlunoService alunoService;
	
	
	
	private Grid<Aluno> grid;
	private TextField filterField;
	private Aluno aluno; 
	
	// new Button("Voltar", e -> UI.getCurrent().navigate(""))
	
	private Binder<Aluno> binder;
	
	public AlunoView (AlunoService alunoService) {
		this.alunoService = alunoService;
        
		setSizeFull();
        setAlignItems(Alignment.START);  
        
        createFieldVariables();
        configuredGrid();
        
        add(createToolbar(),grid);
        loadAluno();	
    }
	
	private void createFieldVariables() {
		this.grid = new Grid<>(Aluno.class);
		this.filterField = new TextField();
		
	}
	
	private void configuredGrid() {
		grid.setSizeFull();
		grid.setColumns("nome","telefone");
		
		grid.addComponentColumn(this::createDetailsButton).setHeader("Ações").setAutoWidth(true);
		
	}
	
	private void showAlunoDetailsDialog(Aluno aluno) {
	    Dialog dialog = new Dialog();
	    dialog.setHeaderTitle("Detalhes do Aluno: ");
	    dialog.setCloseOnOutsideClick(false);
	    dialog.setCloseOnEsc(false);

	    VerticalLayout layout = new VerticalLayout();
	    layout.add(
	        new Span("Código: " + aluno.getId()),
	        new Span("Nome: " + aluno.getNome()),
	        new Span("Telfone: " + aluno.getTelefone())
	    );

	    Button fechar = new Button("Fechar", e -> dialog.close());
	    layout.add(fechar);
	    
	    
	 // Botão Editar
	    Button editar = new Button("Editar", e -> {
	        //dialog.close(); // fecha detalhes
	        openDialogEditAluno(aluno); // chama o dialog de edição
	    });
	    editar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

	 // Botão Apagar com confirmação
	    Button apagar = new Button("Apagar", e -> {
	        dialog.close();
	        Dialog confirmDialog = new Dialog();
	        confirmDialog.setHeaderTitle("Confirmação");

	        confirmDialog.add(new Span("Tem certeza que deseja remover este aluno?"));

	        Button sim = new Button("Sim", ev -> {
	            alunoService.remove(aluno);
	            updateGrid(); // atualiza a grid
	            confirmDialog.close();
	            Notification.show("Aluno removido com sucesso.").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	        });

	        Button nao = new Button("Não", ev -> confirmDialog.close());
	        nao.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

	        HorizontalLayout confirmButtons = new HorizontalLayout(sim, nao);
	        confirmDialog.add(confirmButtons);
	        confirmDialog.open();
	    });
	    apagar.addThemeVariants(ButtonVariant.LUMO_ERROR);

	    
	    // Layout de botões
	    HorizontalLayout buttons = new HorizontalLayout(editar, apagar, fechar);
	    layout.add(buttons);

	    dialog.add(layout);
	    dialog.open();
	}
	
	private Button createDetailsButton(Aluno aluno) {
	    Button detalhesBtn = new Button("Detalhes");
	    detalhesBtn.addClickListener(e -> showAlunoDetailsDialog(aluno));
	    return detalhesBtn;
	}

	
	
	private Component createToolbar() {
		filterField.setPlaceholder("Pesquisar pelo nome");
		filterField.setClearButtonVisible(true);
		filterField.setValueChangeMode(ValueChangeMode.LAZY); 
		filterField.addValueChangeListener(e -> updateAluno());
		
		Button addProdutoButton = new Button(new Icon(VaadinIcon.PLUS));
		Button editProdutoButton = new Button(new Icon(VaadinIcon.EDIT));
		Button removeProdutoButton = new Button(new Icon(VaadinIcon.TRASH));
		
		addProdutoButton.getStyle().set("background-color", "#28a745"); // Verde
		addProdutoButton.getStyle().set("color", "white");
		editProdutoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);  // Cinza
		removeProdutoButton.addThemeVariants(ButtonVariant.LUMO_ERROR);   // Vermelho
		
		addProdutoButton.addClickListener(event ->openDialogAddAluno());
		
		removeProdutoButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("remover_alunos")));
		
		editProdutoButton.setEnabled(false);
		grid.addSelectionListener(event -> {
			editProdutoButton.setEnabled(event.getFirstSelectedItem().isPresent());
		} );
		
		editProdutoButton.addClickListener(e -> {
			Optional<Aluno> selectedProduto = grid.getSelectedItems().stream().findFirst();
			
			selectedProduto.ifPresentOrElse(
					aluno -> openDialogEditAluno(aluno), 
			        () -> Notification.show("Nenhum producto selecionado!")
			    );
		});
		
		return new HorizontalLayout(filterField,addProdutoButton,editProdutoButton,removeProdutoButton);
	}
	
private void openDialogAddAluno() {
		
		Dialog dialog = new Dialog();
		dialog.setWidth("500px");
		dialog.setHeight("300px");
		dialog.setHeaderTitle("Novos Aluno");
		
		// Impede que o usuário feche o dialog clicando fora ou pressionando ESC
	    dialog.setCloseOnOutsideClick(false);
	    dialog.setCloseOnEsc(false);
	    
		Aluno novoAluno = new Aluno();
		TextField nome = new TextField("Nome do Aluno");
		TextField telefone = new TextField("Telefone");

		binder = new Binder<>(Aluno.class);
		binder.forField(nome).asRequired("Nome é obrigatório ").bind(Aluno::getNome, Aluno::setNome);
		binder.forField(telefone).bind(Aluno::getTelefone, Aluno::setTelefone);
		
		
		// Layout em duas colunas
	    VerticalLayout coluna1 = new VerticalLayout(nome, telefone);
	    coluna1.setPadding(false);
	    
	    HorizontalLayout formLayout = new HorizontalLayout(coluna1);
	    formLayout.setWidthFull();
	    formLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

		Button saveButton = new Button("Salvar", event -> {
			if (binder.writeBeanIfValid(novoAluno)) {
				alunoService.save(novoAluno); // Salva no banco de dados
                updateGrid();
                dialog.close();
                Notification notification = Notification.show("Produto salvo com sucesso!");
				notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				notification.setPosition(Position.TOP_CENTER);
            }
		});
		
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
		
		Button cancelButton = new Button("Cancelar", event -> dialog.close());
		cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		
		HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
	    buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
	    buttonLayout.setWidthFull();
		
		dialog.add(new VerticalLayout(formLayout,buttonLayout));
		dialog.open();
		
	}


private void openDialogEditAluno(Aluno aluno) {

	Dialog dialog = new Dialog();
	dialog.setWidth("500px");
	dialog.setHeight("300px");
    dialog.setHeaderTitle("Editar Aluno");
    
 // Impede que o usuário feche o dialog clicando fora ou pressionando ESC
    dialog.setCloseOnOutsideClick(false);
    dialog.setCloseOnEsc(false);

	TextField nome = new TextField("Nome do Aluno");
	TextField telefone = new TextField("Telefone");

	nome.setValue(aluno.getNome()!= null ? aluno.getNome(): ""); // Preenche com o nome atual
	telefone.setValue(aluno.getTelefone()!= null ? aluno.getTelefone(): "");
	

    
	binder = new Binder<>(Aluno.class);
	binder.forField(nome).asRequired("Nome é obrigatório ").bind(Aluno::getNome, Aluno::setNome);
	binder.forField(telefone).bind(Aluno::getTelefone, Aluno::setTelefone);
	
    binder.readBean(aluno); // Preenche os campos com os dados atuais
    
	// Layout em duas colunas
    VerticalLayout coluna1 = new VerticalLayout(nome, telefone);
    coluna1.setPadding(false);

    HorizontalLayout formLayout = new HorizontalLayout(coluna1);
    formLayout.setWidthFull();
    formLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

    Button saveButton = new Button("Salvar", event -> {
        if (binder.writeBeanIfValid(aluno)) { 
        	alunoService.update(aluno); // Atualiza no banco de dados
            updateGrid();
            dialog.close();
            Notification notification = Notification.show("Aluno salvo com sucesso!");
			notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			notification.setPosition(Position.TOP_CENTER);
        }
    });
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

    Button cancelButton = new Button("Cancelar", event -> dialog.close());
    cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

    HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
    buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    buttonLayout.setWidthFull();

    dialog.add(new VerticalLayout(formLayout, buttonLayout));
    dialog.open(); 
	
}


private void updateGrid() {
    List<Aluno> aluno = alunoService.findAll();
    grid.setItems(aluno);
}

private void loadAluno() { 
	grid.setItems(alunoService.findAll());
	
}

private void updateAluno() {
	grid.setItems(alunoService.find(filterField.getValue()));
}			

	
}

